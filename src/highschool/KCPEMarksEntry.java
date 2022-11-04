/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package highschool;

import java.awt.Color;
import java.awt.Font;
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
import javax.swing.JProgressBar;

import javax.swing.JWindow;

import static javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE;

import javax.swing.border.TitledBorder;

import net.miginfocom.swing.MigLayout;

/**
 * @author FRED
 */
public class KCPEMarksEntry extends JFrame implements ActionListener {
    private int height = 450;
    private int width = 700;
    private ResultSet rs;
    private Connection con = DbConnection.connectDb();
    private PreparedStatement ps;
    private FredLabel index = new FredLabel("K.C.P.E Index Number");
    //  private FredLabel grade=new FredLabel("Grade");
    private FredLabel point = new FredLabel("Marks");
    private FredLabel kcse = new FredLabel("K.C.P.E Year");
    private FredLabel subject = new FredLabel("Subject Name");
    private FredLabel subjectCode = new FredLabel("Subject Code");
    private FredLabel adm = new FredLabel("admission Number");
    private FredLabel studentName = new FredLabel("Student Name");
    private FredCheckBox analyse = new FredCheckBox("Analyse First");
    private FredTextField jschool = new FredTextField();
    private FredLabel school = new FredLabel("Primary School Name");
    private FredTextField jindex = new FredTextField();
    // private FredTextField jgrade=new FredTextField();
    private FredTextField jpoint = new FredTextField();
    private FredTextField jindexsuffix = new FredTextField();
    private FredTextField jadm = new FredTextField();
    boolean analysis;

    private FredCombo jsubject = new FredCombo("Select Subject");
    private FredCombo jsubjectCode = new FredCombo("Select Subject Code");
    private FredCombo jKcpe = new FredCombo("Select K.C.P.E Exam Year");
    private FredButton save = new FredButton("Save");
    private FredButton cancel = new FredButton("Cancel");
    private FredButton result = new FredButton("Show Results");
    private FredButton missing = new FredButton("Show Missing Marks");
    private FredButton delete = new FredButton("Delete");


