/*
 * Real-time and concurrent programming course, laboratory 3
 * Department of Computer Science, Lund Institute of Technology
 *
 * PP 980813 Created
 * PP 990924 Revised, ID moved from superclass to own mode attribute
 */

package todo;

import se.lth.cs.realtime.event.*;

/**
 * This event is sent by washing program processes to the spin
 * controller process. It is an order to set a particular spin.
 */
public class SpinEvent extends RTEvent {
	private static final long serialVersionUID = 1L;

	// ----------------------------------------------------------- CONSTRUCTOR

	/**
	 * @param   mode   Spin regulation mode (SPIN_OFF, SPIN_SLOW, SPIN_FAST)
	 */
	public SpinEvent(Object source, int mode) {
		super(source);

		myMode = mode;
	}

	// -------------------------------------------------------- PUBLIC METHODS

	/**
	 * @return Spin regulation mode (SPIN_OFF, SPIN_SLOW, or SPIN_FAST)
	 */
	public int getMode() {
		return myMode;
	}

	// ------------------------------------------------------ PUBLIC CONSTANTS

	/** Turn off motor. */
	public static final int SPIN_OFF  = 0;

	/** Slow spin, changing direction periodically. */
	public static final int SPIN_SLOW = 1;

	/** Fast spin. */
	public static final int SPIN_FAST = 2;

	// ---------------------------------------------------- PRIVATE ATTRIBUTES

	private int myMode;       // Spin mode (SPIN_OFF, SPIN_SLOW, or SPIN_FAST)
}
