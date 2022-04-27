package jShot.ui.overlay;

import java.awt.image.BufferedImage;
import java.util.Stack;

import jUtils.tools.ImageTools;

public class UndoHistory {

	private Stack<BufferedImage> stack;
	private BufferedImage lastImage;
	private BufferedImage originalImage;
	private boolean lastPushed;

	public UndoHistory(BufferedImage originalImage) {
		stack = new Stack<BufferedImage>();
		this.originalImage = ImageTools.cloneBufferedImage(originalImage);
		lastPushed = false;
	}

	public void push(BufferedImage img) {
		if (lastImage != null) {
			stack.push(ImageTools.cloneBufferedImage(lastImage));
		}
		
		lastImage = ImageTools.cloneBufferedImage(img);
		lastPushed = true;
	}

	public BufferedImage undo() {
		if (!lastPushed) {
			lastImage = null;
			lastPushed = false;
		}
		
		if (stack.size() == 0) {
			lastImage = null;
			return ImageTools.cloneBufferedImage(originalImage);
		}
		
		lastImage = stack.pop();
		return ImageTools.cloneBufferedImage(lastImage);
	}
}
