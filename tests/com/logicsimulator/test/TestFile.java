package com.logicsimulator.test;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertTrue;

import com.logicsimulator.BasicSimulator;
import com.logicsimulator.BuilderClient;
import com.logicsimulator.EvlModules;
import com.logicsimulator.EvlTokenFile;
import com.logicsimulator.GatePrototypes;
import com.logicsimulator.Netlist;
import com.logicsimulator.NetlistBuilder;
import com.logicsimulator.Simulator;

public class TestFile {
	
	GatePrototypes gatePrototypes;

	@Before
	public void setUp() throws Exception {
	}
	
	@Before
	public void setUpBeforeClass() throws Exception {
		gatePrototypes = new GatePrototypes(true);
	}
	
	private void process(String file) {
		EvlTokenFile tf = new EvlTokenFile("test_cases/" + file);
		EvlModules evlModules = tf.readModules();
		BuilderClient bc = new BuilderClient(evlModules, new NetlistBuilder(gatePrototypes));
		Netlist nl = bc.buildNetlist();
		nl.storeNetlistToFile("test_cases/" + file);
		Simulator simulator = new BasicSimulator(nl);
		simulator.simulate();
		assertTrue(nl.checkOutputs());
	}

	@Test
	public void test1() {
		String file = "counter.evl";
		process(file);
		
	}
	
	@Test
	public void test2() {
		String file = "lfsr10.evl";
		process(file);
	}

	@Test
	public void test3() {
		String file = "s15850.evl";
		process(file);
	}

	@Test
	public void test4() {
		String file = "tris_lut_assign.evl";
		process(file);
	}

	@Test
	public void test5() {
		String file = "lut_test.evl";
		process(file);
	}

}
