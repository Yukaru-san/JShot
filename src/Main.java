import hooks.Shortcut;
import hooks.Tray;
import jShot.JShot;

public class Main {
	
	public static void main(String[] args) throws Exception {	
		
		JShot jShot = new JShot();
		
		Tray.prepareTray(jShot);
		Shortcut.prepareShortcut(jShot);
		
	}
	
}
