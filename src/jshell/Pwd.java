package jshell;

public class Pwd implements Executable {

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "pwd";
	}

	@Override
	public String getAbout() {
		// TODO Auto-generated method stub
		return getName() + " - Prints current directory";
	}

	@Override
	public void execute(String[] currDir, String[] args) {
		System.out.print("\n"+currDir[0]+"/");

	}

}
