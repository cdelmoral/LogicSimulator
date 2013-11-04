package com.logicsimulator;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public class Netlist {
	private Map<String, Net> nets;
	private Set<Gate> outputGates;
	private List<Gate> gates;
	private List<SeqGate> seqGates;

	Netlist(Map<String, Net> nets, List<Gate> gates, List<SeqGate> seqGates,
			Set<Gate> outputGates) {
		this.nets = nets;
		this.gates = gates;
		this.seqGates = seqGates;
		for (Gate gate : outputGates) {
			assert (gate.getType().equals("output"));
		}
		this.outputGates = outputGates;
	}

	public void storeNetlistToFile(String fileName) {
		try {
			fileName = fileName + ".netlist";
			File file = new File(fileName);
			file.delete();
			PrintStream out = new PrintStream(file);
			out.println(this);
			out.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	void computeOutput() {
		for (Gate gate : outputGates) {
			gate.computeState(null);
		}
	}

	@Override
	public String toString() {
		String netlist = nets.size() + " " + gates.size();
		nets.values();
		for (Net net : (new TreeMap<String, Net>(nets)).values()) {
			netlist = netlist + "\n" + net;
		}
		for (Gate gate : gates) {
			netlist = netlist + "\n" + gate;
		}
		return netlist;
	}

	public void computeAllNets() {
		for (Net net : nets.values()) {
			net.getState();
		}
	}

	public void markNetsAsUnknown() {
		for (Net net : nets.values()) {
			net.markAsUnknown();
		}
	}

	public void nextState() {
		for (SeqGate seqGate : seqGates) {
			seqGate.computeNextState();
		}
	}

	public void checkOutputs() {
		for (Gate gate : outputGates) {
			GoldenComparator.compare("test_cases/" + gate.name);
		}
	}
}
