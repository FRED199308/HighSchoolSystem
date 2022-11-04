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
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;

import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JProgressBar;

import static javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE;

import javax.swing.border.TitledBorder;

/**
 * @author ExamSeverPc
 */
public class BestImproved extends JFrame implements ActionListener {
    private Connection con = DbConnection.connectDb();
    private int width = 750;
    private int height = 530;
    private PreparedStatement ps;
    private ResultSet rs;
    private FredButton view = new FredButton("View Report");
    private FredButton cancel = new FredButton("Close");
    private FredButton meanimprovement = new FredButton("Overall Improved");

    private FredLabel infor = new FredLabel("Select The Lower Refference Exam");
    private FredCombo jclass = new FredCombo("Select Class");
    private FredLabel name = new FredLabel("Class");
    private FredLabel examname = new FredLabel("Exam Name");
    private FredLabel examcode = new FredLabel("Exam Code");
    private FredLabel number = new FredLabel("Fetch Best");
    private FredTextField jnumber = new FredTextField();
    private FredCombo jexamcode = new FredCombo("Select Exam Code");
    private FredLabel stream = new FredLabel("Stream");
    private FredCombo jstream = new FredCombo("Select Stream");
    private FredCombo jterm = new FredCombo("Select Term");
    private FredLabel term = new FredLabel("Term");
    private FredCombo jexame = new FredCombo("Select Exam");
    private FredLabel academicyear = new FredLabel("Academic Year");
    private FredCombo jacademicyear = new FredCombo("Select Academic Year");


    private FredLabel infor1 = new FredLabel("Select The Upper Refference Exam");
    private FredCombo jclass1 = new FredCombo("Select Class");
    private FredLabel name1 = new FredLabel("Class");
    private FredLabel examname1 = new FredLabel("Exam Name");
    private FredLabel examcode1 = new FredLabel("Exam Code");
    private FredLabel number1 = new FredLabel("Fetch Best");
    private FredTextField jnumber1 = new FredTextField("3");
    private FredCombo jexamcode1 = new FredCombo("Select Exam Code");
    private FredLabel stream1 = new FredLabel("Stream");
    private FredCombo jstream1 = new FredCombo("Select Stream");
    private FredCombo jterm1 = new FredCombo("Select Term");
    private FredLabel term1 = new FredLabel("Term");
    private FredCombo jexame1 = new FredCombo("Select Exam");
    private FredLabel academicyear1 = new FredLabel("Academic Year");
    private FredCombo jacademicyear1 = new FredCombo("Select Academic Year");

    private FredCheckBox analyse = new FredCheckBox("Analyse First");
    JProgressBar bar = new JProgressBar();


    private FredButton mean = new FredButton("Grade Distribution Per Subject");
    String tm, cl, yr, ex;
    String tm1, cl1, yr1, ex1;

