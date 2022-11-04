/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package highschool;

import java.awt.Color;
import java.awt.Font;
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
import javax.swing.ButtonGroup;
import javax.swing.JComponent;
import javax.swing.JFrame;

import javax.swing.JOptionPane;
import javax.swing.JProgressBar;

import javax.swing.JWindow;

import static javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE;

import javax.swing.border.TitledBorder;

/**
 * @author FRED
 */
public class SmsParentExamResults extends JFrame implements ActionListener {

    private Connection con = DbConnection.connectDb();
    private int width = 750;
    private int height = 400;
    private PreparedStatement ps;
    private ResultSet rs;
    private FredButton send = new FredButton("Send Results");
    private FredButton cancel = new FredButton("Close");

    private FredCombo jclass = new FredCombo("Select Class");
    private FredLabel name = new FredLabel("Class");
    private FredLabel examname = new FredLabel("Exam Name");
    private FredLabel examcode = new FredLabel("Exam Code");
    private FredCombo jexamcode = new FredCombo("Select Exam Code");
    private FredLabel stream = new FredLabel("Stream");
    private FredCombo jstream = new FredCombo("Select Stream");
    private FredCombo jterm = new FredCombo("Select Term");
    private FredLabel term = new FredLabel("Term");
    private FredCombo jexame = new FredCombo("Select Exam");
    private FredLabel academicyear = new FredLabel("Academic Year");
    private FredCombo jacademicyear = new FredCombo("Select Academic Year");
    private FredCheckBox option1 = new FredCheckBox("Order By Performance");
    private FredCheckBox option2 = new FredCheckBox("Include Next Term Fee Balance");
    private ButtonGroup group = new ButtonGroup();
    private FredLabel adm = new FredLabel("admission Number");
    private FredTextField jadm = new FredTextField();
    String tm, cl, yr, ex;

