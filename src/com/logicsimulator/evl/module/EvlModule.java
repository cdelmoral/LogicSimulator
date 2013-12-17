package com.logicsimulator.evl.module;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

import com.logicsimulator.evl.assign.EvlAssignFSM;
import com.logicsimulator.evl.component.EvlComponent;
import com.logicsimulator.evl.component.EvlComponentFSM;
import com.logicsimulator.evl.pin.EvlPin;
import com.logicsimulator.evl.statement.EvlStatement;
import com.logicsimulator.evl.wire.EvlWireFSM;

public class EvlModule {

	private String name, fileName;
	private Map<String, Integer> wires;
	private List<EvlComponent> components;

	public EvlModule(String name, String fileName) {
		this.name = name;
		this.fileName = fileName;
		wires = new HashMap<String, Integer>();
		components = new LinkedList<EvlComponent>();
	}

	String getName() {
		return name;
	}

	void extractWiresFromStat(EvlStatement stat) {
		EvlWireFSM wireFSM = new EvlWireFSM(wires, stat);
		wireFSM.extractWires();
	}

	void extractComponentsFromStat(EvlStatement stat) {
		EvlComponentFSM componentFSM = new EvlComponentFSM(components, stat);
		componentFSM.extractComponents();
	}

	void extractAssignsFromStat(EvlStatement stat) {
		EvlAssignFSM assignFSM = new EvlAssignFSM(components, stat);
		assignFSM.extractAssigns();
	}

	void correctPins() {
		for (ListIterator<EvlComponent> itc = components.listIterator(); itc
				.hasNext();) {
			EvlComponent comp = itc.next();
			for (ListIterator<EvlPin> itp = comp.pins.listIterator(); itp
					.hasNext();) {
				EvlPin pin = itp.next();
				if (!wires.containsKey(pin.getName())) {
					throw new RuntimeException();
				} else {
					int wireWidth = wires.get(pin.getName());
					if (wireWidth == 1 && (pin.getBusLSB() == -1)
							&& (pin.getBusMSB() == -1)) {
						continue;
					} else if (wireWidth > 1) {
						if (pin.getBusMSB() == -1 && pin.getBusLSB() == -1) {
							pin.setBusMSB(wireWidth - 1);
							pin.setBusLSB(0);
						} else if (pin.getBusMSB() != -1
								&& pin.getBusLSB() != -1) {
							if ((wireWidth <= pin.getBusMSB())
									|| (pin.getBusMSB() < pin.getBusLSB())
									|| (pin.getBusLSB() < 0)) {
								throw new RuntimeException();
							}
						} else if ((pin.getBusMSB() != -1)
								&& (pin.getBusLSB() == -1)) {
							if (wireWidth > pin.getBusMSB()
									&& pin.getBusMSB() >= 0) {
								pin.setBusLSB(pin.getBusMSB());
							} else {
								throw new RuntimeException();
							}
						}
					}
				}
			}
		}
	}

	@Override
	public String toString() {
		return "module " + name + " " + wires.size() + " " + components.size();
	}

	void storeModuleToFile(String fileName) {
		try {
			fileName = fileName + ".syntax";
			File file = new File(fileName);
			file.delete();
			PrintStream out = new PrintStream(file);
			out.println(this);
			SortedMap<String, Integer> wiresSorted = new TreeMap<String, Integer>(
					wires);
			Iterator<String> it1 = wiresSorted.keySet().iterator();
			Iterator<Integer> it2 = wiresSorted.values().iterator();
			while (it1.hasNext() && it2.hasNext()) {
				out.println("wire " + it1.next() + " " + it2.next());
			}
			ListIterator<EvlComponent> it = components.listIterator();
			while (it.hasNext()) {
				EvlComponent comp = it.next();
				out.println("component " + comp.getType() + " "
						+ comp.getName() + " " + comp.pins.size());
				ListIterator<EvlPin> itp = comp.pins.listIterator();
				while (itp.hasNext()) {
					EvlPin pin = itp.next();
					out.println("pin " + pin.getName() + " " + pin.getBusMSB()
							+ " " + pin.getBusLSB());
				}
			}
			out.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	String getFileName() {
		return fileName;
	}

	public Set<Map.Entry<String, Integer>> getWiresSet() {
		return wires.entrySet();
	}

	public List<EvlComponent> getComponents() {
		return components;
	}

}