package ui.overlay;

import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.concurrent.Callable;

import javax.swing.JPanel;

import ui.CustomState;
import ui.Icons;
import ui.Icons.List;
import ui.ScreenshotWindow;
import ui.ScreenshotPainter;
import jcrop.crop.CropTarget;
import jcrop.crop.Painter;
import jcrop.handler.JCropMouseAdapter;
import jcrop.events.StateEvent;
import jcrop.events.StateEventFunc;
import jcrop.states.CroppingState;
import toolset.Collection;
import toolset.PainterSettings;

public class OverlayHandler {

	// Reference to Window
	private ScreenshotWindow parentWindow;

	// Vars related to Overlay-Tools
	private UndoHistory history;
	private ScreenshotPainter painter;
	public CustomState currentCustomState = CustomState.NONE;

	// Mouse-related vars
	private boolean wasMouseDown = false;
	private Point lastMousePoint = new Point(0, 0);
	public ArrayList<Point> mousePositions = new ArrayList<Point>();

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
		public Rectangle onClick(Painter statePainter) {
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
					int h = 26;

					int x2 = t.x + t.width + 10;
					int y2 = t.y + t.height - 200;
					int w2 = 26;
					int h2 = 200;

					g2d.setColor(new Color(210, 210, 210));
					g2d.fillRoundRect(x, y, w, h, 2, 2);
					g2d.fillRoundRect(x2, y2, w2, h2, 2, 2);

					g2d.setColor(new Color(100, 100, 100));
					g2d.drawLine(x + w - 25, y + h - 22, x + w - 25, y + h - 3);

					return null;
				}

