package mainPackage;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.SwingWorker;
import javax.swing.Timer;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import uk.co.caprica.vlcj.component.EmbeddedMediaPlayerComponent;
import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer;

/**
 * SoftEng206 Project - media player class
 * 
 * Purpose: The purpose of this class is to create the GUI for the media player
 * and handle all the actions performed. This class is used in
 * mainPackage.Main.java to initialize the media player.
 * 
 * This class is a singleton, as for our application, only one instance of the
 * MediaPlayer class should be created, since the media player object should
 * only be created once instead of having multiple instances being created as
 * the application progresses.
 * 
 * Media Player provides the functionality to play, stop, pause, mute, forward,
 * rewind the media and also provides a volume and a video slider.
 * 
 * 
 * @author Chahat Chawla ccha504 8492142
 * 
 *         Partial Code Extracted by Assignment 3
 * @author Chahat Chawla and Zainab Al Lawati
 * 
 */
public class MediaPlayer extends JPanel implements ActionListener,
		ChangeListener {

	private static final long serialVersionUID = 1L;

	// Initializing the singleton instance of this class
	private static MediaPlayer instance = new MediaPlayer();

	// Initializing the embedded media player components
	private final EmbeddedMediaPlayerComponent mediaPlayerComponent;
	public EmbeddedMediaPlayer video;

	// Initializing the image icons
	protected ImageIcon forward;
	protected ImageIcon rewind;
	protected ImageIcon play;
	public ImageIcon pause;
	protected ImageIcon stop;
	protected ImageIcon mute;
	protected ImageIcon unmute;

	// Initializing the buttons
	protected JButton fastFwdBtn = new JButton();
	protected JButton backFwdBtn = new JButton();
	public JButton playBtn = new JButton();
	protected JButton stopBtn = new JButton();
	protected JButton muteBtn = new JButton();

	// Initializing the labels
	protected JLabel timeDisplay = new JLabel("00:00:00");

	// Initializing the JSliders
	protected JSlider volume = new JSlider(JSlider.HORIZONTAL, 0, 200, 100);
	protected JSlider videoSlider = new JSlider(JSlider.HORIZONTAL, 0, 54900, 0);

	protected SkipTask longTask;
	protected Download download;
	protected String inputVideo;

	/**
	 * Constructor for MediaPlayer() -Sets up the GUI for the media player -
	 * sets up the default layout
	 */

	private MediaPlayer() {

		// sets the images so they are imported from the resources folder in
		// this package
		// Reference:
		// http://docs.oracle.com/javase/tutorial/uiswing/components/icon.html
		forward = new ImageIcon(
				MediaPlayer.class.getResource("Resources/forward.png"));
		rewind = new ImageIcon(
				MediaPlayer.class.getResource("Resources/rewind.png"));
		play = new ImageIcon(
				MediaPlayer.class.getResource("Resources/play.png"));
		pause = new ImageIcon(
				MediaPlayer.class.getResource("Resources/pause.png"));
		stop = new ImageIcon(
				MediaPlayer.class.getResource("Resources/stop.png"));
		mute = new ImageIcon(
				MediaPlayer.class.getResource("Resources/mute.png"));
		unmute = new ImageIcon(
				MediaPlayer.class.getResource("Resources/volume.png"));

		// set the preferred sizes for the buttons
		playBtn.setPreferredSize(new Dimension(60, 35));
		fastFwdBtn.setPreferredSize(new Dimension(60, 35));
		backFwdBtn.setPreferredSize(new Dimension(60, 35));
		stopBtn.setPreferredSize(new Dimension(60, 35));
		muteBtn.setPreferredSize(new Dimension(60, 35));
		volume.setPreferredSize(new Dimension(120, 35));

		// set the icons to all the buttons
		playBtn.setIcon(play);
		playBtn.setBorder(null);
		playBtn.setOpaque(false);
		playBtn.setContentAreaFilled(false);
		playBtn.setBorderPainted(false);

		fastFwdBtn.setIcon(forward);
		fastFwdBtn.setBorder(null);
		fastFwdBtn.setOpaque(false);
		fastFwdBtn.setContentAreaFilled(false);
		fastFwdBtn.setBorderPainted(false);

		backFwdBtn.setIcon(rewind);
		backFwdBtn.setBorder(null);
		backFwdBtn.setOpaque(false);
		backFwdBtn.setContentAreaFilled(false);
		backFwdBtn.setBorderPainted(false);

		stopBtn.setIcon(stop);
		stopBtn.setBorder(null);
		stopBtn.setOpaque(false);
		stopBtn.setContentAreaFilled(false);
		stopBtn.setBorderPainted(false);

		muteBtn.setIcon(unmute);
		muteBtn.setBorder(null);
		muteBtn.setOpaque(false);
		muteBtn.setContentAreaFilled(false);
		muteBtn.setBorderPainted(false);

		// Set the media player
		mediaPlayerComponent = new EmbeddedMediaPlayerComponent();
		mediaPlayerComponent.setPreferredSize(new Dimension(675, 500));
		video = mediaPlayerComponent.getMediaPlayer();

		// Set the time Bar
		JPanel timeBar = new JPanel();
		timeBar.add(videoSlider);
		timeBar.add(timeDisplay);

		// Set the tool Bar
		JPanel toolBar = new JPanel();
		toolBar.add(backFwdBtn);
		toolBar.add(stopBtn);
		toolBar.add(playBtn);
		toolBar.add(fastFwdBtn);
		toolBar.add(muteBtn);
		toolBar.add(volume);

		JPanel allBars = new JPanel(new BorderLayout());
		allBars.add(timeBar, BorderLayout.NORTH);
		allBars.add(toolBar, BorderLayout.CENTER);

		// Add all the panels to the main MediaPlayer panel
		setLayout(new BorderLayout());
		add(mediaPlayerComponent, BorderLayout.CENTER);
		add(allBars, BorderLayout.SOUTH);

		// adding action listeners to all the buttons so when a user clicks a
		// button, the corresponding actions are done.
		backFwdBtn.addActionListener(this);
		stopBtn.addActionListener(this);
		playBtn.addActionListener(this);
		fastFwdBtn.addActionListener(this);
		muteBtn.addActionListener(this);
		volume.addChangeListener(this);
		videoSlider.addChangeListener(this);

		// set the video slider to 0 initially
		videoSlider.setValue(0);

		// Add a mouse listener to find and calculate where to transition to on
		// the video/audio.
		videoSlider.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {

				// find the position of the click on the jslider
				double position = (((double) e.getX() / (double) videoSlider
						.getWidth()) * videoSlider.getMaximum());

				if (position >= videoSlider.getMaximum()) {
					// if they have reached the maximum, go back to 0
					videoSlider.setValue(0);
				}

				else {
					// update the video slider
					videoSlider.setValue((int) position);

					// then find the time of the video and set it
					double time = ((double) videoSlider.getValue() / videoSlider
							.getMaximum()) * ((double) video.getLength());
					video.setTime((long) time);

				}
			}

			public void mousePressed(MouseEvent e) {

				// find the position of the click on the jslider
				double position = (((double) e.getX() / (double) videoSlider
						.getWidth()) * videoSlider.getMaximum());

				if (position >= videoSlider.getMaximum()) {
					// if they have reached the maximum, go back to 0
					videoSlider.setValue(0);
				}

				else {
					// update the video slider
					videoSlider.setValue((int) position);

					// then find the time of the video and set it
					double time = ((double) videoSlider.getValue() / videoSlider
							.getMaximum()) * ((double) video.getLength());
					video.setTime((long) time);

				}
			}

			public void mouseReleased(MouseEvent e) {

				// find the position of the click on the jslider
				double position = (((double) e.getX() / (double) videoSlider
						.getWidth()) * videoSlider.getMaximum());

				if (position >= videoSlider.getMaximum()) {
					// if they have reached the maximum, go back to 0
					videoSlider.setValue(0);
				}

				else {
					// update the video slider
					videoSlider.setValue((int) position);

					// then find the time of the video and set it
					double time = ((double) videoSlider.getValue() / videoSlider
							.getMaximum()) * ((double) video.getLength());
					video.setTime((long) time);

				}
			}
		});

		// Setting preferred sizes for some components
		timeDisplay.setPreferredSize(new Dimension(100, 20));
		videoSlider.setPreferredSize(new Dimension(400, 20));

		// Set the volume values back to default when the user closes the Vamix
		// player
		Main.frame.addWindowListener(new java.awt.event.WindowAdapter() {
			@Override
			public void windowClosing(java.awt.event.WindowEvent windowEvent) {
				video.mute(false);
				video.setVolume(100);
			}
		});
		// Set the video playing and the video imported by user to null
		// to start the window with fresh inputs next time.
		Main.frame.addWindowListener(new java.awt.event.WindowAdapter() {
			@Override
			public void windowClosed(java.awt.event.WindowEvent windowEvent) {
				inputVideo = null;
			}
		});

	}

	/**
	 * getInstance() returns the singleton instance of the MediaPlayer class
	 * 
	 * @return
	 */
	public static MediaPlayer getInstance() {
		return instance;
	}

	/**
	 * Activate the volume bar
	 */
	@Override
	public void stateChanged(ChangeEvent e) {
		if (video.isPlayable()) {
			if (e.getSource() == volume) {
				video.setVolume(volume.getValue());
			}
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {

		// If the mute button is clicked
		if (e.getSource() == muteBtn) {
			// Un-mute the video, if the video is already mute
			if (video.isMute()) {
				video.mute(false);
				muteBtn.setIcon(unmute);
			} else { // mute the video otherwise
				video.mute(true);
				muteBtn.setIcon(mute);
			}
		}
		// If the stop button is clicked
		else if (e.getSource() == stopBtn) {
			// stop the video and refresh
			video.stop();
			timeDisplay.setText("00:00:00");
			videoSlider.setValue(0);
			playBtn.setIcon(play);

		}
		// If the play button is clicked
		else if (e.getSource() == playBtn) {
			if (longTask != null && !longTask.isDone()) {
				// if the video is currently skipping, stop the skipping task
				// and play the video
				longTask.cancel(false);
				video.play();
				playBtn.setIcon(pause);
			} else if (video.isPlaying()) {
				// If the video is playing, pause it.
				video.pause();
				playBtn.setIcon(play);
			} else {
				if (!video.isPlayable()) {
					// If the mediaPlayerComponent doesn't have a video yet
					if (download != null || readVideoPath() != null) {
						if (download != null) {
							// Store the inputVideo that has been downloaded
							String chosenFile = download.getChosenFile();
							if (!chosenFile.equals("No file yet")) {
								// play the chosen file using the playVideo()
								// method
								playVideo(chosenFile);
							}
						}
						if (readVideoPath() != null) {
							// Play the video that was imported from folder
							// using the playVideo() method
							playVideo(readVideoPath());
						}
					}
				}
				// If video is paused
				else {
					video.play();
					playBtn.setIcon(pause);
				}
			}
		}
		// If the fast forward button is clicked
		else if (e.getSource() == fastFwdBtn) {
			// If the video is already fast forwarding, cancel it and start a
			// new forwarding task
			if (longTask != null && !longTask.isDone()) {
				longTask.cancel(false);
			}
			// Start the forwarding task if video is playing
			if (video.isPlaying()) {
				longTask = new SkipTask(10);
				longTask.execute();
				playBtn.setIcon(play);
			}
		}
		// If the back forward button is clicked
		else if (e.getSource() == backFwdBtn) {
			// If the video is already is rewinding, cancel it and start a new
			// rewind task
			if (longTask != null && !longTask.isDone()) {
				longTask.cancel(false);
			}
			// Start the rewind task if the video is playing
			if (video.isPlaying()) {
				longTask = new SkipTask(-10);
				longTask.execute();
				playBtn.setIcon(play);
			}
		}

	}

	/**
	 * readVideoPath Method reads the project file and store the full path of
	 * the imported video
	 * 
	 * @return input video path
	 */
	private String readVideoPath() {

		// Reference:
		// http://docs.oracle.com/javase/7/docs/api/java/io/BufferedReader.html

		// Get the main project file
		String projectPath = Menu.getInstance().getProjectPath();
		File f = new File(projectPath);
		try {
			// Read the file and save the necessary variables
			BufferedReader reader;
			reader = new BufferedReader(new FileReader(f));
			reader.readLine(); // project path
			reader.readLine(); // Hidden Directory
			reader.readLine(); // Working Directory
			String videoPath = reader.readLine();
			reader.close();
			return videoPath;

		} catch (IOException e1) {
		}

		return null;
	}

	/**
	 * playVideo Method, takes in an input string with the name of the media
	 * file, plays the file, and activates the ticker while the video is playing
	 * 
	 * @param input
	 */
	protected void playVideo(String input) {
		inputVideo = input;
		video.playMedia(inputVideo);
		playBtn.setIcon(pause);
		// Setting the timer
		Timer ticker = new Timer(200, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				/*
				 * Display the time in the format "hh:mm:ss"
				 * 
				 * @reference: http://stackoverflow.com/questions /9214786/how
				 * -to-convert-the-seconds-in-this-format-hhmmss
				 */
				int time = (int) (video.getTime());
				SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");
				TimeZone tz = TimeZone.getTimeZone("UTC");
				df.setTimeZone(tz);
				String formatedTime = df.format(new Date(time));
				if (time < video.getLength()) {
					timeDisplay.setText(formatedTime);

					// update the video slider as timer progresses
					videoSlider.setValue((int) ((double) video.getTime()
							/ video.getLength() * videoSlider.getMaximum()));
				} else {
					playBtn.setIcon(play);
				}
			}
		});
		ticker.start();
	}

	/**
	 * stopVideo Method allows other classes to stop the current played video
	 */
	protected void stopVideo() {
		if (video.isPlayable()) {

			video.stop();
			timeDisplay.setText("00:00:00");
			videoSlider.setValue(0);
			playBtn.setIcon(play);

		}
	}

	/**
	 * SkipTask class rewinds or forwards the video n a SwingWorker
	 */
	class SkipTask extends SwingWorker<Void, Void> {
		private int skipSpeed;

		// Set the skipping duration
		public SkipTask(int time) {
			this.skipSpeed = time;
		}

		/**
		 * doInBackground() performs all the long tasks so the application does
		 * not freeze
		 */
		@Override
		protected Void doInBackground() throws Exception {
			try {
				while (!isCancelled()) {
					video.skip(skipSpeed);
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			return null;
		}
	}
}
