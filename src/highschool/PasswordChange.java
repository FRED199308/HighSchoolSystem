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
 * @author FRED_ADMIN
 */
public class PasswordChange extends JFrame implements ActionListener {

    public static void main(String[] u) {
        new PasswordChange();
    }

    private int width = 700;
    private int height = 430;
    private FredLabel jCurrentUser, jNewUser, jNewPass, jCurrentPass, jPassConfirm;
    private FredTextField currentUser, newUser;
    private JPasswordField pCurrentPass, pNewPass, pPassConfirm;
    private JButton save, cancel;
    private PreparedStatement ps;
    private ResultSet rs;
    private Connection con;

    public PasswordChange() {
        setSize(width, height);
        setTitle("passwordChange");
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
        con = DbConnection.connectDb();
        setLayout(null);

        this.setIconImage(FrameProperties.icon());
        ((JComponent) getContentPane()).setBorder(
                BorderFactory.createMatteBorder(5, 5, 5, 5, Color.MAGENTA));
        setLocationRelativeTo(null);
        getContentPane().setBackground(Color.cyan);
        jCurrentUser = new FredLabel("Enter The Current UserName");
        jNewUser = new FredLabel("Enter The New Username");
        jCurrentPass = new FredLabel("Enter The Current Password");
        jNewPass = new FredLabel("Enter The New Password");
        jPassConfirm = new FredLabel("Retype The Password");
        currentUser = new FredTextField();
        newUser = new FredTextField();

        pCurrentPass = new JPasswordField();
        pNewPass = new JPasswordField();
        pPassConfirm = new JPasswordField();
        save = new JButton("Save");
        cancel = new JButton("Cancel");
        jCurrentUser.setBounds(20, 30, 200, 30);
        add(jCurrentUser);
        currentUser.setBounds(350, 30, 250, 30);
        add(currentUser);
        jCurrentPass.setBounds(20, 90, 200, 30);
        add(jCurrentPass);
        pCurrentPass.setBounds(350, 90, 250, 30);
        add(pCurrentPass);
        jNewUser.setBounds(20, 150, 200, 30);
        add(jNewUser);
        ;
        newUser.setBounds(350, 150, 250, 30);
        add(newUser);
        jNewPass.setBounds(20, 210, 200, 30);
        add(jNewPass);
        pNewPass.setBounds(350, 210, 250, 30);
        add(pNewPass);
        jPassConfirm.setBounds(20, 270, 200, 30);
        add(jPassConfirm);
        pPassConfirm.setBounds(350, 270, 250, 30);
        add(pPassConfirm);
        save.setBounds(150, 330, 150, 30);
        add(save);
        cancel.setBounds(400, 330, 150, 30);
        add(cancel);

        cancel.addActionListener(this);
        save.addActionListener(this);
        boolean status = false;
        status = ConfigurationIntialiser.usernameChange();
        if (status == false) {
            currentUser.setText(Globals.CurrentUser);
            newUser.setText(Globals.CurrentUser);
            currentUser.setEditable(false);
            newUser.setEditable(false);
        }

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
            DataEncriptor dd = new DataEncriptor();
            String newuser = this.newUser.getText();
            String oldUser = currentUser.getText();
            String newPass = pNewPass.getText().toString();
            String oldPass = pCurrentPass.getText();
            String pass2 = pPassConfirm.getText().toString();
            boolean comply = true;
            if (oldUser.equalsIgnoreCase("") && comply == true) {
                comply = false;
                getToolkit().beep();
                JOptionPane.showMessageDialog(this, "Kindly type Your Current UserName", "missing Current Username", JOptionPane.ERROR_MESSAGE);
            }
            if (oldPass.equalsIgnoreCase("") && comply == true) {
                comply = false;
                getToolkit().beep();
                JOptionPane.showMessageDialog(this, "Kindly type Current password", "missing Current password", JOptionPane.ERROR_MESSAGE);
            }

            if (newuser.equalsIgnoreCase("") && comply == true) {
                comply = false;
                getToolkit().beep();
                JOptionPane.showMessageDialog(this, "Kindly Type Your New Username", "missing New Username", JOptionPane.ERROR_MESSAGE);
            }
            if (newPass.equalsIgnoreCase("") && comply == true) {
                comply = false;
                getToolkit().beep();
                JOptionPane.showMessageDialog(this, "Kindly  The  Your New password", "missing new password", JOptionPane.ERROR_MESSAGE);
            }

            if (pass2.equalsIgnoreCase("") && comply == true) {
                comply = false;
                getToolkit().beep();
                JOptionPane.showMessageDialog(this, "Kindly Retype The  Your New password", "missing password Cofirmation", JOptionPane.ERROR_MESSAGE);
            }

            if (newPass.equals(pass2) && comply == true) {


                try {
                    DbConnection db = new DbConnection();
                    con = DbConnection.connectDb();
                    String querry = "Select * from UserAccounts where UserName='" + oldUser + "' and Password='" + DataEncriptor.encript(oldPass) + "'";
                    ps = con.prepareStatement(querry);
                    rs = ps.executeQuery();
                    if (rs.next()) {
                        String p = rs.getString("password");
                        String u = rs.getString("Username");
                        boolean status = false;
                        status = ConfigurationIntialiser.passwordConstraint();
                        if (status == false) {
                            String querry1 = "Update userAccounts set password='" + DataEncriptor.encript(newPass) + "',username='" + newuser + "' where username='" + u + "' and password='" + p + "'";
                            ps = con.prepareStatement(querry);
                            ps.execute();

                            JOptionPane.showMessageDialog(this, "Password and UserName Change Successful", "success", JOptionPane.INFORMATION_MESSAGE);
                        } else {
                            if (DataValidation.password(newPass)) {
                                String querry1 = "Update userAccounts set password='" + DataEncriptor.encript(newPass) + "',username='" + newuser + "' where username='" + u + "' and password='" + p + "'";
                                ps = con.prepareStatement(querry);
                                ps.execute();

                                JOptionPane.showMessageDialog(this, "Password and UserName Change Successful", "success", JOptionPane.INFORMATION_MESSAGE);
                            } else {

                                getToolkit().beep();
                                JOptionPane.showMessageDialog(this, "Password Weak Kindly Choose a new Password\n Password Must contain  Atleast "
                                        + "\n An Uppercase Letter ,A Lowercase letter,A Digit \n A Special Character and Atleast Six Characters", "       password Fails To Meet Requirements", JOptionPane.INFORMATION_MESSAGE);


                            }
                        }


                    } else {
                        JOptionPane.showMessageDialog(this, "No such UserName Password Combination  Found ", "Failed", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (HeadlessException | SQLException sq) {
                    JOptionPane.showMessageDialog(this, sq.getMessage());
                }
            } else {
                JOptionPane.showMessageDialog(this, "Password Mismatch:   " + newPass + "   and   " + pass2 + "   :Not The Same", "Check Password", JOptionPane.ERROR_MESSAGE);
            }

        }

    }

}
