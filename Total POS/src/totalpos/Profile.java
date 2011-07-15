package totalpos;

/**
 *
 * @author Saul Hidalgo
 */
public class Profile {
    private String id;
    private String description;

    public Profile(String id, String description) {
        this.id = id;
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public String getId() {
        return id;
    }

}
