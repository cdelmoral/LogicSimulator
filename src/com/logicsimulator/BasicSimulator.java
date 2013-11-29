package com.logicsimulator;

public class BasicSimulator extends Simulator {

	public BasicSimulator(Netlist nl) {
		super(nl);
	}

	@Override
	public void simulate() {
		int maxTransitions = 1000;
		for (int transition = 0; transition < maxTransitions; transition++) {
			nl.computeAllNets();
			nl.computeOutput();
			nl.nextState();
			nl.markNetsAsUnknown();
		}
	}

}
