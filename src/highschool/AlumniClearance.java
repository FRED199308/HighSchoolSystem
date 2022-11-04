/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package highschool;

import com.itextpdf.text.Font;

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
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JDialog;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import static javax.swing.WindowConstants.DISPOSE_ON_CLOSE;

import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;

import net.miginfocom.swing.MigLayout;

/**
 * @author FRED
 */
public class AlumniClearance implements ActionListener {

    private JPanel holder;
    private JPanel top;
    private JPanel bottom;

    private FredButton view, back;
    private Connection con = DbConnection.connectDb();
    private PreparedStatement ps;
    JScrollPane panel;
    private ResultSet rs;
    private FredButton cancel, photo;
    JTable tab = new JTable();
    private FileDialog fi;
    private ImageIcon iimage;
    private String path;
    String adm;
    private JDialog dia;
    private JPanel pane = new JPanel(), pane2;
    private FredLabel admno, fname, im, mname, lname, gender, dob, doa, fma, sta, tea, ct, cterm, cstraeam, cform, program, country, province, county, constituency, ward, parentname, tel1, tel2;
    private FredLabel admno1, fname1, im1, mname1, lname1, gender1, dob1, doa1, fma1, sta1, tea1, ct1, cterm1, cstraeam1, cform1, program1, country1, province1, county1, constituency1, ward1, parentname1, tel11, tel21;

    private FredCombo genderfilter, programfilter;

    private FredButton print;
    private FredButton cancel2 = new FredButton("Close");
    private FredCombo jsubject = new FredCombo("Select The Clearance Subject");
    private FredButton filterDate, clearancer, bookhistory;
    private FredLabel search = new FredLabel("Search Alumni By Name Or admission Number");
    FredLabel information;
    private int sum = 0;
    private FredLabel classname = new FredLabel("Classs/Level To Demote Or Promote To");

    private FredTextField jsearch;
    private FredLabel infor = new FredLabel();
    FredCombo yearfilter = new FredCombo("Filter By Year Of Completion");
    FredButton print2 = new FredButton("Export To Pdf");

    FredButton clear = new FredButton("Clear");
    JTable tab2 = new JTable();
    Object cols[] = {"NO", "Name", "admission Number", "Year Of Completion"};
    Object row[] = new Object[cols.length];
    JDialog dia2 = new JDialog();
    DefaultTableModel model = new DefaultTableModel() {
        @Override
        public boolean isCellEditable(int row, int col) {
            return false;
        }
    };

    public AlumniClearance() {

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
        program = new FredLabel("Enrolled Program");
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


        for (int k = 2015; k <= Globals.academicYear(); k++) {
            yearfilter.addItem(k);
        }

        print = new FredButton("Generate Report");

        back = new FredButton("Back");


        back.addActionListener(this);
        print.addActionListener(this);


        genderfilter = new FredCombo("Separate Girls Form Boys");
        genderfilter.addItem("MALE");
        genderfilter.addItem("FEMALE");


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
        bottom.setBackground(Color.lightGray);
        clearancer = new FredButton("Generate Clearance Form");
        bookhistory = new FredButton("View Book Issue History");
        infor = new FredLabel("Alumni Record");
        top.setLayout(new MigLayout());
        bottom.setLayout(new MigLayout());
        holder.setLayout(new MigLayout());
        holder.add(top, "grow,push,wrap");
        holder.add(bottom, "growx,pushx");

        top.add(infor, "Gapleft 30,growx,pushx");
        top.add(search, "Gapleft 30");
        top.add(jsearch, "Gapleft 30,growx,pushx,wrap");
        top.add(panel, "gapleft 30,push,grow,spanx");

        view = new FredButton("View Alumni Details");
        view.addActionListener(this);

        programfilter = new FredCombo("Filter By Progarm");
        search.setIcon(new ImageIcon("C:\\SmartFinanceSoftware\\icons/search-icon.png"));

        bottom.add(print, "growx,pushx,gapleft 30");
        bottom.add(clearancer, "growx,pushx,gapleft 30");
        bottom.add(bookhistory, "growx,pushx,gapleft 30");
        bottom.add(view, "growx,pushx,gapleft 30,wrap");

        bottom.add(back, "growx,pushx,gapleft 30");
        bottom.add(genderfilter, "growx,pushx,gapleft 30");
        bottom.add(yearfilter, "growx,pushx,gapleft 30");

        bottom.add(clear, "growx,pushx,gapleft 30");
        try {

            int counter = 0;
            String sql = "Select admnumber,yearofcompletion from admission,completionclasslists where currentform like '" + "AL" + "%' and admissionnumber=admnumber group by admissionnumber order by admNumber";
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                counter++;
                row[0] = counter;
                row[1] = Globals.fullName(rs.getString("admnumber"));
                row[2] = rs.getString("admnumber");

                row[3] = rs.getInt("YearofCompletion");
                model.addRow(row);

            }
            int programconter = 0;


        } catch (SQLException sq) {
            sq.printStackTrace();
            JOptionPane.showMessageDialog(holder, sq.getMessage());
        }
        bookhistory.addActionListener(this);
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
                        String sql = "Select firstname,middlename,lastname,currentform,currentstream,admissionnumber,program from admission where admissionnumber like '" + jsearch.getText() + "%' or firstname like '" + jsearch.getText() + "%' or middlename like '" + jsearch.getText() + "%' or lastname like '" + jsearch.getText() + "%' and currentform like '" + "AL" + "%' " + Globals.sortcode + " ";

