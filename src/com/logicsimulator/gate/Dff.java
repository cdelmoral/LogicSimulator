package com.logicsimulator.gate;

import com.logicsimulator.net.Net;

public class Dff extends SeqGate {

	private Net.NetState lastState;

	Dff(String name) {
		super("dff", name, new OneOutOneIn());
		lastState = Net.NetState.LOW;
	}

	@Override
	public Gate copy(String name) {
		return new Dff(name);
	}

	@Override
	public Net.NetState computeState(Net net) {
		return lastState;
	}

	@Override
	public
	void computeNextState() {
		lastState = pins.get(1).getState();
	}

}
