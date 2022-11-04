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
import javax.swing.JComponent;
import javax.swing.JFrame;

import javax.swing.JOptionPane;

import static javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE;

/**
 * @author FRED
 */
public class StudentSubjectAllocation extends JFrame implements ActionListener {

    private Connection con = DbConnection.connectDb();
    private int width = 700;
    private int height = 400;
    private PreparedStatement ps;
    private ResultSet rs;
    private FredButton singleAlocator = new FredButton("Single Student Allocation");
    private FredLabel classname = new FredLabel("Class Name");
    private FredLabel admnumber = new FredLabel("admission Number");
    private FredLabel stream = new FredLabel("Stream Name");
    private FredLabel subject = new FredLabel("Subject Name");
    private FredLabel academicyear = new FredLabel("Academic Year");
    private FredCombo jacademicyear = new FredCombo("Select Academic Year");
    private FredCombo jclassname = new FredCombo("Select Class");
    private FredCombo jstream = new FredCombo("Select Stream");
    private FredCombo jsubject = new FredCombo("Select Subject");
    private FredTextField jadmnumber = new FredTextField();
    private FredButton byaclass = new FredButton("Allocate By Class");
    private FredButton bystream = new FredButton("Allocate By Stream");
    private FredButton byadm = new FredButton("Allocate By admission Number");
    private FredButton report = new FredButton("View Report");
    private FredButton deallocate = new FredButton("Deallocate");

