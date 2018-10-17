package done;

import java.awt.*;


public class ClockCanvas extends Canvas {
	private static final long serialVersionUID = 1L;
	private Graphics backbuff;
	private Image buff;
	private Image clockimg;
	private Image btnblue;
	private Image btnblue_p;
	private Image btnyellow;
	private Image btnyellow_p;
	
	private ClockGUI parent;
	
	private char[]	lcdcache	= { ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ' };
	private char[]	lcdcache2	= { ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ' };
	
	public ClockCanvas(ClockGUI parent) {
		setSize(400, 189);
		buff = parent.createImage(400, 189);
		backbuff = buff.getGraphics();
		this.parent = parent;
		clockimg = parent.getImage(parent.getClass().getResource("/clock.png"));
		btnblue = parent.getImage(parent.getClass().getResource("/btnblue.png"));
		btnblue_p = parent.getImage(parent.getClass().getResource("/btnblue_p.png"));
		btnyellow = parent.getImage(parent.getClass().getResource("/btnyellow.png"));
		btnyellow_p = parent.getImage(parent.getClass().getResource("/btnyellow_p.png"));
	}
	
	public void update(Graphics g) {
		paint(g);
	}

	public void paint(Graphics g) {
		backbuff.drawImage(clockimg, 0, 0, this);
		if (parent.b1) {
			backbuff.drawImage(btnblue_p, 90, 120, this);
		} else {
			backbuff.drawImage(btnblue, 90, 120, this);
		}
		if (parent.b2) {
			backbuff.drawImage(btnblue_p, 90, 150, this);
		} else {
			backbuff.drawImage(btnblue, 90, 150, this);
		}
		if (parent.bl) {
			backbuff.drawImage(btnyellow_p, 177, 150, this);
		} else {
			backbuff.drawImage(btnyellow, 177, 150, this);
		}
		if (parent.bu) {
			backbuff.drawImage(btnyellow_p, 228, 120, this);
		} else {
			backbuff.drawImage(btnyellow, 228, 120, this);
		}
		if (parent.bd) {
			backbuff.drawImage(btnyellow_p, 228, 150, this);
		} else {
			backbuff.drawImage(btnyellow, 228, 150, this);
		}
		if (parent.br) {
			backbuff.drawImage(btnyellow_p, 279, 150, this);
		} else {
			backbuff.drawImage(btnyellow, 279, 150, this);
		}
		backbuff.setFont(new Font("Courier", Font.PLAIN, 28));
		backbuff.setColor(new Color(0, 68, 85));

		int hhmmss;

		hhmmss = parent.clocktime;
		lcdcache[7] = (char) (hhmmss % 10 + '0');
		hhmmss /= 10;
		lcdcache[6] = (char) (hhmmss % 10 + '0');
		hhmmss /= 10;
		lcdcache[5] = ':';
		lcdcache[4] = (char) (hhmmss % 10 + '0');
		hhmmss /= 10;
		lcdcache[3] = (char) (hhmmss % 10 + '0');
		hhmmss /= 10;
		lcdcache[2] = ':';
		lcdcache[1] = (char) (hhmmss % 10 + '0');
		hhmmss /= 10;
		lcdcache[0] = (char) (hhmmss % 10 + '0');

		backbuff.drawString(new String(lcdcache), 50, 60);

		hhmmss = parent.alarmtime;
		lcdcache2[7] = (char) (hhmmss % 10 + '0');
		hhmmss /= 10;
		lcdcache2[6] = (char) (hhmmss % 10 + '0');
		hhmmss /= 10;
		lcdcache2[4] = (char) (hhmmss % 10 + '0');
		hhmmss /= 10;
		lcdcache2[3] = (char) (hhmmss % 10 + '0');
		hhmmss /= 10;
		lcdcache2[1] = (char) (hhmmss % 10 + '0');
		hhmmss /= 10;
		lcdcache2[0] = (char) (hhmmss % 10 + '0');

		if (parent.alarmpulse) {
			lcdcache2[5] = ' ';
			lcdcache2[2] = ' ';
		} else {
			if (parent.clkinput.alarmOn) {
				lcdcache2[5] = ':';
				lcdcache2[2] = ':';
			} else {
				lcdcache2[5] = '_';
				lcdcache2[2] = '_';
			}
		}

		backbuff.drawString(new String(lcdcache2), 50, 85);

		if (parent.clockmode == ClockGUI.MODE_TSET || parent.clockmode == ClockGUI.MODE_ASET) {
			int cur = 5 - parent.cursor;
			if (parent.cursor < 4) {
				cur++;
				if (parent.cursor < 2) {
					cur++;
				}
			}
			char[] cur$ = { ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ' };
			cur$[cur] = '_';
			if (parent.clockmode == ClockGUI.MODE_ASET) {
				backbuff.drawString(new String(cur$), 50, 85);
			} else {
				backbuff.drawString(new String(cur$), 50, 60);
			}
		}

		// Paint the back buffer
		g.drawImage(buff, 0, 0, this);
	}
}
