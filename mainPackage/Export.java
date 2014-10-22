package mainPackage;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JOptionPane;

public class Export {
	protected void export(){
		int i = -1;
		while (i<0) {	//Loop until the user provides a valid output name or cancels exporting
			//Take the output file name as an input from the user
			String outputName = JOptionPane.showInputDialog(null,
					"Please choose the output name for the final .mp4 file: ", 
					"Export project",
					JOptionPane.DEFAULT_OPTION);

			if (outputName == null) { //Cancel is pressed, go back to main frame
				i++;

			} else if (outputName.isEmpty()) {
				JOptionPane.showMessageDialog(null, "Sorry, output file name cannot be empty!");

			} else {
				/*
				 * Create a matcher to check that the output name has no space in middle
				 * @reference: http://stackoverflow.com/questions/4067809/how-to-check-space-in-string
				 */
				Pattern pattern = Pattern.compile("\\s");
				Matcher matcher = pattern.matcher(outputName);
				boolean found = matcher.find();

				if (found) {
					JOptionPane.showMessageDialog(null, "Sorry, output file name cannot contain spaces.");
				} else {

					//Check that the file doesn't exists in the specified directory
					String outputFileName = Menu.getInstance().workingDir+"/"+outputName+".mp4";
					File f = new File(outputFileName);
					if ( f.exists()) {
						//Allow user to choose either overwriting the existing file or cancel exporting
						Object[] existOptions = {"Cancel", "Overwrite"};
						int optionChosen = JOptionPane.showOptionDialog(null, "File already exists." +
								"Do you want to overwrite the existing media file?",
								"File Exists!", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE,
								null,existOptions, existOptions[0]);

						if (optionChosen == 1) { //if overwrite, delete the existing file
							f.delete();
							i++;

						} else { //Cancel exporting project
							return;
						}
					}
					i++; //to exit the while loop


					//Declare exit status variables
					int textExitStatus = 0;
					int audioExitStatus = 0;
					int videoExitStatus = 0;
					
					
					StringBuilder finalCmd = new StringBuilder(); 
							
					//Get the video path
					String videoPath = "";
					File f1 = new File(Menu.getInstance().projectPath);

					try {
						//Read the file and save the necessary variables
						BufferedReader reader;
						reader = new BufferedReader(new FileReader(f1));
						reader.readLine();
						reader.readLine();
						reader.readLine();
						videoPath = reader.readLine();
						reader.close();
					} catch (IOException e1) {
						e1.printStackTrace();
					}

					//Check what edits were performed previously

					File f2 = new File(Menu.getInstance().workingDir+"/.videoFields");
					File f3 = new File(Menu.getInstance().workingDir+"/.audioFields");
					File f4 = new File(Menu.getInstance().workingDir+"/.creditFields");
					File f5 = new File(Menu.getInstance().workingDir+"/.titleFields");



					//If no edits were done in the project do not export
					if (!f2.exists() && !f3.exists() && !(f4.exists()||f5.exists()) ) {
						JOptionPane.showMessageDialog(null, "Please save the changes before exporting");
						return;
					} else if (f2.exists() && !f3.exists() && !(f4.exists()||f5.exists()) ) { //User did video manipulation only
						finalCmd.append(Main.videoMan.makeCommand(videoPath, outputFileName));

					} else if (!f2.exists() && f3.exists() && !(f4.exists()||f5.exists()) ) { //User did audio manipulation only
						finalCmd.append(Main.audioMan.makeCommand(videoPath, outputFileName));

					} else if (!f2.exists() && !f3.exists() && (f4.exists()||f5.exists())) { //User did text editing only
						finalCmd.append(Main.textEdit.makeCommand(videoPath, outputFileName));

					} 
					//User did video manipulation and text editing 
					else if (f2.exists() && !f3.exists() && (f4.exists()||f5.exists()) ) { 
						finalCmd.append(Main.videoMan.makeCommand(videoPath, Menu.getInstance().hiddenDir+"/temp.mp4"));
						finalCmd.append(Main.textEdit.makeCommand(Menu.getInstance().hiddenDir+"/temp.mp4", outputFileName));
					}

					//User did audio manipulation and text editing 
					else if (!f2.exists() && f3.exists() && (f4.exists()||f5.exists()) ) { 
						finalCmd.append( Main.audioMan.makeCommand(videoPath, Menu.getInstance().hiddenDir+"/temp.mp4"));
						finalCmd.append(Main.textEdit.makeCommand(Menu.getInstance().hiddenDir+"/temp.mp4", outputFileName));
					}

					//User did video manipulation and audio manipulation
					else if (f2.exists() && f3.exists() && !(f4.exists()||f5.exists()) ) { 
						finalCmd.append(Main.videoMan.makeCommand(videoPath, Menu.getInstance().hiddenDir+"/temp.mp4"));
						finalCmd.append(Main.audioMan.makeCommand(Menu.getInstance().hiddenDir+"/temp.mp4", outputFileName));
					}
					//User did video manipulation and audio manipulation and text editing 
					else {
						finalCmd.append(Main.videoMan.makeCommand(videoPath, Menu.getInstance().hiddenDir+"/temp.mp4"));
						finalCmd.append(Main.audioMan.makeCommand(Menu.getInstance().hiddenDir+"/temp.mp4", Menu.getInstance().hiddenDir+"/temp2.mp4"));
						finalCmd.append(Main.textEdit.makeCommand(Menu.getInstance().hiddenDir+"/temp2.mp4", outputFileName));
					}

					
					System.out.println(finalCmd.toString());
					ExportBackGroundTask longTask = new ExportBackGroundTask(finalCmd.toString());
					longTask.execute();

				}
			}
		}
		
	}
}
