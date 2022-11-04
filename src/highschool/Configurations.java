/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package highschool;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JFrame;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;

import javax.swing.JTextPane;
import javax.swing.UIManager;

import static javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE;

import javax.swing.border.TitledBorder;

import net.miginfocom.swing.MigLayout;

/**
 * @author FRED
 */
public class Configurations extends JFrame implements ActionListener {

    public static void main(String[] args) {
        new Configurations();
    }

    private int height = 600;
    private int width = 930;
    private JPanel panel1;
    private JPanel pane;
    private JPanel panel2;
    private JPanel panel3;
    private JButton cancel, save;
    private JCheckBox nimbus, windows, windowsclassic, mac, metal, cde, mint, mcwin, alluminium, lastname, residentialDetails, image, dateofbirth, parentDetails, phone, votehead;
    private JCheckBox passchange, passscontraint, userchange, admins, docopener, onlinesender, offlinesender, autoclear, migrateReadiness, feeBal;
    private FredTextField rimindertime, jUsername, jimagelocation;
    private FredLabel time, smsKey, smsUsername, advice, imageloctaion;
    private ButtonGroup looks, smsoptions;
    private String selectedFeel = "nimbus";
    private JPanel panel4;
    private JPasswordField jSmsKey;
    private String auto = "False";

    private String passStatus = "false", readiness = "False", OFFLINE = "false", voteadDistribution, usernamePolicy = "false", adminStatus = "false", passCon = "false", IMAGE = "false", LAST = "false", DATEOFBIRTH = "false", PHONE = "false", RESIDETIALDETAILS = "false", PARENTDETAILS = "false", DOC = "false", bal = "false";

