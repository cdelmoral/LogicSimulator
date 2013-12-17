package com.logicsimulator.gate;

import java.util.HashMap;
import java.util.Map;

public class GatePrototypes {
	private Map<String, Gate> gates;
	boolean standardGatesLoaded;

	public GatePrototypes(boolean loadStandard) {
		gates = new HashMap<String, Gate>();
		if (loadStandard) {
			loadStandardGates();
			standardGatesLoaded = true;
		} else {
			standardGatesLoaded = false;
		}
	}

	public GatePrototypes() {
		this(false);
	}

	public void loadStandardGates() {
		if (standardGatesLoaded) {
			throw new RuntimeException();
		} else {

			String name = "prototype";

			Gate andGate = new AndGate(name);
			Gate orGate = new OrGate(name);
			Gate xorGate = new XorGate(name);
			Gate bufGate = new BufGate(name);
			Gate flipFlop = new Dff(name);
			Gate notGate = new NotGate(name);
			Gate inputGate = new InputGate(name);
			Gate oneGate = new OneGate(name);
			Gate zeroGate = new ZeroGate(name);
			Gate outputGate = new OutputGate(name);
			Gate tristateGate = new TristateGate(name);
			Gate lookupGate = new LookupGate(name);
			Gate assignGate = new AssignGate(name);

			store(andGate);
			store(orGate);
			store(xorGate);
			store(bufGate);
			store(flipFlop);
			store(notGate);
			store(inputGate);
			store(oneGate);
			store(zeroGate);
			store(outputGate);
			store(tristateGate);
			store(lookupGate);
			store(assignGate);

		}
	}

	public void store(Gate gate) {
		if (gates.containsKey(gate.getType())) {
			throw new RuntimeException();
		} else {
			gates.put(gate.getType(), gate);
		}
	}

	public boolean isLoaded(String gateType) {
		return gates.containsKey(gateType);
	}

	public Gate getPrototype(String gateType) {
		if (!gates.containsKey(gateType)) {
			throw new RuntimeException();
		} else {
			return gates.get(gateType);
		}
	}
}
