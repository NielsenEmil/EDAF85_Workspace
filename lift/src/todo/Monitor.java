package todo;

import done.LiftView;

public class Monitor {
	private int here, next, load;
	private int[] waitEntry, waitExit;
	private boolean goingUp;
	private LiftView liftView;

	public Monitor(LiftView liftView) {
		this.liftView = liftView;
		waitEntry = new int[7];
		waitExit = new int[7];
		goingUp = true;
	}

	public synchronized int[] move() {
		here = next;
		while ((waitEntry[here] != 0 && load != 4) || waitExit[here] != 0) {
			try {
				notifyAll();
				wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		if (goingUp) {
			next++;
		} else {
			next--;
		}

		if (next == 6) {
			goingUp = false;
		} else if (next == 0) {
			goingUp = true;
		}
		notifyAll();
		return new int[] { here, next };
	}

	public synchronized void enter(int enter, int dest) {

		waitEntry[enter]++;
		liftView.getLevelDrawer().drawWaiting(enter, +1);
		notifyAll();

		while (enter != here || load == 4 || here != next) {
			try {
				wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		waitEntry[enter]--;
		waitExit[dest]++;
		load++;

		liftView.getBasketDrawer().drawLift(enter, load);
		liftView.getLevelDrawer().drawWaiting(enter, -1);
		notifyAll();
	}

	public synchronized void exit(int dest) {
		while (here != dest) {
			try {
				wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		waitExit[here]--;
		load--;

		liftView.getBasketDrawer().drawLift(here, load);
		liftView.getLevelDrawer().drawLeaving(here);
		notifyAll();

	}
}