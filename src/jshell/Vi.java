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
		return getName() + " - built in text editor";
	}

	@Override
	public void execute(String[] currDir, String[] args) {
		isExecuting = true;
		if(args.length<1){
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
				BufferedReader fileReader = new BufferedReader(new FileReader(editFile));
				String line = fileReader.readLine();
				StringBuilder wholeString = new StringBuilder();
				isCreated = true;
				while (line != null) {
					wholeString.append(line);
					wholeString.append(System.lineSeparator());
					line = fileReader.readLine();
				}
				String fileString = wholeString.toString();
				outputView.setText(fileString);

				fileReader.close();


			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}


		}else{
			//Create the file

			isCreated = false;
			outputView.setText("");
			editFile = new File(args[0]);
		}
		editsMade = outputView.getText();


	}

	public void setEditable(boolean value){
		isEditable = value;
	}

	@Override
	public void update(Observable o, Object arg) {

		if(isExecuting){

			if(inputText.getText().equals("i")||isEditable){
				isEditable = true;
				editsMade = outputView.getText();
				outputView.setEditable(true);
				if(notifyInsert==false){
					inputText.setText("Insert mode");
					if(editsMade.length()-1>0){
						//do not add secondary i
						editsMade = editsMade.substring(0, editsMade.length()-1);
						outputView.setText(editsMade);
					}
					notifyInsert = true;
					notifyVisual = false;
				}

			}
			else if(inputText.getText().equals("v")||isEditable == false){
				outputView.setEditable(true);
				outputView.setText(editsMade);
				isEditable = false;
				if(notifyVisual==false){
					inputText.setText("Visual mode");
					notifyInsert = false;
					notifyVisual = true;
				}

			}

			if(inputText.getText().equals(":q!")){
				outputView.setText(prevText);
				outputView.setEditable(false);
				isExecuting = false;

			}


			else if(inputText.getText().equals(":w")){
				try {
					if(isCreated == false){
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
					BufferedWriter fileWriter = new BufferedWriter(new FileWriter(editFile));
					fileWriter.write(outputView.getText());
					fileWriter.close();
					inputText.setText("File saved");
				} catch (IOException e) {
					inputText.setText("Error: Could not save file");
					e.printStackTrace();
				}

			}

			else if(inputText.getText().equals(":wq")){
				try {
					if(isCreated == false){
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
					BufferedWriter fileWriter = new BufferedWriter(new FileWriter(editFile));
					fileWriter.write(outputView.getText());
					fileWriter.close();
					inputText.setText("File saved");
					outputView.setText(prevText);
					outputView.setEditable(false);
					isExecuting = false;
				} catch (IOException e) {
					inputText.setText("Error: Could not save file");
					e.printStackTrace();
				}

			}

		}
	}

}
