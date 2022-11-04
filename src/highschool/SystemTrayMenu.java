/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package highschool;

import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * @author FREDDY
 */
public class SystemTrayMenu extends PopupMenu implements ActionListener {
    MenuItem restart = new MenuItem("Restart Sever");
    MenuItem exit = new MenuItem("Exit");


    public SystemTrayMenu() {
        this.add(restart);
        this.add(exit);
        exit.addActionListener(this);
        restart.addActionListener(this);
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
