import java.util.EventObject;

class MyMessage extends EventObject {
	private static final long serialVersionUID = 1L;
	private static int msg_num = 0;
	
	private int id;
	
	String msg;
	boolean ack_request;
	
	public MyMessage( MailingThread sender, String msg, boolean ack_request) {
		super(sender);
		
		id = msg_num++;
		this.msg = msg;
		this.ack_request  = ack_request;
	}
	
	public int getId() { return id;}
	
	public String getMessage() { return msg;}
	
	public boolean needsAck() { return ack_request;}
	
}