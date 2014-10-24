package mainPackage;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import audioManipulator.MainAudioManipulator;

import com.sun.jna.platform.FileUtils;

public class NewProject {
	protected void newProject(){

		//Refresh the project
		Menu.getInstance().export.setEnabled(false);
		
		Main.tabbedPane.setEnabled(false);
		Main.apf.enableAudioMan(false);
		Main.vpf.enableVideoMan(false);
		Main.tpf.enableTextEdit(false);
		Main.spf.enableSubtitle(false);
		
		Main.vpf.refreshVideoMan();
		Main.apf.refreshAudioMan();
		Main.tpf.refreshtextEdit();
		Main.tpf.refreshTitleScreen();
		Main.tpf.refreshCreditScreen();
		Main.spf.refreshSubtitles();
		
		//Stop the played video
		VideoPlayer.getInstance().stopVideo();
		Menu.getInstance().inputVideo = null;

		int i = -1;
		while (i<0) {	//Loop until project is created successfully or cancelled

			//Take the project name as an input from the user
			String projectName = JOptionPane.showInputDialog(
					null,
					"Project name: ",
					"Create new project",
					JOptionPane.DEFAULT_OPTION);
			if (projectName == null) {
				//Cancel is pressed, go back to main frame
				i++;

			} else if (projectName.isEmpty()) {
				JOptionPane.showMessageDialog(null, "Please enter the project's name!");

			} else {
				/*
				 * Create a matcher to check that the output name has no space in middle
				 * @reference: http://stackoverflow.com/questions/4067809/how-to-check-space-in-string
				 */
				Pattern pattern = Pattern.compile("\\s");
				Matcher matcher = pattern.matcher(projectName);
				boolean found = matcher.find();

				if (found) {
					JOptionPane.showMessageDialog(null, "Sorry, project name cannot contain spaces.");

				} else {
					//Allow the user to choose the project file destination and set it as his working directory
					JFileChooser dirChooser = new JFileChooser();
					dirChooser.setDialogTitle("Choose Working Directory.");
					dirChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
					int returnVal = dirChooser.showSaveDialog(null);
					//If no directory was selected, don't go back to main frame
					if(returnVal != JFileChooser.APPROVE_OPTION) {
						return;
					}

					//Get the name of the chosen directory
					Menu.getInstance().workingDir = dirChooser.getSelectedFile().getPath()+"/"+projectName;
					//Create the working directory
					File workDir = new File(Menu.getInstance().workingDir);
					//workDir.mkdir();

					//Check that the project doesn't exists in the specified directory
					String outputFileName = Menu.getInstance().workingDir+"/"+projectName+".vamix";
					File f = new File(outputFileName);
					Menu.getInstance().hiddenDir = Menu.getInstance().workingDir+"/."+projectName+".vamix";
					File hidden = new File(Menu.getInstance().hiddenDir);
					
					
					
					if (workDir.exists()) {
						
						//Allow user to choose either overwriting the current project or cancel creating new project
						Object[] existOptions = {"Overwrite", "Cancel"};
						int optionChosen = JOptionPane.showOptionDialog(null, "Project already exists. " +
								"Do you want to overwrite the existing project?",
								"Project Exists!", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE,
								null,existOptions, existOptions[0]);
												
						if (optionChosen == 0) { //if overwrite, delete the existing file and hidden directory
							
							String cmd = "rm -r " + Menu.getInstance().workingDir;
							ProcessBuilder builder = new ProcessBuilder("/bin/bash", "-c",
									cmd);
							Process process;
							try {
								process = builder.start();
							} catch (IOException e1) {
								e1.printStackTrace();
							}
							
							try {
								workDir.mkdir();
								//Create the main project file
								f.createNewFile();
								//Create the hidden directory that holds all the results of the intermediate processes
								hidden.mkdir();
							
								//Write the hidden directory and the working directory to the main project file
								FileWriter fw = new FileWriter(f);
								BufferedWriter bw = new BufferedWriter(fw);
								Menu.getInstance().projectPath = f.getPath();
								bw.write(Menu.getInstance().projectPath); //Store the project path
								bw.newLine();
								bw.write(hidden.toString()); //Store the hidden folder path
								bw.newLine();
								bw.write(Menu.getInstance().workingDir); //Store the working directory
								bw.newLine();
								bw.close();

								//When project is created successfully, enable importing media
								Menu.getInstance().submenu.setEnabled(true);

							} catch (IOException e1) {
								e1.printStackTrace();
							}
				
							i++;

						} else { //Cancel creating new project
							return;
						}
					}
					i++; //to exit the while loop

					try {
						
						workDir.mkdir();
						//Create the main project file
						f.createNewFile();
						//Create the hidden directory that holds all the results of the intermediate processes
						hidden.mkdir();

						//Write the hidden directory and the working directory to the main project file
						FileWriter fw = new FileWriter(f);
						BufferedWriter bw = new BufferedWriter(fw);
						Menu.getInstance().projectPath = f.getPath();
						bw.write(Menu.getInstance().projectPath); //Store the project path
						bw.newLine();
						bw.write(hidden.toString()); //Store the hidden folder path
						bw.newLine();
						bw.write(Menu.getInstance().workingDir); //Store the working directory
						bw.newLine();
						bw.close();

						//When project is created successfully, enable importing media
						Menu.getInstance().submenu.setEnabled(true);

					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}
			}
		}
	}

}
