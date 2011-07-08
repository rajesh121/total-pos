package totalpos;

/**
 *
 * @author Saul Hidalgo.
 */
public class Main {

    public static void main(String[] args) {
        StartSplash splash = new StartSplash();
        splash.setVisible(true);
        Shared.centerFrame(splash);
        splash.changeStatus(Constants.appName, 10);

    }
}
