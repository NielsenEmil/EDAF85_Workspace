import se.lth.cs.realtime.semaphore.*;

class CircleTrain extends Thread {
	private String name;
	private MutexSem[] stops;
	private long[] stopTimes;
	private int firstStop;
	
	public CircleTrain( String name, MutexSem a, MutexSem b, MutexSem c, MutexSem d,
						long general, long spec1, long spec2, int first) {
		stops = new MutexSem[4];
		stops[0] = a;
		stops[1] = b;
		stops[2] = c;
		stops[3] = d;
		
		stopTimes = new long[4];
		stopTimes[0] = general;
		stopTimes[1] = spec1;
		stopTimes[2] = general;
		stopTimes[3] = spec2;
		firstStop = first;
		
		this.name = name;
	}
	
	public void run() {
		int current = firstStop;
		int next;
		stops[current].take();
		while( !interrupted()) {
			try {
				sleep(stopTimes[current]);
			} catch( InterruptedException e){}
			if( (next = current+1) >= 4) next = 0;
			stops[next].take();
			System.out.println( name + " leaving stop " + current);
			System.out.flush();
			stops[current].give();
			current = next;
			System.out.println( name + " arrived at stop " + current);
			System.out.flush();
			
		}
	
	}
}