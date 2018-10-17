/*
 * Real-time and concurrent programming course, laboratory 3
 * Department of Computer Science, Lund Institute of Technology
 *
 * PP 980728 Created
 * PP 990924 Revised, now uses OngoingThread instead of java.lang.Thread
 * PP 991005 Revised, now has a surprise guest in the washing machine
 * MH 110927 Revised
 */

package done;

import java.awt.*;
import java.awt.event.*;
import java.util.*;

import se.lth.cs.realtime.*;

/**
 * A graphic visualization of a washing machine. Displays the
 * state of the machine in its own window. Runs in its own thread.
 */
class WashingView extends Frame implements Runnable {
	private static final long serialVersionUID = 1L;

	// -------------------------------------------------------- PUBLIC METHODS

	/**
	 * Initializes the view and creates a window for display.
	 *
	 * @param   mach   Simulation to display.
	 * @param   speed  Simulation speed.
	 */
	public WashingView(WashingMachineSimulation mach,
			double speed,
			double freakShowProbability) {
		super("Simulateour de la Machine a Laver Automatique des CMLA");
		myMachine              = mach;
		mySpeed                = speed;
		myFreakShowProbability = freakShowProbability;
		myRandom               = new Random();
		myThread               = new OngoingThread(this);

		// This is not time-critical stuff. Set low priority.
		//  	try {
		myThread.setPriority(Thread.MIN_PRIORITY);
		//  	}
		//  	catch (FixedPriorityException e) {
		//  	    System.err.println("WashingView has gone bananas: " + e);
		//  	    System.exit(-1);
		//  	}

		setLayout(null);      // Handle layout manually
		setSize(WINDOW_W, WINDOW_H);
		setBackground(Color.white);
		setForeground(Color.black);

		// Add four buttons, 0..3
		for(int i = 0; i < 4; i++) {
			Button b = new Button("" + i);
			b.setBounds(260 + i * 21, 142, 20, 20);
			add(b);
			b.addActionListener(myMachine);
		}

		// Add labels for temperature, water level, time, and heat on/off
		myWaterLabel = new Label();
		add(myWaterLabel);
		myWaterLabel.setBounds(WATERLABEL_X,
				WATERLABEL_Y,
				WATERLABEL_W,
				WATERLABEL_H);

		myTempLabel = new Label();
		add(myTempLabel);
		myTempLabel.setBounds(TEMPLABEL_X,
				TEMPLABEL_Y,
				TEMPLABEL_W,
				TEMPLABEL_H);

		myTimeLabel = new Label("", Label.CENTER);
		add(myTimeLabel);
		myTimeLabel.setBounds(TIMELABEL_X,
				TIMELABEL_Y,
				TIMELABEL_W,
				TIMELABEL_H);

		myHeatingLabel = new Label("", Label.CENTER);
		add(myHeatingLabel);
		myHeatingLabel.setBounds(HEATINGLABEL_X,
				HEATINGLABEL_Y,
				HEATINGLABEL_W,
				HEATINGLABEL_H);
		myHeatingLabel.setForeground(Color.red);

		// Load some cool images
		MediaTracker mt = new MediaTracker(this);
		int imageID = 0;

		myMachineImage = Toolkit.getDefaultToolkit().getImage(
				Toolkit.class.getResource(IMAGE_PATH
						+ "machine.gif"));
		mt.addImage(myMachineImage, imageID++);

		myLockedImage = Toolkit.getDefaultToolkit().getImage(
				Toolkit.class.getResource(IMAGE_PATH
						+ "locked.gif"));
		mt.addImage(myLockedImage, imageID++);

		myUnlockedImage = Toolkit.getDefaultToolkit().getImage(
				Toolkit.class.getResource(IMAGE_PATH
						+ "unlocked.gif"));
		mt.addImage(myUnlockedImage, imageID++);

		myPipeInImage = Toolkit.getDefaultToolkit().getImage(
				Toolkit.class.getResource(IMAGE_PATH
						+ "pipein.gif"));
		mt.addImage(myPipeInImage, imageID++);

		myPipeInActiveImage = Toolkit.getDefaultToolkit().getImage(Toolkit.class.getResource(
				IMAGE_PATH
				+ "pipeinactive.gif"));
		mt.addImage(myPipeInActiveImage, imageID++);

		myPipeOutImage = Toolkit.getDefaultToolkit().getImage(Toolkit.class.getResource(
				IMAGE_PATH
				+ "pipeout.gif"));
		mt.addImage(myPipeOutImage, imageID++);

		myPipeOutActiveImage = Toolkit.getDefaultToolkit().getImage(Toolkit.class.getResource(
				IMAGE_PATH
				+ "pipeoutactive.gif"));
		mt.addImage(myPipeOutActiveImage, imageID++);

		myFireImage = Toolkit.getDefaultToolkit().getImage(Toolkit.class.getResource(
				IMAGE_PATH
				+ "fire.gif"));
		mt.addImage(myFireImage, imageID++);

		myFishImage = Toolkit.getDefaultToolkit().getImage(Toolkit.class.getResource(
				IMAGE_PATH
				+ "fish.gif"));
		mt.addImage(myFishImage, imageID++);

		myTumblerImages = new Image[SPIN_CYCLE * 2];
		for (int i = 0; i < SPIN_CYCLE * 2; i++) {
			myTumblerImages[i]
					= Toolkit.getDefaultToolkit().getImage(Toolkit.class.getResource(
							IMAGE_PATH + "tumbler" + i
							+ ".gif"));
			mt.addImage(myTumblerImages[i], imageID++);
		}

		// Load images once and for all
		try {
			mt.waitForAll();
		}
		catch (InterruptedException ie) {
			System.err.println("Interrupted while loading images. "
					+ "Will continue anyway.");
			System.err.println(ie.toString());
		}

		// Set clever values for cached simulation state
		cachedTemp = -1;                       // Force update
		cachedWaterLevelBarSize = -1;          // Force update

		// Kick off
		setVisible(true);   
		enableEvents(AWTEvent.WINDOW_EVENT_MASK);
		myThread.start();
	}

