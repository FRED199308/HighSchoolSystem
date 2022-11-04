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


/**
 * @author FRED
 */
public class CountryReg extends JFrame implements ActionListener {

    public static void main(String[] args) {
        new CountryReg();
    }

    private int width = 800;
    private int height = 300;
    private FredButton cancel, save;
    private FredTextField jname;
    private PreparedStatement ps;
    private Connection con;
    ResultSet rs;
    private FredLabel name;


    public CountryReg() {
        setTitle("Country Registration");
        setLayout(null);
        setSize(width, height);
        setLocationRelativeTo(null);
        getContentPane().setBackground(Color.cyan);
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
        this.setIconImage(FrameProperties.icon());
        ((JComponent) getContentPane()).setBorder(
                BorderFactory.createMatteBorder(5, 5, 5, 5, Color.MAGENTA));

        save = new FredButton("Save");
        cancel = new FredButton("Close");
        jname = new FredTextField();
        name = new FredLabel("Country Name");
        name.setBounds(30, 40, 150, 30);
        add(name);
        jname.setBounds(350, 40, 300, 30);
        add(jname);
        cancel.setBounds(100, 150, 130, 30);
        add(cancel);
        save.setBounds(400, 150, 130, 30);
        add(save);
        setVisible(true);
        cancel.addActionListener(this);
        save.addActionListener(this);
        jname.addKeyListener(new KeyAdapter() {

            @Override
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();
                if (Character.isDigit(c) || jname.getText().length() > 30) {
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
            if (jname.getText().isEmpty()) {
                getToolkit().beep();
                JOptionPane.showMessageDialog(this, "Country Name Cannot Be Blank");
            } else {

                try {
                    con = DbConnection.connectDb();
                    String sql = "Select * from countries Where countryName='" + jname.getText() + "'";
                    ps = con.prepareStatement(sql);
                    rs = ps.executeQuery();
                    if (rs.next()) {
                        JOptionPane.showMessageDialog(this, "This Country is Already Registered", "Duplicate Prevention", JOptionPane.ERROR_MESSAGE);
                    } else {
                        String key = "CO" + IdGenerator.keyGen();
                        String sql2 = "Insert into Countries values('" + jname.getText().toUpperCase() + "','" + key + "')";
                        ps = con.prepareStatement(sql2);
                        ps.execute();
                        JOptionPane.showMessageDialog(this, "Country Registered SuccessFully");
                        jname.setText(null);
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
