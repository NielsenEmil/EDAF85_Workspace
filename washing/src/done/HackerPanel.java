/*
 * Real-time and concurrent programming course, laboratory 3
 * Department of Computer Science, Lund Institute of Technology
 *
 * PP 980728 Created
 * PP 990924 Revised
 * MH 110927 Revised
 */

package done;

import java.awt.*;
import java.awt.event.*;

/**
 * A little panel window with a few buttons to click. Used
 * to manipulate washing machine parameters directly.
 */

class HackerPanel
extends Frame
implements ActionListener {
	private static final long serialVersionUID = 1L;

	// ---------------------------------------------------------- PUBLIC METHODS

	public HackerPanel(AbstractWashingMachine mach) {
		super("Washing Machine Hacker's Toolbox");
		myMachine = mach;
		setSize(PANEL_WIDTH, PANEL_HEIGHT);
		setLayout(new GridLayout(4, 3));

		Button b;
		b = new Button("Heat on");
		b.addActionListener(this);
		add(b);
		b = new Button("Heat off");
		b.addActionListener(this);
		add(b);
		b = new Button("Fill on");
		b.addActionListener(this);
		add(b);
		b = new Button("Fill off");
		b.addActionListener(this);
		add(b);
		b = new Button("Drain on");
		b.addActionListener(this);
		add(b);
		b = new Button("Drain off");
		b.addActionListener(this);
		add(b);
		b = new Button("Lock");
		b.addActionListener(this);
		add(b);
		b = new Button("Unlock");
		b.addActionListener(this);
		add(b);
		b = new Button("Spin off");
		b.addActionListener(this);
		add(b);
		b = new Button("Spin left");
		b.addActionListener(this);
		add(b);
		b = new Button("Spin right");
		b.addActionListener(this);
		add(b);
		b = new Button("Spin fast");
		b.addActionListener(this);
		add(b);

		enableEvents(AWTEvent.WINDOW_EVENT_MASK);
		setVisible(true);
	}

	/**
	 * Listener method; automatically called by AWT upon
	 * window events, such as when a button is clicked.
	 *
	 * @param e An ActionEvent describing the event.
	 */

	public void actionPerformed(ActionEvent e) {
		String command = e.getActionCommand();
		if(command.equals("Heat on"))
			myMachine.setHeating(true);
		else if(command.equals("Heat off"))
			myMachine.setHeating(false);
		else if(command.equals("Fill on"))
			myMachine.setFill(true);
		else if(command.equals("Fill off"))
			myMachine.setFill(false);
		else if(command.equals("Drain on"))
			myMachine.setDrain(true);
		else if(command.equals("Drain off"))
			myMachine.setDrain(false);
		else if(command.equals("Lock"))
			myMachine.setLock(true);
		else if(command.equals("Unlock"))
			myMachine.setLock(false);
		else if(command.equals("Spin off"))
			myMachine.setSpin(AbstractWashingMachine.SPIN_OFF);
		else if(command.equals("Spin left"))
			myMachine.setSpin(AbstractWashingMachine.SPIN_LEFT);
		else if(command.equals("Spin right"))
			myMachine.setSpin(AbstractWashingMachine.SPIN_RIGHT);
		else if(command.equals("Spin fast"))
			myMachine.setSpin(AbstractWashingMachine.SPIN_FAST);
	}

	/**
	 * Handle window closing.
	 * @param e The WindowEvent provided by AWT.
	 */

	public void processWindowEvent(WindowEvent e) {
		super.processWindowEvent(e);
		if(e.getID() == WindowEvent.WINDOW_CLOSING)
			System.exit(0);
	}

	// ---------------------------------------------- PRIVATE INSTANCE VARIABLES

	private AbstractWashingMachine myMachine;

	// ------------------------------------------------------- PRIVATE CONSTANTS

	private static final int PANEL_WIDTH  = 300;
	private static final int PANEL_HEIGHT = 200;
}
