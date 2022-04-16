import java.util.ArrayList;
public class File {
 
    public String mNum;
    public String name;
    private ArrayList<String> content = new ArrayList<String>(); 
 
    public File(String name) {
        this.name = name;
        this.content = null;
    }
    
    public void rename(String newName){
        this.name = newName;
    }
    
    public String getName(){
        return this.name;
    }
    
    public void addContent(String line){
        this.content.add(line);
    }
}