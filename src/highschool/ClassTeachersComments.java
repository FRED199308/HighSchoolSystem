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
import javax.swing.JScrollPane;
import javax.swing.JTable;

import static javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE;

import javax.swing.table.DefaultTableModel;

/**
 * @author FRED
 */
public class ClassTeachersComments extends JFrame implements ActionListener {

    private Connection con = DbConnection.connectDb();
    private int width = 800;
    private int height = 550;
    private PreparedStatement ps;
    private ResultSet rs;

    private DefaultTableModel model = new DefaultTableModel();
    private Object cols[] = {"CLASS", "Grade", "Comments"};
    private Object row[] = new Object[cols.length];
    private JTable tab = new JTable();
    private JScrollPane pane = new JScrollPane(tab);
    private FredButton save = new FredButton("Save");
    private FredCombo jclass = new FredCombo("Select Class");
    private FredCombo jgrade = new FredCombo("Select Grade");
    private FredLabel class1 = new FredLabel("Class");
    private FredLabel grade = new FredLabel("Grade");
    private FredButton cancel = new FredButton("Close");

    public ClassTeachersComments() {
        tab.setModel(model);
        model.setColumnIdentifiers(cols);
        setSize(width, height);
        setTitle("Report Forms Teacher's  Comments");
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
        class1.setBounds(30, 30, 150, 30);
        add(class1);
        jclass.setBounds(200, 30, 200, 30);
        add(jclass);
        grade.setBounds(450, 30, 100, 30);
        add(grade);
        jgrade.setBounds(550, 30, 150, 30);
        add(jgrade);
        save.setBounds(30, 100, 130, 30);
        add(save);
        cancel.setBounds(400, 100, 130, 30);
        add(cancel);
        pane.setBounds(30, 150, 700, 330);
        add(pane);
        try {
            String sql = "Select * from classes where precision1<5 order by precision1 asc";
            con = DbConnection.connectDb();
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                jclass.addItem(rs.getString("ClassName"));

            }

            String sql2 = "Select * from points_for_each_grade";
            ps = con.prepareStatement(sql2);
            rs = ps.executeQuery();
            while (rs.next()) {
                jgrade.addItem(rs.getString("Grade"));
            }
            String sql3 = "SElect * from teachersgeneralcomments";
            ps = con.prepareStatement(sql3);
            rs = ps.executeQuery();
            while (rs.next()) {
                row[0] = Globals.className(rs.getString("Classcode"));
                row[1] = rs.getString("grade");
                row[2] = rs.getString("comments");
                model.addRow(row);
            }


        } catch (Exception sq) {
            sq.printStackTrace();
            JOptionPane.showMessageDialog(null, sq.getMessage());
        }

        save.addActionListener(this);
        cancel.addActionListener(this);
        jclass.addActionListener(this);
        jgrade.addActionListener(this);
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
        }
        if (obj == jclass) {
            while (model.getRowCount() > 0) {
                model.removeRow(0);
            }
            try {

                String sql3 = "SElect * from teachersgeneralcomments where classcode='" + Globals.classCode(jclass.getSelectedItem().toString()) + "'";
                ps = con.prepareStatement(sql3);
                rs = ps.executeQuery();
                while (rs.next()) {
                    row[0] = Globals.className(rs.getString("Classcode"));
                    row[1] = rs.getString("grade");
                    row[2] = rs.getString("comments");
                    model.addRow(row);
                }
            } catch (Exception sq) {
                sq.printStackTrace();
                JOptionPane.showMessageDialog(null, sq.getMessage());
            }


        } else if (obj == jgrade) {
            while (model.getRowCount() > 0) {
                model.removeRow(0);
            }

            if (jclass.getSelectedIndex() == 0) {
                try {
                    String sql3 = "SElect * from teachersgeneralcomments where grade='" + jgrade.getSelectedItem() + "'";
                    ps = con.prepareStatement(sql3);
                    rs = ps.executeQuery();
                    while (rs.next()) {
                        row[0] = Globals.className(rs.getString("Classcode"));
                        row[1] = rs.getString("grade");
                        row[2] = rs.getString("comments");
                        model.addRow(row);
                    }
                } catch (Exception sq) {
                    sq.printStackTrace();
                    JOptionPane.showMessageDialog(null, sq.getMessage());
                }
            } else {
                try {
                    String sql3 = "SElect * from teachersgeneralcomments where classcode='" + Globals.classCode(jclass.getSelectedItem().toString()) + "' and grade='" + jgrade.getSelectedItem() + "'";
                    ps = con.prepareStatement(sql3);
                    rs = ps.executeQuery();
                    while (rs.next()) {
                        row[0] = Globals.className(rs.getString("Classcode"));
                        row[1] = rs.getString("grade");
                        row[2] = rs.getString("comments");
                        model.addRow(row);
                    }
                } catch (Exception sq) {
                    sq.printStackTrace();
                    JOptionPane.showMessageDialog(null, sq.getMessage());
                }
            }
        } else if (obj == save) {
            if (tab.getSelectedRowCount() < 1) {
                JOptionPane.showMessageDialog(this, "Kindly Select The class Grade Combination To Edit Its Commment from The Table Below And Edit To Save");
            } else {
                int Selected = tab.getSelectedRow();
                String comment = model.getValueAt(Selected, 2).toString();
                String grade = model.getValueAt(Selected, 1).toString();
                String clscode = Globals.subjectCode(model.getValueAt(Selected, 0).toString());
                try {
                    String sql = "UPdate teachersgeneralcomments set comments='" + comment + "' where grade='" + grade + "' and classcode='" + clscode + "'";
                    ps = con.prepareStatement(sql);
                    ps.execute();
                    tab.requestFocus();
                    JOptionPane.showMessageDialog(this, "New Comment Affected");
                    ;
                } catch (Exception sq) {
                    sq.printStackTrace();
                    JOptionPane.showMessageDialog(null, sq.getMessage());
                }
            }

        }

    }

    public static void main(String[] args) {
        new ClassTeachersComments();
    }
}
