package jCrop.events;

/*
 * StateEvent lets you implement custom functions during Cropping States.
 * The Rectangle returned by onState should represent whatever you have drawn and
 * is used to make the onClick function work.
 * 
 */
public interface StateEventFunc<Graphics2D, Painter, Rectangle> {

	// onState is called on every tick within the defined state
	public Rectangle onState(Graphics2D g2d, Painter painter);
	
	// onClick is called whenever the Event was clicked
	public Rectangle onClick(Painter painter);

}
