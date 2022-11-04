/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package highschool;

import java.awt.Color;
import java.awt.Font;
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
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;

import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;

import static javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE;

import javax.swing.table.DefaultTableModel;

import net.miginfocom.swing.MigLayout;

/**
 * @author FRED
 */
public class RecievePayment extends JFrame implements ActionListener {

    public static void main(String[] args) {
        new RecievePayment();
    }

    private int height = 550;
    private int width = 1000;
    private Connection con;
    private PreparedStatement ps;
    private ResultSet rs;
    private FredDateChooser jdate;
    private FredButton save, cancel, votehead;
    private FredCheckBox option, confirmation, reciept1;
    private FredLabel receiptNumber, classl, name, search, amount, date, pmmode, slipnumber, infor, infor2;
    private FredCombo jclassl, jname, jpmmode;
    private JTextField jamount, adm, jslipnumber, jReceiptNumber;
    private String admissionNumber = "", AMOUNT = "";
    private FredButton save2 = new FredButton("Save");
    private FredButton close = new FredButton("Close");
    //  private String voteheads[][]=new String
    String ADM = "";
    private JTable tab = new JTable();
    private JDialog dia = new JDialog();
    private JScrollPane pane = new JScrollPane(tab);
    private DefaultTableModel model = new DefaultTableModel();
    private Object cols[] = {"Votehead Name", "Votehead Balance", "Credit Amount"};
    ;
    private Object row[] = new Object[cols.length];

