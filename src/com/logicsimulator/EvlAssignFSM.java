package com.logicsimulator;

import java.util.List;
import java.util.ListIterator;

public class EvlAssignFSM {

	EvlPin pin;
	EvlComponent component;

	private EvlStatement statement;
	private List<EvlComponent> components;

	private AssignFSMState state;

	private AssignFSMState initState;
	private AssignFSMState lhsState;
	private AssignFSMState lhsNameState;
	private AssignFSMState lhsBusState;
	private AssignFSMState lhsMSBState;
	private AssignFSMState lhsColonState;
	private AssignFSMState lhsLSBState;
	private AssignFSMState rhsState;
	private AssignFSMState lhsDoneState;
	private AssignFSMState rhsNameState;
	private AssignFSMState rhsBusState;
	private AssignFSMState rhsMSBState;
	private AssignFSMState rhsColonState;
	private AssignFSMState rhsLSBState;
	private AssignFSMState rhsDoneState;
	private AssignFSMState doneState;

	public EvlAssignFSM(List<EvlComponent> components, EvlStatement statement) {
		this.components = components;
		this.statement = statement;

		initState = new InitState(this);
		lhsState = new LHSState(this);
		lhsNameState = new LHSNameState(this);
		lhsBusState = new LHSBusState(this);
		lhsMSBState = new LHSMSBState(this);
		lhsColonState = new LHSColonState(this);
		lhsLSBState = new LHSLSBState(this);
		lhsDoneState = new LHSDoneState(this);
		rhsState = new RHSState(this);
		rhsNameState = new RHSNameState(this);
		rhsBusState = new RHSBusState(this);
		rhsMSBState = new RHSMSBState(this);
		rhsColonState = new RHSColonState(this);
		rhsLSBState = new RHSLSBState(this);
		rhsDoneState = new RHSDoneState(this);
		doneState = new DoneState(this);

		state = initState;
	}

