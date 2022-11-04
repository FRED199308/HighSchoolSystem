/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package highschool;

import com.itextpdf.text.Font;

import java.awt.Color;
import java.awt.FileDialog;
import java.awt.Graphics2D;
import java.awt.HeadlessException;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JDialog;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;

import static javax.swing.WindowConstants.DISPOSE_ON_CLOSE;

import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;

import net.miginfocom.swing.MigLayout;
import org.jfree.chart.title.Title;

/**
 * @author FRED
 */
public class StudentView implements ActionListener {

    private JPanel holder;
    private JPanel top;
    private JPanel bottom;
    private FredButton edit, delete;
    private FredButton view, back;
    private Connection con = DbConnection.connectDb();
    private PreparedStatement ps;
    JScrollPane panel;
    private ResultSet rs;
    private FredButton save, cancel, photo;
    JTable tab = new JTable();
    private FileDialog fi;
    private ImageIcon iimage;
    private String path;
    String adm;
    private JDialog dia;
    private JPanel pane = new JPanel(), pane2;
    private FredLabel admno, fname, im, mname, lname, gender, dob, doa, fma, sta, tea, ct, cterm, cstraeam, cform, kcse, country, province, county, constituency, ward, parentname, tel1, tel2;
    private FredLabel admno1, upi1, fname1, pros1, im1, mname1, lname1, gender1, dob1, doa1, fma1, sta1, tea1, ct1, cterm1, cstraeam1, cform1, kcse1, country1, province1, county1, constituency1, ward1, parentname1, tel11, tel21;
    private FredTextField jadm, jupi, jfname, jmname, jlanme, jparentname, jtel1, jtel2, jProgram;
    private FredCombo jfma, jsta, jtea, jcform, jcstream, jcterm, jcountry, jconstituency, jprovince, pros, jward, jcounty, jgender, classfilter, genderfilter;
    private FredDateChooser jdoa, jdob;
    private FredButton print;
    private FredButton allYrEx;
    private FredButton pending;
    private FredButton filterDate;
    private FredLabel search = new FredLabel("Search Student By Name Or admission Number");
    private int sum = 0;
    private FredLabel classname = new FredLabel("Classs/Level To Demote Or Promote To");
    private FredCombo jclass = new FredCombo("Select Stream");
    private FredTextField jsearch;
    private FredLabel infor = new FredLabel("FORM 1 STUDENTS");
    private JScrollPane panescroll = new JScrollPane();
    private FredButton demote = new FredButton("Demote/Promote Student");

    Object cols[] = {"NO", "Name", "admission Number", "UPI", "Class", "Stream"};
    Object row[] = new Object[cols.length];

    DefaultTableModel model = new DefaultTableModel() {
        @Override
        public boolean isCellEditable(int row, int col) {
            return false;
        }
    };