    @SuppressWarnings("LeakingThisInConstructor")
    public Configurations() {
        setSize(width, height);
        setTitle("                         SYSTEM CONFIGURATIONS");
        // getContentPane().setBackground(Color.cyan);
        setLocationRelativeTo(null);
        setLayout(new MigLayout());
        lastname = new JCheckBox("LastName");
        residentialDetails = new JCheckBox("Residential Details");
        parentDetails = new JCheckBox("Parent Names");
        dateofbirth = new JCheckBox("Date Of Birth");
        phone = new JCheckBox("Phone Number");
        image = new JCheckBox("Image");
        migrateReadiness = new FredCheckBox("Class/Term Migration Readiness");
        passchange = new JCheckBox("Force Users To Change Password Default Password");
        passscontraint = new JCheckBox("Enforce Password Strength");
        docopener = new JCheckBox("Open Reports On System Software");
        userchange = new JCheckBox("Allow Users To Change Their UserNames");
        autoclear = new JCheckBox("Enable Automatic Clearance");
        admins = new JCheckBox("Allow Multiple Admin Accounts");
        time = new FredLabel("School K.C.S.E Prefix Code");
        rimindertime = new FredTextField();
        onlinesender = new JCheckBox("Use Online Gateway To Send SMS(Internet)");
        offlinesender = new JCheckBox("Use Local Resource To Send SMS(Simcard)");
        feeBal = new JCheckBox("Capture Fee Bal. Automatically(Requires periodic fee Update)");
        votehead = new JCheckBox("Automatic Votehead Payment Distribution");
        panel1 = new JPanel();
        panel1.setBackground(Color.WHITE);
        pane = new JPanel();
        advice = new FredLabel("Kindly Note That The System Uses AfricanTalking Gate ways To send SMS");
        smsoptions = new ButtonGroup();
        smsKey = new FredLabel("SMS GateWay Key");
        smsUsername = new FredLabel("Username");
        smsoptions.add(onlinesender);
        smsoptions.add(offlinesender);
        jSmsKey = new JPasswordField();
        String l = "@";
        jimagelocation = new FredTextField();
        imageloctaion = new FredLabel("Student Image Location Folder");
        jSmsKey.setEchoChar(l.charAt(0));
        jUsername = new FredTextField();
        panel2 = new JPanel();
        panel3 = new JPanel();
        panel4 = new JPanel();
        pane.setLayout(new MigLayout());
        panel4.setLayout(new MigLayout());
        panel3.setLayout(new MigLayout());
        panel3.add(advice, "growx,pushx,gaptop 10,wrap");
        panel3.add(smsUsername, "split,gaptop 20");
        panel3.add(jUsername, "pushx,growx,gaptop 20,wrap");
        panel3.add(smsKey, "split,gaptop 20");
        panel3.add(jSmsKey, "pushx,growx,gaptop 20,wrap");
        panel3.add(panel4, "push,grow");
        panel4.setBorder(new TitledBorder("Optional Student Registration Fields Requirements Settings(Mark To Enforce Input)"));
        panel4.setBackground(Color.PINK);
        panel4.add(lastname, "pushx,growx,gapleft 50");
        panel4.add(parentDetails, "pushx,growx,gapleft 50,wrap");
        panel4.add(dateofbirth, "pushx,growx,gapleft 50");
        panel4.add(phone, "pushx,growx,gapleft 50,wrap");
        panel4.add(residentialDetails, "pushx,growx,gapleft 50");
        panel4.add(image, "pushx,growx,gapleft 50");
        panel2.setBackground(Color.WHITE);
        panel3.setBackground(Color.WHITE);
        panel2.setLayout(new MigLayout());
        panel2.add(passchange, "grow,push,gapleft 50,wrap");
        panel2.add(passscontraint, "grow,push,gapleft 50,wrap");
        panel2.add(offlinesender, "grow,push,gapleft 50,wrap");
        panel2.add(onlinesender, "grow,push,gapleft 50,wrap");
        panel2.add(userchange, "grow,push,gapleft 50,wrap");

        panel2.add(imageloctaion, "gapleft 50,split");
        panel2.add(jimagelocation, "wrap,growx,pushx");
        panel2.add(time, "gapleft 20,split");

        panel2.add(rimindertime, "gapleft 20,growx,pushx");
        cancel = new JButton("CLOSE");
        save = new JButton("APPLY");
        nimbus = new JCheckBox("NIMBUS");
        mac = new JCheckBox("HIFI");
        windows = new JCheckBox("WINDOWS");
        windowsclassic = new JCheckBox("WINDOWS CLASSIC");
        metal = new JCheckBox("Metal");
        cde = new JCheckBox("CDE");
        pane.setBorder(new TitledBorder("System Aspects"));
        looks = new ButtonGroup();
        looks.add(mac);
        looks.add(windowsclassic);
        looks.add(metal);
        looks.add(cde);
        looks.add(windows);
        looks.add(nimbus);
        panel1.setBorder(new TitledBorder("Select The Look An Feel For The System Theme"));
        panel1.setLayout(new MigLayout());
        panel1.add(windows, "Gapleft 30,push,grow");
        panel1.add(cde, "Gapleft 30,push,grow,wrap");
        panel1.add(windowsclassic, "Gapleft 30,push,grow");
        panel1.add(mac, "Gapleft 30,push,grow,wrap");
        panel1.add(metal, "Gapleft 30,push,grow");
        panel1.add(nimbus, "Gapleft 30,push,grow");
        pane.add(autoclear, "growx,pushx,gapleft 50");
        pane.add(admins, "grow,push,gapleft 50,wrap");
        pane.add(docopener, "grow,push,gapleft 50");


        if (Globals.depName.equalsIgnoreCase("Finance")) {
            pane.add(migrateReadiness, "grow,push,gapleft 50,wrap");
            pane.add(feeBal, "grow,push,gapleft 50");
            pane.add(votehead, "grow,push,gapleft 50,wrap");
        }

        add(panel1, "grow,push");
        add(pane, "Grow,push,wrap");
        add(panel2, "grow,push");
        add(panel3, "grow,push,wrap");
        add(save, "gapleft 50,growx,pushx,split");
        add(cancel, "gapleft 30,growx,pushx");
        this.addMouseMotionListener(new MouseAdapter() {

            @Override
            public void mouseMoved(MouseEvent e) {

                Cursor cur = new Cursor(Cursor.HAND_CURSOR);
                save.setCursor(cur);
                cde.setCursor(cur);
                metal.setCursor(cur);
                ;
                nimbus.setCursor(cur);
                windows.setCursor(cur);
                windowsclassic.setCursor(cur);
                cancel.setCursor(cur);
                phone.setCursor(cur);
                passchange.setCursor(cur);
                passscontraint.setCursor(cur);
                admins.setCursor(cur);
                userchange.setCursor(cur);
                mac.setCursor(cur);
                residentialDetails.setCursor(cur);
                lastname.setCursor(cur);
                parentDetails.setCursor(cur);
                dateofbirth.setCursor(cur);
                image.setCursor(cur);
                docopener.setCursor(cur);
            }
        });
        //jSmsKey.set
        panel2.setBorder(new TitledBorder("System Aspects"));
        panel3.setBorder(new TitledBorder("more Configuration"));
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        this.addWindowListener(new WindowAdapter() {

            @Override
            public void windowClosing(WindowEvent e) {
                CurrentFrame.currentWindow();
                dispose();
            }
        });
        this.setIconImage(FrameProperties.icon());
        ((JComponent) getContentPane()).setBorder(
                BorderFactory.createMatteBorder(5, 5, 5, 5, Color.MAGENTA)
        );
        if (ConfigurationIntialiser.nimbus() == true) {
            nimbus.setSelected(true);
        }
        if (ConfigurationIntialiser.cde() == true) {
            cde.setSelected(true);
        }
        if (ConfigurationIntialiser.mac() == true) {
            mac.setSelected(true);
        }
        if (ConfigurationIntialiser.metal() == true) {
            metal.setSelected(true);
        }
        if (ConfigurationIntialiser.multipleAdmins() == true) {
            admins.setSelected(true);
        }
        if (ConfigurationIntialiser.windows() == true) {
            windows.setSelected(true);
        }
        if (ConfigurationIntialiser.windowsClassic() == true) {
            windowsclassic.setSelected(true);
        }
        if (ConfigurationIntialiser.passwordChange() == true) {
            passchange.setSelected(true);
        }
        if (ConfigurationIntialiser.usernameChange() == true) {
            userchange.setSelected(true);
        }
        if (ConfigurationIntialiser.passwordConstraint() == true) {
            passscontraint.setSelected(true);
        }
        if (ConfigurationIntialiser.residentialDetails() == true) {
            residentialDetails.setSelected(true);
        }
        if (ConfigurationIntialiser.parentDetails() == true) {
            parentDetails.setSelected(true);
        }
        if (ConfigurationIntialiser.phone() == true) {
            phone.setSelected(true);
        }
        if (ConfigurationIntialiser.dateOfBirth() == true) {
            dateofbirth.setSelected(true);
        }
        if (ConfigurationIntialiser.lastName() == true) {
            lastname.setSelected(true);
        }
        if (ConfigurationIntialiser.image() == true) {
            image.setSelected(true);
        }
        if (ConfigurationIntialiser.docOpener() == true) {
            docopener.setSelected(true);
        }
        if (ConfigurationIntialiser.smsOfflineSender() == true) {
            offlinesender.setSelected(true);
        } else {
            onlinesender.setSelected(true);
        }
        if (ConfigurationIntialiser.AutoClearance()) {
            autoclear.setSelected(true);
        } else {
            autoclear.setSelected(false);
        }
        if (ConfigurationIntialiser.migrationReadiness()) {
            migrateReadiness.setSelected(true);
        } else {
            migrateReadiness.setSelected(false);
        }

        if (ConfigurationIntialiser.automaticFeeCupture()) {
            feeBal.setSelected(true);
        } else {
            feeBal.setSelected(false);
        }
        if (ConfigurationIntialiser.automaticVoteheadAmountDistribution()) {
            votehead.setSelected(true);
        } else {
            votehead.setSelected(false);
        }
        jUsername.setText(ConfigurationIntialiser.smsUsername1());
        jSmsKey.setText(ConfigurationIntialiser.smsKey());
        jimagelocation.setText(ConfigurationIntialiser.imageFolder());

        rimindertime.setText(ConfigurationIntialiser.reminderTime());
        nimbus.addActionListener(this);
        mac.addActionListener(this);
        windows.addActionListener(this);
        windowsclassic.addActionListener(this);
        metal.addActionListener(this);
        cde.addActionListener(this);
        cancel.addActionListener(this);
        save.addActionListener(this);
        passchange.addActionListener(this);
        passscontraint.addActionListener(this);
        userchange.addActionListener(this);
        admins.addActionListener(this);


        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object obj = e.getSource();
        if (obj == cancel) {
            CurrentFrame.currentWindow();
            dispose();
        } else if (obj == save) {
            if (mac.isSelected()) {
                selectedFeel = "com.jtattoo.plaf.hifi.HiFiLookAndFeel";

            } else if (windows.isSelected()) {
                selectedFeel = "com.sun.java.swing.plaf.windows.WindowsLookAndFeel";
            } else if (windowsclassic.isSelected()) {
                selectedFeel = "com.sun.java.swing.plaf.windows.WindowsClassicLookAndFeel";
            } else if (metal.isSelected()) {
                selectedFeel = "com.jtattoo.plaf.mcwin.McWinLookAndFeel";
            } else if (cde.isSelected()) {
                selectedFeel = "com.sun.java.swing.plaf.motif.MotifLookAndFeel";
            } else if (nimbus.isSelected()) {
                selectedFeel = "com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel";
            }
            if (passchange.isSelected()) {
                passStatus = "true";
            } else {
                passStatus = "false";
            }

            if (passscontraint.isSelected()) {
                passCon = "true";
            } else {
                passCon = "false";
            }
            if (userchange.isSelected()) {
                usernamePolicy = "true";
            } else {
                usernamePolicy = "false";
            }
            if (admins.isSelected()) {
                adminStatus = "true";
            } else {
                adminStatus = "false";
            }
            if (lastname.isSelected()) {
                LAST = "true";
            }
            if (phone.isSelected()) {
                PHONE = "true";
            }
            if (parentDetails.isSelected()) {
                PARENTDETAILS = "true";
            }
            if (residentialDetails.isSelected()) {
                RESIDETIALDETAILS = "true";
            }
            if (dateofbirth.isSelected()) {
                DATEOFBIRTH = "true";
            }
            if (image.isSelected()) {
                IMAGE = "true";
            }
            if (docopener.isSelected()) {
                DOC = "true";
            }
            if (offlinesender.isSelected()) {
                OFFLINE = "true";
            } else {
                OFFLINE = "false";
            }
            if (autoclear.isSelected()) {
                auto = "True";
            } else {
                auto = "False";
            }
            if (migrateReadiness.isSelected()) {
                readiness = "True";
            } else {
                readiness = "False";
            }
            if (feeBal.isSelected()) {
                bal = "True";
            } else {
                bal = "False";
            }

            if (votehead.isSelected()) {
                voteadDistribution = "True";
            } else {
                voteadDistribution = "False";
            }
            boolean comply = true;

            if (rimindertime.getText().isEmpty() && comply == true) {
                getToolkit().beep();
                comply = false;
                JOptionPane.showMessageDialog(this, "Kindly Input sytem Reminder time Intervals as a number");
            }
            if (comply == true) {

                PreparedStatement ps = null;
                ResultSet rs = null;
                Connection con = null;
                String configid = null;
                try {
                    String querry = "Select configurationid,status from systemconfiguration";
                    con = DbConnection.connectDb();
                    ps = con.prepareStatement(querry);
                    rs = ps.executeQuery();
                    int counter = 1;
                    while (rs.next()) {
                        if (counter > 6) {
                            break;
                        }
                        configid = rs.getString("ConfigurationId");
                        if (rs.getString("Status").equalsIgnoreCase("true")) {
                            String querry3 = "Update Systemconfiguration set status='" + "false" + "' where configurationid='" + configid + "'";
                            PreparedStatement ps1 = con.prepareStatement(querry3);
                            ps1.execute();


                        }

                        counter++;

                    }


                    String querry4 = "update systemconfiguration  set status='" + "true" + "' where configurationname='" + selectedFeel + "'";
                    ps = con.prepareStatement(querry4);
                    ps.execute();
                    String querry5 = "update systemconfiguration set status='" + auto + "' where configurationid='" + "CO012" + "'";
                    ps = con.prepareStatement(querry5);
                    ps.execute();
                    String querry6 = " update  systemconfiguration set status='" + rimindertime.getText() + "' where configurationid='" + "CO011" + "'";
                    ps = con.prepareStatement(querry6);
                    ps.execute();
                    String querry7 = "update systemconfiguration set status='" + passStatus + "' where configurationid='" + "CO007" + "'";
                    ps = con.prepareStatement(querry7);
                    ps.execute();
                    String querry8 = "update systemconfiguration set status='" + passCon + "' where configurationid='" + "CO008" + "'";
                    ps = con.prepareStatement(querry8);
                    ps.execute();
                    String querry9 = "update systemconfiguration set status='" + usernamePolicy + "' where configurationid='" + "CO009" + "'";
                    ps = con.prepareStatement(querry9);
                    ps.execute();
                    String querry10 = "update systemconfiguration set status='" + adminStatus + "' where configurationid='" + "CO010" + "'";
                    ps = con.prepareStatement(querry10);
                    ps.execute();
                    String querry11 = "update systemconfiguration set status='" + jSmsKey.getText() + "' where configurationid='" + "CO014" + "'";
                    ps = con.prepareStatement(querry11);
                    ps.execute();
                    String querry12 = "update systemconfiguration set status='" + jUsername.getText() + "' where configurationid='" + "CO013" + "'";
                    ps = con.prepareStatement(querry12);
                    ps.execute();
                    String querry13 = "update systemconfiguration set status='" + LAST + "' where configurationid='" + "CO015" + "'";
                    ps = con.prepareStatement(querry13);
                    ps.execute();
                    String querry14 = "update systemconfiguration set status='" + PARENTDETAILS + "' where configurationid='" + "CO016" + "'";
                    ps = con.prepareStatement(querry14);
                    ps.execute();
                    String querry15 = "update systemconfiguration set status='" + RESIDETIALDETAILS + "' where configurationid='" + "CO017" + "'";
                    ps = con.prepareStatement(querry15);
                    ps.execute();
                    String querry16 = "update systemconfiguration set status='" + DATEOFBIRTH + "' where configurationid='" + "CO018" + "'";
                    ps = con.prepareStatement(querry16);
                    ps.execute();
                    String querry17 = "update systemconfiguration set status='" + PHONE + "' where configurationid='" + "CO019" + "'";
                    ps = con.prepareStatement(querry17);
                    ps.execute();
                    String querry18 = "update systemconfiguration set status='" + IMAGE + "' where configurationid='" + "CO020" + "'";
                    ps = con.prepareStatement(querry18);
                    ps.execute();
                    String querry19 = "update systemconfiguration set status='" + DOC + "' where configurationid='" + "CO021" + "'";
                    ps = con.prepareStatement(querry19);
                    ps.execute();
                    String querry20 = "update systemconfiguration set status='" + OFFLINE + "' where configurationid='" + "CO022" + "'";
                    ps = con.prepareStatement(querry20);
                    ps.execute();
                    String querry21 = "update systemconfiguration set status=? where configurationid='" + "CO023" + "'";
                    ps = con.prepareStatement(querry21);
                    ps.setString(1, jimagelocation.getText());
                    ps.execute();
                    String querry22 = "update systemconfiguration set status='" + readiness + "' where configurationid='" + "CO024" + "'";
                    ps = con.prepareStatement(querry22);
                    ps.execute();
                    String querry26 = "update systemconfiguration set status='" + bal + "' where configurationid='" + "CO026" + "'";
                    ps = con.prepareStatement(querry26);
                    ps.execute();
                    String querry27 = "update systemconfiguration set status='" + voteadDistribution + "' where configurationid='" + "CO027" + "'";
                    ps = con.prepareStatement(querry27);
                    ps.execute();
                    if (selectedFeel.equalsIgnoreCase(Globals.feel)) {
                        JOptionPane.showMessageDialog(this, "Configurations Updated Successfully\n New System Configuration Effected", "Details Saved", JOptionPane.INFORMATION_MESSAGE);
                    } else {

                        Globals.feel = selectedFeel;
                        JOptionPane.showMessageDialog(this, "Configurations Updated Successfully\n System Theme Change Has Been Detected\n The System Must Restart To Apply The New Theme...!", "Details Saved", JOptionPane.INFORMATION_MESSAGE);
                        CurrentFrame.ReportDialogue.dispose();
                        CurrentFrame.secondFrame.dispose();
                        CurrentFrame.mainFrame().dispose();
                        try {
                            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {

                                UIManager.setLookAndFeel(Globals.feel);

                            }
                        } catch (Exception sq) {

                        }

                        if (Globals.depName.equalsIgnoreCase("Finance")) {
                            new FinanceHome();
                        } else {


                            if (Globals.moduleName.equalsIgnoreCase("Exam")) {
                                ExamHome.timeOutChecker.stop();
                                new ExamHome();

                            } else {
                                AcademicsHome.timeOutChecker.stop();
                                new AcademicsHome();
                            }


                        }


                    }


                } catch (HeadlessException | SQLException sq) {
                    JOptionPane.showMessageDialog(this, sq, "Error Occurred", JOptionPane.ERROR_MESSAGE);
                }
            }


        } else if (obj == passchange) {
            if (passchange.isSelected()) {
                passStatus = "true";
            } else {
                passStatus = "false";
            }

        } else if (obj == passscontraint) {
            if (passscontraint.isSelected()) {
                passCon = "true";
            } else {
                passCon = "false";
            }

        } else if (obj == userchange) {
            if (userchange.isSelected()) {
                usernamePolicy = "true";
            } else {
                usernamePolicy = "false";
            }
        } else if (obj == admins) {
            if (admins.isSelected()) {
                adminStatus = "true";
            } else {
                adminStatus = "false";
            }

        }


    }


}