	/**
	 * Update lock display. Creates a Graphics object g, calls
	 * updateLock(g), and disposes the Graphics object.
	 */
	public void updateLock() {
		Graphics g = getGraphics();
		updateLock(g);
		g.dispose();
	}

	/**
	 * Update fill tap display. Creates a Graphics object g, calls
	 * updateFill(g), and disposes the Graphics object.
	 */
	public void updateFill() {
		Graphics g = getGraphics();
		updateFill(g);
		g.dispose();
	}

	/**
	 * Update drain pump display. Creates a Graphics object g, calls
	 * updateDrain(g), and disposes the Graphics object.
	 */
	public void updateDrain() {
		Graphics g = getGraphics();
		updateDrain(g);
		g.dispose();
	}

	/**
	 * Update tumbler display. Creates a Graphics object g, calls
	 * updateTumbler(g), and disposes the Graphics object.
	 */
	public void updateTumbler() {
		Graphics g = getGraphics();
		updateTumbler(g);
		g.dispose();
	}

	// ---------------------------------------------------- OVERRIDDEN METHODS

	/**
	 * Update the graphic view according to given washing machine.
	 * @param g The Graphics object provided by AWT.
	 */
	public void paint(Graphics g) {

		g.drawImage(myMachineImage, MACHINE_X, MACHINE_Y,
				Color.lightGray, this);

		// Draw frames for temperature and water level bars
		g.drawRoundRect(TEMP_X - 3, TEMP_Y - 3, 206, 10, 3, 3);
		g.drawRoundRect(WATERLEVEL_X - 3, WATERLEVEL_Y - 3, 206, 10, 3, 3);
		g.setColor(Color.black);

		// Display state
		if (myMachine != null) {
			updateTemperature(g);
			updateWaterLevel(g);
			updateLock(g);
			updateFill(g);
			updateDrain(g);
			updateTumbler(g);
			updateHeating();
			updateTime();
		}
	}

	/**
	 * Handle window closing. Calls super method.
	 * @param e The WindowEvent provided by AWT.
	 */
	public void processWindowEvent(WindowEvent e) {
		super.processWindowEvent(e);
		if (e.getID() == WindowEvent.WINDOW_CLOSING)
			System.exit(0);
	}

