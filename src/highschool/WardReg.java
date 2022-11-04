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
public class WardReg extends JFrame implements ActionListener {

    public static void main(String[] args) {
        new WardReg();
    }

    private int height = 550;
    private int width = 700;
    private FredLabel country, county, constituency, ward;
    private FredLabel province;
    private FredCombo jprovince, jcounty, jconstituency;
    private FredCombo jcountry;
    private FredTextField jward;
    private FredButton save, cancel;
    private PreparedStatement ps;
    private Connection con;
    ResultSet rs;

    public WardReg() {
        setSize(width, height);
        setLocationRelativeTo(null);
        setLayout(null);
        setTitle("Ward Registration");
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
        ward = new FredLabel("Ward");
        jconstituency = new FredCombo("Select Constituency");
        jward = new FredTextField();
        jcounty = new FredCombo("Select County");
        cancel = new FredButton("Close");
        save = new FredButton("Save");
        constituency = new FredLabel("Constituency");
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
        constituency.setBounds(30, 310, 150, 30);
        add(constituency);
        jconstituency.setBounds(300, 310, 250, 30);
        add(jconstituency);
        ward.setBounds(30, 400, 130, 30);
        add(ward);
        jward.setBounds(300, 400, 250, 30);
        add(jward);
        cancel.setBounds(100, 460, 130, 30);
        add(cancel);
        save.setBounds(400, 460, 130, 30);
        add(save);
        setVisible(true);
        save.addActionListener(this);
        cancel.addActionListener(this);
        jprovince.addActionListener(this);
        jcountry.addActionListener(this);
        jconstituency.addActionListener(this);
        jcounty.addActionListener(this);
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

        jward.addKeyListener(new KeyAdapter() {

            @Override
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();
                if (Character.isDigit(c) || jward.getText().length() > 30) {
                    getToolkit().beep();
                    e.consume();
                }

            }
        });


        setVisible(true);
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

            if (jcountry.getSelectedIndex() == 0) {
                getToolkit().beep();
                JOptionPane.showMessageDialog(this, "Select The Country");
            } else {
                if (jprovince.getSelectedIndex() == 0) {
                    getToolkit().beep();
                    JOptionPane.showMessageDialog(this, "Select The Province");
                } else {
                    if (jcounty.getSelectedIndex() == 0) {
                        JOptionPane.showMessageDialog(this, "Select County");

                    } else {
                        if (jconstituency.getSelectedIndex() == 0) {
                            getToolkit().beep();
                            JOptionPane.showMessageDialog(this, "Select The Costituency");
                        } else {

                            if (jward.getText().isEmpty()) {
                                getToolkit().beep();
                                JOptionPane.showMessageDialog(this, "Kindly Input a valid Ward Name");
                            } else {
                                try {
                                    String contrycode = "", provincecode = "", countycode = "", constituencycode = "";
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
                                    String querry = "Select countycode from counties where countyName='" + jcounty.getSelectedItem() + "'";
                                    ps = con.prepareStatement(querry);
                                    rs = ps.executeQuery();
                                    if (rs.next()) {
                                        countycode = rs.getString("countyCode");
                                    }
                                    String sql4 = "Select constituencycode from constituencies where constituencyname='" + jconstituency.getSelectedItem() + "'";
                                    ps = con.prepareStatement(sql4);
                                    rs = ps.executeQuery();
                                    while (rs.next()) {
                                        constituencycode = rs.getString("ConstituencyCode");
                                    }


                                    String SQL = "Select * from ward  where wardname='" + jward.getText() + "' and provincecode='" + provincecode + "' and countrycode='" + contrycode + "' and countycode='" + countycode + "' and constituencycode='" + constituencycode + "'";
                                    ps = con.prepareStatement(sql);
                                    rs = ps.executeQuery();
                                    if (rs.next()) {
                                        JOptionPane.showMessageDialog(this, "This Ward Already Registered");
                                    } else {

                                        String sql3 = "Insert into ward values('" + jward.getText().toUpperCase() + "','" + "WA" + IdGenerator.keyGen() + "','" + constituencycode + "','" + countycode + "','" + provincecode + "','" + contrycode + "')";
                                        ps = con.prepareStatement(sql3);
                                        ps.execute();
                                        JOptionPane.showMessageDialog(this, "Ward Registered SuccessFully");
                                        ;
                                        jcountry.setSelectedIndex(0);
                                        jprovince.setSelectedIndex(0);
                                        jward.setText("");
                                        jcounty.setSelectedIndex(0);
                                        jconstituency.setSelectedIndex(0);

                                    }


                                } catch (HeadlessException | SQLException sq) {
                                    JOptionPane.showMessageDialog(this, sq.getMessage());
                                }
                            }


                        }
                    }
                }
            }


        } else if (obj == jcounty) {
            jconstituency.removeAllItems();
            jconstituency.addItem("Select Constituency");
            try {

                String sql = "Select countycode from counties where countyname='" + jcounty.getSelectedItem() + "'";
                ps = con.prepareStatement(sql);
                rs = ps.executeQuery();
                if (rs.next()) {
                    String sql1 = "Select constituencyname  from constituencies where countycode='" + rs.getString("countycode") + "' order by constituencyname asc";
                    ps = con.prepareStatement(sql1);
                    rs = ps.executeQuery();
                    while (rs.next()) {
                        jconstituency.addItem(rs.getString("constituencyname"));
                    }
                }

            } catch (SQLException ex) {
                Logger.getLogger(studentReg.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else if (obj == jcountry) {
            try {
                jprovince.removeActionListener(this);
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
                jprovince.addActionListener(this);
            } catch (SQLException ex) {
                Logger.getLogger(studentReg.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else if (obj == jprovince) {
            jcounty.removeActionListener(this);
            jcounty.removeAllItems();
            jcounty.addItem("Select County");
            try {

                String sql = "Select provincecode from provinces where provincename='" + jprovince.getSelectedItem() + "'";
                ps = con.prepareStatement(sql);
                rs = ps.executeQuery();
                if (rs.next()) {
                    String sql1 = "Select countyname  from counties where provincecode='" + rs.getString("provincecode") + "' order by countyName asc";
                    ps = con.prepareStatement(sql1);
                    rs = ps.executeQuery();
                    while (rs.next()) {
                        jcounty.addItem(rs.getString("countyname"));
                    }
                }


            } catch (SQLException ex) {
                Logger.getLogger(studentReg.class.getName()).log(Level.SEVERE, null, ex);
            }
            jcounty.addActionListener(this);
        }


    }


}
