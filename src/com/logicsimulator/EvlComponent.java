package com.logicsimulator;

import java.util.LinkedList;
import java.util.List;

class EvlComponent {
	private String type, name;
	List<EvlPin> pins;

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

	List<EvlPin> getEvlPins() {
		return pins;
	}
}