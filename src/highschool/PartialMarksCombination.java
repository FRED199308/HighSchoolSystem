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
import java.sql.ResultSetMetaData;
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

/**
 * @author FRED
 */
public class PartialMarksCombination extends JFrame implements ActionListener {

    private Connection con = DbConnection.connectDb();
    private int width = 750;
    private int height = 450;
    private PreparedStatement ps;
    private ResultSet rs;
    private FredButton view = new FredButton("Combine");
    private FredButton cancel = new FredButton("Close");

    private FredCombo jclass = new FredCombo("Select Class");
    private FredLabel name = new FredLabel("Class");
    private FredLabel examname = new FredLabel("Exam Name");
    private FredLabel examcode = new FredLabel("Exam Code");
    private FredLabel number = new FredLabel("Fetch Best");
    private FredTextField jnumber = new FredTextField("3");
    private FredCombo jexamcode = new FredCombo("Select Exam Code");
    private FredLabel stream = new FredLabel("Stream");
    private FredCombo jstream = new FredCombo("Select Stream");
    private FredCombo jterm = new FredCombo("Select Term");
    private FredLabel term = new FredLabel("Term");
    private FredCombo jexame = new FredCombo("Select Exam");
    private FredLabel academicyear = new FredLabel("Academic Year");
    private FredCombo jacademicyear = new FredCombo("Select Academic Year");
    private FredCombo jsubject = new FredCombo("Leave This To Combine All Subjects");
    private FredCombo jsubjectcode = new FredCombo("Subject Code");
    private FredLabel subject = new FredLabel("Subject");
    private FredLabel subjectcode = new FredLabel("Subject Code");
    private FredButton mean = new FredButton("Grade Distribution Per Subject");
    String tm, cl, yr, ex;

