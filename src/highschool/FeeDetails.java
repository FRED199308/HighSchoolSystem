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
public class FeeDetails implements ActionListener {
    static String extendedString = "";
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
    private FredLabel infor, filter, search;
    private FredButton print, statement, OveraalStatement, amountfilter, datefilter, back;
    private PreparedStatement ps;
    private ResultSet rs;
    private JTextField jserch;
    Object cols[] = {"AdmissionNumber", "Name", "Class", "Stream", "Paid(KSH.)", "Balance(KSH.)"};
    private Connection con;
    private FredCombo classfilter, streamfilter;
    Object row[] = new Object[cols.length];
    double total = 0;
    int totalpaid = 0;
    Object arraytotal[] = new Object[cols.length];

    public JPanel feeholder() {
        holder = new JPanel();
        top = new JPanel();
        bottom = new JPanel();
        print = new FredButton("Generate Report");
        infor = new FredLabel("Form 1 Balances For " + Globals.currentTermName());
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
        totalexpected = new FredCheckBox("Show Total Expected Only");
        model.setColumnIdentifiers(cols);

        cleared = new FredCheckBox("Cleared Students");
        statement = new FredButton("Fee Statement");
        back = new FredButton("Back");
        OveraalStatement = new FredButton("Total Fee Statement");
        filter = new FredLabel("Filter Fee Over");
        amountfilter = new FredButton("Filter Balances Over Figure");
        datefilter = new FredButton("Filter By Date");
        streamfilter = new FredCombo("Select Stream");
        classfilter = new FredCombo("Select Class");
        jserch = new JTextField(5);
        search = new FredLabel("Search Student By admission Number(Total Balance Will Be Displayed)");
        tab.setModel(model);
        top.add(infor, "gapleft 30,growx,pushx");
        top.add(search, "gapleft 20,growx,pushx");
        top.add(jserch, "gapleft 5,growx,pushx,wrap");
        top.add(pane, "grow,push,span");
        bottom.add(print, "growx,pushx");
        //  bottom.add(paymentcat, "growx,pushx");
        bottom.add(classfilter, "growx,pushx");
        bottom.add(streamfilter, "growx,pushx");
        bottom.add(cleared, "growx,pushx,wrap");
        bottom.add(statement, "growx,pushx");
        bottom.add(OveraalStatement, "growx,pushx");
        bottom.add(amountfilter, "growx,pushx");
        // bottom.add(datefilter, "growx,pushx");
        //  bottom.add(totalexpected, "growx,pushx");
        bottom.add(back, "growx,pushx");
        con = DbConnection.connectDb();

        for (int n = 0; n < arraytotal.length; ++n) {
            arraytotal[n] = "";
        }

        print.addActionListener((ActionEvent e) -> {
            ReportGenerator.generateReport(infor.getText().toUpperCase(), "feebal", tab);
        });
        totalexpected.addActionListener(this);
        jserch.addKeyListener(new KeyAdapter() {

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

                            dia.setSize(300, 60);

                            bar.setBorder(new TitledBorder("Processing..."));
                            dia.setAlwaysOnTop(true);
                            dia.setLocationRelativeTo(CurrentFrame.mainFrame());
                            dia.setIconImage(FrameProperties.icon());
                            dia.add(bar);
                            dia.setVisible(true);
                            while (model.getRowCount() > 0) {
                                model.removeRow(0);
                            }

                            try {
                                infor.setText("Search Results");
                                String adm = "";
                                int individualtotalpaid = 0;
                                int individualexpected = 0;
                                int counter = 0;


                                con = DbConnection.connectDb();


                                ps = con.prepareStatement("Select admissionnumber,currentform as classcode,currentstream as streamcode from admission  where  admissionnumber like '" + jserch.getText() + "%' ");
                                rs = ps.executeQuery();
                                while (rs.next()) {
                                    adm = rs.getString("admissionnumber");
                                    row[0] = adm;
                                    row[1] = Globals.fullName(adm);
                                    row[2] = Globals.className(rs.getString("classcode"));

                                    row[3] = Globals.streamName(rs.getString("streamcode"));


                                    row[5] = Globals.balanceCalculator(adm);


                                    ps = con.prepareStatement("Select sum(amount) from reciepts where admnumber='" + adm + "'");
                                    ResultSet rx = ps.executeQuery();
                                    if (rx.next()) {
                                        row[4] = rx.getString("Sum(amount)");
                                    } else {
                                        row[4] = "";
                                    }
                                    if (row[4] == null) {
                                        row[4] = "";
                                    }
                                    model.addRow(row);
                                }

                            } catch (SQLException sq) {
                                dia.dispose();
                                JOptionPane.showMessageDialog(holder, sq.getMessage());
                            }

                            dia.dispose();
                        }

                    }.start();
                }
            }
        });

        try {
            totalpaid = 0;
            total = 0;
            ps = con.prepareStatement("Select admissionnumber,currentform as classcode,currentstream as streamcode from admission  where  currentform='" + Globals.classCode("Form 1") + "' ");
            rs = ps.executeQuery();
            while (rs.next()) {
                String adm = rs.getString("admissionnumber");
                row[0] = adm;
                row[1] = Globals.fullName(adm);
                row[2] = Globals.className(rs.getString("classcode"));

                row[3] = Globals.streamName(rs.getString("streamcode"));


                row[5] = Globals.balanceCalculator(adm);
                total += Globals.balanceCalculator(adm);


                ps = con.prepareStatement("Select sum(amount) from reciepts where admnumber='" + adm + "'");
                ResultSet rx = ps.executeQuery();
                if (rx.next()) {
                    row[4] = rx.getString("Sum(amount)");
                } else {
                    row[4] = "";
                }
                if (row[4] == null) {
                    row[4] = "";
                }
                model.addRow(row);
            }
            row[0] = "TOTAL";
            row[1] = "";
            row[2] = "";
            row[3] = "";
            row[4] = "";
            row[5] = total;
            model.addRow(row);


            String sql2 = "select * from classes  where precision1<5 order by precision1";
            ps = con.prepareStatement(sql2);
            rs = ps.executeQuery();
            while (rs.next()) {
                classfilter.addItem(rs.getString("ClassName"));
            }
            String sql4 = "select streamname from streams";
            ps = con.prepareStatement(sql4);
            rs = ps.executeQuery();
            while (rs.next()) {
                streamfilter.addItem(rs.getString("streamname"));
            }

        } catch (SQLException sq) {
            sq.printStackTrace();
            JOptionPane.showMessageDialog(holder, sq.getMessage());
        } finally {
            try {
                con.close();
                ps.close();
            } catch (SQLException sq) {

            }

        }


        classfilter.addActionListener((ActionEvent e) -> {
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
                    dia.add(bar);
                    dia.setVisible(true);
                    try {
                        total = 0;
                        con = DbConnection.connectDb();
                        model.getDataVector().removeAllElements();
                        if (cleared.isSelected()) {
                            infor.setText("Cleared Student In " + classfilter.getSelectedItem().toString() + "  Balances");
                            ps = con.prepareStatement("Select admissionnumber,currentform as classcode,currentstream as streamcode from admission  where  currentform='" + Globals.classCode(classfilter.getSelectedItem().toString()) + "' ");
                            rs = ps.executeQuery();
                            while (rs.next()) {
                                String adm = rs.getString("admissionnumber");
                                row[0] = adm;
                                double bal = Globals.balanceCalculator(adm);
                                ;
                                row[1] = Globals.fullName(adm);
                                row[2] = Globals.className(rs.getString("classcode"));

                                row[3] = Globals.streamName(rs.getString("streamcode"));


                                row[5] = bal;


                                ps = con.prepareStatement("Select sum(amount) from reciepts where admnumber='" + adm + "'");
                                ResultSet rx = ps.executeQuery();
                                if (rx.next()) {
                                    row[4] = rx.getString("Sum(amount)");
                                }
                                if (bal <= 0) {
                                    model.addRow(row);
                                    if (row[4] == null) {
                                        row[4] = "";
                                    }
                                    total += bal;
                                }
                            }
                            row[0] = "TOTAL";
                            row[1] = "";
                            row[2] = "";
                            row[3] = "";
                            row[4] = "";
                            row[5] = total;
                            model.addRow(row);
                        } else {
                            infor.setText(classfilter.getSelectedItem().toString() + "  Balances");
                            ps = con.prepareStatement("Select admissionnumber,currentform as classcode,currentstream as streamcode from admission  where  currentform='" + Globals.classCode(classfilter.getSelectedItem().toString()) + "' ");
                            rs = ps.executeQuery();
                            while (rs.next()) {
                                String adm = rs.getString("admissionnumber");
                                row[0] = adm;
                                row[1] = Globals.fullName(adm);
                                row[2] = Globals.className(rs.getString("classcode"));

                                row[3] = Globals.streamName(rs.getString("streamcode"));


                                row[5] = Globals.balanceCalculator(adm);
                                total += Globals.balanceCalculator(adm);

                                ps = con.prepareStatement("Select sum(amount) from reciepts where admnumber='" + adm + "'");
                                ResultSet rx = ps.executeQuery();
                                if (rx.next()) {
                                    row[4] = rx.getString("Sum(amount)");
                                }
                                model.addRow(row);
                            }
                            row[0] = "TOTAL";
                            row[1] = "";
                            row[2] = "";
                            row[3] = "";
                            row[4] = "";
                            row[5] = total;
                            model.addRow(row);
                        }

                        dia.dispose();

                    } catch (SQLException sq) {

                        // JOptionPane.showMessageDialog(holder, sq.getMessage());
                    } finally {
                        try {
                            con.close();
                            ps.close();
                        } catch (SQLException sq) {

                        }

                    }
                    dia.dispose();
                }

            }.start();

        });

        streamfilter.addActionListener((ActionEvent e) -> {
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
                    dia.add(bar);
                    dia.setVisible(true);
                    model.getDataVector().removeAllElements();
                    try {
                        con = DbConnection.connectDb();
                        total = 0;

                        if (cleared.isSelected()) {
                            if (classfilter.getSelectedIndex() > 0) {
                                infor.setText(classfilter.getSelectedItem().toString() + "    " + streamfilter.getSelectedItem() + " Balances");
                                ps = con.prepareStatement("Select admissionnumber,currentform as classcode,currentstream as streamcode from admission  where  currentform='" + Globals.classCode(classfilter.getSelectedItem().toString()) + "' and currentstream='" + Globals.streamcode(streamfilter.getSelectedItem().toString()) + "' ");
                                rs = ps.executeQuery();
                                while (rs.next()) {
                                    String adm = rs.getString("admissionnumber");
                                    row[0] = adm;
                                    double bal = Globals.balanceCalculator(adm);

                                    row[1] = Globals.fullName(adm);
                                    row[2] = Globals.className(rs.getString("classcode"));

                                    row[3] = Globals.streamName(rs.getString("streamcode"));


                                    row[5] = bal;


                                    ps = con.prepareStatement("Select sum(amount) from reciepts where admnumber='" + adm + "'");
                                    ResultSet rx = ps.executeQuery();
                                    if (rx.next()) {
                                        row[4] = rx.getString("Sum(amount)");
                                    }
                                    if (bal <= 0) {
                                        if (row[4] == null) {
                                            row[4] = "";
                                        }
                                        model.addRow(row);
                                        total += bal;
                                    }
                                }
                                row[0] = "TOTAL";
                                row[1] = "";
                                row[2] = "";
                                row[3] = "";
                                row[4] = "";
                                row[5] = total;
                                model.addRow(row);


                            } else {
                                infor.setText(streamfilter.getSelectedItem() + " Balances");
                                ps = con.prepareStatement("Select admissionnumber,currentform as classcode,currentstream as streamcode from admission  where  currentstream='" + Globals.streamcode(streamfilter.getSelectedItem().toString()) + "' ");
                                rs = ps.executeQuery();
                                while (rs.next()) {

                                    String adm = rs.getString("admissionnumber");
                                    double bal = Globals.balanceCalculator(adm);
                                    row[0] = adm;
                                    row[1] = Globals.fullName(adm);
                                    row[2] = Globals.className(rs.getString("classcode"));

                                    row[3] = Globals.streamName(rs.getString("streamcode"));


                                    row[5] = bal;


                                    ps = con.prepareStatement("Select sum(amount) from reciepts where admnumber='" + adm + "'");
                                    ResultSet rx = ps.executeQuery();
                                    if (rx.next()) {
                                        row[4] = rx.getString("Sum(amount)");
                                    }
                                    if (bal <= 0) {
                                        if (row[4] == null) {
                                            row[4] = "";
                                        }
                                        model.addRow(row);
                                    }
                                }


                            }


                        } else {
                            if (classfilter.getSelectedIndex() > 0) {
                                infor.setText(classfilter.getSelectedItem().toString() + "    " + streamfilter.getSelectedItem() + " Balances");
                                ps = con.prepareStatement("Select admissionnumber,currentform as classcode,currentstream as streamcode from admission  where  currentform='" + Globals.classCode(classfilter.getSelectedItem().toString()) + "' and currentstream='" + Globals.streamcode(streamfilter.getSelectedItem().toString()) + "' ");
                                rs = ps.executeQuery();
                                while (rs.next()) {
                                    String adm = rs.getString("admissionnumber");
                                    row[0] = adm;
                                    row[1] = Globals.fullName(adm);
                                    row[2] = Globals.className(rs.getString("classcode"));

                                    row[3] = Globals.streamName(rs.getString("streamcode"));


                                    row[5] = Globals.balanceCalculator(adm);
                                    total += Globals.balanceCalculator(adm);

                                    ps = con.prepareStatement("Select sum(amount) from reciepts where admnumber='" + adm + "'");
                                    ResultSet rx = ps.executeQuery();
                                    if (rx.next()) {
                                        row[4] = rx.getString("Sum(amount)");
                                    }
                                    if (row[4] == null) {
                                        row[4] = "";
                                    }
                                    model.addRow(row);
                                }
                                row[0] = "TOTAL";
                                row[1] = "";
                                row[2] = "";
                                row[3] = "";
                                row[4] = "";
                                row[5] = total;
                                model.addRow(row);

                            } else {
                                infor.setText(streamfilter.getSelectedItem() + " Balances");
                                ps = con.prepareStatement("Select admissionnumber,currentform as classcode,currentstream as streamcode from admission  where  currentstream='" + Globals.streamcode(streamfilter.getSelectedItem().toString()) + "' ");
                                rs = ps.executeQuery();
                                while (rs.next()) {
                                    String adm = rs.getString("admissionnumber");
                                    row[0] = adm;
                                    row[1] = Globals.fullName(adm);
                                    row[2] = Globals.className(rs.getString("classcode"));

                                    row[3] = Globals.streamName(rs.getString("streamcode"));


                                    row[5] = Globals.balanceCalculator(adm);
                                    total += Globals.balanceCalculator(adm);

                                    ps = con.prepareStatement("Select sum(amount) from reciepts where admnumber='" + adm + "'");
                                    ResultSet rx = ps.executeQuery();
                                    if (rx.next()) {
                                        row[4] = rx.getString("Sum(amount)");
                                    }
                                    if (row[4] == null) {
                                        row[4] = "";
                                    }
                                    model.addRow(row);
                                }
                                row[0] = "TOTAL";
                                row[1] = "";
                                row[2] = "";
                                row[3] = "";
                                row[4] = "";
                                row[5] = total;
                                model.addRow(row);


                            }
                        }

                        dia.dispose();

                    } catch (SQLException sq) {
                        JOptionPane.showMessageDialog(holder, sq.getMessage());
                    } finally {
                        try {
                            con.close();
                            ps.close();
                        } catch (SQLException sq) {

                        }

                    }

                    dia.dispose();
                }

            }.start();

        });

        amountfilter.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {

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

                        String amount = JOptionPane.showInputDialog(holder, "Enter The Balance Figure To Filter Balance Over it", "0");

                        if (DataValidation.number2(amount)) {
                            dia.add(bar);
                            dia.setVisible(true);
                            int Amount = Integer.parseInt(amount);
                            try {
                                con = DbConnection.connectDb();
                                model.getDataVector().removeAllElements();

                                if (classfilter.getSelectedIndex() > 0) {
                                    if (streamfilter.getSelectedIndex() == 0) {
                                        infor.setText(classfilter.getSelectedItem() + " Balances Over Ksh. " + Amount + ".00");
                                        ps = con.prepareStatement("Select admissionnumber,currentform as classcode,currentstream as streamcode from admission  where  currentform='" + Globals.classCode(classfilter.getSelectedItem().toString()) + "' ");
                                        rs = ps.executeQuery();
                                        while (rs.next()) {

                                            String adm = rs.getString("admissionnumber");
                                            double bal = Globals.balanceCalculator(adm);
                                            row[0] = adm;
                                            row[1] = Globals.fullName(adm);
                                            row[2] = Globals.className(rs.getString("classcode"));

                                            row[3] = Globals.streamName(rs.getString("streamcode"));


                                            row[5] = bal;


                                            ps = con.prepareStatement("Select sum(amount) from reciepts where admnumber='" + adm + "'");
                                            ResultSet rx = ps.executeQuery();
                                            if (rx.next()) {
                                                row[4] = rx.getString("Sum(amount)");
                                            }
                                            if (bal >= Amount) {
                                                model.addRow(row);
                                            }
                                        }
                                    } else {
                                        infor.setText(classfilter.getSelectedItem().toString() + "  " + streamfilter.getSelectedItem() + " Balances Over Ksh. " + Amount + ".00");
                                        ps = con.prepareStatement("Select admissionnumber,currentform as classcode,currentstream as streamcode from admission  where  currentstream='" + Globals.streamcode(streamfilter.getSelectedItem().toString()) + "' and  currentform='" + Globals.classCode(classfilter.getSelectedItem().toString()) + "'");
                                        rs = ps.executeQuery();
                                        while (rs.next()) {

                                            String adm = rs.getString("admissionnumber");
                                            double bal = Globals.balanceCalculator(adm);
                                            row[0] = adm;
                                            row[1] = Globals.fullName(adm);
                                            row[2] = Globals.className(rs.getString("classcode"));

                                            row[3] = Globals.streamName(rs.getString("streamcode"));


                                            row[5] = bal;


                                            ps = con.prepareStatement("Select sum(amount) from reciepts where admnumber='" + adm + "'");
                                            ResultSet rx = ps.executeQuery();
                                            if (rx.next()) {
                                                row[4] = rx.getString("Sum(amount)");
                                            }
                                            if (bal > Amount) {
                                                model.addRow(row);
                                            }
                                        }

                                    }

                                } else {


                                    if (streamfilter.getSelectedIndex() == 0) {
                                        infor.setText(" Balances Over Ksh. " + Amount + ".00");
                                        ps = con.prepareStatement("Select admissionnumber,currentform as classcode,currentstream as streamcode from admission ");
                                        rs = ps.executeQuery();
                                        while (rs.next()) {

                                            String adm = rs.getString("admissionnumber");
                                            double bal = Globals.balanceCalculator(adm);
                                            row[0] = adm;
                                            row[1] = Globals.fullName(adm);
                                            row[2] = Globals.className(rs.getString("classcode"));

                                            row[3] = Globals.streamName(rs.getString("streamcode"));


                                            row[5] = bal;


                                            ps = con.prepareStatement("Select sum(amount) from reciepts where admnumber='" + adm + "'");
                                            ResultSet rx = ps.executeQuery();
                                            if (rx.next()) {
                                                row[4] = rx.getString("Sum(amount)");
                                            }
                                            if (bal >= Amount) {
                                                model.addRow(row);
                                            }
                                        }
                                    } else {
                                        infor.setText(streamfilter.getSelectedItem() + " Balances Over Ksh. " + Amount + ".00");
                                        ps = con.prepareStatement("Select admissionnumber,currentform as classcode,currentstream as streamcode from admission  where  currentstream='" + Globals.streamcode(streamfilter.getSelectedItem().toString()) + "'");
                                        rs = ps.executeQuery();
                                        while (rs.next()) {

                                            String adm = rs.getString("admissionnumber");
                                            double bal = Globals.balanceCalculator(adm);
                                            row[0] = adm;
                                            row[1] = Globals.fullName(adm);
                                            row[2] = Globals.className(rs.getString("classcode"));

                                            row[3] = Globals.streamName(rs.getString("streamcode"));


                                            row[5] = bal;


                                            ps = con.prepareStatement("Select sum(amount) from reciepts where admnumber='" + adm + "'");
                                            ResultSet rx = ps.executeQuery();
                                            if (rx.next()) {
                                                row[4] = rx.getString("Sum(amount)");
                                            }
                                            if (bal > Amount) {
                                                model.addRow(row);
                                            }
                                        }

                                    }


                                }

                                dia.dispose();
                            } catch (NumberFormatException | SQLException sq) {
                                JOptionPane.showMessageDialog(holder, sq.getMessage());
                            } finally {
                                try {
                                    con.close();
                                    ps.close();
                                } catch (SQLException sq) {

                                }

                            }

                        } else {
                            dia.dispose();
                            JOptionPane.showMessageDialog(holder, "Invalid Balance Figure");
                        }
                        dia.dispose();
                    }

                }.start();


            }
        });

        statement.addActionListener((ActionEvent e) -> {
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
                    dia.add(bar);
                    dia.setVisible(true);

                    if (tab.getSelectedRowCount() < 1) {
                        dia.dispose();
                        JOptionPane.showMessageDialog(holder, "Kindly Select The Student To generate This Terms Fee Statement");

                    } else {
                        String admnumber = model.getValueAt(tab.getSelectedRow(), 0).toString();
                        ReportGenerator.FeeStatementGenerator(admnumber);
                        dia.dispose();
                    }

                }

            }.start();
        });


        OveraalStatement.addActionListener((ActionEvent e) -> {
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
                    dia.add(bar);
                    dia.setVisible(true);

                    if (tab.getSelectedRowCount() < 1) {
                        dia.dispose();
                        JOptionPane.showMessageDialog(holder, "Kindly Select The Student To generate This Terms Fee Statement");

                    } else {
                        String admnumber = model.getValueAt(tab.getSelectedRow(), 0).toString();
                        ReportGenerator.totalFeeStatementGenerator(admnumber);
                        dia.dispose();
                    }

                }

            }.start();
        });

        return holder;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
