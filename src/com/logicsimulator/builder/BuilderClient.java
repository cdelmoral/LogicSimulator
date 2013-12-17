package com.logicsimulator.builder;

import java.util.Map;

import com.logicsimulator.evl.component.EvlComponent;
import com.logicsimulator.evl.module.EvlModule;
import com.logicsimulator.evl.module.EvlModules;
import com.logicsimulator.evl.pin.EvlPin;
import com.logicsimulator.netlist.Netlist;

public class BuilderClient {
	private EvlModules evlModules;
	NetlistAbstractBuilder builder;

	public BuilderClient(EvlModules evlModules, NetlistAbstractBuilder builder) {
		this.evlModules = evlModules;
		this.builder = builder;
	}

	public Netlist buildNetlist() {
		for (EvlModule evlModule : evlModules) {
			for (Map.Entry<String, Integer> wire : evlModule.getWiresSet()) {
				if (wire.getValue() == 1) {
					builder.addNet(wire.getKey());
				} else if (wire.getValue() > 1) {
					for (int i = 0; i < wire.getValue(); i++) {
						builder.addNet(wire.getKey() + "[" + i + "]");
					}
				}
			}
			for (EvlComponent c : evlModule.getComponents()) {
				int gateId = builder.addGate(c.getName(), c.getType());
				for (EvlPin p : c.getEvlPins()) {
					if (p.getBusMSB() == -1) {
						builder.appendPin(gateId, p.getName());
					} else {
						builder.appendPinBus(gateId, p.getName(),
								p.getBusMSB(), p.getBusLSB());
					}
				}
			}
		}
		return builder.getNetlist();
	}

}
