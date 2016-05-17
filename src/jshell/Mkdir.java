package jshell;

public class Mkdir implements Executable {

	FileSystem sys;
	public Mkdir(FileSystem sys){
		this.sys = sys;
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
	public void execute(String[] args) {
		if(args.length<0){
			System.out.print("\nError: Please supply a directory name or path to create");
		}else{
			sys.createFolder(args[0]);
		}

	}

}
