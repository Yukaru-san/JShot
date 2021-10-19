package ui.crop;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.image.BufferedImage;

import javax.swing.JComponent;

public class Painter {

	private JComponent component;
	private CropTarget target;
	private Stroke defaultStroke;
	private Stroke borderStroke; 

	public Painter(JComponent component, CropTarget target) {
		this.component = component;
		this.target = target;
		borderStroke = new BasicStroke(1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[]{target.strokeSize}, 0);
	}
	
	public void drawImage(Graphics2D g2d, BufferedImage image) {
		try {
			g2d.drawImage(image.getSubimage(target.x, target.y-5, target.width, target.height), target.x, target.y, component);
		} catch (Exception e) {
			// Do nothing
		}
	}
	
	public void drawBorder(Graphics2D g2d) {
		try {
			if (defaultStroke == null)
				defaultStroke = g2d.getStroke();
			
			g2d.setStroke(borderStroke);
			g2d.setColor(Color.white);
			g2d.drawRect(target.x, target.y, target.width, target.height);
		} catch (Exception e) {
			// Do nothing
		}
	}
	
	public void drawDragPoints(Graphics2D g2d) {
		DragPoint[] dragPoints = target.calcDragPoints();
		
		// Paint drag-points
		g2d.setStroke(defaultStroke);
		for (DragPoint point : dragPoints) {
			g2d.setColor(Color.black);
			g2d.fillRect(point.position.x, point.position.y, 6, 6);
			g2d.setColor(Color.white);
			g2d.drawRect(point.position.x, point.position.y, 6, 6);
		}		
	}

	public CropTarget getTarget() {
		return target;
	}
	
	
}