    public BestImproved() {
        setSize(width, height);
        setTitle("Most Improved Student Overall");
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
        infor.setBounds(150, 10, 300, 30);
        add(infor);
        name.setBounds(30, 50, 150, 30);
        add(name);
        jclass.setBounds(150, 50, 200, 30);
        add(jclass);
        stream.setBounds(400, 50, 150, 30);
        add(stream);
        jstream.setBounds(500, 50, 200, 30);
        add(jstream);
        term.setBounds(30, 100, 150, 30);
        add(term);
        jterm.setBounds(150, 100, 200, 30);
        add(jterm);
        academicyear.setBounds(400, 100, 150, 30);
        add(academicyear);
        jacademicyear.setBounds(500, 100, 200, 30);
        add(jacademicyear);
        examname.setBounds(30, 140, 150, 30);
        add(examname);
        jexame.setBounds(150, 140, 200, 30);
        add(jexame);
        examcode.setBounds(400, 140, 150, 30);
        add(examcode);
        jexamcode.setBounds(500, 140, 200, 30);
        add(jexamcode);


        infor1.setBounds(150, 180, 300, 30);
        add(infor1);
        name1.setBounds(30, 210, 150, 30);
        add(name1);
        jclass1.setBounds(150, 210, 200, 30);
        add(jclass1);
        stream1.setBounds(400, 210, 150, 30);
        add(stream1);
        jstream1.setBounds(500, 210, 200, 30);
        add(jstream1);
        term1.setBounds(30, 250, 150, 30);
        add(term1);
        jterm1.setBounds(150, 250, 200, 30);
        add(jterm1);
        academicyear1.setBounds(400, 250, 150, 30);
        add(academicyear1);
        jacademicyear1.setBounds(500, 250, 200, 30);
        add(jacademicyear1);
        examname1.setBounds(30, 300, 150, 30);
        add(examname1);
        jexame1.setBounds(150, 300, 200, 30);
        add(jexame1);
        examcode.setBounds(400, 300, 150, 30);
        add(examcode1);
        jexamcode1.setBounds(500, 300, 200, 30);
        add(jexamcode1);


        number.setBounds(150, 370, 200, 30);
        add(number);
        jnumber.setBounds(350, 370, 100, 30);
        add(jnumber);
        analyse.setBounds(500, 370, 200, 30);
        add(analyse);

        cancel.setBounds(100, 420, 130, 50);
        add(cancel);

        meanimprovement.setBounds(450, 420, 200, 50);
        add(meanimprovement);


        bar.setBorder(new TitledBorder("Processing Request"));
        bar.setIndeterminate(true);
        bar.setBounds(150, 470, 500, 25);
        add(bar);
        bar.setVisible(false);
        try {

            for (int k = 2015; k <= Globals.academicYear(); ++k) {
                jacademicyear.addItem(k);
                jacademicyear1.addItem(k);
            }
            String sqls = "Select examname from exams group by examname";
            ps = con.prepareStatement(sqls);
            rs = ps.executeQuery();
            while (rs.next()) {
                jexame.addItem(rs.getString("ExamName"));
                jexame1.addItem(rs.getString("ExamName"));
            }

            String sql2 = "Select * from streams";

            ps = con.prepareStatement(sql2);
            rs = ps.executeQuery();
            while (rs.next()) {

                jstream.addItem(rs.getString("StreamName"));
                jstream1.addItem(rs.getString("StreamName"));
            }
            String sqld = "Select examcode from exams ";
            ps = con.prepareStatement(sqld);
            rs = ps.executeQuery();
            while (rs.next()) {
                jexamcode.addItem(rs.getString("Examcode"));
                jexamcode1.addItem(rs.getString("Examcode"));
            }
            String sql = "Select * from classes where precision1<5 order by precision1 asc";
            con = DbConnection.connectDb();
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                jclass.addItem(rs.getString("ClassName"));
                jclass1.addItem(rs.getString("ClassName"));

            }
            String sql9 = "Select* from terms";
            ps = con.prepareStatement(sql9);
            rs = ps.executeQuery();
            while (rs.next()) {
                jterm.addItem(rs.getString("TermName"));
                jterm1.addItem(rs.getString("TermName"));

            }
        } catch (Exception sq) {
            sq.printStackTrace();
            JOptionPane.showMessageDialog(null, sq.getMessage());
        }
        setVisible(true);
        jstream.setEnabled(false);
        view.addActionListener(this);
        mean.addActionListener(this);
        jexamcode1.addActionListener(this);
        jexame1.addActionListener(this);
        jterm1.addActionListener(this);
        jacademicyear1.addActionListener(this);
        jclass1.addActionListener(this);
        jexamcode.addActionListener(this);
        jexame.addActionListener(this);
        cancel.addActionListener(this);
        meanimprovement.addActionListener(this);
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
        if (jstream.getItemCount() < 3) {
            jstream.setSelectedIndex(1);
        }
        jacademicyear.setSelectedItem(Globals.academicYear());
        jterm.setSelectedItem(Globals.currentTermName());

        if (jstream1.getItemCount() < 3) {
            jstream1.setSelectedIndex(1);
        }
        jacademicyear1.setSelectedItem(Globals.academicYear());
        jterm1.setSelectedItem(Globals.currentTermName());
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

