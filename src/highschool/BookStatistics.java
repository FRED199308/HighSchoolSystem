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
import javax.swing.JPanel;

import static javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE;

import javax.swing.border.TitledBorder;

/**
 * @author ExamSeverPc
 */
public class BookStatistics extends JFrame implements ActionListener {

    private int width = 800;
    private int height = 450;
    private Connection con = DbConnection.connectDb();
    private ResultSet rs;
    private PreparedStatement ps;
    private JPanel pane1 = new JPanel();
    private JPanel pane2 = new JPanel();
    private FredButton gen = new FredButton("Generate Satistics");
    private FredButton cancel = new FredButton("Close");
    private FredLabel subject = new FredLabel("By Subject");
    private FredLabel subject1 = new FredLabel("By Subject");
    private FredLabel bookType = new FredLabel("By Type");
    private FredLabel condition = new FredLabel("By Condition");
    private FredLabel classs = new FredLabel("By Class");

    private FredCombo jsubject = new FredCombo("Select Subject");
    private FredCombo jsubject1 = new FredCombo("Select Subject");
    private FredCombo jbookType = new FredCombo("Select Type");
    private FredCombo jcondition = new FredCombo("Choose Condition");
    private FredCombo jclasss = new FredCombo("Select Class");

    public BookStatistics() {

        setSize(width, height);
        setTitle("Book Statistics Generation Panel");
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
        pane1.setBorder(new TitledBorder("Book Categorisation"));
        pane2.setBorder(new TitledBorder("Student Categorisation"));
        pane1.setBounds(20, 10, 400, 300);
        add(pane1);
        pane2.setBounds(420, 10, 350, 300);
        add(pane2);
        pane1.setLayout(null);
        pane2.setLayout(null);
        cancel.setBounds(150, 350, 150, 50);
        add(cancel);
        gen.setBounds(500, 350, 180, 50);
        add(gen);
        subject.setBounds(30, 30, 100, 30);
        pane1.add(subject);
        jsubject.setBounds(200, 30, 150, 30);
        pane1.add(jsubject);

        bookType.setBounds(30, 130, 100, 30);
        pane1.add(bookType);
        jbookType.setBounds(200, 130, 150, 30);
        pane1.add(jbookType);

        condition.setBounds(30, 230, 100, 30);
        pane1.add(condition);
        jcondition.setBounds(200, 230, 150, 30);
        pane1.add(jcondition);

        classs.setBounds(30, 80, 100, 30);
        pane2.add(classs);
        jclasss.setBounds(150, 80, 150, 30);
        pane2.add(jclasss);
        subject1.setBounds(30, 180, 100, 30);
        pane2.add(subject1);
        jsubject1.setBounds(150, 180, 150, 30);
        pane2.add(jsubject1);

        try {
            String sql2 = "Select * from subjects";
            ps = con.prepareStatement(sql2);
            rs = ps.executeQuery();
            while (rs.next()) {
                jsubject.addItem(rs.getString("SubjectName"));
                jsubject1.addItem(rs.getString("SubjectName"));
            }
            String sql = "Select * from classes  where  precision1<5 order by precision1 asc";
            con = DbConnection.connectDb();
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                jclasss.addItem(rs.getString("ClassName"));

            }

        } catch (Exception sq) {
            sq.printStackTrace();

        }
        jcondition.addItem("Perfect");
        jcondition.addItem("Good");
        jcondition.addItem("Worn Out");
        jbookType.addItem("Course Book");
        jbookType.addItem("Revision Book");
        jbookType.addItem("Novel Book");
        jbookType.addItem("Atlas Book");
        jbookType.addItem("Others");

        setVisible(true);
        cancel.addActionListener(this);
        gen.addActionListener(this);

    }

    public static void main(String[] args) {
        new BookStatistics();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object obj = e.getSource();
        if (obj == cancel) {
            CurrentFrame.currentWindow();
            dispose();
        } else if (obj == gen) {
            if (jsubject.getSelectedIndex() == 0 && jbookType.getSelectedIndex() == 0 && jcondition.getSelectedIndex() == 0) {
                JOptionPane.showMessageDialog(this, "You Must Select Atleast One Of The Book Categorisation Field To Generate The Book Statistics");
            } else {
                if (jclasss.getSelectedIndex() == 0 && jsubject1.getSelectedIndex() == 0) {
                    JOptionPane.showMessageDialog(this, "You Must Select Atleast One Of The Student Categorisation Field To Generate The Book Statistics");
                } else {


                    ReportGenerator.bookStatistics(jsubject.getSelectedItem().toString(), jbookType.getSelectedItem().toString(), jcondition.getSelectedItem().toString(), jclasss.getSelectedItem().toString(), jsubject1.getSelectedItem().toString());

                }

            }

        }
    }
}
