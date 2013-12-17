package com.logicsimulator.gate;

public abstract class SeqGate extends Gate {

	public SeqGate(String type, String name, SemanticsType semantics) {
		super(type, name, semantics);
	}

	public abstract void computeNextState();

}