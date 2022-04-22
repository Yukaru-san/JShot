package toolset;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.awt.RenderingHints.Key;
import java.util.HashMap;
import java.util.Map;

public class PainterSettings {
	public static RenderingHints renderingHints;

	public static int fontSize;
	public static Font font;
	
	public static Stroke stroke;
	public static Stroke h_stroke;
	
	public static Color color;
	public static Color h_color;
	
	public static Point lastDrawnPoint;
	
	// Lazy load settings on first usage
	static {
		prepareSettings();
	}
	
	// Prepares the settings using either user or default values
	public static void prepareSettings() {
		// Rendering
		Map<Key, Object> hintsMap = new HashMap<RenderingHints.Key, Object>();
		hintsMap.put(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
		hintsMap.put(RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_ENABLE);
		hintsMap.put(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		renderingHints = new RenderingHints(hintsMap);

		// Load from saved config if possible TODO
		String saveData = AppPrefs.Settings.get("");
		if (saveData.length() > 0 && loadSettings(saveData)) {
			return;
		}		
		
		// Load default
		fontSize = 16;
		font = new Font("Arial", Font.BOLD, fontSize); 
		stroke = new BasicStroke(3, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND, 1.7f);
		h_stroke = new BasicStroke(12, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND, 1.7f);
		color = Color.RED;
		h_color = new Color(255, 255, 0, 50);
	}

	// Load user settings
	private static boolean loadSettings (String data) {
		String[] settings = data.split("%%");
		
		try {
			fontSize = Integer.parseInt(settings[0]);
			font = new Font(settings[1], Font.BOLD, fontSize);
			stroke = new BasicStroke(Integer.parseInt(settings[2]), BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND, 1.7f);
			h_stroke = new BasicStroke(Integer.parseInt(settings[3]), BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND, 1.7f);
			
			String[] rgba = settings[4].split(",");
			color = new Color (Integer.parseInt(rgba[0]), Integer.parseInt(rgba[1]), Integer.parseInt(rgba[2]), Integer.parseInt(rgba[3]));
			
			String[] h_rgba = settings[5].split(",");
			h_color = new Color (Integer.parseInt(h_rgba[0]), Integer.parseInt(h_rgba[1]), Integer.parseInt(h_rgba[2]), Integer.parseInt(h_rgba[3]));
		} catch (Exception e) {
			return false;
		}
		
		return true;
	}
	
	// Save current settings
	public static void saveSettings() {
		StringBuilder sb = new StringBuilder();
		sb.append(fontSize).append("%%");
		sb.append(font.getName()).append("%%");
		sb.append(fontSize).append("%%");
		
		
	//	AppPrefs.Settings.put(outputFile.getParentFile().getAbsolutePath());
	}
}
