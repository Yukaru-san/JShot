package toolset;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;

public class Collection {

	// Copies an BufferedImage and creates a darkened version
	public static BufferedImage createDarkerBufferedImage(BufferedImage image) {
		// Deep Copy the BI
		ColorModel cm = image.getColorModel();
		boolean isAlphaPremultiplied = cm.isAlphaPremultiplied();
		WritableRaster raster = image.copyData(null);
		BufferedImage darkenedImage = new BufferedImage(cm, raster, isAlphaPremultiplied, null);

		// Obtain the Graphics2D context associated with the BufferedImage.
		Graphics2D g = darkenedImage.createGraphics();

		// Draw on the BufferedImage via the graphics context.
		g.setColor(new Color(0, 0, 0, 125));
		g.fillRect(0, 0, darkenedImage.getWidth(), darkenedImage.getHeight());

		return darkenedImage;
	}

}
