package done;
import java.applet.Applet;
import java.awt.*;
import java.awt.event.*;

import todo.AlarmClock;


public class ClockGUI extends Applet implements KeyListener, ClockTimeDisplay, ItemListener {
	private static final long serialVersionUID = 1L;
	// Clock input mode
	static final int MODE_NONE	=0;
	static final int MODE_TSHOW	=1;
	static final int MODE_ASET	=2;
	static final int MODE_TSET	=3;
	static final int MODE_ATOGGLE	=4;

	boolean b1 = false;
	boolean b2 = false;
	private boolean b1p = false;
	private boolean b2p = false;
	boolean bl = false;
	boolean bu = false;
	boolean bd = false;
	boolean br = false;
	ClockInput	clkinput;
	private ClockOutput	clkoutput;

	int cursor = 0;
	int clockmode = MODE_NONE;
	int clocktime = 0;// Clock time
	int alarmtime = 0;// Alarm time
	private int inputval = 0;// Input value
	private boolean	hiding = false;
	private AlarmClock	control;
	boolean	alarmpulse = false;
	private Checkbox	chb_alarmon;
	private ClockCanvas	clkcanvas;
	private Checkbox	chb_tshow;
	private Checkbox	chb_tset;
	private Checkbox	chb_aset;
	private CheckboxGroup	chg_mode;

	/**
	 * Initializes the applet. You never need to call this directly; it is
	 * called automatically by the system once the applet is created.
	 */
	public void init() {
		setSize(400, 250);
		setLayout(new FlowLayout());
		
		clkcanvas = new ClockCanvas(this); 
		add(clkcanvas);
		clkcanvas.addKeyListener(this);
		
		chb_alarmon = new Checkbox("Alarm on");
		chb_alarmon.addItemListener(this);
		add(chb_alarmon);
		
		chb_tshow = new Checkbox("Show Time");
		chb_tshow.setState(true);
		chb_tshow.setEnabled(false);
		chb_tset = new Checkbox("Set Time");
		chb_tset.setEnabled(false);
		chb_aset = new Checkbox("Set Alarm");
		chb_aset.setEnabled(false);
		chg_mode = new CheckboxGroup();
		chb_tshow.setCheckboxGroup(chg_mode);
		chb_tset.setCheckboxGroup(chg_mode);
		chb_aset.setCheckboxGroup(chg_mode);
		
		add(chb_tshow);
		add(chb_tset);
		add(chb_aset);
		
		clkinput = new ClockInput();
		clkoutput = new ClockOutput(this);
		
		// Instantiate alarm-clock software using application interfaces.
		control = new AlarmClock(clkinput, clkoutput);
	}

	private void onInput() {
		int prevmode = clockmode;

		// Read push button states
		if (b1 != b1p || b2 != b2p) {
			if (b1 && b2) {
				clkinput.alarmOn = !clkinput.alarmOn;
				chb_alarmon.setState(clkinput.alarmOn);
				chb_tshow.setState(true);
				clockmode = MODE_ATOGGLE;
			} else if (b1) {
				clkinput.choice = ClockInput.SET_TIME;
				chb_tset.setState(true);
				clockmode = MODE_TSET;
			} else if (b2) {
				clkinput.choice = ClockInput.SET_ALARM;
				chb_aset.setState(true);
				clockmode = MODE_ASET;
			} else {
				clkinput.choice = ClockInput.SHOW_TIME;
				chb_tshow.setState(true);
				clockmode = MODE_TSHOW;
			}
		}
		b1p = b1;
		b2p = b2;

		// Any button changed?
		if (clockmode == MODE_TSET) {
			TimeSet(prevmode);
		} else if (clockmode == MODE_ASET) {
			AlarmSet(prevmode);
		} else {
			cursor = 0;
		}

		clkcanvas.repaint();

		// Signal ClockInput original, see change below
		// clkinput.lastValueSet = inputval;
		// clkinput.getSemaphoreInstance().give();

		
		// Signal ClockInput
		// clkinput.lastValueSet contains garbage if not read at the right time
		// leading to really funny output for the time value
		if( prevmode != clockmode 
				&& ( prevmode == MODE_TSET || prevmode == MODE_ASET)) 
			clkinput.lastValueSet = inputval;
		else
			clkinput.lastValueSet = -010101;
		
		clkinput.getSemaphoreInstance().give();

	}

