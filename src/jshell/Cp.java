package jshell;

import java.io.File;
import java.io.IOException;
import org.apache.commons.io.FileUtils;

public class Cp implements Executable {

	FileSystem sys;

	/**
	 * Constructor for Cp
	 * @param sys file system to manipulate
	 */
	public Cp(FileSystem sys){
		this.sys = sys;
	}


	@Override
	public String getName() {
		return "cp";
	}

	@Override
	public String getAbout() {
		return getName() + " - copy a file into another file";
	}


	/**
	 * (Javadoc)
	 * @see jshell.Executable#execute(java.lang.String[], java.lang.String[])
	 * Cp takes two arguments, a source and destination, and will copy the contents of either the file or the directory from the soruce to the destination. 
	 * A correct configuration of arguments is required for the function to work.
	 * You can copy a directory to a directory, a file to a file, a directory to a non-existent directory, and a file to a non-existent file, but not a directory to a file
	 */
	@Override
	public void execute(String[] currDir, String[] args) {
		if(args.length!=2){
			System.out.print("\nError: Incorrect number of arguments");
		}else{	
			File sourcePath = new File(sys.getRootPath()+args[0]);
			File destPath = new File (sys.getRootPath()+args[1]);
			String destPath2 = sys.getRootPath()+args[1];

			if(sourcePath.isFile()&&destPath.isFile()){
				try {
					FileUtils.copyFile(sourcePath, destPath);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			else if(sourcePath.isDirectory()&&destPath.isDirectory()){
				try {
					FileUtils.deleteDirectory(destPath);
					FileUtils.forceMkdir(destPath);
					FileUtils.copyDirectory(sourcePath, destPath);

				} catch (IOException e) {
					System.out.print("\nError: Problem copying directory into directory, check arguments and try again");
				}
			}
			else if(sourcePath.isDirectory()&&!destPath.isDirectory()){
				try {
					FileUtils.forceMkdir(destPath);
				} catch (IOException e) {
					System.out.print("\nError: Problem creating desired directory, check arguments and try again");
				}
				try {
					FileUtils.copyDirectory(sourcePath, destPath);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			else if(sourcePath.isFile()&&!destPath.isFile()){
				File newFile;
				if(destPath2.startsWith(sys.getRootPath()+sys.getRoot()+File.separator)){

					newFile = new File(destPath2);

					try {
						FileUtils.copyFile(sourcePath, newFile);
					} catch (IOException e) {
						System.out.print("\nError: Cannot copy file");
					}
				}

				else{
					System.out.println("\nError: Cannot copy file to given directory");
				}
			}
			else{
				System.out.println("\nError: Incorrect usage of cp, look at arguments and retry");
			}
		}
	}
}
