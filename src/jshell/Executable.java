package jshell;

/**
 * Interface for all executable commands on the Shell
 */
public interface Executable {
	public String getName();//Needs to have command name to compare against (such as rm, mkdir, ect)
	public String getAbout();//Gives information about the command, its inputs, and how it operates
	public void execute(String[] currDir, String[] args);//Must be able to execute the command given an array of parameters
}
