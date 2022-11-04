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
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JWindow;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;

import net.miginfocom.swing.MigLayout;

/**
 * @author FRED
 */
public class FinanceClearance {
    static JTextField jamount;
    static JTextField jname;
    static JTextField jslipnumber;
    static FredCombo jpmode = new FredCombo("Select Payment Mode");
    static String extendedString = "";
    static String alumnicode = "";
    static String classname;
    static String streamname;
    static String paymentname;
    static String status;
    static String over;
    static String title;
    private JPanel bottom;
    private JPanel top;
    private JPanel holder;
    private JTable tab;
    private DefaultTableModel model;
    private JScrollPane pane;
    private FredCheckBox totalexpected, cleared;
    private FredLabel infor, search;
    private FredButton print, back, statement, clear;
    private PreparedStatement ps;
    private ResultSet rs;
    private JTextField jsearch;
    private FredCombo yearFilter = new FredCombo("Filter By Year Of Completion");
    Object cols[] = {"AdmissionNumber", "Name", "Category", "Year Of Completion", "Balance(KSH.)", "Status"};
    private Connection con;
    private FredCombo paymentcat, classfilter, streamfilter;
    Object row[] = new Object[cols.length];
    int total = 0;
    int totalpaid = 0;
    Object arraytotal[] = new Object[cols.length];

    public FinanceClearance() {


    }

