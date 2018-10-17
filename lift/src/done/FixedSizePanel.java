package done;

import java.awt.Dimension;

import javax.swing.JPanel;

class FixedSizePanel extends JPanel {
	private static final long serialVersionUID = 1L;
	private Dimension dim;

	public FixedSizePanel(int w,int h) {
		dim = new Dimension(w,h);
		setSize(dim);
	}

	public Dimension getPreferredSize() {
		return dim;
	}
}

