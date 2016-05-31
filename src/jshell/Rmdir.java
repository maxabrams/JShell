package jshell;

import java.io.File;
import java.io.IOException;
import org.apache.commons.io.FileUtils;

public class Rmdir implements Executable {
	FileSystem sys;

	public Rmdir(FileSystem sys){
		this.sys = sys;
	}
	@Override
	public String getName() {
		return "rmdir";
	}

	@Override
	public String getAbout() {
		return getName() + "- recursively remove a directory at the given full path";
	}

	/**
	 * (Javadoc)
	 * @see jshell.Executable#execute(java.lang.String[], java.lang.String[])
	 * Rmdir will take an argument given, concatenate it to the root path, and then run a series of checks to see if the desired delete action can be taken
	 * Rmdir will not allow you to delete your current directory, a directory above you in your direct file path, or a file, as rmdir is only for directories
	 */
	@Override
	public void execute(String[] currDir, String[] args) {
		if(args.length!=1){
			System.out.print("\nError: Incorrect number of arguments");
		}
		else{
			File sourcePath = new File(sys.getRootPath()+args[0]);
			//directory must exist
			if(sourcePath.exists()){
				//if the argument equals the root directory in any way, we do not delete it
				if(args[0].toLowerCase().equals(sys.getRoot().toLowerCase())||args[0].toLowerCase().equals(sys.getRoot().toLowerCase()+File.separator)){
					System.out.print("\nError: Cannot delete this directory");
				}else{
					//if you attempt to delete the current directory return out, we can't do that
					if(args[0].equals(currDir[0])){
						System.out.print("\nError: Cannot delete directory you are currently inside");
						return;
					}
					//if you attempt to delete a directory up the file path, can't do that
					if(currDir[0].startsWith(args[0])){
						System.out.print("\nError: Cannot delete a directory up the file tree from current directory");
						return;
					}

					//insure the source is a directory
					if(sourcePath.isDirectory()){
						try {
							FileUtils.deleteDirectory(sourcePath);
						} catch (IOException e) {
							System.out.print("\nError: Given directory does not exist");
						}

					}
					else{
						System.out.print("\nError: Cannot delete a file with rmdir, use command rm");
					}
				}
			}
			else{
				System.out.print("\nError: Given argument does not exist. Did you use a full path?");
			}
		}
	}
}
