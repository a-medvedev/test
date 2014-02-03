package DBHighLoader;

import DBHighLoader.db.Connector;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Map;
import java.util.Set;

/**
 * User: tantal
 * Date: 02.02.14
 * Time: 11:43
 */
public class Launcher {
    public static void main(String[] args) throws SQLException {
//        Loader l = new Loader();
//        l.generateAppMap(1000000);
//        Map<Application, Set<String>> appMap = l.getAppMap();
        Connector conn = new Connector("localhost", "parser", "secret", "google_parser", "5432", "psql");
        Connection c = conn.getConnection();
        Statement st = c.createStatement();
        st.execute("drop table if exists tmp_apps;");
        st.execute("create table if not exists tmp_apps(id text NOT NULL, url text NOT NULL, adding_date bigint NOT NULL, country text NOT NULL);");
        //ResultSet rs = st.getResultSet();

        Loader l = new Loader();
        l.generateAppMap(1000000);

        //добавить вставку значений в таблицу
        DBHelper dbh = new DBHelper();
        dbh.storeApps(l.getAppMap());


        //создаем таблицу из ID приложений для сверки.
        st.execute("drop table if exists tmp_apps_id;");
        st.execute("create table tmp_apps_id as select id from apps;");
        st.execute("create index tmp_apps_idx on tmp_apps_id(id);");

        //создаем таблицу приложений для вставки
        st.execute("drop table if exists insert_apps;");
        st.execute("create table insert_apps as select * from tmp_apps where id not in (select id from tmp_apps_id);");

        //создаем таблицу приложений для обновления
        st.execute("drop table if exists update_apps;");
        st.execute("create table update_apps as select * from tmp_apps where id in (select id from tmp_apps_id);");

        //удаляем временные таблицы
        st.execute("drop table if exists tmp_apps_id;");
        st.execute("drop table if exists tmp_apps;");
        st.execute("drop table if exists insert_apps;");
        st.execute("drop table if exists update_apps;");

        st.close();
    }
}
