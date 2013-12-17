package com.logicsimulator.gate;

import com.logicsimulator.net.Net;
import com.logicsimulator.pin.Pin;

public class XorGate extends Gate {

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
