package ui.overlay;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.concurrent.Callable;

import jcrop.crop.CropTarget;
import jcrop.crop.Painter;
import jcrop.events.StateEvent;
import jcrop.events.StateEventFunc;
import ui.Icons;

public class OverlayPrefab {

	// Function to draw a utility Icon
	public static Rectangle drawUtilityIcon(Graphics2D g2d, Painter painter, boolean isHorizontal, int index, Icons.List iconEnum) {
		CropTarget t = painter.getTarget();

		int x = t.x + t.width - 75 + (isHorizontal ? index * 25 : 85);
		int y = t.y + t.height + 10 + (!isHorizontal ? index * 24 - 205 : 0);

		painter.drawImageAt(g2d, Icons.getIcon(iconEnum), x, y);

		return new Rectangle(x, y, 24, 24);
	}
	
	// Generates a default Utility Btn at the given position (index). Saves code repetition.
	public static StateEvent generateUtilityButton(boolean isHorizontal, int index, Icons.List iconEnum,
			Callable<Rectangle> onClick) {
		return new StateEvent(new StateEventFunc<Graphics2D, Painter, Rectangle>() {
			@Override
			public Rectangle onState(Graphics2D g2d, Painter painter) {
				return drawUtilityIcon(g2d, painter, isHorizontal, index, iconEnum);
			}

			@Override
			public Rectangle onClick() {
				try {
					return onClick.call();
				} catch (Exception e) {
					e.printStackTrace();
				}
				return null;
			}
		});
	}

}
