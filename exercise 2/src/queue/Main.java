package queue;

public class Main {
	/**
	 * Static stuff which permits the class to be called as a program.
	 */
	public static void main(String args[]) {
		// Since this is a static function, we can only access static data.
		// Therefore, create an instance of this class; run constructor:
		new Main();
	}

	/**
	 * Constructor which in this example acts as the main program.
	 */
	public Main () {
		int nCounters = 3;
		
		YourMonitor mon = new YourMonitor(nCounters);
		DisplayHandler disp = new DisplayHandler(mon);
		ClerkHandler[] clerk = new ClerkHandler[nCounters];
		for (int i=0; i<nCounters; i++) clerk[i] = new ClerkHandler(mon, i);
		CustomerHandler dispenser = new CustomerHandler(mon);
		
		disp.start();
		for (int i=0; i<nCounters; i++) clerk[i].start();
		dispenser.start();
		
		System.out.println("\n\n"+"Main: Threads are running ...");
	}
} // RTsemBuffer
