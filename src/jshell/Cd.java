package jshell;

import java.io.File;

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
	public void execute(String[] currDir, String[] args) {
		if(args.length<1){
			System.out.print("\nError: Please supply a directory name");
		}else{
			//public void changeDir(String path){
			String path = args[0];
			String root = sys.getRoot();
			String rootPath = sys.getRootPath();
			if(path.startsWith(root)&&!path.startsWith(root+File.separator)){
				System.out.print("\nError: please add file serpator to root folder");
				return;
			}
			//TESTING before and after System.out.print("prev"+currDir[0]);
			if (path.equals("..")){
				//need to go up a directory
				if(!currDir[0].equals(root+File.separator)){
					//cannot go past root
					String[] parsePath = currDir[0].split(File.separator);
					//Loop and re-create path
					//currDir[0] = sys.getRoot() + File.separator;
					if(parsePath.length<=2){
						currDir[0] = sys.getRoot(); //+ File.separator;// currDir[0] + parsePath[0];
					}else{
						currDir[0] = sys.getRoot() + File.separator;
						for(int i = 1; i < parsePath.length-1;i++){
							if(i==parsePath.length-2){
								currDir[0] = currDir[0] + parsePath[i];
							}else{
								currDir[0] = currDir[0] + parsePath[i] + File.separator;
							}
						}
					}
					//
				}
				//TESTING System.out.print("fin"+currDir[0]);
				return;

			}

			if(path.startsWith(root)||path.split(File.separator).length==1){

				//full path specified or only a name given
				File newFile;
				if(!path.startsWith(root)){
					//Must be a folder inside curr dir
					newFile = new File(rootPath + currDir[0] + File.separator + path);	
				}else{
					//change to full path
					newFile = new File(rootPath + path);
				}
				boolean canFind = newFile.isDirectory();
				if(canFind){
					if(path.startsWith(root+File.separator)){
						if(path.endsWith("/")){
							path = path.substring(0, path.length()-1);
						}
						currDir[0] = path;
					}else{
						currDir[0] = currDir[0] + File.separator + path;
					}
				}
				else{
					System.out.print("\nError: Cannot cannot find dir: " + path);
				}
			}else{
				System.out.print("\nError: Cannot move to folder outside of root path");
			}
			//TESTING before and after System.out.print("fin"+currDir[0]);
		}
	}

}

