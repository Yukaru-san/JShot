package ui.overlay;

import java.awt.image.BufferedImage;
import java.util.Stack;

import toolset.Collection;

public class UndoHistory {

	private Stack<BufferedImage> stack;
	private BufferedImage lastImage;
	private BufferedImage originalImage;
	private boolean lastPushed;

	public UndoHistory(BufferedImage originalImage) {
		stack = new Stack<BufferedImage>();
		this.originalImage = Collection.cloneBufferedImage(originalImage);
		lastPushed = false;
	}

	public void push(BufferedImage img) {
		if (lastImage != null) {
			stack.push(Collection.cloneBufferedImage(lastImage));
		}
		
		lastImage = Collection.cloneBufferedImage(img);
		lastPushed = true;
	}

	public BufferedImage undo() {
		if (!lastPushed) {
			lastImage = null;
			lastPushed = false;
		}
		
		if (stack.size() == 0) {
			lastImage = null;
			return Collection.cloneBufferedImage(originalImage);
		}
		
		lastImage = stack.pop();
		return Collection.cloneBufferedImage(lastImage);
	}
}
