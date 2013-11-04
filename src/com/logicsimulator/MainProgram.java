//package com.logicsimulator;
//
//
//public class MainProgram {
//
//	/**
//	 * @param args
//	 */
//	public static void main(String[] args) {
//		if (args.length != 1) {
//			return;
//		}
//
//		String[] files = { /*/"lfsr10.evl", "counter.evl", "s15850.evl", /**/"tris_lut_assign.evl" };
//
//		for (String file : files) {
//			
//			System.out.println("Computing " + file + "...");
//			
//			try {				
//				GatePrototypes gatePrototypes = new GatePrototypes(true);
//				
//				EvlTokenFile tf = new EvlTokenFile("test_cases/" + file);
//				EvlModules evlModules = tf.readModules();
//				BuilderClient bc = new BuilderClient(evlModules, new NetlistBuilder(gatePrototypes));
//				Netlist nl = bc.buildNetlist();
//				nl.storeNetlistToFile("test_cases/" + file);
//				System.out.println("\tNetlist created");
//				Simulator simulator = new BasicSimulator(nl);
//				System.out.print("\tSimulating: ");
//				simulator.simulate();
//			} catch (RuntimeException e) {
//				System.err.println(e.getMessage());
//				e.printStackTrace();
////				return;
//			}
//			
//		}
//		System.out.println("Reached end of program.");
//	}
//
//}
