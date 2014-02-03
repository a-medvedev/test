package DBHighLoader.db;

import v1.application.Application;
import v1.author.Author;
import v1.category.Category;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: medvedev
 * Date: 13.12.13
 */

//вспомогательный класс для работы с БД. Извлекает категории, авторов, приложения в виде списка
public final class DBHelper {

    private final Connector connectionFactory;

    public DBHelper(Connector connector){
        this.connectionFactory = connector;
    }


    //извлекает категории  из БД
    public List<Category> getCategories() throws SQLException {
        return null;   //необходимо убрать эту заглушку
    }

    //извлекает авторов из БД
    public List<Author> getAuthors() throws SQLException, ClassNotFoundException {
        Connection connection = connectionFactory.getConnection();
        Statement statement = connection.createStatement();
        ResultSet ListAuthor = null;
        List<Author> author_list = new ArrayList<Author>();
        Author author = new Author();
        try {
            ListAuthor = statement.executeQuery("SELECT authors.id, authors.name, authors.url, authors.date_add, authors.country, markets.name FROM authors, markets WHERE (authors.market_id = markets.id);");
            while ( ListAuthor.next() ){
                author.setId(ListAuthor.getString(1));
                author.setName(ListAuthor.getString(2));
                author.setUrl(ListAuthor.getString(3));
                author.setAddingDate(ListAuthor.getTimestamp(4).getTime());
                author.setMarketId(ListAuthor.getString(5));
                author_list.add(author);
            }
            ListAuthor.close();
            return author_list;
        } catch (SQLException e)
        {
            System.out.println(e.getMessage());
            return author_list;
        }
        finally {
            if (ListAuthor != null) {
                ListAuthor.close();
            }
            if (connection != null) {
                connection.close();
            }
            return author_list;
        }
    }

    //извлекает приложения из БД
    public List<Application> getApplications() throws SQLException, ClassNotFoundException {
        Connection connection = connectionFactory.getConnection();
        Statement statement = connection.createStatement();
        ResultSet ListApplications = null;
        List<Application> applications_list = new ArrayList<Application>();
        Application application = new Application();
        try {
            ListApplications = statement.executeQuery("SELECT applications.id, applications.name, applications.url, authors.id, applications.scraping_date, applications.publish_date, applications.price, applications.rate, applications.country, markets.name FROM authors, markets, authors WHERE ( (applications.market_id = markets.id) AND (applications.author_id = authors.id) );");
            while ( ListApplications.next() ){
                application.setId(ListApplications.getString(1));
                application.setName(ListApplications.getString(2));
                application.setUrl(ListApplications.getString(3));
                application.setAuthorId(ListApplications.getString(4));
                application.setScraping_date(ListApplications.getTimestamp(5).getTime());
                application.setPublish_date(ListApplications.getTimestamp(6).getTime());
                application.setPrice(ListApplications.getString(7));
                application.setRating(ListApplications.getFloat(8));
                //написать метод для извлечения и заполнения списка стран
                application.setMarketId(ListApplications.getString(10));
                applications_list.add(application);
            }
            ListApplications.close();
            return applications_list;
        } catch (SQLException e)
        {
            System.out.println(e.getMessage());
            return applications_list;
        }
        finally {
            if (ListApplications != null) {
                ListApplications.close();
            }
            if (connection != null) {
                connection.close();
            }
            return applications_list;
        }
    }
}
