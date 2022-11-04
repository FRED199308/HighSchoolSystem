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
import java.io.File;
import java.io.IOException;
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


/**
 * @author FRED
 */
public class StaffRegistration extends javax.swing.JFrame implements ActionListener {

    public static void main(String args[]) {
        new StaffRegistration();
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
    private FredCombo jDept;
    private javax.swing.JTextField jEmail;
    private javax.swing.JTextField jFirstName;
    private FredCombo jGender;
    private javax.swing.JTextField jIdNumber;
    private FredLabel jLabel10;
    private FredLabel jLabel11;
    private FredLabel jLabel12;
    private FredLabel jLabel13;
    private FredLabel jLabel3;
    private FredLabel jLabel4;
    private FredLabel jLabel5;
    private FredLabel jLabel6;
    private FredLabel jLabel8;
    private FredLabel jLabel9;
    private javax.swing.JTextField jLastName;
    private javax.swing.JTextField jMiddleName;
    private javax.swing.JTextField jPhoneNumber;
    private JTextField jDOB;
    private FredLabel jWard;
    private FredCombo jprovince;
    private FredLabel lastName;
    private FredLabel middleName;
    private javax.swing.JButton save;
    private FredDateChooser jDate;
    // End of variables declaration                   

    public StaffRegistration() {
        initComponents();

        setSize(1100, 680);
        setLocationRelativeTo(null);
        getContentPane().setBackground(Color.CYAN);
        setVisible(true);
        setTitle("Staff Registration Panel");
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
            String sql = "Select * from Departments";
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                jDept.addItem(rs.getString("Name"));
            }
            String sql3 = "Select * from countries";

            ps = con.prepareStatement(sql3);
            rs = ps.executeQuery();
            while (rs.next()) {
                jCountry.addItem(rs.getString("countryName"));

            }

        } catch (SQLException e) {
            e.printStackTrace();
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
        jLabel3 = new FredLabel();
        jLabel4 = new FredLabel();
        jLabel5 = new FredLabel();
        jLabel6 = new FredLabel();
        lastName = new FredLabel();
        jLabel8 = new FredLabel();
        jLabel9 = new FredLabel();
        jLabel10 = new FredLabel();
        jLabel11 = new FredLabel();
        jLabel12 = new FredLabel();
        jLabel13 = new FredLabel();
        age = new FredLabel();
        jFirstName = new javax.swing.JTextField();
        jMiddleName = new javax.swing.JTextField();
        jLastName = new javax.swing.JTextField();
        jDOB = new JTextField();

        jIdNumber = new javax.swing.JTextField();
        jGender = new FredCombo();
        jPhoneNumber = new javax.swing.JTextField();
        jEmail = new javax.swing.JTextField();
        save = new javax.swing.JButton();
        cancel = new javax.swing.JButton();
        jDept = new FredCombo();
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

        jLabel3.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        jLabel3.setText("Constituency");
        getContentPane().add(jLabel3);
        jLabel3.setBounds(634, 323, 130, 20);

        jLabel4.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        jLabel4.setText("County");
        getContentPane().add(jLabel4);
        jLabel4.setBounds(642, 264, 130, 14);

        jLabel5.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        jLabel5.setText("Province");
        getContentPane().add(jLabel5);
        jLabel5.setBounds(642, 180, 110, 14);

        jLabel6.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        jLabel6.setText("Country");
        getContentPane().add(jLabel6);
        jLabel6.setBounds(642, 113, 100, 14);

        lastName.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        lastName.setText("LastName");
        getContentPane().add(lastName);
        lastName.setBounds(52, 136, 130, 14);

        jLabel8.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        jLabel8.setText("Gender");
        getContentPane().add(jLabel8);
        jLabel8.setBounds(52, 258, 120, 14);

        jLabel9.setText("ID Number");
        getContentPane().add(jLabel9);
        jLabel9.setBounds(52, 336, 120, 14);

        jLabel10.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        jLabel10.setText("Phone Number");
        getContentPane().add(jLabel10);
        jLabel10.setBounds(52, 400, 110, 14);

        jLabel11.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        jLabel11.setText("Email");
        getContentPane().add(jLabel11);
        jLabel11.setBounds(52, 489, 110, 14);

        jLabel12.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        jLabel12.setText("Department");
        getContentPane().add(jLabel12);
        jLabel12.setBounds(642, 34, 90, 14);

        jLabel13.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        jLabel13.setText("Date Employed");
        getContentPane().add(jLabel13);
        jLabel13.setBounds(634, 492, 150, 14);

        age.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        age.setText("Initials");
        getContentPane().add(age);
        age.setBounds(52, 203, 100, 14);
        getContentPane().add(jFirstName);
        jFirstName.setBounds(241, 15, 260, 30);


        getContentPane().add(jMiddleName);
        jMiddleName.setBounds(241, 66, 260, 30);
        getContentPane().add(jLastName);
        jLastName.setBounds(241, 123, 260, 30);
        getContentPane().add(jDOB);
        jDOB.setBounds(241, 182, 260, 30);
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

        jDept.setModel(new javax.swing.DefaultComboBoxModel(new String[]{"Select Department"}));
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
            String DOB = jDOB.getText();
            try {

                if (jFirstName.getText().isEmpty()) {
                    getToolkit().beep();
                    JOptionPane.showMessageDialog(this, "Enter A Valid First Name");
                } else {
                    if (jMiddleName.getText().isEmpty()) {
                        getToolkit().beep();
                        JOptionPane.showMessageDialog(this, "Enter A Valid Middle  Name");
                    } else {

                        if (jGender.getSelectedIndex() == 0) {
                            getToolkit().beep();
                            JOptionPane.showMessageDialog(this, "Select Gender");
                        } else {

                            if (jPhoneNumber.getText().isEmpty()) {
                                getToolkit().beep();
                                JOptionPane.showMessageDialog(this, "Enter A Valid Phone Number");
                            } else {

                                if (jDept.getSelectedIndex() == 0) {
                                    JOptionPane.showMessageDialog(this, "Select Department");
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
                                        String qq = "Select Departmentcode from departments where name='" + jDept.getSelectedItem() + "'";
                                        ps = con.prepareStatement(qq);
                                        rs = ps.executeQuery();
                                        if (rs.next()) {
                                            deptcode = rs.getString("DepartmentCode");
                                        }

                                        String sqll = "Insert into staffs values(?,?,?,'" + DOB + "','" + jGender.getSelectedItem() + "','" + jIdNumber.getText() + "',"
                                                + "'" + jPhoneNumber.getText() + "','" + jEmail.getText() + "','" + deptcode + "','" + countrycode + "','" + provincecode + "','" + countycode + "','" + constituencycode + "','" + wardcode + "','" + DOA + "','" + "EM" + IdGenerator.keyGen() + "','" + "Active" + "')";
                                        ps = con.prepareStatement(sqll);
                                        ps.setString(1, jFirstName.getText().toUpperCase());
                                        ps.setString(2, jMiddleName.getText().toUpperCase());
                                        ps.setString(3, jLastName.getText().toUpperCase());
                                        ps.execute();
                                        JOptionPane.showMessageDialog(this, "Staff Registered SuccessFully");
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
                JOptionPane.showMessageDialog(this, ex.getMessage());
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
                JOptionPane.showMessageDialog(this, ex.getMessage());
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
                JOptionPane.showMessageDialog(this, ex.getMessage());
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
                JOptionPane.showMessageDialog(this, ex.getMessage());
            }

            jConstituency.addActionListener(this);


        }
    }

}
