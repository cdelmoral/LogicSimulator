package com.logicsimulator.net;

import java.util.LinkedList;
import java.util.List;

import com.logicsimulator.gate.Gate;
import com.logicsimulator.pin.Pin;

public class Net {

	public static enum NetState {
		HIGH, LOW, UNKNOWN, Z;
	}

	private String name;
	private List<Pin> pins;
	private NetState state;
	private List<Gate> drivers;

	public Net(String name) {
		this.name = name;
		pins = new LinkedList<Pin>();
		drivers = new LinkedList<Gate>();
		state = NetState.UNKNOWN;
	}

	public String getName() {
		return name;
	}

	public boolean isUnknown() {
		if (state == NetState.UNKNOWN) {
			return true;
		} else {
			return false;
		}
	}

	public boolean isHigh() {
		if (state == NetState.HIGH) {
			return true;
		} else {
			return false;
		}
	}

	public boolean isLow() {
		if (state == NetState.LOW) {
			return true;
		} else {
			return false;
		}
	}

	public void addPin(Pin pin) {
		pins.add(pin);
	}

	@Override
	public String toString() {
		String net = "net " + name + " " + pins.size();
		for (Pin pin : pins) {
			net = net + '\n' + "pin " + pin.getType() + " " + pin.getGateName()
					+ " " + pin.getPos();
		}
		return net;
	}

	public NetState getState() {
		if (state == NetState.UNKNOWN) {
			boolean found = false;
			for (Gate gate : drivers) {
				NetState gateState = gate.computeState(this);
				if (gateState != NetState.Z) {
					if (found == true) {
						throw new RuntimeException();
					}
					found = true;
					state = gateState;
				}
			}
			if (!found) {
				throw new RuntimeException();
			}
		}
		return state;
	}

	public void markAsUnknown() {
		state = NetState.UNKNOWN;
	}

	public void setDriver(Gate gate) {
		drivers.add(gate);
	}

}
