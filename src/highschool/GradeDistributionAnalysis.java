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
import javax.swing.ButtonGroup;
import javax.swing.JComponent;
import javax.swing.JFrame;

import javax.swing.JOptionPane;

import static javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE;

/**
 * @author FRED
 */
public class GradeDistributionAnalysis extends JFrame implements ActionListener {

    private Connection con = DbConnection.connectDb();
    private int width = 750;
    private int height = 420;
    private PreparedStatement ps;
    private ResultSet rs;
    private FredButton view = new FredButton("Mean Grade Distribution");
    private FredButton cancel = new FredButton("Close");

    private FredCheckBox summary = new FredCheckBox("Overall Summary");
    private FredCheckBox gendersort = new FredCheckBox("Show Distribution By Gender");
    private ButtonGroup buttonGroup = new ButtonGroup();
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
    private FredCombo jsubject = new FredCombo("Leave This To View All Subjects");
    private FredCombo jsubjectcode = new FredCombo("Subject Code");
    private FredLabel subject = new FredLabel("Subject");
    private FredLabel subjectcode = new FredLabel("Subject Code");
    private FredButton mean = new FredButton("Grade Distribution Per Subject");
    private FredCheckBox targets = new FredCheckBox("Include Targets");
    String tm, cl, yr, ex;

    public GradeDistributionAnalysis() {

        setSize(width, height);
        setTitle("Grade Distribution Analysis");
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
        buttonGroup.add(summary);
        buttonGroup.add(gendersort);
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

        subject.setBounds(30, 250, 150, 30);
        add(subject);
        jsubject.setBounds(150, 250, 200, 30);
        add(jsubject);
        subjectcode.setBounds(400, 250, 150, 30);
        add(jsubjectcode);
        jsubjectcode.setBounds(500, 250, 200, 30);
        add(jsubjectcode);
        summary.setBounds(120, 280, 150, 30);
        add(summary);
        gendersort.setBounds(300, 280, 200, 30);
        add(gendersort);
        targets.setBounds(500, 280, 150, 30);
        add(targets);
        cancel.setBounds(80, 320, 130, 50);
        add(cancel);
        mean.setBounds(240, 320, 220, 50);
        add(mean);
        view.setBounds(500, 320, 200, 50);
        add(view);
        summary.setSelected(true);
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
            String sqld = "Select examcode from exams ";
            ps = con.prepareStatement(sqld);
            rs = ps.executeQuery();
            while (rs.next()) {
                jexamcode.addItem(rs.getString("Examcode"));
            }
            String sql2 = "Select * from streams";

            ps = con.prepareStatement(sql2);
            rs = ps.executeQuery();
            while (rs.next()) {

                jstream.addItem(rs.getString("StreamName"));
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
        } else if (obj == jsubject) {
            try {
                String sql = "Select subjectcode from subjects where subjectname='" + jsubject.getSelectedItem() + "'";
                ps = con.prepareStatement(sql);
                rs = ps.executeQuery();
                if (rs.next()) {
                    jsubjectcode.setSelectedItem(rs.getString("Subjectcode"));
                } else {
                    jsubjectcode.setSelectedIndex(0);
                }
            } catch (Exception sq) {
                sq.printStackTrace();
                JOptionPane.showMessageDialog(null, sq.getMessage());
            }
        } else if (obj == jsubjectcode) {
            try {
                String sql = "Select subjectname from subjects where subjectcode='" + jsubjectcode.getSelectedItem() + "'";
                ps = con.prepareStatement(sql);
                rs = ps.executeQuery();
                if (rs.next()) {
                    jsubject.setSelectedItem(rs.getString("Subjectname"));
                }
            } catch (Exception sq) {
                sq.printStackTrace();
                JOptionPane.showMessageDialog(null, sq.getMessage());
            }
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

                                String st = "";
                                if (jstream.getSelectedIndex() == 0) {
                                    st = "OVERALL";
                                } else {
                                    st = jstream.getSelectedItem().toString();
                                }
                                String gender = "";
                                if (summary.isSelected()) {
                                    gender = "Summary";
                                } else {
                                    gender = "gender";
                                }
                                boolean includetargets = false;
                                if (targets.isSelected()) {
                                    includetargets = true;
                                } else {
                                    includetargets = false;
                                }
                                GradeDistributionReport.gradedistributionReport(jexamcode.getSelectedItem().toString(), jexame.getSelectedItem().toString(), jterm.getSelectedItem().toString(), jclass.getSelectedItem().toString(), jacademicyear.getSelectedItem().toString(), st, gender, includetargets);

                            }
                        }
                    }
                }
            }

        } else if (obj == mean) {

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

                                String st = "";
                                String sub = "";
                                if (jstream.getSelectedIndex() == 0) {
                                    st = "OVERALL";
                                } else {
                                    st = jstream.getSelectedItem().toString();
                                }
                                if (jsubjectcode.getSelectedIndex() == 0) {
                                    sub = "all";
                                } else {
                                    sub = jsubjectcode.getSelectedItem().toString();
                                }
                                String gender = "";
                                if (summary.isSelected()) {
                                    gender = "All";
                                } else {
                                    gender = "gender";
                                }
                                boolean includetargets = false;
                                if (targets.isSelected()) {
                                    includetargets = true;
                                } else {
                                    includetargets = false;
                                }

                                GradeDistributionReport.distributionPerSubject(jexamcode.getSelectedItem().toString(), jexame.getSelectedItem().toString(), jterm.getSelectedItem().toString(), jclass.getSelectedItem().toString(), jacademicyear.getSelectedItem().toString(), st, sub, gender, includetargets);

                            }
                        }
                    }
                }
            }

        }

    }

    public static void main(String[] args) {
        new GradeDistributionAnalysis();
    }

}
