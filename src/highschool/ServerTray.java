/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package highschool;

import java.awt.Image;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ImageIcon;

/**
 * @author FREDDY
 */
public class ServerTray extends TrayIcon implements ActionListener {
    MenuItem restart = new MenuItem("Restart Sever");
    MenuItem exit = new MenuItem("Exit");
    ImageIcon imageIcon = new ImageIcon(getClass().getResource("/icons/icon.png"));
    PopupMenu pop = new PopupMenu();
    Image im = imageIcon.getImage();

    public ServerTray(Image image, String tooltip, PopupMenu popup) {


        super(image, tooltip, popup);


        this.setToolTip("System Results Displayed Here");


        try {
            SystemTray.getSystemTray().add(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.displayMessage("High School Pro", "System Started,Current Status: Running....", TrayIcon.MessageType.INFO);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object obj = e.getSource();
        if (obj == exit) {
            System.exit(0);
        } else if (obj == restart) {

            // new ExamServerSender();
        }
    }


}
