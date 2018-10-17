package done;
import java.awt.*;


public class BasketDrawer {

	private FixedSizePanel shaftPane;
	private Basket basket;
	private int floor_height;
	private int shaft_width;
	private int no_of_floors;
	private int max_load;

	public BasketDrawer( int shaftW, int nf, int fh, int maxL, FixedSizePanel shaftP) {
		floor_height = fh;
		shaft_width = shaftW;
		no_of_floors = nf;
		shaftPane = shaftP;	
		max_load = maxL;
		basket = new Basket( shaft_width, no_of_floors, floor_height, shaftPane);
	}

	public void drawLift(int floor, int load) {
		if (load<0 || load>max_load) {
			throw new Error("Illegal load parameter to drawLift.");
		}
		if (floor<0 || floor>=no_of_floors) {
			throw new Error("Illegal floor parameter to drawLift");
		}
		basket.draw(floor,load);
	}


	public void moveLift(int here, int next) {
		if (here<0 || here>=no_of_floors || next<0 || next>=no_of_floors ||
				here==next) {
			throw new Error("Illegal parameters to moveLift.");
		}
		basket.moveBasket(here,next);
		try {
			Thread.sleep(200);
		} catch(InterruptedException e) { }
	}


	private class Basket extends FixedSizePanel {
		private static final long serialVersionUID = 1L;
		private int width,floorHeight,noOfFloors;
		private int INCREMENT = 2;
		private int load;

		public Basket(int w,int nof,int fh,FixedSizePanel shaft) {
			super(w-4,fh);
			width = w;
			noOfFloors = nof;
			floorHeight = fh;
			load = 0;
			setBackground(Color.YELLOW);
			shaft.add(this);
			setLocation(2,(nof-1)*fh);
		}

		protected void paintComponent(Graphics g) {
			super.paintComponent(g);
			g.drawRect(0,0,width-5,floorHeight-1);
			for(int i=0;i<load;i++) {
				PersonDrawer.draw(g,i*35+5,floorHeight-5);
			}
		}

		private int floorOffset(int floor) {
			return (noOfFloors-floor-1)*floorHeight;
		}


		public void moveBasket(int from, int to) {
			int start = floorOffset(from);
			int stop = floorOffset(to);
			if (start<stop) {
				for(int y=start;y<stop;y+=INCREMENT) {
					setLocation(2,y);
					try {
						Thread.sleep(50);
					} catch(InterruptedException e) { }
				}
			} else {
				for(int y=start;y>stop;y-=INCREMENT) {
					setLocation(2,y);
					try {
						Thread.sleep(50);
					} catch(InterruptedException e) { }
				}
			}
			setLocation(2,stop);
		}

		public void draw(int f,int l) {
			load = l;
			setLocation(2,floorOffset(f));
			repaint();
		}
	}

	

}

