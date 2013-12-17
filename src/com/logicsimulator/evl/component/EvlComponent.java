package com.logicsimulator.evl.component;

import java.util.LinkedList;
import java.util.List;

import com.logicsimulator.evl.pin.EvlPin;

public class EvlComponent {
	private String type, name;
	public List<EvlPin> pins;

	public EvlComponent(String type, String name) {
		this.type = type;
		this.name = name;
		pins = new LinkedList<EvlPin>();
	}

	public String getType() {
		return type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<EvlPin> getEvlPins() {
		return pins;
	}
}