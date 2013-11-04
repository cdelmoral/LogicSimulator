package com.logicsimulator;

import java.util.List;
import java.util.ListIterator;

public class EvlComponentFSM {

	EvlPin pin;
	EvlComponent component;

	private EvlStatement statement;
	private List<EvlComponent> components;

	private ComponentFSMState state;

	private ComponentFSMState initState;
	private ComponentFSMState doneState;
	private ComponentFSMState typeState;
	private ComponentFSMState nameState;
	private ComponentFSMState pinsState;
	private ComponentFSMState pinNameState;
	private ComponentFSMState busState;
	private ComponentFSMState busMSBState;
	private ComponentFSMState busColonState;
	private ComponentFSMState busLSBState;
	private ComponentFSMState busDoneState;
	private ComponentFSMState pinsDoneState;

	public EvlComponentFSM(List<EvlComponent> components, EvlStatement statement) {
		this.components = components;
		this.statement = statement;

		initState = new InitState(this);
		doneState = new DoneState(this);
		typeState = new TypeState(this);
		nameState = new NameState(this);
		pinsState = new PinsState(this);
		pinNameState = new PinNameState(this);
		busState = new BusState(this);
		busMSBState = new BusMSBState(this);
		busColonState = new BusColonState(this);
		busLSBState = new BusLSBState(this);
		busDoneState = new BusDoneState(this);
		pinsDoneState = new PinsDoneState(this);

		state = initState;
	}

	public void extractComponents() {
		EvlToken token;
		ListIterator<EvlToken> it;
		for (it = statement.getTokenIterator(); it.hasNext()
				&& state != doneState;) {
			token = it.next();
			state.consume(token);
		}
		if (it.hasNext() || state != doneState) {
			throw new RuntimeException();
		}
	}

	public void setState(ComponentFSMState state) {
		this.state = state;
	}

	public abstract class ComponentFSMState {
		protected EvlComponentFSM fsm;

		public ComponentFSMState(EvlComponentFSM fsm) {
			this.fsm = fsm;
		}

		public abstract void consume(EvlToken token);
	}

	private class InitState extends ComponentFSMState {
		public InitState(EvlComponentFSM fsm) {
			super(fsm);
		}

		@Override
		public void consume(EvlToken token) {
			if (token.getType() == EvlToken.TokenType.NAME) {
				component = new EvlComponent(token.getName(), "NONE");
				fsm.setState(typeState);
			} else {
				throw new RuntimeException();
			}
		}
	}

	private class DoneState extends ComponentFSMState {
		public DoneState(EvlComponentFSM fsm) {
			super(fsm);
		}

		@Override
		public void consume(EvlToken token) {
			throw new RuntimeException();
		}
	}

	private class TypeState extends ComponentFSMState {
		public TypeState(EvlComponentFSM fsm) {
			super(fsm);
		}

		@Override
		public void consume(EvlToken token) {
			if (token.getType() == EvlToken.TokenType.NAME) {
				component.setName(token.getName());
				fsm.setState(fsm.nameState);
			} else if (token.getName().equals("(")) {
				fsm.setState(fsm.pinsState);
			} else {
				throw new RuntimeException();
			}
		}
	}

	private class NameState extends ComponentFSMState {
		public NameState(EvlComponentFSM fsm) {
			super(fsm);
		}

		@Override
		public void consume(EvlToken token) {
			if (token.getName().equals("(")) {
				fsm.setState(fsm.pinsState);
			} else {
				throw new RuntimeException();
			}
		}
	}

	private class PinsState extends ComponentFSMState {
		public PinsState(EvlComponentFSM fsm) {
			super(fsm);
		}

		@Override
		public void consume(EvlToken token) {
			if (token.getType() == EvlToken.TokenType.NAME) {
				pin = new EvlPin(token.getName(), -1, -1);
				fsm.setState(fsm.pinNameState);
			} else {
				throw new RuntimeException();
			}
		}
	}

	private class PinNameState extends ComponentFSMState {
		public PinNameState(EvlComponentFSM fsm) {
			super(fsm);
		}

		@Override
		public void consume(EvlToken token) {
			if (token.getName().equals("[")) {
				fsm.setState(fsm.busState);
			} else if (token.getName().equals(")")) {
				component.pins.add(pin);
				fsm.setState(fsm.pinsDoneState);
			} else if (token.getName().equals(",")) {
				component.pins.add(pin);
				fsm.setState(fsm.pinsState);
			} else {
				throw new RuntimeException();
			}
		}
	}

	private class BusState extends ComponentFSMState {
		public BusState(EvlComponentFSM fsm) {
			super(fsm);
		}

		@Override
		public void consume(EvlToken token) {
			if (token.getType() == EvlToken.TokenType.NUMBER) {
				pin.setBusMSB(Integer.valueOf(token.getName()));
				fsm.setState(fsm.busMSBState);
			} else {
				throw new RuntimeException();
			}
		}
	}

	private class BusMSBState extends ComponentFSMState {
		public BusMSBState(EvlComponentFSM fsm) {
			super(fsm);
		}

		@Override
		public void consume(EvlToken token) {
			if (token.getName().equals("]")) {
				fsm.setState(fsm.busDoneState);
			} else if (token.getName().equals(":")) {
				fsm.setState(fsm.busColonState);
			} else {
				throw new RuntimeException();
			}
		}
	}

	private class BusColonState extends ComponentFSMState {
		public BusColonState(EvlComponentFSM fsm) {
			super(fsm);
		}

		@Override
		public void consume(EvlToken token) {
			if (token.getType() == EvlToken.TokenType.NUMBER) {
				pin.setBusLSB(Integer.valueOf(token.getName()));
				fsm.setState(fsm.busLSBState);
			} else {
				throw new RuntimeException();
			}
		}
	}

	private class BusLSBState extends ComponentFSMState {
		public BusLSBState(EvlComponentFSM fsm) {
			super(fsm);
		}

		@Override
		public void consume(EvlToken token) {
			if (token.getName().equals("]")) {
				fsm.setState(fsm.busDoneState);
			} else {
				throw new RuntimeException();
			}
		}
	}

	private class BusDoneState extends ComponentFSMState {
		public BusDoneState(EvlComponentFSM fsm) {
			super(fsm);
		}

		@Override
		public void consume(EvlToken token) {
			if (token.getName().equals(")")) {
				component.pins.add(pin);
				fsm.setState(fsm.pinsDoneState);
			} else if (token.getName().equals(",")) {
				component.pins.add(pin);
				fsm.setState(fsm.pinsState);
			} else {
				throw new RuntimeException();
			}
		}
	}

	private class PinsDoneState extends ComponentFSMState {
		public PinsDoneState(EvlComponentFSM fsm) {
			super(fsm);
		}

		@Override
		public void consume(EvlToken token) {
			if (token.getName().equals(";")) {
				components.add(component);
				fsm.setState(fsm.doneState);
			} else {
				throw new RuntimeException();
			}
		}
	}

}
