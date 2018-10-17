package queue;

public class HW {
	static void waitClerkButton(int id) {
		long sleep = (long)(Math.random() * 60000);
		try {
			Thread.sleep(sleep);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	static void waitCustomerButton() {
		long sleep = (long)(Math.random() * 20000);
		try {
			Thread.sleep(sleep);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	static void printTicket(int qNum) {
		System.out.println("TICKET: customer got ticket "+qNum);
	}
	
	static void display(int ticket, int counter) {
		System.out.println("DISPLAY: counter "+counter+" now handling ticket "+ticket);
	}
}
