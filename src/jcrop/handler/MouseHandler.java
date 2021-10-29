package jcrop.handler;

import java.awt.Cursor;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.HashMap;

import jcrop.crop.CropTarget;
import jcrop.crop.DragPoint;
import jcrop.events.StateEvent;
import jcrop.states.*;
import toolset.Collection;

public class MouseHandler extends MouseAdapter {

	// Given object that handles the cropped dimensions
	private CropTarget target;
	private HashMap<CroppingState, ArrayList<StateEvent>> stateEvents;
	
	// Used Variables
	private Rectangle startPosition;
	private Point startRepositionPoint;
	private DragPoint startRescalePoint;
	private DragPoint[] hoveredDragPoints;
	private boolean ignorePressEvent;

	public MouseHandler(CropTarget t, HashMap<CroppingState, ArrayList<StateEvent>> stateEvents) {
		this.stateEvents = stateEvents;
		target = t;
		target.currentState = CroppingState.CROPPING_START;
		startPosition = new Rectangle(0, 0, 0, 0);
		startRepositionPoint = new Point(0, 0);
		ignorePressEvent = false;
	}

	@Override
	public void mousePressed(MouseEvent e) {		
		target.mousePressed = true;
		
		// Check for onClick events
		ArrayList<StateEvent> stateList = stateEvents.get(target.currentState);
		if (stateList != null) {
			stateList.forEach(stateEvent -> {
				if (stateEvent.getBounds() != null) {
					Rectangle bounds = stateEvent.getBounds();
					
					if (target.mousePosition.x >= bounds.x && target.mousePosition.x <= bounds.x + bounds.width &&
							target.mousePosition.y >= bounds.y && target.mousePosition.y <= bounds.y + bounds.height) {
						stateEvent.setBounds(stateEvent.eventFunction.onClick());
						ignorePressEvent = true;
						return;
					}
				}
			});
		}
		 
		// If at least one event was called, don't execute the standard behaviour
		if (ignorePressEvent) {
			ignorePressEvent = false;
			return;
		}

 		// Do Cropping-Tool related OnClickEvents
		switch (target.currentState) {
		case CROPPING_START:
			// Set the starting point for the initial Rectangle
			updateStartPoint(e.getPoint());
			target.currentState = CroppingState.CROPPING_INIT;
			break;
		case CROPPING_EDIT:
			// Reposition if clicked inside or start anew otherwise
			if (isMouseOutOfBounds(e.getPoint())) {
				resetTargetRect(e.getPoint());
				target.currentState = CroppingState.CROPPING_START;
			} else {
				DragPoint hoveredDragPoint = getHoveredDragPoint(e.getPoint());

				// Click inside the field: Reposition!
				if (hoveredDragPoint == null) {
					startRepositionPoint = e.getPoint();
					target.currentState = CroppingState.CROPPING_REPOSITION;
				}
				// Click on a Drag Point: Resize the field
				else {
					startRescalePoint = hoveredDragPoint;
					target.currentState = CroppingState.CROPPING_RESCALE;
				}
			}
			break;
		default:
			// Do nothing
		}
	}
	
	@Override
	public void mouseMoved(MouseEvent e) {
		hoveredDragPoints = target.calcDragPoints();
		target.mousePosition = e.getPoint();
		
		if (target.currentState == CroppingState.CUSTOM)
			return;
		
		DragPoint hoveredDragPoint = getHoveredDragPoint(target.mousePosition);

		if (hoveredDragPoint == null) {
			if (isMouseOutOfBounds(e.getPoint())) {
				target.setCursorStyle(Cursor.DEFAULT_CURSOR);
			} else {
				target.setCursorStyle(Cursor.MOVE_CURSOR);
			}
		} else
			setCursorStyle(hoveredDragPoint.direction);
	}

	@Override
	public void mouseReleased(MouseEvent e) {	
		target.mousePressed = false;
		
		if (target.currentState == CroppingState.CUSTOM) return;
		
		if (target.width > 0 || target.height > 0) {
			target.currentState = CroppingState.CROPPING_EDIT;
			startPosition = new Rectangle(target.x, target.y, target.width, target.height);
		} else {
			target.currentState = CroppingState.CROPPING_START;
		}
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		switch (target.currentState) {
		case CROPPING_START:
			target.currentState = CroppingState.CROPPING_INIT;
		case CROPPING_INIT:
			updateTargetRect(e.getPoint());
			break;
		case CROPPING_REPOSITION:
			moveTargetRect(e.getPoint());
			break;
		case CROPPING_RESCALE:
			rescaleTargetRect(e.getPoint());
			break;
		case CUSTOM:
			target.mousePosition = e.getPoint();
		default:
			// Do nothing
		}
	}

