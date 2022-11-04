/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package highschool;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.JDialog;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextPane;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;

import net.miginfocom.swing.MigLayout;

/**
 * @author FRED
 */
public class ExpenseManager implements ActionListener {
    private JPanel bottom;
    private JPanel top;
    private JPanel holder;
    private JTable tab;
    private DefaultTableModel model;
    private JScrollPane pane;
    private JTextPane desc = new JTextPane();

    private FredLabel infor, filter;
    private FredButton print, back, delete, filterbydate, reprintVocher;
    private FredCombo yearfilter, account, voteheadFilter;
    private PreparedStatement ps;
    private FredTextField jsearch = new FredTextField();
    private ResultSet rs;
    Object cols[] = {"NO", "VoteHead Category", "Expense Name", "Date", "Amount", "Account", "Invoice Number", "Expenditure Ref No."};
    private Connection con = DbConnection.connectDb();
    Object row[] = new Object[cols.length];
    Object addedrow[] = {"Total", "", "", "", "", "", "", ""};

    public JPanel expensePanel() {
        holder = new JPanel();
        top = new JPanel();
        bottom = new JPanel();
        print = new FredButton("Generate Report");
        delete = new FredButton("Delete");
        filterbydate = new FredButton("Filter Payments By Date");
        account = new FredCombo("Filter By Account");
        back = new FredButton("Back");
        reprintVocher = new FredButton("Reprint Payment Voucher");
        yearfilter = new FredCombo("Filter By Year");
        voteheadFilter = new FredCombo("Filter By votehead");
        infor = new FredLabel("Current School Expenditure");
        top.setLayout(new MigLayout());
        bottom.setLayout(new MigLayout());
        holder.setLayout(new MigLayout());
        holder.add(top, "grow,push,wrap");
        holder.add(bottom, "growx,pushx");
        jsearch.setBorder(new TitledBorder("Type To Search A Transaction"));
        tab = new JTable();
        model = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int col) {
                return false;
            }
        };
        pane = new JScrollPane(tab);
        top.add(infor, "gapleft 50");
        top.add(jsearch, "gapleft 50,growx,pushx,wrap");
        top.add(pane, "grow,push,span 2 1,wrap");
        top.add(desc, "growx,span 2 1,pushx");
        desc.setBorder(new TitledBorder("Expense Description"));
        bottom.add(print, "growx,pushx,gapleft 20");
        bottom.add(back, "growx,pushx,gapleft 20");
        bottom.add(account, "growx,pushx,gapleft 20");
        bottom.add(voteheadFilter, "growx,pushx,gapleft 20,wrap");
        bottom.add(yearfilter, "growx,pushx,gapleft 20");
        bottom.add(filterbydate, "growx,pushx,gapleft 20");
        bottom.add(reprintVocher, "growx,pushx,gapleft 20");
        model.setColumnIdentifiers(cols);
        tab.setModel(model);

        try {

            for (int k = 2015; k <= Globals.academicYear(); k++) {
                yearfilter.addItem(k);
            }
            int counter = 1;
            int total = 0;
            String sql = "Select * from spendings";
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                row[0] = counter;
                row[1] = Globals.VoteHeadName(rs.getString("voteheadid"));

                row[2] = rs.getString("Spendingname");
                row[3] = rs.getString("Date");
                row[4] = rs.getString("Amount");
                row[5] = Globals.AccountName(rs.getString("accountid"));
                row[6] = rs.getString("InvoiceNumber");
                total += rs.getInt("Amount");
                row[7] = rs.getString("SpendingID");
                model.addRow(row);
                counter++;

            }

            ps = con.prepareStatement("Select * from Voteheads");
            rs = ps.executeQuery();
            while (rs.next()) {
                voteheadFilter.addItem(rs.getString("VoteheadName"));
            }

            ps = con.prepareStatement("Select accountname,accountid from schoolaccounts");
            rs = ps.executeQuery();
            while (rs.next()) {
                account.addItem(rs.getString("AccountName"));


            }

            addedrow[4] = total;
            model.addRow(addedrow);


        } catch (Exception sq) {
            JOptionPane.showMessageDialog(holder, sq.getMessage());
        }

        jsearch.addKeyListener(new KeyAdapter() {

            public void keyReleased(KeyEvent key) {
                try {

                    while (model.getRowCount() > 0) {
                        model.removeRow(0);
                    }
                    infor.setText("Search Results(limit 100 records)");
                    int total = 0;
                    String sql = "Select * from spendings where SpendingName like '" + jsearch.getText() + "%' or  InvoiceNumber like '" + jsearch.getText() + "%' or RecieptNumber like '" + jsearch.getText() + "%' or Amount like '" + jsearch.getText() + "%' or Comment like '" + jsearch.getText() + "%' or SpendingID like '" + jsearch.getText() + "%' limit 100 ";
                    ps = con.prepareStatement(sql);
                    rs = ps.executeQuery();
                    while (rs.next()) {
                        row[0] = model.getRowCount() + 1;
                        row[1] = Globals.VoteHeadName(rs.getString("voteheadid"));

                        row[2] = rs.getString("Spendingname");
                        row[3] = rs.getString("Date");
                        row[4] = rs.getString("Amount");
                        row[5] = Globals.AccountName(rs.getString("accountid"));
                        row[6] = rs.getString("InvoiceNumber");
                        total += rs.getInt("Amount");
                        row[7] = rs.getString("SpendingID");
                        model.addRow(row);


                    }


                } catch (Exception sq) {
                    sq.printStackTrace();
                }


            }

        });
        print.addActionListener(this);
        filterbydate.addActionListener(this);
        back.addActionListener(this);
        reprintVocher.addActionListener(this);
        yearfilter.addActionListener(this);
        delete.addActionListener(this);
        account.addActionListener(this);
        voteheadFilter.addActionListener(this);

        return holder;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object obj = e.getSource();
        if (obj == reprintVocher) {


        }
        if (obj == yearfilter) {
            if (yearfilter.getSelectedIndex() == 0) {

                while (model.getRowCount() > 0) {
                    model.removeRow(0);
                }

                infor.setText("Current School Expenditure");

                try {
                    int counter = 1;
                    int total = 0;
                    String sql = "Select * from spendings";
                    ps = con.prepareStatement(sql);
                    rs = ps.executeQuery();
                    while (rs.next()) {
                        row[0] = counter;
                        row[1] = Globals.VoteHeadName(rs.getString("voteheadid"));

                        row[7] = rs.getString("SpendingID");
                        row[2] = rs.getString("Spendingname");
                        row[3] = rs.getString("Date");
                        row[4] = rs.getString("Amount");
                        row[5] = Globals.AccountName(rs.getString("accountId"));
                        row[6] = rs.getString("InvoiceNumber");
                        total += rs.getInt("Amount");

                        model.addRow(row);
                        counter++;

                    }
                    addedrow[4] = total;
                    model.addRow(addedrow);


                } catch (Exception sq) {
                    JOptionPane.showMessageDialog(holder, sq.getMessage());
                }


            } else {

                while (model.getRowCount() > 0) {
                    model.removeRow(0);
                }

                infor.setText(" School Expenditure For Academic Year " + yearfilter.getSelectedItem());

                try {
                    int counter = 1;
                    int total = 0;
                    String sql = "Select * from spendings where year(Date)='" + yearfilter.getSelectedItem() + "'";
                    ps = con.prepareStatement(sql);
                    rs = ps.executeQuery();
                    while (rs.next()) {
                        row[0] = counter;
                        row[1] = Globals.VoteHeadName(rs.getString("voteheadid"));

                        row[2] = rs.getString("Spendingname");
                        row[3] = rs.getString("Date");
                        row[4] = rs.getString("Amount");
                        row[5] = Globals.AccountName(rs.getString("accountId"));
                        row[6] = rs.getString("InvoiceNumber");
                        total += rs.getInt("Amount");
                        row[7] = rs.getString("SpendingID");
                        model.addRow(row);
                        counter++;

                    }
                    addedrow[4] = total;
                    model.addRow(addedrow);


                } catch (Exception sq) {
                    JOptionPane.showMessageDialog(holder, sq.getMessage());
                }


            }


        }
        if (obj == print) {
            ReportGenerator.generateReport(infor.getText(), "expensis", tab);
        } else if (obj == back) {
            while (model.getRowCount() > 0) {
                model.removeRow(0);
            }

            infor.setText("Current School Expenditure");

            try {
                int counter = 1;
                int total = 0;
                String sql = "Select * from spendings";
                ps = con.prepareStatement(sql);
                rs = ps.executeQuery();
                while (rs.next()) {
                    row[0] = counter;

                    row[1] = Globals.VoteHeadName(rs.getString("voteheadid"));

                    row[2] = rs.getString("Spendingname");
                    row[3] = rs.getString("Date");
                    row[4] = rs.getString("Amount");
                    row[5] = Globals.AccountName(rs.getString("accountId"));
                    row[6] = rs.getString("InvoiceNumber");
                    total += rs.getInt("Amount");
                    row[7] = rs.getString("SpendingID");
                    model.addRow(row);
                    counter++;

                }
                addedrow[4] = total;
                model.addRow(addedrow);


            } catch (Exception sq) {
                JOptionPane.showMessageDialog(holder, sq.getMessage());
            }

        } else if (obj == filterbydate) {
            String interval = "";
            SimpleDateFormat frm = new SimpleDateFormat("yyyy/MM/dd");
            interval = JOptionPane.showInputDialog(null, "Kindly Enter The Duration Interval To View Its Equivalent Fee Paid", "2012/12/31-" + frm.format(new Date()));
            if (DataValidation.dateInteval(interval)) {
                if (account.getSelectedIndex() == 0 & voteheadFilter.getSelectedIndex() == 0) {

                    while (model.getRowCount() > 0) {
                        model.removeRow(0);
                    }

                    try {

                        int k = interval.indexOf("-");
                        String lowerDate = interval.substring(0, k);
                        String upperDate = interval.substring(k + 1);
                        infor.setText("Expenses Incurred Between " + lowerDate + " And " + upperDate);
                        int counter = 1;
                        int total = 0;
                        String sql = "Select * from spendings where Date BETWEEN '" + lowerDate + "' and '" + upperDate + "' ";
                        ps = con.prepareStatement(sql);
                        rs = ps.executeQuery();
                        while (rs.next()) {
                            row[0] = counter;
                            row[1] = Globals.VoteHeadName(rs.getString("voteheadid"));

                            row[2] = rs.getString("Spendingname");
                            row[3] = rs.getString("Date");
                            row[4] = rs.getString("Amount");
                            row[5] = Globals.AccountName(rs.getString("accountId"));
                            row[6] = rs.getString("InvoiceNumber");
                            total += rs.getInt("Amount");

                            model.addRow(row);
                            counter++;

                        }
                        addedrow[4] = total;
                        model.addRow(addedrow);


                    } catch (Exception sq) {
                        JOptionPane.showMessageDialog(holder, sq.getMessage());
                    }

                } else {

                    if (account.getSelectedIndex() > 0) {
                        if (voteheadFilter.getSelectedIndex() > 0) {
                            while (model.getRowCount() > 0) {
                                model.removeRow(0);
                            }

                            try {

                                int k = interval.indexOf("-");
                                String lowerDate = interval.substring(0, k);
                                String upperDate = interval.substring(k + 1);
                                infor.setText(voteheadFilter.getSelectedItem() + " Expenditure Incurred Between " + lowerDate + " And " + upperDate + " on " + account.getSelectedItem());
                                int counter = 1;
                                int total = 0;
                                String sql = "Select * from spendings where Date BETWEEN '" + lowerDate + "' and '" + upperDate + "' and voteheadid='" + Globals.voteHeadId(voteheadFilter.getSelectedItem().toString()) + "' and accountid='" + Globals.AccountCode(account.getSelectedItem().toString()) + "'";
                                ps = con.prepareStatement(sql);
                                rs = ps.executeQuery();
                                while (rs.next()) {
                                    row[0] = counter;
                                    row[1] = Globals.VoteHeadName(rs.getString("voteheadid"));

                                    row[2] = rs.getString("Spendingname");
                                    row[3] = rs.getString("Date");
                                    row[4] = rs.getString("Amount");
                                    row[5] = Globals.AccountName(rs.getString("accountId"));
                                    row[6] = rs.getString("InvoiceNumber");
                                    total += rs.getInt("Amount");

                                    model.addRow(row);
                                    counter++;

                                }
                                addedrow[4] = total;
                                model.addRow(addedrow);


                            } catch (Exception sq) {
                                JOptionPane.showMessageDialog(holder, sq.getMessage());
                            }

                        } else {

                            while (model.getRowCount() > 0) {
                                model.removeRow(0);
                            }

                            try {

                                int k = interval.indexOf("-");
                                String lowerDate = interval.substring(0, k);
                                String upperDate = interval.substring(k + 1);
                                infor.setText(voteheadFilter.getSelectedItem() + " Expenditure Incurred Between " + lowerDate + " And " + upperDate);
                                int counter = 1;
                                int total = 0;
                                String sql = "Select * from spendings where Date BETWEEN '" + lowerDate + "' and '" + upperDate + "' and voteheadid='" + Globals.voteHeadId(voteheadFilter.getSelectedItem().toString()) + "'";
                                ps = con.prepareStatement(sql);
                                rs = ps.executeQuery();
                                while (rs.next()) {
                                    row[0] = counter;
                                    row[1] = Globals.VoteHeadName(rs.getString("voteheadid"));

                                    row[2] = rs.getString("Spendingname");
                                    row[3] = rs.getString("Date");
                                    row[4] = rs.getString("Amount");
                                    row[5] = Globals.AccountName(rs.getString("accountId"));
                                    row[6] = rs.getString("InvoiceNumber");
                                    total += rs.getInt("Amount");

                                    model.addRow(row);
                                    counter++;

                                }
                                addedrow[4] = total;
                                model.addRow(addedrow);


                            } catch (Exception sq) {
                                JOptionPane.showMessageDialog(holder, sq.getMessage());
                            }


                        }

                    }


                }

            }


        }
        if (obj == account) {
            if (account.getSelectedIndex() == 0) {

                while (model.getRowCount() > 0) {
                    model.removeRow(0);
                }

                infor.setText("Current School Expenditure");

                try {
                    int counter = 1;
                    int total = 0;
                    String sql = "Select * from spendings";
                    ps = con.prepareStatement(sql);
                    rs = ps.executeQuery();
                    while (rs.next()) {
                        row[0] = counter;
                        row[1] = Globals.VoteHeadName(rs.getString("voteheadid"));
                        row[7] = rs.getString("SpendingID");
                        row[2] = rs.getString("Spendingname");
                        row[3] = rs.getString("Date");
                        row[4] = rs.getString("Amount");
                        row[5] = Globals.AccountName(rs.getString("AccountID"));
                        row[6] = rs.getString("InvoiceNumber");
                        total += rs.getInt("Amount");

                        model.addRow(row);
                        counter++;

                    }
                    addedrow[4] = total;
                    model.addRow(addedrow);


                } catch (Exception sq) {
                    JOptionPane.showMessageDialog(holder, sq.getMessage());
                }


            } else {

                while (model.getRowCount() > 0) {
                    model.removeRow(0);
                }

                infor.setText("Current School Expenditure On " + account.getSelectedItem());

                try {
                    int counter = 1;
                    int total = 0;
                    String sql = "Select * from spendings where accountId='" + Globals.AccountCode(account.getSelectedItem().toString()) + "'";
                    ps = con.prepareStatement(sql);
                    rs = ps.executeQuery();
                    while (rs.next()) {
                        row[0] = counter;

                        row[1] = Globals.VoteHeadName(rs.getString("voteheadid"));

                        row[7] = rs.getString("SpendingID");
                        row[2] = rs.getString("Spendingname");
                        row[3] = rs.getString("Date");
                        row[4] = rs.getString("Amount");
                        row[5] = Globals.AccountName(rs.getString("accountId"));
                        row[6] = rs.getString("InvoiceNumber");
                        total += rs.getInt("Amount");

                        model.addRow(row);
                        counter++;

                    }
                    addedrow[4] = total;
                    model.addRow(addedrow);


                } catch (Exception sq) {
                    JOptionPane.showMessageDialog(holder, sq.getMessage());
                    sq.printStackTrace();
                }


            }

        } else if (obj == reprintVocher) {

            if (tab.getSelectedRowCount() < 1) {
                JOptionPane.showMessageDialog(holder, "Select A Payment Record to Reprint voucher");
            } else {

                FredButton sv = new FredButton("Save");
                FredButton cn = new FredButton("Close");
                FredLabel payeename = new FredLabel("Payee Name");
                FredLabel payeeadress = new FredLabel("Payee Address");
                FredLabel payeePhone = new FredLabel("Phone");
                FredLabel id = new FredLabel("Payee ID Number");
                FredTextField jpayeename = new FredTextField();
                FredTextField jpayeeadress = new FredTextField();
                FredTextField jpayeePhone = new FredTextField();
                FredTextField jid = new FredTextField();
                JDialog dia = new JDialog();
                dia.setSize(500, 350);
                dia.setTitle("Payee Details");
                dia.setLayout(null);
                dia.setLocationRelativeTo(CurrentFrame.secondFrame());
                dia.setIconImage(FrameProperties.icon());
                payeename.setBounds(30, 30, 150, 30);
                dia.add(payeename);
                jpayeename.setBounds(200, 30, 180, 30);
                dia.add(jpayeename);
                payeeadress.setBounds(30, 100, 150, 30);
                dia.add(payeeadress);
                jpayeeadress.setBounds(200, 100, 180, 30);
                dia.add(jpayeeadress);

                payeePhone.setBounds(30, 170, 150, 30);
                dia.add(payeePhone);
                jpayeePhone.setBounds(200, 170, 180, 30);
                dia.add(jpayeePhone);

                id.setBounds(30, 240, 150, 30);
                dia.add(id);
                jid.setBounds(200, 240, 180, 30);
                dia.add(jid);

                cn.setBounds(70, 280, 100, 30);
                dia.add(cn);
                sv.setBounds(350, 280, 100, 30);
                dia.add(sv);
                dia.setVisible(true);

                cn.addActionListener(new ActionListener() {

                    @Override
                    public void actionPerformed(ActionEvent e) {
                        dia.dispose();


                    }
                });
                sv.addActionListener(new ActionListener() {

                    @Override
                    public void actionPerformed(ActionEvent e) {

                        if (jpayeename.getText().isEmpty()) {
                            JOptionPane.showMessageDialog(dia, "Input Valid Payee Name Details");
                        } else {

                            int selectedRow = tab.getSelectedRow();
                            String Date = tab.getValueAt(selectedRow, 3).toString();
                            String invoicenumber = tab.getValueAt(selectedRow, 6).toString();
                            String spendid = tab.getValueAt(selectedRow, 7).toString();
                            String amount1 = tab.getValueAt(selectedRow, 4).toString();
                            String exid = "";
                            String desc = "", paymode = "";
                            try {
                                ps = con.prepareStatement("Select * from spendings where spendingid='" + spendid + "'");
                                rs = ps.executeQuery();
                                if (rs.next()) {
                                    desc = rs.getString("Comment");
                                    exid = rs.getString("VoteHeadid");
                                    paymode = rs.getString("PaymentMode");
                                }


                                ReportGenerator.paymentVocherGenerator(spendid,
                                        jpayeename.getText(), jpayeeadress.getText()
                                        , Date,
                                        invoicenumber, desc,
                                        amount1, paymode,
                                        exid,
                                        jid.getText() + "  Phone: " + jpayeePhone.getText());

                            } catch (Exception sq) {
                                sq.printStackTrace();
                                JOptionPane.showMessageDialog(null, sq.getMessage());
                            }


                        }

                    }
                });


            }


        } else if (obj == voteheadFilter) {
            if (voteheadFilter.getSelectedIndex() > 0) {


                if (account.getSelectedIndex() == 0) {


                    while (model.getRowCount() > 0) {
                        model.removeRow(0);
                    }

                    infor.setText(voteheadFilter.getSelectedItem() + " Current School Expenditure");

                    try {
                        int counter = 1;
                        int total = 0;
                        String sql = "Select * from spendings where voteheadid='" + Globals.voteHeadId(voteheadFilter.getSelectedItem().toString()) + "'";
                        ps = con.prepareStatement(sql);
                        rs = ps.executeQuery();
                        while (rs.next()) {
                            row[0] = counter;

                            row[1] = voteheadFilter.getSelectedItem();

                            row[7] = rs.getString("SpendingID");
                            row[2] = rs.getString("Spendingname");
                            row[3] = rs.getString("Date");
                            row[4] = rs.getString("Amount");
                            row[5] = Globals.AccountName(rs.getString("accountId"));
                            row[6] = rs.getString("InvoiceNumber");
                            total += rs.getInt("Amount");

                            model.addRow(row);
                            counter++;

                        }
                        addedrow[4] = total;
                        model.addRow(addedrow);


                    } catch (Exception sq) {
                        JOptionPane.showMessageDialog(holder, sq.getMessage());
                        sq.printStackTrace();
                    }


                } else {


                    while (model.getRowCount() > 0) {
                        model.removeRow(0);
                    }

                    infor.setText(voteheadFilter.getSelectedItem() + " Current School Expenditure On " + account.getSelectedItem());

                    try {
                        int counter = 1;
                        int total = 0;
                        String sql = "Select * from spendings where accountid='" + Globals.AccountCode(account.getSelectedItem().toString()) + "' and voteheadid='" + Globals.voteHeadId(voteheadFilter.getSelectedItem().toString()) + "'";
                        ps = con.prepareStatement(sql);
                        rs = ps.executeQuery();
                        while (rs.next()) {
                            row[0] = counter;

                            row[1] = voteheadFilter.getSelectedItem();

                            row[7] = rs.getString("SpendingID");
                            row[2] = rs.getString("Spendingname");
                            row[3] = rs.getString("Date");
                            row[4] = rs.getString("Amount");
                            row[5] = Globals.AccountName(rs.getString("accountId"));
                            row[6] = rs.getString("InvoiceNumber");
                            total += rs.getInt("Amount");

                            model.addRow(row);
                            counter++;

                        }
                        addedrow[4] = total;
                        model.addRow(addedrow);


                    } catch (Exception sq) {
                        JOptionPane.showMessageDialog(holder, sq.getMessage());
                        sq.printStackTrace();
                    }


                }

            }


        }


    }
}
