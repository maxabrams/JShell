package jshell;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;

public class FileSystem {
	private String rootPath;
	private String root;
	private String currDir;

	public FileSystem(String rootFolder, PrintStream stream){
		root = rootFolder;
		System.setOut(stream);
		createRootIfMissing();
		currDir = root;
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
			System.out.print("\nError:Cannot create file outside of root path");
		}
	}
	
	public void createFolder(String path){
		if(path.startsWith(root)||path.split(File.separator).length==1){
			//full path specified or only a name given
			File newFile;
			if(!path.startsWith(root)){
				newFile = new File(rootPath + currDir + File.separator + path);	
			}else{
				newFile = new File(rootPath + path);
			}
			try {
				newFile.mkdirs();
			} catch (SecurityException e) {
				System.out.print("\nError: Cannot create dirs: " + path);
			}
		}else{
			System.out.print("\nError:Cannot create folder outside of root path");
		}
	}
}
