/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package highschool;


import java.awt.Color;
import java.awt.FileDialog;
import java.awt.Graphics2D;
import java.awt.HeadlessException;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import static javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS;

import static javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE;

import javax.swing.border.TitledBorder;

import net.miginfocom.swing.MigLayout;

/**
 * @author FRED
 */
public class studentReg extends JFrame implements ActionListener {

    public static void main(String[] args) {
        new studentReg();
    }

    private static void copyFileUsingStream(File source, File dest) throws IOException {
        InputStream is = null;
        OutputStream os = null;
        try {
            is = new FileInputStream(source);
            os = new FileOutputStream(dest);
            byte[] buffer = new byte[1024];
            int length;
            while ((length = is.read(buffer)) > 0) {
                os.write(buffer, 0, length);
            }

        } finally {
            is.close();
            os.close();
        }
    }

    private int width = 1000;
    private int height = 710;
    private FredButton save, cancel, photo;
    private PreparedStatement ps;
    private FileDialog fi;
    private ImageIcon iimage;
    private String path;
    private Connection con;
    private JDialog dialog;
    private JPanel pane, pane2;
    private FredCombo pros = new FredCombo("Select Program");
    private FredLabel admno, fname, im, mname, lname, gender, dob, doa, fma, sta, tea, ct, cterm, cstraeam, cform, kcse, country, province, county, constituency, ward, parentname, tel1, tel2;
    private FredTextField jadm, jfname, jmname, jlanme, jparentname, jtel1, jtel2, jProgram, upi;
    private FredCombo jfma, jsta, jtea, jcform, jcstream, jcterm, jcountry, jconstituency, jprovince, jward, jcounty, jgender;
    private FredDateChooser jdoa, jdob;
    ResultSet rs;

