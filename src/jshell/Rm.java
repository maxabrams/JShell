package jshell;

import java.io.File;
import java.io.IOException;
import org.apache.commons.io.FileUtils;

public class Rm implements Executable {

	FileSystem sys;
	/**
	 * Constructor for rm
	 * @param sys file system to operate on
	 */
	public Rm(FileSystem sys){
		this.sys=sys;
	}


	@Override
	public String getName() {
		return "rm";
	}

	@Override
	public String getAbout() {
		return getName() + " - remove a file at given full path with this command. Be careful!";
	}

	/**
	 * (Javadoc)
	 * @see jshell.Executable#execute(java.lang.String[], java.lang.String[])
	 * Rm removes files only. The below code runs a series of checks to make sure that the argument passed exists and is a file. 
	 * Rm will not remove a directory
	 */

	@Override
	public void execute(String[] currDir, String[] args) {
		// TODO Auto-generated method stub

		if(args.length!=1){
			System.out.print("\nError: Incorrect number of arguments");
		}
		else{
			File sourcePath = new File(sys.getRootPath()+args[0]);
			//file must exist to delete it
			if(sourcePath.exists()){
				//check that it is a file, we only delete files with rm
				if(sourcePath.isFile()){
					try {
						FileUtils.forceDelete(sourcePath);
					} catch (IOException e) {
						System.out.print("\nError: Check argument and try again");
					}
				}
				else{
					System.out.print("\nError: Cannot delete a directory with rm, use command rmdir");
				}			
			}
			else{
				System.out.print("\nError: Given argument does not exist. Did you use a full path?");
			}
		}
	}
}
