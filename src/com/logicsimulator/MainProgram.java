package com.logicsimulator;

public class MainProgram {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		if (args.length != 1) {
			return;
		}

		System.out.print("Running " + args[0] + "... ");

		try {
			String file = args[0];
			GatePrototypes gatePrototypes = new GatePrototypes(true);

			EvlTokenFile tf = new EvlTokenFile("test_cases/" + file);
			EvlModules evlModules = tf.readModules();
			BuilderClient bc = new BuilderClient(evlModules,
					new NetlistBuilder(gatePrototypes));
			Netlist nl = bc.buildNetlist();
			nl.storeNetlistToFile("test_cases/" + file);
			Simulator simulator = new BasicSimulator(nl);
			simulator.simulate();
		} catch (Exception e) {
			System.out.println("Error!");
			return;
		}

		System.out.println("Done!");
	}
}
