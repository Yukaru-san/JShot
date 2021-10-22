package ui.crop;

import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
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
import ui.states.StateEvent;

public class CroppingPanel extends JPanel {

	private static final long serialVersionUID = 3261471643722773626L;

	// Input & Passed down actions
	private ActionMap actionMap;
	private InputMap inputMap;
	private HashMap<CroppingState, ArrayList<StateEvent>> stateEvents;

	// Attributes
	private Painter painter;
	private BufferedImage origImg;
	private MouseHandler mouseHandler;

	// Creates a cropping Overlay for the given bi
	public CroppingPanel(BufferedImage bi) {
		// Setup for Key Maps
		actionMap = getActionMap();
		inputMap = getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
		stateEvents = new HashMap<CroppingState, ArrayList<StateEvent>>();

		// Setup for Variables
		origImg = bi;
		CropTarget target = new CropTarget(3.5f, new Point(bi.getWidth(), bi.getHeight() + 5));
		painter = new Painter(this, target);

		// Set Background
		JLabel bg = new JLabel(new ImageIcon(Collection.createDarkerBufferedImage(bi)));
		bg.setBounds(0, 0, bi.getWidth(), bi.getHeight());
		add(bg);

		// Create Mouse Listener
		mouseHandler = new MouseHandler(target, stateEvents);
		addMouseListener(mouseHandler);
		addMouseMotionListener(mouseHandler);

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
	
	// Implements a Method to call when within a specific CroppingState. Called
	// every fixed Update.
	public void addStateEvent(CroppingState croppingState, StateEvent function) {
		ArrayList<StateEvent> stateList = stateEvents.get(croppingState);

		if (stateList == null) {
			stateList = new ArrayList<StateEvent>();
			stateEvents.put(croppingState, stateList);
		}

		stateList.add(function);
	}

	// Returns the CropTarget, containg info about the current state and selected
	// area
	public CropTarget getCropTarget() {
		return painter.getTarget();
	}
	
	public BufferedImage getCroppedImage() {
		CropTarget target = painter.getTarget();
		return origImg.getSubimage(target.x, target.y-5, target.width, target.height);
	}

	@Override
	// Paint the UI
	public void paint(Graphics g) {
		super.paint(g);
		Graphics2D g2d = (Graphics2D) g;

		// Paint Borders
		painter.drawImageWithinTarget(g2d, origImg);
		painter.drawBorder(g2d);
		painter.drawDragPoints(g2d);

		// Set Cursor
		this.setCursor(Cursor.getPredefinedCursor(painter.getTarget().cursorStyle));

		// Call functions based on current state
		g2d.setStroke(painter.defaultStroke);

		ArrayList<StateEvent> stateList = stateEvents.get(painter.getTarget().currentState);
		if (stateList != null) {
			stateList.forEach(stateEvent -> {
				stateEvent.setBounds(stateEvent.eventFunction.onState(g2d, painter));
			});
		}
	}
}
