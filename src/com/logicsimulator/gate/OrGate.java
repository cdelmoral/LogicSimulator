package com.logicsimulator.gate;

import com.logicsimulator.net.Net;
import com.logicsimulator.pin.Pin;

public class OrGate extends Gate {

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
