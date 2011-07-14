package totalpos;

/**
 *
 * @author Saul Hidalgo.
 */
public class Main {

    protected static StartSplash splash;
    protected static Login login;

    public static void main(String[] args) {
        splash = new StartSplash();
        Shared.centerFrame(splash);
        splash.setVisible(true);
        
        splash.changeStatus("Conectado a base de datos...", 50);
        assert (ConnectionDrivers.initialize());

        login = new Login();
        Shared.centerFrame(login);
        login.setVisible(true);

        splash.setVisible(false);

    }
}
