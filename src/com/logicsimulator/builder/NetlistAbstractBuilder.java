package com.logicsimulator.builder;

import com.logicsimulator.gate.GatePrototypes;
import com.logicsimulator.netlist.Netlist;

abstract public class NetlistAbstractBuilder {
	protected GatePrototypes gatePrototypes;

	public NetlistAbstractBuilder(GatePrototypes gatePrototypes) {
		this.gatePrototypes = gatePrototypes;
	}

	abstract public void addNet(String name);

	abstract public int addGate(String name, String type);

	abstract public void appendPin(int gateId, String name);

	abstract public Netlist getNetlist();

	abstract public void appendPinBus(int gateId, String name, int busMSB,
			int busLSB);
}