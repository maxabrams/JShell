package jshell;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;

public class FileSystem {
	private String rootPath;
	private String root;

	/**
	 * Constructor for FileSystem
	 * @param rootFolder the name of the root (home directory) to use
	 * @param stream the output to print information to
	 */
	public FileSystem(String rootFolder, PrintStream stream){
		root = rootFolder;
		System.setOut(stream);
		//If the root is missing or has never been created, make a new root directory
		createRootIfMissing();
	}

	/**
	 * Returns the root directory name
	 * @return the root directory name
	 */
	public String getRoot(){
		return root;
	}

	/**
	 * Method to get the path of the root folder on the real file system
	 * @return the whole "real" path up to root 
	 */
	public String getRootPath(){
		return rootPath;
	}

	/**
	 * Method to create root file if non-existent
	 */
	private void createRootIfMissing(){
		//For this project, we will always place the "root" folder on the desktop
		rootPath = System.getProperty("user.home") + File.separator + "Desktop" + File.separator;
		File rootFolder = new File(rootPath + root);
		if(!rootFolder.isDirectory()){
			//No root folder, will need to create it
			rootFolder.mkdir();
		}
	}

	/**
	 * Method to create a file on the file system
	 * @param currDir user current directory
	 * @param path location of where to place the new file if possible
	 */
	public void createFile(String currDir, String path){
		if(path.startsWith(root)||path.split(File.separator).length==1){
			//Must be inside of the root directory or inside of current directory
			File newFile;
			if(!path.startsWith(root)){
				//create file in current directory
				newFile = new File(rootPath + currDir + File.separator + path);	
			}else{
				//create a file under root (user used full path)
				newFile = new File(rootPath + path);
			}
			try {
				//create the file
				newFile.createNewFile();
			} catch (IOException e) {
				System.out.print("\nError: Cannot create file: " + path);
			}
		}else{
			System.out.print("\nError: Cannot create file outside of root path");
		}
	}

	/**
	 * Method to check if a file exists in the file system
	 * @param currDir user current directory 
	 * @param path location of where to check for the file
	 * @return boolean for if the file exists (true) or not (false)
	 */
	public boolean doesFileExist(String currDir, String path){
		File newFile;
		if(!path.startsWith(root)){
			//must be directory in current directory
			newFile = new File(rootPath + currDir + File.separator + path);	
		}else{
			//full path given, check full path
			newFile = new File(rootPath + path);
		}

		//Check if path gives a file
		return newFile.isFile();	
	}

	/**
	 * Method to check get a file if it exists in the file system
	 * @param currDir user current directory 
	 * @param path location of where to check for the file
	 * @return the file if it exists in the file system. If not, a null file is returned
	 */
	public File getFile(String currDir, String path){
		if(doesFileExist(currDir, path)){
			//File must exist
			File newFile;
			if(!path.startsWith(root)){	
				//must be directory in current directory
				newFile = new File(rootPath + currDir + File.separator + path);	
			}else{
				//full path given, use full path
				newFile = new File(rootPath + path);
			}
			return newFile;
		}
		System.out.print("\nError: Cannot get file: "+ path);
		return null;
	}


	/**
	 * Create a folder given a path if it is within root structure
	 * @param currDir user current directory 
	 * @param path location of where to create the folder
	 */
	public void createFolder(String currDir, String path){
		if(path.startsWith(root)||path.split(File.separator).length==1){
			//full path specified or only a name given
			File newFile;
			boolean didCreate = false;
			if(!path.startsWith(root)){
				//must want to create in current directory
				newFile = new File(rootPath + currDir + File.separator + path);	
			}else{
				//full path given, use full path to create
				newFile = new File(rootPath + path);
			}
			try {
				//create path
				didCreate = newFile.mkdir();
			} catch (SecurityException e) {
				System.out.print("\nError: Cannot create dirs: " + path);
			}

			if(didCreate == false){
				//folders in path are missing, need to create those first
				System.out.print("\nError: Cannot create directory since missing directory in path: " + path);
			}
		}else{
			//Was outside of root path
			System.out.print("\nError: Cannot create folder outside of root path");
		}
	}
}
