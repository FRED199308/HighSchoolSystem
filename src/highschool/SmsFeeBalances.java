/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package highschool;

import java.awt.Color;
import java.awt.Font;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JFrame;

import javax.swing.JOptionPane;

import jdk.nashorn.internal.objects.Global;

/**
 * @author FRED
 */
public class SmsFeeBalances extends JFrame implements ActionListener {

    public static void main(String[] args) {
        new SmsFeeBalances();
    }

    private int width = 700;
    private int height = 400;
    private PreparedStatement ps;
    private ResultSet rs;
    private Connection con = DbConnection.connectDb();
    private FredCombo jclass = new FredCombo("Select Class");
    private FredTextField AdmissionNumberField = new FredTextField();
    private FredButton cancel = new FredButton("Cancel");
    private FredButton sms = new FredButton("SMS Fee Balances");
    private FredLabel classs = new FredLabel("Class");
    private FredLabel payment = new FredLabel("admission Number");
    ArrayList<Map> messagesList = new ArrayList<Map>();
    HashMap<String, String> messageData = new HashMap<String, String>();

    FredLabel infor = new FredLabel("");

    public SmsFeeBalances() {
        setSize(width, height);
        setTitle("SMS Parents Fee Balances");
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        setLayout(null);
        this.addWindowListener(new WindowAdapter() {

            @Override
            public void windowClosing(WindowEvent e) {
                try {
                    con.close();
                } catch (SQLException sq) {

                }
                CurrentFrame.currentWindow();
                dispose();
            }
        });
        this.setIconImage(FrameProperties.icon());
        ((JComponent) getContentPane()).setBorder(
                BorderFactory.createMatteBorder(5, 5, 5, 5, Color.MAGENTA)
        );
        classs.setBounds(30, 30, 150, 30);
        add(classs);
        jclass.setBounds(300, 30, 300, 30);
        add(jclass);
        payment.setBounds(30, 130, 150, 30);
        add(payment);

        AdmissionNumberField.setBounds(300, 130, 300, 30);
        add(AdmissionNumberField);
        infor.setBounds(100, 180, 500, 30);
        add(infor);
        cancel.setBounds(150, 270, 150, 30);
        add(cancel);
        sms.setBounds(470, 270, 180, 30);
        add(sms);

        jclass.addItem("All classes");
        infor.setFont(new Font("serif", Font.BOLD, 18));
        setLocationRelativeTo(null);
        setVisible(true);

        try {
            String sql4 = "Select * from classes order by precision1";
            ps = con.prepareStatement(sql4);
            rs = ps.executeQuery();
            while (rs.next()) {
                jclass.addItem(rs.getString("ClassName"));
            }

            cancel.addActionListener(this);
            sms.addActionListener(this);

        } catch (SQLException sq) {
            JOptionPane.showMessageDialog(this, sq.getMessage());
        }

        AdmissionNumberField.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent key) {
                String adm = AdmissionNumberField.getText();
                infor.setText(Globals.fullName(adm) + "Current Balance: KSH. " + Globals.balanceCalculator(adm));

            }

        });
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        ArrayList<Map> messagesList = new ArrayList<Map>();
        HashMap<String, String> messageData = new HashMap<String, String>();
        Object obj = e.getSource();
        if (obj == cancel) {
            try {
                con.close();
            } catch (SQLException sq) {

            }
            CurrentFrame.currentWindow();
            dispose();
        } else if (obj == sms) {
            if (!AdmissionNumberField.getText().isEmpty()) {
                try {
                    String adm = AdmissionNumberField.getText();
                    String sql1 = "Select gender,parentfullNames,telephone1,admissionnumber from  admission where admissionNumber='" + adm + "'";
                    ps = con.prepareStatement(sql1);
                    ResultSet RS = ps.executeQuery();
                    if (RS.next()) {

                        adm = RS.getString("admissionnumber");
                        double BALANCE = Globals.balanceCalculator(adm);

                        if (adm.equalsIgnoreCase("")) {

                        } else {

                            if (BALANCE > 0) {

                                if (!RS.getString("telephone1").equalsIgnoreCase("")) {
                                    String p = RS.getString("telephone1");
                                    String phone = p;
                                    String message;
                                    String name = RS.getString("parentfullnames");
                                    if (RS.getString("gender").equalsIgnoreCase("Male")) {
                                        message = "Dear Parent/ Guardian,You Are Kindly Requested To Pay  Your Sons Outstanding  Fee Balance Of KSH." + BALANCE + ".00 KAMARANDI SEC. SCH.";
                                    } else {
                                        message = "Dear Parent/Guardian,You Are Kindly Requested To Pay  Your Daughters Outstanding  Fee Balance Of KSH." + BALANCE + ".00 KAMARANDI SEC. SCH.";
                                    }

                                    String pf = p.replaceFirst("0", "+254");
                                    messageData = new HashMap<String, String>();
                                    messageData.put("message", message);
                                    messageData.put("phone", phone);
                                    messagesList.add(messageData);

                                } else {

                                }

                            } else {
                                JOptionPane.showMessageDialog(this, "The Student With The Keyed admission Number Does Not Seem To Have Any Fee arrears,Reminder Not Sent");
                            }

                        }

                        MessageGateway.batchMessageQueuer(messagesList);
                        JOptionPane.showMessageDialog(this, "Fee Payment Reminders Sent  System SMS Server Successfully,\n System Gathering The Necessary Resources To Send SMS");

                    } else {
                        JOptionPane.showMessageDialog(this, "Invalid admission Number Check!!");
                    }

                } catch (HeadlessException | SQLException sq) {
                    JOptionPane.showMessageDialog(this, sq.getMessage());
                    sq.printStackTrace();
                }

            } else if (jclass.getSelectedIndex() > 0) {

                String adm = "";
                int individualtotalpaid = 0;
                int individualexpected = 0;
                int counter = 0, total = 0;
                if (jclass.getSelectedIndex() == 1) {

                    try {
                        String sql1 = "Select gender,parentfullNames,telephone1,admissionnumber from  admission where currentform like '" + "FM" + "%' order by admissionnumber";
                        ps = con.prepareStatement(sql1);
                        ResultSet RS = ps.executeQuery();
                        while (RS.next()) {

                            adm = RS.getString("admissionnumber");
                            double BALANCE = Globals.balanceCalculator(adm);
                            System.err.println("balance:" + BALANCE);
                            System.err.println("adm:" + adm);

                            if (adm.equalsIgnoreCase("")) {

                            } else {

                                if (BALANCE > 0) {

                                    if (!RS.getString("telephone1").equalsIgnoreCase("")) {
                                        String p = RS.getString("telephone1");
                                        String phone = p;
                                        String message;
                                        String name = RS.getString("parentfullnames");
                                        if (RS.getString("gender").equalsIgnoreCase("Male")) {
                                            message = "Dear Parent/ Guardian,You Are Kindly Requested To Pay  Your Sons Outstanding " + Globals.currentTermName() + " Fee Balance Of KSH." + BALANCE + ".00";
                                        } else {
                                            message = "Dear Parent/Guardian,You Are Kindly Requested To Pay  Your Daughters Outstanding " + Globals.currentTermName() + " Fee Balance Of KSH." + BALANCE + ".00";
                                        }


                                        //phone=pf;
                                        messageData = new HashMap<String, String>();
                                        messageData.put("message", message);
                                        messageData.put("phone", "254" + phone.substring(1));
                                        messagesList.add(messageData);

                                    } else {

                                    }

                                }

                            }

                            counter++;
                        }
                        MessageGateway.batchMessageQueuer(messagesList);
                        // JOptionPane.showMessageDialog(this, "Fee Payment Reminders Sent  System SMS Server Successfully,\n System Gathering The Necessary Resources To Send SMS");
                    } catch (HeadlessException | SQLException sq) {
                        JOptionPane.showMessageDialog(this, sq.getMessage());
                        sq.printStackTrace();
                    }

                } else {

                    try {
                        String sql1 = "Select gender,parentfullNames,telephone1,admissionnumber from  admission where currentform ='" + Globals.classCode(jclass.getSelectedItem().toString()) + "' order by admissionnumber";
                        ps = con.prepareStatement(sql1);
                        ResultSet RS = ps.executeQuery();
                        while (RS.next()) {

                            adm = RS.getString("admissionnumber");
                            double BALANCE = Globals.balanceCalculator(adm);

                            if (adm.equalsIgnoreCase("")) {

                            } else {

                                if (BALANCE > 0) {

                                    if (!RS.getString("telephone1").equalsIgnoreCase("")) {
                                        String p = RS.getString("telephone1");
                                        String phone = p;
                                        String message;
                                        String name = RS.getString("parentfullnames");
                                        if (RS.getString("gender").equalsIgnoreCase("Male")) {
                                            message = "Dear Parent/ Guardian,You Are Kindly Requested To Pay  Your Sons Outstanding " + Globals.currentTermName() + " Fee Balance Of KSH." + BALANCE + ".00";
                                        } else {
                                            message = "Dear Parent/Guardian,You Are Kindly Requested To Pay  Your Daughters Outstanding " + Globals.currentTermName() + " Fee Balance Of KSH." + BALANCE + ".00";
                                        }

//                            
                                        //     String pf=p.replaceFirst("0", "+254");
//     phone=pf;
                                        PreparedStatement ps1;

                                        messageData = new HashMap<String, String>();
                                        messageData.put("message", message);
                                        messageData.put("phone", "254" + phone.substring(1));
                                        messagesList.add(messageData);

                                    } else {

                                    }

                                }

                            }

                            counter++;
                        }

                        MessageGateway.batchMessageQueuer(messagesList);
                        // JOptionPane.showMessageDialog(this, "Fee Payment Reminders Sent  System SMS Server Successfully,\n System Gathering The Necessary Resources To Send SMS");
                    } catch (HeadlessException | SQLException sq) {
                        JOptionPane.showMessageDialog(this, sq.getMessage());
                        sq.printStackTrace();
                    }

                }

            } else {
                JOptionPane.showMessageDialog(this, "Kindly Select The Category To Send Reminders To");
            }
        }
    }
}
