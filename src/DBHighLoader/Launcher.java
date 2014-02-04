package DBHighLoader;

import DBHighLoader.db.Connector;

import java.sql.*;
import java.util.Map;
import java.util.Set;

/**
 * User: tantal
 * Date: 02.02.14
 * Time: 11:43
 */
public class Launcher {
    public static void main(String[] args) {
//        Loader l = new Loader();
//        l.generateAppMap(1000000);
//        Map<Application, Set<String>> appMap = l.getAppMap();
        System.currentTimeMillis();
        Connector conn = new Connector("localhost", "parser", "secret", "google_parser", "5432", "psql");
        Connection c = conn.getConnection();
        Statement st = null;
        try {
            st = c.createStatement();
            st.execute("drop table if exists tmp_apps;");
            st.execute("create table if not exists tmp_apps(id text NOT NULL, url text NOT NULL, adding_date bigint NOT NULL, country text NOT NULL);");
        } catch (SQLException e) {
            System.out.println("Создание временной таблицы для напарсенных приложений не удалось. Причина:");
            System.out.println(e.getMessage());
        }
        //ResultSet rs = st.getResultSet();
        if (st == null){
            System.out.println("Создание запроса к базе не удалось. Завершение.");
            return;
        }
        Loader l = new Loader();
        l.generateAppMap(1000000);

        //вставка значений во временную таблицу
        DBHelper dbh = new DBHelper();
        try {
            dbh.storeApps(l.getAppMap());
        } catch (SQLException e) {
            System.out.println("При добавлении приложений во временную таблица произошла ошибка: ");
            System.out.println(e.getMessage());
        }

        //создаем таблицу из ID приложений для сверки.
        try{
            st.execute("drop table if exists tmp_apps_id;");
            st.execute("create table tmp_apps_id as select id from apps;");
            st.execute("create index tmp_apps_idx on tmp_apps_id(id);");
        } catch (SQLException e) {
            System.out.println("Ошибка при создании временной таблицы tmp_apps_id. Причина: ");
            System.out.println(e.getMessage());
        }

        //создаем таблицу приложений для вставки
        try{
            st.execute("drop table if exists insert_apps;");
            st.execute("create table insert_apps as select * from tmp_apps where id not in (select id from tmp_apps_id);");
        } catch (SQLException e) {
            System.out.println("Ошибка при создании таблицы приложений для вставки. Причина: ");
            System.out.println(e.getMessage());
        }

        //отключаем индекс у основной таблицы, для быстрой вставки
        try {
            st.execute("drop index apps_idx if exists;");
        } catch (SQLException e) {
            System.out.println("Отключение индекса основной таблицы не удалось. Причинна: ");
            System.out.println(e.getMessage());
        }

        //подготовленные запросы для вставки приложения
        PreparedStatement addCountryAppStatement = null;
        PreparedStatement insertStatement = null;
        //PreparedStatement selectCountryStatement = null;
        try {
            insertStatement = c.prepareStatement("insert into apps (id, url, adding_date) values (?, ?, ?);");
            //selectCountryStatement = c.prepareStatement("select id from countries where country = ?;");
            addCountryAppStatement = c.prepareStatement("insert into apps_countries (app_id, country_id) values (?, (select id from countries where name = ?));");
        } catch (SQLException e) {
            System.out.println("Создание подготовленных запросов для вставки не удалось. Причина: ");
            System.out.println(e.getMessage());
        }

        //получение всех приложений, подготовленных ко вставке в виде ResultSet для последующих манипуляций
        ResultSet rsForInsert = null;
        try{
            rsForInsert = st.executeQuery("select id, adding_date, url, country from insert_apps;");
        } catch (SQLException e) {
            System.out.println("Невозможно получить приложения для вставки. Причина: ");
            System.out.println(e.getMessage());
        }

        //устанавливаем автокоммит в false, для пакетной обработки запросов к БД
        try {
            c.setAutoCommit(false);
        } catch (SQLException e) {
            //do nothing
        }

        //вставка приложения в основную таблицу
        if (rsForInsert != null){
            //осуществляем проход по Result Set для вставки приложений
            try{
                while(rsForInsert.next()){
                    String country = rsForInsert.getString("country");
                    String id = rsForInsert.getString("id");
                    String url = rsForInsert.getString("url");
                    Float addingDate = rsForInsert.getFloat("adding_date");
                    insertStatement.setString(1, id);
                    insertStatement.setString(2, url);
                    insertStatement.setFloat(3, addingDate);
                    try {
                        insertStatement.execute();
                    } catch (SQLException e) {
                        System.out.println("Добавление приложения в БД не удалось. Причина: ");
                        System.out.println(e.getMessage());
                    }

                    addCountryAppStatement.setString(1, id);
                    addCountryAppStatement.setString(2, country);
                    try {
                        addCountryAppStatement.execute();
                    } catch (SQLException e) {
                        System.out.println("Добавление связи приложение-страна в БД не удалось. Причина: ");
                        System.out.println(e.getMessage());
                    }
                }
            } catch (SQLException e) {
                System.out.println("Добавление в БД не удалось. Причина: ");
                System.out.println(e.getMessage());
            }
        }

        //отправляем запросы на сервер БД
        try {
            c.commit();
            rsForInsert.close();
        } catch (SQLException e) {
            System.out.println("Невозможно отослать запросы на сервер. Причина: ");
            System.out.println(e.getMessage());
        }

        //создаем таблицу приложений для обновления
        try {
            st.execute("drop table if exists update_apps;");
            st.execute("create table update_apps as select * from tmp_apps where id in (select id from tmp_apps_id);");
        } catch (SQLException e) {
            System.out.println("Создание таблицы приложений для обновления не удалось. Причина: ");
            System.out.println(e.getMessage());
        }

        ResultSet rsForUpdate = null;
        try {
            rsForUpdate = st.executeQuery("select id, adding_date, url, country from update_apps;");
        } catch (SQLException e) {
            System.out.println("Выборка приложения для обновления не удалась. Причина:\n" + e.getMessage());
        }

        if (rsForUpdate != null){
            while (rsForUpdate.next()){
                String country =
            }
        }


        //удаляем временные таблицы
        try{
            st.execute("drop table if exists tmp_apps_id;");
            st.execute("drop table if exists tmp_apps;");
            st.execute("drop table if exists insert_apps;");
            st.execute("drop table if exists update_apps;");
        } catch (SQLException e) {
            System.out.println("Удаление временных таблиц не удалось. Причина: ");
            System.out.println(e.getMessage());
        }

        //пересоздаем индекс для основной таблицы
        try{
            st.execute("create index apps_idx on apps(id)");
        } catch (SQLException e) {
            System.out.println("Создание индекса главной таблицы не удалось. Причина: ");
            System.out.println(e.getMessage());
        }


        try {
            st.close();
        } catch (SQLException e) {
            System.out.println("Проблемы при закрытии соединения с БД. Причина: ");
            System.out.println(e.getMessage());
        }
    }
}
