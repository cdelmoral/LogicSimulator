package com.logicsimulator;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.logicsimulator.Net.NetState;

abstract class Gate {

	abstract public Gate copy(String name);

	abstract public Net.NetState computeState(Net net);

	private SemanticsType semantics;
	private String type;
	protected String name;
	protected List<Pin> pins;

	public Gate(String type, String name, SemanticsType semantics) {
		this.type = type;
		this.name = name;
		this.semantics = semantics;
		pins = new LinkedList<Pin>();
	}

	public void checkSemantics() {
		semantics.checkSemantics(pins);
	}

	public void addPin(Pin pin) {
		if (pin.getPos() != pins.size()) {
			throw new RuntimeException();
		} else {
			pins.add(pin);
		}
	}

	public List<Pin> getPins() {
		return pins;
	}

	public String getType() {
		return type;
	}

	public int getNumberOfPins() {
		return pins.size();
	}

	public String getName() {
		return name;
	}

	@Override
	public String toString() {
		String gate = "gate " + type + " " + name + " " + pins.size();
		for (Pin pin : pins) {
			gate = gate + "\npin " + pin.getNumberOfNets();
			for (Net net : pin) {
				gate = gate + " " + net.getName();
			}
		}
		return gate;
	}

}

abstract class SeqGate extends Gate {

	public SeqGate(String type, String name, SemanticsType semantics) {
		super(type, name, semantics);
	}

	abstract void computeNextState();

}

class AndGate extends Gate {

	AndGate(String name) {
		super("and", name, new OneOutMultIn());
	}

	@Override
	public Gate copy(String name) {
		return new AndGate(name);
	}

	@Override
	public Net.NetState computeState(Net net) {
		for (Pin pin : pins) {
			if (pin.getIO() == Pin.IO.INPUT) {
				Net.NetState state = pin.getState();
				if (state == Net.NetState.LOW) {
					return Net.NetState.LOW;
				}
			}
		}
		return Net.NetState.HIGH;
	}
}

class OrGate extends Gate {

	OrGate(String name) {
		super("or", name, new OneOutMultIn());
	}

	@Override
	public Gate copy(String name) {
		return new OrGate(name);
	}

	@Override
	public Net.NetState computeState(Net net) {
		for (Pin pin : pins) {
			if (pin.getIO() == Pin.IO.INPUT) {
				Net.NetState state = pin.getState();
				if (state == Net.NetState.HIGH) {
					return Net.NetState.HIGH;
				}
			}
		}
		return Net.NetState.LOW;
	}

}

class XorGate extends Gate {

	XorGate(String name) {
		super("xor", name, new OneOutMultIn());
	}

	@Override
	public Gate copy(String name) {
		return new XorGate(name);
	}

	@Override
	public Net.NetState computeState(Net net) {
		Net.NetState state = Net.NetState.LOW;
		for (Pin pin : pins) {
			if (pin.getIO() == Pin.IO.INPUT) {
				if (pin.getState() == Net.NetState.HIGH) {
					if (state == Net.NetState.HIGH) {
						state = Net.NetState.LOW;
					} else {
						state = Net.NetState.HIGH;
					}
				}
			}
		}
		return state;
	}

}

class BufGate extends Gate {

	BufGate(String name) {
		super("buf", name, new OneOutOneIn());
	}

	@Override
	public Gate copy(String name) {
		return new BufGate(name);
	}

	@Override
	public Net.NetState computeState(Net net) {
		return pins.get(1).getState();
	}

}

class Dff extends SeqGate {

	private Net.NetState lastState;

	Dff(String name) {
		super("dff", name, new OneOutOneIn());
		lastState = Net.NetState.LOW;
	}

	@Override
	public Gate copy(String name) {
		return new Dff(name);
	}

	@Override
	public Net.NetState computeState(Net net) {
		return lastState;
	}

	@Override
	void computeNextState() {
		lastState = pins.get(1).getState();
	}

}

class NotGate extends Gate {

	NotGate(String name) {
		super("not", name, new OneOutOneIn());
	}

	@Override
	public Gate copy(String name) {
		return new NotGate(name);
	}

	@Override
	public Net.NetState computeState(Net net) {
		if (pins.get(1).getState() == Net.NetState.HIGH) {
			return Net.NetState.LOW;
		} else {
			return Net.NetState.HIGH;
		}
	}

}

class InputGate extends Gate {

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

class OneGate extends Gate {

	OneGate(String name) {
		super("one", name, new MultBusOutNoIn());
	}

	@Override
	public Gate copy(String name) {
		return new OneGate(name);
	}

	@Override
	public Net.NetState computeState(Net net) {
		return Net.NetState.HIGH;
	}

}

class ZeroGate extends Gate {

	ZeroGate(String name) {
		super("zero", name, new MultBusOutNoIn());
	}

	@Override
	public Gate copy(String name) {
		return new ZeroGate(name);
	}

	@Override
	public Net.NetState computeState(Net net) {
		return Net.NetState.LOW;
	}

}

class OutputGate extends Gate {

	private boolean initialized;
	private List<Net> nets;
	private PrintStream out;

	OutputGate(String name) {
		super("output", name, new NoOutMultBusIn());
		initialized = false;
		nets = new LinkedList<Net>();
	}

	@Override
	public Gate copy(String name) {
		return new OutputGate(name);
	}

	@Override
	public Net.NetState computeState(Net net) {
		if (!initialized) {
			String fileName = "test_cases/" + name;
			File file = new File(fileName);
			file.delete();
			try {
				out = new PrintStream(file);
			} catch (FileNotFoundException e) {
				throw new RuntimeException();
			}
			out.println(pins.size());
			for (Pin pin : pins) {
				out.print("pin " + pin.getNumberOfNets());
				for (Net n : pin) {
					out.print(" " + n.getName());
					nets.add(n);
				}
				out.println();
			}
			initialized = true;
		}

		Iterator<Pin> it = pins.iterator();
		while (it.hasNext()) {
			Pin pin = it.next();
			long value = 0;
			int sign = 0;
			for (Net n : pin) {
				if (n.isHigh()) {
					value += Math.pow(2, sign++);
				} else {
					sign++;
				}
			}
			int pad = (int) Math.ceil((double) pin.getNumberOfNets() / 4);
			out.print(String.format("%0" + pad + "x", value).toUpperCase());
			if (it.hasNext())
				out.print(" ");
		}

		out.println();

		return null;
	}

}

class TristateGate extends Gate {

	TristateGate(String name) {
		super("tris", name, new OneOutTwoIn());
	}

	@Override
	public Gate copy(String name) {
		return new TristateGate(name);
	}

	@Override
	public NetState computeState(Net net) {
		if (pins.get(2).getState() == Net.NetState.HIGH) {
			return pins.get(1).getState();
		} else {
			return Net.NetState.Z;
		}
	}

}

class LookupGate extends Gate {

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

class AssignGate extends Gate {

	public AssignGate(String name) {
		super("assign", name, new OneBusOutOneBusIn());
	}

	@Override
	public Gate copy(String name) {
		return new AssignGate(name);
	}

	@Override
	public NetState computeState(Net netName) {
		int index = pins.get(0).getIndexOf(netName);
		return pins.get(1).getNetState(index);
	}

}