    public RecievePayment() {
        setSize(width, height);
        setLocationRelativeTo(null);
        setLayout(null);
        setTitle("Record Fee Panel");
        dia.setSize(500, 400);
        dia.setLocationRelativeTo(CurrentFrame.secondFrame);
        dia.setLayout(new MigLayout());

        dia.setIconImage(FrameProperties.icon());
        dia.add(pane, "Grow,push,span 2 1,wrap");
        dia.add(save2);
        dia.add(close);
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
        model.setColumnIdentifiers(cols);
        tab.setModel(model);

        con = DbConnection.connectDb();
        this.setIconImage(FrameProperties.icon());
        ((JComponent) getContentPane()).setBorder(
                BorderFactory.createMatteBorder(5, 5, 5, 5, Color.MAGENTA));
        save = new FredButton("Save");
        cancel = new FredButton("Close");
        option = new FredCheckBox("Record By admission Number");
        classl = new FredLabel("Class");
        receiptNumber = new FredLabel("Reciept No.");
        search = new FredLabel("admission Number");
        adm = new JTextField();
        votehead = new FredButton("Vote Head Listing");
        amount = new FredLabel("Amount(KSH.)");
        name = new FredLabel("Name");
        jamount = new JTextField();
        jname = new FredCombo("Select Student");
        jReceiptNumber = new FredTextField();
        jclassl = new FredCombo("Select Form");
        pmmode = new FredLabel("Payment Mode");
        slipnumber = new FredLabel("Bank Slip Number/Cheque No.");
        jpmmode = new FredCombo("Select Payment Mode");
        jslipnumber = new JTextField();
        infor = new FredLabel("");
        infor2 = new FredLabel("");
        date = new FredLabel("Date Deposited");
        reciept1 = new FredCheckBox("Instant Receipt Print Out");
        jdate = new FredDateChooser();
        jdate.setDateFormatString("yyyy-MM-dd");
        confirmation = new FredCheckBox("Send SMS Reciept Confirmation");
        option.setBounds(50, 30, 200, 30);
        add(option);
        adm.setBounds(300, 30, 250, 30);
        add(adm);
        infor.setBounds(50, 65, 800, 30);
        add(infor);
        receiptNumber.setBounds(30, 110, 150, 30);
        add(receiptNumber);
        jReceiptNumber.setBounds(250, 110, 200, 30);
        add(jReceiptNumber);
        classl.setBounds(30, 200, 150, 30);
        add(classl);
        jclassl.setBounds(250, 200, 200, 30);
        add(jclassl);
        name.setBounds(30, 300, 200, 30);
        add(name);
        jname.setBounds(250, 300, 200, 30);
        add(jname);
        pmmode.setBounds(550, 30, 200, 30);
        add(pmmode);
        jpmmode.setBounds(700, 30, 200, 30);
        add(jpmmode);
        slipnumber.setBounds(550, 120, 200, 30);
        add(slipnumber);
        jslipnumber.setBounds(700, 120, 200, 30);
        add(jslipnumber);
        date.setBounds(550, 210, 200, 30);
        add(date);
        jdate.setBounds(700, 210, 200, 30);
        add(jdate);
        amount.setBounds(550, 300, 150, 30);
        add(amount);
        jamount.setBounds(700, 300, 200, 30);
        add(jamount);
        votehead.setBounds(750, 280, 130, 20);
        add(votehead);
        reciept1.setSelected(true);
        confirmation.setBounds(200, 370, 230, 30);
        add(confirmation);
        reciept1.setBounds(550, 370, 230, 30);
        add(reciept1);
        infor2.setBounds(720, 330, 300, 30);
        add(infor2);
        cancel.setBounds(200, 450, 130, 30);
        add(cancel);
        save.setBounds(550, 450, 130, 30);
        add(save);

        adm.setEnabled(false);
        setVisible(true);

        if (ConfigurationIntialiser.autoReceiptNumber()) {
            jReceiptNumber.setText(String.valueOf(Globals.nextReceiptNumber()));
        } else {
            jReceiptNumber.setText("");
        }

        infor.setFont(new Font("serif", Font.ITALIC, 18));
        infor2.setFont(new Font("serif", Font.ITALIC, 18));
        jamount.addKeyListener(new KeyAdapter() {

            @Override
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();
                if (Character.isAlphabetic(c) || jamount.getText().length() > 30) {
                    getToolkit().beep();
                    e.consume();
                }

            }

            public void keyReleased(KeyEvent key) {
                String admNumber = adm.getText();
                if (adm.isEnabled()) {
                    admNumber = adm.getText();
                } else {
                    admNumber = Globals.admissionNumberForTheName(jname.getSelectedItem().toString());
                }

                if (!jamount.getText().isEmpty()) {
                    double bal = (Globals.balanceCalculator(admNumber)) - Double.valueOf(jamount.getText());
                    infor2.setText("Balance After: Ksh. " + bal);
                } else {
                    infor2.setText("");
                }
            }
        });
        try {
            ps = con.prepareStatement("Select * from classes  where precision1<5 order by precision1");
            rs = ps.executeQuery();
            while (rs.next()) {
                jclassl.addItem(rs.getString("ClassName"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        jpmmode.addItem("Cash");
        jpmmode.addItem("In Kind");
        jpmmode.addItem("Bank");

        jpmmode.addItem("Banker Cheque");
        jpmmode.addItem("Bursary");

        jpmmode.addItem("Mobile Payment");
        jclassl.addActionListener(this);
        cancel.addActionListener(this);
        save.addActionListener(this);
        option.addActionListener(this);
        votehead.addActionListener(this);
        close.addActionListener(this);
        save2.addActionListener(this);

        adm.addKeyListener(new KeyAdapter() {

            public void keyReleased(KeyEvent key) {
                String admNumber = adm.getText();
                infor.setText("Student Name:" + Globals.fullName(admNumber) + "    Current Balance: Ksh. " + Globals.balanceCalculator(admNumber));
            }
        });
        jpmmode.addActionListener(this);

        if (ConfigurationIntialiser.automaticVoteheadAmountDistribution()) {
            votehead.setVisible(true);
        } else {
            votehead.setVisible(false);
        }

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object obj = e.getSource();
        String pcode = "";
        if (obj == cancel) {
            try {
                con.close();
            } catch (SQLException sq) {

            }
            CurrentFrame.currentWindow();
            dispose();
        } else if (obj == close) {

            dia.dispose();
        } else if (obj == save2) {
            ArrayList messagesList = new ArrayList<Map>();
            HashMap messageData = new HashMap<String, String>();

            if (adm.isEnabled()) {
                ADM = adm.getText();
            } else {
                ADM = Globals.admissionNumberForTheName(jname.getSelectedItem().toString());
            }

            double totalAmount = 0;
            for (int i = tab.getRowCount() - 1; i >= 0; --i) {

                if (DataValidation.number2(model.getValueAt(i, 2).toString())) {

                    totalAmount = Integer.parseInt(model.getValueAt(i, 2).toString()) + totalAmount;
                }

            }
            if (totalAmount > Double.valueOf(jamount.getText())) {
                JOptionPane.showMessageDialog(this, "Vote Head Amount Distribution Exceeds Amount Paid\n Amount Paid= Ksh. " + jamount.getText() + " \n Votehead Distribution Total Ksh. " + totalAmount);
            } else if (totalAmount < Double.valueOf(jamount.getText())) {
                JOptionPane.showMessageDialog(this, "Vote Head Amount Distribution is less Than Amount Paid\n Amount Paid= Ksh. " + jamount.getText() + " \n Votehead Distribution Total Ksh. " + totalAmount);
            } else if (totalAmount == Double.valueOf(jamount.getText())) {

                try {

                    String depositedate = ((JTextField) jdate.getDateEditor().getUiComponent()).getText();
                    if (depositedate.isEmpty() || depositedate == null) {
                        Date date = new Date();
                        SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
                        depositedate = format.format(date);
                    }
                    for (int i = tab.getRowCount() - 1; i >= 0; --i) {

                        if (DataValidation.number2(model.getValueAt(i, 2).toString())) {

                            totalAmount = Double.parseDouble(model.getValueAt(i, 2).toString()) + totalAmount;

                            String voteheadid = Globals.voteHeadId(model.getValueAt(i, 0).toString());
                            double creditamount = Double.parseDouble(model.getValueAt(i, 2).toString());

                            ps = con.prepareStatement("Insert into reciepts values('" + jReceiptNumber.getText() + "','" + voteheadid + "','" + Globals.currentTermName() + "','" + Globals.academicYear() + "','" + IdGenerator.keyGen() + "',now(),'" + creditamount + "','" + ADM + "','" + jpmmode.getSelectedItem() + "','" + depositedate + "','" + jslipnumber.getText() + "')");
                            ps.execute();
                            String itemname = Globals.fullName(ADM) + "," + jReceiptNumber.getText();
                            double cashbookvalue = 0, finalcachbookvalue = 0;
                            String querry0 = "Select max(date),bankbal from cashbookanalysis where  mode='" + "Bank" + "' and accountid='" + "3" + "'";
                            ps = con.prepareStatement(querry0);
                            ResultSet rs1 = ps.executeQuery();
                            while (rs1.next()) {
                                cashbookvalue = rs1.getDouble("bankbal");
                            }

                            finalcachbookvalue = cashbookvalue + creditamount;
                            String querry = "Insert into Cashbookanalysis values('" + itemname + "',Now(),'" + jslipnumber.getText() + "','" + "DEP" + "','" + creditamount + "','" + "0" + "','" + finalcachbookvalue + "','" + jpmmode.getSelectedItem() + "','" + "" + "','" + "3" + "','" + creditamount + "')";
                            ps = con.prepareStatement(querry);
                            ps.execute();

                        }

                    }
                    dia.dispose();

                    // JOptionPane.showMessageDialog(this, "Receipt Recorded Successfully");    
                    AMOUNT = jamount.getText();
                    admissionNumber = adm.getText();
                    if (reciept1.isSelected()) {
                        JOptionPane.showMessageDialog(this, " Payment Details Saved Sucessfully\n Kindly Wait For A Short Moment As The System Prepares The Payment Reciept");
                        Date date = new Date();
                        SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");

                        RecieptGenerator.generateReciept(adm.getText(), jReceiptNumber.getText(), format.format(date), jamount.getText(), jpmmode.getSelectedItem().toString(), depositedate);
                    } else {
                        JOptionPane.showMessageDialog(this, " Payment Details Saved Sucessfully");
                    }

                    adm.setText("");
                    jReceiptNumber.setText("");
                    jamount.setText("");
                    jslipnumber.setText("");
                    jname.setSelectedIndex(0);
                    jclassl.setSelectedIndex(0);

                    if (ConfigurationIntialiser.autoReceiptNumber()) {
                        jReceiptNumber.setText(String.valueOf(Globals.nextReceiptNumber()));
                    } else {
                        jReceiptNumber.setText("");
                    }

                    if (confirmation.isSelected()) {

                        if (ConfigurationIntialiser.smsOfflineSender()) {
                            String ParentNames = "";
                            String querr = "select parentfullnames,telephone1,admissionnumber  from admission where admissionnumber='" + admissionNumber + "'";
                            String phone = "";
                            String mesage = "";
                            ps = con.prepareStatement(querr);
                            rs = ps.executeQuery();
                            ResultSetMetaData meta = rs.getMetaData();

                            if (rs.next()) {
                                boolean testa = false;
                                ParentNames = rs.getString("ParentfullNames");
                                String p = rs.getString("telephone1");
                                if (p.equalsIgnoreCase("")) {

                                } else {

                                    phone = "254" + p.substring(1);

                                    mesage = "Dear Parent/Guardian, Your Fee Payment Of KSH." + AMOUNT + " Has Been Recieved," + "Your New Fee Balance Is KSH." + Globals.balanceCalculator(admissionNumber) + ".00 KAMARANDI SEC. SCH.";
                                    PreparedStatement ps1;

                                    messageData = new HashMap<String, String>();
                                    messageData.put("message", mesage);
                                    messageData.put("phone", phone);
                                    messagesList.add(messageData);

                                    MessageGateway.batchMessageQueuer(messagesList);
                                    JOptionPane.showMessageDialog(this, "Confirmation Reciept Sent Successfully");

                                }

                            }

                        } else {

                        }

                    }

                } catch (Exception sq) {
                    sq.printStackTrace();
                }

            }

        } else if (obj == jpmmode) {
            if (jpmmode.getSelectedIndex() < 3) {
                jslipnumber.setVisible(false);
                jdate.setVisible(false);
            } else {
                jslipnumber.setVisible(true);
                jdate.setVisible(true);
            }
        } else if (obj == option) {
            if (option.isSelected()) {
                jname.setEnabled(false);
                jclassl.setEnabled(false);
                adm.setEnabled(true);
            } else {
                jname.setEnabled(true);
                jclassl.setEnabled(true);
                adm.setEnabled(false);
            }
        } else if (obj == votehead) {
            if (jamount.getText().isEmpty() || jamount.getText().equalsIgnoreCase("0")) {
                JOptionPane.showMessageDialog(this, "Please Input A Valid Amount");
            } else {
                if (jReceiptNumber.getText().isEmpty()) {
                    JOptionPane.showConfirmDialog(this, "Input a valid receipt Number");
                } else {
                    if (jpmmode.getSelectedIndex() == 0) {
                        JOptionPane.showMessageDialog(this, "Select Payment Mode");
                    } else {

                        if (jname.getSelectedIndex() > 0 || Globals.admissionVerifier(adm.getText()) == true) {
                            try {
                                if (adm.isEnabled()) {
                                    ADM = adm.getText();
                                } else {
                                    ADM = Globals.admissionNumberForTheName(jname.getSelectedItem().toString());
                                }
                                while (tab.getRowCount() > 0) {
                                    model.removeRow(0);
                                }
                                ps = con.prepareStatement("Select distinct voteheadid from StudentPayableVoteHeads ");
                                rs = ps.executeQuery();
                                while (rs.next()) {
                                    double bal = 0, b1 = 0, b2 = 0;
                                    String vName = Globals.VoteHeadName(rs.getString("VoteHeadId"));
                                    String Vid = rs.getString("VoteHeadId");
                                    row[0] = vName;

                                    ps = con.prepareStatement("Select sum(amount) from payablevoteheadperstudent where admnumber='" + ADM + "' and voteheadid='" + Vid + "'");
                                    ResultSet rx = ps.executeQuery();
                                    if (rx.next()) {

                                        b1 = rx.getDouble("Sum(Amount)");

                                    }

                                    ps = con.prepareStatement("Select sum(amount) from reciepts where admnumber='" + ADM + "' and voteheadid='" + Vid + "'");
                                    rx = ps.executeQuery();
                                    if (rx.next()) {

                                        b2 = rx.getDouble("Sum(Amount)");

                                    }

                                    bal = b1 - b2;
                                    row[1] = bal;
                                    row[2] = "";
                                    model.addRow(row);
                                }

                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }
                            dia.setTitle("votehead amount Distribution,Input valid Amounts. Total Amount= Ksh. " + jamount.getText());
                            dia.setVisible(true);
                            //  dia.setAlwaysOnTop(true);

                        } else {
                            JOptionPane.showMessageDialog(this, "No student Pointed  to");
                        }

                    }
                }

            }

        } else if (obj == save) {
            double bal = 0, paid = 0;
            boolean proceed = true;
            ArrayList messagesList = new ArrayList<Map>();
            HashMap messageData = new HashMap<String, String>();
            try {
                String pycode = "", classcode = "", streamcode = "";
                String depositedate = ((JTextField) jdate.getDateEditor().getUiComponent()).getText();
                if (depositedate.isEmpty() || depositedate == null) {
                    Date date = new Date();
                    SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
                    depositedate = format.format(date);
                }
                if (adm.isEnabled()) {
                    if (adm.getText().equalsIgnoreCase("")) {
                        getToolkit().beep();
                        JOptionPane.showMessageDialog(this, "Kindly Input Valid admission Number");
                    } else {
                        if (jReceiptNumber.getText().isEmpty()) {
                            getToolkit().beep();
                            JOptionPane.showMessageDialog(this, "Input A Valid Reciept Number");
                        } else {

                            if (jpmmode.getSelectedIndex() == 0) {
                                getToolkit().beep();
                                JOptionPane.showMessageDialog(this, "Select Payment Mode");
                            } else {
                                if (jamount.getText().isEmpty()) {
                                    getToolkit().beep();
                                    JOptionPane.showMessageDialog(this, "Kidly Type A Valid Amount Figure");
                                } else {

                                    double instantpayment = Double.parseDouble(jamount.getText());

                                    String sql1 = "Select * from admission where admissionnumber='" + adm.getText() + "'";
                                    ResultSet RS;
                                    ps = con.prepareStatement(sql1);
                                    RS = ps.executeQuery();
                                    if (RS.next()) {
                                        classcode = RS.getString("Currentform");
                                        streamcode = RS.getString("CurrentStream");

                                    } else {
                                        proceed = false;
                                        getToolkit().beep();
                                        JOptionPane.showMessageDialog(this, "Invalid admission Number,Kindly Check The admission Number");
                                    }
                                    if (proceed) {

                                        String admReciptConflict = Globals.receiptConflictChecker(jReceiptNumber.getText());
                                        if (admReciptConflict.equalsIgnoreCase("")) {
                                            proceed = true;
                                        } else {
                                            JOptionPane.showMessageDialog(this, "This Receipt Number Has Already Been Updated To " + Globals.fullName(admReciptConflict) + "\n Kindly Choose A Diffrent Receipt Number To Avoid System Refference Errors");
                                            proceed = false;
                                        }
                                    }
                                    if (proceed) {

                                        bal = Globals.balanceCalculator(adm.getText());

                                        double finalBalance = bal - Double.valueOf(jamount.getText());

                                        paid += Double.valueOf(jamount.getText());

//                                        int total = 0, totalpaid = 0;
//                                        int individualtotalpaid = 0;
//                                        int individualexpected = 0;
//                                        int counter = 0;
                                        String postingYear = "", postingVotehead = "";
                                        ResultSet rx;

                                        if (jslipnumber.isVisible()) {

                                            if (depositedate.equalsIgnoreCase("")) {
                                                JOptionPane.showMessageDialog(this, "Select The Date Amount Was Deposited");
                                            } else {
                                                int priority = 0;
                                                String voteheadid;
                                                boolean fullyserviced = false;

                                                ps = con.prepareStatement("Select voteheadid,AmountPerHead,priority from  studentpayablevoteheads order by priority");
                                                rs = ps.executeQuery();
                                                while (rs.next()) {

                                                    double voteheadPayable = 0, voteheadpaid = 0, voteheadBalance = 0, finalvoteheadbalance = 0;
                                                    priority = rs.getInt("Priority");
                                                    voteheadid = rs.getString("VoteheadId");
                                                    String year = "";
                                                    double expectedFromPreviousPeriods = 0, currentPaid = 0, previosPeriodsBal = 0;
                                                    ps = con.prepareStatement("Select Sum(amount),academicyear from payablevoteheadperstudent where  admnumber='" + adm.getText() + "' and academicYear!='" + Globals.academicYear() + "'");
                                                    rx = ps.executeQuery();
                                                    if (rx.next()) {
                                                        expectedFromPreviousPeriods = rx.getDouble("Sum(amount)");
                                                        year = rx.getString("AcademicYear");

                                                    }
                                                    ps = con.prepareStatement("Select Sum(Amount) from reciepts where  admnumber='" + adm.getText() + "'");
                                                    rx = ps.executeQuery();
                                                    if (rx.next()) {
                                                        currentPaid = rx.getDouble("Sum(Amount)");
                                                    }
                                                    previosPeriodsBal = (expectedFromPreviousPeriods - currentPaid);
                                                    boolean debtor = false;
                                                    String debtorvoteheadid = "";
                                                    if (previosPeriodsBal > 0) {
                                                        debtor = true;
                                                        ps = con.prepareStatement("Select voteheadid from voteheads where voteheadname='" + "Debtors" + "'");
                                                        rx = ps.executeQuery();
                                                        if (rx.next()) {
                                                            debtorvoteheadid = rx.getString("VoteHeadId");
                                                        }
                                                        postingYear = year;
                                                        postingVotehead = debtorvoteheadid;

                                                    } else {
                                                        postingVotehead = voteheadid;
                                                        debtor = false;
                                                        postingYear = String.valueOf(Globals.academicYear());
                                                    }
                                                    postingYear = String.valueOf(Globals.academicYear());
                                                    ps = con.prepareStatement("Select Sum(amount) from payablevoteheadperstudent where voteheadid='" + voteheadid + "' and admnumber='" + adm.getText() + "'");
                                                    rx = ps.executeQuery();
                                                    if (rx.next()) {
                                                        voteheadPayable = rx.getDouble("Sum(Amount)");
                                                    }
                                                    ps = con.prepareStatement("Select Sum(Amount) from reciepts where voteheadid='" + voteheadid + "' and admnumber='" + adm.getText() + "'");
                                                    rx = ps.executeQuery();
                                                    if (rx.next()) {
                                                        voteheadpaid = rx.getDouble("Sum(Amount)");
                                                    }
                                                    voteheadBalance = (voteheadPayable - voteheadpaid);

                                                    if (voteheadBalance <= 0) {
                                                        fullyserviced = false;
                                                    } else {
                                                        finalvoteheadbalance = voteheadBalance - instantpayment;
                                                        if (finalvoteheadbalance < 0) {

                                                            ps = con.prepareStatement("Insert into reciepts values('" + jReceiptNumber.getText() + "','" + voteheadid + "','" + Globals.currentTermName() + "','" + postingYear + "','" + IdGenerator.keyGen() + "',now(),'" + voteheadBalance + "','" + adm.getText() + "','" + jpmmode.getSelectedItem() + "','" + depositedate + "','" + jslipnumber.getText() + "')");
                                                            ps.execute();

                                                            String itemname = Globals.fullName(adm.getText()) + "," + jReceiptNumber.getText();
                                                            double cashbookvalue = 0, finalcachbookvalue = 0;
                                                            String querry0 = "Select max(date),bankbal from cashbookanalysis where  mode='" + "Bank" + "' and accountid='" + "3" + "'";
                                                            ps = con.prepareStatement(querry0);
                                                            ResultSet rs1 = ps.executeQuery();
                                                            while (rs1.next()) {
                                                                cashbookvalue = rs1.getDouble("bankbal");
                                                            }

                                                            finalcachbookvalue = cashbookvalue + instantpayment;
                                                            String querry = "Insert into Cashbookanalysis values('" + itemname + "',Now(),'" + jslipnumber.getText() + "','" + "DEP" + "','" + voteheadBalance + "','" + "0" + "','" + finalcachbookvalue + "','" + jpmmode.getSelectedItem() + "','" + "" + "','" + "3" + "','" + postingVotehead + "')";
                                                            ps = con.prepareStatement(querry);
                                                            ps.execute();
                                                            instantpayment = instantpayment - voteheadBalance;
                                                            if (instantpayment <= 0) {
                                                                fullyserviced = true;
                                                                break;
                                                            }

                                                        } else {
                                                            ps = con.prepareStatement("Insert into reciepts values('" + jReceiptNumber.getText() + "','" + voteheadid + "','" + Globals.currentTermName() + "','" + postingYear + "','" + IdGenerator.keyGen() + "',now(),'" + instantpayment + "','" + adm.getText() + "','" + jpmmode.getSelectedItem() + "','" + depositedate + "','" + jslipnumber.getText() + "')");
                                                            ps.execute();
                                                            String itemname = Globals.fullName(adm.getText()) + "," + jReceiptNumber.getText();
                                                            double cashbookvalue = 0, finalcachbookvalue = 0;
                                                            String querry0 = "Select max(date),bankbal from cashbookanalysis where  mode='" + "Bank" + "' and accountid='" + "3" + "'";
                                                            ps = con.prepareStatement(querry0);
                                                            ResultSet rs1 = ps.executeQuery();
                                                            while (rs1.next()) {
                                                                cashbookvalue = rs1.getDouble("bankbal");
                                                            }

                                                            con = DbConnection.connectDb();

                                                            finalcachbookvalue = cashbookvalue + instantpayment;
                                                            String querry = "Insert into Cashbookanalysis values('" + itemname + "',Now(),'" + jslipnumber.getText() + "','" + "DEP" + "','" + instantpayment + "','" + "0" + "','" + finalcachbookvalue + "','" + jpmmode.getSelectedItem() + "','" + "" + "','" + "3" + "','" + postingVotehead + "')";
                                                            ps = con.prepareStatement(querry);
                                                            ps.execute();

                                                            fullyserviced = true;
                                                            break;
                                                        }

                                                    }

                                                }
                                                if (fullyserviced == false) {

                                                    double overpayment = instantpayment;
                                                    String maxpaidvoteheadid = "";
                                                    double maxamount = 0;
                                                    ps = con.prepareStatement("Select voteheadid,max(amount) from payablevoteheadperstudent where term='" + Globals.currentTermName() + "' and academicyear='" + Globals.academicYear() + "' and admnumber='" + adm.getText() + "'");
                                                    rs = ps.executeQuery();
                                                    if (rs.next()) {
                                                        maxamount = rs.getDouble("Max(amount)");
                                                        maxpaidvoteheadid = rs.getString("Voteheadid");

                                                    }
                                                    ps = con.prepareStatement("Insert into reciepts values('" + jReceiptNumber.getText() + "','" + maxpaidvoteheadid + "','" + Globals.currentTermName() + "','" + Globals.academicYear() + "','" + IdGenerator.keyGen() + "',now(),'" + overpayment + "','" + adm.getText() + "','" + jpmmode.getSelectedItem() + "','" + depositedate + "','" + jslipnumber.getText() + "')");
                                                    ps.execute();
                                                    String itemname = Globals.fullName(adm.getText()) + "," + jReceiptNumber.getText();
                                                    double cashbookvalue = 0, finalcachbookvalue = 0;
                                                    String querry0 = "Select max(date),bankbal from cashbookanalysis where  mode='" + "Bank" + "' and accountid='" + "3" + "'";
                                                    ps = con.prepareStatement(querry0);
                                                    ResultSet rs1 = ps.executeQuery();
                                                    while (rs1.next()) {
                                                        cashbookvalue = rs1.getDouble("bankbal");
                                                    }

                                                    finalcachbookvalue = cashbookvalue + overpayment;
                                                    String querry = "Insert into Cashbookanalysis values('" + itemname + "',Now(),'" + jslipnumber.getText() + "','" + "DEP" + "','" + overpayment + "','" + "0" + "','" + finalcachbookvalue + "','" + jpmmode.getSelectedItem() + "','" + "" + "','" + "3" + "','" + maxpaidvoteheadid + "')";
                                                    ps = con.prepareStatement(querry);
                                                    ps.execute();

                                                }

                                                AMOUNT = jamount.getText();
                                                admissionNumber = adm.getText();
                                                if (reciept1.isSelected()) {
                                                    JOptionPane.showMessageDialog(this, " Payment Details Saved Sucessfully\n Kindly Wait For A Short Moment As The System Prepares The Payment Reciept");
                                                    Date date = new Date();
                                                    SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");

                                                    RecieptGenerator.generateReciept(adm.getText(), jReceiptNumber.getText(), format.format(date), jamount.getText(), jpmmode.getSelectedItem().toString(), depositedate);
                                                } else {
                                                    JOptionPane.showMessageDialog(this, " Payment Details Saved Sucessfully");
                                                }

                                                adm.setText("");
                                                jReceiptNumber.setText("");
                                                jamount.setText("");
                                                jslipnumber.setText("");
                                                jname.setSelectedIndex(0);
                                                jclassl.setSelectedIndex(0);
                                                if (ConfigurationIntialiser.autoReceiptNumber()) {
                                                    jReceiptNumber.setText(String.valueOf(Globals.nextReceiptNumber()));
                                                } else {
                                                    jReceiptNumber.setText("");
                                                }

                                            }

                                            if (confirmation.isSelected()) {

                                                if (ConfigurationIntialiser.smsOfflineSender()) {
                                                    String ParentNames = "";
                                                    String querr = "select parentfullnames,telephone1,admissionnumber  from admission where admissionnumber='" + adm.getText() + "'";
                                                    String phone = "";
                                                    String mesage = "";
                                                    ps = con.prepareStatement(querr);
                                                    rs = ps.executeQuery();
                                                    ResultSetMetaData meta = rs.getMetaData();

                                                    if (rs.next()) {
                                                        boolean testa = false;
                                                        ParentNames = rs.getString("ParentfullNames");
                                                        String p = rs.getString("telephone1");
                                                        if (p.equalsIgnoreCase("")) {

                                                        } else {

                                                            phone = "254" + p.substring(1);

                                                            mesage = "Dear Parent/Gurdian, Your Fee Payment Of KSH." + AMOUNT + " Has Been Recieved," + "Your New Fee Balance Is KSH." + Globals.balanceCalculator(admissionNumber) + ".00 KAMARANDI SEC. SCH.";
                                                            messageData = new HashMap<String, String>();
                                                            messageData = new HashMap<String, String>();
                                                            messageData.put("message", mesage);
                                                            messageData.put("phone", phone);
                                                            messagesList.add(messageData);

                                                            MessageGateway.batchMessageQueuer(messagesList);

                                                            JOptionPane.showMessageDialog(this, "Confirmation Reciept Sent Successfully");

                                                        }

                                                    }

                                                } else {

                                                }

                                            }
                                        } else {

                                            int priority = 0;
                                            String voteheadid;
                                            boolean fullyserviced = false;
                                            ps = con.prepareStatement("Select voteheadid,AmountPerHead,priority from  studentpayablevoteheads order by priority");
                                            rs = ps.executeQuery();
                                            while (rs.next()) {
                                                double voteheadPayable = 0, voteheadpaid = 0, voteheadBalance = 0, finalvoteheadbalance = 0;
                                                priority = rs.getInt("Priority");
                                                voteheadid = rs.getString("VoteheadId");

                                                String year = "";
                                                double expectedFromPreviousPeriods = 0, currentPaid = 0, previosPeriodsBal = 0;
                                                ps = con.prepareStatement("Select Sum(amount),academicyear from payablevoteheadperstudent where  admnumber='" + adm.getText() + "' and academicYear!='" + Globals.academicYear() + "'");
                                                rx = ps.executeQuery();
                                                if (rx.next()) {
                                                    expectedFromPreviousPeriods = rx.getDouble("Sum(amount)");
                                                    year = rx.getString("AcademicYear");

                                                }
                                                ps = con.prepareStatement("Select Sum(Amount) from reciepts where  admnumber='" + adm.getText() + "'");
                                                rx = ps.executeQuery();
                                                if (rx.next()) {
                                                    currentPaid = rx.getDouble("Sum(Amount)");
                                                }
                                                previosPeriodsBal = (expectedFromPreviousPeriods - currentPaid);
                                                boolean debtor = false;
                                                String debtorvoteheadid = "";
                                                if (previosPeriodsBal >= 0) {
                                                    debtor = true;
                                                    ps = con.prepareStatement("Select voteheadid from voteheads where voteheadname='" + "Debtors" + "'");
                                                    rx = ps.executeQuery();
                                                    if (rx.next()) {
                                                        debtorvoteheadid = rx.getString("VoteHeadId");
                                                    }
                                                    postingYear = year;
                                                    postingVotehead = debtorvoteheadid;

                                                } else {
                                                    postingVotehead = voteheadid;
                                                    debtor = false;
                                                    postingYear = String.valueOf(Globals.academicYear());
                                                }
                                                postingYear = String.valueOf(Globals.academicYear());
                                                ps = con.prepareStatement("Select Sum(amount) from payablevoteheadperstudent where voteheadid='" + voteheadid + "' and admnumber='" + adm.getText() + "'");
                                                rx = ps.executeQuery();
                                                if (rx.next()) {
                                                    voteheadPayable = rx.getDouble("Sum(Amount)");
                                                }
                                                ps = con.prepareStatement("Select Sum(Amount) from reciepts where voteheadid='" + voteheadid + "' and admnumber='" + adm.getText() + "'");
                                                rx = ps.executeQuery();
                                                if (rx.next()) {
                                                    voteheadpaid = rx.getDouble("Sum(Amount)");
                                                }
                                                voteheadBalance = (voteheadPayable - voteheadpaid);

                                                if (voteheadBalance <= 0) {

                                                } else {
                                                    finalvoteheadbalance = voteheadBalance - instantpayment;
                                                    if (finalvoteheadbalance < 0) {

                                                        ps = con.prepareStatement("Insert into reciepts values('" + jReceiptNumber.getText() + "','" + voteheadid + "','" + Globals.currentTermName() + "','" + postingYear + "','" + IdGenerator.keyGen() + "',now(),'" + voteheadBalance + "','" + adm.getText() + "','" + jpmmode.getSelectedItem() + "',CurDate(),'" + "" + "')");
                                                        ps.execute();
                                                        String itemname = Globals.fullName(adm.getText()) + "," + jReceiptNumber.getText();
                                                        double cashbookvalue = 0, finalcachbookvalue = 0;
                                                        String querry0 = "Select max(date),cashbal from cashbookanalysis where  mode='" + "cash" + "' and accountid='" + "3" + "'";
                                                        ps = con.prepareStatement(querry0);
                                                        ResultSet rs1 = ps.executeQuery();
                                                        while (rs1.next()) {
                                                            cashbookvalue = rs1.getDouble("cashbal");
                                                        }
                                                        con = DbConnection.connectDb();

                                                        finalcachbookvalue = cashbookvalue + instantpayment;
                                                        String querry = "Insert into Cashbookanalysis values('" + itemname + "',Now(),'" + jReceiptNumber.getText() + "','" + "DEP" + "','" + voteheadBalance + "','" + finalcachbookvalue + "','" + "0" + "','" + jpmmode.getSelectedItem() + "','" + "" + "','" + "3" + "','" + postingVotehead + "')";
                                                        ps = con.prepareStatement(querry);
                                                        ps.execute();
                                                        instantpayment = instantpayment - voteheadBalance;
                                                        if (instantpayment <= 0) {
                                                            fullyserviced = true;
                                                            break;
                                                        }

                                                    } else {
                                                        ps = con.prepareStatement("Insert into reciepts values('" + jReceiptNumber.getText() + "','" + voteheadid + "','" + Globals.currentTermName() + "','" + postingYear + "','" + IdGenerator.keyGen() + "',now(),'" + instantpayment + "','" + adm.getText() + "','" + jpmmode.getSelectedItem() + "',CurDate(),'" + "" + "')");
                                                        ps.execute();
                                                        String itemname = adm.getText() + "," + jReceiptNumber.getText();
                                                        double cashbookvalue = 0, finalcachbookvalue = 0;
                                                        String querry0 = "Select max(date),cashbal from cashbookanalysis where  mode='" + "cash" + "' and accountid='" + "3" + "'";
                                                        ps = con.prepareStatement(querry0);
                                                        ResultSet rs1 = ps.executeQuery();
                                                        while (rs1.next()) {
                                                            cashbookvalue = rs1.getDouble("cashbal");
                                                        }
                                                        con = DbConnection.connectDb();

                                                        finalcachbookvalue = cashbookvalue + instantpayment;
                                                        String querry = "Insert into Cashbookanalysis values('" + itemname + "',Now(),'" + jReceiptNumber.getText() + "','" + "DEP" + "','" + instantpayment + "','" + finalcachbookvalue + "','" + "0" + "','" + jpmmode.getSelectedItem() + "','" + "" + "','" + "3" + "','" + postingVotehead + "')";
                                                        ps = con.prepareStatement(querry);
                                                        ps.execute();
                                                        fullyserviced = true;
                                                        break;
                                                    }

                                                }

                                            }
                                            if (fullyserviced == false) {

                                                double overpayment = instantpayment;
                                                String maxpaidvoteheadid = "";
                                                double maxamount = 0;
                                                ps = con.prepareStatement("Select voteheadid,max(amount) from payablevoteheadperstudent where term='" + Globals.currentTermName() + "' and academicyear='" + Globals.academicYear() + "' and admnumber='" + adm.getText() + "'");
                                                rs = ps.executeQuery();
                                                if (rs.next()) {
                                                    maxamount = rs.getDouble("Max(amount)");
                                                    maxpaidvoteheadid = rs.getString("Voteheadid");

                                                }
                                                ps = con.prepareStatement("Insert into reciepts values('" + jReceiptNumber.getText() + "','" + maxpaidvoteheadid + "','" + Globals.currentTermName() + "','" + Globals.academicYear() + "','" + IdGenerator.keyGen() + "',now(),'" + overpayment + "','" + adm.getText() + "','" + jpmmode.getSelectedItem() + "',curDate(),'" + "" + "')");
                                                ps.execute();
                                                String itemname = adm.getText() + "," + jReceiptNumber.getText();
                                                double cashbookvalue = 0, finalcachbookvalue = 0;
                                                String querry0 = "Select max(date),cashbal from cashbookanalysis where  mode='" + "cash" + "' and accountid='" + "3" + "'";
                                                ps = con.prepareStatement(querry0);
                                                ResultSet rs1 = ps.executeQuery();
                                                while (rs1.next()) {
                                                    cashbookvalue = rs1.getDouble("cashbal");
                                                }
                                                con = DbConnection.connectDb();

                                                finalcachbookvalue = cashbookvalue + overpayment;
                                                String querry = "Insert into Cashbookanalysis values('" + itemname + "',Now(),'" + jReceiptNumber.getText() + "','" + "DEP" + "','" + overpayment + "','" + finalcachbookvalue + "','" + "0" + "','" + jpmmode.getSelectedItem() + "','" + "" + "','" + "3" + "','" + maxpaidvoteheadid + "')";
                                                ps = con.prepareStatement(querry);
                                                ps.execute();

                                            }

                                            AMOUNT = jamount.getText();
                                            admissionNumber = adm.getText();

                                            if (reciept1.isSelected()) {
                                                JOptionPane.showMessageDialog(this, " Payment Details Saved Sucessfully\n Kindly Wait For A Short Moment As The System Prepares The Payment Reciept");
                                                Date date = new Date();
                                                SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");

                                                RecieptGenerator.generateReciept(adm.getText(), jReceiptNumber.getText(), format.format(date), jamount.getText(), jpmmode.getSelectedItem().toString(), depositedate);

                                            } else {
                                                JOptionPane.showMessageDialog(this, " Payment Details Saved Sucessfully");
                                            }

                                            adm.setText("");
                                            jReceiptNumber.setText("");
                                            jamount.setText("");
                                            jslipnumber.setText("");
                                            jname.setSelectedIndex(0);
                                            jclassl.setSelectedIndex(0);
                                            if (ConfigurationIntialiser.autoReceiptNumber()) {
                                                jReceiptNumber.setText(String.valueOf(Globals.nextReceiptNumber()));
                                            } else {
                                                jReceiptNumber.setText("");
                                            }

                                            if (confirmation.isSelected()) {

                                                if (ConfigurationIntialiser.smsOfflineSender()) {
                                                    String ParentNames = "";
                                                    String querr = "select parentfullnames,telephone1,admissionnumber  from admission where admissionnumber='" + admissionNumber + "'";
                                                    String phone = "";
                                                    String mesage = "";
                                                    ps = con.prepareStatement(querr);
                                                    rs = ps.executeQuery();
                                                    ResultSetMetaData meta = rs.getMetaData();

                                                    if (rs.next()) {
                                                        boolean testa = false;
                                                        ParentNames = rs.getString("ParentfullNames");
                                                        String p = rs.getString("telephone1");
                                                        if (p.equalsIgnoreCase("")) {

                                                        } else {

                                                            phone = "254" + p.substring(1);

                                                            mesage = "Dear Parent/Guardian, Your Fee Payment Of KSH." + AMOUNT + " Has Been Recieved," + "Your New Fee Balance Is KSH." + Globals.balanceCalculator(admissionNumber) + ".00 KAMARANDI SEC. SCH.";
                                                            messageData = new HashMap<String, String>();
                                                            messageData.put("message", mesage);
                                                            messageData.put("phone", phone);
                                                            messagesList.add(messageData);

                                                            MessageGateway.batchMessageQueuer(messagesList);
                                                            JOptionPane.showMessageDialog(this, "Confirmation Reciept Sent Successfully");

                                                        }

                                                    }

                                                } else {

                                                }

                                            }

                                        }

                                    }

                                }

                            }

                        }
                    }
                } else {
                    if (jReceiptNumber.getText().isEmpty()) {
                        getToolkit().beep();
                        JOptionPane.showMessageDialog(this, "Input A Valid Reciept Number");
                    } else {

                        if (jclassl.getSelectedIndex() == 0) {
                            getToolkit().beep();
                            JOptionPane.showMessageDialog(this, "Select Class");
                        } else {
                            if (jname.getSelectedIndex() == 0) {
                                getToolkit().beep();
                                JOptionPane.showMessageDialog(this, "Select The Student To Record Againist");
                            } else {
                                if (jpmmode.getSelectedIndex() == 0) {
                                    getToolkit().beep();
                                    JOptionPane.showMessageDialog(this, "Select Payment Mode");
                                } else {
                                    if (jamount.getText().isEmpty()) {
                                        getToolkit().beep();
                                        JOptionPane.showMessageDialog(this, "Kidly Type A Valid Amount Figure");
                                    } else {

                                        String admReciptConflict = Globals.receiptConflictChecker(jReceiptNumber.getText());
                                        if (admReciptConflict.equalsIgnoreCase("")) {

                                            double instantpayment = Double.valueOf(jamount.getText());

                                            classcode = Globals.classCode(jclassl.getSelectedItem().toString());

                                            int namematch = 0;
                                            boolean nametie = false;
                                            String admno = "";

                                            String querry41 = "Select firstname,middlename,lastname,admissionNumber from admission ";
                                            ps = con.prepareStatement(querry41);
                                            ResultSet Rs = ps.executeQuery();
                                            while (Rs.next()) {
                                                if ((Rs.getString("FirstName") + "       " + Rs.getString("Middlename") + "     " + Rs.getString("Lastname")).equalsIgnoreCase(jname.getSelectedItem().toString())) {
                                                    admno = Rs.getString("admissionNumber");
                                                    namematch++;
                                                    if (namematch > 1) {
                                                        break;
                                                    }
                                                }
                                            }

                                            if (namematch > 1) {
                                                JOptionPane.showMessageDialog(this, "System Could Not Resolve Name Issue On The Selected Student\n Use admission Number To Update His/Her Balance\n Possible Tie On All Registered Name On Selected Student");
                                            } else {
                                                String querry4 = "Select firstname,middlename,lastname,admissionNumber from admission ";
                                                ps = con.prepareStatement(querry4);
                                                Rs = ps.executeQuery();
                                                while (Rs.next()) {
                                                    if ((Rs.getString("FirstName") + "       " + Rs.getString("Middlename") + "     " + Rs.getString("Lastname")).equalsIgnoreCase(jname.getSelectedItem().toString())) {
                                                        admno = Rs.getString("admissionNumber");
                                                        break;
                                                    }
                                                }
                                                String postingYear = "", postingVotehead = "";
                                                ResultSet rx;

                                                if (jslipnumber.isVisible()) {

                                                    if (depositedate.equalsIgnoreCase("")) {
                                                        JOptionPane.showMessageDialog(this, "Select The Date Amount Was Deposited");
                                                    } else {
                                                        int priority = 0;
                                                        String voteheadid;
                                                        boolean fullyserviced = false;
                                                        ps = con.prepareStatement("Select voteheadid,AmountPerHead,priority from  studentpayablevoteheads order by priority");
                                                        rs = ps.executeQuery();
                                                        while (rs.next()) {
                                                            double voteheadPayable = 0, voteheadpaid = 0, voteheadBalance = 0, finalvoteheadbalance = 0;
                                                            priority = rs.getInt("Priority");
                                                            voteheadid = rs.getString("VoteheadId");

                                                            String year = "";
                                                            double expectedFromPreviousPeriods = 0, currentPaid = 0, previosPeriodsBal = 0;
                                                            ps = con.prepareStatement("Select Sum(amount),academicyear from payablevoteheadperstudent where  admnumber='" + admno + "' and academicYear!='" + Globals.academicYear() + "'");
                                                            rx = ps.executeQuery();
                                                            if (rx.next()) {
                                                                expectedFromPreviousPeriods = rx.getDouble("Sum(amount)");
                                                                year = rx.getString("AcademicYear");

                                                            }
                                                            ps = con.prepareStatement("Select Sum(Amount) from reciepts where  admnumber='" + admno + "'");
                                                            rx = ps.executeQuery();
                                                            if (rx.next()) {
                                                                currentPaid = rx.getDouble("Sum(Amount)");
                                                            }
                                                            previosPeriodsBal = (expectedFromPreviousPeriods - currentPaid);
                                                            boolean debtor = false;
                                                            String debtorvoteheadid = "";
                                                            if (previosPeriodsBal >= 0) {
                                                                debtor = true;
                                                                ps = con.prepareStatement("Select voteheadid from voteheads where voteheadname='" + "Debtors" + "'");
                                                                rx = ps.executeQuery();
                                                                if (rx.next()) {
                                                                    debtorvoteheadid = rx.getString("VoteHeadId");
                                                                }
                                                                postingYear = year;
                                                                postingVotehead = debtorvoteheadid;

                                                            } else {
                                                                postingVotehead = voteheadid;
                                                                debtor = false;
                                                                postingYear = String.valueOf(Globals.academicYear());
                                                            }
                                                            postingYear = String.valueOf(Globals.academicYear());
                                                            ps = con.prepareStatement("Select Sum(amount) from payablevoteheadperstudent where voteheadid='" + voteheadid + "' and admnumber='" + admno + "'");
                                                            rx = ps.executeQuery();
                                                            if (rx.next()) {
                                                                voteheadPayable = rx.getDouble("Sum(Amount)");
                                                            }
                                                            ps = con.prepareStatement("Select Sum(Amount) from reciepts where voteheadid='" + voteheadid + "' and admnumber='" + admno + "'");
                                                            rx = ps.executeQuery();
                                                            if (rx.next()) {
                                                                voteheadpaid = rx.getDouble("Sum(Amount)");
                                                            }
                                                            voteheadBalance = (voteheadPayable - voteheadpaid);

                                                            if (voteheadBalance <= 0) {

                                                            } else {
                                                                finalvoteheadbalance = voteheadBalance - instantpayment;
                                                                if (finalvoteheadbalance < 0) {

                                                                    ps = con.prepareStatement("Insert into reciepts values('" + jReceiptNumber.getText() + "','" + voteheadid + "','" + Globals.currentTermName() + "','" + postingYear + "','" + IdGenerator.keyGen() + "',now(),'" + voteheadBalance + "','" + admno + "','" + jpmmode.getSelectedItem() + "','" + depositedate + "','" + jslipnumber.getText() + "')");
                                                                    ps.execute();
                                                                    String itemname = Globals.fullName(admno) + "," + jReceiptNumber.getText();
                                                                    double cashbookvalue = 0, finalcachbookvalue = 0;
                                                                    String querry0 = "Select max(date),bankbal from cashbookanalysis where  mode='" + "Bank" + "' and accountid='" + "3" + "'";
                                                                    ps = con.prepareStatement(querry0);
                                                                    ResultSet rs1 = ps.executeQuery();
                                                                    while (rs1.next()) {
                                                                        cashbookvalue = rs1.getDouble("bankbal");
                                                                    }

                                                                    finalcachbookvalue = cashbookvalue + instantpayment;
                                                                    String querry = "Insert into Cashbookanalysis values('" + itemname + "',Now(),'" + jslipnumber.getText() + "','" + "DEP" + "','" + voteheadBalance + "','" + "0" + "','" + finalcachbookvalue + "','" + jpmmode.getSelectedItem() + "','" + "" + "','" + "3" + "','" + postingVotehead + "')";
                                                                    ps = con.prepareStatement(querry);
                                                                    ps.execute();

                                                                    instantpayment = instantpayment - voteheadBalance;

                                                                    if (instantpayment <= 0) {
                                                                        fullyserviced = true;
                                                                        break;
                                                                    }

                                                                } else {
                                                                    ps = con.prepareStatement("Insert into reciepts values('" + jReceiptNumber.getText() + "','" + voteheadid + "','" + Globals.currentTermName() + "','" + postingYear + "','" + IdGenerator.keyGen() + "',now(),'" + instantpayment + "','" + admno + "','" + jpmmode.getSelectedItem() + "','" + depositedate + "','" + jslipnumber.getText() + "')");
                                                                    ps.execute();
                                                                    String itemname = Globals.fullName(admno) + "," + jReceiptNumber.getText();
                                                                    double cashbookvalue = 0, finalcachbookvalue = 0;
                                                                    String querry0 = "Select max(date),bankbal from cashbookanalysis where  mode='" + "Bank" + "' and accountid='" + "3" + "'";
                                                                    ps = con.prepareStatement(querry0);
                                                                    ResultSet rs1 = ps.executeQuery();
                                                                    while (rs1.next()) {
                                                                        cashbookvalue = rs1.getDouble("bankbal");
                                                                    }

                                                                    finalcachbookvalue = cashbookvalue + instantpayment;
                                                                    String querry = "Insert into Cashbookanalysis values('" + itemname + "',Now(),'" + jslipnumber.getText() + "','" + "DEP" + "','" + instantpayment + "','" + "0" + "','" + finalcachbookvalue + "','" + jpmmode.getSelectedItem() + "','" + "" + "','" + "3" + "','" + postingVotehead + "')";
                                                                    ps = con.prepareStatement(querry);
                                                                    ps.execute();
                                                                    fullyserviced = true;
                                                                    break;
                                                                }

                                                            }

                                                        }
                                                        if (fullyserviced == false) {

                                                            double overpayment = instantpayment;
                                                            String maxpaidvoteheadid = "";
                                                            double maxamount = 0;
                                                            ps = con.prepareStatement("Select voteheadid,max(amount) from payablevoteheadperstudent where term='" + Globals.currentTermName() + "' and academicyear='" + Globals.academicYear() + "' and admnumber='" + admno + "'");
                                                            rs = ps.executeQuery();
                                                            if (rs.next()) {
                                                                maxamount = rs.getDouble("Max(amount)");
                                                                maxpaidvoteheadid = rs.getString("Voteheadid");

                                                            }
                                                            ps = con.prepareStatement("Insert into reciepts values('" + jReceiptNumber.getText() + "','" + maxpaidvoteheadid + "','" + Globals.currentTermName() + "','" + Globals.academicYear() + "','" + IdGenerator.keyGen() + "',now(),'" + overpayment + "','" + admno + "','" + jpmmode.getSelectedItem() + "','" + depositedate + "','" + jslipnumber.getText() + "')");
                                                            ps.execute();
                                                            String itemname = Globals.fullName(admno) + "," + jReceiptNumber.getText();
                                                            double cashbookvalue = 0, finalcachbookvalue = 0;
                                                            String querry0 = "Select max(date),bankbal from cashbookanalysis where  mode='" + "Bank" + "' and accountid='" + "3" + "'";
                                                            ps = con.prepareStatement(querry0);
                                                            ResultSet rs1 = ps.executeQuery();
                                                            while (rs1.next()) {
                                                                cashbookvalue = rs1.getDouble("bankbal");
                                                            }

                                                            con = DbConnection.connectDb();

                                                            finalcachbookvalue = cashbookvalue + overpayment;
                                                            String querry = "Insert into Cashbookanalysis values('" + itemname + "',Now(),'" + jslipnumber.getText() + "','" + "DEP" + "','" + overpayment + "','" + "0" + "','" + finalcachbookvalue + "','" + jpmmode.getSelectedItem() + "','" + "" + "','" + "3" + "','" + maxpaidvoteheadid + "')";
                                                            ps = con.prepareStatement(querry);
                                                            ps.execute();

                                                        }

                                                        AMOUNT = jamount.getText();
                                                        admissionNumber = admno;
                                                        if (reciept1.isSelected()) {
                                                            JOptionPane.showMessageDialog(this, " Payment Details Saved Sucessfully\n Kindly Wait For A Short Moment As The System Prepares The Payment Reciept");
                                                            Date date = new Date();
                                                            SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");

                                                            RecieptGenerator.generateReciept(admno, jReceiptNumber.getText(), format.format(date), jamount.getText(), jpmmode.getSelectedItem().toString(), depositedate);
                                                        } else {
                                                            JOptionPane.showMessageDialog(this, " Payment Details Saved Sucessfully");
                                                        }

                                                        adm.setText("");
                                                        jReceiptNumber.setText("");
                                                        jamount.setText("");
                                                        jslipnumber.setText("");
                                                        jname.setSelectedIndex(0);
                                                        jclassl.setSelectedIndex(0);
                                                        if (ConfigurationIntialiser.autoReceiptNumber()) {
                                                            jReceiptNumber.setText(String.valueOf(Globals.nextReceiptNumber()));
                                                        } else {
                                                            jReceiptNumber.setText("");
                                                        }

                                                    }

                                                    if (confirmation.isSelected()) {

                                                        if (ConfigurationIntialiser.smsOfflineSender()) {
                                                            String ParentNames = "";
                                                            String querr = "select parentfullnames,telephone1,admissionnumber  from admission where admissionnumber='" + admno + "'";
                                                            String phone = "";
                                                            String mesage = "";
                                                            ps = con.prepareStatement(querr);
                                                            rs = ps.executeQuery();
                                                            ResultSetMetaData meta = rs.getMetaData();

                                                            if (rs.next()) {
                                                                boolean testa = false;
                                                                ParentNames = rs.getString("ParentfullNames");
                                                                String p = rs.getString("telephone1");
                                                                if (p.equalsIgnoreCase("")) {

                                                                } else {

                                                                    phone = "254" + p.substring(1);


                                                                    mesage = "Dear Parent/Gurdian, Your Fee Payment Of KSH." + jamount.getText() + " Has Been Recieved," + "Your New Fee Balance Is KSH." + Globals.balanceCalculator(admno) + ".00 KAMARANDI SEC. SCH.";
                                                                    messageData = new HashMap<String, String>();
                                                                    messageData.put("message", mesage);
                                                                    messageData.put("phone", phone);
                                                                    messagesList.add(messageData);

                                                                    MessageGateway.batchMessageQueuer(messagesList);
                                                                    JOptionPane.showMessageDialog(this, "Confirmation Reciept Sent Successfully");

                                                                }

                                                            }

                                                        } else {

                                                        }

                                                    }
                                                } else {

                                                    int priority = 0;
                                                    String voteheadid;
                                                    boolean fullyserviced = false;
                                                    ps = con.prepareStatement("Select voteheadid,AmountPerHead,priority from  studentpayablevoteheads order by priority");
                                                    rs = ps.executeQuery();
                                                    while (rs.next()) {
                                                        double voteheadPayable = 0, voteheadpaid = 0, voteheadBalance = 0, finalvoteheadbalance = 0;
                                                        priority = rs.getInt("Priority");
                                                        voteheadid = rs.getString("VoteheadId");

                                                        String year = "";
                                                        double expectedFromPreviousPeriods = 0, currentPaid = 0, previosPeriodsBal = 0;
                                                        ps = con.prepareStatement("Select Sum(amount),academicyear from payablevoteheadperstudent where  admnumber='" + admno + "' and academicYear!='" + Globals.academicYear() + "'");
                                                        rx = ps.executeQuery();
                                                        if (rx.next()) {
                                                            expectedFromPreviousPeriods = rx.getDouble("Sum(amount)");
                                                            year = rx.getString("AcademicYear");

                                                        }
                                                        ps = con.prepareStatement("Select Sum(Amount) from reciepts where  admnumber='" + admno + "'");
                                                        rx = ps.executeQuery();
                                                        if (rx.next()) {
                                                            currentPaid = rx.getDouble("Sum(Amount)");
                                                        }
                                                        previosPeriodsBal = (expectedFromPreviousPeriods - currentPaid);
                                                        boolean debtor = false;
                                                        String debtorvoteheadid = "";
                                                        if (previosPeriodsBal >= 0) {
                                                            debtor = true;
                                                            ps = con.prepareStatement("Select voteheadid from voteheads where voteheadname='" + "Debtors" + "'");
                                                            rx = ps.executeQuery();
                                                            if (rx.next()) {
                                                                debtorvoteheadid = rx.getString("VoteHeadId");
                                                            }
                                                            postingYear = year;
                                                            postingVotehead = debtorvoteheadid;

                                                        } else {
                                                            postingVotehead = voteheadid;
                                                            debtor = false;
                                                            postingYear = String.valueOf(Globals.academicYear());
                                                        }
                                                        postingYear = String.valueOf(Globals.academicYear());
                                                        ps = con.prepareStatement("Select Sum(amount) from payablevoteheadperstudent where voteheadid='" + voteheadid + "' and admnumber='" + admno + "'");
                                                        rx = ps.executeQuery();
                                                        if (rx.next()) {
                                                            voteheadPayable = rx.getDouble("Sum(Amount)");
                                                        }
                                                        ps = con.prepareStatement("Select Sum(Amount) from reciepts where voteheadid='" + voteheadid + "' and admnumber='" + admno + "'");
                                                        rx = ps.executeQuery();
                                                        if (rx.next()) {
                                                            voteheadpaid = rx.getDouble("Sum(Amount)");
                                                        }
                                                        voteheadBalance = (voteheadPayable - voteheadpaid);

                                                        if (voteheadBalance <= 0) {

                                                        } else {
                                                            finalvoteheadbalance = voteheadBalance - instantpayment;
                                                            if (finalvoteheadbalance < 0) {

                                                                ps = con.prepareStatement("Insert into reciepts values('" + jReceiptNumber.getText() + "','" + voteheadid + "','" + Globals.currentTermName() + "','" + postingYear + "','" + IdGenerator.keyGen() + "',now(),'" + voteheadBalance + "','" + admno + "','" + jpmmode.getSelectedItem() + "',CurDate(),'" + "" + "')");
                                                                ps.execute();

                                                                String itemname = admno + "," + jReceiptNumber.getText();
                                                                double cashbookvalue = 0, finalcachbookvalue = 0;
                                                                String querry0 = "Select max(date),cashbal from cashbookanalysis where  mode='" + "cash" + "' and accountid='" + "3" + "'";
                                                                ps = con.prepareStatement(querry0);
                                                                ResultSet rs1 = ps.executeQuery();
                                                                while (rs1.next()) {
                                                                    cashbookvalue = rs1.getDouble("cashbal");
                                                                }
                                                                con = DbConnection.connectDb();

                                                                finalcachbookvalue = cashbookvalue + instantpayment;
                                                                String querry = "Insert into Cashbookanalysis values('" + itemname + "',Now(),'" + jReceiptNumber.getText() + "','" + "DEP" + "','" + voteheadBalance + "','" + finalcachbookvalue + "','" + "0" + "','" + jpmmode.getSelectedItem() + "','" + "" + "','" + "3" + "','" + postingVotehead + "')";
                                                                ps = con.prepareStatement(querry);
                                                                ps.execute();
                                                                instantpayment = instantpayment - voteheadBalance;
                                                                if (instantpayment <= 0) {
                                                                    fullyserviced = true;
                                                                    break;
                                                                }

                                                            } else {
                                                                ps = con.prepareStatement("Insert into reciepts values('" + jReceiptNumber.getText() + "','" + voteheadid + "','" + Globals.currentTermName() + "','" + postingYear + "','" + IdGenerator.keyGen() + "',now(),'" + instantpayment + "','" + admno + "','" + jpmmode.getSelectedItem() + "',CurDate(),'" + "" + "')");
                                                                ps.execute();
                                                                String itemname = Globals.fullName(admno) + "," + jReceiptNumber.getText();
                                                                double cashbookvalue = 0, finalcachbookvalue = 0;
                                                                String querry0 = "Select max(date),cashbal from cashbookanalysis where  mode='" + "cash" + "' and accountid='" + "3" + "'";
                                                                ps = con.prepareStatement(querry0);
                                                                ResultSet rs1 = ps.executeQuery();
                                                                while (rs1.next()) {
                                                                    cashbookvalue = rs1.getDouble("cashbal");
                                                                }
                                                                con = DbConnection.connectDb();

                                                                finalcachbookvalue = cashbookvalue + instantpayment;
                                                                String querry = "Insert into Cashbookanalysis values('" + itemname + "',Now(),'" + jReceiptNumber.getText() + "','" + "DEP" + "','" + instantpayment + "','" + finalcachbookvalue + "','" + "0" + "','" + jpmmode.getSelectedItem() + "','" + "" + "','" + "3" + "','" + postingVotehead + "')";
                                                                ps = con.prepareStatement(querry);
                                                                ps.execute();
                                                                fullyserviced = true;
                                                                break;
                                                            }

                                                        }

                                                    }
                                                    if (fullyserviced == false) {

                                                        double overpayment = instantpayment;
                                                        String maxpaidvoteheadid = "";
                                                        double maxamount = 0;
                                                        ps = con.prepareStatement("Select voteheadid,max(amount) from payablevoteheadperstudent where term='" + Globals.currentTermName() + "' and academicyear='" + Globals.academicYear() + "' and admnumber='" + admno + "'");
                                                        rs = ps.executeQuery();
                                                        if (rs.next()) {
                                                            maxamount = rs.getDouble("Max(amount)");
                                                            maxpaidvoteheadid = rs.getString("Voteheadid");

                                                        }
                                                        ps = con.prepareStatement("Insert into reciepts values('" + jReceiptNumber.getText() + "','" + maxpaidvoteheadid + "','" + Globals.currentTermName() + "','" + Globals.academicYear() + "','" + IdGenerator.keyGen() + "',now(),'" + overpayment + "','" + admno + "','" + jpmmode.getSelectedItem() + "',curDate(),'" + "" + "')");
                                                        ps.execute();
                                                        String itemname = admno + "," + jReceiptNumber.getText();
                                                        double cashbookvalue = 0, finalcachbookvalue = 0;
                                                        String querry0 = "Select max(date),cashbal from cashbookanalysis where  mode='" + "cash" + "' and accountid='" + "3" + "'";
                                                        ps = con.prepareStatement(querry0);
                                                        ResultSet rs1 = ps.executeQuery();
                                                        while (rs1.next()) {
                                                            cashbookvalue = rs1.getDouble("cashbal");
                                                        }
                                                        con = DbConnection.connectDb();

                                                        finalcachbookvalue = cashbookvalue + overpayment;
                                                        String querry = "Insert into Cashbookanalysis values('" + itemname + "',Now(),'" + jReceiptNumber.getText() + "','" + "DEP" + "','" + overpayment + "','" + finalcachbookvalue + "','" + "0" + "','" + jpmmode.getSelectedItem() + "','" + "" + "','" + "3" + "','" + maxpaidvoteheadid + "')";
                                                        ps = con.prepareStatement(querry);
                                                        ps.execute();

                                                    }

                                                    AMOUNT = jamount.getText();
                                                    admissionNumber = admno;

                                                    if (reciept1.isSelected()) {
                                                        JOptionPane.showMessageDialog(this, " Payment Details Saved Sucessfully\n Kindly Wait For A Short Moment As The System Prepares The Payment Reciept");
                                                        Date date = new Date();
                                                        SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");

                                                        RecieptGenerator.generateReciept(admno, jReceiptNumber.getText(), format.format(date), jamount.getText(), jpmmode.getSelectedItem().toString(), depositedate);
                                                        jclassl.setSelectedIndex(0);
                                                        jname.setSelectedIndex(0);

                                                    } else {
                                                        JOptionPane.showMessageDialog(this, " Payment Details Saved Sucessfully");
                                                    }

                                                    adm.setText("");
                                                    jReceiptNumber.setText("");
                                                    jamount.setText("");
                                                    jslipnumber.setText("");
                                                    jname.setSelectedIndex(0);
                                                    jclassl.setSelectedIndex(0);
                                                    if (ConfigurationIntialiser.autoReceiptNumber()) {
                                                        jReceiptNumber.setText(String.valueOf(Globals.nextReceiptNumber()));
                                                    } else {
                                                        jReceiptNumber.setText("");
                                                    }

                                                    if (confirmation.isSelected()) {

                                                        if (ConfigurationIntialiser.smsOfflineSender()) {
                                                            String ParentNames = "";
                                                            String querr = "select parentfullnames,telephone1,admissionnumber  from admission where admissionnumber='" + admissionNumber + "'";
                                                            String phone = "";
                                                            String mesage = "";
                                                            ps = con.prepareStatement(querr);
                                                            rs = ps.executeQuery();
                                                            ResultSetMetaData meta = rs.getMetaData();

                                                            if (rs.next()) {
                                                                boolean testa = false;
                                                                ParentNames = rs.getString("ParentfullNames");
                                                                String p = rs.getString("telephone1");
                                                                if (p.equalsIgnoreCase("")) {

                                                                } else {

                                                                    phone = "254" + p.substring(1);

                                                                    mesage = "Dear Parent/Guardian, Your Fee Payment Of KSH." + AMOUNT + " Has Been Recieved," + "Your New Fee Balance Is KSH." + Globals.balanceCalculator(admissionNumber) + ".00 KAMARANDI SEC. SCH.";
                                                                    messageData = new HashMap<String, String>();
                                                                    messageData.put("message", mesage);
                                                                    messageData.put("phone", phone);
                                                                    messagesList.add(messageData);

                                                                    MessageGateway.batchMessageQueuer(messagesList);
                                                                    JOptionPane.showMessageDialog(this, "Confirmation Reciept Sent Successfully");

                                                                }

                                                            }

                                                        } else {

                                                        }

                                                    }

                                                }

                                            }

                                        } else {
                                            JOptionPane.showMessageDialog(this, "This Receipt Number Has Already Been Updated To " + Globals.fullName(admReciptConflict) + "\n Kindly Choose A Diffrent Receipt Number To Avoid System Refference Errors");

                                        }

                                    }

                                }

                            }
                        }
                    }

                }

            } catch (HeadlessException | NumberFormatException | SQLException sq) {
                JOptionPane.showMessageDialog(this, sq.getMessage());
                sq.printStackTrace();
            }

        } else if (obj == jclassl) {

            try {
                String classcode = "";
                String sql7 = "Select classcode from classes where classname='" + jclassl.getSelectedItem() + "' ";
                ps = con.prepareStatement(sql7);
                rs = ps.executeQuery();
                if (rs.next()) {
                    classcode = rs.getString("Classcode");
                }

                jname.removeAllItems();
                jname.addItem("Select Student");

                String sql3 = "Select firstname,middlename,lastname from admission where currentform='" + classcode + "'";
                ps = con.prepareStatement(sql3);
                ResultSet Rs = ps.executeQuery();
                while (Rs.next()) {
                    jname.addItem(Rs.getString("FirstName") + "       " + Rs.getString("Middlename") + "     " + Rs.getString("Lastname"));
                }

            } catch (SQLException sq) {
                JOptionPane.showMessageDialog(this, sq.getMessage());
                sq.printStackTrace();
            }
        }

    }

}
