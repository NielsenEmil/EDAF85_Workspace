package done;
import javax.swing.*;


import java.awt.event.*;
import java.awt.*;


public class LiftView {

	private JFrame view;
	private FixedSizePanel entryPane, shaftPane, exitPane;
//	private FloorExit exitPane;
	private LevelDrawer levels;
	private BasketDrawer basket;
	private static int FLOOR_HEIGHT = 100;
	private static int ENTRY_WIDTH = 300;
	private static int EXIT_WIDTH = 200;
	private static int SHAFT_WIDTH = 150;
	private static int NO_OF_FLOORS = 7;
	private static int MAX_LOAD = 4;
	//private FloorEntry[] floorIn;

	public LiftView() {
		view = new JFrame("LiftView");
		view.getContentPane().setLayout(new BorderLayout());
		WindowListener wl = new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		};
		view.addWindowListener(wl);
		view.setResizable(false);

		entryPane = new FixedSizePanel(ENTRY_WIDTH,NO_OF_FLOORS*FLOOR_HEIGHT);
		entryPane.setLayout(new GridLayout(NO_OF_FLOORS,1));
		view.getContentPane().add("West",entryPane);

		exitPane = new FixedSizePanel(EXIT_WIDTH,NO_OF_FLOORS*FLOOR_HEIGHT);
		exitPane.setLayout(new GridLayout(NO_OF_FLOORS,1));
		view.getContentPane().add("East",exitPane);

		levels = new LevelDrawer( ENTRY_WIDTH, EXIT_WIDTH, NO_OF_FLOORS, FLOOR_HEIGHT, entryPane, exitPane);
		
		shaftPane = new FixedSizePanel(SHAFT_WIDTH,NO_OF_FLOORS*FLOOR_HEIGHT);
		shaftPane.setBackground(Color.LIGHT_GRAY);
		shaftPane.setLayout(null);
		view.getContentPane().add("Center",shaftPane);
		basket = new BasketDrawer(SHAFT_WIDTH,NO_OF_FLOORS,FLOOR_HEIGHT,MAX_LOAD,shaftPane);

		view.pack();
		view.setVisible(true);
	}

	public BasketDrawer getBasketDrawer() {
		return basket;
	}

	public LevelDrawer getLevelDrawer() {
		return levels;
	}
	
	
	public static void main(String[] args) {
		LiftView lv = new LiftView();
		LevelDrawer lD = lv.getLevelDrawer();
		BasketDrawer bD = lv.getBasketDrawer();
		
		bD.drawLift(0,3);
		lD.drawWaiting(5,4);
		try {
			Thread.sleep(1000);
		} catch(InterruptedException e) { }
		bD.moveLift(0,1);
		bD.drawLift(1,2);
		lD.drawLeaving(1);
	}


}

