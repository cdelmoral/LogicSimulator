package com.logicsimulator.evl.module.fsm;

import com.logicsimulator.evl.token.EvlToken;

/**
 * Abstract state for <code>EvlModuleFSM</code>
 */
public abstract class EvlModuleFSMState {
	private EvlModuleFSM fsm;
	
	public EvlModuleFSMState(EvlModuleFSM fsm) {
		this.fsm = fsm;
	}
	
	/**
	 * Consumes a token of the statement
	 * 
	 * @param token	the token to be processed
	 */
	public abstract void consume(EvlToken token);

	/**
	 * Changes the state of the FSM
	 * 
	 * @param nextState	the new state of the FSM
	 */
	public void changeState(String nextState) {
		fsm.setState(nextState);
	}
	
	/**
	 * Creates a new module for the FSM
	 * 
	 * @param type the type of the new module
	 */
	public void createNewModule(String type) {
		fsm.createNewModule(type);
	}
}