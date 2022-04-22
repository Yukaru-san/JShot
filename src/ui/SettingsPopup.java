package ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GraphicsEnvironment;
import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.border.EmptyBorder;
import javax.swing.UIManager;
import javax.swing.JSpinner;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.border.LineBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;
import javax.swing.text.JTextComponent;

import toolset.Constants;
import toolset.Constants.ColorOptions;
import toolset.PainterSettings;
import toolset.ScreenTools;

public class SettingsPopup extends JDialog {

	// ID to make compiler happy
	private static final long serialVersionUID = 2412929449516957819L;

	// Outside reference
	ScreenshotWindow screenshotWindow;

	// Panel references
	private JPanel settingsPanel;
	private JPanel colorPanel;
	private JPanel color_preview;
	private JPanel h_color_preview;
	private JColorChooser jcc;

	// Variables
	private Dimension windowSize;
	private String[] fonts;
	private String lastFont;
	private ColorOptions targetColorOption;

	// Constructor
	public SettingsPopup(ScreenshotWindow screenshotWindow) {
		// Save info
		this.screenshotWindow = screenshotWindow;
		this.windowSize = ScreenTools.getScreenDimensions(0);
		targetColorOption = ColorOptions.DEFAULT;

		// Get Fonts
		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		fonts = ge.getAvailableFontFamilyNames();
		lastFont = PainterSettings.data.fontName;

		// Set window size and position
		setBounds((windowSize.width / 2) - (Constants.SETTINGS_WINDOW_WIDTH / 2),
				(windowSize.height / 2) - (Constants.SETTINGS_WINDOW_HEIGHT / 2), Constants.SETTINGS_WINDOW_WIDTH,
				Constants.SETTINGS_WINDOW_HEIGHT);
		setAlwaysOnTop(true);
		setResizable(false);
		setTitle("Settings");

		// Go back to default on close
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				screenshotWindow.hideSettings();
			}

