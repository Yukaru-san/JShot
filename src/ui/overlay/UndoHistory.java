package ui.overlay;

import java.awt.image.BufferedImage;
import java.util.Stack;

import toolset.Collection;

public class UndoHistory {
	
	private Stack<BufferedImage> stack;
	private BufferedImage originalImage;
	private int pointer;
	private boolean firstUndo;

	public UndoHistory(BufferedImage originalImage) {
		pointer = -1;
		stack = new Stack<BufferedImage>();
		firstUndo = true;
		this.originalImage = Collection.cloneBufferedImage(originalImage);
	}
	
	public void push(BufferedImage img) {
		deleteElementsAfterPointer(pointer);
		stack.push(Collection.cloneBufferedImage(img));
		pointer++;
		firstUndo = true;
	}

	public BufferedImage undo() {
		if (pointer == -1 || firstUndo && pointer == 0) {
			pointer = -1;
			return Collection.cloneBufferedImage(originalImage);
		}
		
		if (firstUndo) { 
			pointer--;
			firstUndo = false;
		}
		
		BufferedImage img = stack.get(pointer);
		pointer--;
		return img;
	}

	public BufferedImage redo() {
		if (pointer == stack.size() - 1)
			return null;
		
		pointer++;
		BufferedImage img = stack.get(pointer);
		return img;
	}
	
	private void deleteElementsAfterPointer(int pointer) {
		if (stack.size() < 1)
			return;
		for (int i = stack.size() - 1; i > pointer; i--) {
			stack.remove(i);
		}
	}
}
