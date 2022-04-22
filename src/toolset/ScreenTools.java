package toolset;

import java.awt.AWTException;
import java.awt.Dimension;
import java.awt.DisplayMode;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.image.BufferedImage;

public class ScreenTools {

	// Takes a screenshot from the given Dimension
	public static BufferedImage takeScreenshot(Dimension screenshotDimensions) throws AWTException {
		return new Robot()
				.createScreenCapture(new Rectangle(0, 0, screenshotDimensions.width, screenshotDimensions.height));
	}

	// Returns the size of the screen at the given index
	public static Dimension getScreenDimensions(int screenNum) {
		// Get Graphics Environment
		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		
		// Get Displays
		GraphicsDevice[] gs = ge.getScreenDevices();
		
		if (screenNum > gs.length-1) {
			screenNum = gs.length-1;
		}

		// Find the required height and width
		Dimension dimensions = new Dimension();
		DisplayMode mode = gs[screenNum].getDisplayMode();
		dimensions.width = mode.getWidth();
		dimensions.height = mode.getHeight();

		return dimensions;
	}

	// Returns the maximum screen sizes (TODO: Test for multiple monitors on top of
	// each other)
	public static Dimension getMaxDimensions() {
		// Get Graphics Environment
		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		
		// Get Displays
		GraphicsDevice[] gs = ge.getScreenDevices();

		// Find the required height and width
		Dimension dimensions = new Dimension();

		// Count up the width and height of all devices
		for (GraphicsDevice curGs : gs) {
			DisplayMode mode = curGs.getDisplayMode();
			dimensions.width += mode.getWidth();
			if (mode.getHeight() > dimensions.height) {
				dimensions.height = mode.getHeight();
			}
		}
		
		// Return
		return dimensions;
	}

	// Returns the getMaxDimension method as a Rectangle
	public static Rectangle getMaxRectangle() {
		Dimension maxD = getMaxDimensions();
		return new Rectangle(0, 0, maxD.width, maxD.height);
	}
}
