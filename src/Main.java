import java.nio.file.Paths;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.time.*;
import java.time.format.DateTimeFormatter;

public class Main {
	static Directory root = new Directory("C:", false, false, true, false);
	static Directory dsktp = new Directory("Desktop", false, false, true, false);
	static Directory dcmnts = new Directory("Documents", false, false, true, false);
	static Directory dwnlds = new Directory("Downloads", false, false, true, false);
	static Directory pics = new Directory("Pictures", false, false, true, false);
	static Directory music = new Directory("Music", false, false, true, false);
	static Directory vids = new Directory("Videos", false, false, true, false);

	static Directory currentDir = root;

	static FCB block = new FCB();

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

		while (true) {
			// prints the absolute path to current directory
			printPath(currentDir);

			String input = scnr.nextLine();

			// allows the user to create and travers files and folders with more than one
			// word in its name
			ArrayList<String> list = new ArrayList<String>();
			Matcher m = Pattern.compile("([^\"]\\S*|\".+?\")\\s*").matcher(input);
			while (m.find())
				list.add(m.group(1));

			// exits terminal
			if (list.get(0).equals("exit")) {
				break;
			}

			// parse the command that was entered
			currentDir = processCMD(currentDir, list);
			list.clear();

		}
	}

	static void printPath(Directory current) {
		System.out.println();
		ArrayList<String> path = new ArrayList<String>();
		while (true) {
			path.add(current.getName());
			current = current.parent;
			if (current == null) {
				break;
			}
		}
		for (int i = path.size() - 1; i > -1; i--) {
			System.out.print(path.get(i) + "\\");
		}
		System.out.print(">");

	}

	static Directory processCMD(Directory current, ArrayList<String> cmd) {
		Directory currentDir = current;
		// allow only certain inputs as commands
		String[] valid_cmds = { "mkdir", "mkfile", "rename", "cd", "ls", "rm", "mv", "cat", "find", "help", "write", "overwrite", "read", "meta" };
		if (Arrays.asList(valid_cmds).contains(cmd.get(0))) {
			switch (cmd.get(0)) {
				case "mkdir":
					if (cmd.size() == 2) {
						mkdir(current, cmd.get(1));
					} else if (cmd.size() == 1) {
						mkdir(current, "New Folder");
					} else {
						System.out.println("Invalid command\n");
					}
					currentDir = current;
					break;

				case "mkfile":
					if (cmd.size() == 2) {
						mkfile(current, cmd.get(1));
					} else if (cmd.size() == 1) {
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
					if (cmd.size() == 2) {
						currentDir = cd(current, cmd.get(1));
					} else {
						System.out.println("Invalid command\n");
					}
					break;

				case "ls":
					if (cmd.size() == 1) {
						System.out.println("\nFolders:\n");
						if (current.getDirs().size() == 0) {
							System.out.println("** No Folders **");
						} else {
							viewDirs(current);
						}
						System.out.println("\nFiles:\n");
						if (current.getFiles().size() == 0) {
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
					if (cmd.size() == 2) {
						rm(currentDir, cmd.get(1));
					} else {
						System.out.println("Invalid command\n");
					}
					break;

				case "mv":
					if (cmd.size() == 3) {
						move(currentDir, cmd.get(1), cmd.get(2));
					} else {
						System.out.println("Invalid command\n");
					}
					break;
				case "cat":
					if (cmd.size() == 2)
					{
						cat(currentDir, cmd.get(1));
					}
					break;
				case "find":
					if (cmd.size() == 2)
						System.out.println(find(currentDir, cmd.get(1)));
					else
						System.out.println("Invalid command\n");
					break;
				case "help":
					if (cmd.size() == 1)
						help();
					else
						System.out.println("Invalid command\n");
					break;
				case "write":
					if (cmd.size() == 2){
						write(currentDir, cmd.get(1));
					} else {
						System.out.println("Invalid command\n");
					}
					break;
				case "overwrite":
					if (cmd.size() == 2){
						overwrite(currentDir, cmd.get(1));
					} else {
						System.out.println("Invalid command\n");
					}
					break;
				case "read":
					if (cmd.size() == 2){
						read(currentDir, cmd.get(1));
					} else {
						System.out.println("Invalid command\n");
					}
					break;
				case "meta":
					if (cmd.size() == 2)
						meta(currentDir, cmd.get(1));
					else
						System.out.println("Invalid command\n");
					break;	
			}
		} else {
			System.out.println("Invalid command\n");
		}
		return currentDir;
	}

	static void mkdir(Directory current, String dirName) {
		String name = dirName.replaceAll("\"", ""); // remove double quotes from directory name
		ArrayList<Directory> dirs = current.getDirs();
		boolean exists = false;
		for (int i = 0; i < dirs.size(); i++) {
			if (name.equals(dirs.get(i).getName())) {
				exists = true;
			}
		}
		if (exists) {
			System.out.println("Directory already exists");
		} else {
			Directory dir = new Directory(name, true, true, true, true);
			current.addSubDir(dir);
			dir.addParent(current);
		}
	}

	static void mkfile(Directory current, String fname){
		String name = fname.replaceAll("\"", "");
		ArrayList<File> files = current.getFiles();
		boolean exists = false;
		for (int i = 0; i < files.size(); i++) {
			if (name.equals(files.get(i).getName())) {
				exists = true;
			}
		}
		if (exists) {
			System.out.println("File already exists");
		} else {
			if (current.canWrite()) {
				File file = new File(name, true, true, true, true);
				current.addFile(file);
				//Adding new file's magicnumber to the FCB with the content
				block.addContent(file.getmNum(), "");
			} else {
				System.out.println("Cannot write files!");
			}
		}
	}

	static void rename(Directory current, String original, String nName) {
		Scanner input = new Scanner(System.in);
		String oriName = original.replaceAll("\"", "");
		String newName = nName.replaceAll("\"", "");
		int[] type = type(current, oriName);

		// if object is directory, rename
		if (type[0] == 2) {
			Directory dir = current.getDir(type[1]);
			// check if this dir is renameable
			if (dir.canRename()) {
				dir.rename(newName);
			} else {
				System.out.println("Cannot rename folder!");
			}

		}
		// if object is file, rename
		else if (type[0] == 1) {
			File file = current.getFile(type[1]);
			file.rename(newName);
		}
	}

	static Directory cd(Directory current, String dirName) {
		String name = dirName.replaceAll("\"", ""); // remove double quotes from directory name
		Directory newCurrentDir = null;
		int[] type = new int[2];

		if (name.equals("..")) {
			type[0] = 2;
		} else {
			// prevent crashes by not allowing user to change directories to files
			type = type(current, name);
		}
		if (type[0] == 2) {
			// check if the current directory is the root
			if (current.parent == null) {
				if (name.equals("..")) {
					System.out.println("In root directory!");
					newCurrentDir = current;
				} else if (current.isSubDir(name)) {
					newCurrentDir = current.getDir(type[1]);
				}
			}
			// if not the root and user wants to move up, get parent dir
			else if (current.parent != null) {
				if (name.equals("..") || name.equals(current.parent.getName())) {
					newCurrentDir = current.parent;
				}
				// if not the root and user wants to move down, get subdirectory
				else if (current.isSubDir(name)) {
					newCurrentDir = current.getDir(type[1]);
				}
			}
		} else {
			System.out.println("Cannot use file as input!");
			newCurrentDir = current;
		}
		return newCurrentDir;
	}

	static void viewDirs(Directory current) {
		ArrayList<Directory> dirs = current.getDirs();
		for (int i = 0; i < dirs.size(); i++) {
			System.out.println(dirs.get(i).getName() + "  ");
		}
	}

	static void viewFiles(Directory current) {
		ArrayList<File> files = current.getFiles();
		for (int i = 0; i < files.size(); i++) {
			System.out.println(files.get(i).getName() + "  ");
		}
	}

	static void help() {
		// ("mkdir", "mkfile", "rename", "cd", "ls", "rm", "mv", "cat", "find", "meta")
		System.out.println("mkdir x: Create subdirectory x");
		System.out.println("mkfile x: Create file x");
		System.out.println("Rename x y: Rename file or directory x to y");
		System.out.println("cd x: Change to directory x from current directory (use .. to back out)");
		System.out.println("ls: Lists all files and subdirectories in current directory");
		System.out.println("rm x: Removes directory or file if it is deletable");
		System.out.println("mv x y: Moves file or directory x to specified directory y");
		System.out.println("find x: Finds file or directory x within current directory and subdirectories");
		System.out.println("meta x: Displays metadata of a file entered");
	}

	static String find(Directory current, String filename) {
		Boolean fileFound = false;
		String path = "File/directory not found";

		for (int i = 0; i < current.getFiles().size(); i++) { // search through current directory
			if (current.getFile(i).getName().equals(filename)) {
				fileFound = true;
				path = "File " + filename + " found at " + current.getName();
			}
		}
		if (fileFound == false) {

			for (int i = 0; i < current.getDirs().size(); i++) { // search through all subdirs in current dir
				if (current.getDir(i).getName().equals(filename)) {
					fileFound = true;
					path = "Directory " + filename + " found at " + current.getName();
				}
				if (fileFound == false) {
					for (int j = 0; j < (current.getDir(i).getFiles()).size(); j++) { // search through subdir[i]
						if (current.getDir(i).getFile(j).getName().equals(filename)) {
							fileFound = true;
							path = "File " + filename + " found at " + current.getDir(i).getName();
						}

					}
					if (fileFound == false) {
						for (int j = 0; j < (current.getDir(i).getDirs()).size(); j++) { // search through subdir[i]
							if (current.getDir(i).getDir(j).getName().equals(filename)) {
								path = "Directory " + filename + "  found at " + current.getDir(i).getName();
							}

						}
					}
				}
			}
		}
		return path;

	}

	static void rm(Directory current, String name) {
		String toDelete = name.replaceAll("\"", ""); // remove double quotes from directory name
		Scanner input = new Scanner(System.in);
		ArrayList<Directory> dirs = current.getDirs();
		int indexToDelete = 99999;
		boolean hasFiles = false;
		boolean hasSubDirs = false;
		String proceed = null;
		int[] type = type(current, toDelete);

		if (type[0] == 2) {
			if (current.getDir(type[1]).canDelete()) {
				System.out.print("This will delete folder and all contents. Proceed? [y/n]: ");
				proceed = input.next();
				if (proceed.equalsIgnoreCase("y")) {
					current.getDir(type[1]).clear();
					current.deleteDir(type[1]);
				} else {
					System.out.println("Directory not deleted");
				}

			} else {
				System.out.println("Directory cannot be deleted!");
			}
		} else if (type[0] == 1) {
			if (current.getFile(type[1]).canDelete()) {
				System.out.print("This will delete the file. Proceed? [y/n]: ");
				proceed = input.next();
				if (proceed.equalsIgnoreCase("y")) {
					current.deleteFile(type[1]);
				} else {
					System.out.println("File not deleted");
				}
			} else {
				System.out.println("File cannot be deleted!");
			}
		} else {
			System.out.println("Specified object does not exist!");
		}
	}

	static void move(Directory current, String oriName, String newDir) {
		String name = oriName.replaceAll("\"", ""); // remove double quotes from directory name
		String newDirName = newDir.replaceAll("\"", ""); // remove double quotes from directory name
		Directory toDir;
		int[] fileObj = type(current, name);
		int[] newDirInfo = new int[2];

		// case 1; toDir is ".."
		// make sure we are not already in root directory
		if (newDirName.equals("..")) {
			if (current.parent == null) {
				System.out.println("In root directory, cannot process request!");
			} else {
				newDirInfo[0] = 2;
				toDir = current.parent; // ".." is the parent dir

				// case 1; object is a file
				if (fileObj[0] == 1) {
					File f = current.getFile(fileObj[1]);
					toDir.addFile(f);
					current.clearFile(fileObj[1]);

					// case 2; object is a directory
				} else if (fileObj[0] == 2) {
					Directory d = current.getDir(fileObj[1]);
					toDir.addSubDir(d);
					d.addParent(toDir);
					current.clearDir(fileObj[1]);

				} else {
					System.out.println("Invalid input!");
				}
			}
		} else {
			// case 2; toDir is the parent dir's name
			// make sure that we're not already in root directory
			if (current.parent == null) {
				System.out.println("In root folder, cannot process request!");
			} else if (current.parent.getName().equals(newDir)) {
				newDirInfo[0] = 2;
				toDir = current.parent;

				// case 1; object is a file
				if (fileObj[0] == 1) {
					File f = current.getFile(fileObj[1]);
					toDir.addFile(f);
					current.clearFile(fileObj[1]);

					// case 2; object is a directory
				} else if (fileObj[0] == 2) {
					Directory d = current.getDir(fileObj[1]);
					toDir.addSubDir(d);
					d.addParent(toDir);
					current.clearDir(fileObj[1]);

				} else {
					System.out.println("Invalid input!");
				}
			}
			// case 3; toDir is a subDir's name
			else if (current.isSubDir(newDir)) {
				newDirInfo = type(current, newDirName);
				if (newDirInfo[0] == 2) {
					toDir = current.getDir(newDirInfo[1]);

					// case 1; object is a file
					if (fileObj[0] == 1) {
						File f = current.getFile(fileObj[1]);
						toDir.addFile(f);
						current.clearFile(fileObj[1]);

						// case 2; object is a directory
					} else if (fileObj[0] == 2) {
						Directory d = current.getDir(fileObj[1]);
						toDir.addSubDir(d);
						d.addParent(toDir);
						current.clearDir(fileObj[1]);
					} else {
						System.out.println("Invalid input!");
					}
				}
			}
		}
	}

	static void cat(Directory current, String fname)
	{
		int fileIndex = 0;
		boolean isFile = false;
		String toPrint = fname.replaceAll("\"", "");
		// search through files for matching name
		for (int i = 0; i < current.getFiles().size(); i++){
			if (current.getFile(i).getName().equals(toPrint)){
				isFile = true;
				fileIndex = i;
			}
		}
		if(isFile)
		{
			String magicNum = current.getFile(fileIndex).getmNum();
			System.out.println(block.getContent(magicNum));
		}
	}

	static int findFile(Directory search, String fname)
	{
		for (int i = 0; i < search.getFiles().size(); i++){
			if (search.getFile(i).getName().equals(fname)){
				return i;
			}
		}
		return -1;
	}

	static int[] type(Directory current, String name) {
		Scanner input = new Scanner(System.in);
		String toMove = name.replaceAll("\"", ""); // remove double quotes from name
		int[] fileType = new int[2];
		int fileOrFolder = 3;
		boolean isFile = false;
		boolean isDir = false;
		int dirIndex = 0;
		int fileIndex = 0;

		// search through subdirectories for matching name
		for (int i = 0; i < current.getDirs().size(); i++) {
			if (current.getDir(i).getName().equals(toMove)) {
				isDir = true;
				dirIndex = i;
			}
		}

		// search through files for matching name
		for (int i = 0; i < current.getFiles().size(); i++) {
			if (current.getFile(i).getName().equals(toMove)) {
				isFile = true;
				fileIndex = i;
			}
		}

		if (isDir && isFile) {
			System.out.println(
					"Found 1 file and 1 folder matching that name. Which do you want to rename? [1] file [2] folder: ");
			fileOrFolder = input.nextInt();
			input.close();
			if (fileOrFolder == 2) {
				fileType[0] = 2;
				fileType[1] = dirIndex;
			} else if (fileOrFolder == 1) {
				fileType[0] = 1;
				fileType[1] = fileIndex;
			} else {
				System.out.println("Input not valid");
			}
		} else if (isDir && !isFile) {
			fileType[0] = 2;
			fileType[1] = dirIndex;
		} else if (isFile && !isDir) {
			fileType[0] = 1;
			fileType[1] = fileIndex;
		} else {
			fileType[0] = 9999;
			fileType[1] = 9999;
		}
		return fileType;
	}

	static void write(Directory current, String name){
		Scanner in = new Scanner(System.in);
		String file = name.replaceAll("\"", ""); // remove double quotes from name
		int [] type = type(current, file);
		// name refers to a file object
		if (type[0] == 1) {
			// get access to file using dir index
			File f = current.getFile(type[1]);
			// feed file to handler
			w_handling(f);
		}
		// name refers to a dir object
		else if (type[0] == 2){
			System.out.println("Cannot write to directories");
		}
		// no object w/ that name exists
		else {
			System.out.println("File does not exist in current directory! Create file? [y/n]");
			String input = in.next();
			if (input.equalsIgnoreCase("y")){
				mkfile(current, file);
			}
		}
	}

	static void w_handling(File name){
		Scanner in = new Scanner(System.in);
		//ArrayList<String> content = name.getContent();
		ArrayList<String> buffer = new ArrayList<String>();
		while(true){
			r_handling(name);
			System.out.print(">>> ");
			String input = in.nextLine();
			if (input.equalsIgnoreCase("#x")){
				break;
			}
			buffer.add(input);
			//content.add(input);
		}
		//Putting all of the content into a buffer and then writing that to a file via the FCB
		String content = "";
		for (String thing: buffer) {
			content += thing + '\n';
		}
		block.addContent(name.getmNum(), content);
	}

	static void overwrite(Directory current, String name){
		Scanner in = new Scanner(System.in);
		String file = name.replaceAll("\"", ""); // remove double quotes from name
		int [] type = type(current, file);
		// name refers to a file object
		if (type[0] == 1) {
			// get access to file using dir index
			File f = current.getFile(type[1]);
			// feed file to handler
			ow_handling(f);
		}
		// name refers to a dir object
		else if (type[0] == 2){
			System.out.println("Cannot write to directories");
		}
		// no object w/ that name exists
		else {
			System.out.println("File does not exist in current directory! Create file? [y/n]");
			String input = in.next();
			if (input.equalsIgnoreCase("y")){
				mkfile(current, file);
			}
		}
	}

	static void ow_handling(File name){
		Scanner in = new Scanner(System.in);
		ArrayList<String> content = name.getContent();
		while(content.size()>0){
			r_handling(name);
			System.out.print("\nLine to change: ");
			int index = in.nextInt();
			in.nextLine();
			if (index == 9999){
				break;
			}
			System.out.print("Enter new line: ");
			String line = in.nextLine();
			content.set(index, line);
		}
		if (content.size()==0){
			System.out.println("No content. Can't overwrite nothing!");
		}
	}

	static void read(Directory current, String name){
		Scanner in = new Scanner(System.in);
		String file = name.replaceAll("\"", ""); // remove double quotes from name
		int [] type = type(current, file);
		// name refers to a file object
		if (type[0] == 1) {
			// get access to file using dir index
			File f = current.getFile(type[1]);
			// feed file to handler
			r_handling(f);
		}
		// name refers to a dir object
		else if (type[0] == 2){
			System.out.println("Cannot read directories");
		}
		// no object w/ that name exists
		else {
			System.out.println("File does not exist in current directory! Create file? [y/n]");
			String input = in.next();
			if (input.equalsIgnoreCase("y")){
				mkfile(current, file);
			}
		}
	}

	static void r_handling(File name){
		ArrayList<String> content = name.getContent();

		if (content.size() == 0){
			System.out.println("\nCurrent Contents:");
			System.out.println("*** No Content ***\n");
		} else {
			System.out.println("\nCurrent Contents:");
			for (int i = 0; i < content.size(); i++) {
				System.out.println("[" + i + "] " + content.get(i));
			}
		}
	}
	
	static void meta(Directory current, String name){
		File f1 = new File(name, true, true, true, true); // file object
		f1.filePath = Paths.get(name).toAbsolutePath(); // retrieves path of file whose name was entered in command line
		System.out.println("Location: " + f1.filePath); // displays path where file is located
		LocalDateTime obj = LocalDateTime.now(); // retrieves current date and time
		DateTimeFormatter formatted = DateTimeFormatter.ofPattern("MM-dd-yyyy HH:mm:ss");
		String formattedDateTime = obj.format(formatted);
		f1.dateCreated = formattedDateTime;
		System.out.println("Date created: " + f1.dateCreated); // shows date and time file was created
		f1.dateLastAccessed = formattedDateTime;
		System.out.println("Date last accessed: " + f1.dateLastAccessed);
	}
}