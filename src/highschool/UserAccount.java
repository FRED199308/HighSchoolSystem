/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package highschool;

/**
 * @author FRED_ADMIN
 */

import java.awt.Color;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JButton;
import javax.swing.JFrame;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.BorderFactory;

import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;

import static javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE;

public class UserAccount extends JFrame implements ActionListener {

    public static void main(String[] args) {
        new UserAccount();
    }

    private int width = 700;
    private int height = 450;
    private Connection con;
    private PreparedStatement ps;
    private FredLabel username;
    @SuppressWarnings("FieldMayBeFinal")
    private FredLabel userLevel;
    private JButton create;
    private JButton cancel;
    private FredLabel user;
    private FredCombo ur;
    private String[] le = {"Normal", "Admin"};
    private FredCombo Staffs;
    private String[] LEVEL = {"Select Level"};
    private ResultSet rs;
    private DbConnection one = new DbConnection();
    FredTextField jUsername;
    private String combined;
    private FredLabel SystemTimeOut;
    private FredTextField jSystemTimeOut;

    String lname;
    String mname;
    String fname;
    String employeeCode;
    String level;
    String userName;

    @SuppressWarnings("OverridableMethodCallInConstructor")
    public UserAccount() {
        setLayout(null);
        setTitle("User Account creation");
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
        setSize(width, height);
        setLocationRelativeTo(null);
        getContentPane().setBackground(Color.cyan);
        setLayout(null);

        this.setIconImage(FrameProperties.icon());
        ((JComponent) getContentPane()).setBorder(
                BorderFactory.createMatteBorder(5, 5, 5, 5, Color.MAGENTA));
        String[] e = {"Select User"};
        ur = new FredCombo("Select Level");
        ur.addItem(le[0]);
        ur.addItem(le[1]);
        con = DbConnection.connectDb();
        Staffs = new FredCombo("Select User");
        ur.setSelectedIndex(0);
        String[] inter = null;
        jSystemTimeOut = new FredTextField();
        SystemTimeOut = new FredLabel("Sytem Time Out In (Sec.)");

        try {
            con = DbConnection.connectDb();

            String querr = "select firstname,middlename,lastname  from staffs where Departmentcode='" + Globals.depcode + "'   order by FirstName ASC";
            ps = con.prepareStatement(querr);
            rs = ps.executeQuery();
            while (rs.next()) {
                fname = rs.getString("firstname");
                mname = rs.getString("middlename");
                lname = rs.getString("lastname");
                combined = fname + "   " + mname + "   " + lname;
                Staffs.addItem(combined);
            }


        } catch (SQLException sq) {
            JOptionPane.showMessageDialog(this, sq);
        }

        username = new FredLabel("Username");
        userLevel = new FredLabel("UserLevel");
        user = new FredLabel("User");

        create = new JButton("CREATE");
        cancel = new JButton("CANCEL");
        create.addActionListener(this);
        cancel.addActionListener(this);

        jUsername = new FredTextField();

        username.setBounds(80, 30, 100, 30);
        jUsername.setBounds(350, 30, 250, 30);
        userLevel.setBounds(80, 130, 250, 30);
        ur.setBounds(350, 130, 250, 30);

        user.setBounds(80, 210, 100, 30);
        Staffs.setBounds(350, 210, 250, 30);
        SystemTimeOut.setBounds(80, 275, 150, 30);
        add(SystemTimeOut);
        jSystemTimeOut.setBounds(350, 275, 250, 30);
        add(jSystemTimeOut);

        create.setBounds(150, 340, 100, 30);
        cancel.setBounds(400, 340, 100, 30);

        add(username);
        add(jUsername);
        add(user);
        add(userLevel);
        add(ur);
        add(Staffs);
        add(create);
        add(cancel);

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
        } else if (obj == create) {
            DataValidation validate = new DataValidation();
            boolean comply = true;
            if (DataValidation.name(jUsername.getText()) == false && comply == true) {
                getToolkit().beep();
                JOptionPane.showMessageDialog(this, "Kindly Input a Valid UserName", "Invalid Entry", JOptionPane.ERROR_MESSAGE);
                comply = false;
            } else if (ur.getSelectedItem().toString().equalsIgnoreCase("Select Level") && comply == true) {
                getToolkit().beep();
                JOptionPane.showMessageDialog(this, "Kindly Select User Level", "Level Not Selected", JOptionPane.ERROR_MESSAGE);
                comply = false;
            } else if (Staffs.getSelectedItem().toString().equalsIgnoreCase("Select User") && comply == true) {
                getToolkit().beep();
                JOptionPane.showMessageDialog(this, "Kindly Select User ", "User Not Selected", JOptionPane.ERROR_MESSAGE);
                comply = false;
            } else if (DataValidation.number(jSystemTimeOut.getText()) == false && comply == true) {
                getToolkit().beep();
                JOptionPane.showMessageDialog(this, "Invalid System Time out Time", "Invalid Input", JOptionPane.ERROR_MESSAGE);
                comply = false;
            }
            boolean status;
            try {
                status = ConfigurationIntialiser.multipleAdmins();

                if (status == false && ur.getSelectedItem().toString().equalsIgnoreCase("Admin")) {
                    String querry5 = "select Level from userAccounts where Level='" + "Admin" + "'";
                    ps = con.prepareStatement(querry5);
                    rs = ps.executeQuery();
                    if (rs.next()) {
                        comply = false;
                        getToolkit().beep();
                        JOptionPane.showMessageDialog(this, "The Current System Configurations Do Not Allow Multiple Admin accounts", "Integrity Constraint", JOptionPane.ERROR_MESSAGE);
                    }

                }
            } catch (HeadlessException | SQLException sq) {
                JOptionPane.showMessageDialog(this, sq.getMessage());
            }

            if (comply == true) {
                userName = jUsername.getText();
                level = ur.getSelectedItem().toString();
                JPasswordField jj = new JPasswordField();

                String password = "pass";
                jj.setText(password);
                try {
                    DbConnection one = new DbConnection();
                    con = DbConnection.connectDb();

                    String querry1 = "Select firstname,middlename,lastname,employeecode from staffs  ";
                    ps = con.prepareStatement(querry1);
                    rs = ps.executeQuery();
                    while (rs.next()) {
                        String membername = Staffs.getSelectedItem().toString();
                        combined = rs.getString("Firstname") + "   " + rs.getString("Middlename") + "   " + rs.getString("lastname");
                        if (membername.equalsIgnoreCase(combined)) {
                            employeeCode = rs.getString("employeecode");
                            break;
                        }
                    }

                    String querry2 = "Select * from userAccounts where username='" + userName + "' or employeecode='" + employeeCode + "'";
                    ps = con.prepareStatement(querry2);
                    rs = ps.executeQuery();

                    if (rs.next()) {
                        if (rs.getString("employeecode").equalsIgnoreCase(employeeCode)) {
                            getToolkit().beep();
                            JOptionPane.showMessageDialog(this, "Member Selected Already Has an Account", "Duplicate Prevention", JOptionPane.WARNING_MESSAGE);
                            comply = false;
                        }
                        if (rs.getString("username").equalsIgnoreCase(jUsername.getText())) {
                            getToolkit().beep();
                            JOptionPane.showMessageDialog(this, "Username Already Taken", "Duplicate Prevention", JOptionPane.WARNING_MESSAGE);
                            comply = false;
                        }
                    }
                    if (comply == true) {

                        String querr = "select firstname,middlename,lastname,employeecode  from staffs";
                        ps = con.prepareStatement(querr);
                        rs = ps.executeQuery();
                        while (rs.next()) {
                            fname = rs.getString("firstname");
                            mname = rs.getString("middlename");
                            lname = rs.getString("lastname");
                            employeeCode = rs.getString("employeecode");
                            String combined = fname + "   " + mname + "   " + lname;
                            if (combined.equals(Staffs.getSelectedItem().toString())) {
                                break;
                            }

                        }


                        String querry3 = "insert into Useraccounts Values('" + userName + "','" + employeeCode + "','" + level + "','" + DataEncriptor.encript(password) + "','" + jSystemTimeOut.getText() + "','" + "ACTIVE" + "')";
                        ps = con.prepareStatement(querry3);
                        ps.execute();
                        if (level.equalsIgnoreCase("normal")) {
                            for (int i = 1; i < 11; ++i) {

                                String querry4 = "Insert into userrights values('" + userName + "','" + "RG000" + i + "','" + "False" + "')";
                                ps = con.prepareStatement(querry4);
                                ps.execute();

                            }
                        } else {

                            for (int i = 1; i < 11; ++i) {

                                String querry4 = "Insert into userrights values('" + userName + "','" + "RG000" + i + "','" + "true" + "')";
                                ps = con.prepareStatement(querry4);
                                ps.execute();

                            }
                        }

                        JOptionPane.showMessageDialog(this, "User account created successfuly login with default password " + password);
                        jUsername.setText("");
                        jSystemTimeOut.setText("");
                        ur.setSelectedIndex(0);
                        Staffs.setSelectedIndex(0);

                    }

                } catch (HeadlessException | SQLException sq) {

                    JOptionPane.showMessageDialog(this, sq.getMessage());
                }

            }
        }

    }


}
