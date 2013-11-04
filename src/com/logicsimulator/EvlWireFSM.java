package com.logicsimulator;

import java.util.ListIterator;
import java.util.Map;

public class EvlWireFSM {

	private EvlStatement statement;
	private Map<String, Integer> wires;
	private String wireName;
	private int width;

	private WireFSMState state;

	private WireFSMState initState;
	private WireFSMState wireState;
	private WireFSMState wireNameState;
	private WireFSMState busState;
	private WireFSMState wiresState;
	private WireFSMState doneState;
	private WireFSMState busMSBState;
	private WireFSMState busColonState;
	private WireFSMState busLSBState;
	private WireFSMState busDoneState;

	public EvlWireFSM(Map<String, Integer> wires, EvlStatement statement) {
		this.wires = wires;
		this.statement = statement;

		initState = new InitState(this);
		wireState = new WireState(this);
		wireNameState = new WireNameState(this);
		busState = new BusState(this);
		wiresState = new WiresState(this);
		doneState = new DoneState(this);
		busMSBState = new BusMSBState(this);
		busColonState = new BusColonState(this);
		busLSBState = new BusLSBState(this);
		busDoneState = new BusDoneState(this);

		state = initState;
	}

	public void extractWires() {
		EvlToken token;
		ListIterator<EvlToken> it;
		for (it = statement.getTokenIterator(); it.hasNext()
				&& state != doneState;) {
			token = it.next();
			state.consume(token);
		}
		if (it.hasNext() || state != doneState) {
			// throw new InvalidStatement(fileName, line_no,
			// line.charAt(index), index);
			throw new RuntimeException();
		}
	}

	public void setState(WireFSMState state) {
		this.state = state;
	}

	public abstract class WireFSMState {
		protected EvlWireFSM fsm;

		public WireFSMState(EvlWireFSM fsm) {
			this.fsm = fsm;
		}

		public abstract void consume(EvlToken token);
	}

	private class InitState extends WireFSMState {
		public InitState(EvlWireFSM fsm) {
			super(fsm);
		}

		@Override
		public void consume(EvlToken token) {
			if (token.getName().equals("wire")) {
				fsm.setState(fsm.wireState);
			} else {
				// throw new InvalidStatement(fileName, line_no,
				// line.charAt(index), index);
				throw new RuntimeException();
			}

		}
	}

	private class WireState extends WireFSMState {
		public WireState(EvlWireFSM fsm) {
			super(fsm);
		}

		@Override
		public void consume(EvlToken token) {
			if (token.getType() == EvlToken.TokenType.NAME) {
				if (wires.containsKey(token.getName())) {
					// throw new InvalidStatement(fileName, line_no,
					// line.charAt(index), index);
					throw new RuntimeException();
				} else {
					width = 1;
					wires.put(token.getName(), width);
					fsm.setState(fsm.wireNameState);
				}
			} else if (token.getName().equals("[")) {
				fsm.setState(fsm.busState);
			} else {
				// throw new InvalidStatement(fileName, line_no,
				// line.charAt(index), index);
				throw new RuntimeException();
			}
		}
	}

	private class WireNameState extends WireFSMState {
		public WireNameState(EvlWireFSM fsm) {
			super(fsm);
		}

		@Override
		public void consume(EvlToken token) {
			if (token.getName().equals(",")) {
				fsm.setState(fsm.wiresState);
			} else if (token.getName().equals(";")) {
				fsm.setState(fsm.doneState);
			} else {
				// throw new InvalidStatement(fileName, line_no,
				// line.charAt(index), index);
				throw new RuntimeException();
			}

		}
	}

	private class WiresState extends WireFSMState {
		public WiresState(EvlWireFSM fsm) {
			super(fsm);
		}

		@Override
		public void consume(EvlToken token) {
			if (token.getType() == EvlToken.TokenType.NAME) {
				if (wires.containsKey(token.getName())) {
					// throw new InvalidStatement(fileName, line_no,
					// line.charAt(index), index);
					throw new RuntimeException();
				} else {
					wires.put(token.getName(), width);
					fsm.setState(fsm.wireNameState);
				}
			} else {
				// throw new InvalidStatement(fileName, line_no,
				// line.charAt(index), index);
				throw new RuntimeException();
			}
		}
	}

	private class BusState extends WireFSMState {
		public BusState(EvlWireFSM fsm) {
			super(fsm);
		}

		@Override
		public void consume(EvlToken token) {
			if (token.getType() == EvlToken.TokenType.NUMBER) {
				width = Integer.valueOf(token.getName()) + 1;
				if (width < 2) {
					// throw new InvalidStatement(fileName, line_no,
					// line.charAt(index), index);
					throw new RuntimeException();
				}
				fsm.setState(fsm.busMSBState);
			} else {
				// throw new InvalidStatement(fileName, line_no,
				// line.charAt(index), index);
				throw new RuntimeException();
			}
		}

	}

	private class BusMSBState extends WireFSMState {
		public BusMSBState(EvlWireFSM fsm) {
			super(fsm);
		}

		@Override
		public void consume(EvlToken token) {
			if (token.getName().equals(":")) {
				fsm.setState(fsm.busColonState);
			} else {
				// throw new InvalidStatement(fileName, line_no,
				// line.charAt(index), index);
				throw new RuntimeException();
			}
		}
	}

	private class BusColonState extends WireFSMState {
		public BusColonState(EvlWireFSM fsm) {
			super(fsm);
		}

		@Override
		public void consume(EvlToken token) {
			if (token.getName().equals("0")) {
				fsm.setState(fsm.busLSBState);
			} else {
				// throw new InvalidStatement(fileName, line_no,
				// line.charAt(index), index);
				throw new RuntimeException();
			}
		}

	}

	private class BusLSBState extends WireFSMState {
		public BusLSBState(EvlWireFSM fsm) {
			super(fsm);
		}

		@Override
		public void consume(EvlToken token) {
			if (token.getName().equals("]")) {
				fsm.setState(fsm.busDoneState);
			} else {
				// throw new InvalidStatement(fileName, line_no,
				// line.charAt(index), index);
				throw new RuntimeException();
			}
		}
	}

	private class BusDoneState extends WireFSMState {
		public BusDoneState(EvlWireFSM fsm) {
			super(fsm);
		}

		@Override
		public void consume(EvlToken token) {
			if (token.getType() == EvlToken.TokenType.NAME) {
				wireName = token.getName();
				wires.put(wireName, width);
				fsm.setState(fsm.wireNameState);
			} else {
				// throw new InvalidStatement(fileName, line_no,
				// line.charAt(index), index);
				throw new RuntimeException();
			}
		}
	}

	private class DoneState extends WireFSMState {
		public DoneState(EvlWireFSM fsm) {
			super(fsm);
		}

		@Override
		public void consume(EvlToken token) {
			// throw new InvalidStatement(fileName, line_no,
			// line.charAt(index), index);
			throw new RuntimeException();
		}
	}

}