    public JPanel feeClear() {

        holder = new JPanel();
        top = new JPanel();
        bottom = new JPanel();
        print = new FredButton("Generate Report");
        infor = new FredLabel("ALUMNI CLEARANCE RECORD");
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
        model.setColumnIdentifiers(cols);
        tab.setModel(model);
        back = new FredButton("Back");
        statement = new FredButton("Fee Statement");
        pane = new JScrollPane(tab);
        jsearch = new JTextField();

        clear = new FredButton("Generate Clearance Form");
        search = new FredLabel("Search An Alumni By admission Number Or Name");
        top.add(infor, "gapleft 30,growx,pushx");
        top.add(search, "gapleft 30");
        top.add(jsearch, "gapleft 30,growx,pushx,wrap");
        top.add(pane, "grow,push,span");
        bottom.add(print, "gapleft 20,growx,pushx");
        bottom.add(statement, "gapleft 20,growx,pushx,wrap");
        bottom.add(back, "gapleft 20,growx,pushx");
        bottom.add(clear, "gapleft 20,growx,pushx");
        bottom.add(yearFilter, "gapleft 20,growx,pushx");
        Object row[] = new Object[cols.length];

        for (int k = 2015; k <= Globals.academicYear(); k++) {
            yearFilter.addItem(k);
        }
        try {


            String sql = "Select firstname,middlename,lastname,admissionNumber,currentform from admission where admission.currentform='" + Globals.classCode("Alumni") + "' or admission.currentform='" + Globals.classCode("transfered") + "'  order by admissionnumber ";
            con = DbConnection.connectDb();
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();

            while (rs.next()) {
                String adm = rs.getString("AdmissionNumber");
                row[0] = rs.getString("AdmissionNumber");
                row[3] = Globals.completionYear(adm);
                row[1] = rs.getString("firstName") + "    " + rs.getString("Middlename") + "    " + rs.getString("LastName");
                if (rs.getString("currentForm").startsWith("AL")) {
                    row[2] = "COMPLETION";
                } else {
                    row[2] = "TRANSFERED";
                }
                double bal = Globals.balanceCalculator(adm);
                String pycode = "";
                int year = 0;
                row[4] = bal;
                total += bal;

                if (bal > 0) {
                    row[5] = "Not Cleared";
                } else {
                    row[5] = "Cleared";
                }


                model.addRow(row);


            }
            for (int i = 0; i < row.length; ++i) {
                row[i] = "";
            }
            row[0] = "Total";
            row[4] = total;
            model.addRow(row);

        } catch (SQLException sq) {
            JOptionPane.showMessageDialog(holder, sq.getMessage());
        }
        yearFilter.addActionListener((ActionEvent e) -> {
            new Thread() {
                @Override
                public void run() {
                    total = 0;
                    JWindow dia = new JWindow();
                    JProgressBar bar = new JProgressBar();
                    bar.setIndeterminate(true);
                    dia.setSize(300, 60);
                    bar.setBorder(new TitledBorder("Processing..."));
                    dia.setLayout(new MigLayout());
                    dia.add(bar, "grow,push");
                    dia.setAlwaysOnTop(true);
                    dia.setLocationRelativeTo(CurrentFrame.mainFrame());
                    dia.setIconImage(FrameProperties.icon());
                    dia.setVisible(true);
                    int total1 = 0;
                    while (model.getRowCount() > 0) {
                        model.removeRow(0);
                    }
                    if (yearFilter.getSelectedIndex() > 0) {
                        infor.setText("ALUMNI CLEARANCE RECORD CLASS OF " + yearFilter.getSelectedItem());
                        try {
                            String sql = "Select firstname,middlename,lastname,admissionNumber,currentform from admission where admission.currentform='" + Globals.classCode("Alumni") + "' or admission.currentform='" + Globals.classCode("transfered") + "'  order by admissionnumber ";
                            con = DbConnection.connectDb();
                            ps = con.prepareStatement(sql);
                            rs = ps.executeQuery();

                            while (rs.next()) {
                                String adm = rs.getString("AdmissionNumber");

                                if (Globals.completionYear(adm).equalsIgnoreCase(yearFilter.getSelectedItem().toString())) {

                                    row[0] = rs.getString("AdmissionNumber");
                                    row[3] = Globals.completionYear(adm);
                                    row[1] = rs.getString("firstName") + "    " + rs.getString("Middlename") + "    " + rs.getString("LastName");
                                    if (rs.getString("currentForm").startsWith("AL")) {
                                        row[2] = "COMPLETION";
                                    } else {
                                        row[2] = "TRANSFERED";
                                    }
                                    double bal = Globals.balanceCalculator(adm);
                                    String pycode = "";
                                    int year = 0;
                                    row[4] = bal;
                                    total += bal;

                                    if (bal > 0) {
                                        row[5] = "Not Cleared";
                                    } else {
                                        row[5] = "Cleared";
                                    }


                                    model.addRow(row);
                                }


                            }
                            for (int i = 0; i < row.length; ++i) {
                                row[i] = "";
                            }
                            row[0] = "Total";
                            row[4] = total;
                            model.addRow(row);
                        } catch (SQLException sq) {
                            JOptionPane.showMessageDialog(holder, sq.getMessage());
                        }
                    } else {

                        infor.setText("ALUMNI CLEARANCE RECORD");
                        try {
                            String sql = "Select firstname,middlename,lastname,admissionNumber,currentform from admission where admission.currentform='" + Globals.classCode("Alumni") + "' or admission.currentform='" + Globals.classCode("transfered") + "'  order by admissionnumber ";
                            con = DbConnection.connectDb();
                            ps = con.prepareStatement(sql);
                            rs = ps.executeQuery();

                            while (rs.next()) {
                                String adm = rs.getString("AdmissionNumber");
                                row[0] = rs.getString("AdmissionNumber");
                                row[3] = Globals.completionYear(adm);
                                row[1] = rs.getString("firstName") + "    " + rs.getString("Middlename") + "    " + rs.getString("LastName");
                                if (rs.getString("currentForm").startsWith("AL")) {
                                    row[2] = "COMPLETION";
                                } else {
                                    row[2] = "TRANSFERED";
                                }
                                double bal = Globals.balanceCalculator(adm);
                                String pycode = "";
                                int year = 0;
                                row[4] = bal;
                                total += bal;

                                if (bal > 0) {
                                    row[5] = "Not Cleared";
                                } else {
                                    row[5] = "Cleared";
                                }


                                model.addRow(row);


                            }
                            for (int i = 0; i < row.length; ++i) {
                                row[i] = "";
                            }
                            row[0] = "Total";
                            row[4] = total;
                            model.addRow(row);
                        } catch (SQLException sq) {
                            JOptionPane.showMessageDialog(holder, sq.getMessage());
                        }
                        dia.dispose();
                    }
                    dia.dispose();
                }
            }.start();
        });

        back.addActionListener((ActionEvent e) -> {
            new Thread() {
                @Override
                public void run() {
                    JWindow dia = new JWindow();
                    JProgressBar bar = new JProgressBar();
                    bar.setIndeterminate(true);

                    dia.setSize(300, 60);
                    total = 0;
                    bar.setBorder(new TitledBorder("Processing..."));
                    dia.setLayout(new MigLayout());
                    dia.add(bar, "grow,push");
                    dia.setAlwaysOnTop(true);
                    dia.setLocationRelativeTo(CurrentFrame.mainFrame());
                    dia.setIconImage(FrameProperties.icon());
                    dia.setVisible(true);
                    top.removeAll();
                    top.add(infor, "gapleft 30");
                    top.add(search, "gapleft 30");
                    top.add(jsearch, "gapleft 30,growx,pushx,wrap");
                    top.add(pane, "grow,push,span");
                    top.revalidate();
                    top.repaint();
                    while (model.getRowCount() > 0) {
                        model.removeRow(0);
                    }
                    infor.setText("ALUMNI CLEARANCE RECORD");
                    model.setColumnIdentifiers(cols);
                    try {
                        String sql = "Select firstname,middlename,lastname,admissionNumber,currentform from admission where admission.currentform='" + Globals.classCode("Alumni") + "' or admission.currentform='" + Globals.classCode("transfered") + "'  order by admissionnumber ";
                        con = DbConnection.connectDb();
                        ps = con.prepareStatement(sql);
                        rs = ps.executeQuery();

                        while (rs.next()) {
                            String adm = rs.getString("AdmissionNumber");
                            row[0] = rs.getString("AdmissionNumber");
                            row[3] = Globals.completionYear(adm);
                            row[1] = rs.getString("firstName") + "    " + rs.getString("Middlename") + "    " + rs.getString("LastName");
                            if (rs.getString("currentForm").startsWith("AL")) {
                                row[2] = "COMPLETION";
                            } else {
                                row[2] = "TRANSFERED";
                            }
                            double bal = Globals.balanceCalculator(adm);
                            String pycode = "";
                            int year = 0;
                            row[4] = bal;
                            total += bal;

                            if (bal > 0) {
                                row[5] = "Not Cleared";
                            } else {
                                row[5] = "Cleared";
                            }


                            model.addRow(row);


                        }
                        for (int i = 0; i < row.length; ++i) {
                            row[i] = "";
                        }
                        row[0] = "Total";
                        row[4] = total;
                        model.addRow(row);
                    } catch (SQLException sq) {
                        JOptionPane.showMessageDialog(holder, sq.getMessage());
                    }
                    dia.dispose();
                }
            }.start();
        });
        print.addActionListener((ActionEvent e) -> {
            ReportGenerator.generateReport(infor.getText(), "fee-Alumini", tab);
        });
        clear.addActionListener((ActionEvent e) -> {
            new Thread() {
                @Override
                public void run() {
                    JWindow dia = new JWindow();
                    JProgressBar bar = new JProgressBar();
                    bar.setIndeterminate(true);

                    dia.setSize(300, 60);

                    bar.setBorder(new TitledBorder("Processing..."));
                    dia.setAlwaysOnTop(true);
                    dia.setLocationRelativeTo(CurrentFrame.mainFrame());
                    dia.setIconImage(FrameProperties.icon());
                    dia.setLayout(new MigLayout());
                    dia.add(bar, "grow,push");
                    dia.setVisible(true);

                    if (tab.getSelectedRowCount() < 1) {

                        JOptionPane.showMessageDialog(holder, "Kindly Select The Alumni To generate His/Her Clearance Report");
                    } else {

                        String adm = tab.getValueAt(tab.getSelectedRow(), 0).toString();
                        ClearanceReportForm.AlumniReport(adm, adm + "Clearanceform");


                    }
                    dia.dispose();
                }
            }.start();
        });

        statement.addActionListener((ActionEvent sq) -> {
            if (tab.getSelectedRowCount() < 1) {

                JOptionPane.showMessageDialog(holder, "Kindly Select The Alumni To generate His/Her Fee Statement");
            } else {
                new Thread() {
                    @Override
                    public void run() {
                        JWindow dia = new JWindow();
                        JProgressBar bar = new JProgressBar();
                        bar.setIndeterminate(true);
                        total = 0;
                        dia.setSize(300, 70);

                        bar.setBorder(new TitledBorder("Processing..."));
                        dia.setAlwaysOnTop(true);
                        dia.setLocationRelativeTo(CurrentFrame.mainFrame());
                        dia.setIconImage(FrameProperties.icon());
                        dia.setLayout(new MigLayout());
                        dia.add(bar, "grow,push");
                        dia.setVisible(true);

                        String adm = tab.getValueAt(tab.getSelectedRow(), 0).toString();
                        ReportGenerator.FeeStatementGenerator(adm);

                        dia.dispose();
                    }


                }.start();

            }

        });


        jsearch.addKeyListener(new KeyAdapter() {

            @Override
            public void keyTyped(KeyEvent e) {

                char c = e.getKeyChar();
                if (c == KeyEvent.VK_ENTER) {

                    new Thread() {
                        @Override
                        public void run() {
                            JWindow dia = new JWindow();
                            JProgressBar bar = new JProgressBar();
                            bar.setIndeterminate(true);
                            total = 0;
                            dia.setSize(300, 70);

                            bar.setBorder(new TitledBorder("Processing..."));
                            dia.setAlwaysOnTop(true);
                            dia.setLocationRelativeTo(CurrentFrame.mainFrame());
                            dia.setIconImage(FrameProperties.icon());
                            dia.setLayout(new MigLayout());
                            dia.add(bar, "grow,push");
                            dia.setVisible(true);

                            try {
                                while (model.getRowCount() > 0) {
                                    model.removeRow(0);
                                }
                                infor.setText("Search Results");


                                String sql = "Select firstname,middlename,lastname,admissionNumber,currentform from admission where admission.currentform='" + Globals.classCode("Alumni") + "' and admissionnumber like '" + jsearch.getText() + "%' or firstname like '" + jsearch.getText() + "%' or middlename like '" + jsearch.getText() + "%' or lastname like '" + jsearch.getText() + "%' order by admissionnumber ";
                                con = DbConnection.connectDb();
                                ps = con.prepareStatement(sql);
                                rs = ps.executeQuery();

                                while (rs.next()) {
                                    String adm = rs.getString("AdmissionNumber");
                                    row[0] = rs.getString("AdmissionNumber");
                                    row[3] = Globals.completionYear(adm);
                                    row[1] = rs.getString("firstName") + "    " + rs.getString("Middlename") + "    " + rs.getString("LastName");
                                    if (rs.getString("currentForm").startsWith("AL")) {
                                        row[2] = "COMPLETION";
                                    } else {
                                        row[2] = "TRANSFERED";
                                    }
                                    double bal = Globals.balanceCalculator(adm);
                                    String pycode = "";
                                    int year = 0;
                                    row[4] = bal;
                                    total += bal;

                                    if (bal > 0) {
                                        row[5] = "Not Cleared";
                                    } else {
                                        row[5] = "Cleared";
                                    }


                                    model.addRow(row);


                                }
                                for (int i = 0; i < row.length; ++i) {
                                    row[i] = "";
                                }
                                row[0] = "Total";
                                row[4] = total;
                                model.addRow(row);


                            } catch (SQLException sq) {

                                JOptionPane.showMessageDialog(holder, sq.getMessage());
                            }
                            dia.dispose();
                        }
                    }.start();


                }


            }
        });


        return holder;
    }

}
