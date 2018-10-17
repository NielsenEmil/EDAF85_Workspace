
class Producer extends Thread {
	
	private String name;
	private long sleepTime;
	private int maxRuns;
	
	MyBuffer theBuffer;
	
	public Producer( String name, long sleepTime, int maxRuns, MyBuffer theBuffer) {
		this.name = name;
		this.sleepTime = sleepTime;
		this.theBuffer = theBuffer;
		this.maxRuns = maxRuns;
		
	}
	
	public void run() {
		int count = 1;
		try{
			while( count <= maxRuns){
				String text = new String( name + "text" + count++);
				System.out.println( name + " sending: " + text + " to buffer");

				theBuffer.post( text);
				sleep( sleepTime);
			}
		} catch(InterruptedException e) {}
	}
	
}