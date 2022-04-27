package jShot.ui;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.Line2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import jCrop.utils.Utils;
import jShot.toolset.PainterSettings;

public class ScreenshotPainter {

	// Runtime settings
	private boolean useHighlight = false;

	// Toggles the useHighlight variable
	public void toggleHighlight() {
		useHighlight = !useHighlight;
	}

	// Get useHighlight
	public boolean isHighlighted() {
		return useHighlight;
	}

	// Apply the current settings on the given graphics
	public void applyGraphicsSettings(Graphics2D g) {
		if (useHighlight) {
			g.setRenderingHints(PainterSettings.renderingHints);
			g.setColor(PainterSettings.data.h_color);
			g.setStroke(PainterSettings.h_stroke);
		} else {
			g.setRenderingHints(PainterSettings.renderingHints);
			g.setColor(PainterSettings.data.color);
			g.setStroke(PainterSettings.stroke);
		}
	}

	// Apply the current settings on the given graphics
	public void applyTextGraphicsSettings(Graphics2D g) {
		g.setRenderingHints(PainterSettings.renderingHints);
		g.setFont(PainterSettings.font);
		g.setColor(PainterSettings.data.color);
	}

	// Apply the current settings on the given graphics
	public void applyBorderGraphicsSettings(Graphics2D g) {
		g.setRenderingHints(PainterSettings.renderingHints);
		g.setStroke(Utils.createBorderStroke(3));
		g.setColor(Color.WHITE);
	}

	// Draw a polyline on the given graphics context
	public void drawPolyline(Graphics2D g, ArrayList<Point> points, boolean respectBezel) {
		int[] pointsX = new int[points.size()];
		int[] pointsY = new int[points.size()];

		for (int i = 0; i < points.size(); i++) {
			Point p = points.get(i);
			pointsX[i] = p.x;
			pointsY[i] = p.y - (respectBezel ? 5 : 0);
		}

		g.drawPolyline(pointsX, pointsY, pointsX.length);
	}

	// TODO Improve and scale with arrow width
	// Draw a line with an arrowhead
	public void drawArrowLine(Graphics2D g2d, Point start, Point end) {
		double dy = end.y - start.y;
		double dx = end.x - start.x;
		double theta = Math.atan2(dy, dx);
		double x, y, rho = theta + Math.toRadians(40);

		g2d.drawLine(start.x, start.y, end.x, end.y);
		for (int j = 0; j < 2; j++) {
			x = end.x - 20 * Math.cos(rho);
			y = end.y - 20 * Math.sin(rho);
			g2d.draw(new Line2D.Double(end.x, end.y, x, y));
			rho = theta - Math.toRadians(40);
		}
	}

	// Draws on a BufferedImage based on the current Mouse Events
	public void doDrawEvent(ArrayList<Point> points, BufferedImage targetImage) {
		// Prepare Drawing
		Graphics2D g = targetImage.createGraphics();
		applyGraphicsSettings(g);

		// Draw
		drawPolyline(g, points, true);
		g.dispose();
	}

	// Draws a line based on the given Points TODO use polygon instead?
	public void doLineEvent(Point startPoint, Point endPoint, BufferedImage targetImage) {
		// Prepare Drawing
		Graphics2D g = targetImage.createGraphics();
		applyGraphicsSettings(g);

		// Draw
		g.drawLine(startPoint.x, startPoint.y - 5, endPoint.x, endPoint.y - 5);
		g.dispose();
	}

	// Draws a line based on the given Points
	public void doArrowEvent(Point startPoint, Point endPoint, BufferedImage targetImage) {
		// Prepare Drawing
		Graphics2D g = targetImage.createGraphics();
		applyGraphicsSettings(g);

		// Adjust points
		Point start = new Point(startPoint.x, startPoint.y - 5);
		Point end = new Point(endPoint.x, endPoint.y - 5);

		// Draw
		drawArrowLine(g, start, end);
		g.dispose();
	}

	// Draws a Rectangle based on the given Points
	public void doRectEvent(Point startPoint, Point endPoint, BufferedImage targetImage) {
		// Prepare Drawing
		Graphics2D g = targetImage.createGraphics();
		applyGraphicsSettings(g);

		// Draw
		g.drawRect(startPoint.x, startPoint.y - 5, endPoint.x - startPoint.x, endPoint.y - startPoint.y);
		g.dispose();
	}

	// Draws a Rectangle based on the given Points
	public void doTextEvent(String input, Point position, BufferedImage targetImage) {
		// Prepare Drawing
		Graphics2D g = targetImage.createGraphics();
		applyTextGraphicsSettings(g);
		
		// Draw
		g.drawString(input, position.x + 15,
				position.y + PainterSettings.data.fontSize / 2 - 5);
		g.dispose();
	}
}
