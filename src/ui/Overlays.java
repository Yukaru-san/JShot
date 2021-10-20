package ui;

import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Color;
import java.awt.Rectangle;

import ui.crop.CropTarget;
import ui.crop.Painter;
import ui.states.StateEvent;
import ui.states.StateEventFunc;

public class Overlays {

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
		}
	);
	
	public static final StateEvent DRAW_UTILITY_OVERLAY_HORIZONTAL = new StateEvent(
			new StateEventFunc<Graphics2D, Painter, Rectangle>() {
				@Override
				public Rectangle onState(Graphics2D g2d, Painter painter) {
					CropTarget t = painter.getTarget();
										
					int x = t.x + t.width - 200;
					int y = t.y + t.height + 10;
					int w = 200;
					int h = 25;

					g2d.fillRoundRect(x, y, w, h, 2, 2);
					
					return new Rectangle(x, y, w, h);
				}
				
				@Override
				public Rectangle onClick() {
					// TODO
					System.out.println("Clicked on DRAW_UTILITY_OVERLAY_HORIZONTAL");
					return null;
				}
			}
		);
	
	public static final StateEvent DRAW_UTILITY_OVERLAY_VERTICAL = new StateEvent(
			new StateEventFunc<Graphics2D, Painter, Rectangle>() {
				@Override
				public Rectangle onState(Graphics2D g2d, Painter painter) {
					CropTarget t = painter.getTarget();
										
					int x = t.x + t.width + 10;
					int y = t.y + t.height - 200;
					int w = 25;
					int h = 200;

					g2d.fillRoundRect(x, y, w, h, 2, 2);
					
					return new Rectangle(x, y, w, h);
				}
				
				@Override
				public Rectangle onClick() {
					// TODO
					System.out.println("Clicked on DRAW_UTILITY_OVERLAY_VERTICAL");
					return null;
				}
			}
		);
}
