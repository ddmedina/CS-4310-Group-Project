import java.util.ArrayList;

public class Directory {
 
    //public String mNum;
    private String name;
    public Directory parent = null;
    private ArrayList<Directory> subDirs = new ArrayList<Directory>();
    private ArrayList<File> fileObjs = new ArrayList<File>();
 
    public Directory(String name) {
        this.name = name;
    }
    
    public void addParent(Directory parent){
        this.parent = parent;
    }
    
    public void rename(String newName){
        this.name = newName;
    }
    
    public String getName(){
        return this.name;
    }
 
    public void addSubDir(Directory newDir) {
        this.subDirs.add(newDir);
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
    
    public ArrayList<File> getFiles() {
        return this.fileObjs;
    }

    public File getFile(int i) {
        return this.fileObjs.get(i);
    }
 
}