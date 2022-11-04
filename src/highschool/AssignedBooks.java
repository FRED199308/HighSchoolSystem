/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package highschool;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.ImageIcon;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import javax.swing.table.DefaultTableModel;

import net.miginfocom.swing.MigLayout;

/**
 * @author FRED
 */
public class AssignedBooks implements ActionListener {


    private JPanel bottom;
    private JPanel top;
    private JPanel holder;
    private JTable tab;
    private DefaultTableModel model;
    private JScrollPane pane;
    private FredLabel infor;
    private PreparedStatement ps;
    private ResultSet rs;
    private FredLabel search = new FredLabel("Search Book");
    private FredTextField jsearch = new FredTextField();
    private Connection con = DbConnection.connectDb();
    private FredButton print = new FredButton("Export To Pdf");
    private FredCombo classfilter, subjectfilter, booktypefilter;
    private Object col[] = {"No", "Book Name", "Book Title", "Book Serial No.", "Date Issued", "Book Holder(Student name)", "admission NO."};
    private Object row[] = new Object[col.length];

    public JPanel assignedBooksPanel() {
        holder = new JPanel();
        top = new JPanel();
        bottom = new JPanel();
        booktypefilter = new FredCombo("Filter Book By Type");
        infor = new FredLabel("Issued Books");
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
        pane = new JScrollPane(tab);
        model.setColumnIdentifiers(col);
        top.add(infor, "growx,pushx");
        top.add(search, "growx,pushx");
        top.add(jsearch, "growx,pushx,wrap");
        top.add(pane, "grow,push,span");
        bottom.add(classfilter, "growx,pushx,gapleft 20");
        bottom.add(subjectfilter, "growx,pushx,gapleft 20");
        bottom.add(booktypefilter, "growx,pushx,gapleft 20");
        bottom.add(print, "growx,pushx,gapleft 20");
        try {

            if (Globals.Level.equalsIgnoreCase("Admin")) {
                String sql = "Select * from classes  where  precision1<5 order by precision1 asc";
                con = DbConnection.connectDb();
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
                String sql3 = "Select books.bookserial,bookname,admnumber,dateissued,booktitle from issuedbooks,books where books.bookserial=issuedbooks.bookserial and books.status='" + "ISSUED" + "' and issuedbooks.status!='" + "Returned" + "' order by bookname";
                ps = con.prepareStatement(sql3);
                rs = ps.executeQuery();
                while (rs.next()) {
                    row[0] = counter;
                    row[1] = rs.getString("bookname");
                    row[2] = rs.getString("booktitle");
                    row[3] = rs.getString("bookserial");
                    row[4] = rs.getString("dateissued");
                    row[5] = Globals.fullName(rs.getString("admnumber"));
                    row[6] = rs.getString("admnumber");
                    model.addRow(row);
                    counter++;
                }


            } else {
                String sql = "Select * from classes,subjectrights where classes.classcode=subjectrights.classcode  group by subjectrights.classcode order by precision1 asc ";
                con = DbConnection.connectDb();
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
                String sql3 = "Select books.bookserial,bookname,subjectcode,classcode,admnumber,dateissued,booktitle from issuedbooks,books where books.bookserial=issuedbooks.bookserial and books.status='" + "ISSUED" + "'  and issuedbooks.status!='" + "Returned" + "' order by bookname";
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
            sq.printStackTrace();
            JOptionPane.showMessageDialog(null, sq.getMessage());
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
                        String sql3 = "Select books.bookserial,bookname,admnumber,dateissued,booktitle from issuedbooks,books where books.bookserial=issuedbooks.bookserial and books.status='" + "ISSUED" + "' and issuedbooks.status!='" + "Returned" + "' order by bookname";
                        ps = con.prepareStatement(sql3);
                        rs = ps.executeQuery();
                        while (rs.next()) {

                            row[0] = counter;
                            row[1] = rs.getString("bookname");
                            row[2] = rs.getString("booktitle");
                            row[3] = rs.getString("bookserial");
                            row[4] = rs.getString("dateissued");
                            row[5] = Globals.fullName(rs.getString("admnumber"));
                            row[6] = rs.getString("admnumber");
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
                        String sql3 = "Select books.bookserial,bookname,subjectcode,classcode,admnumber,dateissued,booktitle from issuedbooks,books where books.bookserial=issuedbooks.bookserial and books.status='" + "ISSUED" + "'  and issuedbooks.status!='" + "Returned" + "' order by bookname";
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
                    sq.printStackTrace();
                    JOptionPane.showMessageDialog(null, sq.getMessage());
                }


            }


        });
        print.addActionListener(this);
        subjectfilter.addActionListener(this);
        classfilter.addActionListener(this);
        booktypefilter.addActionListener(this);
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
        }
        if (obj == subjectfilter) {
            if (Globals.Level.equalsIgnoreCase("Admin")) {
                while (model.getRowCount() > 0) {
                    model.removeRow(0);
                }
                try {
                    if (subjectfilter.getSelectedIndex() > 0 && classfilter.getSelectedIndex() == 0) {
                        infor.setText(subjectfilter.getSelectedItem() + " BOOKS ISSUED");
                        int counter = 1;
                        String sql3 = "Select books.bookserial,bookname,admnumber,dateissued,booktitle from issuedbooks,books where books.bookserial=issuedbooks.bookserial and books.status='" + "ISSUED" + "' and issuedbooks.status!='" + "Returned" + "' order by bookname";
                        ps = con.prepareStatement(sql3);
                        rs = ps.executeQuery();
                        while (rs.next()) {
                            row[0] = counter;
                            String bkserial = rs.getString("bookserial");
                            ;
                            row[1] = rs.getString("bookname");
                            row[2] = rs.getString("booktitle");
                            row[3] = rs.getString("bookserial");
                            row[4] = rs.getString("dateissued");
                            row[5] = Globals.fullName(rs.getString("admnumber"));
                            row[6] = rs.getString("admnumber");
                            String querry = "select subjectcode from books where subjectcode='" + Globals.subjectCode(subjectfilter.getSelectedItem().toString()) + "' and bookserial='" + bkserial + "'";
                            ps = con.prepareStatement(querry);
                            ResultSet Rs = ps.executeQuery();
                            if (Rs.next()) {
                                model.addRow(row);
                                counter++;
                            }

                        }
                    } else if (subjectfilter.getSelectedIndex() > 0 && classfilter.getSelectedIndex() > 0) {
                        infor.setText(classfilter.getSelectedItem() + "  " + subjectfilter.getSelectedItem() + " BOOKS ISSUED");
                        int counter = 1;
                        String sql3 = "Select books.bookserial,bookname,admnumber,dateissued,booktitle from issuedbooks,books where books.bookserial=issuedbooks.bookserial and books.status='" + "ISSUED" + "' and issuedbooks.status!='" + "Returned" + "'  order by bookname";
                        ps = con.prepareStatement(sql3);
                        rs = ps.executeQuery();
                        while (rs.next()) {
                            row[0] = counter;
                            row[1] = rs.getString("bookname");
                            row[2] = rs.getString("booktitle");
                            row[3] = rs.getString("bookserial");
                            row[4] = rs.getString("dateissued");
                            row[5] = Globals.fullName(rs.getString("admnumber"));
                            row[6] = rs.getString("admnumber");
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
                if (subjectfilter.getSelectedIndex() > 0 && classfilter.getSelectedIndex() == 0) {

                    try {
                        infor.setText(subjectfilter.getSelectedItem() + " BOOKS ISSUED(Limited View)");
                        int counter = 1;
                        String sql3 = "Select books.bookserial,bookname,subjectcode,classcode,admnumber,dateissued,booktitle from issuedbooks,books where books.bookserial=issuedbooks.bookserial and books.status='" + "ISSUED" + "'  and issuedbooks.status!='" + "Returned" + "'  order by bookname";
                        ps = con.prepareStatement(sql3);
                        rs = ps.executeQuery();
                        while (rs.next()) {
                            String bkserial = rs.getString("bookserial");

                            row[0] = counter;
                            row[1] = rs.getString("bookname");
                            row[2] = rs.getString("booktitle");
                            row[3] = rs.getString("bookserial");
                            row[4] = rs.getString("dateissued");
                            row[5] = Globals.fullName(rs.getString("admnumber"));
                            row[6] = rs.getString("admnumber");
                            // String sql4="Select teachercode from subjectrights where subjectcode= and classcode"
                            String querry = "select subjectcode,classcode from books where subjectcode='" + Globals.subjectCode(subjectfilter.getSelectedItem().toString()) + "' and bookserial='" + bkserial + "'";
                            ps = con.prepareStatement(querry);
                            ResultSet Rs = ps.executeQuery();
                            if (Rs.next()) {
                                String classcode = Rs.getString("classcode");
                                String subcode = Rs.getString("Subjectcode");
                                String sql5 = "Select subjectrights.subjectcode from subjectrights where subjectrights.classcode='" + classcode + "' and subjectrights.subjectcode='" + Globals.subjectCode(subjectfilter.getSelectedItem().toString()) + "' and teachercode='" + Globals.empcode + "'";
                                ps = con.prepareStatement(sql5);
                                ResultSet RS = ps.executeQuery();
                                if (RS.next()) {
                                    model.addRow(row);
                                    counter++;
                                }
                            }


                        }

                    } catch (SQLException sq) {
                        sq.printStackTrace();
                        JOptionPane.showMessageDialog(null, sq.getMessage());
                    }

                } else if (subjectfilter.getSelectedIndex() > 0 && classfilter.getSelectedIndex() > 0) {
                    infor.setText(classfilter.getSelectedItem() + "  " + subjectfilter.getSelectedItem() + " BOOKS ISSUED(Limited View)");
                    try {
                        int counter = 1;
                        String sql3 = "Select books.bookserial,bookname,subjectcode,classcode,admnumber,dateissued,booktitle from issuedbooks,books where books.bookserial=issuedbooks.bookserial and books.status='" + "ISSUED" + "'  and issuedbooks.status!='" + "Returned" + "' order by bookname";
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
                            row[4] = rs.getString("dateissued");
                            row[5] = Globals.fullName(rs.getString("admnumber"));
                            row[6] = rs.getString("admnumber");
                            String querry = "select subjectcode,classcode from books where subjectcode='" + Globals.subjectCode(subjectfilter.getSelectedItem().toString()) + "' and bookserial='" + bkserial + "'";
                            ps = con.prepareStatement(querry);
                            ResultSet Rs = ps.executeQuery();
                            if (Rs.next()) {

                                subcode = Rs.getString("Subjectcode");
                                String sql6 = "Select subjectrights.subjectcode from subjectrights where subjectrights.classcode='" + classfilter.getSelectedItem() + "' and subjectrights.subjectcode='" + Globals.subjectCode(subjectfilter.getSelectedItem().toString()) + "' and teachercode='" + Globals.empcode + "'";
                                ps = con.prepareStatement(sql6);
                                Rs = ps.executeQuery();
                                if (Rs.next()) {
                                    model.addRow(row);
                                    counter++;
                                }
                            }

                            counter++;

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
                        infor.setText("Issued Books".toUpperCase());
                        int counter = 1;
                        String sql3 = "Select books.bookserial,bookname,admnumber,dateissued,booktitle from issuedbooks,books where books.bookserial=issuedbooks.bookserial and books.status='" + "ISSUED" + "' and issuedbooks.status!='" + "Returned" + "' order by bookname";
                        ps = con.prepareStatement(sql3);
                        rs = ps.executeQuery();
                        while (rs.next()) {
                            row[0] = counter;
                            row[1] = rs.getString("bookname");
                            row[2] = rs.getString("booktitle");
                            row[3] = rs.getString("bookserial");
                            row[4] = rs.getString("dateissued");
                            row[5] = Globals.fullName(rs.getString("admnumber"));
                            row[6] = rs.getString("admnumber");
                            model.addRow(row);
                            counter++;
                        }
                    } else {
                        infor.setText(classfilter.getSelectedItem() + " BOOKS ISSUED");
                        int counter = 1;
                        String sql3 = "Select books.bookserial,bookname,admnumber,dateissued,booktitle from issuedbooks,books where books.bookserial=issuedbooks.bookserial and books.status='" + "ISSUED" + "' and issuedbooks.status!='" + "Returned" + "'  order by bookname";
                        ps = con.prepareStatement(sql3);
                        rs = ps.executeQuery();
                        while (rs.next()) {
                            row[0] = counter;
                            row[1] = rs.getString("bookname");
                            row[2] = rs.getString("booktitle");
                            row[3] = rs.getString("bookserial");
                            row[4] = rs.getString("dateissued");
                            row[5] = Globals.fullName(rs.getString("admnumber"));
                            row[6] = rs.getString("admnumber");
                            String bkserial = rs.getString("bookserial");
                            String querry = "select subjectcode from books where classcode='" + Globals.classCode(classfilter.getSelectedItem().toString()) + "' and bookserial='" + bkserial + "'";
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
                    if (classfilter.getSelectedIndex() == 0) {
                        infor.setText("ISSUED BOOKS(Limited View)");

                        int counter = 1;
                        String sql3 = "Select books.bookserial,bookname,subjectcode,classcode,admnumber,dateissued,booktitle from issuedbooks,books where books.bookserial=issuedbooks.bookserial and books.status='" + "ISSUED" + "'  and issuedbooks.status!='" + "Returned" + "' order by bookname";
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
                            String sql5 = "Select subjectcode from subjectrights where subjectcode='" + subcode + "' and classcode='" + classcode + "' and teachercode='" + Globals.empcode + "'";
                            ps = con.prepareStatement(sql5);
                            ResultSet RS = ps.executeQuery();
                            if (RS.next()) {
                                model.addRow(row);
                            }

                            counter++;

                        }
                    } else {
                        infor.setText(classfilter.getSelectedItem() + " BOOKS ISSUED(Limited View)");
                        int counter = 1;
                        String sql3 = "Select books.bookserial,bookname,subjectcode,classcode,admnumber,dateissued,booktitle from issuedbooks,books where books.bookserial=issuedbooks.bookserial and books.status='" + "ISSUED" + "'  and issuedbooks.status!='" + "Returned" + "' order by bookname";
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

        } else if (obj == booktypefilter) {
            try {
                if (Globals.Level.equalsIgnoreCase("Admin")) {
                    if (booktypefilter.getSelectedIndex() == 0) {
                        while (model.getRowCount() > 0) {
                            model.removeRow(0);
                        }
                        try {
                            if (subjectfilter.getSelectedIndex() > 0 && classfilter.getSelectedIndex() == 0) {
                                infor.setText(classfilter.getSelectedItem() + " BOOKS ISSUED");
                                int counter = 1;
                                String sql3 = "Select books.bookserial,bookname,admnumber,dateissued,booktitle from issuedbooks,books where books.bookserial=issuedbooks.bookserial and books.status='" + "ISSUED" + "' and issuedbooks.status!='" + "Returned" + "' order by bookname";
                                ps = con.prepareStatement(sql3);
                                rs = ps.executeQuery();
                                while (rs.next()) {
                                    row[0] = counter;
                                    String bkserial = rs.getString("booktitle");
                                    ;
                                    row[1] = rs.getString("bookname");
                                    row[2] = rs.getString("booktitle");
                                    row[3] = rs.getString("bookserial");
                                    row[4] = rs.getString("dateissued");
                                    row[5] = Globals.fullName(rs.getString("admnumber"));
                                    row[6] = rs.getString("admnumber");
                                    String querry = "select subjectcode from books where subjectcode='" + Globals.subjectCode(subjectfilter.getSelectedItem().toString()) + "' and bookserial='" + bkserial + "'";
                                    ps = con.prepareStatement(querry);
                                    ResultSet Rs = ps.executeQuery();
                                    if (Rs.next()) {
                                        model.addRow(row);
                                        counter++;
                                    }

                                }
                            } else if (subjectfilter.getSelectedIndex() > 0 && classfilter.getSelectedIndex() > 0) {
                                infor.setText(classfilter.getSelectedItem() + "  " + subjectfilter.getSelectedItem() + " BOOKS ISSUED");
                                int counter = 1;
                                String sql3 = "Select books.bookserial,bookname,admnumber,dateissued,booktitle from issuedbooks,books where books.bookserial=issuedbooks.bookserial and books.status='" + "ISSUED" + "' and issuedbooks.status!='" + "Returned" + "'  order by bookname";
                                ps = con.prepareStatement(sql3);
                                rs = ps.executeQuery();
                                while (rs.next()) {
                                    row[0] = counter;
                                    row[1] = rs.getString("bookname");
                                    row[2] = rs.getString("booktitle");
                                    row[3] = rs.getString("bookserial");
                                    row[4] = rs.getString("dateissued");
                                    row[5] = Globals.fullName(rs.getString("admnumber"));
                                    row[6] = rs.getString("admnumber");
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
                                infor.setText(subjectfilter.getSelectedItem() + "   " + booktypefilter.getSelectedItem().toString().toUpperCase() + "  ISSUED");
                                int counter = 1;
                                String sql3 = "Select books.bookserial,bookname,admnumber,dateissued,booktitle from issuedbooks,books where books.bookserial=issuedbooks.bookserial and books.status='" + "ISSUED" + "' and issuedbooks.status!='" + "Returned" + "' order by bookname";
                                ps = con.prepareStatement(sql3);
                                rs = ps.executeQuery();
                                while (rs.next()) {
                                    row[0] = counter;
                                    String bkserial = rs.getString("bookserial");
                                    row[1] = rs.getString("bookname");
                                    row[2] = rs.getString("booktitle");
                                    row[3] = rs.getString("bookserial");
                                    row[4] = rs.getString("dateissued");
                                    row[5] = Globals.fullName(rs.getString("admnumber"));
                                    row[6] = rs.getString("admnumber");
                                    String querry = "select subjectcode from books where subjectcode='" + Globals.subjectCode(subjectfilter.getSelectedItem().toString()) + "' and bookserial='" + bkserial + "' and type='" + booktypefilter.getSelectedItem() + "'";
                                    ps = con.prepareStatement(querry);
                                    ResultSet Rs = ps.executeQuery();
                                    if (Rs.next()) {
                                        model.addRow(row);
                                        counter++;
                                    }

                                }
                            } else if (subjectfilter.getSelectedIndex() > 0 && classfilter.getSelectedIndex() > 0) {
                                infor.setText(classfilter.getSelectedItem() + "  " + subjectfilter.getSelectedItem() + "  " + booktypefilter.getSelectedItem() + "S ISSUED");
                                int counter = 1;
                                String sql3 = "Select books.bookserial,bookname,admnumber,dateissued,booktitle from issuedbooks,books where books.bookserial=issuedbooks.bookserial and books.status='" + "ISSUED" + "' and issuedbooks.status!='" + "Returned" + "'  order by bookname";
                                ps = con.prepareStatement(sql3);
                                rs = ps.executeQuery();
                                while (rs.next()) {
                                    row[0] = counter;
                                    row[1] = rs.getString("bookname");
                                    row[2] = rs.getString("booktitle");
                                    row[3] = rs.getString("bookserial");
                                    row[4] = rs.getString("dateissued");
                                    row[5] = Globals.fullName(rs.getString("admnumber"));
                                    row[6] = rs.getString("admnumber");
                                    String bkserial = rs.getString("bookserial");
                                    String querry = "select subjectcode from books where subjectcode='" + Globals.subjectCode(subjectfilter.getSelectedItem().toString()) + "' and bookserial='" + bkserial + "' and classcode='" + Globals.classCode(classfilter.getSelectedItem().toString()) + "' and type='" + booktypefilter.getSelectedItem() + "'";
                                    ps = con.prepareStatement(querry);
                                    ResultSet Rs = ps.executeQuery();
                                    if (Rs.next()) {
                                        model.addRow(row);
                                        counter++;
                                    }


                                }
                            } else if (classfilter.getSelectedIndex() == 0 && subjectfilter.getSelectedIndex() == 0) {
                                infor.setText(booktypefilter.getSelectedItem().toString().toUpperCase() + "S ISSUED");

                                int counter = 1;
                                String sql3 = "Select books.bookserial,bookname,admnumber,dateissued,booktitle from issuedbooks,books where books.bookserial=issuedbooks.bookserial and books.status='" + "ISSUED" + "' and issuedbooks.status!='" + "Returned" + "'  order by bookname";
                                ps = con.prepareStatement(sql3);
                                rs = ps.executeQuery();
                                while (rs.next()) {

                                    row[0] = counter;
                                    row[1] = rs.getString("bookname");
                                    row[2] = rs.getString("booktitle");
                                    row[3] = rs.getString("bookserial");
                                    row[4] = rs.getString("dateissued");
                                    row[5] = Globals.fullName(rs.getString("admnumber"));
                                    row[6] = rs.getString("admnumber");
                                    String bkserial = rs.getString("bookserial");
                                    String querry = "select subjectcode from books where bookserial='" + bkserial + "'  and type='" + booktypefilter.getSelectedItem() + "'";
                                    ps = con.prepareStatement(querry);
                                    ResultSet Rs = ps.executeQuery();
                                    if (Rs.next()) {
                                        model.addRow(row);
                                        counter++;
                                    }


                                }
                            } else if (classfilter.getSelectedIndex() > 0 && subjectfilter.getSelectedIndex() == 0) {
                                infor.setText(classfilter.getSelectedItem() + "   " + booktypefilter.getSelectedItem().toString().toUpperCase() + "  ISSUED");
                                int counter = 1;
                                String sql3 = "Select books.bookserial,bookname,admnumber,dateissued,booktitle from issuedbooks,books where books.bookserial=issuedbooks.bookserial and books.status='" + "ISSUED" + "' and issuedbooks.status!='" + "Returned" + "' order by bookname";
                                ps = con.prepareStatement(sql3);
                                rs = ps.executeQuery();
                                while (rs.next()) {
                                    row[0] = counter;
                                    String bkserial = rs.getString("bookserial");
                                    row[1] = rs.getString("bookname");
                                    row[2] = rs.getString("booktitle");
                                    row[3] = rs.getString("bookserial");
                                    row[4] = rs.getString("dateissued");
                                    row[5] = Globals.fullName(rs.getString("admnumber"));
                                    row[6] = rs.getString("admnumber");
                                    String querry = "select subjectcode from books where classcode='" + Globals.classCode(classfilter.getSelectedItem().toString()) + "' and bookserial='" + bkserial + "' and type='" + booktypefilter.getSelectedItem() + "'";
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

                    }


                } else {

                    if (booktypefilter.getSelectedIndex() == 0) {

                        if (subjectfilter.getSelectedIndex() > 0 && classfilter.getSelectedIndex() == 0) {

                            while (model.getRowCount() > 0) {
                                model.removeRow(0);
                            }
                            try {
                                if (classfilter.getSelectedIndex() == 0) {
                                    infor.setText("ISSUED BOOKS(Limited View)");

                                    int counter = 1;
                                    String sql3 = "Select books.bookserial,bookname,subjectcode,classcode,admnumber,dateissued,booktitle from issuedbooks,books where books.bookserial=issuedbooks.bookserial and books.status='" + "ISSUED" + "'  and issuedbooks.status!='" + "Returned" + "' order by bookname";
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
                                        String sql5 = "Select subjectcode from subjectrights where subjectcode='" + subcode + "' and classcode='" + classcode + "' and teachercode='" + Globals.empcode + "'";
                                        ps = con.prepareStatement(sql5);
                                        ResultSet RS = ps.executeQuery();
                                        if (RS.next()) {
                                            model.addRow(row);
                                        }

                                        counter++;

                                    }
                                } else {
                                    infor.setText(classfilter.getSelectedItem() + " BOOKS ISSUED(Limited View)");
                                    int counter = 1;
                                    String sql3 = "Select books.bookserial,bookname,subjectcode,classcode,admnumber,dateissued,booktitle from issuedbooks,books where books.bookserial=issuedbooks.bookserial and books.status='" + "ISSUED" + "'  and issuedbooks.status!='" + "Returned" + "' order by bookname";
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
                                infor.setText(subjectfilter.getSelectedItem() + " " + booktypefilter.getSelectedItem().toString().toUpperCase() + " ISSUED(Limited View)");
                                int counter = 1;
                                String sql3 = "Select books.bookserial,bookname,subjectcode,classcode,admnumber,dateissued,booktitle from issuedbooks,books where books.bookserial=issuedbooks.bookserial and books.status='" + "ISSUED" + "'  and issuedbooks.status!='" + "Returned" + "'  order by bookname";
                                ps = con.prepareStatement(sql3);
                                rs = ps.executeQuery();
                                while (rs.next()) {
                                    String bkserial = rs.getString("bookserial");

                                    row[0] = counter;
                                    row[1] = rs.getString("bookname");
                                    row[2] = rs.getString("booktitle");
                                    row[3] = rs.getString("bookserial");
                                    row[4] = rs.getString("dateissued");
                                    row[5] = Globals.fullName(rs.getString("admnumber"));
                                    row[6] = rs.getString("admnumber");
                                    // String sql4="Select teachercode from subjectrights where subjectcode= and classcode"
                                    String querry = "select subjectcode,classcode from books where subjectcode='" + Globals.subjectCode(subjectfilter.getSelectedItem().toString()) + "' and bookserial='" + bkserial + "' and type='" + booktypefilter.getSelectedItem() + "'";
                                    ps = con.prepareStatement(querry);
                                    ResultSet Rs = ps.executeQuery();
                                    if (Rs.next()) {
                                        String classcode = Rs.getString("classcode");
                                        String subcode = Rs.getString("Subjectcode");
                                        String sql5 = "Select subjectrights.subjectcode from subjectrights where subjectrights.classcode='" + classcode + "' and subjectrights.subjectcode='" + Globals.subjectCode(subjectfilter.getSelectedItem().toString()) + "' and teachercode='" + Globals.empcode + "'";
                                        ps = con.prepareStatement(sql5);
                                        ResultSet RS = ps.executeQuery();
                                        if (RS.next()) {
                                            model.addRow(row);
                                            counter++;
                                        }
                                    }


                                }

                            } catch (SQLException sq) {
                                sq.printStackTrace();
                                JOptionPane.showMessageDialog(null, sq.getMessage());
                            }

                        } else if (subjectfilter.getSelectedIndex() > 0 && classfilter.getSelectedIndex() > 0) {
                            infor.setText(classfilter.getSelectedItem() + "  " + subjectfilter.getSelectedItem() + booktypefilter.getSelectedItem().toString().toUpperCase() + " ISSUED(Limited View)");
                            try {
                                int counter = 1;
                                String sql3 = "Select books.bookserial,type,bookname,subjectcode,classcode,admnumber,dateissued,booktitle from issuedbooks,books where books.bookserial=issuedbooks.bookserial and books.status='" + "ISSUED" + "' and type='" + booktypefilter.getSelectedItem() + "'   and issuedbooks.status!='" + "Returned" + "' order by bookname";
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
                                    row[4] = rs.getString("dateissued");
                                    row[5] = Globals.fullName(rs.getString("admnumber"));
                                    row[6] = rs.getString("admnumber");
                                    String querry = "select subjectcode,classcode from books where subjectcode='" + Globals.subjectCode(subjectfilter.getSelectedItem().toString()) + "' and bookserial='" + bkserial + "' and type='" + booktypefilter.getSelectedItem().toString() + "'";
                                    ps = con.prepareStatement(querry);
                                    ResultSet Rs = ps.executeQuery();
                                    if (Rs.next()) {


                                        String sql6 = "Select subjectrights.subjectcode from subjectrights where subjectrights.classcode='" + classcode + "' and subjectrights.subjectcode='" + Globals.subjectCode(subjectfilter.getSelectedItem().toString()) + "' and teachercode='" + Globals.empcode + "'";
                                        ps = con.prepareStatement(sql6);
                                        Rs = ps.executeQuery();
                                        if (Rs.next()) {
                                            if (type.equalsIgnoreCase(booktypefilter.getSelectedItem().toString())) {
                                                model.addRow(row);
                                                counter++;
                                            }

                                        }
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
