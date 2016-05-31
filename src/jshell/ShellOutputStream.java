package jshell;

import java.io.IOException;
import java.io.OutputStream;

import javax.swing.JTextArea;

public class ShellOutputStream extends OutputStream{
	
	private JTextArea textArea;
	
	/**
	 * Constructor for output stream
	 * @param text the JTextArea to send the output to
	 */
	public ShellOutputStream(JTextArea text){
		textArea = text;
	}
	
	@Override
	public void write(int b) throws IOException {
		//write the output to the text area by appending it to text area
		textArea.append(String.valueOf((char)b));
		//move caret to last value for later writing to text area
		textArea.setCaretPosition(textArea.getDocument().getLength());
	}
}
