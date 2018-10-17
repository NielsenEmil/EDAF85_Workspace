package done;

import java.awt.Color;
import java.awt.Graphics;

class PersonDrawer {

	public static void draw(Graphics g,int x,int y) {
		g.drawLine(x,y,x+12,y-30);
		g.drawLine(x+12,y-30,x+24,y);
		g.drawLine(x+12,y-30,x+12,y-55);
		g.drawLine(x+12,y-55,x,y-35);
		g.drawLine(x+12,y-55,x+24,y-35);
		g.drawOval(x+5,y-70,15,15);
	}

	public static void erase(Graphics g,int x,int y) {
		Color c = g.getColor();
		g.setColor(Color.WHITE);
		draw(g,x,y);
		g.setColor(c);
	}

}
