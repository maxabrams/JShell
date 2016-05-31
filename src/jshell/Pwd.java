package jshell;

public class Pwd implements Executable {

	@Override
	public String getName() {
		return "pwd";
	}

	@Override
	public String getAbout() {
		return getName() + " - Prints current directory";
	}

	@Override
	public void execute(String[] currDir, String[] args) {
		//Format and print current directory reported by the shell
		System.out.print("\n"+currDir[0]+"/");
	}
}
