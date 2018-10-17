package todo;

public class Person extends Thread {
	private Monitor monitor;
	private int enter, dest;

	public Person(Monitor monitor) {
		this.monitor = monitor;
	}

	public void run() {
		while (true) {
			int delay = 1000 * ((int) (Math.random() * 46.0));
			try {
				sleep(delay);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			enter = ((int) (Math.random() * 7.0));
			dest = ((int) (Math.random() * 7.0));

			if (enter == dest) {
				if (enter == 6) {
					dest--;
				} else {
					dest++;
				}
			}

			monitor.enter(enter, dest);
			monitor.exit(dest);
		}
	}
}