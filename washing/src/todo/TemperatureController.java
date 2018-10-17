package todo;


import se.lth.cs.realtime.*;
import done.AbstractWashingMachine;


public class TemperatureController extends PeriodicThread {
	private int mode;
	private AbstractWashingMachine mach;
	private double speed;
	private double targetTemp,limitOn,limitOff;
	private RTThread source;
	private boolean sentAck;
	private boolean heaterOn;
	private long cycle;

	public TemperatureController(AbstractWashingMachine mach, double speed) {
		super((long) (1000/speed));
		this.mach = mach;
		this.speed = speed;
		mode = TemperatureEvent.TEMP_IDLE;
	}

	public void perform() {
		TemperatureEvent msg = (TemperatureEvent) 
		this.mailbox.tryFetch();
		if(msg != null){
			mode = msg.getMode();
			source = (RTThread) msg.getSource();
			targetTemp = msg.getTemperature();
			
			switch(mode) {
			case TemperatureEvent.TEMP_IDLE:
				mach.setHeating(false);
				break;
			case TemperatureEvent.TEMP_SET:
				mach.setHeating(true);
				heaterOn = true;
				limitOn = targetTemp - 1.8;
				limitOff = targetTemp - 1.2;
				sentAck = false;
				cycle = System.currentTimeMillis();
			}
		}
		
		if(source != null && !source.isAlive()){
			mach.setHeating(false);
			heaterOn = false;
			mode = TemperatureEvent.TEMP_IDLE;
		}
			
		if(mode == TemperatureEvent.TEMP_SET && System.currentTimeMillis() >= (cycle + 2000/speed)){
			cycle = System.currentTimeMillis();
			
			if(heaterOn && mach.getTemperature() > limitOff){
				mach.setHeating(false);
				heaterOn = false;
				if(!sentAck){
					source.putEvent(new AckEvent(this));
					sentAck = true;
				}
			} else if (mach.getTemperature() < limitOn){
				mach.setHeating(true);
				heaterOn = true;
			}
		}
		
	}
}