/*
 * Real-time and concurrent programming course, laboratory 3
 * Department of Computer Science, Lund Institute of Technology
 *
 * PP 980730
 */

package done;

/**
 * Interface for handling interrupts due to button presses.
 */

public interface ButtonListener {

  /**
   * Called whenever one of the front buttons is pressed.
   *
   * @param   theButton   Indicates pressed button (0, 1, 2, or 3)
   */
  
  void processButton(int theButton);
}
