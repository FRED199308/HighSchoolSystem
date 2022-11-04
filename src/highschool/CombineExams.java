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
import java.text.NumberFormat;
import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JFrame;

import javax.swing.JOptionPane;
import javax.swing.JProgressBar;

import javax.swing.JWindow;

import static javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE;

import javax.swing.border.TitledBorder;

import org.jfree.chart.title.Title;

/**
 * @author FRED
 */
public class CombineExams extends JFrame implements ActionListener {


    private Connection con = DbConnection.connectDb();
    private int width = 750;
    private int height = 400;
    private PreparedStatement ps;
    private ResultSet rs;
    private FredButton view = new FredButton("Combine Marks");
    private FredButton cancel = new FredButton("Close");
    private FredLabel number = new FredLabel(" Fetch Best :");
    private FredTextField jnumber = new FredTextField("3");
    private FredCombo jclass = new FredCombo("Select Class");
    private FredLabel name = new FredLabel("Class");
    private FredLabel examname = new FredLabel("Exam Name");

    private FredCheckBox reverse = new FredCheckBox("Use Previous Term End Term Exam");
    private FredLabel stream = new FredLabel("Stream");
    private FredCombo jstream = new FredCombo("Select Stream");
    private FredCombo jterm = new FredCombo("Select Term");
    private FredLabel term = new FredLabel("Term");
    private FredCombo jexame = new FredCombo("Select Exam");
    private FredLabel academicyear = new FredLabel("Academic Year");
    private FredCombo jacademicyear = new FredCombo("Select Academic Year");

    String tm, cl, yr, ex;


