package jshell;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;

public class FileSystem {
	private String rootPath;
	private String root;
	//private String currDir;

	public FileSystem(String rootFolder, PrintStream stream){
		root = rootFolder;
		System.setOut(stream);
		createRootIfMissing();
		//currDir = root;
	}

	public String getRoot(){
		return root;
	}

	public String getRootPath(){
		return rootPath;
	}
	private void createRootIfMissing(){
		rootPath = System.getProperty("user.home") + File.separator + "Desktop" + File.separator;
		File rootFolder = new File(rootPath + root);
		if(!rootFolder.exists()){
			//No root folder, will need to create it
			rootFolder.mkdir();
		}

	}

	public void createFile(String currDir, String path){
		if(path.startsWith(root)||path.split(File.separator).length==1){
			File newFile;
			if(!path.startsWith(root)){
				newFile = new File(rootPath + currDir + File.separator + path);	
			}else{
				newFile = new File(rootPath + path);
			}
			try {
				newFile.createNewFile();
			} catch (IOException e) {
				System.out.print("\nError: Cannot create file: " + path);
			}
		}else{
			System.out.print("\nError: Cannot create file outside of root path");
		}
	}

	public void createFolder(String currDir, String path){
		if(path.startsWith(root)||path.split(File.separator).length==1){
			//full path specified or only a name given
			File newFile;
			boolean didCreate = false;
			if(!path.startsWith(root)){
				newFile = new File(rootPath + currDir + File.separator + path);	
			}else{
				newFile = new File(rootPath + path);
			}
			try {
				didCreate = newFile.mkdir();
			} catch (SecurityException e) {
				System.out.print("\nError: Cannot create dirs: " + path);
			}
			
			if(didCreate == false){
				System.out.print("\nError: Cannot create directory since missing directory in path: " + path);
			}
		}else{
			System.out.print("\nError: Cannot create folder outside of root path");
		}
	}

	/*
	public void changeDir(String path){
		System.out.print(currDir);
		if (path.equals("..")){
			//need to go up a directory
			if(!currDir.equals(root+File.separator)){
				//cannot go past root
				String[] parsePath = currDir.split(File.separator);
				//Loop and re-create path
				currDir = "";
				for(int i = 0; i < parsePath.length-1;i++){
					if(i==parsePath.length-1){
						currDir = currDir + parsePath[i];
					}else{
						currDir = currDir + parsePath[i] + File.separator;
					}
				}
				//
			}
			return;
		}

		if(path.startsWith(root)||path.split(File.separator).length==1){
			//full path specified or only a name given
			File newFile;
			if(!path.startsWith(root)){
				//Must be a folder inside curr dir
				newFile = new File(rootPath + currDir + File.separator + path);	
			}else{
				//change to full path
				newFile = new File(rootPath + path);
			}
			boolean canFind = newFile.exists();
			if(canFind){
				currDir = currDir + File.separator + path;
			}
			else{
				System.out.print("\nError: Cannot cannot find dir: " + path);
			}
		}else{
			System.out.print("\nError: Cannot move to folder outside of root path");
		}
		System.out.print(currDir);
	}*/
}
