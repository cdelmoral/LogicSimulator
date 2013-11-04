package com.logicsimulator;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class Pin implements Iterable<Net> {

	public enum IO {
		INPUT, OUTPUT;
	}

	private Gate gate;
	private String name;
	private int position;
	private List<Net> nets;
	private IO io;

	public Pin(Gate gate, String name, int position) {
		this.gate = gate;
		this.name = name;
		this.position = position;
		nets = new LinkedList<Net>();
	}

	public Net.NetState getNetState(int index) {
		if (index < nets.size()) {
			return nets.get(index).getState();
		} else {
			throw new RuntimeException();
		}
	}

	public Net.NetState getState() {
		if (nets.size() == 1) {
			return nets.get(0).getState();
		} else {
			throw new RuntimeException();
		}
	}

	public String getType() {
		return gate.getType();
	}

	public String getName() {
		return name;
	}

	public int getPos() {
		return position;
	}

	public int getNumberOfNets() {
		return nets.size();
	}

	public void addNet(Net net) {
		nets.add(net);
	}

	public String getGateName() {
		return gate.getName();
	}

	@Override
	public Iterator<Net> iterator() {
		return nets.iterator();
	}

	public int netsSize() {
		return nets.size();
	}

	public void setAsOutput() {
		io = IO.OUTPUT;
		for (Net net : nets) {
			net.setDriver(gate);
		}
	}

	public void setAsInput() {
		io = IO.INPUT;
	}

	public IO getIO() {
		return io;
	}

	public int getIndexOf(Net net) {
		return nets.indexOf(net);
	}
}