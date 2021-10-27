package ui.crop;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.RenderingHints.Key;
import java.awt.Stroke;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JComponent;

import toolset.Collection;
import ui.states.CroppingState;

public class Painter {

	private JComponent component;
	private CropTarget target;
	
	// Used for User-made Drawings
	private RenderingHints renderingHints;
	private Stroke stroke;
	private Point lastDrawnPoint;
	
	private Stroke borderStroke; 
	public Stroke defaultStroke;

	public Painter(JComponent component, CropTarget target) {
		this.component = component;
		this.target = target;
		borderStroke = new BasicStroke(1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[]{target.strokeSize}, 0);
		
		Map<Key, Object> hintsMap = new HashMap<RenderingHints.Key,Object>();
        hintsMap.put(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        hintsMap.put(RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_ENABLE);
        hintsMap.put(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        renderingHints = new RenderingHints(hintsMap);
        
        stroke = new BasicStroke(
                3,BasicStroke.CAP_ROUND,BasicStroke.JOIN_ROUND,1.7f);
	}
	
	public void drawImageWithinTarget(Graphics2D g2d, BufferedImage image) {
		try {
			g2d.drawImage(image.getSubimage(target.x, target.y-5, target.width, target.height), target.x, target.y, component);
		} catch (Exception e) {
			// Do nothing
		}
	}
	
	public void drawImageAt(Graphics2D g2d, BufferedImage image, int x, int y) {
		g2d.drawImage(image, x, y, component);
	}
	
	public void drawBorder(Graphics2D g2d) {
		try {
			if (defaultStroke == null)
				defaultStroke = g2d.getStroke();
			// Paint
			g2d.setStroke(borderStroke);
			g2d.setColor(Color.white);
			g2d.drawRect(target.x, target.y, target.width, target.height);
			
			// Reset
			g2d.setStroke(defaultStroke);
			
		} catch (Exception e) {
			// Do nothing
		}
	}
	
	public void drawDragPoints(Graphics2D g2d) {
		if (target.width == 0 || target.height == 0) return;
		
		DragPoint[] dragPoints = target.calcDragPoints();
		
		// Paint drag-points
		for (DragPoint point : dragPoints) {
			g2d.setColor(Color.black);
			g2d.fillRect(point.position.x, point.position.y, 6, 6);
			g2d.setColor(Color.white);
			g2d.drawRect(point.position.x, point.position.y, 6, 6);
		}		
	}

	public void userDraw(BufferedImage targetImage) {
		if (target.currentState != CroppingState.PAINTING) return;
		
		Graphics2D g = targetImage.createGraphics();
	    g.setRenderingHints(renderingHints);
	    g.setColor(target.drawingColor);
	    g.setStroke(stroke);
	    
	    if (lastDrawnPoint != null && target.mousePosition != null && target.mousePressed && Collection.isPointInBounds(target.mousePosition, new Rectangle(target.x, target.y, target.width, target.height))) {
	    	g.drawLine(lastDrawnPoint.x, lastDrawnPoint.y, target.mousePosition.x, target.mousePosition.y);
	    }
	    
	    lastDrawnPoint = target.mousePosition;
	    g.dispose();
	}

	public CropTarget getTarget() {
		return target;
	}
	
	
}
