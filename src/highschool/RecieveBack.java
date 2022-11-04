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
public class RecieveBack implements ActionListener {

    private JPanel bottom;
    private JPanel top;
    private JPanel holder;
    private JTable tab;
    private FredTextField jsearch;
    private DefaultTableModel model;
    private JScrollPane pane;
    private FredLabel infor, search;
    private PreparedStatement ps;
    private ResultSet rs;
    private Connection con = DbConnection.connectDb();
    private FredButton save;
    private FredCombo bookstatus;
    private Object col[] = {"Student Name", "admission Number", "Book Name", "Book Title", "Book Serial No.", "Status", "Date Issued", "Issue Ref No."};
    private Object row[] = new Object[col.length];

    public JPanel bookrecieverPanel() {


        holder = new JPanel();
        top = new JPanel();
        bottom = new JPanel();
        save = new FredButton("Save");
        bookstatus = new FredCombo("Select The Book Status");
        infor = new FredLabel("Search Results");

        top.setLayout(new MigLayout());
        bottom.setLayout(new MigLayout());
        holder.setLayout(new MigLayout());
        holder.add(top, "grow,push,wrap");
        holder.add(bottom, "growx,pushx");
        bottom.setBackground(Color.WHITE);

        model = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int col) {
                return false;
            }
        };
        tab = new JTable();

        tab.setModel(model);
        pane = new JScrollPane(tab);
        model.setColumnIdentifiers(col);
        jsearch = new FredTextField();
        search = new FredLabel("Search Student To Update Her Book Status,(Its A Must To Search If He/She Is An Alumni)");
        top.add(infor, "growx,pushx,");
        top.add(search, "growx,pushx");
        top.add(jsearch, "growx,pushx,wrap");

        top.add(pane, "grow,push,span,wrap");
        top.add(bookstatus, "Skip");
        bottom.add(save, "growx,push");
        bookstatus.addItem("Returned");
        bookstatus.addItem("Replaced");
        bookstatus.addItem("Lost");


        try {

            if (Globals.Level.equalsIgnoreCase("Admin")) {
                String sql = "Select * from issuedBooks,books where issuedbooks.status!='" + "Returned" + "' and issuedbooks.bookserial=books.bookserial  order by admnumber";
                ps = con.prepareStatement(sql);
                rs = ps.executeQuery();
                while (rs.next()) {
                    row[0] = Globals.fullName(rs.getString("Admnumber"));
                    row[1] = rs.getString("Admnumber");
                    row[2] = rs.getString("BookName");
                    row[3] = rs.getString("BookTitle");
                    row[4] = rs.getString("Bookserial");
                    row[5] = rs.getString("Status");
                    row[6] = rs.getString("DateIssued");
                    row[7] = rs.getString("IssueId");
                    model.addRow(row);

                }
            } else {
                String sql = "Select * from issuedBooks,books where issuedbooks.status!='" + "Returned" + "' and issuedbooks.bookserial=books.bookserial order by admnumber";
                ps = con.prepareStatement(sql);
                rs = ps.executeQuery();
                while (rs.next()) {
                    row[0] = Globals.fullName(rs.getString("Admnumber"));
                    row[1] = rs.getString("Admnumber");
                    row[2] = rs.getString("BookName");
                    row[3] = rs.getString("BookTitle");
                    row[4] = rs.getString("Bookserial");
                    row[5] = rs.getString("Status");
                    row[6] = rs.getString("DateIssued");
                    row[7] = rs.getString("IssueId");
                    String subcode, classcode;
                    subcode = rs.getString("Subjectcode");
                    classcode = rs.getString("Classcode");
                    String sql1 = "Select * from subjectrights where teachercode='" + Globals.empcode + "' and classcode='" + classcode + "' and subjectcode='" + subcode + "' ";
                    ps = con.prepareStatement(sql1);
                    ResultSet RS = ps.executeQuery();
                    if (RS.next()) {
                        model.addRow(row);
                    }


                }

            }

        } catch (SQLException sq) {
            sq.printStackTrace();
            JOptionPane.showMessageDialog(null, sq.getMessage());
        }
        search.setIcon(new ImageIcon("C:\\SmartFinanceSoftware\\icons/search-icon.png"));
        jsearch.addKeyListener(new KeyAdapter() {

            @Override
            public void keyTyped(KeyEvent e) {
                while (model.getRowCount() > 0) {
                    model.removeRow(0);
                }
                String adm = "";
                try {
                    if (Globals.Level.equalsIgnoreCase("Admin")) {

                        String querry = "Select admissionnumber from admission where firstname like '" + jsearch.getText() + "%' or middlename  like '" + jsearch.getText() + "%' or lastname  like '" + jsearch.getText() + "%' or admissionnumber  like '" + jsearch.getText() + "%'";
                        ps = con.prepareStatement(querry);
                        ResultSet RS = ps.executeQuery();
                        while (RS.next()) {
                            adm = RS.getString("admissionnumber");

                            String sql = "Select * from issuedBooks,books where issuedbooks.status!='" + "Returned" + "' and issuedbooks.bookserial=books.bookserial and admnumber='" + adm + "'";
                            ps = con.prepareStatement(sql);
                            rs = ps.executeQuery();
                            while (rs.next()) {
                                row[0] = Globals.fullName(rs.getString("Admnumber"));
                                row[1] = rs.getString("Admnumber");
                                row[2] = rs.getString("BookName");
                                row[3] = rs.getString("BookTitle");
                                row[4] = rs.getString("Bookserial");
                                row[5] = rs.getString("Status");
                                row[6] = rs.getString("DateIssued");
                                row[7] = rs.getString("IssueId");
                                model.addRow(row);

                            }
                            String sqll = "Select * from alumnibooksrecord,books where alumnibooksrecord.status!='" + "Returned" + "' and alumnibooksrecord.bookserial=books.bookserial and admnumber='" + adm + "'";
                            ps = con.prepareStatement(sqll);
                            rs = ps.executeQuery();
                            while (rs.next()) {
                                row[0] = Globals.fullName(rs.getString("Admnumber"));
                                row[1] = rs.getString("Admnumber");
                                row[2] = rs.getString("BookName");
                                row[3] = rs.getString("BookTitle");
                                row[4] = rs.getString("Bookserial");
                                row[5] = rs.getString("Status");
                                row[6] = rs.getString("DateIssued");
                                row[7] = rs.getString("IssueId");
                                model.addRow(row);

                            }
                        }
                    } else {
                        String querry = "Select admissionnumber from admission where firstname like '" + jsearch.getText() + "%' or middlename  like '" + jsearch.getText() + "%' or lastname  like '" + jsearch.getText() + "%' or admissionnumber  like '" + jsearch.getText() + "%'";
                        ps = con.prepareStatement(querry);
                        ResultSet RS = ps.executeQuery();
                        while (RS.next()) {
                            adm = RS.getString("admissionnumber");

                            String sql = "Select * from issuedBooks,books where issuedbooks.status!='" + "Returned" + "' and issuedbooks.bookserial=books.bookserial and admnumber='" + adm + "'";
                            ps = con.prepareStatement(sql);
                            rs = ps.executeQuery();
                            while (rs.next()) {
                                row[0] = Globals.fullName(rs.getString("Admnumber"));
                                row[1] = rs.getString("Admnumber");
                                row[2] = rs.getString("BookName");
                                row[3] = rs.getString("BookTitle");
                                row[4] = rs.getString("Bookserial");
                                row[5] = rs.getString("Status");
                                row[6] = rs.getString("DateIssued");
                                row[7] = rs.getString("IssueId");
                                String subcode, classcode;
                                subcode = rs.getString("Subjectcode");
                                classcode = rs.getString("Classcode");
                                String sql1 = "Select * from subjectrights where teachercode='" + Globals.empcode + "' and classcode='" + classcode + "' and subjectcode='" + subcode + "' ";
                                ps = con.prepareStatement(sql1);
                                ResultSet Rm = ps.executeQuery();
                                if (Rm.next()) {
                                    model.addRow(row);
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


        save.addActionListener(this);
        return holder;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object obj = e.getSource();
        if (obj == save) {
            if (tab.getSelectedRowCount() < 1) {
                JOptionPane.showMessageDialog(holder, "Select The Student To Recieve  Book From");
            } else {
                if (bookstatus.getSelectedIndex() == 0) {
                    JOptionPane.showMessageDialog(holder, "Select The Book Status");
                } else {
                    try {
                        int selected = tab.getSelectedRow();
                        String adm = model.getValueAt(selected, 1).toString();
                        String classcode = "";
                        String querry1 = "Select currentform from admission where admissionnumber='" + adm + "'";
                        ps = con.prepareStatement(querry1);
                        rs = ps.executeQuery();
                        if (rs.next()) {
                            classcode = rs.getString("currentform");
                        }
                        if (classcode.startsWith("AL")) {
                            String bookserial = model.getValueAt(selected, 4).toString();

                            String querry = "Update alumnibooksrecord set status='" + bookstatus.getSelectedItem() + "',datereturned=curdate() where bookserial='" + bookserial + "' and admnumber='" + adm + "'";
                            ps = con.prepareStatement(querry);
                            ps.execute();
                            if (bookstatus.getSelectedItem().toString().equalsIgnoreCase("Returned") || bookstatus.getSelectedItem().toString().equalsIgnoreCase("Replaced")) {
                                String sql = "Update books set status='" + "On Store" + "' where bookserial='" + bookserial + "'";
                                ps = con.prepareStatement(sql);
                                ps.execute();
                            } else {

                            }
                        } else {
                            String bookserial = model.getValueAt(selected, 4).toString();

                            String querry = "Update issuedbooks set status='" + bookstatus.getSelectedItem() + "',datereturned=curdate() where bookserial='" + bookserial + "' and admnumber='" + adm + "'";
                            ps = con.prepareStatement(querry);
                            ps.execute();
                            if (bookstatus.getSelectedItem().toString().equalsIgnoreCase("Returned") || bookstatus.getSelectedItem().toString().equalsIgnoreCase("Replaced")) {
                                String sql = "Update books set status='" + "On Store" + "' where bookserial='" + bookserial + "'";
                                ps = con.prepareStatement(sql);
                                ps.execute();
                            } else {

                            }
                        }


                        JOptionPane.showMessageDialog(holder, "Book Recovered Successfully");
                        model.removeRow(selected);
                        bookstatus.setSelectedIndex(0);
                    } catch (HeadlessException | SQLException sq) {
                        sq.printStackTrace();
                        JOptionPane.showMessageDialog(null, sq.getMessage());
                    }


                }
            }
        }
    }

}
