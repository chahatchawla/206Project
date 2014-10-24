package subtitles;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.SwingWorker;

import textEditor.TextEditor;


public class SubtitleList {

	// If the inputOverlayButton is clicked
	protected void inputSubtitle(){
		// Append a row 
		Subtitles.getInstance().model.addRow(new Object[]{Subtitles.getInstance().text.getText(), Subtitles.getInstance().startTime.getText(), Subtitles.getInstance().endTime.getText()});
	}

	// If the deleteSubtitle is clicked
	protected void deleteSubtitle(){
		// Delete a row
		int deleteRow = Subtitles.getInstance().subtitlesTable.getSelectedRow();
		Subtitles.getInstance().model.removeRow(deleteRow);

	}

	// If the playSubtitle is clicked
	protected void playSubtitle(){

	}

	// If the generate is clicked
	protected void generateSubtitle(){

		//allchecks
		FileWriter fw;

		File f = new File(Subtitles.getInstance().outputFileName.getText() + ".srt");
		
		try {
			fw = new FileWriter(f, false);
			BufferedWriter bw = new BufferedWriter(fw);
			PrintWriter x = new PrintWriter(bw);


			for (int i = 0; i < Subtitles.getInstance().model.getRowCount(); i++){

				System.out.println(i+1);
				x.println(i+1);
				System.out.println(Subtitles.getInstance().model.getValueAt(i, 1) + " --> " + Subtitles.getInstance().model.getValueAt(i, 2));
				System.out.println(Subtitles.getInstance().model.getValueAt(i, 0));
				x.println(Subtitles.getInstance().model.getValueAt(i, 1) + " --> " + Subtitles.getInstance().model.getValueAt(i, 2));
				x.println(Subtitles.getInstance().model.getValueAt(i, 0));

				
				x.println("");
			}


			bw.close();
		} catch (IOException e1) {
			e1.printStackTrace();
		}


	}

}
