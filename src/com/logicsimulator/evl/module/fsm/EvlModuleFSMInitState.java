package com.logicsimulator.evl.module.fsm;

import com.logicsimulator.evl.token.EvlToken;

/**
 * First state of the FSM
 */
public class EvlModuleFSMInitState extends EvlModuleFSMState {

	/**
	 * @param fsm
	 */
	public EvlModuleFSMInitState(EvlModuleFSM fsm) {
		super(fsm);
	}

	/* (non-Javadoc)
	 * @see com.logicsimulator.evl.module.fsm.EvlModuleFSMState#consume()
	 */
	@Override
	public void consume(EvlToken token) {
		if (token.getType() == EvlToken.TokenType.NAME && token.getName().equals("module")) {
			changeState("module");
		} else {
			throw new RuntimeException();
		}
	}

}
