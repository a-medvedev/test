package DBHighLoader;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

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

    //создает и возвращает одно, случайное приложение
    private Application generateApp(){
        Application app = new Application();
        app.setId(generateId(15));
        app.setAddingDate(generateDate());
        app.setUrl(generateUrl());
        app.setCountry(generateId(2));
        return app;
    }

    //создает и возвращает одно, случайное приложение с указанным id
    private Application generateApp(String id){
        Application app = new Application();
        app.setId(id);
        app.setAddingDate(generateDate());
        app.setUrl(generateUrl());
        app.setCountry(generateId(2));
        return app;
    }

    //создает id из набоа букв, выбранных случайно, указанной размерности
    //при некорретно указанной размерности возвращает пустую строку
    public String generateId(int size){
        if (size < 1){
            return "";
        }
        char[] alpha = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z'};
        int alphaSize = alpha.length;
        Random r = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < size; i++){
            char c = alpha[r.nextInt(alphaSize)];
            sb.append(c);
        }
        return sb.toString();
    }

    //генерирует рандомный УРЛ в виде строки
    public String generateUrl(){
        StringBuilder url = new StringBuilder();
        Random r = new Random();
        int length = r.nextInt(3) + 4;
        url.append("http://www.").append(generateId(length)).append(".").append(generateId(2)).append("/");
        length = r.nextInt(3) + 5;
        url.append(generateId(length));
        return url.toString();
    }

    public long generateDate(){
        Random r = new Random();
        return  (long)(System.currentTimeMillis() * r.nextGaussian());
    }

    public List<Application> createAppsFromExistedIds() throws FileNotFoundException {
        List<String> existedAppsIds = new ArrayList<String>();
        File existedIds = new File("src/DBHighLoader/existedIds.txt");
        Scanner s = new Scanner(existedIds);
        if (existedIds != null && !existedIds.exists()){
            while (s.hasNextLine()){
                existedAppsIds.add(s.nextLine());
            }
        }
        List<Application> result = new ArrayList<Application>();
        for (String id : existedAppsIds){
            result.add(generateApp(id));
        }
        return result;
    }
}
