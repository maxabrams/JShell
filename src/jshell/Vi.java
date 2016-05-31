package jshell;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JTextArea;
import javax.swing.JTextField;

public class Vi implements Executable, Observer{

	//Members of the text editor
	private JTextArea outputView;
	private JTextField inputText;
	String prevText;
	FileSystem sys;
	File editFile;
	boolean isCreated;
	String currDir;
	String args;
	boolean isEditable;
	boolean isExecuting;
	String editsMade;
	boolean notifyInsert;
	boolean notifyVisual;

	/**
	 * Constructor for text editor
	 * @param window the window to edit in
	 * @param commandLine the field to listen for commands on
	 * @param sys the file system to manipulate files on
	 */
	public Vi(JTextArea window, JTextField commandLine, FileSystem sys){
		outputView = window;
		inputText = commandLine;
		this.sys = sys;
		prevText = ""; //Default
		isEditable = false;
		isExecuting = false;
		editsMade = "";
		notifyInsert = false;
		notifyVisual = true;
	}

	/**
	 * Return if the text editor is executing
	 * @return if the text editor is executing or not
	 */
	public boolean isExecuting(){
		return isExecuting;
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "vi";
	}

	@Override
	public String getAbout() {
		// TODO Auto-generated method stub
		return getName() + " - built in text editor for specified file";
	}

	@Override
	public void execute(String[] currDir, String[] args) {
		isExecuting = true;
		if(args.length<1){
			//Need to give a file name to manipulate
			System.out.print("\nError: Need to supply a file name");
			isExecuting = false;
			return;
		}
		this.currDir = currDir[0];
		this.args = args[0];
		prevText = outputView.getText();
		if(sys.doesFileExist(currDir[0], args[0])){
			//If file exists, open it	
			editFile = sys.getFile(currDir[0], args[0]);
			try {
				//open the file
				BufferedReader fileReader = new BufferedReader(new FileReader(editFile));
				String line = fileReader.readLine();
				StringBuilder wholeString = new StringBuilder();
				isCreated = true;
				//Loop through whole file and add add each line to a string
				while (line != null) {
					wholeString.append(line);
					wholeString.append(System.lineSeparator());
					line = fileReader.readLine();
				}
				//print the string that contains the file contents to screen
				String fileString = wholeString.toString();
				outputView.setText(fileString);

				fileReader.close();

			} catch (FileNotFoundException e) {
				System.out.print("\nError: File not found");
			} catch (IOException e) {
				System.out.print("\nError: File not found");
			}


		}else{
			//File does not exist, so create the file
			isCreated = false;
			outputView.setText("");
			editFile = new File(args[0]);
		}
		editsMade = outputView.getText();
	}

	/**
	 * Method to set editable status
	 * @param value boolean value if text editor is edit enabled
	 */
	public void setEditable(boolean value){
		isEditable = value;
	}

	@Override
	public void update(Observable o, Object arg) {
		//Notification of change recieved
		if(isExecuting){
			//VI currently running
			if(inputText.getText().equals("i")||isEditable){
				//Insert (Editable) mode, should record edits
				isEditable = true;
				//Save edits made to buffer - will use this to ignore anything in visual mode
				editsMade = outputView.getText();
				outputView.setEditable(true);
				if(notifyInsert==false){
					//User has not been notified of Insert mode
					//Show insert mode in command bar
					inputText.setText("Insert mode");
					if(editsMade.length()-1>0){
						//Do not add secondary i
						editsMade = editsMade.substring(0, editsMade.length()-1);
						outputView.setText(editsMade);
					}
					//Show that user has been notified of change
					notifyInsert = true;
					notifyVisual = false;
				}

			}
			else if(inputText.getText().equals("v")||isEditable == false){
				//Visual mode (non-editable)
				outputView.setEditable(true);//must be still true to allow arrow keys
				outputView.setText(editsMade);//Ignore anything added by setting to previous text (still allows arrow keys)
				isEditable = false;
				if(notifyVisual==false){
					//User has not been notified of Visual mode
					//Display visual mode in command bar
					inputText.setText("Visual mode");
					notifyInsert = false;
					notifyVisual = true;
				}
			}

			if(inputText.getText().equals(":q!")){
				//Quit without saving
				outputView.setText(prevText);
				outputView.setEditable(false);
				isExecuting = false;

			}


			else if(inputText.getText().equals(":w")){
				//Save current text to file
				try {
					if(isCreated == false){
						//Create the file if has not been created
						sys.createFile(currDir, args);
						editFile = sys.getFile(currDir, args);
						isCreated = true;
					}
					if(sys.doesFileExist(currDir, args)==false){
						//File error - outside of path
						isExecuting=false;
						outputView.setText(prevText);
						outputView.setEditable(false);
						System.out.print("\nError: Could not save file");
						return;
					}
					//Write the file
					BufferedWriter fileWriter = new BufferedWriter(new FileWriter(editFile));
					fileWriter.write(outputView.getText());
					fileWriter.close();
					inputText.setText("File saved");
				} catch (IOException e) {
					inputText.setText("Error: Could not save file");
				}
			}

			else if(inputText.getText().equals(":wq")){
				//Save the current text to file then quit
				try {
					if(isCreated == false){
						//Create the file if has not been created
						sys.createFile(currDir, args);
						editFile = sys.getFile(currDir, args);
						isCreated = true;
					}
					if(sys.doesFileExist(currDir, args)==false){
						//File error - outside of path
						isExecuting=false;
						outputView.setText(prevText);
						outputView.setEditable(false);
						System.out.print("\nError: Could not save file");
						return;
					}
					//Write the file
					BufferedWriter fileWriter = new BufferedWriter(new FileWriter(editFile));
					fileWriter.write(outputView.getText());
					fileWriter.close();
					inputText.setText("File saved");
					outputView.setText(prevText);
					outputView.setEditable(false);
					//Set executing to false to "quit"
					isExecuting = false;
				} catch (IOException e) {
					inputText.setText("Error: Could not save file");
				}
			}
		}
	}
}
