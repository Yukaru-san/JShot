package ui;

import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Color;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.Callable;

import javax.imageio.ImageIO;

import toolset.Collection;
import ui.crop.CropTarget;
import ui.crop.Painter;
import ui.states.CroppingState;
import ui.states.StateEvent;
import ui.states.StateEventFunc;

public class Overlays {
	
	// Reference to Window
	public static ScreenshotWindow parentWindow;
	
	// Following up are all the UI Events	
	public static final StateEvent DRAW_INITIAL_TOOLTIP = new StateEvent(
			new StateEventFunc<Graphics2D, Painter, Rectangle>() {
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

	public static final StateEvent DRAW_UTILITY_OVERLAY_BG = new StateEvent(
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
					
					g2d.setColor(new Color(210,210,210));
					g2d.fillRoundRect(x, y, w, h, 2, 2);
					g2d.fillRoundRect(x2, y2, w2, h2, 2, 2);

					g2d.setColor(new Color(100, 100, 100));
					g2d.drawLine(x+w-25, y+h-22, x+w-25, y+h-3);
					
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

	public static final StateEvent DRAW_UTILITY_BTN_COPY = generateUtilityButton(true, 0, Icons.List.COPY,
			new Callable<Rectangle>() {
				@Override
				public Rectangle call() throws Exception {
					parentWindow.copyCroppedImage();
					return null;
				}
			});

	public static final StateEvent DRAW_UTILITY_BTN_SAVE = generateUtilityButton(true, 1, Icons.List.SAVE,
			new Callable<Rectangle>() {
				@Override
				public Rectangle call() throws Exception {
					parentWindow.saveCroppedImage();
					return null;
				}
			});

	public static final StateEvent DRAW_UTILITY_BTN_EXIT = generateUtilityButton(true, 2, Icons.List.CLOSE,
			new Callable<Rectangle>() {
				@Override
				public Rectangle call() throws Exception {
					System.exit(0);
					return null;
				}
			});
	
	public static final StateEvent DRAW_UTILITY_BTN_DRAW = generateUtilityButton(false, 0, Icons.List.DRAW,
			new Callable<Rectangle>() {
				@Override
				public Rectangle call() throws Exception {
					System.out.println("DRAW");
					CropTarget target = parentWindow.getCroppingPanel().getCropTarget();
					if (target.currentState != CroppingState.PAINTING) 
						target.currentState = CroppingState.PAINTING;
					else
						target.currentState = CroppingState.CROPPING_EDIT;
				
					return null;
				}
			});
	public static final StateEvent DRAW_UTILITY_BTN_LINE = generateUtilityButton(false, 1, Icons.List.LINE,
			new Callable<Rectangle>() {
				@Override
				public Rectangle call() throws Exception {
					System.out.println("Exit");
					return null;
				}
			});
	
	public static final StateEvent DRAW_UTILITY_BTN_ARROW = generateUtilityButton(false, 2, Icons.List.ARROW,
			new Callable<Rectangle>() {
				@Override
				public Rectangle call() throws Exception {
					System.out.println("Exit");
					return null;
				}
			});
	
	public static final StateEvent DRAW_UTILITY_BTN_RECTANGLE = generateUtilityButton(false, 3, Icons.List.RECTANGLE,
			new Callable<Rectangle>() {
				@Override
				public Rectangle call() throws Exception {
					System.out.println("Exit");
					return null;
				}
			});
	
	public static final StateEvent DRAW_UTILITY_BTN_MARKER = generateUtilityButton(false, 4, Icons.List.MARKER,
			new Callable<Rectangle>() {
				@Override
				public Rectangle call() throws Exception {
					System.out.println("Exit");
					return null;
				}
			});
	
	public static final StateEvent DRAW_UTILITY_BTN_TEXT = generateUtilityButton(false, 5, Icons.List.TEXT,
			new Callable<Rectangle>() {
				@Override
				public Rectangle call() throws Exception {
					System.out.println("Exit");
					return null;
				}
			});
	
	public static final StateEvent DRAW_UTILITY_BTN_COLOR = generateUtilityButton(false, 6, Icons.List.EXPAND,
			new Callable<Rectangle>() {
				@Override
				public Rectangle call() throws Exception {
					System.out.println("Exit");
					return null;
				}
			});
	
	public static final StateEvent DRAW_UTILITY_BTN_UNDO = generateUtilityButton(false, 7, Icons.List.UNDO,
			new Callable<Rectangle>() {
				@Override
				public Rectangle call() throws Exception {
					System.out.println("Exit");
					return null;
				}
			});

	// Generates a Utility Btn at the given position (index). Saves code repetition
	// above
	private static StateEvent generateUtilityButton(boolean isHorizontal, int index, Icons.List iconEnum,
			Callable<Rectangle> onClick) {
		return new StateEvent(new StateEventFunc<Graphics2D, Painter, Rectangle>() {
			@Override
			public Rectangle onState(Graphics2D g2d, Painter painter) {
				CropTarget t = painter.getTarget();

				int x = t.x + t.width - 75 + (isHorizontal ? index * 25 : 85);
				int y = t.y + t.height + 10 + (!isHorizontal ? index * 24 - 205 : 0);

				painter.drawImageAt(g2d, Icons.getIcon(iconEnum), x, y);
				
				return new Rectangle(x, y, 24, 24);
			}

			@Override
			public Rectangle onClick() {
				try {
					// Targeting is kinda bugged..?
					return onClick.call();
				} catch (Exception e) {
					e.printStackTrace();
				}
				return null;
			}
		});
	}

}
