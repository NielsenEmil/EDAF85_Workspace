/**
 * Small test program according to lecture example by Roger Henriksson.
 * Illustrates effect of mutex semaphore(s).
 * 
 * Feel free to play!
 * 2016-08-15, Elin A. Topp
 */
import se.lth.cs.realtime.semaphore.*;

class SemShowThread extends Thread 
{ 
  String theName; 
  Semaphore theSem; 

  public SemShowThread( String n, Semaphore s){ 
    theName = n; 
    theSem = s;
  } 

  public void run() {
	// see what happens when you change the position of the take / give
	theSem.take(); 
    for(int t=1;t<=10;t++) {
//      theSem.take(); 
      System.out.println(theName + ": " + t);
//      theSem.give();
      try{
      	sleep( 1);
      } catch ( InterruptedException e) { System.out.println( "?");}

    }
    theSem.give();
  }
  

// main method put here for convenience
  public static void main(String[] args) {

	Thread t1,t2; 
	Semaphore s; 
	s = new MutexSem(); 

	t1 = new SemShowThread("Thread one",s); 
	t2 = new SemShowThread("Thread two",s); 
	t1.start(); 
	t2.start();
  }
}