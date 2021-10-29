package jcrop.handler;

import java.awt.event.MouseAdapter;
import jcrop.crop.CropTarget;

/*
 * Used to create a MouseAdapter that has access to the CropTarget.
 * Use getCropTarget to access it!
 */
public class JCropMouseAdapter extends MouseAdapter {

	private CropTarget target;
		
	public void setCropTarget(CropTarget t) {
		target = t;
	}
	
	public CropTarget getCropTarget() {
	    return target;
	}
	
}
