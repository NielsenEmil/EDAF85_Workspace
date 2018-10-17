/*
 * Real-time and concurrent programming course, laboratory 3
 * Department of Computer Science, Lund Institute of Technology
 *
 * PP 980813 Created
 * PP 990924 Revised
 */

package todo;

import se.lth.cs.realtime.event.*;

/**
 * This event is sent by washing program processes to the temperature
 * controller process. It is an order to reach and hold a given
 * temperature.
 */
public class TemperatureEvent extends RTEvent {
	private static final long serialVersionUID = 1L;

	// ----------------------------------------------------------- CONSTRUCTOR

	/**
	 * @param   mode     Temperature regulation mode (TEMP_IDLE, TEMP_SET)
	 * @param   target   Target temperature to reach and hold
	 */
	public TemperatureEvent(Object source, int mode, double temp) {
		super(source);

		myMode   = mode;
		myTemp   = temp;
	}

	// -------------------------------------------------------- PUBLIC METHODS

	/**
	 * @return Temperature regulation mode
	 */
	public int getMode() {
		return myMode;
	}

	/**
	 * @return Target temperature
	 */
	public double getTemperature() {
		return myTemp;
	}

	// ------------------------------------------------------ PUBLIC CONSTANTS

	/** Temperature regulation off (no heating) */
	public static final int TEMP_IDLE  = 0;

	/** Reach and hold the given temperature */
	public static final int TEMP_SET   = 1;

	// -------------------------------------------- PRIVATE INSTANCE VARIABLES

	// Temperature regulation mode
	private int myMode;

	// Target temperature
	private double myTemp;
}
