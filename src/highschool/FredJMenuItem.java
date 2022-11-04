/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


package highschool;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JMenuItem;

/**
 * @author FRED
 */
public class FredJMenuItem extends JMenuItem {
    public FredJMenuItem(String t) {

        Cursor cur = new Cursor(Cursor.HAND_CURSOR);
        this.setCursor(cur);
        this.setText(t);
        this.setFont(new Font("serif", Font.BOLD, 17));

        if (t.startsWith("Create")) {
            this.setIcon(new ImageIcon(getClass().getResource("/icons/create-icon.png")));
        }
        if (t.contains("egist")) {
            this.setIcon(new ImageIcon(getClass().getResource("/icons/create-icon.png")));
        }
        if (t.contains("SMS")) {
            this.setIcon(new ImageIcon(getClass().getResource("/icons/SMS-bubble-icon.png")));
        }
        if (t.contains("Report")) {
            this.setIcon(new ImageIcon(getClass().getResource("/icons/Adobe-PDF-Document-icon.png")));
        }
        if (t.contains("Set up School Fees")) {
            this.setIcon(new ImageIcon(getClass().getResource("/icons/Settings-icon.png")));
        }

        if (t.startsWith("Set")) {
            this.setIcon(new ImageIcon(getClass().getResource("/icons/Settings-icon.png")));
        }
        if (t.startsWith("View")) {
            this.setIcon(new ImageIcon(getClass().getResource("/icons/Actions-view-list-details-icon.png")));
        }
        if (t.startsWith("Collec")) {
            this.setIcon(new ImageIcon(getClass().getResource("/icons/Actions-view-list-details-icon.png")));
        }
        if (t.contains("Class Mig") || t.contains("Term Tra")) {

            this.setIcon(new ImageIcon(getClass().getResource("/icons/Money-icon.png")));
        }
        if (t.startsWith("Receive")) {
            this.setIcon(new ImageIcon(getClass().getResource("/icons/recieve.png")));
        }
        if (t.startsWith("Record")) {
            this.setIcon(new ImageIcon(getClass().getResource("/icons/medical-record-icon.png")));
        }
        if (t.contains("Review")) {
            this.setIcon(new ImageIcon(getClass().getResource("/icons/review-icon.png")));
        }
        if (t.contains("Remote")) {
            this.setIcon(new ImageIcon(getClass().getResource("/icons/Team-Viewer-icon.png")));
        }
        if (t.contains("Right") || t.contains("Assign")) {
            this.setIcon(new ImageIcon(getClass().getResource("/icons/right-indent-folded-icon.png")));
        }
        if (t.contains("Clearance Record")) {
            this.setIcon(new ImageIcon(getClass().getResource("/icons/Clear-Tick-icon.png")));
        }
        if (t.startsWith("Log")) {
            this.setIcon(new ImageIcon(getClass().getResource("/icons/logout-icon.png")));
        }
        if (t.startsWith("Change")) {
            this.setIcon(new ImageIcon(getClass().getResource("/icons/change-password-icon.png")));
        }
        if (t.startsWith("Receive Back")) {
            this.setIcon(new ImageIcon(getClass().getResource("/icons/Actions-arrow-right-icon.png")));
        }
        if (t.startsWith("Issue Book")) {
            this.setIcon(new ImageIcon(getClass().getResource("/icons/Button-Previous-icon.png")));
        }

        if (t.startsWith("Return H")) {
            this.setIcon(new ImageIcon(getClass().getResource("/icons/1-Normal-Home-icon.png")));
        }
        if (t.startsWith("System Set")) {
            this.setIcon(new ImageIcon(getClass().getResource("/icons/Settings-icon.png")));
        }

        if (t.contains("Merit List")) {
            this.setIcon(new ImageIcon(getClass().getResource("/icons/merit.png")));
        }
        if (t.contains("Class List") || t.contains("Preview")) {
            this.setIcon(new ImageIcon(getClass().getResource("/icons/list.png")));
        }
        if (t.contains("Enter")) {
            this.setIcon(new ImageIcon(getClass().getResource("/icons/enter.png")));
        }
        if (t.contains("Allocate")) {
            this.setIcon(new ImageIcon(getClass().getResource("/icons/allocate.png")));
        }
        if (t.contains("Comment")) {
            this.setIcon(new ImageIcon(getClass().getResource("/icons/comment.png")));
        }
        if (t.contains("Delete")) {
            this.setIcon(new ImageIcon(getClass().getResource("/icons/delete-icon.png")));
        }
        if (t.contains("Missing")) {
            this.setIcon(new ImageIcon(getClass().getResource("/icons/missing.png")));
        }
        if (t.contains("Distribution")) {
            this.setIcon(new ImageIcon(getClass().getResource("/icons/distribution.png")));
        }
        if (t.contains("Migration")) {
            this.setIcon(new ImageIcon(getClass().getResource("/icons/next.png")));
        }
        if (t.contains("Combine")) {
            this.setIcon(new ImageIcon(getClass().getResource("/icons/combine.png")));
        }
        if (t.contains("Best") || t.contains("Teachers Det")) {
            this.setIcon(new ImageIcon(getClass().getResource("/icons/show.png")));
        }
        if (t.contains("Analysis")) {
            this.setIcon(new ImageIcon(getClass().getResource("/icons/mean.png")));
        }
        if (t.contains("Manage User") || t.contains("Take")) {
            this.setIcon(new ImageIcon(getClass().getResource("/icons/User-Group-icon.png")));
        }
        if (t.contains("Apply")) {
            this.setIcon(new ImageIcon(getClass().getResource("/icons/apply.png")));
        }
        if (t.contains("Configure")) {
            this.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/Settings-icon.png")));
        }
        if (t.contains("Switch")) {
            this.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/review-icon.png")));
        }

        if (t.contains("Querry")) {
            this.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/apply.png")));
        }
        if (t.contains("Most")) {
            this.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/results.png")));
        }
        if (t.startsWith("Phone")) {
            this.setIcon(new ImageIcon(getClass().getResource("/icons/list.png")));
        }

        if (t.startsWith("System Set")) {
            this.setIcon(new ImageIcon(getClass().getResource("/icons/Settings-icon.png")));
        }
        if (t.startsWith("Add")) {
            this.setIcon(new ImageIcon(getClass().getResource("/icons/Actions-list-add-icon.png")));
        }

        if (t.startsWith("Account")) {
            this.setIcon(new ImageIcon(getClass().getResource("/icons/Network-Data-In-Both-Directions-icon.png")));
        }
        if (t.startsWith("Create A")) {
            this.setIcon(new ImageIcon(getClass().getResource("/icons/Person-Male-Dark-icon.png")));
        }

        if (t.startsWith("Manage")) {
            this.setIcon(new ImageIcon(getClass().getResource("/icons/User-Group-icon.png")));
        }
        if (t.startsWith("Fee Register")) {
            this.setIcon(new ImageIcon(getClass().getResource("/icons/Actions-view-list-details-icon.png")));
        }
        this.addMouseListener(new MouseAdapter() {


            @Override
            public void mouseEntered(MouseEvent e) {
                setForeground(Color.WHITE);
                setFont(new Font("serif", Font.ITALIC, 17));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                setForeground(Color.black);
                setFont(new Font("serif", Font.BOLD, 17));
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                setForeground(Color.black);
                setFont(new Font("normal", Font.BOLD, 17));
            }
        });
    }
}