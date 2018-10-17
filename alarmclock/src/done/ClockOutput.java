package done;


public class ClockOutput {

    private ClockTimeDisplay theDisplay;
    
    ClockOutput(ClockTimeDisplay display) {
        theDisplay = display;
    }
    
    /** 
     * Wake-up clock user.
     */
    public void doAlarm() {
        System.out.println("Beep!");
        theDisplay.setAlarmPulse(true);
        try {Thread.sleep(300);} catch (InterruptedException e) {}
        theDisplay.setAlarmPulse(false);
    }
    
    /**
     * If the display is currently used to display the time, update it.
     * If user is using display for setting clock or alarm time, do
     * nothing when with actual hardware or show info when simulating.
     */
    public void showTime(int hhmmss) {
        theDisplay.setTime(hhmmss);
    }
}
