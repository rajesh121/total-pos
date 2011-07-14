package totalpos;

/**
 *
 * @author Saul Hidalgo.
 */
public class Main {

    public static void main(String[] args) {
        StartSplash splash = new StartSplash();
        Shared.centerFrame(splash);
        splash.setVisible(true);
        
        splash.changeStatus("Conectado a base de datos...", 50);
        assert (ConnectionDrivers.initialize());

        Login login = new Login();
        Shared.centerFrame(login);
        login.setVisible(true);

        splash.setVisible(false);

    }
}
