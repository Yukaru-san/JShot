package ui;

import java.awt.Dimension;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import jcrop.crop.CroppingPanel;
import toolset.Collection;

import java.awt.Color;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;

public class SettingsWindow extends JFrame implements KeyListener {

	// Some serial number to not get warnings
	private static final long serialVersionUID = 1843680528713970765L;

	// References
	private ScreenshotWindow screenshotWindow;
	private SettingsPopup popup;

	// Setup
	public SettingsWindow(Dimension windowSize, ScreenshotWindow screenshotWindow) {
		// Reference
		this.screenshotWindow = screenshotWindow;

		// Set window size and position
		setBounds(0, 0, windowSize.width, windowSize.height + 5);
		setUndecorated(true);
		setAlwaysOnTop(true);
		setLayout(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		repaint();
		setVisible(false);
		addKeyListener(this);
		
		// Add components
		addBackground();
		addForeground(windowSize);
	}

	// Adds the background of the frame (the image)
	public void addBackground() {	
		// Get inner reference
		CroppingPanel cp = screenshotWindow.getCroppingPanel();
	
		// Set Background
		BufferedImage bi = cp.getOriginalImage();
		BufferedImage ci = cp.getCroppedImage();
		int x = cp.getCropTarget().getX();
		int y = cp.getCropTarget().getY();

		// Create background
		JLabel bg = new JLabel(new ImageIcon(Collection.createDarkerBufferedImage(bi)));
		JLabel cg = new JLabel(new ImageIcon(ci));

		// Set BG bounds
		bg.setBounds(0, 0, bi.getWidth(), bi.getHeight());
		cg.setBounds(x, y-5, ci.getWidth(), ci.getHeight());

		// Add BG
		add(cg);
		add(bg);
		
		// Show frame
		setVisible(true);
	}
	
	// Adds the JPanel and its content
	private void addForeground(Dimension windowSize) {
		// add(new SettingsPopup(windowSize, screenshotWindow));
		popup = new SettingsPopup(windowSize, screenshotWindow);
	}
	
	// Dispose neatly
	public void close() {
		popup.dispose();
		this.dispose();
	}
	
	/* -- KeyEvents -- */

	@Override
	public void keyTyped(KeyEvent e) {
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if (e.getKeyCode() == 27) { // ESC
			screenshotWindow.hideSettings();
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
	}

}
