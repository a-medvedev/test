package DBHighLoader;

import java.util.Map;
import java.util.Set;

/**
 * User: tantal
 * Date: 02.02.14
 * Time: 11:43
 */
public class Launcher {
    public static void main(String[] args) {
        Loader l = new Loader();
        l.generateAppMap(1000000);
        Map<Application, Set<String>> appMap = l.getAppMap();
    }
}
