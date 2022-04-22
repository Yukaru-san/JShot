package ui;

import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

import toolset.Collection;

public class Icons {

	public enum List {
		ARROW, CLOSE, COPY, DRAW,  LINE,
		MARKER, RECTANGLE, TEXT, SETTINGS, SAVE, UNDO
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
				ICON_ARROW = Collection.loadEmbeddedImage("resources/arrow.png");
				ICON_CLOSE = Collection.loadEmbeddedImage("resources/close.png");
				ICON_COPY = Collection.loadEmbeddedImage("resources/copy.png");
				ICON_TEXT = Collection.loadEmbeddedImage("resources/text.png");
			}
		}.start();
		
		new Thread() {
			@Override
			public void run() {
				ICON_DRAW = Collection.loadEmbeddedImage("resources/draw.png");
				ICON_SETTINGS = Collection.loadEmbeddedImage("resources/settings.png");
				ICON_LINE = Collection.loadEmbeddedImage("resources/line.png");
				ICON_UNDO = Collection.loadEmbeddedImage("resources/undo.png");
			}
		}.start();
		
		new Thread() {
			@Override
			public void run() {
				ICON_MARKER = Collection.loadEmbeddedImage("resources/marker.png");
				ICON_RECTANGLE = Collection.loadEmbeddedImage("resources/rectangle.png");
				ICON_SAVE = Collection.loadEmbeddedImage("resources/save.png");
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
