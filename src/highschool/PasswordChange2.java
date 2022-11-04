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
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;

import javax.swing.JOptionPane;
import javax.swing.JPasswordField;

import static javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE;

/**
 * @author FRED
 */
public class PasswordChange2 extends JFrame implements ActionListener {

    public static void main(String[] args) {
        new PasswordChange2();
    }

    private int width = 700;
    private int height = 400;
    private FredLabel password1, password2;
    private JPasswordField jPassword1, jPassword2;
    private JButton cancel;
    private JButton save;
    Connection con = DbConnection.connectDb();

    public PasswordChange2() {
        setSize(width, height);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        setLayout(null);
        setTitle("         Kindly Change Your Password");
        setLocationRelativeTo(null);
        getContentPane().setBackground(Color.cyan);
        setResizable(false);
        this.setIconImage(FrameProperties.icon());
        ((JComponent) getContentPane()).setBorder(
                BorderFactory.createMatteBorder(5, 5, 5, 5, Color.MAGENTA)
        );
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
        cancel = new JButton("CLOSE");
        save = new JButton("SAVE");
        password1 = new FredLabel("Input Your New Password");
        password2 = new FredLabel("Retype Your new Password");
        jPassword1 = new JPasswordField();
        jPassword2 = new JPasswordField();
        password1.setBounds(30, 30, 200, 30);
        add(password1);
        jPassword1.setBounds(350, 30, 250, 30);
        add(jPassword1);
        password2.setBounds(30, 120, 200, 30);
        add(password2);
        jPassword2.setBounds(350, 120, 250, 30);
        add(jPassword2);
        save.setBounds(150, 250, 130, 30);
        add(save);
        cancel.setBounds(400, 250, 130, 30);
        add(cancel);
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
        } else if (obj == save) {
            boolean comply = true;
            if (jPassword1.getText().equalsIgnoreCase("") && comply == true) {
                comply = false;
                getToolkit().beep();
                JOptionPane.showMessageDialog(this, "Kindly type password", "missing password", JOptionPane.ERROR_MESSAGE);
            }
            if (jPassword2.getText().equalsIgnoreCase("") && comply == true) {
                comply = false;
                getToolkit().beep();
                JOptionPane.showMessageDialog(this, "Kindly Retype The password", "missing password Cofirmation", JOptionPane.ERROR_MESSAGE);
            }
            if (jPassword1.getText().equals(jPassword2.getText()) == false && comply == true) {
                getToolkit().beep();
                comply = false;
                JOptionPane.showMessageDialog(this, "Passwords Do not Match", "Password Mismatch", JOptionPane.ERROR_MESSAGE);
            }
            if (comply == true) {

                try {
                    boolean status = false;
                    PreparedStatement ps;
                    ResultSet rs;

                    status = ConfigurationIntialiser.passwordConstraint();

                    if (status == false) {
                        String querry1 = "update useraccounts set password='" + DataEncriptor.encript(jPassword2.getText()) + "' where username='" + Globals.CurrentUser + "' ";
                        ps = con.prepareStatement(querry1);
                        ps.execute();
                        JOptionPane.showMessageDialog(this, "Password Changed Successfully", "       password Changed", JOptionPane.INFORMATION_MESSAGE);
                        dispose();

                        new ExamHome();
                        Globals.moduleName = "Exam";


                    } else {
                        if (DataValidation.password(jPassword1.getText())) {
                            String querry1 = "update useraccounts set password='" + DataEncriptor.encript(jPassword2.getText()) + "' where username='" + Globals.CurrentUser + "' ";
                            ps = con.prepareStatement(querry1);
                            ps.execute();
                            JOptionPane.showMessageDialog(this, "Password Changed Successfully", "       password Changed", JOptionPane.INFORMATION_MESSAGE);
                            dispose();
                            dispose();
                            Globals.moduleName = "Exam";
                            new ExamHome();

                        } else {
                            getToolkit().beep();
                            JOptionPane.showMessageDialog(this, "Password Weak Kindly Choose a new Password\n Password Must contain  Atleast "
                                    + "\n An Uppercase Letter ,A Lowercase letter,A Digit \n A Special Character and Atleast Six Characters", "       password Fails To Meet Requirements", JOptionPane.INFORMATION_MESSAGE);
                        }

                    }

                } catch (HeadlessException | SQLException sq) {
                    JOptionPane.showMessageDialog(this, sq.getMessage());
                }


            }


        }

    }

}
