/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package highschool;


import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import static javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE;

/**
 * @author FREDDY
 */
public class FeeRegister extends JFrame implements ActionListener {
    private Connection con = DbConnection.connectDb();
    private int width = 750;
    private int height = 350;
    private PreparedStatement ps;
    private ResultSet rs;
    private FredButton view = new FredButton("Generate");
    private FredButton cancel = new FredButton("Close");
    private FredLabel number = new FredLabel(" Fetch Best :");
    private FredTextField jnumber = new FredTextField("3");
    private FredCombo jclass = new FredCombo("Select Class");
    private FredLabel name = new FredLabel("Class");
    private FredLabel examname = new FredLabel("Exam Name");
    private FredCombo jvotehead = new FredCombo("Leave This Unselected To View All");
    private FredLabel votehead = new FredLabel("Vote Head");


    private FredLabel stream = new FredLabel("Stream");
    private FredCombo jstream = new FredCombo("Select Stream");
    private FredCombo jterm = new FredCombo("Select Term");
    private FredLabel term = new FredLabel("Term");
    private FredCombo jexame = new FredCombo("Select Exam");
    private FredLabel academicyear = new FredLabel("Academic Year");
    private FredCombo jacademicyear = new FredCombo("Select Academic Year");

    public FeeRegister() {

        cancel = new FredButton("Close");
        setSize(width, height);
        setLocationRelativeTo(null);
        setLayout(null);
        setTitle("Fee Register Generation Panel");

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

        name.setBounds(30, 30, 150, 30);
        add(name);
        jclass.setBounds(150, 30, 200, 30);
        add(jclass);
        academicyear.setBounds(400, 30, 150, 30);
        add(academicyear);
        jacademicyear.setBounds(500, 30, 200, 30);
        add(jacademicyear);

        votehead.setBounds(150, 130, 150, 30);
        add(votehead);
        jvotehead.setBounds(350, 130, 200, 30);
        add(jvotehead);


        cancel.setBounds(100, 200, 130, 70);
        add(cancel);

        view.setBounds(400, 200, 200, 70);
        add(view);

        try {

            for (int k = 2015; k <= Globals.academicYear(); ++k) {
                jacademicyear.addItem(k);
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
            ps = con.prepareStatement("Select VoteheadName from voteheads where payableasfee='" + "1" + "'");
            rs = ps.executeQuery();
            while (rs.next()) {
                jvotehead.addItem(rs.getString("VoteheadName"));
            }


        } catch (Exception sq) {
            sq.printStackTrace();
            JOptionPane.showMessageDialog(null, sq.getMessage());
        }

        setVisible(true);

        view.addActionListener(this);
        cancel.addActionListener(this);
    }


    @Override
    public void actionPerformed(ActionEvent e) {

        Object obj = e.getSource();
        if (obj == view) {


            if (jclass.getSelectedIndex() == 0) {
                JOptionPane.showMessageDialog(this, "Kindly Select The Class");
            } else {

                if (jacademicyear.getSelectedIndex() == 0) {
                    JOptionPane.showMessageDialog(this, "Select The Year");
                }
                String vhead = null;
                if (jvotehead.getSelectedIndex() == 0) {
                    vhead = null;
                } else {
                    vhead = jvotehead.getSelectedItem().toString();


                }
                ReportGenerator.feeRegister(jclass.getSelectedItem().toString(), jacademicyear.getSelectedItem().toString(), jterm.getSelectedItem().toString(), vhead);

            }


        } else if (obj == cancel) {
            CurrentFrame.currentWindow();
            this.dispose();
        }


    }

    public static void main(String[] args) {
        new FeeRegister();
    }

}