			public void windowLostFocus(WindowEvent e) {
				screenshotWindow.hideSettings();
			}
		});

		// Create frame
		initializeSettingsPanel();
		initializeColorPanel();
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
				box.setPopupVisible(false);
				lastFont = autocompleteText(editor.getText());
				editor.setText(lastFont);

				PainterSettings.data.fontName = lastFont;
				PainterSettings.font = new Font(PainterSettings.data.fontName, Font.BOLD,
						PainterSettings.data.fontSize);
				PainterSettings.saveSettings();
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

	// Opens the color panel
	private void openColorSelector(ColorOptions targetColor) {
		// Change panes
		getContentPane().remove(settingsPanel);
		getContentPane().add(colorPanel, BorderLayout.CENTER);
		setBounds(windowSize.width / 2 - Constants.SETTINGS_COLORWINDOW_WIDTH / 2,
				windowSize.height / 2 - Constants.SETTINGS_COLORWINDOW_HEIGHT / 2, Constants.SETTINGS_COLORWINDOW_WIDTH,
				Constants.SETTINGS_COLORWINDOW_HEIGHT);

		// Update target color
		targetColorOption = targetColor;
		if (targetColorOption == ColorOptions.DEFAULT) {
			jcc.setColor(PainterSettings.data.color);
		} else {
			jcc.setColor(PainterSettings.data.h_color);
		}

		// Update
		revalidate();
		repaint();
	}

	// Closes the color panel
	private void closeColorSelector() {
		getContentPane().remove(colorPanel);
		getContentPane().add(settingsPanel, BorderLayout.CENTER);
		setBounds(windowSize.width / 2 - Constants.SETTINGS_WINDOW_WIDTH / 2,
				windowSize.height / 2 - Constants.SETTINGS_WINDOW_HEIGHT / 2, Constants.SETTINGS_WINDOW_WIDTH,
				Constants.SETTINGS_WINDOW_HEIGHT);
		revalidate();
		repaint();
	}

	// The boring part: Add the components.
	private void initializeSettingsPanel() {

		// Set look & feel
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			e.printStackTrace();
		}

		// Create the pannel
		settingsPanel = new JPanel();
		settingsPanel.setBorder(new EmptyBorder(0, 10, 10, 10));
		getContentPane().add(settingsPanel, BorderLayout.CENTER);
		settingsPanel.setLayout(new BoxLayout(settingsPanel, BoxLayout.Y_AXIS));

		// Create sub panel
		JPanel upperPanel = new JPanel();
		upperPanel.setLayout(new GridLayout(0, 2, 0, 0));
		settingsPanel.add(upperPanel);

		// Add font components
		JLabel font_lbl = new JLabel("Font");
		upperPanel.add(font_lbl);

		JPanel font_pnl = new JPanel();
		font_pnl.setBorder(new EmptyBorder(10, 10, 10, 10));
		upperPanel.add(font_pnl);
		font_pnl.setLayout(new GridLayout(0, 1, 0, 0));
		upperPanel.add(font_pnl);

		JComboBox<String> box = new JComboBox<String>(fonts);
		box.getModel().setSelectedItem(lastFont);
		box.setEditable(true);
		font_pnl.add(box);

		// Apply autocomplete, focus handling... on fonts
		applyAdvancedFontSettings(box);

		JLabel fsize_lbl = new JLabel("Font size");
		upperPanel.add(fsize_lbl);

		JPanel fsize_pnl = new JPanel();
		fsize_pnl.setBorder(new EmptyBorder(10, 10, 10, 10));
		upperPanel.add(fsize_pnl);
		fsize_pnl.setLayout(new GridLayout(0, 1, 0, 0));

		JSpinner fsize_input = new JSpinner();
		fsize_input.setValue(PainterSettings.data.fontSize);
		fsize_input.addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent e) {
				PainterSettings.data.fontSize = (int) fsize_input.getValue();
				PainterSettings.saveSettings();
			}

		});
		fsize_pnl.add(fsize_input);

		// Add stroke size components
		JLabel ssize_lbl = new JLabel("Stroke size");
		upperPanel.add(ssize_lbl);

		JPanel ssize_pnl = new JPanel();
		ssize_pnl.setBorder(new EmptyBorder(10, 10, 10, 10));
		upperPanel.add(ssize_pnl);
		ssize_pnl.setLayout(new GridLayout(0, 1, 0, 0));

		JSpinner ssize_input = new JSpinner();
		ssize_input.setValue(PainterSettings.data.strokeWidth);
		ssize_input.addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent e) {
				PainterSettings.data.strokeWidth = (int) ssize_input.getValue();
				PainterSettings.stroke = new BasicStroke(PainterSettings.data.strokeWidth, BasicStroke.CAP_ROUND,
						BasicStroke.JOIN_ROUND, 1.7f);
				PainterSettings.saveSettings();
			}

		});
		ssize_pnl.add(ssize_input);

		// Add highlighted stroke size components
		JLabel h_ssize_lbl = new JLabel("Highlighted stroke size");
		upperPanel.add(h_ssize_lbl);

		JPanel h_ssize_pnl = new JPanel();
		h_ssize_pnl.setBorder(new EmptyBorder(10, 10, 10, 10));
		upperPanel.add(h_ssize_pnl);
		h_ssize_pnl.setLayout(new GridLayout(0, 1, 0, 0));

		JSpinner h_ssize_input = new JSpinner();
		h_ssize_input.setValue(PainterSettings.data.h_strokeWidth);
		h_ssize_input.addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent e) {
				PainterSettings.data.h_strokeWidth = (int) h_ssize_input.getValue();
				PainterSettings.h_stroke = new BasicStroke(PainterSettings.data.h_strokeWidth, BasicStroke.CAP_ROUND,
						BasicStroke.JOIN_ROUND, 1.7f);
				PainterSettings.saveSettings();
			}

		});
		h_ssize_pnl.add(h_ssize_input);

		// Add color components
		JLabel color_lbl = new JLabel("Color");
		upperPanel.add(color_lbl);

		JPanel color_in_pnl = new JPanel();
		color_in_pnl.setBorder(new EmptyBorder(10, 10, 10, 10));
		upperPanel.add(color_in_pnl);
		color_in_pnl.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));

		color_preview = new JPanel();
		color_preview.setBorder(new LineBorder(new Color(0, 0, 0)));
		color_preview.setPreferredSize(new Dimension(20, 20));
		color_preview.setBackground(PainterSettings.data.color);
		color_in_pnl.add(color_preview);

		JButton color_input = new JButton("Choose Color");
		color_input.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				openColorSelector(ColorOptions.DEFAULT);
			}

		});
		color_in_pnl.add(color_input);

		// Add highlighted color components
		JLabel h_color_lbl = new JLabel("Highlight color");
		upperPanel.add(h_color_lbl);

		JPanel h_color_in_pnl = new JPanel();
		h_color_in_pnl.setBorder(new EmptyBorder(10, 10, 10, 10));
		upperPanel.add(h_color_in_pnl);
		h_color_in_pnl.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));

		h_color_preview = new JPanel();
		h_color_preview.setBorder(new LineBorder(new Color(0, 0, 0)));
		h_color_preview.setPreferredSize(new Dimension(20, 20));
		h_color_preview.setBackground(PainterSettings.data.h_color);
		h_color_in_pnl.add(h_color_preview);

		JButton h_color_input = new JButton("Choose Color");
		h_color_input.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				openColorSelector(ColorOptions.HIGHLIGHTED);
			}

		});
		h_color_in_pnl.add(h_color_input);

		// create the lower part's panel
		JPanel lowerPanel = new JPanel();
		lowerPanel.setLayout(new GridLayout(0, 2, 10, 0));
		lowerPanel.setBorder(new EmptyBorder(0, 100, 0, 100));
		lowerPanel.setPreferredSize(new Dimension(0, 50));
		settingsPanel.add(lowerPanel);

		// Reset btn - resets all default values and applies them
		JButton reset_btn = new JButton("reset");
		reset_btn.setFont(Constants.SETTINGS_BTN_FONT);
		reset_btn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				PainterSettings.data.applyDefault();
				PainterSettings.saveSettings();

				fsize_input.setValue(PainterSettings.data.fontSize);
				h_ssize_input.setValue(PainterSettings.data.h_strokeWidth);
				color_preview.setBackground(PainterSettings.data.color);
				h_color_preview.setBackground(PainterSettings.data.h_color);

				JTextComponent editor = (JTextComponent) box.getEditor().getEditorComponent();
				lastFont = PainterSettings.data.fontName;
				editor.setText(lastFont);

				repaint();
			}

		});
		lowerPanel.add(reset_btn);

		// Exit btn
		JButton exit_btn = new JButton("exit");
		exit_btn.setFont(Constants.SETTINGS_BTN_FONT);
		exit_btn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				
			}

		});
		lowerPanel.add(exit_btn);

		setVisible(true);
	}

	// Creates the panel used for color selection
	private void initializeColorPanel() {

		// Create the panel
		colorPanel = new JPanel();
		colorPanel.setOpaque(true);
		colorPanel.setLayout(new BoxLayout(colorPanel, BoxLayout.Y_AXIS));

		// Add the color selector
		jcc = new JColorChooser(PainterSettings.data.color);
		jcc.setBorder(BorderFactory.createTitledBorder("Select a color"));
		colorPanel.add(jcc);

		// Add Btns-panel
		JPanel btns_pnl = new JPanel();
		btns_pnl.setLayout(new GridLayout(0, 2, 0, 0));
		btns_pnl.setBorder(new EmptyBorder(10, 50, 10, 50));
		btns_pnl.setPreferredSize(new Dimension(0, 50));
		colorPanel.add(btns_pnl);

		// Add OK btn
		JButton ok_btn = new JButton("Ok");
		ok_btn.setFont(Constants.SETTINGS_BTN_FONT);
		ok_btn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// Update UI & Settings
				if (targetColorOption == ColorOptions.DEFAULT) {
					PainterSettings.data.color = jcc.getColor();
					color_preview.setBackground(PainterSettings.data.color);
				} else {
					PainterSettings.data.h_color = jcc.getColor();
					h_color_preview.setBackground(PainterSettings.data.h_color);
				}

				// Update settings
				PainterSettings.saveSettings();

				// Swap back
				closeColorSelector();
			}

		});
		btns_pnl.add(ok_btn);

		// Add cancel btn
		JButton cancel_btn = new JButton("cancel");
		cancel_btn.setFont(Constants.SETTINGS_BTN_FONT);
		cancel_btn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				closeColorSelector();
			}

		});
		btns_pnl.add(cancel_btn);

	}
}
