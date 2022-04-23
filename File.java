import java.util.ArrayList;
public class File {
 
    public String mNum;
    public String name;
    private ArrayList<String> content = new ArrayList<String>();
    private boolean renameable = false;
    private boolean moveable = false;
    private boolean writeable = false;
    private boolean deleteable = false;
 
    public File(String name, boolean renameable, boolean moveable, boolean writeable, boolean deleteable) {
        this.name = name;
        this.renameable = renameable;
        this.moveable = moveable;
        this.writeable = writeable;
        this.deleteable = deleteable;
        this.content = null;
    }
    
    public void rename(String newName){
        if (this.renameable){
            this.name = newName;
        }
    }
    
    public String getName(){
        return this.name;
    }
    
    public void addContent(String line){
        if (this.writeable){
            this.content.add(line);
        }
    }

    public boolean canDelete(){
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
}