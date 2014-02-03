package DBHighLoader;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * User: tantal
 * Date: 02.02.14
 * Time: 11:43
 */
public class Launcher {
    public static void main(String[] args) throws IOException {
        File existedIds = new File("src/DBHighLoader/existedIds.txt");
        if (!existedIds.exists()){
            try {
                existedIds.createNewFile();
            } catch (IOException e) {
                //do nothing
            }
        }
        Loader l = new Loader();
        FileWriter fw = new FileWriter(existedIds, true);
        for (int i = 0; i < 100000; i++){
            fw.write(l.generateId(15));
            fw.write("\n");
        }
        fw.close();
    }
}
