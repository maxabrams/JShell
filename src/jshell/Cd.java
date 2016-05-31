package jshell;

import java.io.File;

public class Cd implements Executable {

	FileSystem sys;
	/**
	 * Constructor for Cd
	 * @param sys FileSystem used for directory manipulation
	 */
	public Cd(FileSystem sys){
		this.sys = sys;
	}

	@Override
	public String getName() {
		return "cd";
	}

	@Override
	public String getAbout() {
		return getName() + " - change to given directory";
	}

	@Override
	public void execute(String[] currDir, String[] args) {
		//Make sure a directory to change into is given
		if(args.length<1){
			System.out.print("\nError: Please supply a directory name");
		}else{
			//Path is given, parse the path for manipulation
			String path = args[0];
			String root = sys.getRoot();
			String rootPath = sys.getRootPath();
			if(path.startsWith(root)&&!path.startsWith(root+File.separator)){
				//Found that user wants to change to root, but does not have file separator indicating want to be "inside" of folder
				System.out.print("\nError: please add file serpator to root folder");
				return;
			}

			if (path.equals("..")){
				//Need to go up a directory
				if(!currDir[0].equals(root+File.separator)){
					//Cannot go past root
					String[] parsePath = currDir[0].split(File.separator);
					//Loop and re-create path
					if(parsePath.length<=2){
						//If parsed path has a length less than or equal to two, user must want to be in highest directory
						currDir[0] = sys.getRoot(); 
					}else{
						//Parse whole given path
						currDir[0] = sys.getRoot() + File.separator;
						for(int i = 1; i < parsePath.length-1;i++){
							if(i==parsePath.length-2){
								currDir[0] = currDir[0] + parsePath[i];
							}else{
								currDir[0] = currDir[0] + parsePath[i] + File.separator;
							}
						}
					}
				}
				return;
			}

			if(path.startsWith(root)||path.split(File.separator).length==1){
				//Full path specified or only a name given
				File newFile;
				if(!path.startsWith(root)){
					//Must be a folder inside curr directory
					newFile = new File(rootPath + currDir[0] + File.separator + path);	
				}else{
					//Change to full path
					newFile = new File(rootPath + path);
				}

				//Check if specified directory exists to change into
				boolean canFind = newFile.isDirectory();

				if(canFind){
					//Exists
					if(path.startsWith(root+File.separator)){
						//Must be higher directory
						if(path.endsWith("/")){
							//format user input to exclude "/"
							path = path.substring(0, path.length()-1);
						}
						currDir[0] = path;
					}else{
						//Must be lower directory
						currDir[0] = currDir[0] + File.separator + path;
					}
				}
				else{
					System.out.print("\nError: Cannot cannot find dir: " + path);
				}
			}else{
				System.out.print("\nError: Cannot move to folder outside of root path");
			}
		}
	}
}

