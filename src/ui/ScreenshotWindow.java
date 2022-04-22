package ui;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.Transferable;
import java.awt.image.BufferedImage;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.concurrent.Callable;

import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.UIManager;
import javax.swing.filechooser.FileNameExtensionFilter;

import jcrop.crop.CroppingPanel;
import toolset.AppPrefs;
import toolset.TransferableImage;

public class ScreenshotWindow extends JFrame {

	// Some serial number to not get warnings
	private static final long serialVersionUID = -7077528694515804789L;

	// Reference
	private CroppingPanel croppingPanel;
	private JFileChooser fileChooser;
	private SettingsWindow settingsWindow;
	private Dimension windowSize;
	
	// Constructor setting up the frame itself
	public ScreenshotWindow(Dimension windowSize, CroppingPanel p) {
		// Set reference
		croppingPanel = p;
		this.windowSize = windowSize;
		
		// Set window size and position
		setBounds(0, -5, windowSize.width, windowSize.height + 5);
		setUndecorated(true);
		setAlwaysOnTop(true);
		setLayout(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setContentPane(p);
		repaint();
		setVisible(true);
	}

	public CroppingPanel getCroppingPanel() {
		return croppingPanel;
	}
	
	// Copies the currently selected Crop of the Screen's Image
	public void copyCroppedImage() {
		TransferableImage trans = new TransferableImage(croppingPanel.getCroppedImage());
		Clipboard c = Toolkit.getDefaultToolkit().getSystemClipboard();
		c.setContents(trans, new ClipboardOwner() {
			@Override
			public void lostOwnership(Clipboard clipboard, Transferable contents) {
			}
		});
		System.exit(0);
	}

	// Saves the currently selected Crop of the Screen'n Image
	public void saveCroppedImage() {
		setAlwaysOnTop(false);

		try {
			BufferedImage bi = croppingPanel.getCroppedImage();

			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

			fileChooser = new JFileChooser();
			fileChooser.setAcceptAllFileFilterUsed(false);

			String location = AppPrefs.FileLocation.get(System.getProperty("user.home"));
			fileChooser.setCurrentDirectory(new File(location));

			FileNameExtensionFilter filter = new FileNameExtensionFilter("Portable Network Graphics (png)", "png");
			fileChooser.addChoosableFileFilter(filter);

			int userSelection = fileChooser.showSaveDialog(this);

			if (userSelection == JFileChooser.APPROVE_OPTION) {
				File outputFile = fileChooser.getSelectedFile();
				AppPrefs.FileLocation.put(outputFile.getParentFile().getAbsolutePath());
				ImageIO.write(bi, "png", outputFile);
			}
		} catch (Exception e) {
			e.printStackTrace();

		}
		System.exit(0);
	}

	// Shows the settings window
	public void showSettings() {
		settingsWindow = new SettingsWindow(windowSize, this);
		setAlwaysOnTop(false);
		setVisible(false);
	}
	
	// Hides the settings window
	public void hideSettings() {
		setAlwaysOnTop(true);
		setVisible(true);
		settingsWindow.close();
	}
}