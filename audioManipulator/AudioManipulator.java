package audioManipulator;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.concurrent.ExecutionException;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.filechooser.FileNameExtensionFilter;

import mainPackage.VideoPlayer;

/**
 * SoftEng206 Assignment3 - audio manipulator class
 * 
 * @author Chahat Chawla ccha504 8492142
 * 
 * Partial Code Extracted by Assignment 3 
 * @author Chahat Chawla and Zainab Al Lawati
 * 
 */

public class AudioManipulator extends JPanel implements ItemListener,
ActionListener {

	private static AudioManipulator instance = new AudioManipulator();
	protected AudioChecks ac = new AudioChecks();
	protected AudioProjectFunctions apf = new AudioProjectFunctions();
	protected AudioHelp ah = new AudioHelp();
	protected AudioSave as = new AudioSave();
	protected OverlayList ol = new OverlayList();
	protected ReplacePreview rp = new ReplacePreview();
	protected AudioBackgroundTask abt = new AudioBackgroundTask();

	// Initializing the text for the buttons
	protected final String TEXT_SAVE = "Save";
	protected final String TEXT_CHOOSE = "Choose Audio For Replace";
	protected final String TEXT_OVERLAY = "Add Audio";
	protected final String TEXT_DELETE = "Delete Selected";
	protected final String TEXT_PLAY_AUDIO = "Play Audio";

	// // Initializing the labels
	protected JLabel audioManipulatorLabel = new JLabel("Audio Manipulator");
	protected JLabel optionalLabel = new JLabel("(* = Optional)");
	protected JLabel starLabel2 = new JLabel("*");
	protected JLabel starLabel3 = new JLabel("*");
	protected JLabel startTimeLabelExtract = new JLabel("Start Time: ");
	protected JLabel lengthLabelExtract = new JLabel("Length: ");
	protected JLabel outputFileNameLabel = new JLabel("Output File Name: ");
	protected JLabel mp3Label = new JLabel(".mp3");
	protected JLabel startTimeLabelOverlay = new JLabel("Start Time: ");
	protected JLabel lengthLabelOverlay = new JLabel("Length: ");
	protected JLabel listOverlayLabel = new JLabel("List of added audio files for overlay : ");

	// Initializing the textFields
	protected JTextField startTimeExtract = new JTextField("hh:mm:ss");
	protected JTextField lengthExtract = new JTextField("hh:mm:ss");
	protected JTextField startTimeOverlay = new JTextField("hh:mm:ss");
	protected JTextField lengthOverlay = new JTextField("hh:mm:ss");
	protected JTextField outputFileName = new JTextField("");

	// Initializing the buttons
	protected JButton helpButton = new JButton();
	protected JButton inputAudioReplaceButton = new JButton(TEXT_CHOOSE);
	protected JButton inputOverlayButton = new JButton(TEXT_OVERLAY);
	protected JButton deleteOverlayButton = new JButton(TEXT_DELETE);
	protected JButton playOverlayButton = new JButton(TEXT_PLAY_AUDIO);
	protected JButton saveButton = new JButton(TEXT_SAVE);
	protected JButton replacePlayButton = new JButton(TEXT_PLAY_AUDIO);

	// Initializing the check boxes
	protected JCheckBox removeCheck = new JCheckBox("Remove Audio");
	protected JCheckBox extractCheck = new JCheckBox("Extract Audio");
	protected JCheckBox replaceCheck = new JCheckBox("Replace Audio");
	protected JCheckBox overlayCheck = new JCheckBox("Overlay Audio");

	// Initializing the radio buttons
	protected JRadioButton extractDurationCheck = new JRadioButton(
			"Set Audio Duration");

	protected JRadioButton overlayDurationCheck = new JRadioButton(
			"Set Video Duration");

	// Initializing the list
	protected DefaultListModel audioFiles = new DefaultListModel();
	protected JList audioFilesList = new JList(audioFiles);
	protected JScrollPane scrollPane = new JScrollPane(audioFilesList);
	protected DefaultListModel fullNames = new DefaultListModel();

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

	// Initializing the enable booleans
	protected boolean removeEnable = false;
	protected boolean extractEnable = false;
	protected boolean extractDurationEnable = false;
	protected boolean replaceEnable = false;
	protected boolean overlayEnable = false;
	protected boolean overlayDurationEnable = false;

	// Initializing the components for JFileChooser()
	protected JFileChooser chooser = new JFileChooser();
	protected String inputFile;
	protected FileNameExtensionFilter filter = new FileNameExtensionFilter(
			"mp3 files", "mp3");


	// Initializing the image for the icons
	protected ImageIcon help ;
	protected JLabel helpImage;

	// Initializing the Strings
	protected String removeCmd = "";
	protected String extractCmd = "";
	protected String replaceCmd = "";
	protected String overlayCmd = "";
	protected String projectPath;
	protected String hiddenDir;
	protected String videoPath;
	protected String videoLength;
	protected String workingDir;
	protected String audioFields;

	// Initializing the swing worker AudioBackgroundTask
	protected AudioBackgroundTask longTask;

	/**
	 * Constructor for AudioManipulator() -Sets up the GUI for audio
	 * manipulation tab -Sets up the default layout
	 */

	private AudioManipulator() {
		
		help = new ImageIcon(VideoPlayer.class.getResource("Resources/help.png"));
		helpImage = new JLabel(new ImageIcon(VideoPlayer.class.getResource("Resources/audio.png")));
		// set the icons to the help button
		helpButton.setIcon(help);
		helpButton.setBorder(null);
		helpButton.setOpaque(false);
		helpButton.setContentAreaFilled(false);
		helpButton.setBorderPainted(false);

		// change the font of the title, subTitles and starLabels
		audioManipulatorLabel.setFont(new Font("TimesRoman", Font.BOLD, 20));

		audioManipulatorLabel.setFont(new Font("TimesRoman", Font.BOLD, 20));
		starLabel2.setFont(new Font("TimesRoman", Font.BOLD, 18));
		removeCheck.setFont(new Font("Dialog", Font.BOLD, 15));
		extractCheck.setFont(new Font("Dialog", Font.BOLD, 15));
		replaceCheck.setFont(new Font("Dialog", Font.BOLD, 15));
		overlayCheck.setFont(new Font("Dialog", Font.BOLD, 15));

		// set the columns of the textFields
		startTimeExtract.setColumns(6);
		lengthExtract.setColumns(6);
		startTimeOverlay.setColumns(6);
		lengthOverlay.setColumns(6);
		outputFileName.setColumns(15);

		// settings for the list
		audioFilesList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		audioFilesList.setSelectedIndex(0);
		audioFilesList.setVisibleRowCount(5);
		scrollPane.setPreferredSize(new Dimension(450, 100));

		// set the size of the buttons
		helpButton.setPreferredSize(new Dimension(60, 40));
		inputAudioReplaceButton.setPreferredSize(new Dimension(230, 20));
		inputOverlayButton.setPreferredSize(new Dimension(150, 20));
		deleteOverlayButton.setPreferredSize(new Dimension(150, 20));
		saveButton.setPreferredSize(new Dimension(150, 25));
		replacePlayButton.setPreferredSize(new Dimension(130, 20));
		playOverlayButton.setPreferredSize(new Dimension(130, 20));

		// add item to the implemented listeners
		removeCheck.addItemListener(this);
		extractCheck.addItemListener(this);
		extractDurationCheck.addItemListener(this);
		replaceCheck.addItemListener(this);
		overlayCheck.addItemListener(this);
		overlayDurationCheck.addItemListener(this);
		inputAudioReplaceButton.addActionListener(this);
		inputOverlayButton.addActionListener(this);
		deleteOverlayButton.addActionListener(this);
		saveButton.addActionListener(this);
		helpButton.addActionListener(this);
		replacePlayButton.addActionListener(this);
		playOverlayButton.addActionListener(this);

		// adding all components to the Panel
		add(audioManipulatorLabel);
		add(separator);
		add(optionalLabel);
		add(separator2);
		add(removeCheck);
		add(separator3);
		add(separator4);
		add(extractCheck);
		add(separator5);
		add(outputFileNameLabel);
		add(outputFileName);
		add(mp3Label);
		add(separator6);
		add(starLabel2);
		add(extractDurationCheck);
		add(startTimeLabelExtract);
		add(startTimeExtract);
		add(lengthLabelExtract);
		add(lengthExtract);
		add(separator7);
		add(replaceCheck);
		add(separator8);
		add(inputAudioReplaceButton);
		add(replacePlayButton);
		add(separator9);
		add(separator11);
		add(overlayCheck);
		add(separator12);
		add(starLabel3);
		add(overlayDurationCheck);
		add(startTimeLabelOverlay);
		add(startTimeOverlay);
		add(lengthLabelOverlay);
		add(lengthOverlay);
		add(separator13);
		add(inputOverlayButton);
		add(deleteOverlayButton);
		add(playOverlayButton);
		add(separator14);
		add(listOverlayLabel);
		add(scrollPane);
		add(separator10);
		add(helpButton);
		add(saveButton);

		// setting the size for the seperators
		separator.setPreferredSize(new Dimension(525, 10));
		separator2.setPreferredSize(new Dimension(525, 10));
		separator3.setPreferredSize(new Dimension(525, 0));
		separator4.setPreferredSize(new Dimension(525, 10));
		separator5.setPreferredSize(new Dimension(525, 0));
		separator6.setPreferredSize(new Dimension(525, 0));
		separator7.setPreferredSize(new Dimension(525, 10));
		separator8.setPreferredSize(new Dimension(525, 0));
		separator9.setPreferredSize(new Dimension(525, 0));
		separator10.setPreferredSize(new Dimension(525, 10));
		separator11.setPreferredSize(new Dimension(525, 10));
		separator12.setPreferredSize(new Dimension(525, 0));
		separator13.setPreferredSize(new Dimension(525, 0));
		separator14.setPreferredSize(new Dimension(525, 10));


		// disabling components that are not accessible in the default screen
		outputFileNameLabel.setEnabled(false);
		outputFileName.setEnabled(false);
		mp3Label.setEnabled(false);
		starLabel2.setEnabled(false);
		extractDurationCheck.setEnabled(false);
		startTimeLabelExtract.setEnabled(false);
		startTimeExtract.setEnabled(false);
		lengthLabelExtract.setEnabled(false);
		lengthExtract.setEnabled(false);
		inputAudioReplaceButton.setEnabled(false);
		replacePlayButton.setEnabled(false);

		starLabel3.setEnabled(false);
		overlayDurationCheck.setEnabled(false);
		startTimeLabelOverlay.setEnabled(false);
		startTimeOverlay.setEnabled(false);
		lengthLabelOverlay.setEnabled(false);
		lengthOverlay.setEnabled(false);
		inputOverlayButton.setEnabled(false);
		deleteOverlayButton.setEnabled(false);
		scrollPane.setEnabled(false);
		audioFilesList.setEnabled(false);
		playOverlayButton.setEnabled(false);
		listOverlayLabel.setEnabled(false);

	}


	public static AudioManipulator getInstance() {
		return instance;
	}


	@Override
	public void itemStateChanged(ItemEvent e) {

		// Reference for JCheckBox:
		// http://www.java2s.com/Code/Java/Swing-JFC/SwingCheckBoxDemo.htm

		// if the removeCheck is clicked
		if (e.getItemSelectable() == removeCheck) {

			// if the removeCheck is enabled, set removeEnable to true
			if (e.getStateChange() == 1) {
				removeEnable = true;


			}

			// if the removeCheck is disabled, set removeEnable to false
			else {
				removeEnable = false;
			}
		}

		// if the extractCheck is clicked
		else if (e.getItemSelectable() == extractCheck) {

			// if the extractCheck is enabled, set extractEnable to true and
			// enable the user to replace an output file name and set duration
			if (e.getStateChange() == 1) {
				outputFileNameLabel.setEnabled(true);
				outputFileName.setEnabled(true);
				mp3Label.setEnabled(true);
				starLabel2.setEnabled(true);
				extractDurationCheck.setEnabled(true);

				extractEnable = true;

			}
			// if the extractCheck is disabled, set extractEnable and
			// extractDurationEnable to false and set the extract options to
			// default
			else {
				outputFileNameLabel.setEnabled(false);
				outputFileName.setEnabled(false);
				outputFileName.setText("");
				mp3Label.setEnabled(false);
				starLabel2.setEnabled(false);
				extractDurationCheck.setEnabled(false);

				extractDurationCheck.setSelected(false);
				startTimeLabelExtract.setEnabled(false);
				startTimeExtract.setEnabled(false);
				startTimeExtract.setText("");
				lengthLabelExtract.setEnabled(false);
				lengthExtract.setEnabled(false);
				lengthExtract.setText("");

				extractEnable = false;
				extractDurationEnable = false;
			}
		}
		// if the overlayCheck is clicked
		else if (e.getItemSelectable() == overlayCheck) {

			// if the overlayCheck is enabled, set overlayEnable to true and
			// enable the user to choose an input .mp3 file
			if (e.getStateChange() == 1) {

				removeCheck.setEnabled(false);
				removeCheck.setSelected(false);
				removeEnable = false;

				starLabel3.setEnabled(true);
				overlayDurationCheck.setEnabled(true);
				inputOverlayButton.setEnabled(true);
				deleteOverlayButton.setEnabled(true);
				playOverlayButton.setEnabled(true);
				scrollPane.setEnabled(true);
				audioFilesList.setEnabled(true);
				listOverlayLabel.setEnabled(true);
				overlayEnable = true;
			}

			// if the overlayCheck is disabled, set overlayEnable and
			// overlayDurationEnable to false and set the overlay options to
			// default
			else {

				removeCheck.setEnabled(true);

				starLabel2.setEnabled(false);
				overlayDurationCheck.setSelected(false);
				inputOverlayButton.setEnabled(false);
				deleteOverlayButton.setEnabled(false);
				playOverlayButton.setEnabled(false);
				listOverlayLabel.setEnabled(false);
				scrollPane.setEnabled(false);
				audioFilesList.setEnabled(false);
				overlayEnable = false;

				startTimeLabelOverlay.setEnabled(false);
				startTimeOverlay.setEnabled(false);
				startTimeOverlay.setText("");
				lengthLabelOverlay.setEnabled(false);
				lengthOverlay.setEnabled(false);
				lengthOverlay.setText("");

			}
		}

		// if the replaceCheck is clicked
		else if (e.getItemSelectable() == replaceCheck) {

			// if the replaceCheck is enabled, set replaceEnable to true and
			// enable the user to choose an input .mp3 file
			if (e.getStateChange() == 1) {
				inputAudioReplaceButton.setEnabled(true);
				replacePlayButton.setEnabled(true);
				replaceEnable = true;
			}

			// if the replaceCheck is disabled, set replaceEnable and
			// replaceDurationEnable to false and set the replace options to
			// default
			else {
				inputAudioReplaceButton.setEnabled(false);
				replacePlayButton.setEnabled(false);
				inputFile = "";
				replaceEnable = false;

			}
		}

		// Reference for JRadioButton:
		// http://www.tutorialspoint.com/swing/swing_jradiobutton.htm

		// if the extractDurationCheck is clicked
		else if (e.getItemSelectable() == extractDurationCheck) {

			// if the extractDurationCheck is enabled, enable duration options
			if (e.getStateChange() == 1) {

				startTimeLabelExtract.setEnabled(true);
				startTimeExtract.setEnabled(true);
				lengthLabelExtract.setEnabled(true);
				lengthExtract.setEnabled(true);
				extractDurationEnable = true;
			}

			// if the extractDurationCheck is disabled, disable duration options
			else {
				startTimeLabelExtract.setEnabled(false);
				startTimeExtract.setEnabled(false);
				lengthLabelExtract.setEnabled(false);
				lengthExtract.setEnabled(false);
				extractDurationEnable = false;
			}
		}

		// if the overlayDurationCheck is clicked
		else if (e.getItemSelectable() == overlayDurationCheck) {

			// if the overlayDurationCheck is enabled, enable duration options
			if (e.getStateChange() == 1) {

				startTimeLabelOverlay.setEnabled(true);
				startTimeOverlay.setEnabled(true);
				lengthLabelOverlay.setEnabled(true);
				lengthOverlay.setEnabled(true);
				overlayDurationEnable = true;
			}

			// if the overlayDurationCheck is disabled, disable duration options
			else {
				startTimeLabelOverlay.setEnabled(false);
				startTimeOverlay.setEnabled(false);
				lengthLabelOverlay.setEnabled(false);
				lengthOverlay.setEnabled(false);
				overlayDurationEnable = false;
			}
		}

	}

	@Override
	public void actionPerformed(ActionEvent e) {

		// If the inputAudioButton is clicked
		if (e.getSource() == inputAudioReplaceButton) {

			// Reference for JFileChooser():
			// http://docs.oracle.com/javase/7/docs/api/javax/swing/JFileChooser.html

			// Show only correct extension type file by default
			chooser.setFileFilter(filter);
			int returnVal = chooser.showOpenDialog(null);
			// Store the chosen file
			if (returnVal == JFileChooser.APPROVE_OPTION) {
				inputFile = chooser.getSelectedFile().toString();
			}

		}
		// If the inputOverlayButton is clicked
		else if (e.getSource() == inputOverlayButton) {
			ol.inputOverlay();
		}
		// If the deleteOverlayButton is clicked
		else if (e.getSource() == deleteOverlayButton) {
			ol.deleteOverlay();
		}

		// If the playOverlayButton is clicked
		else if (e.getSource() == playOverlayButton) {
			ol.playOverlay();
		}
		// If the save button is clicked
		else if (e.getSource() == saveButton) {
			as.audioSave();
		}

		// If the replace play button is clicked
		else if (e.getSource() == replacePlayButton) {
			rp.replacePreview();
		}

		else if (e.getSource() == helpButton){
			ah.audioHelp();
		}

	}
	/**
	 * Method run all the audio manipulating commands in a background thread
	 * 
	 * @return exit status
	 */
	public String makeCommand(String input, String output){
		
		String cmd = abt.makeAudioCommand(input, output);
		
		return cmd;
	
	}
}