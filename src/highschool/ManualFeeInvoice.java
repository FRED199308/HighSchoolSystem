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
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JFrame;

import javax.swing.JOptionPane;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JWindow;

import static javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE;

import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;

import net.miginfocom.swing.MigLayout;

/**
 * @author FRED
 */
public class ManualFeeInvoice extends JFrame implements ActionListener {

    public static void main(String[] args) {
        new ManualFeeInvoice();
    }

    private int width = 1100, height = 600;
    private FredButton save, cancel, bill;
    private PreparedStatement ps;
    private Connection con;
    private FredLabel search, sort, amount;
    private JTextField jsearch, jamount;
    private JTable tab, tab2;
    private FredCombo jsort, jpaytype;
    private JScrollPane pane, pane2;
    private DefaultTableModel model, model2;
    private FredCheckBox starndard;
    private FredCombo term = new FredCombo();
    private FredCombo academicYear = new FredCombo();
    private String description = "";
    private FredLabel pr = new FredLabel("Program");
    private FredCombo jProgram = new FredCombo("Sort By School Program");
    Object row[];
    boolean proceed = true;
    private Object cols[] = {"admission No.", "Name", "Class", "Stream"};
    private FredButton unbil = new FredButton("Reverse Billing");

    ResultSet rs;

    public ManualFeeInvoice() {
        setSize(width, height);
        setLocationRelativeTo(null);
        setLayout(new MigLayout());
        setTitle("Manual Payment Billing");
        getContentPane().setBackground(Color.CYAN);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        this.addWindowListener(new WindowAdapter() {

            @Override
            public void windowClosing(WindowEvent e) {
                try {
                    con.close();
                } catch (SQLException sq) {

                }
                CurrentFrame.currentWindow();
                e.getWindow().dispose();
            }
        });
        con = DbConnection.connectDb();
        this.setIconImage(FrameProperties.icon());
        ((JComponent) getContentPane()).setBorder(
                BorderFactory.createMatteBorder(5, 5, 5, 5, Color.MAGENTA));
        search = new FredLabel("Search Student By Name/admission Number");
        jsearch = new JTextField();

        jamount = new JTextField();
        amount = new FredLabel("Customized Amount");
        starndard = new FredCheckBox("Use Set Standard Payment Invoice");
        sort = new FredLabel("Sort By Class");
        jsort = new FredCombo("Select Class");
        save = new FredButton("Bill");
        bill = new FredButton("Bill All Payable Voteheads");
        cancel = new FredButton("Close");
        jpaytype = new FredCombo("Select Vote Head");
        tab = new JTable();
        tab2 = new JTable();
        model = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int col) {
                return false;
            }
        };
        term.setBorder(new TitledBorder("Select Term"));
        academicYear.setBorder(new TitledBorder("Select Year"));
        model2 = new DefaultTableModel();
        model.setColumnIdentifiers(cols);
        model2.setColumnIdentifiers(cols);
        tab.setModel(model);
        tab2.setModel(model2);
        pane = new JScrollPane(tab);
        pane2 = new JScrollPane(tab2);

        for (int k = 2015; k <= Globals.academicYear(); ++k) {
            academicYear.addItem(k);
        }
        academicYear.setSelectedItem(Globals.academicYear());
        term.setSelectedItem(Globals.currentTermName());
        add(search, "gapleft 10,growx,pushx");
        add(jsearch, "gapleft 20,growx,pushx");

        add(jsort, "gapleft 30,growx,pushx");
        add(jProgram, "growx,pushx,wrap");
        jsort.setBorder(new TitledBorder("Sort By Class"));
        jProgram.setBorder(new TitledBorder("Sort By Program"));
        //add(pr, "gapleft 30,growx,pushx,wrap");
        //add(jProgram, "growx,pushx");
        jamount.setBorder(new TitledBorder("Custom Amount"));
        add(pane, "gapleft 30,grow,push,spanx,wrap");
        add(starndard, "gapleft 50");

        add(jamount, "gapleft 30,growx");
        add(term, "gapleft 30,growx");
        add(academicYear, "gapleft 30,growx");
        add(jpaytype, "gapleft 30,growx,wrap");
        add(cancel, "gapleft 70");
        add(save, "gapleft 70");
        add(unbil, "gapleft 30");
        add(bill, "gapleft 30");
        save.addActionListener(this);
        cancel.addActionListener(this);
        starndard.addActionListener(this);
        jsort.addActionListener(this);
        bill.addActionListener(this);
        unbil.addActionListener(this);
        jProgram.addActionListener(this);
        row = new Object[cols.length];

        setVisible(true);
        try {

            String sql = "Select firstname,middlename,lastname,currentform,currentstream,admissionnumber from admission where  currentform like '" + "FM" + "%'  order by admissionnumber";
            con = DbConnection.connectDb();
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                row[0] = rs.getString("admissionnumber");
                row[1] = rs.getString("firstname") + "       " + rs.getString("middlename") + "     " + rs.getString("lastname");

                String sql1 = "select classname from classes where classcode='" + rs.getString("currentform") + "'";
                ps = con.prepareStatement(sql1);
                ResultSet RX = ps.executeQuery();
                if (RX.next()) {
                    row[2] = RX.getString("ClassName");
                }
                String sql2 = "select streamname from streams where streamcode='" + rs.getString("currentstream") + "'";
                ps = con.prepareStatement(sql2);
                ResultSet Rx = ps.executeQuery();
                if (Rx.next()) {
                    row[3] = Rx.getString("streamName");
                }

                model.addRow(row);
            }
            String sql9 = "Select* from terms";
            ps = con.prepareStatement(sql9);
            rs = ps.executeQuery();
            while (rs.next()) {
                term.addItem(rs.getString("TermName"));

            }
            String sql4 = "Select * from classes where precision1<5 order by precision1";
            ps = con.prepareStatement(sql4);
            rs = ps.executeQuery();
            while (rs.next()) {
                jsort.addItem(rs.getString("ClassName"));
            }
            //  String sql5 = "Select distinct voteheadid from StudentPayableVoteHeads where termcode='" + Globals.currentTerm() + "' and academicYear='" + Globals.academicYear() + "' and amountperhead!='" + "0" + "' and amountperhead!='" + "" + "'";
            String sql5 = "Select distinct voteheadid from StudentPayableVoteHeads where  amountperhead!='" + "0" + "' and amountperhead!='" + "" + "'";
            ps = con.prepareStatement(sql5);
            rs = ps.executeQuery();
            while (rs.next()) {
                jpaytype.addItem(Globals.VoteHeadName(rs.getString("voteheadid")));
            }
            int programconter = 0;
            String qeurry = "Select programname from programs";
            ps = con.prepareStatement(qeurry);
            rs = ps.executeQuery();
            while (rs.next()) {
                programconter++;
                jProgram.addItem(rs.getString("Programname"));
            }
            if (programconter == 1) {
                jProgram.setSelectedIndex(1);
            }


        } catch (SQLException sq) {
            JOptionPane.showMessageDialog(this, sq.getMessage());
        }
        jamount.addKeyListener(new KeyAdapter() {

            @Override
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();
                if (Character.isAlphabetic(c) || jamount.getText().length() > 30) {
                    getToolkit().beep();
                    e.consume();
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
                    String sql = "Select firstname,middlename,lastname,currentform,currentstream,admissionnumber from admission where  currentform !='" + Globals.classCode("Alumni") + "' and admissionnumber like '" + jsearch.getText() + "%' or firstname like '" + jsearch.getText() + "%' or middlename like '" + jsearch.getText() + "%' or lastname like '" + jsearch.getText() + "%' order by admissionnumber";
                    con = DbConnection.connectDb();
                    ps = con.prepareStatement(sql);
                    rs = ps.executeQuery();
                    while (rs.next()) {
                        row[0] = rs.getString("admissionnumber");
                        row[1] = rs.getString("firstname") + "       " + rs.getString("middlename") + "     " + rs.getString("lastname");

                        String sql1 = "select classname from classes where classcode='" + rs.getString("currentform") + "'";
                        ps = con.prepareStatement(sql1);
                        ResultSet RX = ps.executeQuery();
                        if (RX.next()) {
                            row[2] = RX.getString("ClassName");
                        }
                        String sql2 = "select streamname from streams where streamcode='" + rs.getString("currentstream") + "'";
                        ps = con.prepareStatement(sql2);
                        ResultSet Rx = ps.executeQuery();
                        if (Rx.next()) {
                            row[3] = Rx.getString("streamName");
                        }

                        model.addRow(row);
                    }
                } catch (SQLException sq) {
                }

            }
        });

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object obj = e.getSource();
        if (obj == starndard) {
            if (starndard.isSelected()) {
                jamount.setVisible(false);
            } else {
                jamount.setVisible(true);
            }

        } else if (obj == cancel) {
            try {
                con.close();
            } catch (SQLException sq) {

            }
            CurrentFrame.currentWindow();
            dispose();
        } else if (obj == bill) {


            if (tab.getSelectedRowCount() < 1) {
                getToolkit().beep();
                JOptionPane.showMessageDialog(this, "Kindly Select The Students To Bill In Reference To Fee Structure from The Table Above");
            } else {
                new Thread() {
                    @Override
                    public void run() {
                        JWindow dia = new JWindow();
                        JProgressBar bar = new JProgressBar();
                        bar.setIndeterminate(true);

                        dia.setSize(300, 60);

                        bar.setBorder(new TitledBorder("System Performing Total Billing"));
                        dia.setAlwaysOnTop(true);
                        dia.setLocationRelativeTo(CurrentFrame.mainFrame());
                        dia.setIconImage(FrameProperties.icon());
                        dia.add(bar);
                        dia.setVisible(true);


                        boolean success = false;
                        int counter = 0;
                        int[] selectedRows = tab.getSelectedRows();
                        Object row[] = new Object[model.getColumnCount()];
                        if (selectedRows.length > 0) {
                            for (int w = 0; w < selectedRows.length; ++w) {
                                row[0] = model.getValueAt(selectedRows[w], 0);
                                row[1] = model.getValueAt(selectedRows[w], 1);
                                row[2] = model.getValueAt(selectedRows[w], 2);
                                row[3] = model.getValueAt(selectedRows[w], 3);

                                model2.addRow(row);

                            }
                            for (int k = 0; k < model2.getRowCount(); ++k) {

                                try {
                                    String classcode = Globals.classCode(model2.getValueAt(k, 2).toString());

                                    String voteheadid = "";
                                    counter++;
                                    String adm = model2.getValueAt(k, 0).toString();
                                    String program = Globals.studentProgram(adm);
                                    double fee = 0, openingbal = 0;

                                    String sql7 = "Select amountperhead,voteheadid from studentpayablevoteheads where termcode='" + Globals.termcode(term.getSelectedItem().toString()) + "' and academicYear='" + academicYear.getSelectedItem().toString() + "'  and amountperhead!='" + "" + "' and amountPerhead!='" + "0" + "' and program='" + program + "' and classcode='" + classcode + "'";
                                    ps = con.prepareStatement(sql7);
                                    rs = ps.executeQuery();
                                    while (rs.next()) {

                                        fee = rs.getDouble("amountperhead");
                                        voteheadid = rs.getString("voteheadid");
                                        ps = con.prepareStatement("Delete from payablevoteheadperstudent where admnumber='" + adm + "' and term='" + term.getSelectedItem() + "' and academicyear='" + academicYear.getSelectedItem() + "' and voteheadid='" + voteheadid + "'");
                                        ps.execute();
                                        String sql12 = "Insert into payablevoteheadperstudent values('" + adm + "','" + term.getSelectedItem() + "','" + academicYear.getSelectedItem() + "','" + voteheadid + "','" + fee + "',curDate(),'" + "INV" + "')";
                                        ps = con.prepareStatement(sql12);
                                        ps.execute();
                                        openingbal = 0;

                                    }

                                } catch (HeadlessException | NumberFormatException | SecurityException | SQLException sq) {
                                    revalidate();
                                    repaint();
                                    dia.dispose();
                                    sq.printStackTrace();
                                    ;
                                    JOptionPane.showMessageDialog(CurrentFrame.mainFrame(), sq.getMessage());
                                }
                            }
                            while (model2.getRowCount() > 0) {
                                model2.removeRow(0);
                            }
                            if (counter > 0) {
                                revalidate();
                                repaint();
                                dia.dispose();
                                JOptionPane.showMessageDialog(CurrentFrame.mainFrame(), counter + " Student(s) have been Billed Successfully");
                            }

                        }


                        revalidate();
                        repaint();
                        dia.dispose();
                    }

                }.start();

            }


        } else if (obj == jsort) {
            while (model.getRowCount() > 0) {
                model.removeRow(0);
            }

            try {
                String sql3 = "Select classcode from classes where classname='" + jsort.getSelectedItem() + "'";
                ps = con.prepareStatement(sql3);
                rs = ps.executeQuery();
                if (rs.next()) {
                    String classcode = rs.getString("classcode");
                    String sql = "Select firstname,middlename,lastname,currentform,currentstream,admissionnumber from admission where currentform='" + classcode + "' order by admissionnumber";
                    con = DbConnection.connectDb();
                    ps = con.prepareStatement(sql);
                    rs = ps.executeQuery();
                    while (rs.next()) {
                        row[0] = rs.getString("admissionnumber");
                        row[1] = rs.getString("firstname") + "       " + rs.getString("middlename") + "     " + rs.getString("lastname");

                        String sql1 = "select classname from classes where classcode='" + rs.getString("currentform") + "'";
                        ps = con.prepareStatement(sql1);
                        ResultSet RX = ps.executeQuery();
                        if (RX.next()) {
                            row[2] = RX.getString("ClassName");
                        }
                        String sql2 = "select streamname from streams where streamcode='" + rs.getString("currentstream") + "'";
                        ps = con.prepareStatement(sql2);
                        ResultSet Rx = ps.executeQuery();
                        if (Rx.next()) {
                            row[3] = Rx.getString("streamName");
                        }

                        model.addRow(row);
                    }
                }

            } catch (SQLException ex) {
                Logger.getLogger(ManualFeeInvoice.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else if (obj == jProgram) {
            while (model.getRowCount() > 0) {
                model.removeRow(0);
            }

            try {
                if (jsort.getSelectedIndex() == 0) {


                    String sql = "Select firstname,middlename,lastname,currentform,currentstream,admissionnumber,program from admission where  program='" + jProgram.getSelectedItem() + "' order by admissionnumber";
                    con = DbConnection.connectDb();
                    ps = con.prepareStatement(sql);
                    rs = ps.executeQuery();
                    while (rs.next()) {
                        row[0] = rs.getString("admissionnumber");
                        row[1] = rs.getString("firstname") + "       " + rs.getString("middlename") + "     " + rs.getString("lastname");
                        row[4] = rs.getString("Program");
                        String sql1 = "select classname from classes where classcode='" + rs.getString("currentform") + "'";
                        ps = con.prepareStatement(sql1);
                        ResultSet RX = ps.executeQuery();
                        if (RX.next()) {
                            row[2] = RX.getString("ClassName");
                        }
                        String sql2 = "select streamname from streams where streamcode='" + rs.getString("currentstream") + "'";
                        ps = con.prepareStatement(sql2);
                        ResultSet Rx = ps.executeQuery();
                        if (Rx.next()) {
                            row[3] = Rx.getString("streamName");
                        }

                        model.addRow(row);
                    }


                } else {

                    String sql3 = "Select classcode from classes where classname='" + jsort.getSelectedItem() + "'";
                    ps = con.prepareStatement(sql3);
                    rs = ps.executeQuery();
                    if (rs.next()) {
                        String classcode = rs.getString("classcode");
                        String sql = "Select firstname,middlename,program,lastname,currentform,currentstream,admissionnumber from admission where currentform='" + classcode + "' and program='" + jProgram.getSelectedItem() + "' order by admissionnumber";
                        con = DbConnection.connectDb();
                        ps = con.prepareStatement(sql);
                        rs = ps.executeQuery();
                        while (rs.next()) {
                            row[0] = rs.getString("admissionnumber");
                            row[1] = rs.getString("firstname") + "       " + rs.getString("middlename") + "     " + rs.getString("lastname");

                            String sql1 = "select classname from classes where classcode='" + rs.getString("currentform") + "'";
                            ps = con.prepareStatement(sql1);
                            ResultSet RX = ps.executeQuery();
                            if (RX.next()) {
                                row[2] = RX.getString("ClassName");
                            }
                            String sql2 = "select streamname from streams where streamcode='" + rs.getString("currentstream") + "'";
                            ps = con.prepareStatement(sql2);
                            ResultSet Rx = ps.executeQuery();
                            if (Rx.next()) {
                                row[3] = Rx.getString("streamName");
                            }

                            model.addRow(row);
                        }
                    }
                }


            } catch (SQLException ex) {
                Logger.getLogger(ManualFeeInvoice.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else if (obj == unbil) {
            if (tab.getSelectedRowCount() < 1) {
                getToolkit().beep();
                JOptionPane.showMessageDialog(this, "Kindly Select The Students To Reverse Bill from The Table Above");
            } else {

                new Thread() {
                    @Override
                    public void run() {
                        JWindow dia = new JWindow();
                        JProgressBar bar = new JProgressBar();
                        bar.setIndeterminate(true);

                        dia.setSize(300, 60);

                        bar.setBorder(new TitledBorder("System Performing Reverse Billing"));
                        dia.setAlwaysOnTop(true);
                        dia.setLocationRelativeTo(CurrentFrame.mainFrame());
                        dia.setIconImage(FrameProperties.icon());
                        dia.add(bar);
                        dia.setVisible(true);
                        if (jpaytype.getSelectedIndex() == 0) {
                            revalidate();
                            repaint();
                            dia.dispose();
                            getToolkit().beep();
                            JOptionPane.showMessageDialog(null, "Select The Payment Category To  Reverse Bill The Student(s)");
                        } else {

                            if (proceed) {

                                boolean success = false;
                                int counter = 0;
                                int[] selectedRows = tab.getSelectedRows();
                                Object row[] = new Object[model.getColumnCount()];
                                if (selectedRows.length > 0) {
                                    for (int w = 0; w < selectedRows.length; ++w) {
                                        row[0] = model.getValueAt(selectedRows[w], 0);
                                        row[1] = model.getValueAt(selectedRows[w], 1);
                                        row[2] = model.getValueAt(selectedRows[w], 2);
                                        row[3] = model.getValueAt(selectedRows[w], 3);

                                        model2.addRow(row);

                                    }
                                    for (int k = 0; k < model2.getRowCount(); ++k) {

                                        try {
                                            String classcode = Globals.classCode(model2.getValueAt(k, 2).toString());

                                            String streamcode = "";
                                            String querry = "Select Streamcode from streams where streamname='" + model2.getValueAt(k, 3) + "'";
                                            ps = con.prepareStatement(querry);
                                            rs = ps.executeQuery();
                                            if (rs.next()) {
                                                streamcode = rs.getString("Streamcode");
                                            } else {
                                                revalidate();
                                                repaint();
                                                dia.dispose();
                                                JOptionPane.showMessageDialog(CurrentFrame.mainFrame(), "Stream Not Traced");
                                            }

                                            String pycode = "";
                                            pycode = Globals.voteHeadId(jpaytype.getSelectedItem().toString());

                                            String sql = "Select * from PayableVoteHeadPerStudent where voteheadid='" + pycode + "' and admnumber='" + model2.getValueAt(k, 0) + "' and term='" + term.getSelectedItem() + "' and academicyear='" + academicYear.getSelectedItem() + "' ";
                                            ps = con.prepareStatement(sql);
                                            rs = ps.executeQuery();
                                            if (rs.next()) {


                                                counter++;
                                                String sql12 = "Delete  from PayableVoteHeadPerStudent where admnumber='" + model2.getValueAt(k, 0) + "' and Term='" + term.getSelectedItem() + "' and academicYear='" + academicYear.getSelectedItem() + "' and VoteHeadId='" + pycode + "'";
                                                ps = con.prepareStatement(sql12);
                                                ps.execute();


                                            } else {

                                                revalidate();
                                                repaint();
                                                dia.setAlwaysOnTop(false);
                                                getToolkit().beep();
                                                JOptionPane.showMessageDialog(CurrentFrame.mainFrame(), "The System Has Detected that " + model2.getValueAt(k, 1) + " Was Not  invoiced For " + jpaytype.getSelectedItem() + " For the selected Period" + "\n No Action Taken");
                                                dia.setAlwaysOnTop(true);

                                            }

                                        } catch (HeadlessException | NumberFormatException | SecurityException | SQLException sq) {
                                            revalidate();
                                            repaint();
                                            dia.dispose();
                                            sq.printStackTrace();
                                            ;
                                            JOptionPane.showMessageDialog(CurrentFrame.mainFrame(), sq.getMessage());
                                        }
                                    }
                                    while (model2.getRowCount() > 0) {
                                        model2.removeRow(0);
                                    }
                                    if (counter > 0) {
                                        revalidate();
                                        repaint();
                                        dia.dispose();
                                        JOptionPane.showMessageDialog(CurrentFrame.mainFrame(), counter + " Student(s) have been Reverse Billed Successfully\n Kindly Note Payments Before reversed Billing are Not Reversed");
                                    }

                                }
                            }

                        }
                        revalidate();
                        repaint();
                        dia.dispose();
                    }

                }.start();


            }
        } else if (obj == save) {

            if (tab.getSelectedRowCount() < 1) {
                getToolkit().beep();
                JOptionPane.showMessageDialog(this, "Kindly Select The Students To Bill from The Table Above");
            } else {
                new Thread() {
                    @Override
                    public void run() {
                        JWindow dia = new JWindow();
                        JProgressBar bar = new JProgressBar();
                        bar.setIndeterminate(true);

                        dia.setSize(300, 60);

                        bar.setBorder(new TitledBorder("System Performing Manual Billing"));
                        dia.setAlwaysOnTop(true);
                        dia.setLocationRelativeTo(CurrentFrame.mainFrame());
                        dia.setIconImage(FrameProperties.icon());
                        dia.add(bar);
                        dia.setVisible(true);
                        if (jpaytype.getSelectedIndex() == 0) {
                            revalidate();
                            repaint();
                            dia.dispose();
                            getToolkit().beep();
                            JOptionPane.showMessageDialog(null, "Select The Payment Category To Bill The Student(s)");
                        } else {
                            if (jamount.isVisible()) {
                                if (jamount.getText().equalsIgnoreCase("")) {
                                    revalidate();
                                    repaint();
                                    dia.dispose();
                                    getToolkit().beep();
                                    JOptionPane.showMessageDialog(null, "Kindly Enter A valid Amount Figure");
                                    proceed = false;
                                } else {
                                    proceed = true;
                                }

                            } else {
                                proceed = true;
                            }
                            if (proceed) {

                                boolean success = false;
                                int counter = 0;
                                int[] selectedRows = tab.getSelectedRows();
                                Object row[] = new Object[model.getColumnCount()];
                                if (selectedRows.length > 0) {
                                    for (int w = 0; w < selectedRows.length; ++w) {
                                        row[0] = model.getValueAt(selectedRows[w], 0);
                                        row[1] = model.getValueAt(selectedRows[w], 1);
                                        row[2] = model.getValueAt(selectedRows[w], 2);
                                        row[3] = model.getValueAt(selectedRows[w], 3);

                                        model2.addRow(row);

                                    }
                                    for (int k = 0; k < model2.getRowCount(); ++k) {

                                        try {
                                            String classcode = Globals.classCode(model2.getValueAt(k, 2).toString());

                                            String streamcode = "";
                                            String querry = "Select Streamcode from streams where streamname='" + model2.getValueAt(k, 3) + "'";
                                            ps = con.prepareStatement(querry);
                                            rs = ps.executeQuery();
                                            if (rs.next()) {
                                                streamcode = rs.getString("Streamcode");
                                            } else {
                                                revalidate();
                                                repaint();
                                                dia.dispose();
                                                JOptionPane.showMessageDialog(CurrentFrame.mainFrame(), "Stream Not Traced");
                                            }

                                            String pycode = "";
                                            pycode = Globals.voteHeadId(jpaytype.getSelectedItem().toString());

                                            String sql = "Select * from PayableVoteHeadPerStudent where voteheadid='" + pycode + "' and admnumber='" + model2.getValueAt(k, 0) + "' and term='" + term.getSelectedItem() + "' and academicyear='" + academicYear.getSelectedItem() + "' ";
                                            ps = con.prepareStatement(sql);
                                            rs = ps.executeQuery();
                                            if (rs.next()) {
                                                revalidate();
                                                repaint();
                                                dia.setAlwaysOnTop(false);
                                                getToolkit().beep();
                                                JOptionPane.showMessageDialog(CurrentFrame.mainFrame(), "The System Has Detected that " + model2.getValueAt(k, 1) + " Is Already invoiced For " + jpaytype.getSelectedItem() + " On the Selected Period" + "\n The System Cannot Apply Double Billing");
                                                dia.setAlwaysOnTop(true);
                                            } else {

                                                double bal = 0;

                                                if (jamount.isVisible()) {
                                                    dia.setAlwaysOnTop(false);
                                                    description = JOptionPane.showInputDialog(null, "Input Descprition For Not Using Standard Invoice Amount");

                                                    bal = Integer.parseInt(jamount.getText());

                                                    dia.setAlwaysOnTop(true);

                                                } else {
                                                    String cllass = Globals.classCode(model2.getValueAt(k, 2).toString());
                                                    String program = Globals.studentProgram(model2.getValueAt(k, 0).toString());
                                                    String sql4 = "Select amountperhead from StudentPayableVoteHeads where termcode='" + Globals.termcode(term.getSelectedItem().toString()) + "'  and voteheadid='" + pycode + "' and academicyear='" + academicYear.getSelectedItem() + "' and classcode='" + cllass + "' and program='" + program + "'";
                                                    ps = con.prepareStatement(sql4);
                                                    rs = ps.executeQuery();
                                                    if (rs.next()) {

                                                        bal = rs.getDouble("amountperhead");

                                                    } else {
                                                        bal = 0;
                                                    }


                                                    description = "INV";
                                                    //  bal = rs.getDouble("amountperhead");
                                                }

                                                counter++;
                                                String sql12 = "Insert into PayableVoteHeadPerStudent values('" + model2.getValueAt(k, 0) + "','" + term.getSelectedItem() + "','" + academicYear.getSelectedItem() + "','" + pycode + "','" + bal + "',curDate(),'" + description + "')";
                                                ps = con.prepareStatement(sql12);
                                                ps.execute();

                                            }

                                        } catch (HeadlessException | NumberFormatException | SecurityException | SQLException sq) {
                                            revalidate();
                                            repaint();
                                            dia.dispose();
                                            sq.printStackTrace();
                                            ;
                                            JOptionPane.showMessageDialog(CurrentFrame.mainFrame(), sq.getMessage());
                                        }
                                    }
                                    while (model2.getRowCount() > 0) {
                                        model2.removeRow(0);
                                    }
                                    if (counter > 0) {
                                        revalidate();
                                        repaint();
                                        dia.dispose();
                                        JOptionPane.showMessageDialog(CurrentFrame.mainFrame(), counter + " Student(s) have been Billed Successfully");
                                    }

                                }
                            }

                        }
                        revalidate();
                        repaint();
                        dia.dispose();
                    }

                }.start();

            }

        }
    }

}
