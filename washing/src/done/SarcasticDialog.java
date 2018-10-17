/*
 * Real-time and concurrent programming course, laboratory 3
 * Department of Computer Science, Lund Institute of Technology
 *
 * PP 980925 Created
 * PP 990924 Revised
 */

package done;

import java.awt.*;
import java.awt.event.*;

/**
 * A dialog for sarcastic messages when things go wrong.
 *
 * @param   parent   Parent Frame
 * @param   title    Dialog title
 * @param   msg      Sarcastic message to print in dialog window
 * @param   image    Image to display in window center
 * @param   imageW   Image width (pixels)
 * @param   imageH   Image height (pixels)
 * @param   bgColor  Dialog window background color
 * @param   fgColor  Dialog window foreground color
 */
class SarcasticDialog extends Dialog implements ActionListener {
	private static final long serialVersionUID = 1L;

	// --------------------------------------------- PACKAGE PRIVATE CONSTRUCTOR

	/**
	 * 
	 */
	SarcasticDialog(Frame  parent,
			String title,
			String msg,
			Image  image,
			int    imageW,
			int    imageH,
			Color  bgColor,
			Color  fgColor) {
		super(parent, title, true);

		setBackground(bgColor);
		setForeground(fgColor);
		Label l = new Label(msg, Label.CENTER);
		add("North", l);
		add("East", new ImageCanvas(image, imageW, imageH, bgColor));
		Button b = new Button("OK");
		add("South", b);
		b.addActionListener(this);
		setSize(WIDTH, HEIGHT);
	}

	// ------------------------------------------------------ OVERRIDDEN METHODS

	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals("OK"))
			System.exit(0);
	}

	// ----------------------------------------------------------- INNER CLASSES

	/**
	 * A little thing for putting an image in a container.
	 */
	private class ImageCanvas extends Canvas {
		private static final long serialVersionUID = 1L;
		ImageCanvas(Image i,                   // Image to draw
				int w,                         // Width of image
				int h,                         // Height
				Color c) {                     // Color for transp. background
			super();

			myImage = i;
			setBackground(myColor = c);
			setSize(myWidth = w, myHeight = h);
			setVisible(true);
		}

		// Overridden paint method
		public void paint(Graphics g) {
			g.drawImage(myImage, 0, 0, myWidth, myHeight, myColor, this);
		}

		// Private stuff
		private Image myImage;
		private int myWidth, myHeight;
		private Color myColor;
	}

	// ------------------------------------------------------- PRIVATE CONSTANTS
	private static final int WIDTH = 400;
	private static final int HEIGHT = 300;
}