	/**
	 * Handle the thread stuff. Call updatePeriodic every
	 * now and then. When things go wrong, be sarcastic.
	 */
	public void run() {
		boolean hot;
		boolean wet;

		t0 = System.currentTimeMillis();

		try {
			do {
				updatePeriodic();
				RTThread.sleep(SAMPLING_INTERVAL);

				hot = myMachine.isOverHeating();
				wet = myMachine.isOverFlowing();
			} while (!hot && !wet);

			updatePeriodic();             // Get the very latest updates

			// Something went wrong. Be sarcastic.
			if (hot) {
				// Show some fire

				SarcasticDialog sd = new SarcasticDialog(this,
						"Sacre Bleu!!!",
						"Un catastrophe grande de la Feu avec cravatte rouge.",
						myFireImage,
						106, 140, Color.orange, Color.black);
				sd.setVisible(true);
				// We never get here - the dialog kills the application.
			}
			else {
				// Show some fish

				SarcasticDialog sd = new SarcasticDialog(this,
						"Sacre Bleu!!!",
						"Un catastrophe grande de la Aqua maritime cote d'Azur.",
						myFishImage,
						118, 140, Color.blue, Color.white);
				sd.setVisible(true);
			}
		}
		catch (Exception e) { /* Shouldn't happen */ }
	}

	// ------------------------------------------------------- PRIVATE METHODS

	/*
	 * Update water and temperature indicators, show some rotating
	 * clothes (if the machine currently is spinning), and display
	 * time. Called periodically.
	 *
	 * To avoid too much redrawing, the bar sizes are cached and the
	 * bars are only redrawn if they have changed in size.
	 */
	private void updatePeriodic() {
		Graphics g = null;

		// If spinning, show rotating clothes
		int spin = myMachine.getSpin();
		if (spin != AbstractWashingMachine.SPIN_OFF) {

			if (! freakShowing) {
				freakShowing = 
						(myRandom.nextDouble() < myFreakShowProbability);
				if (freakShowing) {
					freakShowCount = FREAKSHOW_LENGTH;
				}
			}

			if (spin == AbstractWashingMachine.SPIN_FAST) {
				mySpinPosition = (mySpinPosition + 1) % SPIN_CYCLE;

				// Update view
				if (g == null)
					g = getGraphics();
				updateTumbler(g);
			} else {
				// Slow spin
				if (--myTicksUntilSpinUpdate == 0) {
					myTicksUntilSpinUpdate = SLOW_SPIN;
					switch (spin) {
					case AbstractWashingMachine.SPIN_LEFT:
						mySpinPosition = (mySpinPosition + 1) % SPIN_CYCLE;
						break;
					case AbstractWashingMachine.SPIN_RIGHT:
						mySpinPosition--;
						if (mySpinPosition < 0)
							mySpinPosition = SPIN_CYCLE - 1;
						break;
					}

					// Update view
					if (g == null)
						g = getGraphics();
					updateTumbler(g);
				}
			}		
		}

		// Update water level
		int barSize = (int)(myMachine.getWaterLevel() * WATERLEVEL_SCALE);
		if (barSize != cachedWaterLevelBarSize) {
			if (g == null)
				g = getGraphics();
			updateWaterLevel(g);
			cachedWaterLevelBarSize = barSize;
		}

		// Update temperature
		double currentTemp = myMachine.getTemperature();
		int current = (int)(currentTemp * 10);
		int previous = (int)(cachedTemp * 10);
		if (current != previous) {
			if (g == null)
				g = getGraphics();
			updateTemperature(g);
			cachedTemp = currentTemp;
		}

		// Update time
		long now = System.currentTimeMillis();

		long t = (long)(mySpeed * (now - t0) / (1000 * 60));
		if (t != tDisplayed) {
			tDisplayed = t;
			updateTime();
		}

		// Update heating info
		boolean nowHeating = myMachine.isHeating();
		if (wasHeating != nowHeating) {
			wasHeating = nowHeating;
			updateHeating();
		}

		// Clean up
		if (g != null)
			g.dispose();
	}

	/**
	 * Update lock display.
	 * @param g Graphics context to use for drawing.
	 */
	private void updateLock(Graphics g) {
		if(myMachine.isLocked())
			g.drawImage(myLockedImage, LOCK_X, LOCK_Y, Color.lightGray, this);
		else
			g.drawImage(myUnlockedImage, LOCK_X, LOCK_Y, Color.lightGray, this);
	}

	/**
	 * Update fill tap display.
	 * @param g Graphics context to use for drawing.
	 */
	private void updateFill(Graphics g) {
		if(myMachine.isFilling())
			g.drawImage(myPipeInActiveImage, FILL_X, FILL_Y,
					Color.white, this);
		else
			g.drawImage(myPipeInImage, FILL_X, FILL_Y,
					Color.white, this);
	}

