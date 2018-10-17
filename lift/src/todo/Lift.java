package todo;

import done.LiftView;

public class Lift extends Thread {
	private LiftView liftView;
	private Monitor monitor;
	private int[] toDest;

	public Lift(Monitor monitor, LiftView liftView) {
		this.liftView = liftView;
		this.monitor = monitor;
	}

	public void run() {
		while (true) {
			toDest = monitor.move();
			liftView.getBasketDrawer().moveLift(toDest[0], toDest[1]);
		}
	}
}