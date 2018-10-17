class Consumer extends Thread {
	
	private String name;
	private long sleepTime;
	
	MyBuffer theBuffer;
	
	public Consumer( String name, long sleepTime, MyBuffer theBuffer) {
		this.name = name;
		this.sleepTime = sleepTime;
		this.theBuffer = theBuffer;
		
	}
	
	public void run() {
		int count = 1;
		try{
			while( true){
				sleep( sleepTime);
				String text = theBuffer.fetch();
				System.out.println( name + " received: " + count++ + " " + text + " from buffer");
			}
		} catch( InterruptedException e) { 
			System.out.println( name + " terminating after interrupt");
		}
		
	}
	
}