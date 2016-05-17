package jshell;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;

public class FileSystem {
	private String rootPath;
	private String root;

	public FileSystem(String rootFolder, PrintStream stream){
		root = rootFolder;
		System.setOut(stream);
		createRootIfMissing();
	}

	public String getRoot(){
		return root;
	}

	private void createRootIfMissing(){
		rootPath = System.getProperty("user.home") + File.separator + "Desktop" + File.separator;
		File rootFolder = new File(rootPath + root);
		if(!rootFolder.exists()){
			//No root folder, will need to create it
			rootFolder.mkdir();
		}

	}

	public void createFile(String path){
		if(path.startsWith(root)){
			File newFile = new File(rootPath + path);
			try {
				newFile.createNewFile();
			} catch (IOException e) {
				System.out.print("\nError: Cannot create file: " + path);
			}
		}else{
			System.out.println(path);
			System.out.print("\nError:Cannot create file outside of root path");
		}
	}
	
	public void createFolder(String path){
		if(path.startsWith(root)){
			File newFile = new File(rootPath + path);
			try {
				newFile.mkdirs();
			} catch (SecurityException e) {
				System.out.println("Error: Cannot create dirs: " + path);
			}
		}else{
			System.out.print("\nError:Cannot create folder outside of root path");
		}
	}
}
