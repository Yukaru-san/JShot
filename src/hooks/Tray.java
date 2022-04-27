package hooks;

import java.awt.AWTException;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import jShot.JShot;
import jUtils.jTray.JTray;
import jUtils.tools.ImageTools;

public class Tray {

	public static JTray prepareTray(JShot jShot) {
		try {
			
			// Load the icon
			Image img = ImageTools.loadEmbeddedImage("/resources/icon2.png");
			
			// Create and fill tray
			JTray tray = new JTray("JShot", img);
			addTrayItems(tray, jShot);
			
			// Show the tray
			tray.show();
			return tray;
			
		} catch (Exception e) {
			System.err.println("Couldn't load the app's tray!");
		}
		
		return null;
	}
	
	private static void addTrayItems(JTray tray, JShot jShot) {
		tray.addEntry("Take screenshot", new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					if (!jShot.isOpened()) {
						jShot.takeScreenshot();
					}
				} catch (AWTException e1) {
					e1.printStackTrace();
				}
			}
			
		});
		
		tray.addEntry("Settings", new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (!jShot.isOpened()) {
					jShot.showSettings();
				}
			}
		});
		
		tray.addSeparator();
		
		tray.addEntry("Exit", new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
			
		});
	}
	
}
