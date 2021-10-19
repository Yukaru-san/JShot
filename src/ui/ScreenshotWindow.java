package ui;

import java.awt.Dimension;

import javax.swing.JFrame;

public class ScreenshotWindow extends JFrame {

	// Some serial number to not get warnings
	private static final long serialVersionUID = -7077528694515804789L;
	
	// Constructor setting up the frame itself
	public ScreenshotWindow(Dimension windowSize) {
		// Set window size and position
		setBounds(0, -5, windowSize.width, windowSize.height + 5);
		setUndecorated(true);
		setAlwaysOnTop(true);
		setLayout(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
}