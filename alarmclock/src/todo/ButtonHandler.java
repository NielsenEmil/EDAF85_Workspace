package todo;

import se.lth.cs.realtime.semaphore.Semaphore;

public class ButtonHandler extends Thread {
	
	private Data data;
	private Semaphore sem;

	public ButtonHandler(Data data, Semaphore sem) {
		this.data = data;
		this.sem = sem;
	}

	public void run() {
		while(true) {
			sem.take();
			data.checkButtons();
		}
	}

}
