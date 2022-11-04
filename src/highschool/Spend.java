/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package highschool;


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
import java.sql.SQLException;
import java.util.Date;
import javax.swing.JButton;
import javax.swing.JDialog;


import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;

import static javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE;

import javax.swing.border.TitledBorder;

/**
 * @author FRED_ADMIN
 */
public class Spend extends FredFrame implements ActionListener {

    @SuppressWarnings(value = "ResultOfObjectAllocationIgnored")
    public static void main(String[] args) {
        new Spend();
    }

    private int width = 1150;
    private int heigth = 600;

    private FredLabel expenseCategory;

    private FredLabel name;
    private FredLabel date;
    private FredLabel amount, recieptNo, invoice;
    private FredLabel approvedBy;
    private FredLabel desc;
    private JTextPane pane;
    private JTextField jName;
    private FredCombo jExpenseCategory;
    private FredDateChooser jDate;
    private JTextField jreciept;
    private JTextField jAmount;
    private FredCombo jinvoice;


    private JButton save;
    private JButton cancel;

    private FredCombo staff = new FredCombo("Select staff");

    private String[] opt = {"Select Votehead Category"};
    private String[] opt2 = {"Select Status", "Paid", "Pending"};
    private PreparedStatement ps;
    private ResultSet rs;
    private DbConnection connect = new DbConnection();
    private Connection con;
    private FredLabel paymode = new FredLabel("Payment Mode");
    private FredCombo jpaymode = new FredCombo("Select Payment Mode");
    private FredLabel account = new FredLabel("Spending Account");
    private FredCombo jaccount = new FredCombo("Select Affected Account");
    private FredLabel infor = new FredLabel("");
    private IdGenerator generate = new IdGenerator();
    private DataValidation validate = new DataValidation();
    private FredCheckBox pv = new FredCheckBox("Generate Payment Vochure");
    String fname, mname, lname, combined;
    String invoicenumber = "";
    String exid = "";
    String Date = "", description = "";
    String campanyname, amount1, campanyinvoicenumber, date1;

    @SuppressWarnings({"LeakingThisInConstructor", "OverridableMethodCallInConstructor"})
    public Spend() {
        setTitle("Record Expenditure");
        setSize(width, heigth);
        setLocationRelativeTo(null);
        setLayout(null);
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
        name = new FredLabel("Expenditure On");
        desc = new FredLabel("Description");
        amount = new FredLabel("Amount");
        expenseCategory = new FredLabel("Vote Head");
        date = new FredLabel("Date");
        pane = new JTextPane();
        recieptNo = new FredLabel("Reciept/Cheque NO.");
        jreciept = new JTextField();
        jDate = new FredDateChooser();
        jAmount = new JTextField();
        jExpenseCategory = new FredCombo(opt);
        jName = new JTextField();
        invoice = new FredLabel("Invoice Number");
        jinvoice = new FredCombo("Leave This Option If No Is Invoice Available");
        save = new JButton("Save");
        jpaymode.addItem("Cheque");
        jpaymode.addItem("Cash");
        cancel = new JButton("cancel");
        pane.setBorder(new TitledBorder("Brief Description"));
        jDate.setDateFormatString("yyyy/MM/dd");
        try {
            String querry = "select  VoteheadName from voteheads";
            ps = con.prepareStatement(querry);
            rs = ps.executeQuery();
            while (rs.next()) {
                jExpenseCategory.addItem(rs.getString("VoteheadName"));
            }
            String sql = "Select localinvoicenumber,Campany from invoices where status='" + "pending" + "'";
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                jinvoice.addItem(rs.getString("localinvoiceNumber") + "(" + rs.getString("Campany") + ")");
            }
            ps = con.prepareStatement("Select AccountName from schoolaccounts");
            rs = ps.executeQuery();
            while (rs.next()) {
                jaccount.addItem(rs.getString("AccountName"));
            }

        } catch (SQLException sq) {
            JOptionPane.showMessageDialog(this, sq.getMessage());
        }

        expenseCategory.setBounds(30, 30, 150, 30);
        add(expenseCategory);
        jExpenseCategory.setBounds(250, 30, 300, 30);
        add(jExpenseCategory);
        account.setBounds(30, 130, 150, 30);

        jaccount.setBounds(250, 130, 300, 30);

        invoice.setBounds(30, 230, 150, 30);

        jinvoice.setBounds(250, 230, 300, 30);

        date.setBounds(600, 320, 300, 30);
        add(date);
        add(account);
        jDate.setBounds(800, 320, 300, 30);
        add(jaccount);
        add(jDate);
        amount.setBounds(30, 330, 150, 30);
        add(amount);
        infor.setBounds(20, 400, 700, 30);
        add(infor);
        pv.setBounds(100, 450, 200, 30);
        add(pv);
        name.setBounds(600, 30, 180, 30);

