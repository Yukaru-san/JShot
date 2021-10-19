package ui.crop;

import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Callable;
import java.util.function.Function;

import javax.swing.ActionMap;
import javax.swing.ImageIcon;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.KeyStroke;

import toolset.Collection;
import ui.states.CroppingState;

public class CroppingPanel extends JPanel {

	private static final long serialVersionUID = 3261471643722773626L;

	// Input
	private ActionMap actionMap;
	private InputMap inputMap;

	// Attributes
	private Painter painter;
	private BufferedImage origImg;

	// Creates a cropping Overlay for the given bi
	public CroppingPanel(BufferedImage bi) {
		// Setup for Key Maps
		actionMap = getActionMap();
		inputMap = getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);

		// Setup for Variables
		origImg = bi;
		CropTarget target = new CropTarget(3.5f, new Point(bi.getWidth(), bi.getHeight()));
		painter = new Painter(this, target);

		// Set Background
		JLabel bg = new JLabel(new ImageIcon(Collection.createDarkerBufferedImage(bi)));
		bg.setBounds(0, 0, bi.getWidth(), bi.getHeight());
		add(bg);

		// Create Mouse Listener
		MouseHandler mh = new MouseHandler(target);
		addMouseListener(mh);
		addMouseMotionListener(mh);
		
		// Timer
		CroppingPanel p = this;
		new Timer().scheduleAtFixedRate(new TimerTask() {
			@Override
			public void run() {
				p.repaint();
			}
		}, 5, 5);
	}

	// Creates a Cropping Panel and sets the stroke's size aswell
	public CroppingPanel(BufferedImage bi, int strokeSize) {
		this(bi);
		painter.getTarget().setStrokeSize(strokeSize);
	}

	// Implements a Method to call when receiving a given Key Stroke
	public void addKeyBinding(KeyStroke keyStroke, Callable<Object> targetFunc) {
		inputMap.put(keyStroke, keyStroke.toString());
		actionMap.put(keyStroke.toString(), new KeyAction(keyStroke.toString(), targetFunc));
	}
	
	// Implements a Method to call when within a specific CroppingState. Called every fixed Update.
	public void addStateEvent(CroppingState croppingStart, Function<Graphics2D, Object> function) {
		//TODO
	}
	
	// Returns the CropTarget, containg info about the current state and selected area
	public CropTarget getCropTarget() {
		return painter.getTarget();
	}

	@Override
	// Paint the UI
	public void paint(Graphics g) {
		super.paint(g);
		Graphics2D g2d = (Graphics2D) g;

		// Paint Borders
		painter.drawImage(g2d, origImg);
		painter.drawBorder(g2d);
		painter.drawDragPoints(g2d);
		
		// Set Cursor
		this.setCursor(Cursor.getPredefinedCursor(painter.getTarget().cursorStyle));
	}
}
