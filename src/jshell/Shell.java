package jshell;

import java.awt.BorderLayout;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.PrintStream;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.WindowConstants;
import javax.swing.text.DefaultCaret;

public class Shell{
	private static final int FRAME_WIDTH = 600;
	private static final int FRAME_HEIGHT = 600;
	private static final String VERSION = "1.0";

	FileSystem fileSys;

	private JFrame shellFrame;
	JScrollPane outputScroll;
	private JTextField inputText;
	private PrintStream printStream;

	ArrayList<Executable> validCommands;

	public Shell(){


		//Initialize GUI
		shellFrame = new JFrame();
		JPanel shellPanel = new JPanel();
		shellPanel.setLayout(new BorderLayout());

		//Create and add output text field
		JTextArea outputText = new JTextArea();
		for(int i = 0; i < 60; i++){
			//Fill with blank lines to have text be at the bottom
			outputText.append("\n");
		}

		outputText.setEditable(false);//Dont allow previous commands to be modified
		outputText.setLineWrap(true);
		outputScroll  = new JScrollPane(outputText);//let user scroll through previous commands
		shellPanel.add(outputScroll,BorderLayout.CENTER);


		//Autoscroll to the bottom
		DefaultCaret caret = (DefaultCaret) outputText.getCaret();
		caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);

		//Create input text area
		inputText = new JTextField();//use text field to only allow one line

		//set input to react
		inputText.addKeyListener(new KeyListener(){
			public void keyPressed(KeyEvent e) { 
				if(e.getKeyCode() == KeyEvent.VK_ENTER){
					//Enter has been pressed. Execute the command entered.    		
					String formattedInput = inputText.getText();
					inputText.setText("");
					formattedInput.replace("\n", "");//Get rid of new line
					System.out.println("");//Print empty string for formatting
					System.out.print(formattedInput);

					execute(formattedInput);

				}
			}

			public void keyReleased(KeyEvent e) { 
				if(e.getKeyCode() == KeyEvent.VK_ENTER){
					inputText.setText("");
				}
			}

			public void keyTyped(KeyEvent e) { /* ... */ }
		});


		shellPanel.add(inputText,BorderLayout.SOUTH);

		shellFrame.add(shellPanel);

		//Redirect system output to our shell

		printStream = new PrintStream(new ShellOutputStream(outputText));

		//Setup filesystem
		fileSys = new FileSystem("JShellHome",printStream);

		//Add commands
		validCommands= new ArrayList<Executable>();
		validCommands.add(new Mkdir(fileSys));
		validCommands.add(new Cd(fileSys));

		System.setOut(printStream);
		System.out.println("Welcome to JShell. Possible commands are:...");
		for(Executable com : validCommands){
			System.out.println(com.getAbout());
		}
		System.out.print("System is ready");

		//fileSys.createFile("JShellHome/newfile.txt");
		shellFrame.setSize(FRAME_WIDTH, FRAME_HEIGHT);
		shellFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		shellFrame.setVisible(true);

	}

	public FileSystem getFileSystem(){
		return fileSys;
	}

	private void execute(String command){
		String args[] = command.split(" ");
		if(args.length==0){
			//no command to execute

		}else{
			boolean knownCommand = false;
			for(Executable com:validCommands){
				if(com.getName().equals(args[0])){
					//Must be a known command, so let it be executed with parameters
					String params[] = new String[args.length-1];
					//Copy string info into passed param array
					//Don't need to copy command identifier
					for(int i=1; i< args.length;i++){
						params[i-1]=args[i];
					}
					com.execute(params);
					knownCommand=true;
					break;
				}
			}
			if(knownCommand==false){
				System.out.print("\nUnrecognized command: "+command);
			}
		}

	}

}