	private void TimeSet(int prevmode)
	{
		if (prevmode != MODE_TSET) {
			inputval = clocktime;
		}

		if (bl && cursor < 5) {
			cursor++;
		} else if (br && cursor > 0) {
			cursor--;
		} else if (bu) {
			inputval = IncTime(inputval, cursor);
		} else if (bd) {
			if (GetDigit(inputval, cursor) > 0) {
				inputval = DecDigit(inputval, cursor);
			}
		}

		clocktime = inputval;
	}

	private void AlarmSet(int prevmode)
	{
		if (prevmode != MODE_ASET) {
			inputval = alarmtime;
		}

		if (bl && cursor < 5) {
			cursor++;
		} else if (br && cursor > 0) {
			cursor--;
		} else if (bu) {
			inputval = IncTime(inputval, cursor);
		} else if (bd) {
			if (GetDigit(inputval, cursor) > 0) {
				inputval = DecDigit(inputval, cursor);
			}
		}

		alarmtime = inputval;
	}

	public void keyPressed(KeyEvent event) {
		switch (event.getKeyCode()) {
			case KeyEvent.VK_SHIFT:
				b1 = true;
				break;
			case KeyEvent.VK_CONTROL:
				b2 = true;
				break;
			case KeyEvent.VK_LEFT:
				bl = true;
				break;
			case KeyEvent.VK_UP:
				bu = true;
				break;
			case KeyEvent.VK_DOWN:
				bd = true;
				break;
			case KeyEvent.VK_RIGHT:
				br = true;
				break;
			default:
				return;
		}
		clkcanvas.repaint();
		
		onInput();
	}

	public void keyReleased(KeyEvent event) {
		switch (event.getKeyCode()) {
			case KeyEvent.VK_SHIFT:
				b1 = false;
				break;
			case KeyEvent.VK_CONTROL:
				b2 = false;
				break;
			case KeyEvent.VK_LEFT:
				bl = false;
				break;
			case KeyEvent.VK_UP:
				bu = false;
				break;
			case KeyEvent.VK_DOWN:
				bd = false;
				break;
			case KeyEvent.VK_RIGHT:
				br = false;
				break;
			default:
				return;
		}
		clkcanvas.repaint();
		
		onInput();
	}

	public void keyTyped(KeyEvent event) {
	}

	public void setTime(int hhmmss) {
		if (!hiding && clockmode != MODE_TSET) {
			clocktime = hhmmss;
		}
		clkcanvas.repaint();
	}
	
	private int GetDigit(int number, int pos)
	{
		int i;
		for (i = 0; i < pos; i++) number /= 10;
		return number % 10;
	}

	private int IncDigit(int number, int pos)
	{
		int term = 1;
		int i;
		for (i = 0; i < pos; i++) term *= 10;
		return number + term;
	}

	private int DecDigit(int number, int pos)
	{
		int term = 1;
		int i;
		for (i = 0; i < pos; i++) term *= 10;
		return number - term;
	}

	private int IncTime(int time, int pos)
	{
		int newtime = time;
		if ((pos == 5 && GetDigit(time, 5) < 2 && GetDigit(time, 4) < 4) ||
				(pos == 5 && GetDigit(time, 5) < 1) || 
				(pos == 4 && GetDigit(time, 4) < 3 && GetDigit(time, 5) <= 2) ||
				(pos == 4 && GetDigit(time, 4) < 9 && GetDigit(time, 5) <= 1) ||
				(pos == 3 && GetDigit(time, 3) < 5) ||
				(pos == 1 && GetDigit(time, 1) < 5) ||
				((pos == 0 || pos == 2) && GetDigit(time, pos) < 9)) {
			newtime = IncDigit(time, pos);
		}
		return newtime;
	}

	/**
	 * Called to start the applet.  You never need to call this directly; it
	 * is called when the applet's document is visited.
	 */
	public void start() {
		if (hiding) {
			// Control software already running, activate GUI updates:
			hiding = false;
		} else {
			control.start();
		}
	}
	
	/**
	 * Called to stop the applet.  This is called when the applet's document is
	 * no longer on the screen.  It is guaranteed to be called before destroy()
	 * is called.  You never need to call this method directly
	 */
	public void stop() {
		// For the clock to work even when it is not visible, all threads
		// should keep on working. Now when we are not visible we should,
		// however, not waste time on calling GUI methods. Set flag:
		hiding = true;
	}

	public void setAlarmPulse(boolean onoff) {
		alarmpulse = onoff;
		clkcanvas.repaint();
	}

	public void itemStateChanged(ItemEvent evt) {
		if (evt.getSource() == chb_alarmon) {
			clkinput.alarmOn = evt.getStateChange() == ItemEvent.SELECTED;
			clkinput.getSemaphoreInstance().give();
			clkcanvas.repaint();
		}
		
	}

}
