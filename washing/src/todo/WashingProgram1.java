package todo;
import done.*;

public class WashingProgram1 extends WashingProgram {

	protected WashingProgram1(AbstractWashingMachine mach, double speed, TemperatureController tempController,
			WaterController waterController, SpinController spinController) {
		super(mach, speed, tempController, waterController, spinController);
		
	}

	@Override
	protected void wash() throws InterruptedException {
		
		myMachine.setLock(true);
		
		myWaterController.putEvent(new WaterEvent(this,WaterEvent.WATER_FILL,0.5));
		mailbox.doFetch();
		
		// CMLA Water fill before heating
		myTempController.putEvent(new TemperatureEvent(this,TemperatureEvent.TEMP_SET,60));
		mailbox.doFetch();
		
		mySpinController.putEvent(new SpinEvent(this,SpinEvent.SPIN_SLOW));
		
		sleep((long)((1800 * 1000)/mySpeed));

		mySpinController.putEvent(new SpinEvent(this,SpinEvent.SPIN_OFF));
		
		myTempController.putEvent(new TemperatureEvent(this,TemperatureEvent.TEMP_IDLE,0));
		
		myWaterController.putEvent(new WaterEvent(this,WaterEvent.WATER_DRAIN,0));
		mailbox.doFetch();
		
		for(int i = 0; i < 5;i++){
			myWaterController.putEvent(new WaterEvent(this,WaterEvent.WATER_FILL,0.5));
			mailbox.doFetch();
			sleep((long)((120 * 1000)/mySpeed));
			myWaterController.putEvent(new WaterEvent(this,WaterEvent.WATER_DRAIN,0));
			mailbox.doFetch();
		}
		
		// CMLA Drain before centrifuge
		mySpinController.putEvent(new SpinEvent(this,SpinEvent.SPIN_FAST));
		sleep((long)((300 * 1000)/mySpeed));
		mySpinController.putEvent(new SpinEvent(this,SpinEvent.SPIN_OFF));
		
		// CMLA no water when opening hatch
		if(myMachine.getWaterLevel() > 0) {
			myWaterController.putEvent(new WaterEvent(this,WaterEvent.WATER_DRAIN,0));
			mailbox.doFetch();
		}
		
		myMachine.setLock(false);
	}

}