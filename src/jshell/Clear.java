package jshell;

import javax.swing.JTextArea;

public class Clear implements Executable {

	private JTextArea outputView;
	
	public Clear(JTextArea outputView){
		this.outputView = outputView;
	}
	
	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "clear";
	}

	@Override
	public String getAbout() {
		// TODO Auto-generated method stub
		return getName() + " - method to clear the shell";
	}

	@Override
	public void execute(String[] currDir, String[] args) {
		outputView.setText("");

	}

}
