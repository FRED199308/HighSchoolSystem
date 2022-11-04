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
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;

import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.JProgressBar;
import javax.swing.JTextField;

import static javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE;

/**
 * @author FRED
 */
public class ContributionReg extends JFrame implements ActionListener {

    public static void main(String[] args) {
        new ContributionReg();
    }

    private int height = 470;
    private int width = 700;
    private PreparedStatement ps;
    private Connection con;
    private FredButton save, cancel;
    private JTextField jname, jamount;
    private FredLabel name, amount, classl, program;
    private FredCombo jclass, Jprogram;
    private JMenuBar bar;
    private FredCheckBox instant, postponed;
    private ButtonGroup grop = new ButtonGroup();
    ResultSet rs;
    private boolean invoice = true;

    public ContributionReg() {
        setSize(width, height);
        setLocationRelativeTo(null);
        setLayout(null);
        setResizable(false);
        bar = new JMenuBar();
        setTitle("Custom Parent Contribution Registration panel");
        // getContentPane().setBackground(Color.CYAN);
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
        name = new FredLabel("Contribution Name");
        jamount = new JTextField();
        amount = new FredLabel("Amount");
        classl = new FredLabel("Contributors");
        jclass = new FredCombo("Select Contributors");
        save = new FredButton("Save");
        cancel = new FredButton("Close");
        program = new FredLabel("Program");
        Jprogram = new FredCombo("Select Program");
        jname = new JTextField();
        bar.setBounds(7, 5, 670, 20);
        add(bar);
        instant = new FredCheckBox("Bill Instantly");
        postponed = new FredCheckBox("PostPone Billing");
        name.setBounds(30, 30, 150, 30);
        add(name);
        jname.setBounds(300, 30, 300, 30);
        add(jname);
        classl.setBounds(30, 110, 150, 30);
        add(classl);
        jclass.setBounds(300, 110, 300, 30);
        add(jclass);

        program.setBounds(30, 190, 150, 30);
        add(program);
        Jprogram.setBounds(300, 190, 300, 30);
        add(Jprogram);

        amount.setBounds(30, 270, 150, 30);
        add(amount);
        jamount.setBounds(300, 270, 300, 30);
        add(jamount);
        instant.setBounds(230, 320, 130, 30);
        add(instant);
        postponed.setBounds(450, 320, 130, 30);
        add(postponed);
        save.setBounds(400, 360, 130, 30);
        add(save);
        cancel.setBounds(100, 360, 130, 30);
        add(cancel);
        jclass.addItem("All Students");
        grop.add(postponed);
        grop.add(instant);
        instant.setSelected(true);
        try {
            con = DbConnection.connectDb();

            String sql = "select * from classes where precision1<5";
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                jclass.addItem(rs.getString("ClassName"));
            }

            int programconter = 0;
            String qeurry = "Select programname from programs";
            ps = con.prepareStatement(qeurry);
            rs = ps.executeQuery();
            while (rs.next()) {
                programconter++;
                Jprogram.addItem(rs.getString("Programname"));
            }
            if (programconter == 1) {
                Jprogram.setSelectedIndex(0);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        jname.addKeyListener(new KeyAdapter() {

            @Override
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();
                if (Character.isDigit(c) || jname.getText().length() > 30) {
                    getToolkit().beep();
                    e.consume();
                }

            }
        });

        save.addActionListener(this);
        cancel.addActionListener(this);
        jamount.addKeyListener(new KeyAdapter() {

            @Override
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();
                if (!Character.isDigit(c) || jamount.getText().length() > 30) {
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
        if (obj == save) {

            if (instant.isSelected()) {
                invoice = true;
            } else {
                invoice = false;
            }
            if (jname.getText().isEmpty()) {
                getToolkit().beep();
                JOptionPane.showMessageDialog(this, "Enter A Valid Contribution Name");
            } else {
                if (jclass.getSelectedIndex() == 0) {
                    getToolkit().beep();
                    JOptionPane.showMessageDialog(this, "Select The Contributors");
                } else {

                    if (jamount.getText().isEmpty() || Integer.parseInt(jamount.getText()) == 0) {
                        getToolkit().beep();
                        JOptionPane.showMessageDialog(this, "Enter A Valid Amount Figure");
                    } else {

                        new Thread() {
                            @Override
                            public void run() {

                                JDialog dia = new JDialog();
                                JProgressBar bar = new JProgressBar();
                                bar.setIndeterminate(true);

                                dia.setSize(380, 60);

                                dia.setTitle("System Creating &Performing Automatic Billing ");
                                dia.setAlwaysOnTop(true);
                                dia.setLocationRelativeTo(CurrentFrame.mainFrame());
                                dia.setIconImage(FrameProperties.icon());
                                dia.add(bar);
                                dia.setVisible(true);

                                if (Jprogram.getSelectedIndex() == 0) {

                                    dia.setAlwaysOnTop(false);
                                    int option = JOptionPane.showConfirmDialog(null, "Are You Sure That You Want\n All Programs  Among The Selected Contributors \nTo Contribute Towards The Contribution?", "Confirm Program ", JOptionPane.YES_NO_OPTION);
                                    dia.setAlwaysOnTop(true);
                                    if (option == JOptionPane.NO_OPTION) {
                                        JOptionPane.showMessageDialog(null, "Select The Program To Contribute");
                                    } else {
                                        try {
                                            String classcode = "";
                                            String pycode = "PE" + IdGenerator.keyGen();
                                            if (jclass.getSelectedIndex() == 1) {
                                                String sql2 = "Select * from classes where precision1<5";
                                                ps = con.prepareStatement(sql2);
                                                rs = ps.executeQuery();
                                                while (rs.next()) {
                                                    classcode = rs.getString("Classcode");
                                                    String sql3 = "Insert into expectedpayments values('" + jname.getText().toUpperCase() + "','" + Globals.currentTerm() + "','" + rs.getString("Classcode") + "','" + jamount.getText() + "','" + pycode + "','" + Globals.academicYear() + "',Now())";
                                                    ps = con.prepareStatement(sql3);
                                                    ps.execute();
                                                    if (invoice) {
                                                        String sql4 = "Select admissionNumber,currentstream from admission where currentform='" + classcode + "'";
                                                        ps = con.prepareStatement(sql4);
                                                        ResultSet RS = ps.executeQuery();
                                                        while (RS.next()) {
                                                            String sql12 = "Insert into Reciepts values('" + RS.getString("Admissionnumber") + "','" + pycode + "','" + "IN" + IdGenerator.keyGen() + "','" + "NONE" + "','" + "" + "','" + "0" + "','" + "0" + "','" + jamount.getText() + "',now(),'" + Globals.academicYear() + "','" + Globals.currentTerm() + "','" + classcode + "','" + RS.getString("CurrentStream") + "','" + "1900-01-01" + "')";
                                                            ps = con.prepareStatement(sql12);
                                                            ps.execute();

                                                        }
                                                    }

                                                }

                                                dia.dispose();
                                                JOptionPane.showMessageDialog(null, "Contribution Created Successfully");
                                                jamount.setText("");
                                                jclass.setSelectedIndex(0);
                                                jname.setText("");
                                            } else if (jclass.getSelectedIndex() > 1) {

                                                String sql = "Select classcode from classes where classname='" + jclass.getSelectedItem() + "'";
                                                ps = con.prepareStatement(sql);
                                                rs = ps.executeQuery();
                                                if (rs.next()) {
                                                    classcode = rs.getString("classcode");
                                                    String sql3 = "Insert into expectedpayments values('" + jname.getText().toUpperCase() + "','" + Globals.currentTerm() + "','" + rs.getString("Classcode") + "','" + jamount.getText() + "','" + pycode + "','" + Globals.academicYear() + "',Now())";
                                                    ps = con.prepareStatement(sql3);
                                                    ps.execute();
                                                    if (invoice) {
                                                        String sql4 = "Select admissionNumber,currentstream from admission where currentform='" + classcode + "'";
                                                        ps = con.prepareStatement(sql4);
                                                        ResultSet RS = ps.executeQuery();
                                                        while (RS.next()) {
                                                            String sql12 = "Insert into Reciepts values('" + RS.getString("Admissionnumber") + "','" + pycode + "','" + "IN" + IdGenerator.keyGen() + "','" + "NONE" + "','" + "" + "','" + "0" + "','" + "0" + "','" + jamount.getText() + "',Now(),'" + Globals.academicYear() + "','" + Globals.currentTerm() + "','" + classcode + "','" + RS.getString("CurrentStream") + "','" + "1900-01-01" + "')";
                                                            ps = con.prepareStatement(sql12);
                                                            ps.execute();

                                                        }
                                                    }
                                                    dia.dispose();
                                                    JOptionPane.showMessageDialog(null, "Contribution Created Successfully");
                                                    jamount.setText("");
                                                    jclass.setSelectedIndex(0);
                                                    jname.setText("");


                                                }
                                            }


                                        } catch (HeadlessException | SQLException sq) {
                                            dia.dispose();
                                            JOptionPane.showMessageDialog(null, sq.getMessage());
                                        }


                                    }


                                } else {

                                    try {
                                        String classcode = "";
                                        String pycode = "PE" + IdGenerator.keyGen();
                                        if (jclass.getSelectedIndex() == 1) {
                                            String sql2 = "Select * from classes where precision1<5";
                                            ps = con.prepareStatement(sql2);
                                            rs = ps.executeQuery();
                                            while (rs.next()) {
                                                classcode = rs.getString("Classcode");
                                                String sql3 = "Insert into expectedpayments values('" + jname.getText().toUpperCase() + "','" + Globals.currentTerm() + "','" + rs.getString("Classcode") + "','" + jamount.getText() + "','" + pycode + "','" + Globals.academicYear() + "',Now())";
                                                ps = con.prepareStatement(sql3);
                                                ps.execute();
                                                if (invoice) {
                                                    String sql4 = "Select admissionNumber,currentstream from admission where currentform='" + classcode + "' and program='" + Jprogram.getSelectedItem() + "'";
                                                    ps = con.prepareStatement(sql4);
                                                    ResultSet RS = ps.executeQuery();
                                                    while (RS.next()) {
                                                        String sql12 = "Insert into Reciepts values('" + RS.getString("Admissionnumber") + "','" + pycode + "','" + "IN" + IdGenerator.keyGen() + "','" + "NONE" + "','" + "" + "','" + "0" + "','" + "0" + "','" + jamount.getText() + "',Now(),'" + Globals.academicYear() + "','" + Globals.currentTerm() + "','" + classcode + "','" + RS.getString("CurrentStream") + "','" + "1900-01-01" + "')";
                                                        ps = con.prepareStatement(sql12);
                                                        ps.execute();

                                                    }
                                                }

                                            }

                                            dia.dispose();
                                            JOptionPane.showMessageDialog(null, "Contribution Created Successfully");
                                            jamount.setText("");
                                            jclass.setSelectedIndex(0);
                                            jname.setText("");
                                        } else if (jclass.getSelectedIndex() > 2) {

                                            String sql = "Select classcode from classes where classname='" + jclass.getSelectedItem() + "'";
                                            ps = con.prepareStatement(sql);
                                            rs = ps.executeQuery();
                                            if (rs.next()) {
                                                classcode = rs.getString("classcode");
                                                String sql3 = "Insert into expectedpayments values('" + jname.getText().toUpperCase() + "','" + Globals.currentTerm() + "','" + rs.getString("Classcode") + "','" + jamount.getText() + "','" + pycode + "','" + Globals.academicYear() + "',Now())";
                                                ps = con.prepareStatement(sql3);
                                                ps.execute();
                                                if (invoice) {
                                                    String sql4 = "Select admissionNumber,currentstream from admission where currentform='" + classcode + "' and program='" + Jprogram.getSelectedItem() + "'";
                                                    ps = con.prepareStatement(sql4);
                                                    ResultSet RS = ps.executeQuery();
                                                    while (RS.next()) {
                                                        String sql12 = "Insert into Reciepts values('" + RS.getString("Admissionnumber") + "','" + pycode + "','" + "IN" + IdGenerator.keyGen() + "','" + "NONE" + "','" + "" + "','" + "0" + "','" + "0" + "','" + jamount.getText() + "',Now(),'" + Globals.academicYear() + "','" + Globals.currentTerm() + "','" + classcode + "','" + RS.getString("CurrentStream") + "'),'" + "1900-01-01" + "'";
                                                        ps = con.prepareStatement(sql12);
                                                        ps.execute();

                                                    }
                                                }
                                                dia.dispose();
                                                JOptionPane.showMessageDialog(null, "Contribution Created Successfully");
                                                jamount.setText("");
                                                jclass.setSelectedIndex(0);
                                                jname.setText("");


                                            }
                                        }


                                    } catch (HeadlessException | SQLException sq) {
                                        dia.dispose();
                                        JOptionPane.showMessageDialog(null, sq.getMessage());
                                    }

                                }


                                dia.dispose();
                            }


                        }.start();


                    }
                }

            }

        } else if (obj == cancel) {
            try {
                con.close();
            } catch (SQLException sq) {

            }
            CurrentFrame.currentWindow();
            dispose();
        }


    }


}
