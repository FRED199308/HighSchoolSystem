/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package highschool;


import java.awt.Color;
import java.awt.HeadlessException;
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
import javax.swing.JTextField;

import static javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE;

/**
 * @author FRED
 */
public class BookRegistration extends JFrame implements ActionListener {

    public static void main(String[] args) {
        new BookRegistration();
    }

    private int width = 1300;
    private int height = 600;
    private FredButton save, cancel;
    private PreparedStatement ps;
    private Connection con;
    private ResultSet rs;
    private FredLabel bookname, serial, subject, author, condition, title, datebought, classl, type;
    private FredTextField jbookname, jserial, jauthor, jtitle;
    private FredDateChooser jdate;
    private FredCombo jcondition, jclassl, jsubject, jtype;

    public BookRegistration() {
        setSize(width, height);
        setLocationRelativeTo(null);
        setLayout(null);

        getContentPane().setBackground(Color.CYAN);
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

        cancel = new FredButton("Close");
        save = new FredButton("Save");
        author = new FredLabel("Author");
        bookname = new FredLabel("Book Name");
        title = new FredLabel("Book Title");
        serial = new FredLabel("Book Serial Number");
        condition = new FredLabel("Book   Condition");
        datebought = new FredLabel("Date Bought");
        classl = new FredLabel("Class");
        subject = new FredLabel("Subject");
        jdate = new FredDateChooser();

        jserial = new FredTextField();
        jauthor = new FredTextField();
        jtitle = new FredTextField();
        jbookname = new FredTextField();
        type = new FredLabel("Book Type");
        jtype = new FredCombo("Select Book Type");
        jclassl = new FredCombo("Select Class");
        jcondition = new FredCombo("Select Book Condition");
        jsubject = new FredCombo("Select subject");
        bookname.setBounds(30, 30, 150, 30);
        add(bookname);
        jbookname.setBounds(300, 30, 300, 30);
        add(jbookname);
        title.setBounds(30, 100, 150, 30);
        add(title);
        jtitle.setBounds(300, 100, 300, 30);
        add(jtitle);
        author.setBounds(30, 180, 150, 30);
        add(author);
        jauthor.setBounds(300, 180, 300, 30);
        add(jauthor);
        datebought.setBounds(30, 260, 150, 30);
        add(datebought);
        jdate.setBounds(300, 260, 300, 30);
        add(jdate);
        type.setBounds(30, 350, 150, 30);
        add(type);
        jtype.setBounds(300, 350, 300, 30);
        add(jtype);
        condition.setBounds(700, 30, 150, 30);
        add(condition);
        jcondition.setBounds(900, 30, 300, 30);
        add(jcondition);
        subject.setBounds(700, 130, 150, 30);
        add(subject);
        jsubject.setBounds(900, 130, 300, 30);
        add(jsubject);
        classl.setBounds(700, 230, 150, 30);
        add(classl);
        jclassl.setBounds(900, 230, 300, 30);
        add(jclassl);
        serial.setBounds(700, 330, 150, 30);
        add(serial);
        jserial.setBounds(900, 330, 300, 30);
        add(jserial);
        cancel.setBounds(250, 450, 150, 30);
        add(cancel);
        save.setBounds(800, 450, 150, 30);
        add(save);
        con = DbConnection.connectDb();
        this.setIconImage(FrameProperties.icon());
        ((JComponent) getContentPane()).setBorder(
                BorderFactory.createMatteBorder(5, 5, 5, 5, Color.MAGENTA));
        try {

            if (Globals.Level.equalsIgnoreCase("Admin")) {


                String sql = "Select * from classes  where  precision1<5 order by precision1 asc";
                con = DbConnection.connectDb();
                ps = con.prepareStatement(sql);
                rs = ps.executeQuery();
                while (rs.next()) {
                    jclassl.addItem(rs.getString("ClassName"));

                }
                String sql2 = "Select * from subjects";
                ps = con.prepareStatement(sql2);
                rs = ps.executeQuery();
                while (rs.next()) {

                    jsubject.addItem(rs.getString("SubjectName"));
                }

            } else {

                String sql2 = "Select * from subjectrights where teachercode='" + Globals.empcode + "' group by subjectcode";
                ps = con.prepareStatement(sql2);
                rs = ps.executeQuery();
                while (rs.next()) {
                    jclassl.addItem(Globals.className(rs.getString("classcode")));
                }


                sql2 = "Select * from subjectrights where teachercode='" + Globals.empcode + "' group by subjectcode";
                ps = con.prepareStatement(sql2);
                rs = ps.executeQuery();
                while (rs.next()) {
                    jsubject.addItem(Globals.subjectName(rs.getString("Subjectcode")));
                }
            }


        } catch (SQLException sq) {
            sq.printStackTrace();
            JOptionPane.showMessageDialog(null, sq.getMessage());
        }
        jdate.setDateFormatString("yyyy:MM:dd");
        jdate.setMaxSelectableDate(new Date());
        jcondition.addItem("Perfect");
        jcondition.addItem("Good");
        jcondition.addItem("Worn Out");
        jtype.addItem("Course Book");
        jtype.addItem("Revision Book");
        jtype.addItem("Novel Book");
        jtype.addItem("Atlas Book");
        jtype.addItem("Others");
        setVisible(true);
        cancel.addActionListener(this);
        save.addActionListener(this);
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
        } else if (obj == save) {
            if (jbookname.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Input Book Name");
            } else {
                if (jtitle.getText().isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Enter The Book Title");
                } else {
                    if (jcondition.getSelectedIndex() == 0) {
                        JOptionPane.showMessageDialog(this, "Select Book Condition");
                    } else {
                        if (jsubject.getSelectedIndex() == 0) {
                            JOptionPane.showMessageDialog(this, "Kindly Select The Subject Category\n Under which The Book May Be Traced");
                        } else {
                            if (jtype.getSelectedIndex() == 0) {
                                JOptionPane.showMessageDialog(this, "Kindly Book Category(Type)");
                            } else {
                                if (jserial.getText().isEmpty()) {
                                    JOptionPane.showMessageDialog(this, "Kindly Input the Book Serial Number");
                                } else {
                                    try {
                                        String prefixcode = jclassl.getSelectedItem().toString().charAt(0) + "" + jclassl.getSelectedItem().toString().charAt(5) + "/" + jsubject.getSelectedItem().toString().substring(0, 3).toUpperCase() + "/" + jserial.getText().toUpperCase();
                                        //  System.err.println(jclassl.getSelectedItem().toString().charAt(0)+""+jclassl.getSelectedItem().toString().charAt(5));
                                        if (Globals.Level.equalsIgnoreCase("Admin")) {
                                            String querry = "Select bookname from books where bookserial='" + prefixcode + "'";
                                            ps = con.prepareStatement(querry);
                                            rs = ps.executeQuery();
                                            if (rs.next()) {
                                                JOptionPane.showMessageDialog(this, "Books Can Never Share Serial Numbers\n " + rs.getString("BookName") + " Is Already Registered With This Serial Number");
                                            } else {
                                                String DATE = ((JTextField) jdate.getDateEditor().getUiComponent()).getText();
                                                if (DATE.isEmpty()) {
                                                    DATE = "1900:01:01";
                                                }
                                                String sql = "Insert into Books values ('" + jbookname.getText().toUpperCase() + "','" + jtitle.getText().toUpperCase() + "','" + prefixcode + "','" + jauthor.getText().toUpperCase() + "','" + jtype.getSelectedItem().toString().toUpperCase() + "','" + jcondition.getSelectedItem().toString().toUpperCase() + "','" + DATE + "','" + Globals.subjectCode(jsubject.getSelectedItem().toString()).toUpperCase() + "','" + Globals.classCode(jclassl.getSelectedItem().toString()).toUpperCase() + "','" + "On Store" + "')";
                                                ps = con.prepareStatement(sql);
                                                ps.execute();
                                                JOptionPane.showMessageDialog(this, "Book Details Save Successfully");
                                                jbookname.setText("");
                                                jtitle.setText("");
                                                jserial.setText("");
                                                jdate.setDate(null);
                                                jsubject.setSelectedIndex(0);
                                                jclassl.setSelectedIndex(0);
                                                jtype.setSelectedIndex(0);
                                                jcondition.setSelectedIndex(0);
                                                jauthor.setText("");
                                            }
                                        } else {
                                            if (RightsAnnouncer.subjectright(Globals.subjectCode(jsubject.getSelectedItem().toString()), Globals.classCode(jclassl.getSelectedItem().toString()), Globals.empcode)) {
                                                String querry = "Select bookname from books where bookserial='" + prefixcode + "'";
                                                ps = con.prepareStatement(querry);
                                                rs = ps.executeQuery();
                                                if (rs.next()) {
                                                    JOptionPane.showMessageDialog(this, "Books Can Never Share Serial Numbers\n " + rs.getString("BookName") + " Is Already Registered With This Serial Number");
                                                } else {


                                                    String DATE = ((FredTextField) jdate.getDateEditor().getUiComponent()).getText();
                                                    String sql = "Insert into Books values ('" + jbookname.getText().toUpperCase() + "','" + jtitle.getText().toUpperCase() + "','" + prefixcode + "','" + jauthor.getText().toUpperCase() + "','" + jtype.getSelectedItem().toString().toUpperCase() + "','" + jcondition.getSelectedItem().toString().toUpperCase() + "','" + DATE + "','" + Globals.subjectCode(jsubject.getSelectedItem().toString()).toUpperCase() + "','" + Globals.classCode(jclassl.getSelectedItem().toString()).toUpperCase() + "','" + "On Store" + "')";
                                                    ps = con.prepareStatement(sql);
                                                    ps.execute();
                                                    JOptionPane.showMessageDialog(this, "Book Details Save Successfully");
                                                    jbookname.setText("");
                                                    jtitle.setText("");
                                                    jserial.setText("");
                                                    jdate.setDate(null);
                                                    jsubject.setSelectedIndex(0);
                                                    jclassl.setSelectedIndex(0);
                                                    jtype.setSelectedIndex(0);
                                                    jcondition.setSelectedIndex(0);
                                                    jauthor.setText("");
                                                }
                                            } else {
                                                JOptionPane.showMessageDialog(this, "You Do Not Have Sufficient Rights To Add A book this Class");
                                            }
                                        }


                                    } catch (HeadlessException | SQLException sq) {
                                        sq.printStackTrace();
                                        JOptionPane.showMessageDialog(null, sq.getMessage());
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