    public KCPEMarksEntry() {
        setSize(width, height);
        setTitle("K.C.P.E Marks Entry Panel");
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

        index.setBounds(30, 30, 150, 30);
        add(index);
        jindex.setBounds(200, 30, 150, 30);
        add(jindex);
        //  jindexsuffix.setBounds(380,30,50,30);add(jindexsuffix);
        adm.setBounds(450, 30, 130, 30);
        add(adm);
        jadm.setBounds(600, 30, 50, 30);
        add(jadm);
        kcse.setBounds(30, 100, 150, 30);
        add(kcse);
        jKcpe.setBounds(180, 100, 250, 30);
        add(jKcpe);
        studentName.setBounds(500, 100, 200, 30);
        add(studentName);
        subject.setBounds(30, 170, 150, 30);
        add(subject);
        jsubject.setBounds(200, 170, 150, 30);
        add(jsubject);
        subjectCode.setBounds(380, 170, 150, 30);
        add(subjectCode);
        jsubjectCode.setBounds(500, 170, 150, 30);
        add(jsubjectCode);

        school.setBounds(40, 250, 150, 30);
        add(school);
        jschool.setBounds(200, 250, 200, 30);
        add(jschool);
        point.setBounds(430, 250, 100, 30);
        add(point);
        jpoint.setBounds(550, 250, 100, 30);
        add(jpoint);

        save.setBounds(30, 330, 100, 50);
        add(save);
        // missing.setBounds(150,330,160,50);add(missing);
        result.setBounds(250, 330, 130, 50);
        add(result);
        delete.setBounds(470, 330, 100, 50);
        add(delete);
        // analyse.setBounds(500,280,130,50);add(analyse);
        cancel.setBounds(580, 330, 100, 50);
        add(cancel);
        jadm.setFont(new Font("serif", Font.BOLD, 18));
        jindex.setFont(new Font("serif", Font.BOLD, 18));

        jschool.setFont(new Font("serif", Font.BOLD, 18));
        jpoint.setFont(new Font("serif", Font.BOLD, 18));

        jadm.addKeyListener(new KeyAdapter() {

            public void keyTyped(KeyEvent key) {
                char c = key.getKeyChar();

                if (Character.isAlphabetic(c)) {
                    key.consume();
                }
            }

            public void keyReleased(KeyEvent key) {
                try {
                    String sql = "Select firstname,middlename,lastname from admission where admissionnumber='" + jadm.getText() + "'";
                    ps = con.prepareStatement(sql);
                    rs = ps.executeQuery();
                    if (rs.next()) {

                        jadm.setForeground(Color.black);
                        studentName.setText(rs.getString("FirstName") + "    " + rs.getString("MiddleName") + "    " + rs.getString("LastName"));


                    } else {
                        jadm.setForeground(Color.red);
                        studentName.setText("");

                        jindex.setText("");


                    }

                } catch (Exception sq) {
                    sq.printStackTrace();
                    JOptionPane.showMessageDialog(null, sq.getMessage());
                }

            }
        });
        jindex.addKeyListener(new KeyAdapter() {

            public void keyTyped(KeyEvent key) {
                char c = key.getKeyChar();

                if (Character.isAlphabetic(c)) {
                    key.consume();
                }
            }

            public void keyReleased(KeyEvent key) {
                try {

                    String sql = "Select firstname,middlename,lastname,kcpeindexnumber,admnumber from admission,kcpemarkstable where kcpeindexNumber='" + jindex.getText() + "' and admnumber=admissionnumber";
                    ps = con.prepareStatement(sql);
                    rs = ps.executeQuery();
                    if (rs.next()) {

                        jindex.setForeground(Color.black);
                        studentName.setText(rs.getString("FirstName") + "    " + rs.getString("MiddleName") + "    " + rs.getString("LastName"));
                        jadm.setText(rs.getString("admnumber"));


                    } else {
                        jindexsuffix.setForeground(Color.red);
                        studentName.setText("");
                        jadm.setText("");


                    }

                } catch (Exception sq) {
                    sq.printStackTrace();
                    JOptionPane.showMessageDialog(null, sq.getMessage());
                }

            }
        });

        try {
            String sqll = "Select * from primarysubjects";
            ps = con.prepareStatement(sqll);
            rs = ps.executeQuery();
            while (rs.next()) {
                jsubjectCode.addItem(rs.getString("Subjectcode"));
                jsubject.addItem(rs.getString("SubjectName"));
            }
        } catch (Exception e) {
        }
        for (int k = 2015; k <= Globals.academicYear(); ++k) {
            jKcpe.addItem(k);
        }


        jpoint.addKeyListener(new KeyAdapter() {
            public void keyTyped(KeyEvent key) {
                char c = key.getKeyChar();
                if (Character.isAlphabetic(c) || jpoint.getText().length() > 1) {
                    key.consume();
                }
            }

            public void keyReleased(KeyEvent key) {
                char c = key.getKeyChar();
                int mark = Integer.parseInt(jpoint.getText());
                if (mark > 100) {
                    JOptionPane.showMessageDialog(null, "Input A valid Score");
                }

            }

        });

        jpoint.addKeyListener(new KeyAdapter() {
            public void keyTyped(KeyEvent key) {
                char c = key.getKeyChar();
                if (c == KeyEvent.VK_ENTER) {
                    save.doClick();
                }
            }
        });

        setVisible(true);
        jsubject.addActionListener(this);
        jsubjectCode.addActionListener(this);
        save.addActionListener(this);
        result.addActionListener(this);
        cancel.addActionListener(this);
        missing.addActionListener(this);
        delete.addActionListener(this);

    }

