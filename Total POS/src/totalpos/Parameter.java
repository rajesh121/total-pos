package totalpos;

import javax.swing.JLabel;
import javax.swing.JTextField;

/**
 *
 * @author Saúl Hidalgo
 */
public class Parameter {
    private String type;
    private String formName;
    private String fieldName;
    private JLabel label;
    private JTextField textField;

    public Parameter(String type, String formName, String fieldName, JLabel label, JTextField textField) {
        this.type = type;
        this.formName = formName;
        this.fieldName = fieldName;
        this.label = label;
        this.textField = textField;
    }

    public String getFieldName() {
        return fieldName;
    }

    public String getFormName() {
        return formName;
    }

    public JLabel getLabel() {
        return label;
    }

    public JTextField getTextField() {
        return textField;
    }

    public String getType() {
        return type;
    }

    public void setLabel(JLabel label) {
        this.label = label;
    }

    public void setTextField(JTextField textField) {
        this.textField = textField;
    }
    
}
