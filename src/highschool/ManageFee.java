/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package highschool;

import java.awt.Color;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import net.miginfocom.swing.MigLayout;

/**
 * @author FRED
 */
public class ManageFee {

    private JPanel bottom;
    private JPanel top;
    private JPanel holder;
    private JTable tab;
    private DefaultTableModel model;
    private JScrollPane pane;

    private FredLabel infor, filter;
    private FredButton print, delete, filterByDate, alumnifee, back;
    private FredCombo voteheadFilter;
    private PreparedStatement ps;
    private ResultSet rs;

    Object cols[] = {"Class", "Amount Expected", "Amount Paid", "Balance"};
    private Connection con;
    private FredCombo paymentcat, classfilter, streamfilter, yearfilter, termfilter;

    Object row[] = new Object[cols.length];
    int total = 0;
    int totalpaid = 0;
    Object arraytotal[] = new Object[cols.length];

    public JPanel feeManage() {
        holder = new JPanel();
        top = new JPanel();
        bottom = new JPanel();
        print = new FredButton("Generate Report");
        infor = new FredLabel("Fees Summary Report For " + Globals.currentTermName() + "    Year: " + Globals.academicYear());

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
        yearfilter = new FredCombo("Filter By Year");
        termfilter = new FredCombo("Filter By Term");
        pane = new JScrollPane(tab);
        back = new FredButton("Back");
        voteheadFilter = new FredCombo("Filter By Votehead");
        delete = new FredButton("Delete");
        filterByDate = new FredButton("Filter Fee Over Duration");
        alumnifee = new FredButton("View Fee Paid By Alumni");
        print = new FredButton("Generate Report");
        bottom.setLayout(new MigLayout());
        model.setColumnIdentifiers(cols);
        tab.setModel(model);
        top.add(infor, "gapleft 100,growx,pushx,wrap");
        top.add(pane, "gapleft 30,grow,push");
        bottom.add(print, "gapleft 30");
        bottom.add(termfilter, "gapleft 20,growx,pushx");
        bottom.add(yearfilter, "gapleft 20,growx,pushx");
        bottom.add(voteheadFilter, "gapleft 20,growx,pushx");
        bottom.add(filterByDate, "gapleft 20,growx,pushx");


        bottom.setBackground(Color.lightGray);
        try {
            con = DbConnection.connectDb();
            Object row[] = new Object[cols.length];
            String sqlr = "Select termname from terms ";
            ps = con.prepareStatement(sqlr);
            rs = ps.executeQuery();
            while (rs.next()) {
                termfilter.addItem(rs.getString("termname"));
            }
            for (int k = 2015; k <= Globals.academicYear(); k++) {
                yearfilter.addItem(k);
            }
            ps = con.prepareStatement("Select distinct StudentPayableVoteHeads.voteheadid from StudentPayableVoteHeads order by priority");
            rs = ps.executeQuery();
            while (rs.next()) {
                voteheadFilter.addItem(Globals.VoteHeadName(rs.getString("VoteHeadid")));

            }
            int total = 0, totalexpected = 0, totalbalance = 0, counter = 0, totalpd = 0, totalexpt = 0;
            String year = "", termcode = "", classl = "";
            ps = con.prepareStatement("Select * from classes  order by precision1");
            ResultSet rx = ps.executeQuery();
            while (rx.next()) {
                String classcode = rx.getString("classcode");
                String qrry = "Select sum(amount) from payablevoteheadperstudent,admission where admnumber=admissionNumber and currentform='" + classcode + "'  and academicyear='" + Globals.academicYear() + "' and term='" + Globals.currentTermName() + "'";
                ps = con.prepareStatement(qrry);
                rs = ps.executeQuery();
                if (rs.next()) {
                    totalexpected = rs.getInt("Sum(amount)");
                }
                totalexpt += totalexpected;
                ps = con.prepareStatement("Select Sum(amount) from reciepts,admission where admnumber=admissionNumber and currentform='" + classcode + "'  and year(date)='" + Globals.academicYear() + "' and term='" + Globals.currentTermName() + "'");
                rs = ps.executeQuery();
                if (rs.next()) {
                    totalpaid = rs.getInt("sum(amount)");
                }
                totalpd += totalpaid;
                row[0] = Globals.className(classcode);
                row[1] = totalexpected;
                row[2] = totalpaid;
                row[3] = totalexpected - totalpaid;
                model.addRow(row);

            }
            totalbalance = totalexpt - totalpd;
            row[0] = "TOTALS";
            row[1] = totalexpt;
            row[2] = totalpd;
            row[3] = totalbalance;
            model.addRow(row);

        } catch (NumberFormatException | SQLException e) {
            e.printStackTrace();
        }
        termfilter.addActionListener((ActionEvent e) -> {
            try {
                if (termfilter.getSelectedIndex() == 0) {
                    while (model.getRowCount() > 0) {
                        model.removeRow(0);
                    }
                    infor.setText("Fees Summary Report For " + Globals.currentTermName() + "    Year: " + Globals.academicYear());
                    int total = 0, totalexpected = 0, totalbalance = 0, counter = 0, totalpd = 0, totalexpt = 0;
                    String year = "", termcode = "", classl = "";
                    ps = con.prepareStatement("Select * from classes  order by precision1");
                    ResultSet rx = ps.executeQuery();
                    while (rx.next()) {
                        String classcode = rx.getString("classcode");
                        String qrry = "Select sum(amount) from payablevoteheadperstudent,admission where admnumber=admissionNumber and currentform='" + classcode + "'  and academicyear='" + Globals.academicYear() + "' and term='" + Globals.currentTermName() + "'";
                        ps = con.prepareStatement(qrry);
                        rs = ps.executeQuery();
                        if (rs.next()) {
                            totalexpected = rs.getInt("Sum(amount)");
                        }
                        totalexpt += totalexpected;
                        ps = con.prepareStatement("Select Sum(amount) from reciepts,admission where admnumber=admissionNumber and currentform='" + classcode + "'  and year(date)='" + Globals.academicYear() + "' and term='" + Globals.currentTermName() + "'");
                        rs = ps.executeQuery();
                        if (rs.next()) {
                            totalpaid = rs.getInt("sum(amount)");
                        }
                        totalpd += totalpaid;
                        row[0] = Globals.className(classcode);
                        row[1] = totalexpected;
                        row[2] = totalpaid;
                        row[3] = totalexpected - totalpaid;
                        model.addRow(row);

                    }
                    totalbalance = totalexpt - totalpd;
                    row[0] = "TOTALS";
                    row[1] = totalexpt;
                    row[2] = totalpd;
                    row[3] = totalbalance;
                    model.addRow(row);

                } else {
                    while (model.getRowCount() > 0) {
                        model.removeRow(0);
                    }
                    infor.setText("Fee Summary Report For " + termfilter.getSelectedItem());
                    int total = 0, totalexpected = 0, totalbalance = 0, counter = 0, totalpd = 0, totalexpt = 0;
                    String year = "", termcode = "", classl = "";
                    ps = con.prepareStatement("Select * from classes  order by precision1");
                    ResultSet rx = ps.executeQuery();
                    while (rx.next()) {
                        String classcode = rx.getString("classcode");
                        String qrry = "Select sum(amount) from payablevoteheadperstudent,admission where admnumber=admissionNumber and currentform='" + classcode + "' and term='" + termfilter.getSelectedItem() + "'";
                        ps = con.prepareStatement(qrry);
                        rs = ps.executeQuery();
                        if (rs.next()) {
                            totalexpected = rs.getInt("Sum(amount)");
                        }
                        totalexpt += totalexpected;
                        ps = con.prepareStatement("Select Sum(amount) from reciepts,admission where admnumber=admissionNumber and currentform='" + classcode + "' and term='" + termfilter.getSelectedItem() + "'");
                        rs = ps.executeQuery();
                        if (rs.next()) {

                            totalpaid = rs.getInt("sum(amount)");

                        }
                        totalpd += totalpaid;
                        row[0] = Globals.className(classcode);
                        row[1] = totalexpected;
                        row[2] = totalpaid;
                        row[3] = totalexpected - totalpaid;
                        model.addRow(row);

                    }
                    totalbalance = totalexpt - totalpd;
                    row[0] = "TOTALS";
                    row[1] = totalexpt;
                    row[2] = totalpd;
                    row[3] = totalbalance;
                    model.addRow(row);

                }
            } catch (NumberFormatException | SQLException sq) {
                sq.printStackTrace();
            }
        });

        yearfilter.addActionListener((ActionEvent e) -> {
            if (yearfilter.getSelectedIndex() == 0) {
                try {
                    if (termfilter.getSelectedIndex() == 0) {
                        while (model.getRowCount() > 0) {
                            model.removeRow(0);
                        }
                        infor.setText("Fees Summary Report For " + Globals.currentTermName() + "    Year: " + Globals.academicYear());

                        int total = 0, totalexpected = 0, totalbalance = 0, counter = 0, totalpd = 0, totalexpt = 0;
                        String year = "", termcode = "", classl = "";
                        ps = con.prepareStatement("Select * from classes  order by precision1");
                        ResultSet rx = ps.executeQuery();
                        while (rx.next()) {
                            String classcode = rx.getString("classcode");
                            String qrry = "Select sum(amount) from payablevoteheadperstudent,admission where admnumber=admissionNumber and currentform='" + classcode + "'  and academicyear='" + Globals.academicYear() + "' and term='" + Globals.currentTermName() + "'";
                            ps = con.prepareStatement(qrry);
                            rs = ps.executeQuery();
                            if (rs.next()) {
                                totalexpected = rs.getInt("Sum(amount)");
                            }
                            totalexpt += totalexpected;
                            ps = con.prepareStatement("Select Sum(amount) from reciepts,admission where admnumber=admissionNumber and currentform='" + classcode + "'  and year(date)='" + Globals.academicYear() + "' and term='" + Globals.currentTermName() + "'");
                            rs = ps.executeQuery();
                            if (rs.next()) {
                                totalpaid = rs.getInt("sum(amount)");
                            }
                            totalpd += totalpaid;
                            row[0] = Globals.className(classcode);
                            row[1] = totalexpected;
                            row[2] = totalpaid;
                            row[3] = totalexpected - totalpaid;
                            model.addRow(row);

                        }
                        totalbalance = totalexpt - totalpd;
                        row[0] = "TOTALS";
                        row[1] = totalexpt;
                        row[2] = totalpd;
                        row[3] = totalbalance;
                        model.addRow(row);

                    } else {
                        while (model.getRowCount() > 0) {
                            model.removeRow(0);
                        }

                        infor.setText("Fee Summary Report For " + termfilter.getSelectedItem());
                        int total = 0, totalexpected = 0, totalbalance = 0, counter = 0, totalpd = 0, totalexpt = 0;
                        String year = "", termcode = "", classl = "";
                        ps = con.prepareStatement("Select * from classes  order by precision1");
                        ResultSet rx = ps.executeQuery();
                        while (rx.next()) {
                            String classcode = rx.getString("classcode");
                            String qrry = "Select sum(amount) from payablevoteheadperstudent,admission where admnumber=admissionNumber and currentform='" + classcode + "'   and term='" + termfilter.getSelectedItem() + "'";
                            ps = con.prepareStatement(qrry);
                            rs = ps.executeQuery();
                            if (rs.next()) {
                                totalexpected = rs.getInt("Sum(amount)");
                            }
                            totalexpt += totalexpected;
                            ps = con.prepareStatement("Select Sum(amount) from reciepts,admission where admnumber=admissionNumber and currentform='" + classcode + "'   and term='" + termfilter.getSelectedItem() + "'");
                            rs = ps.executeQuery();
                            if (rs.next()) {
                                totalpaid = rs.getInt("sum(amount)");
                            }
                            totalpd += totalpaid;
                            row[0] = Globals.className(classcode);
                            row[1] = totalexpected;
                            row[2] = totalpaid;
                            row[3] = totalexpected - totalpaid;
                            model.addRow(row);

                        }
                        totalbalance = totalexpt - totalpd;
                        row[0] = "TOTALS";
                        row[1] = totalexpt;
                        row[2] = totalpd;
                        row[3] = totalbalance;
                        model.addRow(row);

                    }
                } catch (NumberFormatException | SQLException sq) {
                    sq.printStackTrace();
                }
            } else {
                try {
                    try {
                        if (termfilter.getSelectedIndex() == 0) {
                            while (model.getRowCount() > 0) {
                                model.removeRow(0);
                            }
                            infor.setText("Fee Summary Report Academic Year " + yearfilter.getSelectedItem());
                            int total = 0, totalexpected = 0, totalbalance = 0, counter = 0, totalpd = 0, totalexpt = 0;
                            String year = "", termcode = "", classl = "";
                            ps = con.prepareStatement("Select * from classes  order by precision1");
                            ResultSet rx = ps.executeQuery();
                            while (rx.next()) {
                                String classcode = rx.getString("classcode");
                                String qrry = "Select sum(amount) from payablevoteheadperstudent,admission where admnumber=admissionNumber and currentform='" + classcode + "'  and academicyear='" + yearfilter.getSelectedItem() + "'";
                                ps = con.prepareStatement(qrry);
                                rs = ps.executeQuery();
                                if (rs.next()) {
                                    totalexpected = rs.getInt("Sum(amount)");
                                }
                                totalexpt += totalexpected;
                                ps = con.prepareStatement("Select Sum(amount) from reciepts,admission where admnumber=admissionNumber and currentform='" + classcode + "'  and year(date)='" + yearfilter.getSelectedItem() + "' ");
                                rs = ps.executeQuery();
                                if (rs.next()) {
                                    totalpaid = rs.getInt("sum(amount)");
                                }
                                totalpd += totalpaid;
                                row[0] = Globals.className(classcode);
                                row[1] = totalexpected;
                                row[2] = totalpaid;
                                row[3] = totalexpected - totalpaid;
                                model.addRow(row);

                            }
                            totalbalance = totalexpt - totalpd;
                            row[0] = "TOTALS";
                            row[1] = totalexpt;
                            row[2] = totalpd;
                            row[3] = totalbalance;
                            model.addRow(row);

                        } else {
                            while (model.getRowCount() > 0) {
                                model.removeRow(0);
                            }
                            infor.setText("Fee Summary Report For " + termfilter.getSelectedItem() + " Academic Year " + yearfilter.getSelectedItem());
                            int total = 0, totalexpected = 0, totalbalance = 0, counter = 0, totalpd = 0, totalexpt = 0;
                            String year = "", termcode = "", classl = "";
                            ps = con.prepareStatement("Select * from classes  order by precision1");
                            ResultSet rx = ps.executeQuery();
                            while (rx.next()) {
                                String classcode = rx.getString("classcode");
                                String qrry = "Select sum(amount) from payablevoteheadperstudent,admission where admnumber=admissionNumber and currentform='" + classcode + "'  and academicyear='" + yearfilter.getSelectedItem() + "' and term='" + termfilter.getSelectedItem() + "'";
                                ps = con.prepareStatement(qrry);
                                rs = ps.executeQuery();
                                if (rs.next()) {
                                    totalexpected = rs.getInt("Sum(amount)");
                                }
                                totalexpt += totalexpected;
                                ps = con.prepareStatement("Select Sum(amount) from reciepts,admission where admnumber=admissionNumber and currentform='" + classcode + "'  and year(date)='" + yearfilter.getSelectedItem() + "' and term='" + termfilter.getSelectedItem() + "'");
                                rs = ps.executeQuery();
                                if (rs.next()) {
                                    totalpaid = rs.getInt("sum(amount)");
                                }
                                totalpd += totalpaid;
                                row[0] = Globals.className(classcode);
                                row[1] = totalexpected;
                                row[2] = totalpaid;
                                row[3] = totalexpected - totalpaid;
                                model.addRow(row);

                            }
                            totalbalance = totalexpt - totalpd;
                            row[0] = "TOTALS";
                            row[1] = totalexpt;
                            row[2] = totalpd;
                            row[3] = totalbalance;
                            model.addRow(row);

                        }
                    } catch (NumberFormatException | SQLException sq) {
                        sq.printStackTrace();
                    }
                } catch (Exception sq) {
                    sq.printStackTrace();
                }
            }
        });
        voteheadFilter.addActionListener((ActionEvent e) -> {

        });

        filterByDate.addActionListener((ActionEvent e) -> {
            String interval = "";
            SimpleDateFormat frm = new SimpleDateFormat("yyyy/MM/dd");
            interval = JOptionPane.showInputDialog(null, "Kindly Enter The Duration Interval To View Its Equivalent Fee Paid", "2012/12/31-" + frm.format(new Date()));
            if (DataValidation.dateInteval(interval)) {
                Object[] row1 = new Object[cols.length];
                while (model.getRowCount() > 0) {

                    model.removeRow(0);
                }
                int k = interval.indexOf("-");
                String lowerDate = interval.substring(0, k);
                String upperDate = interval.substring(k + 1);
                try {

                    if (voteheadFilter.getSelectedIndex() == 0) {
                        infor.setText("FEE Collection Status  Between " + lowerDate + " And " + upperDate);
                        int total = 0, totalexpected = 0, totalbalance = 0, counter = 0, totalpd = 0, totalexpt = 0;
                        String year = "", termcode = "", classl = "";
                        ps = con.prepareStatement("Select * from classes  order by precision1");
                        ResultSet rx = ps.executeQuery();
                        while (rx.next()) {
                            String classcode = rx.getString("classcode");
                            String qrry = "Select sum(amount) from payablevoteheadperstudent,admission where admnumber=admissionNumber and currentform='" + classcode + "' and Date BETWEEN '" + lowerDate + "' and '" + upperDate + "' ";
                            ps = con.prepareStatement(qrry);
                            rs = ps.executeQuery();
                            if (rs.next()) {
                                totalexpected = rs.getInt("Sum(amount)");
                            }
                            totalexpt += totalexpected;
                            ps = con.prepareStatement("Select Sum(amount) from reciepts,admission where admnumber=admissionNumber and currentform='" + classcode + "'  and Date BETWEEN '" + lowerDate + "' and '" + upperDate + "' ");
                            rs = ps.executeQuery();
                            if (rs.next()) {
                                totalpaid = rs.getInt("sum(amount)");
                            }
                            totalpd += totalpaid;
                            row[0] = Globals.className(classcode);
                            row[1] = totalexpected;
                            row[2] = totalpaid;
                            row[3] = totalexpected - totalpaid;
                            model.addRow(row);

                        }
                        totalbalance = totalexpt - totalpd;
                        row[0] = "TOTALS";
                        row[1] = totalexpt;
                        row[2] = totalpd;
                        row[3] = totalbalance;
                        model.addRow(row);

                    } else {
                        infor.setText(voteheadFilter.getSelectedItem() + " Collection  Status  Between " + lowerDate + " And " + upperDate);
                        int total = 0, totalexpected = 0, totalbalance = 0, counter = 0, totalpd = 0, totalexpt = 0;
                        String year = "", termcode = "", classl = "";
                        ps = con.prepareStatement("Select * from classes  order by precision1");
                        ResultSet rx = ps.executeQuery();
                        while (rx.next()) {
                            String classcode = rx.getString("classcode");
                            String qrry = "Select sum(amount) from payablevoteheadperstudent,admission where admnumber=admissionNumber and currentform='" + classcode + "' and Date BETWEEN '" + lowerDate + "' and '" + upperDate + "' and voteheadid='" + Globals.voteHeadId(voteheadFilter.getSelectedItem().toString()) + "' ";
                            ps = con.prepareStatement(qrry);
                            rs = ps.executeQuery();
                            if (rs.next()) {
                                totalexpected = rs.getInt("Sum(amount)");
                            }
                            totalexpt += totalexpected;
                            ps = con.prepareStatement("Select Sum(amount) from reciepts,admission where admnumber=admissionNumber and currentform='" + classcode + "'  and Date BETWEEN '" + lowerDate + "' and '" + upperDate + "'  and voteheadid='" + Globals.voteHeadId(voteheadFilter.getSelectedItem().toString()) + "'");
                            rs = ps.executeQuery();
                            if (rs.next()) {
                                totalpaid = rs.getInt("sum(amount)");
                            }
                            totalpd += totalpaid;
                            row[0] = Globals.className(classcode);
                            row[1] = totalexpected;
                            row[2] = totalpaid;
                            row[3] = totalexpected - totalpaid;
                            model.addRow(row);

                        }
                        totalbalance = totalexpt - totalpd;
                        row[0] = "TOTALS";
                        row[1] = totalexpt;
                        row[2] = totalpd;
                        row[3] = totalbalance;
                        model.addRow(row);

                    }

                } catch (NumberFormatException | SQLException sq) {
                    JOptionPane.showMessageDialog(holder, sq.getMessage());
                }
            }
        });
        back.addActionListener((ActionEvent e) -> {
            top.removeAll();
            top.revalidate();
            top.repaint();
            filterByDate.setEnabled(true);
            top.add(infor, "gapleft 100,wrap");
            top.add(pane, "gapleft 30,grow,push");
            back.setVisible(false);
        });

        alumnifee.addActionListener((ActionEvent e) -> {
            top.removeAll();
            back.setVisible(true);
            filterByDate.setEnabled(false);
            top.revalidate();
            top.repaint();
            DefaultTableModel model2 = new DefaultTableModel();
            FredLabel infor2 = new FredLabel("Alumni Payments( Fees Paid After Completion)");
            JTable tab2 = new JTable(model2);
            FredButton datefilter = new FredButton("Filter By Date Paid");
            Object newcols[] = {"Name", "admission Number", "Ref No.", "Date", "Term", "Academic Year", "Amount"};
            model2.setColumnIdentifiers(newcols);
            JScrollPane pane2 = new JScrollPane(tab2);
            top.add(infor2, "gapleft 50,growx,pushx,wrap");
            top.add(pane2, "gapleft 50,grow,push,wrap");
            top.add(datefilter, "gapleft 80");
            Object newrow[] = new Object[newcols.length];
            try {
                int total1 = 0;
                String sql = "Select * from alumnibalances";
                ps = con.prepareStatement(sql);
                rs = ps.executeQuery();
                while (rs.next()) {
                    newrow[0] = Globals.fullName(rs.getString("AdmissionNumber"));
                    newrow[1] = rs.getString("AdmissionNumber");
                    newrow[2] = rs.getString("recieptNo");
                    newrow[3] = rs.getString("date");
                    newrow[4] = Globals.termname(rs.getString("termcode"));
                    newrow[5] = rs.getString("Academicyear");
                    newrow[6] = rs.getString("credit");
                    model2.addRow(newrow);
                    total1 += rs.getInt("credit");
                }
                Object[] addedrow = {"TOTAL", "", "", "", "", "", total1};
                model2.addRow(addedrow);
            } catch (SQLException sq) {
                JOptionPane.showMessageDialog(holder, sq.getMessage());
            }
            datefilter.addActionListener((ActionEvent e1) -> {
                try {
                    String interval = "";
                    SimpleDateFormat frm = new SimpleDateFormat("yyyy/MM/dd");
                    interval = JOptionPane.showInputDialog(null, "Kindly Enter The Date Interval To Retrieve Its Payments", "2014/12/31-" + frm.format(new Date()));
                    if (DataValidation.dateInteval(interval)) {
                        total = 0;
                        int k = interval.indexOf("-");
                        String lowerDate = interval.substring(0, k);
                        String upperDate = interval.substring(k + 1);
                        infor2.setText("Paid Fee By Alumni Between " + lowerDate + " And " + upperDate);
                        while (model2.getRowCount() > 0) {
                            model2.removeRow(0);
                        }

                        String querry = "Select * from alumnibalances where Date BETWEEN '" + lowerDate + "' and '" + upperDate + "' ";
                        ps = con.prepareStatement(querry);
                        rs = ps.executeQuery();
                        while (rs.next()) {

                            newrow[0] = Globals.fullName(rs.getString("admnumber"));
                            newrow[1] = rs.getString("AdmissionNumber");
                            newrow[2] = rs.getString("recieptNo");
                            newrow[3] = rs.getString("date");
                            newrow[4] = Globals.termname(rs.getString("termcode"));
                            newrow[5] = rs.getString("Academicyear");
                            newrow[6] = rs.getString("credit");
                            model2.addRow(newrow);
                            total = rs.getInt("credit");
                        }
                        Object addedrow[] = {"TOTAL", "", "", "", "", "", total};
                        model2.addRow(addedrow);

                    } else {
                        JOptionPane.showMessageDialog(null, "Wrong Date Interval Formaat\n Kindly Use The Format provided", "Wrong Date Format", JOptionPane.ERROR_MESSAGE);
                    }

                } catch (HeadlessException | SQLException sq) {
                    JOptionPane.showMessageDialog(holder, sq.getMessage());
                }
            });
            top.revalidate();
            top.repaint();
        });
        print.addActionListener((ActionEvent e) -> {
            ReportGenerator.generateReport(infor.getText(), "summaryFeeRport", tab);
        });

        return holder;
    }

}
