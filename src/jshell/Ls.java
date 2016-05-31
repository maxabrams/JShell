package jshell;

import java.io.File;

public class Ls implements Executable {
	FileSystem sys;

	/**
	 * Constructore for LS
	 * @param system file system to report on
	 */
	public Ls(FileSystem system){
		sys = system;
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "ls";
	}

	@Override
	public String getAbout() {
		// TODO Auto-generated method stub
		return getName() + " - lists all files and directories in currentFolder";
	}

	@Override
	public void execute(String[] currDir, String[] args) {
		if(args.length<1){
			//list files in current directory
			File currFolder = new File(sys.getRootPath()+currDir[0]);
			File[] listOfFiles = currFolder.listFiles();
			for(int i = 0; i < listOfFiles.length;i++){
				System.out.print("\n"+listOfFiles[i].getName());
			}
		}else{
			//list files in specified path of directories
			File newFile;
			if(!args[0].startsWith(sys.getRoot())){
				//must want to create in current directory
				newFile = new File(sys.getRootPath() + sys.getRoot() + File.separator + args[0]);
			}else{
				//full path given, use full path to create
				newFile = new File(sys.getRootPath() + args[0]);
			}

			if(!newFile.isDirectory()){
				//Not a valid directory
				System.out.println("\nError: No directory found: " + args[0]);
			}else{
				//Valid directory, print all files in directory
				File[] listOfFiles = newFile.listFiles();
				for(int i = 0; i < listOfFiles.length;i++){
					System.out.print("\n"+listOfFiles[i].getName());
				}
			}
		}
	}
}


