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
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;

import javax.swing.table.DefaultTableModel;

import net.miginfocom.swing.MigLayout;

/**
 * @author FRED
 */
public class StaffManagement implements ActionListener {

    private JPanel bottom;
    private JPanel top;
    private JPanel holder;
    private JTable tab;
    private DefaultTableModel model;
    private JScrollPane pane;
    private FredCheckBox totalexpected, cleared;
    private FredLabel infor, filter, search;
    private FredButton print, back, edit, delete, view, save, update;
    private PreparedStatement ps;
    private ResultSet rs;

    private Connection con = DbConnection.connectDb();
    private Object cols[] = {"No.", "Staff Name", "Id Number", "Gender", "Staff Number", "Status"};
    Object row[] = new Object[cols.length];
    String empid = "";
    private FredLabel firstname = new FredLabel("First Name");
    FredLabel mname = new FredLabel("Middle Name");
    FredLabel lname = new FredLabel("Last Name");
    FredLabel gender = new FredLabel("Gender");
    FredLabel idnumber = new FredLabel("Id Number");
    FredLabel email = new FredLabel("Email");
    FredLabel initial = new FredLabel("Teacher Initials");
    FredLabel phone = new FredLabel("Contact");
    FredLabel country = new FredLabel("Country");
    FredLabel county = new FredLabel("Country");
    FredLabel province = new FredLabel("Province");
    FredLabel constituency = new FredLabel("Constituency");
    FredLabel ward = new FredLabel("Ward");
    FredLabel dateofemployment = new FredLabel("Date Employed");
    FredLabel status = new FredLabel("Status");
    FredLabel dept = new FredLabel("Department");
    FredLabel employeeCode = new FredLabel("Employee Number");

    private FredLabel firstname1 = new FredLabel("First Name");
    FredLabel mname1 = new FredLabel("Middle Name");
    FredLabel lname1 = new FredLabel("Last Name");
    FredLabel gender1 = new FredLabel("Gender");
    FredLabel idnumber1 = new FredLabel("Id Number");
    FredLabel email1 = new FredLabel("Email");
    FredLabel initial1 = new FredLabel("Teachers Initials");
    FredLabel phone1 = new FredLabel("Contact");
    FredLabel country1 = new FredLabel("Country");
    FredLabel county1 = new FredLabel("Country");
    FredLabel province1 = new FredLabel("Province");
    FredLabel constituency1 = new FredLabel("Constituency");
    FredLabel ward1 = new FredLabel("Ward");
    FredLabel dateofemployment1 = new FredLabel("Date Employed");
    FredLabel status1 = new FredLabel("Status");
    FredLabel dept1 = new FredLabel("Department");
    FredLabel employeeCode1 = new FredLabel("TSC. No.");

    private FredTextField jfirstname = new FredTextField();
    private FredTextField jsearch = new FredTextField();
    FredTextField jmname = new FredTextField();
    FredTextField jlname = new FredTextField();
    FredCombo jgender = new FredCombo("Select Gender");
    FredTextField jidnumber = new FredTextField();
    FredTextField jemail = new FredTextField();
    FredTextField jintials = new FredTextField();
    FredTextField jphone = new FredTextField();
    FredCombo jcountry = new FredCombo("Select Country");
    FredCombo jcounty = new FredCombo("Select County");
    FredCombo jprovince = new FredCombo("Select Province");
    FredCombo jconstituency = new FredCombo("Select Constituency");
    FredCombo jward = new FredCombo("Select Ward");
    FredDateChooser jdateofemployment = new FredDateChooser();
    FredCombo jstatus = new FredCombo("Select Status");
    FredCombo jdept = new FredCombo("Select Department");
    FredButton staffInventory = new FredButton("Inventroy Issue History");

