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
public class KCSEMarksEntry extends JFrame implements ActionListener {
    private int height = 450;
    private int width = 700;
    private ResultSet rs;
    private Connection con = DbConnection.connectDb();
    private PreparedStatement ps;
    private FredLabel index = new FredLabel("Index Number");
    private FredLabel grade = new FredLabel("Grade");
    private FredLabel point = new FredLabel("Points");
    private FredLabel kcse = new FredLabel("K.C.S.E Year");
    private FredLabel subject = new FredLabel("Subject Name");
    private FredLabel subjectCode = new FredLabel("Subject Code");
    private FredLabel adm = new FredLabel("admission Number");
    private FredLabel studentName = new FredLabel("Student Name");
    private FredCheckBox analyse = new FredCheckBox("Analyse First");

    private FredTextField jindex = new FredTextField();
    private FredTextField jgrade = new FredTextField();
    private FredTextField jpoint = new FredTextField();
    private FredTextField jindexsuffix = new FredTextField();
    private FredTextField jadm = new FredTextField();
    boolean analysis;

    private FredCombo jsubject = new FredCombo("Select Subject");
    private FredCombo jsubjectCode = new FredCombo("Select Subject Code");
    private FredCombo jKcse = new FredCombo("Select K.C.S.E Exam Year");
    private FredButton save = new FredButton("Save");
    private FredButton cancel = new FredButton("Cancel");
    private FredButton result = new FredButton("Show Results");
    private FredButton missing = new FredButton("Show Missing Marks");
    private FredButton delete = new FredButton("Delete");