        add(name);
        jAmount.setBounds(250, 330, 300, 30);
        add(jAmount);
        infor.setFont(new Font("serif", Font.PLAIN, 10));

        pv.setSelected(true);
        jName.setBounds(800, 30, 300, 30);
        add(jName);
        add(invoice);
        add(jinvoice);
        paymode.setBounds(600, 130, 180, 30);
        add(recieptNo);
        jpaymode.setBounds(800, 130, 300, 30);
        add(jreciept);
        recieptNo.setBounds(600, 230, 180, 30);
        add(paymode);
        add(paymode);
        jreciept.setBounds(800, 230, 300, 30);
        add(jpaymode);
        add(jpaymode);

        //  desc.setBounds(30, 500, 150, 30);
        // add(desc);


        pane.setBounds(720, 370, 300, 100);
        add(pane);
        save.setBounds(200, 500, 130, 30);
        add(save);
        cancel.setBounds(750, 500, 130, 30);
        add(cancel);
        jDate.setMaxSelectableDate(new Date());
        cancel.addActionListener(this);
        save.addActionListener(this);
        jinvoice.addActionListener(this);
        jExpenseCategory.addActionListener(this);
//jinvoice.setEditable(true);
        jAmount.addKeyListener(new KeyAdapter() {

            @Override
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();
                if (!Character.isDigit(c) || jAmount.getText().length() > 30) {
                    e.consume();
                }

            }

            public void keyReleased(KeyEvent key) {
                if (jExpenseCategory.getSelectedIndex() > 0 && jaccount.getSelectedIndex() > 0) {
                    double bal = Globals.voteHeadBalanceCalculator(Globals.voteHeadId(jExpenseCategory.getSelectedItem().toString()), Globals.AccountCode(jaccount.getSelectedItem().toString()));
                    if ((bal - Double.valueOf(jAmount.getText())) <= 0) {
                        infor.setText("Warning..!,The Votehead Balance On Selected Account Is Below Amount to Be Paid. BAL Kshs.=" + bal + ",Extra Amount Paid By Other VoteHeads on The Same Account");
                    } else {
                        infor.setText("");
                    }
                } else {
                    infor.setText("");
                }

            }

        });
        jinvoice.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {

                char c = e.getKeyChar();
                for (int i = 0; i < jinvoice.getItemCount(); i++) {
                    if (jinvoice.getItemAt(i).toString().startsWith(jinvoice.getInputContext().toString())) {
                        jinvoice.setSelectedItem(jinvoice.getItemAt(i));

                    }
                }
                {

                }
            }
        });

        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object obj = e.getSource();
        boolean comply = true;

        if (obj == cancel) {
            try {
                con.close();
            } catch (SQLException sq) {

            }
            CurrentFrame.currentWindow();
            dispose();
        } else if (obj == jExpenseCategory) {
            try {
                ps = con.prepareStatement("Select accountAppendedid from voteheads where voteheadname='" + jExpenseCategory.getSelectedItem() + "'");
                rs = ps.executeQuery();
                if (rs.next()) {

                    if (rs.getString("accountAppendedid").equalsIgnoreCase("")) {
                        jaccount.setSelectedIndex(0);

                    } else {
                        jaccount.setSelectedItem(Globals.AccountName(rs.getString("accountAppendedid")));

                    }
                } else {
                    jaccount.setSelectedIndex(0);

                }
            } catch (Exception sq) {
                sq.printStackTrace();
            }

        } else if (obj == jinvoice) {
            if (jinvoice.getSelectedIndex() > 0) {
                this.remove(amount);
                this.remove(jAmount);
                this.remove(desc);
                this.remove(pane);
                this.remove(name);
                this.remove(jName);
                try {
                    int beginindex = jinvoice.getSelectedItem().toString().indexOf("(");

                    String invoiceNumber = jinvoice.getSelectedItem().toString().substring(0, beginindex);

                    String sql = "Select * from invoices where localinvoiceNumber='" + invoiceNumber + "'";
                    ps = con.prepareStatement(sql);
                    rs = ps.executeQuery();
                    if (rs.next()) {
                        campanyname = rs.getString("Campany");
                        amount1 = rs.getString("Amount");
                        campanyinvoicenumber = rs.getString("CampanyInvoiceNumber");
                        invoicenumber = rs.getString("localinvoiceNumber");
                        date1 = rs.getString("DateRecieved");
                        description = rs.getString("Description");
                    }

                    if (jExpenseCategory.getSelectedIndex() > 0 && jaccount.getSelectedIndex() > 0) {
                        double bal = Globals.voteHeadBalanceCalculator(Globals.voteHeadId(jExpenseCategory.getSelectedItem().toString()), Globals.AccountCode(jaccount.getSelectedItem().toString()));
                        if ((bal - Double.valueOf(amount1)) <= 0) {
                            infor.setText("Warning..!,The Votehead Balance On Selected Account Is Below Invoice Value to Be Paid.BAL Kshs." + bal + ",Extra Amount Paid By Other VoteHeads on The Same Account");
                        } else {
                            infor.setText("");
                        }
                    } else {
                        infor.setText("");
                    }


                } catch (SQLException sq) {
                    JOptionPane.showMessageDialog(this, sq.getMessage());
                }
            } else {
                invoicenumber = "";
                name.setBounds(600, 30, 180, 30);
                add(amount);
                add(name);
                jAmount.setBounds(250, 330, 300, 30);
                add(jAmount);

                pane.setBounds(720, 370, 300, 100);
                add(pane);

                jName.setBounds(800, 30, 300, 30);
                add(jName);
            }
            this.revalidate();
            this.repaint();
        } else if (obj == save) {
            String membercode = null;
            String leaderid = null;
            String key = "SP" + IdGenerator.keyGen();
            Date = ((JTextField) jDate.getDateEditor().getUiComponent()).getText();

            if (jExpenseCategory.getSelectedItem().toString().equalsIgnoreCase("Select Expense Category") && comply == true) {
                JOptionPane.showMessageDialog(this, "Kindly  Select the Expense Category");
                comply = false;
            } else if (((JTextField) jDate.getDateEditor().getUiComponent()).getText().isEmpty() && comply == true) {
                JOptionPane.showMessageDialog(this, "Kindl pick A valid Date from the Date Picker");
                comply = false;
            } else if (jName.getText().isEmpty() && comply == true && jinvoice.getSelectedIndex() == 0 && jinvoice.getSelectedIndex() == 0) {
                JOptionPane.showMessageDialog(this, "Kindly Enter a Valid Expenditure name", "Invalid Expenditure Name", JOptionPane.ERROR_MESSAGE);
                comply = false;
            } else if (jAmount.getText().isEmpty() && comply == true && jinvoice.getSelectedIndex() == 0) {
                JOptionPane.showMessageDialog(this, "Kindly Input a Valid Figure on Amount", "invalid Amount", JOptionPane.ERROR_MESSAGE);
                comply = false;
            } else if (jreciept.getText().isEmpty() && comply == true && jinvoice.getSelectedIndex() > 0) {
                JOptionPane.showMessageDialog(this, "Kindly Input a Valid Reciept Number", "Missing Reciept", JOptionPane.ERROR_MESSAGE);
                comply = false;
            }

            if (comply == true) {

                String accountid = Globals.AccountCode(jaccount.getSelectedItem().toString());
                int total = 0;

                con = DbConnection.connectDb();
                try {
                    exid = Globals.voteHeadId(jExpenseCategory.getSelectedItem().toString());
                    if (jinvoice.getSelectedIndex() > 0) {

                    } else {
                        amount1 = jAmount.getText();
                    }
                    double accountbal = Globals.accountBalanceCalculator(Globals.AccountCode(jaccount.getSelectedItem().toString()));
                    if ((accountbal - Double.valueOf(amount1)) <= 0) {
                        JOptionPane.showMessageDialog(this, "Insufficient Fund In The Selected Account To Carry Out The Transaction\nAccount Bal Kshs. " + accountbal, "Insufficient Fund...!!!", JOptionPane.ERROR_MESSAGE);
                    } else {
                        if (jpaymode.getSelectedIndex() == 0) {
                            JOptionPane.showMessageDialog(this, "Kindly Select The Payment Mode");
                        } else {
                            String spendid = "SP" + IdGenerator.keyGen();
                            if (jinvoice.getSelectedIndex() > 0) {

                                int option = JOptionPane.showConfirmDialog(this, "Are You Sure You Want To Pay This Invoice Invoice Details\n Campany: " + campanyname + " Campany Invoice Number: " + campanyinvoicenumber + " Amount: KSH." + amount1 + " Date Delivered: " + date1, "Confirm Invoice Payment", JOptionPane.YES_NO_OPTION);
                                if (option == JOptionPane.NO_OPTION) {
                                    JOptionPane.showMessageDialog(this, "Invoice Payment Postponed");
                                } else {
                                    Date = ((JTextField) jDate.getDateEditor().getUiComponent()).getText();

                                    String re = "";

                                    re = jreciept.getText();

                                    String querry = "Insert into Spendings values('" + exid + "','" + campanyname + "','" + Date + "','" + amount1 + "','" + jpaymode.getSelectedItem() + "','" + jinvoice.getSelectedItem() + "','" + re + "','" + Globals.currentTerm() + "','" + "" + "','" + spendid + "','" + accountid + "')";
                                    ps = con.prepareStatement(querry);
                                    ps.execute();
                                    String sql1 = "Update invoices set status='" + "Paid" + "',datepaid=curDate() where localinvoicenumber='" + invoicenumber + "'";
                                    ps = con.prepareStatement(sql1);
                                    ps.execute();


                                }


                            } else {
                                description = pane.getText();

                                String Date = ((JTextField) jDate.getDateEditor().getUiComponent()).getText();

                                String re = "";

                                re = jreciept.getText();


                                String querry = "Insert into Spendings values('" + exid + "','" + jName.getText() + "','" + Date + "','" + jAmount.getText() + "','" + jpaymode.getSelectedItem() + "','" + "" + "','" + re + "','" + Globals.currentTerm() + "','" + pane.getText() + "','" + spendid + "','" + accountid + "')";
                                ps = con.prepareStatement(querry);
                                ps.execute();


                            }
                            if (jpaymode.getSelectedIndex() == 1) {
                                String itemname = "";
                                if (jinvoice.getSelectedIndex() > 0) {
                                    itemname = campanyname + "," + invoicenumber + "  " + spendid;
                                } else {
                                    itemname = jName.getText() + "  " + spendid;
                                }

                                double cashbookvalue = 0, finalcachbookvalue = 0;
                                String querry0 = "Select max(date),bankbal from cashbookanalysis where  mode='" + "Bank" + "' and accountid='" + accountid + "'";
                                ps = con.prepareStatement(querry0);
                                ResultSet rs1 = ps.executeQuery();
                                while (rs1.next()) {
                                    cashbookvalue = rs1.getDouble("bankbal");
                                }

                                finalcachbookvalue = cashbookvalue - Double.valueOf(amount1);
                                String querryy = "Insert into Cashbookanalysis values('" + itemname + "',Now(),'" + spendid + "','" + "WD" + "','" + amount1 + "','" + "0" + "','" + finalcachbookvalue + "','" + "Bank" + "','" + "" + "','" + accountid + "','" + exid + "')";
                                ps = con.prepareStatement(querryy);
                                ps.execute();


                            } else {
                                String itemname = "";
                                if (jinvoice.getSelectedIndex() > 0) {
                                    itemname = campanyname + "," + invoicenumber + " " + spendid;
                                } else {
                                    itemname = jName.getText() + "  " + spendid;
                                }

                                double cashbookvalue = 0, finalcachbookvalue = 0;
                                String querry0 = "Select max(date),cashbal from cashbookanalysis where  mode='" + "cash" + "' and accountid='" + accountid + "'";
                                ps = con.prepareStatement(querry0);
                                ResultSet rs1 = ps.executeQuery();
                                while (rs1.next()) {
                                    cashbookvalue = rs1.getDouble("cashbal");
                                }

                                finalcachbookvalue = cashbookvalue - Double.valueOf(amount1);
                                String querryy = "Insert into Cashbookanalysis values('" + itemname + "',Now(),'" + spendid + "','" + "WD" + "','" + amount1 + "','" + finalcachbookvalue + "','" + "0" + "','" + "Cash" + "','" + "" + "','" + accountid + "','" + exid + "')";
                                ps = con.prepareStatement(querryy);
                                ps.execute();
                                // jinvoice.removeItemAt(jinvoice.getSelectedIndex());


                            }
                            JOptionPane.showMessageDialog(this, "Record Successfully Saved", "Success", JOptionPane.INFORMATION_MESSAGE);

                            if (pv.isSelected()) {
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

                                            ReportGenerator.paymentVocherGenerator(spendid,
                                                    jpayeename.getText(), jpayeeadress.getText()
                                                    , Date,
                                                    invoicenumber, description,
                                                    amount1, jpaymode.getSelectedItem().toString(),
                                                    exid,
                                                    jid.getText() + "  Phone: " + jpayeePhone.getText());
                                        }

                                    }
                                });

                            }
                            // ReportGenerator.paymentVocherGenerator(invoicenumber, accountid, key, date1, invoicenumber, accountid, amount1, membercode, spendid, spendid);
                            System.err.println(description);
                            jName.setText(null);
                            jinvoice.setSelectedIndex(0);
                            jDate.setDate(null);
                            jAmount.setText(null);
                            pane.setText(null);
                            jName.setText(null);
                            jDate.setDate(null);
                            jAmount.setText(null);
                            pane.setText(null);
                            jExpenseCategory.setSelectedIndex(0);
                            jinvoice.setSelectedIndex(0);
                        }


                    }


                } catch (HeadlessException | NumberFormatException | SQLException sq) {
                    JOptionPane.showMessageDialog(this, sq.getMessage());
                }
            }

        }
    }


}
