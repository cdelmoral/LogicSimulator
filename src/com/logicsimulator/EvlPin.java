package com.logicsimulator;

class EvlPin {
	private String name;
	private int busMSB, busLSB;

	EvlPin(String name, int busMSB, int busLSB) {
		this.name = name;
		this.busMSB = busMSB;
		this.busLSB = busLSB;
	}

	void setBusMSB(int busMSB) {
		this.busMSB = busMSB;
	}

	void setBusLSB(int busLSB) {
		this.busLSB = busLSB;
	}

	int getBusMSB() {
		return busMSB;
	}

	int getBusLSB() {
		return busLSB;
	}

	String getName() {
		return name;
	}
}