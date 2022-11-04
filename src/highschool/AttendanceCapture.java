/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package highschool;


import java.awt.Color;
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
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JFrame;

import javax.swing.JOptionPane;
import javax.swing.JPanel;

import static javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE;

import javax.swing.border.TitledBorder;

import net.miginfocom.swing.MigLayout;

/**
 * @author EXAMSERVERPC
 */
public class AttendanceCapture extends JFrame implements ActionListener {

    private Connection con = DbConnection.connectDb();
    private int width = 800;
    private int height = 500;
    private PreparedStatement ps;
    private ResultSet rs;
    private FredLabel studentimage = new FredLabel("No Image");
    private FredCombo jclass = new FredCombo("Select Class");
    private ResultSet result = null;

    private FredLabel admnumber, term, stream, class1, studentName, date;
    private FredCombo jterm, jstream, jclass1;
    private FredTextField jadmnumber = new FredTextField();
    private FredDateChooser jdate = new FredDateChooser();
    private JPanel top = new JPanel();
    private JPanel bottom = new JPanel();
    private FredButton save = new FredButton("Save");
    private JPanel pane = new JPanel();
    private FredLabel week = new FredLabel(" Week");
    private FredTextField jweek = new FredTextField();
    private FredLabel state = new FredLabel("Attendance Status");
    private FredTextField jstate = new FredTextField("");
    private FredButton cancel = new FredButton("Close");
    private FredButton report = new FredButton("Show Attendance");

    public AttendanceCapture() {
        setSize(width, height);
        setTitle("Student Attendance Capture Register");
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
        setLayout(new MigLayout());
        setResizable(false);
        setLocationRelativeTo(null);
        admnumber = new FredLabel("admission Number");
        term = new FredLabel("Term");
        stream = new FredLabel("Stream");

        class1 = new FredLabel("Class");

        jterm = new FredCombo("Term");
        jstream = new FredCombo("Stream");

        jclass1 = new FredCombo("Class");
        date = new FredLabel("Date");
        studentName = new FredLabel("Student Name");

        jdate.setDateFormatString("yyyy/MM/dd");
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
        } catch (Exception sq) {
            sq.printStackTrace();
            JOptionPane.showMessageDialog(this, sq.getMessage());
        }

        add(top, "grow,push,wrap");
        add(bottom, "grow,push");
        bottom.setBackground(Color.cyan);
        top.setLayout(new MigLayout());
        bottom.setLayout(new MigLayout());
        pane.setBackground(Color.cyan);
        pane.setBorder(new TitledBorder("Student Image"));
        pane.setLayout(new MigLayout());
        pane.add(studentimage, "grow,push");
        top.add(admnumber, "growx,pushx,gapright 20,gaptop 0");
        top.add(jadmnumber, "growx,pushx,gapright 20,gaptop 0");
        top.add(studentName, "growx,pushx,gapright 20,gaptop 0");
        top.add(pane, "grow,push,wrap,gapright 20,gaptop 0,width 200:150:200,height 100:80:150,wrap");
        top.add(class1, "growx,pushx,gapright 20,gaptop 20");
        top.add(jclass1, "growx,pushx,gapright 20,gaptop 20");
        top.add(term, "growx,pushx,gapright 20,gaptop 20");
        top.add(jterm, "growx,pushx,gapright 20,gaptop 20,wrap");

        top.add(stream, "growx,pushx,gapright 20,gaptop 20");
        top.add(jstream, "growx,pushx,gapright 20,gaptop 20");
        top.add(date, "growx,pushx,gapright 20,gaptop 20");
        top.add(jdate, "growx,pushx,gapright 20,gaptop 20,wrap");
        top.add(week, "growx,pushx,gapright 20,gaptop 20");
        top.add(jweek, "growx,pushx,gapright 20,gaptop 20");
        bottom.add(state, "grow,push,gapleft 30");
        bottom.add(jstate, "gapright 200,growx,pushx,wrap");
        bottom.add(cancel, "gapleft 50");
        bottom.add(save);
        bottom.add(report, "gapright 100");
        bottom.setBorder(new TitledBorder("Use Enter Key To Save And Arrow Key To Skip Or Navigate Across Class List. Input Letters P For Present And A For Absent"));
        jadmnumber.setFont(new Font("serif", Font.BOLD, 18));
        cancel.addActionListener(this);
        save.addActionListener(this);
        report.addActionListener(this);
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
                        jstream.setSelectedItem(rs.getString("CurrentStream"));
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

