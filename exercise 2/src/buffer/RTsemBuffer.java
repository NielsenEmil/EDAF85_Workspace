package buffer;

/**
 * Simple producer/consumer example using semaphores.
 * The complete example, including all classes, is put in one outer class.
 * That lets us keep it all in one file. (Not good for larger programs.)
 */
public class RTsemBuffer {
	/**
	 * Static stuff which permits the class to be called as a program.
	 */
	public static void main(String args[]) {
		// Since this is a static function, we can only access static data.
		// Therefore, create an instance of this class; run constructor:
		new RTsemBuffer();
	}

	/**
	 * Constructor which in this example acts as the main program.
	 */
	public RTsemBuffer () {
		Buffer buff = new Buffer();
		Producer p = new Producer(buff);
		Consumer c = new Consumer(buff);
		c.start();
		p.start();
		System.out.println("\n\n"+"RTsemBuffer: Threads are running ...");
		try {
			p.join();
			// Give consumer 10s to complete its work, then stop it.
			Thread.sleep(10000);
			c.interrupt(); // Tell consumer to stop.
			c.join(); // Wait until really stopped.
		}
		catch (InterruptedException e) {/* Continue termination...*/};
		System.out.println("\n"+"RTsemBuffer: Execution completed!");
	}
} // RTsemBuffer
