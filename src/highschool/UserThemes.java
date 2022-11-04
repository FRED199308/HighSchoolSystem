/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package highschool;


import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.UIManager;

import static javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE;

import javax.swing.border.TitledBorder;

import net.miginfocom.swing.MigLayout;

/**
 * @author FREDDY
 */
public class UserThemes extends JFrame implements ActionListener {
    private FredCheckBox nimbus, windows, windowsclassic, mac, metal, cde, mint, mcwin, alluminium;
    private int height = 400;
    private int width = 700;
    private Connection con;
    private PreparedStatement ps;
    private ResultSet rs;
    private FredDateChooser jdate;
    private FredButton save, cancel;
    private JPanel panel1 = new JPanel();
    private ButtonGroup looks;
    private String selectedFeel = "";
    private String currentTheme = ConfigurationIntialiser.userTheme();

    public UserThemes() {


        setSize(width, height);
        setLocationRelativeTo(null);
        setLayout(new MigLayout());
        setTitle("Record Fee Panel");
        setResizable(false);
        getContentPane().setBackground(Color.CYAN);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        this.addWindowListener(new WindowAdapter() {

            @Override
            public void windowClosing(WindowEvent e) {
                try {
                    con.close();
                } catch (SQLException sq) {

                }
                CurrentFrame.currentWindow();
                e.getWindow().dispose();
            }
        });
        con = DbConnection.connectDb();
        this.setIconImage(FrameProperties.icon());
        ((JComponent) getContentPane()).setBorder(
                BorderFactory.createMatteBorder(5, 5, 5, 5, Color.MAGENTA));
        save = new FredButton("Save");
        cancel = new FredButton("Close");

        nimbus = new FredCheckBox("Nimbus");
        mac = new FredCheckBox("HIFI");
        windows = new FredCheckBox("WINDOWS");
        windowsclassic = new FredCheckBox("WINDOWS CLASSIC");
        metal = new FredCheckBox("Metal");
        cde = new FredCheckBox("CDE");

        panel1 = new JPanel();
        panel1.setBackground(Color.WHITE);
        panel1.setLayout(new MigLayout());
        looks = new ButtonGroup();
        looks.add(mac);
        looks.add(windowsclassic);
        looks.add(metal);
        looks.add(cde);
        looks.add(windows);
        looks.add(nimbus);
        panel1.setBorder(new TitledBorder("Select The Look An Feel For The System"));
        panel1.setLayout(new MigLayout());
        panel1.add(windows, "Gapleft 30,push,grow");
        panel1.add(cde, "Gapleft 30,push,grow,wrap");
        panel1.add(windowsclassic, "Gapleft 30,push,grow");
        panel1.add(mac, "Gapleft 30,push,grow,wrap");
        panel1.add(metal, "Gapleft 30,push,grow");
        panel1.add(nimbus, "Gapleft 30,push,grow");


        add(panel1, "grow,push,span 2 1,wrap");
        add(cancel, "Gapleft 100");
        add(save);
        save.addActionListener(this);
        cancel.addActionListener(this);

        setVisible(true);

    }

    public static void main(String[] args) {
        new UserThemes();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object obj = e.getSource();
        if (obj == cancel) {
            try {
                con.close();
            } catch (SQLException sq) {

            }
            CurrentFrame.currentWindow();
            dispose();
        } else if (obj == save) {

            if (mac.isSelected()) {
                selectedFeel = "com.jtattoo.plaf.hifi.HiFiLookAndFeel";

            } else if (windows.isSelected()) {
                selectedFeel = "com.sun.java.swing.plaf.windows.WindowsLookAndFeel";
            } else if (windowsclassic.isSelected()) {
                selectedFeel = "com.sun.java.swing.plaf.windows.WindowsClassicLookAndFeel";
            } else if (metal.isSelected()) {
                selectedFeel = "com.jtattoo.plaf.mcwin.McWinLookAndFeel";
            } else if (cde.isSelected()) {
                selectedFeel = "com.sun.java.swing.plaf.motif.MotifLookAndFeel";
            } else if (nimbus.isSelected()) {
                selectedFeel = "com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel";
            }
            try {
                String querry4 = "update UserAccounts  set theme='" + selectedFeel + "' where username='" + Globals.CurrentUser + "'";
                ps = con.prepareStatement(querry4);
                ps.execute();
            } catch (Exception sq) {
                sq.printStackTrace();
                JOptionPane.showMessageDialog(this, sq.getMessage());
            }

            if (selectedFeel.equalsIgnoreCase(currentTheme)) {
                JOptionPane.showMessageDialog(this, "No Theme Change Detected");
            } else {
                JOptionPane.showMessageDialog(this, "Theme Change Detected \n The System Must Restart In Order To Apply The new Theme");
                dispose();
                CurrentFrame.mainFrame().dispose();
                ;


                try {
                    for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {

                        UIManager.setLookAndFeel(selectedFeel);

                    }
                } catch (Exception sq) {

                    JOptionPane.showMessageDialog(this, "System Was Not Able To Apply The Selected Theme\n Default Theme Applied");


                }
                new FinanceHome();


            }
        }

    }


}
