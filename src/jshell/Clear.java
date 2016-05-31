package jshell;

import javax.swing.JTextArea;

public class Clear implements Executable {

	private JTextArea outputView;

	/**
	 * Constructor for Clear
	 * @param outputView The JTextArea that will be cleared when executed
	 */
	public Clear(JTextArea outputView){
		this.outputView = outputView;
	}

	@Override
	public String getName() {
		return "clear";
	}

	@Override
	public String getAbout() {
		return getName() + " - method to clear the shell";
	}

	@Override
	public void execute(String[] currDir, String[] args) {
		//Clear output when executed by setting text to empty
		outputView.setText("");
		//Fill with blank lines to have text be at the bottom
		for(int i = 0; i < 60; i++){
			outputView.append("\n");
		}
	}
}
