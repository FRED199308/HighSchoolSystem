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
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import static javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE;


/**
 * @author FRED
 */
public class TeacherRegistration extends javax.swing.JFrame implements ActionListener {

    public static void main(String args[]) {
        new TeacherRegistration();
    }

    /**
     * Creates new form StaffRegistration
     */

    private PreparedStatement ps;
    private ResultSet rs;
    private Connection con;

    private FredLabel age;
    private javax.swing.JButton cancel;
    private FredLabel firstName;
    private FredCombo jComboBox1;
    private FredCombo jConstituency;
    private FredCombo jCountry;
    private FredCombo jCounty;
    private FredTextField jDept = new FredTextField();

    private FredTextField jEmail;
    private FredTextField jFirstName;
    private FredCombo jGender;
    private FredTextField jIdNumber;
    private FredLabel FredLabel10;
    private FredLabel FredLabel11;
    private FredLabel FredLabel12;
    private FredLabel FredLabel13;
    private FredLabel FredLabel3;
    private FredLabel FredLabel4;
    private FredLabel FredLabel5;
    private FredLabel FredLabel6;
    private FredLabel FredLabel8;
    private FredLabel FredLabel9;
    private FredTextField jLastName;
    private FredTextField jMiddleName;
    private FredTextField jPhoneNumber;
    private FredTextField jintials;
    private FredLabel jWard;
    private FredCombo jprovince;
    private FredLabel lastName;
    private FredLabel middleName;
    private javax.swing.JButton save;
    private FredDateChooser jDate;
    // End of variables declaration                   

