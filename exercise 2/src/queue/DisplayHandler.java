package queue;

class DisplayHandler extends Thread {
	YourMonitor mon;
	DispData disp;

	DisplayHandler(YourMonitor sharedData) { mon = sharedData; }

	public void run() { 
		while (true) {
			try {
				disp = mon.getDisplayData();
				HW.display(disp.ticket, disp.counter);
				sleep(10000);
			} catch (InterruptedException e) { break; }
		}
	}
}
