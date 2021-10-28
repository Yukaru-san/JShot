import java.awt.Dimension;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.concurrent.Callable;

import javax.swing.KeyStroke;

import jcrop.crop.CroppingPanel;
import jcrop.states.CroppingState;
import toolset.ScreenTools;
import ui.Icons;
import ui.ScreenshotWindow;
import ui.overlay.OverlayHandler;

public class Main {
	
	public static void main(String[] args) throws Exception {	
		Dimension d = ScreenTools.getMaxDimensions();
		BufferedImage img = ScreenTools.takeScreenshot(d);
		
		
		CroppingPanel p = new CroppingPanel(img);
		
		ScreenshotWindow w = new ScreenshotWindow(d, p);
		OverlayHandler oH = new OverlayHandler(w);
		
		prepareKeyEvents(p);
		prepareStateEvents(p, oH);
		prepareUpdateCallback(p, oH);
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
	public static void prepareStateEvents(CroppingPanel p, OverlayHandler oH) {
		ArrayList<CroppingState> editPaintStates = new ArrayList<CroppingState>();
		editPaintStates.add(CroppingState.CROPPING_EDIT);
		editPaintStates.add(CroppingState.CUSTOM);
		
		p.addStateEvent(CroppingState.CROPPING_START, oH.DRAW_INITIAL_TOOLTIP);
		p.addStateEvent(editPaintStates, oH.DRAW_UTILITY_OVERLAY_BG);
		// Horizontal Btns
		p.addStateEvent(editPaintStates, oH.DRAW_UTILITY_BTN_COPY);
		p.addStateEvent(editPaintStates, oH.DRAW_UTILITY_BTN_SAVE);
		p.addStateEvent(editPaintStates, oH.DRAW_UTILITY_BTN_EXIT);
		// Vertical Btns
		p.addStateEvent(editPaintStates, oH.DRAW_UTILITY_BTN_DRAW);
		p.addStateEvent(editPaintStates, oH.DRAW_UTILITY_BTN_LINE);
		p.addStateEvent(editPaintStates, oH.DRAW_UTILITY_BTN_ARROW);
		p.addStateEvent(editPaintStates, oH.DRAW_UTILITY_BTN_RECTANGLE);
		p.addStateEvent(editPaintStates, oH.DRAW_UTILITY_BTN_MARKER);
		p.addStateEvent(editPaintStates, oH.DRAW_UTILITY_BTN_TEXT);
		p.addStateEvent(editPaintStates, oH.DRAW_UTILITY_BTN_COLOR);
		p.addStateEvent(editPaintStates, oH.DRAW_UTILITY_BTN_UNDO);
	}
	
	// Prepares a function that is called on every fixed update
	public static void prepareUpdateCallback(CroppingPanel p, OverlayHandler oH) {
		p.setUpdateCallback(oH.UPDATE_CALLBACK);
	}
}
