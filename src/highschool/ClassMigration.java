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
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JTextPane;

import static javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE;

import javax.swing.border.TitledBorder;

import net.miginfocom.swing.MigLayout;

/**
 * @author FRED
 */
public class ClassMigration extends JFrame implements ActionListener {

    public static void main(String[] args) {
        new ClassMigration();
    }

    private int width = 700, height = 480;
    private FredButton save, cancel;
    private PreparedStatement ps;
    private ResultSet rs;
    private Connection con;
    private JProgressBar bar;
    private Thread th;
    private FredCheckBox migrate;
    private JTextPane pane;
    private FredCheckBox form1 = new FredCheckBox("Form 1");
    private FredCheckBox form2 = new FredCheckBox("Form 2");
    private FredCheckBox form3 = new FredCheckBox("Form 3");

    private JPanel p = new JPanel();

    public ClassMigration() {

        setSize(width, height);
        setLocationRelativeTo(null);
        setLayout(null);
        setResizable(false);
        setTitle("Class Migration Panel");
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
        migrate = new FredCheckBox("Overall School Class Tranfer");
        pane = new JTextPane();
        save = new FredButton("Perform Class Migration");
        cancel = new FredButton("Close");
        bar = new JProgressBar();
        bar.setIndeterminate(false);
        pane.setEditable(false);
        pane.setFont(new Font("serif", Font.ITALIC, 16));
        pane.setText("Kindly Note That  The Current Form 4 Will "
                + "Automaticlly Join Alumni,You Can Only Perform Class Transfer When The Current Term Is The Last Term.KINDLY NOTE THAT THIS ACTION IS NOT REVERSABLE.ALL DEPARTMENTS MUST CONFIRM THEIR READINESS TO MIGRATE");
        pane.setBorder(new TitledBorder("Information"));
        migrate.setBounds(250, 30, 200, 30);
        add(migrate);
        pane.setBounds(150, 100, 420, 150);
        add(pane);
        p.setBounds(150, 270, 420, 80);
        add(p);
        bar.setBounds(150, 410, 300, 30);
        add(bar);
        bar.setVisible(false);
        bar.setBorder(new TitledBorder("Performing class Migration... may take several minutes..sit and relax"));
        cancel.setBounds(100, 370, 130, 30);
        add(cancel);
        save.setBounds(400, 370, 180, 30);
        add(save);
        p.setLayout(new MigLayout());
        p.setBorder(new TitledBorder("Mark classes To Retain Their Subject Allocation"));
        p.add(form1, "gapleft 30");
        p.add(form2, "gapleft 40");
        p.add(form3, "gapleft 40");
        save.addActionListener(this);
        cancel.addActionListener(this);
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
            th = new Thread(() -> {

                bar.setVisible(true);
                bar.setIndeterminate(true);
                if (migrate.isSelected()) {
                    String CURRENTTERM = "";
                    String f1code = "", f2code = "", f3code = "", voteheadid = "";
                    double fee = 0, openingbal = 0;
                    if (form1.isSelected()) {
                        f1code = Globals.classCode("Form 1");
                    }
                    if (form2.isSelected()) {
                        f2code = Globals.classCode("Form 2");
                    }
                    if (form3.isSelected()) {
                        f3code = Globals.classCode("Form 3");
                    }
                    try {
                        int press = 0;

                        String sql = "select precisions from terms where termcode='" + Globals.currentTerm() + "'";
                        ps = con.prepareStatement(sql);
                        rs = ps.executeQuery();
                        if (rs.next()) {
                            press = rs.getInt("precisions");
                        }
                        if (press == 3) {
                            String termcode = "";
                            String classcode = "";

                            try {
                                String sqlz = "Select termcode from terms where status='" + "next" + "'";
                                ps = con.prepareStatement(sqlz);
                                ResultSet rs7 = ps.executeQuery();
                                while (rs7.next()) {
                                    termcode = rs7.getString("termcode");

                                }
                                boolean proceed = true;

                                if (ConfigurationIntialiser.migrationReadiness()) {
                                    proceed = true;
                                } else {
                                    JOptionPane.showMessageDialog(this, "Finance Department Not Ready For Class Migration");
                                    proceed = false;
                                }


                                if (proceed) {
                                    CurrentFrame.secondFrame.setEnabled(false);
                                    int classOder = 0;
                                    String CLASSCODE = "";
                                    String qq = "Select * from subjectrights";
                                    ps = con.prepareStatement(qq);
                                    rs = ps.executeQuery();
                                    while (rs.next()) {
                                        String CLASS = rs.getString("Classcode");
                                        String teachercode = rs.getString("teachercode");
                                        String subcode = rs.getString("subjectcode");
                                        String dd = "Select precision1 from classes where classcode='" + CLASS + "'";
                                        ps = con.prepareStatement(dd);

                                        ResultSet rr = ps.executeQuery();
                                        if (rr.next()) {
                                            classOder = rr.getInt("Precision1") + 1;
                                        }
                                        String oo = "Select classcode from classes where precision1='" + classOder + "'";
                                        ps = con.prepareStatement(oo);
                                        rr = ps.executeQuery();
                                        if (rr.next()) {
                                            CLASSCODE = rr.getString("Classcode");
                                        }
                                        if (classOder > 4) {
                                            String qq2 = "Delete from subjectrights where subjectcode='" + subcode + "' and teachercode='" + teachercode + "' and classcode='" + CLASS + "'";
                                            ps = con.prepareStatement(qq2);
                                            ps.execute();
                                        } else {
                                            String qq2 = "update subjectrights set classcode='" + CLASSCODE + "' where subjectcode='" + subcode + "' and teachercode='" + teachercode + "'";
                                            ps = con.prepareStatement(qq2);
                                            ps.execute();

                                        }
                                    }


                                    String ccode = "", pcode = "", ncode = "", termname = "";
                                    String pyear = "";

                                    String sql4 = "Select termcode,termname,academicyear from terms where status='" + "current" + "'";
                                    ps = con.prepareStatement(sql4);
                                    ResultSet rs2 = ps.executeQuery();
                                    ResultSetMetaData meta = rs2.getMetaData();


                                    if (rs2.next()) {
                                        CURRENTTERM = rs2.getString("termname");
                                        ccode = rs2.getString("Termcode");

                                        pyear = rs2.getString("academicYear");
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
                                    int nextyear = Globals.academicYear() + 1;

                                    String querry = "Update terms set status='" + "current" + "',academicyear='" + nextyear + "' where termcode='" + ncode + "'";
                                    ps = con.prepareStatement(querry);
                                    ps.execute();

                                    String querry1 = "Update terms set status='" + "previous" + "',academicyear='" + nextyear + "' where termcode='" + ccode + "'";
                                    ps = con.prepareStatement(querry);
                                    ps.execute();
                                    String querry2 = "Update terms set status='" + "next" + "',academicyear='" + nextyear + "' where termcode='" + pcode + "'";
                                    ps = con.prepareStatement(querry2);
                                    ps.execute();
                                    pcode = ccode;
                                    String querry3 = "Select termcode,termname,academicyear from terms where status='" + "current" + "'";
                                    ps = con.prepareStatement(querry3);
                                    rs = ps.executeQuery();
                                    if (rs.next()) {
                                        ccode = rs.getString("termcode");
                                        termname = rs.getString("termname");
                                    }
                                    String sql9 = "Update admission set currentterm='" + ccode + "'";
                                    ps = con.prepareStatement(sql9);
                                    ps.execute();


                                    String adm = "", pycode = "", streamcode = "";

                                    int counter = 0, classPress = 0;

                                    String className = "";
                                    String sql8 = "select * from admission   where currentform like '" + "FM" + "%' order by admissionnumber";
                                    ps = con.prepareStatement(sql8);
                                    ResultSet RS;
                                    RS = ps.executeQuery();
                                    while (RS.next()) {

                                        adm = RS.getString("AdmissionNumber");
                                        streamcode = RS.getString("currentstream");
                                        classcode = RS.getString("Currentform");
                                        String sqlb = "Select precision1 from classes where classcode='" + classcode + "' ";
                                        ps = con.prepareStatement(sqlb);
                                        ResultSet RSB = ps.executeQuery();
                                        if (RSB.next()) {
                                            classPress = RSB.getInt("precision1");
                                        }

                                        String nclasscode = "";
                                        int nclass = classPress + 1;
                                        String sqk = "Select classcode from classes where precision1='" + nclass + "'";
                                        ps = con.prepareStatement(sqk);
                                        ResultSet rsa = ps.executeQuery();
                                        if (rsa.next()) {
                                            nclasscode = rsa.getString("Classcode");
                                        }


                                        if (nclass == 5) {


                                            int year = Globals.academicYear() - 1;
                                            counter++;
                                            String sqlbook = "Select * from issuedBooks where admnumber='" + adm + "'";
                                            ps = con.prepareStatement(sqlbook);
                                            rs = ps.executeQuery();
                                            while (rs.next()) {
                                                String bkserial = "", dateissued = "", status = "", datereturned = "", issueid = "";
                                                bkserial = rs.getString("Bookserial");
                                                dateissued = rs.getString("DateIssued");
                                                datereturned = rs.getString("Datereturned");
                                                status = rs.getString("Status");
                                                issueid = rs.getString("Issueid");
                                                if (datereturned == null) {
                                                    String sqltranfer = "Insert into alumnibooksrecord (bookserial,dateissued,admnumber,status,issueid,yearofcompletion) values('" + bkserial + "','" + dateissued + "','" + adm + "','" + status + "','" + issueid + "','" + year + "')";
                                                    ps = con.prepareStatement(sqltranfer);
                                                    ps.execute();
                                                    String deleter = "Delete from issuedbooks where issueid='" + issueid + "'";
                                                    ps = con.prepareStatement(deleter);
                                                    ps.execute();
                                                } else {
                                                    String sqltranfer = "Insert into alumnibooksrecord (bookserial,dateissued,admnumber,status,issueid,datereturned,yearofcompletion) values('" + bkserial + "','" + dateissued + "','" + adm + "','" + status + "','" + issueid + "','" + datereturned + "','" + year + "')";
                                                    ps = con.prepareStatement(sqltranfer);
                                                    ps.execute();
                                                    String deleter = "Delete from issuedbooks where issueid='" + issueid + "'";
                                                    ps = con.prepareStatement(deleter);
                                                    ps.execute();
                                                }

                                            }
                                            String sqs = "Insert into completionclasslists values('" + adm + "','" + year + "')";
                                            ps = con.prepareStatement(sqs);
                                            ps.execute();
                                            String sqla = "Update admission set currentterm='" + ccode + "',currentform='" + nclasscode + "' where admissionnumber='" + adm + "'";
                                            ps = con.prepareStatement(sqla);
                                            ps.execute();

                                        } else if (nclass < 5) {


                                            String sqla = "Update admission set currentterm='" + ccode + "',currentform='" + nclasscode + "' where admissionnumber='" + adm + "'";
                                            ps = con.prepareStatement(sqla);
                                            ps.execute();

                                            if (classcode.equalsIgnoreCase(f1code) || classcode.equalsIgnoreCase(f2code) || classcode.equalsIgnoreCase(f3code)) {
                                                String qrry = "Select * from studentsubjectallocation where admnumber='" + adm + "' and academicyear='" + (Globals.academicYear() - 1) + "'";
                                                ps = con.prepareStatement(qrry);
                                                ResultSet rx = ps.executeQuery();
                                                while (rx.next()) {
                                                    String subcode = rx.getString("Subjectcode");
                                                    String qr = "Insert into studentsubjectallocation values('" + subcode + "','" + adm + "','" + Globals.academicYear() + "')";
                                                    ps = con.prepareStatement(qr);
                                                    ps.execute();
                                                }
                                            }

                                            String program = Globals.studentProgram(adm);
                                            String sql7 = "Select amountperhead,voteheadid from studentpayablevoteheads where termcode='" + Globals.currentTerm() + "' and academicYear='" + Globals.academicYear() + "'  and amountperhead!='" + "" + "' and amountperhead!='" + "0" + "' and classcode='" + nclasscode + "' and program='" + program + "'";
                                            ps = con.prepareStatement(sql7);
                                            rs = ps.executeQuery();
                                            while (rs.next()) {
                                                fee = rs.getDouble("amountperhead");
                                                voteheadid = rs.getString("voteheadid");

                                                String sql12 = "Insert into payablevoteheadperstudent values('" + adm + "','" + Globals.termname(ccode) + "','" + Globals.academicYear() + "','" + voteheadid + "','" + fee + "',curDate(),'" + "INV" + "')";
                                                ps = con.prepareStatement(sql12);
                                                ps.execute();
                                                openingbal = 0;

                                                sql9 = "Update admission set currentterm='" + ccode + "',currentform='" + nclasscode + "' where admissionnumber='" + adm + "'";
                                                ps = con.prepareStatement(sql9);
                                                ps.execute();

                                            }


                                        }


                                    }
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

                                            String examcode1 = ExamCodesGenerator.generatecode(classname, String.valueOf((Globals.academicYear() - 1)), CURRENTTERM, name.toUpperCase());
                                            String qrr = "Select weight from examweights where examcode='" + examcode1 + "'";
                                            ps = con.prepareStatement(qrr);
                                            ResultSet rr = ps.executeQuery();
                                            if (rr.next()) {
                                                weight = rr.getInt("weight");

                                            } else {
                                                weight = 100;

                                            }
                                            String examcode = ExamCodesGenerator.generatecode(classname, String.valueOf(Globals.academicYear()), Globals.currentTermName(), name.toUpperCase());

                                            ResultSet Rs;
                                            String sql2 = "Insert Into exams values('" + name + "','" + examcode + "','" + Globals.currentTerm() + "','" + Globals.academicYear() + "','" + "true" + "','" + classcode + "')";
                                            ps = con.prepareStatement(sql2);
                                            ps.execute();

                                            String querry5 = "Insert into examweights values('" + examcode + "','" + weight + "')";
                                            ps = con.prepareStatement(querry5);
                                            ps.execute();


                                        }

                                    }
                                    String querry22 = "update systemconfiguration set status='" + "False" + "' where configurationid='" + "CO024" + "'";
                                    ps = con.prepareStatement(querry22);
                                    ps.execute();
                                    this.remove(bar);
                                    revalidate();
                                    repaint();
                                    CurrentFrame.secondFrame().setEnabled(true);
                                    JOptionPane.showMessageDialog(this, "Class Migration Succesfull");
                                    th.stop();


                                }


                            } catch (SQLException ex) {
                                this.remove(bar);
                                revalidate();
                                repaint();
                                Logger.getLogger(TermMigration.class.getName()).log(Level.SEVERE, null, ex);
                                CurrentFrame.secondFrame().setEnabled(true);
                            }


                        } else {
                            this.remove(bar);
                            revalidate();
                            repaint();
                            getToolkit().beep();
                            JOptionPane.showMessageDialog(this, "Cannot Perform class migration at Term " + press);
                            th.suspend();
                        }
                    } catch (SQLException sq) {
                        JOptionPane.showMessageDialog(this, sq.getMessage());
                        sq.printStackTrace();
                    }


                } else {
                    this.remove(bar);
                    revalidate();
                    repaint();

                    getToolkit().beep();

                    JOptionPane.showMessageDialog(this, "kindly Confirm That You Want To Do Class Transfer By Ticking The Button On The Upper Side");
                    th.stop();
                }


            });
            th.start();

        }
    }


}
