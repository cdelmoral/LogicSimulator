package com.logicsimulator.gate;

import com.logicsimulator.net.Net;

public class NotGate extends Gate {

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