    @Override
    public void actionPerformed(ActionEvent e) {

        Object obj = e.getSource();

        if (obj == jsubject) {
            try {
                String sql = "Select subjectcode from primarysubjects where subjectname='" + jsubject.getSelectedItem() + "'";
                ps = con.prepareStatement(sql);
                rs = ps.executeQuery();
                if (rs.next()) {
                    jsubjectCode.setSelectedItem(rs.getString("Subjectcode"));
                } else {
                    jsubjectCode.setSelectedIndex(0);
                }
            } catch (Exception sq) {
                sq.printStackTrace();
                JOptionPane.showMessageDialog(null, sq.getMessage());
            }
        } else if (obj == jsubjectCode) {
            try {
                String sql = "Select subjectname from primarysubjects where subjectcode='" + jsubjectCode.getSelectedItem() + "'";
                ps = con.prepareStatement(sql);
                rs = ps.executeQuery();
                if (rs.next()) {
                    jsubject.setSelectedItem(rs.getString("Subjectname"));
                }
            } catch (Exception sq) {
                sq.printStackTrace();
                JOptionPane.showMessageDialog(null, sq.getMessage());
            }
        } else if (obj == save) {


            if (jadm.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Input A Valid admission Number For A Student who Has Been Allocated an index Number");
            } else {
                if (jKcpe.getSelectedIndex() == 0) {
                    JOptionPane.showMessageDialog(this, "Select The  K.C.P.E Exam Year");
                } else {
                    if (jsubject.getSelectedIndex() == 0) {
                        JOptionPane.showMessageDialog(this, "Select The Subject To Record The Grades Against");
                    } else {

                        if (jpoint.getText().isEmpty()) {
                            JOptionPane.showMessageDialog(this, "Input Valid Points Value");
                        } else {


                            marksSaver(jindex.getText() + jindexsuffix.getText(), jadm.getText(), jKcpe.getSelectedItem().toString(), jsubject.getSelectedItem().toString(), jschool.getText(), jpoint.getText());
                            jpoint.setText("");


                        }

                    }
                }
            }


        } else if (obj == result) {
            if (analyse.isSelected()) {
                analysis = true;
            } else {
                analysis = false;
            }
            String n = jadm.getText();
            if (jadm.getText().isEmpty()) {


                if (jKcpe.getSelectedIndex() == 0) {
                    JOptionPane.showMessageDialog(this, "Select The Year To View Its KCPE Results");
                } else {
                    new Thread() {

                        public void run() {
                            JWindow dia = new JWindow();
                            dia.setSize(700, 70);
                            JProgressBar bar = new JProgressBar();
                            bar.setBorder(new TitledBorder("Please Wait While The Program Anlyses The Select KCPE Exam,This May Take Several Minutes"));
                            bar.setIndeterminate(true);
                            dia.setLayout(new MigLayout());
                            dia.add(bar, "Grow,push");
                            dia.setLocationRelativeTo(CurrentFrame.mainFrame());
                            dia.setVisible(true);
                            dia.setAlwaysOnTop(true);


                            ReportGenerator.kcpeMarks(jKcpe.getSelectedItem().toString(), "All", analysis);
                            dia.dispose();

                        }


                    }.start();

                }
            } else {

                if (jKcpe.getSelectedIndex() == 0) {
                    JOptionPane.showMessageDialog(this, "Select The Year To View Its KCSE Results");
                } else {
                    try {
                        String sql = "Select admissionnumber from admission where admissionnumber='" + jadm.getText() + "'";
                        ps = con.prepareStatement(sql);
                        rs = ps.executeQuery();
                        if (rs.next()) {
                            ReportGenerator.kcseMarks(jKcpe.getSelectedItem().toString(), n, analysis);
                        } else {
                            JOptionPane.showMessageDialog(this, "Invalid admission Number,Check...!!");
                        }
                    } catch (Exception sq) {
                        sq.printStackTrace();
                        JOptionPane.showMessageDialog(this, sq.getMessage());
                    }

                }

            }

        } else if (obj == delete) {
            if (jadm.getText().isEmpty()) {
                if (jKcpe.getSelectedIndex() == 0) {
                    JOptionPane.showMessageDialog(this, "Select The KCSE Year");
                } else {
                    if (jsubject.getSelectedIndex() == 0) {
                        int option = JOptionPane.showConfirmDialog(this, "Are You Sure That You Want To Delete All The Entered Marks For The Selected Year??", "Confirm", JOptionPane.YES_NO_OPTION);
                        if (option == JOptionPane.YES_OPTION) {
                            try {
                                String sql = "Delete from kcpemarkstable where yearofkcpe='" + jKcpe.getSelectedItem() + "'";
                                ps = con.prepareStatement(sql);
                                ps.execute();
                                JOptionPane.showMessageDialog(this, "Marks Deleted Successfully");
                            } catch (Exception sq) {
                                sq.printStackTrace();
                                JOptionPane.showMessageDialog(this, sq.getMessage());
                            }
                        }
                    } else {
                        int option = JOptionPane.showConfirmDialog(this, "Are You Sure That You Want To Delete All The Entered Marks For The  Selected Subject & Year ??", "Confirm", JOptionPane.YES_NO_OPTION);
                        if (option == JOptionPane.YES_OPTION) {
                            try {
                                String sql = "update kcpemarkstable set " + jsubject.getSelectedItem() + "='" + "" + "' where yearofkcpe='" + jKcpe.getSelectedItem() + "' and subjectcode='" + jsubjectCode.getSelectedItem() + "'";
                                ps = con.prepareStatement(sql);
                                ps.execute();
                                JOptionPane.showMessageDialog(this, "Marks Deleted Successfully");
                            } catch (Exception sq) {
                                sq.printStackTrace();
                                JOptionPane.showMessageDialog(this, sq.getMessage());
                            }
                        }
                    }
                }

            } else {

                try {
                    String sql = "Select admissionnumber from admission where admissionnumber='" + jadm.getText() + "'";
                    ps = con.prepareStatement(sql);
                    rs = ps.executeQuery();
                    if (rs.next()) {
                        if (jKcpe.getSelectedIndex() == 0) {
                            JOptionPane.showMessageDialog(this, "Select The KCSE Year");
                        } else {
                            if (jsubject.getSelectedIndex() == 0) {
                                int option = JOptionPane.showConfirmDialog(this, "Are You Sure That You Want To Delete All The Entered Marks For This Candidate....??", "Confirm", JOptionPane.YES_NO_OPTION);
                                if (option == JOptionPane.YES_OPTION) {
                                    try {
                                        String sqla = "Delete from kcpemarkstable where yearofkcpe='" + jKcpe.getSelectedItem() + "' and admnumber='" + jadm.getText() + "'";
                                        ps = con.prepareStatement(sqla);
                                        ps.execute();
                                        JOptionPane.showMessageDialog(this, "Marks Deleted Successfully");
                                    } catch (Exception sq) {
                                        sq.printStackTrace();
                                        JOptionPane.showMessageDialog(this, sq.getMessage());
                                    }
                                }
                            } else {
                                int option = JOptionPane.showConfirmDialog(this, "Are You Sure That You Want To Delete All The Entered Marks For This Candidate On The  Selected Subject  ??", "Confirm", JOptionPane.YES_NO_OPTION);
                                if (option == JOptionPane.YES_OPTION) {
                                    try {
                                        String sqll = "Delete from kcpemarkstable where yearofkcpe='" + jKcpe.getSelectedItem() + "' and subjectcode='" + jsubjectCode.getSelectedItem() + "' and admnumber='" + jadm.getText() + "'";
                                        ps = con.prepareStatement(sqll);
                                        ps.execute();
                                        JOptionPane.showMessageDialog(this, "Marks Deleted Successfully");
                                    } catch (Exception sq) {
                                        sq.printStackTrace();
                                        JOptionPane.showMessageDialog(this, sq.getMessage());
                                    }
                                }
                            }
                        }
                    } else {
                        JOptionPane.showMessageDialog(this, "Invalid admission Number,Check...!!");
                    }
                } catch (Exception sq) {
                    sq.printStackTrace();
                    JOptionPane.showMessageDialog(this, sq.getMessage());
                }

            }


        } else if (obj == missing) {
            if (jKcpe.getSelectedIndex() == 0) {
                JOptionPane.showMessageDialog(this, "Select The Year To View Its KCSE Missing Results");
            } else {
                ReportGenerator.kcseMissingMarks(jKcpe.getSelectedItem().toString());
            }
        } else if (obj == cancel) {
            CurrentFrame.currentWindow();
            dispose();
        }
    }