    public TeacherRegistration() {
        initComponents();

        setSize(1100, 680);
        setLocationRelativeTo(null);
        getContentPane().setBackground(Color.CYAN);
        setVisible(true);
        setTitle("Teacher Registration Panel");
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        cancel.addActionListener(this);
        save.addActionListener(this);
        jComboBox1.addActionListener(this);
        jCounty.addActionListener(this);
        jCountry.addActionListener(this);
        jConstituency.addActionListener(this);
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
        this.setIconImage(FrameProperties.icon());
        ((JComponent) getContentPane()).setBorder(

                BorderFactory.createMatteBorder(5, 5, 5, 5, Color.MAGENTA));
        try {
            con = DbConnection.connectDb();

            String sql3 = "Select * from countries";

            ps = con.prepareStatement(sql3);
            rs = ps.executeQuery();
            while (rs.next()) {
                jCountry.addItem(rs.getString("countryName"));

            }

        } catch (SQLException sq) {
            sq.printStackTrace();
            JOptionPane.showMessageDialog(null, sq.getMessage());
        }
        jPhoneNumber.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();
                if (!Character.isDigit(c) || jPhoneNumber.getText().length() > 10) {
                    getToolkit().beep();
                    e.consume();
                }

            }

        });
        jIdNumber.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();

                if (!Character.isDigit(c) || jIdNumber.getText().length() > 8) {
                    getToolkit().beep();
                    e.consume();
                }

            }

        });
        jDept.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();

                if (!Character.isDigit(c) || jDept.getText().length() > 8) {
                    getToolkit().beep();
                    e.consume();
                }

            }

        });


        jFirstName.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();
                if (Character.isDigit(c) || jFirstName.getText().length() > 20 || c == KeyEvent.VK_BACK_SPACE) {
                    getToolkit().beep();
                    e.consume();
                }

            }

        });

        jMiddleName.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();
                if (Character.isDigit(c) || jMiddleName.getText().length() > 20) {
                    getToolkit().beep();
                    e.consume();
                }
                if (c == KeyEvent.VK_BACK_SPACE) {
                    e.consume();
                }
            }

        });

        jLastName.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();
                if (Character.isDigit(c) || jLastName.getText().length() > 20) {
                    getToolkit().beep();
                    e.consume();
                }
                if (c == KeyEvent.VK_BACK_SPACE) {
                    e.consume();
                }
            }

        });


        jDate.setMaxSelectableDate(new Date());


    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">
    @SuppressWarnings(value = "unchecked")
    private void initComponents() {
        firstName = new FredLabel();
        middleName = new FredLabel();
        FredLabel3 = new FredLabel();
        FredLabel4 = new FredLabel();
        FredLabel5 = new FredLabel();
        FredLabel6 = new FredLabel();
        lastName = new FredLabel();
        FredLabel8 = new FredLabel();
        FredLabel9 = new FredLabel();
        FredLabel10 = new FredLabel();
        FredLabel11 = new FredLabel();
        FredLabel12 = new FredLabel();
        FredLabel13 = new FredLabel();
        age = new FredLabel();
        jFirstName = new FredTextField();
        jMiddleName = new FredTextField();
        jLastName = new FredTextField();
        jintials = new FredTextField();

        jIdNumber = new FredTextField();
        jGender = new FredCombo();
        jPhoneNumber = new FredTextField();
        jEmail = new FredTextField();
        save = new javax.swing.JButton();
        cancel = new javax.swing.JButton();

        jCountry = new FredCombo();
        jprovince = new FredCombo();
        jCounty = new FredCombo();
        jWard = new FredLabel();
        jComboBox1 = new FredCombo();
        jConstituency = new FredCombo();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(null);

        firstName.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        firstName.setText("First Name");
        getContentPane().add(firstName);
        firstName.setBounds(52, 18, 120, 14);

        middleName.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        middleName.setText("MiddleName");
        getContentPane().add(middleName);
        middleName.setBounds(52, 79, 120, 14);

        FredLabel3.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        FredLabel3.setText("Constituency");
        getContentPane().add(FredLabel3);
        FredLabel3.setBounds(634, 323, 130, 20);

        FredLabel4.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        FredLabel4.setText("County");
        getContentPane().add(FredLabel4);
        FredLabel4.setBounds(642, 264, 130, 14);

        FredLabel5.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        FredLabel5.setText("Province");
        getContentPane().add(FredLabel5);
        FredLabel5.setBounds(642, 180, 110, 14);

        FredLabel6.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        FredLabel6.setText("Country");
        getContentPane().add(FredLabel6);
        FredLabel6.setBounds(642, 113, 100, 14);

        lastName.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        lastName.setText("LastName");
        getContentPane().add(lastName);
        lastName.setBounds(52, 136, 130, 14);

        FredLabel8.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        FredLabel8.setText("Gender");
        getContentPane().add(FredLabel8);
        FredLabel8.setBounds(52, 258, 120, 14);

        FredLabel9.setText("ID Number");
        getContentPane().add(FredLabel9);
        FredLabel9.setBounds(52, 336, 120, 14);

        FredLabel10.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        FredLabel10.setText("Phone Number");
        getContentPane().add(FredLabel10);
        FredLabel10.setBounds(52, 400, 110, 14);

        FredLabel11.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        FredLabel11.setText("Email");
        getContentPane().add(FredLabel11);
        FredLabel11.setBounds(52, 489, 110, 14);

        FredLabel12.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        FredLabel12.setText("Teacher No.");
        getContentPane().add(FredLabel12);
        FredLabel12.setBounds(642, 34, 90, 14);

        FredLabel13.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        FredLabel13.setText("Date Employed");
        getContentPane().add(FredLabel13);
        FredLabel13.setBounds(634, 492, 150, 14);

        age.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        age.setText("Teacher Initials");
        getContentPane().add(age);
        age.setBounds(52, 203, 100, 14);
        getContentPane().add(jFirstName);
        jFirstName.setBounds(241, 15, 260, 30);


        getContentPane().add(jMiddleName);
        jMiddleName.setBounds(241, 66, 260, 30);
        getContentPane().add(jLastName);
        jLastName.setBounds(241, 123, 260, 30);
        getContentPane().add(jintials);
        jintials.setBounds(241, 182, 260, 30);
        getContentPane().add(jIdNumber);
        jIdNumber.setBounds(241, 323, 260, 30);

        jGender.setModel(new javax.swing.DefaultComboBoxModel(new String[]{"Select Gender", "Male", "Female"}));

        getContentPane().add(jGender);
        jGender.setBounds(241, 248, 260, 30);
        getContentPane().add(jPhoneNumber);
        jPhoneNumber.setBounds(241, 388, 260, 30);
        getContentPane().add(jEmail);
        jEmail.setBounds(241, 476, 260, 30);

        save.setText("Save");
        getContentPane().add(save);
        save.setBounds(270, 572, 130, 30);

        cancel.setText("Close");
        cancel.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/Button-Close-icon.png"))); // NOI18N
        getContentPane().add(cancel);
        cancel.setBounds(660, 562, 108, 30);


        getContentPane().add(jDept);
        jDept.setBounds(790, 30, 240, 30);

        jCountry.setModel(new javax.swing.DefaultComboBoxModel(new String[]{"Select Country"}));
        getContentPane().add(jCountry);
        jCountry.setBounds(793, 110, 230, 30);

        jprovince.setModel(new javax.swing.DefaultComboBoxModel(new String[]{"Select Province"}));
        getContentPane().add(jprovince);
        jprovince.setBounds(793, 167, 230, 30);

        jCounty.setModel(new javax.swing.DefaultComboBoxModel(new String[]{"Select County"}));
        getContentPane().add(jCounty);
        jCounty.setBounds(793, 248, 230, 30);

        jWard.setText("Ward");
        getContentPane().add(jWard);
        jWard.setBounds(634, 387, 169, 32);

        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel(new String[]{"Select Ward"}));
        getContentPane().add(jComboBox1);
        jComboBox1.setBounds(790, 387, 233, 32);

        jConstituency.setModel(new javax.swing.DefaultComboBoxModel(new String[]{"Select Constituency"}));
        getContentPane().add(jConstituency);
        jConstituency.setBounds(790, 324, 233, 30);
        jDate = new FredDateChooser();
        jDate.setBounds(790, 492, 233, 30);
        jDate.setDateFormatString("yyyy/MM/dd");

        getContentPane().add(jDate);
        pack();
    } // </editor-fold>                        

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
            String DOA = ((JTextField) jDate.getDateEditor().getUiComponent()).getText();
            String inti = jintials.getText();
            try {

                if (jFirstName.getText().isEmpty() || DataValidation.name(jFirstName.getText()) == false) {
                    getToolkit().beep();
                    JOptionPane.showMessageDialog(this, "Enter A Valid First Name");
                } else {
                    if (jMiddleName.getText().isEmpty() || DataValidation.name(jMiddleName.getText()) == false) {
                        getToolkit().beep();
                        JOptionPane.showMessageDialog(this, "Enter A Valid Middle  Name");
                    } else {
                        if (inti.isEmpty()) {
                            getToolkit().beep();
                            JOptionPane.showMessageDialog(this, "Enter Valid Teachers's Intials");
                        } else {
                            if (jGender.getSelectedIndex() == 0) {
                                getToolkit().beep();
                                JOptionPane.showMessageDialog(this, "Select Gender");
                            } else {

                                if (jPhoneNumber.getText().isEmpty()) {
                                    getToolkit().beep();
                                    JOptionPane.showMessageDialog(this, "Enter A Valid Phone Number");
                                } else {

                                    if (jDept.getText().equalsIgnoreCase("")) {
                                        JOptionPane.showMessageDialog(this, "Input A valid Teacher Number");
                                    } else {


                                        try {
                                            String wardcode = "", constituencycode = "", countycode = "", provincecode = "", countrycode = "";
                                            String sql = "Select countrycode from countries where countryname='" + jCountry.getSelectedItem() + "'";
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

                                            String sql3 = "Select countycode from counties where countyname='" + jCounty.getSelectedItem() + "'";
                                            ps = con.prepareStatement(sql3);
                                            rs = ps.executeQuery();
                                            if (rs.next()) {
                                                countycode = rs.getString("Countycode");
                                            }
                                            String sql4 = "Select constituencycode from constituencies where constituencyname='" + jConstituency.getSelectedItem() + "'";
                                            ps = con.prepareStatement(sql4);
                                            rs = ps.executeQuery();
                                            if (rs.next()) {
                                                constituencycode = rs.getString("constituencycode");
                                            }
                                            String sql5 = "Select wardcode from ward where wardname='" + jComboBox1.getSelectedItem() + "'";
                                            ps = con.prepareStatement(sql5);
                                            rs = ps.executeQuery();
                                            if (rs.next()) {
                                                wardcode = rs.getString("wardcode");
                                            }
                                            String deptcode = "";
                                            String qq = "Select Departmentcode from departments where name='" + "Academics" + "'";
                                            ps = con.prepareStatement(qq);
                                            rs = ps.executeQuery();
                                            if (rs.next()) {
                                                deptcode = rs.getString("DepartmentCode");
                                            }

                                            String sqll = "Insert into staffs values('" + jFirstName.getText().toUpperCase() + "','" + jMiddleName.getText().toUpperCase() + "','" + jLastName.getText().toUpperCase() + "','" + inti + "','" + jGender.getSelectedItem() + "','" + jIdNumber.getText() + "',"
                                                    + "'" + jPhoneNumber.getText() + "','" + jEmail.getText() + "','" + deptcode + "','" + countrycode + "','" + provincecode + "','" + countycode + "','" + constituencycode + "','" + wardcode + "','" + DOA + "','" + jDept.getText() + "','" + "Active" + "')";
                                            ps = con.prepareStatement(sqll);
                                            ps.execute();
                                            JOptionPane.showMessageDialog(this, "Teacher Registered SuccessFully");
                                            jFirstName.setText("");
                                            jMiddleName.setText("");
                                            jLastName.setText("");
                                            jPhoneNumber.setText("");
                                            jEmail.setText("");
                                            jprovince.setSelectedIndex(0);
                                            jCountry.setSelectedIndex(0);
                                            jCounty.setSelectedIndex(0);
                                            jComboBox1.setSelectedIndex(0);
                                            jConstituency.setSelectedIndex(0);
                                            jIdNumber.setText("");

                                        } catch (HeadlessException | SQLException sq) {
                                            JOptionPane.showMessageDialog(this, sq.getMessage());
                                        }


                                    }


                                }

                            }
                        }
                    }
                }


            } catch (HeadlessException sq) {
                JOptionPane.showMessageDialog(this, sq.getMessage());
            }

        } else if (obj == jCountry) {
            try {
                jprovince.removeActionListener(this);
                jprovince.removeAllItems();
                jprovince.addItem("Select Province");
                String sql = "Select Countrycode from countries where countryname='" + jCountry.getSelectedItem() + "'";
                ps = con.prepareStatement(sql);
                rs = ps.executeQuery();
                if (rs.next()) {
                    String sql1 = "Select * from provinces where countrycode='" + rs.getString("Countrycode") + "' order by provincename asc";
                    ps = con.prepareStatement(sql1);
                    rs = ps.executeQuery();
                    while (rs.next()) {
                        jprovince.addItem(rs.getString("provincename"));
                    }
                }
            } catch (SQLException ex) {
                Logger.getLogger(studentReg.class.getName()).log(Level.SEVERE, null, ex);
            }
            jprovince.addActionListener(this);


        } else if (obj == jprovince) {
            jCounty.removeAllItems();
            jCounty.addItem("Select County");
            try {

                String sql = "Select provincecode from provinces where provincename='" + jprovince.getSelectedItem() + "'";
                ps = con.prepareStatement(sql);
                rs = ps.executeQuery();
                if (rs.next()) {
                    String sql1 = "Select countyname  from counties where provincecode='" + rs.getString("provincecode") + "' order by countyName asc";
                    ps = con.prepareStatement(sql1);
                    rs = ps.executeQuery();
                    while (rs.next()) {
                        jCounty.addItem(rs.getString("countyname"));
                    }
                }


            } catch (SQLException ex) {
                Logger.getLogger(studentReg.class.getName()).log(Level.SEVERE, null, ex);
            }

        } else if (obj == jConstituency) {

            jComboBox1.removeAllItems();
            jComboBox1.addItem("Select Ward");
            try {

                String sql = "Select constituencycode from constituencies where constituencyname='" + jConstituency.getSelectedItem() + "'";
                ps = con.prepareStatement(sql);
                rs = ps.executeQuery();
                if (rs.next()) {
                    String sql1 = "Select wardname  from ward where constituencycode='" + rs.getString("constituencycode") + "' order by wardname asc";
                    ps = con.prepareStatement(sql1);
                    rs = ps.executeQuery();
                    while (rs.next()) {
                        jComboBox1.addItem(rs.getString("wardname"));
                    }
                }

            } catch (SQLException ex) {
                Logger.getLogger(studentReg.class.getName()).log(Level.SEVERE, null, ex);
            }


        } else if (obj == jCounty) {
            jConstituency.removeAllItems();
            jConstituency.addItem("Select Constituency");
            jConstituency.removeActionListener(this);
            try {

                String sql = "Select countycode from counties where countyname='" + jCounty.getSelectedItem() + "'";
                ps = con.prepareStatement(sql);
                rs = ps.executeQuery();
                if (rs.next()) {
                    String sql1 = "Select constituencyname  from constituencies where countycode='" + rs.getString("countycode") + "' order by constituencyname asc";
                    ps = con.prepareStatement(sql1);
                    rs = ps.executeQuery();
                    while (rs.next()) {
                        jConstituency.addItem(rs.getString("constituencyname"));
                    }
                }

            } catch (SQLException ex) {
                Logger.getLogger(studentReg.class.getName()).log(Level.SEVERE, null, ex);
            }

            jConstituency.addActionListener(this);


        }
    }

}