	public void extractAssigns() {
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

	public void setState(AssignFSMState state) {
		this.state = state;
	}

	public abstract class AssignFSMState {
		protected EvlAssignFSM fsm;

		public AssignFSMState(EvlAssignFSM fsm) {
			this.fsm = fsm;
		}

		public abstract void consume(EvlToken token);
	}

	private class InitState extends AssignFSMState {
		public InitState(EvlAssignFSM fsm) {
			super(fsm);
		}

		@Override
		public void consume(EvlToken token) {
			if (token.getType() == EvlToken.TokenType.NAME
					&& token.getName().equals("assign")) {
				component = new EvlComponent("assign", "NONE");
				fsm.setState(lhsState);
			} else {
				throw new RuntimeException();
			}
		}
	}

	private class LHSState extends AssignFSMState {
		public LHSState(EvlAssignFSM fsm) {
			super(fsm);
		}

		@Override
		public void consume(EvlToken token) {
			if (token.getType() == EvlToken.TokenType.NAME) {
				pin = new EvlPin(token.getName(), -1, -1);
				fsm.setState(lhsNameState);
			} else {
				throw new RuntimeException();
			}
		}
	}

	private class LHSNameState extends AssignFSMState {
		public LHSNameState(EvlAssignFSM fsm) {
			super(fsm);
		}

		@Override
		public void consume(EvlToken token) {
			if (token.getType() == EvlToken.TokenType.SINGLE) {
				if (token.getName().equals("=")) {
					fsm.setState(rhsState);
				} else if (token.getName().equals("[")) {
					fsm.setState(lhsBusState);
				} else {
					throw new RuntimeException();
				}
			} else {
				throw new RuntimeException();
			}
		}
	}

	private class LHSBusState extends AssignFSMState {
		public LHSBusState(EvlAssignFSM fsm) {
			super(fsm);
		}

		@Override
		public void consume(EvlToken token) {
			if (token.getType() == EvlToken.TokenType.NUMBER) {
				pin.setBusMSB(Integer.parseInt(token.getName()));
				fsm.setState(lhsMSBState);
			} else {
				throw new RuntimeException();
			}
		}
	}

	private class LHSMSBState extends AssignFSMState {
		public LHSMSBState(EvlAssignFSM fsm) {
			super(fsm);
		}

		@Override
		public void consume(EvlToken token) {
			if (token.getType() == EvlToken.TokenType.SINGLE) {
				if (token.getName().equals("]")) {
					fsm.setState(lhsDoneState);
				} else if (token.getName().equals(":")) {
					fsm.setState(lhsColonState);
				} else {
					throw new RuntimeException();
				}
			} else {
				throw new RuntimeException();
			}
		}
	}

	private class LHSColonState extends AssignFSMState {
		public LHSColonState(EvlAssignFSM fsm) {
			super(fsm);
		}

		@Override
		public void consume(EvlToken token) {
			if (token.getType() == EvlToken.TokenType.NUMBER) {
				pin.setBusLSB(Integer.parseInt(token.getName()));
				fsm.setState(lhsLSBState);
			} else {
				throw new RuntimeException();
			}
		}
	}

	private class LHSLSBState extends AssignFSMState {
		public LHSLSBState(EvlAssignFSM fsm) {
			super(fsm);
		}

		@Override
		public void consume(EvlToken token) {
			if (token.getType() == EvlToken.TokenType.SINGLE
					&& token.getName().equals("]")) {
				fsm.setState(lhsDoneState);
			} else {
				throw new RuntimeException();
			}
		}
	}

	private class LHSDoneState extends AssignFSMState {
		public LHSDoneState(EvlAssignFSM fsm) {
			super(fsm);
		}

		@Override
		public void consume(EvlToken token) {
			if (token.getType() == EvlToken.TokenType.SINGLE
					&& token.getName().equals("=")) {
				fsm.setState(rhsState);
			} else {
				throw new RuntimeException();
			}
		}
	}

	private class RHSState extends AssignFSMState {
		public RHSState(EvlAssignFSM fsm) {
			super(fsm);
		}

		@Override
		public void consume(EvlToken token) {
			component.pins.add(pin);
			if (token.getType() == EvlToken.TokenType.NAME) {
				pin = new EvlPin(token.getName(), -1, -1);
				fsm.setState(rhsNameState);
			} else {
				throw new RuntimeException();
			}
		}
	}

	private class RHSNameState extends AssignFSMState {
		public RHSNameState(EvlAssignFSM fsm) {
			super(fsm);
		}

		@Override
		public void consume(EvlToken token) {
			if (token.getType() == EvlToken.TokenType.SINGLE) {
				if (token.getName().equals(";")) {
					component.pins.add(pin);
					components.add(component);
					fsm.setState(doneState);
				} else if (token.getName().equals("[")) {
					fsm.setState(rhsBusState);
				} else {
					throw new RuntimeException();
				}
			} else {
				throw new RuntimeException();
			}
		}
	}

	private class RHSBusState extends AssignFSMState {
		public RHSBusState(EvlAssignFSM fsm) {
			super(fsm);
		}

		@Override
		public void consume(EvlToken token) {
			if (token.getType() == EvlToken.TokenType.NUMBER) {
				pin.setBusMSB(Integer.parseInt(token.getName()));
				fsm.setState(rhsMSBState);
			} else {
				throw new RuntimeException();
			}
		}
	}

	private class RHSMSBState extends AssignFSMState {
		public RHSMSBState(EvlAssignFSM fsm) {
			super(fsm);
		}

		@Override
		public void consume(EvlToken token) {
			if (token.getType() == EvlToken.TokenType.SINGLE) {
				if (token.getName().equals("]")) {
					fsm.setState(rhsDoneState);
				} else if (token.getName().equals(":")) {
					fsm.setState(rhsColonState);
				} else {
					throw new RuntimeException();
				}
			} else {
				throw new RuntimeException();
			}
		}
	}

	private class RHSColonState extends AssignFSMState {
		public RHSColonState(EvlAssignFSM fsm) {
			super(fsm);
		}

		@Override
		public void consume(EvlToken token) {
			if (token.getType() == EvlToken.TokenType.NUMBER) {
				pin.setBusLSB(Integer.parseInt(token.getName()));
				fsm.setState(rhsLSBState);
			} else {
				throw new RuntimeException();
			}
		}
	}

	private class RHSLSBState extends AssignFSMState {
		public RHSLSBState(EvlAssignFSM fsm) {
			super(fsm);
		}

		@Override
		public void consume(EvlToken token) {
			if (token.getType() == EvlToken.TokenType.SINGLE
					&& token.getName().equals("]")) {
				fsm.setState(rhsDoneState);
			} else {
				throw new RuntimeException();
			}
		}
	}

	private class RHSDoneState extends AssignFSMState {
		public RHSDoneState(EvlAssignFSM fsm) {
			super(fsm);
		}

		@Override
		public void consume(EvlToken token) {
			if (token.getType() == EvlToken.TokenType.SINGLE
					&& token.getName().equals(";")) {
				component.pins.add(pin);
				components.add(component);
				fsm.setState(doneState);
			} else {
				throw new RuntimeException();
			}
		}
	}

	private class DoneState extends AssignFSMState {
		public DoneState(EvlAssignFSM fsm) {
			super(fsm);
		}

		@Override
		public void consume(EvlToken token) {
			throw new RuntimeException();
		}
	}

}
