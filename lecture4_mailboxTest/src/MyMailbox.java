class MyMailbox {
	
	private MyMessage[] buffData;
	private int nextToPut;
	private int nextToGet;
	private boolean bufferFull;
	private boolean bufferEmpty;
	private int numOfObjs;
	private int maxNumOfObjs;
	
	
	public MyMailbox( int numOfPlaces) {
		bufferFull = false;
		bufferEmpty = true;
		
		nextToPut = 0;
		nextToGet = 0;
		numOfObjs = 0;
		
		buffData = new MyMessage[numOfPlaces];
		maxNumOfObjs = numOfPlaces;
	}
	
	public synchronized void doPost( MyMessage msg) {
		while( bufferFull) 
			try {  
					wait(); 
				} catch (InterruptedException e) {};
		if( bufferEmpty) { 
			notifyAll();
			//System.out.println( "buffer had been empty, notifying! " + Thread.currentThread() + "\n"); 
			bufferEmpty = false;
		}
		buffData[nextToPut] = msg;
		if( ++numOfObjs >= maxNumOfObjs) bufferFull = true;
		if( ++nextToPut == maxNumOfObjs) nextToPut = 0;
	}

	public synchronized MyMessage tryPost( MyMessage msg) {
		if( bufferFull) {
			System.out.println( "buffer is full, rejecting! "); 
			return msg;
		} else {
			if( bufferEmpty) { 
				notifyAll();
				bufferEmpty = false;
			}
			buffData[nextToPut] = msg;
			if( ++numOfObjs >= maxNumOfObjs) bufferFull = true;
			if( ++nextToPut == maxNumOfObjs) nextToPut = 0;
			return null;
		}
	}
	
	public synchronized MyMessage doFetch() {
		while( bufferEmpty) {
			try {  
				wait();
			
			} catch (InterruptedException e) {};
		}
		if( bufferFull){
			notifyAll();
			//System.out.println("buffer had been full, notifying! " + Thread.currentThread());
			bufferFull = false;
		}
		MyMessage res = buffData[nextToGet];
		//System.out.println( "getting text " + res);
		buffData[nextToGet] = null;
		if( --numOfObjs < 1) bufferEmpty = true;
		if( ++nextToGet == maxNumOfObjs) nextToGet = 0;
		
		return res;
	}
	
	public synchronized MyMessage tryFetch() {
		if( bufferEmpty) {
			System.out.println( "buffer is empty, no message received! "); 
			return null;	
		} else {
			if( bufferFull){
				notifyAll();
			//System.out.println("buffer had been full, notifying! " + Thread.currentThread());
				bufferFull = false;
			}
			MyMessage res = buffData[nextToGet];
			//System.out.println( "getting text " + res);
			buffData[nextToGet] = null;
			if( --numOfObjs < 1) bufferEmpty = true;
			if( ++nextToGet == maxNumOfObjs) nextToGet = 0;
		
			return res;
		}
	}
}