    public CombineExams() {


        setSize(width, height);
        setTitle("Combine Exam Marks");
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
        reverse.setBounds(400, 180, 250, 30);
        add(reverse);
        //reverse.setBorder(new TitledBorder("kkk"));


        cancel.setBounds(100, 300, 130, 50);
        add(cancel);

        view.setBounds(400, 300, 200, 50);
        add(view);

        try {

            for (int k = 2015; k <= Globals.academicYear(); ++k) {
                jacademicyear.addItem(k);
            }


            jexame.addItem("Combine All Exams");

            String sql2 = "Select * from streams";

            ps = con.prepareStatement(sql2);
            rs = ps.executeQuery();
            while (rs.next()) {

                jstream.addItem(rs.getString("StreamName"));
            }

            String sql = "Select * from classes where precision1<5 order by precision1 asc";
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
        } catch (Exception sq) {
            sq.printStackTrace();
            JOptionPane.showMessageDialog(null, sq.getMessage());
        }
        setVisible(true);
        view.addActionListener(this);
        jexame.addActionListener(this);
        cancel.addActionListener(this);
        jterm.addActionListener(this);
        jacademicyear.addActionListener(this);
        jclass.addActionListener(this);

        jnumber.addKeyListener(new KeyAdapter() {

            public void keyTyped(KeyEvent key) {
                char c = key.getKeyChar();
                if (!Character.isDigit(c) || jnumber.getText().length() > 2) {
                    key.consume();

                }
            }
        });
        jstream.setEnabled(false);
        jclass.addActionListener(this);
        jacademicyear.addActionListener(this);
        jterm.addActionListener(this);
        if (jstream.getItemCount() < 3) {
            jstream.setSelectedIndex(1);
        }
        jacademicyear.setSelectedItem(Globals.academicYear());
        jterm.setSelectedItem(Globals.currentTermName());
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object obj = e.getSource();
        if (obj == view) {

            if (jclass.getSelectedIndex() == 0) {
                JOptionPane.showMessageDialog(this, "Kindly Select The Class");
            } else {
                if (jterm.getSelectedIndex() == 0) {
                    JOptionPane.showMessageDialog(this, "Kindly Select The Term");
                } else {
                    if (jacademicyear.getSelectedIndex() == 0) {
                        JOptionPane.showMessageDialog(this, "Select The Academic Year");
                    } else {

                        if (jexame.getSelectedIndex() == 0) {
                            JOptionPane.showMessageDialog(this, "Kindly Select The Exam Name");
                        } else {


                            //   JOptionPane.showMessageDialog(this, "Unable To Combine Marks ,\nAll Exams May Not Be Ready For Analysis");

                            new Thread() {
                                @Override
                                public void run() {
                                    JWindow dia = new JWindow();
                                    JProgressBar bar = new JProgressBar();
                                    NumberFormat nf = NumberFormat.getNumberInstance();
                                    nf.setMaximumFractionDigits(0);
                                    nf.setMinimumFractionDigits(0);

                                    dia.setSize(300, 60);

                                    bar.setBorder(new TitledBorder("Combining Marks..."));
                                    dia.setAlwaysOnTop(true);
                                    dia.setLocationRelativeTo(CurrentFrame.mainFrame());
                                    dia.setIconImage(FrameProperties.icon());
                                    dia.add(bar);
                                    dia.setVisible(true);
                                    bar.setMaximum(100);
                                    bar.setMinimum(0);
                                    bar.setStringPainted(true);
                                    String excode = ExamCodesGenerator.generatecode(cl, yr, tm, "TOTAL").toUpperCase();

                                    try {

                                        String currentTerm = tm;
                                        int currentyear = Integer.parseInt(yr);
                                        int previousYear = currentyear;
                                        int currenttermNumber = Integer.parseInt(tm.substring(5));
                                        int currentClassNumber = Integer.parseInt(cl.substring(5));
                                        int previousClassNumber = currentClassNumber;


                                        int previoustermNumber = 1;
                                        if (currenttermNumber == 1) {
                                            previoustermNumber = 3;

                                            previousYear = currentyear - 1;
                                            previousClassNumber = previousClassNumber - 1;
                                        } else {
                                            previoustermNumber = currenttermNumber - 1;
                                            previousYear = currentyear;
                                            previousClassNumber = currentClassNumber;
                                        }

                                        String previousTerm = "TERM " + previoustermNumber;
                                        String previuosClass = "Form " + previousClassNumber;
                                        previousYear = previousYear;

                                        String previousExamCode = ExamCodesGenerator.generatecode(previuosClass, String.valueOf(previousYear), previousTerm, "END TERM").toUpperCase();

                                        if (reverse.isSelected()) {

                                            ps = con.prepareStatement("Select * from examcombinationmodes where examcode='" + excode + "'");
                                            rs = ps.executeQuery();
                                            if (rs.next()) {
                                                ps = con.prepareStatement("Update examcombinationmodes set CombineMode='" + "REVERSAL" + "' where examcode='" + excode + "'");
                                                ps.execute();
                                            } else {
                                                ps = con.prepareStatement("Insert into examcombinationmodes values('" + excode + "','" + "REVERSAL" + "')");
                                                ps.execute();
                                            }

                                            int counter = 0;
                                            String sqll = "Select distinct admnumber from  markstable where  termcode='" + Globals.termcode(jterm.getSelectedItem().toString()) + "' and academicyear='" + jacademicyear.getSelectedItem() + "' and classcode='" + Globals.classCode(jclass.getSelectedItem().toString()) + "'";
                                            ps = con.prepareStatement(sqll);
                                            ResultSet ss = ps.executeQuery();
                                            while (ss.next()) {

                                                counter++;
                                            }
                                            if (counter == 0) {
                                                JOptionPane.showMessageDialog(CurrentFrame.mainFrame(), "No Marks Were Found For This Class In The Selected Term And Academic Year");
                                            }
                                            String sqll2 = "Delete from  markstable where  examcode='" + excode + "'";
                                            ps = con.prepareStatement(sqll2);
                                            ps.execute();


                                            int value = 0, counter2 = 0;

                                            String sql2 = "Select distinct admnumber from  markstable where  termcode='" + Globals.termcode(jterm.getSelectedItem().toString()) + "' and academicyear='" + jacademicyear.getSelectedItem() + "' and classcode='" + Globals.classCode(jclass.getSelectedItem().toString()) + "'";
                                            ps = con.prepareStatement(sql2);
                                            ss = ps.executeQuery();
                                            while (ss.next()) {
                                                counter2++;
                                                value = (counter2 * 100) / counter;
                                                bar.setValue(value);
                                                String adm = ss.getString("Admnumber");

                                                String sql = " select * from subjects";
                                                ps = con.prepareStatement(sql);
                                                ResultSet rx = ps.executeQuery();
                                                while (rx.next()) {

                                                    int entrycounter = 0;
                                                    String subcode = rx.getString("Subjectcode");
                                                    boolean openerchecker = false, midtermchecker = false, endtermchercker = false;
                                                    int cummulativeWeight = 0;
                                                    double score = 0, points = 0, openerscore = 0, midtermscore = 0, endtermscore = 0;
                                                    String grade = "", streamcode = "", classCode = "", techcom = "", exname = "";
                                                    String sql3 = "Select * from  markstable where  termcode='" + Globals.termcode(jterm.getSelectedItem().toString()) + "' and academicyear='" + jacademicyear.getSelectedItem() + "' and classcode='" + Globals.classCode(jclass.getSelectedItem().toString()) + "' and admnumber='" + adm + "' and subjectcode='" + subcode + "' and examName !='" + "End Term" + "'";
                                                    ps = con.prepareStatement(sql3);
                                                    ResultSet rr = ps.executeQuery();
                                                    while (rr.next()) {
                                                        String ex = rr.getString("Examcode");

                                                        if (rr.getString("Examname").equalsIgnoreCase("Opener")) {
                                                            openerscore = rr.getInt("convertedscore");
                                                            openerchecker = true;
                                                            cummulativeWeight += Globals.examWeightChecker(ex);
                                                            //  System.err.println("Adm:"+adm+" cummulative weight  in Opener"+Globals.subjectName(subcode)+"  "+cummulativeWeight );

                                                            System.err.println("Adm:" + adm + " Opener Score In " + Globals.subjectName(subcode) + "  " + openerscore);
                                                            score += rr.getInt("convertedscore");
                                                        } else if (rr.getString("Examname").equalsIgnoreCase("Mid Term")) {
                                                            cummulativeWeight += Globals.examWeightChecker(ex);
                                                            midtermscore = rr.getInt("convertedscore");
                                                            score += rr.getInt("convertedscore");
                                                            midtermchecker = true;
                                                            //  System.err.println("Adm:"+adm+" cummulative weight in Midterm "+Globals.subjectName(subcode)+"  "+cummulativeWeight );

                                                            System.err.println("Adm:" + adm + " midterm Score In " + Globals.subjectName(subcode) + "  " + midtermscore);
                                                        }


                                                        streamcode = rr.getString("Streamcode");
                                                        classCode = rr.getString("Classcode");


                                                        techcom = rr.getString("class_teacher_initials");
                                                        entrycounter++;
                                                    }
                                                    String sqla = "Select * from  markstable where  termcode='" + Globals.termcode(previousTerm) + "' and academicyear='" + previousYear + "' and classcode='" + Globals.classCode(previuosClass) + "' and admnumber='" + adm + "' and subjectcode='" + subcode + "' and examName ='" + "End Term" + "'";
                                                    ps = con.prepareStatement(sqla);
                                                    ResultSet RS = ps.executeQuery();
                                                    if (RS.next()) {
                                                        cummulativeWeight += Globals.examWeightChecker(previousExamCode);
                                                        endtermscore = RS.getInt("convertedscore");
                                                        score += RS.getInt("convertedscore");
                                                        endtermchercker = true;
                                                        // System.err.println("Adm:"+adm+" cummulative weight In Endterm "+Globals.subjectName(subcode)+"  "+cummulativeWeight );

                                                        System.err.println("Adm:" + adm + " Endterm Score In " + Globals.subjectName(subcode) + "  " + endtermscore);


                                                    }

                                                    if (cummulativeWeight == 0) {
                                                        cummulativeWeight = 100;
                                                    }
                                                    score = (score * 100) / cummulativeWeight;
                                                    System.err.println("Adm:" + adm + " cummulative Score In " + Globals.subjectName(subcode) + "  " + score);
                                                    //  System.err.println("Adm:"+adm+"Total cummulative weight "+Globals.subjectName(subcode)+"  "+cummulativeWeight );

                                                    String sql5 = "Select grade,end_at,start_from from subjectgrading where classcode='" + classCode + "' and subjectcode='" + subcode + "' and '" + nf.format(score) + "'>=start_from and '" + nf.format(score) + "'<=end_at  group by sortcode";
                                                    ps = con.prepareStatement(sql5);
                                                    rs = ps.executeQuery();
                                                    if (rs.next()) {

                                                        grade = rs.getString("grade");

                                                        String qq = "Select points from points_for_each_grade where grade='" + rs.getString("grade") + "'";
                                                        ps = con.prepareStatement(qq);
                                                        rs = ps.executeQuery();
                                                        if (rs.next()) {
                                                            points = rs.getInt("points");
                                                        }
                                                    }

                                                    String sql4 = "Insert into markstable(admnumber,subjectcode,classcode,streamcode,termcode,academicyear,examname,examcode,examscore,examoutof,convertedscore,convertedscoreoutof,exampercentage,examgrade,exampoints,class_teacher_initials)"
                                                            + " values('" + adm + "','" + subcode + "','" + classCode + "','" + streamcode + "','" + Globals.termcode(jterm.getSelectedItem().toString()) + "','" + jacademicyear.getSelectedItem().toString() + "','" + "TOTAL" + "','" + excode + "','" + score + "','" + "100" + "','" + "100" + "','" + "100" + "','" + score + "','" + grade + "','" + points + "','" + techcom + "')";
                                                    ps = con.prepareStatement(sql4);
                                                    ps.execute();

                                                }


                                            }


                                        } else {

                                            ps = con.prepareStatement("Select * from examcombinationmodes where examcode='" + excode + "'");
                                            rs = ps.executeQuery();
                                            if (rs.next()) {
                                                ps = con.prepareStatement("Update examcombinationmodes set combinemode='" + "NORMAL" + "' where examcode='" + excode + "'");
                                                ps.execute();
                                            } else {
                                                ps = con.prepareStatement("Insert into examcombinationmodes values('" + excode + "','" + "NORMAL" + "')");
                                                ps.execute();
                                            }


                                            int counter = 0;
                                            String sqll = "Select distinct admnumber from  markstable where  termcode='" + Globals.termcode(jterm.getSelectedItem().toString()) + "' and academicyear='" + jacademicyear.getSelectedItem() + "' and classcode='" + Globals.classCode(jclass.getSelectedItem().toString()) + "'";
                                            ps = con.prepareStatement(sqll);
                                            ResultSet ss = ps.executeQuery();
                                            while (ss.next()) {

                                                counter++;
                                            }
                                            if (counter == 0) {
                                                JOptionPane.showMessageDialog(CurrentFrame.mainFrame(), "No Marks Were Found For This Class In The Selected Term And Academic Year");
                                            }
                                            String sqll2 = "Delete from  markstable where  examcode='" + excode + "'";
                                            ps = con.prepareStatement(sqll2);
                                            ps.execute();


                                            int value = 0, counter2 = 0;

                                            String sql2 = "Select distinct admnumber from  markstable where  termcode='" + Globals.termcode(jterm.getSelectedItem().toString()) + "' and academicyear='" + jacademicyear.getSelectedItem() + "' and classcode='" + Globals.classCode(jclass.getSelectedItem().toString()) + "'";
                                            ps = con.prepareStatement(sql2);
                                            ss = ps.executeQuery();
                                            while (ss.next()) {
                                                counter2++;
                                                value = (counter2 * 100) / counter;
                                                bar.setValue(value);
                                                String adm = ss.getString("Admnumber");

                                                String sql = " select * from subjects";
                                                ps = con.prepareStatement(sql);
                                                ResultSet rx = ps.executeQuery();
                                                while (rx.next()) {

                                                    int entrycounter = 0;
                                                    String subcode = rx.getString("Subjectcode");
                                                    boolean openerchecker = false, midtermchecker = false, endtermchercker = false;
                                                    int cummulativeWeight = 0;
                                                    double score = 0, points = 0, openerscore = 0, midtermscore = 0, endtermscore = 0;
                                                    String grade = "", streamcode = "", classCode = "", techcom = "", exname = "";
                                                    String sql3 = "Select * from  markstable where  termcode='" + Globals.termcode(jterm.getSelectedItem().toString()) + "' and academicyear='" + jacademicyear.getSelectedItem() + "' and classcode='" + Globals.classCode(jclass.getSelectedItem().toString()) + "' and admnumber='" + adm + "' and subjectcode='" + subcode + "'";
                                                    ps = con.prepareStatement(sql3);
                                                    ResultSet rr = ps.executeQuery();
                                                    while (rr.next()) {

                                                        System.err.println("Adm:" + adm);
                                                        System.err.println("sub code:" + subcode);
                                                        String ex = rr.getString("Examcode");

                                                        if (rr.getString("Examname").equalsIgnoreCase("Opener")) {
                                                            openerscore = rr.getInt("convertedscore");
                                                            System.err.println("Opener Score:" + openerscore);
                                                            openerchecker = true;
                                                            cummulativeWeight += Globals.examWeightChecker(ex);
                                                            score += rr.getInt("convertedscore");
                                                        } else if (rr.getString("Examname").equalsIgnoreCase("Mid Term")) {
                                                            cummulativeWeight += Globals.examWeightChecker(ex);
                                                            midtermscore = rr.getInt("convertedscore");
                                                            score += rr.getInt("convertedscore");
                                                            midtermchecker = true;
                                                            System.err.println("Midterm Score:" + midtermchecker);
                                                        } else if (rr.getString("Examname").equalsIgnoreCase("End Term")) {
                                                            cummulativeWeight += Globals.examWeightChecker(ex);
                                                            endtermscore = rr.getInt("convertedscore");
                                                            score += rr.getInt("convertedscore");
                                                            endtermchercker = true;
                                                            System.err.println("Endterm Score:" + endtermscore);
                                                        }
                                                        streamcode = rr.getString("Streamcode");
                                                        classCode = rr.getString("Classcode");


                                                        techcom = rr.getString("class_teacher_initials");
                                                        entrycounter++;
                                                    }

                                                    if (cummulativeWeight == 0) {
                                                        cummulativeWeight = 100;
                                                    }
                                                    score = (score * 100) / cummulativeWeight;


                                                    String sql5 = "Select grade,end_at,start_from from subjectgrading where classcode='" + classCode + "' and subjectcode='" + subcode + "' and '" + nf.format(score) + "'>=start_from and '" + nf.format(score) + "'<=end_at  group by sortcode";
                                                    ps = con.prepareStatement(sql5);
                                                    rs = ps.executeQuery();
                                                    if (rs.next()) {

                                                        grade = rs.getString("grade");

                                                        String qq = "Select points from points_for_each_grade where grade='" + rs.getString("grade") + "'";
                                                        ps = con.prepareStatement(qq);
                                                        rs = ps.executeQuery();
                                                        if (rs.next()) {
                                                            points = rs.getInt("points");
                                                        }
                                                    }

                                                    String sql4 = "Insert into markstable(admnumber,subjectcode,classcode,streamcode,termcode,academicyear,examname,examcode,examscore,examoutof,convertedscore,convertedscoreoutof,exampercentage,examgrade,exampoints,class_teacher_initials) values('" + adm + "','" + subcode + "','" + classCode + "','" + streamcode + "','" + Globals.termcode(jterm.getSelectedItem().toString()) + "','" + jacademicyear.getSelectedItem().toString() + "','" + "TOTAL" + "','" + excode + "','" + score + "','" + "100" + "','" + "100" + "','" + "100" + "','" + score + "','" + grade + "','" + points + "','" + techcom + "')";
                                                    ps = con.prepareStatement(sql4);
                                                    ps.execute();

                                                }


                                            }

                                        }


                                        dia.dispose();
                                        JOptionPane.showMessageDialog(CurrentFrame.mainFrame(), "Exams Combined Successfully,\n Open The Marks Analysis Pane And Analyse Total Exam For Report Form Generation");
                                    } catch (Exception sq) {
                                        dia.dispose();
                                        sq.printStackTrace();
                                    }

                                }

                            }.start();


                        }
                    }
                }

            }


        } else if (obj == cancel) {
            CurrentFrame.currentWindow();
            this.dispose();
        } else if (obj == jterm) {
            if (jterm.getSelectedIndex() > 0) {
                tm = jterm.getSelectedItem().toString().toString();
            }
        } else if (obj == jclass) {
            if (jclass.getSelectedIndex() > 0) {
                cl = jclass.getSelectedItem().toString();
            }
        } else if (obj == jacademicyear) {
            if (jacademicyear.getSelectedIndex() > 0) {
                yr = jacademicyear.getSelectedItem().toString();
            }
        }


    }

    public static void main(String[] args) {
        new CombineExams();
    }


}
