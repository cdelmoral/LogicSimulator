package com.logicsimulator.gate;

import java.util.Iterator;
import java.util.List;

import com.logicsimulator.pin.Pin;

interface SemanticsType {

	boolean checkSemantics(List<Pin> pins);

}

class OneOutTwoIn implements SemanticsType {

	@Override
	public boolean checkSemantics(List<Pin> pins) {
		boolean firstPin = true;
		if (pins.size() != 3) {
			throw new RuntimeException();
		}
		for (Pin pin : pins) {
			if (pin.netsSize() != 1) {
				throw new RuntimeException();
			} else {
				if (firstPin) {
					pin.setAsOutput();
					firstPin = false;
				} else {
					pin.setAsInput();
				}
			}
		}
		return false;
	}

}

class OneOutMultIn implements SemanticsType {

	@Override
	public boolean checkSemantics(List<Pin> pins) {
		if (pins.size() < 3) {
			throw new RuntimeException();
		} else {
			Iterator<Pin> it = pins.iterator();
			Pin pin;
			if (it.hasNext()) {
				pin = it.next();
				if (pin.netsSize() != 1) {
					throw new RuntimeException();
				} else {
					pin.setAsOutput();
				}
			}
			while (it.hasNext()) {
				pin = it.next();
				if (pin.netsSize() != 1) {
					throw new RuntimeException();
				} else {
					pin.setAsInput();
				}
			}
		}
		return true;
	}

}

class OneOutOneIn implements SemanticsType {

	@Override
	public boolean checkSemantics(List<Pin> pins) {
		if (pins.size() != 2) {
			throw new RuntimeException();
		} else {
			Iterator<Pin> it = pins.iterator();
			Pin pin;
			if (it.hasNext()) {
				pin = it.next();
				if (pin.netsSize() != 1) {
					throw new RuntimeException();
				} else {
					pin.setAsOutput();
				}
			}
			while (it.hasNext()) {
				pin = it.next();
				if (pin.netsSize() != 1) {
					throw new RuntimeException();
				} else {
					pin.setAsInput();
				}
			}
		}
		return true;
	}

}

class OneBusOutOneBusIn implements SemanticsType {

	@Override
	public boolean checkSemantics(List<Pin> pins) {
		if (pins.size() != 2) {
			throw new RuntimeException();
		} else {
			Iterator<Pin> it = pins.iterator();
			Pin pin = it.next();
			if (pin.netsSize() < 1) {
				throw new RuntimeException();
			} else {
				pin.setAsOutput();
			}
			pin = it.next();
			if (pin.netsSize() < 1) {
				throw new RuntimeException();
			} else {
				pin.setAsInput();
			}
		}
		return true;
	}

}

class MultBusOutNoIn implements SemanticsType {

	@Override
	public boolean checkSemantics(List<Pin> pins) {
		if (pins.size() < 1) {
			throw new RuntimeException();
		} else {
			Iterator<Pin> it = pins.iterator();
			while (it.hasNext()) {
				Pin pin = it.next();
				if (pin.netsSize() < 1) {
					throw new RuntimeException();
				} else {
					pin.setAsOutput();
				}
			}
		}
		return true;
	}

}

class NoOutMultBusIn implements SemanticsType {

	@Override
	public boolean checkSemantics(List<Pin> pins) {
		if (pins.size() < 1) {
			throw new RuntimeException();
		} else {
			Iterator<Pin> it = pins.iterator();
			while (it.hasNext()) {
				Pin pin = it.next();
				if (pin.netsSize() < 1) {
					throw new RuntimeException();
				} else {
					pin.setAsInput();
				}
			}
		}
		return true;
	}

}