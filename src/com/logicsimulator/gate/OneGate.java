package com.logicsimulator.gate;

import com.logicsimulator.net.Net;

public class OneGate extends Gate {

	OneGate(String name) {
		super("one", name, new MultBusOutNoIn());
	}

	@Override
	public Gate copy(String name) {
		return new OneGate(name);
	}

	@Override
	public Net.NetState computeState(Net net) {
		return Net.NetState.HIGH;
	}

}
