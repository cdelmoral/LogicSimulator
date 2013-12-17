package com.logicsimulator.gate;

import com.logicsimulator.net.Net;
import com.logicsimulator.pin.Pin;

public class AndGate extends Gate {

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

