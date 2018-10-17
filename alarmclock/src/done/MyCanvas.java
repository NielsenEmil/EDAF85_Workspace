package done;
import java.awt.Canvas;
import java.awt.Graphics;
import java.awt.Image;


public class MyCanvas extends Canvas {
	private static final long serialVersionUID = 1L;
	Image clockimg;
	
	public MyCanvas(Image img) {
		clockimg = img;
	}
	
	public void paint(Graphics g) {
		g.drawImage(clockimg, 0, 0, 200, 100, this);
	}
}
