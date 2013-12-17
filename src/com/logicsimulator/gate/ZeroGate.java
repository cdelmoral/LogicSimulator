package com.logicsimulator.gate;

import com.logicsimulator.net.Net;

public class ZeroGate extends Gate {

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
