package done;
import se.lth.cs.realtime.semaphore.*;

public class ClockInput {

	/**
     * Return values for getChoice.
     */
    public static final int SHOW_TIME   = 0;
    public static final int SET_ALARM   = 1;
    public static final int SET_TIME    = 2;
	
    /**
     * Construct the interface with semaphore reset.
     */
    public ClockInput() {
        anyButtonChanged = new CountingSem();
    }
    
    /**
     * Semaphore signaling when the user have changed any setting.
     * Actually, according to normal rules for data abstraction, 
     * data (such as the flags and the semaphore in this class) 
     * should be accessed via methods, say awaitAnyButton() to 
     * take the semaphore. However, here we expose variables
     * through a get-method for semaphore-teaching reasons.
     */
    private Semaphore anyButtonChanged;

    /**
     * Get-method to access the semaphore instance directly.
     */
    public Semaphore getSemaphoreInstance() {
        return anyButtonChanged;
    }
    
    /* Package attributes, only used by the simulator/hardware. */
    boolean alarmOn;    // Alarm activation according to checkbox.
    int choice;         // The radio-button choice.
    int lastValueSet;   // Value from last clock or alarm set op.
    
    /**
     * Get check-box state.
     */
    public boolean getAlarmFlag() {
        return alarmOn;
    }
    
    /**
     * Get radio-buttons choice.
     */
    public int getChoice() {
        return choice;
    }
    
    /**
     * When getChoice returns a new choice, and the previous choice
     * was either SET_ALARM or SET_TIME, the set-value of the display
     * is returned in the format hhmmss where h, m, and s denotes 
     * hours, minutes, and seconds digits respectively. This means,
     * for example, that the hour value is obtained by dividing the
     * return value by 10000.
     */
    public int getValue() {
        return lastValueSet;
    }   
}
