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
import javax.swing.ImageIcon;
import javax.swing.JDialog;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import static javax.swing.WindowConstants.DISPOSE_ON_CLOSE;

import javax.swing.table.DefaultTableModel;

import net.miginfocom.swing.MigLayout;

/**
 * @author FRED
 */
public class AllBooks implements ActionListener {


    private JPanel holder;
    private JTable tab;
    private DefaultTableModel model;
    private FredLabel infor;
    private PreparedStatement ps;
    private ResultSet rs;
    private FredLabel search = new FredLabel("Search Book");
    private FredTextField jsearch = new FredTextField();
    private Connection con;
    private FredButton print = new FredButton("Export To Pdf");
    private FredButton details = new FredButton("View Book Details");
    private FredButton delete = new FredButton("Delete Book");
    private FredButton edit = new FredButton("Edit Book Details");
    private FredButton issuepattern = new FredButton("View Book Issue Pattern");
    private FredCombo classfilter, subjectfilter, booktypefilter;
    private Object col[] = {"No", "Book Name", "Book Title", "Book Serial No."};
    private Object row[] = new Object[col.length];

    public AllBooks() {

    }

    public JPanel AllBooksPanel() {
        con = DbConnection.connectDb();
        holder = new JPanel();
        JPanel top = new JPanel();
        JPanel bottom = new JPanel();

        booktypefilter = new FredCombo("Filter Book By Type");
        infor = new FredLabel("ALL BOOKS");
        subjectfilter = new FredCombo("Filter By Subject");
        classfilter = new FredCombo("Filter Books By Class");
        top.setLayout(new MigLayout());
        bottom.setLayout(new MigLayout());
        holder.setLayout(new MigLayout());
        holder.add(top, "grow,push,wrap");
        holder.add(bottom, "growx,pushx");
        bottom.setBackground(Color.WHITE);
        search.setIcon(new ImageIcon("C:\\SmartFinanceSoftware\\icons/search-icon.png"));
        model = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int col) {
                return false;
            }
        };
        tab = new JTable();
        for (int i = 0; i < row.length; ++i) {
            row[i] = "";
        }
        tab.setModel(model);
        JScrollPane pane = new JScrollPane(tab);
        model.setColumnIdentifiers(col);
        top.add(infor, "growx,pushx");
        top.add(search, "growx,pushx");
        top.add(jsearch, "growx,pushx,wrap");
        top.add(pane, "grow,push,span");
        bottom.add(classfilter, "growx,pushx,gapleft 20");
        bottom.add(subjectfilter, "growx,pushx,gapleft 20");
        bottom.add(booktypefilter, "growx,pushx,gapleft 20");
        bottom.add(print, "growx,pushx,gapleft 20,wrap");
        bottom.add(details, "growx,pushx,gapleft 20");
        bottom.add(delete, "growx,pushx,gapleft 20");
        bottom.add(edit, "growx,pushx,gapleft 20");
        bottom.add(issuepattern, "growx,pushx,gapleft 20");
        try {

            if (Globals.Level.equalsIgnoreCase("Admin")) {
                String sql = "Select * from classes  where  precision1<5 order by precision1 asc";

                ps = con.prepareStatement(sql);
                rs = ps.executeQuery();
                while (rs.next()) {
                    classfilter.addItem(rs.getString("ClassName"));

                }

                String sql2 = "Select * from subjects";
                ps = con.prepareStatement(sql2);
                rs = ps.executeQuery();
                while (rs.next()) {
                    subjectfilter.addItem(rs.getString("SubjectName"));
                }
                int counter = 1;
                String sql3 = "Select books.bookserial,bookname,booktitle from books  order by bookname";
                ps = con.prepareStatement(sql3);
                rs = ps.executeQuery();
                while (rs.next()) {
                    row[0] = counter;
                    row[1] = rs.getString("bookname");
                    row[2] = rs.getString("booktitle");
                    row[3] = rs.getString("bookserial");


                    model.addRow(row);
                    counter++;
                }


            } else {
                String sql = "Select * from classes,subjectrights where classes.classcode=subjectrights.classcode  group by subjectrights.classcode order by precision1 asc ";

                ps = con.prepareStatement(sql);
                rs = ps.executeQuery();
                while (rs.next()) {
                    classfilter.addItem(rs.getString("ClassName"));

                }
                String sql2 = "Select * from subjectrights where teachercode='" + Globals.empcode + "' group by subjectcode";
                ps = con.prepareStatement(sql2);
                rs = ps.executeQuery();
                while (rs.next()) {
                    subjectfilter.addItem(Globals.subjectName(rs.getString("Subjectcode")));
                }
                int counter = 1;
                String sql3 = "Select books.bookserial,bookname,subjectcode,classcode,booktitle from books  order by bookname";
                ps = con.prepareStatement(sql3);
                rs = ps.executeQuery();
                while (rs.next()) {
                    String subcode = rs.getString("Subjectcode");
                    String classcode = rs.getString("classcode");
                    row[0] = counter;
                    row[1] = rs.getString("bookname");
                    row[2] = rs.getString("booktitle");
                    row[3] = rs.getString("bookserial");

                    String sql5 = "Select subjectcode from subjectrights where subjectcode='" + subcode + "' and classcode='" + classcode + "' and teachercode='" + Globals.empcode + "'";
                    ps = con.prepareStatement(sql5);
                    ResultSet RS = ps.executeQuery();
                    if (RS.next()) {
                        model.addRow(row);
                        counter++;
                    }


                }


            }

        } catch (SQLException sq) {
            JOptionPane.showMessageDialog(null, sq.getMessage());
            sq.printStackTrace();
        }

        booktypefilter.addItem("Course Book");
        booktypefilter.addItem("Revision Book");
        booktypefilter.addItem("Novel Book");
        booktypefilter.addItem("Atlas Book");
        booktypefilter.addItem("Others");
        jsearch.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {

                try {
                    if (Globals.Level.equalsIgnoreCase("Admin")) {
                        while (model.getRowCount() > 0) {
                            model.removeRow(0);
                        }

                        int counter = 1;
                        String sql3 = "Select books.bookserial,bookname,booktitle from books order by bookname";
                        ps = con.prepareStatement(sql3);
                        rs = ps.executeQuery();
                        while (rs.next()) {

                            row[0] = counter;
                            row[1] = rs.getString("bookname");
                            row[2] = rs.getString("booktitle");
                            row[3] = rs.getString("bookserial");


                            if (rs.getString("bookname").toUpperCase().startsWith(jsearch.getText().toUpperCase()) || rs.getString("bookTitle").toUpperCase().startsWith(jsearch.getText().toUpperCase()) || rs.getString("bookSerial").toUpperCase().startsWith(jsearch.getText().toUpperCase())) {
                                model.addRow(row);
                                counter++;
                            }


                        }


                    } else {
                        while (model.getRowCount() > 0) {
                            model.removeRow(0);
                        }
                        int counter = 1;
                        String sql3 = "Select books.bookserial,bookname,subjectcode,classcode,booktitle from books order by bookname";
                        ps = con.prepareStatement(sql3);
                        rs = ps.executeQuery();
                        while (rs.next()) {
                            String subcode = rs.getString("Subjectcode");
                            String classcode = rs.getString("classcode");
                            row[0] = counter;
                            row[1] = rs.getString("bookname");
                            row[2] = rs.getString("booktitle");
                            row[3] = rs.getString("bookserial");


                            if (rs.getString("bookname").toUpperCase().startsWith(jsearch.getText().toUpperCase()) || rs.getString("bookTitle").toUpperCase().startsWith(jsearch.getText().toUpperCase()) || rs.getString("bookSerial").toUpperCase().startsWith(jsearch.getText().toUpperCase())) {
                                String sql5 = "Select subjectcode from subjectrights where subjectcode='" + subcode + "' and classcode='" + classcode + "' and teachercode='" + Globals.empcode + "'";
                                ps = con.prepareStatement(sql5);
                                ResultSet RS = ps.executeQuery();
                                if (RS.next()) {

                                    model.addRow(row);
                                    counter++;

                                }


                            }


                        }

                    }


                } catch (SQLException sq) {
                    JOptionPane.showMessageDialog(null, sq.getMessage());
                    sq.printStackTrace();
                }


            }


        });
        print.addActionListener(this);
        subjectfilter.addActionListener(this);
        classfilter.addActionListener(this);
        booktypefilter.addActionListener(this);
        delete.addActionListener(this);
        details.addActionListener(this);
        issuepattern.addActionListener(this);
        edit.addActionListener(this);
        return holder;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object obj = e.getSource();
        for (int i = 0; i < row.length; ++i) {
            row[i] = "";
        }
        if (obj == print) {
            ReportGenerator.generateReport(infor.getText(), "Issuedbooks", tab);
        } else if (obj == issuepattern) {
            if (tab.getSelectedRow() < 1) {
                JOptionPane.showMessageDialog(null, "Kindly Select The Book To View Its Book Issue Pattern");
            } else {
                String bookid = tab.getValueAt(tab.getSelectedRow(), 1).toString();


                JDialog dia = new JDialog();

                JTable tab2 = new JTable();
                JScrollPane pane2 = new JScrollPane(tab2);
                DefaultTableModel model2 = new DefaultTableModel();
                Object[] col = {"Book Serial No", "Book Title", "Status", "Date Issued", "Date Returned", "Posessing Student"};
                Object[] row = new Object[col.length];


            }
        }
        if (obj == edit) {
            if (tab.getSelectedRowCount() < 1) {
                JOptionPane.showMessageDialog(holder, "Select The Book To Edit From The Table Obove");
            } else {

                int selelcted = tab.getSelectedRow();
                String bkserial = model.getValueAt(selelcted, 3).toString();
                Globals.bookSerial = bkserial;
                CurrentFrame.setSecondFrame(new EditBook());
                CurrentFrame.mainFrame().setEnabled(false);
            }
        }

        if (obj == delete) {
            if (tab.getSelectedRowCount() < 1) {
                JOptionPane.showMessageDialog(holder, "Select The Book To Delete From The Table Obove");
            } else {
                int confirm = JOptionPane.showConfirmDialog(holder, "Are You Sure That You Want To Delete This book\n Deleting This Book Causes Total Loss Of \nBook Hitory allocation,Proceed With Deletion?", "Confirm Deletion", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.NO_OPTION) {
                    JOptionPane.showMessageDialog(holder, "Deletion Postponed");
                } else {
                    int selelcted = tab.getSelectedRow();
                    String bkserial = model.getValueAt(selelcted, 3).toString();
                    try {
                        String status = "";
                        String querry = "Select status from books where bookserial='" + bkserial + "'";
                        ps = con.prepareStatement(querry);
                        rs = ps.executeQuery();
                        if (rs.next()) {
                            status = rs.getString("Status");
                        }
                        if (status.equalsIgnoreCase("Issued")) {
                            JOptionPane.showMessageDialog(holder, "You Cannot Delete  A Book That Is Bieng Held By A Student");
                        } else {
                            String sql = "Delete from books where bookserial='" + bkserial + "'";
                            ps = con.prepareStatement(sql);
                            ps.execute();
                            JOptionPane.showMessageDialog(holder, "Book Delete Successfully");
                            model.removeRow(selelcted);
                        }

                    } catch (HeadlessException | SQLException sq) {
                        JOptionPane.showMessageDialog(null, sq.getMessage());
                        sq.printStackTrace();
                    }
                }
            }
        } else if (obj == details) {
            if (tab.getSelectedRowCount() < 1) {
                JOptionPane.showMessageDialog(holder, "Select The Book To View Its Details From The Table Obove");
            } else {
                int selelcted = tab.getSelectedRow();
                String bkserial = model.getValueAt(selelcted, 3).toString();

                JDialog dia = new JDialog();
                dia.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
                dia.setAlwaysOnTop(true);
                dia.setSize(800, 400);
                dia.setTitle("Book Information");
                dia.setLocationRelativeTo(CurrentFrame.mainFrame());
                dia.setLayout(new MigLayout());
                dia.setIconImage(FrameProperties.icon());
                dia.setVisible(true);
                FredLabel bookname, serial, subject, author, condition, title, datebought, classl, type, hh, status, dateissued;
                FredLabel bookname1, serial1, subject1, author1, condition1, title1, datebought1, classl1, type1, hh1, status1, dateissued1;
                author = new FredLabel("Author");
                bookname = new FredLabel("Book Name");
                title = new FredLabel("Book Title");
                serial = new FredLabel("Book Serial Number");
                condition = new FredLabel("Book   Condition");
                datebought = new FredLabel("Date Bought");
                classl = new FredLabel("Class");
                subject = new FredLabel("Subject");
                type = new FredLabel("Book Type");
                dateissued = new FredLabel("Date Issued");
                status = new FredLabel("Book Status");
                hh = new FredLabel("Holder");
                author1 = new FredLabel();
                bookname1 = new FredLabel();
                title1 = new FredLabel();
                serial1 = new FredLabel();
                condition1 = new FredLabel();
                datebought1 = new FredLabel();
                classl1 = new FredLabel();
                subject1 = new FredLabel();
                type1 = new FredLabel();
                status1 = new FredLabel();
                hh1 = new FredLabel();
                dateissued1 = new FredLabel();
                bookname1.setForeground(Color.red);
                title1.setForeground(Color.red);
                type1.setForeground(Color.red);
                author1.setForeground(Color.red);
                status1.setForeground(Color.red);
                subject1.setForeground(Color.red);
                classl1.setForeground(Color.red);
                datebought1.setForeground(Color.red);
                condition1.setForeground(Color.red);
                serial1.setForeground(Color.red);
                hh1.setForeground(Color.red);
                dateissued1.setForeground(Color.red);
                dia.add(bookname, "Gapleft 30,gaptop 30,growx,pushx");
                dia.add(bookname1, "Gapleft 30,gaptop 30,growx,pushx");
                dia.add(title, "Gapleft 30,gaptop 30,growx,pushx");
                dia.add(title1, "Gapleft 30,gaptop 30,growx,pushx,wrap");
                dia.add(condition, "Gapleft 30,gaptop 30,growx,pushx");
                dia.add(condition1, "Gapleft 30,gaptop 30,growx,pushx");
                dia.add(datebought, "Gapleft 30,gaptop 30,growx,pushx");
                dia.add(datebought1, "Gapleft 30,gaptop 30,growx,pushx,wrap");
                dia.add(subject, "Gapleft 30,gaptop 30,growx,pushx");
                dia.add(subject1, "Gapleft 30,gaptop 30,growx,pushx");
                dia.add(classl, "Gapleft 30,gaptop 30,growx,pushx");
                dia.add(classl1, "Gapleft 30,gaptop 30,growx,pushx,wrap");
                dia.add(type, "Gapleft 30,gaptop 30,growx,pushx");
                dia.add(type1, "Gapleft 30,gaptop 30,growx,pushx");
                dia.add(status, "Gapleft 30,gaptop 30,growx,pushx");
                dia.add(status1, "Gapleft 30,gaptop 30,growx,pushx,wrap");
                dia.add(hh, "Gapleft 30,gaptop 30,growx,pushx");
                dia.add(hh1, "Gapleft 30,gaptop 30,growx,pushx");
                dia.add(serial, "Gapleft 30,gaptop 30,growx,pushx");
                dia.add(serial1, "Gapleft 30,gaptop 30,wrap,growx,pushx");
                dia.add(dateissued, "Gapleft 30,gaptop 30,growx,pushx");
                dia.add(dateissued1, "Gapleft 30,gaptop 30,growx,pushx");
                dia.add(author, "Gapleft 30,gaptop 30,growx,pushx");
                dia.add(author1, "Gapleft 30,gaptop 30,growx,pushx");

                try {
                    String sql = "Select * from books where bookserial='" + bkserial + "'";
                    ps = con.prepareStatement(sql);
                    rs = ps.executeQuery();
                    if (rs.next()) {
                        bookname1.setText(rs.getString("bookname"));
                        title1.setText(rs.getString("Booktitle"));
                        condition1.setText(rs.getString("Condition"));
                        type1.setText(rs.getString("Type"));
                        classl1.setText(Globals.className(rs.getString("Classcode")));
                        subject1.setText(Globals.subjectName(rs.getString("Subjectcode")));
                        serial1.setText(rs.getString("Bookserial"));
                        status1.setText(rs.getString("Status"));
                        author1.setText(rs.getString("Author"));
                        datebought1.setText(rs.getString("Datebought"));
                        if (rs.getString("Status").equalsIgnoreCase("Issued")) {
                            String sql1 = "Select admnumber,dateissued from issuedbooks where bookserial='" + bkserial + "' and status!='" + "Returned" + "'";
                            ps = con.prepareStatement(sql1);
                            rs = ps.executeQuery();
                            if (rs.next()) {
                                hh1.setText(Globals.fullName(rs.getString("AdmNumber")));
                                dateissued1.setText(rs.getString("DateIssued"));
                            } else {
                                hh.setText("N/A");
                                dateissued1.setText("N/A");
                            }

                        }
                    }


                } catch (SQLException sq) {
                    JOptionPane.showMessageDialog(null, sq.getMessage());
                    JOptionPane.showMessageDialog(null, sq.getMessage());
                    sq.printStackTrace();
                }

            }
        }
        if (obj == subjectfilter) {
            if (Globals.Level.equalsIgnoreCase("Admin")) {
                while (model.getRowCount() > 0) {
                    model.removeRow(0);
                }
                try {
                    if (subjectfilter.getSelectedIndex() > 0 && classfilter.getSelectedIndex() == 0) {
                        infor.setText(subjectfilter.getSelectedItem() + " BOOKS ");
                        int counter = 1;
                        String sql3 = "Select books.bookserial,bookname,booktitle from books  where subjectcode='" + Globals.subjectCode(subjectfilter.getSelectedItem().toString()) + "'  order by bookname";
                        ps = con.prepareStatement(sql3);
                        rs = ps.executeQuery();
                        while (rs.next()) {
                            row[0] = counter;
                            String bkserial = rs.getString("bookserial");
                            ;
                            row[1] = rs.getString("bookname");
                            row[2] = rs.getString("booktitle");
                            row[3] = rs.getString("bookserial");

                            String querry = "select subjectcode from books where subjectcode='" + Globals.subjectCode(subjectfilter.getSelectedItem().toString()) + "' and bookserial='" + bkserial + "'";
                            ps = con.prepareStatement(querry);
                            ResultSet Rs = ps.executeQuery();
                            if (Rs.next()) {
                                model.addRow(row);
                                counter++;
                            }

                        }
                    } else if (subjectfilter.getSelectedIndex() > 0 && classfilter.getSelectedIndex() > 0) {
                        infor.setText(classfilter.getSelectedItem() + "  " + subjectfilter.getSelectedItem() + " BOOKS ");
                        int counter = 1;
                        String sql3 = "Select books.bookserial,bookname,booktitle from books where   subjectcode='" + Globals.subjectCode(subjectfilter.getSelectedItem().toString()) + "' order by bookname";
                        ps = con.prepareStatement(sql3);
                        rs = ps.executeQuery();
                        while (rs.next()) {
                            row[0] = counter;
                            row[1] = rs.getString("bookname");
                            row[2] = rs.getString("booktitle");
                            row[3] = rs.getString("bookserial");

                            String bkserial = rs.getString("bookserial");
                            String querry = "select subjectcode from books where subjectcode='" + Globals.subjectCode(subjectfilter.getSelectedItem().toString()) + "' and bookserial='" + bkserial + "' and classcode='" + Globals.classCode(classfilter.getSelectedItem().toString()) + "'";
                            ps = con.prepareStatement(querry);
                            ResultSet Rs = ps.executeQuery();
                            if (Rs.next()) {
                                model.addRow(row);
                                counter++;
                            }


                        }

                    }


                } catch (SQLException sq) {
                    JOptionPane.showMessageDialog(null, sq.getMessage());
                    sq.printStackTrace();
                    JOptionPane.showMessageDialog(null, sq.getMessage());
                }


            } else {

                while (model.getRowCount() > 0) {
                    model.removeRow(0);
                }
                if (subjectfilter.getSelectedIndex() > 0 && classfilter.getSelectedIndex() == 0) {

                    try {
                        infor.setText(subjectfilter.getSelectedItem() + " BOOKS (Limited View)");
                        int counter = 1;
                        String sql3 = "Select books.bookserial,bookname,subjectcode,classcode,booktitle from books where  subjectcode='" + Globals.subjectCode(subjectfilter.getSelectedItem().toString()) + "'   order by bookname";
                        ps = con.prepareStatement(sql3);
                        rs = ps.executeQuery();
                        while (rs.next()) {
                            String bkserial = rs.getString("bookserial");

                            row[0] = counter;
                            row[1] = rs.getString("bookname");
                            row[2] = rs.getString("booktitle");
                            row[3] = rs.getString("bookserial");


                            String classcode = rs.getString("classcode");
                            String subcode = rs.getString("Subjectcode");
                            String sql5 = "Select subjectrights.subjectcode from subjectrights where subjectrights.classcode='" + classcode + "' and subjectrights.subjectcode='" + Globals.subjectCode(subjectfilter.getSelectedItem().toString()) + "' and teachercode='" + Globals.empcode + "'";
                            ps = con.prepareStatement(sql5);
                            ResultSet RS = ps.executeQuery();
                            if (RS.next()) {
                                model.addRow(row);
                                counter++;
                            }


                        }

                    } catch (SQLException sq) {
                        sq.printStackTrace();
                        JOptionPane.showMessageDialog(null, sq.getMessage());
                    }

                } else if (subjectfilter.getSelectedIndex() > 0 && classfilter.getSelectedIndex() > 0) {
                    infor.setText(classfilter.getSelectedItem() + "  " + subjectfilter.getSelectedItem() + " BOOKS (Limited View)");
                    try {
                        int counter = 1;
                        String sql3 = "Select books.bookserial,bookname,subjectcode,classcode,booktitle from books where  subjectcode='" + Globals.subjectCode(subjectfilter.getSelectedItem().toString()) + "' and classcode='" + Globals.classCode(classfilter.getSelectedItem().toString()) + "'  order by bookname";
                        ps = con.prepareStatement(sql3);
                        rs = ps.executeQuery();
                        while (rs.next()) {
                            String bkserial = rs.getString("bookserial");
                            String subcode = rs.getString("Subjectcode");
                            String classcode = rs.getString("classcode");
                            row[0] = counter;
                            row[1] = rs.getString("bookname");
                            row[2] = rs.getString("booktitle");
                            row[3] = rs.getString("bookserial");


                            String sql6 = "Select subjectrights.subjectcode from subjectrights where subjectrights.classcode='" + Globals.classCode(classfilter.getSelectedItem().toString()) + "' and subjectrights.subjectcode='" + Globals.subjectCode(subjectfilter.getSelectedItem().toString()) + "' and teachercode='" + Globals.empcode + "'";
                            ps = con.prepareStatement(sql6);
                            ResultSet Rs = ps.executeQuery();
                            if (Rs.next()) {
                                model.addRow(row);
                                counter++;
                            }


                        }
                    } catch (SQLException sq) {
                        sq.printStackTrace();
                        JOptionPane.showMessageDialog(null, sq.getMessage());
                    }
                }


            }
        } else if (obj == classfilter) {

            if (Globals.Level.equalsIgnoreCase("Admin")) {
                while (model.getRowCount() > 0) {
                    model.removeRow(0);
                }
                try {
                    if (classfilter.getSelectedIndex() == 0) {
                        infor.setText("Books ".toUpperCase());
                        int counter = 1;
                        String sql3 = "Select books.bookserial,bookname,booktitle from books  order by bookname";
                        ps = con.prepareStatement(sql3);
                        rs = ps.executeQuery();
                        while (rs.next()) {

                            row[0] = counter;
                            row[1] = rs.getString("bookname");
                            row[2] = rs.getString("booktitle");
                            row[3] = rs.getString("bookserial");


                            if (rs.getString("bookname").toUpperCase().startsWith(jsearch.getText().toUpperCase()) || rs.getString("bookTitle").toUpperCase().startsWith(jsearch.getText().toUpperCase()) || rs.getString("bookSerial").toUpperCase().startsWith(jsearch.getText().toUpperCase())) {
                                model.addRow(row);
                                counter++;
                            }


                        }
                    } else {
                        infor.setText(classfilter.getSelectedItem() + " BOOKS");
                        int counter = 1;
                        String sql3 = "Select books.bookserial,bookname,booktitle from books where  classcode='" + Globals.classCode(classfilter.getSelectedItem().toString()) + "'   order by bookname";
                        ps = con.prepareStatement(sql3);
                        rs = ps.executeQuery();
                        while (rs.next()) {
                            row[0] = counter;
                            row[1] = rs.getString("bookname");
                            row[2] = rs.getString("booktitle");
                            row[3] = rs.getString("bookserial");


                            model.addRow(row);
                            counter++;


                        }
                    }

                } catch (SQLException sq) {
                    sq.printStackTrace();
                    JOptionPane.showMessageDialog(null, sq.getMessage());
                }


            } else {

                while (model.getRowCount() > 0) {
                    model.removeRow(0);
                }
                try {
                    if (classfilter.getSelectedIndex() == 0) {
                        infor.setText("BOOKS (Limited View)");

                        int counter = 1;
                        String sql3 = "Select books.bookserial,bookname,subjectcode,booktitle from books  order by bookname";
                        ps = con.prepareStatement(sql3);
                        rs = ps.executeQuery();
                        while (rs.next()) {
                            String subcode = rs.getString("Subjectcode");
                            String classcode = rs.getString("classcode");
                            row[0] = counter;
                            row[1] = rs.getString("bookname");
                            row[2] = rs.getString("booktitle");
                            row[3] = rs.getString("bookserial");

                            String sql5 = "Select subjectcode from subjectrights where subjectcode='" + subcode + "' and classcode='" + classcode + "' and teachercode='" + Globals.empcode + "'";
                            ps = con.prepareStatement(sql5);
                            ResultSet RS = ps.executeQuery();
                            if (RS.next()) {
                                model.addRow(row);
                                counter++;
                            }


                        }
                    } else {
                        infor.setText(classfilter.getSelectedItem() + " BOOKS (Limited View)");
                        int counter = 1;
                        String sql3 = "Select books.bookserial,bookname,subjectcode,classcode,booktitle from books where classcode='" + Globals.classCode(classfilter.getSelectedItem().toString()) + "' order by bookname";
                        ps = con.prepareStatement(sql3);
                        rs = ps.executeQuery();
                        while (rs.next()) {
                            String subcode = rs.getString("Subjectcode");
                            String classcode = rs.getString("classcode");
                            row[0] = counter;
                            row[1] = rs.getString("bookname");
                            row[2] = rs.getString("booktitle");
                            row[3] = rs.getString("bookserial");

                            String sql5 = "Select subjectcode from subjectrights where subjectcode='" + subcode + "' and classcode='" + Globals.classCode(classfilter.getSelectedItem().toString()) + "' and teachercode='" + Globals.empcode + "'";
                            ps = con.prepareStatement(sql5);
                            ResultSet RS = ps.executeQuery();
                            if (RS.next()) {
                                counter++;
                                model.addRow(row);
                            }


                        }

                    }
                } catch (SQLException sq) {
                    sq.printStackTrace();
                    JOptionPane.showMessageDialog(null, sq.getMessage());
                }

            }

        } else if (obj == booktypefilter) {
            try {
                if (Globals.Level.equalsIgnoreCase("Admin")) {
                    if (booktypefilter.getSelectedIndex() == 0) {
                        while (model.getRowCount() > 0) {
                            model.removeRow(0);
                        }
                        try {
                            if (subjectfilter.getSelectedIndex() > 0 && classfilter.getSelectedIndex() == 0) {
                                infor.setText(classfilter.getSelectedItem() + " BOOKS ");
                                int counter = 1;
                                String sql3 = "Select books.bookserial,bookname,booktitle from books where subjectcode='" + Globals.subjectCode(subjectfilter.getSelectedItem().toString()) + "' order by bookname";
                                ps = con.prepareStatement(sql3);
                                rs = ps.executeQuery();
                                while (rs.next()) {
                                    row[0] = counter;
                                    String bkserial = rs.getString("booktitle");
                                    ;
                                    row[1] = rs.getString("bookname");
                                    row[2] = rs.getString("booktitle");

                                    String querry = "select subjectcode from books where subjectcode='" + Globals.subjectCode(subjectfilter.getSelectedItem().toString()) + "' and bookserial='" + bkserial + "'";
                                    ps = con.prepareStatement(querry);
                                    ResultSet Rs = ps.executeQuery();
                                    if (Rs.next()) {
                                        model.addRow(row);
                                        counter++;
                                    }

                                }
                            } else if (subjectfilter.getSelectedIndex() > 0 && classfilter.getSelectedIndex() > 0) {
                                infor.setText(classfilter.getSelectedItem() + "  " + subjectfilter.getSelectedItem() + " BOOKS");
                                int counter = 1;
                                String sql3 = "Select books.bookserial,bookname,booktitle from books where  subjectcode='" + Globals.subjectCode(subjectfilter.getSelectedItem().toString()) + "' and classcode='" + Globals.classCode(classfilter.getSelectedItem().toString()) + "' order by bookname";
                                ps = con.prepareStatement(sql3);
                                rs = ps.executeQuery();
                                while (rs.next()) {
                                    row[0] = counter;
                                    row[1] = rs.getString("bookname");
                                    row[2] = rs.getString("booktitle");
                                    row[3] = rs.getString("bookserial");

                                    String bkserial = rs.getString("bookserial");
                                    String querry = "select subjectcode from books where subjectcode='" + Globals.subjectCode(subjectfilter.getSelectedItem().toString()) + "' and bookserial='" + bkserial + "' and classcode='" + Globals.classCode(classfilter.getSelectedItem().toString()) + "'";
                                    ps = con.prepareStatement(querry);
                                    ResultSet Rs = ps.executeQuery();
                                    if (Rs.next()) {
                                        model.addRow(row);
                                        counter++;
                                    }


                                }
                            }

                        } catch (SQLException sq) {
                            sq.printStackTrace();
                            JOptionPane.showMessageDialog(null, sq.getMessage());
                        }
                    } else {
                        while (model.getRowCount() > 0) {
                            model.removeRow(0);
                        }
                        try {
                            if (subjectfilter.getSelectedIndex() > 0 && classfilter.getSelectedIndex() == 0) {
                                infor.setText(subjectfilter.getSelectedItem() + "   " + booktypefilter.getSelectedItem().toString().toUpperCase() + "  ");
                                int counter = 1;
                                String sql3 = "Select books.bookserial,bookname,booktitle from books where   subjectcode='" + Globals.subjectCode(subjectfilter.getSelectedItem().toString()) + "'  and type='" + booktypefilter.getSelectedItem() + "' order by bookname";
                                ps = con.prepareStatement(sql3);
                                rs = ps.executeQuery();
                                while (rs.next()) {
                                    row[0] = counter;
                                    String bkserial = rs.getString("bookserial");
                                    row[1] = rs.getString("bookname");
                                    row[2] = rs.getString("booktitle");
                                    row[3] = rs.getString("bookserial");


                                    model.addRow(row);
                                    counter++;


                                }
                            } else if (subjectfilter.getSelectedIndex() > 0 && classfilter.getSelectedIndex() > 0) {
                                infor.setText(classfilter.getSelectedItem() + "  " + subjectfilter.getSelectedItem() + "  " + booktypefilter.getSelectedItem() + "S ");
                                int counter = 1;
                                String sql3 = "Select books.bookserial,bookname,booktitle from books where subjectcode='" + Globals.subjectCode(subjectfilter.getSelectedItem().toString()) + "'   and classcode='" + Globals.classCode(classfilter.getSelectedItem().toString()) + "' and type='" + booktypefilter.getSelectedItem() + "' order by bookname";
                                ps = con.prepareStatement(sql3);
                                rs = ps.executeQuery();
                                while (rs.next()) {
                                    row[0] = counter;
                                    row[1] = rs.getString("bookname");
                                    row[2] = rs.getString("booktitle");
                                    row[3] = rs.getString("bookserial");

                                    model.addRow(row);
                                    counter++;


                                }
                            } else if (classfilter.getSelectedIndex() == 0 && subjectfilter.getSelectedIndex() == 0) {
                                infor.setText(booktypefilter.getSelectedItem().toString().toUpperCase() + "S ");

                                int counter = 1;
                                String sql3 = "Select books.bookserial,bookname,booktitle from books where   type='" + booktypefilter.getSelectedItem() + "' order by bookname";
                                ps = con.prepareStatement(sql3);
                                rs = ps.executeQuery();
                                while (rs.next()) {

                                    row[0] = counter;
                                    row[1] = rs.getString("bookname");
                                    row[2] = rs.getString("booktitle");
                                    row[3] = rs.getString("bookserial");

                                    model.addRow(row);
                                    counter++;


                                }
                            } else if (classfilter.getSelectedIndex() > 0 && subjectfilter.getSelectedIndex() == 0) {
                                infor.setText(classfilter.getSelectedItem() + "   " + booktypefilter.getSelectedItem().toString().toUpperCase() + " ");
                                int counter = 1;
                                String sql3 = "Select books.bookserial,bookname,booktitle from books where  classcode='" + Globals.classCode(classfilter.getSelectedItem().toString()) + "' and type='" + booktypefilter.getSelectedItem() + "' order by bookname";
                                ps = con.prepareStatement(sql3);
                                rs = ps.executeQuery();
                                while (rs.next()) {
                                    row[0] = counter;
                                    String bkserial = rs.getString("bookserial");
                                    row[1] = rs.getString("bookname");
                                    row[2] = rs.getString("booktitle");
                                    row[3] = rs.getString("bookserial");

                                    model.addRow(row);
                                    counter++;


                                }
                            }

                        } catch (SQLException sq) {
                            sq.printStackTrace();
                            JOptionPane.showMessageDialog(null, sq.getMessage());
                        }

                    }


                } else {

                    if (booktypefilter.getSelectedIndex() == 0) {

                        if (subjectfilter.getSelectedIndex() > 0 && classfilter.getSelectedIndex() == 0) {

                            while (model.getRowCount() > 0) {
                                model.removeRow(0);
                            }
                            try {
                                if (classfilter.getSelectedIndex() == 0) {
                                    infor.setText(" BOOKS (Limited View)");

                                    int counter = 1;
                                    String sql3 = "Select books.bookserial,bookname,subjectcode,classcode,booktitle from books    order by bookname";
                                    ps = con.prepareStatement(sql3);
                                    rs = ps.executeQuery();
                                    while (rs.next()) {
                                        String subcode = rs.getString("Subjectcode");
                                        String classcode = rs.getString("classcode");
                                        row[0] = counter;
                                        row[1] = rs.getString("bookname");
                                        row[2] = rs.getString("booktitle");
                                        row[3] = rs.getString("bookserial");

                                        String sql5 = "Select subjectcode from subjectrights where subjectcode='" + subcode + "' and classcode='" + classcode + "' and teachercode='" + Globals.empcode + "'";
                                        ps = con.prepareStatement(sql5);
                                        ResultSet RS = ps.executeQuery();
                                        if (RS.next()) {
                                            model.addRow(row);
                                            counter++;
                                        }


                                    }
                                } else {
                                    infor.setText(classfilter.getSelectedItem() + " BOOKS (Limited View)");
                                    int counter = 1;
                                    String sql3 = "Select books.bookserial,bookname,subjectcode,classcode,admnumber,dateissued,booktitle from books where  classcode='" + Globals.classCode(classfilter.getSelectedItem().toString()) + "'  order by bookname";
                                    ps = con.prepareStatement(sql3);
                                    rs = ps.executeQuery();
                                    while (rs.next()) {
                                        String subcode = rs.getString("Subjectcode");
                                        String classcode = rs.getString("classcode");
                                        row[0] = counter;
                                        row[1] = rs.getString("bookname");
                                        row[2] = rs.getString("booktitle");
                                        row[3] = rs.getString("bookserial");
                                        row[4] = rs.getString("dateissued");
                                        row[5] = Globals.fullName(rs.getString("admnumber"));
                                        row[6] = rs.getString("admnumber");
                                        String sql5 = "Select subjectcode from subjectrights where subjectcode='" + subcode + "' and classcode='" + Globals.classCode(classfilter.getSelectedItem().toString()) + "' and teachercode='" + Globals.empcode + "'";
                                        ps = con.prepareStatement(sql5);
                                        ResultSet RS = ps.executeQuery();
                                        if (RS.next()) {
                                            model.addRow(row);
                                        }

                                        counter++;

                                    }

                                }
                            } catch (SQLException sq) {
                                sq.printStackTrace();
                                JOptionPane.showMessageDialog(null, sq.getMessage());
                            }

                        }

                    } else {

                        while (model.getRowCount() > 0) {
                            model.removeRow(0);
                        }
                        if (subjectfilter.getSelectedIndex() > 0 && classfilter.getSelectedIndex() == 0) {

                            try {
                                infor.setText(subjectfilter.getSelectedItem() + booktypefilter.getSelectedItem().toString().toUpperCase() + " (Limited View)");
                                int counter = 1;
                                String sql3 = "Select books.bookserial,classcode,bookname,subjectcode,classcode,booktitle from books where    subjectcode='" + Globals.subjectCode(subjectfilter.getSelectedItem().toString()) + "'  and type='" + booktypefilter.getSelectedItem() + "' order by bookname";
                                ps = con.prepareStatement(sql3);
                                rs = ps.executeQuery();
                                while (rs.next()) {
                                    String bkserial = rs.getString("bookserial");

                                    row[0] = counter;
                                    row[1] = rs.getString("bookname");
                                    row[2] = rs.getString("booktitle");
                                    row[3] = rs.getString("bookserial");


                                    ResultSet Rs = null;

                                    String classcode = rs.getString("classcode");
                                    String subcode = rs.getString("Subjectcode");
                                    String sql5 = "Select subjectrights.subjectcode from subjectrights where subjectrights.classcode='" + classcode + "' and subjectrights.subjectcode='" + Globals.subjectCode(subjectfilter.getSelectedItem().toString()) + "' and teachercode='" + Globals.empcode + "'";
                                    ps = con.prepareStatement(sql5);
                                    ResultSet RS = ps.executeQuery();
                                    if (RS.next()) {
                                        model.addRow(row);
                                        counter++;
                                    }


                                }

                            } catch (SQLException sq) {
                                sq.printStackTrace();
                                JOptionPane.showMessageDialog(null, sq.getMessage());
                            }

                        } else if (subjectfilter.getSelectedIndex() > 0 && classfilter.getSelectedIndex() > 0) {
                            infor.setText(classfilter.getSelectedItem() + "  " + subjectfilter.getSelectedItem() + " " + booktypefilter.getSelectedItem().toString().toUpperCase() + " (Limited View)");
                            try {
                                int counter = 1;
                                String sql3 = "Select books.bookserial,type,bookname,subjectcode,classcode,booktitle from books where subjectcode='" + Globals.subjectCode(subjectfilter.getSelectedItem().toString()) + "' and type='" + booktypefilter.getSelectedItem() + "'    order by bookname";
                                ps = con.prepareStatement(sql3);
                                rs = ps.executeQuery();
                                while (rs.next()) {
                                    String type = rs.getString("Type");
                                    String subcode = rs.getString("Subjectcode");
                                    String classcode = rs.getString("classcode");
                                    String bkserial = rs.getString("bookserial");
                                    row[0] = counter;
                                    row[1] = rs.getString("bookname");
                                    row[2] = rs.getString("booktitle");
                                    row[3] = rs.getString("bookserial");

                                    ResultSet Rs = null;

                                    String sql6 = "Select subjectrights.subjectcode from subjectrights where subjectrights.classcode='" + classcode + "' and subjectrights.subjectcode='" + Globals.subjectCode(subjectfilter.getSelectedItem().toString()) + "' and teachercode='" + Globals.empcode + "'";
                                    ps = con.prepareStatement(sql6);
                                    Rs = ps.executeQuery();
                                    if (Rs.next()) {

                                        model.addRow(row);
                                        counter++;


                                    }

                                    counter++;

                                }
                            } catch (SQLException sq) {
                                sq.printStackTrace();
                                JOptionPane.showMessageDialog(null, sq.getMessage());
                            }
                        }


                    }


                }

            } catch (Exception sq) {
                sq.printStackTrace();
                JOptionPane.showMessageDialog(null, sq.getMessage());

            }
        }


    }

}
