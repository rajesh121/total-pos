package totalpos;

/**
 *
 * @author Saúl Hidalgo
 */
public class Client {
    private String id;
    private String name;
    private String address;
    private String phone;

    public Client(String id, String name, String address, String phone) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    

}
