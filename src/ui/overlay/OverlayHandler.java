package ui.overlay;

import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Color;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.event.MouseEvent;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.util.concurrent.Callable;

import ui.CustomState;
import ui.Icons;
import ui.ScreenshotWindow;
import ui.ScreenshotPainter;
import jcrop.crop.CropTarget;
import jcrop.crop.Painter;
import jcrop.events.UpdateEvent;
import jcrop.handler.JCropMouseAdapter;
import jcrop.events.StateEvent;
import jcrop.events.StateEventFunc;
import jcrop.states.CroppingState;
import toolset.Collection;

public class OverlayHandler {

	// Reference to Window
	private ScreenshotWindow parentWindow;

	// Vars related to Overlay-Tools
	private UndoHistory history;
	private ScreenshotPainter painter;
	public CustomState currentCustomState = CustomState.NONE;
	private boolean wasMouseDown = false;

	public OverlayHandler(ScreenshotWindow window) {
		painter = new ScreenshotPainter();
		parentWindow = window;
		history = new UndoHistory(parentWindow.getCroppingPanel().getOriginalImage());
	}

	public final JCropMouseAdapter MOUSE_LISTENER = new JCropMouseAdapter() {
		@Override
		public void mouseDragged(MouseEvent e) {
			CropTarget target = getCropTarget();
			
			if (target.currentState != CroppingState.CUSTOM)
				return;
			
			target.mousePosition = e.getPoint();
			
			switch (currentCustomState) {
			case PAINTING:
				painter.doDrawEvent(target, parentWindow.getCroppingPanel().getOriginalImage());
			default:
				// Do nothing
			}
		}
		
		@Override
		public void mouseReleased(MouseEvent e) {
			CropTarget target = getCropTarget();
			
			if (target.currentState != CroppingState.CUSTOM)
				return;
			
			switch (currentCustomState) {
			case PAINTING:
				painter.resetDrawEvent();
			default:
				// Do nothing
			}
		}
	};

	// Following up are all the UI Events
	public final StateEvent DRAW_INITIAL_TOOLTIP = new StateEvent(new StateEventFunc<Graphics2D, Painter, Rectangle>() {
		@Override
		public Rectangle onState(Graphics2D g2d, Painter painter) {
			CropTarget t = painter.getTarget();

			if (t.mousePosition == null)
				return null;

			int x = t.mousePosition.x + 20;
			int y = t.mousePosition.y + 20;
			int w = 73;
			int h = 20;

			g2d.setColor(Color.GRAY);
			g2d.fillRect(x - 1, y - 1, w + 2, h + 2);
			g2d.setColor(Color.WHITE);
			g2d.fillRect(x, y, w, h);

			g2d.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 12));
			g2d.setColor(Color.BLACK);
			g2d.drawString("Select Area", x + 5, y + 15);

			return null;
		}

