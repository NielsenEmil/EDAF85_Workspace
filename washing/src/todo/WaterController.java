package todo;


import se.lth.cs.realtime.*;
import done.AbstractWashingMachine;


public class WaterController extends PeriodicThread {
	private AbstractWashingMachine mach;
	private int mode = WaterEvent.WATER_IDLE;
	private double targetLevel;
	private RTThread source;
	
	public WaterController(AbstractWashingMachine mach, double speed) {
		super((long) (1000/speed));
		this.mach = mach;
	}

	public void perform() {
		WaterEvent msg = (WaterEvent) this.mailbox.tryFetch();
		if(msg != null){
			mode = msg.getMode();
			targetLevel = msg.getLevel();
			source = (RTThread) msg.getSource();
			
			switch(mode) {
			case WaterEvent.WATER_FILL:
				// CMLA don't fill and drain simultaneously
				mach.setFill(true);
				mach.setDrain(false);
				break;
			case WaterEvent.WATER_DRAIN:
				// CMLA don't fill and drain simultaneously
				mach.setFill(false);
				mach.setDrain(true);
			}
		}
		
		if(source != null && !source.isAlive()){
			mach.setDrain(false);
			mach.setFill(false);
			mode = WaterEvent.WATER_IDLE;
		}
			
		
		if (mode == WaterEvent.WATER_FILL && mach.getWaterLevel() >= targetLevel) {
			mach.setFill(false);
			mode = WaterEvent.WATER_IDLE;
			source.putEvent(new AckEvent(this));
		} else if (mode == WaterEvent.WATER_DRAIN && mach.getWaterLevel() <= targetLevel) {
			mach.setDrain(false);
			mode = WaterEvent.WATER_IDLE;
			source.putEvent(new AckEvent(this));
		}
	}
}