package queue;

class YourMonitor {
	private int nCounters;
	// Put your attributes here...

	YourMonitor(int n) { 
		nCounters = n;
		// Initialize your attributes here...
	}

	/**
	 * Return the next queue number in the intervall 0...99. 
	 * There is never more than 100 customers waiting.
	 */
	synchronized int customerArrived() { 
		// Implement this method...
		return 0;
	}

	/**
	 * Register the clerk at counter id as free. Send a customer if any. 
	 */
	synchronized void clerkFree(int id) { 
		// Implement this method...
	}

	/**
	 * Wait for there to be a free clerk and a waiting customer, then
	 * return the cueue number of next customer to serve and the counter 
	 * number of the engaged clerk.
	 */
	synchronized DispData getDisplayData() throws InterruptedException { 
		// Implement this method...
		return null;
	}
}
