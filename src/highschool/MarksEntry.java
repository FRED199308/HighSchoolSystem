/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package highschool;

import java.awt.Color;
import java.awt.Dialog;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.NumberFormat;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import static javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS;
import static javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS;
import static javax.swing.WindowConstants.DISPOSE_ON_CLOSE;

import static javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE;

import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;

import net.miginfocom.swing.MigLayout;

/**
 * @author FRED
 */
public class MarksEntry extends JFrame implements ActionListener {

    private Connection con = DbConnection.connectDb();
    private int width = 1100;
    private int height = 720;
    private PreparedStatement ps;
    private ResultSet rs;
    private FredLabel studentimage = new FredLabel("No Image");
    private FredCombo jclass = new FredCombo("Select Class");
    private ResultSet result = null;
    private ResultSet tableresult = null;


    // paper selection field


    private FredLabel examname, admnumber, term, academicyear, stream, subject, class1, examcode, studentName, subjectcode;
    private FredCombo jexamname, jterm, jacademicyear, jstream, jsubject, jclass1, jexamcode, jsubjectcode;
    private FredTextField jadmnumber = new FredTextField();
    private JPanel top = new JPanel();
    private JPanel bottom = new JPanel();
    private FredButton save = new FredButton("Save");
    private FredButton missing = new FredButton("Show Missing Marks");
    private FredButton cancel = new FredButton("close");
    private FredButton marks = new FredButton("Show Result");
    private FredButton delete = new FredButton("Delete Marks");
    private FredCheckBox admcheck = new FredCheckBox("Sort Results By admission Number");
    private FredCheckBox entryMode = new FredCheckBox("Enter As P1,P2,P3 Marks");
    private FredCheckBox performancecheck = new FredCheckBox("Sort Results By Performance");
    private FredLabel score, outof, convertedscore, teachername, percent, advice, grade, points, teacherintial;
    private FredTextField jscore, joutof, jconvertedscore, jteachername, jpercent, jadvice, jgrade, jpoints, jteacherintial;
    private ButtonGroup group = new ButtonGroup();
    private JPanel pane = new JPanel();

    private JPanel tablePane = new JPanel();
    private JTable tab = new JTable();
    private JScrollPane tabpane = new JScrollPane(tab);
    private DefaultTableModel model = new DefaultTableModel() {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };
    private String selectedPaper = "";
    FredCombo papers = new FredCombo();
    private Object cols[] = {"AdmNumber", "Name", "Marks"};
    String text = "";
    String tm, cl, yr, ex;
    int weight = 0;
    JPanel holder = new JPanel();
    JScrollPane scrolls = new JScrollPane(holder);