    public StudentSubjectAllocation() {
        setSize(width, height);
        setTitle("Student Subject Allocation Panel");
        ((JComponent) getContentPane()).setBorder(BorderFactory.createMatteBorder(5, 5, 5, 5, Color.MAGENTA));

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

        subject.setBounds(30, 30, 130, 30);
        add(subject);
        jsubject.setBounds(200, 30, 130, 30);
        add(jsubject);
        classname.setBounds(400, 30, 130, 30);
        add(classname);
        jclassname.setBounds(500, 30, 130, 30);
        add(jclassname);
        stream.setBounds(30, 100, 130, 30);
        add(stream);
        jstream.setBounds(200, 100, 130, 30);
        add(jstream);
        academicyear.setBounds(400, 100, 130, 30);
        add(academicyear);
        jacademicyear.setBounds(500, 100, 130, 30);
        add(jacademicyear);
        admnumber.setBounds(30, 170, 130, 30);
        add(admnumber);
        jadmnumber.setBounds(200, 170, 130, 30);
        add(jadmnumber);
        byadm.setBounds(400, 170, 230, 50);
        add(byadm);
        byaclass.setBounds(30, 250, 180, 50);
        add(byaclass);
        bystream.setBounds(230, 250, 180, 50);
        add(bystream);
        report.setBounds(450, 250, 180, 50);
        add(report);
        deallocate.setBounds(450, 320, 180, 30);
        add(deallocate);
        singleAlocator.setBounds(30, 320, 180, 30);
        add(singleAlocator);
        try {

            String sqll = "Select * from subjects";
            ps = con.prepareStatement(sqll);
            rs = ps.executeQuery();
            while (rs.next()) {
                jsubject.addItem(rs.getString("SubjectName"));
            }
            for (int k = 2015; k <= Globals.academicYear(); ++k) {
                jacademicyear.addItem(k);
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
                jclassname.addItem(rs.getString("ClassName"));

            }

        } catch (Exception e) {
        }

        byaclass.addActionListener(this);
        bystream.addActionListener(this);
        byadm.addActionListener(this);
        deallocate.addActionListener(this);
        report.addActionListener(this);
        singleAlocator.addActionListener(this);
        jacademicyear.setSelectedItem(Globals.academicYear());
        if (jstream.getItemCount() < 3) {
            jstream.setSelectedIndex(1);
        }

        jadmnumber.addKeyListener(new KeyAdapter() {

            public void keyReleased(KeyEvent key) {
                int c = key.getKeyCode();
                if (KeyEvent.VK_ENTER == c && !jadmnumber.getText().isEmpty()) {
                    byadm.doClick();
                }
            }
        });

        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        Object obj = e.getSource();
        if (obj == byaclass) {
            if (jsubject.getSelectedIndex() == 0) {
                JOptionPane.showMessageDialog(this, "Select Subject To Allocate");
            } else {
                if (jacademicyear.getSelectedIndex() == 0) {
                    JOptionPane.showMessageDialog(this, "Select The Academic Year");

                } else {

                    String classcode = Globals.classCode(jclassname.getSelectedItem().toString());
                    String subjectcode = Globals.subjectCode(jsubject.getSelectedItem().toString());
                    System.err.println(classcode);
                    try {

                        String sql = "Select admissionnumber from admission where  currentform='" + classcode + "'";
                        ps = con.prepareStatement(sql);
                        rs = ps.executeQuery();
                        while (rs.next()) {
                            System.err.println("hhh");
                            String adm = rs.getString("AdmissionNumber");
                            String sql2 = "Select * from studentsubjectallocation where admnumber='" + adm + "' and academicyear='" + jacademicyear.getSelectedItem() + "' and subjectcode='" + subjectcode + "'";
                            ps = con.prepareStatement(sql2);
                            ResultSet RS = ps.executeQuery();
                            if (RS.next()) {
                                JOptionPane.showMessageDialog(this, Globals.fullName(adm) + " Was Previously Allocated This Subject In This Academic Year");
                            } else {
                                String sql3 = "Insert into studentsubjectallocation values('" + subjectcode + "','" + adm + "','" + jacademicyear.getSelectedItem() + "')";
                                ps = con.prepareStatement(sql3);
                                ps.execute();
                            }
                        }
                        JOptionPane.showMessageDialog(this, "The Whole Class Assigned To The Subject Successfully");

                    } catch (Exception sq) {
                        sq.printStackTrace();
                        JOptionPane.showMessageDialog(null, sq.getMessage());
                    }
                }
            }
        } else if (obj == bystream) {
            if (jsubject.getSelectedIndex() == 0) {
                JOptionPane.showMessageDialog(this, "Select Subject To Allocate");
            } else {
                if (jacademicyear.getSelectedIndex() == 0) {
                    JOptionPane.showMessageDialog(this, "Select The Academic Year");

                } else {
                    if (jstream.getSelectedIndex() == 0) {
                        JOptionPane.showMessageDialog(this, "Select Stream");
                    } else {

                        String streamcode = Globals.streamcode(jstream.getSelectedItem().toString());
                        String classcode = Globals.classCode(jclassname.getSelectedItem().toString());
                        String subjectcode = Globals.subjectCode(jsubject.getSelectedItem().toString());
                        System.err.println(classcode);
                        try {

                            String sql = "Select admissionnumber from admission where  currentform='" + classcode + "' and currentstream='" + streamcode + "'";
                            ps = con.prepareStatement(sql);
                            rs = ps.executeQuery();
                            while (rs.next()) {

                                String adm = rs.getString("AdmissionNumber");
                                String sql2 = "Select * from studentsubjectallocation where academicyear='" + jacademicyear.getSelectedItem() + "' and subjectcode='" + subjectcode + "' and subjectcode='" + subjectcode + "' and admnumber='" + adm + "'";
                                ps = con.prepareStatement(sql2);
                                ResultSet RS = ps.executeQuery();
                                if (RS.next()) {
                                    JOptionPane.showMessageDialog(this, Globals.fullName(adm) + " Was Previously Allocated This Subject In This Academic Year");
                                } else {
                                    String sql3 = "Insert into studentsubjectallocation values('" + subjectcode + "','" + adm + "','" + jacademicyear.getSelectedItem() + "')";
                                    ps = con.prepareStatement(sql3);
                                    ps.execute();
                                }
                            }
                            JOptionPane.showMessageDialog(this, "The Whole Class Assigned To The Subject Successfully");

                        } catch (Exception sq) {
                            sq.printStackTrace();
                            JOptionPane.showMessageDialog(null, sq.getMessage());
                        }
                    }
                }
            }
        } else if (obj == singleAlocator) {
            try {
                if (jacademicyear.getSelectedIndex() == 0) {
                    JOptionPane.showMessageDialog(this, "Select Academic Year");
                } else {


                    if (jadmnumber.getText().isEmpty()) {
                        JOptionPane.showMessageDialog(this, "Input Student admission Number");
                    } else {
                        String sql = "Select admissionnumber from admission where admissionnumber='" + jadmnumber.getText() + "'";
                        ps = con.prepareStatement(sql);
                        rs = ps.executeQuery();
                        if (rs.next()) {

                            Globals.singleAdmissionAllocator = jadmnumber.getText();
                            Globals.singleyearAllocator = jacademicyear.getSelectedItem().toString();
                            CurrentFrame.setSecondFrame(this);
                            CurrentFrame.secFrame().setEnabled(false);

                            new SingleStudentSubjectAssigner();


                        } else {
                            JOptionPane.showMessageDialog(this, "Invalid admission Number");
                        }
                    }
                }

            } catch (Exception sq) {
                sq.printStackTrace();
                JOptionPane.showMessageDialog(null, sq.getMessage());
            }


        } else if (obj == byadm) {
            if (jsubject.getSelectedIndex() == 0) {
                JOptionPane.showMessageDialog(this, "Select Subject");
            } else {
                try {
                    if (jacademicyear.getSelectedIndex() == 0) {
                        JOptionPane.showMessageDialog(this, "Select Academic Year");
                    } else {


                        if (jadmnumber.getText().isEmpty()) {
                            JOptionPane.showMessageDialog(this, "Input Student admission Number");
                        } else {
                            String sql = "Select admissionnumber from admission where admissionnumber='" + jadmnumber.getText() + "'";
                            ps = con.prepareStatement(sql);
                            rs = ps.executeQuery();
                            if (rs.next()) {
                                String subjectcode = Globals.subjectCode(jsubject.getSelectedItem().toString());
                                String adm = jadmnumber.getText();
                                String sql2 = "Select * from studentsubjectallocation where academicyear='" + jacademicyear.getSelectedItem() + "' and subjectcode='" + subjectcode + "' and admnumber='" + adm + "'";
                                ps = con.prepareStatement(sql2);
                                ResultSet RS = ps.executeQuery();
                                if (RS.next()) {
                                    JOptionPane.showMessageDialog(this, Globals.fullName(adm) + " Was Previously Allocated This Subject In This Academic Year");
                                } else {
                                    String sql3 = "Insert into studentsubjectallocation values('" + subjectcode + "','" + adm + "','" + jacademicyear.getSelectedItem() + "')";
                                    ps = con.prepareStatement(sql3);
                                    ps.execute();
                                }
                                jadmnumber.setText("");
                                JOptionPane.showMessageDialog(this, Globals.fullName(adm) + " Has Been Assigned To The Subject");
                            } else {
                                JOptionPane.showMessageDialog(this, "Invalid admission Number");
                            }
                        }
                    }

                } catch (Exception sq) {
                    sq.printStackTrace();
                    JOptionPane.showMessageDialog(null, sq.getMessage());
                }
            }
        } else if (obj == report) {
            if (jsubject.getSelectedIndex() == 0) {
                JOptionPane.showMessageDialog(this, "Select Subject To Allocate");
            } else {
                if (jacademicyear.getSelectedIndex() == 0) {
                    JOptionPane.showMessageDialog(this, "Select The Academic Year");

                } else {
                    if (jstream.getSelectedIndex() == 0) {
                        JOptionPane.showMessageDialog(this, "Select Stream");
                    } else {


                        ReportGenerator.subjectAllocationReport(jclassname.getSelectedItem().toString(), jacademicyear.getSelectedItem().toString(), jstream.getSelectedItem().toString());

                    }
                }
            }
        } else if (obj == deallocate) {

            if (jsubject.getSelectedIndex() == 0) {
                JOptionPane.showMessageDialog(this, "Select Subject To Allocate");
            } else {
                if (jacademicyear.getSelectedIndex() == 0) {
                    JOptionPane.showMessageDialog(this, "Select The Academic Year");

                } else {
                    if (jadmnumber.getText().isEmpty()) {
                        if (jstream.getSelectedIndex() == 0) {
                            JOptionPane.showMessageDialog(this, "Select Stream");
                        } else {

                            String streamcode = Globals.streamcode(jstream.getSelectedItem().toString());
                            String classcode = Globals.classCode(jclassname.getSelectedItem().toString());
                            String subjectcode = Globals.subjectCode(jsubject.getSelectedItem().toString());

                            try {

                                String sql = "Select admissionnumber from admission where  currentform='" + classcode + "' and currentstream='" + streamcode + "'";
                                ps = con.prepareStatement(sql);
                                rs = ps.executeQuery();
                                while (rs.next()) {

                                    String adm = rs.getString("admissionNumber");
                                    String sql2 = "Delete from studentsubjectallocation where subjectcode='" + subjectcode + "' and admnumber='" + adm + "' and academicyear='" + jacademicyear.getSelectedItem() + "'";
                                    ps = con.prepareStatement(sql2);
                                    ps.execute();
                                }
                                JOptionPane.showMessageDialog(this, "The Whole Class Deallocate From The Subject Successfully");

                            } catch (Exception sq) {
                                sq.printStackTrace();
                                JOptionPane.showMessageDialog(null, sq.getMessage());
                            }
                        }
                    } else {

                        try {

                            String sql = "Select admissionnumber from admission where admissionnumber='" + jadmnumber.getText() + "'";
                            ps = con.prepareStatement(sql);
                            rs = ps.executeQuery();
                            if (rs.next()) {


                                String subjectcode = Globals.subjectCode(jsubject.getSelectedItem().toString());
                                String adm = jadmnumber.getText();
                                String sql2 = "Select * from studentsubjectallocation where academicyear='" + jacademicyear.getSelectedItem() + "' and subjectcode='" + subjectcode + "' and admnumber='" + adm + "'";
                                ps = con.prepareStatement(sql2);
                                ResultSet RS = ps.executeQuery();
                                if (RS.next()) {
                                    String sql22 = "Delete from studentsubjectallocation where subjectcode='" + subjectcode + "' and admnumber='" + adm + "' and academicyear='" + jacademicyear.getSelectedItem() + "'";
                                    ps = con.prepareStatement(sql22);
                                    ps.execute();
                                    JOptionPane.showMessageDialog(this, Globals.fullName(adm) + " Has Been Deallocated To The Subject");
                                } else {
                                    JOptionPane.showMessageDialog(this, Globals.fullName(adm) + " Was Not Previously Allocated This Subject In This Academic Year");
                                }
                                jadmnumber.setText("");


                            } else {
                                JOptionPane.showMessageDialog(this, "Invalid admission Number");
                            }


                        } catch (Exception sq) {
                            sq.printStackTrace();
                            JOptionPane.showMessageDialog(null, sq.getMessage());
                        }


                    }


                }
            }

        }

    }

    public static void main(String[] args) {
        new StudentSubjectAllocation();
    }

}
