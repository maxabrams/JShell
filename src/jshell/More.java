package jshell;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

public class More implements Executable {

	FileSystem sys;
	String currDir;
	
	/**
	 * Constructor for more
	 * @param sys file system to utilize
	 */
	public More(FileSystem sys){
		this.sys = sys;
		currDir = sys.getRoot();
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "more";
	}

	@Override
	public String getAbout() {
		// TODO Auto-generated method stub
		return getName() + " - prints contents of given file to screen";
	}

	@Override
	public void execute(String[] currDir, String[] args) {
		if(args.length<1){
			//Need at least one argument
			System.out.print("\nError: must supply a file to print contents of");
			
		}else{
			
			File printFile;
			if(sys.doesFileExist(currDir[0], args[0])){
				//the file must exist to display text
				printFile = sys.getFile(currDir[0], args[0]);
				try {
					//Read the file
					BufferedReader fileReader = new BufferedReader(new FileReader(printFile));
					String line = fileReader.readLine();
					StringBuilder wholeString = new StringBuilder();
					//Since file may have mutliple lines, add all lines into one string for printing
					//loop through all lines and add to the string builder
					while (line != null) {
						wholeString.append(line);
						wholeString.append(System.lineSeparator());
						line = fileReader.readLine();
					}
					//Combine into one whole string
					String fileString = wholeString.toString();
					//print the file contents to console
					System.out.print("\n"+fileString);

					fileReader.close();
					
				}catch(Exception e){
					
					System.out.print("\n:Error: could not open file: " + args[0]);
					
				}

			}else{
				//File system did not find the file to read
				System.out.print("\nError: could not find file: " + args[0]);
			}
		}
	}
}
