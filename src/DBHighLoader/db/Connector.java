package DBHighLoader.db;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Created with IntelliJ IDEA.
 * User: medvedev
 * Date: 12.12.13
 */

//Фабрика для получения новых подключений к БД
//Инстанцируется через передачу конфиг-файла или прямого задания значений для соединения
public final class Connector {
    private String dbHost;
    private String dbUser;
    private String dbPass;
    private String dbName;
    private String dbPort;
    private String state;

    //Конструктор, получающий конфиг файл
    public Connector (File config, String state) {
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = null;
        Document doc = null;
        try {
            dBuilder = dbFactory.newDocumentBuilder();
            doc = dBuilder.parse(config);
        } catch (ParserConfigurationException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (SAXException e) {

        } catch (IOException e) {

        }


        if (doc != null) {
            doc.getDocumentElement().normalize();


            if ( state.equals("psql") ) {
                //инициализация соединения с Postgarage
                NodeList nList = doc.getElementsByTagName("initializationPSQL");

                for (int temp = 0; temp < nList.getLength(); temp++)
                {
                    Node nNode = nList.item(temp);
                    if (nNode.getNodeType() == Node.ELEMENT_NODE)
                    {
                        Element eElement = (Element) nNode;
                        dbHost = eElement.getElementsByTagName("db_host").item(0).getTextContent();
                        dbName = eElement.getElementsByTagName("db_name").item(0).getTextContent();
                        dbUser = eElement.getElementsByTagName("db_user").item(0).getTextContent();
                        dbPass = eElement.getElementsByTagName("db_password").item(0).getTextContent();
                        dbPort = eElement.getElementsByTagName("db_port").item(0).getTextContent();
                    }

                }
            } else if ( state.equals("mysql") ) {
                //инициализация соединения с MySQL
                NodeList nList = doc.getElementsByTagName("initializationMySQL");

                for (int temp = 0; temp < nList.getLength(); temp++)
                {
                    Node nNode = nList.item(temp);
                    if (nNode.getNodeType() == Node.ELEMENT_NODE)
                    {
                        Element eElement = (Element) nNode;
                        dbHost = eElement.getElementsByTagName("db_host").item(0).getTextContent();
                        dbName = eElement.getElementsByTagName("db_name").item(0).getTextContent();
                        dbUser = eElement.getElementsByTagName("db_user").item(0).getTextContent();
                        dbPass = eElement.getElementsByTagName("db_password").item(0).getTextContent();
                        dbPort = eElement.getElementsByTagName("db_port").item(0).getTextContent();
                    }

                }
            }
        }
        this.state = state;
    }

    //Конструктор непосредственно задающий значения для соединения
    public Connector (String dbHost, String dbUser, String dbPass, String dbName, String dbPort, String state) {
        this.dbHost = dbHost;
        this.dbUser = dbUser;
        this.dbPass = dbPass;
        this.dbName = dbName;
        this.dbPort = dbPort;
        this.state = state;
    }

    //фабричный метод для получения нового экземпляра Connection из имеющихся настроек
    public Connection getConnection() {
        Connection connection = null;
        //соединение с Postgarage
        if ( state.equals("psql") ) {
            try {
                Class.forName("org.postgresql.Driver");
            } catch (ClassNotFoundException e) {
                //e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                return null;
            }
            StringBuilder connectionUrlBuilder = new StringBuilder();
            connectionUrlBuilder.append("jdbc:postgresql://").append(dbHost).append(":").append(dbPort).append("/").append(dbName);
            try {
                connection = DriverManager.getConnection(connectionUrlBuilder.toString(), dbUser, dbPass);
            } catch (SQLException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
        }
        //соединение с MySQL
        if ( state.equals("mysql") ) {
            try {
                Class.forName("com.mysql.jdbc.Driver");
            } catch (ClassNotFoundException e) {
                return null;
            }
            StringBuilder connectionUrlBuilder = new StringBuilder();
            connectionUrlBuilder.append("jdbc:mysql://").append(dbHost).append(":").append(dbPort).append("/").append(dbName);
            try {
                connection = DriverManager.getConnection(connectionUrlBuilder.toString(), dbUser, dbPass);
            } catch (SQLException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
        }
        return connection;
    }
}
