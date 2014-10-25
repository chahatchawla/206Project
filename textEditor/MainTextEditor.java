package textEditor;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

import mainPackage.MediaPlayer;

/**
 * SoftEng206 Project - main text editor class
 * 
 * Purpose: The purpose of this class is to create the GUI for the text editor
 * tab and handle all the actions performed. This class is used in
 * mainPackage.Main.java to initialize the text editor tab.
 * 
 * This class is a singleton, as for our application, only one instance of the
 * MainTextEditor class should be created, since the "tab" object should only be
 * created once instead of having multiple instances being created as the
 * application progresses.
 * 
 * Text Editing provides the functionality to add a title screen or/and a credit
 * screen
 * 
 * @author Chahat Chawla ccha504 8492142
 * 
 *         Partial Code Extracted by Assignment 3
 * @author Chahat Chawla and Zainab Al Lawati
 * 
 */
public class MainTextEditor extends JPanel implements ActionListener,
		ItemListener {
	// Initializing the singleton instance of this class
	private static MainTextEditor instance = new MainTextEditor();

	// Initializing all the helper classes for text editing
	protected TextChecks tc = new TextChecks();
	protected TextProjectFunctions tpf = new TextProjectFunctions();
	protected TextHelp th = new TextHelp();
	protected TextPreview tp = new TextPreview();
	protected TextDelete td = new TextDelete();
	protected TextSave ts = new TextSave();
	protected TextBackgroundCommand tbt = new TextBackgroundCommand();

	private static final long serialVersionUID = 1L;

	// Initializing the text for the buttons
	protected final String TEXT_SAVE = "Save";
	protected final String TEXT_PREVIEW = "Preview";
	protected final String TEXT_DELETE = "Delete";
	protected final String TEXT_TIME = "Get Video Time";

	// Initializing the buttons
	protected JButton saveButton = new JButton(TEXT_SAVE);
	protected JButton prevBtn = new JButton(TEXT_PREVIEW);
	protected JButton deleteBtn = new JButton(TEXT_DELETE);
	protected JButton helpButton = new JButton();
	protected JButton getTime1Button = new JButton(TEXT_TIME);

	// Initializing the labels
	protected JLabel textEditorLabel = new JLabel("Text Editor");
	protected JLabel screenLabel = new JLabel("Add Text On: ");
	protected JLabel durationLabel = new JLabel("Duration (in seconds): ");
	protected JLabel backgroundImageLabel = new JLabel(
			"Choose a Background Option: ");
	protected JLabel addTextLabel = new JLabel("Add Text:");
	protected JLabel wordLimitLabel = new JLabel("(250 characters)");
	protected JLabel chooseFontLabel = new JLabel("Font Type: ");
	protected JLabel chooseFontStyleLabel = new JLabel("Font Style: ");
	protected JLabel chooseFontSizeLabel = new JLabel("Font Size: ");
	protected JLabel chooseColorLabel = new JLabel("Font Colour: ");

	// Initializing the textFields and textAreas
	protected JTextField addDuration = new JTextField();
	protected JTextArea addTextArea = new JTextArea(8, 30);
	protected JTextField addTimeFrame = new JTextField("hh:mm:ss");

	// Initializing the JRadioButton
	protected JRadioButton overlayCheck = new JRadioButton(
			"Overlay on the video");
	protected JRadioButton defaultCheck = new JRadioButton(
			"Default frame from 00:00:01");
	protected JRadioButton frameCheck = new JRadioButton(
			"A frame from the video at:");

	// Initializing the ComboBox and lists of drop down menus
	protected String[] dropDownScreen = { "", "Title Screen", "Credit Screen" };
	protected JComboBox screenList = new JComboBox(dropDownScreen);
	protected String[] dropDownFonts = { "Arial", "Courier", "Georgia",
			"TimesNewRoman", "Verdana" };
	protected JComboBox fontsList = new JComboBox(dropDownFonts);
	protected String[] dropDownStyles = { "PLAIN", "BOLD", "ITALIC",
			"BOLD&ITALIC" };
	protected JComboBox stylesList = new JComboBox(dropDownStyles);
	protected String[] dropDownSizes = { "8", "10", "14", "18", "22", "26",
			"30", "34", "38", "42", "48", "52", "56", "72" };
	protected JComboBox sizesList = new JComboBox(dropDownSizes);
	protected String[] dropDownColors = { "black", "green", "blue", "yellow",
			"red", "white", "pink" };
	protected JComboBox coloursList = new JComboBox(dropDownColors);

	// Initializing the image for the icons
	protected ImageIcon help;
	protected JLabel helpImage;

	// Initializing the seperators
	protected JLabel separator = new JLabel("");
	protected JLabel separator2 = new JLabel("");
	protected JLabel separator3 = new JLabel("");
	protected JLabel separator4 = new JLabel("");
	protected JLabel separator5 = new JLabel("");
	protected JLabel separator6 = new JLabel("");
	protected JLabel separator7 = new JLabel("");
	protected JLabel separator8 = new JLabel("");
	protected JLabel separator9 = new JLabel("");

	// Initializing all the variables for text editing
	protected File textFile;
	protected String screenType = "Title Screen";
	protected int backgroundImageOption = 0;

	protected int backgroundImageTitle = 3;
	protected int backgroundImageCredit = 3;

	protected String titleDuration = "";
	protected String creditDuration = "";
	protected String titleFrameTime = "";
	protected String creditFrameTime = "";
	protected int fontType = 0;
	protected int fontStyle = 0;
	protected int titleFontSize = 30;
	protected int fontSize = 30;
	protected int creditFontSize = 30;
	protected String titleFontColour = "black";
	protected String fontColour = "black";
	protected String creditFontColour = "black";
	protected String fontDir = "/usr/share/fonts/truetype/msttcorefonts/";
	protected String titleFontName = "";
	protected String fontName = "";
	protected String creditFontName = "";
	protected String projectPath;
	protected String hiddenDir;
	protected String videoLength;
	protected String videoPath;
	protected String titleCommand;
	protected String creditCommand;
	protected String previewCommand;
	protected String bothTitleAndCreditCommand;

	protected String titleFields;
	protected String workingDir;
	protected int titleFontStyle = 0;
	protected String titlePreviewFont = "Arial";
	protected int creditFontStyle = 0;
	protected String creditPreviewFont = "Arial";
	protected String prevFont = "Arial";
	protected String creditFields;

	/**
	 * Constructor for MainTextEditor() -Sets up the GUI for text editing tab -
	 * sets up the default layout
	 */

	private MainTextEditor() {
		// sets the images so they are imported from the resources folder in
		// this package
		// Reference:
		// http://docs.oracle.com/javase/tutorial/uiswing/components/icon.html

		help = new ImageIcon(
				MediaPlayer.class.getResource("Resources/help.png"));
		helpImage = new JLabel(new ImageIcon(
				MediaPlayer.class.getResource("Resources/textEdit.png")));

		// set the icons to the help button
		helpButton.setIcon(help);
		helpButton.setBorder(null);
		helpButton.setOpaque(false);
		helpButton.setContentAreaFilled(false);
		helpButton.setBorderPainted(false);

		// set the font size to be 30 as default
		sizesList.setSelectedIndex(6);

		// set the character limit for the text area to be 250
		addTextArea.setDocument(new JTextFieldLimit(250));
		// add the addTextArea textArea to the ScrollPane scroll
		JScrollPane scroll = new JScrollPane(addTextArea);
		
		// set the fonts
		textEditorLabel.setFont(new Font("TimesRoman", Font.BOLD, 20));
		addTextArea.setFont(new Font(prevFont, fontStyle, fontSize));

		// adding action listeners to all the buttons and lists so when a user
		// clicks a button or a list, the corresponding actions are done.
		prevBtn.addActionListener(this);
		deleteBtn.addActionListener(this);
		saveButton.addActionListener(this);
		helpButton.addActionListener(this);
		screenList.addActionListener(this);
		fontsList.addActionListener(this);
		stylesList.addActionListener(this);
		sizesList.addActionListener(this);
		coloursList.addActionListener(this);
		getTime1Button.addActionListener(this);

		// add all the buttons, labels, scrolls, comboBox, radioButtons and
		// textFields to the panel
		add(textEditorLabel);
		add(separator);
		add(backgroundImageLabel);
		add(separator3);
		add(overlayCheck);
		add(defaultCheck);
		add(frameCheck);
		add(addTimeFrame);
		add(getTime1Button);
		add(separator4);
		add(screenLabel);
		add(screenList);
		add(durationLabel);
		add(addDuration);
		add(separator2);
		add(addTextLabel);
		add(wordLimitLabel);
		add(separator5);
		add(chooseFontLabel);
		add(fontsList);
		add(chooseFontStyleLabel);
		add(stylesList);
		add(separator6);
		add(chooseFontSizeLabel);
		add(sizesList);
		add(chooseColorLabel);
		add(coloursList);
		add(separator8);
		add(scroll);
		add(separator7);
		add(deleteBtn);
		add(prevBtn);
		add(separator9);
		add(helpButton);
		add(saveButton);

		// set the preferred size for the seperators 
		separator.setPreferredSize(new Dimension(525, 30));
		separator2.setPreferredSize(new Dimension(525, 20));
		separator3.setPreferredSize(new Dimension(525, 10));
		separator4.setPreferredSize(new Dimension(525, 20));
		separator5.setPreferredSize(new Dimension(525, 10));
		separator6.setPreferredSize(new Dimension(525, 10));
		separator7.setPreferredSize(new Dimension(525, 30));
		separator8.setPreferredSize(new Dimension(525, 10));
		separator9.setPreferredSize(new Dimension(525, 10));

		// set the preferred size for the JComponents 
		saveButton.setPreferredSize(new Dimension(150, 25));
		helpButton.setPreferredSize(new Dimension(60, 40));
		prevBtn.setPreferredSize(new Dimension(150, 25));
		deleteBtn.setPreferredSize(new Dimension(150, 25));
		scroll.setPreferredSize(new Dimension(400, 130));
		getTime1Button.setPreferredSize(new Dimension(150, 20));

		// add ItemListner to the radio buttons, to check what happens when the
		// radioButtons are checked or unchecked
		frameCheck.addItemListener(this);
		defaultCheck.addItemListener(this);
		overlayCheck.addItemListener(this);

		// set the size for the text fields
		addTimeFrame.setColumns(6);
		addDuration.setColumns(3);

		// disabling components that are not accessible in the default screen
		addTimeFrame.setEnabled(false);
		getTime1Button.setEnabled(false);
		frameCheck.setEnabled(false);
		defaultCheck.setEnabled(false);

	}
	/**
	 * getInstance() returns the singleton instance of the MainTextEditor
	 * class
	 * 
	 * @return
	 */
	public static MainTextEditor getInstance() {
		return instance;
	}

	/**
	 * actionPerformed method responds to all the actions done by the user on
	 * the GUI
	 */
	public void actionPerformed(ActionEvent e) {

		// Make sure that the credit and title fields do not interfere with each
		// other
		if (e.getSource() == screenList) {

			screenType = screenList.getSelectedItem().toString();

			// Refresh the fields and load the title fields (if nothing is
			// stored, just keep it empty)
			if (screenList.getSelectedIndex() == 1) {
				tpf.setFieldsEnabled(true);
				tpf.refreshTitleScreen();
				tpf.setTitleFields(titleFields);

				// Refresh the fields and load the credit fields (if nothing is
				// stored, just keep it empty)
			} else if (screenList.getSelectedIndex() == 2) {
				tpf.setFieldsEnabled(true);
				tpf.refreshCreditScreen();
				tpf.setCreditFields(creditFields);

			} else { // Disable the fields if no screen is selected
				tpf.setFieldsEnabled(false);

			}
		}
		// if the save button is clicked 
		if (e.getSource() == saveButton) {
			ts.textSave();
		} 
		// if the fontsList is clicked 
		else if (e.getSource() == fontsList) {
			fontType = fontsList.getSelectedIndex();
			prevFont = fontsList.getSelectedItem().toString();
			// change the font type on the text the user writes to the font type
			// they choose
			addTextArea.setFont(new Font(prevFont, fontStyle, fontSize));
		} 
		// if the stylesList is clicked 
		else if (e.getSource() == stylesList) {
			fontStyle = stylesList.getSelectedIndex();
			// change the font style on the text the user writes to the font
			// style they choose
			addTextArea.setFont(new Font(prevFont, fontStyle, fontSize));
		} 
		// if the sizesList is clicked 
		else if (e.getSource() == sizesList) {
			fontSize = Integer.parseInt(sizesList.getSelectedItem().toString());
			// change the font size on the text the user writes to the font size
			// they choose
			addTextArea.setFont(new Font(prevFont, fontStyle, fontSize));
		} 
		// if the coloursList is clicked 
		else if (e.getSource() == coloursList) {
			fontColour = coloursList.getSelectedItem().toString();
			// change the font color on the text the user writes to the font
			// color they choose
			switch (fontColour) {
			case "black":
				addTextArea.setForeground(Color.black);
				break;
			case "green":
				addTextArea.setForeground(Color.green);
				break;
			case "blue":
				addTextArea.setForeground(Color.blue);
				break;
			case "yellow":
				addTextArea.setForeground(Color.yellow);
				break;
			case "red":
				addTextArea.setForeground(Color.red);
				break;
			case "white":
				addTextArea.setForeground(Color.white);
				break;
			case "pink":
				addTextArea.setForeground(Color.pink);
				break;
			}
		}
		// If the getTime1Button is clicked
		else if (e.getSource() == getTime1Button) {
			
			// if the video has been played once
			if (mainPackage.MediaPlayer.video.getTime() != -1) {
				// get the current time of the video
				int time = (int) mainPackage.MediaPlayer.video.getTime();
				// reference:
				// http://stackoverflow.com/questions
				// /9214786/how-to-convert-the-seconds-in-this-format-hhmmss

				// format it to HH:mm:ss
				
				SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");
				TimeZone tz = TimeZone.getTimeZone("UTC");
				df.setTimeZone(tz);
				String formatedTime = df.format(new Date(time));

				// set the addTimeFrame field to the new formatted time
				addTimeFrame.setText(formatedTime);
			} 
			// if the video hasn't been played once, inform the user so they can
			// do so.
			else {
				JOptionPane.showMessageDialog(null,
						"Please play the imported video once!");
			}

		}

		// if the preview button is clicked
		else if (e.getSource() == prevBtn) {
			tp.textPreview();
		}

		// if the delete button is clicked
		else if (e.getSource() == deleteBtn) {
			td.textDelete();
		}
		// if the help button is clicked
		else if (e.getSource() == helpButton) {
			th.textHelp();
		}
	}

	/**
	 * itemStateChanged method responds to all the item events done by the user
	 * on the GUI
	 */
	@Override
	public void itemStateChanged(ItemEvent e) {
		
		if (e.getSource() == frameCheck) {
			// if frame background is chosen, disable other radio buttons
			if (e.getStateChange() == 1) {
				defaultCheck.setEnabled(false);
				overlayCheck.setEnabled(false);
				backgroundImageOption = 2;
				addTimeFrame.setEnabled(true);
				getTime1Button.setEnabled(true);

			} else {
				addTimeFrame.setText("hh:mm:ss");
				addTimeFrame.setEnabled(false);
				getTime1Button.setEnabled(false);
				defaultCheck.setEnabled(true);
				overlayCheck.setEnabled(true);
			}
		}
		if (e.getSource() == overlayCheck) {
			// if overlay is chosen, disable other radio buttons
			if (e.getStateChange() == 1) {
				frameCheck.setEnabled(false);
				addTimeFrame.setEnabled(false);
				getTime1Button.setEnabled(false);
				defaultCheck.setEnabled(false);
				backgroundImageOption = 0;
			} else {
				frameCheck.setEnabled(true);
				defaultCheck.setEnabled(true);
			}
		}
		if (e.getSource() == defaultCheck) {
			// if a default frame is chosen, disable other radio buttons
			if (e.getStateChange() == 1) {
				frameCheck.setEnabled(false);
				addTimeFrame.setEnabled(false);
				getTime1Button.setEnabled(false);
				overlayCheck.setEnabled(false);
				backgroundImageOption = 1;
			} else {
				frameCheck.setEnabled(true);
				overlayCheck.setEnabled(true);
			}
		}
	}

	/*
	 * The 250 character limit for the the title/credit length can decrease the
	 * possibility of having large texts that exceeds the screen dimensions
	 * since the maximum font size is 72. Although it doesn't guarantee that the
	 * input will always be in right size, limiting characters is easier for the
	 * user since they get a pop up message as soon as they reach 250
	 * characters, allowing them to be aware of the limit and change their text
	 * accordingly.
	 * 
	 * Reference: http://www.java2s.com/Code/Java/Swing-JFC/
	 * LimitJTextFieldinputtoamaximumlength.htm
	 */
	
	/**
	 * JTextFieldLimit class sets the character limit for the text area 
	 * @author ccha504
	 *
	 */

	class JTextFieldLimit extends PlainDocument {
		private int limit;

		JTextFieldLimit(int limit) {
			super();
			this.limit = limit;
		}

		JTextFieldLimit(int limit, boolean upper) {
			super();
			this.limit = limit;
		}

		public void insertString(int offset, String str, AttributeSet attr)
				throws BadLocationException {
			if (str == null)
				return;

			if ((getLength() + str.length()) <= limit) {
				super.insertString(offset, str, attr);
			} else {
				JOptionPane.showMessageDialog(null,
						"ERROR: Text can only be 250 characters.");
			}

		}
	}

	/**
	 * makeCommand Method creates the text editing commands during export
	 * @param input
	 * @param output
	 * @return
	 */
	public String makeCommand(String input, String output) {

		String cmd = tbt.makeTextCommand(input, output);

		return cmd;

	}
}