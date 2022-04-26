import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Random;
import java.security.MessageDigest;
public class File {
 
    public String mNum;
    public String name;
    private ArrayList<String> content = new ArrayList<String>();
    private boolean renameable = false;
    private boolean moveable = false;
    private boolean writeable = false;
    private boolean deleteable = false;

    private static final int SALTLENGTH = 5;
 
    public File(String name, boolean renameable, boolean moveable, boolean writeable, boolean deleteable) {
        this.name = name;
        this.mNum = generatemNum(name);
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

    public String getmNum() { return this.mNum; }
    
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


    //Generating magic number by adding random numbers to the end of the file name then taking the MD5 hash of it
    private String generatemNum(String name) {
        Random r = new Random();
        for(int i = 0; i < SALTLENGTH; i++) {
            name += String.valueOf(r.nextInt(10));

        }
        //System.out.println(name);
        try{
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(name.getBytes());
            byte[] digest = md.digest();
            StringBuilder sb = new StringBuilder();
            for (byte b : digest) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        }
        catch (NoSuchAlgorithmException e)
        {
            throw new RuntimeException(e);
        }
    }
}