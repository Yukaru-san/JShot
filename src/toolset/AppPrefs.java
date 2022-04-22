package toolset;

import java.util.prefs.Preferences;

public enum AppPrefs {
	FileLocation;

	private static Preferences prefs = Preferences.userRoot().node(AppPrefs.class.getName());

	public String get(String defaultValue) {
		return prefs.get(this.name(), defaultValue);
	}

	public void put(String value) {
		prefs.put(this.name(), value);
	}
}