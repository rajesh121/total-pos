package totalpos;

/**
 *
 * @author Saul Hidalgo.
 */
public class Main {

    protected static StartSplash splash;

    public static void main(String[] args) {
        splash = new StartSplash();
        Shared.centerFrame(splash);
        splash.setVisible(true);
        
        splash.changeStatus("Conectado a base de datos...", 30);
        assert (ConnectionDrivers.initialize());

        splash.changeStatus("Creando ventana de login...", 60);
        Login login = new Login();
        Shared.centerFrame(login);

        login.setVisible(true);

        splash.setVisible(false);

    }
}
