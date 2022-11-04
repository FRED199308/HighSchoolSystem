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
import java.text.SimpleDateFormat;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import net.miginfocom.swing.MigLayout;

/**
 * @author FRED
 */
public class RecieptReprinter implements ActionListener {
    private JPanel bottom;
    private JPanel top;
    private JPanel holder;
    private JTable tab;
    private DefaultTableModel model;
    private JScrollPane pane;
    private JTextField jsearch;
    private FredLabel infor, search;
    private PreparedStatement ps;
    private ResultSet rs;
    private Connection con = DbConnection.connectDb();
    private FredButton print, reverse, details;
    private Object col[] = {"Receipt Number", "admission Number", "Date Paid", "Term", "Academic Year", "Reciept Amount", "Payment Mode", "Date Deposited"};
    private Object row[] = new Object[col.length];
    int total = 0;

    public JPanel reprintRecieptPanel() {
        holder = new JPanel();
        top = new JPanel();

        bottom = new JPanel();
        print = new FredButton("Reprint Reciept");
        search = new FredLabel("Input The Reciept Number or admission Number To Search");
        jsearch = new JTextField();
        reverse = new FredButton("Reverse Reciept");
        top.setLayout(new MigLayout());
        bottom.setLayout(new MigLayout());
        holder.setLayout(new MigLayout());
        holder.add(top, "grow,push,");
        holder.add(bottom, "growy,pushy");
        details = new FredButton("View Full Details");
        //bottom.setBackground(Color.cyan);

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
        top.add(search, "growx,pushx,split");

        top.add(jsearch, "growx,pushx,wrap");
        top.add(pane, "grow,push");
        bottom.add(print, "wrap,Gaptop 50,gapbottom 80");
        bottom.add(details, "wrap,Gaptop 50,gapbottom 80");
        //bottom.add(reverse);


        jsearch.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();
                search.setText("Search Results...");
                if (c == KeyEvent.VK_ENTER) {

                    try {
                        while (model.getRowCount() > 0) {
                            model.removeRow(0);
                        }
                        String sql = "Select * from Reciepts where recieptnumber like '" + jsearch.getText() + "%' or admnumber like '" + jsearch.getText() + "%'  group by recieptnumber";
                        ps = con.prepareStatement(sql);
                        rs = ps.executeQuery();
                        while (rs.next()) {
                            String recieptNo = rs.getString("RecieptNumber");
                            row[0] = rs.getString("RecieptNumber");
                            row[1] = rs.getString("Admnumber");
                            row[2] = rs.getString("Date");
                            row[3] = Globals.termname(rs.getString("term"));
                            row[4] = rs.getString("academicYear");

                            ps = con.prepareStatement("Select Sum(amount) from reciepts where recieptnumber='" + recieptNo + "'");
                            ResultSet rx = ps.executeQuery();
                            if (rx.next()) {
                                row[5] = rx.getDouble("Sum(Amount)");
                            } else {
                                row[5] = "";
                            }


                            row[6] = rs.getString("paymentmode");
                            if (rs.getString("DateDeposited").equalsIgnoreCase("1900-01-01")) {
                                row[7] = "";
                            } else {
                                row[7] = rs.getString("DateDeposited");

                            }

                            model.addRow(row);

                        }

                    } catch (SQLException sq) {
                        JOptionPane.showMessageDialog(holder, sq.getMessage());
                    }


                }

            }

        });
        print.addActionListener(this);
        reverse.addActionListener(this);
        return holder;
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        Object obj = e.getSource();
        if (obj == print) {
            if (tab.getSelectedRowCount() < 1) {
                JOptionPane.showMessageDialog(holder, "Kindly Select The Reciept To Reprint");
            } else {

                int selectedRow = tab.getSelectedRow();

                String adm = tab.getValueAt(selectedRow, 1).toString();
                String recieptno = tab.getValueAt(selectedRow, 0).toString();
                String date = tab.getValueAt(selectedRow, 2).toString();
                String paymode = tab.getValueAt(selectedRow, 6).toString();
                String payname = tab.getValueAt(selectedRow, 7).toString();
                String amount = tab.getValueAt(selectedRow, 5).toString();
                String dateprinted = tab.getValueAt(selectedRow, 2).toString();
                SimpleDateFormat format = new SimpleDateFormat();

                RecieptGenerator.generateReciept(adm, recieptno, date, amount, paymode, dateprinted);

                // RecieptGenerator.generateReciept(adm,  recieptno);  


            }

        } else if (obj == reverse) {


            if (tab.getSelectedRowCount() < 1) {
                JOptionPane.showMessageDialog(holder, "Kindly Select The Reciept To Reverse");
            } else {

                int selectedRow = tab.getSelectedRow();
                if (Integer.parseInt(tab.getValueAt(selectedRow, 5).toString()) == 0) {
                    JOptionPane.showMessageDialog(holder, "Reciept Amount Can Never Be Zero\n This Reciept Refferences A Student Invoice", "Unable To Print Reciept", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    String adm = tab.getValueAt(selectedRow, 1).toString();
                    String recieptno = tab.getValueAt(selectedRow, 0).toString();
                    String date = tab.getValueAt(selectedRow, 8).toString();
                    String paymode = tab.getValueAt(selectedRow, 7).toString();
                    String payname = tab.getValueAt(selectedRow, 6).toString();
                    int amount = ((int) tab.getValueAt(selectedRow, 5)) * -1;
                    String dateprinted = tab.getValueAt(selectedRow, 2).toString();
                    try {
                        int balance = 0;
                        String classcode = "", pycode = "", streamcode;
                        int paid = 0;

                        String sqll = "Select * from reciepts where recieptnumber='" + recieptno + "'";
                        ps = con.prepareStatement(sqll);
                        ResultSet rs1 = ps.executeQuery();
                        while (rs1.next()) {
                            paid = rs1.getInt("paid)");
                            balance = rs1.getInt("min(balance)");
                            classcode = rs1.getString("Classcode");
                            pycode = rs1.getString("paymentid");
                            streamcode = rs1.getString("Stremcode");
                        }
                        int finalBalance = balance - amount;

                        String querry9 = "Select max(paid) from reciepts where termcode='" + Globals.currentTerm() + "' and admnumber='" + adm + "'and academicyear='" + Globals.academicYear() + "'";
                        ps = con.prepareStatement(querry9);
                        ResultSet rx = ps.executeQuery();
                        if (rx.next()) {
                            paid = rx.getInt("max(paid)");
                            paid = amount + paid;

                        }
                        String querry10 = "Select min(balance) from reciepts where termcode='" + Globals.currentTerm() + "' and admnumber='" + adm + "'and academicyear='" + Globals.academicYear() + "'";
                        ps = con.prepareStatement(querry10);
                        rx = ps.executeQuery();
                        if (rx.next()) {
                            paid = rx.getInt("max(paid)");
                            paid = amount + paid;

                        }


                    } catch (SQLException sq) {
                        sq.printStackTrace();
                    }

                }

            }

        }

    }


}
