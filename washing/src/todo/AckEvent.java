/*
 * Real-time and concurrent programming course, laboratory 3
 * Department of Computer Science, Lund Institute of Technology
 *
 * PP 980812 Created
 * PP 990924 Revised to fit revised real-time framework
 */

package todo;

import se.lth.cs.realtime.event.*;

/**
 * This event is sent by the temperature and water level control
 * processes to the washing programs. Indicates that the previous order
 * has been carried out.
 *
 * @see TemperatureEvent
 * @see WaterLevelEvent
 * @see SpinLevelEvent
 */
public class AckEvent extends RTEvent {
	private static final long serialVersionUID = 1L;

	// ------------------------------------------------------------- CONSTRUCTOR

	public AckEvent(Object source) {
		super(source);
	}
}
