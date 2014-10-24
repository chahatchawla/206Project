package mainPackage;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

public class ImportFromFolder {
	protected void importFromFolder(){

		JFileChooser chooser = new JFileChooser();
		chooser.setFileFilter(Menu.getInstance().mediaFilter); //Show only correct extension type file by default
		int returnVal = chooser.showOpenDialog(null);

		//Store the chosen file as an input video
		if(returnVal == JFileChooser.APPROVE_OPTION) {
			Menu.getInstance().inputVideo = chooser.getSelectedFile().toString();

			File file = new File(Menu.getInstance().inputVideo);
			Path path = file.toPath();
			String type = "";
			try {
				type = Files.probeContentType(path);
			} catch (IOException f) {
				f.printStackTrace();
			}

			// if the file is NOT an audio or a video or audio file, notify the user and
			// allow them to import again
			if (!(type.equals("audio/mpeg")) && !((type.split("/")[0]).equals("video"))){
				JOptionPane.showMessageDialog(null, "ERROR: file imported does not" +
						"refer to a valid audio or video file. Please select a new input file!");
				return;
			} else if (type.equals("audio/mpeg")) { //If the file is an audio only, enable extracting only
				Main.tabbedPane.setEnabled(true);
				Main.apf.enableExtractOnly();
				Main.tpf.enableTextEdit(false);
				Main.vpf.enableVideoMan(false);
				Main.spf.enableSubtitle(false);


			} else { //2> If it is a video enable all the editing options
				Main.tabbedPane.setEnabled(true);
				Main.vpf.enableVideoMan(true);
				Main.apf.enableAudioMan(true);
				Main.tpf.enableTextEdit(true);
				Main.spf.enableSubtitle(true);
			}

			Menu.getInstance().export.setEnabled(true);
			Menu.getInstance().submenu.setEnabled(false);

		} else { //No media was imported
			return;
		}

		/*
		 * Store the name, directory and length of the input video in a txt file
		 */
		try {
			//Open the video project file
			File f = new File(Menu.getInstance().projectPath);
			FileWriter fw = new FileWriter(f, true);
			BufferedWriter bw = new BufferedWriter(fw);

			//Store the video info in the txt file
			File videoFile = new File(Menu.getInstance().inputVideo);
			bw.write(videoFile.getPath());
			bw.newLine();

			//Get the length of the video
			String cmd = "avprobe -loglevel error -show_streams " + videoFile.getPath() + " | grep -i duration | cut -f2 -d=";
			ProcessBuilder builder = new ProcessBuilder("/bin/bash", "-c", cmd);
			builder.redirectErrorStream(true);
			Process process = builder.start();

			//read the output of the process
			InputStream outStr = process.getInputStream();
			BufferedReader stdout = new BufferedReader(new InputStreamReader(outStr));

			//Write the length of the input video
			bw.write(stdout.readLine());
			bw.newLine();
			bw.close();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}
}
