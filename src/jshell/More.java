package jshell;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

public class More implements Executable {

	FileSystem sys;
	String currDir;
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
			System.out.print("\nError: must supply a file to print contents of");
		}else{
			File printFile;
			if(sys.doesFileExist(currDir[0], args[0])){
				printFile = sys.getFile(currDir[0], args[0]);
				try {
					BufferedReader fileReader = new BufferedReader(new FileReader(printFile));
					String line = fileReader.readLine();
					StringBuilder wholeString = new StringBuilder();
					while (line != null) {
						wholeString.append(line);
						wholeString.append(System.lineSeparator());
						line = fileReader.readLine();
					}
					String fileString = wholeString.toString();
					System.out.print("\n"+fileString);

					fileReader.close();
				}catch(Exception e){
					System.out.print("\n:Error: could not open file: " + args[0]);
				}



			}else{
				System.out.print("\nError: could not find file: " + args[0]);
			}

		}
	}
}
