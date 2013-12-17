package com.logicsimulator.gate;

import com.logicsimulator.net.Net;
import com.logicsimulator.net.Net.NetState;

public class AssignGate extends Gate {

	public AssignGate(String name) {
		super("assign", name, new OneBusOutOneBusIn());
	}

	@Override
	public Gate copy(String name) {
		return new AssignGate(name);
	}

	@Override
	public NetState computeState(Net netName) {
		int index = pins.get(0).getIndexOf(netName);
		return pins.get(1).getNetState(index);
	}

}
