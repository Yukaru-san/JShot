package toolset;

public class Constants {

	public static String SAVE_PATH = System.getProperty("user.home") + "/.yukaru/jshot/";
	public static String SAVE_FILE = "jshot.save";
	public static int SETTINGS_WINDOW_WIDTH = 500;
	public static int SETTINGS_WINDOW_HEIGHT = 350;
	public static int SETTINGS_COLORWINDOW_WIDTH = 650;
	public static int SETTINGS_COLORWINDOW_HEIGHT = 500;
	
	public enum ColorOptions {
		DEFAULT,
		HIGHLIGHTED
	}
}
