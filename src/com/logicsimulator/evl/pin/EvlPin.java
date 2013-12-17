package com.logicsimulator.evl.pin;

public class EvlPin {
	private String name;
	private int busMSB, busLSB;

	public EvlPin(String name, int busMSB, int busLSB) {
		this.name = name;
		this.busMSB = busMSB;
		this.busLSB = busLSB;
	}

	public void setBusMSB(int busMSB) {
		this.busMSB = busMSB;
	}

	public void setBusLSB(int busLSB) {
		this.busLSB = busLSB;
	}

	public int getBusMSB() {
		return busMSB;
	}

	public int getBusLSB() {
		return busLSB;
	}

	public String getName() {
		return name;
	}
}