package todo;

import done.*;
import se.lth.cs.realtime.semaphore.Semaphore;
import se.lth.cs.realtime.semaphore.MutexSem;

public class AlarmClock extends Thread {

	private static ClockInput	input;
	private static ClockOutput	output;
	private static Semaphore	sem; 
	private Data data;
	private ButtonHandler buttonHandler;

	public AlarmClock(ClockInput i, ClockOutput o) {
		input = i;
		output = o;
		sem = input.getSemaphoreInstance();
		data = new Data(i, o, sem, new MutexSem());
		buttonHandler = new ButtonHandler(data, sem);
		
	}

	// The AlarmClock thread is started by the simulator. No
	// need to start it by yourself, if you do you will get
	// an IllegalThreadStateException. The implementation
	// below is a simple alarmclock thread that beeps upon 
	// each keypress. To be modified in the lab.
	public void run() {
		//ButtonHandler thread
		buttonHandler.start();
		
		//Clockcounter thread
		long tempTime, diff;
		tempTime = System.currentTimeMillis();
		while (true) {
			tempTime += 1000; 
			data.countTime();
			diff = tempTime - System.currentTimeMillis();

			try {
				Thread.sleep(diff);
			} catch (Exception e) {
				System.err.println("Något fel på sleep i counter");
				e.printStackTrace();
			}
		}

	}
}
