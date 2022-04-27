package jShot.toolset;

import java.awt.Color;
import java.io.Serializable;

//Used to save and load user settings
public class SettingsData implements Serializable {

	private static final long serialVersionUID = 981191145754577397L;
	
	public int fontSize;
	public String fontName;

	public int strokeWidth;
	public int h_strokeWidth;

	public Color color;
	public Color h_color;

	// Load default data on creation
	public SettingsData() {
		applyDefault();
	}
	
	// Applies the default settings
	public void applyDefault() {
		fontSize = 16;
		fontName = "Arial";
		strokeWidth = 3;
		h_strokeWidth = 12;
		color = Color.RED;
		h_color = new Color(255, 255, 0, 50);
	}
}