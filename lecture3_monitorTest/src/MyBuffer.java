class MyBuffer {
	
	private String[] buffData;
	private int nextToPut;
	private int nextToGet;
	private boolean bufferFull;
	private boolean bufferEmpty;
	private int numOfTexts;
	private int maxNumOfTexts;
	
	
	public MyBuffer( int buffSize) {
		bufferFull = false;
		bufferEmpty = true;
		
		nextToPut = 0;
		nextToGet = 0;
		numOfTexts = 0;
		
		buffData = new String[buffSize];
		maxNumOfTexts = buffSize;
	}
	
	public synchronized void post( String text) throws InterruptedException{
		while( bufferFull) {
			System.out.println( "buffer full! " + Thread.currentThread()); 
			wait(); 
		}
		if( bufferEmpty) { 
			notifyAll();
			System.out.println( "buffer had been empty, notifying! " + Thread.currentThread()); 
			bufferEmpty = false;
		}
		buffData[nextToPut] = new String( text);
		if( ++numOfTexts >= maxNumOfTexts) bufferFull = true;
		if( ++nextToPut == maxNumOfTexts) nextToPut = 0;
	}
	
	public synchronized String fetch() throws InterruptedException {
		while( bufferEmpty) {
			System.out.println( "buffer empty! " + Thread.currentThread()); 
			wait();
		}
		if( bufferFull){
			notifyAll();
			System.out.println("buffer had been full, notifying! " + Thread.currentThread());
			bufferFull = false;
		}
		String res = buffData[nextToGet];

		buffData[nextToGet] = null;
		if( --numOfTexts < 1) bufferEmpty = true;
		if( ++nextToGet == maxNumOfTexts) nextToGet = 0;
		
		return res;
	}
	
	
}