                        ps = con.prepareStatement(sql);
                        rs = ps.executeQuery();
                        while (rs.next()) {
                            counter++;
                            row[0] = counter;
                            row[1] = rs.getString("FirstName") + "     " + rs.getString("MiddleName") + "     " + rs.getString("LastName");
                            row[2] = rs.getString("AdmissionNumber");
                            adm = rs.getString("AdmissionNumber");

                            if (rs.getString("CurrentForm").startsWith("AL")) {
                                String sql2 = "Select yearofcompletion from completionclasslists where admnumber='" + adm + "'";
                                ps = con.prepareStatement(sql2);
                                ResultSet RS = ps.executeQuery();
                                if (RS.next()) {
                                    row[3] = RS.getInt("Yearofcompletion");
                                    model.addRow(row);
                                }

                            }


                        }
                    } catch (SQLException sq) {
                        sq.printStackTrace();
                        JOptionPane.showMessageDialog(holder, sq.getMessage());

                    }
                }
            }
        });


        genderfilter.addActionListener(this);

        programfilter.addActionListener(this);
        cancel2.addActionListener(this);
        print2.addActionListener(this);
        clearancer.addActionListener(this);
        yearfilter.addActionListener(this);

        clear.addActionListener(this);
        return holder;

    }

    @Override
    public void actionPerformed(ActionEvent e) {

        Object obj = e.getSource();
        if (obj == bookhistory) {
            if (tab.getSelectedRowCount() < 1) {
                JOptionPane.showMessageDialog(holder, "Select The Student To Generate His/Her Allocation History ");
            } else {
                int selectedrow = tab.getSelectedRow();
                String Name = model.getValueAt(selectedrow, 1).toString();
                String adm = model.getValueAt(selectedrow, 2).toString();
                dia2 = new JDialog();
                dia2.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
                dia2.setAlwaysOnTop(true);
                dia2.setSize(1000, 500);
                dia2.setTitle("Book Allocation History");
                dia2.setLocationRelativeTo(CurrentFrame.mainFrame());
                dia2.setLayout(new MigLayout());
                dia2.setIconImage(FrameProperties.icon());
                dia2.setVisible(true);
                information = new FredLabel("BOOK ALLOCATION HISTORY FOR " + Name);
                DefaultTableModel model2 = new DefaultTableModel() {

                    @Override
                    public boolean isCellEditable(int row, int col) {
                        return false;

                    }
                };

                JScrollPane pane2 = new JScrollPane(tab2);
                Object cols2[] = {"Book Name", "Book Title", "Book Serial", "Class", "Subject", "Type", "Date Issued", "Status"};
                Object row2[] = new Object[cols2.length];
                model2.setColumnIdentifiers(cols2);
                tab2.setModel(model2);
                dia2.add(information, "Gapleft 30,wrap");
                dia2.add(pane2, "grow,push,wrap");
                dia2.add(cancel2, "Gapleft 100,split");
                dia2.add(print2, "Gapleft 300");
                try {
                    String sql5 = "Select bookname,booktitle,alumnibooksrecord.bookserial,books.classcode,books.subjectcode,alumnibooksrecord.status,dateissued,type from Alumnibooksrecord,books where admnumber='" + adm + "' and books.bookserial=alumnibooksrecord.bookserial";
                    ps = con.prepareStatement(sql5);
                    rs = ps.executeQuery();
                    while (rs.next()) {
                        row2[0] = rs.getString("Bookname");
                        row2[1] = rs.getString("Booktitle");
                        row2[2] = rs.getString("Bookserial");
                        row2[3] = Globals.className(rs.getString("classcode"));
                        row2[4] = Globals.subjectName(rs.getString("subjectcode"));
                        row2[5] = rs.getString("type");
                        row2[6] = rs.getString("DateIssued");
                        row2[7] = rs.getString("Status");
                        model2.addRow(row2);

                    }


                } catch (SQLException sq) {
                    sq.printStackTrace();
                    JOptionPane.showMessageDialog(null, sq.getMessage());
                }


            }


        }
        if (obj == print2) {
            ReportGenerator.generateReport(information.getText(), "BookAllocationHistory", tab2);
        }
        if (obj == cancel2) {
            dia2.dispose();
            ;
        }
        if (obj == clear) {
            if (tab.getSelectedRowCount() < 1) {
                JOptionPane.showMessageDialog(holder, "Kindly Select The Student To Clear");
            } else {
                adm = model.getValueAt(tab.getSelectedRow(), 2).toString();
                dia = new JDialog();
                dia.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
                dia.setSize(600, 300);
                dia.setIconImage(FrameProperties.icon());
                dia.setLocationRelativeTo(holder);
                dia.setTitle("Clearance Selection");
                dia.setLayout(new MigLayout());
                dia.add(new FredLabel("Subject"), "gaptop 50,gapleft 50");
                dia.add(jsubject, "gaptop 50,gapleft 50,wrap");

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
                try {

                    if (Globals.Level.equalsIgnoreCase("Admin")) {
                        String sql2 = "Select * from subjects";
                        ps = con.prepareStatement(sql2);
                        rs = ps.executeQuery();
                        while (rs.next()) {
                            jsubject.addItem(rs.getString("SubjectName"));
                        }
                    } else {
                        String sql2 = "Select * from subjectrights where teachercode='" + Globals.empcode + "'  and status='" + "true" + "' group by subjectcode";
                        ps = con.prepareStatement(sql2);
                        rs = ps.executeQuery();
                        while (rs.next()) {
                            jsubject.addItem(Globals.subjectName(rs.getString("Subjectcode")));
                        }
                    }

                } catch (SQLException sq) {
                    sq.printStackTrace();
                    JOptionPane.showMessageDialog(null, sq.getMessage());
                }

                button1.addActionListener((ActionEvent e1) -> {
                    if (jsubject.getSelectedIndex() == 0) {
                        JOptionPane.showMessageDialog(holder, "Kindly Select The Subject To Clear");
                    } else {
                        try {
                            String bookname = "";
                            String status = "cleared";
                            String bkserial = "";
                            String subcode = Globals.subjectCode(jsubject.getSelectedItem().toString());
                            String sql1 = "Select * from books where subjectcode='" + subcode + "' ";
                            ps = con.prepareStatement(sql1);
                            ResultSet RS = ps.executeQuery();
                            while (RS.next()) {
                                bookname = RS.getString("bookname");
                                bkserial = RS.getString("Bookserial");
                                String sql2 = "Select status from alumnibooksrecord where status!='" + "Returned" + "'  and admnumber='" + adm + "'  and bookserial='" + bkserial + "'";
                                ps = con.prepareStatement(sql2);
                                ResultSet res = ps.executeQuery();
                                if (res.next()) {
                                    status = "Not Cleared";
                                    break;
                                }


                            }
                            if (status.equalsIgnoreCase("Not Cleared")) {
                                JOptionPane.showMessageDialog(holder, "Student Not Eligible For Clearance \n" + bookname + "  " + "Serial Number: " + bkserial + " Not Returned", "Failed To Clear", JOptionPane.INFORMATION_MESSAGE);
                            } else {
                                String querry = "Delete from studentsubjectclearance where admnumber='" + adm + "' and subjectcode='" + subcode + "'";
                                ps = con.prepareStatement(querry);
                                ps.execute();
                                String sql = "Insert into studentsubjectclearance values('" + adm + "','" + subcode + "',curdate(),'" + "Cleared" + "')";
                                ps = con.prepareStatement(sql);
                                ps.execute();
                                JOptionPane.showMessageDialog(holder, "Student Cleared Successfully");
                            }
                        } catch (HeadlessException | SQLException sq) {
                            sq.printStackTrace();
                            JOptionPane.showMessageDialog(null, sq.getMessage());
                        }

                    }
                });

            }
        }
        if (obj == clearancer) {
            if (tab.getSelectedRowCount() < 1) {
                JOptionPane.showMessageDialog(holder, "Select The Student To Gnerate His/Her Clearance Roport");
            } else {
                adm = model.getValueAt(tab.getSelectedRow(), 2).toString();
                ClearanceReportForm.AlumniReport(adm, adm + " clearanceform");
            }
        } else if (obj == yearfilter) {
            if (yearfilter.getSelectedIndex() == 0) {

            } else {

                if (genderfilter.getSelectedIndex() == 0) {

                    while (model.getRowCount() > 0) {
                        model.removeRow(0);
                    }

                    try {
                        infor.setText(" ALUMNI CLASS OF " + yearfilter.getSelectedItem());
                        int counter = 0;
                        String sql = "Select * from admission, completionclasslists where currentform like '" + "AL" + "%' and Yearofcompletion='" + yearfilter.getSelectedItem() + "' and admissionnumber=admnumber " + Globals.sortcode + " ";
                        ps = con.prepareStatement(sql);
                        rs = ps.executeQuery();
                        while (rs.next()) {
                            counter++;
                            row[0] = counter;
                            row[1] = rs.getString("FirstName") + "     " + rs.getString("MiddleName") + "     " + rs.getString("LastName");
                            row[2] = rs.getString("AdmissionNumber");

                            row[3] = rs.getInt("yearofcompletion");
                            model.addRow(row);

                        }

                    } catch (SQLException sq) {
                        JOptionPane.showMessageDialog(holder, sq.getMessage());
                    }


                } else {

                    String sex = "";
                    if (genderfilter.getSelectedIndex() == 1) {
                        sex = "BOYS";
                    } else if (genderfilter.getSelectedIndex() == 2) {
                        sex = "GIRLS";
                    }

                    while (model.getRowCount() > 0) {
                        model.removeRow(0);
                    }

                    try {
                        infor.setText(sex + " CLASS OF " + yearfilter.getSelectedItem());
                        int counter = 0;
                        String sql = "Select * from admission, completionclasslists where currentform like '" + "AL" + "%' and Yearofcompletion='" + yearfilter.getSelectedItem() + "'  and gender='" + genderfilter.getSelectedItem() + "'  and admissionnumber=admnumber " + Globals.sortcode + " ";
                        ps = con.prepareStatement(sql);
                        rs = ps.executeQuery();
                        while (rs.next()) {
                            counter++;
                            row[0] = counter;
                            row[1] = rs.getString("FirstName") + "     " + rs.getString("MiddleName") + "     " + rs.getString("LastName");
                            row[2] = rs.getString("AdmissionNumber");

                            row[3] = rs.getInt("yearofcompletion");
                            model.addRow(row);

                        }

                    } catch (SQLException sq) {
                        JOptionPane.showMessageDialog(holder, sq.getMessage());
                    }


                }
            }


        } else if (obj == genderfilter) {
            if (genderfilter.getSelectedIndex() == 0) {

                infor.setText(" ALUMNI LIST");
                while (model.getRowCount() > 0) {
                    model.removeRow(0);
                }

                try {

                    int counter = 0;

                    String sql = "Select * from admission where currentform like '" + "AL" + "%' " + Globals.sortcode + " ";
                    ps = con.prepareStatement(sql);
                    rs = ps.executeQuery();
                    while (rs.next()) {
                        counter++;
                        row[0] = counter;
                        row[1] = rs.getString("FirstName") + "     " + rs.getString("MiddleName") + "     " + rs.getString("LastName");
                        row[2] = rs.getString("AdmissionNumber");

                        row[3] = rs.getString("program");
                        model.addRow(row);

                    }

                } catch (SQLException sq) {
                    JOptionPane.showMessageDialog(holder, sq.getMessage());
                }


            } else {

                String sex = "";
                if (genderfilter.getSelectedIndex() == 1) {
                    sex = "BOYS";
                } else if (genderfilter.getSelectedIndex() == 2) {
                    sex = "GIRLS";
                }

                while (model.getRowCount() > 0) {
                    model.removeRow(0);
                }

                try {
                    infor.setText(sex + " ALUMNI");
                    int counter = 0;
                    String sql = "Select * from admission,completionclasslist where currentform like '" + "AL" + "%' and gender='" + genderfilter.getSelectedItem() + "' and admissionnumber=admnumber " + Globals.sortcode + " ";
                    ps = con.prepareStatement(sql);
                    rs = ps.executeQuery();
                    while (rs.next()) {
                        counter++;
                        row[0] = counter;
                        row[1] = rs.getString("FirstName") + "     " + rs.getString("MiddleName") + "     " + rs.getString("LastName");
                        row[2] = rs.getString("AdmissionNumber");

                        row[3] = rs.getString("yearofcompletion");

                        model.addRow(row);

                    }

                } catch (SQLException sq) {
                    JOptionPane.showMessageDialog(holder, sq.getMessage());
                }


            }
        }
        if (obj == print) {
            ReportGenerator.generateReport(infor.getText(), "Studentlist", tab);
        }

        if (obj == view) {
            if (tab.getSelectedRowCount() < 1) {
                JOptionPane.showMessageDialog(holder, "Kindly Select The Student To View His Or Her Details");
            } else {


                admno1 = new FredLabel("admission Number");
                admno1.setForeground(Color.red);
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
                program1 = new FredLabel();
                program1.setForeground(Color.red);
                county1 = new FredLabel();
                county1.setForeground(Color.red);
                country1 = new FredLabel();
                country1.setForeground(Color.red);
                province1 = new FredLabel();
                province1.setForeground(Color.red);

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

                pane = new JPanel();
                pane2 = new JPanel();
                pane2.setBackground(Color.WHITE);
                im = new FredLabel();

                cancel = new FredButton("Close");

                top.add(pane, "push,grow");
                back.setVisible(true);
                pane.setBorder(new TitledBorder("Student Details"));

                pane2.setBorder(new TitledBorder("Student Photo"));
                pane.setLayout(new MigLayout());
                pane.add(admno, "growx,pushx,gaptop 10");
                pane.add(admno1, "growx,pushx,gaptop 10");
                pane.add(program, "growx,pushx,gapleft 30,gaptop 10");
                pane.add(program1, "growx,pushx,wrap,gaptop 10");
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
                pane.add(cstraeam1, "gaptop 10,growx,pushx,wrap");
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
                        fname1.setText(rs.getString("FirstName"));
                        mname1.setText(rs.getString("middleName"));
                        lname1.setText(rs.getString("lastName"));
                        admno1.setText(rs.getString("admissionNumber"));
                        gender1.setText(rs.getString("Gender"));
                        doa1.setText(rs.getString("DateofAdmission"));
                        dob1.setText(rs.getString("DateOfBirth"));
                        cform1.setText(Globals.className(rs.getString("CurrentForm")));

                        fma1.setText(Globals.className(rs.getString("classcode")));
                        sta1.setText(Globals.streamName(rs.getString("streamAdmitted")));

                        cterm1.setText(Globals.termname(rs.getString("currentterm")));
                        tea1.setText(Globals.termname(rs.getString("termadmitted")));
                        cstraeam1.setText(Globals.streamName(rs.getString("currentstream")));
                        parentname1.setText(rs.getString("parentfullNames"));
                        tel11.setText(rs.getString("telephone1"));
                        tel21.setText(rs.getString("telephone2"));
                        program1.setText(rs.getString("program"));
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
