package done;
import java.awt.*;


public class LevelDrawer {

	private FixedSizePanel entryPane, exitPane;
	private int floor_height;
	private int entry_width;
	private int exit_width;
	private int no_of_floors;
	private int[] waitingAtFloor;
	private FloorEntry[] floorIn;
	private FloorExit[] floorOut;

	public LevelDrawer( int entryW, int exitW, int nf, int fh, FixedSizePanel entryP, FixedSizePanel exitP) {
		floor_height = fh;
		entry_width = entryW;
		exit_width= exitW;
		no_of_floors = nf;
		entryPane = entryP;
		exitPane = exitP;
		
		floorIn = new FloorEntry[no_of_floors];
		for(int i=0;i<no_of_floors;i++) {
			floorIn[no_of_floors-i-1] = new FloorEntry( entry_width, floor_height);
			entryPane.add( floorIn[no_of_floors-i-1]);
		}

		floorOut = new FloorExit[no_of_floors];
		for(int i=0;i<no_of_floors;i++) {
			floorOut[no_of_floors-i-1] = new FloorExit( exit_width, floor_height);
			exitPane.add( floorOut[no_of_floors-i-1]);
		}
		
		waitingAtFloor = new int[no_of_floors];

	}

	public void drawLeaving(int floor) {
		if (floor<0 || floor>=no_of_floors) {
			throw new Error("Illegal floor in call to drawLeaving.");
		}
		floorOut[floor].animatePerson(floor); 
	}

	public void drawWaiting(int floor, int add) {
		if (floor<0 || floor>=no_of_floors) {
			throw new Error("Illegal floor in call to drawWaiting.");
		}
		
		waitingAtFloor[floor] += add;
		
		if ( waitingAtFloor[floor]<0) {
			throw new Error("Negative number of persons in call to drawWaiting.");
		}
		Thread.yield();
		floorIn[floor].draw( waitingAtFloor[floor]);
		Thread.yield();
	}



	private class FloorEntry extends FixedSizePanel {
		private static final long serialVersionUID = 1L;
		private int width,height;
		private int waiting;

		public FloorEntry(int w,int h) {
			super(w,h);
			setBackground(Color.WHITE);
			height = h;
			width = w;
			waiting = 0;
		}

		public void draw(int w) {
			waiting = w;
			Thread.yield();
			repaint();
		}

		protected void paintComponent(Graphics g) {
			super.paintComponent(g);
			g.drawLine(0,height-1,width,height-1);
			for(int i=0;i<waiting;i++) {
				PersonDrawer.draw(g,width-(i+1)*35,height-5);
			}
		}
	}

	private class FloorExit extends FixedSizePanel {
		private static final long serialVersionUID = 1L;
		private int width,floorHeight;
		private int animateX,animateY;

		public FloorExit(int w, int fh) {
			super(w,fh);
			width = w;
//			noOfFloors = nof;
			floorHeight = fh;
			setBackground(Color.WHITE);
			animateX = 0;
			animateY = 0;
		}

		protected void paintComponent(Graphics g) {
			super.paintComponent(g);
//			for(int i=1;i<noOfFloors;i++) {
			g.drawLine(0, floorHeight-1, width, floorHeight-1);
//			}
			if (animateY!=0) {
				PersonDrawer.draw(g,animateX,animateY);
			}
		}

		public void animatePerson(int floor) {
			animateY = floorHeight-5;
			for(animateX=0;animateX<width;animateX+=20) {
				repaint();
				try {
					Thread.sleep(100);
				} catch(InterruptedException e) { }
			}
			animateX = 0;
			animateY = 0;
			repaint();
		}
	}

}

