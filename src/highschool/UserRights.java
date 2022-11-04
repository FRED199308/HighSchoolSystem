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
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import static javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE;

import javax.swing.table.DefaultTableModel;

import net.miginfocom.swing.MigLayout;

/**
 * @author FRED
 */
public class UserRights extends JFrame implements ActionListener {

    public static void main(String[] args) {
        UserRights start = new UserRights();
    }

    private int width = 1100;
    private int height = 620;
    private FredCheckBox jDel;
    private FredTextField timeout;


    private FredCheckBox jEdite;
    private FredCheckBox paysalaries;
    private FredCheckBox delete;


    private FredCheckBox removeformpayment;


    private FredCheckBox writeoffbalance;

    private FredCheckBox recievepayment;

    private FredCheckBox smsing;
    private FredCheckBox expensisRecord;
    private FredCheckBox createcustompay;


    private FredCheckBox reviewjobgroups;
    private FredCheckBox promote;


    private FredLabel infor1, infor;
    private PreparedStatement ps;
    private ResultSet rs;
    private Connection con;
    private String usermodified;

    String employeecode;
    String[] option = {"Select User"};
    JTable tab;
    private DefaultTableModel model;
    private JScrollPane pane;
    private JPanel top;
    private JPanel bottom;
    private JButton deleteuser;
    private FredButton modifyRights;
    private FredButton deactivate;
    private FredButton activate;
    private FredButton cancel;
    private FredButton ResetPass;
    private FredButton back;
    private FredButton save;
    private FredButton modify;

    private FredButton back1;
    private JPanel panel;
    ;
    String depcode = "";

