/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package highschool;

import java.awt.Color;
import java.awt.HeadlessException;
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
public class CountyReg extends JFrame implements ActionListener {

    public static void main(String[] args) {
        new CountyReg();
    }

    private int height = 450;
    private int width = 700;
    private FredLabel country, county;
    private FredLabel province;
    private FredCombo jprovince;
    private FredCombo jcountry;
    private FredTextField jcounty;
    private FredButton save, cancel;
    private PreparedStatement ps;
    private Connection con;
    ResultSet rs;

    public CountyReg() {
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
        jprovince = new FredCombo("Select province");
        county = new FredLabel("County");
        jcounty = new FredTextField();
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
        county.setBounds(30, 230, 150, 30);
        add(county);
        jcounty.setBounds(300, 230, 250, 30);
        add(jcounty);
        save.setBounds(400, 330, 130, 30);
        add(save);
        cancel.setBounds(100, 330, 130, 30);
        add(cancel);
        setVisible(true);
        save.addActionListener(this);
        cancel.addActionListener(this);
        jprovince.addActionListener(this);
        jcountry.addActionListener(this);

        try {
            String sql3 = "Select * from countries";
            con = DbConnection.connectDb();
            ps = con.prepareStatement(sql3);
            rs = ps.executeQuery();
            while (rs.next()) {
                jcountry.addItem(rs.getString("countryName"));

            }
        } catch (SQLException e) {
        }
        jcounty.addKeyListener(new KeyAdapter() {

            @Override
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();
                if (Character.isDigit(c) || jcounty.getText().length() > 30) {
                    getToolkit().beep();
                    e.consume();
                }

            }
        });

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
        } else if (obj == jcountry) {
            try {
                jprovince.removeAllItems();
                jprovince.addItem("Select Province");
                String sql = "Select Countrycode from countries where countryname='" + jcountry.getSelectedItem() + "'";
                ps = con.prepareStatement(sql);
                rs = ps.executeQuery();
                if (rs.next()) {
                    String sql1 = "Select * from provinces where countrycode='" + rs.getString("Countrycode") + "' order by provincename asc";
                    ps = con.prepareStatement(sql1);
                    rs = ps.executeQuery();
                    while (rs.next()) {
                        jprovince.addItem(rs.getString("provincename"));
                    }
                }
            } catch (SQLException ex) {
                Logger.getLogger(studentReg.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else if (obj == save) {
            if (jcountry.getSelectedIndex() == 0) {
                getToolkit().beep();
                JOptionPane.showMessageDialog(this, "Select The Country");
            } else {
                if (jprovince.getSelectedIndex() == 0) {
                    getToolkit().beep();
                    JOptionPane.showMessageDialog(this, "Select The Province");
                } else {
                    if (jcounty.getText().isEmpty()) {
                        getToolkit().beep();
                        JOptionPane.showMessageDialog(this, "Kindly Input a valid County Name");
                    } else {
                        try {
                            String contrycode = "", provincecode = "";
                            String sql = "Select Countrycode from countries where countryname='" + jcountry.getSelectedItem() + "'";
                            ps = con.prepareStatement(sql);
                            rs = ps.executeQuery();
                            if (rs.next()) {
                                contrycode = rs.getString("Countrycode");
                            }
                            String sql2 = "Select provincecode from provinces where provincename='" + jprovince.getSelectedItem() + "'";
                            ps = con.prepareStatement(sql2);
                            rs = ps.executeQuery();
                            if (rs.next()) {
                                provincecode = rs.getString("Provincecode");
                            }
                            String SQL = "Select * from counties where countyname='" + jcounty.getText() + "' and provincecode='" + provincecode + "' and countrycode='" + contrycode + "'";
                            ps = con.prepareStatement(sql);
                            rs = ps.executeQuery();
                            if (rs.next()) {
                                JOptionPane.showMessageDialog(this, "This County is Already Registered");
                            } else {

                                String sql3 = "Insert into counties values('" + jcounty.getText().toUpperCase() + "','" + "CO" + IdGenerator.keyGen() + "','" + provincecode + "','" + contrycode + "')";
                                ps = con.prepareStatement(sql3);
                                ps.execute();
                                JOptionPane.showMessageDialog(this, "County Registered SuccessFully");
                                ;
                                jcountry.setSelectedIndex(0);
                                jprovince.setSelectedIndex(0);
                                jcounty.setText("");

                            }


                        } catch (HeadlessException | SQLException sq) {
                            JOptionPane.showMessageDialog(this, sq.getMessage());
                        }
                    }
                }
            }


        }

    }


}
