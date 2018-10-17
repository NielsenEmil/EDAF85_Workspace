/**
 * Illustration of deadlocks in circular hold-wait conditions 
 * using trains, rail-chunks, and stations...
 * 
 * Feel free to play!
 * 2016-08-15, Elin A. Topp
 */

import se.lth.cs.realtime.semaphore.*;

class TrainTestMain {
	
	
	public static void main(String[] args) {
		MutexSem A, B, C, D, E, F;
		CircleTrain T1, T2, T3, T4;
		
		A = new MutexSem();
		B = new MutexSem();
		C = new MutexSem();
		D = new MutexSem();
		E = new MutexSem();
		F = new MutexSem();
		
		// change the "type" of the train by changing their route(s), play with the starting places 
		// or the stop times. See what happens when you start the threads...
		
		T1 = new CircleTrain("T1_ABDE", A, B, D, E, 1000, 1000, 1000, 0);
		T2 = new CircleTrain("T2_ACDE", A, B, D, E, 1000, 1000, 1000, 1);
		T3 = new CircleTrain("T3_ABDF", A, B, D, E, 1000, 1000, 1000, 2);
		T4 = new CircleTrain("T4_ACDF", A, B, D, E, 1000, 1000, 1000, 3);
		
		
		T1.start();
		T2.start();
		T3.start();
		T4.start();
	}
	
}