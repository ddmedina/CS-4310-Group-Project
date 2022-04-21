import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main
{
	static Directory root = new Directory("C:", false, false, true, false);
	static Directory dsktp = new Directory("Desktop", false, false, true, false);
	static Directory dcmnts = new Directory("Documents", false, false, true, false);
	static Directory dwnlds = new Directory("Downloads", false, false, true, false);
	static Directory pics = new Directory("Pictures", false, false, true, false);
	static Directory music = new Directory("Music", false, false, true, false);
	static Directory vids = new Directory("Videos", false, false, true, false);

	static Directory currentDir = root;

	public static void main(String[] args) {
	    Scanner scnr = new Scanner(System.in);
	    
	    root.addSubDir(dsktp);
	    dsktp.addParent(root);
	    root.addSubDir(dcmnts);
	    dcmnts.addParent(root);
	    root.addSubDir(dwnlds);
	    dwnlds.addParent(root);
		root.addSubDir(pics);
		pics.addParent(root);
		root.addSubDir(music);
		music.addParent(root);
		root.addSubDir(vids);
		vids.addParent(root);
	    
	    while(true){
			// prints the absolute path to current directory
	        printPath(currentDir);

	        String input = scnr.nextLine();

			// allows the user to create and travers files and folders with more than one word in its name
			ArrayList<String> list = new ArrayList<String>();
			Matcher m = Pattern.compile("([^\"]\\S*|\".+?\")\\s*").matcher(input);
			while (m.find())
				list.add(m.group(1));

			// exits terminal
	        if (list.get(0).equals("exit")){
	            break;
	        }

			// parse the command that was entered
	        currentDir = processCMD(currentDir, list);
	        list.clear();
	        
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
	
	static Directory processCMD(Directory current, ArrayList<String> cmd){
	    Directory currentDir = current;
		// allow only certain inputs as commands
	    String [] valid_cmds = {"mkdir", "mkfile", "rename", "cd", "ls", "rm"};
	    if (Arrays.asList(valid_cmds).contains(cmd.get(0))){
	        switch (cmd.get(0)){
	            case "mkdir":
	                if (cmd.size() == 2){
	                    mkdir(current, cmd.get(1));
	                } else if (cmd.size() == 1){
	                    mkdir(current, "New Folder");
	                } else {
	                    System.out.println("Invalid command\n");
	                }
	                currentDir = current;
	                break;
	                
	            case "mkfile":
	                if (cmd.size() == 2){
	                    mkfile(current, cmd.get(1));
	                } else if (cmd.size() == 1){
	                    mkfile(current, "New File");
	                } else {
	                    System.out.println("Invalid command\n");
	                }
	                currentDir = current;
	                break;

				case "rename":
					if (cmd.size() == 3) {
						rename(current, cmd.get(1), cmd.get(2));
					} else {
						System.out.println("Invalid command\n");
					}
					currentDir = current;
					break;
	                
	            case "cd":
	                if (cmd.size() == 2){
	                    currentDir = cd(current, cmd.get(1));
	                } else {
	                    System.out.println("Invalid command\n");
	                }
	                break;
	                
                case "ls":
                    if (cmd.size() == 1){
						System.out.println("\nFolders:\n");
						if (current.getDirs().size() == 0){
							System.out.println("** No Folders **");
						} else {
							viewDirs(current);
						}
						System.out.println("\nFiles:\n");
						if (current.getFiles().size() == 0){
							System.out.println("** No Files **");
						} else {
							viewFiles(current);
						}
	                } else {
	                    System.out.println("Invalid command\n");
	                }
	                currentDir = current;
	                break;
				case "rm":
					if (cmd.size() == 2){
						rm(currentDir, cmd.get(1));
					} else {
						System.out.println("Invalid command\n");
					}
	        }
	    }
	    else {
	        System.out.println("Invalid command\n");
	    }
	    return currentDir;
	}
	
	static void mkdir(Directory current, String dirName){
		String name = dirName.replaceAll("\"", "");		//remove double quotes from directory name
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
	        Directory dir = new Directory(name, true, true, true, true);
	        current.addSubDir(dir);
	        dir.addParent(current);
	    }
	}
	
	static void mkfile(Directory current, String fname){
		String name = fname.replaceAll("\"", "");
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

	// incomplete
	static void rename(Directory current, String original, String nName){
		Scanner input = new Scanner(System.in);
		String oriName = original.replaceAll("\"", "");
		String newName = nName.replaceAll("\"", "");
		int fileOrFolder = 3;
		boolean isFile = false;
		boolean isDir = false;
		int dirIndex = 0;
		int fileIndex = 0;

		// search through subdirectories for matching name
		for (int i = 0; i < current.getDirs().size(); i++){
			if (current.getDir(i).getName().equals(oriName)){
				isDir = true;
				dirIndex = i;
			}
		}

		// search through files for matching name
		for (int i = 0; i < current.getFiles().size(); i++){
			if (current.getFile(i).getName().equals(oriName)){
				isFile = true;
				fileIndex = i;
			}
		}

		// if object is file and directory, chose which to rename
		if (isDir && isFile){
			System.out.println("Found 1 file and 1 folder matching that name. Which do you want to rename? [1] file [2] folder: ");
			fileOrFolder = input.nextInt();
			if (fileOrFolder == 2){
				Directory dir = current.getDir(dirIndex);
				// check if this dir is renameable
				if (dir.canRename()){
					dir.rename(newName);
				} else {
					System.out.println("Cannot rename folder!");
				}
			} else if (fileOrFolder == 1) {
				File file = current.getFile(fileIndex);
				file.rename(newName);
			} else {
				System.out.println("Input not valid");
			}

		}
		// if object os directory only, rename
		else if (isDir && !isFile) {
			Directory dir = current.getDir(dirIndex);
			// check if this dir is renameable
			if (dir.canRename()){
				dir.rename(newName);
			} else {
				System.out.println("Cannot rename folder!");
			}

		}
		// if object is file only, rename
		else if (isFile && !isDir) {
			File file = current.getFile(fileIndex);
			file.rename(newName);
		}
	}
	
	static Directory cd(Directory current, String dirName){
		String dir = dirName.replaceAll("\"", ""); 	//remove double quotes from directory name
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
	    if (current == root){
			if (dir.equals("..")){
				System.out.println("In root directory!");
			} else if (isSubDir) {
				newCurrentDir = current.getDir(index);
			}
		} else if (dir.equals("..") || dir.equals(current.parent.getName())){
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

	static void viewFiles(Directory current){
		ArrayList<File> files = current.getFiles();
		for (int i = 0; i < files.size(); i++){
			System.out.println(files.get(i).getName() + "  ");
		}
	}

	static void rm(Directory dir, String dirName){
		String dirToDelete = dirName.replaceAll("\"", ""); 	//remove double quotes from directory name
		Scanner input = new Scanner(System.in);
		ArrayList<Directory> dirs = dir.getDirs();
		int indexToDelete = 99999;
		boolean hasFiles = false;
		boolean hasSubDirs = false;
		String proceed = null;

		if (dir.isSubDir(dirToDelete)){
			for (int i = 0; i < dirs.size(); i++){
				if (dir.getDir(i).getName().equals(dirToDelete)) {
					indexToDelete = i;
				}
			}
			if (dir.getDir(indexToDelete).canDelete() && indexToDelete < dirs.size()){
				System.out.println("This will delete folder and all contents. Proceed? [y/n]: ");
				proceed = input.next();
				input.close();
				if (proceed.equalsIgnoreCase("y")){
					dir.getDir(indexToDelete).clearContents();
					dir.delete(indexToDelete);
				} else {
					System.out.println("Directory not deleted");
				}

			} else {
				System.out.println("Directory cannot be deleted!");
			}
		} else {
			System.out.println("Directory specified does not exist!");
		}
	}
}
