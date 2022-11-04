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
import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JFrame;

import javax.swing.JOptionPane;

import static javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE;

/**
 * @author FRED
 */
public class SubjectRegistration extends JFrame implements ActionListener {

    public static void main(String[] args) {
        new SubjectRegistration();
    }

    private int width = 700;
    private int height = 350;
    private FredButton save, cancel;
    private PreparedStatement ps;
    private ResultSet rs;
    private FredLabel subjectname, subjectcode;
    private FredTextField jsubjectname, jsubjectcode;
    private Connection con;

    public SubjectRegistration() {
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
        subjectcode = new FredLabel("Subject Code");
        subjectname = new FredLabel("Subject Name");
        jsubjectcode = new FredTextField();
        jsubjectname = new FredTextField();
        cancel = new FredButton("Close");
        save = new FredButton("Save");
        con = DbConnection.connectDb();
        this.setIconImage(FrameProperties.icon());
        ((JComponent) getContentPane()).setBorder(
                BorderFactory.createMatteBorder(5, 5, 5, 5, Color.MAGENTA));
        setLayout(null);
        subjectname.setBounds(30, 30, 150, 30);
        add(subjectname);
        jsubjectname.setBounds(300, 30, 300, 30);
        add(jsubjectname);
        subjectcode.setBounds(30, 130, 150, 30);
        add(subjectcode);
        jsubjectcode.setBounds(300, 130, 300, 30);
        add(jsubjectcode);
        cancel.setBounds(150, 230, 120, 30);
        add(cancel);
        save.setBounds(500, 230, 120, 30);
        add(save);
        cancel.addActionListener(this);
        save.addActionListener(this);
        setVisible(true);
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
        }
        if (obj == save) {
            if (jsubjectname.getText().equalsIgnoreCase("")) {
                JOptionPane.showMessageDialog(this, "Input The Subject Name");
            } else {
                if (jsubjectcode.getText().isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Input Subject Code");
                } else {
                    try {
                        String sql = "Insert into subjects Values('" + jsubjectname.getText().toUpperCase() + "','" + jsubjectcode.getText() + "')";
                        con = DbConnection.connectDb();
                        ps = con.prepareStatement(sql);
                        ps.execute();
                        JOptionPane.showMessageDialog(this, "Subject Registered Successfuly");
                        jsubjectcode.setText("");
                        jsubjectname.setText("");

                    } catch (HeadlessException | SQLException sq) {
                        sq.printStackTrace();
                        JOptionPane.showMessageDialog(null, sq.getMessage());
                    }
                }
            }
        }
    }

}
