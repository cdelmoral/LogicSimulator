/**
 * 
 */
package com.logicsimulator.evl.module.fsm;

import com.logicsimulator.evl.token.EvlToken;

/**
 * @author carlos
 *
 */
public class EvlModuleFSMDoneState extends EvlModuleFSMState {

	/**
	 * @param fsm
	 */
	public EvlModuleFSMDoneState(EvlModuleFSM fsm) {
		super(fsm);
	}

	/* (non-Javadoc)
	 * @see com.logicsimulator.evl.module.fsm.EvlModuleFSMState#consume(com.logicsimulator.evl.token.EvlToken)
	 */
	@Override
	public void consume(EvlToken token) {
		throw new RuntimeException();
	}

}
