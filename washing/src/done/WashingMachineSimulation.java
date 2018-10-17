/*
 * Real-time and concurrent programming course, laboratory 3
 * Department of Computer Science, Lund Institute of Technology
 *
 * PP 980813 Created
 * PP 990924 Revised, now uses OngoingThread instead of java.lang.Thread
 */

package done;

import se.lth.cs.realtime.*;

/**
 * Implementation of abstract AbstractWashingMachine class.
 * Interfaces to the simulation.
 */

class WashingMachineSimulation
extends AbstractWashingMachine
implements Runnable, java.awt.event.ActionListener {

	// ------------------------------------------------------------- CONSTRUCTOR

	/**
	 * @param speed Simulation speed (1 for normal speed, >1 for faster)
	 */
	public WashingMachineSimulation(double speed,
			double freakShowProbability) {
		mySpeed          = speed;
		myTemperature    = AMBIENT_TEMP;
		myWaterLevel     = 0;
		iAmLocked        =
				iAmHeating       =
				iAmFilling       =
				iAmDraining      =
				iAmOverHeating   =
				iAmOverFlowing   = false;
		mySpin           = SPIN_OFF;
		myButtonListener = null;
		myThread         = new OngoingThread(this);
		myView           = new WashingView(this, mySpeed, freakShowProbability);
	}

	// ------------------------------------------------- PACKAGE PRIVATE METHODS

	/**
	 * Set button listener. The listener's processButton() method will be
	 * called whenever a button is pressed.
	 */
	void setButtonListener(ButtonListener l) {
		myButtonListener = l;
	}

	/**
	 * @return True if currently overheating, false otherwise.
	 */
	synchronized boolean isOverHeating() {
		return iAmOverHeating;
	}

	/**
	 * @return True if currently overflowing, false otherwise.
	 */
	synchronized boolean isOverFlowing() {
		return iAmOverFlowing;
	}

	/**
	 * @return True if currently draining, false otherwise.
	 */
	synchronized boolean isDraining() {
		return iAmDraining;
	}

	/**
	 * @return True if currently filling, false otherwise.
	 */
	synchronized boolean isFilling() {
		return iAmFilling;
	}

	/**
	 * @return Current spin (0..3)
	 */
	synchronized int getSpin() {
		return mySpin;
	}

	// ------------------------------------------------------ OVERRIDDEN METHODS

	/**
	 * Read the water temperature.
	 * @return Temperature in Centigrades (0-100).
	 */

	public synchronized double getTemperature() {
		return myTemperature;
	}

	/**
	 * Read the water level in the machine.
	 * @return A real number between 0 and 1, where 1 indicates an
	 *   absolutely full (i.e. overflowing) machine and 0
	 *   indicates a (practically) empty one.
	 */

	public synchronized double getWaterLevel() {
		return myWaterLevel / MAX_WATER_LEVEL;
	}

	/**
	 * Check if the front door is open.
	 * @return True if the door is locked, false if it is open.
	 */

	public synchronized boolean isLocked() {
		return iAmLocked;
	}

	/**
	 * Check whether currently heating.
	 * @return True if currently heating, false otherwise.
	 */
	public synchronized boolean isHeating() {
		return iAmHeating;
	}

	/**
	 * Turns the heating on/off.
	 * @param on True means "heat on", false means "heat off".
	 */

	public synchronized void setHeating(boolean on) {
		iAmHeating = on;
	}

	/**
	 * Open/close the water input tap.
	 * @param on True means "open tap", false means "close tap".
	 */

	public synchronized void setFill(boolean on) {
		iAmFilling = on;
		myView.updateFill();
	}

	/**
	 * Start/stop the water drain pump.
	 * @param on True means "start pump", false means "stop pump".
	 */

	public synchronized void setDrain(boolean on) {
		iAmDraining = on;
		myView.updateDrain();
	}

	/**
	 * Lock/unlock the front door.
	 * @param on True means "lock door", false means "unlock door".
	 */

	public synchronized void setLock(boolean on) {
		iAmLocked = on;
		myView.updateLock();
	}

	/**
	 * Control the turning motor.
	 * @param direction 1=Left slow, 2=right slow, 3=left fast, 0=stop.
	 */

	public synchronized void setSpin(int direction) {
		mySpin = direction;
		myView.updateTumbler();
	}

	/**
	 * Turn the washing machine on. Called early during initialization 
	 * by main().
	 */

	public synchronized void start() {
		myThread.start();
	}

	/**
	 * Run by Thread to implement the Thread task.
	 */

	public void run() {

		// Adjust sleeping time to avoid too small times
		double sampleInterval = DESIRED_INTERVAL;
		double execInterval   = sampleInterval / mySpeed;

		if (execInterval < MINIMUM_INTERVAL) {
			execInterval = MINIMUM_INTERVAL;
			sampleInterval = MINIMUM_INTERVAL * mySpeed;
		}

		try {
			long t = 0;
			long start = System.currentTimeMillis();
			while(true) {
				// Update simulation state
				updateState(sampleInterval/1000.0);

				// Delay thread execution
				t += execInterval;
				long time = System.currentTimeMillis();
				long sleep = t - (time - start);
				RTThread.sleep(sleep>0?sleep:0);
			}
		}
		catch(Exception e) { /* Shouldn't happen */ }
	}

	/**
	 * Listener method; automatically called by AWT upon
	 * window events, such as when a button is clicked.
	 *
	 * @param e An ActionEvent describing the event.
	 */

	public void actionPerformed(java.awt.event.ActionEvent e) {
		String command = e.getActionCommand();

		if (myButtonListener != null) {
			if (command.equals("0"))
				myButtonListener.processButton(0);
			else if (command.equals("1"))
				myButtonListener.processButton(1);
			else if (command.equals("2"))
				myButtonListener.processButton(2);
			else if (command.equals("3"))
				myButtonListener.processButton(3);
		}
	}

	// --------------------------------------------------------- PRIVATE METHODS

	/**
	 * Update state variables. Called periodically by run().
	 *
	 * @param dt Time since last state update (seconds).
	 */

	private synchronized void updateState(double dt) {
		double dV, dT;

		// Update water level.

		// For water input flow:
		//   Set the new temperature to the weighted average
		//   of old temperature and that of the cold water (weighted by volume).
		if (iAmFilling) {
			dV = FLOW_IN * dt;
			iAmOverFlowing = (myWaterLevel + dV >= MAX_WATER_LEVEL);
			if (iAmOverFlowing)
				dV = MAX_WATER_LEVEL - myWaterLevel;

			// Adjust temperature to compensate for the cold new water
			myTemperature = (myTemperature * myWaterLevel + COOL_WATER_TEMP * dV)
					/ (myWaterLevel + dV);

			myWaterLevel += dV;
		}

		// For water output flow:
		//   If the water level comes down to zero, set temperature to 20 degs C.
		if (iAmDraining) {
			double prevLevel = myWaterLevel;
			dV = - FLOW_OUT * dt;
			myWaterLevel += dV;
			if (myWaterLevel < 0)
				myWaterLevel = 0;
			if (myWaterLevel == 0 && prevLevel > 0 && !iAmHeating)
				myTemperature = AMBIENT_TEMP;
		}

		// Update temperature.

		// For heating:
		//   dT = dt * HEATING_CAPACITY / (V * WATER_CAPACITIVITY)
		if(iAmHeating) {
			if(myWaterLevel > 0) {
				dT = dt * HEATING_CAPACITY / (myWaterLevel * WATER_CAPACITIVITY);
				myTemperature += dT;
			}
			else
				myTemperature = 100;

			// Catch fire when temperature becomes 100 deg C
			if (myTemperature >= 100) {
				myTemperature = 100;
				iAmOverHeating = true;
			}
		}

		// For cooling (very simple model):
		//   dT = - dt * (T - AMBIENT_TEMP) / COOLING_FACTOR
		if (! iAmOverHeating) {
			dT = - dt * (myTemperature - AMBIENT_TEMP) / COOLING_FACTOR;
			myTemperature += dT;
		}
	}

	// ---------------------------------------------- PRIVATE INSTANCE VARIABLES

	// Temperature (centigrade, 0..100)
	private double myTemperature;

	// Water level (0..20)
	private double myWaterLevel;

	// Heating, fill, pump, and lock status
	private boolean iAmHeating, iAmFilling, iAmDraining, iAmLocked;

	// Spin status
	private int mySpin;

	// Overheating indication
	private boolean iAmOverHeating;

	// Overflow indication
	private boolean iAmOverFlowing;

	// Simulation speed
	private double mySpeed;

	// View for visualization
	private WashingView myView;

	// Thread for concurrent execution
	private OngoingThread myThread;

	// Button listener
	private ButtonListener myButtonListener;

	// ------------------------------------------------------- PRIVATE CONSTANTS

	// Water heating capacitivity (J / (kg * K))
	private static final double WATER_CAPACITIVITY = 4180;

	// Max water level (l)
	private static final double MAX_WATER_LEVEL = 20.0;

	// Water input flow (l/s)
	private static final double FLOW_IN = 0.11345677;  // Approx. 0.1

	// Water output flow (l/s)
	private static final double FLOW_OUT = 0.1926345;  // Approx. 0.2

	// Radiator heating capacity (kW)
	private static final double HEATING_CAPACITY = 2034.5;  // Approx. 2kW

	// Cooling proportional constant (a.k.a. Boris' Magic Number)
	private static final double COOLING_FACTOR = 4200;

	// Ambient (room) temperature (centigrades)
	private static final double AMBIENT_TEMP = 20.0;

	// Temperature of cold water from input tap (centigrades)
	private static final double COOL_WATER_TEMP = 8.0;

	// Ideal sampling interval for simulation (ms)
	private static final long DESIRED_INTERVAL = 200;

	// Minimal period time for actual thread (ms)
	private static final long MINIMUM_INTERVAL = 40;
}