    public SmsParentExamResults() {


        setSize(width, height);
        setTitle("Result Sending Panel");
        ((JComponent) getContentPane()).setBorder(
                BorderFactory.createMatteBorder(5, 5, 5, 5, Color.MAGENTA));

        this.setIconImage(FrameProperties.icon());


        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
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
        setLayout(null);
        setResizable(false);
        setLocationRelativeTo(null);
        name.setBounds(30, 30, 150, 30);
        add(name);
        jclass.setBounds(150, 30, 200, 30);
        add(jclass);
        stream.setBounds(400, 30, 150, 30);
        add(stream);
        jstream.setBounds(500, 30, 200, 30);
        add(jstream);
        term.setBounds(30, 100, 150, 30);
        add(term);
        jterm.setBounds(150, 100, 200, 30);
        add(jterm);
        academicyear.setBounds(400, 100, 150, 30);
        add(academicyear);
        jacademicyear.setBounds(500, 100, 200, 30);
        add(jacademicyear);
        examname.setBounds(30, 180, 150, 30);
        add(examname);
        jexame.setBounds(150, 180, 200, 30);
        add(jexame);
        examcode.setBounds(400, 180, 150, 30);
        add(examcode);
        jexamcode.setBounds(500, 180, 200, 30);
        add(jexamcode);
        adm.setBounds(50, 230, 180, 30);
        add(adm);
        jadm.setBounds(200, 230, 80, 30);
        add(jadm);
        option1.setBounds(280, 230, 180, 30);
        add(option1);
        option2.setBounds(510, 230, 200, 30);
        add(option2);
        cancel.setBounds(100, 270, 130, 50);
        add(cancel);
        send.setBounds(450, 270, 200, 50);
        add(send);
        try {

            for (int k = 2015; k <= Globals.academicYear(); ++k) {
                jacademicyear.addItem(k);
            }
            String sqls = "Select examname from exams group by examname";
            ps = con.prepareStatement(sqls);
            rs = ps.executeQuery();
            while (rs.next()) {
                jexame.addItem(rs.getString("ExamName"));
            }

            String sql2 = "Select * from streams";

            ps = con.prepareStatement(sql2);
            rs = ps.executeQuery();
            while (rs.next()) {

                jstream.addItem(rs.getString("StreamName"));
            }
            String sqld = "Select examcode from exams ";
            ps = con.prepareStatement(sqld);
            rs = ps.executeQuery();
            while (rs.next()) {
                jexamcode.addItem(rs.getString("Examcode"));
            }
            String sql = "Select * from classes where precision1<9 order by precision1 asc";
            con = DbConnection.connectDb();
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                jclass.addItem(rs.getString("ClassName"));

            }
            String sql9 = "Select* from terms";
            ps = con.prepareStatement(sql9);
            rs = ps.executeQuery();
            while (rs.next()) {
                jterm.addItem(rs.getString("TermName"));

            }
        } catch (Exception e) {
        }
        setVisible(true);
        jterm.addActionListener(this);
        jclass.addActionListener(this);
        jexame.addActionListener(this);
        jacademicyear.addActionListener(this);
        send.addActionListener(this);
        cancel.addActionListener(this);
        jadm.setFont(new Font("serif", Font.BOLD, 18));
        if (jstream.getItemCount() < 3) {
            jstream.setSelectedIndex(1);
        }
        jacademicyear.setSelectedItem(Globals.academicYear());
        jterm.setSelectedItem(Globals.currentTermName());
        jadm.addKeyListener(new KeyAdapter() {

            public void keyTyped(KeyEvent key) {
                char c = key.getKeyChar();
                if (Character.isAlphabetic(c) || jadm.getText().length() > 8) {
                    key.consume();
                    getToolkit().beep();
                }
            }

            public void keyReleased(KeyEvent key) {
                try {
                    String sql = "Select firstname,middlename,lastname,currentform,ImageLocation,currentstream from admission where admissionnumber='" + jadm.getText() + "'";
                    ps = con.prepareStatement(sql);
                    rs = ps.executeQuery();
                    if (rs.next()) {

                        jadm.setForeground(Color.black);


                    } else {
                        jadm.setForeground(Color.red);


                    }

                } catch (Exception sq) {
                    sq.printStackTrace();
                    JOptionPane.showMessageDialog(null, sq.getMessage());
                }
            }

        });
    }

    public void actionPerformed(ActionEvent e) {
        Object obj = e.getSource();
        if (obj == cancel) {
            CurrentFrame.currentWindow();
            dispose();
        } else if (obj == jterm) {
            if (jterm.getSelectedIndex() > 0) {
                tm = jterm.getSelectedItem().toString().toString();
            } else {
                tm = null;
            }
        } else if (obj == jclass) {
            if (jclass.getSelectedIndex() > 0) {
                cl = jclass.getSelectedItem().toString();
            } else {
                cl = null;
            }
        } else if (obj == jacademicyear) {
            if (jacademicyear.getSelectedIndex() > 0) {
                yr = jacademicyear.getSelectedItem().toString();
            } else {
                yr = null;
            }
        } else if (obj == jexame) {
            jexamcode.removeActionListener(this);
            if (jexame.getSelectedIndex() > 0) {
                ex = jexame.getSelectedItem().toString();

                String excode = ExamCodesGenerator.generatecode(cl, yr, tm, ex).toUpperCase();

                jexamcode.setSelectedItem(excode);

            }
            jexamcode.addActionListener(this);
        } else if (obj == send) {
            if (jclass.getSelectedIndex() == 0) {
                JOptionPane.showMessageDialog(null, "Kindly Select Class");
            } else {
                if (jterm.getSelectedIndex() == 0) {
                    JOptionPane.showMessageDialog(null, "Kindly Select Term");
                } else {

                    if (jacademicyear.getSelectedIndex() == 0) {
                        JOptionPane.showMessageDialog(null, "Kindly Select Academic Year");
                    } else {
                        if (jexamcode.getSelectedIndex() == 0) {
                            JOptionPane.showMessageDialog(this, "Exam Not Selected Or Selection Order Not Adhered\nKindly Repet The Selection Process For The System To Autocupture Exam Code");
                        } else {

                            if (jadm.getText().isEmpty()) {

                                String STREAM = "";
                                if (jstream.getSelectedIndex() == 0) {
                                    STREAM = "COMBINED";
                                } else {
                                    STREAM = jstream.getSelectedItem().toString();
                                }
                                new Thread() {
                                    @Override
                                    public void run() {
                                        JWindow dia = new JWindow();
                                        JProgressBar bar = new JProgressBar();
                                        bar.setIndeterminate(true);

                                        dia.setSize(300, 60);
                                        bar.setBorder(new TitledBorder("Sending Exam Results To Parents...."));

                                        dia.setAlwaysOnTop(true);
                                        dia.setLocationRelativeTo(CurrentFrame.mainFrame());
                                        dia.setIconImage(FrameProperties.icon());
                                        dia.add(bar);
                                        dia.setVisible(true);
                                        if (smsExamToParents()) {
                                            dia.dispose();
                                            JOptionPane.showMessageDialog(CurrentFrame.mainFrame(), "Results  Successfully Placed On The SMS Queue");


                                        } else {
                                            dia.dispose();
                                            JOptionPane.showMessageDialog(CurrentFrame.mainFrame(), "No Results Were Found For The Selected Class Periods Combination\n Kindly Analyse  The Marks If They Have previously Been Entered And No Analysis Has Been Done Or Check The Selection Combinations....!! ");

                                        }


                                    }

                                }.start();


                            } else {
                                if (Globals.admissionVerifier(jadm.getText())) {

                                    smsExamToParentsBitWise(jadm.getText());
                                    JOptionPane.showMessageDialog(this, "Results For " + Globals.fullName(jadm.getText()) + "  Transmitted To SMS Queue succesfully");
                                } else {
                                    JOptionPane.showMessageDialog(this, "Invalid admission Number");
                                }
                            }


                        }
                    }

                }
            }
        }
    }

    public static void main(String[] args) {
        new SmsParentExamResults();
    }


    public boolean smsExamToParentsBitWise(String ADM) {
        String classcodefee = Globals.classCode(jclass.getSelectedItem().toString());
        ArrayList messagesList = new ArrayList<Map>();
        HashMap messageData = new HashMap<String, String>();
        try {
            String nextTermFee = "";
            int year = (int) jacademicyear.getSelectedItem();
            String termcode = "";
            String termname = "";

            String sql = "Select * from terms where status='" + "Next" + "'";
            ps = con.prepareStatement(sql);
            ResultSet rr = ps.executeQuery();
            if (rr.next()) {
                termcode = rr.getString("Termcode");
                termname = rr.getString("TermName");
            }
            if (termname.equalsIgnoreCase("Term 1")) {
                year += 1;
            } else {

            }


            String sqla = "Select classpositionthisterm,classpositionthistermoutof,admnumber,classpositionlastterm,streampositionthisterm,streampositionlastterm,fullname,meangrade,meanpoints,totalmarks,totalpoints from examanalysistable where academicyear='" + jacademicyear.getSelectedItem() + "' and  classname='" + jclass.getSelectedItem() + "' and examcode='" + jexamcode.getSelectedItem() + "' and admnumber='" + ADM + "' group by admnumber";
            ps = con.prepareStatement(sqla);
            rs = ps.executeQuery();
            while (rs.next()) {

                String grade = "", examscore = "", meangrade = "", totalmarks, totalpoints, meanpoints, subjectName;
                int entries = 0;
                meangrade = rs.getString("meangrade");
                totalmarks = rs.getString("totalmarks");
                totalpoints = rs.getString("totalpoints");
                String adm = rs.getString("admnumber");
                String ovrposition = rs.getString("classpositionthisterm");
                String streamposition = rs.getString("Streampositionthisterm");
                String ovroutof = rs.getString("classpositionthistermoutof");
                String streamoutof = "";
                String Name = rs.getString("fullName");

                String Examname = "";
                if (jexame.getSelectedItem().toString().equalsIgnoreCase("Total")) {
                    Examname = "AVERAGE";
                } else {
                    Examname = jexame.getSelectedItem().toString();
                }


                String subjectResults = jterm.getSelectedItem().toString() + " " + Examname + " Results For :" + Name + " ";


                String sqlb = "SElect Distinct subjectcode from subjects order by subjectcode";
                ps = con.prepareCall(sqlb);
                ResultSet rsb = ps.executeQuery();

                while (rsb.next()) {
                    String subcode = rsb.getString("Subjectcode");

                    String sqlc = "Select  exampercentage,subjectexamgrade,subjectName from examanalysistable where academicyear='" + jacademicyear.getSelectedItem() + "' and examcode='" + jexamcode.getSelectedItem() + "'  and subjectcode='" + subcode + "' and admnumber='" + adm + "' and classname='" + jclass.getSelectedItem() + "'";
                    ps = con.prepareStatement(sqlc);
                    ResultSet rsc = ps.executeQuery();
                    if (rsc.next()) {
                        examscore = rsc.getString("ExamPercentage");
                        grade = rsc.getString("subjectexamgrade");

                        subjectResults = subjectResults + " " + rsc.getString("subjectName") + ": " + examscore + " " + grade + ",";
                    } else {
                        examscore = "";
                        grade = "";
                    }

                }


                subjectResults = subjectResults + " Total Points:" + totalpoints + " Mean Grade:" + rs.getString("meangrade") + " Overall Pos:" + rs.getString("classpositionthisterm") + " Out Of :" + ovroutof + ".  " + nextTermFee;

                sql = "Select gender,parentfullNames,telephone1 from admission where admissionnumber='" + adm + "'";
                ps = con.prepareStatement(sql);
                ResultSet RS = ps.executeQuery();
                if (RS.next()) {


                    if (!RS.getString("telephone1").equalsIgnoreCase("")) {
                        String p = RS.getString("telephone1");
                        String phone;
                        String message;
                        String name = RS.getString("parentfullnames");
                        message = subjectResults;
                        p = "254" + p.substring(1);
                        phone = p;
                        if (option2.isSelected()) {
                            message = message + ".Next Term Payable Fee KSH " + (Globals.balanceCalculator(adm) + Globals.nextTermFee(adm, termcode, String.valueOf(year), classcodefee));
                        }
                        messageData = new HashMap<String, String>();
                        messageData.put("message", message);
                        messageData.put("phone", phone);
                        messagesList.add(messageData);


                    } else {

                    }

                }


            }

            if (rs.previous()) {
                MessageGateway.batchMessageQueuer(messagesList);
                return true;
            } else {
                return false;
            }


        } catch (Exception sq) {
            sq.printStackTrace();
            JOptionPane.showMessageDialog(null, sq.getMessage());
            return false;
        }


    }


    public boolean smsExamToParents() {
        ArrayList messagesList = new ArrayList<Map>();
        HashMap messageData = new HashMap<String, String>();

        try {
            String nextTermFee = "";
            int year = (int) jacademicyear.getSelectedItem();
            String termcode = "";
            String termname = "";
            String classcodefee = Globals.classCode(jclass.getSelectedItem().toString());

            String sql = "Select * from terms where status='" + "Next" + "'";
            ps = con.prepareStatement(sql);
            ResultSet rr = ps.executeQuery();
            if (rr.next()) {
                termcode = rr.getString("Termcode");
                termname = rr.getString("TermName");
            }
            if (termname.equalsIgnoreCase("Term 1")) {
                year += 1;
            } else {

            }


            String sqla = "Select classpositionthisterm,classpositionthistermoutof,admnumber,classpositionlastterm,streampositionthisterm,streampositionlastterm,fullname,meangrade,meanpoints,totalmarks,totalpoints from examanalysistable where academicyear='" + jacademicyear.getSelectedItem() + "' and  classname='" + jclass.getSelectedItem() + "' and examcode='" + jexamcode.getSelectedItem() + "' group by admnumber order by classpositionthisterm";
            ps = con.prepareStatement(sqla);
            rs = ps.executeQuery();
            while (rs.next()) {

                String grade = "", examscore = "", meangrade = "", totalmarks, totalpoints, meanpoints, subjectName;
                int entries = 0;
                meangrade = rs.getString("meangrade");
                totalmarks = rs.getString("totalmarks");
                totalpoints = rs.getString("totalpoints");
                String adm = rs.getString("admnumber");
                String ovrposition = rs.getString("classpositionthisterm");
                String streamposition = rs.getString("Streampositionthisterm");
                String ovroutof = rs.getString("classpositionthistermoutof");
                String streamoutof = "";
                String Name = rs.getString("fullName");

                String Examname = "";
                if (jexame.getSelectedItem().toString().equalsIgnoreCase("Total")) {
                    Examname = "AVERAGE";
                } else {
                    Examname = jexame.getSelectedItem().toString();
                }


                String subjectResults = jterm.getSelectedItem().toString() + " " + Examname + " Results For :" + Name + " ";


                String sqlb = "SElect Distinct subjectcode from subjects order by subjectcode";
                ps = con.prepareCall(sqlb);
                ResultSet rsb = ps.executeQuery();

                while (rsb.next()) {
                    String subcode = rsb.getString("Subjectcode");

                    String sqlc = "Select  exampercentage,subjectexamgrade,subjectName from examanalysistable where academicyear='" + jacademicyear.getSelectedItem() + "' and examcode='" + jexamcode.getSelectedItem() + "'  and subjectcode='" + subcode + "' and admnumber='" + adm + "' and classname='" + jclass.getSelectedItem() + "'";
                    ps = con.prepareStatement(sqlc);
                    ResultSet rsc = ps.executeQuery();
                    if (rsc.next()) {
                        examscore = rsc.getString("ExamPercentage");
                        grade = rsc.getString("subjectexamgrade");

                        subjectResults = subjectResults + " " + rsc.getString("subjectName") + ": " + examscore + " " + grade + ",";
                    } else {
                        examscore = "";
                        grade = "";
                    }

                }


                subjectResults = subjectResults + " Total Points:" + totalpoints + " Mean Grade:" + rs.getString("meangrade") + " Overall Pos:" + rs.getString("classpositionthisterm") + " Out Of :" + ovroutof + ".  " + nextTermFee;

                sql = "Select gender,parentfullNames,telephone1 from admission where admissionnumber='" + adm + "'";
                ps = con.prepareStatement(sql);
                ResultSet RS = ps.executeQuery();
                if (RS.next()) {


                    if (!RS.getString("telephone1").equalsIgnoreCase("")) {
                        String p = RS.getString("telephone1");
                        String phone;
                        String message;
                        String name = RS.getString("parentfullnames");
                        message = subjectResults;

                        if (option2.isSelected()) {
                            message = message + ".Next Term Payable Fee KSH " + (Globals.balanceCalculator(adm) + Globals.nextTermFee(adm, termcode, String.valueOf(year), classcodefee));
                        }
                        p = "254" + p.substring(1);
                        phone = p;
                        messageData = new HashMap<String, String>();
                        messageData.put("message", message);
                        messageData.put("phone", phone);
                        messagesList.add(messageData);


                    } else {

                    }

                }


            }


            if (rs.previous()) {
                MessageGateway.batchMessageQueuer(messagesList);
                return true;
            } else {
                return false;
            }


        } catch (Exception sq) {
            sq.printStackTrace();
            JOptionPane.showMessageDialog(null, sq.getMessage());
            return false;
        }


    }
}
