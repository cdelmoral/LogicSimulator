package com.logicsimulator.test;

import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import com.logicsimulator.GoldenComparator;
import com.logicsimulator.MainProgram;

public class TestFile {

	@Before
	public void setUp() throws Exception {
	}

	private void process(String file, String name) {
		String[] args = { file };
		MainProgram.main(args);
		assertTrue(GoldenComparator.compare("test_cases/" + name + ".sim_out"));
	}

	@Test
	public void test1() {
		String file = "counter.evl", name = "counter";
		process(file, name);

	}

	@Test
	public void test2() {
		String file = "lfsr10.evl", name = "lfsr10";
		process(file, name);
	}

	@Test
	public void test3() {
		String file = "s15850.evl", name = "s15850";
		process(file, name);
	}

	@Test
	public void test4() {
		String file = "tris_lut_assign.evl", name = "tris_lut_assign";
		process(file, name);
	}

	@Test
	public void test5() {
		String file = "lut_test.evl", name = "lut_test";
		process(file, name);
	}

}
