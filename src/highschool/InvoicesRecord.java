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
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;

import net.miginfocom.swing.MigLayout;

/**
 * @author FRED
 */
public class InvoicesRecord implements ActionListener {

    private JPanel bottom;
    private JPanel top;
    private JPanel holder;
    private JTable tab;
    private DefaultTableModel model;
    private JScrollPane pane;
    private FredCheckBox totalexpected, cleared;
    private FredLabel infor, filter, search, campany, amount, dateRecieved, campanyinvoiceNumber;
    private FredButton print, back, add, freeze, filterbydate, filterbyamount, EditInvoice, save, update;
    private PreparedStatement ps;
    private ResultSet rs;
    private FredCombo termfilter, yearfilter, status;
    private JTextPane panel = new JTextPane();
    private FredDateChooser jpaydate;
    private JTextField jsearch, jcampany, jamount, jcampanyinvoiceNumber;
    private JTextPane panel2 = new JTextPane();
    private Connection con = DbConnection.connectDb();
    private Object cols[] = {"Invoice Number", "Campany/Supplier", "Date Recieved", "Amount", "Status", "Date Paid", "Term Name", "CampanyInvoice No."};
    private Object row[] = new Object[cols.length];
    private String Array[] = {"Total", "", "", "", "", "", "", ""};
    private String INVOICENUMBER = "";
    int total = 0;

    public InvoicesRecord() {


    }

