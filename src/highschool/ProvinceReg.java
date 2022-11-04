/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package highschool;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JFrame;

import javax.swing.JOptionPane;

import static javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE;

/**
 * @author FRED
 */
public class ProvinceReg extends JFrame implements ActionListener {

    public static void main(String[] args) {
        new ProvinceReg();
    }

    private int width = 700;
    private int height = 400;
    private FredLabel country;
    private FredLabel province;
    private FredTextField jprovince;
    private FredCombo jcountry;
    private FredButton save, cancel;
    private PreparedStatement ps;
    private Connection con;
    ResultSet rs;

    public ProvinceReg() {
        setSize(width, height);
        setLocationRelativeTo(null);
        setLayout(null);
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
        country = new FredLabel("Select Country");
        province = new FredLabel("Province");
        jcountry = new FredCombo("Select Country");
        jprovince = new FredTextField();
        cancel = new FredButton("Close");
        save = new FredButton("Save");

        country.setBounds(30, 30, 150, 30);
        add(country);
        jcountry.setBounds(300, 30, 250, 30);
        add(jcountry);
        province.setBounds(30, 130, 150, 30);
        add(province);
        jprovince.setBounds(300, 130, 250, 30);
        add(jprovince);
        cancel.setBounds(100, 250, 130, 30);
        add(cancel);
        save.setBounds(350, 250, 130, 30);
        add(save);
        try {
            String sql = "Select * from countries";
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                jcountry.addItem(rs.getString("CountryName"));
            }

        } catch (SQLException sq) {
            JOptionPane.showMessageDialog(this, sq.getMessage());
        }
        jprovince.addKeyListener(new KeyAdapter() {

            @Override
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();
                if (Character.isDigit(c) || jprovince.getText().length() > 30) {
                    getToolkit().beep();
                    e.consume();
                }

            }
        });

        setVisible(true);
        cancel.addActionListener(this);
        save.addActionListener(this);
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
            if (jprovince.getText().equalsIgnoreCase("")) {
                getToolkit().beep();
                JOptionPane.showMessageDialog(this, "Missing Province Name");
            } else {
                if (jcountry.getSelectedIndex() == 0) {
                    getToolkit().beep();
                    JOptionPane.showMessageDialog(this, "Select Country");
                } else {
                    try {
                        String countrycode = "";
                        String sql = "Select CountryCode from countries where CountryName='" + jcountry.getSelectedItem() + "'";
                        ps = con.prepareStatement(sql);
                        rs = ps.executeQuery();
                        if (rs.next()) {
                            countrycode = rs.getString("countrycode");
                        }
                        String sql2 = "Insert into provinces value('" + jprovince.getText().toUpperCase() + "','" + "PR" + IdGenerator.keyGen() + "','" + countrycode + "')";
                        ps = con.prepareStatement(sql2);
                        ps.execute();
                        JOptionPane.showMessageDialog(this, "Province Registered SuccessFully");
                        jprovince.setText("");
                        jcountry.setSelectedIndex(0);
                    } catch (SQLException sq) {
                        JOptionPane.showMessageDialog(this, sq.getMessage());
                    }
                }


            }


        }
    }

}
