package DBHighLoader;

import DBHighLoader.db.Connector;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Map;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: medvedev
 * Date: 03.02.14
 */

public class DBHelper {
    public void storeApps(Map<Application, Set<String>> appMap) throws SQLException {
        Connector conn = new Connector("localhost", "parser", "secret", "google_parser", "5432", "psql");
        Connection c = conn.getConnection();
        PreparedStatement ps = c.prepareStatement("insert into tmp_apps (id, url, adding_date, country) values (?, ?, ?, ?);");

        for (Application a : appMap.keySet()){
            ps.setString(1, a.getId());
            ps.setString(2, a.getUrl());
            ps.setLong(3, a.getAddingDate());
            ps.setString(4, a.getCountry());
            ps.execute();
        }

        c.close();
    }

}
