package toolset;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.awt.RenderingHints.Key;
import java.util.HashMap;
import java.util.Map;

public class PainterSettings {

	public static RenderingHints renderingHints;
	public static Point lastDrawnPoint;
	public static Font font;

	public static Stroke stroke;
	public static Stroke h_stroke;

	public static SettingsData data;

	// Lazy load settings data
	static {
		loadSettings();
	}
	
	// Load user settings or default if not present
	private static void loadSettings() {
		FileInputStream f;
		ObjectInputStream o;
		
		try {
			// Try loading saved data
			f = new FileInputStream(new File(Constants.SAVE_PATH + Constants.SAVE_FILE));
			o = new ObjectInputStream(f);

			SettingsData d = (SettingsData) o.readObject();
			data = d;

			f.close();
			o.close();

			System.out.println("Using saved data.");
		} catch (Exception e) {
			// Use default data instead
			data = new SettingsData();
			System.out.println("Using default data.");
		}
		
		// Prepare data-reliant objects
		prepareObjects();
	}

	// Prepares the objects outside the savable data-object
	private static void prepareObjects() {
		// Rendering
		Map<Key, Object> hintsMap = new HashMap<RenderingHints.Key, Object>();
		hintsMap.put(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
		hintsMap.put(RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_ENABLE);
		hintsMap.put(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		renderingHints = new RenderingHints(hintsMap);

		// Font
		font = new Font(data.fontName, Font.BOLD, data.fontSize);
		
		// Stroke
		stroke = new BasicStroke(data.strokeWidth, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND, 1.7f);
		h_stroke = new BasicStroke(data.h_strokeWidth, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND, 1.7f);
	}

	// Save current settings
	public static void saveSettings() {
		FileOutputStream f;
		ObjectOutputStream o;

		try {
			File savePath = new File(Constants.SAVE_PATH);
			savePath.mkdirs();

			f = new FileOutputStream(new File(Constants.SAVE_PATH + Constants.SAVE_FILE));
			o = new ObjectOutputStream(f);

			o.writeObject(data);

			f.close();
			o.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
