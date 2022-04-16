import java.util.*;

public class Main
{
	public static void main(String[] args) {
	    Scanner scnr = new Scanner(System.in);
	    
	    Directory root = new Directory("C:");
	    Directory dsktp = new Directory("Desktop");
	    Directory dcmnts = new Directory("Documents");
	    Directory dwnlds = new Directory("Downloads");
	    
	    root.addSubDir(dsktp);
	    dsktp.addParent(root);
	    root.addSubDir(dcmnts);
	    dcmnts.addParent(root);
	    root.addSubDir(dwnlds);
	    dwnlds.addParent(root);
	    
	    Directory currentDir = dsktp;
	    
	    while(true){
	        printPath(currentDir);
	        String [] input = scnr.nextLine().split(" ");
	        if (input[0].equals("exit")){
	            break;
	        }
	        
	        currentDir = processCMD(currentDir, input);
	        
	        
	    }
	}
	
	static void printPath(Directory current){
	    System.out.println();
	    ArrayList<String> path = new ArrayList<String>();
	    while(true){
	        path.add(current.getName());
	        current = current.parent;
	        if (current == null){
	            break;
	        }
	    }
	    for (int i = path.size() - 1; i > -1; i--){
	        System.out.print(path.get(i) + "\\");
	    }
	    System.out.print(">");
	        
	}
	
	static Directory processCMD(Directory current, String[] cmd){
	    Directory currentDir = current;
	    String [] valid_cmds = {"mkdir", "mkfile", "rename", "cd", "viewDirs"};
	    if (Arrays.asList(valid_cmds).contains(cmd[0])){
	        switch (cmd[0]){
	            case "mkdir":
	                if (cmd.length == 2){
	                    mkdir(current, cmd[1]);
	                } else if (cmd.length == 1){
	                    mkdir(current, "New_Folder");
	                } else {
	                    System.out.println("Invalid command\n");
	                }
	                currentDir = current;
	                break;
	                
	            case "mkfile":
	                if (cmd.length == 2){
	                    mkfile(current, cmd[1]);
	                } else if (cmd.length == 1){
	                    mkfile(current, "New_File");
	                } else {
	                    System.out.println("Invalid command\n");
	                }
	                currentDir = current;
	                break;
	                
	            case "cd":
	                if (cmd.length == 2){
	                    currentDir = cd(current, cmd[1]);
	                } else {
	                    System.out.println("Invalid command\n");
	                }
	                break;
	                
                case "viewDirs":
                    if (cmd.length == 1){
	                    viewDirs(current);
	                } else {
	                    System.out.println("Invalid command\n");
	                }
	                currentDir = current;
	                break;
	        }
	    }
	    else {
	        System.out.println("Invalid command\n");
	    }
	    return currentDir;
	}
	
	static void mkdir(Directory current, String name){
	    ArrayList<Directory> dirs = current.getDirs();
	    boolean exists = false;
	    for (int i = 0; i < dirs.size(); i++){
	        if (name.equals(dirs.get(i).getName())){
	            exists = true;
	        }
	    }
	    if (exists){
	        System.out.println("Directory already exists");
	    }
	    else {
	        Directory dir = new Directory(name);
	        current.addSubDir(dir);
	        dir.addParent(current);
	    }
	}
	
	static void mkfile(Directory current, String name){
	    ArrayList<File> files = current.getFiles();
	    boolean exists = false;
	    for (int i = 0; i < files.size(); i++){
	        if (name.equals(files.get(i).getName())){
	            exists = true;
	        }
	    }
	    if (exists){
	        System.out.println("File already exists");
	    }
	    else {
	        File file = new File(name);
	        current.addFile(file);
	    }
	}
	
	static Directory cd(Directory current, String dir){
	    Directory newCurrentDir = null;
	    int index = 0;
	    ArrayList<Directory> dirs = current.getDirs();
	    boolean isSubDir = false;
	    for (int i = 0; i < dirs.size(); i++){
	        if (dir.equals(dirs.get(i).getName())){
	            isSubDir = true;
	            index = i;
	        }
	    }
	    
	    if (dir.equals("..") || dir.equals(current.parent.getName())){
	        newCurrentDir = current.parent;
	    } else if (isSubDir){
	        newCurrentDir = current.getDir(index);
	    }
	    
	    return newCurrentDir;
	}
	
	static void viewDirs(Directory current){
	    ArrayList<Directory> dirs = current.getDirs();
        for (int i = 0; i < dirs.size(); i++){
            System.out.println(dirs.get(i).getName() + "  ");
        }
	}
}
