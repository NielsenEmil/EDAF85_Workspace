/**
 * Mailbox-communication example
 * Illustrates handling of simple messages (note the "one-way knowledge" about 
 * communication partners).
 * These messages are not "safe" regarding owner checks, etc. This is the case with the 
 * RTEvent message class!
 * 
 * Feel free to play!
 * 2016-08-15, Elin A. Topp
 */

class MailingMain {
	
	public static void main( String[] args) {
		
		MailingThread alice, bob, cecilia, pippi;
		
		alice = new MailingThread( "Alice", 2);
		bob = new MailingThread( "Bob", 2);
		cecilia = new MailingThread( "Cecilia", 2);
		pippi = new MailingThread( "Pippi", 2);
		
		alice.initComPartner( bob);
		bob.initComPartner( cecilia);
		cecilia.initComPartner( alice);
		// skipping Pippi, as she likes to write letters to herself, doesn't she?
		
		alice.start();
		bob.start();
		cecilia.start();
		pippi.start();
		
	}
	
}