    public PartialMarksCombination() {
        setSize(width, height);
        setTitle("Subject Papers Combination Window");
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
        subject.setBounds(400, 30, 150, 30);
        add(subject);
        jsubject.setBounds(500, 30, 200, 30);
        add(jsubject);
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

        cancel.setBounds(100, 300, 130, 50);
        add(cancel);

        view.setBounds(400, 300, 200, 50);
        add(view);

        try {
//jsubject.addItem("Combine All");
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
            String sqll = "Select * from subjects";
            ps = con.prepareStatement(sqll);
            rs = ps.executeQuery();
            while (rs.next()) {
                jsubjectcode.addItem(rs.getString("Subjectcode"));
                jsubject.addItem(rs.getString("SubjectName"));
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
        jstream.setEnabled(false);
        view.addActionListener(this);
        mean.addActionListener(this);
        jexamcode.addActionListener(this);
        jexame.addActionListener(this);
        cancel.addActionListener(this);
        jterm.addActionListener(this);
        jsubject.addActionListener(this);
        jsubjectcode.addActionListener(this);
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
        if (jstream.getItemCount() < 3) {
            jstream.setSelectedIndex(1);
        }
        jacademicyear.setSelectedItem(Globals.academicYear());
        jterm.setSelectedItem(Globals.currentTermName());

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object obj = e.getSource();
        if (obj == cancel) {
            CurrentFrame.currentWindow();
            dispose();
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
        } else if (obj == jexame) {
            jexamcode.removeActionListener(this);
            if (jexame.getSelectedIndex() > 0) {
                ex = jexame.getSelectedItem().toString();

                String excode = ExamCodesGenerator.generatecode(cl, yr, tm, ex).toUpperCase();
                jexamcode.addItem(excode);
                jexamcode.setSelectedItem(excode);

            }
            jexamcode.addActionListener(this);
        } else if (obj == view) {

            if (jclass.getSelectedIndex() == 0) {
                JOptionPane.showMessageDialog(this, "Kindly Select The Class");
            } else {
                if (jterm.getSelectedIndex() == 0) {
                    JOptionPane.showMessageDialog(this, "Kindly Select The Term");
                } else {
                    if (jacademicyear.getSelectedIndex() == 0) {
                        JOptionPane.showMessageDialog(this, "Select The Academic Year");
                    } else {
                        if (jexamcode.getSelectedIndex() == 0) {
                            JOptionPane.showMessageDialog(this, "Kindly Seklect The Exam Code");
                        } else {
                            if (jexame.getSelectedIndex() == 0) {
                                JOptionPane.showMessageDialog(this, "Kindly Select The Exam Name");
                            } else {
                                new Thread() {
                                    @Override
                                    public void run() {
                                        JWindow dia = new JWindow();
                                        JProgressBar bar = new JProgressBar();
                                        NumberFormat nf = NumberFormat.getNumberInstance();
                                        nf.setMaximumFractionDigits(0);
                                        nf.setMinimumFractionDigits(0);

                                        bar.setStringPainted(true);
                                        bar.setMaximum(100);
                                        bar.setMinimum(0);
                                        dia.setSize(400, 60);

                                        bar.setBorder(new TitledBorder("Applying The Grading System And Exam Weight To The Entered Marks"));
                                        dia.setAlwaysOnTop(true);
                                        dia.setLocationRelativeTo(CurrentFrame.mainFrame());
                                        dia.setIconImage(FrameProperties.icon());
                                        dia.add(bar);
                                        dia.setVisible(true);
                                        String excode = jexamcode.getSelectedItem().toString();
                                        String STREAM = "", teacherintial = "";
                                        try {
                                            int counter = 0;
                                            String sqll = "Select distinct admnumber from  partialsubjectMark where  termcode='" + Globals.termcode(jterm.getSelectedItem().toString()) + "' and academicyear='" + jacademicyear.getSelectedItem() + "' and examcode='" + excode + "'";
                                            ps = con.prepareStatement(sqll);
                                            ResultSet ss = ps.executeQuery();
                                            while (ss.next()) {

                                                counter++;
                                            }
                                            if (counter == 0) {
                                                JOptionPane.showMessageDialog(CurrentFrame.mainFrame(), "No Marks Were Found For This Class In The Selected Term And Academic Year");
                                            } else {

                                                int value = 0, counter2 = 0;

                                                String sql2 = "Select distinct admnumber from  partialSubjectMark where  termcode='" + Globals.termcode(jterm.getSelectedItem().toString()) + "' and academicyear='" + jacademicyear.getSelectedItem() + "' and examcode='" + excode + "'";
                                                ps = con.prepareStatement(sql2);
                                                ss = ps.executeQuery();
                                                while (ss.next()) {
                                                    counter2++;
                                                    value = (counter2 * 100) / counter;
                                                    bar.setValue(value);
                                                    String adm = ss.getString("Admnumber");

                                                    if (jsubject.getSelectedIndex() == 0) {

                                                        String sql = " select * from subjects";
                                                        ps = con.prepareStatement(sql);
                                                        ResultSet rx = ps.executeQuery();
                                                        while (rx.next()) {

                                                            int entrycounter = 0;
                                                            String subcode = rx.getString("Subjectcode");

                                                            double score = 0, points = 0, totalValue = 0;
                                                            String grade = "", streamcode = "", classCode = "", techcom = "";

                                                            ps = con.prepareStatement("select * from subjectcombinationRules where subjectcode='" + subcode + "' ");
                                                            ResultSet rx2 = ps.executeQuery();
                                                            while (rx2.next()) {

                                                                String paper = rx2.getString("paper");
                                                                double paperValue = rx2.getDouble("Value");
                                                                double totalPossible = rx2.getDouble("totalPossible");
                                                                String sql3 = "Select * from  partialsubjectMark where  termcode='" + Globals.termcode(jterm.getSelectedItem().toString()) + "' and academicyear='" + jacademicyear.getSelectedItem() + "' and examcode='" + excode + "' and admnumber='" + adm + "' and subjectcode='" + subcode + "' and paper='" + paper + "'";
                                                                ps = con.prepareStatement(sql3);
                                                                ResultSet rr = ps.executeQuery();
                                                                if (rr.next()) {

                                                                    score += (rr.getDouble("Score") * paperValue / totalPossible);

                                                                    streamcode = rr.getString("Streamcode");
                                                                    classCode = rr.getString("Classcode");

                                                                    techcom = rr.getString("teacherinitials");
                                                                    entrycounter++;
                                                                    totalValue += paperValue;
                                                                } else {
                                                                    score += (0 * paperValue / totalPossible);
                                                                    totalPossible = paperValue;
                                                                    totalValue += paperValue;

                                                                }

                                                            }
                                                            if (totalValue > 100) {
                                                                score = score * 100 / totalValue;
                                                            }


                                                            if (entrycounter > 0) {

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
                                                                String sqll2 = "Delete from  markstable where  examcode='" + excode + "' and admnumber='" + adm + "' and subjectcode='" + subcode + "'";
                                                                ps = con.prepareStatement(sqll2);
                                                                ps.execute();
                                                                String sql4 = "Insert into markstable(admnumber,subjectcode,classcode,streamcode,termcode,academicyear,examname,examcode,examscore,examoutof,convertedscore,convertedscoreoutof,exampercentage,examgrade,exampoints,class_teacher_initials) values('" + adm + "','" + subcode + "','" + classCode + "','" + streamcode + "','" + Globals.termcode(jterm.getSelectedItem().toString()) + "','" + jacademicyear.getSelectedItem().toString() + "','" + jexamcode.getSelectedItem() + "','" + excode + "','" + score + "','" + "100" + "','" + "100" + "','" + "100" + "','" + score + "','" + grade + "','" + points + "','" + techcom + "')";
                                                                ps = con.prepareStatement(sql4);
                                                                ps.execute();

                                                            }

                                                        }

                                                    } else {

                                                        String subcode = Globals.subjectCode(jsubject.getSelectedItem().toString());

                                                        int entryCounter = 0;
                                                        double score = 0, points = 0, totalValue = 0;
                                                        String grade = "", streamcode = "", classCode = "", techcom = "";

                                                        ps = con.prepareStatement("select * from subjectcombinationRules where subjectcode='" + subcode + "' ");
                                                        ResultSet rx2 = ps.executeQuery();
                                                        while (rx2.next()) {

                                                            String paper = rx2.getString("paper");
                                                            double paperValue = rx2.getDouble("Value");
                                                            double totalPossible = rx2.getDouble("totalPossible");
                                                            String sql3 = "Select * from  partialsubjectMark where  termcode='" + Globals.termcode(jterm.getSelectedItem().toString()) + "' and academicyear='" + jacademicyear.getSelectedItem() + "' and examcode='" + excode + "' and admnumber='" + adm + "' and subjectcode='" + subcode + "' and paper='" + paper + "'";
                                                            ps = con.prepareStatement(sql3);
                                                            ResultSet rr = ps.executeQuery();
                                                            if (rr.next()) {
                                                                entryCounter++;
                                                                score += (rr.getDouble("Score") * paperValue / totalPossible);

                                                                streamcode = rr.getString("Streamcode");
                                                                classCode = rr.getString("Classcode");

                                                                techcom = rr.getString("teacherinitials");
                                                                totalValue += paperValue;

                                                            } else {
                                                                score += (0 * paperValue / totalPossible);
                                                                totalValue += paperValue;

                                                            }
                                                        }
                                                        if (totalValue > 100) {
                                                            score = score * 100 / totalValue;
                                                        }

                                                        if (entryCounter > 0) {
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
                                                            String sqll2 = "Delete from  markstable where  examcode='" + excode + "' and admnumber='" + adm + "' and subjectcode='" + subcode + "'";
                                                            ps = con.prepareStatement(sqll2);
                                                            ps.execute();
                                                            String sql4 = "Insert into markstable(admnumber,subjectcode,classcode,streamcode,termcode,academicyear,examname,examcode,examscore,examoutof,convertedscore,convertedscoreoutof,exampercentage,examgrade,exampoints,class_teacher_initials) values('" + adm + "','" + subcode + "','" + classCode + "','" + streamcode + "','" + Globals.termcode(jterm.getSelectedItem().toString()) + "','" + jacademicyear.getSelectedItem().toString() + "','" + jexamcode.getSelectedItem() + "','" + excode + "','" + score + "','" + "100" + "','" + "100" + "','" + "100" + "','" + score + "','" + grade + "','" + points + "','" + techcom + "')";
                                                            ps = con.prepareStatement(sql4);
                                                            ps.execute();
                                                        }

                                                    }

                                                }

                                                dia.dispose();
                                                JOptionPane.showMessageDialog(null, "Subject marks combination Complete");

                                            }

                                        } catch (Exception sq) {
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

        }

    }

    public static void main(String[] args) {
        new PartialMarksCombination();
    }
}
