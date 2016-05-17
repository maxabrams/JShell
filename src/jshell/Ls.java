package jshell;

import java.io.File;
import java.io.IOException;

public class Ls implements Executable {
	FileSystem sys;

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
			File newFile = new File(sys.getRootPath() + sys.getRoot() + File.separator + args[0]);
			if(!newFile.exists()){
				System.out.println("\nError: No directory found: " + args[0]);
			}else{
				File[] listOfFiles = newFile.listFiles();
				for(int i = 0; i < listOfFiles.length;i++){
					System.out.print("\n"+listOfFiles[i].getName());
				}
			}
		}
	}

}


