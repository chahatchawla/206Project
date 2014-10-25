package mainPackage;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 * SoftEng206 Project - menu class
 * 
 * Purpose: The purpose of this class is to create the GUI for the menu on top
 * of the screen and handle all the actions performed. This class is used in
 * mainPackage.Main.java to initialize the menu.
 * 
 * This class is a singleton, as for our application, only one instance of the
 * Menu class should be created, since the object should only be created once
 * instead of having multiple instances being created as the application
 * progresses.
 * 
 * The Menu provides the functionality to create a new project, open a project,
 * import a media file from the folder, download a media file, export the media
 * file and finally provide help and tips.
 * 
 * @author Chahat Chawla ccha504 8492142
 * 
 *         Partial Code Extracted by Assignment 3
 * @author Chahat Chawla and Zainab Al Lawati
 * 
 */

public class Menu extends JMenuBar implements ActionListener {

	// Initializing the singleton instance of this class
	private static Menu instance = new Menu();

	// Initializing all the helper classes for menu
	protected NewProject np = new NewProject();
	protected OpenProject op = new OpenProject();
	protected ImportFromFolder iff = new ImportFromFolder();
	protected Export ex = new Export();
	protected HelpTab ht = new HelpTab();

	// Initializing the JMenu components
	protected JMenu fileMenu;
	protected JMenu submenu;
	protected JMenuItem export;
	protected JMenu helpMenu;
	protected JMenuItem newProj, openProj, fromURL, fromFolder, exit, help;

	// Initializing the image for the icons
	protected JLabel videoImage;
	protected JLabel audioImage;
	protected JLabel textEditImage;
	protected JLabel subtitleImage;
	protected JLabel mainImage;;

	// Initializing the components for JFileChooser()
	protected FileNameExtensionFilter mediaFilter = new FileNameExtensionFilter(
			"mp3, avi & mp4 Clips", "mp3", "mp4", "avi");
	protected FileNameExtensionFilter projectFilter = new FileNameExtensionFilter(
			"Vamix projects", "vamix");

	// Initializing the Strings
	protected String workingDir = "";
	protected String hiddenDir = "";
	protected String projectPath = "";
	public String inputVideo;
	public String outputName;

	protected Download download;
	protected JFrame downloadFrame = new JFrame("Download");

	/**
	 * Constructor for Menu() -Sets up the GUI for the menu, sets up the default
	 * layout
	 */
	private Menu() {

		// sets the images so they are imported from the resources folder in
		// this package
		// Reference:
		// http://docs.oracle.com/javase/tutorial/uiswing/components/icon.html
		videoImage = new JLabel(new ImageIcon(
				MediaPlayer.class.getResource("Resources/video.png")));
		audioImage = new JLabel(new ImageIcon(
				MediaPlayer.class.getResource("Resources/audio.png")));
		subtitleImage = new JLabel(new ImageIcon(
				MediaPlayer.class.getResource("Resources/subtitle.png")));
		textEditImage = new JLabel(new ImageIcon(
				MediaPlayer.class.getResource("Resources/textEdit.png")));
		mainImage = new JLabel(new ImageIcon(
				MediaPlayer.class.getResource("Resources/title.png")));

		// Build the file menu.
		fileMenu = new JMenu("File");
		add(fileMenu);

		// Create a group of JMenuItems
		newProj = new JMenuItem("New project");
		fileMenu.add(newProj);
		openProj = new JMenuItem("Open project", new ImageIcon(
				"images/middle.gif"));
		fileMenu.add(openProj);

		// Create a submenu in the file menu
		fileMenu.addSeparator();
		submenu = new JMenu("Import media");
		fromURL = new JMenuItem("Download from URL");
		submenu.add(fromURL);
		fromFolder = new JMenuItem("Import from computer");
		submenu.add(fromFolder);
		submenu.setEnabled(false);
		fileMenu.add(submenu);
		export = new JMenuItem("Export video");
		export.setEnabled(false);
		fileMenu.add(export);

		// Create Exit JMenuItem
		fileMenu.addSeparator();
		exit = new JMenuItem("Exit");
		fileMenu.add(exit);
		add(fileMenu);

		// Build second menu in the menu bar.
		helpMenu = new JMenu("Help");
		help = new JMenuItem("Help & Tips");
		helpMenu.add(help);
		add(helpMenu);

		// Add action listeners
		newProj.addActionListener(this);
		openProj.addActionListener(this);
		fromURL.addActionListener(this);
		fromFolder.addActionListener(this);
		export.addActionListener(this);
		exit.addActionListener(this);
		help.addActionListener(this);

	}

	/**
	 * getInstance() returns the singleton instance of the Menu class
	 * 
	 * @return
	 */
	public static Menu getInstance() {
		return instance;
	}
	/**
	 * actionPerformed method responds to all the actions done by the user on
	 * the GUI
	 */
	@Override
	public void actionPerformed(ActionEvent e) {

		// if new project is clicked
		if (e.getSource() == newProj) {
			np.newProject();	
		} 
		// if the open project is clicked
		else if (e.getSource() == openProj) {
			op.openProject();
	
		} // If user chooses to import video by downloading it from web
		// Assume that the URL is for a valid mp4/mp3 video
		else if (e.getSource() == fromURL) {
			// Create and set a new download frame
			download = new Download(projectPath, workingDir);
			downloadFrame.setContentPane(download);
			downloadFrame.setLocation(300, 300);
			downloadFrame.setSize(500, 200);
			downloadFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			downloadFrame.setVisible(true);
		
		} 
		// If user chooses to import media from local folder
		else if (e.getSource() == fromFolder) {
			iff.importFromFolder();

		}
		// If export is clicked
		else if (e.getSource() == export) {
			ex.export();

		} 
		// If exit is clicked = close the frame
		else if (e.getSource() == exit) {
			MediaPlayer.video.mute(false);
			Main.frame.dispose();
		} 
		// If help is clicked
		else if (e.getSource() == help) {
			ht.helpTab();
		}
	}

	/**
	 * finishDownload() method gets the full path of the project
	 */
	public void finishDownload() {
		downloadFrame.dispose();
	}

	/**
	 * getProjectPath() method gets the full path of the project
	 */
	public String getProjectPath() {
		if (!projectPath.isEmpty()) {
			return projectPath;
		} else {
			return "No file yet";
		}
	}

}
