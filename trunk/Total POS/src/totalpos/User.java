package totalpos;

/**
 *
 * @author Saul Hidalgo.
 */
public class User {
    String login;
    String password;
    String perfil;
    String nombre;
    String apellido;
    String cedula;
    String direccion;

    public User(String login, String password, String perfil) {
        this.login = login;
        this.password = password;
        this.perfil = perfil;
    }

    public User(String login, String password, String perfil, String nombre, String apellido, String cedula, String direccion) {
        this.login = login;
        this.password = password;
        this.perfil = perfil;
        this.nombre = nombre;
        this.apellido = apellido;
        this.cedula = cedula;
        this.direccion = direccion;
    }

    public String getApellido() {
        return apellido;
    }

    public String getCedula() {
        return cedula;
    }

    public String getDireccion() {
        return direccion;
    }

    public String getLogin() {
        return login;
    }

    public String getNombre() {
        return nombre;
    }

    public String getPassword() {
        return password;
    }

    public String getPerfil() {
        return perfil;
    }

}
