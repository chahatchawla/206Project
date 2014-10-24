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

		boolean passedAdd = Subtitles.getInstance().sc.allChecksAdd();

		if (passedAdd){
			// Append a row 
			Subtitles.getInstance().model.addRow(new Object[]{Subtitles.getInstance().text.getText(), Subtitles.getInstance().startTime.getText(), Subtitles.getInstance().endTime.getText()});
		}

	}

	// If the deleteSubtitle is clicked
	protected void deleteSubtitle(){
		// Delete a row
		int deleteRow = Subtitles.getInstance().subtitlesTable.getSelectedRow();
		Subtitles.getInstance().model.removeRow(deleteRow);

	}

	// If the editSubtitle is clicked
	protected void editSubtitle(){

		// Edit a row
		int editRow = Subtitles.getInstance().subtitlesTable.getSelectedRow();
		Subtitles.getInstance().text.setText((String) Subtitles.getInstance().model.getValueAt(editRow, 0));
		Subtitles.getInstance().startTime.setText((String) Subtitles.getInstance().model.getValueAt(editRow, 1));
		Subtitles.getInstance().endTime.setText((String) Subtitles.getInstance().model.getValueAt(editRow, 2));
		Subtitles.getInstance().model.removeRow(editRow);

	}

	// If the generate is clicked
	protected void generateSubtitle(){

		boolean passedGenerate = Subtitles.getInstance().sc.allChecksGenerate();

		if (passedGenerate){

			//allchecks
			FileWriter fw;

			File f = new File(Subtitles.getInstance().workingDir + "/" + Subtitles.getInstance().outputFileName.getText()
					+ ".srt");

			try {
				fw = new FileWriter(f, false);
				BufferedWriter bw = new BufferedWriter(fw);
				PrintWriter x = new PrintWriter(bw);


				for (int i = 0; i < Subtitles.getInstance().model.getRowCount(); i++){

					x.println(i+1);
					x.println(Subtitles.getInstance().model.getValueAt(i, 1) + " --> " + Subtitles.getInstance().model.getValueAt(i, 2));
					x.println(Subtitles.getInstance().model.getValueAt(i, 0));

					x.println("");
				}


				bw.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			
			JOptionPane
			.showMessageDialog(null,
					Subtitles.getInstance().outputFileName.getText() + ".srt has been generated in the project directory!");
			Subtitles.getInstance().srtCheck.setSelected(false);
			Subtitles.getInstance().startTime.setText("hh:mm:ss,mmm");
			Subtitles.getInstance().endTime.setText("hh:mm:ss,mmm");
			Subtitles.getInstance().text.setText("");
			Subtitles.getInstance().outputFileName.setText("");
			
			int listRow = Subtitles.getInstance().model.getRowCount();
			for (int i = 0; i < listRow; i++){
				Subtitles.getInstance().model.removeRow(0);
			}
			
		}
	}
		

}