        jweek.setEditable(false);
        jstate.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent evt) {
                String dd = ((FredTextField) jdate.getDateEditor().getUiComponent()).getText();
                if (dd.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Kindly Check The Date");
                    jstate.setEditable(false);
                } else {
                    jstate.setEnabled(true);
                    try {
                        String op = "";
                        int startWeek = 0;
                        int currentWeek = 0;
                        String termcode = Globals.termcode(jterm.getSelectedItem().toString());
                        String year = String.valueOf((1900 + jdate.getDate().getYear()));
                        System.err.println("year:" + year);
                        String sql = "Select openingdate from termdates  where academicyear='" + year + "' and termcode='" + termcode + "'";
                        ps = con.prepareStatement(sql);
                        rs = ps.executeQuery();
                        if (rs.next()) {
                            op = rs.getString("OpeningDate");
                        }
                        String sql2 = "Select week('" + op + "')";
                        ps = con.prepareStatement(sql2);
                        rs = ps.executeQuery();
                        if (rs.next()) {
                            startWeek = rs.getInt("week('" + op + "')");
                        }
                        String sql3 = "Select week('" + dd + "')";
                        ps = con.prepareStatement(sql3);
                        rs = ps.executeQuery();
                        if (rs.next()) {
                            currentWeek = rs.getInt("week('" + dd + "')");
                        }
                        System.err.println("Current Week:" + currentWeek);
                        System.err.println("Start Week:" + startWeek);
                        int weekDiffrence = (currentWeek - startWeek) + 1;
                        if (weekDiffrence < 1) {
                            JOptionPane.showMessageDialog(null, "Week Number Could Not Be Analysed Automatically ,\nKindly Check The Term Dates\n The Opening Date Of The Term May be After Than The Selected Date");
                        } else {
                            jweek.setText("WEEK " + weekDiffrence);
                        }

                    } catch (Exception sq) {
                        sq.printStackTrace();
                        JOptionPane.showMessageDialog(null, sq.getMessage());
                    }

                }


            }
        });


        jstream.addActionListener(this);
        jstate.setFont(new Font("serif", Font.BOLD, 18));
        jstate.addKeyListener(new KeyAdapter() {

            public void keyReleased(KeyEvent key) {
                char c = key.getKeyChar();
                if (jstate.getText().length() == 1) {
                    jstate.setText(jstate.getText().toUpperCase());
                    ;
                }
                if (key.getKeyCode() == KeyEvent.VK_LEFT) {
                    String dd = ((FredTextField) jdate.getDateEditor().getUiComponent()).getText();
                    try {
                        if (result.previous()) {
                            jstate.setText("");
                            jadmnumber.setText(result.getString("admissionNumber"));
                            studentName.setText(result.getString("firstName") + "    " + result.getString("Middlename") + "    " + result.getString("LastName"));
                            jstate.setText("");
                            String imageurl = result.getString("ImageLocation");
                            if (imageurl.isEmpty()) {
                                studentimage.setText("No Image");
                                studentimage.setIcon(null);

                            } else {
                                studentimage.setIcon(resizeImage(result.getString("ImageLocation")));
                                studentimage.setText("");


                            }

                            String adm = jadmnumber.getText();
                            String sq = "Select * from attendance where date='" + dd + "' and admnumber='" + adm + "'";
                            ps = con.prepareStatement(sq);
                            ResultSet RS = ps.executeQuery();
                            if (RS.next()) {

                                jstate.setText(RS.getString("status"));
                            } else {

                                jstate.setText("");
                            }
                        }
                    } catch (Exception sq) {
                        sq.printStackTrace();
                    }
                }
                if (key.getKeyCode() == KeyEvent.VK_RIGHT) {
                    String dd = ((FredTextField) jdate.getDateEditor().getUiComponent()).getText();

                    try {
                        if (result.next()) {
                            jstate.setText("");
                            jadmnumber.setText(result.getString("admissionNumber"));
                            studentName.setText(result.getString("firstName") + "    " + result.getString("Middlename") + "    " + result.getString("LastName"));
                            jstate.setText("");
                            String imageurl = result.getString("ImageLocation");
                            if (imageurl.isEmpty()) {
                                studentimage.setText("No Image");
                                studentimage.setIcon(null);

                            } else {
                                studentimage.setIcon(resizeImage(result.getString("ImageLocation")));
                                studentimage.setText("");


                            }

                            String adm = jadmnumber.getText();
                            String sq = "Select * from attendance where date='" + dd + "' and admnumber='" + adm + "'";
                            ps = con.prepareStatement(sq);
                            ResultSet RS = ps.executeQuery();
                            if (RS.next()) {

                                jstate.setText(RS.getString("status"));
                            } else {

                                jstate.setText("");
                            }
                        }
                    } catch (Exception sq) {
                        sq.printStackTrace();
                    }
                }

                if (c == KeyEvent.VK_ENTER) {
                    String dd = ((FredTextField) jdate.getDateEditor().getUiComponent()).getText();
                    if (jclass1.getSelectedIndex() == 0) {
                        JOptionPane.showMessageDialog(null, "Select Class");
                    } else {
                        if (jstream.getSelectedIndex() == 0) {
                            JOptionPane.showMessageDialog(null, "Select Stream");
                        } else {
                            if (jterm.getSelectedIndex() == 0) {
                                JOptionPane.showMessageDialog(null, "Selet Term");
                            } else {

                                if (dd.isEmpty()) {
                                    JOptionPane.showMessageDialog(null, "Select Date");
                                } else {
                                    if (jweek.getText().isEmpty()) {
                                        JOptionPane.showMessageDialog(null, "Set Week");
                                    } else {
                                        if (jstate.getText().isEmpty()) {
                                            JOptionPane.showMessageDialog(null, "Invalid Attendance Status Entry");

                                        } else {

                                            if (Globals.admissionVerifier(jadmnumber.getText())) {

                                                try {
                                                    attendanceCapture(Globals.classCode(jclass1.getSelectedItem().toString()), Globals.streamcode(jstream.getSelectedItem().toString()), Globals.termcode(jterm.getSelectedItem().toString()), jadmnumber.getText(), jweek.getText(), jstate.getText(), dd);
                                                    if (result.next()) {
                                                        jstate.setText("");
                                                        jadmnumber.setText(result.getString("admissionNumber"));
                                                        studentName.setText(result.getString("firstName") + "    " + result.getString("Middlename") + "    " + result.getString("LastName"));
                                                        jstate.setText("");
                                                        String imageurl = result.getString("ImageLocation");
                                                        if (imageurl.isEmpty()) {
                                                            studentimage.setText("No Image");
                                                            studentimage.setIcon(null);

                                                        } else {
                                                            studentimage.setIcon(resizeImage(result.getString("ImageLocation")));
                                                            studentimage.setText("");


                                                        }

                                                        String adm = jadmnumber.getText();
                                                        String sq = "Select * from attendance where date='" + dd + "' and admnumber='" + adm + "'";
                                                        ps = con.prepareStatement(sq);
                                                        ResultSet RS = ps.executeQuery();
                                                        if (RS.next()) {

                                                            jstate.setText(RS.getString("status"));
                                                        } else {

                                                            jstate.setText("");
                                                        }
                                                    }


                                                } catch (Exception sq) {
                                                    sq.printStackTrace();
                                                    JOptionPane.showMessageDialog(null, sq.getMessage());
                                                }


                                            } else {
                                                JOptionPane.showMessageDialog(null, "Invalid admission Number");
                                            }


                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            public void keyTyped(KeyEvent key) {
                char c = key.getKeyChar();
                if (Character.isAlphabetic(c)) {

                } else {
                    key.consume();

                }
            }
        });

        jterm.setSelectedItem(Globals.currentTermName());
        setVisible(true);
    }

    public void attendanceCapture(String classcode, String streamcode, String termcode, String adm, String week, String status, String date1) {
        try {
            String sql1 = "Select * from attendance where adm='" + adm + "' and classcode='" + classcode + "' and date='" + date1 + "'";
            ps = con.prepareStatement(sql1);
            ResultSet r2 = ps.executeQuery();
            if (r2.next()) {
                String sql = "update attendance set status='" + status + "' where adm='" + adm + "' and classcode='" + classcode + "' and date='" + date1 + "'";
                ps = con.prepareStatement(sql);
                ps.execute();
            } else {
                String sql = "Insert into attendance values('" + adm + "','" + classcode + "','" + streamcode + "','" + date1 + "','" + week + "','" + termcode + "','" + status + "')";
                ps = con.prepareStatement(sql);
                ps.execute();
            }


        } catch (Exception sq) {
            sq.printStackTrace();
            ;
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


    @Override
    public void actionPerformed(ActionEvent e) {
        Object obj = e.getSource();
        if (obj == save) {
            String dd = ((FredTextField) jdate.getDateEditor().getUiComponent()).getText();
            if (jclass1.getSelectedIndex() == 0) {
                JOptionPane.showMessageDialog(null, "Select Class");
            } else {
                if (jstream.getSelectedIndex() == 0) {
                    JOptionPane.showMessageDialog(null, "Select Stream");
                } else {
                    if (jterm.getSelectedIndex() == 0) {
                        JOptionPane.showMessageDialog(null, "Selet Term");
                    } else {

                        if (dd.isEmpty()) {
                            JOptionPane.showMessageDialog(null, "Select Date");
                        } else {
                            if (jweek.getText().isEmpty()) {
                                JOptionPane.showMessageDialog(null, "Set Week");
                            } else {
                                if (jstate.getText().isEmpty()) {
                                    JOptionPane.showMessageDialog(null, "Invalid Attendance Status Entry");

                                } else {

                                    if (Globals.admissionVerifier(jadmnumber.getText())) {

                                        try {
                                            attendanceCapture(Globals.classCode(jclass1.getSelectedItem().toString()), Globals.streamcode(jstream.getSelectedItem().toString()), Globals.termcode(jterm.getSelectedItem().toString()), jadmnumber.getText(), jweek.getText(), jstate.getText(), dd);
                                            if (result.next()) {
                                                jstate.setText("");
                                                jadmnumber.setText(result.getString("admissionNumber"));
                                                studentName.setText(result.getString("firstName") + "    " + result.getString("Middlename") + "    " + result.getString("LastName"));
                                                jstate.setText("");
                                                String imageurl = result.getString("ImageLocation");
                                                if (imageurl.isEmpty()) {
                                                    studentimage.setText("No Image");
                                                    studentimage.setIcon(null);

                                                } else {
                                                    studentimage.setIcon(resizeImage(result.getString("ImageLocation")));
                                                    studentimage.setText("");


                                                }
                                                String adm = jadmnumber.getText();
                                                String sq = "Select * from attendance where date='" + dd + "' and admnumber='" + adm + "'";
                                                ps = con.prepareStatement(sq);
                                                ResultSet RS = ps.executeQuery();
                                                if (RS.next()) {

                                                    jstate.setText(RS.getString("status"));
                                                } else {

                                                    jstate.setText("");
                                                }
                                            }


                                        } catch (Exception sq) {
                                            sq.printStackTrace();
                                            JOptionPane.showMessageDialog(null, sq.getMessage());
                                        }


                                    } else {
                                        JOptionPane.showMessageDialog(null, "Invalid admission Number");
                                    }


                                }
                            }
                        }
                    }
                }
            }

        } else if (obj == cancel) {
            CurrentFrame.currentWindow();
            dispose();
        } else if (obj == report) {
            String dd = ((FredTextField) jdate.getDateEditor().getUiComponent()).getText();
            if (jclass1.getSelectedIndex() == 0) {
                JOptionPane.showMessageDialog(null, "Select Class");
            } else {
                if (jstream.getSelectedIndex() == 0) {
                    JOptionPane.showMessageDialog(null, "Select Term");
                } else {
                    if (jterm.getSelectedIndex() == 0) {
                        JOptionPane.showMessageDialog(null, "Select Term");
                    } else {
                        if (dd.isEmpty()) {
                            JOptionPane.showMessageDialog(null, "Select Date");
                        } else {

                            ReportGenerator.dayAttendance(Globals.classCode(jclass1.getSelectedItem().toString()), Globals.streamcode(jstream.getSelectedItem().toString()), Globals.termcode(jterm.getSelectedItem().toString()), dd, jweek.getText());


                        }
                    }
                }
            }
        }
        if (obj == jstream) {
            try {
                String sql2 = "Select admissionnumber,firstname,ImageLocation,middlename,lastname from admission where  currentform='" + Globals.classCode(jclass1.getSelectedItem().toString()) + "' and currentstream='" + Globals.streamcode(jstream.getSelectedItem().toString()) + "' order by admissionnumber";
                ps = con.prepareStatement(sql2);
                result = ps.executeQuery();
                if (result.next()) {
                    jadmnumber.setText(result.getString("admissionnumber"));
                    studentName.setText(result.getString("firstName") + "    " + result.getString("Middlename") + "    " + result.getString("LastName"));
                    String imageurl = result.getString("ImageLocation");


                    if (imageurl.isEmpty()) {
                        studentimage.setText("No Image");
                        studentimage.setIcon(null);
                    } else {
                        studentimage.setIcon(resizeImage(result.getString("ImageLocation")));
                        studentimage.setText("");
                    }
                } else {
                    JOptionPane.showMessageDialog(this, "No Students Have Been Allocated To The Selected Subject , Class And Academic Year");

                    studentName.setText("");
                    jadmnumber.setText("");
                }


            } catch (Exception sq) {
                sq.printStackTrace();
                JOptionPane.showMessageDialog(this, sq.getMessage());
            }
        }


    }

    public static void main(String[] args) {
        new AttendanceCapture();
    }

}