    public JPanel invoiceManagerPanel() {


        holder = new JPanel();
        top = new JPanel();
        bottom = new JPanel();
        print = new FredButton("Generate Report");
        infor = new FredLabel("Invoice Record ");
        top.setLayout(new MigLayout());
        bottom.setLayout(new MigLayout());
        holder.setLayout(new MigLayout());
        holder.add(top, "grow,push,wrap");
        holder.add(bottom, "growx,pushx");
        bottom.setBackground(Color.WHITE);
        tab = new JTable();
        model = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int col) {
                return false;
            }
        };
        pane = new JScrollPane(tab);
        model.setColumnIdentifiers(cols);
        tab.setModel(model);
        panel2.setBorder(new TitledBorder("Invoice Description"));
        print = new FredButton("Generate Reports");
        back = new FredButton("Back");
        add = new FredButton("Add new Invoice");
        freeze = new FredButton("Freeze Invoice");
        filterbydate = new FredButton("Filter Invoice By Date");
        filterbyamount = new FredButton("Filter Invoice By Amount");
        termfilter = new FredCombo("Filter By Term");
        yearfilter = new FredCombo("Filter By Year");
        status = new FredCombo("Filter By Status");
        status.addItem("Pending");
        status.addItem("Paid");
        status.addItem("Freezed");
        jsearch = new JTextField();
        EditInvoice = new FredButton("Edit Invoice Details");
        search = new FredLabel("Search Invoice By InvoiceNumber Or Campany/Supplier Name");
        top.add(infor, "gapleft 30,growx,pushx");
        top.add(search, "gapleft 30");
        top.add(jsearch, "gapleft 30,growx,pushx,wrap");
        top.add(pane, "grow,push,span,wrap");
        top.add(panel2, "growx,pushx,span");
        bottom.add(print, "growx,pushx");
        bottom.add(add, "growx,pushx");
        bottom.add(back, "growx,pushx");
        bottom.add(freeze, "growx,pushx");
        bottom.add(EditInvoice, "growx,pushx,wrap");
        bottom.add(filterbyamount, "growx,pushx");
        bottom.add(filterbydate, "growx,pushx");
        bottom.add(status, "growx,pushx");
        bottom.add(termfilter, "growx,pushx");
        bottom.add(yearfilter, "growx,pushx");
        holder.add(top, "grow,push,wrap");
        holder.add(bottom, "growx,pushx");

        campany = new FredLabel("Campany Name/Supplier Name");
        campanyinvoiceNumber = new FredLabel("Campany Invoice ID");
        amount = new FredLabel("Invoice Total Amount");
        dateRecieved = new FredLabel("Date Recieved");
        save = new FredButton("Save");
        jamount = new JTextField();
        jcampany = new JTextField();
        jpaydate = new FredDateChooser();
        jpaydate.setMaxSelectableDate(new Date());
        panel.setBorder(new TitledBorder("Invoice Description"));
        jcampanyinvoiceNumber = new JTextField();
        jpaydate.setDateFormatString("yyyy/MM/dd");
        update = new FredButton("Update Invoice Details");
        freeze.addActionListener(this);
        add.addActionListener(this);
        back.addActionListener(this);
        termfilter.addActionListener(this);
        yearfilter.addActionListener(this);
        EditInvoice.addActionListener(this);
        update.addActionListener(this);
        filterbyamount.addActionListener(this);
        print.addActionListener(this);
        filterbydate.addActionListener(this);
        try {
            String sql = "Select * from invoices order by daterecieved";
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                row[0] = rs.getString("LocalInvoiceNumber");
                row[1] = rs.getString("campany");
                row[2] = rs.getString("DateRecieved");
                row[3] = rs.getString("Amount");
                row[4] = rs.getString("Status");
                total += rs.getInt("amount");
                if (rs.getString("DatePaid").equalsIgnoreCase("1900-01-01")) {
                    row[5] = "";
                } else {
                    row[5] = rs.getString("DatePaid");
                }
                row[6] = Globals.termname(rs.getString("termcode"));
                row[7] = rs.getString("CampanyInvoiceNumber");
                model.addRow(row);

            }
            Array[3] = String.valueOf(total);
            model.addRow(Array);
            total = 0;
            String sql2 = "Select termname from terms order by precisions";
            ps = con.prepareStatement(sql2);
            rs = ps.executeQuery();
            while (rs.next()) {
                termfilter.addItem(rs.getString("termname"));
            }
            for (int k = 2015; k <= Globals.academicYear(); k++) {
                yearfilter.addItem(k);
            }


        } catch (SQLException sq) {
            JOptionPane.showMessageDialog(holder, sq.getMessage());
        }

        tab.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (tab.getSelectedRowCount() > 0) {
                    int selectedRow = tab.getSelectedRow();
                    try {
                        String invoiceNumber = model.getValueAt(selectedRow, 0).toString();
                        String sql = "Select Description from invoices where localinvoicenumber='" + invoiceNumber + "'";
                        ps = con.prepareStatement(sql);
                        rs = ps.executeQuery();
                        if (rs.next()) {
                            panel2.setText(rs.getString("Description"));
                        }
                    } catch (SQLException sq) {
                        JOptionPane.showMessageDialog(holder, sq.getMessage());
                    }
                }


            }

        });
        jsearch.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();

                try {
                    while (model.getRowCount() > 0) {
                        model.removeRow(0);
                    }
                    String sql = "Select * from invoices where campany like '" + jsearch.getText() + "%' or localinvoicenumber like '" + jsearch.getText() + "%' or campanyinvoiceNumber like '" + jsearch.getText() + "%'";
                    ps = con.prepareStatement(sql);
                    rs = ps.executeQuery();
                    while (rs.next()) {
                        row[0] = rs.getString("LocalInvoiceNumber");
                        row[1] = rs.getString("campany");
                        row[2] = rs.getString("DateRecieved");
                        row[3] = rs.getString("Amount");
                        row[4] = rs.getString("Status");
                        total += rs.getInt("amount");
                        if (rs.getString("DatePaid").equalsIgnoreCase("1900-01-01")) {
                            row[5] = "";
                        } else {
                            row[5] = rs.getString("DatePaid");
                        }
                        row[6] = Globals.termname(rs.getString("termcode"));
                        row[7] = rs.getString("CampanyInvoiceNumber");
                        model.addRow(row);

                    }
                    Array[3] = String.valueOf(total);
                    model.addRow(Array);
                    total = 0;
                } catch (SQLException sq) {
                    JOptionPane.showMessageDialog(holder, sq.getMessage());
                }

            }

        });


        return holder;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object obj = e.getSource();

        if (obj == filterbydate) {
            String interval = "";
            SimpleDateFormat frm = new SimpleDateFormat("yyyy/MM/dd");
            interval = JOptionPane.showInputDialog(null, "Kindly Enter The Duration Interval To View Invoices posted", "2012/12/31-" + frm.format(new Date()));
            if (DataValidation.dateInteval(interval)) {

                int k = interval.indexOf("-");
                String lowerDate = interval.substring(0, k);
                String upperDate = interval.substring(k + 1);
                while (model.getRowCount() > 0) {
                    model.removeRow(0);
                }
                if (status.getSelectedIndex() == 0) {
                    while (model.getRowCount() > 0) {
                        model.removeRow(0);
                    }
                    infor.setText("Invoice Record  Between " + lowerDate + " And " + upperDate);
                    try {

                        String sql = "Select * from invoices where  Daterecieved BETWEEN '" + lowerDate + "' and '" + upperDate + "' order by daterecieved";
                        ps = con.prepareStatement(sql);
                        rs = ps.executeQuery();
                        while (rs.next()) {
                            row[0] = rs.getString("LocalInvoiceNumber");
                            row[1] = rs.getString("campany");
                            row[2] = rs.getString("DateRecieved");
                            row[3] = rs.getString("Amount");
                            row[4] = rs.getString("Status");
                            total += rs.getInt("Amount");
                            if (rs.getString("DatePaid").equalsIgnoreCase("1900-01-01")) {
                                row[5] = "";
                            } else {
                                row[5] = rs.getString("DatePaid");
                            }
                            row[6] = Globals.termname(rs.getString("termcode"));
                            row[7] = rs.getString("CampanyInvoiceNumber");
                            model.addRow(row);
                        }
                        Array[3] = String.valueOf(total);
                        model.addRow(Array);
                        total = 0;

                    } catch (SQLException sq) {
                        JOptionPane.showMessageDialog(holder, sq.getMessage());
                    }


                } else {


                    try {
                        infor.setText(status.getSelectedItem() + " Invoice Record Between " + lowerDate + " And " + upperDate);
                        String sql = "Select * from invoices where status='" + status.getSelectedItem() + "' and Daterecieved BETWEEN '" + lowerDate + "' and '" + upperDate + "'  order by daterecieved";
                        ps = con.prepareStatement(sql);
                        rs = ps.executeQuery();
                        while (rs.next()) {
                            row[0] = rs.getString("LocalInvoiceNumber");
                            row[1] = rs.getString("campany");
                            row[2] = rs.getString("DateRecieved");
                            row[3] = rs.getString("Amount");
                            row[4] = rs.getString("Status");
                            total += rs.getInt("Amount");
                            if (rs.getString("DatePaid").equalsIgnoreCase("1900-01-01")) {
                                row[5] = "";
                            } else {
                                row[5] = rs.getString("DatePaid");
                            }
                            row[6] = Globals.termname(rs.getString("termcode"));
                            row[7] = rs.getString("CampanyInvoiceNumber");
                            model.addRow(row);
                        }
                        Array[3] = String.valueOf(total);
                        model.addRow(Array);
                        total = 0;

                    } catch (SQLException sq) {
                        JOptionPane.showMessageDialog(holder, sq.getMessage());
                    }


                }
            } else {
                JOptionPane.showMessageDialog(holder, "Incorrect Date Format");
            }
        }
        if (obj == add) {
            top.removeAll();

            top.add(campany, "gapleft 50,gaptop 40,growx,pushx");
            top.add(jcampany, "gapleft 50,gaptop 40,growx,pushx,wrap");
            top.add(campanyinvoiceNumber, "gapleft 50,gaptop 40,growx,pushx");
            top.add(jcampanyinvoiceNumber, "gapleft 50,gaptop 40,growx,pushx,wrap");
            top.add(dateRecieved, "gapleft 50,gaptop 40,growx,pushx");
            top.add(jpaydate, "gapleft 50,gaptop 40,growx,pushx,wrap");
            top.add(amount, "gapleft 50,gaptop 40,growx,pushx");
            top.add(jamount, "gapleft 50,gaptop 40,growx,pushx,wrap");
            top.add(panel, "gapleft 100,width 100:400:600,height 150:200:200,wrap");
            top.add(save, "gaptop 50,gapleft 100");
            top.revalidate();
            top.repaint();
            save.addActionListener(this);
            jamount.addKeyListener(new KeyAdapter() {
                @Override
                public void keyTyped(KeyEvent e) {
                    char c = e.getKeyChar();
                    if (!Character.isDigit(c) || jamount.getText().length() > 10) {

                        e.consume();
                    }

                }

            });

        } else if (obj == print) {
            ReportGenerator.generateReport(infor.getText(), "Invoices", tab);
        } else if (obj == termfilter) {
            while (model.getRowCount() > 0) {
                model.removeRow(0);
            }
            if (yearfilter.getSelectedIndex() == 0) {
                if (status.getSelectedIndex() == 0) {
                    try {
                        infor.setText("Invoice Record For " + termfilter.getSelectedItem());
                        String sql = "Select * from invoices where termcode='" + Globals.termcode(termfilter.getSelectedItem().toString()) + "' order by daterecieved";
                        ps = con.prepareStatement(sql);
                        rs = ps.executeQuery();
                        while (rs.next()) {
                            row[0] = rs.getString("LocalInvoiceNumber");
                            row[1] = rs.getString("campany");
                            row[2] = rs.getString("DateRecieved");
                            row[3] = rs.getString("Amount");
                            row[4] = rs.getString("Status");
                            total += rs.getInt("Amount");
                            if (rs.getString("DatePaid").equalsIgnoreCase("1900-01-01")) {
                                row[5] = "";
                            } else {
                                row[5] = rs.getString("DatePaid");
                            }
                            row[6] = Globals.termname(rs.getString("termcode"));
                            row[7] = rs.getString("CampanyInvoiceNumber");
                            model.addRow(row);
                        }

                        Array[3] = String.valueOf(total);
                        model.addRow(Array);
                        total = 0;
                    } catch (SQLException sq) {
                        JOptionPane.showMessageDialog(holder, sq.getMessage());
                    }

                } else {
                    try {
                        infor.setText(status.getSelectedItem() + " Invoice Record For " + termfilter.getSelectedItem());
                        String sql = "Select * from invoices where termcode='" + Globals.termcode(termfilter.getSelectedItem().toString()) + "' and status='" + status.getSelectedItem() + "' order by daterecieved";
                        ps = con.prepareStatement(sql);
                        rs = ps.executeQuery();
                        while (rs.next()) {
                            total += rs.getInt("Amount");
                            row[0] = rs.getString("LocalInvoiceNumber");
                            row[1] = rs.getString("campany");
                            row[2] = rs.getString("DateRecieved");
                            row[3] = rs.getString("Amount");
                            row[4] = rs.getString("Status");

                            if (rs.getString("DatePaid").equalsIgnoreCase("1900-01-01")) {
                                row[5] = "";
                            } else {
                                row[5] = rs.getString("DatePaid");
                            }
                            row[6] = Globals.termname(rs.getString("termcode"));
                            row[7] = rs.getString("CampanyInvoiceNumber");
                            model.addRow(row);
                        }
                        Array[3] = String.valueOf(total);
                        model.addRow(Array);
                        total = 0;

                    } catch (SQLException sq) {
                        JOptionPane.showMessageDialog(holder, sq.getMessage());
                    }

                }
            } else {
                if (status.getSelectedIndex() == 0) {

                    try {
                        infor.setText("Invoice Record For " + termfilter.getSelectedItem() + " For Academic Year" + yearfilter.getSelectedItem());
                        String sql = "Select * from invoices where termcode='" + Globals.termcode(termfilter.getSelectedItem().toString()) + "' and academicyear='" + yearfilter.getSelectedItem() + "' order by daterecieved";
                        ps = con.prepareStatement(sql);
                        rs = ps.executeQuery();
                        while (rs.next()) {
                            row[0] = rs.getString("LocalInvoiceNumber");
                            row[1] = rs.getString("campany");
                            row[2] = rs.getString("DateRecieved");
                            row[3] = rs.getString("Amount");
                            row[4] = rs.getString("Status");
                            total += rs.getInt("Amount");
                            if (rs.getString("DatePaid").equalsIgnoreCase("1900-01-01")) {
                                row[5] = "";
                            } else {
                                row[5] = rs.getString("DatePaid");
                            }
                            row[6] = Globals.termname(rs.getString("termcode"));
                            row[7] = rs.getString("CampanyInvoiceNumber");
                            model.addRow(row);
                        }
                        Array[3] = String.valueOf(total);
                        model.addRow(Array);
                        total = 0;

                    } catch (SQLException sq) {
                        JOptionPane.showMessageDialog(holder, sq.getMessage());
                    }

                } else {
                    try {
                        infor.setText(status.getSelectedItem() + " Invoice Record For " + termfilter.getSelectedItem() + " For Academic Year " + yearfilter.getSelectedItem());
                        String sql = "Select * from invoices where termcode='" + Globals.termcode(termfilter.getSelectedItem().toString()) + "' and status='" + status.getSelectedItem() + "' and academicyear='" + yearfilter.getSelectedItem() + "' order by daterecieved";
                        ps = con.prepareStatement(sql);
                        rs = ps.executeQuery();
                        while (rs.next()) {
                            row[0] = rs.getString("LocalInvoiceNumber");
                            row[1] = rs.getString("campany");
                            row[2] = rs.getString("DateRecieved");
                            row[3] = rs.getString("Amount");
                            row[4] = rs.getString("Status");
                            total += rs.getInt("Amount");
                            if (rs.getString("DatePaid").equalsIgnoreCase("1900-01-01")) {
                                row[5] = "";
                            } else {
                                row[5] = rs.getString("DatePaid");
                            }
                            row[6] = Globals.termname(rs.getString("termcode"));
                            row[7] = rs.getString("CampanyInvoiceNumber");
                            model.addRow(row);
                        }
                        Array[3] = String.valueOf(total);
                        model.addRow(Array);
                        total = 0;

                    } catch (SQLException sq) {
                        JOptionPane.showMessageDialog(holder, sq.getMessage());
                    }

                }

            }

        } else if (obj == yearfilter) {
            while (model.getRowCount() > 0) {
                model.removeRow(0);
            }
            if (yearfilter.getSelectedIndex() == 0) {
                if (termfilter.getSelectedIndex() == 0) {

                    if (status.getSelectedIndex() == 0) {
                        while (model.getRowCount() > 0) {
                            model.removeRow(0);
                        }
                        infor.setText("Invoice Record Academic Year");
                        try {

                            String sql = "Select * from invoices  order by daterecieved";
                            ps = con.prepareStatement(sql);
                            rs = ps.executeQuery();
                            while (rs.next()) {
                                row[0] = rs.getString("LocalInvoiceNumber");
                                row[1] = rs.getString("campany");
                                row[2] = rs.getString("DateRecieved");
                                row[3] = rs.getString("Amount");
                                row[4] = rs.getString("Status");
                                total += rs.getInt("Amount");
                                if (rs.getString("DatePaid").equalsIgnoreCase("1900-01-01")) {
                                    row[5] = "";
                                } else {
                                    row[5] = rs.getString("DatePaid");
                                }
                                row[6] = Globals.termname(rs.getString("termcode"));
                                row[7] = rs.getString("CampanyInvoiceNumber");
                                model.addRow(row);
                            }
                            Array[3] = String.valueOf(total);
                            model.addRow(Array);
                            total = 0;

                        } catch (SQLException sq) {
                            JOptionPane.showMessageDialog(holder, sq.getMessage());
                        }


                    } else {


                        try {
                            infor.setText(status.getSelectedItem() + " Invoice Record Academic Year");
                            String sql = "Select * from invoices where status='" + status.getSelectedItem() + "' order by daterecieved";
                            ps = con.prepareStatement(sql);
                            rs = ps.executeQuery();
                            while (rs.next()) {
                                row[0] = rs.getString("LocalInvoiceNumber");
                                row[1] = rs.getString("campany");
                                row[2] = rs.getString("DateRecieved");
                                row[3] = rs.getString("Amount");
                                row[4] = rs.getString("Status");
                                total += rs.getInt("Amount");
                                if (rs.getString("DatePaid").equalsIgnoreCase("1900-01-01")) {
                                    row[5] = "";
                                } else {
                                    row[5] = rs.getString("DatePaid");
                                }
                                row[6] = Globals.termname(rs.getString("termcode"));
                                row[7] = rs.getString("CampanyInvoiceNumber");
                                model.addRow(row);
                            }
                            Array[3] = String.valueOf(total);
                            model.addRow(Array);
                            total = 0;

                        } catch (SQLException sq) {
                            JOptionPane.showMessageDialog(holder, sq.getMessage());
                        }


                    }

                } else {


                    if (status.getSelectedIndex() == 0) {
                        while (model.getRowCount() > 0) {
                            model.removeRow(0);
                        }
                        infor.setText("Invoice Record For " + termfilter.getSelectedItem());
                        try {

                            String sql = "Select * from invoices and termcode='" + Globals.termcode(termfilter.getSelectedItem().toString()) + "' order by daterecieved";
                            ps = con.prepareStatement(sql);
                            rs = ps.executeQuery();
                            while (rs.next()) {
                                row[0] = rs.getString("LocalInvoiceNumber");
                                row[1] = rs.getString("campany");
                                row[2] = rs.getString("DateRecieved");
                                row[3] = rs.getString("Amount");
                                row[4] = rs.getString("Status");
                                total += rs.getInt("Amount");
                                if (rs.getString("DatePaid").equalsIgnoreCase("1900-01-01")) {
                                    row[5] = "";
                                } else {
                                    row[5] = rs.getString("DatePaid");
                                }
                                row[6] = Globals.termname(rs.getString("termcode"));
                                row[7] = rs.getString("CampanyInvoiceNumber");
                                model.addRow(row);
                            }

                            Array[3] = String.valueOf(total);
                            model.addRow(Array);
                            total = 0;
                        } catch (SQLException sq) {
                            JOptionPane.showMessageDialog(holder, sq.getMessage());
                        }


                    } else {


                        try {
                            infor.setText(status.getSelectedItem() + " Invoice Record " + termfilter.getSelectedItem());
                            String sql = "Select * from invoices and termcode='" + Globals.termcode(termfilter.getSelectedItem().toString()) + "' and status='" + status.getSelectedItem() + "' order by daterecieved";
                            ps = con.prepareStatement(sql);
                            rs = ps.executeQuery();
                            while (rs.next()) {
                                row[0] = rs.getString("LocalInvoiceNumber");
                                row[1] = rs.getString("campany");
                                row[2] = rs.getString("DateRecieved");
                                row[3] = rs.getString("Amount");
                                row[4] = rs.getString("Status");
                                total += rs.getInt("Amount");
                                if (rs.getString("DatePaid").equalsIgnoreCase("1900-01-01")) {
                                    row[5] = "";
                                } else {
                                    row[5] = rs.getString("DatePaid");
                                }
                                row[6] = Globals.termname(rs.getString("termcode"));
                                row[7] = rs.getString("CampanyInvoiceNumber");
                                model.addRow(row);
                            }
                            Array[3] = String.valueOf(total);
                            model.addRow(Array);
                            total = 0;

                        } catch (SQLException sq) {
                            JOptionPane.showMessageDialog(holder, sq.getMessage());
                        }


                    }

                }


            } else {
                if (termfilter.getSelectedIndex() == 0) {
                    if (status.getSelectedIndex() == 0) {


                        try {
                            infor.setText("Invoice Record For Academic Year " + yearfilter.getSelectedItem());
                            String sql = "Select * from invoices where academicyear='" + yearfilter.getSelectedItem() + "' order by daterecieved";
                            ps = con.prepareStatement(sql);
                            rs = ps.executeQuery();
                            while (rs.next()) {

                                row[0] = rs.getString("LocalInvoiceNumber");
                                row[1] = rs.getString("campany");
                                row[2] = rs.getString("DateRecieved");
                                row[3] = rs.getString("Amount");
                                row[4] = rs.getString("Status");
                                total += rs.getInt("Amount");
                                if (rs.getString("DatePaid").equalsIgnoreCase("1900-01-01")) {
                                    row[5] = "";
                                } else {
                                    row[5] = rs.getString("DatePaid");
                                }
                                row[6] = Globals.termname(rs.getString("termcode"));
                                row[7] = rs.getString("CampanyInvoiceNumber");
                                model.addRow(row);
                            }
                            Array[3] = String.valueOf(total);
                            model.addRow(Array);
                            total = 0;

                        } catch (SQLException sq) {
                            JOptionPane.showMessageDialog(holder, sq.getMessage());
                        }


                    } else {


                        try {
                            infor.setText(status.getSelectedItem() + " Invoice Record For Academic Year " + yearfilter.getSelectedItem());
                            String sql = "Select * from invoices where academicyear='" + yearfilter.getSelectedItem() + "' and status='" + status.getSelectedItem() + "' order by daterecieved";
                            ps = con.prepareStatement(sql);
                            rs = ps.executeQuery();
                            while (rs.next()) {
                                row[0] = rs.getString("LocalInvoiceNumber");
                                row[1] = rs.getString("campany");
                                row[2] = rs.getString("DateRecieved");
                                row[3] = rs.getString("Amount");
                                row[4] = rs.getString("Status");
                                total += rs.getInt("Amount");
                                if (rs.getString("DatePaid").equalsIgnoreCase("1900-01-01")) {
                                    row[5] = "";
                                } else {
                                    row[5] = rs.getString("DatePaid");
                                }
                                row[6] = Globals.termname(rs.getString("termcode"));
                                row[7] = rs.getString("CampanyInvoiceNumber");
                                model.addRow(row);
                            }

                            Array[3] = String.valueOf(total);
                            model.addRow(Array);
                            total = 0;
                        } catch (SQLException sq) {
                            JOptionPane.showMessageDialog(holder, sq.getMessage());
                        }


                    }

                } else {

                    if (status.getSelectedIndex() == 0) {


                        try {
                            infor.setText("Invoice Record For Academic Year " + yearfilter.getSelectedItem() + " " + termfilter.getSelectedItem());
                            String sql = "Select * from invoices where academicyear='" + yearfilter.getSelectedItem() + "' and termcode='" + Globals.termcode(termfilter.getSelectedItem().toString()) + "' order by daterecieved";
                            ps = con.prepareStatement(sql);
                            rs = ps.executeQuery();
                            while (rs.next()) {
                                row[0] = rs.getString("LocalInvoiceNumber");
                                row[1] = rs.getString("campany");
                                row[2] = rs.getString("DateRecieved");
                                row[3] = rs.getString("Amount");
                                row[4] = rs.getString("Status");
                                total += rs.getInt("Amount");
                                if (rs.getString("DatePaid").equalsIgnoreCase("1900-01-01")) {
                                    row[5] = "";
                                } else {
                                    row[5] = rs.getString("DatePaid");
                                }
                                row[6] = Globals.termname(rs.getString("termcode"));
                                row[7] = rs.getString("CampanyInvoiceNumber");
                                model.addRow(row);
                            }

                            Array[3] = String.valueOf(total);
                            model.addRow(Array);
                            total = 0;
                        } catch (SQLException sq) {
                            JOptionPane.showMessageDialog(holder, sq.getMessage());
                        }


                    } else {


                        try {
                            infor.setText(status.getSelectedItem() + " Invoice Record For Academic Year " + yearfilter.getSelectedItem() + " " + termfilter.getSelectedItem());
                            String sql = "Select * from invoices where academicyear='" + yearfilter.getSelectedItem() + "' and termcode='" + Globals.termcode(termfilter.getSelectedItem().toString()) + "' and status='" + status.getSelectedItem() + "' order by daterecieved";
                            ps = con.prepareStatement(sql);
                            rs = ps.executeQuery();
                            while (rs.next()) {
                                row[0] = rs.getString("LocalInvoiceNumber");
                                row[1] = rs.getString("campany");
                                row[2] = rs.getString("DateRecieved");
                                row[3] = rs.getString("Amount");
                                row[4] = rs.getString("Status");
                                total = rs.getInt("Amount");
                                if (rs.getString("DatePaid").equalsIgnoreCase("1900-01-01")) {
                                    row[5] = "";
                                } else {
                                    row[5] = rs.getString("DatePaid");
                                }
                                row[6] = Globals.termname(rs.getString("termcode"));
                                row[7] = rs.getString("CampanyInvoiceNumber");
                                model.addRow(row);
                            }
                            Array[3] = String.valueOf(total);
                            model.addRow(Array);
                            total = 0;

                        } catch (SQLException sq) {
                            JOptionPane.showMessageDialog(holder, sq.getMessage());
                        }


                    }

                }


            }

        } else if (obj == freeze) {
            if (tab.getSelectedRowCount() < 1) {
                JOptionPane.showMessageDialog(holder, "Kindly  Select The Invoice To Freeze On Table Obove");
            } else {
                int selectedRow = tab.getSelectedRow();
                String status = model.getValueAt(selectedRow, 4).toString();
                INVOICENUMBER = model.getValueAt(selectedRow, 0).toString();
                if (status.equalsIgnoreCase("Paid")) {
                    JOptionPane.showMessageDialog(holder, "Sorry You Cannot Freeze An Invoice That Has Already Been Paid");
                } else {
                    int option = JOptionPane.showConfirmDialog(holder, "Are You Sure You Want To Freeze This Invoice??.\n Once An Invoice Has Been Freezed Payment Cannot Be Done Againist It", "Confirm Freeze", JOptionPane.YES_NO_OPTION);
                    if (option == JOptionPane.NO_OPTION) {
                        JOptionPane.showMessageDialog(holder, "Operation Postponed");
                    } else {
                        try {
                            String sql = "Update invoices set status='" + "Freezed" + "' where localinvoiceNumber='" + INVOICENUMBER + "'";
                            ps = con.prepareStatement(sql);
                            ps.execute();

                            JOptionPane.showMessageDialog(holder, "Invoice Freeze SuccessFully");
                            tab.setValueAt("Freezed", selectedRow, 4);
                        } catch (SQLException ex) {
                            ex.printStackTrace();
                        }
                    }


                }


            }

        } else if (obj == back) {
            top.removeAll();
            top.add(infor, "gapleft 30,growx,pushx");
            top.add(search, "gapleft 30");
            top.add(jsearch, "gapleft 30,growx,pushx,wrap");
            top.add(pane, "grow,push,span");
            top.add(panel2, "growx,pushx,span");
            top.revalidate();
            top.repaint();
        } else if (obj == EditInvoice) {
            if (tab.getSelectedRowCount() < 1) {
                JOptionPane.showMessageDialog(holder, "Kindly  Select The Invoice To Edit On Table Obove");
            } else {
                int selectedRow = tab.getSelectedRow();
                String status = model.getValueAt(selectedRow, 4).toString();
                INVOICENUMBER = model.getValueAt(selectedRow, 0).toString();
                if (status.equalsIgnoreCase("Paid")) {
                    JOptionPane.showMessageDialog(holder, "Sorry You Cannot Edit An Invoice That Has Already Been Paid");
                } else {
                    top.removeAll();
                    top.add(campany, "gapleft 50,gaptop 40,growx,pushx");
                    top.add(jcampany, "gapleft 50,gaptop 40,growx,pushx,wrap");
                    top.add(campanyinvoiceNumber, "gapleft 50,gaptop 40,growx,pushx");
                    top.add(jcampanyinvoiceNumber, "gapleft 50,gaptop 40,growx,pushx,wrap");
                    top.add(dateRecieved, "gapleft 50,gaptop 40,growx,pushx");
                    top.add(jpaydate, "gapleft 50,gaptop 40,growx,pushx,wrap");
                    top.add(amount, "gapleft 50,gaptop 40,growx,pushx");
                    top.add(jamount, "gapleft 50,gaptop 40,growx,pushx,wrap");
                    top.add(panel, "gapleft 100,width 100:400:600,height 150:200:200,wrap");
                    top.add(update, "gaptop 50,gapleft 100");
                    top.revalidate();
                    top.repaint();
                    String sql = "Select * from invoices where localinvoicenumber='" + INVOICENUMBER + "'";
                    try {
                        ps = con.prepareStatement(sql);
                        rs = ps.executeQuery();
                        if (rs.next()) {
                            jcampany.setText(rs.getString("Campany"));
                            jcampanyinvoiceNumber.setText(rs.getString("CampanyInvoiceNumber"));
                            jamount.setText(rs.getString("Amount"));
                            jpaydate.setDate(rs.getDate("Daterecieved"));
                            panel.setText(rs.getString("Description"));
                        }


                    } catch (SQLException sq) {
                        JOptionPane.showMessageDialog(holder, sq.getMessage());
                    }


                }
            }
        } else if (obj == update) {
            if (jcampany.getText().equalsIgnoreCase("")) {
                JOptionPane.showMessageDialog(holder, "Kindly Enter The Campany Name");
            } else {
                if (jcampanyinvoiceNumber.getText().equalsIgnoreCase("")) {
                    JOptionPane.showMessageDialog(holder, "Enter The Campany Invoice Number");
                } else {
                    if (((JTextField) jpaydate.getDateEditor().getUiComponent()).getText().equalsIgnoreCase("")) {
                        JOptionPane.showMessageDialog(holder, "Kindly Enter The Date The Invoice Was Recieved");
                    } else {
                        if (jamount.getText().isEmpty()) {
                            JOptionPane.showMessageDialog(holder, "Kindly Enter A Valid Amount Figure");
                        } else {
                            if (panel.getText().isEmpty()) {
                                JOptionPane.showMessageDialog(holder, "Kindly Type A Short Description Concerning The Invoice");
                            } else {
                                try {
                                    String DateRecive = ((JTextField) jpaydate.getDateEditor().getUiComponent()).getText();
                                    String sql = "update invoices set campany='" + jcampany.getText() + "',campanyinvoiceNumber='" + jcampanyinvoiceNumber.getText() + "',daterecieved='" + DateRecive + "',amount='" + jamount.getText() + "',Datepaid='" + "1900/01/01" + "',description='" + panel.getText() + "' where localinvoicenumber='" + INVOICENUMBER + "'";
                                    ps = con.prepareStatement(sql);
                                    ps.execute();
                                    JOptionPane.showMessageDialog(holder, "Invoice Details Updated SuccessFully");
                                    jcampany.setText("");
                                    jcampanyinvoiceNumber.setText("");
                                    jamount.setText("");
                                    jpaydate.setDate(null);
                                    panel.setText("");
                                } catch (HeadlessException | SQLException sq) {
                                    JOptionPane.showMessageDialog(holder, sq.getMessage());
                                }


                            }


                        }
                    }
                }
            }

        } else if (obj == filterbyamount) {


            String amount = JOptionPane.showInputDialog(holder, "Enter The Amount Figure To Filter Invoices Over it", "0");

            if (DataValidation.number2(amount)) {

                int Amount = Integer.parseInt(amount);
                while (model.getRowCount() > 0) {
                    model.removeRow(0);
                }
                try {
                    infor.setText("Invoice Values Over KSH. " + Amount);
                    String sql = "Select * from invoices  where amount>'" + Amount + "'order by daterecieved";
                    ps = con.prepareStatement(sql);
                    rs = ps.executeQuery();
                    while (rs.next()) {
                        row[0] = rs.getString("LocalInvoiceNumber");
                        row[1] = rs.getString("campany");
                        row[2] = rs.getString("DateRecieved");
                        row[3] = rs.getString("Amount");
                        row[4] = rs.getString("Status");
                        total += rs.getInt("amount");
                        if (rs.getString("DatePaid").equalsIgnoreCase("1900-01-01")) {
                            row[5] = "";
                        } else {
                            row[5] = rs.getString("DatePaid");
                        }
                        row[6] = Globals.termname(rs.getString("termcode"));
                        row[7] = rs.getString("CampanyInvoiceNumber");
                        model.addRow(row);

                    }
                    Array[3] = String.valueOf(total);
                    model.addRow(Array);
                    total = 0;

                } catch (SQLException sq) {
                    JOptionPane.showMessageDialog(holder, sq.getMessage());
                }


            } else {
                JOptionPane.showMessageDialog(holder, "Invalid Amount Figure");
            }
        } else if (obj == save) {
            if (jcampany.getText().equalsIgnoreCase("")) {
                JOptionPane.showMessageDialog(holder, "Kindly Enter The Campany Name");
            } else {
                if (jcampanyinvoiceNumber.getText().equalsIgnoreCase("")) {
                    JOptionPane.showMessageDialog(holder, "Enter The Campany Invoice Number");
                } else {
                    if (((JTextField) jpaydate.getDateEditor().getUiComponent()).getText().equalsIgnoreCase("")) {
                        JOptionPane.showMessageDialog(holder, "Kindly Enter The Date The Invoice Was Recieved");
                    } else {
                        if (jamount.getText().isEmpty()) {
                            JOptionPane.showMessageDialog(holder, "Kindly Enter A Valid Amount Figure");
                        } else {
                            if (panel.getText().isEmpty()) {
                                JOptionPane.showMessageDialog(holder, "Kindly Type A Short Description Concerning The Invoice");
                            } else {
                                try {
                                    String DateRecive = ((JTextField) jpaydate.getDateEditor().getUiComponent()).getText();
                                    String sql = "Insert into invoices values('" + jcampany.getText() + "','" + "#" + IdGenerator.keyGen() + "','" + jcampanyinvoiceNumber.getText() + "','" + DateRecive + "','" + jamount.getText() + "','" + "Pending" + "','" + "1900/01/01" + "','" + panel.getText() + "','" + Globals.currentTerm() + "','" + Globals.academicYear() + "')";
                                    ps = con.prepareStatement(sql);
                                    ps.execute();
                                    JOptionPane.showMessageDialog(holder, "Invoice Details Save SuccessFully");
                                    jcampany.setText("");
                                    jcampanyinvoiceNumber.setText("");
                                    jamount.setText("");
                                    jpaydate.setDate(null);
                                    panel.setText("");
                                } catch (HeadlessException | SQLException sq) {
                                    JOptionPane.showMessageDialog(holder, sq.getMessage());
                                }


                            }


                        }
                    }
                }
            }
        }
    }

}
