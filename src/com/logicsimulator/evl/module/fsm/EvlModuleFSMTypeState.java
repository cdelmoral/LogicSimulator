/**
 * 
 */
package com.logicsimulator.evl.module.fsm;

import com.logicsimulator.evl.token.EvlToken;

/**
 * @author carlos
 *
 */
public class EvlModuleFSMTypeState extends EvlModuleFSMState {

	/**
	 * @param fsm
	 */
	public EvlModuleFSMTypeState(EvlModuleFSM fsm) {
		super(fsm);
	}

	/* (non-Javadoc)
	 * @see com.logicsimulator.evl.module.fsm.EvlModuleFSMState#consume(com.logicsimulator.evl.token.EvlToken)
	 */
	@Override
	public void consume(EvlToken token) {
		if (token.getType() == EvlToken.TokenType.SINGLE) {
			if (token.getName().equals(";")) {
				changeState("done");
			} else if (token.getName().equals("(")) {
				changeState("ports");
			} else {
				throw new RuntimeException();
			}
		} else {
			throw new RuntimeException();
		}
	}

}
