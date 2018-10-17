package todo;

import done.*;

public class WashingController implements ButtonListener {	
	// TODO: add suitable attributes
	private AbstractWashingMachine mach;
	private WashingProgram washP;
	private double speed;
	
	private SpinController spinC;
	private TemperatureController tempC;
	private WaterController waterC;
	
    public WashingController(AbstractWashingMachine theMachine, double theSpeed) {
		mach = theMachine;
		speed = theSpeed;
		
		spinC = new SpinController(mach,speed);
		tempC = new TemperatureController(mach,speed);
		waterC = new WaterController(mach,speed);
		
		spinC.start();
		tempC.start();
		waterC.start();
    }

    public void processButton(int theButton) {

    	if(washP != null && theButton != 0){
    		return;
    	} else if (washP != null && theButton == 0){
    		washP.interrupt();
    		washP = null;
    	}
    	
    	switch(theButton){
    	case 1:
    		washP = new WashingProgram1(mach,speed,tempC,waterC,spinC);
    		washP.start();
    		break;
    	case 2:
    		washP = new WashingProgram2(mach,speed,tempC,waterC,spinC);
    		washP.start();
    		break;
    	case 3:
    		washP = new WashingProgram3(mach,speed,tempC,waterC,spinC);
    		washP.start();
    		break;
    	}
    	
    	
    }
}