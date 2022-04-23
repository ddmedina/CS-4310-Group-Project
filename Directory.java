import java.util.ArrayList;

public class Directory {
 
    //public String mNum;
    private String name;
    public Directory parent = null;
    private boolean renameable = false;
    private boolean moveable = false;
    private boolean writeable = false;
    private boolean deleteable = false;
    private ArrayList<Directory> subDirs = new ArrayList<Directory>();
    private ArrayList<File> fileObjs = new ArrayList<File>();
 
    public Directory(String name, boolean renameable, boolean moveable, boolean writeable, boolean deleteable) {
        this.name = name;
        this.renameable = renameable;
        this.moveable = moveable;
        this.writeable = writeable;
        this.deleteable = deleteable;
    }
    
    public void addParent(Directory parent){
        this.parent = parent;
    }
    
    public void rename(String newName){
        if (renameable){
            this.name = newName;
        }
        else {
            System.out.println("Cannot rename this folder!");
        }
    }
    
    public String getName(){
        return this.name;
    }
 
    public void addSubDir(Directory newDir) {
        this.subDirs.add(newDir);
    }

    public boolean isSubDir(String dir){
        boolean subDir = false;
        for (int i = 0; i < subDirs.size(); i++){
            if (subDirs.get(i).getName().equals(dir)){
                subDir = true;
            }
        }
        return subDir;
    }

    public ArrayList<Directory> getDirs() {
        return this.subDirs;
    }
    
    public Directory getDir(int i){
        return this.subDirs.get(i);
    }
    
    public void addFile(File newFile){
        this.fileObjs.add(newFile);
    }

    public void clearFile(int i){
        this.fileObjs.remove(1);
    }

    public void clearDir(int i){
        this.subDirs.remove(i);
    }
    
    public ArrayList<File> getFiles() {
        return this.fileObjs;
    }

    public File getFile(int i) {
        return this.fileObjs.get(i);
    }

    public boolean canDelete() {
        return this.deleteable;
    }

    public boolean canMove() {
        return this.moveable;
    }

    public boolean canWrite() {
        return this.writeable;
    }

    public boolean canRename() {
        return this.renameable;
    }

    public boolean deleteDir(int indexToDelete){
        this.subDirs.clear();
        this.fileObjs.clear();
        this.subDirs.remove(indexToDelete);
        return true;
    }

    public boolean deleteFile(int indexToDelete){
        this.fileObjs.remove(indexToDelete);
        return true;
    }

}