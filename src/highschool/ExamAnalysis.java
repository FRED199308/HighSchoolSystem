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
import java.text.NumberFormat;
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
public class ExamAnalysis extends JFrame implements ActionListener {

    private Connection con = DbConnection.connectDb();
    private int width = 780;
    private int height = 500;
    private PreparedStatement ps;
    private ResultSet rs;
    private FredLabel adm = new FredLabel("admission Number");
    private FredTextField jadm = new FredTextField();
    private FredLabel name = new FredLabel("Student Name");
    private FredLabel class1 = new FredLabel("Class");
    private FredLabel examname = new FredLabel("Exam Name");
    private FredLabel examcode = new FredLabel("Exam Code");
    private FredLabel term = new FredLabel("Term");
    private FredLabel stream = new FredLabel("Stream");
    private FredCombo jexamname = new FredCombo("Exam Name");
    private FredCombo jexamcode = new FredCombo("Select Exam code");
    private FredCombo jterm = new FredCombo("Select Term");
    private FredLabel academicyear = new FredLabel(" Academic Year");
    private FredCombo jacademicyear = new FredCombo("Select Academic Year");
    private FredCombo jclass = new FredCombo("Select Class");
    private FredCombo jstream = new FredCombo("Select Stream");

    private FredCheckBox option1 = new FredCheckBox("Analyse With Seven Subjects");
    private FredCheckBox option2 = new FredCheckBox("Analyse with All Subjects");
    private FredCheckBox option3 = new FredCheckBox("Allocate position using points");
    private FredCheckBox option4 = new FredCheckBox("Allocate position Using Marks");
    private FredCheckBox option5 = new FredCheckBox("Allocate Meangrade Using Avg Marks");
    private FredCheckBox option6 = new FredCheckBox("Allocate Meangrade Using Avg points");

    JWindow dia = new JWindow();
    private FredButton analyse = new FredButton("Analyse Exam");
    private FredButton show = new FredButton("Show Entered Marks");
    private FredButton report = new FredButton("Generate Report Forms");
    private ButtonGroup group1 = new ButtonGroup();
    private ButtonGroup group2 = new ButtonGroup();
    private ButtonGroup group3 = new ButtonGroup();
    int rows = 0;
    double value = 0.0000;
    int counter = 0;
    JProgressBar bar;
    String tm, cl, yr, ex;
    NumberFormat nf = NumberFormat.getNumberInstance();
    NumberFormat nf2 = NumberFormat.getNumberInstance();
    String analysisMode = "";

