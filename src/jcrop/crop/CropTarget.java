package jcrop.crop;

import java.awt.Cursor;
import java.awt.Point;
import java.awt.Rectangle;

import jcrop.states.CroppingState;
import jcrop.states.DragDirection;

public class CropTarget {

	// Changeable Variables
	public int x;
	public int y;
	public int width;
	public int height;
	public float strokeSize;

	public CroppingState currentState;

	public int cursorStyle;
	public boolean useCustomCursor;
	
	public Point mousePosition;
	public boolean mousePressed;
		
	// Class-Only Variables
	private Point maxBounds;
	private int hiddenWidth;
	private int hiddenHeight;

	public CropTarget(float strokeSize, Point maxBounds) {
		x = 0;
		y = 0;
		width = 0;
		height = 0;
		
		currentState = CroppingState.CROPPING_INIT;
		
		cursorStyle = Cursor.DEFAULT_CURSOR;
		useCustomCursor = false;
		
		mousePosition = null;
		mousePressed = false;

		this.strokeSize = strokeSize;
		this.maxBounds = maxBounds;
	}
		
		
	// Returns the Points where the resizing-points should be placed
	public DragPoint[] calcDragPoints() {
		if (width == 0 || height == 0) return new DragPoint[0];
		
		// Calculate drag-point positions
		DragPoint[] dragPoints = new DragPoint[] {
				new DragPoint(new Point(x - 3 + (width / 2), y - 3), DragDirection.NORTH),
				new DragPoint(new Point(x - 3 + width, y + (height / 2) - 3), DragDirection.EAST),
				new DragPoint(new Point(x - 3 + (width / 2), y + height - 3), DragDirection.SOUTH),
				new DragPoint(new Point(x - 3, y + (height / 2) - 3), DragDirection.WEST), 
				new DragPoint(new Point(x - 3, y - 3), DragDirection.NORTH_WEST),
				new DragPoint(new Point(x - 3 + width, y - 3), DragDirection.NORTH_EAST), 
				new DragPoint(new Point(x - 3, y + height - 3), DragDirection.SOUTH_WEST),
				new DragPoint(new Point(x - 3 + width, y + height - 3), DragDirection.SOUTH_EAST),
		};

		return dragPoints;
	}
	
	// Toggles the CroppingState between Custom / Edit
	public void toggleCustomCroppingState() {
		if (currentState != CroppingState.CUSTOM)
			currentState = CroppingState.CUSTOM;
		else
			currentState = CroppingState.CROPPING_EDIT;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
		correctBoundsX();
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
		correctBoundsY();
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
		correctBoundsX();
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
		correctBoundsY();
	}

	public float getStrokeSize() {
		return strokeSize;
	}

	public void setStrokeSize(float strokeSize) {
		this.strokeSize = strokeSize;
	}

	public int getCursorStyle() {
		return cursorStyle;
	}

	public void setCursorStyle(int cursorStyle) {
		this.cursorStyle = cursorStyle;
	}

	public Point getMaxBounds() {
		return maxBounds;
	}

	private void correctBoundsX() {
		if (x < 0)
			x = 0;
		else if (x + width > maxBounds.x) {
			hiddenWidth += width - (maxBounds.x - x);
			width = maxBounds.x - x;
		} else if (hiddenWidth > 0) {
			int rest = maxBounds.x - x - width;
			width += rest;
			hiddenWidth -= rest;
		}
	}

	private void correctBoundsY() {
		if (y < 5)
			y = 5;
		else if (y + height > maxBounds.y) {
			hiddenHeight += height - (maxBounds.y - y);
			height = maxBounds.y - y;
		} else if (hiddenHeight > 0) {
			int rest = maxBounds.y - y - height;
			height += rest;
			hiddenHeight -= rest;
		}
	}

	// Functions to scale the CroppingTarget at a given direction
	public void scaleNorth(Rectangle startPosition, Point targetPosition) {
		setY(Math.min(startPosition.y + startPosition.height, targetPosition.y));
		setHeight(Math.abs(startPosition.y + startPosition.height - targetPosition.y));
	}
	public void scaleEast(Rectangle startPosition, Point targetPosition) {
		setX(Math.min(startPosition.x, targetPosition.x));
		setWidth(Math.abs(startPosition.x - targetPosition.x));
	}
	public void scaleSouth(Rectangle startPosition, Point targetPosition) {
		setY(Math.min(startPosition.y, targetPosition.y));
		setHeight(Math.abs(startPosition.y - targetPosition.y));
	}
	public void scaleWest(Rectangle startPosition, Point targetPosition) {
		setX(Math.min(startPosition.x + startPosition.width, targetPosition.x));
		setWidth(Math.abs(startPosition.x + startPosition.width - targetPosition.x));
	}

	public Point getMousePosition() {
		return mousePosition;
	}

	public void setMousePosition(Point mousePosition) {
		this.mousePosition = mousePosition;
	}


	public boolean isUseCustomCursor() {
		return useCustomCursor;
	}


	public void setUseCustomCursor(boolean useCustomCursor) {
		this.useCustomCursor = useCustomCursor;
	}
}
