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
import java.util.jar.Attributes;
import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JFrame;

import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.JTable;

import static javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE;

import javax.swing.table.DefaultTableModel;

/**
 * @author FRED
 */
public class SubjectClassList extends JFrame implements ActionListener {


    private int height = 430;
    private int width = 700;
    private FredButton cancel, print;
    private FredLabel classname, stream, gender;
    private FredCombo jclassname, jstream, jgender;
    private Connection con;
    PreparedStatement ps;
    private FredLabel academicyear = new FredLabel("Academic Year");
    private FredCombo jacademicyear = new FredCombo("Select Academic Year");
    private FredCombo jsubject = new FredCombo("Leave This To View All Subjects");
    private FredLabel subject = new FredLabel("Subject");
    ResultSet rs;

    public SubjectClassList() {
        setSize(width, height);
        setLocationRelativeTo(null);
        setLayout(null);

        setResizable(false);
        setTitle("Class List Generation Panel");

        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        this.addWindowListener(new WindowAdapter() {

            @Override
            public void windowClosing(WindowEvent e) {
                CurrentFrame.currentWindow();
                e.getWindow().dispose();
            }
        });
        con = DbConnection.connectDb();
        cancel = new FredButton("Close");

        print = new FredButton("Generate ClassList");
        classname = new FredLabel("Class");
        stream = new FredLabel("Stream");
        jclassname = new FredCombo("Select Form");
        jstream = new FredCombo("Select Stream");
        gender = new FredLabel("Separate Girls From Boys");
        jgender = new FredCombo("Sort By Gender");
        this.setIconImage(FrameProperties.icon());
        ((JComponent) getContentPane()).setBorder(
                BorderFactory.createMatteBorder(5, 5, 5, 5, Color.MAGENTA));

        classname.setBounds(30, 70, 150, 30);
        add(classname);
        jclassname.setBounds(300, 70, 300, 30);
        add(jclassname);
        stream.setBounds(30, 170, 150, 30);
        add(stream);
        jstream.setBounds(300, 170, 300, 30);
        add(jstream);

        subject.setBounds(30, 250, 150, 30);
        add(subject);
        jsubject.setBounds(300, 250, 300, 30);
        add(jsubject);
        cancel.setBounds(100, 320, 150, 30);
        add(cancel);
        print.setBounds(400, 320, 200, 30);
        add(print);
        try {
            for (int k = 2015; k <= Globals.academicYear(); ++k) {
                jacademicyear.addItem(k);
            }
            String sql = "Select * from classes where precision1<5";
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                jclassname.addItem(rs.getString("Classname"));
            }
            String sql2 = "Select Streamname from streams ";
            ps = con.prepareStatement(sql2);
            rs = ps.executeQuery();
            while (rs.next()) {
                jstream.addItem(rs.getString("StreamName"));
            }
            String sqll = "Select * from subjects";
            ps = con.prepareStatement(sqll);
            rs = ps.executeQuery();
            while (rs.next()) {
                jsubject.addItem(rs.getString("SubjectName"));
            }
        } catch (SQLException sq) {
            JOptionPane.showMessageDialog(this, sq.getMessage());
        }
        print.addActionListener(this);
        cancel.addActionListener(this);
        if (jstream.getItemCount() < 3) {
            jstream.setSelectedIndex(1);
        }
        setVisible(true);

    }

    public static void main(String[] args) {
        new SubjectClassList();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object obj = e.getSource();

        if (obj == print) {
            if (jclassname.getSelectedIndex() == 0) {
                JOptionPane.showMessageDialog(this, "Kindly Select The Class name");
            } else {

                if (jsubject.getSelectedIndex() == 0) {
                    JOptionPane.showMessageDialog(this, "Select The Subject");
                } else {
                    String st = "";
                    if (jstream.getSelectedIndex() == 0) {
                        st = "OVERALL";
                    } else {
                        st = jstream.getSelectedItem().toString();
                    }

                    ClassListReport.subjectClassList(Globals.subjectCode(jsubject.getSelectedItem().toString()), st, jclassname.getSelectedItem().toString());
                }

            }

        } else if (obj == cancel) {

            CurrentFrame.currentWindow();
            dispose();
        }


    }

}
