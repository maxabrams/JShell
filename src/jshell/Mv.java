package jshell;

import java.io.File;
import java.io.IOException;
import org.apache.commons.io.FileUtils;

public class Mv implements Executable {
	
	FileSystem sys;
	
	/**
	 * Constructor for mv
	 * @param sys file system to manipulate
	 */
	public Mv(FileSystem sys){
		this.sys = sys;;
	}
	
	@Override
	public String getName() {
		return "mv";
	}

	@Override
	public String getAbout() {
		return getName() + " - moves a file from one directory to another";
	}

	@Override
	/**
	 * (Javadoc)
	 * @see jshell.Executable#execute(java.lang.String[], java.lang.String[])
	 * Mv allows you to move a file to another directory or move a directory into another directory
	 * The source and destination must exist for mv to work
	 */
	public void execute(String[] currDir, String[] args) {
		if(args.length!=2){
			System.out.print("\nError: Incorrect number of arguments");
		}
		else{
			File sourcePath = new File(sys.getRootPath()+args[0]);
			File destPath = new File (sys.getRootPath()+args[1]);
			//case for moving a directory to inside another directory
			if(sourcePath.isDirectory()&&destPath.isDirectory()){
				try {
					FileUtils.moveDirectoryToDirectory(sourcePath, destPath, true);
				} catch (IOException e) {
					System.out.print("\nError: Cannot move a directory into itself");
				}
			}
			//case for file into directory
			else if(sourcePath.isFile()&&destPath.isDirectory()){
				try {
					FileUtils.moveFileToDirectory(sourcePath, destPath, true);
				} catch (IOException e) {
					System.out.print("\nError: Destination file already exists within directory, please check arguments");
				}
			}
			else{
				System.out.print("\nError: Incorrect arguments for mv, either source or destination does not exist, please check pathing");
			}	
		}
	}
}
