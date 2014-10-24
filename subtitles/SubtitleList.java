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

import textEditor.MainTextEditor;


public class SubtitleList {

	// If the inputOverlayButton is clicked
	protected void inputSubtitle(){

		boolean passedAdd = MainSubtitles.getInstance().sc.allChecksAdd();

		if (passedAdd){
			// Append a row 
			MainSubtitles.getInstance().model.addRow(new Object[]{MainSubtitles.getInstance().text.getText(), MainSubtitles.getInstance().startTime.getText(), MainSubtitles.getInstance().endTime.getText()});
		}

	}

	// If the deleteSubtitle is clicked
	protected void deleteSubtitle(){
		// Delete a row
		int deleteRow = MainSubtitles.getInstance().subtitlesTable.getSelectedRow();
		MainSubtitles.getInstance().model.removeRow(deleteRow);

	}

	// If the editSubtitle is clicked
	protected void editSubtitle(){

		// Edit a row
		int editRow = MainSubtitles.getInstance().subtitlesTable.getSelectedRow();
		MainSubtitles.getInstance().text.setText((String) MainSubtitles.getInstance().model.getValueAt(editRow, 0));
		MainSubtitles.getInstance().startTime.setText((String) MainSubtitles.getInstance().model.getValueAt(editRow, 1));
		MainSubtitles.getInstance().endTime.setText((String) MainSubtitles.getInstance().model.getValueAt(editRow, 2));
		MainSubtitles.getInstance().model.removeRow(editRow);

	}

	// If the generate is clicked
	protected void generateSubtitle(){

		boolean passedGenerate = MainSubtitles.getInstance().sc.allChecksGenerate();

		if (passedGenerate){

			//allchecks
			FileWriter fw;

			File f = new File(MainSubtitles.getInstance().workingDir + "/" + MainSubtitles.getInstance().outputFileName.getText()
					+ ".srt");

			try {
				fw = new FileWriter(f, false);
				BufferedWriter bw = new BufferedWriter(fw);
				PrintWriter x = new PrintWriter(bw);


				for (int i = 0; i < MainSubtitles.getInstance().model.getRowCount(); i++){

					x.println(i+1);
					x.println(MainSubtitles.getInstance().model.getValueAt(i, 1) + " --> " + MainSubtitles.getInstance().model.getValueAt(i, 2));
					x.println(MainSubtitles.getInstance().model.getValueAt(i, 0));

					x.println("");
				}


				bw.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			
			JOptionPane
			.showMessageDialog(null,
					MainSubtitles.getInstance().outputFileName.getText() + ".srt has been generated in the project directory!");
			MainSubtitles.getInstance().srtCheck.setSelected(false);
			MainSubtitles.getInstance().startTime.setText("hh:mm:ss,mmm");
			MainSubtitles.getInstance().endTime.setText("hh:mm:ss,mmm");
			MainSubtitles.getInstance().text.setText("");
			MainSubtitles.getInstance().outputFileName.setText("");
			
			int listRow = MainSubtitles.getInstance().model.getRowCount();
			for (int i = 0; i < listRow; i++){
				MainSubtitles.getInstance().model.removeRow(0);
			}
			
		}
	}
		

}
