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
        splash.changeStatus(Constants.appName, 10);

        Login login = new Login();
        Shared.centerFrame(login);
        login.setVisible(true);

        splash.setVisible(false);
        
    }
}
