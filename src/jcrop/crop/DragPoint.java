package jCrop.crop;

import java.awt.Point;

import jCrop.states.DragDirection;

public class DragPoint {

	public Point position;
	public DragDirection direction;
	
	public DragPoint(Point position, DragDirection direction) {
		this.position = position;
		this.direction = direction;
	}
	

}
