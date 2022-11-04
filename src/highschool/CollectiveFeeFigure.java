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
 * @author FREDDY
 */
public class CollectiveFeeFigure extends JFrame implements ActionListener {

    private Connection con = DbConnection.connectDb();
    private int width = 750;
    private int height = 400;
    private PreparedStatement ps;
    private ResultSet rs;
    private FredButton configure = new FredButton("Open Fee VoteHead Configuration");
    private FredButton view = new FredButton("View");
    private FredButton cancel = new FredButton("Close");

    private FredCombo jclass = new FredCombo("Select Class");
    private FredLabel name = new FredLabel("Class");
    private FredLabel academicyear = new FredLabel("Academic Year");
    private FredCombo jacademicyear = new FredCombo("Select Academic Year");
    private FredCheckBox option1 = new FredCheckBox("Sort By Performance");
    private FredCheckBox option2 = new FredCheckBox("Sort By admission Number");
    private ButtonGroup group = new ButtonGroup();
    private FredCombo jterm = new FredCombo("Select Term");
    private FredCombo jpros = new FredCombo("Select Program");
    private FredLabel pros = new FredLabel("Program");
    private FredLabel term = new FredLabel("Term");

    public CollectiveFeeFigure() {
        group.add(option1);
        group.add(option2);
        option1.setSelected(true);
        setSize(width, height);
        setTitle("Collective Fee Figure Window");
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
        pros.setBounds(400, 30, 150, 30);
        add(pros);
        jpros.setBounds(500, 30, 200, 30);
        add(jpros);
        term.setBounds(30, 100, 150, 30);
        add(term);
        jterm.setBounds(150, 100, 200, 30);
        add(jterm);
        academicyear.setBounds(400, 100, 150, 30);
        add(academicyear);
        jacademicyear.setBounds(500, 100, 200, 30);
        add(jacademicyear);

        cancel.setBounds(100, 270, 130, 50);
        add(cancel);
        view.setBounds(300, 270, 150, 50);
        add(view);
        configure.setBounds(500, 270, 230, 50);
        add(configure);
        try {
            int programconter = 0;
            String qeurry = "Select programname from programs";
            ps = con.prepareStatement(qeurry);
            rs = ps.executeQuery();
            while (rs.next()) {
                programconter++;
                jpros.addItem(rs.getString("Programname"));
            }

            if (programconter == 1) {
                jpros.setSelectedIndex(1);
            }
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
        } catch (Exception sq) {
            sq.printStackTrace();
            JOptionPane.showMessageDialog(null, sq.getMessage());
        }
        jacademicyear.setSelectedItem(Globals.academicYear());
        jterm.setSelectedItem(Globals.currentTermName());
        setVisible(true);
        jterm.addActionListener(this);
        jclass.addActionListener(this);

        jacademicyear.addActionListener(this);
        view.addActionListener(this);
        cancel.addActionListener(this);
        configure.addActionListener(this);
        jacademicyear.setSelectedItem(Globals.academicYear());
        jterm.setSelectedItem(Globals.currentTermName());
        setVisible(true);

    }

    public static void main(String[] args) {
        new CollectiveFeeFigure();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object obj = e.getSource();
        if (obj == cancel) {
            CurrentFrame.currentWindow();
            dispose();
        } else if (obj == view) {

            if (jclass.getSelectedIndex() == 0) {
                JOptionPane.showMessageDialog(this, "Select Class");
            } else {
                if (jpros.getSelectedIndex() == 0) {
                    JOptionPane.showMessageDialog(this, "Select Program");
                } else {
                    if (jterm.getSelectedIndex() == 0) {
                        JOptionPane.showMessageDialog(this, "Select Term");
                    } else {
                        if (jacademicyear.getSelectedIndex() == 0) {
                            JOptionPane.showMessageDialog(this, "Select Year");
                        } else {

                            CollectivefeefigureReport.feeAmount(jacademicyear.getSelectedItem().toString(), jterm.getSelectedItem().toString(), jclass.getSelectedItem().toString(), jpros.getSelectedItem().toString());

                        }
                    }
                }
            }

        } else if (obj == configure) {
            dispose();
            CurrentFrame.setSecondFrame(new VoteHeadFeeConfiguration());
        }
    }

}
