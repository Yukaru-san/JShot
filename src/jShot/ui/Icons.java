package jShot.ui;

import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

import jUtils.tools.ImageTools;

public class Icons {

	public enum List {
		ARROW, CLOSE, COPY, DRAW, LINE, MARKER, RECTANGLE, TEXT, SETTINGS, SAVE, UNDO
	}

	public static BufferedImage ICON_ARROW;
	public static BufferedImage ICON_CLOSE;
	public static BufferedImage ICON_COPY;
	public static BufferedImage ICON_DRAW;
	public static BufferedImage ICON_LINE;
	public static BufferedImage ICON_MARKER;
	public static BufferedImage ICON_RECTANGLE;
	public static BufferedImage ICON_TEXT;
	public static BufferedImage ICON_SETTINGS;
	public static BufferedImage ICON_SAVE;
	public static BufferedImage ICON_UNDO;

	static {
		ImageIO.setUseCache(false);

		new Thread() {
			@Override
			public void run() {
				try {
					ICON_ARROW = ImageTools.loadEmbeddedImage("/jShot/ui/overlay/resources/arrow.png");
					ICON_CLOSE = ImageTools.loadEmbeddedImage("/jShot/ui/overlay/resources/close.png");
					ICON_COPY = ImageTools.loadEmbeddedImage("/jShot/ui/overlay/resources/copy.png");
					ICON_TEXT = ImageTools.loadEmbeddedImage("/jShot/ui/overlay/resources/text.png");
				} catch (Exception e) {
					System.err.println("Couldn't load embedded images!");
				}
			}
		}.start();

		new Thread() {
			@Override
			public void run() {
				try {
					ICON_DRAW = ImageTools.loadEmbeddedImage("/jShot/ui/overlay/resources/draw.png");
					ICON_SETTINGS = ImageTools.loadEmbeddedImage("/jShot/ui/overlay/resources/settings.png");
					ICON_LINE = ImageTools.loadEmbeddedImage("/jShot/ui/overlay/resources/line.png");
					ICON_UNDO = ImageTools.loadEmbeddedImage("/jShot/ui/overlay/resources/undo.png");
				} catch (Exception e) {
					System.err.println("Couldn't load embedded images!");
				}
			}
		}.start();

		new Thread() {
			@Override
			public void run() {
				try {
					ICON_MARKER = ImageTools.loadEmbeddedImage("/jShot/ui/overlay/resources/marker.png");
					ICON_RECTANGLE = ImageTools.loadEmbeddedImage("/jShot/ui/overlay/resources/rectangle.png");
					ICON_SAVE = ImageTools.loadEmbeddedImage("/jShot/ui/overlay/resources/save.png");
				} catch (Exception e) {
					System.err.println("Couldn't load embedded images!");
				}
			}
		}.start();
	}

	public static BufferedImage getIcon(List iconEnum) {
		switch (iconEnum) {
		case ARROW:
			return ICON_ARROW;
		case CLOSE:
			return ICON_CLOSE;
		case COPY:
			return ICON_COPY;
		case DRAW:
			return ICON_DRAW;
		case SETTINGS:
			return ICON_SETTINGS;
		case LINE:
			return ICON_LINE;
		case MARKER:
			return ICON_MARKER;
		case RECTANGLE:
			return ICON_RECTANGLE;
		case SAVE:
			return ICON_SAVE;
		case TEXT:
			return ICON_TEXT;
		case UNDO:
			return ICON_UNDO;
		default:
			return null;
		}
	}
}
