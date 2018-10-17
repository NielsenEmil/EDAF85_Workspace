package buffer;

/**
 * The Consumer.
 */
class Consumer extends Thread {
	Buffer theBuffer;
	Consumer(Buffer b) {
		super();
		theBuffer = b;
	}
	public void run() {
		try {
			sleep(10000); // 10s until work starts.
			while (true) {
				System.out.println(theBuffer.getLine());
			}
		}
		catch (Exception e) {/* Let thread terminate. */};
	} // run
} // Consumer

