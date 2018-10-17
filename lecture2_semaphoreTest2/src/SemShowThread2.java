/**
 * Small test program according to lecture example by Roger Henriksson.
 * Illustrates effects of counting (signaling) semaphore(s).
 * 
 * Feel free to play!
 * 2016-08-15, Elin A. Topp
 */
import se.lth.cs.realtime.semaphore.*;

class SemShowThread2 extends Thread 
{ 
  String theName; 
  Semaphore mySem, theirSem; 

  public SemShowThread2( String n, 
		  CountingSem s1, CountingSem s2){ 
    theName = n; 
    mySem = s1;
    theirSem = s2;
  } 

  public void run() {
	for(int t=1;t<=10;t++) {
      mySem.take(); 
      System.out.println(theName + ": " + t);
      theirSem.give();
      try{
      	sleep(1);
      } catch ( InterruptedException e) { System.out.println( "?");}

    }
  }

//main method put here for convenience
  public static void main(String[] args) {

	Thread t1,t2; 
	CountingSem s1, s2; 
	// see what happens when you play with the startup setting!

	//s1 = new CountingSem(0); 
	//s2 = new CountingSem(1); 

	
	//s1 = new CountingSem( 1); 
	//s2 = new CountingSem( 1); 

	//s1 = new CountingSem(1); 
	//s2 = new CountingSem(0); 
	
	s1 = new CountingSem(0); 
	s2 = new CountingSem(0); 
		
		
	t1 = new SemShowThread2("Thread one", s1, s2); 
	t2 = new SemShowThread2("Thread two", s2, s1); 
	t1.start(); 
	t2.start();
  }
}