package com.logicsimulator.gate;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.logicsimulator.evl.token.EvlToken;
import com.logicsimulator.evl.token.EvlTokenFile;
import com.logicsimulator.net.Net;
import com.logicsimulator.net.Net.NetState;

public class LookupGate extends Gate {

	private long maxValueWord, nWords;
	private boolean initialized;
	private Map<Long, Long> memValues;
	private Map<String, Net.NetState> lastValues;
	private long lastReqAdd;

	public LookupGate(String name) {
		super("lut", name, new OneBusOutOneBusIn());
		initialized = false;
		memValues = new HashMap<Long, Long>();
		lastValues = new HashMap<String, Net.NetState>();
	}

	@Override
	public Gate copy(String name) {
		return new LookupGate(name);
	}

	@Override
	public NetState computeState(Net net) {
		if (!initialized) {
			initialize();
			initialized = true;
		}
		long address = getAddress();
		if (address != lastReqAdd) {
			lastReqAdd = address;
			long memValue = memValues.get(address);
			lastValues.clear();
			for (Net n : pins.get(0)) {
				if (memValue % 2 == 1) {
					lastValues.put(n.getName(), Net.NetState.HIGH);
					memValue = (memValue - 1) / 2;
				} else {
					lastValues.put(n.getName(), Net.NetState.LOW);
					memValue = memValue / 2;
				}
			}
		}
		return lastValues.get(net.getName());
	}

	private long getAddress() {
		long address = 0;
		int sign = 0;
		for (Net n : pins.get(1)) {
			if (n.getState() == Net.NetState.HIGH) {
				address += Math.pow(2, sign);
			}
			sign++;
		}
		return address;
	}

	private void initialize() {

		String fileName = "test_cases/" + name + ".rom";
		BufferedReader input;
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

		Iterator<EvlToken> it = evlTokens.iterator();
		EvlToken token;

		if (it.hasNext()) {
			token = it.next();
			int wordWidth = Integer.parseInt(token.getName());
			if (wordWidth != pins.get(0).getNumberOfNets()) {
				throw new RuntimeException();
			} else {
				maxValueWord = (long) Math.pow(2, wordWidth);
			}
		} else {
			throw new RuntimeException();
		}

		if (it.hasNext()) {
			token = it.next();
			int addWidth = Integer.parseInt(token.getName());
			if (addWidth != pins.get(1).getNumberOfNets()) {
				throw new RuntimeException();
			} else {
				nWords = (long) Math.pow(2, addWidth);
				lastReqAdd = nWords + 1;
			}
		} else {
			throw new RuntimeException();
		}

		long i = 0;
		while (it.hasNext()) {
			token = it.next();
			long value = Long.parseLong(token.getName(), 16);
			if (i < nWords && value < maxValueWord) {
				memValues.put(i++, value);
			} else {
				throw new RuntimeException();
			}
		}

	}
}
