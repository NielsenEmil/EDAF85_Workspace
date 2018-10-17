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
 * This event is sent by washing program processes to the water level
 * controller process. It is an order to reach and hold a given level.
 */
public class WaterEvent extends RTEvent {
	private static final long serialVersionUID = 1L;

	// ----------------------------------------------------------- CONSTRUCTOR

	/**
	 * @param   mode     Regulation mode (WATER_IDLE, WATER_FILL, WATER_DRAIN)
	 * @param   target   Water level to reach and hold
	 */
	public WaterEvent(Object source, int mode, double level) {
		super(source);

		myMode   = mode;
		myLevel  = level;
	}

	// -------------------------------------------------------- PUBLIC METHODS

	/**
	 * @return Water regulation mode (WATER_IDLE, WATER_FILL, WATER_DRAIN)
	 */
	public int getMode() {
		return myMode;
	}

	/**
	 * @return Target level
	 */
	public double getLevel() {
		return myLevel;
	}

	// ------------------------------------------------------ PUBLIC CONSTANTS

	/** Regulation off, turn off all pumps */
	public static final int WATER_IDLE  = 0;

	/** Fill water to a given level */
	public static final int WATER_FILL  = 1;

	/** Drain, leave drain pump running when finished */
	public static final int WATER_DRAIN = 2;

	// -------------------------------------------- PRIVATE INSTANCE VARIABLES

	// Water regulation mode (WATER_IDLE, WATER_FILL, WATER_DRAIN)
	private int myMode;

	// Target level
	private double myLevel;
}
