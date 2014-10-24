package VideoManipulator;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import mainPackage.VideoPlayer;


/**
 * SoftEng206 Assignment3 - video manipulator class
 * 
 * @author Chahat Chawla ccha504 8492142
 * 
 *         Partial Code Extracted by Assignment 3
 * @author Chahat Chawla and Zainab Al Lawati
 * 
 */

public class VideoManipulator extends JPanel implements ItemListener,
ActionListener {

	private static VideoManipulator instance = new VideoManipulator();
	protected VideoChecks vc = new VideoChecks();
	protected VideoProjectFunctions vpf = new VideoProjectFunctions();
	protected VideoHelp vh = new VideoHelp();
	protected LoopPreview lp = new LoopPreview();
	protected SnapshotPreview sp = new SnapshotPreview();
	protected FilterPreview fp = new FilterPreview();
	protected VideoSave vs = new VideoSave();
	protected VideoBackgroundTask vbt = new VideoBackgroundTask();

	// Initializing the text for the buttons
	protected final String TEXT_SAVE = "Save";
	protected final String TEXT_PREVIEW = "Preview Filter";
	protected final String TEXT_SNAPSHOT = "Preview Snapshot";
	protected final String TEXT_LOOP = "Preview Extract Video";
	protected final String TEXT_TIME = "Get Video Time";

	// // Initializing the labels
	protected JLabel videoManipulatorLabel = new JLabel("Video Manipulator");
	protected JLabel snapshotTimeLabel = new JLabel("Snapshot Frame Time : ");
	protected JLabel outputSnapshotLabel = new JLabel("Output Snapshot Name: ");
	protected JLabel pngLabel = new JLabel(".png");

	protected JLabel startTimeLabel = new JLabel("Start Time : ");
	protected JLabel lengthLabel = new JLabel("Length : ");
	protected JLabel outputLoopVideoLabel = new JLabel(
			"Output Loop Video Name: ");
	protected JLabel mp4Label = new JLabel(".mp4");
	protected JLabel loopLabel = new JLabel("Loop Extracted Video");
	protected JLabel loop1Label = new JLabel(" Times");

	protected JLabel selectFilterLabel = new JLabel("Please Select a Filter : ");

	// Initializing the textFields
	protected JTextField timeSnapshot = new JTextField("hh:mm:ss");
	protected JTextField timeStart = new JTextField("hh:mm:ss");
	protected JTextField timeLength = new JTextField("hh:mm:ss");
	protected JTextField outputSnapshotName = new JTextField("");
	protected JTextField outputLoopVideoName = new JTextField("");
	protected JTextField loop = new JTextField("");

	// Initializing the buttons
	protected JButton helpButton = new JButton();
	protected JButton saveButton = new JButton(TEXT_SAVE);
	protected JButton prevButton = new JButton(TEXT_PREVIEW);
	protected JButton snapshotPrevButton = new JButton(TEXT_SNAPSHOT);
	protected JButton loopPrevButton = new JButton(TEXT_LOOP);
	protected JButton getTime1Button = new JButton(TEXT_TIME);

	// Initializing the check boxes
	protected JCheckBox snapshotCheck = new JCheckBox("Snapshot a Frame");
	protected JCheckBox loopVideoCheck = new JCheckBox("Extract and Loop Video");
	protected JCheckBox filterCheck = new JCheckBox(
			"Add a Filter to Input Video");

	// Initializing the drop down list
	protected String[] dropDownList = { "", "Negate", "Blur",
			"Horizontal Flip", "Vertical Flip", "Fade In", "Transpose" };
	protected JComboBox filterList = new JComboBox(dropDownList);

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

	// Initializing the image for the icons
	protected ImageIcon help;
	protected JLabel helpImage;

	// Initializing the enable booleans
	protected boolean filterEnable = false;
	protected boolean snapshotEnable = false;
	protected boolean loopVideoEnable = false;

	// Initializing the Strings

	protected String snapshotCmd = "";
	protected String loopVideoCmd = "";
	protected String filterCmd = "";
	protected String previewCmd = "";
	protected String previewSnapCmd = "";
	protected String previewFrameCmd = "";

	protected String filter = "";

	protected String projectPath;
	protected String hiddenDir;
	protected String videoPath;
	protected String videoLength;
	protected String workingDir;

	protected String videoFields;

	// Initializing the swing worker BackgroundTask
	private VideoBackgroundTask longTask;

	/**
	 * Constructor for VideoManipulator() -Sets up the GUI for video
	 * manipulation tab -Sets up the default layout
	 */

	private VideoManipulator() {
		help = new ImageIcon(VideoPlayer.class.getResource("Resources/help.png"));
		helpImage = new JLabel(new ImageIcon(VideoPlayer.class.getResource("Resources/video.png")));

		// set the icons to the help button
		helpButton.setIcon(help);
		helpButton.setBorder(null);
		helpButton.setOpaque(false);
		helpButton.setContentAreaFilled(false);
		helpButton.setBorderPainted(false);

		// change the font of the title, subTitles and starLabels
		videoManipulatorLabel.setFont(new Font("TimesRoman", Font.BOLD, 20));

		snapshotCheck.setFont(new Font("Dialog", Font.BOLD, 15));
		loopVideoCheck.setFont(new Font("Dialog", Font.BOLD, 15));
		filterCheck.setFont(new Font("Dialog", Font.BOLD, 15));

		// set the columns of the textFields
		timeSnapshot.setColumns(6);
		timeStart.setColumns(6);
		timeLength.setColumns(6);
		outputSnapshotName.setColumns(15);
		outputLoopVideoName.setColumns(15);
		loop.setColumns(2);

		// set the size of the buttons
		helpButton.setPreferredSize(new Dimension(60, 40));
		saveButton.setPreferredSize(new Dimension(150, 25));
		prevButton.setPreferredSize(new Dimension(150, 20));
		snapshotPrevButton.setPreferredSize(new Dimension(165, 20));
		loopPrevButton.setPreferredSize(new Dimension(210, 20));
		getTime1Button.setPreferredSize(new Dimension(150, 20));

		// add item to the implemented listeners
		filterCheck.addItemListener(this);
		snapshotCheck.addItemListener(this);
		loopVideoCheck.addItemListener(this);
		filterList.addActionListener(this);
		saveButton.addActionListener(this);
		helpButton.addActionListener(this);
		prevButton.addActionListener(this);
		snapshotPrevButton.addActionListener(this);
		loopPrevButton.addActionListener(this);
		getTime1Button.addActionListener(this);

		// adding all components to the Panel
		add(videoManipulatorLabel);
		add(separator);
		add(separator2);
		add(snapshotCheck);
		add(separator3);
		add(snapshotTimeLabel);
		add(timeSnapshot);
		add(getTime1Button);
		add(separator13);
		add(snapshotPrevButton);
		add(separator4);
		add(outputSnapshotLabel);
		add(outputSnapshotName);
		add(pngLabel);
		add(separator5);
		add(loopVideoCheck);
		add(separator6);
		add(startTimeLabel);
		add(timeStart);
		add(lengthLabel);
		add(timeLength);
		add(separator12);
		add(loopPrevButton);
		add(separator11);
		add(loopLabel);
		add(loop);
		add(loop1Label);
		add(separator9);
		add(outputLoopVideoLabel);
		add(outputLoopVideoName);
		add(mp4Label);
		add(separator7);
		add(filterCheck);
		add(separator8);
		add(selectFilterLabel);
		add(filterList);
		add(prevButton);
		add(separator10);
		add(helpButton);
		add(saveButton);

		// setting the size for the seperators
		separator.setPreferredSize(new Dimension(525, 10));
		separator2.setPreferredSize(new Dimension(525, 10));
		separator3.setPreferredSize(new Dimension(525, 10));
		separator4.setPreferredSize(new Dimension(525, 10));
		separator5.setPreferredSize(new Dimension(525, 20));
		separator6.setPreferredSize(new Dimension(525, 10));
		separator7.setPreferredSize(new Dimension(525, 20));
		separator8.setPreferredSize(new Dimension(525, 10));
		separator9.setPreferredSize(new Dimension(525, 10));
		separator10.setPreferredSize(new Dimension(525, 50));
		separator11.setPreferredSize(new Dimension(525, 10));
		separator12.setPreferredSize(new Dimension(525, 10));
		separator12.setPreferredSize(new Dimension(525, 0));

		// disabling components that are not accessible in the default screen
		snapshotTimeLabel.setEnabled(false);
		outputSnapshotLabel.setEnabled(false);
		pngLabel.setEnabled(false);
		timeSnapshot.setEnabled(false);
		outputSnapshotName.setEnabled(false);
		snapshotPrevButton.setEnabled(false);
		getTime1Button.setEnabled(false);

		startTimeLabel.setEnabled(false);
		lengthLabel.setEnabled(false);
		outputLoopVideoLabel.setEnabled(false);
		mp4Label.setEnabled(false);
		timeStart.setEnabled(false);
		timeLength.setEnabled(false);
		outputLoopVideoName.setEnabled(false);
		loopPrevButton.setEnabled(false);
		loop.setEnabled(false);
		loopLabel.setEnabled(false);
		loop1Label.setEnabled(false);


		selectFilterLabel.setEnabled(false);
		filterList.setEnabled(false);
		prevButton.setEnabled(false);

	}

	public static VideoManipulator getInstance() {
		return instance;
	}

	@Override
	public void itemStateChanged(ItemEvent e) {

		// Reference for JCheckBox:
		// http://www.java2s.com/Code/Java/Swing-JFC/SwingCheckBoxDemo.htm

		// if the filterCheck is clicked
		if (e.getItemSelectable() == filterCheck) {

			// if the filterCheck is enabled, set filterEnable to true and
			// enable the user to select a filter from the dropDownList

			if (e.getStateChange() == 1) {
				filterEnable = true;
				selectFilterLabel.setEnabled(true);
				filterList.setEnabled(true);
				prevButton.setEnabled(true);
			}

			// if the filterCheck is disabled, set filterEnable to false and the
			// filter options to default
			else {
				filterEnable = false;
				selectFilterLabel.setEnabled(false);
				filterList.setEnabled(false);
				prevButton.setEnabled(false);
			}
		}

		// if the snapshotCheck is clicked
		else if (e.getItemSelectable() == snapshotCheck) {

			// if the snapshotCheck is enabled, set snapshotEnable to true and
			// enable the user to add an output file name and add the snapshot
			// frame time
			if (e.getStateChange() == 1) {
				snapshotTimeLabel.setEnabled(true);
				outputSnapshotLabel.setEnabled(true);
				pngLabel.setEnabled(true);
				timeSnapshot.setEnabled(true);
				outputSnapshotName.setEnabled(true);
				snapshotPrevButton.setEnabled(true);
				getTime1Button.setEnabled(true);

				snapshotEnable = true;

			}
			// if the snapshotCheck is disabled, set snapshotEnable to false and
			// the
			// snapshot options to default
			else {
				snapshotTimeLabel.setEnabled(false);
				outputSnapshotLabel.setEnabled(false);
				pngLabel.setEnabled(false);
				timeSnapshot.setEnabled(false);
				outputSnapshotName.setEnabled(false);
				snapshotPrevButton.setEnabled(false);
				getTime1Button.setEnabled(false);

				snapshotEnable = false;
			}
		}

		// if the loopVideoCheck is clicked
		else if (e.getItemSelectable() == loopVideoCheck) {

			// if the loopVideoCheck is enabled, set loopVideoEnable to true and
			// enable the user to add an output file name, set duration and add
			// the frame time
			if (e.getStateChange() == 1) {

				loopVideoEnable = true;

				startTimeLabel.setEnabled(true);
				lengthLabel.setEnabled(true);
				outputLoopVideoLabel.setEnabled(true);
				mp4Label.setEnabled(true);
				timeStart.setEnabled(true);
				timeLength.setEnabled(true);
				outputLoopVideoName.setEnabled(true);
				loopPrevButton.setEnabled(true);
				loop.setEnabled(true);
				loopLabel.setEnabled(true);

				loop1Label.setEnabled(true);

			}

			// if the loopVideoCheck is disabled, set loopVideoEnable and
			// to false and set the loop video options to default
			else {
				startTimeLabel.setEnabled(false);
				lengthLabel.setEnabled(false);
				outputLoopVideoLabel.setEnabled(false);
				mp4Label.setEnabled(false);
				timeStart.setEnabled(false);
				timeLength.setEnabled(false);
				outputLoopVideoName.setEnabled(false);
				loopPrevButton.setEnabled(false);
				loop.setEnabled(false);
				loopLabel.setEnabled(false);

				loop1Label.setEnabled(false);

				loopVideoEnable = false;
			}
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {

		// If the save button is clicked
		if (e.getSource() == saveButton) {
			vs.videoSave();
		}

		// if the user selects a filter from the drop down list
		else if (e.getSource() == filterList) {
			filter = filterList.getSelectedItem().toString();
		}
		// If the getTime1Button is clicked
		else if (e.getSource() == getTime1Button) {

			if (mainPackage.VideoPlayer.video.getTime() != -1){
				int time = (int) mainPackage.VideoPlayer.video.getTime();
				SimpleDateFormat df = new SimpleDateFormat(
						"HH:mm:ss");
				TimeZone tz = TimeZone.getTimeZone("UTC");
				df.setTimeZone(tz);
				String formatedTime = df.format(new Date(time));

				timeSnapshot.setText(formatedTime);
			}
			else {
				JOptionPane.showMessageDialog(null,"Please play the imported video once!");
			}

		}
		// if the user wants to preview the filter
		else if (e.getSource() == prevButton) {
			fp.filterPreview();

		}

		// if the user clicks on the preview button for the snapshot option
		else if (e.getSource() == snapshotPrevButton) {
			sp.snapshotPreview();

		}
		// if the user clicks on the preview button for the loop frame option
		else if (e.getSource() == loopPrevButton) {
			lp.loopPreview();

		}

		else if (e.getSource() == helpButton) {
			vh.videoHelp();
		}
	}

	public String makeCommand(String input, String output){
		String cmd = vbt.makeVideoCommand(input, output);

		return cmd;
	}

}