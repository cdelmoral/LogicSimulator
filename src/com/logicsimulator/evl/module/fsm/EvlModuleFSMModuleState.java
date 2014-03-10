/**
 * 
 */
package com.logicsimulator.evl.module.fsm;

import com.logicsimulator.evl.token.EvlToken;

/**
 * @author carlos
 *
 */
public class EvlModuleFSMModuleState extends EvlModuleFSMState {

	/**
	 * @param fsm
	 */
	public EvlModuleFSMModuleState(EvlModuleFSM fsm) {
		super(fsm);
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
	 * @see com.logicsimulator.evl.module.fsm.EvlModuleFSMState#consume(com.logicsimulator.evl.token.EvlToken)
	 */
	@Override
	public void consume(EvlToken token) {
		if (token.getType() == EvlToken.TokenType.NAME) {
			createNewModule(token.getName());
			changeState("module");
		} else {
			throw new RuntimeException();
		}
	}

}
