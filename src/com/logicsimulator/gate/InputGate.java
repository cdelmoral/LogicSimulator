package com.logicsimulator.gate;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.logicsimulator.evl.token.EvlToken;
import com.logicsimulator.evl.token.EvlTokenFile;
import com.logicsimulator.net.Net;
import com.logicsimulator.pin.Pin;

public class InputGate extends Gate {

	private BufferedReader input;
	private Map<String, Net.NetState> values;
	private List<String> nets;
	private int nPins;
	private int[] pinWidth;
	private Iterator<EvlToken> it;
	private int currentTransition, transitions;
	private boolean initialized;
	private Set<String> alreadyAsked;

	InputGate(String name) {
		super("input", name, new MultBusOutNoIn());
		currentTransition = 0;
		transitions = 0;
		initialized = false;
		values = new HashMap<String, Net.NetState>();
		nets = new LinkedList<String>();
		alreadyAsked = new HashSet<String>();
	}

	private Iterator<EvlToken> openHexFile(String fileName) {

		try {
			input = new BufferedReader(new FileReader(fileName));
		} catch (FileNotFoundException e) {
			throw new RuntimeException();
		}

		List<EvlToken> evlTokens = new LinkedList<EvlToken>();
		String line;
		int lineNo;

		try {
			for (lineNo = 1, line = input.readLine(); line != null; lineNo++, line = input
					.readLine()) {
				if (!EvlTokenFile.extractHexTokensFromLine(line, lineNo++,
						evlTokens)) {
					input.close();
					throw new RuntimeException();
				}
			}
			input.close();
		} catch (IOException e) {
			throw new RuntimeException();
		}

		return evlTokens.iterator();

	}

	private void readFirstLine() {
		if (it.hasNext()) {
			EvlToken token = it.next();
			if (token.getType() == EvlToken.TokenType.NUMBER) {
				nPins = Integer.parseInt(token.getName());
				pinWidth = new int[nPins];
			} else {
				throw new RuntimeException();
			}
		} else {
			throw new RuntimeException();
		}
		for (int i = 0; i < nPins; i++) {
			if (it.hasNext()) {
				pinWidth[i] = Integer.parseInt(it.next().getName());
			} else {
				throw new RuntimeException();
			}
		}
	}

	@Override
	public Gate copy(String name) {
		return new InputGate(name);
	}

	@Override
	public Net.NetState computeState(Net net) {
		if (!initialized) {
			initialized = true;
			initialize();
		}
		if (alreadyAsked.contains(net.getName())) {
			currentTransition++;
			alreadyAsked.clear();
		}
		if (it.hasNext() && (transitions == currentTransition)) {
			initialized = true;
			alreadyAsked.clear();
			transitions = Integer.parseInt(it.next().getName());
			currentTransition = 0;
			Iterator<String> itN = nets.iterator();
			for (int i = 0; i < nPins; i++) {
				if (it.hasNext()) {
					int value = Integer.parseInt(it.next().getName(), 16);
					for (int j = 0; j < pinWidth[i]; j++) {
						if (itN.hasNext()) {
							String n = itN.next();
							if (value % 2 == 1) {
								value = (value - 1) / 2;
								values.put(n, Net.NetState.HIGH);
							} else {
								value = value / 2;
								values.put(n, Net.NetState.LOW);
							}
						} else {
							throw new RuntimeException();
						}
					}
				} else {
					throw new RuntimeException();
				}
			}
		}
		alreadyAsked.add(net.getName());
		return values.get(net.getName());
	}

	private void initialize() {
		it = openHexFile("test_cases/" + name);
		readFirstLine();
		for (Pin pin : pins) {
			for (Net net : pin) {
				values.put(net.getName(), Net.NetState.UNKNOWN);
				nets.add(net.getName());
			}
		}
	}

}
