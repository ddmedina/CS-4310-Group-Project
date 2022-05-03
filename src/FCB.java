import java.util.HashMap;
import java.util.Enumeration;

public class FCB {
    private HashMap<String, String> files = new HashMap<String, String>();

    public void addContent(String MagicNumber, String content)
    {
        files.put(MagicNumber, content);
    }
    public String getContent(String MagicNumber)
    {
        return files.get(MagicNumber);
    }
    public void deleteContent(String MagicNumber) { files.remove(MagicNumber); }
}
