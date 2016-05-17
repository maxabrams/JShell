package jshell;

public class Cd implements Executable {

	FileSystem sys;
	public Cd(FileSystem sys){
		this.sys = sys;
	}
	
	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "cd";
	}

	@Override
	public String getAbout() {
		// TODO Auto-generated method stub
		return getName() + " - change to given directory";
	}

	@Override
	public void execute(String[] args) {
		if(args.length<0){
			System.out.print("\nError: Please supply a directory name");
		}else{
			sys.changeDir(args[0]);
		}

	}

}
