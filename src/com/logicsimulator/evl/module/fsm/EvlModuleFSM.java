package com.logicsimulator.evl.module.fsm;

import java.util.HashMap;
import java.util.Map;

import com.logicsimulator.evl.module.EvlModule;

/**
 * FSM for extended <code>module</code> statements
 */
public class EvlModuleFSM {

	private EvlModule module;
	private EvlModuleFSMState currentState;
	private Map<String, EvlModuleFSMState> states;
	
	public EvlModuleFSM() {
		states = new HashMap<String, EvlModuleFSMState>();
		states.put("init", new EvlModuleFSMInitState(this));
		states.put("module", new EvlModuleFSMModuleState(this));
	}
	
	public void extractModules() {
		
	}
	
	public void setState(String nextState) {
		if (states.containsKey(nextState)) {
			currentState = states.get(nextState);
		} else {
			throw new RuntimeException();
		}
	}
	
	/**
	 * Creates a new module for the FSM
	 * 
	 * @param type the type of the new module
	 */
	public void createNewModule(String type) {
		module = new EvlModule(type, null); // TODO: put fileName instead of null
	}
}
