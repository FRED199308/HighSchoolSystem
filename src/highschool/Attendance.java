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
import java.util.Date;
import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JFrame;

import javax.swing.JOptionPane;

import static javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE;

/**
 * @author EXAMSERVERPC
 */
public class Attendance extends JFrame implements ActionListener {


    private Connection con = DbConnection.connectDb();
    private int width = 750;
    private int height = 480;
    private PreparedStatement ps;
    private ResultSet rs;
    private FredButton view = new FredButton("View Report");
    private FredButton cancel = new FredButton("Close");
    private FredLabel adm = new FredLabel("admission Number");
    private FredLabel studentName = new FredLabel("Student Name");
    private FredTextField jadm = new FredTextField();
    private FredLabel lowerdate = new FredLabel("Start Date");
    private FredLabel upperdate = new FredLabel("Upper Date");
    private FredDateChooser jlower = new FredDateChooser();
    private FredDateChooser jupperdate = new FredDateChooser();
    private FredCheckBox total = new FredCheckBox("Show Totals");
    private FredLabel stream = new FredLabel("Stream");
    private FredCombo jstream = new FredCombo("Select Stream");
    private FredCombo jterm = new FredCombo("Select Term");
    private FredLabel term = new FredLabel("Term");
    private FredCombo jclass = new FredCombo("Select Class");
    private FredLabel name = new FredLabel("Class");

    public Attendance() {

        setSize(width, height);

        setTitle("Attendance Report Pane");
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
        adm.setBounds(30, 30, 150, 30);
        add(adm);
        jadm.setBounds(200, 30, 100, 30);
        add(jadm);
        studentName.setBounds(450, 30, 200, 30);
        add(studentName);
        name.setBounds(30, 100, 150, 30);
        add(name);
        jclass.setBounds(150, 100, 200, 30);
        add(jclass);
        stream.setBounds(430, 100, 150, 30);
        add(stream);
        jstream.setBounds(500, 100, 200, 30);
        add(jstream);
        term.setBounds(30, 170, 150, 30);
        add(term);
        jterm.setBounds(150, 170, 200, 30);
        add(jterm);

        total.setBounds(400, 170, 150, 30);
        add(total);

        lowerdate.setBounds(30, 240, 150, 30);
        add(lowerdate);
        jlower.setBounds(150, 240, 200, 30);
        add(jlower);
        upperdate.setBounds(430, 240, 150, 30);
        add(upperdate);
        jupperdate.setBounds(500, 240, 200, 30);
        add(jupperdate);

        cancel.setBounds(100, 330, 130, 50);
        add(cancel);
        view.setBounds(450, 330, 130, 50);
        add(view);
        total.setSelected(true);
        jlower.setDateFormatString("yyyy/MM/dd");
        jupperdate.setDate(new Date());
        jupperdate.setDateFormatString("yyyy/MM/dd");

        try {
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
            JOptionPane.showMessageDialog(this, sq.getMessage());
        }
        setVisible(true);
        if (jstream.getItemCount() < 3) {
            jstream.setSelectedIndex(1);
        }

        jterm.setSelectedItem(Globals.currentTermName());
        view.addActionListener(this);
        cancel.addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object obj = e.getSource();
        if (obj == cancel) {
            CurrentFrame.currentWindow();
            dispose();
        } else if (obj == view) {
            String ldate = ((FredTextField) jlower.getDateEditor().getUiComponent()).getText();
            String udate = ((FredTextField) jupperdate.getDateEditor().getUiComponent()).getText();
            if (jclass.getSelectedIndex() == 0) {
                JOptionPane.showMessageDialog(this, "Select Class");
            } else {
                if (jstream.getSelectedIndex() == 0) {
                    JOptionPane.showMessageDialog(this, "Select Stream");
                } else {
                    if (jterm.getSelectedIndex() == 0) {
                        JOptionPane.showMessageDialog(this, "Select Term");
                    } else {
                        if (ldate.isEmpty()) {
                            JOptionPane.showMessageDialog(this, "Select The Start Date");
                        } else {
                            if (udate.isEmpty()) {
                                JOptionPane.showMessageDialog(this, "Select Tje Upper date");
                            } else {
                                if (jlower.getDate().before(jupperdate.getDate())) {
                                    JOptionPane.showMessageDialog(this, "The Lower Date Can Never Be Before The Upper Date");
                                } else {


                                }
                            }
                        }
                    }
                }
            }
        }
    }

    public static void main(String[] args) {
        new Attendance();
    }

}
