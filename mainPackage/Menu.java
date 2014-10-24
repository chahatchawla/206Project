package mainPackage;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JTabbedPane;
import javax.swing.filechooser.FileNameExtensionFilter;
/**
 * SoftEng206 Assignment3 - menu class
 * 
 * @author Chahat Chawla ccha504 8492142
 * 
 * Partial Code Extracted by Assignment 3 
 * @author Chahat Chawla and Zainab Al Lawati
 * 
 */

public class Menu extends JMenuBar implements ActionListener {
	private static Menu instance = new Menu();
	protected NewProject np = new NewProject();
	protected OpenProject op = new OpenProject();
	protected ImportFromFolder iff = new ImportFromFolder();
	protected Export ex = new Export();
	protected HelpTab ht = new HelpTab();

	protected JMenu fileMenu;
	protected static JMenu submenu;
	protected static JMenuItem export;
	protected JMenu helpMenu;
	protected JMenuItem newProj, openProj, fromURL, fromFolder, exit, help;

	// Initializing the image for the icons

	protected JLabel videoImage; 

	protected JLabel audioImage ;

	protected JLabel textEditImage ;
	
	protected JLabel subtitleImage; 

	protected JLabel mainImage ;
;


	protected FileNameExtensionFilter mediaFilter = new FileNameExtensionFilter( "mp3, avi & mp4 Clips", "mp3", "mp4", "avi");
	protected FileNameExtensionFilter projectFilter = new FileNameExtensionFilter( "Vamix projects", "vamix");

	protected static String workingDir = "";
	protected static String hiddenDir = "";
	protected static String projectPath = "";

	public String inputVideo;
	public String outputName;
	protected Download download;
	protected static JFrame downloadFrame = new JFrame("Download");


	/*
	 * The class constructor
	 */
	private Menu() {
		
		videoImage= new JLabel(new ImageIcon(VideoPlayer.class.getResource("Resources/video.png")));
		audioImage = new JLabel(new ImageIcon(VideoPlayer.class.getResource("Resources/audio.png")));
		subtitleImage = new JLabel(new ImageIcon(VideoPlayer.class.getResource("Resources/subtitle.png")));
		textEditImage = new JLabel(new ImageIcon(VideoPlayer.class.getResource("Resources/textEdit.png")));
		mainImage = new JLabel(new ImageIcon(VideoPlayer.class.getResource("Resources/title.png")));
		//Build the file menu.
		fileMenu = new JMenu("File");
		add(fileMenu);

		//Create a group of JMenuItems
		newProj = new JMenuItem("New project");
		fileMenu.add(newProj);
		openProj = new JMenuItem("Open project", new ImageIcon("images/middle.gif"));
		fileMenu.add(openProj);

		//Create a submenu in the file menu
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

		//Create Exit JMenuItem
		fileMenu.addSeparator();
		exit = new JMenuItem("Exit");
		fileMenu.add(exit);
		add(fileMenu);

		//Build second menu in the menu bar.
		helpMenu = new JMenu("Help");
		help = new JMenuItem("Help & Tips");
		helpMenu.add(help);
		add(helpMenu);

		//Add action listeners
		newProj.addActionListener(this);
		openProj.addActionListener(this);
		fromURL.addActionListener(this);
		fromFolder.addActionListener(this);
		export.addActionListener(this);
		exit.addActionListener(this);
		help.addActionListener(this);

	}

	public static Menu getInstance() {
		return instance;
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == newProj) {
			np.newProject();
			//-----------------------------------------------------------------------------------
		} else if (e.getSource() == openProj) {
			op.openProject();

			//-----------------------------------------------------------------------------------
			//If user chooses to import video by downloading it from web
			//Assume that the URL is for a valid mp4 video
		} else if (e.getSource() == fromURL) {
			//Create and set a new download frame
			download = new Download(projectPath, workingDir);
			downloadFrame.setContentPane(download);
			downloadFrame.setLocation(300, 300);
			downloadFrame.setSize(500, 200);
			downloadFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			downloadFrame.setVisible(true);


			//-----------------------------------------------------------------------------------
			//If user chooses to import video from local folder
		} else if (e.getSource() == fromFolder) {
			iff.importFromFolder();
			//-----------------------------------------------------------------------------------
		} else if (e.getSource() == export) {
			ex.export();

			//-----------------------------------------------------------------------------------

			//Close the frame
		} else if (e.getSource() == exit) {
			VideoPlayer.video.mute(false);
			Main.frame.dispose();
		} else if (e.getSource() == help) {

			ht.helpTab();

		} 

	}

	/**
	 * Gets the full path of the project
	 */
	public static void finishDownload() {
		downloadFrame.dispose();
	}
	/**
	 * Gets the full path of the project
	 */
	public static String getProjectPath() {
		if (!projectPath.isEmpty()) {
			return projectPath;
		} else {
			return "No file yet";
		}
	}

}
