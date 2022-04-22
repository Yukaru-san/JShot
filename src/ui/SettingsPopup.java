package ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GraphicsEnvironment;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;

import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.border.EmptyBorder;
import javax.swing.UIManager;
import javax.swing.JSpinner;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.border.LineBorder;
import javax.swing.text.JTextComponent;

public class SettingsPopup extends JDialog {
	
	// ID to make compiler happy
	private static final long serialVersionUID = 2412929449516957819L;
	
	// Variables
	private String[] fonts;
	private String lastFont;
	
	// Constructor
	public SettingsPopup(Dimension windowSize, ScreenshotWindow screenshotWindow) {
		
		// Get Fonts
		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		fonts = ge.getAvailableFontFamilyNames();
		lastFont = "Arial"; // TODO Settings

		// Set window size and position
		setBounds(windowSize.width / 2 - 250, windowSize.height / 2 - 175, 500, 350);
		setAlwaysOnTop(true);
		
		// Go back to default on close
		addWindowListener(new WindowAdapter() 
		{
		  public void windowClosing(WindowEvent e)
		  {
			  screenshotWindow.hideSettings();
		  }
		  
		  public void windowLostFocus(WindowEvent e) {
			  System.out.println("LOST WINDOW");
			  screenshotWindow.hideSettings();
		  }
		});
		
		// Create frame
		initialize();		
	}

	// Makes the font box not auto-focus and adds auto-completion
	private void applyAdvancedFontSettings(JComboBox<String> box) {
		// Get editor component
		JTextComponent editor = (JTextComponent) box.getEditor().getEditorComponent();

		// Prevent auto focus
		editor.setCaretPosition(editor.getText().length());

		// Apply autocomplete
		editor.addFocusListener(new FocusAdapter() {
			public void focusLost(FocusEvent e) {
				System.out.println("Focus lost");
				box.setPopupVisible(false);
				lastFont = autocompleteText(editor.getText());
				editor.setText(lastFont);
			}
		});
	}
	
	// Autocompletes the given text based on the font list
	private String autocompleteText(String text) {
		for (int i = 0; i < fonts.length; i++) {
			if (fonts[i].toLowerCase().startsWith(text.toLowerCase())) {
				return fonts[i];
			}
		}
		
		return lastFont;
	}

	// The boring part: Add the components.
	private void initialize() {

		// Set look & feel
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
		}

		// Add font components
		JPanel panel = new JPanel();
		panel.setBorder(new EmptyBorder(10, 10, 10, 10));
		getContentPane().add(panel, BorderLayout.CENTER);
		panel.setLayout(new GridLayout(0, 2, 0, 0));

		JLabel font_lbl = new JLabel("Font");
		panel.add(font_lbl);

		JPanel font_pnl = new JPanel();
		font_pnl.setBorder(new EmptyBorder(10, 10, 10, 10));
		panel.add(font_pnl);
		font_pnl.setLayout(new GridLayout(0, 1, 0, 0));
		panel.add(font_pnl);

		JComboBox<String> box = new JComboBox<String>(fonts);
		box.getModel().setSelectedItem(lastFont);
		box.setEditable(true);
		font_pnl.add(box);

		// Apply autocomplete, focus handling... on fonts
		applyAdvancedFontSettings(box);

		JLabel fsize_lbl = new JLabel("Font size");
		panel.add(fsize_lbl);

		JPanel fsize_pnl = new JPanel();
		fsize_pnl.setBorder(new EmptyBorder(10, 10, 10, 10));
		panel.add(fsize_pnl);
		fsize_pnl.setLayout(new GridLayout(0, 1, 0, 0));

		JSpinner fsize_input = new JSpinner();
		fsize_input.setValue(16);
		fsize_pnl.add(fsize_input);

		// Add stroke size components
		JLabel ssize_lbl = new JLabel("Stroke size");
		panel.add(ssize_lbl);

		JPanel ssize_pnl = new JPanel();
		ssize_pnl.setBorder(new EmptyBorder(10, 10, 10, 10));
		panel.add(ssize_pnl);
		ssize_pnl.setLayout(new GridLayout(0, 1, 0, 0));

		JSpinner ssize_input = new JSpinner();
		ssize_input.setValue(3);
		ssize_pnl.add(ssize_input);

		// Add highlighted stroke size components
		JLabel h_ssize_lbl = new JLabel("Highlighted stroke size");
		panel.add(h_ssize_lbl);

		JPanel h_ssize_pnl = new JPanel();
		h_ssize_pnl.setBorder(new EmptyBorder(10, 10, 10, 10));
		panel.add(h_ssize_pnl);
		h_ssize_pnl.setLayout(new GridLayout(0, 1, 0, 0));

		JSpinner h_ssize_input = new JSpinner();
		h_ssize_input.setValue(12);
		h_ssize_pnl.add(h_ssize_input);

		// Add color components
		JLabel color_lbl = new JLabel("Color");
		panel.add(color_lbl);

		JPanel color_pnl = new JPanel();
		color_pnl.setBorder(new EmptyBorder(10, 10, 10, 10));
		panel.add(color_pnl);
		color_pnl.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));

		JPanel color_preview = new JPanel();
		color_preview.setBorder(new LineBorder(new Color(0, 0, 0)));
		color_preview.setPreferredSize(new Dimension(20, 20));
		color_preview.setBackground(Color.RED);
		color_pnl.add(color_preview);

		JButton color_input = new JButton("Choose Color");
		color_pnl.add(color_input);

		// Add highlighted color components
		JLabel h_color_lbl = new JLabel("Highlight color");
		panel.add(h_color_lbl);

		JPanel h_color_pnl = new JPanel();
		h_color_pnl.setBorder(new EmptyBorder(10, 10, 10, 10));
		panel.add(h_color_pnl);
		h_color_pnl.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));

		JPanel h_color_preview = new JPanel();
		h_color_preview.setBorder(new LineBorder(new Color(0, 0, 0)));
		h_color_preview.setPreferredSize(new Dimension(20, 20));
		h_color_preview.setBackground(new Color(255, 255, 0, 50));
		h_color_pnl.add(h_color_preview);

		JButton h_color_input = new JButton("Choose Color");
		h_color_pnl.add(h_color_input);
		
		setVisible(true);
	}
}
