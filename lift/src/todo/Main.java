package todo;

import done.LiftView;

public class Main {
	private static LiftView liftView;
	private static Monitor monitor;

	public static void main(String[] args) {
		liftView = new LiftView();
		monitor = new Monitor(liftView);

		new Lift(monitor, liftView).start();

		for (int i = 0; i < 20; i++) {
			new Person(monitor).start();
		}
	}
}