	// Sets the position where the rectangle starts
	private void updateStartPoint(Point targetPosition) {
		startPosition = new Rectangle(targetPosition);
		target.setX(targetPosition.x);
		target.setY(targetPosition.y);
		target.setCursorStyle(Cursor.DEFAULT_CURSOR);
	}

	// Resets the target rectangle to have 0 width and height and start at the given
	// pos
	private void resetTargetRect(Point targetPosition) {
		startPosition = new Rectangle(targetPosition);
		updateTargetRect(targetPosition);
	}

	// Updates and creates the initial target rectangle
	private void updateTargetRect(Point currentDragPoint) {
		target.setX(Math.min(startPosition.x, currentDragPoint.x));
		target.setY(Math.min(startPosition.y, currentDragPoint.y));
		target.setWidth(Math.abs(startPosition.x - currentDragPoint.x));
		target.setHeight(Math.abs(startPosition.y - currentDragPoint.y));

		startPosition.width = target.width;
		startPosition.height = target.height;
	}

	// Moves the already-made Rectangle linear to the given pos
	private void moveTargetRect(Point targetPosition) {
		target.setX(startPosition.x + (targetPosition.x - startRepositionPoint.x));
		target.setY(startPosition.y + (targetPosition.y - startRepositionPoint.y));
	}

	// Returns true if the given Point is outside the selected rectangle
	private boolean isMouseOutOfBounds(Point mousePos) {
		return !(Collection.isPointInBounds(mousePos, new Rectangle(target.x, target.y, target.width, target.height)))
		&& getHoveredDragPoint(mousePos) == null;
	}

	// Returns the enum of the currently hit DragPoint
	private DragPoint getHoveredDragPoint(Point mousePos) {
		DragPoint[] dragPoints = hoveredDragPoints;

		// Iterate all drag points
		for (int i = 0; i < dragPoints.length; i++) {
			if (dragPoints[i].position.x > mousePos.x - 15 && dragPoints[i].position.x < mousePos.x + 15
					&& dragPoints[i].position.y > mousePos.y - 15 && dragPoints[i].position.y < mousePos.y + 15) {
				return dragPoints[i];
			}
		}

		// No hit
		return null;
	}

	// Rescales the Rectangle at one of it's 8 Drag Points
	public void rescaleTargetRect(Point targetPosition) {
		switch (startRescalePoint.direction) {
		case NORTH:
			target.scaleNorth(startPosition, targetPosition);
			break;
		case EAST:
			target.scaleEast(startPosition, targetPosition);
			break;
		case SOUTH:
			target.scaleSouth(startPosition, targetPosition);
			break;
		case WEST:
			target.scaleWest(startPosition, targetPosition);
			break;
		case NORTH_EAST:
			target.scaleNorth(startPosition, targetPosition);
			target.scaleEast(startPosition, targetPosition);
			break;
		case NORTH_WEST:
			target.scaleNorth(startPosition, targetPosition);
			target.scaleWest(startPosition, targetPosition);
			break;
		case SOUTH_EAST:
			target.scaleSouth(startPosition, targetPosition);
			target.scaleEast(startPosition, targetPosition);
			break;
		case SOUTH_WEST:
			target.scaleSouth(startPosition, targetPosition);
			target.scaleWest(startPosition, targetPosition);
			break;
		}
	}

	// Sets the Cursor Style fitting to the given Direction Type
	private void setCursorStyle(DragDirection type) {
		switch (type) {
		case NORTH:
			target.setCursorStyle(Cursor.N_RESIZE_CURSOR);
			break;
		case EAST:
			target.setCursorStyle(Cursor.E_RESIZE_CURSOR);
			break;
		case SOUTH:
			target.setCursorStyle(Cursor.S_RESIZE_CURSOR);
			break;
		case WEST:
			target.setCursorStyle(Cursor.W_RESIZE_CURSOR);
			break;
		case NORTH_EAST:
			target.setCursorStyle(Cursor.NE_RESIZE_CURSOR);
			break;
		case NORTH_WEST:
			target.setCursorStyle(Cursor.NW_RESIZE_CURSOR);
			break;
		case SOUTH_EAST:
			target.setCursorStyle(Cursor.SE_RESIZE_CURSOR);
			break;
		case SOUTH_WEST:
			target.setCursorStyle(Cursor.SW_RESIZE_CURSOR);
			break;
		}
	}
}
