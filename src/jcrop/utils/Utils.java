package jCrop.utils;

import java.awt.BasicStroke;

public class Utils {
	
	public static BasicStroke createBorderStroke(float strokeSize) {
		return new BasicStroke(1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[]{strokeSize}, 0);
	}
	
}