                jexamcode.setSelectedItem(excode);

            }
            jexamcode.addActionListener(this);
        } else if (obj == jterm1) {
            if (jterm1.getSelectedIndex() > 0) {
                tm1 = jterm1.getSelectedItem().toString().toString();
            }
        } else if (obj == jclass1) {
            if (jclass1.getSelectedIndex() > 0) {
                cl1 = jclass1.getSelectedItem().toString();
            }
        } else if (obj == jacademicyear1) {
            if (jacademicyear1.getSelectedIndex() > 0) {
                yr1 = jacademicyear1.getSelectedItem().toString();
            }
        } else if (obj == jexame1) {
            jexamcode1.removeActionListener(this);
            if (jexame1.getSelectedIndex() > 0) {
                ex1 = jexame1.getSelectedItem().toString();

                String excode = ExamCodesGenerator.generatecode(cl1, yr1, tm1, ex1).toUpperCase();

                jexamcode1.setSelectedItem(excode);

            }
            jexamcode1.addActionListener(this);
        } else if (obj == meanimprovement) {

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
                            JOptionPane.showMessageDialog(this, "Kindly Select The Exam Code");
                        } else {
                            if (jexame.getSelectedIndex() == 0) {
                                JOptionPane.showMessageDialog(this, "Kindly Select The Exam Name");
                            } else {


                                if (jclass.getSelectedIndex() == 0) {
                                    JOptionPane.showMessageDialog(this, "Kindly Select The Class For The Second Exam");
                                } else {
                                    if (jterm.getSelectedIndex() == 0) {
                                        JOptionPane.showMessageDialog(this, "Kindly Select The Term For The Second Exam");
                                    } else {
                                        if (jacademicyear.getSelectedIndex() == 0) {
                                            JOptionPane.showMessageDialog(this, "Select The Academic Year For The Second Exam");
                                        } else {
                                            if (jexamcode.getSelectedIndex() == 0) {
                                                JOptionPane.showMessageDialog(this, "Kindly Select The Exam Code For The Second Exam");
                                            } else {
                                                if (jexame.getSelectedIndex() == 0) {
                                                    JOptionPane.showMessageDialog(this, "Kindly Select The Exam Name For The Second Exam");
                                                } else {

                                                    String examCode = jexamcode.getSelectedItem().toString(), examName = jexame.getSelectedItem().toString(), term = jterm.getSelectedItem().toString(), class1 = jclass.getSelectedItem().toString(), academicYear = jacademicyear.getSelectedItem().toString();
                                                    String examCode1 = jexamcode1.getSelectedItem().toString(), examName1 = jexame1.getSelectedItem().toString(), term1 = jterm1.getSelectedItem().toString(), class11 = jclass1.getSelectedItem().toString(), academicYear1 = jacademicyear1.getSelectedItem().toString();


                                                    if (analyse.isSelected()) {
                                                        new Thread() {
                                                            public void run() {
                                                                int limit = 0;
                                                                if (jnumber.getText().isEmpty()) {
                                                                    limit = 0;
                                                                } else {
                                                                    limit = Integer.parseInt(jnumber.getText());
                                                                }
                                                                bar.setVisible(true);
                                                                BestStudentOverallReport.mostImprovedArithmetics(examCode, examName, academicYear, class1, term, examCode1, examName1, academicYear1, class11, term1);
                                                                BestStudentOverallReport.improvedStudentsReport(examCode, examName, academicYear, class1, term, examCode1, examName1, academicYear1, class11, term1, jstream1.getSelectedItem().toString(), jclass1.getSelectedItem().toString(), limit);
                                                                bar.setVisible(false);
                                                            }


                                                        }.start();

                                                    } else {
                                                        bar.setVisible(true);
                                                        int limit = 0;
                                                        if (jnumber.getText().isEmpty()) {
                                                            limit = 0;
                                                        } else {
                                                            limit = Integer.parseInt(jnumber.getText());
                                                        }
                                                        BestStudentOverallReport.improvedStudentsReport(examCode, examName, academicYear, class1, term, examCode1, examName1, academicYear1, class11, term1, jstream1.getSelectedItem().toString(), jclass1.getSelectedItem().toString(), limit);
                                                        bar.setVisible(false);
                                                    }


                                                }
                                            }
                                        }
                                    }
                                }


                            }
                        }
                    }
                }
            }


        }


    }

    public static void main(String[] args) {
        new BestImproved();
    }

}
