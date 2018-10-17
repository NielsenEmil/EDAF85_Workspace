package done;


/**
 * This interface was created to get rid of the circular compile-time 
 * dependency: 
 * Classes to-do depend on hw/sw interface classes,
 * which for the simulation depend on the ClockGUI class,
 * which call the AlarmClock constructor to-do,
 * which depends on (instantiates) the classes to-do!
 * 
 * With this interface, the dependency of the ClockGUI can be
 * postphoned until run-time, allowing classes to be compiled.
 */

interface ClockTimeDisplay {

    /**
     * Should be able to show a clock time given as an int formatted as
     * hhmmss where hh denotes the hours (0..23), mm the minutes, and
     * ss the seconds. 
     */
    void setTime(int hhmmss);

	void setAlarmPulse(boolean b);
}
