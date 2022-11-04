package highschool;


import java.awt.*;
import java.util.Date;
import javax.swing.*;
import javax.swing.border.TitledBorder;

import net.miginfocom.swing.MigLayout;

public class SplashScreen extends JWindow {

    public static void main(String[] args) {
        // Throw a nice little title page up on the screen first
        SplashScreen splash = new SplashScreen(1500);

        // Normally, we'd call splash.showSplash() and get on
        // with the program. But, since this is only a test...
        splash.showSplashAndExit();
    }

    private int duration;

    public SplashScreen(int d) {
        duration = d;
    }

    // A simple little method to show a title screen in the center
    // of the screen for the amount of time given in the constructor
    public void showSplash() {

        JPanel content = (JPanel) getContentPane();
        content.setBackground(Color.white);
        JProgressBar bar = new JProgressBar();
        bar.setIndeterminate(true);
        bar.setBorder(new TitledBorder("System Intializing Start Up Parameters...."));
        // Set the window's bounds, centering the window
        int width = 600;
        int height = 300;
        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (screen.width - width) / 2;
        int y = (screen.height - height) / 2;
        setBounds(x, y, width, height);
        int year = new Date().getYear() + 1900;
        // Build the splash screen
        JLabel label = new JLabel(new ImageIcon(getClass().getResource("/images/logo.png")));
        JLabel copyrt = new JLabel
                ("Copyright " + year + "  Lunar Tech Solution", FredLabel.CENTER);
        copyrt.setFont(new Font("Serif", Font.BOLD, 17));
        content.setLayout(new MigLayout());
        content.add(label, "grow,push,wrap");
        content.add(bar, "growx,pushx,wrap");
        content.add(copyrt, "growx,pushx");
        Color oraRed = new Color(156, 20, 20, 255);
        content.setBorder(BorderFactory.createLineBorder(Color.MAGENTA, 5));

        // Display it
        setVisible(true);

        // Wait a little while, maybe while loading resources
        try {
            Thread.sleep(duration);
        } catch (InterruptedException e) {
        }
        new Login().setVisible(true);
        DocHead pp = new DocHead();
        SystemTrayMenu menu = new SystemTrayMenu();
        ServerTray tray = new ServerTray(pp.trayIcon().getImage(), "System Started", menu);
        Globals.systemTray = tray;


    }

    public void showSplashAndExit() {

        showSplash();
        this.dispose();

    }

}