		@Override
		public Rectangle onClick() {
			return null;
		}
	});

	public final StateEvent DRAW_UTILITY_OVERLAY_BG = new StateEvent(
			new StateEventFunc<Graphics2D, Painter, Rectangle>() {
				@Override
				public Rectangle onState(Graphics2D g2d, Painter painter) {
					CropTarget t = painter.getTarget();

					int x = t.x + t.width - 76;
					int y = t.y + t.height + 10;
					int w = 76;
					int h = 25;

					int x2 = t.x + t.width + 10;
					int y2 = t.y + t.height - 200;
					int w2 = 25;
					int h2 = 200;

					g2d.setColor(new Color(210, 210, 210));
					g2d.fillRoundRect(x, y, w, h, 2, 2);
					g2d.fillRoundRect(x2, y2, w2, h2, 2, 2);

					g2d.setColor(new Color(100, 100, 100));
					g2d.drawLine(x + w - 25, y + h - 22, x + w - 25, y + h - 3);

					// Color-selection Rect
					int x3 = t.x + t.width - 75 + 85;
					int y3 = t.y + t.height + 10 + 6 * 24 - 205;

					g2d.setColor(Color.RED);
					g2d.fillRect(x3 + 2, y3 + 2, 21, 20);

					return null;
				}

				@Override
				public Rectangle onClick() {
					// TODO
					return null;
				}
			});

	public final StateEvent DRAW_UTILITY_BTN_COPY = OverlayPrefab.generateUtilityButton(true, 0, Icons.List.COPY,
			new Callable<Rectangle>() {
				@Override
				public Rectangle call() throws Exception {
					parentWindow.copyCroppedImage();
					return null;
				}
			});

	public final StateEvent DRAW_UTILITY_BTN_SAVE = OverlayPrefab.generateUtilityButton(true, 1, Icons.List.SAVE,
			new Callable<Rectangle>() {
				@Override
				public Rectangle call() throws Exception {
					parentWindow.saveCroppedImage();
					return null;
				}
			});

	public final StateEvent DRAW_UTILITY_BTN_EXIT = OverlayPrefab.generateUtilityButton(true, 2, Icons.List.CLOSE,
			new Callable<Rectangle>() {
				@Override
				public Rectangle call() throws Exception {
					System.exit(0);
					return null;
				}
			});

	public final StateEvent DRAW_UTILITY_BTN_DRAW = new StateEvent(
			new StateEventFunc<Graphics2D, Painter, Rectangle>() {
				@Override
				public Rectangle onState(Graphics2D g2d, Painter painter) {
					// Get Target
					CropTarget target = painter.getTarget();

					// Draw Drawing Indicator at Mouse
					if (currentCustomState == CustomState.PAINTING && target.currentState == CroppingState.CUSTOM) {
						Point p = target.mousePosition;

						// Dont do anything if not within Bounds
						if (Collection.isPointInBounds(p,
								new Rectangle(target.x, target.y, target.width, target.height))) {

							Shape circle = new Ellipse2D.Double(p.x - 2, p.y - 2, 4, 4);
							g2d.draw(circle);

							// Check if user is painting
							if (target.mousePressed) {
								wasMouseDown = true;
							}

							// Push to Stack when complete
							else if (!target.mousePressed && wasMouseDown) {
								wasMouseDown = false;
								history.push(parentWindow.getCroppingPanel().getOriginalImage());
							}
						}
					}

					// Draw the Icon
					return OverlayPrefab.drawUtilityIcon(g2d, painter, false, 0, Icons.List.DRAW);
				}

				@Override
				public Rectangle onClick() {
					toggleCustomCroppingState();
					currentCustomState = CustomState.PAINTING;
					history.push(parentWindow.getCroppingPanel().getOriginalImage());
					
					return null;
				}
			});

	public final StateEvent DRAW_UTILITY_BTN_LINE = OverlayPrefab.generateUtilityButton(false, 1, Icons.List.LINE,
			new Callable<Rectangle>() {
				@Override
				public Rectangle call() throws Exception {
					System.out.println("LINE (TODO)");
					return null;
				}
			});

	public final StateEvent DRAW_UTILITY_BTN_ARROW = OverlayPrefab.generateUtilityButton(false, 2, Icons.List.ARROW,
			new Callable<Rectangle>() {
				@Override
				public Rectangle call() throws Exception {
					System.out.println("ARROW (TODO)");
					return null;
				}
			});

	public final StateEvent DRAW_UTILITY_BTN_RECTANGLE = OverlayPrefab.generateUtilityButton(false, 3,
			Icons.List.RECTANGLE, new Callable<Rectangle>() {
				@Override
				public Rectangle call() throws Exception {
					System.out.println("RECTANGLE (TODO)");
					return null;
				}
			});

	public final StateEvent DRAW_UTILITY_BTN_MARKER = OverlayPrefab.generateUtilityButton(false, 4, Icons.List.MARKER,
			new Callable<Rectangle>() {
				@Override
				public Rectangle call() throws Exception {
					System.out.println("MAKER (TODO)");
					return null;
				}
			});

	public final StateEvent DRAW_UTILITY_BTN_TEXT = OverlayPrefab.generateUtilityButton(false, 5, Icons.List.TEXT,
			new Callable<Rectangle>() {
				@Override
				public Rectangle call() throws Exception {
					System.out.println("TEXT (TODO)");
					return null;
				}
			});

	public final StateEvent DRAW_UTILITY_BTN_COLOR = OverlayPrefab.generateUtilityButton(false, 6, Icons.List.EXPAND,
			new Callable<Rectangle>() {
				@Override
				public Rectangle call() throws Exception {
					System.out.println("COLOR (TODO)");
					return null;
				}
			});

	public final StateEvent DRAW_UTILITY_BTN_UNDO = OverlayPrefab.generateUtilityButton(false, 7, Icons.List.UNDO,
			new Callable<Rectangle>() {
				@Override
				public Rectangle call() throws Exception {
					BufferedImage img = history.undo();
					if (img != null)
						parentWindow.getCroppingPanel().setOriginalImage(img);
					else
						painter.resetDrawEvent();

					return null;
				}
			});

	// Sets the JCrop's Sate to Custom / Edit
	private void toggleCustomCroppingState() {
		parentWindow.getCroppingPanel().getCropTarget().toggleCustomCroppingState();
	}
}
