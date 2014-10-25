package subtitles;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import javax.swing.JOptionPane;

/**
 * SoftEng206 Project - subtitle table class
 * 
 * Purpose: The purpose of this class is to perform all the functions related to
 * the subtitle table feature. It allows the user to input subtitle into the
 * table , delete or edit the subtitle. This class is used in
 * MainSubtitles.java.
 * 
 * @author Chahat Chawla ccha504 8492142
 * 
 *         Partial Code Extracted by Assignment 3
 * @author Chahat Chawla and Zainab Al Lawati
 * 
 */

public class SubtitleTable {

	// Reference:
	// http://docs.oracle.com/javase/tutorial/uiswing/components/table.html

	/**
	 * inputSubtitle Method is performed when the inputSubtitleButton is
	 * clicked. It adds the created subtitle to subtitle table
	 */
	protected void inputSubtitle() {

		// check if all checks for adding a subtitle pass
		boolean passedAdd = MainSubtitles.getInstance().sc.allChecksAdd();

		// if they pass
		if (passedAdd) {
			// Append a row to the subtitle table using the information provided
			// by the user
			MainSubtitles.getInstance().model.addRow(new Object[] {
					MainSubtitles.getInstance().text.getText(),
					MainSubtitles.getInstance().startTime.getText(),
					MainSubtitles.getInstance().endTime.getText() });
		}

	}

	/**
	 * deleteSubtitle Method is performed when the deleteSubtitleButton is
	 * clicked. It deletes a selected subtitle from the table
	 */
	protected void deleteSubtitle() {

		// check if the table is empty
		if (MainSubtitles.getInstance().model.getRowCount() == 0) {
			JOptionPane.showMessageDialog(null,
					"ERROR: subtitle table is empty, cannot perform DELETE");
		}
		// if it is not empty
		else {

			// check if something is selected in the table
			if (MainSubtitles.getInstance().subtitlesTable.getSelectedRow() != -1) {
				// Delete a row selected by the user from the subtitle table
				int deleteRow = MainSubtitles.getInstance().subtitlesTable
						.getSelectedRow();
				MainSubtitles.getInstance().model.removeRow(deleteRow);

			}
			// if nothing is selected, prompt the user and allow them to
			// select a subtitle from the table
			else {
				JOptionPane
						.showMessageDialog(null,
								"ERROR: please select an subtitle from the table to delete");
			}
		}
	}

	/**
	 * editSubtitle Method is performed when the editSubtitleButton is clicked.
	 * It allows the user to edit a selected subtitle from the table
	 */
	protected void editSubtitle() {
		// check if the table is empty
		if (MainSubtitles.getInstance().model.getRowCount() == 0) {
			JOptionPane.showMessageDialog(null,
					"ERROR: subtitle table is empty, cannot perform EDIT");
		}
		// if it is not empty
		else {
			// check if something is selected in the table
			if (MainSubtitles.getInstance().subtitlesTable.getSelectedRow() != -1) {
				// All the user to edit the selected row by putting the subtitle
				// from the selected row into the user input fields
				int editRow = MainSubtitles.getInstance().subtitlesTable
						.getSelectedRow();
				MainSubtitles.getInstance().text.setText((String) MainSubtitles
						.getInstance().model.getValueAt(editRow, 0));
				MainSubtitles.getInstance().startTime
						.setText((String) MainSubtitles.getInstance().model
								.getValueAt(editRow, 1));
				MainSubtitles.getInstance().endTime
						.setText((String) MainSubtitles.getInstance().model
								.getValueAt(editRow, 2));
				MainSubtitles.getInstance().model.removeRow(editRow);

			}
			// if nothing is selected, prompt the user and allow them to
			// select a subtitle from the table
			else {
				JOptionPane
						.showMessageDialog(null,
								"ERROR: please select an subtitle from the table to edit");
			}
		}

	}

	/**
	 * generateSubtitle Method is performed when the generateButton is clicked.
	 * It allows the user to generate a .srt file into the project directory if
	 * all checks are passed
	 */
	protected void generateSubtitle() {

		// check if all checks for generating a .srt subtitle file pass
		boolean passedGenerate = MainSubtitles.getInstance().sc
				.allChecksGenerate();

		// if all checks are passed
		if (passedGenerate) {

			//Reference: 
			//http://docs.oracle.com/javase/7/docs/api/java/io/BufferedWriter.html
			FileWriter fw;
			
			// create the .srt file 
			File f = new File(MainSubtitles.getInstance().workingDir + "/"
					+ MainSubtitles.getInstance().outputFileName.getText()
					+ ".srt");
			try {
				fw = new FileWriter(f, false);
				BufferedWriter bw = new BufferedWriter(fw);
				PrintWriter x = new PrintWriter(bw);

				// add each subtitle 
				for (int i = 0; i < MainSubtitles.getInstance().model
						.getRowCount(); i++) {

					x.println(i + 1);
					x.println(MainSubtitles.getInstance().model
							.getValueAt(i, 1)
							+ " --> "
							+ MainSubtitles.getInstance().model
									.getValueAt(i, 2));
					x.println(MainSubtitles.getInstance().model
							.getValueAt(i, 0));

					x.println("");
				}

				bw.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}

			// inform the user that the .srt file has been generated 
			JOptionPane
					.showMessageDialog(
							null,
							MainSubtitles.getInstance().outputFileName
									.getText()
									+ ".srt has been generated in the project directory!");
			// refresh srtCheck
			MainSubtitles.getInstance().srtCheck.setSelected(false);
			MainSubtitles.getInstance().startTime.setText("hh:mm:ss,mmm");
			MainSubtitles.getInstance().endTime.setText("hh:mm:ss,mmm");
			MainSubtitles.getInstance().text.setText("");
			MainSubtitles.getInstance().outputFileName.setText("");
			int listRow = MainSubtitles.getInstance().model.getRowCount();
			for (int i = 0; i < listRow; i++) {
				MainSubtitles.getInstance().model.removeRow(0);
			}

		}
	}

}
