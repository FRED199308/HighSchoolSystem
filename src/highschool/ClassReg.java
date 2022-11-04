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
public class ClassReg extends JFrame implements ActionListener {

    public static void main(String[] args) {
        new ClassReg();
    }

    private int width = 700;
    private int height = 400;
    private FredButton save, cancel;
    private PreparedStatement ps;
    private Connection con;
    private FredLabel form, precission;
    private FredTextField jname, jprecission;
    ResultSet rs;

    public ClassReg() {

        setSize(width, height);
        setLocationRelativeTo(null);
        setLayout(null);
        jname = new FredTextField();
        precission = new FredLabel("Precision");
        jprecission = new FredTextField();
        form = new FredLabel("Class name");
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
        con = DbConnection.connectDb();
        this.setIconImage(FrameProperties.icon());
        ((JComponent) getContentPane()).setBorder(
                BorderFactory.createMatteBorder(5, 5, 5, 5, Color.MAGENTA));
        form.setBounds(30, 30, 150, 30);
        add(form);
        jname.setBounds(300, 30, 250, 30);
        add(jname);
        precission.setBounds(30, 120, 150, 30);
        add(precission);
        jprecission.setBounds(300, 120, 250, 30);
        add(jprecission);
        cancel.setBounds(100, 230, 150, 30);
        add(cancel);
        save.setBounds(350, 230, 150, 30);
        add(save);
        setVisible(true);
        save.addActionListener(this);
        cancel.addActionListener(this);
        jprecission.addKeyListener(new KeyAdapter() {

            @Override
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();
                if (Character.isAlphabetic(c) || jprecission.getText().length() > 3) {
                    getToolkit().beep();
                    e.consume();
                }

            }
        });
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object obj = e.getSource();
        if (obj == save) {
            if (jname.getText().equalsIgnoreCase("")) {
                getToolkit().beep();
                JOptionPane.showMessageDialog(this, "Missing Class Name");
            } else {
                if (jprecission.getText().equalsIgnoreCase("")) {
                    JOptionPane.showMessageDialog(null, "Kindly Input The Class Precission Number");
                } else {

                    String sql1 = "Select * from classes where classname='" + jname.getText() + "'";
                    try {
                        con = DbConnection.connectDb();
                        ps = con.prepareStatement(sql1);
                        rs = ps.executeQuery();
                        if (rs.next()) {
                            getToolkit().beep();
                            JOptionPane.showMessageDialog(this, "This Class Is AlreadyRegistered");
                        } else {
                            String sql3 = "Select * from classes where precision1='" + jprecission.getText() + "'";
                            ps = con.prepareStatement(sql3);
                            rs = ps.executeQuery();
                            if (rs.next()) {
                                JOptionPane.showMessageDialog(this, rs.getString("classname") + "  is Already assigned precission : " + jprecission.getText());
                            } else {
                                String sql = "Insert into classes values('" + jname.getText().toUpperCase() + "','" + "FM" + IdGenerator.keyGen() + "')";
                                ps = con.prepareStatement(sql);
                                ps.execute();
                                JOptionPane.showMessageDialog(this, "Class Registered Successfully");
                                jname.setText("");
                                jprecission.setText("");
                            }


                        }
                    } catch (HeadlessException | SQLException sq) {
                        JOptionPane.showMessageDialog(this, sq.getMessage());
                    }

                }

            }

        } else if (obj == cancel) {
            try {
                con.close();
            } catch (SQLException sq) {

            }
            CurrentFrame.currentWindow();
            dispose();
        }
    }
}
