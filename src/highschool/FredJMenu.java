/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package highschool;

import java.awt.Cursor;
import java.awt.Font;
import javax.swing.ImageIcon;
import javax.swing.JMenu;

/**
 * @author FRED
 */
public class FredJMenu extends JMenu {
    public FredJMenu(String t) {
        Cursor cur = new Cursor(Cursor.HAND_CURSOR);
        this.setCursor(cur);
        this.setText(t);
        setFont(new Font("serif", Font.BOLD, 18));
        if (t.equalsIgnoreCase("Home")) {
            this.setIcon(new ImageIcon(getClass().getResource("/icons/Home-icon.png")));
        }
        if (t.startsWith("Commun")) {
            this.setIcon(new ImageIcon(getClass().getResource("/icons/Communication-Tower-icon.png")));
        }
        if (t.startsWith("Repor")) {
            this.setIcon(new ImageIcon(getClass().getResource("/icons/Reports-icon.png")));
        }


        if (t.startsWith("Master")) {
            this.setIcon(new ImageIcon(getClass().getResource("/icons/Control-Panel-icon.png")));
        }
        if (t.contains("elp")) {
            this.setIcon(new ImageIcon(getClass().getResource("/icons/Help-Support-icon.png")));
        }
        if (t.startsWith("Payment")) {
            this.setIcon(new ImageIcon(getClass().getResource("/icons/payment-icon.png")));
        }
        if (t.startsWith("Expen")) {
            this.setIcon(new ImageIcon(getClass().getResource("/icons/Invoice-icon.png")));
        }

        if (t.startsWith("Regi")) {
            this.setIcon(new ImageIcon(getClass().getResource("/icons/Registration-icon.png")));
        }
        if (t.startsWith("Fee")) {
            this.setIcon(new ImageIcon(getClass().getResource("/icons/Status-wallet-open-icon.png")));
        }
        if (t.startsWith("View B")) {
            this.setIcon(new ImageIcon(getClass().getResource("/icons/Books-icon.png")));
        }
        if (t.startsWith("Contact")) {
            this.setIcon(new ImageIcon(getClass().getResource("/icons/list.png")));
        }

        if (t.startsWith("Book")) {
            this.setIcon(new ImageIcon(getClass().getResource("/icons/Actions-arrow-right-double-icon.png")));
        }

        if (t.startsWith("My A")) {
            this.setIcon(new ImageIcon(getClass().getResource("/icons/user-info-icon.png")));
        }
        if (t.contains("Clear")) {
            this.setIcon(new ImageIcon(getClass().getResource("/icons/Clear-Green-Button-icon.png")));
        }
        if (t.contains("Exam")) {
            this.setIcon(new ImageIcon(getClass().getResource("/icons/ex2.png")));
        }

    }
}
