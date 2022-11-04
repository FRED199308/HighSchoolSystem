/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package highschool;

import com.toedter.calendar.*;

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
import javax.swing.JTextField;

import static javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE;

/**
 * @author FRED
 */
public class FeeReg extends JFrame implements ActionListener {

    public static void main(String[] args) {
        new FeeReg();
    }

    private int width = 700;
    private int height = 510;
    private Connection con;
    private ResultSet rs;
    private FredLabel name, level, term, date, amount;
    private JTextField jname, jAmount;
    private FredCombo jterm, jlevel;
    private JYearChooser jdate;
    private PreparedStatement ps;
    private FredButton save, cancel;

    public FeeReg() {
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
        setTitle("School Fee Set");
        name = new FredLabel("Name");
        term = new FredLabel("Select Term");
        date = new FredLabel("Date");
        amount = new FredLabel("Amount");
        level = new FredLabel("Class");

        jname = new JTextField();
        jdate = new JYearChooser();
        jAmount = new JTextField();
        jlevel = new FredCombo("Select Form");
        jterm = new FredCombo("Select Term");
        save = new FredButton("Save");
        cancel = new FredButton("Close");
        name.setBounds(30, 30, 150, 30);
        add(name);
        jname.setBounds(350, 30, 250, 30);
        add(jname);
        term.setBounds(30, 110, 150, 30);
        add(term);
        jterm.setBounds(350, 110, 250, 30);
        add(jterm);
        level.setBounds(30, 190, 150, 30);
        add(level);
        jlevel.setBounds(350, 190, 250, 30);
        add(jlevel);
        amount.setBounds(30, 270, 150, 30);
        add(amount);
        jAmount.setBounds(350, 270, 250, 30);
        add(jAmount);
        date.setBounds(30, 350, 150, 30);
        add(date);
        jdate.setBounds(350, 350, 250, 30);
        add(jdate);
        cancel.setBounds(100, 420, 130, 30);
        add(cancel);
        save.setBounds(400, 420, 130, 30);
        add(save);
        jname.setText("FEE");
        jname.setEditable(false);
        try {
            String querry = "Select Distinct termname from terms";
            ps = con.prepareStatement(querry);
            rs = ps.executeQuery();
            while (rs.next()) {
                jterm.addItem(rs.getString("TermName"));
            }
            String sql = "select * from classes where precision1<5 order by precision1";
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                jlevel.addItem(rs.getString("ClassName"));
            }
        } catch (SQLException sq) {
            JOptionPane.showMessageDialog(this, sq.getMessage());
        }

        jAmount.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();
                if (!Character.isDigit(c) || jAmount.getText().length() > 10) {
                    getToolkit().beep();
                    e.consume();
                }

            }

        });
        jname.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();
                if (Character.isDigit(c) || jname.getText().length() > 25) {
                    getToolkit().beep();
                    e.consume();
                }

            }

        });
        save.addActionListener(this);
        cancel.addActionListener(this);
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object obj = e.getSource();
        if (obj == save) {
            if (jterm.getSelectedIndex() == 0) {
                getToolkit().beep();
                JOptionPane.showMessageDialog(this, "Kindly Select The Term To set Fee");
            } else {
                if (jlevel.getSelectedIndex() == 0) {
                    int k = JOptionPane.showConfirmDialog(this, "Are You Sure You Want To Set A Constant School Fee Across All classes", "Confirm", JOptionPane.YES_NO_OPTION);
                    if (k == JOptionPane.YES_NO_OPTION) {
                        try {
                            String termcode = "", classcode = "", yaer;

                            String sql = "Select termcode from terms where termname='" + jterm.getSelectedItem() + "'";
                            ps = con.prepareStatement(sql);
                            rs = ps.executeQuery();
                            if (rs.next()) {
                                termcode = rs.getString("Termcode");
                            }

                            String sql1 = "Select classcode from classes where classname='" + jlevel.getSelectedItem() + "'";
                            ps = con.prepareStatement(sql1);
                            rs = ps.executeQuery();
                            if (rs.next()) {
                                classcode = rs.getString("Classcode");
                            }
                            ResultSet RS;
                            String sql3 = "Select * from classes  where precision1<5 order by precision1";
                            ps = con.prepareStatement(sql3);
                            RS = ps.executeQuery();
                            while (RS.next()) {
                                classcode = RS.getString("ClassCode");
                                String CLASS = RS.getString("ClassName");
                                String sql2 = "Select * from expectedpayments where termcode='" + termcode + "' and classcode='" + classcode + "' and Date='" + jdate.getYear() + "'";
                                ps = con.prepareStatement(sql2);
                                rs = ps.executeQuery();
                                if (rs.next()) {

                                    JOptionPane.showMessageDialog(this, "The School Fee For:" + CLASS + "  " + jterm.getSelectedItem() + " Year :" + jdate.getYear() + " Is Already Set");

                                } else {
                                    String Sql = "Insert into expectedpayments values('" + jname.getText().toUpperCase() + "','" + termcode + "','" + classcode + "','" + jAmount.getText() + "','" + "FE" + IdGenerator.keyGen() + "','" + jdate.getYear() + "',NOW())";
                                    ps = con.prepareStatement(sql);
                                    ps.execute();

                                }

                            }
                            jlevel.setSelectedIndex(0);
                            jterm.setSelectedIndex(0);
                            jAmount.setText("");
                            JOptionPane.showMessageDialog(this, "Fee Details Set Successfully");
                        } catch (SQLException ex) {
                            Logger.getLogger(FeeReg.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    } else {
                        JOptionPane.showMessageDialog(this, "Select Class");
                    }
                } else {
                    try {
                        String termcode = "", classcode = "", yaer;
                        String sql = "Select termcode from terms where termname='" + jterm.getSelectedItem() + "'";
                        ps = con.prepareStatement(sql);
                        rs = ps.executeQuery();
                        if (rs.next()) {
                            termcode = rs.getString("Termcode");
                        }
                        String sql1 = "Select classcode from classes where classname='" + jlevel.getSelectedItem() + "'";
                        ps = con.prepareStatement(sql1);
                        rs = ps.executeQuery();
                        if (rs.next()) {
                            classcode = rs.getString("Classcode");
                        }
                        String sql2 = "Select * from expectedpayments where termcode='" + termcode + "' and classcode='" + classcode + "' and Date='" + jdate.getYear() + "'";
                        ps = con.prepareStatement(sql2);
                        rs = ps.executeQuery();
                        if (rs.next()) {

                            JOptionPane.showMessageDialog(this, "The School Fee For:" + jlevel.getSelectedItem() + "  " + jterm.getSelectedItem() + " Year :" + jdate.getYear() + " Is Already Set");

                        } else {
                            String Sql = "Insert into expectedpayments values('" + jname.getText().toUpperCase() + "','" + termcode + "','" + classcode + "','" + jAmount.getText() + "','" + "FE" + IdGenerator.keyGen() + "','" + jdate.getYear() + "',NOW())";
                            ps = con.prepareStatement(sql);
                            ps.execute();
                            JOptionPane.showMessageDialog(this, "Fee Details Set Successfully");
                            jlevel.setSelectedIndex(0);
                            jterm.setSelectedIndex(0);
                            jAmount.setText("");
                        }
                    } catch (SQLException sq) {
                        JOptionPane.showMessageDialog(this, sq.getMessage());
                    }


                }
            }
        }
        if (obj == cancel) {
            try {
                con.close();
            } catch (SQLException sq) {

            }
            CurrentFrame.currentWindow();
            dispose();
        }
    }


}
