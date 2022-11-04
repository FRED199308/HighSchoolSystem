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

import javax.swing.JMenuBar;
import javax.swing.JOptionPane;

import static javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE;

/**
 * @author FRED
 */
public class DepartmentReg extends JFrame implements ActionListener {

    public static void main(String[] args) {
        new DepartmentReg();
    }

    private int width = 700;
    private int height = 250;
    private FredButton save, cancel;
    private PreparedStatement ps;
    private Connection con;
    private FredLabel form;
    private FredTextField jname;
    ResultSet rs;

    public DepartmentReg() {
        setSize(width, height);
        setLocationRelativeTo(null);
        setLayout(null);
        setResizable(false);
        setTitle("Department Registration");
        jname = new FredTextField();
        form = new FredLabel("Department Name");
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
        JMenuBar bar = new JMenuBar();
        cancel = new FredButton("Close");
        save = new FredButton("Save");
        con = DbConnection.connectDb();
        this.setIconImage(FrameProperties.icon());
        ((JComponent) getContentPane()).setBorder(
                BorderFactory.createMatteBorder(5, 5, 5, 5, Color.MAGENTA));
        bar.setBounds(5, 5, 675, 30);
        add(bar);
        form.setBounds(30, 50, 150, 30);
        add(form);
        jname.setBounds(300, 50, 300, 30);
        add(jname);
        cancel.setBounds(100, 150, 130, 30);
        add(cancel);

        save.setBounds(400, 150, 130, 30);
        add(save);
        cancel.addActionListener(this);
        save.addActionListener(this);
        setVisible(true);

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object obj = e.getSource();
        if (obj == save) {
            if (jname.getText().isEmpty()) {
                getToolkit().beep();
                JOptionPane.showMessageDialog(this, "Please Enter A Valid Department Name");
            } else {
                try {
                    String sql = "Select * from Departments where name='" + jname.getText() + "'";
                    ps = con.prepareStatement(sql);
                    rs = ps.executeQuery();
                    if (rs.next()) {
                        getToolkit().beep();
                        JOptionPane.showMessageDialog(this, "A Department With Simmilar Name Already Exist\n Choose A diffrent Name");
                    } else {
                        String sql2 = "Insert into Departments values('" + jname.getText().toUpperCase() + "','" + "DP" + IdGenerator.keyGen() + "')";
                        ps = con.prepareStatement(sql2);
                        ps.execute();
                        JOptionPane.showMessageDialog(this, "Department Registered Successfully");
                        jname.setText("");
                    }

                } catch (HeadlessException | SQLException sq) {
                    JOptionPane.showMessageDialog(this, sq.getMessage());
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
