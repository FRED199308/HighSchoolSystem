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
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;

import javax.swing.JOptionPane;

/**
 * @author FRED
 */
public class LostBooksNotifier extends JFrame implements ActionListener {

    public static void main(String[] args) {
        new LostBooksNotifier();
    }

    private int width = 600;
    private int height = 300;
    private FredCombo jclass = new FredCombo("All");
    private FredLabel class1 = new FredLabel("Recipients");
    private FredButton cancel = new FredButton("Close");
    private FredButton save = new FredButton("Send");
    private Connection con;
    private ResultSet rs;
    private PreparedStatement ps;

    public LostBooksNotifier() {
        con = DbConnection.connectDb();
        setSize(width, height);
        setLocationRelativeTo(CurrentFrame.mainFrame());
        setLayout(null);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        setTitle("Lost Book Notification Panel");
        jclass.addItem("Students On Session Only");
        jclass.addItem("Alumni Only");
        this.addWindowListener(new WindowAdapter() {

            @Override
            public void windowClosing(WindowEvent e) {
                CurrentFrame.currentWindow();
                e.getWindow().dispose();
                try {
                    con.close();
                } catch (SQLException sq) {

                }

            }
        });

        this.setIconImage(FrameProperties.icon());
        ((JComponent) getContentPane()).setBorder(
                BorderFactory.createMatteBorder(5, 5, 5, 5, Color.MAGENTA));
        class1.setBounds(30, 40, 100, 30);
        add(class1);
        jclass.setBounds(250, 40, 250, 30);
        add(jclass);

        cancel.setBounds(150, 150, 100, 30);
        add(cancel);
        save.setBounds(350, 150, 100, 30);
        add(save);
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

            try {

                if (jclass.getSelectedIndex() == 0) {
                    String sql3 = "Select books.bookserial,bookname,admnumber,dateissued,booktitle from issuedbooks,books where books.bookserial=issuedbooks.bookserial and books.status='" + "ISSUED" + "' and issuedbooks.status='" + "LOST" + "' order by bookname";
                    ps = con.prepareStatement(sql3);
                    rs = ps.executeQuery();
                    while (rs.next()) {
                        String adm = rs.getString("AdmNumber");
                        String bookTitle = rs.getString("booktitle");
                        String bookName = rs.getString("bookname");

                        String sql = "Select gender,parentfullNames,telephone1 from admission where admissionnumber='" + adm + "'";
                        ps = con.prepareStatement(sql);
                        ResultSet RS = ps.executeQuery();
                        if (RS.next()) {

                            if (!RS.getString("telephone1").equalsIgnoreCase("")) {
                                String p = RS.getString("telephone1");
                                String phone;
                                String message;
                                String name = RS.getString("parentfullnames");
                                if (RS.getString("gender").equalsIgnoreCase("Male")) {
                                    message = "Hi Mr./Mrs " + name + ",You Are Kindly Requested To Replace Your Sons Lost Book,Book Details Name:" + bookName + " Book Title:" + bookTitle;
                                } else {
                                    message = "Hi Mr./Mrs " + name + ",You Are Kindly Requested To Replace Your Daughters Lost Book,Book Details Name:" + bookName + " Book Title:" + bookTitle;
                                }
                                if (ConfigurationIntialiser.smsOfflineSender()) {

                                    String pf = p.replaceFirst("0", "+254");
                                    phone = pf;

                                    PreparedStatement ps1;

                                    String sqlInsert =
                                            "INSERT INTO " +
                                                    "ozekimessageout (receiver,msg,status) " +
                                                    "VALUES " +
                                                    "('" + pf + "','" + message + "','" + "Send" + "')";
                                    ps1 = con.prepareStatement(sqlInsert);

                                    ps1.execute();


                                } else {
                                    JOptionPane.showMessageDialog(this, "System Unable To Send Message\nNo Internet Connection,Host Machine Offline");
                                }


                            } else {

                            }

                        }
                    }


                    String sql4 = "Select books.bookserial,bookname,admnumber,dateissued,booktitle from ALumnibooksRecord,books where books.bookserial=Alumnibooksrecord.bookserial and books.status='" + "ISSUED" + "' and Alumnibooksrecord.status='" + "LOST" + "' order by bookname";
                    ps = con.prepareStatement(sql4);
                    rs = ps.executeQuery();
                    while (rs.next()) {
                        String adm = rs.getString("AdmNumber");
                        String bookTitle = rs.getString("booktitle");
                        String bookName = rs.getString("bookname");


                        String sql = "Select gender,parentfullNames,telephone1 from admission where admissionnumber='" + adm + "' and currentform like '" + "AL" + "%'";
                        ps = con.prepareStatement(sql);
                        ResultSet RS = ps.executeQuery();
                        if (RS.next()) {

                            if (!RS.getString("telephone1").equalsIgnoreCase("")) {
                                String p = RS.getString("telephone1");
                                String phone;
                                String message;
                                String name = RS.getString("parentfullnames");
                                if (RS.getString("gender").equalsIgnoreCase("Male")) {
                                    message = "Hi Mr./Mrs " + name + ",You Are Kindly Requested To Replace Your So's Lost Book,Book Details Name:" + bookName + " Book Title:" + bookTitle;
                                } else {
                                    message = "Hi Mr./Mrs " + name + ",You Are Kindly Requested To Replace Your Daughter's Lost Book,Book Details Name:" + bookName + " Book Title:" + bookTitle;
                                }
                                if (ConfigurationIntialiser.smsOfflineSender()) {

                                    String pf = p.replaceFirst("0", "+254");
                                    phone = pf;

                                    PreparedStatement ps1;

                                    String sqlInsert =
                                            "INSERT INTO " +
                                                    "ozekimessageout (receiver,msg,status) " +
                                                    "VALUES " +
                                                    "('" + pf + "','" + message + "','" + "send" + "')";
                                    ps1 = con.prepareStatement(sqlInsert);

                                    ps1.execute();

                                } else {
                                    JOptionPane.showMessageDialog(this, "System Unable To Send Message\nNo Internet Connection,Host Machine Offline");
                                }


                            } else {

                            }

                        }


                    }


                    JOptionPane.showMessageDialog(this, "Notifications Sent Succcessfully");


                } else if (jclass.getSelectedIndex() == 1) {
                    String sql3 = "Select books.bookserial,bookname,admnumber,dateissued,booktitle from issuedbooks,books where books.bookserial=issuedbooks.bookserial and books.status='" + "ISSUED" + "' and issuedbooks.status='" + "LOST" + "' order by bookname";
                    ps = con.prepareStatement(sql3);
                    rs = ps.executeQuery();
                    while (rs.next()) {
                        String adm = rs.getString("AdmNumber");
                        String bookTitle = rs.getString("booktitle");
                        String bookName = rs.getString("bookname");


                        String sql = "Select gender,parentfullNames,telephone1 from admission where admissionnumber='" + adm + "' and currentform like '" + "FM" + "%'";
                        ps = con.prepareStatement(sql);
                        ResultSet RS = ps.executeQuery();
                        if (RS.next()) {
                            if (!RS.getString("telephone1").equalsIgnoreCase("")) {
                                String p = RS.getString("telephone1");
                                String phone;
                                String message;
                                String name = RS.getString("parentfullnames");
                                if (RS.getString("gender").equalsIgnoreCase("Male")) {
                                    message = "Hi Mr./Mrs " + name + ",You Are Kindly Requested To Replace Your Sons Lost Book,Book Details Name:" + bookName + " Book Title:" + bookTitle;
                                } else {
                                    message = "Hi Mr./Mrs " + name + ",You Are Kindly Requested To Replace Your Daughters Lost Book,Book Details Name:" + bookName + " Book Title:" + bookTitle;
                                }
                                if (ConfigurationIntialiser.smsOfflineSender()) {

                                    String pf = p.replaceFirst("0", "+254");
                                    phone = pf;

                                    PreparedStatement ps1;

                                    String sqlInsert =
                                            "INSERT INTO " +
                                                    "ozekimessageout (receiver,msg,status) " +
                                                    "VALUES " +
                                                    "('" + pf + "','" + message + "','" + "send" + "')";
                                    ps1 = con.prepareStatement(sqlInsert);

                                    ps1.execute();

                                } else {
                                    JOptionPane.showMessageDialog(this, "System Unable To Send Message\nNo Internet Connection,Host Machine Offline");
                                }


                            } else {

                            }

                        }


                    }
                    JOptionPane.showMessageDialog(this, "Notifications Sent Succcessfully");
                } else if (jclass.getSelectedIndex() == 2) {


                    String sql3 = "Select books.bookserial,bookname,admnumber,dateissued,booktitle from ALumnibooksRecord,books where books.bookserial=Alumnibooksrecord.bookserial and books.status='" + "ISSUED" + "' and Alumnibooksrecord.status='" + "LOST" + "' order by bookname";
                    ps = con.prepareStatement(sql3);
                    rs = ps.executeQuery();
                    while (rs.next()) {
                        String adm = rs.getString("AdmNumber");
                        String bookTitle = rs.getString("booktitle");
                        String bookName = rs.getString("bookname");


                        String sql = "Select gender,parentfullNames,telephone1 from admission where admissionnumber='" + adm + "' and currentform like '" + "AL" + "%'";
                        ps = con.prepareStatement(sql);
                        ResultSet RS = ps.executeQuery();
                        if (RS.next()) {

                            if (!RS.getString("telephone1").equalsIgnoreCase("")) {
                                String p = RS.getString("telephone1");
                                String phone;
                                String message;
                                String name = RS.getString("parentfullnames");
                                if (RS.getString("gender").equalsIgnoreCase("Male")) {
                                    message = "Hi Mr./Mrs " + name + ",You Are Kindly Requested To Replace Your So's Lost Book,Book Details Name:" + bookName + " Book Title:" + bookTitle;
                                } else {
                                    message = "Hi Mr./Mrs " + name + ",You Are Kindly Requested To Replace Your Daughter's Lost Book,Book Details Name:" + bookName + " Book Title:" + bookTitle;
                                }
                                if (ConfigurationIntialiser.smsOfflineSender()) {

                                    String pf = p.replaceFirst("0", "+254");
                                    phone = pf;

                                    PreparedStatement ps1;

                                    String sqlInsert =
                                            "INSERT INTO " +
                                                    "ozekimessageout (receiver,msg,status) " +
                                                    "VALUES " +
                                                    "('" + pf + "','" + message + "','" + "send" + "')";
                                    ps1 = con.prepareStatement(sqlInsert);

                                    ps1.execute();

                                } else {
                                    JOptionPane.showMessageDialog(this, "System Unable To Send Message\nNo Internet Connection,Host Machine Offline");
                                }


                            } else {

                            }

                        }


                    }
                    JOptionPane.showMessageDialog(this, "Notifications Sent Succcessfully");

                }

            } catch (HeadlessException | SQLException sq) {
                sq.printStackTrace();
                JOptionPane.showMessageDialog(null, sq.getMessage());

            }


        }

    }

}
 

