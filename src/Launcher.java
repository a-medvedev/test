/**
 * Created with IntelliJ IDEA.
 * User: tantal
 * Date: 02.02.14
 * Time: 9:08
 * To change this template use File | Settings | File Templates.
 */
import java.util.Map;
import java.util.HashMap;

public class Launcher {
    static private Map<String, String> map;
    static {
        map = new HashMap<String, String>();
        map.put("First name", "John");
        map.put("Second name", "Doe");
        map.put("Prison", "Guantanamo");
        map.put("Limitation", "For term of life");
    }

    public static void main(String[] args){
        for ( String s : map.keySet() )
            System.out.println(s + ": " + map.get(s));
    }
}
