/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package highschool;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FileDialog;
import java.awt.HeadlessException;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JFrame;

import javax.swing.JOptionPane;
import javax.swing.JPanel;

import javax.swing.JTextPane;

import static javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE;

import javax.swing.border.TitledBorder;

import net.miginfocom.swing.MigLayout;

/**
 * @author FRED
 */
public class SchoolDetailsReg extends JFrame implements ActionListener {

    public static void main(String[] args) {
        new SchoolDetailsReg();
    }

    private int width = 900;
    private int height = 700;
    private FredButton save, cancel;
    private PreparedStatement ps;
    private Connection con;
    private FredButton logo;
    private JPanel pane;
    private FileDialog fi;
    private FredLabel name, motto, mission, location, vision, contact, im, adress, email;
    private FredTextField jname, jlocation, jcontact, jemail;
    private JTextPane jVision, jmission, jmotto, jadress;
    String path = "";
    ResultSet rs;

    public SchoolDetailsReg() {
        setSize(width, height);
        setLocationRelativeTo(null);
        setLayout(new MigLayout());
        setTitle("School Detail Registration Panel");
        jname = new FredTextField();
        pane = new JPanel();
        save = new FredButton("Save");
        cancel = new FredButton("Close");
        name = new FredLabel("School name");
        mission = new FredLabel("School Mission");
        location = new FredLabel("Location");
        adress = new FredLabel("School Adress");
        logo = new FredButton("Browse Logo");
        vision = new FredLabel("School Vission");
        contact = new FredLabel("School Contact");
        email = new FredLabel("School Email");
        motto = new FredLabel("School Motto");
        jcontact = new FredTextField();
        jadress = new JTextPane();
        jVision = new JTextPane();
        jmotto = new JTextPane();
        jmission = new JTextPane();
        jlocation = new FredTextField();
        jemail = new FredTextField();
        im = new FredLabel();

        add(name, "growx,pushx,gapleft 30");
        add(jname, "growx,pushx,gapright 30,gapleft 50,wrap");
        add(contact, "growx,pushx,gapleft 30,gaptop 20");
        add(jcontact, "growx,pushx,gapright 30,gaptop 20,gapleft 50,wrap");
        add(email, "growx,pushx,gapleft 30,gaptop 20");
        add(jemail, "growx,pushx,gapright 30,gaptop 20,gapleft 50,wrap");
        add(location, "growx,pushx,gapleft 30,gaptop 20");
        add(jlocation, "growx,pushx,gapright 30,gaptop 20,gapleft 50,wrap");
        add(adress, "growx,pushx,gapleft 30,gaptop 20");
        add(jadress, "grow,push,gapright 30,gaptop 10,gapleft 50,wrap");
        add(motto, "growx,pushx,gapleft 30,gaptop 20");
        add(jmotto, "grow,push,gapright 30,gaptop 10,gapleft 50,wrap");
        add(mission, "growx,pushx,gapleft 30,gaptop 20");
        add(jmission, "grow,push,gapright 30,gaptop 10,gapleft 50,wrap");
        add(vision, "growx,pushx,gapleft 30,gaptop 20");
        add(jVision, "grow,push,gapright 30,gaptop 10,gapleft 50,wrap");
        add(logo, "gapleft 100,skip,split");
        add(pane, "grow,push,gapright 30,gaptop 10,gapleft 100,height 100:150:200,width 200:200:200,wrap");
        add(cancel, "gapleft 100");
        add(save, "gapleft 100");
        pane.setLayout(new BorderLayout());
        pane.add(im, BorderLayout.CENTER);
        pane.setBorder(new TitledBorder("School Logo"));
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

        con = DbConnection.connectDb();
        this.setIconImage(FrameProperties.icon());
        ((JComponent) getContentPane()).setBorder(
                BorderFactory.createMatteBorder(5, 5, 5, 5, Color.MAGENTA));
        setVisible(true);
        save.addActionListener(this);
        cancel.addActionListener(this);
        logo.addActionListener(this);
        jcontact.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();
                if (!Character.isDigit(c) || jcontact.getText().length() > 10) {
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
                JOptionPane.showMessageDialog(this, "Kindly Input A Valid School Name");

            } else {
                if (jcontact.getText().equalsIgnoreCase("")) {
                    getToolkit().beep();
                    JOptionPane.showMessageDialog(this, "Kindly Input A Valid Contact Number");

                } else {
                    if (jemail.getText().isEmpty()) {
                        getToolkit().beep();
                        JOptionPane.showMessageDialog(this, "Input A Valid Email");
                    } else {
                        if (jlocation.getText().isEmpty()) {
                            getToolkit().beep();
                            JOptionPane.showMessageDialog(this, "Input A Valid Location Name");
                        } else {
                            if (jadress.getText().isEmpty()) {
                                getToolkit().beep();
                                JOptionPane.showMessageDialog(this, "Enter Vaalid Address");
                            } else {
                                if (jmotto.getText().isEmpty()) {
                                    getToolkit().beep();
                                    JOptionPane.showMessageDialog(this, "Enter a Valid School Motto");
                                } else {
                                    if (jmission.getText().isEmpty()) {
                                        getToolkit().beep();
                                        JOptionPane.showMessageDialog(this, "Missing Mission");
                                    } else {
                                        if (jVision.getText().isEmpty()) {
                                            getToolkit().beep();
                                            JOptionPane.showMessageDialog(this, "Enter School Vission");
                                        } else {
                                            if (path.isEmpty()) {
                                                getToolkit().beep();
                                                JOptionPane.showMessageDialog(this, "Select The School Logo");
                                            } else {

                                                FileInputStream in = null;
                                                try {
                                                    String sql1 = "select * from schooldetails";
                                                    ps = con.prepareStatement(sql1);
                                                    rs = ps.executeQuery();
                                                    if (rs.next()) {
                                                        getToolkit().beep();
                                                        int option = JOptionPane.showConfirmDialog(this, "School Details Are Already In The System,\nPerforming This Operation Overwrites The"
                                                                + "Previous Details.\nDo you Want To Proceed", "Warning", JOptionPane.YES_NO_OPTION);
                                                        if (option == JOptionPane.NO_OPTION) {
                                                            JOptionPane.showMessageDialog(this, "Operation Cancelled");
                                                        } else {
                                                            String sql2 = "delete from schooldetails";
                                                            ps = con.prepareStatement(sql2);
                                                            ps.execute();

                                                            File img = new File(path);
                                                            in = new FileInputStream(img);
                                                            String sql = "insert into schooldetails values('" + jname.getText() + "','" + jadress.getText() + "','" + jcontact.getText() + "','" + jemail.getText() + "','" + jmotto.getText() + "','" + jmission.getText() + "','" + jVision.getText() + "','" + jlocation.getText() + "',?)";
                                                            ps = con.prepareStatement(sql);
                                                            ps.setBinaryStream(1, in, (int) img.length());
                                                            ps.execute();
                                                            JOptionPane.showMessageDialog(this, "School Details Updated Successfuly");

                                                        }
                                                    } else {

                                                        File img = new File(path);
                                                        in = new FileInputStream(img);

                                                        String sql = "insert into schooldetails values('" + jname.getText() + "','" + jadress.getText() + "','" + jcontact.getText() + "','" + jemail.getText() + "','" + jmotto.getText() + "','" + jmission.getText() + "','" + jVision.getText() + "','" + jlocation.getText() + "',?)";
                                                        ps = con.prepareStatement(sql);
                                                        ps.setBinaryStream(1, in, (int) img.length());
                                                        ps.execute();
                                                        JOptionPane.showMessageDialog(this, "Registration successfull");
                                                    }

                                                } catch (HeadlessException | FileNotFoundException | SQLException sq) {
                                                    JOptionPane.showMessageDialog(this, sq.getMessage());
                                                } finally {
                                                    try {
                                                        in.close();
                                                    } catch (IOException sq) {
                                                        JOptionPane.showMessageDialog(this, sq.getMessage());
                                                    }
                                                }
                                            }

                                        }
                                    }
                                }
                            }
                        }
                    }
                }

            }
        } else if (obj == cancel) {
            try {
                con.close();
            } catch (SQLException sq) {

            }
            CurrentFrame.currentWindow();
            dispose();
        } else if (obj == logo) {
            fi = new FileDialog(this);
            fi.show();
            path = fi.getDirectory() + fi.getFile();

            im.setIcon(resizeImage(path));
        }

    }

    public ImageIcon resizeImage(String path) {
        ImageIcon iimage;
        iimage = new ImageIcon(path);
        Image image = iimage.getImage();

        Image newimage = image.getScaledInstance(pane.getWidth(), pane.getHeight(), Image.SCALE_SMOOTH);
        ImageIcon ima = new ImageIcon(newimage);

        return ima;
    }


}