    public void marksSaver(String index, String adm, String year, String subjectcode, String school, String points) {

        try {


            String sql1 = "Select * from kcpemarkstable where yearofkcpe='" + year + "' and kcpeindexnumber='" + index + "' and admnumber='" + adm + "'";
            ps = con.prepareStatement(sql1);
            rs = ps.executeQuery();
            if (rs.next()) {

                String sql = "Update kcpemarkstable set " + subjectcode + "='" + points + "' where yearofkcpe='" + year + "' and kcpeindexnumber='" + index + "' and admnumber='" + adm + "'  ";
                ps = con.prepareStatement(sql);
                ps.execute();
                JOptionPane.showMessageDialog(this, "Record Updated");
                jsubject.setSelectedIndex(0);

            } else {


                String sql = "Insert into kcpemarkstable( yearofKcpe,KCPEIndexNumber,Admnumber," + subjectcode + ",primaryschoolname) values('" + year + "','" + index + "','" + adm + "','" + points + "','" + school + "')";
                ps = con.prepareStatement(sql);
                ps.execute();
                JOptionPane.showMessageDialog(this, "Record Saved Successfully");
                jsubject.setSelectedIndex(0);

            }


        } catch (Exception sq) {
            sq.printStackTrace();
        }

    }

    public static void main(String[] args) {
        new KCPEMarksEntry();
    }

}
