package totalpos;

/**
 *
 * @author Saúl Hidalgo
 */
public class Column {
    private String name;
    private String fieldName;
    private String myClass;

    public Column(String name, String fieldName, String myClass) {
        this.name = name;
        this.fieldName = fieldName;
        this.myClass = myClass;
    }

    public String getFieldName() {
        return fieldName;
    }

    public String getMyClass() {
        return myClass;
    }

    public String getName() {
        return name;
    }
}
