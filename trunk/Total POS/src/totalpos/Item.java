package totalpos;

/**
 *
 * @author shidalgo
 */
public class Item {
    private String code;
    private String description;
    private String model;
    private double price;
    private String imageAddr;

    public Item(String code, String description, String model, double price, String imageAddr) {
        this.code = code;
        this.description = description;
        this.model = model;
        this.price = price;
        this.imageAddr = imageAddr;
    }

    public String getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    public String getModel() {
        return model;
    }

    public double getPrice() {
        return price;
    }

    public String getImageAddr() {
        return imageAddr;
    }

}
