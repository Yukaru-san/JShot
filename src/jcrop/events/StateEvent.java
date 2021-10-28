package jcrop.events;

import java.awt.Graphics2D;
import java.awt.Rectangle;

import jcrop.crop.Painter;

public class StateEvent {

	public StateEventFunc<Graphics2D, Painter, Rectangle> eventFunction;
	private Rectangle bounds;
	
	public StateEvent(StateEventFunc<Graphics2D, Painter, Rectangle> eventFunction) {
		this.eventFunction = eventFunction;
		bounds = null;
	}
	
	public StateEvent(StateEventFunc<Graphics2D, Painter, Rectangle> eventFunction, Rectangle bounds) {
		this(eventFunction);
		this.setBounds(bounds);
	}

	public Rectangle getBounds() {
		return bounds;
	}

	public void setBounds(Rectangle bounds) {
		this.bounds = bounds;
	}
	
}
