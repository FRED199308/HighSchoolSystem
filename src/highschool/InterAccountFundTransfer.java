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
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JOptionPane;

/**
 * @author ExamSeverPc
 */
public class InterAccountFundTransfer extends FredFrame implements ActionListener {


    private PreparedStatement ps;
    private ResultSet rs;
    private Connection con = DbConnection.connectDb();

    private int width = 700;
    private int heigth = 500;
    private FredLabel school = new FredLabel("School Fund");
    private FredLabel tution = new FredLabel("Tution a/c");
    private FredLabel operation = new FredLabel("Operation a/c");
    private FredLabel infor = new FredLabel("Account Status");
    private FredLabel from = new FredLabel("From: ");
    private FredLabel to = new FredLabel("To: ");
    private FredLabel amount = new FredLabel("Amount");
    private FredButton cancel = new FredButton("Close");
    private FredButton transfer = new FredButton("TransFer");
    private FredCombo jfrom = new FredCombo("Select Account Transferable");
    private FredCombo jto = new FredCombo("Select The Account To Transfer To");

    private FredTextField jamount = new FredTextField();

    public InterAccountFundTransfer() {


        setTitle("Inter Account Fund Transfer Pane");
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

        infor.setBounds(250, 30, 200, 30);
        add(infor);

        school.setBounds(100, 70, 450, 30);
        add(school);
        tution.setBounds(100, 120, 450, 30);
        add(tution);
        operation.setBounds(100, 170, 450, 30);
        add(operation);
        from.setBounds(30, 210, 150, 30);
        add(from);
        jfrom.setBounds(150, 210, 200, 30);
        add(jfrom);
        to.setBounds(400, 210, 150, 30);
        add(to);
        jto.setBounds(450, 210, 200, 30);
        add(jto);
        amount.setBounds(150, 270, 150, 30);
        add(amount);
        jamount.setBounds(300, 270, 150, 30);
        add(jamount);


        cancel.setBounds(100, 350, 100, 30);
        add(cancel);
        transfer.setBounds(400, 350, 150, 30);
        add(transfer);

        jamount.addKeyListener(new KeyAdapter() {
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();
                if (Character.isAlphabetic(c) || jamount.getText().length() > 30) {
                    getToolkit().beep();
                    e.consume();
                }
            }


        });
        try {

            ps = con.prepareStatement("Select accountname,accountid from schoolaccounts");
            rs = ps.executeQuery();
            while (rs.next()) {
                jfrom.addItem(rs.getString("AccountName"));
                jto.addItem(rs.getString("AccountName"));


            }
            school.setText("School Fund Account Balance Kshs. " + String.valueOf(Globals.accountBalanceCalculator(Globals.AccountCode("School Fund Account"))));
            tution.setText("Tution Account Balance Kshs. " + String.valueOf(Globals.accountBalanceCalculator(Globals.AccountCode("Tution Account"))));
            operation.setText("Operation Account Balance Ksh. " + String.valueOf(Globals.accountBalanceCalculator(Globals.AccountCode("Operation Account"))));


        } catch (Exception sq) {
            sq.printStackTrace();
        }
        transfer.addActionListener(this);
        cancel.addActionListener(this);
        setVisible(true);

    }

    public static void main(String[] args) {
        new InterAccountFundTransfer();
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        Object obj = e.getSource();
        if (obj == cancel) {
            try {
                con.close();
            } catch (SQLException sq) {

            }
            CurrentFrame.currentWindow();
            dispose();
        } else if (obj == transfer) {
            if (jfrom.getSelectedIndex() == 0) {
                JOptionPane.showMessageDialog(this, "Select The Account To Transfer From");
            } else {

                if (jto.getSelectedIndex() == 0) {
                    JOptionPane.showMessageDialog(this, "Select The Acount To Transfer To");
                } else {
                    if (jfrom.getSelectedIndex() == jto.getSelectedIndex()) {
                        JOptionPane.showMessageDialog(this, "You Cannot Tranfer Funds To The Same Account");
                    } else {
                        if (jamount.getText().isEmpty()) {
                            JOptionPane.showMessageDialog(this, "Input A Valid Amount");
                        } else {
                            int transferAmount = Integer.parseInt(jamount.getText());
                            String accountfromid = Globals.AccountCode(jfrom.getSelectedItem().toString());
                            String accounttoId = Globals.AccountCode(jto.getSelectedItem().toString());
                            double bal = (Globals.accountBalanceCalculator(accountfromid) - transferAmount);

                            if (bal < 0) {
                                JOptionPane.showMessageDialog(this, "This Account Does Not Have Sufficient Fund To Transfer The Amount\n Failed");
                            } else {

                                try {
                                    String voteheadid1f = "", voteheadid1t = "";
                                    ps = con.prepareStatement("Select voteheadid from voteheads where voteheadName='" + jto.getSelectedItem() + "'");
                                    rs = ps.executeQuery();
                                    if (rs.next()) {
                                        voteheadid1t = rs.getString("Voteheadid");

                                    }
                                    ps = con.prepareStatement("Select voteheadid from voteheads where voteheadName='" + jfrom.getSelectedItem() + "'");
                                    rs = ps.executeQuery();
                                    if (rs.next()) {
                                        voteheadid1f = rs.getString("Voteheadid");

                                    }

                                    String itemname = jto.getSelectedItem() + ", " + "Fund Transfer";
                                    int cashbookvalue = 0, finalcachbookvalue = 0;
                                    String querry0 = "Select max(date),bankbal from cashbookanalysis where  mode='" + "Bank" + "' and accountid='" + accountfromid + "'";
                                    ps = con.prepareStatement(querry0);
                                    ResultSet rs1 = ps.executeQuery();
                                    while (rs1.next()) {
                                        cashbookvalue = rs1.getInt("bankbal");
                                    }


                                    finalcachbookvalue = cashbookvalue - transferAmount;
                                    String querry = "Insert into Cashbookanalysis values('" + itemname + "',Now(),'" + IdGenerator.keyGen() + "','" + "WD" + "','" + transferAmount + "','" + "0" + "','" + finalcachbookvalue + "','" + "BANK" + "','" + "" + "','" + accountfromid + "','" + voteheadid1t + "')";
                                    ps = con.prepareStatement(querry);
                                    ps.execute();

                                    itemname = jfrom.getSelectedItem() + ", " + "Fund Transfer";
                                    cashbookvalue = 0;
                                    finalcachbookvalue = 0;
                                    querry0 = "Select max(date),bankbal from cashbookanalysis where  mode='" + "Bank" + "' and accountid='" + accounttoId + "'";
                                    ps = con.prepareStatement(querry0);
                                    rs1 = ps.executeQuery();
                                    while (rs1.next()) {
                                        cashbookvalue = rs1.getInt("bankbal");
                                    }


                                    finalcachbookvalue = cashbookvalue + transferAmount;
                                    querry = "Insert into Cashbookanalysis values('" + itemname + "',Now(),'" + IdGenerator.keyGen() + "','" + "DEP" + "','" + transferAmount + "','" + "0" + "','" + finalcachbookvalue + "','" + "BANK" + "','" + "" + "','" + accounttoId + "','" + voteheadid1f + "')";
                                    ps = con.prepareStatement(querry);
                                    ps.execute();


                                    JOptionPane.showMessageDialog(this, "Cash Transfered Successfully");
                                    jamount.setText("");
                                    jfrom.setSelectedIndex(0);
                                    jto.setSelectedIndex(0);
                                    school.setText("School Fund Account Balance Kshs. " + String.valueOf(Globals.accountBalanceCalculator(Globals.AccountCode("School Fund Account"))));
                                    tution.setText("Tution Account Balance Kshs. " + String.valueOf(Globals.accountBalanceCalculator(Globals.AccountCode("Tution Account"))));
                                    operation.setText("Operation Account Balance Ksh. " + String.valueOf(Globals.accountBalanceCalculator(Globals.AccountCode("Operation Account"))));


                                } catch (Exception sq) {
                                    sq.printStackTrace();
                                }


                            }


                        }

                    }
                }
            }
        }

    }


}
