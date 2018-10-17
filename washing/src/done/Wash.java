/*
 * Real-time and concurrent programming course, washing machine lab.
 * Department of Computer Science, Lund Institute of Technology
 *
 * PP 980923 Created
 * PP 990924 Revised
 */

package done;

import todo.WashingController;

/**
 * Main program. Basically just parses the command line and then
 * creates a WashingMachineSimulation and a WashingController.
 *
 * @see todo.WashingController
 */

public class Wash {

	// ------------------------------------------------------------- MAIN METHOD

	/**
	 * Parses command line arguments and creates a WashingController.
	 *
	 * Possible arguments:
	 * <DL>
	 * <DT>-speed N     <DD>Speeds up simulation N times.
	 * <DT>-hackermode  <DD>Adds a panel with a few useful buttons.
	 * <DT>-help        <DD>Does NOT provide any help.
	 * </DL>
	 *
	 * @exception   NumberFormatException  When the numeric speed argument fails.
	 * @exception   InterruptedException   Won't happen, I'm just too lazy
	 *                                     to catch it.
	 */
	public static void main(String[] args)
			throws NumberFormatException, InterruptedException {
		// Options
		double theSpeed             = 50.0;
		boolean useHackerPanel      = false;
		double freakShowProbability = 0.00333333;    // Once a minute
		// Read options
		for(int i = 0; i < args.length; i++) {
			if (args[i].equals("-speed") && i < (args.length - 1)) {
				theSpeed = (new Double(args[i + 1])).doubleValue();
				i++;    // Don't consider the speed anymore
			}
			else if (args[i].equals("-freakshowprobability")) {
				freakShowProbability = (new Double(args[i + 1])).doubleValue();
				i++;    // Don't consider the probability anymore
			}
			else if (args[i].equals("-hackermode"))
				useHackerPanel = true;
			else if (args[i].equals("-help")) {
				System.exit(0);
			}
			else
				System.out.println("Unrecognized command line argument '"
						+ args[i] + "'. Ignored.");
		}

		// Create a washing machine simulation and a controller
		AbstractWashingMachine theMachine
		= new WashingMachineSimulation(theSpeed, freakShowProbability);
		@SuppressWarnings("unused")
		HackerPanel p;
		if (useHackerPanel) {
			p = new HackerPanel(theMachine);
		}
		theMachine.setButtonListener(new WashingController(theMachine, theSpeed));
		theMachine.start();

		// Wait for hell to freeze over
		Object o = new Object();
		synchronized(o) {
			o.wait();
		}
	}
};
