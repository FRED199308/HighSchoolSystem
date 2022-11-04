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
import javax.swing.JButton;

/**
 * @author FRED
 */
public class FredButton extends JButton {
    public FredButton(String g) {

        this.setForeground(Color.blue);
        this.setBackground(Color.GREEN);
        Cursor cur = new Cursor(Cursor.HAND_CURSOR);
        this.setCursor(cur);
        this.setText(g);
        this.setText(g);

        this.setToolTipText("Click to " + g);
        if (g.equalsIgnoreCase("close")) {
            this.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/Windows-Close-Program-icon.png")));
        } else if (g.equalsIgnoreCase("Resume Home")) {
            this.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/Home-icon.png")));

        } else if (g.equalsIgnoreCase("Back")) {

            this.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/Button-Previous-icon.png")));
        } else if (g.equalsIgnoreCase("save")) {
            this.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/Actions-document-save-icon.png")));
        } else if (g.startsWith("Delet")) {//this.setVisible(false);
            if (RightsAnnouncer.DeleteRights()) {
                this.setEnabled(true);
            } else {

                this.setEnabled(false);
                if (Globals.Level.equalsIgnoreCase("Admin")) {
                    this.setEnabled(true);
                }
            }
        } else if (g.startsWith("edit")) {
            if (RightsAnnouncer.editRights()) {
                this.setEnabled(true);
            } else {

                this.setEnabled(false);
                if (Globals.Level.equalsIgnoreCase("Admin")) {
                    this.setEnabled(true);
                }
            }
        }
        if (g.startsWith("Generate")) {
            this.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/Adobe-PDF-Document-icon.png")));
        }
        if (g.startsWith("Manage")) {
            this.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/Very-Basic-Services-icon.png")));
        }
        if (g.startsWith("Create") || g.startsWith("Add")) {
            this.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/create-icon.png")));
        }
        if (g.startsWith("View")) {
            this.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/Actions-view-list-details-icon.png")));
        }
        if (g.contains("Report")) {
            this.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/Adobe-PDF-Document-icon.png")));
        }
        if (g.contains("Expor") || g.contains("Statement")) {
            this.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/Adobe-PDF-Document-icon.png")));
        }
        if (g.contains("Clear")) {
            this.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/Clear-Tick-icon.png")));
        }
        if (g.contains("lose") || g.contains("Cance")) {
            this.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/Button-Close-icon.png")));
        }
        if (g.contains("Inbox")) {
            this.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/email-receive-icon.png")));
        }
        if (g.contains("Sen") || g.contains("sen")) {
            this.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/email-send-icon.png")));
        }
        if (g.startsWith("Record")) {
            this.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/medical-record-icon.png")));
        }
        if (g.contains("elete")) {
            this.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/delete-icon.png")));
        }
        if (g.contains("SMS")) {
            this.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/email-send-icon.png")));
        }
        if (g.contains("Date") || g.contains("Duration")) {
            this.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/event-icon.png")));
        }
        if (g.contains("Reprint")) {
            this.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/print-icon.png")));
        }

        this.addMouseListener(new MouseAdapter() {


            @Override
            public void mouseEntered(MouseEvent e) {
                setForeground(Color.MAGENTA);
                setFont(new Font("serif", Font.ITALIC, 15));
                Cursor cur = new Cursor(Cursor.HAND_CURSOR);
                setCursor(cur);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                setForeground(Color.MAGENTA);
                setFont(new Font("serif", Font.ITALIC, 15));
                Cursor cur = new Cursor(Cursor.HAND_CURSOR);
                setCursor(cur);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                setForeground(Color.black);
                setFont(new Font("normal", Font.PLAIN, 13));
                Cursor cur = new Cursor(Cursor.DEFAULT_CURSOR);
                setCursor(cur);
            }

            @Override
            public void mousePressed(MouseEvent e) {
                Cursor cur = new Cursor(Cursor.CROSSHAIR_CURSOR);
                setCursor(cur);
            }
        });

    }

}
