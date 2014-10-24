package subtitles;

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
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;

import mainPackage.VideoPlayer;

/**
 * SoftEng206 Assignment3 - subtitles class
 * 
 * @author Chahat Chawla ccha504 8492142
 * 
 * Partial Code Extracted by Assignment 3 
 * @author Chahat Chawla and Zainab Al Lawati
 * 
 */

public class Subtitles extends JPanel implements ItemListener,
ActionListener {

	private static Subtitles instance = new Subtitles();
	//	protected AudioChecks ac = new AudioChecks();
	//	protected AudioProjectFunctions apf = new AudioProjectFunctions();
	//	protected AudioHelp ah = new AudioHelp();
	//	protected AudioSave as = new AudioSave();
	protected SubtitleList sl = new SubtitleList();
	//	protected ReplacePreview rp = new ReplacePreview();
	//	protected AudioBackgroundTask abt = new AudioBackgroundTask();

	// Initializing the text for the buttons
	protected final String TEXT_SAVE = "Save";
	protected final String TEXT_ADD = "Add Subtitle";
	protected final String TEXT_DELETE = "Delete Subtitle";
	protected final String TEXT_PREVIEW = "Preview Subtitle";
	protected final String TEXT_CHOOSE = "Choose";
	protected final String TEXT_GENERATE = "Generate";
	protected final String TEXT_PLAY = "Play Subtitles on Imported Video";
	protected final String TEXT_TIME = "Get Video Time";

	// // Initializing the labels
	protected JLabel srtLabel = new JLabel("Subtitles");
	protected JLabel listOverlayLabel = new JLabel("List of all subtitles : ");
	protected JLabel outputFileLabel = new JLabel("Output file name : ");
	protected JLabel chooseSrtLabel = new JLabel("Please choose a .srt file : ");
	protected JLabel srt = new JLabel(" .srt");
	protected JLabel startTimeLabel = new JLabel("Start Time: ");
	protected JLabel endTimeLabel = new JLabel("End Time:   ");
	protected JLabel textLabel = new JLabel("Text: ");

	// Initializing the textField
	protected JTextField outputFileName = new JTextField("");
	protected JTextField startTime = new JTextField("hh:mm:ss");
	protected JTextField endTime = new JTextField("hh:mm:ss");
	protected JTextField text = new JTextField("");


	// Initializing the buttons
	protected JButton helpButton = new JButton();
	protected JButton inputSubtitleButton = new JButton(TEXT_ADD);
	protected JButton deleteSubtitleButton = new JButton(TEXT_DELETE);
	protected JButton playSubtitleButton = new JButton(TEXT_PREVIEW);
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
	protected JTable subtitlesTable = new JTable(model){

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
	protected ImageIcon help ;
	protected JLabel helpImage;

	// Initializing the Strings

	protected String srtCmd = "";
	protected String projectPath;
	protected String hiddenDir;
	protected String videoPath;
	protected String videoLength;
	protected String workingDir;
	protected String subtitlesFields;

	// Initializing the swing worker AudioBackgroundTask
	protected Subtitles longTask;

	/**
	 * Constructor for AudioManipulator() -Sets up the GUI for audio
	 * manipulation tab -Sets up the default layout
	 */

	private Subtitles() {


		// Create a couple of columns 
		model.addColumn("Text"); 
		model.addColumn("Start Time"); 
		model.addColumn("End Time"); 



		help = new ImageIcon(VideoPlayer.class.getResource("Resources/help.png"));
		helpImage = new JLabel(new ImageIcon(VideoPlayer.class.getResource("Resources/audio.png")));
		// set the icons to the help button
		helpButton.setIcon(help);
		helpButton.setBorder(null);
		helpButton.setOpaque(false);
		helpButton.setContentAreaFilled(false);
		helpButton.setBorderPainted(false);

		// change the font of the title, subTitles and starLabels
		srtLabel.setFont(new Font("TimesRoman", Font.BOLD, 20));
		srtCheck.setFont(new Font("Dialog", Font.BOLD, 15));
		importCheck.setFont(new Font("Dialog", Font.BOLD, 15));

		// set the columns of the textFields
		outputFileName.setColumns(15);
		startTime.setColumns(6);
		endTime.setColumns(6);
		text.setColumns(22);

		// settings for the list
		subtitlesTable.setRowSelectionAllowed(true);
		scrollPane.setPreferredSize(new Dimension(450, 110));

		// set the size of the buttons
		helpButton.setPreferredSize(new Dimension(60, 40));
		inputSubtitleButton.setPreferredSize(new Dimension(130, 20));
		deleteSubtitleButton.setPreferredSize(new Dimension(150, 20));
		saveButton.setPreferredSize(new Dimension(150, 25));
		playSubtitleButton.setPreferredSize(new Dimension(160, 20));
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
		playSubtitleButton.addActionListener(this);

		// adding all components to the Panel
		add(srtLabel);
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
		add(listOverlayLabel);
		add(scrollPane);
		add(separator8);
		add(deleteSubtitleButton);
		add(playSubtitleButton);
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
		playSubtitleButton.setEnabled(false);
		listOverlayLabel.setEnabled(false);
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


	public static Subtitles getInstance() {
		return instance;
	}


	@Override
	public void itemStateChanged(ItemEvent e) {

		// Reference for JCheckBox:
		// http://www.java2s.com/Code/Java/Swing-JFC/SwingCheckBoxDemo.htm


		if (e.getItemSelectable() == srtCheck) {


			if (e.getStateChange() == 1) {


				inputSubtitleButton.setEnabled(true);
				deleteSubtitleButton.setEnabled(true);
				playSubtitleButton.setEnabled(true);
				scrollPane.setEnabled(true);
				subtitlesTable.setEnabled(true);
				listOverlayLabel.setEnabled(true);
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



				importCheck.setEnabled(false);
				importCheck.setSelected(false);
				importEnable = false;

			}


			else {


				inputSubtitleButton.setEnabled(false);
				deleteSubtitleButton.setEnabled(false);
				playSubtitleButton.setEnabled(false);
				listOverlayLabel.setEnabled(false);
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

				importCheck.setEnabled(true);



			}
		}
		else if (e.getItemSelectable() == importCheck) {

			if (e.getStateChange() == 1) {
				inputSrtButton.setEnabled(true);
				playButton.setEnabled(true);
				chooseSrtLabel.setEnabled(true);
				importEnable = true;


				srtCheck.setEnabled(false);
				srtCheck.setSelected(false);
				srtEnable = false;

			}

			else {

				inputSrtButton.setEnabled(false);
				playButton.setEnabled(false);
				chooseSrtLabel.setEnabled(false);
				importEnable = false;

				srtCheck.setEnabled(true);


			}
		}

	}

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
		else if (e.getSource() == playSubtitleButton) {
			sl.playSubtitle();
		}
		
		
		// If the playSubtitleButton is clicked
		else if (e.getSource() == generateButton) {
			sl.generateSubtitle();
		}






		// If the save button is clicked
		else if (e.getSource() == saveButton) {
			//as.audioSave();
		}


		else if (e.getSource() == helpButton){
			//ah.audioHelp();
		}

	}
	/**
	 * Method run all the audio manipulating commands in a background thread
	 * 
	 * @return exit status
	 */
	public String makeCommand(String input, String output){

		String cmd = "";
		//= abt.makeAudioCommand(input, output);

		return cmd;

	}
}