//        Object obj = e.getSource();
//    if (obj == totalexpected) {
//         
//          
//            new Thread() {
//                @Override
//                public void run() {
//                    JWindow dia = new JWindow();
//                    JProgressBar bar = new JProgressBar();
//                    bar.setIndeterminate(true);
//
//                    dia.setSize(300, 60);
//
//                    bar.setBorder(new TitledBorder("Processing..."));
//                    dia.setAlwaysOnTop(true);
//                    dia.setLocationRelativeTo(CurrentFrame.mainFrame());
//                    dia.setIconImage(FrameProperties.icon());
//                    dia.add(bar);
//                    dia.setVisible(true);
//             while(model.getRowCount()>0)
//             {
//                 model.removeRow(0);
//             }
//
//                   
//
//                    if (paymentcat.getSelectedIndex() > 0) {
//                        paymentname = paymentcat.getSelectedItem().toString();
//
//                    } else {
//                        paymentname = "".toUpperCase();
//                    }
//                    if (cleared.isSelected()) {
//                        status = " Cleared Students Towards ".toUpperCase();
//                    } else {
//                        status = "".toUpperCase();
//                    }
//                    if (streamfilter.getSelectedIndex() > 0) {
//                        streamname = streamfilter.getSelectedItem().toString();
//                    } else {
//                        streamname = "";
//                    }
//                    if (classfilter.getSelectedIndex() > 0) {
//                        classname = classfilter.getSelectedItem().toString() + " ";
//                    } else {
//                        classname = "";
//                    }
//                    title = classname + streamname + status + paymentname.toUpperCase();
//                   // infor.setText(title);
//
//                    if (totalexpected.isSelected()) {
//                        paymentcat.setEnabled(false);
//                    } else {
//                        paymentcat.setEnabled(true);
//                    }
//                   
//                    dia.dispose();
//                }
//
//            }.start();
//            
//            paymentcat.addActionListener(this);
//           
//        }
    }
}
