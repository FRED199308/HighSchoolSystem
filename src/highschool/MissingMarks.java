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

import static javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE;

/**
 * @author FRED
 */
public class MissingMarks extends JFrame implements ActionListener {

    private Connection con = DbConnection.connectDb();
    private int width = 750;
    private int height = 400;
    private PreparedStatement ps;
    private ResultSet rs;
    private FredButton view = new FredButton("View Report");
    private FredButton cancel = new FredButton("Close");
    private FredButton scoreSheet = new FredButton("Generete Empty Score Sheet");
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
    String tm, cl, yr, ex;

    public MissingMarks() {


        setSize(width, height);
        setTitle("View Overall Missing Marks");
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
                e.getWindow().dispose();
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
        cancel.setBounds(70, 250, 130, 50);
        add(cancel);
        scoreSheet.setBounds(280, 250, 220, 50);
        add(scoreSheet);
        view.setBounds(580, 250, 130, 50);
        add(view);
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
        jterm.addActionListener(this);
        jclass.addActionListener(this);
        jexame.addActionListener(this);
        jacademicyear.addActionListener(this);
        view.addActionListener(this);
        cancel.addActionListener(this);
        scoreSheet.addActionListener(this);
        if (jstream.getItemCount() < 3) {
            jstream.setSelectedIndex(1);
        }
        jacademicyear.setSelectedItem(Globals.academicYear());
        jterm.setSelectedItem(Globals.currentTermName());

    }


    public static void main(String[] args) {
        new MissingMarks();
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
        } else if (obj == scoreSheet) {

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
                                ReportGenerator.emptyScoreSheet(jclass.getSelectedItem().toString(), jacademicyear.getSelectedItem().toString(), jstream.getSelectedItem().toString(), jexamcode.getSelectedItem().toString(), jexame.getSelectedItem().toString(), jterm.getSelectedItem().toString());


                            }
                        }
                    }
                }
            }


        } else if (obj == view) {
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

                                ReportGenerator.missingResultsOverall(jclass.getSelectedItem().toString(), jacademicyear.getSelectedItem().toString(), jstream.getSelectedItem().toString(), jexamcode.getSelectedItem().toString(), jexame.getSelectedItem().toString(), jterm.getSelectedItem().toString());

                            }
                        }
                    }
                }
            }
        }
    }


}
