package ui.overlay;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Cursor;
import java.util.concurrent.Callable;

import jcrop.crop.CropTarget;
import jcrop.crop.Painter;
import jcrop.events.StateEvent;
import jcrop.events.StateEventFunc;
import ui.Icons;

public class OverlayPrefab {

	// Sets the cursor's style (use Cusor.[const]) and overwrites default until called again with active:false (-> HAPPENS AUTOMATICALLY ON EACH CUSTOM EVENT CALL!)
	public static void setCursorStyle(Painter painter, int cursorStyle, boolean active) {
		CropTarget target = painter.getTarget();
		
		target.cursorStyle = active ? cursorStyle : Cursor.DEFAULT_CURSOR;
		target.setUseCustomCursor(active);
	}
	
	// Function to draw a utility Icon
	public static Rectangle drawUtilityIcon(Graphics2D g2d, Painter painter, boolean isHorizontal, int index, Icons.List iconEnum, boolean isActive) {
		CropTarget t = painter.getTarget();

		int x = t.x + t.width - 75 + (isHorizontal ? index * 25 : 85);
		int y = t.y + t.height + 10 + (!isHorizontal ? index * 24 - 205 : 0);

		if (isActive) {
			g2d.setStroke(new BasicStroke(2, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND, 1.7f));
			g2d.setColor(Color.GRAY);
			g2d.drawRect(x,y,25,25);
		}
		
		painter.drawImageAt(g2d, Icons.getIcon(iconEnum), x+1, y+1);
		
		return new Rectangle(x, y, 24, 24);
	}
	
	// Generates a default Utility Btn at the given position (index). Saves code repetition.
	public static StateEvent generateUtilityButton(boolean isHorizontal, int index, Icons.List iconEnum, boolean isActive,
			Callable<Rectangle> onClick) {
		return new StateEvent(new StateEventFunc<Graphics2D, Painter, Rectangle>() {
			@Override
			public Rectangle onState(Graphics2D g2d, Painter painter) {
				return drawUtilityIcon(g2d, painter, isHorizontal, index, iconEnum, isActive);
			}

			@Override
			public Rectangle onClick(Painter painter) {
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