    public ExamAnalysis() {
        bar = new JProgressBar();
        setSize(width, height);
        setTitle("Exam Analysis Panel");
        ((JComponent) getContentPane()).setBorder(
                BorderFactory.createMatteBorder(5, 5, 5, 5, Color.MAGENTA));

        this.setIconImage(FrameProperties.icon());

        nf.setMaximumFractionDigits(2);
        nf.setMinimumFractionDigits(2);
        nf2.setMaximumFractionDigits(0);
        nf2.setMinimumFractionDigits(0);
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
        setLayout(null);
        setResizable(false);
        setLocationRelativeTo(null);
        group1.add(option1);
        group1.add(option2);
        group2.add(option3);
        group2.add(option4);
        group3.add(option5);
        group3.add(option6);
        option2.setSelected(true);
        option4.setSelected(true);
        option5.setSelected(true);
        adm.setBounds(30, 30, 150, 30);
        add(adm);
        jadm.setBounds(200, 30, 250, 30);
        add(jadm);
        name.setBounds(500, 30, 150, 30);
        add(name);
        class1.setBounds(30, 100, 150, 30);
        add(class1);
        jclass.setBounds(150, 100, 200, 30);
        add(jclass);
        stream.setBounds(400, 100, 100, 30);
        add(stream);
        jstream.setBounds(500, 100, 200, 30);
        add(jstream);

        term.setBounds(30, 170, 150, 30);
        add(term);
        jterm.setBounds(150, 170, 200, 30);
        add(jterm);
        academicyear.setBounds(400, 170, 100, 30);
        add(academicyear);
        jacademicyear.setBounds(500, 170, 200, 30);
        add(jacademicyear);

        examname.setBounds(30, 250, 150, 30);
        add(examname);
        jexamname.setBounds(150, 250, 200, 30);
        add(jexamname);
        examcode.setBounds(400, 250, 100, 30);
        add(examcode);
        jexamcode.setBounds(500, 250, 200, 30);
        add(jexamcode);

        option1.setBounds(50, 300, 200, 30);
        add(option1);
        option2.setBounds(50, 340, 200, 30);
        add(option2);

        option3.setBounds(280, 300, 200, 30);
        add(option3);
        option4.setBounds(280, 340, 200, 30);
        add(option4);
        option5.setBounds(500, 300, 240, 30);
        add(option5);
        option6.setBounds(500, 340, 240, 30);
        add(option6);

        analyse.setBounds(80, 390, 150, 50);
        add(analyse);
        report.setBounds(270, 390, 200, 50);
        add(report);
        show.setBounds(530, 390, 200, 50);
        add(show);
        try {

            String sql2 = "Select * from streams";

            ps = con.prepareStatement(sql2);
            rs = ps.executeQuery();
            while (rs.next()) {

                jstream.addItem(rs.getString("StreamName"));
            }
            String sql = "Select * from classes where precision1<5 order by precision1 asc";

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

            for (int k = 2015; k <= Globals.academicYear(); ++k) {
                jacademicyear.addItem(k);
            }
            String sqls = "Select examname from exams group by examname";
            ps = con.prepareStatement(sqls);
            rs = ps.executeQuery();
            while (rs.next()) {
                jexamname.addItem(rs.getString("ExamName"));
            }
            String sqls1 = "Select examcode from exams ";
            ps = con.prepareStatement(sqls1);
            rs = ps.executeQuery();
            while (rs.next()) {
                jexamcode.addItem(rs.getString("Examcode"));
            }

        } catch (Exception sq) {
            sq.printStackTrace();
            JOptionPane.showMessageDialog(null, sq.getMessage());
        }
        jacademicyear.setSelectedItem(Globals.academicYear());
        jterm.setSelectedItem(Globals.currentTermName());
        jterm.addActionListener(this);
        jclass.addActionListener(this);
        jacademicyear.addActionListener(this);
        jexamcode.addActionListener(this);
        jexamname.addActionListener(this);
        jterm.addActionListener(this);
        analyse.addActionListener(this);
        report.addActionListener(this);
        show.addActionListener(this);
        jexamcode.setEnabled(false);
        if (jstream.getItemCount() < 3) {
            jstream.setSelectedIndex(1);
        }
        jacademicyear.setSelectedItem(Globals.academicYear());
        jterm.setSelectedItem(Globals.currentTermName());
        setVisible(true);
        jadm.setFont(new Font("serif", Font.BOLD, 18));

        jadm.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent key) {

                try {
                    String sql = "Select firstname,middlename,lastname from admission where admissionnumber='" + jadm.getText() + "'";
                    ps = con.prepareStatement(sql);
                    rs = ps.executeQuery();
                    if (rs.next()) {

                        jadm.setForeground(Color.BLACK);
                        name.setText(rs.getString("FirstName") + "    " + rs.getString("MiddleName") + "    " + rs.getString("LastName"));
                    } else {
                        name.setText("");
                        jadm.setForeground(Color.red);
                    }


                } catch (Exception sq) {
                    sq.printStackTrace();
                    JOptionPane.showMessageDialog(null, sq.getMessage());
                }

            }
        });

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object obj = e.getSource();
        if (obj == analyse) {
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
                            JOptionPane.showMessageDialog(this, "Exam Not Selected Or Selection Order Not Adhered\nKindly Repeat The Selection Process For The System To Autocupture Exam Code");
                        } else {
                            int results = 0;
                            try {

                                String sql2 = "Select count(*) from  markstable where examcode='" + jexamcode.getSelectedItem() + "' and termcode='" + Globals.termcode(jterm.getSelectedItem().toString()) + "' and academicyear='" + jacademicyear.getSelectedItem() + "' and classcode='" + Globals.classCode(jclass.getSelectedItem().toString()) + "'";
                                ps = con.prepareStatement(sql2);
                                rs = ps.executeQuery();
                                if (rs.next()) {
                                    results = rs.getInt("count(*)");
                                }
                            } catch (Exception sq) {
                                sq.printStackTrace();
                                JOptionPane.showMessageDialog(null, sq.getMessage());
                            }

                            if (results < 1) {
                                JOptionPane.showMessageDialog(this, "No Marks Are Available For Analysis For The Selected Exam");
                            } else {

                                new Thread() {

                                    public void run() {
                                        String examcd = jexamcode.getSelectedItem().toString();
                                        String classcd = jclass.getSelectedItem().toString();
                                        String term = jterm.getSelectedItem().toString();
                                        String year = jacademicyear.getSelectedItem().toString();

                                        bar.setIndeterminate(true);

                                        dia.setSize(300, 60);

                                        bar.setBorder(new TitledBorder("Analysing Exam"));
                                        bar.setStringPainted(true);
                                        dia.setLocationRelativeTo(CurrentFrame.secondFrame());
                                        dia.setIconImage(FrameProperties.icon());
                                        dia.add(bar);
                                        dia.setVisible(true);
                                        try {

                                            String sql22 = "Select count(*) from  markstable where examcode='" + jexamcode.getSelectedItem() + "' and termcode='" + Globals.termcode(jterm.getSelectedItem().toString()) + "' and academicyear='" + jacademicyear.getSelectedItem() + "' and classcode='" + Globals.classCode(jclass.getSelectedItem().toString()) + "'";
                                            ps = con.prepareStatement(sql22);
                                            rs = ps.executeQuery();
                                            if (rs.next()) {

                                                rows = rs.getInt("count(*)");
                                            }

                                            bar.setMinimum(0);
                                            bar.setMaximum(100);

                                            String sqlstart = "select Admnumber from examanalysistable where classname='" + jclass.getSelectedItem() + "' and examcode='" + jexamcode.getSelectedItem() + "' and term='" + jterm.getSelectedItem() + "' and academicyear='" + jacademicyear.getSelectedItem() + "'";
                                            ps = con.prepareStatement(sqlstart);
                                            rs = ps.executeQuery();
                                            if (rs.next()) {

                                                int opt = JOptionPane.showConfirmDialog(CurrentFrame.mainFrame(), "This Exam Had Previousily Been Anlysed\n Do You Want To Reanalyse This Exam??", "Confirm Reanalyse", JOptionPane.YES_NO_OPTION);
                                                if (opt == JOptionPane.NO_OPTION) {

                                                } else {
                                                    String state = "";
                                                    String sq = "Select * from ExamAnalysisLock where examcode='" + jexamcode.getSelectedItem() + "'";
                                                    ps = con.prepareStatement(sq);
                                                    ResultSet result = ps.executeQuery();
                                                    if (result.next()) {
                                                        state = result.getString("Lock");

                                                    } else {
                                                        String SQ = "insert into examanalysislock values('" + jexamcode.getSelectedItem() + "','" + "ON" + "')";
                                                        ps = con.prepareStatement(SQ);
                                                        ps.execute();
                                                    }
//                                                    if (state.equalsIgnoreCase("ON")) {
//                                                        dia.dispose();
//                                                        JOptionPane.showMessageDialog(null, "Another Computer On The Network Is Currently Analysing The Selected Exam\n Please Wait For  Analysis Completion By The Computer For Results Consistency");
//                                                    } else {lll

                                                    if (option1.isSelected()) {
                                                        analysisMode = "By Seven";
                                                    } else {
                                                        analysisMode = "All";
                                                    }


                                                    String analysisObject;
                                                    if (option5.isSelected()) {
                                                        analysisObject = "Average Marks";
                                                    } else {
                                                        analysisObject = "Average Points";
                                                    }
                                                    String sq1 = "Select * from examanalysismodes where examcode='" + jexamcode.getSelectedItem() + "'";
                                                    ps = con.prepareStatement(sq1);
                                                    result = ps.executeQuery();
                                                    if (result.next()) {
                                                        ps = con.prepareStatement("Update examanalysismodes set AnalysisMode='" + analysisMode + "', analysisobject='" + analysisObject + "' where examcode='" + jexamcode.getSelectedItem() + "'");
                                                        ps.execute();

                                                    } else {
                                                        String SQ = "insert into examanalysismodes values('" + jexamcode.getSelectedItem() + "','" + analysisMode + "','" + analysisObject + "')";
                                                        ps = con.prepareStatement(SQ);
                                                        ps.execute();
                                                    }

                                                    dia.setAlwaysOnTop(true);

                                                    String sql = "Delete from examanalysistable where classname='" + jclass.getSelectedItem() + "' and examcode='" + jexamcode.getSelectedItem() + "' and term='" + jterm.getSelectedItem() + "' and academicyear='" + jacademicyear.getSelectedItem() + "'";
                                                    ps = con.prepareStatement(sql);
                                                    ps.execute();
                                                    int i = 0;
                                                    bar.setIndeterminate(false);
                                                    String sql2 = "Select * from  markstable where examcode='" + jexamcode.getSelectedItem() + "' and termcode='" + Globals.termcode(jterm.getSelectedItem().toString()) + "' and academicyear='" + jacademicyear.getSelectedItem() + "' and classcode='" + Globals.classCode(jclass.getSelectedItem().toString()) + "'";
                                                    ps = con.prepareStatement(sql2);
                                                    rs = ps.executeQuery();
                                                    while (rs.next()) {
                                                        value += (40.0000) / rows;

                                                        bar.setValue((int) value);

                                                        String sql3 = "Insert into examanalysistable (admnumber,fullname,classname,stream,academicyear,examcode,examname,term,subjectcode,subjectname,examscore,examoutof,convertedscore,convertedscoreoutof,exampercentage,subjectexamgrade,Sujectexampoints,teacherinitials) values("
                                                                + "'" + rs.getString("Admnumber") + "','" + Globals.fullName(rs.getString("AdmNumber")) + "','" + Globals.className(rs.getString("Classcode")) + "','" + Globals.streamName(rs.getString("streamcode")) + "','" + rs.getString("AcademicYear") + "'"
                                                                + ",'" + rs.getString("Examcode") + "','" + rs.getString("Examname") + "','" + Globals.termname(rs.getString("Termcode")) + "','" + rs.getString("Subjectcode") + "','" + Globals.subjectName(rs.getString("subjectcode")) + "','" + rs.getString("Examscore") + "','" + rs.getString("Examoutof") + "','" + rs.getString("Convertedscore") + "','" + rs.getString("convertedscoreoutof") + "'"
                                                                + ",'" + rs.getString("Exampercentage") + "','" + rs.getString("Examgrade") + "','" + rs.getString("Exampoints") + "','" + rs.getString("class_teacher_initials") + "')";
                                                        ps = con.prepareStatement(sql3);
                                                        ps.execute();

                                                    }
                                                    subjectpositionAssigner();
                                                    meanGradeAssigner();
                                                    positionAssigner();
                                                    dia.dispose();
                                                    String sqq = "Delete from ExamAnalysisLock where examcode='" + jexamcode.getSelectedItem() + "'";
                                                    ps = con.prepareStatement(sqq);
                                                    ps.execute();
                                                    JOptionPane.showMessageDialog(CurrentFrame.mainFrame(), "Exam Analysis Completed");

                                                }

                                            } else {

                                                String analysisObject;
                                                if (option5.isSelected()) {
                                                    analysisObject = "Average Marks";
                                                } else {
                                                    analysisObject = "Average Points";
                                                }


                                                dia.setAlwaysOnTop(true);
                                                String state = "";
                                                String sq = "Select * from ExamAnalysisLock where examcode='" + jexamcode.getSelectedItem() + "'";
                                                ps = con.prepareStatement(sq);
                                                ResultSet result = ps.executeQuery();
                                                if (result.next()) {
                                                    state = result.getString("Lock");

                                                } else {
                                                    String SQ = "insert into examanalysislock values('" + jexamcode.getSelectedItem() + "','" + "ON" + "')";
                                                    ps = con.prepareStatement(SQ);
                                                    ps.execute();
                                                }
//                                                if (state.equalsIgnoreCase("ON")) {
//                                                    dia.dispose();
//                                                    JOptionPane.showMessageDialog(null, "Another Computer On The Network Is Currently Analysing The Exam");
//                                                } else {...
                                                if (option1.isSelected()) {
                                                    analysisMode = "By Seven";
                                                } else {
                                                    analysisMode = "All";
                                                }
                                                String sq1 = "Select * from examanalysismodes where examcode='" + jexamcode.getSelectedItem() + "'";
                                                ps = con.prepareStatement(sq1);
                                                result = ps.executeQuery();
                                                if (result.next()) {
                                                    ps = con.prepareStatement("Update examanalysismodes set AnalysisMode='" + analysisMode + "',analysisObject='" + analysisObject + "' where examcode='" + jexamcode.getSelectedItem() + "'");
                                                    ps.execute();

                                                } else {
                                                    String SQ = "insert into examanalysismodes values('" + jexamcode.getSelectedItem() + "','" + analysisMode + "','" + analysisObject + "')";
                                                    ps = con.prepareStatement(SQ);
                                                    ps.execute();
                                                }
                                                String sql = "Delete from examanalysistable where classname='" + jclass.getSelectedItem() + "' and examcode='" + jexamcode.getSelectedItem() + "' and term='" + jterm.getSelectedItem() + "' and academicyear='" + jacademicyear.getSelectedItem() + "'";
                                                ps = con.prepareStatement(sql);
                                                ps.execute();
                                                int i = 0;
                                                bar.setIndeterminate(false);
                                                String sql2 = "Select * from  markstable where examcode='" + jexamcode.getSelectedItem() + "' and termcode='" + Globals.termcode(jterm.getSelectedItem().toString()) + "' and academicyear='" + jacademicyear.getSelectedItem() + "' and classcode='" + Globals.classCode(jclass.getSelectedItem().toString()) + "'";
                                                ps = con.prepareStatement(sql2);
                                                rs = ps.executeQuery();
                                                while (rs.next()) {
                                                    value += (40.0000) / rows;
                                                    bar.setValue((int) value);

                                                    String sql3 = "Insert into examanalysistable (admnumber,fullname,classname,stream,academicyear,examcode,examname,term,subjectcode,subjectname,examscore,examoutof,convertedscore,convertedscoreoutof,exampercentage,subjectexamgrade,Sujectexampoints,teacherinitials) values("
                                                            + "'" + rs.getString("Admnumber") + "','" + Globals.fullName(rs.getString("AdmNumber")) + "','" + Globals.className(rs.getString("Classcode")) + "','" + Globals.streamName(rs.getString("streamcode")) + "','" + rs.getString("AcademicYear") + "'"
                                                            + ",'" + rs.getString("Examcode") + "','" + rs.getString("Examname") + "','" + Globals.termname(rs.getString("Termcode")) + "','" + rs.getString("Subjectcode") + "','" + Globals.subjectName(rs.getString("subjectcode")) + "','" + rs.getString("Examscore") + "','" + rs.getString("Examoutof") + "','" + rs.getString("Convertedscore") + "','" + rs.getString("convertedscoreoutof") + "'"
                                                            + ",'" + rs.getString("Exampercentage") + "','" + rs.getString("Examgrade") + "','" + rs.getString("Exampoints") + "','" + rs.getString("class_teacher_initials") + "')";
                                                    ps = con.prepareStatement(sql3);
                                                    ps.execute();

                                                }
                                                subjectpositionAssigner();
                                                meanGradeAssigner();
                                                positionAssigner();
                                                dia.dispose();
                                                String sqq = "Delete from ExamAnalysislock where examcode='" + jexamcode.getSelectedItem() + "'";
                                                ps = con.prepareStatement(sqq);
                                                ps.execute();
                                                JOptionPane.showMessageDialog(CurrentFrame.mainFrame(), "Exam Analysis Completed");

                                            }

                                        } catch (Exception sq) {
                                            dia.dispose();
                                            sq.printStackTrace();
                                            JOptionPane.showMessageDialog(null, sq.getMessage());
                                        }
                                    }

                                }.start();

                            }

                        }
                    }
                }

            }
        } else if (obj == show) {
            if (jclass.getSelectedIndex() == 0) {
                JOptionPane.showMessageDialog(null, "Kindly Select Class");
            } else {
                if (jterm.getSelectedIndex() == 0) {
                    JOptionPane.showMessageDialog(null, "Kindly Select Term");
                } else {
                    if (jstream.getSelectedIndex() == 0) {
                        JOptionPane.showMessageDialog(null, "Kindly Select Stream");
                    } else {
                        if (jacademicyear.getSelectedIndex() == 0) {
                            JOptionPane.showMessageDialog(null, "Kindly Select Academic Year");
                        } else {
                            if (jexamcode.getSelectedIndex() == 0) {
                                JOptionPane.showMessageDialog(this, "Exam Not Selected Or Selection Order Not Adhered\nKindly Repet The Selection Process For The System To Autocupture Exam Code");
                            } else {
                                ReportGenerator.missingResultsOverall(jclass.getSelectedItem().toString(), jacademicyear.getSelectedItem().toString(), jstream.getSelectedItem().toString(), jexamcode.getSelectedItem().toString(), jexamname.getSelectedItem().toString(), jterm.getSelectedItem().toString());

                            }
                        }
                    }
                }
            }
        } else if (obj == report) {
            if (jadm.getText().isEmpty()) {
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
                                JOptionPane.showMessageDialog(this, "Exam Not Selected Or Selection Order Not Adhered\nKindly Repeat The Selection Process For The System To Autocupture Exam Code");
                            } else {
                                new Thread() {
                                    @Override
                                    public void run() {
                                        JWindow dia = new JWindow();
                                        JProgressBar bar = new JProgressBar();
                                        bar.setIndeterminate(true);

                                        dia.setSize(300, 70);

                                        bar.setBorder(new TitledBorder("Generating Student Report Forms.Please wait......"));

                                        dia.setLocationRelativeTo(CurrentFrame.mainFrame());
                                        dia.setIconImage(FrameProperties.icon());
                                        dia.add(bar);
                                        dia.setVisible(true);
                                        CurrentFrame.secondFrame.setEnabled(false);
                                        ReportFormGenerator.reportForms(jexamname.getSelectedItem().toString(), jexamcode.getSelectedItem().toString(), jacademicyear.getSelectedItem().toString(), jclass.getSelectedItem().toString(), jterm.getSelectedItem().toString(), null);
                                        CurrentFrame.secondFrame.setEnabled(true);
                                        revalidate();
                                        repaint();
                                        dia.dispose();
                                    }

                                }.start();

                            }
                        }
                    }

                }

            } else {
                try {
                    String sql = "Select admissionnumber from admission where admissionnumber='" + jadm.getText() + "'";
                    ps = con.prepareStatement(sql);
                    rs = ps.executeQuery();
                    if (rs.next()) {

                        ReportFormGenerator.reportForms(jexamname.getSelectedItem().toString(), jexamcode.getSelectedItem().toString(), jacademicyear.getSelectedItem().toString(), jclass.getSelectedItem().toString(), jterm.getSelectedItem().toString(), jadm.getText());
                    } else {
                        JOptionPane.showMessageDialog(this, "Invalid admission Number Check!!");
                    }

                } catch (Exception sq) {
                    sq.printStackTrace();
                    JOptionPane.showMessageDialog(null, sq.getMessage());
                }
            }

        } else if (obj == jterm) {
            if (jterm.getSelectedIndex() > 0) {
                tm = jterm.getSelectedItem().toString().toString();
            }
        }
        if (obj == jclass) {
            if (jclass.getSelectedIndex() > 0) {
                cl = jclass.getSelectedItem().toString();
            }
        } else if (obj == jacademicyear) {
            if (jacademicyear.getSelectedIndex() > 0) {
                yr = jacademicyear.getSelectedItem().toString();
            }
        } else if (obj == jexamname) {
            jexamcode.removeActionListener(this);
            if (jexamname.getSelectedIndex() > 0) {
                ex = jexamname.getSelectedItem().toString();

                String excode = ExamCodesGenerator.generatecode(cl, yr, tm, ex).toUpperCase();
                jexamcode.setSelectedItem(excode);

            }
            jexamcode.addActionListener(this);
        } else if (obj == jexamcode) {
            jexamname.removeActionListener(this);
            if (jexamcode.getSelectedIndex() > 0) {
                try {
                    String sql = "Select Examname from exams where examcode='" + jexamcode.getSelectedItem() + "'";
                    ps = con.prepareStatement(sql);
                    rs = ps.executeQuery();
                    while (rs.next()) {
                        jexamname.setSelectedItem(rs.getString("ExamName"));
                    }

                    jexamname.addActionListener(this);
                } catch (Exception sq) {
                    sq.printStackTrace();
                    JOptionPane.showMessageDialog(null, sq.getMessage());
                }
            }
        }

    }

    public static void main(String[] args) {
        new ExamAnalysis();
    }

    public void subjectpositionAssigner() {
        try {
            rows = 0;

            String sql1 = "Select subjectcode from examanalysistable where classname='" + jclass.getSelectedItem() + "'  and examcode='" + jexamcode.getSelectedItem() + "' and academicyear='" + jacademicyear.getSelectedItem() + "' group by subjectcode";
            ps = con.prepareStatement(sql1);
            ResultSet RS = ps.executeQuery();
            while (RS.next()) {
                rows++;
            }
            counter = 1;

            String sql = "Select subjectcode  from examanalysistable where classname='" + jclass.getSelectedItem() + "'  and examcode='" + jexamcode.getSelectedItem() + "' and academicyear='" + jacademicyear.getSelectedItem() + "' group by subjectcode order by subjectcode";
            ps = con.prepareStatement(sql);
            RS = ps.executeQuery();
            while (RS.next()) {
                value += (20.0000 / rows);
                bar.setValue((int) value);
                counter++;

                String subcode = RS.getString("subjectcode");

                int headcount = 0;
                int previousscore = 0;
                int totalentries = 0;

                String sql2 = "Select count(*) from examanalysistable where examcode='" + jexamcode.getSelectedItem() + "' and classname='" + jclass.getSelectedItem() + "' and academicyear='" + jacademicyear.getSelectedItem() + "' and subjectcode='" + subcode + "' order by examscore";
                ps = con.prepareStatement(sql2);
                rs = ps.executeQuery();
                if (rs.next()) {
                    totalentries = rs.getInt("count(*)");
                }
                int tiechck = 0;

                String sql3 = "Select examscore,admnumber,subjectexamgrade from examanalysistable where examcode='" + jexamcode.getSelectedItem() + "' and academicyear='" + jacademicyear.getSelectedItem() + "' and subjectcode='" + subcode + "' order by examscore desc";
                ps = con.prepareStatement(sql3);
                rs = ps.executeQuery();
                while (rs.next()) {
                    String adm = rs.getString("admNumber");
                    String grade = rs.getString("subjectexamgrade");

                    int examresult = rs.getInt("Examscore");
                    if (examresult == previousscore) {
                        tiechck++;
                    } else {
                        headcount++;
                        headcount += tiechck;
                        tiechck = 0;
                    }
                    String comment = "";
                    String querry = "Select comment from subjectcomments where subjectcode='" + subcode + "' and grade='" + grade + "'";
                    ps = con.prepareStatement(querry);
                    ResultSet rx = ps.executeQuery();
                    if (rx.next()) {
                        comment = rx.getString("comment");
                    }
                    String sql4 = "Update examanalysistable  set subjectposition='" + headcount + "',subjectpositionoutof='" + totalentries + "',teacherscomment='" + comment + "' where examcode='" + jexamcode.getSelectedItem() + "' and academicyear='" + jacademicyear.getSelectedItem() + "' and classname='" + jclass.getSelectedItem() + "' and subjectcode='" + subcode + "' and admnumber='" + adm + "' ";
                    ps = con.prepareStatement(sql4);
                    ps.execute();

                    previousscore = rs.getInt("Examscore");
                }
            }
        } catch (Exception sq) {
            dia.dispose();
            sq.printStackTrace();
            JOptionPane.showMessageDialog(null, sq.getMessage());
        }

    }

    public void meanGradeAssigner() {
        try {
            rows = 0;
            String analysisMode = "All";
            String sqlstart1 = "select count(*) from examanalysistable where classname='" + jclass.getSelectedItem() + "' and examcode='" + jexamcode.getSelectedItem() + "' and term='" + jterm.getSelectedItem() + "' and academicyear='" + jacademicyear.getSelectedItem() + "' group by admNumber";
            ps = con.prepareStatement(sqlstart1);
            rs = ps.executeQuery();
            while (rs.next()) {
                rows++;
            }

            counter = 1;
        } catch (Exception sq) {
            sq.printStackTrace();
            JOptionPane.showMessageDialog(null, sq.getMessage());
        }
        if (option1.isSelected()) {

            try {

                String sqlstart = "select admnumber from examanalysistable where classname='" + jclass.getSelectedItem() + "' and examcode='" + jexamcode.getSelectedItem() + "' and term='" + jterm.getSelectedItem() + "' and academicyear='" + jacademicyear.getSelectedItem() + "' group by admNumber";
                ps = con.prepareStatement(sqlstart);
                rs = ps.executeQuery();
                while (rs.next()) {
                    int codepointer = 0;
                    String codes[] = new String[5];
                    value += 20.0000 / rows;
                    bar.setValue((int) value);
                    counter++;
                    String adm = rs.getString("admnumber");
                    int subtp1 = 0, subtp2 = 0, subtp3 = 0, subtp4 = 0;
                    int subtm1 = 0, subtm2 = 0, subtm3 = 0, subtm4 = 0;
                    double totalpoint = 0;
                    double totalmarks = 0;
                    int totalsubject = 0;
                    if (Globals.gradable(adm, jexamcode.getSelectedItem().toString(), jacademicyear.getSelectedItem().toString())) {


                        String sql = "select sum(exampercentage),sum(Sujectexampoints) from examanalysistable,subjects where classname='" + jclass.getSelectedItem() + "' and examcode='" + jexamcode.getSelectedItem() + "' and term='" + jterm.getSelectedItem() + "' and academicyear='" + jacademicyear.getSelectedItem() + "' and admnumber='" + adm + "' and subjects.subjectcode=examanalysistable.subjectcode and category='" + "languages" + "' ";
                        ps = con.prepareStatement(sql);
                        ResultSet rx = ps.executeQuery();
                        if (rx.next()) {
                            subtp1 = rx.getInt("sum(Sujectexampoints)");
                            subtm1 = rx.getInt("sum(exampercentage)");

                        }
                        codes[codepointer] = "101";
                        codepointer++;
                        codes[codepointer] = "102";
                        int subcounter = 1;
                        String sqly = "select exampercentage,Sujectexampoints,examanalysistable.subjectcode from examanalysistable,subjects where classname='" + jclass.getSelectedItem() + "' and examcode='" + jexamcode.getSelectedItem() + "' and term='" + jterm.getSelectedItem() + "' and academicyear='" + jacademicyear.getSelectedItem() + "' and admnumber='" + adm + "' and subjects.subjectcode=examanalysistable.subjectcode and category='" + "sciences" + "' order by exampercentage desc";
                        ps = con.prepareStatement(sqly);
                        rx = ps.executeQuery();
                        while (rx.next()) {
                            if (subcounter > 2) {
                                break;
                            }
                            codepointer++;
                            codes[codepointer] = rx.getString("SubjectCode");

                            subtp2 += rx.getInt("Sujectexampoints");
                            subtm2 += rx.getInt("exampercentage");

                            subcounter++;
                        }
                        String sqlxx = "select exampercentage,Sujectexampoints,examanalysistable.subjectcode from examanalysistable,subjects where classname='" + jclass.getSelectedItem() + "' and examcode='" + jexamcode.getSelectedItem() + "' and term='" + jterm.getSelectedItem() + "' and academicyear='" + jacademicyear.getSelectedItem() + "' and admnumber='" + adm + "' and subjects.subjectcode=examanalysistable.subjectcode and category='" + "mathematics" + "' order by exampercentage desc";
                        ps = con.prepareStatement(sqlxx);
                        rx = ps.executeQuery();
                        while (rx.next()) {
                            codepointer++;
                            codes[codepointer] = rx.getString("SubjectCode");
                            subtp4 += rx.getInt("Sujectexampoints");
                            subtm4 += rx.getInt("exampercentage");
                            break;
                        }
                        int sub = 1;
                        String sqlz = "select exampercentage,Sujectexampoints from examanalysistable where classname='" + jclass.getSelectedItem() + "' and examcode='" + jexamcode.getSelectedItem() + "' and term='" + jterm.getSelectedItem() + "' and academicyear='" + jacademicyear.getSelectedItem() + "' and admnumber='" + adm + "' and subjectcode!='" + codes[0] + "' and subjectcode!='" + codes[1] + "' and subjectcode!='" + codes[2] + "' and subjectcode!='" + codes[3] + "' and subjectcode!='" + codes[4] + "'  order by exampercentage desc";
                        ps = con.prepareStatement(sqlz);
                        rx = ps.executeQuery();
                        while (rx.next()) {

                            subtp3 += rx.getInt("Sujectexampoints");
                            subtm3 += rx.getInt("exampercentage");
                            sub++;
                            if (sub > 2) {
                                break;
                            }
                        }


                        totalpoint += subtp1 + subtp2 + subtp3 + subtp4;
                        totalmarks += subtm1 + subtm2 + subtm3 + subtm4;

                        totalsubject = 7;

                        String grade = "";
                        if (option5.isSelected()) {

                            String sqla = "Select grade from meangrade_table where classcode='" + Globals.classCode(jclass.getSelectedItem().toString()) + "' and '" + nf2.format((totalmarks / totalsubject)) + "'>=start_from and '" + nf2.format((totalmarks / totalsubject)) + "'<=end_at ";
                            ps = con.prepareStatement(sqla);
                            rx = ps.executeQuery();
                            if (rx.next()) {
                                grade = rx.getString("grade");
                            }
                        } else {
                            double p = (totalpoint / totalsubject);
                            int point = (int) Math.round(p);

                            String sqla = "Select grade from points_for_each_grade where points='" + point + "' ";
                            ps = con.prepareStatement(sqla);
                            rx = ps.executeQuery();
                            if (rx.next()) {
                                grade = rx.getString("grade");
                            }
                        }
                        String teachercomment = "";
                        String principalcomment = "";
                        String sqla = "Select comments from teachersgeneralcomments where classcode='" + Globals.classCode(jclass.getSelectedItem().toString()) + "' and grade='" + grade + "' ";
                        ps = con.prepareStatement(sqla);
                        rx = ps.executeQuery();
                        if (rx.next()) {
                            teachercomment = rx.getString("comments");
                        }
                        String sqlb = "Select comments from principalcomments where grade='" + grade + "' ";
                        ps = con.prepareStatement(sqlb);
                        rx = ps.executeQuery();
                        if (rx.next()) {
                            principalcomment = rx.getString("comments");
                        }
                        double avgp = (totalpoint / totalsubject);
                        double avgm = (totalmarks / totalsubject);
                        String kcpe = Globals.kcpeMarks(adm);
                        double kcpem = 0;
                        double kcperatio = 0, exammeanratio = 0;
                        if (kcpe.isEmpty()) {
                            kcperatio = 0;
                            exammeanratio = 0;
                        } else {
                            kcpem = Double.parseDouble(kcpe);


                            kcperatio = kcpem / 500.00;
                            exammeanratio = avgp / 12.00;
                        }
                        double vap = (exammeanratio - kcperatio);


                        String sql3 = "Update examanalysistable set totalmarks='" + totalmarks + "',principalscomment='" + principalcomment + "',classteachergeneralcomment='" + teachercomment + "',totalpoints='" + totalpoint + "',meanpoints='" + nf.format(avgp) + "',meanmarks='" + nf.format(avgm) + "',meangrade='" + grade + "' ,vap='" + nf.format(vap) + "' where examcode='" + jexamcode.getSelectedItem() + "' and academicyear='" + jacademicyear.getSelectedItem() + "' and classname='" + jclass.getSelectedItem() + "' and admnumber='" + adm + "' ";
                        ps = con.prepareStatement(sql3);
                        ps.execute();
                    } else {
                        String sql3 = "Update examanalysistable set principalscomment='" + "You Should Not Miss Exams" + "',classteachergeneralcomment='" + "Missing Exams Negatively Affects Your Academic Potential" + "' where examcode='" + jexamcode.getSelectedItem() + "' and academicyear='" + jacademicyear.getSelectedItem() + "' and classname='" + jclass.getSelectedItem() + "' and admnumber='" + adm + "' ";
                        ps = con.prepareStatement(sql3);
                        ps.execute();

                    }
                }

            } catch (Exception sq) {
                sq.printStackTrace();
                JOptionPane.showMessageDialog(null, sq.getMessage());
            }

        } else {
            try {
                String sqlstart = "select admnumber from examanalysistable where classname='" + jclass.getSelectedItem() + "' and examcode='" + jexamcode.getSelectedItem() + "' and term='" + jterm.getSelectedItem() + "' and academicyear='" + jacademicyear.getSelectedItem() + "' group by admNumber";
                ps = con.prepareStatement(sqlstart);
                rs = ps.executeQuery();
                while (rs.next()) {

                    value += 20.0000 / rows;
                    bar.setValue((int) value);
                    counter++;
                    double avgp = 0, avgm = 0;
                    double totalpoint = 0;
                    double totalmarks = 0;

                    String adm = rs.getString("admnumber");

                    if (Globals.gradableeditted(adm, jexamcode.getSelectedItem().toString(), jacademicyear.getSelectedItem().toString())) {


                        int totalsubject = Globals.subjectAllocationCounter(adm, jacademicyear.getSelectedItem().toString());
                        String sql = "select sum(exampercentage),sum(Sujectexampoints) from examanalysistable where classname='" + jclass.getSelectedItem() + "' and examcode='" + jexamcode.getSelectedItem() + "' and term='" + jterm.getSelectedItem() + "' and academicyear='" + jacademicyear.getSelectedItem() + "' and admnumber='" + adm + "'";
                        ps = con.prepareStatement(sql);
                        ResultSet rx = ps.executeQuery();
                        if (rx.next()) {
                            totalpoint = rx.getInt("sum(Sujectexampoints)");
                            totalmarks = rx.getInt("sum(exampercentage)");

                        }

                        String grade = "";
                        if (option5.isSelected()) {
                            String sqla = "Select grade from meangrade_table where classcode='" + Globals.classCode(jclass.getSelectedItem().toString()) + "' and '" + nf2.format((totalmarks / totalsubject)) + "'>=start_from and '" + nf2.format((totalmarks / totalsubject)) + "'<=end_at ";
                            ps = con.prepareStatement(sqla);
                            rx = ps.executeQuery();
                            if (rx.next()) {
                                grade = rx.getString("grade");
                            }
                        } else {

                            double p = (totalpoint / totalsubject);
                            int point = (int) Math.round(p);

                            String sqla = "Select grade from points_for_each_grade where points='" + point + "' ";
                            ps = con.prepareStatement(sqla);
                            rx = ps.executeQuery();
                            if (rx.next()) {
                                grade = rx.getString("grade");
                            }
                        }
                        String teachercomment = "";
                        String principalcomment = "";
                        String sqla = "Select comments from teachersgeneralcomments where classcode='" + Globals.classCode(jclass.getSelectedItem().toString()) + "' and grade='" + grade + "' ";
                        ps = con.prepareStatement(sqla);
                        rx = ps.executeQuery();
                        if (rx.next()) {
                            teachercomment = rx.getString("comments");
                        }
                        String sqlb = "Select comments from principalcomments where grade='" + grade + "' ";
                        ps = con.prepareStatement(sqlb);
                        rx = ps.executeQuery();
                        if (rx.next()) {
                            principalcomment = rx.getString("comments");
                        }
                        avgp = totalpoint / totalsubject;
                        avgm = (totalmarks / totalsubject);
                        String kcpe = Globals.kcpeMarks(adm);
                        double kcpem = 0;
                        double kcperatio = 0, exammeanratio = 0;
                        if (kcpe.isEmpty()) {
                            kcperatio = 0;
                            exammeanratio = 0;
                        } else {
                            kcpem = Double.parseDouble(kcpe);


                            kcperatio = kcpem / 500.00;
                            exammeanratio = avgp / 12.00;
                        }

                        double vap = (exammeanratio - kcperatio);

                        System.err.println("mean points" + nf.format(avgp) + " For :" + adm);
                        String sql3 = "Update examanalysistable set totalmarks='" + totalmarks + "',principalscomment='" + principalcomment + "',vap='" + nf.format(vap) + "',classteachergeneralcomment='" + teachercomment + "',totalpoints='" + totalpoint + "',meanpoints='" + nf.format(avgp) + "',meanmarks='" + nf.format(avgm) + "',meangrade='" + grade + "'  where examcode='" + jexamcode.getSelectedItem() + "' and academicyear='" + jacademicyear.getSelectedItem() + "' and classname='" + jclass.getSelectedItem() + "' and admnumber='" + adm + "' ";
                        ps = con.prepareStatement(sql3);
                        ps.execute();
                    } else {

                        String sql3 = "Update examanalysistable set principalscomment='" + "You Should Not Miss Exams" + "',classteachergeneralcomment='" + "Missing Exams Negatively Affects Your Academic Potential" + "' where examcode='" + jexamcode.getSelectedItem() + "' and academicyear='" + jacademicyear.getSelectedItem() + "' and classname='" + jclass.getSelectedItem() + "' and admnumber='" + adm + "' ";
                        ps = con.prepareStatement(sql3);
                        ps.execute();

                    }

                }

            } catch (Exception sq) {
                sq.printStackTrace();
                JOptionPane.showMessageDialog(null, sq.getMessage());

            }

        }

    }

    public void positionAssigner() {
        if (option3.isSelected()) {
            try {
                int press = 0;
                int year = (int) jacademicyear.getSelectedItem();
                ps = con.prepareStatement("Select precisions from terms where termname='" + jterm.getSelectedItem() + "'");
                rs = ps.executeQuery();
                if (rs.next()) {
                    press = rs.getInt("precisions");
                }
                int lastpress = press - 1;
                int headcount = 0;
                double previousscore = 0;
                int totalentries = 0;

                String sql2 = "Select admnumber  from examanalysistable where examcode='" + jexamcode.getSelectedItem() + "' and classname='" + jclass.getSelectedItem() + "' and academicyear='" + jacademicyear.getSelectedItem() + "' group by admnumber";
                ps = con.prepareStatement(sql2);
                rs = ps.executeQuery();
                while (rs.next()) {
                    totalentries++;
                }
                int tiechck = 0;
                rows = totalentries;
                counter = 1;
                String sql3 = "Select meanpoints,admnumber from examanalysistable where examcode='" + jexamcode.getSelectedItem() + "' and academicyear='" + jacademicyear.getSelectedItem() + "' and classname='" + jclass.getSelectedItem() + "'   group by admnumber order by meanpoints desc";
                ps = con.prepareStatement(sql3);
                rs = ps.executeQuery();
                while (rs.next()) {
                    String adm = rs.getString("admNumber");

                    value += 10.0000 / rows;
                    bar.setValue((int) value);
                    counter++;
                    double examresult = rs.getDouble("meanpoints");
                    if (examresult == previousscore) {
                        tiechck++;
                    } else {
                        headcount++;
                        headcount += tiechck;
                        tiechck = 0;
                    }

                    String lastexamcode = " ";
                    String lasttermposition = " ";
                    String lastposstreampos = " ";
                    String lastermposoutof = " ";
                    String lasttermstreamposoutof = " ";
                    if (jterm.getSelectedItem().toString().equalsIgnoreCase("Term 1")) {
                        lastexamcode = jexamcode.getSelectedItem().toString().replaceAll(String.valueOf(year), String.valueOf((year - 1)));
                        lastexamcode = lastexamcode.replaceAll(jexamname.getSelectedItem() + "TM" + press, jexamname.getSelectedItem() + "TM3");
                    } else {

                        lastexamcode = jexamcode.getSelectedItem().toString().replaceAll(jexamname.getSelectedItem() + "TM" + press, jexamname.getSelectedItem() + "TM" + lastpress);
                    }

                    String sqla = "Select classpositionthisterm,Streampositionthisterm,Streampositionthistermoutof,classpositionthistermoutof from examanalysistable where examcode='" + lastexamcode + "' and admnumber='" + adm + "' group by admnumber ";
                    ps = con.prepareStatement(sqla);
                    ResultSet last = ps.executeQuery();
                    if (last.next()) {
                        lasttermposition = last.getString("classpositionthisterm");
                        lastposstreampos = last.getString("Streampositionthisterm");
                        lastermposoutof = last.getString("classpositionthistermoutof");
                        lasttermstreamposoutof = last.getString("Streampositionthistermoutof");

                    } else {
                        lasttermposition = " ";
                        lastposstreampos = " ";
                        lastermposoutof = " ";
                        lasttermstreamposoutof = " ";
                    }

                    String sql4 = "Update examanalysistable  set classpositionlasttermoutof='" + lastermposoutof + "',Streampositionlasttermoutof='" + lasttermstreamposoutof + "',classpositionlastterm='" + lasttermposition + "', streampositionlastterm='" + lastposstreampos + "', classpositionthisterm='" + headcount + "',classpositionthistermoutof='" + totalentries + "' where examcode='" + jexamcode.getSelectedItem() + "' and academicyear='" + jacademicyear.getSelectedItem() + "' and classname='" + jclass.getSelectedItem() + "'  and admnumber='" + adm + "' ";
                    ps = con.prepareStatement(sql4);
                    ps.execute();

                    previousscore = rs.getDouble("meanpoints");
                }

                String sql = "Select streamname from streams";
                ps = con.prepareStatement(sql);
                ResultSet Rx = ps.executeQuery();
                while (Rx.next()) {
                    String name = Rx.getString("StreamName");
                    headcount = 0;
                    previousscore = 0;
                    totalentries = 0;

                    sql2 = "Select admnumber  from examanalysistable where examcode='" + jexamcode.getSelectedItem() + "' and classname='" + jclass.getSelectedItem() + "' and academicyear='" + jacademicyear.getSelectedItem() + "' and stream='" + name + "' group by admnumber";
                    ps = con.prepareStatement(sql2);
                    rs = ps.executeQuery();
                    while (rs.next()) {
                        totalentries++;
                    }
                    tiechck = 0;
                    rows = totalentries;
                    counter = 1;
                    sql3 = "Select meanpoints,admnumber from examanalysistable where examcode='" + jexamcode.getSelectedItem() + "' and academicyear='" + jacademicyear.getSelectedItem() + "' and stream='" + name + "'  group by admnumber order by meanpoints desc";
                    ps = con.prepareStatement(sql3);
                    rs = ps.executeQuery();
                    while (rs.next()) {
                        value += 2.5000 / rows;
                        bar.setValue((int) value);
                        counter++;
                        String adm = rs.getString("admNumber");

                        double examresult = rs.getDouble("meanpoints");
                        if (examresult == previousscore) {
                            tiechck++;
                        } else {
                            headcount++;
                            headcount += tiechck;
                            tiechck = 0;
                        }

                        String sql4 = "Update examanalysistable  set Streampositionthisterm='" + headcount + "',Streampositionthistermoutof='" + totalentries + "' where examcode='" + jexamcode.getSelectedItem() + "' and academicyear='" + jacademicyear.getSelectedItem() + "' and classname='" + jclass.getSelectedItem() + "'  and admnumber='" + adm + "' ";
                        ps = con.prepareStatement(sql4);
                        ps.execute();

                        previousscore = rs.getDouble("meanpoints");
                    }

                }

            } catch (Exception sq) {
                sq.printStackTrace();
                JOptionPane.showMessageDialog(null, sq.getMessage());
            }

        } else {
            try {
                int press = 0;
                int year = (int) jacademicyear.getSelectedItem();
                ps = con.prepareStatement("Select precisions from terms where termname='" + jterm.getSelectedItem() + "'");
                rs = ps.executeQuery();
                if (rs.next()) {
                    press = rs.getInt("precisions");
                }
                int lastpress = press - 1;
                int headcount = 0;
                double previousscore = 0;
                int totalentries = 0;

                String sql2 = "Select admnumber from examanalysistable where examcode='" + jexamcode.getSelectedItem() + "' and classname='" + jclass.getSelectedItem() + "' and academicyear='" + jacademicyear.getSelectedItem() + "' group by admnumber";
                ps = con.prepareStatement(sql2);
                rs = ps.executeQuery();
                while (rs.next()) {
                    totalentries++;
                }
                int tiechck = 0;
                rows = totalentries;
                counter = 1;

                String sql3 = "Select meanmarks,admnumber from examanalysistable where examcode='" + jexamcode.getSelectedItem() + "' and academicyear='" + jacademicyear.getSelectedItem() + "'  group by admnumber order by meanmarks desc";
                ps = con.prepareStatement(sql3);
                rs = ps.executeQuery();
                while (rs.next()) {
                    String adm = rs.getString("admNumber");

                    value += 10.0000 / rows;
                    bar.setValue((int) value);
                    counter++;
                    double examresult = rs.getDouble("meanmarks");
                    if (examresult == previousscore) {
                        tiechck++;
                    } else {
                        headcount++;
                        headcount = (headcount + tiechck);
                        tiechck = 0;
                    }
                    String lastexamcode = " ";
                    String lasttermposition = " ";
                    String lastposstreampos = " ";
                    String lastermposoutof = " ";
                    String lasttermstreamposoutof = " ";
                    if (jterm.getSelectedItem().toString().equalsIgnoreCase("Term 1")) {
                        lastexamcode = jexamcode.getSelectedItem().toString().replaceAll(String.valueOf(year), String.valueOf((year - 1)));
                        lastexamcode = lastexamcode.replaceAll(jexamname.getSelectedItem() + "TM" + press, jexamname.getSelectedItem() + "TM3");
                    } else {

                        lastexamcode = jexamcode.getSelectedItem().toString().replaceAll(jexamname.getSelectedItem() + "TM" + press, jexamname.getSelectedItem() + "TM" + lastpress);
                    }

                    String sqla = "Select classpositionthisterm,Streampositionthisterm,Streampositionthistermoutof,classpositionthistermoutof from examanalysistable where examcode='" + lastexamcode + "' and admnumber='" + adm + "' group by admnumber ";
                    ps = con.prepareStatement(sqla);
                    ResultSet last = ps.executeQuery();
                    if (last.next()) {
                        lasttermposition = last.getString("classpositionthisterm");
                        lastposstreampos = last.getString("Streampositionthisterm");
                        lastermposoutof = last.getString("classpositionthistermoutof");
                        lasttermstreamposoutof = last.getString("Streampositionthistermoutof");

                    } else {
                        lasttermposition = " ";
                        lastposstreampos = " ";
                        lastermposoutof = " ";
                        lasttermstreamposoutof = " ";
                    }
                    String sql4 = "Update examanalysistable  set classpositionlasttermoutof='" + lastermposoutof + "',Streampositionlasttermoutof='" + lasttermstreamposoutof + "',classpositionlastterm='" + lasttermposition + "',streampositionlastterm='" + lastposstreampos + "', classpositionthisterm='" + headcount + "',classpositionthistermoutof='" + totalentries + "' where examcode='" + jexamcode.getSelectedItem() + "' and academicyear='" + jacademicyear.getSelectedItem() + "' and classname='" + jclass.getSelectedItem() + "'  and admnumber='" + adm + "' ";
                    ps = con.prepareStatement(sql4);
                    ps.execute();

                    previousscore = rs.getDouble("meanmarks");
                }

                String sql = "Select streamname from streams ";
                ps = con.prepareStatement(sql);
                ResultSet Rx = ps.executeQuery();
                while (Rx.next()) {
                    String name = Rx.getString("StreamName");
                    headcount = 0;
                    previousscore = 0;
                    totalentries = 0;

                    sql2 = "Select distinct admnumber from examanalysistable where examcode='" + jexamcode.getSelectedItem() + "' and classname='" + jclass.getSelectedItem() + "' and academicyear='" + jacademicyear.getSelectedItem() + "' and stream='" + name + "' group by admnumber";
                    ps = con.prepareStatement(sql2);
                    ResultSet re = ps.executeQuery();
                    while (re.next()) {

                        totalentries++;
                    }

                    tiechck = 0;
                    rows = totalentries;
                    counter = 1;
                    sql3 = "Select meanmarks,admnumber from examanalysistable where examcode='" + jexamcode.getSelectedItem() + "' and academicyear='" + jacademicyear.getSelectedItem() + "' and stream='" + name + "'  group by admnumber order by meanmarks desc";
                    ps = con.prepareStatement(sql3);
                    rs = ps.executeQuery();
                    while (rs.next()) {
                        String adm = rs.getString("admNumber");
                        value += 2.5000 / rows;
                        bar.setValue((int) value);
                        counter++;

                        double examresult = rs.getDouble("meanmarks");
                        if (examresult == previousscore) {
                            tiechck++;
                        } else {
                            headcount++;
                            headcount = (headcount + tiechck);
                            tiechck = 0;
                        }

                        String sql4 = "Update examanalysistable  set Streampositionthisterm='" + headcount + "',Streampositionthistermoutof='" + totalentries + "' where examcode='" + jexamcode.getSelectedItem() + "' and academicyear='" + jacademicyear.getSelectedItem() + "' and classname='" + jclass.getSelectedItem() + "'  and admnumber='" + adm + "' ";
                        ps = con.prepareStatement(sql4);
                        ps.execute();

                        previousscore = rs.getDouble("meanmarks");
                    }

                }

            } catch (Exception sq) {
                sq.printStackTrace();
                value = 0;
                bar.setValue(0);
                dia.setAlwaysOnTop(false);
                JOptionPane.showMessageDialog(null, sq.getMessage());
            }
        }

        value = 0;
        bar.setValue(0);
        dia.setAlwaysOnTop(false);
    }

    public void overallPerformance() {
        try {
            if (option3.isSelected()) {
                double total = 0;
                String sqla = "Select distint subjectcode from subjects order by subjectcode";
                ps = con.prepareStatement(sqla);
                rs = ps.executeQuery();
                while (rs.next()) {
                    String subcode = rs.getString("SubjectCode");
                    String sql = "Select avg(totalpoints) from examanalysistable where examcode='" + jexamcode.getSelectedItem() + "' and classname='" + jclass.getSelectedItem() + "' and academicyear='" + jacademicyear.getSelectedItem() + "' and subjectcode='" + subcode + "'";
                    ps = con.prepareStatement(sql);
                    ResultSet rsb = ps.executeQuery();
                    if (rs.next()) {
                        total = rsb.getDouble("avg(totalpoints)");
                    }

                }
                String sql = "Select sum(totalpoints) from examanalysistable where examcode='" + jexamcode.getSelectedItem() + "' and classname='" + jclass.getSelectedItem() + "' and academicyear='" + jacademicyear.getSelectedItem() + "'";

            }

        } catch (Exception sq) {
            sq.printStackTrace();
            JOptionPane.showMessageDialog(null, sq.getMessage());

        }
    }

}
