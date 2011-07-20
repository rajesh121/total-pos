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
    int bloqueado;

    public User(String login, String password, String perfil) {
        this.login = login;
        this.password = password;
        this.perfil = perfil;
    }

    public User(String login, String password, String perfil, String nombre, String apellido, String cedula, String direccion, int bloqueado) {
        this.login = login;
        this.password = password;
        this.perfil = perfil;
        this.nombre = nombre;
        this.apellido = apellido;
        this.bloqueado = bloqueado;
    }

    public String getApellido() {
        return apellido;
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

    public boolean getBloqueado(){
        return bloqueado != 0;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public void setBloqueado(int bloqueado) {
        this.bloqueado = bloqueado;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setPerfil(String perfil) {
        this.perfil = perfil;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final User other = (User) obj;
        if ((this.login == null) ? (other.login != null) : !this.login.equals(other.login)) {
            return false;
        }
        return true;
    }

}
