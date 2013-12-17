package com.logicsimulator.test;

import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import com.logicsimulator.MainProgram;
import com.logicsimulator.test.util.GoldenComparator;

public class TestCases {

	@Before
	public void setUp() throws Exception {
	}

	private void process(String name) {
		final String file = name + ".evl";
		String[] args = { file };
		MainProgram.main(args);
		assertTrue(GoldenComparator.compare("test_cases/" + name + ".sim_out"));
	}

	@Test
	public void test1() {
		String name = "counter";
		process(name);

	}

	@Test
	public void test2() {
		String name = "lfsr10";
		process(name);
	}

	@Test
	public void test3() {
		String name = "s15850";
		process(name);
	}

	@Test
	public void test4() {
		String name = "tris_lut_assign";
		process(name);
	}

	@Test
	public void test5() {
		String name = "lut_test";
		process(name);
	}

}
