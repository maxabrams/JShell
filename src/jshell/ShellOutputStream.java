package jshell;

import java.io.IOException;
import java.io.OutputStream;

import javax.swing.JTextArea;

public class ShellOutputStream extends OutputStream{
	private JTextArea textArea;
	
	public ShellOutputStream(JTextArea text){
		textArea = text;
	}
	
	@Override
	public void write(int b) throws IOException {
		textArea.append(String.valueOf((char)b));
		textArea.setCaretPosition(textArea.getDocument().getLength());
	}

}
