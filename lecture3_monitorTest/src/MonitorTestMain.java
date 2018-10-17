/**
 * A little more complex test program to illustrate a producer-consumer
 * relationship via buffer 
 *
 * Feel free to play!
 * 2016-08-15, Elin A. Topp
 */

class MonitorTestMain {
	public static void main(String[] args) {
		
		Producer prod0, prod1;
		Consumer cons0, cons1;
		MyBuffer buff1;
		
		buff1 = new MyBuffer( 4);
		
		prod0 = new Producer( "prod0", 100, 10, buff1);
		prod1 = new Producer( "prod1", 100, 10, buff1);
		
		cons0 = new Consumer( "cons0", 0, buff1);
		cons1 = new Consumer( "cons1", 0, buff1);
		
		System.out.println( prod0 + " is prod0");
		System.out.println( prod1 + " is prod1");
		System.out.println( cons0 + " is cons0");
		System.out.println( cons1 + " is cons1");
		prod0.start();
		prod1.start();
		
		cons0.start();
		cons1.start();
		
		try {
			prod0.join();
			prod1.join();
			
			Thread.sleep( 10000);
			cons0.interrupt();
			cons1.interrupt();
			cons0.join();
			cons1.join();
		} catch(Exception e){};
	}
}