				@Override
				public Rectangle onClick(Painter statePainter) {
					// TODO
					return null;
				}
			});

	public final StateEvent DRAW_UTILITY_BTN_COPY = OverlayPrefab.generateUtilityButton(true, 0, Icons.List.COPY, false,
			new Callable<Rectangle>() {
				@Override
				public Rectangle call() throws Exception {
					parentWindow.copyCroppedImage();
					return null;
				}
			});

	public final StateEvent DRAW_UTILITY_BTN_SAVE = OverlayPrefab.generateUtilityButton(true, 1, Icons.List.SAVE, false,
			new Callable<Rectangle>() {
				@Override
				public Rectangle call() throws Exception {
					parentWindow.saveCroppedImage();
					return null;
				}
			});

	public final StateEvent DRAW_UTILITY_BTN_EXIT = OverlayPrefab.generateUtilityButton(true, 2, Icons.List.CLOSE,
			false, new Callable<Rectangle>() {
				@Override
				public Rectangle call() throws Exception {
					System.exit(0);
					return null;
				}
			});

	public final StateEvent DRAW_UTILITY_BTN_DRAW = new StateEvent(
			new StateEventFunc<Graphics2D, Painter, Rectangle>() {
				@Override
				public Rectangle onClick(Painter statePainter) {
					toggleCustomCroppingState(CustomState.DRAW);

					return null;
				}

				@Override
				public Rectangle onState(Graphics2D g2d, Painter statePainter) {
					// Get Target
					CropTarget target = statePainter.getTarget();

					// Preview or conclude drawing
					if (currentCustomState == CustomState.DRAW && target.currentState == CroppingState.CUSTOM) {
						handleDrawStateInput(target, g2d, true, new DrawStateInterface<Point>() {

							@Override
							public void drawPreview(Point p) {
								painter.drawPolyline(g2d, mousePositions, false);
							}

							@Override
							public void concludeState(Point p) {
								painter.doDrawEvent(mousePositions, parentWindow.getCroppingPanel().getOriginalImage());
							}
						});
					}

					// Draw the Icon
					return OverlayPrefab.drawUtilityIcon(g2d, statePainter, false, 0, Icons.List.DRAW,
							currentCustomState == CustomState.DRAW);
				}

			});

	public final StateEvent DRAW_UTILITY_BTN_LINE = new StateEvent(
			new StateEventFunc<Graphics2D, Painter, Rectangle>() {

				@Override
				public Rectangle onClick(Painter statePainter) {
					toggleCustomCroppingState(CustomState.LINE);

					return null;
				}

				@Override
				public Rectangle onState(Graphics2D g2d, Painter statePainter) {
					// Get Target
					CropTarget target = statePainter.getTarget();

					// Preview or conclude drawing
					if (currentCustomState == CustomState.LINE && target.currentState == CroppingState.CUSTOM) {
						handleDrawStateInput(target, g2d, true, new DrawStateInterface<Point>() {

							@Override
							public void drawPreview(Point p) {
								g2d.drawLine(lastMousePoint.x, lastMousePoint.y, p.x, p.y);
							}

							@Override
							public void concludeState(Point p) {
								painter.doLineEvent(lastMousePoint, p,
										parentWindow.getCroppingPanel().getOriginalImage());
							}

						});
					}

					// Draw the Icon
					return OverlayPrefab.drawUtilityIcon(g2d, statePainter, false, 1, Icons.List.LINE,
							currentCustomState == CustomState.LINE);
				}

			});

	public final StateEvent DRAW_UTILITY_BTN_ARROW = new StateEvent(
			new StateEventFunc<Graphics2D, Painter, Rectangle>() {

				@Override
				public Rectangle onClick(Painter statePainter) {
					toggleCustomCroppingState(CustomState.ARROW);

					return null;
				}

				@Override
				public Rectangle onState(Graphics2D g2d, Painter statePainter) {
					// Get Target
					CropTarget target = statePainter.getTarget();

					// Preview or conclude drawing
					if (currentCustomState == CustomState.ARROW && target.currentState == CroppingState.CUSTOM) {
						handleDrawStateInput(target, g2d, true, new DrawStateInterface<Point>() {

							@Override
							public void drawPreview(Point p) {
								painter.drawArrowLine(g2d, lastMousePoint, p);
							}

							@Override
							public void concludeState(Point p) {
								painter.doArrowEvent(lastMousePoint, p,
										parentWindow.getCroppingPanel().getOriginalImage());
							}
						});
					}

					// Draw the Icon
					return OverlayPrefab.drawUtilityIcon(g2d, statePainter, false, 2, Icons.List.ARROW,
							currentCustomState == CustomState.ARROW);
				}

			});

	public final StateEvent DRAW_UTILITY_BTN_RECTANGLE = new StateEvent(
			new StateEventFunc<Graphics2D, Painter, Rectangle>() {

				@Override
				public Rectangle onClick(Painter statePainter) {
					toggleCustomCroppingState(CustomState.RECTANGLE);

					return null;
				}

				@Override
				public Rectangle onState(Graphics2D g2d, Painter statePainter) {
					// Get Target
					CropTarget target = statePainter.getTarget();

					// Preview or conclude drawing
					if (currentCustomState == CustomState.RECTANGLE && target.currentState == CroppingState.CUSTOM) {
						handleDrawStateInput(target, g2d, true, new DrawStateInterface<Point>() {

							@Override
							public void drawPreview(Point p) {
								g2d.drawRect(lastMousePoint.x, lastMousePoint.y, p.x - lastMousePoint.x,
										p.y - lastMousePoint.y);
							}

							@Override
							public void concludeState(Point p) {
								painter.doRectEvent(lastMousePoint, p,
										parentWindow.getCroppingPanel().getOriginalImage());
							}
						});
					}

					// Draw the Icon
					return OverlayPrefab.drawUtilityIcon(g2d, statePainter, false, 3, Icons.List.RECTANGLE,
							currentCustomState == CustomState.RECTANGLE);
				}
			});

	public final StateEvent DRAW_UTILITY_BTN_MARKER = new StateEvent(
			new StateEventFunc<Graphics2D, Painter, Rectangle>() {

				@Override
				public Rectangle onClick(Painter statePainter) {
					// Toggle
					painter.toggleHighlight();

					return null;
				}

				@Override
				public Rectangle onState(Graphics2D g2d, Painter statePainter) {
					// Draw the Icon
					return OverlayPrefab.drawUtilityIcon(g2d, statePainter, false, 4, Icons.List.MARKER,
							painter.isHighlighted());
				}
			});

	public final StateEvent DRAW_UTILITY_BTN_TEXT = new StateEvent(
			new StateEventFunc<Graphics2D, Painter, Rectangle>() {

				// Event vars
				boolean listenerSet = false;
				private boolean saveText = false;
				private String textInput = "";
				
				// Return values (icon)
				private List icon = Icons.List.TEXT;
				private CustomState state = CustomState.TEXT;
				
				// Listener
				private KeyListener inputListener = new KeyListener() {

					@Override
					public void keyTyped(KeyEvent e) {}

					@Override
					public void keyPressed(KeyEvent e) {
						switch (e.getKeyCode()) {
						case 27: // ESC 
							// TODO, prevent app exit
							break;
						case 8: // Backspace
							if (textInput.length() > 0) {
								textInput = textInput.substring(0, textInput.length() - 1);
							}
							break;
						case 10: // Enter
							saveText = true;
							break;
						default:
							if (e.getKeyCode() > 32) {
								textInput += e.getKeyChar();
							}
						}
					}

					@Override
					public void keyReleased(KeyEvent e) {}
				};
				
				// Resets the event's vars
				private void resetSettings() {
					textInput = "";
					saveText = false;
				}
				
				@Override
				public Rectangle onClick(Painter statePainter) {
					// Set the cursor depending on the active-state
					OverlayPrefab.setCursorStyle(statePainter, Cursor.TEXT_CURSOR, currentCustomState == CustomState.TEXT);

					toggleCustomCroppingState(CustomState.TEXT);
					return null;
				}

				@Override
				public Rectangle onState(Graphics2D g2d, Painter statePainter) {
					
					// Find required vars
					CropTarget target = statePainter.getTarget();
					Point p = target.mousePosition;

					// Check for correct state: If not correct, the event is over
					if (currentCustomState != CustomState.TEXT || target.currentState != CroppingState.CUSTOM) {
						resetSettings();
						return OverlayPrefab.drawUtilityIcon(g2d, statePainter, false, 5, icon,
								currentCustomState == state);
					}
					
					// Add key listener if needed
					if (!listenerSet) {
						listenerSet = true;
						parentWindow.addKeyListener(inputListener);
					}
					
					// If within Bounds
					if (Collection.isPointInBounds(p, new Rectangle(target.x, target.y, target.width, target.height))) {

						// Catch mouse press
						if (target.mousePressed) {

							// Remember click position
							lastMousePoint = p;
							wasMouseDown = true;
							
						}
						
						// Preview, draw, save ... when mouse was clicked at least once
						if (wasMouseDown) {

							// Save and reset after [Enter] press
							if (saveText) {
								
								// Save if there is text
								if (textInput.length() > 0) {
									painter.doTextEvent(textInput, lastMousePoint, parentWindow.getCroppingPanel().getOriginalImage());
									history.push(parentWindow.getCroppingPanel().getOriginalImage());
								}
								
								// Reset and exit
								resetSettings();
								wasMouseDown = false;
								toggleCustomCroppingState(CustomState.TEXT);
								
								// Early return of icon
								return OverlayPrefab.drawUtilityIcon(g2d, statePainter, false, 5, icon,
										currentCustomState == state);
								}
							
							// -- Show input preview -- //

							// Apply text settings & write
							painter.applyTextGraphicsSettings(g2d);
							int textWidth = g2d.getFontMetrics().stringWidth(textInput);
							g2d.drawString(textInput, lastMousePoint.x + 15,
									lastMousePoint.y + PainterSettings.data.fontSize / 2);

							// Apply border settings & draw outline
							painter.applyBorderGraphicsSettings(g2d);
							g2d.drawRect(lastMousePoint.x + 10, lastMousePoint.y - PainterSettings.data.fontSize / 2,
								textWidth + 8, (int) (PainterSettings.data.fontSize * 1.4));
							
						}

					}
					// Draw the Icon
					return OverlayPrefab.drawUtilityIcon(g2d, statePainter, false, 5, icon,
							currentCustomState == state);
				}
			});

	public final StateEvent DRAW_UTILITY_BTN_SETTINGS = OverlayPrefab.generateUtilityButton(false, 6, Icons.List.SETTINGS,
			false, new Callable<Rectangle>() {
				@Override
				public Rectangle call() throws Exception {
					parentWindow.showSettings();
					return null;
				}
			});

	public final StateEvent DRAW_UTILITY_BTN_UNDO = OverlayPrefab.generateUtilityButton(false, 7, Icons.List.UNDO,
			false, new Callable<Rectangle>() {
				@Override
				public Rectangle call() throws Exception {
					BufferedImage img = history.undo();
					if (img != null)
						parentWindow.getCroppingPanel().setOriginalImage(img);

					return null;
				}
			});

	
	// Sets the JCrop's Sate to Custom / Edit
	private void toggleCustomCroppingState(CustomState targetState) {
		parentWindow.getCroppingPanel().manualUpdateCursorStyle();
		
		if (targetState == currentCustomState || currentCustomState == CustomState.NONE) {
			parentWindow.getCroppingPanel().getCropTarget().toggleCustomCroppingState();
		}

		if (targetState == currentCustomState) {
			currentCustomState = CustomState.NONE;
			return;
		}

		currentCustomState = targetState;
	}

	// Method containing things used by every drawing-event (reduce code duplicates)
	private void handleDrawStateInput(CropTarget target, Graphics2D g2d, boolean drawIndicator,
			DrawStateInterface<Point> drawState) {
		Point p = target.mousePosition;

		// If within Bounds
		if (Collection.isPointInBounds(p, new Rectangle(target.x, target.y, target.width, target.height))) {

			// Draw mouse indicator
			if (drawIndicator) {
				Shape circle = new Ellipse2D.Double(p.x - 2, p.y - 2, 4, 4);
				g2d.draw(circle);
			}

			// Prepare settings for the line
			painter.applyGraphicsSettings(g2d);

			// Draw line if mouse is down
			if (target.mousePressed) {

				// First call while btn is pressed: save position
				if (!wasMouseDown) {
					lastMousePoint = p;
				}

				// Remember mouse and position
				mousePositions.add(p);
				wasMouseDown = true;

				// Draw preview
				drawState.drawPreview(p);
			}

			// Save drawing after it is done
			else if (!target.mousePressed && wasMouseDown) {
				// Draw and conclude
				drawState.drawPreview(p);
				drawState.concludeState(p);

				// History and vars
				history.push(parentWindow.getCroppingPanel().getOriginalImage());
				wasMouseDown = false;
				mousePositions.clear();
			}
		}

		// Out of bounds && mouse released
		else if (!target.mousePressed) {
			wasMouseDown = false;
			mousePositions.clear();

		}
	}
}
