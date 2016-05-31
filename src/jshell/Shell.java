package jshell;

import java.awt.BorderLayout;
import java.awt.Frame;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Observable;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.text.DefaultCaret;

public class Shell extends Observable{
	//Window specs
	private static final int FRAME_WIDTH = 600;
	private static final int FRAME_HEIGHT = 600;
	
	//Shell Version
	private static final String VERSION = "1.0";

	//private members
	private FileSystem fileSys;
	private JFrame shellFrame;
	private JScrollPane outputScroll;
	private JTextField inputText;
	private PrintStream printStream;
	private JTextArea outputText;
	private String[] currDir;
	private Boolean isExecuting = false;
	private Vi textEditor;
	private ArrayList<Executable> validCommands;

	/**
	 * Constructor to create and show shell
	 */
	public Shell(){

		//Initialize GUI
		shellFrame = new JFrame();
		JPanel shellPanel = new JPanel();
		shellPanel.setLayout(new BorderLayout());

		//Create and add output text field
		outputText = new JTextArea();
		for(int i = 0; i < 60; i++){
			//Fill with blank lines to have text be at the bottom of console
			outputText.append("\n");
		}

		outputText.setEditable(false);//Don't allow previous commands to be modified
		outputText.setLineWrap(true);
		outputText.addKeyListener(new KeyListener(){

			@Override
			public void keyTyped(KeyEvent e) {	
			}

			@Override
			public void keyPressed(KeyEvent e) {	
			}

			@Override
			public void keyReleased(KeyEvent e) {
				//check if escape was pressed and currently running text editor
				if(e.getKeyCode() == KeyEvent.VK_ESCAPE&&textEditor.isExecuting()){
					//should not be editable - escape editing mode
					textEditor.setEditable(false);
				}
				else if(e.getKeyCode() == KeyEvent.getExtendedKeyCodeForChar('i')&&textEditor.isExecuting()){
					//should enter insert mode
					textEditor.setEditable(true);	
				}
				//Notify text editor
				setChanged();
				notifyObservers();	
			}
		});
		
		outputScroll  = new JScrollPane(outputText);//let user scroll through previous commands
		shellPanel.add(outputScroll,BorderLayout.CENTER);

		//Autoscroll to the bottom
		DefaultCaret caret = (DefaultCaret) outputText.getCaret();
		caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);

		//Create input text area
		inputText = new JTextField();//use text field to only allow one line

		//Add listeners to inputText
		//set input to react to clear input window if running the text editor (for ease when typing in file editor and want to enter command)
		inputText.addFocusListener(new FocusListener(){

			@Override
			public void focusGained(FocusEvent e) {
				if(textEditor.isExecuting()){
					//clear contents
					inputText.setText("");
				}
			}

			@Override
			public void focusLost(FocusEvent e) {
			}	
		});
		
		inputText.addKeyListener(new KeyListener(){
			public void keyPressed(KeyEvent e) { 
				if(textEditor.isExecuting()==false){
					//Text editor will close
					isExecuting = false;
				}
				if(e.getKeyCode() == KeyEvent.VK_ENTER){
					if(isExecuting == false){// Only send execute when not already executing
						//Enter has been pressed. Execute the command entered.    		
						String formattedInput = inputText.getText();
						inputText.setText("");
						formattedInput.replace("\n", "");//Get rid of new line
						System.out.println("");//Print empty string for formatting
						System.out.print(">"+formattedInput);
						
						execute(formattedInput);
					}else{
						//Already executing
						setChanged();
						notifyObservers();
					}
				}
			}

			public void keyReleased(KeyEvent e) { 
				if(e.getKeyCode() == KeyEvent.VK_ENTER){
					//clear once command has been sent
					inputText.setText("");
				}
			}

			public void keyTyped(KeyEvent e) { /* ... */ }
		});


		//Add to GUI
		shellPanel.add(inputText,BorderLayout.SOUTH);
		shellFrame.add(shellPanel);

		//Create print stream to send shell output to
		printStream = new PrintStream(new ShellOutputStream(outputText));

		//Setup file system
		fileSys = new FileSystem("~",printStream);
		currDir = new String[1];
		currDir[0]=fileSys.getRoot();

		//Add commands
		validCommands= new ArrayList<Executable>();
		validCommands.add(new Mkdir(fileSys));
		validCommands.add(new Cd(fileSys));
		validCommands.add(new Ls(fileSys));
		validCommands.add(new Pwd());
		textEditor = new Vi(outputText,inputText,fileSys);
		validCommands.add(textEditor);
		this.addObserver(textEditor);
		validCommands.add(new Clear(outputText));
		validCommands.add(new More(fileSys));

		//Redirect system output to our shell
		System.setOut(printStream);
		
		//Print Welcome
		System.out.println("Welcome to JShell. Version: " + VERSION + ". Possible commands are:");
		for(Executable com : validCommands){
			System.out.println(com.getAbout());
		}
		System.out.print("System is ready");

		//Handle multiple windows when closing
		shellFrame.addWindowListener(new WindowListener(){

			@Override
			public void windowOpened(WindowEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void windowClosing(WindowEvent e) {
				int shouldClose = 0;
				for(Frame f : Frame.getFrames()){
					if(f.isVisible()==true){
						shouldClose++;
					}
				}

				if(shouldClose<=1){
					System.exit(0);
				}
				shellFrame.dispose();

			}

			@Override
			public void windowClosed(WindowEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void windowIconified(WindowEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void windowDeiconified(WindowEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void windowActivated(WindowEvent e) {
				//Set window to active output when interacting with window
				System.setOut(printStream);

			}

			@Override
			public void windowDeactivated(WindowEvent e) {
				// TODO Auto-generated method stub

			}

		});
		
		//Set frame specs
		shellFrame.setSize(FRAME_WIDTH, FRAME_HEIGHT);
		shellFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		shellFrame.setVisible(true);

	}

	/**
	 * Method to return shell file system
	 * @return file sytem the shell uses
	 */
	public FileSystem getFileSystem(){
		return fileSys;
	}

	/**
	 * Method to execute executables on the shell
	 * @param command the command that should be executed
	 */
	private void execute(String command){
		isExecuting = true;
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
					//Execute the command
					com.execute(currDir, params);
					knownCommand=true;
					break;
				}
			}
			if(knownCommand==false){
				//Unknown command
				System.out.print("\nUnrecognized command: "+command);
			}
			if(textEditor.isExecuting()==true){//If command is VI, it is still executing until editor is closed
				//otherwise, signal that execution is finished after loop
				isExecuting = true;
			}else{
				isExecuting = false;
			}
		}
	}
}

