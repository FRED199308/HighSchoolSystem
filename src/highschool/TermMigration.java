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
import javax.swing.JComponent;
import javax.swing.JFrame;

import javax.swing.JOptionPane;
import javax.swing.JProgressBar;
import javax.swing.JTextPane;

import static javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE;

import javax.swing.border.TitledBorder;

/**
 * @author FRED
 */
public class TermMigration extends JFrame implements ActionListener {

    public static void main(String[] args) {
        new TermMigration();
    }

    private int height = 440;
    private int width = 700;
    private JProgressBar bar = new JProgressBar();
    private Connection con;
    private FredCheckBox mig;
    private PreparedStatement ps;
    private FredButton save, cancel;
    private FredLabel classl;
    private FredCombo jclass;
    private JTextPane pane = new JTextPane();
    ResultSet rs;

    public TermMigration() {
        setSize(width, height);
        setLocationRelativeTo(null);
        setLayout(null);
        setTitle("Term Migration Panel");
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
        mig = new FredCheckBox("Overall School Term Transfer");
        save = new FredButton("Perform Term Migration");
        cancel = new FredButton("Close");
        classl = new FredLabel("Class/Form");
        jclass = new FredCombo("Select Form");
        bar.setVisible(false);
        mig.setBounds(150, 30, 300, 30);
        add(mig);
        pane.setBounds(150, 100, 350, 150);
        add(pane);
        bar.setBounds(200, 280, 300, 30);
        add(bar);
        cancel.setBounds(120, 330, 120, 40);
        add(cancel);
        save.setBounds(390, 330, 180, 40);
        add(save);
        bar.setIndeterminate(true);
        bar.setBorder(new TitledBorder("Performing Term Migration... may take several minutes..sit and relax"));
        pane.setText("You Cannot Perform Term Tranfer When The Current Term Is Term 3 You Should Consinder Class Tranfer At Term 3,The System Will Automatically Move To Term 1 Of The Next Academic Year.KINDLY NOTE THAT THIS ACTION IS NOT REVERSABLE.");
        pane.setBorder(new TitledBorder("Information"));
        mig.addActionListener(this);
        cancel.addActionListener(this);
        save.addActionListener(this);
        try {
            String sql = "select * from classes  where precision1<5 order by precision1";
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                jclass.addItem(rs.getString("ClassName"));
            }
        } catch (SQLException sq) {
            JOptionPane.showMessageDialog(this, sq.getMessage());
        }
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
            if (!mig.isSelected()) {
                getToolkit().beep();
                JOptionPane.showMessageDialog(this, "Kindly Confirm Term Tranfer By Marking The  Overall Term Tranfer Option");
            } else {
                new Thread() {
                    @Override
                    public void run() {
                        String termcode = "";
                        String classcode = "";
                        String voteheadid = "";
                        bar.setVisible(true);
                        bar.setIndeterminate(true);
                        try {
                            String CURRENTTERM = "";

                            String sql = "Select termcode from terms where status='" + "next" + "'";
                            ps = con.prepareStatement(sql);
                            rs = ps.executeQuery();
                            while (rs.next()) {
                                termcode = rs.getString("termcode");
                            }
                            boolean proceed = true;


                            if (ConfigurationIntialiser.migrationReadiness()) {
                                proceed = true;
                            } else {
                                JOptionPane.showMessageDialog(CurrentFrame.mainFrame(), "Finance Department Not Ready For Term Migration");
                                proceed = false;
                            }
                            if (proceed) {
                                CurrentFrame.secondFrame().setEnabled(false);
                                if (Globals.currentTermName().equalsIgnoreCase("Term 3")) {
                                    bar.setVisible(false);
                                    bar.setIndeterminate(false);
                                    JOptionPane.showMessageDialog(null, "End Of Academic Year,Kindly Consinder Doing Class Transfer");
                                } else {

                                    String ccode = "", pcode = "", ncode = "", termname = "";
                                    double fee = 0, openingbal = 0;
                                    String sql4 = "Select termcode,termname from terms where status='" + "current" + "'";
                                    ps = con.prepareStatement(sql4);
                                    rs = ps.executeQuery();
                                    if (rs.next()) {
                                        CURRENTTERM = rs.getString("Termname");
                                        ccode = rs.getString("Termcode");
                                    }

                                    String sql5 = "Select termcode from terms where status='" + "next" + "'";
                                    ps = con.prepareStatement(sql5);
                                    rs = ps.executeQuery();
                                    if (rs.next()) {
                                        ncode = rs.getString("Termcode");
                                    }

                                    String sql6 = "Select termcode from terms where status='" + "previous" + "'";
                                    ps = con.prepareStatement(sql6);
                                    rs = ps.executeQuery();
                                    if (rs.next()) {
                                        pcode = rs.getString("Termcode");
                                    }
                                    String querry = "Update terms set status='" + "current" + "' where termcode='" + ncode + "'";
                                    ps = con.prepareStatement(querry);
                                    ps.execute();

                                    String querry1 = "Update terms set status='" + "previous" + "' where termcode='" + ccode + "'";
                                    ps = con.prepareStatement(querry1);
                                    ps.execute();
                                    String querry2 = "Update terms set status='" + "next" + "' where termcode='" + pcode + "'";
                                    ps = con.prepareStatement(querry2);
                                    ps.execute();
                                    pcode = ccode;
                                    String querry3 = "Select termcode,termname from terms where status='" + "current" + "'";
                                    ps = con.prepareStatement(querry3);
                                    rs = ps.executeQuery();
                                    if (rs.next()) {
                                        ccode = rs.getString("termcode");
                                        termname = rs.getString("termname");
                                    }


                                    String sql9 = "Update admission set currentterm='" + ccode + "'";
                                    ps = con.prepareStatement(sql9);
                                    ps.execute();


                                    String sqlb = "Select distinct examname from exams where transferable='" + "true" + "'";
                                    ps = con.prepareStatement(sqlb);
                                    ResultSet rss = ps.executeQuery();
                                    while (rss.next()) {
                                        String name = rss.getString("ExamName");
                                        String sqld = "select * from classes  where precision1<5 order by precision1";
                                        ps = con.prepareStatement(sqld);
                                        rs = ps.executeQuery();
                                        while (rs.next()) {
                                            String classname = rs.getString("ClassName");
                                            classcode = rs.getString("Classcode");
                                            int weight = 0;

                                            String examcode1 = ExamCodesGenerator.generatecode(classname, String.valueOf((Globals.academicYear())), CURRENTTERM, name.toUpperCase());
                                            String qrr = "Select weight from examweights where examcode='" + examcode1 + "'";
                                            ps = con.prepareStatement(qrr);
                                            ResultSet rr = ps.executeQuery();
                                            if (rr.next()) {
                                                weight = rr.getInt("weight");

                                            } else {
                                                weight = 100;

                                            }

                                            String examcode = ExamCodesGenerator.generatecode(classname, String.valueOf(Globals.academicYear()), Globals.currentTermName(), name.toUpperCase());
                                            jclass.addItem(rs.getString("ClassName"));
                                            ResultSet Rs;
                                            String sql2 = "Insert Into exams values('" + name + "','" + examcode + "','" + Globals.currentTerm() + "','" + Globals.academicYear() + "','" + "true" + "','" + classcode + "')";
                                            ps = con.prepareStatement(sql2);
                                            ps.execute();

                                            String querry5 = "Insert into examweights values('" + examcode + "','" + weight + "')";
                                            ps = con.prepareStatement(querry5);
                                            ps.execute();


                                        }

                                    }

                                    String adm = "", pycode = "", streamcode = "";

                                    int counter = 0;

                                    String className = "";
                                    String sql8 = "select * from classes where precision1<5 order by precision1";
                                    ps = con.prepareStatement(sql8);
                                    ResultSet RS;
                                    RS = ps.executeQuery();
                                    while (RS.next()) {
                                        className = RS.getString("classname");
                                        classcode = RS.getString("Classcode");


                                        ResultSet rx;
                                        String sql10 = "Select admissionNumber from admission where currentform='" + classcode + "' and currentterm='" + ccode + "' ";
                                        ps = con.prepareStatement(sql10);
                                        rx = ps.executeQuery();
                                        while (rx.next()) {


                                            counter++;
                                            adm = rx.getString("AdmissionNumber");
                                            String program = Globals.studentProgram(adm);


                                            String sql7 = "Select amountperhead,voteheadid from studentpayablevoteheads where termcode='" + termcode + "' and academicYear='" + Globals.academicYear() + "'  and amountperhead!='" + "" + "' and amountPerhead!='" + "0" + "' and program='" + program + "' and classcode='" + classcode + "'";
                                            ps = con.prepareStatement(sql7);
                                            rs = ps.executeQuery();
                                            while (rs.next()) {

                                                fee = rs.getDouble("amountperhead");
                                                voteheadid = rs.getString("voteheadid");

                                                String sql12 = "Insert into payablevoteheadperstudent values('" + adm + "','" + Globals.termname(ccode) + "','" + Globals.academicYear() + "','" + voteheadid + "','" + fee + "',curDate(),'" + "INV" + "')";
                                                ps = con.prepareStatement(sql12);
                                                ps.execute();
                                                openingbal = 0;

                                            }


                                        }


                                    }
                                    String querry22 = "update systemconfiguration set status='" + "False" + "' where configurationid='" + "CO024" + "'";
                                    ps = con.prepareStatement(querry22);
                                    ps.execute();

                                    CurrentFrame.secondFrame().setEnabled(true);
                                    bar.setVisible(false);
                                    JOptionPane.showMessageDialog(null, "Term Migration Succesfull");


                                }


                            }


                        } catch (SQLException sq) {
                            bar.setVisible(false);
                            CurrentFrame.secondFrame().setEnabled(true);
                            bar.setIndeterminate(false);
                            JOptionPane.showMessageDialog(null, sq.getMessage());
                            sq.printStackTrace();
                        }
                    }

                }.start();


            }


        }
    }

}
