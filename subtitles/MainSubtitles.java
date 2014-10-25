package subtitles;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import mainPackage.MediaPlayer;

/**
 * SoftEng206 Project - main subtitles class
 * 
 * Purpose: The purpose of this class is to create the GUI for the subtitles tab
 * and handle all the actions performed. This class is used in
 * mainPackage.Main.java to initialize the subtitles tab.
 * 
 * This class is a singleton, as for our application, only one instance of the
 * MainSubtitles class should be created, since the "tab" object should only be
 * created once instead of having multiple instances being created as the
 * application progresses.
 * 
 * Subtitles provides the functionality to create your own .srt file and also
 * import subtitles
 * 
 * @author Chahat Chawla ccha504 8492142
 * 
 *         Partial Code Extracted by Assignment 3
 * @author Chahat Chawla and Zainab Al Lawati
 * 
 */
public class MainSubtitles extends JPanel implements ItemListener,
		ActionListener {

	// Initializing the singleton instance of this class
	private static MainSubtitles instance = new MainSubtitles();

	// Initializing all the helper classes for subtitles
	protected SubtitleChecks sc = new SubtitleChecks();
	protected SubtitleProjectFunctions spf = new SubtitleProjectFunctions();
	protected SubtitleHelp sh = new SubtitleHelp();
	protected SubtitleSave ss = new SubtitleSave();
	protected SubtitleTable sl = new SubtitleTable();

	// Initializing the text for the buttons
	protected final String TEXT_SAVE = "Save";
	protected final String TEXT_ADD = "Add Subtitle";
	protected final String TEXT_DELETE = "Delete Subtitle";
	protected final String TEXT_EDIT = "Edit Subtitle";
	protected final String TEXT_CHOOSE = "Choose";
	protected final String TEXT_GENERATE = "Generate";
	protected final String TEXT_PLAY = "Play Subtitles on Imported Video";
	protected final String TEXT_TIME = "Get Video Time";

	// // Initializing the labels
	protected JLabel subtitlesLabel = new JLabel("Subtitles");
	protected JLabel listSubtitleLabel = new JLabel("List of all subtitles : ");
	protected JLabel outputFileLabel = new JLabel("Output file name : ");
	protected JLabel chooseSrtLabel = new JLabel("Please choose a .srt file : ");
	protected JLabel srt = new JLabel(" .srt");
	protected JLabel startTimeLabel = new JLabel("Start Time: ");
	protected JLabel endTimeLabel = new JLabel("End Time:   ");
	protected JLabel textLabel = new JLabel("Text: ");

	// Initializing the textField
	protected JTextField outputFileName = new JTextField("");
	protected JTextField startTime = new JTextField("hh:mm:ss,mmm");
	protected JTextField endTime = new JTextField("hh:mm:ss,mmm");
	protected JTextField text = new JTextField("");

	// Initializing the buttons
	protected JButton helpButton = new JButton();
	protected JButton inputSubtitleButton = new JButton(TEXT_ADD);
	protected JButton deleteSubtitleButton = new JButton(TEXT_DELETE);
	protected JButton editSubtitleButton = new JButton(TEXT_EDIT);
	protected JButton generateButton = new JButton(TEXT_GENERATE);
	protected JButton inputSrtButton = new JButton(TEXT_CHOOSE);
	protected JButton getTime1Button = new JButton(TEXT_TIME);
	protected JButton getTime2Button = new JButton(TEXT_TIME);
	protected JButton playButton = new JButton(TEXT_PLAY);
	protected JButton saveButton = new JButton(TEXT_SAVE);

	// Initializing the check boxes
	protected JCheckBox importCheck = new JCheckBox("Import Subtitles");
	protected JCheckBox srtCheck = new JCheckBox("Create Your Own .srt File");

	// Initializing the table
	protected DefaultTableModel model = new DefaultTableModel();
	protected JTable subtitlesTable = new JTable(model) {

		private static final long serialVersionUID = 1L;

		public boolean isCellEditable(int row, int column) {
			return false;
		};
	};;
	protected JScrollPane scrollPane = new JScrollPane(subtitlesTable);

	// Initializing the separators
	protected JLabel separator = new JLabel("");
	protected JLabel separator2 = new JLabel("");
	protected JLabel separator3 = new JLabel("");
	protected JLabel separator4 = new JLabel("");
	protected JLabel separator5 = new JLabel("");
	protected JLabel separator6 = new JLabel("");
	protected JLabel separator7 = new JLabel("");
	protected JLabel separator8 = new JLabel("");
	protected JLabel separator9 = new JLabel("");
	protected JLabel separator10 = new JLabel("");
	protected JLabel separator11 = new JLabel("");
	protected JLabel separator12 = new JLabel("");
	protected JLabel separator13 = new JLabel("");
	protected JLabel separator14 = new JLabel("");

	// Initializing the components for JFileChooser()
	protected JFileChooser chooser = new JFileChooser();
	protected String importSrt;
	protected FileNameExtensionFilter filter = new FileNameExtensionFilter(
			"srt files", "srt");

	// Initializing the enable booleans
	protected boolean srtEnable = false;
	protected boolean importEnable = false;

	// Initializing the image for the icons
	protected ImageIcon help;
	protected JLabel helpImage;

	// Initializing the Strings
	protected String inputFile = "";
	protected String srtCmd = "";
	protected String projectPath;
	protected String hiddenDir;
	protected String videoPath;
	protected String videoLength;
	protected String workingDir;
	protected String subtitlesFields;

	// Initializing the swing worker AudioBackgroundTask
	protected MainSubtitles longTask;

	/**
	 * Constructor for MainSubtitles() -Sets up the GUI for subtitle tab -Sets
	 * up the default layout
	 */

	private MainSubtitles() {

		// sets the images so they are imported from the resources folder in
		// this package
		// Reference:
		// http://docs.oracle.com/javase/tutorial/uiswing/components/icon.html
		help = new ImageIcon(
				MediaPlayer.class.getResource("Resources/help.png"));
		helpImage = new JLabel(new ImageIcon(
				MediaPlayer.class.getResource("Resources/subtitle.png")));
		// set the icons to the help button
		helpButton.setIcon(help);
		helpButton.setBorder(null);
		helpButton.setOpaque(false);
		helpButton.setContentAreaFilled(false);
		helpButton.setBorderPainted(false);

		// change the font of the title, subTitles and starLabels
		subtitlesLabel.setFont(new Font("TimesRoman", Font.BOLD, 20));
		srtCheck.setFont(new Font("Dialog", Font.BOLD, 15));
		importCheck.setFont(new Font("Dialog", Font.BOLD, 15));

		// set the columns of the textFields
		outputFileName.setColumns(15);
		startTime.setColumns(9);
		endTime.setColumns(9);
		text.setColumns(22);

		// settings for the list
		subtitlesTable.setRowSelectionAllowed(true);
		scrollPane.setPreferredSize(new Dimension(450, 110));
		// Create the columns for the table
		model.addColumn("Text");
		model.addColumn("Start Time");
		model.addColumn("End Time");

		// set the size of the buttons
		helpButton.setPreferredSize(new Dimension(60, 40));
		inputSubtitleButton.setPreferredSize(new Dimension(130, 20));
		deleteSubtitleButton.setPreferredSize(new Dimension(150, 20));
		saveButton.setPreferredSize(new Dimension(150, 25));
		editSubtitleButton.setPreferredSize(new Dimension(160, 20));
		inputSrtButton.setPreferredSize(new Dimension(90, 20));
		playButton.setPreferredSize(new Dimension(270, 20));
		generateButton.setPreferredSize(new Dimension(110, 20));
		getTime1Button.setPreferredSize(new Dimension(150, 20));
		getTime2Button.setPreferredSize(new Dimension(150, 20));

		// add item to the implemented listeners
		srtCheck.addItemListener(this);
		importCheck.addItemListener(this);
		inputSubtitleButton.addActionListener(this);
		deleteSubtitleButton.addActionListener(this);
		generateButton.addActionListener(this);
		saveButton.addActionListener(this);
		helpButton.addActionListener(this);
		editSubtitleButton.addActionListener(this);
		getTime1Button.addActionListener(this);
		getTime2Button.addActionListener(this);
		inputSrtButton.addActionListener(this);
		playButton.addActionListener(this);

		// adding all components to the Panel
		add(subtitlesLabel);
		add(separator);
		add(separator2);
		add(srtCheck);
		add(separator6);
		add(startTimeLabel);
		add(startTime);
		add(getTime1Button);
		add(separator13);
		add(endTimeLabel);
		add(endTime);
		add(getTime2Button);
		add(separator11);
		add(textLabel);
		add(text);
		add(separator12);
		add(inputSubtitleButton);
		add(separator7);
		add(listSubtitleLabel);
		add(scrollPane);
		add(separator8);
		add(deleteSubtitleButton);
		add(editSubtitleButton);
		add(separator10);
		add(outputFileLabel);
		add(outputFileName);
		add(srt);
		add(generateButton);
		add(separator9);
		add(importCheck);
		add(separator3);
		add(chooseSrtLabel);
		add(inputSrtButton);
		add(separator4);
		add(playButton);
		add(separator5);
		add(helpButton);
		add(saveButton);

		// setting the size for the seperators
		separator.setPreferredSize(new Dimension(525, 10));
		separator2.setPreferredSize(new Dimension(525, 10));
		separator3.setPreferredSize(new Dimension(525, 10));
		separator4.setPreferredSize(new Dimension(525, 0));
		separator5.setPreferredSize(new Dimension(525, 10));
		separator6.setPreferredSize(new Dimension(525, 10));
		separator7.setPreferredSize(new Dimension(525, 0));
		separator8.setPreferredSize(new Dimension(525, 0));
		separator9.setPreferredSize(new Dimension(525, 10));
		separator10.setPreferredSize(new Dimension(525, 0));
		separator11.setPreferredSize(new Dimension(525, 0));
		separator12.setPreferredSize(new Dimension(525, 0));
		separator13.setPreferredSize(new Dimension(525, 0));

		// disabling components that are not accessible in the default screen
		outputFileName.setEnabled(false);
		outputFileLabel.setEnabled(false);
		srt.setEnabled(false);
		subtitlesTable.setEnabled(false);
		inputSubtitleButton.setEnabled(false);
		deleteSubtitleButton.setEnabled(false);
		scrollPane.setEnabled(false);
		subtitlesTable.setEnabled(false);
		editSubtitleButton.setEnabled(false);
		listSubtitleLabel.setEnabled(false);
		inputSrtButton.setEnabled(false);
		playButton.setEnabled(false);
		chooseSrtLabel.setEnabled(false);
		generateButton.setEnabled(false);
		startTimeLabel.setEnabled(false);
		startTime.setEnabled(false);
		getTime1Button.setEnabled(false);
		endTimeLabel.setEnabled(false);
		endTime.setEnabled(false);
		getTime2Button.setEnabled(false);
		textLabel.setEnabled(false);
		text.setEnabled(false);

	}

	/**
	 * getInstance() returns the singleton instance of the MainAudioManipulator
	 * class
	 * 
	 * @return
	 */
	public static MainSubtitles getInstance() {
		return instance;
	}
	/**
	 * itemStateChanged method responds to all the item events done by the user
	 * on the GUI
	 */
	@Override
	public void itemStateChanged(ItemEvent e) {

		// Reference for JCheckBox:
		// http://www.java2s.com/Code/Java/Swing-JFC/SwingCheckBoxDemo.htm

		// if the srtCheck is clicked
		if (e.getItemSelectable() == srtCheck) {

			// if the srtCheck is enabled, set srtEnable to true and
			// enable the user to create a .srtFile
			if (e.getStateChange() == 1) {

				inputSubtitleButton.setEnabled(true);
				deleteSubtitleButton.setEnabled(true);
				editSubtitleButton.setEnabled(true);
				scrollPane.setEnabled(true);
				subtitlesTable.setEnabled(true);
				listSubtitleLabel.setEnabled(true);
				srtEnable = true;
				outputFileLabel.setEnabled(true);
				srt.setEnabled(true);
				subtitlesTable.setEnabled(true);
				outputFileName.setEnabled(true);
				generateButton.setEnabled(true);
				startTimeLabel.setEnabled(true);
				startTime.setEnabled(true);
				getTime1Button.setEnabled(true);
				endTimeLabel.setEnabled(true);
				endTime.setEnabled(true);
				getTime2Button.setEnabled(true);
				textLabel.setEnabled(true);
				text.setEnabled(true);

				// disable import feature while srt is enabled
				importCheck.setEnabled(false);
				importCheck.setSelected(false);
				importEnable = false;

			}
			// if the srtCheck is disabled, set srtEnable and set the srt
			// options to default
			else {

				inputSubtitleButton.setEnabled(false);
				deleteSubtitleButton.setEnabled(false);
				editSubtitleButton.setEnabled(false);
				listSubtitleLabel.setEnabled(false);
				scrollPane.setEnabled(false);
				subtitlesTable.setEnabled(false);
				srtEnable = false;
				outputFileLabel.setEnabled(false);
				srt.setEnabled(false);
				subtitlesTable.setEnabled(false);
				outputFileName.setEnabled(false);
				generateButton.setEnabled(false);
				startTimeLabel.setEnabled(false);
				startTime.setEnabled(false);
				getTime2Button.setEnabled(false);
				endTimeLabel.setEnabled(false);
				endTime.setEnabled(false);
				getTime1Button.setEnabled(false);
				textLabel.setEnabled(false);
				text.setEnabled(false);

				// enable import feature while srt is disabled
				importCheck.setEnabled(true);

			}
		}
		// if the importCheck is clicked
		else if (e.getItemSelectable() == importCheck) {

			// if the importCheck is enabled, set importEnable to true and
			// enable the user to import and add subtitles
			if (e.getStateChange() == 1) {
				inputSrtButton.setEnabled(true);
				chooseSrtLabel.setEnabled(true);
				importEnable = true;

				// disable srt feature while import is enabled
				srtCheck.setEnabled(false);
				srtCheck.setSelected(false);
				srtEnable = false;

			}
			// if the importCheck is disabled, set importEnable and set the srt
			// options to default
			else {

				inputSrtButton.setEnabled(false);
				playButton.setEnabled(false);
				chooseSrtLabel.setEnabled(false);
				importEnable = false;
				// enable srt feature while import is disabled
				srtCheck.setEnabled(true);

			}
		}

	}
	/**
	 * actionPerformed method responds to all the actions done by the user on
	 * the GUI
	 */
	@Override
	public void actionPerformed(ActionEvent e) {

		// If the inputSubtitleButton is clicked
		if (e.getSource() == inputSubtitleButton) {
			sl.inputSubtitle();
		}

		// If the deleteSubtitleButton is clicked
		else if (e.getSource() == deleteSubtitleButton) {
			sl.deleteSubtitle();
		}

		// If the playSubtitleButton is clicked
		else if (e.getSource() == editSubtitleButton) {
			sl.editSubtitle();
		}

		// If the playSubtitleButton is clicked
		else if (e.getSource() == generateButton) {
			sl.generateSubtitle();
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

				// format it to HH:mm:ss,SSS
				SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss,SSS");
				TimeZone tz = TimeZone.getTimeZone("UTC");
				df.setTimeZone(tz);
				String formatedTime = df.format(new Date(time));
				// set the startTime field to the new formatted time
				startTime.setText(formatedTime);
			}
			// if the video hasn't been played once, inform the user so they can
			// do so.
			else {
				JOptionPane.showMessageDialog(null,
						"Please play the imported video once!");
			}

		}

		// If the getTime1Button is clicked
		else if (e.getSource() == getTime2Button) {
			// if the video has been played once
			if (mainPackage.MediaPlayer.video.getTime() != -1) {

				// get the current time of the video
				int time = (int) mainPackage.MediaPlayer.video.getTime();

				// reference:
				// http://stackoverflow.com/questions
				// /9214786/how-to-convert-the-seconds-in-this-format-hhmmss

				// format it to HH:mm:ss,SSS
				SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss,SSS");
				TimeZone tz = TimeZone.getTimeZone("UTC");
				df.setTimeZone(tz);
				String formatedTime = df.format(new Date(time));
				// set the endTime field to the new formatted time
				endTime.setText(formatedTime);
			}
			// if the video hasn't been played once, inform the user so they can
			// do so.
			else {
				JOptionPane.showMessageDialog(null,
						"Please play the imported video once!");
			}
		}

		// If the inputSrtButton is clicked
		else if (e.getSource() == inputSrtButton) {

			// Reference for JFileChooser():
			// http://docs.oracle.com/javase/7/docs/api/javax/swing/JFileChooser.html

			// Show only correct extension type file by default
			chooser.setFileFilter(filter);
			int returnVal = chooser.showOpenDialog(null);
			// Store the chosen file
			if (returnVal == JFileChooser.APPROVE_OPTION) {
				inputFile = chooser.getSelectedFile().toString();

				// Reference to File.probeContentType
				// http://docs.oracle.com/javase/7/docs/api/java/nio/file/spi/FileTypeDetector.html
				File file = new File(inputFile);
				Path path = file.toPath();
				String type = "";

				try {
					type = Files.probeContentType(path);
				} catch (IOException f) {
					f.printStackTrace();
				}

				// if the file is NOT a .srt file, notify the user and
				// allow them to select again
				if (!(type.equals("application/x-subrip"))) {
					JOptionPane
							.showMessageDialog(
									null,
									"ERROR: "
											+ inputFile
											+ " does not refer to a valid .srt file. Please select a new input file!");
				} else {

					// if it is a .srt file, enable the play button
					playButton.setEnabled(true);
				}

			}

		}
		// If the playButton is clicked
		else if (e.getSource() == playButton) {

			// play the video from the start 
			mainPackage.MediaPlayer.getInstance().video.stop();
			mainPackage.MediaPlayer.getInstance().video.play();
			mainPackage.MediaPlayer.getInstance().playBtn
					.setIcon(mainPackage.MediaPlayer.getInstance().pause);
			// add the subtitles 
			mainPackage.MediaPlayer.video.setSubTitleFile(new File(inputFile));
			JOptionPane.showMessageDialog(null,
					"Imported subtitles have been added to the media player!");
		}

		// If the save button is clicked
		else if (e.getSource() == saveButton) {
			ss.subtitleSave();
		}
		// If the help button is clicked
		else if (e.getSource() == helpButton) {
			sh.subtitleHelp();
		}

	}
}