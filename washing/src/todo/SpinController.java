package todo;


import se.lth.cs.realtime.*;
import done.AbstractWashingMachine;


public class SpinController extends PeriodicThread {
	// TODO: add suitable attributes
	private int mode;
	private AbstractWashingMachine mach;
	private double speed;
	private int direction;
	private long cycle;
	private RTThread source;

	public SpinController(AbstractWashingMachine mach, double speed) {
		super((long) (1000/speed));
		this.mach = mach;
		this.speed = speed;
		
	}

	public void perform() {
		SpinEvent msg = (SpinEvent) this.mailbox.tryFetch();
		if(msg != null){
			mode = msg.getMode();
			source =(RTThread) msg.getSource();
			
			switch (mode) {
			case SpinEvent.SPIN_SLOW:
				direction = AbstractWashingMachine.SPIN_LEFT;
				mach.setSpin(direction);
				break;
			case SpinEvent.SPIN_FAST:
				mach.setSpin(AbstractWashingMachine.SPIN_FAST);
				break;
			case SpinEvent.SPIN_OFF:
				mach.setSpin(AbstractWashingMachine.SPIN_OFF);
				break;
			}
		}
		
		if(source != null && !source.isAlive()){
			mode = SpinEvent.SPIN_OFF;
			mach.setSpin(AbstractWashingMachine.SPIN_OFF);
		}
		
		if(mode == SpinEvent.SPIN_SLOW && System.currentTimeMillis() > (cycle + ((1000*60)/speed))){
			cycle = System.currentTimeMillis();
			if(direction == AbstractWashingMachine.SPIN_LEFT){
				direction = AbstractWashingMachine.SPIN_RIGHT;
			} else if(direction == AbstractWashingMachine.SPIN_RIGHT){
				direction = AbstractWashingMachine.SPIN_LEFT;
			}
			mach.setSpin(direction);
		}
	}
}