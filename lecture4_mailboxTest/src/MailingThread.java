class MailingThread extends Thread {
	
	private String name;
	private MyMailbox mailbox;
	private int maxMsg;
	private MailingThread comPartner;
	
	
	public MailingThread( String name, int maxMsg) {
		this.name = name;
		mailbox = new MyMailbox(5);
		comPartner = this;
		this.maxMsg = maxMsg;
	}
	
	public void initComPartner( MailingThread c) {
		comPartner = c;
	}
	
	public void putMsg( MyMessage msg) {
		mailbox.doPost( msg);
	}
	
	public void run() {
		int count = 1;
		boolean waitingForAck = false;
		int ackId = -1;
		MyMessage msg1 = null;
		MyMessage ack = null;
		boolean sending = true;
		
		// exchange "sending" in the condition with "true", to see what happens in terms of how
		// the thread handles itself
		// while( true){
		while( sending){
			if( waitingForAck) {
				// would it work to do tryFetch instead of doFetch?
				// ack = mailbox.tryFetch();
				ack = mailbox.doFetch();
				if( ack.getMessage().startsWith( String.valueOf( ackId))) {
					System.out.println( name + " received ACK: " + ack.getMessage());
					waitingForAck = false;
					msg1 = null;
				} else {
					System.out.println( name + " still waiting for ack...");
					msg1 = ack;
				}
			} else {
				msg1 = mailbox.tryFetch();
			}
			
			if( msg1 != null) {
				System.out.println( name + " received id " + msg1.getId() + ": " + msg1.getMessage());
				if( msg1.needsAck()) {
				
					Object s = msg1.getSource();
					if( s instanceof MailingThread) {
						((MailingThread) s).putMsg( new MyMessage( this, msg1.getId() + " ACKed, " + name + " says hi back!", false));
						msg1 = null;
					}
				}
			} else {
				if( count > maxMsg) {
					sending = false;
				} else {
					System.out.println( name + ": sending " + count++);
					// change from true to false here to see the difference in message numbers and
					// how the "sending" variable then influences the behaviour
					// MyMessage msg2 = new MyMessage( this, name + " says hi!", false);
					MyMessage msg2 = new MyMessage( this, name + " says hi!", true);
					ackId = msg2.getId();
					waitingForAck = true;
					comPartner.putMsg( msg2);
				}
			}
			try{
				sleep( 2000);
			} catch(InterruptedException e) {}
		}
	}
	
}