    public JPanel staffManagerPanel() {
        search = new FredLabel("Search Teacher By Teacher Number Or FirstName");
        holder = new JPanel();
        top = new JPanel();
        bottom = new JPanel();
        print = new FredButton("Generate Report");
        infor = new FredLabel("Current Teachers Registered");
        top.setLayout(new MigLayout());
        bottom.setLayout(new MigLayout());
        holder.setLayout(new MigLayout());
        holder.add(top, "grow,push");
        holder.add(bottom, "growy,pushy");
        bottom.setBackground(Color.WHITE);
        tab = new JTable();
        model = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int col) {
                return false;
            }
        };
        pane = new JScrollPane(tab);
        tab.setModel(model);
        model.setColumnIdentifiers(cols);
        top.add(infor, "gapleft 30,split");
        top.add(search, "gapleft 50");
        top.add(jsearch, "gapleft 30,growx,pushx,wrap");
        jstatus.addItem("Active");
        jstatus.addItem("Transfered");
        jstatus.addItem("Resigned");

        jdateofemployment.setDateFormatString("yyyy/MM/dd");
        jgender.addItem("Male");
        jgender.addItem("Female");
        print = new FredButton("Generate Report");
        back = new FredButton("Back");
        delete = new FredButton("Delete");

        save = new FredButton("Save");

        view = new FredButton("View Staff Details");
        edit = new FredButton("Edit Staff Details");
        update = new FredButton("Update Staff Details");

        bottom.add(edit, "gaptop 30,wrap");
        bottom.add(view, "gaptop 30,wrap");
        bottom.add(back, "gaptop 30,wrap");
        bottom.add(delete, "gaptop 30,wrap");
        bottom.add(staffInventory, "gaptop 30,wrap");
        top.add(pane, "grow,push,span,wrap");
        top.add(save, "gapleft 100,width 180:180:180,height 30:30:30");


        firstname1.setForeground(Color.RED);
        mname1.setForeground(Color.RED);
        lname1.setForeground(Color.RED);
        gender1.setForeground(Color.RED);
        idnumber1.setForeground(Color.RED);
        phone1.setForeground(Color.RED);
        email1.setForeground(Color.RED);
        province1.setForeground(Color.RED);
        country1.setForeground(Color.RED);
        county1.setForeground(Color.RED);
        ward1.setForeground(Color.RED);
        constituency1.setForeground(Color.RED);
        dept1.setForeground(Color.RED);
        employeeCode1.setForeground(Color.red);
        status1.setForeground(Color.red);
        initial1.setForeground(Color.red);
        dateofemployment1.setForeground(Color.red);


        try {

            String sqlt = "Select * from Departments";
            ps = con.prepareStatement(sqlt);
            rs = ps.executeQuery();
            while (rs.next()) {
                jdept.addItem(rs.getString("Name"));
            }
            int counter = 1;
            String sql2 = "Select firstName,Middlename,Departmentcode,LastName,employeecode,status,idnumber,gender from staffs";
            ps = con.prepareStatement(sql2);
            rs = ps.executeQuery();
            while (rs.next()) {

                row[0] = counter;
                row[1] = rs.getString("firstName") + "    " + rs.getString("Middlename") + "    " + rs.getString("LastName");
                row[2] = rs.getString("IdNumber");
                row[3] = rs.getString("gender");
                row[4] = rs.getString("EmployeeCode");
                row[5] = rs.getString("Status");

                model.addRow(row);
                counter++;


            }

        } catch (SQLException sq) {
            JOptionPane.showMessageDialog(holder, sq.getMessage());
        }


        jsearch.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();

                try {
                    while (model.getRowCount() > 0) {
                        model.removeRow(0);
                    }


                    int counter = 1;
                    String sql2 = "Select firstName,Middlename,LastName,Departmentcode,employeecode,status,idnumber,gender from staffs where firstname like '" + jsearch.getText() + "%' or middlename like '" + jsearch.getText() + "%' or lastname like '" + jsearch.getText() + "%' or employeecode like '" + jsearch.getText() + "%'";
                    ps = con.prepareStatement(sql2);
                    rs = ps.executeQuery();
                    while (rs.next()) {

                        row[0] = counter;
                        row[1] = rs.getString("firstName") + "    " + rs.getString("Middlename") + "    " + rs.getString("LastName");
                        row[2] = rs.getString("IdNumber");
                        row[3] = rs.getString("gender");
                        row[4] = rs.getString("EmployeeCode");
                        row[5] = rs.getString("Status");
                        if (rs.getString("Departmentcode").equalsIgnoreCase(Globals.depcode)) {
                            model.addRow(row);
                            counter++;
                        }

                    }


                } catch (SQLException sq) {

                }

            }

        });


        jphone.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();
                if (!Character.isDigit(c) || jphone.getText().length() > 10) {

                    e.consume();
                }

            }

        });
        jidnumber.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();
                if (!Character.isDigit(c) || jidnumber.getText().length() > 10) {

                    e.consume();
                }

            }

        });


        jfirstname.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();
                if (Character.isDigit(c) || jfirstname.getText().length() > 20) {

                    e.consume();
                }

            }

        });

        jmname.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();
                if (Character.isDigit(c) || jmname.getText().length() > 20) {

                    e.consume();
                }

            }

        });
        jlname.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();
                if (Character.isDigit(c) || jlname.getText().length() > 20) {

                    e.consume();
                }

            }

        });


        edit.addActionListener(this);
        update.addActionListener(this);
        view.addActionListener(this);
        back.addActionListener(this);
        delete.addActionListener(this);
        print.addActionListener(this);

        back.setEnabled(false);
        return holder;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object obj = e.getSource();

        if (obj == print) {
            ReportGenerator.generateReport(infor.getText(), "Employee", tab);
        } else if (obj == back) {
            edit.setEnabled(true);
            edit.setEnabled(true);
            update.setEnabled(false);

            back.setEnabled(false);
            view.setEnabled(true);
            top.removeAll();
            top.add(infor, "gapleft 50,Wrap");
            top.add(pane, "grow,push,wrap");
            top.add(save, "gapleft 100,width 180:180:180,height 30:30:30");


            infor.setText("Current Staffs Registered");


            top.revalidate();
            top.repaint();
        }
        if (obj == delete) {

            if (tab.getSelectedRowCount() < 1) {
                JOptionPane.showMessageDialog(holder, "Select The Staff To Vie His/Her Details");
            } else {

                back.setEnabled(true);
                int selectedrow = tab.getSelectedRow();
                empid = model.getValueAt(selectedrow, 4).toString();
                try {
                    String sql = "Delete from staffs where employeecode='" + empid + "'";
                    ps = con.prepareStatement(sql);
                    ps.execute();
                    JOptionPane.showMessageDialog(holder, "Teacher Deleted Successfully");
                    model.removeRow(selectedrow);
                } catch (Exception sq) {
                    sq.printStackTrace();
                    JOptionPane.showMessageDialog(holder, sq.getMessage());

                }


            }

        } else if (obj == view) {
            if (tab.getSelectedRowCount() < 1) {
                JOptionPane.showMessageDialog(holder, "Select The Staff To Vie His/Her Details");
            } else {
                edit.setEnabled(false);
                update.setEnabled(false);

                view.setEnabled(false);
                back.setEnabled(true);
                int selectedrow = tab.getSelectedRow();
                empid = model.getValueAt(selectedrow, 4).toString();

                top.removeAll();
                top.add(firstname, "gapleft 30,width 50:150:250,height 30:30:30");
                top.add(firstname1, "gapleft 50,width 50:250:250,height 30:30:30");
                top.add(mname, "gapleft 30,width 50:150:250,height 30:30:30");
                top.add(mname1, "gapleft 50,width 50:250:250,height 30:30:30,wrap");
                top.add(lname, "gapleft 30,gaptop 20,width 50:150:250,height 30:30:30");
                top.add(lname1, "gapleft 50,gaptop 20,width 50:250:250,height 30:30:30");
                top.add(gender, "gapleft 30,gaptop 20,width 50:150:250,height 30:30:30");
                top.add(gender1, "gapleft 50,gaptop 20,width 50:250:250,height 30:30:30,wrap");
                top.add(idnumber, "gapleft 30,gaptop 20,width 50:150:250,height 30:30:30");
                top.add(idnumber1, "gapleft 50,gaptop 20,width 50:250:250,height 30:30:30");
                top.add(phone, "gapleft 30,gaptop 20,width 50:150:250,height 30:30:30");
                top.add(phone1, "gapleft 50,gaptop 20,width 50:250:250,height 30:30:30,wrap");
                top.add(email, "gapleft 30,gaptop 20,width 50:150:250,height 30:30:30");
                top.add(email1, "gapleft 50,gaptop 20,width 50:250:250,height 30:30:30");
                top.add(initial, "gapleft 30,gaptop 20,width 50:150:250,height 30:30:30");
                top.add(initial1, "gapleft 50,gaptop 20,width 50:250:250,height 30:30:30,wrap");
                top.add(country, "gapleft 30,gaptop 20,width 50:150:250,height 30:30:30");
                top.add(country1, "gapleft 50,gaptop 20,width 50:250:250,height 30:30:30");
                top.add(province, "gapleft 30,gaptop 20,width 50:150:250,height 30:30:30");
                top.add(province1, "gapleft 50,gaptop 20,width 50:250:250,height 30:30:30,wrap");
                top.add(county, "gapleft 30,gaptop 20,width 50:150:250,height 30:30:30");
                top.add(county1, "gapleft 50,gaptop 20,width 50:250:250,height 30:30:30");
                top.add(constituency, "gapleft 30,gaptop 20,width 50:150:250,height 30:30:30");
                top.add(constituency1, "gapleft 50,gaptop 20,width 50:250:250,height 30:30:30,wrap");
                top.add(ward, "gapleft 30,gaptop 20,width 50:150:250,height 30:30:30");
                top.add(ward1, "gapleft 50,gaptop 20,width 50:250:250,height 30:30:30");
                top.add(dateofemployment, "gapleft 30,gaptop 20,width 50:150:250,height 30:30:30");
                top.add(dateofemployment1, "gapleft 50,gaptop 20,width 50:250:250,height 30:30:30,wrap");
                top.add(status, "gapleft 30,gaptop 20,width 50:150:250,height 30:30:30");
                top.add(status1, "gapleft 50,gaptop 20,width 50:250:250,height 30:30:30");
                top.add(dept, "gapleft 30,gaptop 20,width 50:150:250,height 30:30:30");
                top.add(dept1, "gapleft 50,gaptop 20,width 50:250:250,height 30:30:30,wrap");
                top.add(employeeCode, "gapleft 30,gaptop 20,width 50:150:250,height 30:30:30");
                top.add(employeeCode1, "gapleft 50,gaptop 20,width 50:250:250,height 30:30:30");


                top.revalidate();
                top.repaint();
                String sql = "Select * from staffs where employeecode='" + empid + "'";
                try {
                    ps = con.prepareStatement(sql);
                    rs = ps.executeQuery();
                    if (rs.next()) {
                        employeeCode1.setText(empid);
                        status1.setText(rs.getString("Status"));
                        phone1.setText(rs.getString("PhoneNumber"));
                        email1.setText(rs.getString("Email"));
                        firstname1.setText(rs.getString("FirstName"));
                        mname1.setText(rs.getString("MiddleName"));
                        lname1.setText(rs.getString("LastName"));
                        idnumber1.setText(rs.getString("IdNumber"));
                        dateofemployment1.setText(rs.getString("Dateemployed"));
                        initial1.setText(rs.getString("Initials"));
                        gender1.setText(rs.getString("Gender"));
                        String countryid = rs.getString("Countrycode");
                        String countyid = rs.getString("Countycode");
                        String provinceid = rs.getString("provincecode");
                        String constituencyid = rs.getString("Constituencycode");
                        String wardid = rs.getString("Wardcode");
                        String deptid = rs.getString("Departmentcode");


                        String sql1 = " Select countryName from countries where countrycode='" + countryid + "'";
                        ;
                        ps = con.prepareStatement(sql1);
                        rs = ps.executeQuery();
                        if (rs.next()) {

                            country1.setText(rs.getString("CountryName"));

                        }
                        String sql2 = "Select Provincename from provinces where provincecode='" + provinceid + "'";
                        ps = con.prepareStatement(sql2);
                        rs = ps.executeQuery();
                        if (rs.next()) {
                            province1.setText(rs.getString("ProvinceName"));

                        }
                        String sql7 = "Select Countyname from counties where countycode='" + countyid + "'";
                        ps = con.prepareStatement(sql7);
                        rs = ps.executeQuery();
                        if (rs.next()) {

                            county1.setText(rs.getString("Countyname"));

                        }
                        String sql4 = "Select ConstituencyName from Constituencies where constituencycode='" + constituencyid + "'";
                        ps = con.prepareStatement(sql4);
                        rs = ps.executeQuery();
                        if (rs.next()) {

                            constituency1.setText(rs.getString("Constituencyname"));

                        }
                        String sql5 = "Select WardName from ward where wardcode='" + wardid + "'";
                        ps = con.prepareStatement(sql5);
                        rs = ps.executeQuery();
                        if (rs.next()) {

                            ward1.setText(rs.getString("WardName"));

                        }

                        String sql6 = "Select Name from departments where Departmentcode='" + deptid + "'";
                        ps = con.prepareStatement(sql6);
                        rs = ps.executeQuery();
                        if (rs.next()) {

                            dept1.setText(rs.getString("Name"));

                        }


                    }

                } catch (SQLException sq) {
                    JOptionPane.showMessageDialog(holder, sq.getMessage());
                }


            }


        } else if (obj == update) {
            String DOA = ((JTextField) jdateofemployment.getDateEditor().getUiComponent()).getText();
            String init = jintials.getText();
            try {

                if (jfirstname.getText().isEmpty()) {

                    JOptionPane.showMessageDialog(holder, "Enter A Valid First Name");
                } else {
                    if (jmname.getText().isEmpty()) {

                        JOptionPane.showMessageDialog(holder, "Enter A Valid Middle  Name");
                    } else {
                        if (init.isEmpty()) {

                            JOptionPane.showMessageDialog(holder, "Enter Intials");
                        } else {
                            if (jgender.getSelectedIndex() == 0) {

                                JOptionPane.showMessageDialog(holder, "Select Gender");
                            } else {

                                if (jdept.getSelectedIndex() == 0) {
                                    JOptionPane.showMessageDialog(holder, "Select The Department");
                                } else {
                                    if (jdept.getSelectedItem().toString().equalsIgnoreCase("Finance") && Globals.depName.equalsIgnoreCase("Academics")) {
                                        JOptionPane.showMessageDialog(holder, "Sorry You Lack Sufficient Rights To Place A Staff Under Finance Department");
                                    } else {


                                        try {
                                            String wardcode = "", constituencycode = "", countycode = "", provincecode = "", countrycode = "";
                                            String sql = "Select countrycode from countries where countryname='" + jcountry.getSelectedItem() + "'";
                                            ps = con.prepareStatement(sql);
                                            rs = ps.executeQuery();
                                            if (rs.next()) {
                                                countrycode = rs.getString("Countrycode");

                                            }
                                            String sql2 = "Select provincecode from provinces where provincename='" + jprovince.getSelectedItem() + "'";
                                            ps = con.prepareStatement(sql2);
                                            rs = ps.executeQuery();
                                            if (rs.next()) {
                                                provincecode = rs.getString("provincecode");
                                            }

                                            String sql3 = "Select countycode from counties where countyname='" + jcounty.getSelectedItem() + "'";
                                            ps = con.prepareStatement(sql3);
                                            rs = ps.executeQuery();
                                            if (rs.next()) {
                                                countycode = rs.getString("Countycode");
                                            }
                                            String sql4 = "Select constituencycode from constituencies where constituencyname='" + jconstituency.getSelectedItem() + "'";
                                            ps = con.prepareStatement(sql4);
                                            rs = ps.executeQuery();
                                            if (rs.next()) {
                                                constituencycode = rs.getString("constituencycode");
                                            }
                                            String sql5 = "Select wardcode from ward where wardname='" + jward.getSelectedItem() + "'";
                                            ps = con.prepareStatement(sql5);
                                            rs = ps.executeQuery();
                                            if (rs.next()) {
                                                wardcode = rs.getString("wardcode");
                                            }
                                            String deptcode = "";
                                            String qq = "Select Departmentcode from departments where name='" + jdept.getSelectedItem() + "'";
                                            ps = con.prepareStatement(qq);
                                            rs = ps.executeQuery();
                                            if (rs.next()) {
                                                deptcode = rs.getString("DepartmentCode");

                                            }

                                            String sqll = "update  staffs set firstname='" + jfirstname.getText().toUpperCase() + "',middlename='" + jmname.getText().toUpperCase() + "',lastname='" + jlname.getText().toUpperCase() + "',Initials='" + init + "',gender='" + jgender.getSelectedItem() + "',idNumber='" + jidnumber.getText() + "',"
                                                    + "phonenumber='" + jphone.getText() + "',email='" + jemail.getText() + "',Departmentcode='" + deptcode + "',countrycode='" + countrycode + "',provincecode='" + provincecode + "',countycode='" + countycode + "',constituencycode='" + constituencycode + "',wardcode='" + wardcode + "',dateemployed='" + DOA + "',status='" + jstatus.getSelectedItem() + "' where employeecode='" + empid + "'";
                                            ps = con.prepareStatement(sqll);
                                            ps.execute();

                                            JOptionPane.showMessageDialog(holder, "Staff Details Updated SuccessFully");
                                            jfirstname.setText("");
                                            jmname.setText("");
                                            jlname.setText("");
                                            jphone.setText("");
                                            jemail.setText("");
                                            jprovince.setSelectedIndex(0);
                                            jcountry.setSelectedIndex(0);
                                            jcounty.setSelectedIndex(0);
                                            jward.setSelectedIndex(0);
                                            jconstituency.setSelectedIndex(0);
                                            jidnumber.setText("");
                                            jdateofemployment.setDate(null);
                                            jintials.setText("");

                                        } catch (HeadlessException | SQLException sq) {
                                            JOptionPane.showMessageDialog(holder, sq.getMessage());
                                        }


                                    }


                                }


                            }
                        }
                    }
                }


            } catch (HeadlessException sq) {
                JOptionPane.showMessageDialog(holder, sq.getMessage());
            }


        } else if (obj == edit) {
            if (tab.getSelectedRowCount() < 1) {
                JOptionPane.showMessageDialog(holder, "Kindly Select The Staff To Edit His/Her Details");
            } else {
                edit.setEnabled(false);
                update.setEnabled(true);

                view.setEnabled(false);
                back.setEnabled(true);
                int selectedrow = tab.getSelectedRow();
                empid = model.getValueAt(selectedrow, 4).toString();

                top.removeAll();
                top.add(firstname, "gapleft 30,width 50:150:250,height 30:30:30");
                top.add(jfirstname, "gapleft 50,width 50:250:250,height 30:30:30");
                top.add(mname, "gapleft 30,width 50:150:250,height 30:30:30");
                top.add(jmname, "gapleft 50,width 50:250:250,height 30:30:30,wrap");
                top.add(lname, "gapleft 30,gaptop 20,width 50:150:250,height 30:30:30");
                top.add(jlname, "gapleft 50,gaptop 20,width 50:250:250,height 30:30:30");
                top.add(gender, "gapleft 30,gaptop 20,width 50:150:250,height 30:30:30");
                top.add(jgender, "gapleft 50,gaptop 20,width 50:250:250,height 30:30:30,wrap");
                top.add(idnumber, "gapleft 30,gaptop 20,width 50:150:250,height 30:30:30");
                top.add(jidnumber, "gapleft 50,gaptop 20,width 50:250:250,height 30:30:30");
                top.add(phone, "gapleft 30,gaptop 20,width 50:150:250,height 30:30:30");
                top.add(jphone, "gapleft 50,gaptop 20,width 50:250:250,height 30:30:30,wrap");
                top.add(email, "gapleft 30,gaptop 20,width 50:150:250,height 30:30:30");
                top.add(jemail, "gapleft 50,gaptop 20,width 50:250:250,height 30:30:30");
                top.add(initial, "gapleft 30,gaptop 20,width 50:150:250,height 30:30:30");
                top.add(jintials, "gapleft 50,gaptop 20,width 50:250:250,height 30:30:30,wrap");
                top.add(country, "gapleft 30,gaptop 20,width 50:150:250,height 30:30:30");
                top.add(jcountry, "gapleft 50,gaptop 20,width 50:250:250,height 30:30:30");
                top.add(province, "gapleft 30,gaptop 20,width 50:150:250,height 30:30:30");
                top.add(jprovince, "gapleft 50,gaptop 20,width 50:250:250,height 30:30:30,wrap");
                top.add(county, "gapleft 30,gaptop 20,width 50:150:250,height 30:30:30");
                top.add(jcounty, "gapleft 50,gaptop 20,width 50:250:250,height 30:30:30");
                top.add(constituency, "gapleft 30,gaptop 20,width 50:150:250,height 30:30:30");
                top.add(jconstituency, "gapleft 50,gaptop 20,width 50:250:250,height 30:30:30,wrap");
                top.add(ward, "gapleft 30,gaptop 20,width 50:150:250,height 30:30:30");
                top.add(jward, "gapleft 50,gaptop 20,width 50:250:250,height 30:30:30");
                top.add(dateofemployment, "gapleft 30,gaptop 20,width 50:150:250,height 30:30:30");
                top.add(jdateofemployment, "gapleft 50,gaptop 20,width 50:250:250,height 30:30:30,wrap");
                top.add(status, "gapleft 30,gaptop 20,width 50:150:250,height 30:30:30");
                top.add(jstatus, "gapleft 50,gaptop 20,width 50:250:250,height 30:30:30");
                top.add(dept, "gapleft 30,gaptop 20,width 50:150:250,height 30:30:30");
                top.add(jdept, "gapleft 50,gaptop 20,width 50:250:250,height 30:30:30,wrap");
                top.add(update, "Gapleft 30,Gaptop 50");


                top.revalidate();
                top.repaint();

                String sql = "Select * from staffs where employeecode='" + empid + "'";
                try {
                    ps = con.prepareStatement(sql);
                    rs = ps.executeQuery();
                    if (rs.next()) {

                        jstatus.setSelectedItem(rs.getString("Status"));
                        jphone.setText(rs.getString("PhoneNumber"));
                        jemail.setText(rs.getString("Email"));
                        jfirstname.setText(rs.getString("FirstName"));
                        jmname.setText(rs.getString("MiddleName"));
                        jlname.setText(rs.getString("LastName"));
                        jidnumber.setText(rs.getString("IdNumber"));
                        if (rs.getString("Dateemployed").isEmpty()) {
                            jdateofemployment.setDate(null);
                        } else {
                            jdateofemployment.setDate(rs.getDate("Dateemployed"));
                        }

                        jintials.setText(rs.getString("Initials"));
                        jgender.setSelectedItem(rs.getString("Gender"));
                        String countryid = rs.getString("Countrycode");
                        String countyid = rs.getString("Countycode");
                        String provinceid = rs.getString("provincecode");
                        String constituencyid = rs.getString("Constituencycode");
                        String wardid = rs.getString("Wardcode");
                        String deptid = rs.getString("Departmentcode");


                        String sql1 = " Select countryName from countries where countrycode='" + countryid + "'";
                        ;
                        ps = con.prepareStatement(sql1);
                        rs = ps.executeQuery();
                        if (rs.next()) {
                            jcountry.setEditable(true);
                            jcountry.setSelectedItem(rs.getString("CountryName"));
                            jcountry.setEditable(false);
                        }
                        String sql2 = "Select Provincename from provinces where provincecode='" + provinceid + "'";
                        ps = con.prepareStatement(sql2);
                        rs = ps.executeQuery();
                        if (rs.next()) {
                            jprovince.setEditable(true);
                            jprovince.setSelectedItem(rs.getString("ProvinceName"));
                            jprovince.setEditable(false);
                        }
                        String sql7 = "Select Countyname from counties where countycode='" + countyid + "'";
                        ps = con.prepareStatement(sql7);
                        rs = ps.executeQuery();
                        if (rs.next()) {
                            jcounty.setEditable(true);
                            jcounty.setSelectedItem(rs.getString("Countyname"));
                            jcounty.setEditable(false);
                        }
                        String sql4 = "Select ConstituencyName from Constituencies where constituencycode='" + constituencyid + "'";
                        ps = con.prepareStatement(sql4);
                        rs = ps.executeQuery();
                        if (rs.next()) {
                            jconstituency.setEditable(true);
                            jconstituency.setSelectedItem(rs.getString("Constituencyname"));
                            jconstituency.setEditable(false);
                        }
                        String sql5 = "Select WardName from ward where wardcode='" + wardid + "'";
                        ps = con.prepareStatement(sql5);
                        rs = ps.executeQuery();
                        if (rs.next()) {
                            jward.setEditable(true);
                            jward.setSelectedItem(rs.getString("WardName"));
                            jward.setEditable(false);
                        }

                        String sql6 = "Select Name from departments where Departmentcode='" + deptid + "'";
                        ps = con.prepareStatement(sql6);
                        rs = ps.executeQuery();
                        if (rs.next()) {
                            jdept.setEditable(true);
                            jdept.setSelectedItem(rs.getString("Name"));
                            jdept.setEditable(false);
                        }


                    }

                } catch (SQLException sq) {
                    JOptionPane.showMessageDialog(holder, sq.getMessage());
                    sq.printStackTrace();
                }


            }

        }

    }


}
