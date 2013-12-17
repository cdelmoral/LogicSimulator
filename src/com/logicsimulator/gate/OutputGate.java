package com.logicsimulator.gate;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import com.logicsimulator.net.Net;
import com.logicsimulator.pin.Pin;

public class OutputGate extends Gate {

	private boolean initialized;
	private List<Net> nets;
	private PrintStream out;

	OutputGate(String name) {
		super("output", name, new NoOutMultBusIn());
		initialized = false;
		nets = new LinkedList<Net>();
	}

	@Override
	public Gate copy(String name) {
		return new OutputGate(name);
	}

	@Override
	public Net.NetState computeState(Net net) {
		if (!initialized) {
			String fileName = "test_cases/" + name;
			File file = new File(fileName);
			file.delete();
			try {
				out = new PrintStream(file);
			} catch (FileNotFoundException e) {
				throw new RuntimeException();
			}
			out.println(pins.size());
			for (Pin pin : pins) {
				out.print("pin " + pin.getNumberOfNets());
				for (Net n : pin) {
					out.print(" " + n.getName());
					nets.add(n);
				}
				out.println();
			}
			initialized = true;
		}

		Iterator<Pin> it = pins.iterator();
		while (it.hasNext()) {
			Pin pin = it.next();
			long value = 0;
			int sign = 0;
			for (Net n : pin) {
				if (n.isHigh()) {
					value += Math.pow(2, sign++);
				} else {
					sign++;
				}
			}
			int pad = (int) Math.ceil((double) pin.getNumberOfNets() / 4);
			out.print(String.format("%0" + pad + "x", value).toUpperCase());
			if (it.hasNext())
				out.print(" ");
		}

		out.println();

		return null;
	}

}
