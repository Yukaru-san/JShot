import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.util.concurrent.Callable;
import java.util.function.Function;

import javax.swing.KeyStroke;

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
		
		ScreenshotWindow w = new ScreenshotWindow(d);
		w.setContentPane(p);
		w.repaint();
		w.setVisible(true);	
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
		p.addStateEvent(CroppingState.CROPPING_START, new Function<Graphics2D, Object>() {
	
			@Override
			public Object apply(Graphics2D g2d) {
				g2d.fillRect(0, 0, 500, 500);
				return null;
			}
			
		});
	}
}
