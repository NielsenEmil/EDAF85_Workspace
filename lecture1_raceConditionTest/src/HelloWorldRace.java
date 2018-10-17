/**
 * Small test program to illustrate race conditions
 * Depending on computation power available, it is difficult to observe 
 * the effects without extra sleep time...
 * 
 * Feel free to play!
 * 2016-08-15, Elin A. Topp
 */
class HelloWorldRace extends Thread {

	// using default constructor, hence no need to write one
	
	public void run() {
		// use the sleep-sequence to play around, test what happens!

		try {
			sleep( 1000);
		} catch (InterruptedException e) {
			System.out.println("Could not sleep!");
		}

		System.out.print( " and goodbye");
	}

	public static void main( String[] arg) {
		System.out.print( "Hello");
		// see difference between calling run or start!
		new HelloWorldRace().run();
		//new HelloWorldRace().start();
/*
		try {
			sleep( 0, 1);
		} catch (InterruptedException e) {
			System.out.println("Could not sleep!");
		}
*/
		System.out.println( " World!");
	}
}