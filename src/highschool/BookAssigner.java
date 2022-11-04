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

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import javax.swing.table.DefaultTableModel;

import net.miginfocom.swing.MigLayout;

/**
 * @author FRED
 */
public class BookAssigner implements ActionListener {


    private JPanel bottom;
    private JPanel top;
    private JPanel holder;
    private FredLabel search, search2;
    private FredTextField jsearch, jsearch2;
    private JTable tab, tab2;
    private volatile DefaultTableModel model, model2;
    private JScrollPane pane, pane2;
    private FredLabel infor, infor2;
    private PreparedStatement ps;
    private ResultSet rs;
    private Connection con = DbConnection.connectDb();
    private FredButton assign;

    private Object col[] = {"Book Name", "Book Serial No.", "Book Title", "Class", "Subject", "Type"};
    private Object col2[] = {"admission Number.", "Student Name", "Class"};
    private Object row[] = new Object[col.length];
    private Object row2[] = new Object[col2.length];

    public JPanel bookassignpanel() {
        holder = new JPanel();
        top = new JPanel();
        bottom = new JPanel();
        assign = new FredButton("Issue Book");
        infor = new FredLabel("Book Available For Issue");
        infor2 = new FredLabel("Form 1 Class List");

        top.setLayout(new MigLayout());
        bottom.setLayout(new MigLayout());
        holder.setLayout(new MigLayout());
        holder.add(top, "grow,push,wrap");
        holder.add(bottom, "growx,pushx");
        bottom.setBackground(Color.WHITE);
        search = new FredLabel("Search Book");
        search2 = new FredLabel("Search Student");
        jsearch = new FredTextField();
        jsearch2 = new FredTextField();
        model = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int col) {
                return false;
            }
        };
        model2 = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int col) {
                return false;
            }
        };
        tab = new JTable();
        tab2 = new JTable();

        tab.setModel(model);
        tab2.setModel(model2);
        pane = new JScrollPane(tab);
        pane2 = new JScrollPane(tab2);
        model.setColumnIdentifiers(col);
        model2.setColumnIdentifiers(col2);
        top.add(infor, "growx,pushx");
        top.add(search, "gapleft 20");
        top.add(jsearch, "growx,pushx");
        top.add(infor2, "growx,pushx");
        top.add(search2, "gapleft 20");
        top.add(jsearch2, "growx,pushx,wrap");
        top.add(pane, "grow,push,span 3 1");
        top.add(pane2, "grow,push,span 3 1");
        bottom.add(assign, "growx,pushx,gapleft 20");
        search.setIcon(new ImageIcon("C:\\SmartFinanceSoftware\\icons/search-icon.png"));
        search2.setIcon(new ImageIcon("C:\\SmartFinanceSoftware\\icons/search-icon.png"));
        try {
            if (Globals.Level.equalsIgnoreCase("Normal")) {
                String sql = "Select distinct classcode from subjectrights where teachercode='" + Globals.empcode + "' ";
                ps = con.prepareStatement(sql);
                rs = ps.executeQuery();
                while (rs.next()) {
                    String sql2 = "Select firstname,middlename,lastname,currentform,admissionnumber from admission where currentform='" + rs.getString("Classcode") + "' " + Globals.sortcode + " ";
                    ps = con.prepareStatement(sql2);
                    ResultSet RS = ps.executeQuery();
                    while (RS.next()) {
                        row2[0] = RS.getString("AdmissionNumber");
                        row2[1] = RS.getString("firstName") + "    " + RS.getString("Middlename") + "    " + RS.getString("LastName");
                        row2[2] = Globals.className(rs.getString("classcode"));

                        model2.addRow(row2);
                    }
                }
                String sql3 = "Select  subjectcode,classcode from subjectrights where teachercode='" + Globals.empcode + "'";
                ps = con.prepareStatement(sql3);
                rs = ps.executeQuery();
                while (rs.next()) {
                    String sql4 = "Select booktitle,SubjectCode,bookname,bookserial,type,classcode from books where subjectcode='" + rs.getString("Subjectcode") + "' and status='" + "On Store" + "'  and classcode='" + rs.getString("classcode") + "' order by bookname ";
                    ps = con.prepareStatement(sql4);

                    ResultSet RS = ps.executeQuery();
                    while (RS.next()) {
                        row[0] = RS.getString("BookName");
                        row[1] = RS.getString("BookSerial");
                        row[2] = RS.getString("BookTitle");
                        row[3] = Globals.className(RS.getString("classcode"));
                        row[4] = Globals.subjectName(RS.getString("SubjectCode"));
                        row[5] = RS.getString("type");
                        model.addRow(row);
                    }
                }

            } else {
                String sql2 = "Select firstname,middlename,lastname,currentform,admissionnumber from admission where currentform ='" + Globals.classCode("Form 1") + "' " + Globals.sortcode + " ";
                ps = con.prepareStatement(sql2);
                ResultSet RS = ps.executeQuery();
                while (RS.next()) {
                    row2[0] = RS.getString("AdmissionNumber");
                    row2[1] = RS.getString("firstName") + "    " + RS.getString("Middlename") + "    " + RS.getString("LastName");
                    row2[2] = Globals.className(RS.getString("currentForm"));
                    model2.addRow(row2);
                }


                String sql4 = "Select booktitle,bookname,type,subjectcode,bookserial,classcode from books where status='" + "On Store" + "' order by bookname";
                ps = con.prepareStatement(sql4);

                RS = ps.executeQuery();
                while (RS.next()) {
                    row[0] = RS.getString("BookName");
                    row[1] = RS.getString("BookSerial");
                    row[2] = RS.getString("BookTitle");
                    row[3] = Globals.className(RS.getString("classcode"));
                    row[4] = Globals.subjectName(RS.getString("SubjectCode"));
                    row[5] = RS.getString("type");
                    model.addRow(row);
                }

            }


        } catch (SQLException sq) {
            sq.printStackTrace();
            JOptionPane.showMessageDialog(null, sq.getMessage());
        }

        jsearch2.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();
                if (c == KeyEvent.VK_ENTER) {

                    while (model2.getRowCount() > 0) {
                        model2.removeRow(0);
                    }

                    try {

                        if (Globals.Level.equalsIgnoreCase("Normal")) {
                            String sql2 = "Select firstname,middlename,lastname,currentform,admissionnumber from admission where admissionnumber like '" + jsearch2.getText() + "%' or firstname like '" + jsearch2.getText() + "%' or middlename like '" + jsearch2.getText() + "%' or lastname like '" + jsearch2.getText() + "%' " + Globals.sortcode + " ";
                            ps = con.prepareStatement(sql2);
                            ResultSet RS = ps.executeQuery();
                            while (RS.next()) {
                                String form = RS.getString("currentform");
                                row2[0] = RS.getString("AdmissionNumber");
                                row2[1] = RS.getString("firstName") + "    " + RS.getString("Middlename") + "    " + RS.getString("LastName");
                                row2[2] = Globals.className(RS.getString("currentform"));
                                String sql = "Select distinct classcode from subjectrights where teachercode='" + Globals.empcode + "' and classcode='" + form + "' ";
                                ps = con.prepareStatement(sql);
                                rs = ps.executeQuery();
                                if (rs.next()) {

                                    model2.addRow(row2);
                                }


                            }


                        } else {
                            int counter = 0;
                            while (tab2.getRowCount() > 0) {
                                model2.removeRow(0);
                            }
                            String sql = "Select firstname,middlename,lastname,currentform,currentstream,admissionnumber from admission where  currentform !='" + Globals.classCode("Alumni") + "' and admissionnumber like '" + jsearch2.getText() + "%' or firstname like '" + jsearch2.getText() + "%' or middlename like '" + jsearch2.getText() + "%' or lastname like '" + jsearch2.getText() + "%' " + Globals.sortcode + " ";
                            ps = con.prepareStatement(sql);
                            ResultSet RS = ps.executeQuery();
                            while (RS.next()) {
                                row2[0] = RS.getString("AdmissionNumber");
                                row2[1] = RS.getString("firstName") + "    " + RS.getString("Middlename") + "    " + RS.getString("LastName");
                                row2[2] = Globals.className(RS.getString("currentForm"));

                                if (RS.getString("currentForm").startsWith("FM")) {
                                    model2.addRow(row2);
                                }


                            }


                        }


                    } catch (SQLException sq) {
                        sq.printStackTrace();
                        JOptionPane.showMessageDialog(null, sq.getMessage());
                    }
                }
            }


        });
        jsearch.addKeyListener(new KeyAdapter() {

            @Override
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();
                if (c == KeyEvent.VK_ENTER) {
                    try {
                        while (tab.getRowCount() > 0) {
                            model.removeRow(0);
                        }
                        if (Globals.Level.equalsIgnoreCase("Normal")) {
                            String sql4 = "Select booktitle,bookname,type,bookserial,classcode,subjectcode from books where booktitle like '" + jsearch.getText() + "%' or bookname  like '" + jsearch.getText() + "%' or bookserial  like '" + jsearch.getText() + "%' and status='" + "On Store" + "' order by bookname";
                            ps = con.prepareStatement(sql4);

                            ResultSet RS = ps.executeQuery();
                            while (RS.next()) {
                                String subcode = RS.getString("SubjectCode");
                                row[0] = RS.getString("BookName");
                                row[1] = RS.getString("BookSerial");
                                row[2] = RS.getString("BookTitle");
                                row[3] = Globals.className(RS.getString("classcode"));
                                row[4] = Globals.subjectName(subcode);
                                row[5] = RS.getString("type");
                                String sql3 = "Select  subjectcode from subjectrights where teachercode='" + Globals.empcode + "' and subjectcode='" + subcode + "' and classcode='" + RS.getString("Classcode") + "'";
                                ps = con.prepareStatement(sql3);
                                rs = ps.executeQuery();
                                if (rs.next()) {

                                    model.addRow(row);

                                }


                            }
                        } else {
                            String sql4 = "Select booktitle,type,status,bookname,subjectcode,bookserial,classcode from books where status='" + "On Store" + "' and booktitle like '" + jsearch.getText() + "%' or bookname  like '" + jsearch.getText() + "%' or bookserial  like '" + jsearch.getText() + "%'   order by bookname";
                            ps = con.prepareStatement(sql4);
                            ResultSet RS;
                            RS = ps.executeQuery();
                            while (RS.next()) {
                                row[0] = RS.getString("BookName");
                                row[1] = RS.getString("BookSerial");
                                row[2] = RS.getString("BookTitle");
                                row[3] = Globals.className(RS.getString("classcode"));
                                row[4] = Globals.subjectName(RS.getString("SubjectCode"));
                                row[5] = RS.getString("type");
                                if (RS.getString("status").equalsIgnoreCase("On Store")) {
                                    model.addRow(row);
                                }

                            }

                        }


                    } catch (SQLException sq) {
                        sq.printStackTrace();
                        JOptionPane.showMessageDialog(null, sq.getMessage());
                    }
                }
            }


        });
        assign.addActionListener(this);

        return holder;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object obj = e.getSource();
        if (obj == assign) {
            if (tab.getSelectedRowCount() < 1) {
                JOptionPane.showMessageDialog(holder, "Kindly Select The Book To Issue");
            } else {
                if (tab2.getSelectedRowCount() < 1) {
                    JOptionPane.showMessageDialog(holder, "Kindly Select The Student To Issue The Book To");
                } else {
                    String bookserial = tab.getValueAt(tab.getSelectedRow(), 1).toString();
                    String adm = tab2.getValueAt(tab2.getSelectedRow(), 0).toString();
                    String type = tab.getValueAt(tab.getSelectedRow(), 5).toString();
                    String bookclass = tab.getValueAt(tab.getSelectedRow(), 3).toString();
                    String studentclass = tab2.getValueAt(tab2.getSelectedRow(), 2).toString();
                    try {
                        if (type.equalsIgnoreCase("Course Book")) {
                            int studentclasspress = 0;
                            int bookclasspress = 0;
                            String querry = "Select precision1 from classes where classname='" + bookclass + "'";
                            ps = con.prepareStatement(querry);
                            rs = ps.executeQuery();
                            while (rs.next()) {
                                bookclasspress = rs.getInt("precision1");
                            }

                            String querry2 = "Select precision1 from classes where classname='" + studentclass + "'";
                            ps = con.prepareStatement(querry2);
                            rs = ps.executeQuery();
                            while (rs.next()) {
                                studentclasspress = rs.getInt("precision1");
                            }
                            if (studentclasspress < bookclasspress) {
                                JOptionPane.showMessageDialog(holder, "Course Books Cannot Be Issued To Student Below The Book Level");
                            } else {
                                String sql = "Insert into issuedBooks (BookSerial,DateIssued,AdmNumber,Status,IssueID) values('" + bookserial + "',curDate(),'" + adm + "','" + "Not Returned" + "','" + "IS" + IdGenerator.keyGen() + "')";
                                ps = con.prepareStatement(sql);
                                ps.execute();
                                String sql2 = "Update books set status='" + "ISSUED" + "' where bookserial='" + bookserial + "'";
                                ps = con.prepareStatement(sql2);
                                ps.execute();
                                String subcode = "";
                                String querry1 = "Select SubjectCode from books where bookserial='" + bookserial + "'";
                                ps = con.prepareStatement(querry1);
                                rs = ps.executeQuery();
                                if (rs.next()) {
                                    subcode = rs.getString("subjectcode");
                                }

                                String querry3 = "Delete from studentsubjectclearance where admnumber='" + adm + "' and subjectcode='" + subcode + "'";
                                ps = con.prepareStatement(querry3);
                                ps.execute();

                                JOptionPane.showMessageDialog(holder, "Book Issued Successfully");
                                model.removeRow(tab.getSelectedRow());

                            }

                        } else {
                            String sql = "Insert into issuedBooks (BookSerial,DateIssued,AdmNumber,Status,IssueID) values('" + bookserial + "',curDate(),'" + adm + "','" + "Not Returned" + "','" + "IS" + IdGenerator.keyGen() + "')";
                            ps = con.prepareStatement(sql);
                            ps.execute();
                            String sql2 = "Update books set status='" + "ISSUED" + "' where bookserial='" + bookserial + "'";
                            ps = con.prepareStatement(sql2);
                            ps.execute();
                            String subcode = "";
                            String querry1 = "Select subjectcode from books where bookserial='" + bookserial + "'";
                            ps = con.prepareStatement(querry1);
                            rs = ps.executeQuery();
                            if (rs.next()) {
                                subcode = rs.getString("subjectcode");
                            }

                            String querry = "Delete from studentsubjectclearance where admnumber='" + adm + "' and subjectcode='" + subcode + "'";
                            ps = con.prepareStatement(querry);
                            ps.execute();

                            JOptionPane.showMessageDialog(holder, "Book Issued Successfully");
                            model.removeRow(tab.getSelectedRow());
                        }


                    } catch (HeadlessException | SQLException sq) {
                        sq.printStackTrace();
                        JOptionPane.showMessageDialog(null, sq.getMessage());
                    }

                }
            }
        }


    }

}
