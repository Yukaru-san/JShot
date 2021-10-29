package toolset;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;
import java.io.File;

import javax.imageio.ImageIO;

import ui.overlay.OverlayHandler;

public class Collection {

	// Deep copies a Buffered Image
	public static BufferedImage cloneBufferedImage(BufferedImage image) {
		// Deep Copy the BI
		ColorModel cm = image.getColorModel();
		boolean isAlphaPremultiplied = cm.isAlphaPremultiplied();
		WritableRaster raster = image.copyData(null);
		BufferedImage clone = new BufferedImage(cm, raster, isAlphaPremultiplied, null);
		return clone;
	}

	// Copies an BufferedImage and creates a darkened version
	public static BufferedImage createDarkerBufferedImage(BufferedImage image) {
		BufferedImage darkenedImage = cloneBufferedImage(image);
		
		// Obtain the Graphics2D context associated with the BufferedImage.
		Graphics2D g = darkenedImage.createGraphics();

		// Draw on the BufferedImage via the graphics context.
		g.setColor(new Color(0, 0, 0, 125));
		g.fillRect(0, 0, darkenedImage.getWidth(), darkenedImage.getHeight());

		return darkenedImage;
	}

	// Loads an Image from within the Java Classpath or .jar file
	public static BufferedImage loadEmbeddedImage(String path) {
		BufferedImage img = null;
		try {
			img = ImageIO.read(OverlayHandler.class.getResourceAsStream(path));
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

		return img;
	}

	// Returns true if p is within r
	public static boolean isPointInBounds(Point p, Rectangle r) {
		return (p.x > r.x && p.x < r.x + r.width && p.y > r.y && p.y < r.y + r.height);
	}
	
	public static void saveBufferedImageAsFile(BufferedImage img, String path) {
		try {
			ImageIO.write(img, "png", new File(path));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
