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
		return new Robot().createScreenCapture(new Rectangle(0, 0, screenshotDimensions.width, screenshotDimensions.height));
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

		for (GraphicsDevice curGs : gs) {
			DisplayMode mode = curGs.getDisplayMode();
			dimensions.width += mode.getWidth();
			if (mode.getHeight() > dimensions.height) {
				dimensions.height = mode.getHeight();
			}
		}
		return dimensions;
	}

	public static Rectangle getMaxRectangle() {
		Dimension maxD = getMaxDimensions();
		return new Rectangle(0, 0, maxD.width, maxD.height);
	}
}
