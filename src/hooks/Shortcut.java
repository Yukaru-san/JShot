package hooks;

import java.awt.AWTException;

import com.github.kwhat.jnativehook.NativeHookException;
import com.github.kwhat.jnativehook.keyboard.NativeKeyEvent;

import jShot.JShot;
import jUtils.interfaces.Callback;
import jUtils.jShortcut.JShortcut;
import jUtils.jShortcut.KeyAction;

public class Shortcut {

	public static void prepareShortcut(JShot jShot) {
		JShortcut s = new JShortcut();

		s.addAction(new KeyAction(NativeKeyEvent.VC_PRINTSCREEN, new Callback() {
			@Override
			public void call() {
				try {
					jShot.takeScreenshot();
				} catch (AWTException e) {
					e.printStackTrace();
				}
			}
		}));

		try {
			s.start();
		} catch (NativeHookException e) {
			e.printStackTrace();
		}
	}

}