import java.awt.Dimension;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.util.concurrent.Callable;

import javax.swing.KeyStroke;

import ui.Icons;
import ui.Overlays;
import ui.ScreenshotWindow;
import ui.crop.CroppingPanel;
import ui.states.CroppingState;

public class Main {
	
	public static void main(String[] args) throws Exception {	
		Dimension d = ScreenTools.getMaxDimensions();
		BufferedImage img = ScreenTools.takeScreenshot(d);
		
		CroppingPanel p = new CroppingPanel(img);
		prepareKeyEvents(p);
		prepareStateEvents(p);
		
		ScreenshotWindow w = new ScreenshotWindow(d, p);
		Overlays.parentWindow = w;
	}
	
	// Adds KeyEvents to the Cropping Panel
	public static void prepareKeyEvents(CroppingPanel p) {
		p.addKeyBinding(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), new Callable<Object>() {
			public Object call() throws Exception {
				System.exit(0);
				return null;
			}
		});
	}
	
	// Adds State-Related Paintings to the Cropping Panel
	public static void prepareStateEvents(CroppingPanel p) {
		p.addStateEvent(CroppingState.CROPPING_START, Overlays.DRAW_INITIAL_TOOLTIP);
		p.addStateEvent(CroppingState.CROPPING_EDIT, Overlays.DRAW_UTILITY_OVERLAY_BG);
		// Horizontal Btns
		p.addStateEvent(CroppingState.CROPPING_EDIT, Overlays.DRAW_UTILITY_BTN_COPY);
		p.addStateEvent(CroppingState.CROPPING_EDIT, Overlays.DRAW_UTILITY_BTN_SAVE);
		p.addStateEvent(CroppingState.CROPPING_EDIT, Overlays.DRAW_UTILITY_BTN_EXIT);
		// Vertical Btns
		p.addStateEvent(CroppingState.CROPPING_EDIT, Overlays.DRAW_UTILITY_BTN_DRAW);
		p.addStateEvent(CroppingState.CROPPING_EDIT, Overlays.DRAW_UTILITY_BTN_LINE);
		p.addStateEvent(CroppingState.CROPPING_EDIT, Overlays.DRAW_UTILITY_BTN_ARROW);
		p.addStateEvent(CroppingState.CROPPING_EDIT, Overlays.DRAW_UTILITY_BTN_RECTANGLE);
		p.addStateEvent(CroppingState.CROPPING_EDIT, Overlays.DRAW_UTILITY_BTN_MARKER);
		p.addStateEvent(CroppingState.CROPPING_EDIT, Overlays.DRAW_UTILITY_BTN_TEXT);
		p.addStateEvent(CroppingState.CROPPING_EDIT, Overlays.DRAW_UTILITY_BTN_COLOR);
		p.addStateEvent(CroppingState.CROPPING_EDIT, Overlays.DRAW_UTILITY_BTN_UNDO);
	}
}