    public StudentView() {

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
        pros = new FredCombo("Select program");

        constituency = new FredLabel("Constituency");
        ward = new FredLabel("Ward");
        parentname = new FredLabel("Parent Full Names");
        tel1 = new FredLabel("Telephone Number");
        tel2 = new FredLabel("Telephone 2");
        photo = new FredButton("Browse Photo");
        jadm = new FredTextField();
        jupi = new FredTextField();
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
        jcountry.addActionListener(this);
        save = new FredButton("Update Details");
        delete = new FredButton("Delete Student");
        print = new FredButton("Generate Report");

        back = new FredButton("Back");
        save.addActionListener(this);
        jcountry.addActionListener(this);
        jprovince.addActionListener(this);
        jcounty.addActionListener(this);
        jconstituency.addActionListener(this);
        demote.addActionListener(this);
        back.addActionListener(this);
        print.addActionListener(this);
        delete.addActionListener(this);
        jupi.setBorder(new TitledBorder("UPI CODE"));
        classfilter = new FredCombo("Select Class");
        genderfilter = new FredCombo("Separate Girls Form Boys");
        genderfilter.addItem("MALE");
        genderfilter.addItem("FEMALE");
        jgender.addItem("MALE");
        jgender.addItem("FEMALE");
        jfname.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();
                if (Character.isDigit(c) || jfname.getText().length() > 20) {

                    e.consume();
                }

            }

        });
        jlanme.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();
                if (Character.isDigit(c) || jlanme.getText().length() > 20) {

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
        jparentname.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();
                if (Character.isDigit(c) || jparentname.getText().length() > 50) {

                    e.consume();
                }

            }

        });

        jtel1.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();
                if (!Character.isDigit(c) || jtel1.getText().length() > 10) {

                    e.consume();
                }

            }

        });

        jtel2.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();
                if (!Character.isDigit(c) || jtel2.getText().length() > 10) {

                    e.consume();
                }

            }

        });


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
            String sql = "Select * from classes order by precision1 asc";
            con = DbConnection.connectDb();
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                jfma.addItem(rs.getString("ClassName"));
                jcform.addItem(rs.getString("ClassName"));
                jclass.addItem(rs.getString("ClassName"));
                classfilter.addItem(rs.getString("ClassName"));
            }
            String sql2 = "Select * from streams";

            ps = con.prepareStatement(sql2);
            rs = ps.executeQuery();
            while (rs.next()) {
                jsta.addItem(rs.getString("streamName"));
                jcstream.addItem(rs.getString("StreamName"));
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
            JOptionPane.showMessageDialog(holder, sq.getMessage());
        }

    }

    public JPanel studentManger() {
        con = DbConnection.connectDb();

        tab.setModel(model);
        panel = new JScrollPane(tab);
        model.setColumnIdentifiers(cols);
        holder = new JPanel();
        holder = new JPanel();
        top = new JPanel();
        jsearch = new FredTextField();
        bottom = new JPanel();

        infor = new FredLabel("FORM 1 STUDENTS");
        top.setLayout(new MigLayout());
        bottom.setLayout(new MigLayout());
        holder.setLayout(new MigLayout());
        holder.add(top, "grow,push,wrap");
        holder.add(bottom, "growx,pushx");
        bottom.setBackground(Color.lightGray);
        top.add(infor, "Gapleft 30,growx,pushx");
        top.add(search, "Gapleft 30");
        top.add(jsearch, "Gapleft 30,growx,pushx,wrap");
        top.add(panel, "gapleft 30,push,grow,spanx");
        edit = new FredButton("Edit Student Details");
        view = new FredButton("View Student Details");
        view.addActionListener(this);


        bottom.add(print, "growx,pushx,gapleft 30");

        bottom.add(view, "growx,pushx,gapleft 30");
        bottom.add(edit, "growx,pushx,gapleft 30");
        bottom.add(back, "growx,pushx,gapleft 30,wrap");
        bottom.add(delete, "growx,pushx,gapleft 30");
        bottom.add(demote, "growx,pushx,gapleft 30");
        bottom.add(classfilter, "growx,pushx,gapleft 30");
        bottom.add(genderfilter, "growx,pushx,gapleft 30");

        try {

            int counter = 0;
            String sql = "Select * from admission where currentform = '" + Globals.classCode("FORM 1") + "' " + Globals.sortcode + " ";
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                counter++;
                row[0] = counter;
                row[1] = rs.getString("FirstName") + "     " + rs.getString("MiddleName") + "     " + rs.getString("LastName");
                row[2] = rs.getString("AdmissionNumber");
                row[3] = rs.getString("upi");
                row[4] = Globals.className(rs.getString("CurrentForm"));
                row[5] = Globals.streamName(rs.getString("CurrentStream"));

                model.addRow(row);

            }
            int programconter = 0;


        } catch (SQLException sq) {
            JOptionPane.showMessageDialog(holder, sq.getMessage());
        }

        jsearch.addKeyListener(new KeyAdapter() {

            @Override
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();
                if (c == KeyEvent.VK_ENTER) {

                    while (model.getRowCount() > 0) {
                        model.removeRow(0);
                    }
                    int counter = 0;
                    try {
                        String sql = "Select firstname,middlename,lastname,currentform,currentstream,upi,admissionnumber,kcpemarks from admission where admissionnumber like '" + jsearch.getText() + "%' or firstname like '" + jsearch.getText() + "%' or middlename like '" + jsearch.getText() + "%' or lastname like '" + jsearch.getText() + "%' or upi like '" + jsearch.getText() + "%' and  currentform like '" + "FM" + "%'  " + Globals.sortcode + " ";

                        ps = con.prepareStatement(sql);
                        rs = ps.executeQuery();
                        while (rs.next()) {
                            counter++;
                            row[0] = counter;
                            row[1] = rs.getString("FirstName") + "     " + rs.getString("MiddleName") + "     " + rs.getString("LastName");
                            row[2] = rs.getString("AdmissionNumber");
                            row[3] = rs.getString("upi");
                            row[4] = Globals.className(rs.getString("CurrentForm"));
                            row[5] = Globals.streamName(rs.getString("CurrentStream"));

                            model.addRow(row);

                        }
                    } catch (SQLException sq) {
                        JOptionPane.showMessageDialog(holder, sq.getMessage());
                        sq.printStackTrace();

                    }
                }
            }
        });
        photo.addActionListener((ActionEvent e1) -> {
            fi = new FileDialog(CurrentFrame.mainFrame());

            fi.show();
            path = fi.getDirectory() + fi.getFile();

            im.setIcon(resizeImage(path));
        });
        edit.addActionListener((ActionEvent e) -> {
            if (tab.getSelectedRowCount() < 1) {
                JOptionPane.showMessageDialog(holder, "Kindly Select The Student To Edit His Or Her Details");
            } else {

                top.removeAll();
                top.revalidate();
                top.repaint();
                pane.removeAll();
                pane.revalidate();
                pane.repaint();
                jdoa = new FredDateChooser();
                jdob = new FredDateChooser();
                jdob.setMaxSelectableDate(new Date());

                pane2 = new JPanel();
                pane2.setBackground(Color.WHITE);
                im = new FredLabel();
                jdoa.setDateFormatString("yyyy/MM/dd");
                jdob.setDateFormatString("yyyy/MM/dd");
                cancel = new FredButton("Close");

                top.add(pane, "push,grow");
                back.setVisible(true);
                pane.setBorder(new TitledBorder("Input Student Details"));

                pane2.setBorder(new TitledBorder("Student Photo"));
                pane.setLayout(new MigLayout());
                pane.add(admno, "growx,pushx");
                pane.add(jadm, "growx,pushx,split 2");
                pane.add(jupi, "growx,pushx");
                pane.add(kcse, "growx,pushx,gapleft 30");
                pane.add(jProgram, "growx,pushx,wrap");
                pane.add(fname, "growx,pushx");
                pane.add(jfname, "growx,pushx");
                pane.add(country, "growx,pushx,gapleft 30");
                pane.add(jcountry, "growx,pushx,wrap");
                pane.add(mname, "growx,pushx");
                pane.add(jmname, "growx,pushx");
                pane.add(province, "growx,pushx,gapleft 30");
                pane.add(jprovince, "growx,pushx,wrap");
                pane.add(lname, "growx,pushx");
                pane.add(jlanme, "growx,pushx");
                pane.add(county, "growx,pushx,gapleft 30");
                pane.add(jcounty, "growx,pushx,wrap");
                pane.add(gender, "growx,pushx");
                pane.add(jgender, "growx,pushx");
                pane.add(constituency, "growx,pushx,gapleft 30");
                pane.add(jconstituency, "growx,pushx,wrap");
                pane.add(dob, "growx,pushx");
                pane.add(jdob, "growx,pushx");
                pane.add(ward, "growx,pushx,gapleft 30");
                pane.add(jward, "growx,pushx,wrap");
                pane.add(doa, "growx,pushx");
                pane.add(jdoa, "growx,pushx");
                pane.add(parentname, "growx,pushx,gapleft 30");
                pane.add(jparentname, "growx,pushx,wrap");
                pane.add(fma, "growx,pushx");
                pane.add(jfma, "growx,pushx");
                pane.add(tel1, "growx,pushx,gapleft 30");
                pane.add(jtel1, "growx,pushx,wrap");
                pane.add(sta, "growx,pushx");
                pane.add(jsta, "growx,pushx");
                pane.add(tel2, "growx,pushx,gapleft 30");
                pane.add(jtel2, "growx,pushx,wrap");
                pane.add(tea, "growx,pushx,gaptop 20");
                pane.add(jtea, "gaptop 20,growx,pushx");
                pane.add(photo, "gapleft 50,gaptop 20");
                pane.add(pane2, "grow,push,gaptop 20,span 1 3,width 200:200:200,height 150:150:150,wrap");
                pane.add(cform, "growx,pushx,gaptop 20");
                pane.add(jcform, "gaptop 20,growx,pushx,wrap");
                pane.add(cstraeam, "growx,pushx,gaptop 20");
                pane.add(jcstream, "gaptop 20,growx,pushx,split 2");
                pane.add(pros, "gaptop 20,growx,pushx,split 2,wrap");
                pane.add(cterm, "growx,pushx,gaptop 20");
                pane.add(jcterm, "gaptop 20,growx,pushx");
                ;
                pane.add(cancel, "gapleft 50,gaptop 15");
                pane.add(save, "gaptop 15,wrap");
                pane2.add(im);
                pros.setBorder(new TitledBorder("admission Program"));
                cancel.setVisible(false);
                String countryid = "", countyid = "", provinceid = "", constituencyid = "", wardid = "";
                adm = tab.getValueAt(tab.getSelectedRow(), 2).toString();
                try {
                    String sql3 = "Select * from countries";

                    ps = con.prepareStatement(sql3);
                    rs = ps.executeQuery();
                    while (rs.next()) {
                        jcountry.addItem(rs.getString("countryName"));

                    }
                    String sql = "Select * from admission Where admissionNumber ='" + adm + "'";
                    ps = con.prepareStatement(sql);
                    rs = ps.executeQuery();
                    while (rs.next()) {

                        im.setIcon(resizeImage(ConfigurationIntialiser.imageFolder() + "/" + jadm.getText() + ".jpg"));


                        jfname.setText(rs.getString("FirstName"));
                        jmname.setText(rs.getString("middleName"));
                        jlanme.setText(rs.getString("lastName"));
                        jadm.setText(rs.getString("admissionNumber"));
                        jgender.setSelectedItem(rs.getString("Gender"));
                        jdoa.setDate(rs.getDate("DateofAdmission"));
                        jdob.setDate(rs.getDate("DateOfBirth"));
                        jcform.setSelectedItem(Globals.className(rs.getString("CurrentForm")));

                        jfma.setSelectedItem(Globals.className(rs.getString("classcode")));
                        jsta.setSelectedItem(Globals.streamName(rs.getString("streamAdmitted")));

                        jcterm.setSelectedItem(Globals.termname(rs.getString("currentterm")));
                        jtea.setSelectedItem(Globals.termname(rs.getString("termadmitted")));
                        jcstream.setSelectedItem(Globals.streamName(rs.getString("currentstream")));
                        jparentname.setText(rs.getString("parentfullNames"));
                        jtel1.setText(rs.getString("telephone1"));
                        jtel2.setText(rs.getString("telephone2"));
                        jProgram.setText(rs.getString("Kcpemarks"));
                        pros.setSelectedItem(rs.getString("program"));
                        countryid = rs.getString("Country");
                        countyid = rs.getString("County");
                        constituencyid = rs.getString("constituency");
                        provinceid = rs.getString("province");
                        wardid = rs.getString("ward");

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
                    }


                } catch (SQLException sq) {
                    JOptionPane.showMessageDialog(holder, sq.getMessage());
                }
            }

        });
        genderfilter.addActionListener(this);
        classfilter.addActionListener(this);


        return holder;

    }

    @Override
    public void actionPerformed(ActionEvent e) {

        Object obj = e.getSource();
        if (obj == classfilter) {
            if (classfilter.getSelectedIndex() == 0) {
                while (model.getRowCount() > 0) {
                    model.removeRow(0);
                }

                try {
                    infor.setText(classfilter.getSelectedItem() + " CLASS LIST");
                    int counter = 0;
                    String sql = "Select * from admission where  currentform like '" + "FM" + "%'  " + Globals.sortcode + " ";
                    ps = con.prepareStatement(sql);
                    rs = ps.executeQuery();
                    while (rs.next()) {
                        counter++;
                        row[0] = counter;
                        row[1] = rs.getString("FirstName") + "     " + rs.getString("MiddleName") + "     " + rs.getString("LastName");
                        row[2] = rs.getString("AdmissionNumber");
                        row[3] = rs.getString("upi");
                        row[4] = Globals.className(rs.getString("CurrentForm"));
                        row[5] = Globals.streamName(rs.getString("CurrentStream"));
                        ;
                        model.addRow(row);

                    }

                } catch (SQLException sq) {
                    JOptionPane.showMessageDialog(holder, sq.getMessage());
                }

            } else {


                while (model.getRowCount() > 0) {
                    model.removeRow(0);
                }

                try {
                    infor.setText(classfilter.getSelectedItem() + " CLASS LIST");
                    int counter = 0;
                    String sql = "Select * from admission where currentform ='" + Globals.classCode(classfilter.getSelectedItem().toString()) + "' " + Globals.sortcode + " ";
                    ps = con.prepareStatement(sql);
                    rs = ps.executeQuery();
                    while (rs.next()) {
                        counter++;
                        row[0] = counter;
                        row[1] = rs.getString("FirstName") + "     " + rs.getString("MiddleName") + "     " + rs.getString("LastName");
                        row[2] = rs.getString("AdmissionNumber");
                        row[3] = rs.getString("upi");
                        row[4] = Globals.className(rs.getString("CurrentForm"));
                        row[5] = Globals.streamName(rs.getString("CurrentStream"));
                        ;
                        model.addRow(row);

                    }

                } catch (SQLException sq) {
                    JOptionPane.showMessageDialog(holder, sq.getMessage());
                }
            }
        } else if (obj == genderfilter) {
            if (genderfilter.getSelectedIndex() == 0) {

                if (classfilter.getSelectedIndex() == 0) {
                    while (model.getRowCount() > 0) {
                        model.removeRow(0);
                    }

                    try {
                        infor.setText(classfilter.getSelectedItem() + " CLASS LIST");
                        int counter = 0;
                        String sql = "Select * from admission where  currentform like '" + "FM" + "%'  " + Globals.sortcode + " ";
                        ps = con.prepareStatement(sql);
                        rs = ps.executeQuery();
                        while (rs.next()) {
                            counter++;
                            row[0] = counter;
                            row[1] = rs.getString("FirstName") + "     " + rs.getString("MiddleName") + "     " + rs.getString("LastName");
                            row[2] = rs.getString("AdmissionNumber");
                            row[3] = rs.getString("upi");
                            row[4] = Globals.className(rs.getString("CurrentForm"));
                            row[5] = Globals.streamName(rs.getString("CurrentStream"));
                            ;

                            model.addRow(row);

                        }

                    } catch (SQLException sq) {
                        JOptionPane.showMessageDialog(holder, sq.getMessage());
                    }

                } else {


                    while (model.getRowCount() > 0) {
                        model.removeRow(0);
                    }

                    try {
                        infor.setText(classfilter.getSelectedItem() + " CLASS LIST");
                        int counter = 0;
                        String sql = "Select * from admission where currentform= '" + Globals.classCode(classfilter.getSelectedItem().toString()) + "' " + Globals.sortcode + " ";
                        ps = con.prepareStatement(sql);
                        rs = ps.executeQuery();
                        while (rs.next()) {
                            counter++;
                            row[0] = counter;
                            row[1] = rs.getString("FirstName") + "     " + rs.getString("MiddleName") + "     " + rs.getString("LastName");
                            row[2] = rs.getString("AdmissionNumber");
                            row[3] = rs.getString("upi");
                            row[4] = Globals.className(rs.getString("CurrentForm"));
                            row[5] = Globals.streamName(rs.getString("CurrentStream"));

                            model.addRow(row);

                        }

                    } catch (SQLException sq) {
                        JOptionPane.showMessageDialog(holder, sq.getMessage());
                    }
                }

            } else {

                String sex = "";
                if (genderfilter.getSelectedIndex() == 1) {
                    sex = "BOYS";
                } else if (genderfilter.getSelectedIndex() == 2) {
                    sex = "GIRLS";
                }
                if (classfilter.getSelectedIndex() == 0) {
                    while (model.getRowCount() > 0) {
                        model.removeRow(0);
                    }

                    try {
                        infor.setText(sex + " CLASS LIST");
                        int counter = 0;
                        String sql = "Select * from admission where  currentform like '" + "FM" + "%'  and gender='" + genderfilter.getSelectedItem() + "' " + Globals.sortcode + " ";
                        ps = con.prepareStatement(sql);
                        rs = ps.executeQuery();
                        while (rs.next()) {
                            counter++;
                            row[0] = counter;
                            row[1] = rs.getString("FirstName") + "     " + rs.getString("MiddleName") + "     " + rs.getString("LastName");
                            row[2] = rs.getString("AdmissionNumber");
                            row[3] = rs.getString("upi");
                            row[4] = Globals.className(rs.getString("CurrentForm"));
                            row[5] = Globals.streamName(rs.getString("CurrentStream"));


                            model.addRow(row);

                        }

                    } catch (SQLException sq) {
                        JOptionPane.showMessageDialog(holder, sq.getMessage());
                    }

                } else {

                    if (genderfilter.getSelectedIndex() == 1) {
                        sex = "BOYS";
                    } else if (genderfilter.getSelectedIndex() == 2) {
                        sex = "GIRLS";
                    }
                    while (model.getRowCount() > 0) {
                        model.removeRow(0);
                    }

                    try {
                        infor.setText(classfilter.getSelectedItem() + " " + sex + " CLASS LIST ");
                        int counter = 0;
                        String sql = "Select * from admission where currentform = '" + Globals.classCode(classfilter.getSelectedItem().toString()) + "'  and gender='" + genderfilter.getSelectedItem() + "' " + Globals.sortcode + " ";
                        ps = con.prepareStatement(sql);
                        rs = ps.executeQuery();
                        while (rs.next()) {
                            counter++;
                            row[0] = counter;
                            row[1] = rs.getString("FirstName") + "     " + rs.getString("MiddleName") + "     " + rs.getString("LastName");
                            row[2] = rs.getString("AdmissionNumber");
                            row[3] = rs.getString("upi");
                            row[4] = Globals.className(rs.getString("CurrentForm"));
                            row[5] = Globals.streamName(rs.getString("CurrentStream"));

                            model.addRow(row);

                        }

                    } catch (SQLException sq) {
                        JOptionPane.showMessageDialog(holder, sq.getMessage());
                    }
                }
            }
        }
        if (obj == print) {
            ReportGenerator.generateReport(infor.getText(), "Studentlist", tab);
        }
        if (obj == delete) {
            if (tab.getSelectedRowCount() < 1) {
                JOptionPane.showMessageDialog(holder, "Kindly Select The Student To Delete From School Register ");
            } else {
                int option = JOptionPane.showConfirmDialog(holder, "Are You Sure You Want To Delete\n This Student From The School Register??,\n All Information Concerning Him/Her Will Be Lost", "Confirm Deletion", JOptionPane.YES_NO_OPTION);
                if (option == JOptionPane.YES_OPTION) {
                    adm = tab.getValueAt(tab.getSelectedRow(), 2).toString();
                    try {
                        String sql = "Delete From admission Where admissionnumber='" + adm + "'";
                        ps = con.prepareStatement(sql);
                        ps.execute();
                        JOptionPane.showMessageDialog(holder, "Student Deleted From School Register Successfully");

                    } catch (HeadlessException | SQLException sq) {
                        JOptionPane.showMessageDialog(holder, sq.getMessage());
                        model.removeRow(tab.getSelectedRow());
                    }
                } else {
                    JOptionPane.showMessageDialog(holder, "Deletion PostPoned");
                }


            }
        }
        if (obj == demote) {

            if (Globals.Level.equalsIgnoreCase("Admin")) {
                if (tab.getSelectedRowCount() < 1) {
                    JOptionPane.showMessageDialog(holder, "Kindly Select The Student To Demote ");
                } else {

                    dia = new JDialog();
                    dia.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
                    dia.setSize(600, 300);
                    dia.setIconImage(FrameProperties.icon());
                    dia.setLocationRelativeTo(holder);
                    dia.setTitle("Student Demotion/Promotion Panel");
                    dia.setLayout(new MigLayout());
                    dia.add(classname, "gaptop 50,gapleft 50");
                    dia.add(jclass, "gaptop 50,gapleft 50,wrap");

                    FredButton button1 = new FredButton("Proceed");

                    infor.setFont(new java.awt.Font("serif", Font.NORMAL, 2));
                    dia.add(button1, "gaptop 100,gapleft 100");
                    dia.setVisible(true);
                    dia.addWindowListener(new WindowAdapter() {

                        @Override
                        public void windowOpened(WindowEvent e) {
                            CurrentFrame.mainFrame().setEnabled(false);
                        }

                        @Override
                        public void windowClosing(WindowEvent e) {
                            CurrentFrame.currentWindow();
                        }


                    });
                    button1.addActionListener((ActionEvent e1) -> {
                        if (jclass.getSelectedIndex() == 0) {
                            JOptionPane.showMessageDialog(holder, "Kindly The Level To demote /Promote To");
                        } else {
                            try {
                                int amount = 0, balance = 0, previousbalacence = 0;
                                String adm1 = tab.getValueAt(tab.getSelectedRow(), 2).toString();
                                String classcode = Globals.classCode(tab.getValueAt(tab.getSelectedRow(), 3).toString());
                                String selectedclasscode = Globals.classCode(jclass.getSelectedItem().toString());
                                String pycode = "";
                                String streamcode = Globals.streamcode(tab.getValueAt(tab.getSelectedRow(), 4).toString());
                                if (selectedclasscode.equalsIgnoreCase(classcode)) {
                                    JOptionPane.showMessageDialog(holder, "No Change Of Level Detected");
                                } else {


                                    String sql5 = "Update admission set currentform='" + selectedclasscode + "' where admissionnumber='" + adm1 + "'";
                                    ps = con.prepareStatement(sql5);
                                    ps.execute();
                                    JOptionPane.showMessageDialog(holder, "Student Demotion/Promotion Successfully");


                                }
                            } catch (HeadlessException | SQLException sq) {
                                sq.printStackTrace();
                                JOptionPane.showMessageDialog(holder, sq.getMessage());
                            }
                        }
                    });

                }

            } else {
                JOptionPane.showMessageDialog(holder, "You Lack Sufficient Rights To Perfom This Operation\n Consult Administrator", "Access Denied", JOptionPane.INFORMATION_MESSAGE);
            }


        }
        if (obj == view) {
            if (tab.getSelectedRowCount() < 1) {
                JOptionPane.showMessageDialog(holder, "Kindly Select The Student To View His Or Her Details");
            } else {


                admno1 = new FredLabel("admission Number");
                admno1.setForeground(Color.red);
                upi1 = new FredLabel();
                upi1.setBorder(new TitledBorder("UPI CODE"));
                fname1 = new FredLabel();
                fname1.setForeground(Color.red);
                mname1 = new FredLabel();
                mname1.setForeground(Color.red);
                lname1 = new FredLabel();
                lname1.setForeground(Color.red);
                gender1 = new FredLabel();
                gender1.setForeground(Color.red);
                dob1 = new FredLabel();
                dob1.setForeground(Color.red);
                doa1 = new FredLabel();
                doa1.setForeground(Color.red);
                fma1 = new FredLabel();
                fma1.setForeground(Color.red);
                sta1 = new FredLabel();
                sta1.setForeground(Color.red);
                tea1 = new FredLabel();
                tea1.setForeground(Color.red);
                cterm1 = new FredLabel();
                cterm1.setForeground(Color.red);
                cstraeam1 = new FredLabel();
                cstraeam1.setForeground(Color.red);
                cform1 = new FredLabel();
                cform1.setForeground(Color.red);
                kcse1 = new FredLabel();
                kcse1.setForeground(Color.red);
                county1 = new FredLabel();
                county1.setForeground(Color.red);
                country1 = new FredLabel();
                country1.setForeground(Color.red);
                province1 = new FredLabel();
                province1.setForeground(Color.red);
                pros1 = new FredLabel("");
                pros1.setBorder(new TitledBorder("admission Program"));
                constituency1 = new FredLabel();
                constituency1.setForeground(Color.red);
                ward1 = new FredLabel();
                ward1.setForeground(Color.red);
                parentname1 = new FredLabel();
                parentname1.setForeground(Color.red);
                tel11 = new FredLabel();
                tel11.setForeground(Color.red);
                tel21 = new FredLabel();
                tel21.setForeground(Color.red);
                top.removeAll();
                top.revalidate();
                top.repaint();

                jdoa = new FredDateChooser();
                jdob = new FredDateChooser();
                jdob.setMaxSelectableDate(new Date());
                pane = new JPanel();
                pane2 = new JPanel();
                pane2.setBackground(Color.WHITE);
                im = new FredLabel();
                jdoa.setDateFormatString("yyyy/MM/dd");
                jdob.setDateFormatString("yyyy/MM/dd");
                cancel = new FredButton("Close");

                top.add(pane, "push,grow");
                back.setVisible(true);
                pane.setBorder(new TitledBorder("Student Details"));

                pane2.setBorder(new TitledBorder("Student Photo"));
                pane.setLayout(new MigLayout());
                pane.add(admno, "growx,pushx,gaptop 10");
                pane.add(admno1, "growx,pushx,split 2,gaptop 10");
                pane.add(upi1, "growx,pushx,gaptop 10");
                pane.add(kcse, "growx,pushx,gapleft 30,gaptop 10");
                pane.add(kcse1, "growx,pushx,wrap,gaptop 10");
                pane.add(fname, "growx,pushx,gaptop 10");
                pane.add(fname1, "growx,pushx,gaptop 10");
                pane.add(country, "growx,pushx,gapleft 30,gaptop 10");
                pane.add(country1, "growx,pushx,wrap,gaptop 10");
                pane.add(mname, "growx,pushx,gaptop 10");
                pane.add(mname1, "growx,pushx,gaptop 10");
                pane.add(province, "growx,pushx,gapleft 30,gaptop 10");
                pane.add(province1, "growx,pushx,wrap,gaptop 10");
                pane.add(lname, "growx,pushx,gaptop 10");
                pane.add(lname1, "growx,pushx,gaptop 10");
                pane.add(county, "growx,pushx,gapleft 30,gaptop 10");
                pane.add(county1, "growx,pushx,wrap,gaptop 10");
                pane.add(gender, "growx,pushx,gaptop 10");
                pane.add(gender1, "growx,pushx,gaptop 10");
                pane.add(constituency, "growx,pushx,gapleft 30,gaptop 10");
                pane.add(constituency1, "growx,pushx,wrap,gaptop 10");
                pane.add(dob, "growx,pushx,gaptop 10");
                pane.add(dob1, "growx,pushx,gaptop 10");
                pane.add(ward, "growx,pushx,gapleft 30,gaptop 10");
                pane.add(ward1, "growx,pushx,gaptop 10,wrap");
                pane.add(doa, "growx,pushx,gaptop 10");
                pane.add(doa1, "growx,pushx,gaptop 10");
                pane.add(parentname, "growx,pushx,gapleft 30,gaptop 10");
                pane.add(parentname1, "growx,pushx,gaptop 10,wrap");
                pane.add(fma, "growx,pushx,gaptop 10");
                pane.add(fma1, "growx,pushx,gaptop 10");
                pane.add(tel1, "growx,pushx,gapleft 30,gaptop 10");
                pane.add(tel11, "growx,pushx,gaptop 10,wrap");
                pane.add(sta, "growx,pushx,gaptop 10");
                pane.add(sta1, "growx,pushx,gaptop 10");
                pane.add(tel2, "growx,pushx,gapleft 30,gaptop 10");
                pane.add(tel21, "growx,pushx,gaptop 10,wrap");
                pane.add(tea, "growx,pushx,gaptop 10");
                pane.add(tea1, "gaptop 10,growx,pushx,gaptop 10");
                //pane.add(photo, "gapleft 50,gaptop 20");
                pane.add(pane2, "grow,push,gaptop 10,span 1 3,width 200:200:200,height 150:150:150,wrap");
                pane.add(cform, "growx,pushx,gaptop 10");
                pane.add(cform1, "gaptop 10,growx,pushx,wrap");
                pane.add(cstraeam, "growx,pushx,gaptop 10");
                pane.add(cstraeam1, "gaptop 10,growx,pushx,split 2");
                pane.add(pros1, "gaptop 10,growx,pushx,wrap");
                pane.add(cterm, "growx,pushx,gaptop 15");
                pane.add(cterm1, "gaptop 10,growx,pushx");
                ;
                pane.add(cancel, "gapleft 50,gaptop 10");

                pane2.add(im);
                String countryid = "", countyid = "", provinceid = "", constituencyid = "", wardid = "";
                adm = tab.getValueAt(tab.getSelectedRow(), 2).toString();

                try {


                    String sql = "Select * from admission Where admissionNumber ='" + adm + "'";
                    ps = con.prepareStatement(sql);
                    rs = ps.executeQuery();
                    while (rs.next()) {
                        im.setIcon(resizeImage(ConfigurationIntialiser.imageFolder() + "/" + adm + ".jpg"));
                        //im.setIcon(resizeImage(ConfigurationIntialiser.imageFolder()+"/"+jadm.getText()+".jpg"));
                        System.err.println(ConfigurationIntialiser.imageFolder() + "/" + adm + ".jpg");
                        fname1.setText(rs.getString("FirstName"));
                        mname1.setText(rs.getString("middleName"));
                        lname1.setText(rs.getString("lastName"));
                        admno1.setText(rs.getString("admissionNumber"));
                        gender1.setText(rs.getString("Gender"));
                        doa1.setText(rs.getString("DateofAdmission"));
                        dob1.setText(rs.getString("DateOfBirth"));
                        cform1.setText(Globals.className(rs.getString("CurrentForm")));
                        upi1.setText(rs.getString("UPI"));
                        pros1.setText(rs.getString("program"));

                        fma1.setText(Globals.className(rs.getString("classcode")));
                        sta1.setText(Globals.streamName(rs.getString("streamAdmitted")));

                        cterm1.setText(Globals.termname(rs.getString("currentterm")));
                        tea1.setText(Globals.termname(rs.getString("termadmitted")));
                        cstraeam1.setText(Globals.streamName(rs.getString("currentstream")));
                        parentname1.setText(rs.getString("parentfullNames"));
                        tel11.setText(rs.getString("telephone1"));
                        tel21.setText(rs.getString("telephone2"));
                        kcse1.setText(rs.getString("kcpemarks"));
                        countryid = rs.getString("Country");
                        countyid = rs.getString("County");
                        constituencyid = rs.getString("constituency");
                        provinceid = rs.getString("province");
                        wardid = rs.getString("ward");

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
                    }

                } catch (SQLException sq) {
                    JOptionPane.showMessageDialog(holder, sq.getMessage());
                }


                cancel.setVisible(false);

            }
        }

        if (obj == jcountry) {

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

            } catch (SQLException ex) {
                Logger.getLogger(studentReg.class.getName()).log(Level.SEVERE, null, ex);
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
                JOptionPane.showMessageDialog(holder, sq.getMessage());
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

                JOptionPane.showMessageDialog(holder, "Invalid admission Number");
                comply = false;
            } else if (comply == true && jfname.getText().isEmpty()) {

                JOptionPane.showMessageDialog(holder, "Invalid First Name");
                comply = false;
            } else if (comply == true && jmname.getText().isEmpty()) {

                JOptionPane.showMessageDialog(holder, "Invalid Middle Name");
                comply = false;
            } else if (comply == true && jgender.getSelectedIndex() == 0) {

                JOptionPane.showMessageDialog(holder, "Select gender");
                comply = false;
            } else if (comply == true && DOB.isEmpty()) {

                JOptionPane.showMessageDialog(holder, "Invalid Date Of Birth");
                comply = false;
            } else if (comply == true && DOA.isEmpty()) {
                if (ConfigurationIntialiser.dateOfBirth()) {

                    JOptionPane.showMessageDialog(holder, "Invalid Date Of admission");
                    comply = false;
                }

            } else if (comply == true && jdob.getDate().after(new Date())) {

                JOptionPane.showMessageDialog(holder, "Future Date Not Allowed");
                comply = false;
            } else if (comply == true && jdoa.getDate().after(new Date())) {

                JOptionPane.showMessageDialog(holder, "Future Date Not Allowed");
                comply = false;
            } else if (comply == true && jfma.getSelectedIndex() == 0) {

                JOptionPane.showMessageDialog(holder, "Select Student Admitted  Level");
                comply = false;
            } else if (comply == true && jsta.getSelectedIndex() == 0) {

                JOptionPane.showMessageDialog(holder, "Select Stream Admitted");
                comply = false;
            } else if (comply == true && jtea.getSelectedIndex() == 0) {

                JOptionPane.showMessageDialog(holder, "Select Term Admitted");
                comply = false;
            } else if (comply == true && jtea.getSelectedIndex() == 0) {

                JOptionPane.showMessageDialog(holder, "Select Term Admitted");
                comply = false;
            } else if (comply == true && jcform.getSelectedIndex() == 0) {

                JOptionPane.showMessageDialog(holder, "Select Current Form");
                comply = false;
            } else if (comply == true && jcstream.getSelectedIndex() == 0) {

                JOptionPane.showMessageDialog(holder, "Select Current Stream");
                comply = false;
            }
          /*      else if(comply==true&&jProgram.getText().isEmpty())
          {
              JOptionPane.showMessageDialog(holder, "Select Enrolled School Program");
              comply=false;   
          }
          */
            else if (comply == true && jcountry.getSelectedIndex() == 0) {
                if (ConfigurationIntialiser.residentialDetails()) {

                    JOptionPane.showMessageDialog(holder, "Select Country");
                    comply = false;
                }

            } else if (comply == true && jprovince.getSelectedIndex() == 0) {
                if (ConfigurationIntialiser.residentialDetails()) {

                    JOptionPane.showMessageDialog(holder, "Select Province");
                    comply = false;
                }

            } else if (comply == true && jcounty.getSelectedIndex() == 0) {
                if (ConfigurationIntialiser.residentialDetails()) {

                    JOptionPane.showMessageDialog(holder, "Select County");
                    comply = false;
                }

            } else if (comply == true && jconstituency.getSelectedIndex() == 0) {
                if (ConfigurationIntialiser.residentialDetails()) {

                    JOptionPane.showMessageDialog(holder, "Select Constituency");
                    comply = false;
                }

            } else if (comply == true && jward.getSelectedIndex() == 0) {
                if (ConfigurationIntialiser.residentialDetails()) {
                    JOptionPane.showMessageDialog(holder, "Select Ward");
                    comply = false;

                }

            } else if (comply == true && jparentname.getText().isEmpty()) {
                if (ConfigurationIntialiser.parentDetails()) {

                    JOptionPane.showMessageDialog(holder, "Invalid Parent Details");
                    comply = false;
                }


            } else if (comply == true && jtel1.getText().isEmpty() || jtel1.getText().length() < 10) {
                if (ConfigurationIntialiser.phone()) {

                    JOptionPane.showMessageDialog(holder, "Invalid  Telephone 1 Details");
                    comply = false;
                }


            } else if (comply == true && jtel2.getText().isEmpty() || jtel1.getText().length() < 10) {
                if (ConfigurationIntialiser.phone()) {

                    JOptionPane.showMessageDialog(holder, "Invalid  Telephone 1 Details");
                    comply = false;
                }


            }

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
                        if (path == null || path.isEmpty()) {

                        } else {
                            File img = new File(path);

                            File out = new File(ConfigurationIntialiser.imageFolder() + "/" + jadm.getText() + ".jpg");
                            out.delete();
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

                        String SQL = "Update admission set FirstName='" + jfname.getText().toUpperCase() + "',MiddleName='" + jmname.getText().toUpperCase() + "',LastName='" + jlanme.getText().toUpperCase() + "',Gender='" + jgender.getSelectedItem() + "'"
                                + ",DateOfBirth='" + DOB + "',DateofAdmission='" + DOA + "',classcode='" + formcodea + "',TermAdmitted='" + Globals.termcode(jtea.getSelectedItem().toString()) + "',StreamAdmitted='" + streamcodea + "',CurrentForm='" + formcodec + "',CurrentTerm='" + Globals.currentTerm() + "',CurrentStream='" + streamcodec + "',kcpemarks='" + jProgram.getText() + "',Country='" + countrycode + "',province='" + provincecode + "',"
                                + "county='" + countycode + "',constituency='" + constituencycode + "',ward='" + wardcode + "',parentfullNames='" + jparentname.getText().toUpperCase() + "',Telephone1='" + jtel1.getText() + "',telephone2='" + jtel2.getText() + "',AdmissionNumber='" + jadm.getText() + "',upi='" + jupi.getText().toUpperCase() + "',ImageLocation=? ,program='" + pros.getSelectedItem() + "' where admissionNumber='" + adm + "'";
                        ps = con.prepareStatement(SQL);
                        ps.setString(1, ConfigurationIntialiser.imageFolder() + "/" + jadm.getText() + ".jpg");
                        ps.execute();

                        JOptionPane.showMessageDialog(holder, "Student Details Updated SuccessFully");
                        jfname.setText("");
                        jmname.setText("");
                        jlanme.setText("");
                        jgender.setSelectedIndex(0);
                        jtea.setSelectedIndex(0);
                        jfma.setSelectedIndex(0);
                        jcform.setSelectedIndex(0);
                        jcstream.setSelectedIndex(0);
                        jsta.setSelectedIndex(0);
                        jcterm.setSelectedIndex(0);
                        jconstituency.setSelectedIndex(0);
                        jward.setSelectedIndex(0);
                        jprovince.setSelectedIndex(0);
                        jcountry.setSelectedIndex(0);
                        jcounty.setSelectedIndex(0);
                        jProgram.setText("");
                        jparentname.setText("");
                        jtel1.setText("");
                        jtel2.setText("");
                        jadm.setText("");
                        path = null;

                    }

                } catch (HeadlessException | IOException | SQLException sq) {
                    JOptionPane.showMessageDialog(holder, path);
                    sq.printStackTrace();
                }

            }

        } else if (obj == back) {
            top.remove(pane);
            top.revalidate();
            top.repaint();
            top.add(infor, "Gapleft 30,growx,pushx");
            top.add(search, "Gapleft 30");
            top.add(jsearch, "Gapleft 30,growx,pushx,wrap");
            top.add(panel, "gapleft 30,push,grow,spanx");
        }


    }

    public ImageIcon resizeImage(String path) {

        iimage = new ImageIcon(path);
        Image image = iimage.getImage();

        Image newimage = image.getScaledInstance(180, 130, Image.SCALE_SMOOTH);
        ImageIcon ima = new ImageIcon(newimage);

        return ima;
    }
}
