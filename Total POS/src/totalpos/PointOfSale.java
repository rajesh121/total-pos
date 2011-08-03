package totalpos;

/**
 *
 * @author Saul Hidalgo
 */
public class PointOfSale {
    private String id;
    private String description;
    private String printer;

    public PointOfSale(String id, String description, String printer) {
        this.id = id;
        this.description = description;
        this.printer = printer;
    }

    public String getDescription() {
        return description;
    }

    public String getId() {
        return id;
    }

    public String getPrinter() {
        return printer;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setPrinter(String printer) {
        this.printer = printer;
    }

}
