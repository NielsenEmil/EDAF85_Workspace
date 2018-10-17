package queue;

class ClerkHandler extends Thread {
	YourMonitor mon;
	int id;

	ClerkHandler(YourMonitor sharedData, int id) { mon=sharedData; this.id=id; }

	public void run() { 
		while (true) {
			HW.waitClerkButton(id);
			mon.clerkFree(id); 
		}
	}
}
