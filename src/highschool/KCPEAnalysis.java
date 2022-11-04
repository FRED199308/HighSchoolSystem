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
import javax.swing.JProgressBar;
import javax.swing.JWindow;

import static javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE;

import javax.swing.border.TitledBorder;

/**
 * @author FREDDY
 */
public class KCPEAnalysis extends JFrame implements ActionListener {


    private Connection con = DbConnection.connectDb();
    private int width = 620;
    private int height = 330;
    private PreparedStatement ps;
    private ResultSet rs;
    private FredLabel class1, year;
    private FredCombo jclass1, jacademicYear;
    private FredButton cancel, analyse, report;

    public KCPEAnalysis() {


        setSize(width, height);
        setTitle("K.C.P.E Analysis");
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

        report = new FredButton("Show Report");
        analyse = new FredButton("Analyse");
        cancel = new FredButton("Close");
        year = new FredLabel("Class Of");
        class1 = new FredLabel("Class");
        jclass1 = new FredCombo("Select Class");
        jacademicYear = new FredCombo();

        analyse.addActionListener(this);
        cancel.addActionListener(this);
        report.addActionListener(this);

        class1.setBounds(30, 10, 200, 30);
        jclass1.setBounds(330, 10, 200, 30);
        add(class1);
        add(jclass1);
        //year.setBounds(30,80,200,30);jacademicYear.setBounds(330,80,200,30);add(year);add(jacademicYear);

        cancel.setBounds(20, 180, 150, 50);
        analyse.setBounds(200, 180, 150, 50);
        report.setBounds(400, 180, 150, 50);
        add(cancel);
        add(analyse);
        add(report);
        try {
            String sql = "Select * from classes where precision1<5 order by precision1 asc";
            con = DbConnection.connectDb();
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                jclass1.addItem(rs.getString("ClassName"));

            }
            for (int k = 2015; k <= Globals.academicYear(); ++k) {
                jacademicYear.addItem(k);
            }
            jacademicYear.setSelectedItem(Globals.academicYear());

        } catch (Exception sq) {
            sq.printStackTrace();
        }


        setVisible(true);
    }

    public static void main(String[] args) {
        new KCPEAnalysis();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object obj = e.getSource();
        if (obj == report) {

            if (jclass1.getSelectedIndex() == 0) {
                JOptionPane.showMessageDialog(this, "Select Class");
            } else {
                ReportGenerator.kcperesults(jclass1.getSelectedItem().toString());
            }


        } else if (obj == cancel) {
            CurrentFrame.currentWindow();
            this.dispose();
        } else if (obj == analyse) {

            if (jclass1.getSelectedIndex() == 0) {
                JOptionPane.showMessageDialog(this, "Select Class");
            } else {

                new Thread() {
                    @Override
                    public void run() {
                        JWindow dia = new JWindow();
                        JProgressBar bar = new JProgressBar();
                        bar.setIndeterminate(true);

                        dia.setSize(300, 60);

                        bar.setBorder(new TitledBorder("Please Wait... processing Request...."));
                        dia.setAlwaysOnTop(true);
                        dia.setLocationRelativeTo(CurrentFrame.mainFrame());
                        dia.setIconImage(FrameProperties.icon());
                        dia.add(bar);
                        dia.setVisible(true);

                        String classcode = Globals.classCode(jclass1.getSelectedItem().toString());
                        Globals.kcpeAnalysis(classcode);


                        dia.dispose();
                        JOptionPane.showMessageDialog(null, "Operation Completed");
                    }

                }.start();


            }


        }
    }

}
