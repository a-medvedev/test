package DBHighLoader;

/**
 * User: tantal
 * Date: 02.02.14
 * Time: 11:27
 */
public class Application {
    private String id;
    private String url;
    private Long addingDate;

    public Application(){
        id = "";
        url = "";
        addingDate = 0L;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Long getAddingDate() {
        return addingDate;
    }

    public void setAddingDate(Long addingDate) {
        this.addingDate = addingDate;
    }
}
