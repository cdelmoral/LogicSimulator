package com.logicsimulator.gate;

import java.util.LinkedList;
import java.util.List;

import com.logicsimulator.net.Net;
import com.logicsimulator.pin.Pin;

public abstract class Gate {

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