    public studentReg() {
        admno = new FredLabel("admission Number");
        fname = new FredLabel("First Name");
        mname = new FredLabel("Middle Name");
        lname = new FredLabel("Last Name");
        gender = new FredLabel("Gender");
        dob = new FredLabel("Date Of Birth");
        doa = new FredLabel("Date Of admission");
        fma = new FredLabel("Form Admitted");
        sta = new FredLabel("Stream Admitted");
        tea = new FredLabel("Term Admitted");
        cterm = new FredLabel("Current Term");
        cstraeam = new FredLabel("Current Stream");
        cform = new FredLabel("Current Form");
        kcse = new FredLabel("K.C.P.E Marks");
        county = new FredLabel("County");
        country = new FredLabel("Country");
        province = new FredLabel("Province");
        county = new FredLabel("County");
        constituency = new FredLabel("Constituency");
        ward = new FredLabel("Ward");
        parentname = new FredLabel("Parent Full Names");
        tel1 = new FredLabel("Telephone Number");
        tel2 = new FredLabel("Telephone 2");
        photo = new FredButton("Browse Photo");
        jadm = new FredTextField();
        jfname = new FredTextField();
        jmname = new FredTextField();
        jlanme = new FredTextField();
        jProgram = new FredTextField();
        jparentname = new FredTextField();
        jtel1 = new FredTextField();
        jtel2 = new FredTextField();
        jfma = new FredCombo("Select Form Admitted");
        jsta = new FredCombo("Select Stream Admitted");
        jtea = new FredCombo("Select Term Admitted");
        jcform = new FredCombo("Select Current Form");
        jcstream = new FredCombo("Select Currrent Stream");
        jcterm = new FredCombo("Select Current Term");
        jcountry = new FredCombo("Select Country");
        jprovince = new FredCombo("select Province");
        jconstituency = new FredCombo("Select Constituency");
        jward = new FredCombo("Select Ward");
        jcounty = new FredCombo("Select County");
        jgender = new FredCombo("select Gender");
        jdoa = new FredDateChooser();
        jdob = new FredDateChooser();
        jdob.setMaxSelectableDate(new Date());
        pane = new JPanel();
        JScrollPane scrolls = new JScrollPane(pane);
        scrolls.setVerticalScrollBarPolicy(VERTICAL_SCROLLBAR_ALWAYS);
        // scrolls.setHorizontalScrollBarPolicy(HORIZONTAL_SCROLLBAR_ALWAYS);
        pane2 = new JPanel();
        pane2.setBackground(Color.WHITE);
        im = new FredLabel();
        save = new FredButton("Save Details");
        cancel = new FredButton("Close");
        upi = new FredTextField();
        upi.setBorder(new TitledBorder("UPI CODE"));
        setSize(width, height);
        setLocationRelativeTo(null);
        setLayout(new MigLayout());
        setTitle("Student Registration Panel");
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
        pane.setBorder(new TitledBorder("Input Student Details"));
        add(scrolls, "push,grow");
        pane2.setBorder(new TitledBorder("Student Photo"));
        pane.setLayout(new MigLayout());
        pane.add(admno, "growx,pushx,");
        pane.add(jadm, "growx,pushx,split 2");
        pane.add(upi, "growx,pushx");
        pane.add(kcse, "growx,pushx,gapleft 30");
        pane.add(jProgram, "growx,pushx,wrap");
        pane.add(fname, "growx,pushx,gaptop 15");
        pane.add(jfname, "growx,pushx,gaptop 15");
        pane.add(country, "growx,pushx,gapleft 30,gaptop 15");
        pane.add(jcountry, "growx,pushx,gaptop 15,wrap");
        pane.add(mname, "growx,pushx,gaptop 15");
        pane.add(jmname, "growx,pushx,gaptop 15");
        pane.add(province, "growx,pushx,gapleft 30,gaptop 15");
        pane.add(jprovince, "growx,pushx,gaptop 15,wrap");
        pane.add(lname, "growx,pushx,gaptop 15");
        pane.add(jlanme, "gaptop 15,growx,pushx");
        pane.add(county, "growx,pushx,gapleft 30,gaptop 15");
        pane.add(jcounty, "growx,pushx,gaptop 15,wrap");
        pane.add(gender, "growx,pushx,gaptop 15");
        pane.add(jgender, "gaptop 15,growx,pushx");
        pane.add(constituency, "growx,pushx,gapleft 30,gaptop 15");
        pane.add(jconstituency, "growx,pushx,gaptop 15,wrap");
        pane.add(dob, "growx,pushx,gaptop 15");
        pane.add(jdob, "gaptop 15,growx,pushx");
        pane.add(ward, "growx,pushx,gapleft 30,gaptop 15");
        pane.add(jward, "growx,pushx,gaptop 15,wrap");
        pane.add(doa, "growx,pushx,gaptop 15");
        pane.add(jdoa, "gaptop 15,growx,pushx");
        pane.add(parentname, "growx,pushx,gapleft 30,gaptop 15");
        pane.add(jparentname, "growx,pushx,gaptop 15,wrap");
        pane.add(fma, "growx,pushx,gaptop 15");
        pane.add(jfma, "gaptop 15,growx,pushx");
        pane.add(tel1, "growx,pushx,gapleft 30,gaptop 15");
        pane.add(jtel1, "growx,pushx,gaptop 15,wrap");
        pane.add(sta, "growx,pushx,gaptop 15");
        pane.add(jsta, "gaptop 15,growx,pushx");
        pane.add(tel2, "growx,pushx,gapleft 30,gaptop 15");
        pane.add(jtel2, "growx,pushx,gaptop 15,wrap");
        pane.add(tea, "growx,pushx,gaptop 20");
        pane.add(jtea, "gaptop 20,growx,pushx");
        pane.add(photo, "gapleft 50,gaptop 20");
        pane.add(pane2, "grow,push,gaptop 20,span 1 3,width 200:200:200,height 150:150:150,wrap");
        pane.add(cform, "growx,pushx,gaptop 20");
        pane.add(jcform, "gaptop 20,growx,pushx,wrap");
        pane.add(cstraeam, "growx,pushx,gaptop 20");
        pane.add(jcstream, "gaptop 20,growx,pushx,split 2");
        pane.add(pros, "gaptop 20,growx,pushx,split 2,wrap");
        // pane.add(cterm,"growx,pushx,gaptop 20");
        // pane.add(jcterm,"gaptop 20,growx,pushx");
        ;
        pane.add(cancel, "gapleft 50,gaptop 15,Skip");
        pane.add(save, "gaptop 15,skip,wrap");
        pros.setBorder(new TitledBorder("Select program"));

        pane2.add(im);
        photo.addActionListener(this);
        cancel.addActionListener(this);
        save.addActionListener(this);
        jcountry.addActionListener(this);
        jprovince.addActionListener(this);
        jcounty.addActionListener(this);
        jconstituency.addActionListener(this);
        jdoa.setDateFormatString("yyyy:MM:dd");
        jdob.setDateFormatString("yyyy:MM:dd");

        setVisible(true);

        jgender.addItem("MALE");
        jgender.addItem("FEMALE");
        jfname.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();
                if (Character.isDigit(c) || jfname.getText().length() > 20) {
                    getToolkit().beep();
                    e.consume();
                }

            }

        });
        jlanme.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();
                if (Character.isDigit(c) || jlanme.getText().length() > 20) {
                    getToolkit().beep();
                    e.consume();
                }

            }

        });
        jmname.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();
                if (Character.isDigit(c) || jmname.getText().length() > 20) {
                    getToolkit().beep();
                    e.consume();
                }

            }

        });
        jparentname.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();
                if (Character.isDigit(c) || jparentname.getText().length() > 50) {
                    getToolkit().beep();
                    e.consume();
                }

            }

        });


        jtel1.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();
                if (!Character.isDigit(c) || jtel1.getText().length() > 10) {
                    getToolkit().beep();
                    e.consume();
                }

            }

        });


        jtel2.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();
                if (!Character.isDigit(c) || jtel2.getText().length() > 10) {
                    getToolkit().beep();
                    e.consume();
                }

            }

        });
        jadm.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();

                if (!Character.isDigit(c) || jadm.getText().length() > 8) {
                    getToolkit().beep();
                    e.consume();
                }

            }

        });

        jadm.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() > 1) {
                    jadm.setText(String.valueOf(Globals.nextAdmissionNumber()));
                }

            }

        });
        jadm.setBorder(new TitledBorder("Double Click To Auto Assign"));
        try {
            int programconter = 0;
            String qeurry = "Select programname from programs";
            ps = con.prepareStatement(qeurry);
            rs = ps.executeQuery();
            while (rs.next()) {
                programconter++;
                pros.addItem(rs.getString("Programname"));
            }
            if (programconter == 1) {
                pros.setSelectedIndex(1);
            }
            pros.setSelectedIndex(1);
            String sql = "Select * from classes order by precision1 asc";
            con = DbConnection.connectDb();
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                jfma.addItem(rs.getString("ClassName"));
                jcform.addItem(rs.getString("ClassName"));
            }
            String sql2 = "Select * from streams";

            ps = con.prepareStatement(sql2);
            rs = ps.executeQuery();
            while (rs.next()) {
                jsta.addItem(rs.getString("streamName"));
                jcstream.addItem(rs.getString("StreamName"));
            }
            String sql3 = "Select * from countries";

            ps = con.prepareStatement(sql3);
            rs = ps.executeQuery();
            while (rs.next()) {
                jcountry.addItem(rs.getString("countryName"));

            }
            String sql9 = "Select* from terms";
            ps = con.prepareStatement(sql9);
            rs = ps.executeQuery();
            while (rs.next()) {
                jtea.addItem(rs.getString("TermName"));

            }
            String sql10 = "Select* from terms where status='" + "Current" + "'";
            ps = con.prepareStatement(sql10);
            rs = ps.executeQuery();
            while (rs.next()) {

                jcterm.addItem(rs.getString("TermName"));
            }


        } catch (SQLException sq) {
            JOptionPane.showMessageDialog(this, sq.getMessage());
        }
        if (jcstream.getItemCount() < 3) {
            jcstream.setSelectedIndex(1);
        }
        jcountry.addActionListener(this);

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object obj = e.getSource();
        if (obj == cancel) {
            try {
                con.close();
            } catch (SQLException sq) {
                sq.printStackTrace();
            }
            CurrentFrame.currentWindow();
            dispose();
        } else if (obj == photo) {
            fi = new FileDialog(this);
            fi.show();
            path = fi.getDirectory() + fi.getFile();

            im.setIcon(resizeImage(path));

        } else if (obj == jcountry) {
            try {
                jprovince.removeActionListener(this);
                jprovince.removeAllItems();
                jprovince.addItem("Select Province");
                String sql = "Select Countrycode from countries where countryname='" + jcountry.getSelectedItem() + "'";
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
            jcounty.removeAllItems();
            jcounty.addItem("Select County");
            try {

                String sql = "Select provincecode from provinces where provincename='" + jprovince.getSelectedItem() + "'";
                ps = con.prepareStatement(sql);
                rs = ps.executeQuery();
                if (rs.next()) {
                    String sql1 = "Select countyname  from counties where provincecode='" + rs.getString("provincecode") + "' order by countyName asc";
                    ps = con.prepareStatement(sql1);
                    rs = ps.executeQuery();
                    while (rs.next()) {
                        jcounty.addItem(rs.getString("countyname"));
                    }
                }


            } catch (SQLException sq) {
                JOptionPane.showMessageDialog(this, sq.getMessage());
            }
        } else if (obj == jcounty) {
            jconstituency.removeAllItems();
            jconstituency.addItem("Select Constituency");
            jconstituency.removeActionListener(this);
            try {

                String sql = "Select countycode from counties where countyname='" + jcounty.getSelectedItem() + "'";
                ps = con.prepareStatement(sql);
                rs = ps.executeQuery();
                if (rs.next()) {
                    String sql1 = "Select constituencyname  from constituencies where countycode='" + rs.getString("countycode") + "' order by constituencyname asc";
                    ps = con.prepareStatement(sql1);
                    rs = ps.executeQuery();
                    while (rs.next()) {
                        jconstituency.addItem(rs.getString("constituencyname"));
                    }
                }

            } catch (SQLException sq) {
                JOptionPane.showMessageDialog(this, sq.getMessage());
            }

            jconstituency.addActionListener(this);


        } else if (obj == jconstituency) {


            jward.removeAllItems();
            jward.addItem("Select Ward");
            try {

                String sql = "Select constituencycode from constituencies where constituencyname='" + jconstituency.getSelectedItem() + "'";
                ps = con.prepareStatement(sql);
                rs = ps.executeQuery();
                if (rs.next()) {
                    String sql1 = "Select wardname  from ward where constituencycode='" + rs.getString("constituencycode") + "' order by wardname asc";
                    ps = con.prepareStatement(sql1);
                    rs = ps.executeQuery();
                    while (rs.next()) {
                        jward.addItem(rs.getString("wardname"));
                    }
                }

            } catch (SQLException ex) {
                Logger.getLogger(studentReg.class.getName()).log(Level.SEVERE, null, ex);
            }


        } else if (obj == save) {


            String DOB = ((JTextField) jdob.getDateEditor().getUiComponent()).getText();
            String DOA = ((JTextField) jdoa.getDateEditor().getUiComponent()).getText();
            String constituencycode = "", wardcode = "", provincecode = "", countycode = "", countrycode = "", formcodea = "", formcodec = "", termcodea = "", termcodec = "", streamcodea = "", streamcodec = "";
            boolean comply = true;

            if (comply == true && jadm.getText().isEmpty()) {
                getToolkit().beep();
                JOptionPane.showMessageDialog(this, "Invalid admission Number");
                comply = false;
            } else if (comply == true && jfname.getText().isEmpty()) {
                getToolkit().beep();
                JOptionPane.showMessageDialog(this, "Invalid First Name");
                comply = false;
            } else if (comply == true && jmname.getText().isEmpty()) {
                getToolkit().beep();
                JOptionPane.showMessageDialog(this, "Invalid Middle Name");
                comply = false;
            } else if (comply == true && jgender.getSelectedIndex() == 0) {
                getToolkit().beep();
                JOptionPane.showMessageDialog(this, "Select gender");
                comply = false;
            }
            
        /*   else if(comply==true&&DOB.isEmpty() )
          {
           getToolkit().beep();
              JOptionPane.showMessageDialog(this, "Invalid Date Of Birth");
              comply=false;   
          }
            else if(comply==true&&DOA.isEmpty() )
          {
               if(ConfigurationIntialiser.dateOfBirth())
              {
                 getToolkit().beep();
              JOptionPane.showMessageDialog(this, "Invalid Date Of admission");
              comply=false;  
              }
             
          }
          else if(comply==true&& jdob.getDate().after(new Date()))
          {
             getToolkit().beep();
              JOptionPane.showMessageDialog(this, "Future Date Not Allowed");
              comply=false;   
          }
           else if(comply==true&& jdoa.getDate().after(new Date()))
          {
             getToolkit().beep();
              JOptionPane.showMessageDialog(this, "Future Date Not Allowed");
              comply=false;   
          }
                else if(comply==true&&jfma.getSelectedIndex()==0)
          {
           getToolkit().beep();
              JOptionPane.showMessageDialog(this, "Select Student Admitted  Level");
              comply=false;   
          }*/
            else if (comply == true && jsta.getSelectedIndex() == 0) {
                getToolkit().beep();
                JOptionPane.showMessageDialog(this, "Select Stream Admitted");
                comply = false;
            } else if (comply == true && jtea.getSelectedIndex() == 0) {
                getToolkit().beep();
                JOptionPane.showMessageDialog(this, "Select Term Admitted");
                comply = false;
            } else if (comply == true && jtea.getSelectedIndex() == 0) {
                getToolkit().beep();
                JOptionPane.showMessageDialog(this, "Select Term Admitted");
                comply = false;
            } else if (comply == true && jcform.getSelectedIndex() == 0) {
                getToolkit().beep();
                JOptionPane.showMessageDialog(this, "Select Current Form");
                comply = false;
            } else if (comply == true && jcstream.getSelectedIndex() == 0) {
                getToolkit().beep();
                JOptionPane.showMessageDialog(this, "Select Current Stream");
                comply = false;
            } else if (comply == true && pros.getSelectedIndex() == 0) {
                getToolkit().beep();
                JOptionPane.showMessageDialog(this, "Select admission Program");
                comply = false;
            }
        /*       else if(comply==true&&jProgram.getText().isEmpty())
          {
           getToolkit().beep();
              JOptionPane.showMessageDialog(this, "ENTER KCPE MARKS");
              comply=false;   
          }*/

            else if (comply == true && jcountry.getSelectedIndex() == 0) {
                if (ConfigurationIntialiser.residentialDetails()) {
                    getToolkit().beep();
                    JOptionPane.showMessageDialog(this, "Select Country");
                    comply = false;
                }

            } else if (comply == true && jprovince.getSelectedIndex() == 0) {
                if (ConfigurationIntialiser.residentialDetails()) {
                    getToolkit().beep();
                    JOptionPane.showMessageDialog(this, "Select Province");
                    comply = false;
                }

            } else if (comply == true && jcounty.getSelectedIndex() == 0) {
                if (ConfigurationIntialiser.residentialDetails()) {
                    getToolkit().beep();
                    JOptionPane.showMessageDialog(this, "Select County");
                    comply = false;
                }

            } else if (comply == true && jconstituency.getSelectedIndex() == 0) {
                if (ConfigurationIntialiser.residentialDetails()) {
                    getToolkit().beep();
                    JOptionPane.showMessageDialog(this, "Select Constituency");
                    comply = false;
                }

            } else if (comply == true && jward.getSelectedIndex() == 0) {
                if (ConfigurationIntialiser.residentialDetails()) {
                    getToolkit().beep();
                    JOptionPane.showMessageDialog(this, "Select Ward");
                    comply = false;

                }

            } else if (comply == true && jparentname.getText().isEmpty()) {
                if (ConfigurationIntialiser.parentDetails()) {
                    getToolkit().beep();
                    JOptionPane.showMessageDialog(this, "Invalid Parent Details");
                    comply = false;
                }


            }
            if (comply == true && jtel1.getText().isEmpty() || jtel1.getText().length() < 10) {
                if (ConfigurationIntialiser.phone()) {
                    getToolkit().beep();
                    JOptionPane.showMessageDialog(this, "Invalid  Telephone 1 Details");
                    comply = false;
                }


            }
         /*  else if(comply==true&& jtel2.getText().isEmpty()||jtel1.getText().length()<10)
           {if(ConfigurationIntialiser.phone())
           {
              getToolkit().beep();
              JOptionPane.showMessageDialog(this, "Invalid  Telephone 1 Details");
              comply=false;      
           }
            
              
           }
            */

            if (comply == true) {
                try {
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
                    String sql6 = "Select  classcode from classes where classname='" + jfma.getSelectedItem() + "'";
                    ps = con.prepareStatement(sql6);
                    rs = ps.executeQuery();
                    if (rs.next()) {
                        formcodea = rs.getString("classcode");
                    }
                    String sql7 = "Select  classcode from classes where classname='" + jcform.getSelectedItem() + "'";
                    ps = con.prepareStatement(sql7);
                    rs = ps.executeQuery();
                    if (rs.next()) {
                        formcodec = rs.getString("classcode");
                    }
                    String sql8 = "Select* from streams where streamname='" + jsta.getSelectedItem() + "'";
                    ps = con.prepareStatement(sql8);
                    rs = ps.executeQuery();
                    while (rs.next()) {
                        streamcodea = rs.getString("Streamcode");

                    }
                    String sql9 = "Select* from streams where streamname='" + jcstream.getSelectedItem() + "'";
                    ps = con.prepareStatement(sql9);
                    rs = ps.executeQuery();
                    while (rs.next()) {
                        streamcodec = rs.getString("Streamcode");
                    }
                    String querry = "Select termcode from terms where termname='" + jtea.getSelectedItem() + "'";
                    ps = con.prepareStatement(querry);
                    rs = ps.executeQuery();
                    while (rs.next()) {
                        termcodea = rs.getString("Termcode");

                    }


                    if (comply == true) {

                        String querry3 = "Select firstname,lastname,middlename from admission where admissionnumber='" + jadm.getText() + "'";
                        ps = con.prepareStatement(querry3);
                        ResultSet Rs = ps.executeQuery();
                        if (Rs.next()) {
                            JOptionPane.showMessageDialog(this, "admission Number " + jadm.getText() + " Is Bieng Used By " + Rs.getString("firstName") + "  " + Rs.getString("MiddleName") + " " + Rs.getString("LastName") + " \n admission Numbers Can Never Be Shared", "Duplicate Prevention", JOptionPane.INFORMATION_MESSAGE);
                        } else {

                            if (path == null || path.isEmpty()) {

                            } else {
                                File img = new File(path);

                                File out = new File(ConfigurationIntialiser.imageFolder() + "/" + jadm.getText() + ".jpg");
                                if (img != null || out != null) {
                                    Files.copy(img.toPath(), out.toPath());
                                    BufferedImage inputImage = ImageIO.read(img);
                                    BufferedImage outputImage = new BufferedImage(1280, 850, inputImage.getType());
                                    Graphics2D g2d = outputImage.createGraphics();
                                    g2d.drawImage(inputImage, 0, 0, 1280, 850, null);
                                    g2d.dispose();

                                    ImageIO.write(outputImage, "jpg", out);
                                }
                            }
                            int avg = 0;

                            if (DOB.isEmpty()) {
                                DOB = "2000:01:01";
                            }
                            if (DOA.isEmpty()) {
                                DOA = "2015:01:01";
                            }
                            String SQL = "Insert into admission values('" + jfname.getText().toUpperCase() + "','" + jmname.getText().toUpperCase() + "','" + jlanme.getText().toUpperCase() + "','" + jgender.getSelectedItem() + "'"
                                    + ",'" + DOB + "','" + DOA + "','" + formcodea + "','" + termcodea + "','" + streamcodea + "','" + formcodec + "','" + Globals.currentTerm() + "','" + streamcodec + "','" + jProgram.getText() + "','" + countrycode + "','" + provincecode + "',"
                                    + "'" + countycode + "','" + constituencycode + "','" + wardcode + "','" + jparentname.getText().toUpperCase() + "','" + jtel1.getText() + "','" + jtel2.getText() + "','" + jadm.getText() + "',?,'" + upi.getText().toUpperCase() + "','" + pros.getSelectedItem() + "')";

                            ps = con.prepareStatement(SQL);
                            ps.setString(1, ConfigurationIntialiser.imageFolder() + "/" + jadm.getText() + ".jpg");
                            ps.execute();
                            JOptionPane.showMessageDialog(this, "Student Details Saved SuccessFully");
                            jfname.setText("");
                            jmname.setText("");
                            jlanme.setText("");
                            jgender.setSelectedIndex(0);
//                    jtea.setSelectedIndex(0);
//                    jfma.setSelectedIndex(0);jcform.setSelectedIndex(0);
//                    jcstream.setSelectedIndex(0);
//                    jsta.setSelectedIndex(0);jcterm.setSelectedIndex(0);
                            jconstituency.setSelectedIndex(0);
                            jward.setSelectedIndex(0);
                            jprovince.setSelectedIndex(0);
                            upi.setText("");
                            jcountry.setSelectedIndex(0);
                            jcounty.setSelectedIndex(0);
                            jProgram.setText("");
                            jparentname.setText("");
                            jtel1.setText("");
                            jtel2.setText("");
                            jadm.setText("");

                        }


                    }


                } catch (HeadlessException | IOException | SQLException sq) {
                    JOptionPane.showMessageDialog(this, sq.getMessage());
                    sq.printStackTrace();
                }

            }

        }


    }

    public ImageIcon resizeImage(String path2) {

        iimage = new ImageIcon(path2);
        Image image = iimage.getImage();


        Image newimage = image.getScaledInstance(180, 120, Image.SCALE_SMOOTH);
        ImageIcon ima = new ImageIcon(newimage);

        return ima;
    }


}
