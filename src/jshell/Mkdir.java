package jshell;

public class Mkdir implements Executable {

	FileSystem sys;
	String currDir;
	public Mkdir(FileSystem sys){
		this.sys = sys;
		currDir = sys.getRoot();
	}
	
	@Override
	public String getName() {
		return "mkdir";
	}

	@Override
	public String getAbout() {
		// TODO Auto-generated method stub
		return getName()+" - Allows the user to create a new directory given a directory name or filepath";
	}

	@Override
	public void execute(String[] currDir, String[] args) {
		if(args.length<1){
			System.out.print("\nError: Please supply a directory name or path to create");
		}else{
			sys.createFolder(currDir[0], args[0]);
		}

	}

}