    public MarksEntry() {


        subjectcode = new FredLabel("Subject Code");
        jsubjectcode = new FredCombo("Select Subject Code");
        examname = new FredLabel("Exam Name");
        admnumber = new FredLabel("admission Number");
        term = new FredLabel("Term");
        stream = new FredLabel("Stream");
        subject = new FredLabel("Subject");
        class1 = new FredLabel("Class");
        examcode = new FredLabel("Exam Code");
        academicyear = new FredLabel("Academic Year");
        jexamname = new FredCombo("Exam Name");
        jterm = new FredCombo("Term");
        jstream = new FredCombo("Stream");
        jsubject = new FredCombo("Subject");
        jclass1 = new FredCombo("Class");
        jexamcode = new FredCombo("Exam Code");
        studentName = new FredLabel("Student Name");
        jacademicyear = new FredCombo("Academic Year");
        score = new FredLabel("Sudent Score");
        outof = new FredLabel("Out Of");
        convertedscore = new FredLabel("Converted Score");
        teacherintial = new FredLabel("Teachers Intials");
        teachername = new FredLabel("Teacher Name");
        percent = new FredLabel("% Converted Score");
        grade = new FredLabel("Grade");
        points = new FredLabel("Points");
        advice = new FredLabel("The Student Score Will Be Converted To Select Exam Weight Settings");
        jscore = new FredTextField();
        joutof = new FredTextField();
        jconvertedscore = new FredTextField();
        jteacherintial = new FredTextField();
        jteachername = new FredTextField();
        jpercent = new FredTextField();
        jgrade = new FredTextField();
        jpoints = new FredTextField();
        model.setColumnIdentifiers(cols);
        tab.setModel(model);
        tablePane.setLayout(new MigLayout());
        tablePane.add(tabpane, "push,grow");
        setSize(width, height);
        this.setLayout(new MigLayout());
        top.setLayout(new MigLayout());
        bottom.setLayout(new MigLayout());
        performancecheck.setSelected(true);
        bottom.setBackground(Color.cyan);
        setLocationRelativeTo(CurrentFrame.mainFrame());
        holder.setLayout(new MigLayout());

        holder.add(top, "pushx,growx");
        holder.add(tablePane, "push,grow,span 1 2,wrap");
        holder.add(bottom, "push,grow");
        add(scrolls, "grow,Push");
        setTitle("Marks Entry Panel");
        ((JComponent) getContentPane()).setBorder(
                BorderFactory.createMatteBorder(5, 5, 5, 5, Color.MAGENTA));
        group.add(performancecheck);
        group.add(admcheck);
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
        pane.setBackground(Color.cyan);
        pane.setBorder(new TitledBorder("Student Image"));
        pane.setLayout(new MigLayout());
        //pane.add(entryMode,"wrap");
        pane.add(studentimage, "grow,push");
        top.add(entryMode, "wrap");
        top.add(admnumber, "growx,pushx,gapright 20");
        top.add(jadmnumber, "growx,pushx,gapright 20");
        top.add(studentName, "growx,pushx,gapright 20");
        top.add(pane, "grow,push,wrap,gapright 20,width 200:150:200,height 100:80:150,wrap");
        top.add(class1, "growx,pushx,gapright 20,gaptop 20");
        top.add(jclass1, "growx,pushx,gapright 20,gaptop 20");
        top.add(term, "growx,pushx,gapright 20,gaptop 20");
        top.add(jterm, "growx,pushx,gapright 20,gaptop 20,wrap");

        top.add(stream, "growx,pushx,gapright 20,gaptop 20");
        top.add(jstream, "growx,pushx,gapright 20,gaptop 20");
        top.add(academicyear, "growx,pushx,gapright 20,gaptop 20");
        top.add(jacademicyear, "growx,pushx,gapright 20,gaptop 20,wrap");
        top.add(subject, "growx,pushx,gapright 20,gaptop 20");
        top.add(jsubject, "growx,pushx,gapright 20,gaptop 20");
        top.add(subjectcode, "growx,pushx,gapright 20,gaptop 20");
        top.add(jsubjectcode, "growx,pushx,gapright 20,gaptop 20,wrap");
        top.add(examname, "growx,pushx,gapright 20,gaptop 20");
        top.add(jexamname, "growx,pushx,gapright 20,gaptop 20");
        top.add(examcode, "growx,pushx,gapright 20,gaptop 20");
        top.add(jexamcode, "growx,pushx,gapright 20,gaptop 20,wrap");
        bottom.setBorder(new TitledBorder("Use Arrow To Skip Student And Enter Keys To Save"));
        bottom.add(advice, "Gapleft 300,spanx,growx,pushx,wrap");
        bottom.add(score, "gapleft 50,pushx,growx");
        bottom.add(jscore, "growx,pushx,gapleft 0");
        bottom.add(outof, "growx,pushx,gapleft 0");
        bottom.add(joutof, "growx,pushx,gapleft 0");
        bottom.add(teachername, "growx,pushx,gapleft 30");
        bottom.add(jteachername, "growx,pushx,gapleft 0,wrap");
        bottom.add(convertedscore, "gapleft 50,pushx,growx");
        bottom.add(jconvertedscore, "growx,pushx,gapleft 0");
        bottom.add(percent, "growx,pushx,gapleft 30");
        bottom.add(jpercent, "growx,pushx,gapleft 0,gaptop 25");
        bottom.add(teacherintial, "growx,pushx,gapleft 30");
        bottom.add(jteacherintial, "growx,pushx,gapleft 0,wrap");
        bottom.add(grade, "growx,pushx,gapleft 50");
        bottom.add(jgrade, "growx,pushx");
        bottom.add(points, "growx,pushx");
        bottom.add(jpoints, "growx,pushx");
        bottom.add(performancecheck, "growx,pushx,gaptop 30");
        bottom.add(admcheck, "growx,pushx,wrap");
        bottom.add(save, "Skip 2,grow,push,gaptop 30");
        bottom.add(save, "Skip 1,grow,push,gaptop 30");
        bottom.add(missing, "gaptop 30,grow,push");
        bottom.add(marks, "gaptop 30,grow,push");
        bottom.add(delete, "gaptop 30,grow,push");
        bottom.add(cancel, "gaptop 30,grow,push");

        try {

            String sql2 = "Select * from streams";

            ps = con.prepareStatement(sql2);
            rs = ps.executeQuery();
            while (rs.next()) {

                jstream.addItem(rs.getString("StreamName"));
            }
            String sql = "Select * from classes where precision1<5 order by precision1 asc";

            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                jclass1.addItem(rs.getString("ClassName"));

            }
            String sql9 = "Select* from terms";
            ps = con.prepareStatement(sql9);
            rs = ps.executeQuery();
            while (rs.next()) {
                jterm.addItem(rs.getString("TermName"));

            }
            String sqll = "Select * from subjects";
            ps = con.prepareStatement(sqll);
            rs = ps.executeQuery();
            while (rs.next()) {
                jsubjectcode.addItem(rs.getString("Subjectcode"));
                jsubject.addItem(rs.getString("SubjectName"));
            }
            for (int k = 2015; k <= Globals.academicYear(); ++k) {
                jacademicyear.addItem(k);
            }
            String sqls = "Select examname from exams group by examname";
            ps = con.prepareStatement(sqls);
            rs = ps.executeQuery();
            while (rs.next()) {
                jexamname.addItem(rs.getString("ExamName"));
            }
            String sqls1 = "Select examcode from exams ";
            ps = con.prepareStatement(sqls1);
            rs = ps.executeQuery();
            while (rs.next()) {
                jexamcode.addItem(rs.getString("Examcode"));
            }
        } catch (Exception sq) {
            sq.printStackTrace();
            JOptionPane.showMessageDialog(null, sq.getMessage());
        }
        jacademicyear.setSelectedItem(Globals.academicYear());
        jterm.setSelectedItem(Globals.currentTermName());
        cancel.addActionListener(this);
        save.addActionListener(this);
        jsubject.addActionListener(this);
        jsubjectcode.addActionListener(this);
        jterm.addActionListener(this);
        jclass1.addActionListener(this);
        jexamname.addActionListener(this);
        jacademicyear.addActionListener(this);
        jexamcode.addActionListener(this);
        marks.addActionListener(this);
        missing.addActionListener(this);
        jpercent.setEditable(false);
        jpercent.setFont(new Font("serif", Font.BOLD, 17));
        jpercent.setForeground(Color.RED);
        jconvertedscore.setEditable(false);
        jconvertedscore.setFont(new Font("serif", Font.BOLD, 17));
        jconvertedscore.setForeground(Color.RED);
        jpoints.setEditable(false);
        jpoints.setFont(new Font("serif", Font.BOLD, 17));
        jpoints.setForeground(Color.RED);
        jgrade.setEditable(false);
        jexamcode.setEditable(false);
        jstream.addActionListener(this);
        jgrade.setFont(new Font("serif", Font.BOLD, 17));
        jgrade.setForeground(Color.RED);
        jadmnumber.setFont(new Font("serif", Font.BOLD, 17));
        if (jstream.getItemCount() < 3) {
            jstream.setSelectedIndex(1);
        }
        jacademicyear.setSelectedItem(Globals.academicYear());
        jterm.setSelectedItem(Globals.currentTermName());
        setVisible(true);
        joutof.addKeyListener(new KeyAdapter() {
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();
                if (!Character.isDigit(c)) {
                    getToolkit().beep();
                    e.consume();
                    ;
                }
            }
        });

        delete.addActionListener(this);
        delete.addKeyListener(new KeyAdapter() {

            public void keyReleased(KeyEvent key) {
                if (key.getKeyCode() == KeyEvent.VK_DELETE) {

                    if (jclass1.getSelectedIndex() == 0) {
                        JOptionPane.showMessageDialog(null, "Kindly Select Class");
                    } else {
                        if (jterm.getSelectedIndex() == 0) {
                            JOptionPane.showMessageDialog(null, "Kindly Select Term");
                        } else {
                            if (jstream.getSelectedIndex() == 0) {
                                JOptionPane.showMessageDialog(null, "Kindly Select Stream");
                            } else {
                                if (jacademicyear.getSelectedIndex() == 0) {
                                    JOptionPane.showMessageDialog(null, "Kindly Select Academic Year");
                                } else {
                                    if (jsubject.getSelectedIndex() == 0) {
                                        JOptionPane.showMessageDialog(null, "Kindly Select Subject");
                                    } else {
                                        if (jsubjectcode.getSelectedIndex() == 0) {
                                            JOptionPane.showMessageDialog(null, "Kindly Select Subject Code");
                                        } else {
                                            if (jexamname.getSelectedIndex() == 0) {
                                                JOptionPane.showMessageDialog(null, "Kindly Select Exam Name");
                                            } else {
                                                if (jexamcode.getSelectedIndex() == 0) {
                                                    JOptionPane.showMessageDialog(null, "Kindly Select Exam Code");
                                                } else {
                                                    if (Globals.admissionVerifier(jadmnumber.getText())) {

                                                        if (jscore.getText().isEmpty()) {
                                                            JOptionPane.showMessageDialog(null, "Cannot Delete Empty Mark");

                                                        } else {


                                                            marksDelete(jadmnumber.getText(), jsubjectcode.getSelectedItem().toString(), Globals.termcode(jterm.getSelectedItem().toString()), jacademicyear.getSelectedItem().toString(), Globals.classCode(jclass1.getSelectedItem().toString()), jstream.getSelectedItem().toString(), jexamname.getSelectedItem().toString(), jexamcode.getSelectedItem().toString());


                                                        }

                                                    } else {
                                                        JOptionPane.showMessageDialog(CurrentFrame.mainFrame(), "Invalid admission Number,Kindly Check");
                                                    }

                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }


                }
            }

        });


        jscore.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent key) {


                if (key.getKeyCode() == KeyEvent.VK_DELETE) {

                    if (jclass1.getSelectedIndex() == 0) {
                        JOptionPane.showMessageDialog(null, "Kindly Select Class");
                    } else {
                        if (jterm.getSelectedIndex() == 0) {
                            JOptionPane.showMessageDialog(null, "Kindly Select Term");
                        } else {
                            if (jstream.getSelectedIndex() == 0) {
                                JOptionPane.showMessageDialog(null, "Kindly Select Stream");
                            } else {
                                if (jacademicyear.getSelectedIndex() == 0) {
                                    JOptionPane.showMessageDialog(null, "Kindly Select Academic Year");
                                } else {
                                    if (jsubject.getSelectedIndex() == 0) {
                                        JOptionPane.showMessageDialog(null, "Kindly Select Subject");
                                    } else {
                                        if (jsubjectcode.getSelectedIndex() == 0) {
                                            JOptionPane.showMessageDialog(null, "Kindly Select Subject Code");
                                        } else {
                                            if (jexamname.getSelectedIndex() == 0) {
                                                JOptionPane.showMessageDialog(null, "Kindly Select Exam Name");
                                            } else {
                                                if (jexamcode.getSelectedIndex() == 0) {
                                                    JOptionPane.showMessageDialog(null, "Kindly Select Exam Code");
                                                } else {
                                                    if (Globals.admissionVerifier(jadmnumber.getText())) {

                                                        if (jscore.getText().isEmpty()) {
                                                            JOptionPane.showMessageDialog(null, "Cannot Delete Empty Mark");

                                                        } else {


                                                            marksDelete(jadmnumber.getText(), jsubjectcode.getSelectedItem().toString(), Globals.termcode(jterm.getSelectedItem().toString()), jacademicyear.getSelectedItem().toString(), Globals.classCode(jclass1.getSelectedItem().toString()), jstream.getSelectedItem().toString(), jexamname.getSelectedItem().toString(), jexamcode.getSelectedItem().toString());


                                                        }

                                                    } else {
                                                        JOptionPane.showMessageDialog(CurrentFrame.mainFrame(), "Invalid admission Number,Kindly Check");
                                                    }

                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }


                }

                if (key.getKeyCode() == KeyEvent.VK_RIGHT) {
                    try {

                        if (result.next()) {


                            String subjectcode = jsubjectcode.getSelectedItem().toString();
                            String classcode = Globals.classCode(jclass1.getSelectedItem().toString());
                            String examcode1 = jexamcode.getSelectedItem().toString();
                            String adm = result.getString("admnumber");
                            String imageurl = result.getString("ImageLocation");
                            if (imageurl.isEmpty()) {
                                studentimage.setText("No Image");
                                studentimage.setIcon(null);
                            } else {
                                studentimage.setIcon(resizeImage(result.getString("ImageLocation")));
                                studentimage.setText("");
                            }
                            jadmnumber.setText(adm);
                            studentName.setText(result.getString("firstName") + "    " + result.getString("Middlename") + "    " + result.getString("LastName"));
                            if (entryMode.isSelected()) {

                                String sq = "Select * from partialSubjectMark where subjectcode='" + subjectcode + "' and academicyear='" + jacademicyear.getSelectedItem() + "' and examcode='" + jexamcode.getSelectedItem() + "' and admnumber='" + adm + "' and paper='" + selectedPaper + "'";
                                ps = con.prepareStatement(sq);
                                rs = ps.executeQuery();
                                if (rs.next()) {

                                    jscore.setText(rs.getString("score"));

                                } else {
                                    jpoints.setText("");
                                    jconvertedscore.setText("");
                                    jscore.setText("");

                                    jpercent.setText("");
                                    jgrade.setText("");
                                }

                            } else {


                                String sq = "Select * from markstable where subjectcode='" + subjectcode + "' and academicyear='" + jacademicyear.getSelectedItem() + "' and examcode='" + jexamcode.getSelectedItem() + "' and admnumber='" + adm + "'";
                                ps = con.prepareStatement(sq);
                                rs = ps.executeQuery();
                                if (rs.next()) {
                                    jpoints.setText(rs.getString("examPoints"));
                                    jconvertedscore.setText(rs.getString("convertedscore"));
                                    jscore.setText(rs.getString("examscore"));
                                    joutof.setText(rs.getString("Examoutof"));
                                    jpercent.setText(rs.getString("Exampercentage"));
                                    jgrade.setText(rs.getString("examgrade"));
                                } else {
                                    jpoints.setText("");
                                    jconvertedscore.setText("");
                                    jscore.setText("");

                                    jpercent.setText("");
                                    jgrade.setText("");
                                }
                            }
                        } else {
                            JOptionPane.showMessageDialog(null, "End Of List");
                        }
                    } catch (Exception e) {
                    }
                }
                if (key.getKeyCode() == KeyEvent.VK_LEFT) {
                    try {

                        if (result.previous()) {


                            String subjectcode = jsubjectcode.getSelectedItem().toString();
                            String classcode = Globals.classCode(jclass1.getSelectedItem().toString());
                            String examcode1 = jexamcode.getSelectedItem().toString();
                            String adm = result.getString("admnumber");
                            String imageurl = result.getString("ImageLocation");
                            if (imageurl.isEmpty()) {
                                studentimage.setText("No Image");
                                studentimage.setIcon(null);
                            } else {
                                studentimage.setIcon(resizeImage(result.getString("ImageLocation")));
                                studentimage.setText("");
                            }
                            jadmnumber.setText(adm);
                            studentName.setText(result.getString("firstName") + "    " + result.getString("Middlename") + "    " + result.getString("LastName"));
                            if (entryMode.isSelected()) {

                                String sq = "Select * from partialSubjectMark where subjectcode='" + subjectcode + "' and academicyear='" + jacademicyear.getSelectedItem() + "' and examcode='" + jexamcode.getSelectedItem() + "' and admnumber='" + adm + "' and paper='" + selectedPaper + "'";
                                ps = con.prepareStatement(sq);
                                rs = ps.executeQuery();
                                if (rs.next()) {

                                    jscore.setText(rs.getString("score"));

                                } else {
                                    jpoints.setText("");
                                    jconvertedscore.setText("");
                                    jscore.setText("");

                                    jpercent.setText("");
                                    jgrade.setText("");
                                }


                            } else {

                                String sq = "Select * from markstable where subjectcode='" + subjectcode + "' and academicyear='" + jacademicyear.getSelectedItem() + "' and examcode='" + jexamcode.getSelectedItem() + "' and admnumber='" + adm + "'";
                                ps = con.prepareStatement(sq);
                                rs = ps.executeQuery();
                                if (rs.next()) {
                                    jpoints.setText(rs.getString("examPoints"));
                                    jconvertedscore.setText(rs.getString("convertedscore"));
                                    jscore.setText(rs.getString("examscore"));
                                    joutof.setText(rs.getString("Examoutof"));
                                    jpercent.setText(rs.getString("Exampercentage"));
                                    jgrade.setText(rs.getString("examgrade"));


                                } else {
                                    jpoints.setText("");
                                    jconvertedscore.setText("");
                                    jscore.setText("");

                                    jpercent.setText("");
                                    jgrade.setText("");
                                }


                            }
                        } else {
                            JOptionPane.showMessageDialog(null, "Cursor Pointing The First Person");
                        }
                    } catch (Exception e) {
                    }
                }
            }

            public void keyTyped(KeyEvent key) {
                char c = key.getKeyChar();
                if (c == KeyEvent.VK_ENTER) {
                    if (jclass1.getSelectedIndex() == 0) {
                        JOptionPane.showMessageDialog(null, "Kindly Select Class");
                    } else {
                        if (jterm.getSelectedIndex() == 0) {
                            JOptionPane.showMessageDialog(null, "Kindly Select Term");
                        } else {
                            if (jstream.getSelectedIndex() == 0) {
                                JOptionPane.showMessageDialog(null, "Kindly Select Stream");
                            } else {
                                if (jacademicyear.getSelectedIndex() == 0) {
                                    JOptionPane.showMessageDialog(null, "Kindly Select Academic Year");
                                } else {
                                    if (jsubject.getSelectedIndex() == 0) {
                                        JOptionPane.showMessageDialog(null, "Kindly Select Subject");
                                    } else {
                                        if (jsubjectcode.getSelectedIndex() == 0) {
                                            JOptionPane.showMessageDialog(null, "Kindly Select Subject Code");
                                        } else {
                                            if (jexamname.getSelectedIndex() == 0) {
                                                JOptionPane.showMessageDialog(null, "Kindly Select Exam Name");
                                            } else {
                                                if (jexamcode.getSelectedIndex() == 0) {
                                                    JOptionPane.showMessageDialog(null, "Kindly Select Exam Code");
                                                } else {
                                                    if (Globals.admissionVerifier(jadmnumber.getText())) {

                                                        if (jscore.getText().isEmpty() || !DataValidation.number(jscore.getText())) {
                                                            JOptionPane.showMessageDialog(null, "Invalid Score");

                                                        } else {
                                                            if (joutof.getText().isEmpty() || !DataValidation.number(joutof.getText())) {
                                                                JOptionPane.showMessageDialog(null, "Invalid Total Possible Score Value(OUT OF VALUE)");
                                                            } else {

                                                                try {
                                                                    if (entryMode.isSelected()) {
                                                                        PartialMarksSaver(jadmnumber.getText(), jsubjectcode.getSelectedItem().toString(), Globals.termcode(jterm.getSelectedItem().toString()), jacademicyear.getSelectedItem().toString(), jscore.getText(), jexamcode.getSelectedItem().toString());

                                                                    } else {
                                                                        marksSaver(jadmnumber.getText(), jsubjectcode.getSelectedItem().toString(), Globals.termcode(jterm.getSelectedItem().toString()), jacademicyear.getSelectedItem().toString(), jscore.getText(), joutof.getText(), jpercent.getText(), jconvertedscore.getText(), String.valueOf(weight), jgrade.getText(), jpoints.getText(), jteacherintial.getText(), Globals.classCode(jclass1.getSelectedItem().toString()), jstream.getSelectedItem().toString(), jexamname.getSelectedItem().toString(), jexamcode.getSelectedItem().toString());

                                                                    }


                                                                    if (result.next()) {
                                                                        jadmnumber.setText(result.getString("admNumber"));
                                                                        studentName.setText(result.getString("firstName") + "    " + result.getString("Middlename") + "    " + result.getString("LastName"));
                                                                        String subjectcode = jsubjectcode.getSelectedItem().toString();
                                                                        String imageurl = result.getString("ImageLocation");
                                                                        if (imageurl.isEmpty()) {
                                                                            studentimage.setText("No Image");
                                                                            studentimage.setIcon(null);
                                                                        } else {
                                                                            studentimage.setIcon(resizeImage(result.getString("ImageLocation")));
                                                                            studentimage.setText("");
                                                                        }
                                                                        String examcode1 = jexamcode.getSelectedItem().toString();
                                                                        String adm = jadmnumber.getText();
                                                                        if (entryMode.isSelected()) {


                                                                            String sq = "Select * from partialSubjectMark where subjectcode='" + subjectcode + "' and academicyear='" + jacademicyear.getSelectedItem() + "' and examcode='" + jexamcode.getSelectedItem() + "' and admnumber='" + adm + "' and paper='" + selectedPaper + "'";
                                                                            ps = con.prepareStatement(sq);
                                                                            rs = ps.executeQuery();
                                                                            if (rs.next()) {

                                                                                jscore.setText(rs.getString("score"));

                                                                            } else {

                                                                                jpoints.setText("");
                                                                                jconvertedscore.setText("");
                                                                                jscore.setText("");

                                                                                jpercent.setText("");
                                                                                jgrade.setText("");
                                                                            }

                                                                        } else {


                                                                            String sq = "Select * from markstable where subjectcode='" + subjectcode + "' and academicyear='" + jacademicyear.getSelectedItem() + "' and examcode='" + jexamcode.getSelectedItem() + "' and admnumber='" + adm + "'";
                                                                            ps = con.prepareStatement(sq);
                                                                            rs = ps.executeQuery();
                                                                            if (rs.next()) {
                                                                                jpoints.setText(rs.getString("examPoints"));
                                                                                jconvertedscore.setText(rs.getString("convertedscore"));
                                                                                jscore.setText(rs.getString("examscore"));
                                                                                joutof.setText(rs.getString("Examoutof"));
                                                                                jpercent.setText(rs.getString("Exampercentage"));
                                                                                jgrade.setText(rs.getString("examgrade"));
                                                                            } else {

                                                                                jpoints.setText("");
                                                                                jconvertedscore.setText("");
                                                                                jscore.setText("");

                                                                                jpercent.setText("");
                                                                                jgrade.setText("");
                                                                            }

                                                                        }
                                                                    } else {
                                                                        JOptionPane.showMessageDialog(null, "End Of List");
                                                                    }

                                                                } catch (Exception sq) {
                                                                    sq.printStackTrace();
                                                                    JOptionPane.showMessageDialog(null, sq.getMessage());
                                                                }
                                                            }
                                                        }

                                                    } else {
                                                        JOptionPane.showMessageDialog(CurrentFrame.mainFrame(), "Invalid admission Number,Kindly Check");
                                                    }

                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

            }

            public void keyReleased(KeyEvent key) {
                char c = key.getKeyChar();

                if (Character.isDigit(c) || c == KeyEvent.VK_BACK_SPACE || c == KeyEvent.VK_DELETE) {

                    if (jscore.getText().isEmpty() || joutof.getText().isEmpty() || jscore.getText().contains("-")) {
                        jpoints.setText("");
                        jgrade.setText("");
                        jconvertedscore.setText("");
                        jpercent.setText("");
                    } else {
                        try {
                            NumberFormat nf = NumberFormat.getInstance();
                            nf.setMinimumFractionDigits(0);
                            String gr = "";
                            String subjectcode = jsubjectcode.getSelectedItem().toString();
                            String classcode = Globals.classCode(jclass1.getSelectedItem().toString());
                            String examcode1 = jexamcode.getSelectedItem().toString();
                            int score = Integer.parseInt(jscore.getText());
                            int examtototal = Integer.parseInt(joutof.getText());
                            int perce = (score * 100) / examtototal;
                            if (score > examtototal) {
                                getToolkit().beep();
                                JOptionPane.showMessageDialog(null, "Score Not Allowed");

                                jscore.setText("");
                                jpoints.setText("");
                                jgrade.setText("");
                                jpercent.setText("");
                                jconvertedscore.setText("");
                            } else {
                                if (entryMode.isSelected()) {

                                } else {


                                    jpercent.setText(String.valueOf(perce));
                                    String sql = "Select grade,end_at,start_from from subjectgrading where classcode='" + classcode + "' and subjectcode='" + subjectcode + "' and '" + perce + "'>=start_from and '" + perce + "'<=end_at  group by sortcode";
                                    ps = con.prepareStatement(sql);
                                    rs = ps.executeQuery();
                                    if (rs.next()) {

                                        jgrade.setText(rs.getString("grade"));
                                        String qq = "Select points from points_for_each_grade where grade='" + rs.getString("grade") + "'";
                                        ps = con.prepareStatement(qq);
                                        rs = ps.executeQuery();
                                        if (rs.next()) {
                                            jpoints.setText(rs.getString("points"));
                                        }
                                    }

                                    String sql2 = "Select weight from examweights where examcode='" + examcode1 + "'";
                                    ps = con.prepareStatement(sql2);
                                    rs = ps.executeQuery();
                                    if (rs.next()) {

                                        weight = rs.getInt("Weight");
                                    }
                                    double converted = (score * weight) / examtototal;
                                    jconvertedscore.setText(String.valueOf(nf.format(converted)));

                                }
                            }

                        } catch (Exception sq) {
                            sq.printStackTrace();
                            JOptionPane.showMessageDialog(null, sq.getMessage());
                        }
                    }

                } else {
                    key.consume();
                }
            }
        });

        jscore.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {

                if (jclass1.getSelectedIndex() == 0) {
                    JOptionPane.showMessageDialog(null, "Kindly Select Class");
                } else {
                    if (jterm.getSelectedIndex() == 0) {
                        JOptionPane.showMessageDialog(null, "Kindly Select Term");
                    } else {
                        if (jstream.getSelectedIndex() == 0) {
                            JOptionPane.showMessageDialog(null, "Kindly Select Stream");
                        } else {
                            if (jacademicyear.getSelectedIndex() == 0) {
                                JOptionPane.showMessageDialog(null, "Kindly Select Academic Year");
                            } else {
                                if (jsubject.getSelectedIndex() == 0) {
                                    JOptionPane.showMessageDialog(null, "Kindly Select Subject");
                                } else {
                                    if (jsubjectcode.getSelectedIndex() == 0) {
                                        JOptionPane.showMessageDialog(null, "Kindly Select Subject Code");
                                    } else {
                                        if (jexamname.getSelectedIndex() == 0) {
                                            JOptionPane.showMessageDialog(null, "Kindly Select Exam Name");
                                        } else {
                                            if (jexamcode.getSelectedIndex() == 0) {
                                                JOptionPane.showMessageDialog(null, "Kindly Select Exam Code");
                                            } else {
                                                String subjectcode = jsubjectcode.getSelectedItem().toString();
                                                String classcode = Globals.classCode(jclass1.getSelectedItem().toString());
                                                String Streamcode = Globals.streamcode(jstream.getSelectedItem().toString());
                                                if (Globals.Level.equalsIgnoreCase("Admin")) {
                                                    try {
                                                        String adm = jadmnumber.getText();
                                                        if (entryMode.isSelected()) {

                                                            String sq = "Select * from partialSubjectMark where subjectcode='" + subjectcode + "' and academicyear='" + jacademicyear.getSelectedItem() + "' and examcode='" + jexamcode.getSelectedItem() + "' and admnumber='" + adm + "' and paper='" + selectedPaper + "'";
                                                            ps = con.prepareStatement(sq);
                                                            rs = ps.executeQuery();
                                                            //   tableresult=rs;
                                                            if (rs.next()) {

                                                                jscore.setText(rs.getString("score"));

                                                            }


                                                        } else {

                                                            String sq = "Select * from markstable where subjectcode='" + subjectcode + "' and academicyear='" + jacademicyear.getSelectedItem() + "' and examcode='" + jexamcode.getSelectedItem() + "' and admnumber='" + adm + "'";
                                                            ps = con.prepareStatement(sq);
                                                            rs = ps.executeQuery();
                                                            //   tableresult=rs;
                                                            if (rs.next()) {
                                                                jpoints.setText(rs.getString("examPoints"));
                                                                jconvertedscore.setText(rs.getString("convertedscore"));
                                                                jscore.setText(rs.getString("examscore"));
                                                                joutof.setText(rs.getString("Examoutof"));
                                                                jpercent.setText(rs.getString("Exampercentage"));
                                                                jgrade.setText(rs.getString("examgrade"));
                                                            }
                                                        }
                                                /*   else{
                                                      jpoints.setText("");
                                                     jconvertedscore.setText("");
                                                     jscore.setText("");
                                                     joutof.setText("");
                                                     jpercent.setText("");
                                                     jgrade.setText("");
                                                 }*/
                                                        String sqla = "Select firstname,initials,teachercode from subjectrights,staffs where classcode='" + classcode + "' and subjectcode='" + subjectcode + "'  and  streamcode='" + Streamcode + "' and teachercode=staffs.employeecode";
                                                        ps = con.prepareStatement(sqla);
                                                        rs = ps.executeQuery();
                                                        if (rs.next()) {
                                                            jteacherintial.setText(rs.getString("Initials"));
                                                            jteachername.setText(rs.getString("firstname"));
                                                        } else {

                                                        }

                                                    } catch (Exception sq) {
                                                        sq.printStackTrace();
                                                        JOptionPane.showMessageDialog(null, sq.getMessage());
                                                    }
                                                    if (joutof.getText().isEmpty()) {
                                                        JOptionPane.showMessageDialog(null, "Fill The Out Of Field For Conversion");
                                                        jscore.setEnabled(false);
                                                    } else {
                                                        jscore.setEnabled(true);
                                                    }
                                                } else {
                                                    if (RightsAnnouncer.subjectMarksEntry(subjectcode, classcode, Streamcode, Globals.empcode)) {
                                                        try {
                                                            String adm = jadmnumber.getText();
                                                            String sq = "Select * from markstable where subjectcode='" + subjectcode + "' and academicyear='" + jacademicyear.getSelectedItem() + "' and examcode='" + jexamcode.getSelectedItem() + "' and admnumber='" + adm + "'";
                                                            ps = con.prepareStatement(sq);
                                                            rs = ps.executeQuery();
                                                            tableresult = rs;
                                                            if (rs.next()) {
                                                                jpoints.setText(rs.getString("examPoints"));
                                                                jconvertedscore.setText(rs.getString("convertedscore"));
                                                                jscore.setText(rs.getString("examscore"));
                                                                joutof.setText(rs.getString("Examoutof"));
                                                                jpercent.setText(rs.getString("Exampercentage"));
                                                                jgrade.setText(rs.getString("examgrade"));
                                                            }
                                                       /*   else{
                                                      jpoints.setText("");
                                                     jconvertedscore.setText("");
                                                     jscore.setText("");
                                                     joutof.setText("");
                                                     jpercent.setText("");
                                                     jgrade.setText("");
                                                 }*/

                                                            String sqla = "Select initials,teachercode,username from subjectrights,staffs,useraccounts where classcode='" + classcode + "' and subjectcode='" + subjectcode + "'  and   streamcode='" + Streamcode + "' and teachercode=staffs.employeecode and staffs.Employeecode=useraccounts.Employeecode";
                                                            ps = con.prepareStatement(sqla);
                                                            rs = ps.executeQuery();
                                                            if (rs.next()) {
                                                                jteacherintial.setText(rs.getString("Initials"));
                                                                jteachername.setText(rs.getString("UserName"));
                                                            } else {
                                                                jteacherintial.setText(Globals.initials);
                                                                jteachername.setText("");
                                                            }

                                                        } catch (Exception sq) {
                                                            sq.printStackTrace();
                                                            JOptionPane.showMessageDialog(null, sq.getMessage());
                                                        }
                                                        jscore.setEnabled(true);
                                                        if (joutof.getText().isEmpty()) {
                                                            JOptionPane.showMessageDialog(null, "Fill The Out Of Field For Conversion");
                                                            jscore.setEnabled(false);
                                                        } else {
                                                            jscore.setEnabled(true);
                                                        }
                                                    } else {
                                                        jscore.setEnabled(false);
                                                        JOptionPane.showMessageDialog(null, "You Are Not Allowed To Enter Marks In This Class Stream Subject Combination\n Consult Admin For Assistance");
                                                    }
                                                }

                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

            }

        });

        jadmnumber.addKeyListener(new KeyAdapter() {

            public void keyTyped(KeyEvent key) {
                char c = key.getKeyChar();

                if (Character.isAlphabetic(c)) {
                    key.consume();
                }
            }

            public void keyReleased(KeyEvent key) {

                try {
                    String sql = "Select firstname,middlename,lastname,currentform,ImageLocation,currentstream from admission where admissionnumber='" + jadmnumber.getText() + "'";
                    ps = con.prepareStatement(sql);
                    rs = ps.executeQuery();
                    if (rs.next()) {

                        jadmnumber.setForeground(Color.black);
                        studentName.setText(rs.getString("FirstName") + "    " + rs.getString("MiddleName") + "    " + rs.getString("LastName"));
                        jclass1.setSelectedItem(Globals.className(rs.getString("CurrentForm")));
                        jstream.setSelectedItem(Globals.streamName(rs.getString("CurrentStream")));
                        studentimage.setIcon(resizeImage(rs.getString("ImageLocation")));

                    } else {
                        jadmnumber.setForeground(Color.red);
                        studentName.setText("");
                        studentimage.setText("No Image");

                    }

                } catch (Exception sq) {
                    sq.printStackTrace();
                    JOptionPane.showMessageDialog(null, sq.getMessage());
                }

            }
        });
        tab.setForeground(Color.blue);

        tab.setFont(new Font("serif", Font.BOLD, 14));
        tab.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (tab.getSelectedRowCount() > 0) {
                    int row = tab.getSelectedRow();
                    try {
                        result.absolute(row + 1);


                        jadmnumber.setText(result.getString("admNumber"));
                        studentName.setText(result.getString("firstName") + "    " + result.getString("Middlename") + "    " + result.getString("LastName"));
                        String subjectcode = jsubjectcode.getSelectedItem().toString();
                        String imageurl = result.getString("ImageLocation");
                        if (imageurl.isEmpty()) {
                            studentimage.setText("No Image");
                            studentimage.setIcon(null);
                        } else {
                            studentimage.setIcon(resizeImage(result.getString("ImageLocation")));
                            studentimage.setText("");
                        }
                        String examcode1 = jexamcode.getSelectedItem().toString();
                        String adm = jadmnumber.getText();
                        if (entryMode.isSelected()) {


                            String sq = "Select * from partialSubjectMark where subjectcode='" + subjectcode + "' and academicyear='" + jacademicyear.getSelectedItem() + "' and examcode='" + jexamcode.getSelectedItem() + "' and admnumber='" + adm + "' and paper='" + selectedPaper + "'";
                            ps = con.prepareStatement(sq);
                            rs = ps.executeQuery();
                            if (rs.next()) {

                                jscore.setText(rs.getString("score"));

                            } else {

                                jpoints.setText("");
                                jconvertedscore.setText("");
                                jscore.setText("");

                                jpercent.setText("");
                                jgrade.setText("");

                            }


                        } else {


                            String sq = "Select * from markstable where subjectcode='" + subjectcode + "' and academicyear='" + jacademicyear.getSelectedItem() + "' and examcode='" + jexamcode.getSelectedItem() + "' and admnumber='" + adm + "'";
                            ps = con.prepareStatement(sq);
                            rs = ps.executeQuery();
                            if (rs.next()) {
                                jpoints.setText(rs.getString("examPoints"));
                                jconvertedscore.setText(rs.getString("convertedscore"));
                                jscore.setText(rs.getString("examscore"));
                                joutof.setText(rs.getString("Examoutof"));
                                jpercent.setText(rs.getString("Exampercentage"));
                                jgrade.setText(rs.getString("examgrade"));
                            } else {

                                jpoints.setText("");
                                jconvertedscore.setText("");
                                jscore.setText("");

                                jpercent.setText("");
                                jgrade.setText("");

                            }


                        }


                    } catch (Exception rr) {
                        rr.printStackTrace();
                    }

                }

            }

        });


        tab.addKeyListener(new KeyAdapter() {

            @Override
            public void keyReleased(KeyEvent e) {
                char c = e.getKeyChar();
                if (e.getKeyCode() == KeyEvent.VK_DOWN || e.getKeyCode() == KeyEvent.VK_UP) {
                    if (tab.getSelectedRowCount() > 0) {
                        int row = tab.getSelectedRow() + 1;


                        try {


                            result.absolute(row);


                            jadmnumber.setText(result.getString("admNumber"));
                            studentName.setText(result.getString("firstName") + "    " + result.getString("Middlename") + "    " + result.getString("LastName"));
                            String subjectcode = jsubjectcode.getSelectedItem().toString();
                            String imageurl = result.getString("ImageLocation");
                            if (imageurl.isEmpty()) {
                                studentimage.setText("No Image");
                                studentimage.setIcon(null);
                            } else {
                                studentimage.setIcon(resizeImage(result.getString("ImageLocation")));
                                studentimage.setText("");
                            }
                            String examcode1 = jexamcode.getSelectedItem().toString();
                            String adm = jadmnumber.getText();
                            if (entryMode.isSelected()) {


                                String sq = "Select * from partialSubjectMark where subjectcode='" + subjectcode + "' and academicyear='" + jacademicyear.getSelectedItem() + "' and examcode='" + jexamcode.getSelectedItem() + "' and admnumber='" + adm + "' and paper='" + selectedPaper + "'";
                                ps = con.prepareStatement(sq);
                                rs = ps.executeQuery();
                                if (rs.next()) {

                                    jscore.setText(rs.getString("score"));

                                } else {

                                    jpoints.setText("");
                                    jconvertedscore.setText("");
                                    jscore.setText("");

                                    jpercent.setText("");
                                    jgrade.setText("");

                                }


                            } else {


                                String sq = "Select * from markstable where subjectcode='" + subjectcode + "' and academicyear='" + jacademicyear.getSelectedItem() + "' and examcode='" + jexamcode.getSelectedItem() + "' and admnumber='" + adm + "'";
                                ps = con.prepareStatement(sq);
                                rs = ps.executeQuery();
                                if (rs.next()) {
                                    jpoints.setText(rs.getString("examPoints"));
                                    jconvertedscore.setText(rs.getString("convertedscore"));
                                    jscore.setText(rs.getString("examscore"));
                                    joutof.setText(rs.getString("Examoutof"));
                                    jpercent.setText(rs.getString("Exampercentage"));
                                    jgrade.setText(rs.getString("examgrade"));
                                } else {

                                    jpoints.setText("");
                                    jconvertedscore.setText("");
                                    jscore.setText("");

                                    jpercent.setText("");
                                    jgrade.setText("");

                                }


                            }


                        } catch (Exception rr) {
                            rr.printStackTrace();
                        }


                    }

                }

            }

        });

        entryMode.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (entryMode.isSelected()) {
                    jpoints.setEnabled(false);
                    jconvertedscore.setEnabled(false);


                    jpercent.setEnabled(false);
                    jgrade.setEnabled(false);
                    joutof.setEnabled(false);
                    if (selectedPaper.isEmpty() && jsubject.getSelectedIndex() > 0) {
                        displayPaperSelectionWidow();
                    }
                } else {

                    jpoints.setEnabled(true);
                    jconvertedscore.setEnabled(true);


                    jpercent.setEnabled(true);
                    jgrade.setEnabled(true);
                    joutof.setEnabled(true);

                }
            }
        });

    }

    public static void main(String[] args) {
        new MarksEntry();
    }

    public void displayPaperSelectionWidow() {
        CurrentFrame.secondFrame().setEnabled(false);
        JDialog dia = new JDialog();
        dia.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        // dia.setAlwaysOnTop(true);
        dia.setSize(350, 200);
        dia.setTitle("Select Paper details");
        dia.setLocationRelativeTo(CurrentFrame.secondFrame());
        dia.setLayout(new MigLayout());
        dia.setIconImage(FrameProperties.icon());

        dia.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        papers.removeAllItems();
        //papers.addItem("Select Paper");
        papers.addItem("1");
        papers.addItem("2");
        papers.addItem("3");
        papers.setBorder(new TitledBorder("Select Subject Paper"));
        FredButton set = new FredButton("Accept");
        dia.add(papers, "gapleft 100,gaptop 50,Wrap");
        dia.add(set, "gapleft 100");
        dia.setVisible(true);
        set.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {


                String sub = jsubjectcode.getSelectedItem().toString();


                selectedPaper = papers.getSelectedItem().toString();
                String totalValue = "";

                try {

                    PreparedStatement px = con.prepareStatement("Select * from Subjectcombinationrules where subjectcode='" + sub + "' and paper='" + selectedPaper + "'");
                    ResultSet rx = px.executeQuery();
                    if (rx.next()) {
                        totalValue = rx.getString("totalPossible");
                        joutof.setText(totalValue);

                        JOptionPane.showMessageDialog(CurrentFrame.secondFrame(), "Paper Set successfully,\n Proceed To Select Exam To Enter " + jsubject.getSelectedItem() + " Paper " + selectedPaper + " marks");
                        CurrentFrame.secondFrame().setEnabled(true);
                        dia.dispose();
                    } else {
                        JOptionPane.showMessageDialog(CurrentFrame.secondFrame(), "Kindly Select A Valid Examination Paper \n The selected paper is not Valid for this Subject");
                    }

                } catch (Exception sq) {
                    sq.printStackTrace();
                    JOptionPane.showMessageDialog(null, sq.getMessage());
                }


            }
        });


    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object obj = e.getSource();
        if (obj == cancel) {

            CurrentFrame.currentWindow();
            dispose();
        } else if (obj == jstream) {
            jsubject.removeActionListener(this);
            jsubjectcode.removeActionListener(this);
            jsubject.setSelectedIndex(0);
            jsubjectcode.setSelectedIndex(0);
            jsubject.addActionListener(this);
            jsubjectcode.addActionListener(this);
        }
        if (obj == missing) {
            if (jclass1.getSelectedIndex() == 0) {
                JOptionPane.showMessageDialog(null, "Kindly Select Class");
            } else {
                if (jterm.getSelectedIndex() == 0) {
                    JOptionPane.showMessageDialog(null, "Kindly Select Term");
                } else {
                    if (jstream.getSelectedIndex() == 0) {
                        JOptionPane.showMessageDialog(null, "Kindly Select Stream");
                    } else {
                        if (jacademicyear.getSelectedIndex() == 0) {
                            JOptionPane.showMessageDialog(null, "Kindly Select Academic Year");
                        } else {
                            if (jsubject.getSelectedIndex() == 0) {
                                JOptionPane.showMessageDialog(null, "Kindly Select Subject");
                            } else {
                                if (jsubjectcode.getSelectedIndex() == 0) {
                                    JOptionPane.showMessageDialog(null, "Kindly Select Subject Code");
                                } else {
                                    if (jexamname.getSelectedIndex() == 0) {
                                        JOptionPane.showMessageDialog(null, "Kindly Select Exam Name");
                                    } else {
                                        if (jexamcode.getSelectedIndex() == 0) {
                                            JOptionPane.showMessageDialog(null, "Kindly Select Exam Code");
                                        } else {
                                            String sort = "";
                                            if (admcheck.isSelected()) {
                                                sort = "admNumber";
                                            } else {
                                                if (entryMode.isSelected()) {
                                                    sort = "Score";
                                                } else {
                                                    sort = "Examscore";
                                                }
                                            }
                                            if (entryMode.isSelected()) {
                                                ReportGenerator.partialMissingMarks(jexamcode.getSelectedItem().toString(), jexamname.getSelectedItem().toString(), jstream.getSelectedItem().toString(), jterm.getSelectedItem().toString(), jacademicyear.getSelectedItem().toString(), jsubjectcode.getSelectedItem().toString(), jclass1.getSelectedItem().toString(), jsubject.getSelectedItem().toString(), selectedPaper);

                                            } else {

                                                ReportGenerator.missingMarks(jexamcode.getSelectedItem().toString(), jexamname.getSelectedItem().toString(), jstream.getSelectedItem().toString(), jterm.getSelectedItem().toString(), jacademicyear.getSelectedItem().toString(), jsubjectcode.getSelectedItem().toString(), jclass1.getSelectedItem().toString(), jsubject.getSelectedItem().toString());

                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } else if (obj == marks) {
            if (jclass1.getSelectedIndex() == 0) {
                JOptionPane.showMessageDialog(null, "Kindly Select Class");
            } else {
                if (jterm.getSelectedIndex() == 0) {
                    JOptionPane.showMessageDialog(null, "Kindly Select Term");
                } else {
                    if (jstream.getSelectedIndex() == 0) {
                        JOptionPane.showMessageDialog(null, "Kindly Select Stream");
                    } else {
                        if (jacademicyear.getSelectedIndex() == 0) {
                            JOptionPane.showMessageDialog(null, "Kindly Select Academic Year");
                        } else {
                            if (jsubject.getSelectedIndex() == 0) {
                                JOptionPane.showMessageDialog(null, "Kindly Select Subject");
                            } else {
                                if (jsubjectcode.getSelectedIndex() == 0) {
                                    JOptionPane.showMessageDialog(null, "Kindly Select Subject Code");
                                } else {
                                    if (jexamname.getSelectedIndex() == 0) {
                                        JOptionPane.showMessageDialog(null, "Kindly Select Exam Name");
                                    } else {
                                        if (jexamcode.getSelectedIndex() == 0) {
                                            JOptionPane.showMessageDialog(null, "Kindly Select Exam Code");
                                        } else {
                                            String sort = "";
                                            if (admcheck.isSelected()) {
                                                sort = "admNumber";
                                            } else {
                                                if (entryMode.isSelected()) {
                                                    sort = "Score";
                                                } else {
                                                    sort = "Examscore";
                                                }
                                            }
                                            if (entryMode.isSelected()) {
                                                ReportGenerator.partialSubjectResults(jexamcode.getSelectedItem().toString(), jexamname.getSelectedItem().toString(), jstream.getSelectedItem().toString(), jterm.getSelectedItem().toString(), jacademicyear.getSelectedItem().toString(), jsubjectcode.getSelectedItem().toString(), jclass1.getSelectedItem().toString(), jsubject.getSelectedItem().toString(), sort, selectedPaper);

                                            } else {


                                                ReportGenerator.subjectResults(jexamcode.getSelectedItem().toString(), jexamname.getSelectedItem().toString(), jstream.getSelectedItem().toString(), jterm.getSelectedItem().toString(), jacademicyear.getSelectedItem().toString(), jsubjectcode.getSelectedItem().toString(), jclass1.getSelectedItem().toString(), jsubject.getSelectedItem().toString(), sort);
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

        }

        if (obj == delete) {
            if (jclass1.getSelectedIndex() == 0) {
                JOptionPane.showMessageDialog(null, "Kindly Select Class");
            } else {
                if (jterm.getSelectedIndex() == 0) {
                    JOptionPane.showMessageDialog(null, "Kindly Select Term");
                } else {
                    if (jstream.getSelectedIndex() == 0) {
                        JOptionPane.showMessageDialog(null, "Kindly Select Stream");
                    } else {
                        if (jacademicyear.getSelectedIndex() == 0) {
                            JOptionPane.showMessageDialog(null, "Kindly Select Academic Year");
                        } else {
                            if (jsubject.getSelectedIndex() == 0) {
                                JOptionPane.showMessageDialog(null, "Kindly Select Subject");
                            } else {
                                if (jsubjectcode.getSelectedIndex() == 0) {
                                    JOptionPane.showMessageDialog(null, "Kindly Select Subject Code");
                                } else {
                                    if (jexamname.getSelectedIndex() == 0) {
                                        JOptionPane.showMessageDialog(null, "Kindly Select Exam Name");
                                    } else {
                                        if (jexamcode.getSelectedIndex() == 0) {
                                            JOptionPane.showMessageDialog(null, "Kindly Select Exam Code");
                                        } else {
                                            if (Globals.admissionVerifier(jadmnumber.getText())) {

                                                if (jscore.getText().isEmpty()) {
                                                    JOptionPane.showMessageDialog(null, "Cannot Delete Empty Mark");

                                                } else {

                                                    if (entryMode.isSelected()) {
                                                        PartialmarksDelete(jadmnumber.getText(), jsubjectcode.getSelectedItem().toString(), Globals.termcode(jterm.getSelectedItem().toString()), jacademicyear.getSelectedItem().toString(), Globals.classCode(jclass1.getSelectedItem().toString()), jstream.getSelectedItem().toString(), jexamname.getSelectedItem().toString(), jexamcode.getSelectedItem().toString());


                                                    } else {

                                                        marksDelete(jadmnumber.getText(), jsubjectcode.getSelectedItem().toString(), Globals.termcode(jterm.getSelectedItem().toString()), jacademicyear.getSelectedItem().toString(), Globals.classCode(jclass1.getSelectedItem().toString()), jstream.getSelectedItem().toString(), jexamname.getSelectedItem().toString(), jexamcode.getSelectedItem().toString());

                                                    }

                                                }

                                            } else {
                                                JOptionPane.showMessageDialog(CurrentFrame.mainFrame(), "Invalid admission Number,Kindly Check");
                                            }

                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } else if (obj == save) {
            if (jclass1.getSelectedIndex() == 0) {
                JOptionPane.showMessageDialog(null, "Kindly Select Class");
            } else {
                if (jterm.getSelectedIndex() == 0) {
                    JOptionPane.showMessageDialog(null, "Kindly Select Term");
                } else {
                    if (jstream.getSelectedIndex() == 0) {
                        JOptionPane.showMessageDialog(null, "Kindly Select Stream");
                    } else {
                        if (jacademicyear.getSelectedIndex() == 0) {
                            JOptionPane.showMessageDialog(null, "Kindly Select Academic Year");
                        } else {
                            if (jsubject.getSelectedIndex() == 0) {
                                JOptionPane.showMessageDialog(null, "Kindly Select Subject");
                            } else {
                                if (jsubjectcode.getSelectedIndex() == 0) {
                                    JOptionPane.showMessageDialog(null, "Kindly Select Subject Code");
                                } else {
                                    if (jexamname.getSelectedIndex() == 0) {
                                        JOptionPane.showMessageDialog(null, "Kindly Select Exam Name");
                                    } else {
                                        if (jexamcode.getSelectedIndex() == 0) {
                                            JOptionPane.showMessageDialog(null, "Kindly Select Exam Code");
                                        } else {
                                            if (Globals.admissionVerifier(jadmnumber.getText())) {
                                                if (jscore.getText().isEmpty() || !DataValidation.number(jscore.getText())) {
                                                    JOptionPane.showMessageDialog(null, "Invalid Score");

                                                } else {
                                                    if (joutof.getText().isEmpty() || !DataValidation.number(joutof.getText())) {
                                                        JOptionPane.showMessageDialog(this, "Invalid Total Possible Score Value(OUT OF VALUE)");
                                                    } else {

                                                        try {
                                                            if (entryMode.isSelected()) {
                                                                PartialMarksSaver(jadmnumber.getText(), jsubjectcode.getSelectedItem().toString(), Globals.termcode(jterm.getSelectedItem().toString()), jacademicyear.getSelectedItem().toString(), jscore.getText(), jexamcode.getSelectedItem().toString());

                                                            } else {

                                                                marksSaver(jadmnumber.getText(), jsubjectcode.getSelectedItem().toString(), Globals.termcode(jterm.getSelectedItem().toString()), jacademicyear.getSelectedItem().toString(), jscore.getText(), joutof.getText(), jpercent.getText(), jconvertedscore.getText(), String.valueOf(weight), jgrade.getText(), jpoints.getText(), jteacherintial.getText(), Globals.classCode(jclass1.getSelectedItem().toString()), jstream.getSelectedItem().toString(), jexamname.getSelectedItem().toString(), jexamcode.getSelectedItem().toString());
                                                            }
                                                            if (result.next()) {
                                                                jadmnumber.setText(result.getString("admNumber"));
                                                                studentName.setText(result.getString("firstName") + "    " + result.getString("Middlename") + "    " + result.getString("LastName"));
                                                                String subjectcode = jsubjectcode.getSelectedItem().toString();
                                                                String imageurl = result.getString("ImageLocation");
                                                                if (imageurl.isEmpty()) {
                                                                    studentimage.setText("No Image");
                                                                    studentimage.setIcon(null);
                                                                } else {
                                                                    studentimage.setIcon(resizeImage(result.getString("ImageLocation")));
                                                                    studentimage.setText("");
                                                                }
                                                                String examcode1 = jexamcode.getSelectedItem().toString();
                                                                String adm = jadmnumber.getText();
                                                                if (entryMode.isSelected()) {

                                                                    String sq = "Select * from partialSubjectMark where subjectcode='" + subjectcode + "' and academicyear='" + jacademicyear.getSelectedItem() + "' and examcode='" + jexamcode.getSelectedItem() + "' and admnumber='" + adm + "' and paper='" + selectedPaper + "'";
                                                                    ps = con.prepareStatement(sq);
                                                                    rs = ps.executeQuery();
                                                                    if (rs.next()) {

                                                                        jscore.setText(rs.getString("score"));

                                                                    } else {

                                                                        jpoints.setText("");
                                                                        jconvertedscore.setText("");
                                                                        jscore.setText("");

                                                                        jpercent.setText("");
                                                                        jgrade.setText("");

                                                                    }


                                                                } else {


                                                                    String sq = "Select * from markstable where subjectcode='" + subjectcode + "' and academicyear='" + jacademicyear.getSelectedItem() + "' and examcode='" + jexamcode.getSelectedItem() + "' and admnumber='" + adm + "'";
                                                                    ps = con.prepareStatement(sq);
                                                                    rs = ps.executeQuery();
                                                                    if (rs.next()) {
                                                                        jpoints.setText(rs.getString("examPoints"));
                                                                        jconvertedscore.setText(rs.getString("convertedscore"));
                                                                        jscore.setText(rs.getString("examscore"));
                                                                        joutof.setText(rs.getString("Examoutof"));
                                                                        jpercent.setText(rs.getString("Exampercentage"));
                                                                        jgrade.setText(rs.getString("examgrade"));
                                                                    } else {

                                                                        jpoints.setText("");
                                                                        jconvertedscore.setText("");
                                                                        jscore.setText("");

                                                                        jpercent.setText("");
                                                                        jgrade.setText("");

                                                                    }


                                                                }
                                                            } else {
                                                                JOptionPane.showMessageDialog(null, "End of List");
                                                            }

                                                        } catch (Exception sq) {
                                                            sq.printStackTrace();
                                                            JOptionPane.showMessageDialog(null, sq.getMessage());
                                                        }
                                                    }
                                                }

                                            } else {
                                                JOptionPane.showMessageDialog(CurrentFrame.mainFrame(), "Invalid admission Number,Kindly Check");
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

        } else if (obj == jsubject) {
            jsubjectcode.removeActionListener(this);
            try {
                String sq = "Select subjectcode from subjects where subjectname='" + jsubject.getSelectedItem() + "'";
                ps = con.prepareStatement(sq);
                rs = ps.executeQuery();
                while (rs.next()) {
                    jsubjectcode.setSelectedItem(rs.getString("subjectcode"));
                }

                String sql2 = "Select admnumber,firstname,middlename,ImageLocation,lastname from studentsubjectallocation,admission where admnumber=admissionNumber and currentform='" + Globals.classCode(ClassProgressTracker.currentClass(Integer.parseInt(jacademicyear.getSelectedItem().toString()), jclass1.getSelectedItem().toString())) + "' and currentstream='" + Globals.streamcode(jstream.getSelectedItem().toString()) + "' and subjectcode='" + jsubjectcode.getSelectedItem() + "' and academicyear='" + jacademicyear.getSelectedItem() + "' order by admnumber";
                ps = con.prepareStatement(sql2);
                result = ps.executeQuery();
                tableresult = result;
                if (result.next()) {
                    jadmnumber.setText(result.getString("admNumber"));

                    studentName.setText(result.getString("firstName") + "    " + result.getString("Middlename") + "    " + result.getString("LastName"));
                    String imageurl = result.getString("ImageLocation");

                    jpoints.setText("");

                    jconvertedscore.setText("");
                    jscore.setText("");

                    jpercent.setText("");
                    jgrade.setText("");

                    if (imageurl.isEmpty()) {
                        studentimage.setText("No Image");
                        studentimage.setIcon(null);
                    } else {
                        studentimage.setIcon(resizeImage(result.getString("ImageLocation")));
                        studentimage.setText(null);
                    }

                    model.getDataVector().removeAllElements();
                    while (tableresult.next()) {
                        cols[0] = tableresult.getString("AdmNumber");
                        cols[1] = tableresult.getString("firstName") + "    " + tableresult.getString("Middlename") + "    " + tableresult.getString("LastName");
                        cols[2] = "";
                        model.addRow(cols);
                    }
                    if (entryMode.isSelected()) {
                        displayPaperSelectionWidow();
                    }
                } else {
                    JOptionPane.showMessageDialog(this, "No Students Have Been Allocated To The Selected Subject , Class And Academic Year");
                    jsubjectcode.setSelectedIndex(0);
                    jsubject.setSelectedIndex(0);
                    jpoints.setText("");

                    jconvertedscore.setText("");
                    jscore.setText("");
                    jadmnumber.setText("");
                    jpercent.setText("");
                    jgrade.setText("");
                    studentName.setText("");
                }

            } catch (Exception sq) {
                sq.printStackTrace();
                JOptionPane.showMessageDialog(null, sq.getMessage());
            }
            jsubjectcode.addActionListener(this);


        } else if (obj == jsubjectcode) {
            jsubject.removeActionListener(this);
            try {
                String sq = "Select subjectname from subjects where subjectcode='" + jsubjectcode.getSelectedItem() + "'";
                ps = con.prepareStatement(sq);
                rs = ps.executeQuery();
                while (rs.next()) {
                    jsubject.setSelectedItem(rs.getString("subjectname"));
                }
                String sql2 = "Select admnumber,firstname,ImageLocation,middlename,lastname from studentsubjectallocation,admission where admnumber=admissionNumber and currentform='" + Globals.classCode(jclass1.getSelectedItem().toString()) + "' and currentstream='" + Globals.streamcode(jstream.getSelectedItem().toString()) + "' and subjectcode='" + jsubjectcode.getSelectedItem() + "' and academicyear='" + jacademicyear.getSelectedItem() + "' order by admnumber";
                ps = con.prepareStatement(sql2);
                result = ps.executeQuery();
                tableresult = result;
                if (result.next()) {
                    jadmnumber.setText(result.getString("admNumber"));
                    studentName.setText(result.getString("firstName") + "    " + result.getString("Middlename") + "    " + result.getString("LastName"));
                    String imageurl = result.getString("ImageLocation");
                    jpoints.setText("");

                    jconvertedscore.setText("");
                    jscore.setText("");

                    jpercent.setText("");
                    jgrade.setText("");

                    if (imageurl.isEmpty()) {
                        studentimage.setText("No Image");
                        studentimage.setIcon(null);
                    } else {
                        studentimage.setIcon(resizeImage(result.getString("ImageLocation")));
                        studentimage.setText("");
                    }
                    model.getDataVector().removeAllElements();
                    while (tableresult.next()) {
                        cols[0] = tableresult.getString("AdmNumber");
                        cols[1] = tableresult.getString("firstName") + "    " + tableresult.getString("Middlename") + "    " + tableresult.getString("LastName");
                        cols[2] = "";
                        model.addRow(cols);
                    }


                } else {
                    JOptionPane.showMessageDialog(this, "No Students Have Been Allocated To The Selected Subject , Class And Academic Year");
                    jsubjectcode.setSelectedIndex(0);
                    jsubject.setSelectedIndex(0);
                    jpoints.setText("");

                    jconvertedscore.setText("");
                    jscore.setText("");

                    jpercent.setText("");
                    jgrade.setText("");
                    studentName.setText("");
                    jadmnumber.setText("");
                }

                if (entryMode.isSelected()) {
                    displayPaperSelectionWidow();
                }
            } catch (Exception sq) {
                sq.printStackTrace();
                JOptionPane.showMessageDialog(null, sq.getMessage());
            }
            jsubject.addActionListener(this);
        } else if (obj == jterm) {
            if (jterm.getSelectedIndex() > 0) {
                tm = jterm.getSelectedItem().toString().toString();
            }
            jexamcode.removeActionListener(this);
            jexamname.removeActionListener(this);
            jexamcode.setSelectedIndex(0);
            jexamname.setSelectedIndex(0);
            jexamcode.addActionListener(this);
            jexamname.addActionListener(this);
            jsubject.removeActionListener(this);
            jsubjectcode.removeActionListener(this);
            jsubject.setSelectedIndex(0);
            jsubjectcode.setSelectedIndex(0);
            jsubject.addActionListener(this);
            jsubjectcode.addActionListener(this);
        } else if (obj == jclass1) {

            if (jadmnumber.getText().isEmpty()) {
                if (jclass1.getSelectedIndex() > 0) {
                    cl = jclass1.getSelectedItem().toString();
                }
                jexamcode.removeActionListener(this);
                jexamname.removeActionListener(this);
                jexamcode.setSelectedIndex(0);
                jexamname.setSelectedIndex(0);
                jsubject.removeActionListener(this);
                jsubjectcode.removeActionListener(this);
                jsubject.setSelectedIndex(0);
                jsubjectcode.setSelectedIndex(0);
                jsubject.addActionListener(this);
                jsubjectcode.addActionListener(this);
                jexamcode.addActionListener(this);
                jexamname.addActionListener(this);
            } else {

                if (jclass1.getSelectedIndex() > 0) {
                    jexamcode.removeActionListener(this);
                    jexamname.removeActionListener(this);
                    jexamcode.setSelectedIndex(0);
                    jexamname.setSelectedIndex(0);
                    jexamcode.addActionListener(this);
                    jexamname.addActionListener(this);
                    cl = jclass1.getSelectedItem().toString();
                }
            }
        } else if (obj == jacademicyear) {
            jsubject.removeActionListener(this);
            jsubjectcode.removeActionListener(this);
            jsubject.setSelectedIndex(0);
            jsubjectcode.setSelectedIndex(0);
            jsubject.addActionListener(this);
            jsubjectcode.addActionListener(this);
            if (jacademicyear.getSelectedIndex() > 0) {

                yr = jacademicyear.getSelectedItem().toString();
            }
            jexamcode.removeActionListener(this);
            jexamname.removeActionListener(this);
            jexamcode.setSelectedIndex(0);
            jexamname.setSelectedIndex(0);
            jexamcode.addActionListener(this);
            jexamname.addActionListener(this);
        } else if (obj == jexamname) {
            jexamcode.removeActionListener(this);
            if (jexamname.getSelectedIndex() > 0) {
                ex = jexamname.getSelectedItem().toString();

                String excode = ExamCodesGenerator.generatecode(cl, yr, tm, ex).toUpperCase();
                jexamcode.setSelectedItem(excode);
                try {
                    if (entryMode.isSelected()) {
                        String sql2 = "Select value from subjectcombinationrules  where paper='" + selectedPaper + "' and subjectcode='" + jsubjectcode.getSelectedItem() + "'";
                        ps = con.prepareStatement(sql2);
                        rs = ps.executeQuery();
                        if (rs.next()) {
                            advice.setText("The Student Score in " + jsubject.getSelectedItem() + " Paper " + selectedPaper + " Will Be Converted To X/" + rs.getString("value"));

                        }

                    } else {

                        String sql2 = "Select weight from examweights where examcode='" + excode + "'";
                        ps = con.prepareStatement(sql2);
                        rs = ps.executeQuery();
                        if (rs.next()) {
                            advice.setText("The Student Score Will Be Converted To X/" + rs.getString("weight"));

                        }

                    }


                    tableresult.beforeFirst();

                    model.getDataVector().removeAllElements();
                    while (tableresult.next()) {

                        String adm = tableresult.getString("AdmNumber");
                        cols[0] = tableresult.getString("AdmNumber");
                        cols[1] = tableresult.getString("firstName") + "    " + tableresult.getString("Middlename") + "    " + tableresult.getString("LastName");

                        if (entryMode.isSelected()) {
                            String sq = "Select score from partialSubjectMark where subjectcode='" + jsubjectcode.getSelectedItem() + "' and academicyear='" + jacademicyear.getSelectedItem() + "' and examcode='" + jexamcode.getSelectedItem() + "' and admnumber='" + adm + "' and paper='" + selectedPaper + "'";
                            ps = con.prepareStatement(sq);
                            rs = ps.executeQuery();
                            if (rs.next()) {
                                cols[2] = rs.getString("Score");
                            } else {
                                cols[2] = "";
                            }
                            model.addRow(cols);
                        } else {
                            String sq = "Select examscore from markstable where subjectcode='" + jsubjectcode.getSelectedItem() + "' and academicyear='" + jacademicyear.getSelectedItem() + "' and streamcode='" + Globals.streamcode(jstream.getSelectedItem().toString()) + "' and examcode='" + jexamcode.getSelectedItem() + "' and admnumber='" + adm + "'";
                            ps = con.prepareStatement(sq);
                            rs = ps.executeQuery();
                            if (rs.next()) {
                                cols[2] = rs.getString("ExamScore");
                            } else {
                                cols[2] = "";
                            }
                            model.addRow(cols);
                        }
                    }
                    tableresult.beforeFirst();
                    tab.revalidate();
                    tab.repaint();
                } catch (Exception sq) {
                    sq.printStackTrace();
                    JOptionPane.showMessageDialog(null, sq.getMessage());
                }
            }
            jexamcode.addActionListener(this);
        } else if (obj == jexamcode) {
            jexamname.removeActionListener(this);
            if (jexamcode.getSelectedIndex() > 0) {
                try {
                    String sql = "Select Examname from exams where examcode='" + jexamcode.getSelectedItem() + "'";
                    ps = con.prepareStatement(sql);
                    rs = ps.executeQuery();
                    while (rs.next()) {
                        jexamname.setSelectedItem(rs.getString("ExamName"));
                    }
                    String sql2 = "Select weight from examweights where examcode='" + jexamcode.getSelectedItem() + "'";
                    ps = con.prepareStatement(sql2);
                    rs = ps.executeQuery();
                    if (rs.next()) {
                        advice.setText("The Student Score Will Be Converted To X/" + rs.getString("weight"));

                    }


                    tableresult.first();

                    model.getDataVector().removeAllElements();
                    while (tableresult.next()) {
                        String adm = tableresult.getString("AdmNumber");
                        cols[0] = tableresult.getString("AdmNumber");
                        cols[1] = tableresult.getString("firstName") + "    " + tableresult.getString("Middlename") + "    " + tableresult.getString("LastName");

                        if (entryMode.isSelected()) {
                            String sq = "Select score from partialSubjectMark where subjectcode='" + jsubjectcode.getSelectedItem() + "' and academicyear='" + jacademicyear.getSelectedItem() + "' and examcode='" + jexamcode.getSelectedItem() + "' and admnumber='" + adm + "' and paper='" + selectedPaper + "'";
                            ps = con.prepareStatement(sq);
                            rs = ps.executeQuery();
                            if (rs.next()) {
                                cols[2] = rs.getString("Score");
                            } else {
                                cols[2] = "";
                            }
                            model.addRow(cols);
                        } else {
                            String sq = "Select examscore from markstable where subjectcode='" + jsubjectcode.getSelectedItem() + "' and academicyear='" + jacademicyear.getSelectedItem() + "' and streamcode='" + Globals.streamcode(jstream.getSelectedItem().toString()) + "' and examcode='" + jexamcode.getSelectedItem() + "' and admnumber='" + adm + "'";
                            ps = con.prepareStatement(sq);
                            rs = ps.executeQuery();
                            if (rs.next()) {
                                cols[2] = rs.getString("ExamScore");
                            } else {
                                cols[2] = "";
                            }
                            model.addRow(cols);
                        }
                    }

                    tab.revalidate();
                    tab.repaint();


                    jexamname.addActionListener(this);
                } catch (Exception sq) {
                    sq.printStackTrace();
                    JOptionPane.showMessageDialog(null, sq.getMessage());
                }
            }
        }

    }

    public synchronized void marksSaver(String adm, String subjectcode, String termcode, String year, String score, String outof, String percent, String convertedscore, String convertedscoreoutof, String grade, String point, String teachername, String classcode, String streamcode, String examname, String examcode) {

        try {
            String sq = "Select * from markstable where subjectcode='" + subjectcode + "' and academicyear='" + jacademicyear.getSelectedItem() + "' and examcode='" + jexamcode.getSelectedItem() + "' and admnumber='" + adm + "'";
            ps = con.prepareStatement(sq);
            rs = ps.executeQuery();
            if (rs.next()) {
                String sql2 = "update markstable set examscore='" + score + "',examoutof='" + outof + "',convertedscore='" + convertedscore + "',exampercentage='" + percent + "',examgrade='" + grade + "',exampoints='" + point + "' where academicyear='" + year + "' and subjectcode='" + subjectcode + "'  and admnumber='" + adm + "' and examcode='" + examcode + "'";
                ps = con.prepareStatement(sql2);
                ps.execute();
            } else {

                String sql = "Insert into markstable values('" + adm + "','" + subjectcode + "','" + classcode + "','" + Globals.streamcode(streamcode) + "','" + termcode + "','" + year + "','" + examname + "','" + examcode + "','" + score + "','" + outof + "','" + convertedscore + "','" + convertedscoreoutof + "','" + percent + "','" + grade + "','" + point + "','" + "1" + "','" + "1" + "','" + teachername + "')";
                ps = con.prepareStatement(sql);
                ps.execute();

            }
            if (result.getRow() < 1) {
                tab.setValueAt(score, 0, 2);
            } else {
                tab.setValueAt(score, result.getRow() - 1, 2);
            }

        } catch (Exception sq) {
            sq.printStackTrace();
            JOptionPane.showMessageDialog(null, sq.getMessage());
        }

    }

    public synchronized void PartialMarksSaver(String adm, String subjectcode, String termcode, String year, String score, String examcode) {

        try {
            String sq = "Select * from partialsubjectMark where subjectcode='" + subjectcode + "' and academicyear='" + jacademicyear.getSelectedItem() + "' and examcode='" + jexamcode.getSelectedItem() + "' and admnumber='" + adm + "' and paper='" + selectedPaper + "'";
            ps = con.prepareStatement(sq);
            rs = ps.executeQuery();
            if (rs.next()) {
                String sql2 = "update partialsubjectMark set score='" + score + "' where academicyear='" + year + "' and subjectcode='" + subjectcode + "'  and admnumber='" + adm + "' and examcode='" + examcode + "' and paper='" + selectedPaper + "'";
                ps = con.prepareStatement(sql2);
                ps.execute();
            } else {

                String sql = "Insert into partialsubjectMark (AdmNumber,subjectcode,termcode,academicyear,examcode,paper,score,classcode,streamcode,teacherInitials)  values('" + adm + "','" + subjectcode + "','" + termcode + "','" + year + "','" + examcode + "','" + selectedPaper + "','" + score + "','" + Globals.classCode(jclass1.getSelectedItem().toString()) + "','" + Globals.streamcode(jstream.getSelectedItem().toString()) + "','" + jteacherintial.getText() + "')";
                ps = con.prepareStatement(sql);
                ps.execute();

            }
            if (result.getRow() < 1) {
                tab.setValueAt(score, 0, 2);
            } else {
                tab.setValueAt(score, result.getRow() - 1, 2);
            }

        } catch (Exception sq) {
            sq.printStackTrace();
            JOptionPane.showMessageDialog(null, sq.getMessage());
        }

    }

    public synchronized void marksDelete(String adm, String subjectcode, String termcode, String year, String classcode, String streamcode, String examname, String examcode) {

        try {

            String sql2 = "Delete from markstable  where academicyear='" + year + "' and subjectcode='" + subjectcode + "'  and admnumber='" + adm + "' and examcode='" + examcode + "'";
            ps = con.prepareStatement(sql2);
            ps.execute();
            jscore.setText("");
            jconvertedscore.setText("");
            jgrade.setText("");
            jpercent.setText("");
            jpoints.setText("");
            joutof.setText("");
            if (result.getRow() < 1) {
                tab.setValueAt(score, 0, 2);
            } else {
                tab.setValueAt(score, result.getRow() - 1, 2);
            }

        } catch (Exception sq) {
            sq.printStackTrace();
            JOptionPane.showMessageDialog(null, sq.getMessage());
        }

    }


    public synchronized void PartialmarksDelete(String adm, String subjectcode, String termcode, String year, String classcode, String streamcode, String examname, String examcode) {

        try {

            String sql2 = "Delete from partialSubjectMark  where academicyear='" + year + "' and subjectcode='" + subjectcode + "'  and admnumber='" + adm + "' and examcode='" + examcode + "' and paper='" + selectedPaper + "'";
            ps = con.prepareStatement(sql2);
            ps.execute();
            jscore.setText("");
            jconvertedscore.setText("");
            jgrade.setText("");
            jpercent.setText("");
            jpoints.setText("");
            joutof.setText("");
            if (result.getRow() < 1) {
                tab.setValueAt(score, 0, 2);
            } else {
                tab.setValueAt(score, result.getRow() - 1, 2);
            }

        } catch (Exception sq) {
            sq.printStackTrace();
            JOptionPane.showMessageDialog(null, sq.getMessage());
        }

    }


    public ImageIcon resizeImage(String path) {
        ImageIcon iimage;
        iimage = new ImageIcon(path);
        Image image = iimage.getImage();

        Image newimage = image.getScaledInstance(100, 80, Image.SCALE_SMOOTH);
        ImageIcon ima = new ImageIcon(newimage);

        return ima;
    }
}