	/**
	 * Update drain pump display.
	 * @param g Graphics context to use for drawing.
	 */
	private void updateDrain(Graphics g) {
		if(myMachine.isDraining())
			g.drawImage(myPipeOutActiveImage, DRAIN_X, DRAIN_Y,
					Color.white, this);
		else
			g.drawImage(myPipeOutImage, DRAIN_X, DRAIN_Y,
					Color.white, this);
	}

	/**
	 * Update tumbler display.
	 * @param g Graphics context to use for drawing.
	 */
	private void updateTumbler(Graphics g) {
		if (myMachine.getSpin() == AbstractWashingMachine.SPIN_OFF) {
			freakShowing = false;
		}

		if (freakShowing) {
			g.drawImage(myTumblerImages[mySpinPosition + SPIN_CYCLE],
					TUMBLER_X, TUMBLER_Y, Color.white, this);
			if (--freakShowCount == 0) {
				freakShowing = false;
			}
		}
		else {
			g.drawImage(myTumblerImages[mySpinPosition],
					TUMBLER_X, TUMBLER_Y, Color.white, this);
		}
	}

	/**
	 * Update temperature display.
	 * @param g Graphics context to use for drawing.
	 */
	private void updateTemperature(Graphics g) {
		int maxBar = (int)(100 * TEMP_SCALE);
		int barSize = (int)(myMachine.getTemperature() * TEMP_SCALE);

		g.setColor(Color.red);
		g.fillRect(TEMP_X, TEMP_Y, barSize, 5);
		g.setColor(Color.white);
		g.fillRect(TEMP_X + barSize, TEMP_Y, maxBar - barSize, 5);

		int wholePart = (int)myMachine.getTemperature();
		int decPart = (int)(myMachine.getTemperature() * 10) % 10;
		myTempLabel.setText("Temperature: " + wholePart + '.'
				+ decPart + " deg C");
	}

	/**
	 * Update water level display.
	 * @param g Graphics context to use for drawing.
	 */
	private void updateWaterLevel(Graphics g) {
		int maxBar = (int)WATERLEVEL_SCALE;
		int barSize = (int)(myMachine.getWaterLevel() * WATERLEVEL_SCALE);

		g.setColor(Color.blue);
		g.fillRect(WATERLEVEL_X, WATERLEVEL_Y, barSize, 5);
		g.setColor(Color.white);
		g.fillRect(WATERLEVEL_X + barSize, WATERLEVEL_Y, maxBar - barSize, 5);

		int wholePart = (int)(myMachine.getWaterLevel() * 20);
		int decPart = (int)(myMachine.getWaterLevel() * 20 * 10) % 10;
		myWaterLabel.setText("Water level: "
				+ wholePart + '.' + decPart + " l");
	}

	/**
	 * Update time display.
	 */
	private void updateTime() {
		long mins = tDisplayed % 60;
		myTimeLabel.setText("" + tDisplayed / 60 + ":"
				+ (mins / 10) + (mins % 10));
	}

	/**
	 * Update info about whether we're heating or not.
	 */
	private void updateHeating() {
		if (myMachine.isHeating())
			myHeatingLabel.setText("HEATING");
		else
			myHeatingLabel.setText("");
	}

	// -------------------------------------------- PRIVATE INSTANCE VARIABLES

	// Thread for concurrent execution
	private OngoingThread myThread;

	// Washing machine used
	private WashingMachineSimulation myMachine;

	// Simulation speed
	private double mySpeed;

	// Current spinning position
	private int mySpinPosition;

	// Number of ticks until next spinning position update
	private int myTicksUntilSpinUpdate = 1;

	// Images
	private Image myMachineImage;
	private Image myLockedImage;
	private Image myUnlockedImage;
	private Image myPipeInImage;
	private Image myPipeInActiveImage;
	private Image myPipeOutImage;
	private Image myPipeOutActiveImage;
	private Image myFishImage;
	private Image myFireImage;
	private Image[] myTumblerImages;

	// Labels
	private Label myWaterLabel, myTempLabel, myTimeLabel, myHeatingLabel;

	// Buttons
	@SuppressWarnings("unused")
	private Button b0, b1, b2, b3;

	// Cached values of simulation state
	private int cachedWaterLevelBarSize;
	private double cachedTemp;
	private boolean wasHeating;

	// Time when simulation started (millis)
	private long t0;

	// Last time displayed (minutes)
	private long tDisplayed;

