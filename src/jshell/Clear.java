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
	}
}
