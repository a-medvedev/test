package DBHighLoader;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Loader {
    private Map<Application, Set<String>> appMap;

    public void generateAppMap(int appCount){
        if (appCount < 1){
            return;
        } else {

        }
    }


    //создает и возвращает список случайных приложений
    private List<Application> generateAppList(int appCount){
        //Создаем список для всех приложений сразу, размерностью равной кол-ву приложений
        List<Application> result = new ArrayList<Application>(appCount);
        for (int i = 0; i < appCount; i++){

        }
        return result;
    }

    //создает и возвращает одно, случайно созданное приложение
    private Application generateApp(){
        Application app = new Application();
        String appId, url;
        Long addingDate = System.currentTimeMillis();

        return app;
    }
}
