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
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import static javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE;

import javax.swing.table.DefaultTableModel;

/**
 * @author ExamSeverPc
 */
public class RecieveFromGOK extends JFrame implements ActionListener {

    public int width = 730;
    private int height = 500;
    private PreparedStatement ps;
    private ResultSet rs;
    private DefaultTableModel model = new DefaultTableModel();
    private JTable tab = new JTable();
    private JScrollPane pane = new JScrollPane(tab);
    private Object cols[] = {"VoteHeadName", "Account", "Available Balance"};
    private Object row[] = new Object[cols.length];
    public FredCombo jacc = new FredCombo("Filter By Account");
    public FredLabel acc = new FredLabel("Account");
    public FredButton recieve = new FredButton("Credit VoteHead");
    public FredCombo jvotehead = new FredCombo("Filter VoteHead");
    public FredLabel votehead = new FredLabel("Vote Heads");
    FredLabel amount = new FredLabel("Amount");
    FredTextField jamount = new FredTextField();

    private Connection con = DbConnection.connectDb();
    double ovrbal = 0.0;

    public RecieveFromGOK() {
        setTitle("Recieve From Ministry Of Education");
        setSize(width, height);
        setLocationRelativeTo(null);
        setResizable(false);
        setLayout(null);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        ((JComponent) getContentPane()).setBorder(
                BorderFactory.createMatteBorder(5, 5, 5, 5, Color.MAGENTA));


        this.setIconImage(FrameProperties.icon());
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

        pane.setBounds(30, 30, 650, 300);
        add(pane);
        acc.setBounds(50, 350, 100, 30);
        add(acc);
        jacc.setBounds(150, 350, 200, 30);
        add(jacc);
        votehead.setBounds(380, 350, 100, 30);
        add(votehead);
        jvotehead.setBounds(530, 350, 150, 30);
        add(jvotehead);

        amount.setBounds(50, 420, 100, 30);
        add(amount);
        jamount.setBounds(150, 420, 150, 30);
        add(jamount);
        recieve.setBounds(400, 420, 150, 30);
        add(recieve);

        try {

            ps = con.prepareStatement("Select accountname from schoolaccounts");
            rs = ps.executeQuery();
            while (rs.next()) {
                jacc.addItem(rs.getString("AccountName"));
            }

            ps = con.prepareStatement("Select * from voteheads");
            rs = ps.executeQuery();
            while (rs.next()) {
                double bal = 0.0, sumused = 0.0, sumincome = 0.0;
                String voteheadid = rs.getString("voteheadid");
                row[0] = rs.getString("VoteheadName");
                ps = con.prepareStatement(" select accountid from schoolaccounts");
                ResultSet RS = ps.executeQuery();
                while (RS.next()) {
                    String accountid = RS.getString("Accountid");
                    bal = Globals.voteHeadBalanceCalculator(voteheadid, accountid);

                    ovrbal += bal;
                    row[1] = Globals.AccountName(accountid);
                    row[2] = bal;
                    model.addRow(row);

                }

            }

            row[0] = "TOTAL";
            row[1] = "";
            row[2] = ovrbal;
            model.addRow(row);
            ps = con.prepareStatement("Select voteheadname from voteheads");
            rs = ps.executeQuery();
            while (rs.next()) {
                jvotehead.addItem(rs.getString("VoteHeadName"));
            }

        } catch (Exception sq) {
            sq.printStackTrace();
        }
        setVisible(true);
        jacc.addActionListener(this);
        jvotehead.addActionListener(this);
        recieve.addActionListener(this);
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
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object obj = e.getSource();
        if (obj == jacc) {
            if (jacc.getSelectedIndex() > 0) {
                model.getDataVector().removeAllElements();
                ovrbal = 0.0;
                try {
                    String accountid = Globals.AccountCode(jacc.getSelectedItem().toString());
                    ps = con.prepareStatement("Select * from voteheads");
                    rs = ps.executeQuery();
                    while (rs.next()) {
                        double bal = 0.0, sumused = 0.0, sumincome = 0.0;
                        String voteheadid = rs.getString("voteheadid");
                        row[0] = rs.getString("VoteheadName");

                        bal = Globals.voteHeadBalanceCalculator(voteheadid, accountid);


                        ovrbal += bal;
                        row[1] = jacc.getSelectedItem();
                        row[2] = bal;
                        model.addRow(row);


                    }
                    row[0] = "TOTAL";
                    row[1] = "";
                    row[2] = ovrbal;
                    model.addRow(row);
                } catch (Exception sq) {
                    sq.printStackTrace();
                }


            }
        } else if (obj == jvotehead) {
            if (jacc.getSelectedIndex() > 0 && jvotehead.getSelectedIndex() > 0) {

                model.getDataVector().removeAllElements();
                ovrbal = 0.0;
                try {
                    String accountid = Globals.AccountCode(jacc.getSelectedItem().toString());

                    double bal = 0.0, sumused = 0.0, sumincome = 0.0;
                    String voteheadid = Globals.voteHeadId(jvotehead.getSelectedItem().toString());
                    row[0] = jvotehead.getSelectedItem();

                    bal = Globals.voteHeadBalanceCalculator(voteheadid, accountid);


                    ovrbal += bal;
                    row[1] = jacc.getSelectedItem();
                    row[2] = bal;
                    model.addRow(row);


                    row[0] = "TOTAL";
                    row[1] = "";
                    row[2] = ovrbal;
                    model.addRow(row);
                } catch (Exception sq) {
                    sq.printStackTrace();
                }


            }


        } else if (obj == recieve) {
            if (jacc.getSelectedIndex() == 0) {
                JOptionPane.showMessageDialog(this, "Select The Account To Credit");
            } else {
                if (jvotehead.getSelectedIndex() == 0) {
                    JOptionPane.showMessageDialog(this, "Select The VoteHead To Credit");
                } else {
                    if (jamount.getText().isEmpty()) {
                        JOptionPane.showMessageDialog(this, "Input Valid Amount");
                    } else {

                        String accountid = Globals.AccountCode(jacc.getSelectedItem().toString());
                        String voteHeadId = Globals.voteHeadId(jvotehead.getSelectedItem().toString());
                        double donationAmount = Double.valueOf(jamount.getText());
                        try {
                            String donationid = IdGenerator.keyGen();
                            ps = con.prepareStatement("Insert into GOKdonations values('" + accountid + "','" + voteHeadId + "','" + Globals.currentTermName() + "','" + Globals.academicYear() + "','" + donationid + "',curDate(),'" + donationAmount + "')");
                            ps.execute();

                            String itemname = "G.O.K Donation " + ", " + jvotehead.getSelectedItem();
                            double cashbookvalue = 0, finalcachbookvalue = 0;
                            String querry0 = "Select max(date),bankbal from cashbookanalysis where  mode='" + "Bank" + "' and accountid='" + accountid + "'";
                            ps = con.prepareStatement(querry0);
                            ResultSet rs1 = ps.executeQuery();
                            while (rs1.next()) {
                                cashbookvalue = rs1.getDouble("bankbal");
                            }


                            finalcachbookvalue = cashbookvalue + donationAmount;
                            String querry = "Insert into Cashbookanalysis values('" + itemname + "',Now(),'" + donationid + "','" + "DEP" + "','" + donationAmount + "','" + "0" + "','" + finalcachbookvalue + "','" + "BANK" + "','" + "" + "','" + accountid + "','" + voteHeadId + "')";
                            ps = con.prepareStatement(querry);
                            ps.execute();

                            JOptionPane.showMessageDialog(this, "Account Credited Successfully");
                            jamount.setText("");
                            jvotehead.setSelectedIndex(jvotehead.getSelectedIndex());

                        } catch (Exception sq) {
                            sq.printStackTrace();
                        }


                    }


                }
            }
        }

    }

    public static void main(String[] args) {
        new RecieveFromGOK();
    }

}
