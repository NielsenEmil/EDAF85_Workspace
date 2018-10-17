package queue;

class CustomerHandler extends Thread {
	YourMonitor mon;

	CustomerHandler(YourMonitor sharedData) { mon = sharedData; }

	public void run() {
		while (true) {
			HW.waitCustomerButton();
			int qNum = mon.customerArrived();
			HW.printTicket(qNum);
		}
	}
}
