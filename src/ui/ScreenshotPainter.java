package ui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.awt.RenderingHints.Key;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

import jcrop.crop.CropTarget;
import toolset.Collection;

public class ScreenshotPainter {
	
	// Used for User-made Drawings
	private RenderingHints renderingHints;
	private Stroke stroke;
	private Color drawingColor;
	private Point lastDrawnPoint;
	
	public ScreenshotPainter() {
		Map<Key, Object> hintsMap = new HashMap<RenderingHints.Key,Object>();
        hintsMap.put(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        hintsMap.put(RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_ENABLE);
        hintsMap.put(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        renderingHints = new RenderingHints(hintsMap);
      
        stroke = new BasicStroke(
                3,BasicStroke.CAP_ROUND,BasicStroke.JOIN_ROUND,1.7f);
        
        drawingColor = Color.RED;
	}

	// Draws on a BufferedImage based on the current Mouse Events
	public void doDrawEvent(CropTarget target, BufferedImage targetImage) {
		// Draw on Image
		Graphics2D g = targetImage.createGraphics();
	    g.setRenderingHints(renderingHints);
	    g.setColor(drawingColor);
	    g.setStroke(stroke);
	    
	    if (lastDrawnPoint != null && target.mousePosition != null && target.mousePressed && Collection.isPointInBounds(target.mousePosition, new Rectangle(target.x, target.y, target.width, target.height))) {
	    	g.drawLine(lastDrawnPoint.x, lastDrawnPoint.y, target.mousePosition.x, target.mousePosition.y);
	    }
	    
	    lastDrawnPoint = target.mousePosition;
	    g.dispose();
	}
}
