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
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import net.miginfocom.swing.MigLayout;

/**
 * @author FRED
 */
public class ManageCustomPayments implements ActionListener {

    private JPanel bottom;
    private JPanel top;
    private JPanel holder;
    private JTable tab;
    private DefaultTableModel model;
    private JScrollPane pane;
    private FredLabel infor;
    private PreparedStatement ps;
    private ResultSet rs;
    private Connection con = DbConnection.connectDb();
    private FredButton print, delete, view, remove, back;
    private Object col[] = {"Class", "Academic Year", "Term", "No. Of Students", "Invoice Value", "Amount Expected", "Amount Paid", "Balance", "PaymentName"};
    private Object row[] = new Object[col.length];
    int total = 0;
    String pycode = "";
    String termcode = "";
    int totalpaid = 0;
    Object arraytotal[] = new Object[col.length];

    public JPanel customPaymentHolder() {
        holder = new JPanel();
        top = new JPanel();
        bottom = new JPanel();
        print = new FredButton("Generate Report");
        infor = new FredLabel("Custom Payment Summary Report");
        view = new FredButton("View Students Paying");
        remove = new FredButton("Remove From Payment");
        top.setLayout(new MigLayout());
        bottom.setLayout(new MigLayout());
        holder.setLayout(new MigLayout());
        holder.add(top, "grow,push,wrap");
        holder.add(bottom, "growx,pushx");
        bottom.setBackground(Color.WHITE);
        print = new FredButton("Generate Report");
        model = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int col) {
                return false;
            }
        };
        tab = new JTable();
        delete = new FredButton("Delete Payment");
        back = new FredButton("Back");
        tab.setModel(model);
        pane = new JScrollPane(tab);
        model.setColumnIdentifiers(col);
        top.add(infor, "growx,pushx,wrap");
        top.add(pane, "grow,push");
        bottom.add(print, "growx,pushx,gapleft 20");
        bottom.add(view, "growx,pushx,gapleft 20");
        bottom.add(remove, "growx,pushx,gapleft 20");
        bottom.add(back, "growx,pushx,gapleft 20");
        bottom.add(delete, "growx,pushx,gapleft 20");
        try {
            int total = 0, totalexpected = 0, totalbalance = 0, counter = 0;
            String year = "", termcode = "", classl = "";
            String sql = "Select * from expectedPayments where pycode like '" + "PE" + "%' order by date desc";
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {

                classl = rs.getString("classcode");
                termcode = rs.getString("termcode");
                row[1] = rs.getInt("Date");
                row[8] = rs.getString("Name");
                row[0] = Globals.className(rs.getString("classcode"));
                row[2] = Globals.termname(rs.getString("termcode"));

                year = rs.getString("date");
                pycode = rs.getString("pycode");
                row[4] = rs.getString("Amount");
                String sql2 = "Select max(paid),admnumber,classcode,streamcode,min(balance) from  Reciepts where academicyear='" + year + "' and termcode='" + termcode + "' and paymentid like '" + "PE" + "%' and classcode='" + classl + "' and paymentid='" + pycode + "' group by admnumber order by admnumber";
                ps = con.prepareStatement(sql2);
                ResultSet RS = ps.executeQuery();
                while (RS.next()) {

                    total += Integer.parseInt(RS.getString("max(paid)"));
                    totalexpected += Integer.parseInt(RS.getString("min(balance)"));
                }
                String sql3 = "Select max(balance),admnumber,classcode,streamcode,min(paid) from  Reciepts where academicyear='" + year + "' and termcode='" + termcode + "' and paymentid like '" + "PE" + "%' and classcode='" + classl + "' and paymentid='" + pycode + "' group by admnumber order by admnumber";
                ps = con.prepareStatement(sql3);
                ResultSet rx = ps.executeQuery();
                while (rx.next()) {
                    totalbalance += rx.getInt("max(balance)");

                }


                String sqlb = "Select count(*) as records from reciepts where termcode='" + termcode + "' and academicyear='" + year + "' and classcode='" + classl + "' and paymentmode='" + "None" + "' and paymentid like '" + "PE" + "%'";
                ps = con.prepareStatement(sqlb);
                rx = ps.executeQuery();
                if (rx.next()) {
                    row[3] = rx.getString("records");
                }

                row[5] = totalbalance;
                row[6] = total;
                row[7] = totalexpected;
                model.addRow(row);

                total = 0;
                totalbalance = 0;
                totalexpected = 0;


            }
        } catch (NumberFormatException | SQLException sq) {
            JOptionPane.showMessageDialog(holder, sq.getMessage());
        }
        remove.setEnabled(false);
        back.setEnabled(false);
        print.addActionListener(this);
        delete.addActionListener(this);
        view.addActionListener(this);
        back.addActionListener(this);
        remove.addActionListener(this);
        bottom.setBackground(Color.lightGray);
        return holder;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object obj = e.getSource();
        if (obj == back) {
            remove.setEnabled(false);
            delete.setEnabled(true);
            while (model.getRowCount() > 0) {
                model.removeRow(0);
            }
            model.setColumnIdentifiers(col);
            infor.setText("Custom Payment Summary Report");
            try {
                int total = 0, totalexpected = 0, totalbalance = 0, counter = 0;
                String year = "", termcode = "", classl = "";
                String sql = "Select * from expectedPayments where pycode like '" + "PE" + "%' ";
                ps = con.prepareStatement(sql);
                rs = ps.executeQuery();
                while (rs.next()) {

                    classl = rs.getString("classcode");
                    termcode = rs.getString("termcode");
                    row[1] = rs.getInt("Date");
                    row[8] = rs.getString("Name");
                    row[0] = Globals.className(rs.getString("classcode"));
                    row[2] = Globals.termname(rs.getString("termcode"));

                    year = rs.getString("date");
                    pycode = rs.getString("pycode");
                    row[4] = rs.getString("Amount");
                    String sql2 = "Select paymentid,max(paid),admnumber,classcode,streamcode,min(balance) from  Reciepts where academicyear='" + year + "' and termcode='" + termcode + "' and paymentid like '" + "PE" + "%' and classcode='" + classl + "' group by admnumber order by admnumber";
                    ps = con.prepareStatement(sql2);
                    ResultSet RS = ps.executeQuery();
                    while (RS.next()) {

                        total += Integer.parseInt(RS.getString("max(paid)"));
                        totalexpected += Integer.parseInt(RS.getString("min(balance)"));
                    }
                    String sql3 = "Select paymentid,max(balance),admnumber,classcode,streamcode,min(paid) from  Reciepts where academicyear='" + year + "' and termcode='" + termcode + "' and paymentid like '" + "PE" + "%' and classcode='" + classl + "' group by admnumber order by admnumber";
                    ps = con.prepareStatement(sql3);
                    ResultSet rx = ps.executeQuery();
                    while (rx.next()) {
                        totalbalance += rx.getInt("max(balance)");

                    }


                    String sqlb = "Select count(*) as records from reciepts where termcode='" + termcode + "' and academicyear='" + year + "' and classcode='" + classl + "' and paymentmode='" + "None" + "' and paymentid like '" + "PE" + "%'";
                    ps = con.prepareStatement(sqlb);
                    rx = ps.executeQuery();
                    if (rx.next()) {
                        row[3] = rx.getString("records");
                    }

                    row[5] = totalbalance;
                    row[6] = total;
                    row[7] = totalexpected;
                    model.addRow(row);

                    total = 0;
                    totalbalance = 0;
                    totalexpected = 0;


                }
            } catch (NumberFormatException | SQLException sq) {
                JOptionPane.showMessageDialog(holder, sq.getMessage());
            }

        } else if (obj == remove) {

            if (tab.getSelectedRowCount() < 1) {
                JOptionPane.showMessageDialog(holder, "Kindly Select The Student To Remove\n From The Payment On The Table Obove");
            } else {
                try {
                    int selectedrow = tab.getSelectedRow();
                    int option = JOptionPane.showConfirmDialog(holder, "Are You Sure You Want To Remove\n This Student From Thi Payment?\n  His/Her Payment Details Towards The Payment Will Be Lost", "Confirm Removal", JOptionPane.YES_NO_OPTION);
                    if (option == JOptionPane.NO_OPTION) {
                        JOptionPane.showMessageDialog(holder, "Removal Postponed");
                    } else {

                        String admnumber = model.getValueAt(selectedrow, 1).toString();
                        String sql = "Delete from Reciepts where paymentid='" + pycode + "' and admnumber='" + admnumber + "' and termcode='" + termcode + "'";
                        ps = con.prepareStatement(sql);
                        ps.execute();
                        JOptionPane.showMessageDialog(holder, "Student Removed Successfully from The Payment");

                    }

                } catch (SQLException sq) {
                    JOptionPane.showMessageDialog(holder, sq.getMessage());
                }

            }


        } else if (obj == delete) {
            if (tab.getSelectedRowCount() < 1) {
                JOptionPane.showMessageDialog(holder, "Kindly Select The Payment To Delete");
            } else {
                int selectedrow = tab.getSelectedRow();
                String payname = model.getValueAt(selectedrow, 8).toString();
                String termcode = Globals.termcode(model.getValueAt(selectedrow, 2).toString());
                String year = model.getValueAt(selectedrow, 1).toString().replaceAll("-01-01", "");
                int option = JOptionPane.showConfirmDialog(holder, "Are You Sure Yo Want To Delete This Payment Category??\n Deleting This Payment Causes Total Loss Of Data Concerning \n Payment Made On it", "Confirm Deletion", JOptionPane.YES_NO_OPTION);
                if (option == JOptionPane.NO_OPTION) {
                    JOptionPane.showMessageDialog(holder, "Deletion Postponed");
                } else {
                    try {

                        String sql = "Delete from expectedpayments where name='" + payname + "' and termcode='" + termcode + "' and date='" + year + "'";
                        ps = con.prepareStatement(sql);
                        ps.execute();
                        JOptionPane.showMessageDialog(holder, "Payment Deleted Successffully");
                        model.removeRow(selectedrow);
                    } catch (HeadlessException | SQLException sq) {
                        JOptionPane.showMessageDialog(holder, sq.getMessage());
                    }
                }
            }
        } else if (obj == print) {
            ReportGenerator.generateReport(infor.getText(), "custompayments", tab);
        } else if (obj == view) {
            if (tab.getSelectedRowCount() < 1) {
                JOptionPane.showMessageDialog(holder, "Kindly Select The Payment To View Student Paying Toward It On The Obove Table");
            } else {
                remove.setEnabled(true);
                delete.setEnabled(false);
                back.setEnabled(true);
                try {
                    int selectedrow = tab.getSelectedRow();
                    String payname = model.getValueAt(selectedrow, 8).toString();
                    termcode = Globals.termcode(model.getValueAt(selectedrow, 2).toString());
                    String year = model.getValueAt(selectedrow, 1).toString().replaceAll("-01-01", "");
                    String classcode = Globals.classCode(model.getValueAt(selectedrow, 0).toString());
                    infor.setText("Student Paying" + model.getValueAt(tab.getSelectedRow(), 8));

                    while (model.getRowCount() > 0) {
                        model.removeRow(0);
                    }
                    Object newcols[] = {"NO.", "admission Number", "Name", "Class"};
                    Object newrow[] = new Object[newcols.length];
                    model.setColumnIdentifiers(newcols);
                    String sqll = "Select pycode from expectedpayments where name='" + payname + "' and termcode='" + termcode + "' and date='" + year + "' and classcode='" + classcode + "'";
                    ps = con.prepareStatement(sqll);
                    rs = ps.executeQuery();
                    if (rs.next()) {
                        pycode = rs.getString("pycode");
                    }
                    int counter = 1;
                    String sql = "Select admnumber,classcode from reciepts where paymentid='" + pycode + "'  and termcode='" + termcode + "' and academicyear='" + year + "' and paymentmode='" + "None" + "' order by admnumber";
                    ps = con.prepareStatement(sql);
                    rs = ps.executeQuery();
                    while (rs.next()) {
                        newrow[0] = counter;
                        newrow[1] = rs.getString("Admnumber");

                        newrow[2] = Globals.fullName(rs.getString("Admnumber"));
                        newrow[3] = Globals.className(rs.getString("Classcode"));
                        model.addRow(newrow);
                        counter++;
                    }
                } catch (SQLException sq) {
                    JOptionPane.showMessageDialog(holder, sq.getMessage());
                }


            }


        }
    }

}
