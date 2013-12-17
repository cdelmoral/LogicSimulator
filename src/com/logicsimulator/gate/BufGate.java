package com.logicsimulator.gate;

import com.logicsimulator.net.Net;

public class BufGate extends Gate {

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
