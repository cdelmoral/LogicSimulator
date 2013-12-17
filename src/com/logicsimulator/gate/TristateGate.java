package com.logicsimulator.gate;

import com.logicsimulator.net.Net;
import com.logicsimulator.net.Net.NetState;

public class TristateGate extends Gate {

	TristateGate(String name) {
		super("tris", name, new OneOutTwoIn());
	}

	@Override
	public Gate copy(String name) {
		return new TristateGate(name);
	}

	@Override
	public NetState computeState(Net net) {
		if (pins.get(2).getState() == Net.NetState.HIGH) {
			return pins.get(1).getState();
		} else {
			return Net.NetState.Z;
		}
	}

}