    @SuppressWarnings("OverridableMethodCallInConstructor")
    public UserRights() {
        setSize(width, height);
        setTitle("      User Management Panel");
        setLayout(new MigLayout());
        setLocationRelativeTo(null);
        this.setIconImage(FrameProperties.icon());
        deleteuser = new JButton("Delete User");
        deactivate = new FredButton("Deactivate UserAccount");
        activate = new FredButton("Activate User Account");
        cancel = new FredButton("CLOSE");
        ResetPass = new FredButton("Reset Password");
        modifyRights = new FredButton("Modify User Rights");
        modify = new FredButton("Modify System Time Out");
        back = new FredButton("Back");
        panel = new JPanel();
        paysalaries = new FredCheckBox("Pay Salaries");
        writeoffbalance = new FredCheckBox("Write Off School Fee Balance");
        createcustompay = new FredCheckBox("Create Custom Payment");
        jDel = new FredCheckBox("Delete Right");

        jEdite = new FredCheckBox("Edit Data");

        recievepayment = new FredCheckBox("Recieve All Payments");

        smsing = new FredCheckBox("Use Communication Module");
        removeformpayment = new FredCheckBox("Remove Student From Payment Category");
        reviewjobgroups = new FredCheckBox("Review Salary Job Groups");
        promote = new FredCheckBox("Promote/Demote Student");
        expensisRecord = new FredCheckBox("Record Expenses");

        timeout = new FredTextField();
        infor1 = new FredLabel("System Time Out(Sec.)");


        save = new FredButton("Save");
        infor = new FredLabel("Registered System Users");
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        ((JComponent) getContentPane()).setBorder(
                BorderFactory.createMatteBorder(5, 5, 5, 5, Color.pink));
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
        top = new JPanel();
        bottom = new JPanel();
        back1 = new FredButton("Back");
        tab = new JTable();
        pane = new JScrollPane(tab);
        model = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }

        };
        tab.setModel(model);
        tab.setRowHeight(25);

        Object cols[] = {"UserName", "Name", "Status", "Level", "SystemTimeOut(Sec.)"};
        model.setColumnIdentifiers(cols);
        getContentPane().setBackground(Color.lightGray);

        Object rows[];
        try {
            String querry = "Select * from userAccounts";
            rows = new Object[5];
            con = DbConnection.connectDb();
            ps = con.prepareStatement(querry);
            rs = ps.executeQuery();
            while (rs.next()) {
                rows[0] = rs.getString("username");
                rows[2] = rs.getString("Status");
                rows[3] = rs.getString("level");
                rows[4] = rs.getString("Timeout");

                employeecode = rs.getString("employeecode");
                String querry2 = "Select firstname,middlename,lastname,departmentcode from staffs where employeecode='" + employeecode + "' ";
                PreparedStatement ps1;
                ResultSet rs1;
                ps1 = con.prepareStatement(querry2);
                rs1 = ps1.executeQuery();

                while (rs1.next()) {
                    String name = (rs1.getString("firstname") + "    " + rs1.getString("middlename") + "    " + rs1.getString("lastname"));
                    rows[1] = name;
                    depcode = rs1.getString("Departmentcode");

                }
                if (depcode.equalsIgnoreCase(Globals.depcode)) {
                    model.addRow(rows);
                }


            }


        } catch (SQLException sq) {
            JOptionPane.showMessageDialog(this, sq.getMessage());
        }

        add(top, "push,grow,wrap");
        top.setLayout(new MigLayout());
        bottom.setLayout(new MigLayout());
        add(bottom, "push,grow");
        top.add(infor, "gapleft 80,pushx,growx,wrap");
        top.add(pane, "gapleft 50,pushx,growx,gapright 30");
        bottom.add(activate, "pushx,growx,shrink 25");
        bottom.add(deactivate, "pushx,growx,shrink 25");
        bottom.add(deleteuser, "pushx,growx,shrink 25,wrap");


        bottom.add(ResetPass, "pushx,growx,shrink 25");


        bottom.add(back1, "pushx,growx,shrink 25,split");
        bottom.add(modify, "pushx,growx,shrink 25");
        bottom.add(cancel, "pushx,growx,shrink 25");
        top.setBackground(Color.LIGHT_GRAY);
        bottom.setBackground(Color.DARK_GRAY);
        cancel.addActionListener(this);
        modifyRights.addActionListener(this);
        back.addActionListener(this);
        save.addActionListener(this);
        activate.addActionListener(this);
        deactivate.addActionListener(this);
        deleteuser.addActionListener(this);
        ResetPass.addActionListener(this);
        modify.addActionListener(this);

        back1.addActionListener(this);


        setVisible(true);

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object obj = e.getSource();
        if (obj == modify) {
            if (tab.getSelectedRowCount() < 1) {
                JOptionPane.showMessageDialog(this, "Please Select The User To  modify His/Her System Time Out (In Seconds)");
            } else {
                int selectedrow = tab.getSelectedRow();
                String user = model.getValueAt(selectedrow, 0).toString();

                String time = JOptionPane.showInputDialog(this, "Enter The New System Time Out (In Seconds)");


                if (DataValidation.number(time)) {
                    int TIME = Integer.parseInt(time);
                    try {
                        String sql = "Update useraccounts set timeout='" + TIME + "' where username='" + user + "'";
                        ps = con.prepareStatement(sql);
                        ps.execute();
                        JOptionPane.showMessageDialog(this, "New System Time Out affected Successfully\n The New Time Out Will Used From Next User Loggin Session");
                        model.setValueAt(time, selectedrow, 4);

                    } catch (Exception sq) {
                        JOptionPane.showMessageDialog(this, sq.getMessage());
                        sq.printStackTrace();
                    }
                } else {
                    JOptionPane.showMessageDialog(this, "Enter A Valid Time Out Value");
                }
            }

        }
        if (obj == cancel) {
            try {
                con.close();
            } catch (SQLException sq) {

            }
            CurrentFrame.currentWindow();
            dispose();
        } else if (obj == modifyRights) {
            if (tab.getSelectedRowCount() < 1) {
                getToolkit().beep();
                JOptionPane.showMessageDialog(this, "Kindly Select The User to modify His/Her Rights", "Selection Omitted", JOptionPane.ERROR_MESSAGE);
            } else {
                setTitle("                User Right Modification Panel. USER: " + tab.getValueAt(tab.getSelectedRow(), 0));
                int selected = tab.getSelectedRow();
                usermodified = model.getValueAt(selected, 0).toString();
                String level = model.getValueAt(selected, 3).toString();
                rightsIntialiser();
                this.remove(top);
                this.remove(bottom);
                revalidate();
                repaint();

                panel = assignRights();

                panel.setBackground(Color.white);

                try {
                    String querry = "Select * from userAccounts where username='" + usermodified + "'";

                    ps = con.prepareStatement(querry);
                    rs = ps.executeQuery();
                    while (rs.next()) {
                        timeout.setText(rs.getString("timeout"));

                    }
                } catch (SQLException sq) {
                    JOptionPane.showMessageDialog(null, sq, "Error", JOptionPane.INFORMATION_MESSAGE);
                }

                this.add(panel, "grow,push");

                revalidate();
                repaint();
            }
        } else if (obj == back) {
            setTitle("      User Management Panel");
            this.remove(panel);
            this.setLayout(new MigLayout());
            this.setResizable(true);
            revalidate();
            repaint();
            add(top, "push,grow,wrap");
            add(bottom, "push,grow");

        } else if (obj == activate) {
            if (tab.getSelectedRowCount() < 1) {
                getToolkit().beep();
                JOptionPane.showMessageDialog(this, "Kindly select The user  to activate his/her account", "Selection Omitted", JOptionPane.ERROR_MESSAGE);
            } else {
                int selected = tab.getSelectedRow();
                try {
                    String username = model.getValueAt(selected, 0).toString();
                    String querry = "update useraccounts set status='" + "ACTIVE" + "' where Username='" + username + "'";
                    ps = con.prepareStatement(querry);
                    ps.execute();
                    JOptionPane.showMessageDialog(this, "User Account Activated", "Success", JOptionPane.INFORMATION_MESSAGE);
                    tab.setValueAt("ACTIVE", selected, 2);
                } catch (HeadlessException | SQLException sq) {
                    JOptionPane.showMessageDialog(this, sq.getMessage());
                }
            }

        } else if (obj == deactivate) {
            if (tab.getSelectedRowCount() < 1) {
                getToolkit().beep();
                JOptionPane.showMessageDialog(this, "Kindly select The user  to activate his/her account", "Selection Omitted", JOptionPane.ERROR_MESSAGE);
            } else {
                int selected = tab.getSelectedRow();
                try {
                    String username = model.getValueAt(selected, 0).toString();
                    String querry = "update useraccounts set status='" + "INACTIVE" + "' where Username='" + username + "'";
                    ps = con.prepareStatement(querry);
                    ps.execute();
                    JOptionPane.showMessageDialog(this, "User Account Deactivated", "Success", JOptionPane.INFORMATION_MESSAGE);
                    tab.setValueAt("INACTIVE", selected, 2);
                } catch (HeadlessException | SQLException sq) {
                    JOptionPane.showMessageDialog(this, sq.getMessage());
                }
            }

        } else if (obj == deleteuser) {

            if (tab.getSelectedRowCount() < 1) {
                getToolkit().beep();
                JOptionPane.showMessageDialog(this, "Kindly select The user  Account To Delete", "Selection Omitted", JOptionPane.ERROR_MESSAGE);
            } else {
                int warn = JOptionPane.showConfirmDialog(this, "Are you Sure You Want To delete This Account??\n It is Highly Advisable To Deacivate"
                        + "An Account Instead Of Deleting ,\n Note that All Transaction Committed By The user Will be Disposed, \n Unless no Refference is To be made On His/Her Transaction \n"
                        + "its The Only Time When Deletion Is Advisable", "Confirm", JOptionPane.YES_NO_OPTION);
                if (warn == JOptionPane.YES_OPTION) {
                    int selected = tab.getSelectedRow();
                    try {
                        String username = model.getValueAt(selected, 0).toString();
                        String querry = "Delete from useraccounts  where Username='" + username + "'";
                        ps = con.prepareStatement(querry);
                        ps.execute();
                        JOptionPane.showMessageDialog(this, "User Account Deleted", "Success", JOptionPane.INFORMATION_MESSAGE);
                        model.removeRow(selected);
                    } catch (HeadlessException | SQLException sq) {
                        JOptionPane.showMessageDialog(null, sq.getMessage(), "Error has occured while deleting", JOptionPane.INFORMATION_MESSAGE);
                    }
                } else if (warn == JOptionPane.NO_OPTION) {
                    JOptionPane.showMessageDialog(this, "Operation Cancelled");
                }
            }

        } else if (obj == save) {
            int selected = tab.getSelectedRow();
            boolean comply = true;
            DataValidation validate = new DataValidation();
            if (DataValidation.number2(timeout.getText()) == false && comply == true) {
                getToolkit().beep();
                JOptionPane.showMessageDialog(this, "Invalid System Time out Time", "Invalid Input", JOptionPane.ERROR_MESSAGE);
                comply = false;
            }
            if (comply == true) {
                try {
                    String state;
                    if (jEdite.isSelected()) {
                        state = "True";
                        String querry = "Update userrights set status='" + state + "' where rightid='" + "RG0001" + "' and Username='" + usermodified + "'";
                        ps = con.prepareStatement(querry);
                        ps.execute();
                    } else {
                        state = "False";
                        String querry = "Update userrights set status='" + state + "' where rightid='" + "RG0001" + "' and Username='" + usermodified + "'";
                        ps = con.prepareStatement(querry);
                        ps.execute();
                    }
                    if (jDel.isSelected()) {
                        state = "True";
                        String querry = "Update userrights set status='" + state + "' where rightid='" + "RG0002" + "' and Username='" + usermodified + "'";
                        ps = con.prepareStatement(querry);
                        ps.execute();
                    } else {
                        state = "False";
                        String querry = "Update userrights set status='" + state + "' where rightid='" + "RG0002" + "' and Username='" + usermodified + "'";
                        ps = con.prepareStatement(querry);
                        ps.execute();
                    }
                    if (paysalaries.isSelected()) {
                        state = "True";
                        String querry = "Update userrights set status='" + state + "' where rightid='" + "RG0003" + "' and Username='" + usermodified + "'";
                        ps = con.prepareStatement(querry);
                        ps.execute();
                    } else {
                        state = "False";
                        String querry = "Update userrights set status='" + state + "' where rightid='" + "RG0003" + "' and Username='" + usermodified + "'";
                        ps = con.prepareStatement(querry);
                        ps.execute();
                    }
                    if (expensisRecord.isSelected()) {
                        state = "True";
                        String querry = "Update userrights set status='" + state + "' where rightid='" + "RG0004" + "' and Username='" + usermodified + "'";
                        ps = con.prepareStatement(querry);
                        ps.execute();
                    } else {
                        state = "False";
                        String querry = "Update userrights set status='" + state + "' where rightid='" + "RG0004" + "' and Username='" + usermodified + "'";
                        ps = con.prepareStatement(querry);
                        ps.execute();
                    }
                    if (recievepayment.isSelected()) {
                        state = "True";
                        String querry = "Update userrights set status='" + state + "' where rightid='" + "RG0005" + "' and Username='" + usermodified + "'";
                        ps = con.prepareStatement(querry);
                        ps.execute();
                    } else {
                        state = "False";
                        String querry = "Update userrights set status='" + state + "' where rightid='" + "RG0005" + "' and Username='" + usermodified + "'";
                        ps = con.prepareStatement(querry);
                        ps.execute();
                    }
                    if (createcustompay.isSelected()) {
                        state = "True";
                        String querry = "Update userrights set status='" + state + "' where rightid='" + "RG0006" + "' and Username='" + usermodified + "'";
                        ps = con.prepareStatement(querry);
                        ps.execute();
                    } else {
                        state = "False";
                        String querry = "Update userrights set status='" + state + "' where rightid='" + "RG0006" + "' and Username='" + usermodified + "'";
                        ps = con.prepareStatement(querry);
                        ps.execute();
                    }
                    if (smsing.isSelected()) {
                        state = "True";
                        String querry = "Update userrights set status='" + state + "' where rightid='" + "RG0007" + "' and Username='" + usermodified + "'";
                        ps = con.prepareStatement(querry);
                        ps.execute();
                    } else {
                        state = "False";
                        String querry = "Update userrights set status='" + state + "' where rightid='" + "RG0007" + "' and Username='" + usermodified + "'";
                        ps = con.prepareStatement(querry);
                        ps.execute();
                    }
                    if (reviewjobgroups.isSelected()) {
                        state = "True";
                        String querry = "Update userrights set status='" + state + "' where rightid='" + "RG0008" + "' and Username='" + usermodified + "'";
                        ps = con.prepareStatement(querry);
                        ps.execute();
                    } else {
                        state = "False";
                        String querry = "Update userrights set status='" + state + "' where rightid='" + "RG0008" + "' and Username='" + usermodified + "'";
                        ps = con.prepareStatement(querry);
                        ps.execute();
                    }
                    if (promote.isSelected()) {
                        state = "True";
                        String querry = "Update userrights set status='" + state + "' where rightid='" + "RG0009" + "' and Username='" + usermodified + "'";
                        ps = con.prepareStatement(querry);
                        ps.execute();
                    } else {
                        state = "False";
                        String querry = "Update userrights set status='" + state + "' where rightid='" + "RG0009" + "' and Username='" + usermodified + "'";
                        ps = con.prepareStatement(querry);
                        ps.execute();
                    }
                    if (writeoffbalance.isSelected()) {
                        state = "True";
                        String querry = "Update userrights set status='" + state + "' where rightid='" + "RG00010" + "' and Username='" + usermodified + "'";
                        ps = con.prepareStatement(querry);
                        ps.execute();
                    } else {
                        state = "False";
                        String querry = "Update userrights set status='" + state + "' where rightid='" + "RG00010" + "' and Username='" + usermodified + "'";
                        ps = con.prepareStatement(querry);
                        ps.execute();
                    }

                    String querry = "update useraccounts set timeOut='" + timeout.getText() + "' where username='" + usermodified + "'";

                    ps = con.prepareStatement(querry);
                    ps.execute();
                    model.setValueAt(timeout.getText(), selected, 4);

                    JOptionPane.showMessageDialog(this, "Rights  And System Time Out Modified\n The new System Time Out Starts From The Next UserLogin Session", "Success", JOptionPane.INFORMATION_MESSAGE);

                } catch (HeadlessException | SQLException sq) {
                    JOptionPane.showMessageDialog(null, sq.getMessage(), "Error", JOptionPane.INFORMATION_MESSAGE);
                }
            }

        } else if (obj == ResetPass) {
            if (tab.getSelectedRowCount() < 1) {
                getToolkit().beep();
                JOptionPane.showMessageDialog(this, "Kindly select The user  to activate his/her account", "Selection Omitted", JOptionPane.ERROR_MESSAGE);
            } else {
                int selected = tab.getSelectedRow();
                try {
                    String username = model.getValueAt(selected, 0).toString();
                    String querry = "update useraccounts set password='" + DataEncriptor.encript("pass") + "' where Username='" + username + "'";
                    ps = con.prepareStatement(querry);
                    ps.execute();
                    JOptionPane.showMessageDialog(this, "Password Reset Successfull \n The user should login with the default password  :  pass", "Success", JOptionPane.INFORMATION_MESSAGE);

                } catch (HeadlessException | SQLException sq) {
                    JOptionPane.showMessageDialog(null, sq, "Error", JOptionPane.INFORMATION_MESSAGE);
                }
            }

        } else if (obj == back1) {

            model.getDataVector().removeAllElements();
            Object cols[] = {"UserName", "Name", "Status", "Level", "SystemTimeOut(Sec.)"};
            model.setColumnIdentifiers(cols);

            deactivate.setEnabled(true);
            deleteuser.setEnabled(true);
            modifyRights.setEnabled(true);
            activate.setEnabled(true);
            ResetPass.setEnabled(true);
            Object rows[];
            try {
                String querry = "Select * from userAccounts";
                rows = new Object[5];

                ps = con.prepareStatement(querry);
                rs = ps.executeQuery();
                while (rs.next()) {
                    rows[0] = rs.getString("username");
                    rows[2] = rs.getString("Status");
                    rows[3] = rs.getString("level");
                    rows[4] = rs.getString("Timeout");

                    employeecode = rs.getString("employeecode");
                    String querry2 = "Select firstname,middlename,lastname,departmentcode from staffs where employeecode='" + employeecode + "' ";
                    PreparedStatement ps1;
                    ResultSet rs1;
                    ps1 = con.prepareStatement(querry2);
                    rs1 = ps1.executeQuery();

                    while (rs1.next()) {
                        String name = (rs1.getString("firstname") + "    " + rs1.getString("middlename") + "    " + rs1.getString("lastname"));
                        rows[1] = name;
                        depcode = rs1.getString("Departmentcode");

                    }
                    if (depcode.equalsIgnoreCase(Globals.depcode)) {

                        model.addRow(rows);
                    }

                    if (depcode.equalsIgnoreCase(Globals.depcode)) {

                        modifyRights.setEnabled(false);
                    }

                }

            } catch (SQLException sq) {
                JOptionPane.showMessageDialog(this, sq.getMessage());
            }

        }

    }

    public JPanel assignRights() {
        JPanel ss = new JPanel();
        ss.setBackground(Color.GREEN);
        ss.setLayout(new MigLayout());


        ss.add(infor1, "gapleft 150,gaptop 30");
        ss.add(timeout, "gapleft 150,gaptop 30,growx,pushx,span 2 1,wrap");
        ss.add(jEdite, "growx,pushx,gapleft 30,gaptop 50");
        ss.add(jDel, "growx,pushx,gapleft 50,gaptop 50");
        ss.add(paysalaries, "growx,pushx,gapleft 50,gaptop 50,wrap");
        ss.add(expensisRecord, "growx,pushx,gapleft 50,gaptop 50");
        ss.add(createcustompay, "growx,pushx,gapleft 80,gaptop 50");
        ss.add(smsing, "growx,pushx,gapleft 50,gaptop 50,wrap");
        ss.add(recievepayment, "growx,pushx,gapleft 50,gaptop 50");
        ss.add(reviewjobgroups, "growx,pushx,gapleft 50,gaptop 50");
        ss.add(promote, "growx,pushx,gapleft 30,gaptop 50,wrap");
        ss.add(writeoffbalance, "growx,pushx,gapleft 50,gaptop 50,skip,wrap");

        ss.add(back, "gapleft 150,growx,pushx,gaptop 70");
        ss.add(save, "gapleft 150,growx,pushx,gaptop 70");

        return ss;

    }

    public void rightsIntialiser() {
        try {
            String querry1 = "Select * from Userrights where username='" + usermodified + "'";
            con = DbConnection.connectDb();
            ps = con.prepareStatement(querry1);
            rs = ps.executeQuery();
            while (rs.next()) {

                if (rs.getString("Rightid").equalsIgnoreCase("RG0001") && rs.getString("status").equalsIgnoreCase("true")) {
                    jEdite.setSelected(true);
                } else if (rs.getString("Rightid").equalsIgnoreCase("RG0002") && rs.getString("status").equalsIgnoreCase("true")) {
                    jDel.setSelected(true);
                } else if (rs.getString("Rightid").equalsIgnoreCase("RG0003") && rs.getString("status").equalsIgnoreCase("true")) {
                    paysalaries.setSelected(true);
                } else if (rs.getString("Rightid").equalsIgnoreCase("RG0004") && rs.getString("status").equalsIgnoreCase("true")) {
                    expensisRecord.setSelected(true);
                } else if (rs.getString("Rightid").equalsIgnoreCase("RG0005") && rs.getString("status").equalsIgnoreCase("true")) {
                    recievepayment.setSelected(true);
                } else if (rs.getString("Rightid").equalsIgnoreCase("RG0006") && rs.getString("status").equalsIgnoreCase("true")) {
                    createcustompay.setSelected(true);
                } else if (rs.getString("Rightid").equalsIgnoreCase("RG0007") && rs.getString("status").equalsIgnoreCase("true")) {
                    smsing.setSelected(true);
                } else if (rs.getString("Rightid").equalsIgnoreCase("RG0008") && rs.getString("status").equalsIgnoreCase("true")) {
                    reviewjobgroups.setSelected(true);
                } else if (rs.getString("Rightid").equalsIgnoreCase("RG0009") && rs.getString("status").equalsIgnoreCase("true")) {
                    promote.setSelected(true);
                } else if (rs.getString("Rightid").equalsIgnoreCase("RG00010") && rs.getString("status").equalsIgnoreCase("true")) {
                    writeoffbalance.setSelected(true);
                }

            }
        } catch (SQLException sq) {

            JOptionPane.showMessageDialog(null, sq.getMessage(), "Error", JOptionPane.INFORMATION_MESSAGE);

        }
    }


}