	// Freak show
	private Random myRandom;
	private double myFreakShowProbability;
	private boolean freakShowing = false;
	private int freakShowCount = 10;

	// ----------------------------------------------------- PRIVATE CONSTANTS

	// Sampling speed (ms)
	private static final long SAMPLING_INTERVAL = 200;

	// Tells how often the tumbler turns, for different speeds
	private static final int SLOW_SPIN     = 5;

	// Length of spin cycle (i.e. how many images we have)
	private static final int SPIN_CYCLE    = 8;

	// Path for GIF files
	private static final String IMAGE_PATH
	= "/images/";

	// Number of ticks for surprise guest appearance
	private static final int FREAKSHOW_LENGTH = 8;

	// Window dimensions
	private static final int WINDOW_W      = 600;
	private static final int WINDOW_H      = 700;

	// Temperature bar position
	private static final int TEMP_X        = 300;
	private static final int TEMP_Y        = 50;
	private static final double TEMP_SCALE = 2;

	// Water level bar position
	private static final int WATERLEVEL_X  = 300;
	private static final int WATERLEVEL_Y  = 650;
	private static final double WATERLEVEL_SCALE = 200;

	// Water level label position
	private static final int WATERLABEL_X  = 100;
	private static final int WATERLABEL_Y  = 610;
	private static final int WATERLABEL_W  = 190;
	private static final int WATERLABEL_H  = 80;

	// Temperature label position
	private static final int TEMPLABEL_X   = 100;
	private static final int TEMPLABEL_Y   = 10;
	private static final int TEMPLABEL_W   = 190;
	private static final int TEMPLABEL_H   = 80;

	// Time label position
	private static final int TIMELABEL_X   = 10;
	private static final int TIMELABEL_Y   = 10;
	private static final int TIMELABEL_W   = 80;
	private static final int TIMELABEL_H   = 80;

	// Heating label position
	private static final int HEATINGLABEL_X = 510;
	private static final int HEATINGLABEL_Y = 10;
	private static final int HEATINGLABEL_W = 80;
	private static final int HEATINGLABEL_H = 80;

	// Hatch (circle) position
	@SuppressWarnings("unused")
	private static final int CIRCLE_X      = 300;
	@SuppressWarnings("unused")
	private static final int CIRCLE_Y      = 400;
	@SuppressWarnings("unused")
	private static final int CIRCLE_R      = 130;  

	// Washing machine dimensions
	private static final int MACHINE_X     = 100;  
	private static final int MACHINE_Y     = 100;  
	@SuppressWarnings("unused")
	private static final int MACHINE_W     = 400;  
	@SuppressWarnings("unused")
	private static final int MACHINE_H     = 500;  

	// Machine name label dimensions
	@SuppressWarnings("unused")
	private static final int NAMELABEL_X   = 103;
	@SuppressWarnings("unused")
	private static final int NAMELABEL_Y   = 103;
	@SuppressWarnings("unused")
	private static final int NAMELABEL_W   = 394;
	@SuppressWarnings("unused")
	private static final int NAMELABEL_H   = 47;

	// Button panel dimensions
	@SuppressWarnings("unused")
	private static final int BUTTONPANEL_X = 150;
	@SuppressWarnings("unused")
	private static final int BUTTONPANEL_Y = 150;
	@SuppressWarnings("unused")
	private static final int BUTTONPANEL_W = 300;
	@SuppressWarnings("unused")
	private static final int BUTTONPANEL_H = 100;

	// Lock dimensions
	private static final int LOCK_X        = 425;
	private static final int LOCK_Y        = 378;

	// Fill pump dimensions
	private static final int FILL_X        = 20;
	private static final int FILL_Y        = 100;
	@SuppressWarnings("unused")
	private static final int FILL_W        = 80;
	@SuppressWarnings("unused")
	private static final int FILL_H        = 64;

	// Drain pump dimensions
	private static final int DRAIN_X       = 501;
	private static final int DRAIN_Y       = 556;
	@SuppressWarnings("unused")
	private static final int DRAIN_W       = 64;
	@SuppressWarnings("unused")
	private static final int DRAIN_H       = 64;

	// Tumbler position
	private static final int TUMBLER_X     = 220;
	private static final int TUMBLER_Y     = 320;
	@SuppressWarnings("unused")
	private static final int TUMBLER_W     = 160;
	@SuppressWarnings("unused")
	private static final int TUMBLER_H     = 160;
}