    public KCSEMarksEntry() {
        setSize(width, height);
        setTitle("K.C.S.E Marks Entry Panel");
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
        jindexsuffix.setBounds(380, 30, 50, 30);
        add(jindexsuffix);
        adm.setBounds(450, 30, 130, 30);
        add(adm);
        jadm.setBounds(600, 30, 50, 30);
        add(jadm);
        kcse.setBounds(30, 100, 150, 30);
        add(kcse);
        jKcse.setBounds(180, 100, 250, 30);
        add(jKcse);
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

        grade.setBounds(120, 250, 100, 30);
        add(grade);
        jgrade.setBounds(230, 250, 100, 30);
        add(jgrade);
        point.setBounds(370, 250, 100, 30);
        add(point);
        jpoint.setBounds(420, 250, 100, 30);
        add(jpoint);

        save.setBounds(30, 330, 100, 50);
        add(save);
        missing.setBounds(150, 330, 160, 50);
        add(missing);
        result.setBounds(320, 330, 130, 50);
        add(result);
        delete.setBounds(470, 330, 100, 50);
        add(delete);
        analyse.setBounds(500, 280, 130, 50);
        add(analyse);
        cancel.setBounds(580, 330, 100, 50);
        add(cancel);
        jadm.setFont(new Font("serif", Font.BOLD, 18));
        jindex.setFont(new Font("serif", Font.BOLD, 18));
        jindexsuffix.setFont(new Font("serif", Font.BOLD, 18));
        jindex.setText(ConfigurationIntialiser.reminderTime());
        jindex.setEditable(false);
        jgrade.setFont(new Font("serif", Font.BOLD, 18));
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
                    String sql = "Select firstname,middlename,lastname,kcseindexnumber from admission,kcseindexnumbers where admissionnumber='" + jadm.getText() + "' and admnumber=admissionnumber";
                    ps = con.prepareStatement(sql);
                    rs = ps.executeQuery();
                    if (rs.next()) {

                        jadm.setForeground(Color.black);
                        studentName.setText(rs.getString("FirstName") + "    " + rs.getString("MiddleName") + "    " + rs.getString("LastName"));
                        jindexsuffix.setText(rs.getString("kcseindexnumber").substring(8, rs.getString("kcseindexnumber").length()).trim());


                    } else {
                        jadm.setForeground(Color.red);
                        studentName.setText("");

                        jindexsuffix.setText("");


                    }

                } catch (Exception sq) {
                    sq.printStackTrace();
                    JOptionPane.showMessageDialog(null, sq.getMessage());
                }

            }
        });
        jindexsuffix.addKeyListener(new KeyAdapter() {

            public void keyTyped(KeyEvent key) {
                char c = key.getKeyChar();

                if (Character.isAlphabetic(c)) {
                    key.consume();
                }
            }

            public void keyReleased(KeyEvent key) {
                try {

                    String sql = "Select firstname,middlename,lastname,kcseindexnumber,admnumber from admission,kcseindexnumbers where kcseindexNumber='" + jindex.getText() + jindexsuffix.getText() + "' and admnumber=admissionnumber";
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
            String sqll = "Select * from subjects";
            ps = con.prepareStatement(sqll);
            rs = ps.executeQuery();
            while (rs.next()) {
                jsubjectCode.addItem(rs.getString("Subjectcode"));
                jsubject.addItem(rs.getString("SubjectName"));
            }
        } catch (Exception e) {
        }
        for (int k = 2015; k <= Globals.academicYear(); ++k) {
            jKcse.addItem(k);
        }
        jgrade.addKeyListener(new KeyAdapter() {

            public void keyTyped(KeyEvent key) {
                char c = key.getKeyChar();
                if (Character.isDigit(c) || jgrade.getText().length() > 1) {
                    key.consume();
                }
            }

            public void keyReleased(KeyEvent key) {
                jgrade.setText(jgrade.getText().toUpperCase());
                char c = key.getKeyChar();

                try {
                    String sql = "Select points from points_for_each_grade where  grade='" + jgrade.getText() + "'";
                    ps = con.prepareStatement(sql);
                    rs = ps.executeQuery();
                    if (rs.next()) {
                        jpoint.setText(rs.getString("Points"));
                    } else {
                        jpoint.setText("");
                    }


                } catch (Exception sq) {
                    sq.printStackTrace();
                    ;
                }
            }


        });

        jpoint.addKeyListener(new KeyAdapter() {
            public void keyTyped(KeyEvent key) {
                char c = key.getKeyChar();
                if (Character.isAlphabetic(c) || jpoint.getText().length() > 1) {
                    key.consume();
                }
            }

            public void keyReleased(KeyEvent key) {
                char c = key.getKeyChar();

                try {
                    String sql = "Select grade from points_for_each_grade where  points='" + jpoint.getText() + "'";
                    ps = con.prepareStatement(sql);
                    rs = ps.executeQuery();
                    if (rs.next()) {
                        jgrade.setText(rs.getString("grade"));
                    } else {
                        jgrade.setText("");
                    }


                } catch (Exception sq) {
                    sq.printStackTrace();
                    ;
                }
            }

        });
        jgrade.addKeyListener(new KeyAdapter() {
            public void keyTyped(KeyEvent key) {
                char c = key.getKeyChar();
                if (c == KeyEvent.VK_ENTER) {
                    save.doClick();
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
                String sql = "Select subjectcode from subjects where subjectname='" + jsubject.getSelectedItem() + "'";
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
                String sql = "Select subjectname from subjects where subjectcode='" + jsubjectCode.getSelectedItem() + "'";
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
            if (jindex.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Input a valid School KCSE Index Numbers Prefix Code");
            } else {
                if (jindexsuffix.getText().isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Input a Valid Candidate Index Number");
                } else {
                    if (jadm.getText().isEmpty()) {
                        JOptionPane.showMessageDialog(this, "Input A Valid admission Number For A Student who Has Been Allocated an index Number");
                    } else {
                        if (jKcse.getSelectedIndex() == 0) {
                            JOptionPane.showMessageDialog(this, "Select The  K.C.S.E Exam Year");
                        } else {
                            if (jsubject.getSelectedIndex() == 0) {
                                JOptionPane.showMessageDialog(this, "Select The Subject To Record The Grades Against");
                            } else {
                                if (jgrade.getText().isEmpty()) {
                                    JOptionPane.showMessageDialog(this, "Input A Valid Grade");
                                } else {
                                    if (jpoint.getText().isEmpty()) {
                                        JOptionPane.showMessageDialog(this, "Input Valid Points Value");
                                    } else {


                                        marksSaver(jindex.getText() + jindexsuffix.getText(), jadm.getText(), jKcse.getSelectedItem().toString(), jsubjectCode.getSelectedItem().toString(), jgrade.getText(), jpoint.getText());
                                        jgrade.setText("");
                                        jpoint.setText("");


                                    }
                                }
                            }
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


                if (jKcse.getSelectedIndex() == 0) {
                    JOptionPane.showMessageDialog(this, "Select The Year To View Its KCSE Results");
                } else {
                    new Thread() {

                        public void run() {
                            JWindow dia = new JWindow();
                            dia.setSize(700, 70);
                            JProgressBar bar = new JProgressBar();
                            bar.setBorder(new TitledBorder("Please Wait While The Program Anlyses The Select KCSE Exam,This May Take Several Minutes"));
                            bar.setIndeterminate(true);
                            dia.setLayout(new MigLayout());
                            dia.add(bar, "Grow,push");
                            dia.setLocationRelativeTo(CurrentFrame.mainFrame());
                            dia.setVisible(true);
                            dia.setAlwaysOnTop(true);


                            ReportGenerator.kcseMarks(jKcse.getSelectedItem().toString(), "All", analysis);
                            dia.dispose();

                        }


                    }.start();

                }
            } else {

                if (jKcse.getSelectedIndex() == 0) {
                    JOptionPane.showMessageDialog(this, "Select The Year To View Its KCSE Results");
                } else {
                    try {
                        String sql = "Select admissionnumber from admission where admissionnumber='" + jadm.getText() + "'";
                        ps = con.prepareStatement(sql);
                        rs = ps.executeQuery();
                        if (rs.next()) {
                            ReportGenerator.kcseMarks(jKcse.getSelectedItem().toString(), n, analysis);
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
                if (jKcse.getSelectedIndex() == 0) {
                    JOptionPane.showMessageDialog(this, "Select The KCSE Year");
                } else {
                    if (jsubject.getSelectedIndex() == 0) {
                        int option = JOptionPane.showConfirmDialog(this, "Are You Sure That You Want To Delete All The Entered Marks For The Selected Year??", "Confirm", JOptionPane.YES_NO_OPTION);
                        if (option == JOptionPane.YES_OPTION) {
                            try {
                                String sql = "Delete from kcsemarks where kcseyear='" + jKcse.getSelectedItem() + "'";
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
                                String sql = "Delete from kcsemarks where kcseyear='" + jKcse.getSelectedItem() + "' and subjectcode='" + jsubjectCode.getSelectedItem() + "'";
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
                        if (jKcse.getSelectedIndex() == 0) {
                            JOptionPane.showMessageDialog(this, "Select The KCSE Year");
                        } else {
                            if (jsubject.getSelectedIndex() == 0) {
                                int option = JOptionPane.showConfirmDialog(this, "Are You Sure That You Want To Delete All The Entered Marks For This Candidate....??", "Confirm", JOptionPane.YES_NO_OPTION);
                                if (option == JOptionPane.YES_OPTION) {
                                    try {
                                        String sqla = "Delete from kcsemarks where kcseyear='" + jKcse.getSelectedItem() + "' and admnumber='" + jadm.getText() + "'";
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
                                        String sqll = "Delete from kcsemarks where kcseyear='" + jKcse.getSelectedItem() + "' and subjectcode='" + jsubjectCode.getSelectedItem() + "' and admnumber='" + jadm.getText() + "'";
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
            if (jKcse.getSelectedIndex() == 0) {
                JOptionPane.showMessageDialog(this, "Select The Year To View Its KCSE Missing Results");
            } else {
                ReportGenerator.kcseMissingMarks(jKcse.getSelectedItem().toString());
            }
        } else if (obj == cancel) {
            CurrentFrame.currentWindow();
            dispose();
        }
    }

    public void marksSaver(String index, String adm, String year, String subjectcode, String grade, String points) {

        try {

            String querry = "Select * from kcseindexnumbers where admnumber='" + adm + "' and kcseindexnumber='" + index + "' and academicyear='" + year + "' ";
            ps = con.prepareStatement(querry);
            rs = ps.executeQuery();
            if (rs.next()) {
                String sql1 = "Select * from kcsemarks where kcseyear='" + year + "' and kcseindexnumber='" + index + "' and admnumber='" + adm + "' and subjectcode='" + subjectcode + "'";
                ps = con.prepareStatement(sql1);
                rs = ps.executeQuery();
                if (rs.next()) {
                    int option = JOptionPane.showConfirmDialog(this, "Marks For " + Globals.fullName(adm) + " In " + jsubject.getSelectedItem() + " Have Already Been Entered \n Do You Want To Update To The New Value", "Confirm Record Update", JOptionPane.YES_NO_OPTION);
                    if (option == JOptionPane.NO_OPTION) {

                    } else {
                        String sql = "Update kcsemarks set grade='" + grade + "' ,points='" + points + "' where kcseyear='" + year + "' and kcseindexnumber='" + index + "' and admnumber='" + adm + "' and subjectcode='" + subjectcode + "' ";
                        ps = con.prepareStatement(sql);
                        ps.execute();
                        JOptionPane.showMessageDialog(this, "Record Updated");
                        jsubject.setSelectedIndex(0);
                    }
                } else {


                    String sql = "Insert into kcsemarks( KCSEYear,KCSEIndexNumber,Admnumber,SubjectCode,Grade,points) values('" + year + "','" + index + "','" + adm + "','" + subjectcode + "','" + grade + "','" + points + "')";
                    ps = con.prepareStatement(sql);
                    ps.execute();
                    JOptionPane.showMessageDialog(this, "Record Saved Successfully");
                    jsubject.setSelectedIndex(0);

                }
            } else {
                JOptionPane.showMessageDialog(this, "The KCSE Year Selected Does Not Match To The Candidate's  Year Of Completion Check....!!!");
            }


        } catch (Exception sq) {
            sq.printStackTrace();
        }

    }

    public static void main(String[] args) {
        new KCSEMarksEntry();
    }

}
