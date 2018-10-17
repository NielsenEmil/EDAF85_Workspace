package buffer;

/**
 * The producer.
 */
class Producer extends Thread {
	Buffer theBuffer;
	Producer(Buffer b) {
		super(); // Construct the actual thread object.
		theBuffer = b;
	}
	public void run() {
		String producedData = "";
		try {
			while (true) {
				if (producedData.length()>75) break;
				producedData = new String("Hi! "+producedData);
				sleep(1000); // It takes a second to obtain data.
				theBuffer.putLine(producedData);
			}
		}
		catch (Exception e) {
			// Just let thread terminate (i.e., return from run).
		}
	} // run
} // Producer
