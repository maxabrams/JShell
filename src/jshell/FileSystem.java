package jshell;

import java.io.File;

public class FileSystem {
	private String root;
	public FileSystem(String rootFolder){
		root = rootFolder;
		createRootIfMissing();
	}
	
	public String getRoot(){
		return root;
	}
	
	private void createRootIfMissing(){
		root = System.getProperty("user.home") + File.separator + "Desktop" + File.separator + root;
		System.out.println(root);
		File rootFolder = new File(root);
		if(!rootFolder.exists()){
			//No root folder, will need to create it
			rootFolder.mkdir();
		}
		
	}
}
