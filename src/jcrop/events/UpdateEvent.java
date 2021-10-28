package jcrop.events;

/*
 * UpdateEvent lets you implement custom functions that are called on every Fixed Update.
 * 
 */
public interface UpdateEvent<CropTarget> {

	// onUpdate is called on every tick
	public void onUpdate(CropTarget target);
}
