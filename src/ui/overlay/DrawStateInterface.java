package ui.overlay;

/*
 * DrawStateInterface is used to spare some repetitive code on draw-states
 */
public interface DrawStateInterface<Point> {
	
	// Draw your preview here (once per frame)
	public void drawPreview(Point endPoint);
	
	// Finalize your drawing here
	public void concludeState(Point endPoint);
		
}