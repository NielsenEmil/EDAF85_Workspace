/*
 * Real-time and concurrent programming course, laboratory 3
 * Department of Computer Science, Lund Institute of Technology
 *
 * PP 980728 Created
 * PP 990924 Revised
 */

package done;

/**
 * Abstract description of a washing machine. Specialized
 * into simulations and actual hardware implementations.
 */

public abstract class AbstractWashingMachine {

  /**
   * Read the water temperature.
   * @return Temperature in Centigrades (0-100).
   */

  public abstract double getTemperature();

  /**
   * Read the water level in the machine.
   * @return A real number between 0 and 1, where 1 indicates an
   *   absolutely full (i.e. overflowing) machine and 0
   *   indicates a (practically) empty one.
   */

  public abstract double getWaterLevel();

  /**
   * Check if the front door is open.
   * @return True if the door is locked, false if it is open.
   */

  public abstract boolean isLocked();

  /**
   * Turns the heating on/off.
   * @param on True means "heat on", false means "heat off".
   */

  public abstract void setHeating(boolean on);

  /**
   * Open/close the water input tap.
   * @param on True means "open tap", false means "close tap".
   */

  public abstract void setFill(boolean on);

  /**
   * Start/stop the water drain pump.
   * @param on True means "start pump", false means "stop pump".
   */

  public abstract void setDrain(boolean on);
  
  /**
   * Lock/unlock the front door.
   * @param on True means "lock door", false means "unlock door".
   */

  public abstract void setLock(boolean on);

  /**
   * Control the turning motor.
   * @param direction SPIN_OFF, SPIN_LEFT, SPIN_RIGHT, or SPIN_FAST.
   */

  public abstract void setSpin(int direction);
  
  /**
   * Activate the washing machine. Called early during initialization 
   * by main().
   */
  
  public abstract void start();
      
  // ------------------------------------------------- PACKAGE PRIVATE METHODS

  /**
   * Set button listener. The listener's processButton() method will be
   * called whenever a button is pressed.
   */
  abstract void setButtonListener(ButtonListener l);

  // ------------------------------------------------------- PRIVATE CONSTANTS

  /**
   * Stop spin.
   */
  public static final int SPIN_OFF   = 0;

  /**
   * Slow left spin.
   */
  public static final int SPIN_LEFT  = 1;

  /**
   * Slow right spin.
   */
  public static final int SPIN_RIGHT = 2;

  /**
   * Fast left spin.
   */
  public static final int SPIN_FAST  = 3;
};
