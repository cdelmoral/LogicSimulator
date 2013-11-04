package com.logicsimulator;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class NetlistBuilder extends NetlistAbstractBuilder {
	protected List<Gate> gates;
	private List<SeqGate> seqGates;
	private Set<Gate> outputGates;
	protected Map<String, Net> nets;

	public NetlistBuilder(GatePrototypes gatesPrototypes) {
		super(gatesPrototypes);
		nets = new HashMap<String, Net>();
		gates = new LinkedList<Gate>();
		seqGates = new LinkedList<SeqGate>();
		outputGates = new HashSet<Gate>();
	}

	@Override
	public void addNet(String name) {
		if (nets.containsKey(name)) {
			throw new RuntimeException();
		} else {
			nets.put(name, new Net(name));
		}
	}

	@Override
	public int addGate(String name, String type) {
		if (!gatePrototypes.isLoaded(type)) {
			throw new RuntimeException("Gate " + type + " not loaded.");
		} else {
			int gateId = gates.size();
			gates.add(gatePrototypes.getPrototype(type).copy(name));
			return gateId;
		}
	}

	@Override
	public void appendPin(int gateId, String name) {
		if (gateId < gates.size()) {
			Gate gate = gates.get(gateId);
			Pin pin = new Pin(gate, name, gate.getNumberOfPins());
			gate.addPin(pin);
			if (nets.containsKey(name)) {
				Net net = nets.get(name);
				pin.addNet(net);
				net.addPin(pin);
			} else {
				throw new RuntimeException();
			}
		} else {
			throw new RuntimeException();
		}
	}

	@Override
	public void appendPinBus(int gateId, String name, int busMSB, int busLSB) {
		if (gateId < gates.size()) {
			Gate gate = gates.get(gateId);
			Pin pin = new Pin(gate, name, gate.getNumberOfPins());
			gate.addPin(pin);
			for (int i = busLSB; i <= busMSB; i++) {
				String netName = name + "[" + i + "]";
				if (nets.containsKey(netName)) {
					Net net = nets.get(netName);
					pin.addNet(net);
					net.addPin(pin);
				} else {
					throw new RuntimeException();
				}
			}
		} else {
			throw new RuntimeException();
		}
	}

	@Override
	public Netlist getNetlist() {
		for (Gate gate : gates) {
			gate.checkSemantics();
			if (gate.getType().equals("output")) {
				outputGates.add(gate);
			} else if (gate instanceof SeqGate) {
				seqGates.add((SeqGate) gate);
			}
		}
		return new Netlist(nets, gates, seqGates, outputGates);
	}

}
