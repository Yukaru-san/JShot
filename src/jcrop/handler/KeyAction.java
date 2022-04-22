package jcrop.handler;

import java.awt.event.ActionEvent;
import java.util.concurrent.Callable;

import javax.swing.AbstractAction;

/*
 * Represents a single Key Stroke and the corresponding event.
 * Action is defined by a unique String
 * 
 */
public class KeyAction extends AbstractAction {
	private static final long serialVersionUID = 7177457064379993566L;
	
	private Callable<Object> targetFunc;
	
	public KeyAction(String actionCommand, Callable<Object> targetFunc) {
		this.targetFunc = targetFunc;
		putValue(ACTION_COMMAND_KEY, actionCommand);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		try {
			targetFunc.call();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}