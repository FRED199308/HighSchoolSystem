/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package highschool;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
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
import javax.swing.JScrollPane;
import javax.swing.JTable;

import static javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE;

import javax.swing.table.DefaultTableModel;

/**
 * @author ExamSeverPc
 */
public class VoteHeadAdd extends JFrame implements ActionListener {


    private int width = 750;
    private int height = 580;
    private Connection con = DbConnection.connectDb();
    private PreparedStatement ps;
    private ResultSet rs;
    private DefaultTableModel model = new DefaultTableModel();
    private JTable tab = new JTable();
    private JScrollPane pane = new JScrollPane(tab);
    private Object cols[] = {"VoteHeadName", "VoteHead Ref No", "AccountApended"};
    private Object row[] = new Object[cols.length];

    private FredLabel vname = new FredLabel("Vote Head Name");
    private FredLabel accountName = new FredLabel("Account Appended");
    private FredCheckBox include = new FredCheckBox("Include As Fees");
    private FredTextField jvname = new FredTextField();
    private FredCombo jaccountName = new FredCombo("Leave This Unselected If Any");
    private FredButton cancel = new FredButton("Close");
    private FredButton save = new FredButton("Save");
    private FredButton edit = new FredButton("Update");
    String ACCOUNTCODE = "";

    public VoteHeadAdd() {
        setSize(width, height);
        setTitle("Vote Head Addition Pane");
        ((JComponent) getContentPane()).setBorder(
                BorderFactory.createMatteBorder(5, 5, 5, 5, Color.MAGENTA));

        this.setIconImage(FrameProperties.icon());


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
        setLayout(null);
        setResizable(false);
        setLocationRelativeTo(null);
        model.setColumnIdentifiers(cols);
        tab.setModel(model);
        try {
            ps = con.prepareStatement("Select * from SchoolAccounts");
            rs = ps.executeQuery();
            while (rs.next()) {
                jaccountName.addItem(rs.getString("AccountName"));
            }

            ps = con.prepareStatement("Select * from voteHeads");
            rs = ps.executeQuery();
            while (rs.next()) {
                row[0] = rs.getString("VoteheadName");
                row[1] = rs.getString("VoteheadId");
                row[2] = Globals.AccountName(rs.getString("AccountAppendedid"));
                model.addRow(row);
            }


        } catch (Exception sq) {
            sq.printStackTrace();

        }


        pane.setBounds(20, 30, 700, 300);
        add(pane);
        vname.setBounds(20, 350, 150, 30);
        add(vname);
        jvname.setBounds(200, 350, 200, 30);
        add(jvname);
        accountName.setBounds(420, 350, 150, 30);
        add(accountName);
        jaccountName.setBounds(550, 350, 180, 30);
        add(jaccountName);
        include.setBounds(400, 400, 200, 20);
        add(include);
        cancel.setBounds(100, 470, 130, 30);
        add(cancel);
        save.setBounds(300, 470, 130, 30);
        add(save);
        edit.setBounds(500, 470, 130, 30);
        add(edit);
        setVisible(true);
        save.addActionListener(this);
        edit.addActionListener(this);
        cancel.addActionListener(this);
        tab.addMouseListener(new MouseAdapter() {

            public void mouseClicked(MouseEvent ev) {
                try {
                    String payable = "";
                    String accountid = "";

                    int selectedrow = tab.getSelectedRow();
                    String vid = tab.getValueAt(selectedrow, 1).toString();
                    ACCOUNTCODE = tab.getValueAt(selectedrow, 1).toString();
                    String vnam = tab.getValueAt(selectedrow, 0).toString();
                    jvname.setText(vnam);
                    ps = con.prepareStatement("Select PayableAsFee,AccountAppendedId from voteheads where VoteHeadID='" + vid + "'");
                    rs = ps.executeQuery();
                    if (rs.next()) {
                        payable = rs.getString("PayableAsFee");
                        accountid = rs.getString("AccountAppendedId");
                    }
                    if (payable.equalsIgnoreCase("0")) {
                        include.setSelected(false);
                    } else {
                        include.setSelected(true);
                    }


                    if (accountid.equalsIgnoreCase("")) {
                        jaccountName.setSelectedIndex(0);
                    } else {
                        jaccountName.setSelectedItem(Globals.AccountName(accountid));
                    }


                } catch (SQLException sq) {
                    sq.printStackTrace();
                }


            }


        });

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
        } else if (obj == save) {
            if (jvname.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please Input  A Valid Vote Haed Name");
            } else {
                String accountid = "";
                String in = "";
                String vid = IdGenerator.keyGen();
                if (jaccountName.getSelectedIndex() > 0) {
                    accountid = Globals.AccountCode(jaccountName.getSelectedItem().toString());
                }
                if (include.isSelected()) {
                    in = "1";
                } else {
                    in = "0";
                }

                try {

                    ps = con.prepareStatement("Select * from voteheads where voteheadname='" + jvname.getText() + "'");
                    rs = ps.executeQuery();
                    if (rs.next()) {
                        JOptionPane.showMessageDialog(this, "Kindly Choose A Diffrent Votehead Name That Does Not Raise\n Ambigousness from The Already Created Voteheads");
                    } else {
                        ps = con.prepareStatement("Insert into voteHeads values('" + jvname.getText().toUpperCase() + "','" + vid + "',now(),'" + in + "','" + accountid + "')");
                        ps.execute();
                        if (in.equalsIgnoreCase("1")) {
                            int prio = 0;
                            ps = con.prepareStatement("Select count(*) from StudentPayableVoteHeads");
                            rs = ps.executeQuery();
                            if (rs.next()) {
                                prio = rs.getInt("Count(*)");
                            }
                            ps = con.prepareStatement("Insert into StudentPayableVoteHeads (voteheadid,academicyear,termcode,priority,amountPerHead) values('" + vid + "','" + Globals.academicYear() + "','" + Globals.currentTerm() + "','" + prio + "','" + "0" + "')");
                            ps.execute();
                        }
                        JOptionPane.showMessageDialog(this, "VoteHead Added Successfully");


                        row[0] = jvname.getText().toUpperCase();
                        row[1] = vid;
                        row[2] = Globals.AccountName(accountid);
                        model.addRow(row);
                        jvname.setText("");
                        jaccountName.setSelectedIndex(0);
                        include.setSelected(false);


                    }


                } catch (Exception sq) {
                    sq.printStackTrace();
                }

            }
        } else if (obj == edit) {


            if (jvname.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please Input  A Valid Vote Haed Name");
            } else {
                String accountid = "";
                String in = "";

                if (jaccountName.getSelectedIndex() > 0) {
                    accountid = Globals.AccountCode(jaccountName.getSelectedItem().toString());
                }
                if (include.isSelected()) {
                    in = "1";
                } else {
                    in = "0";
                }

                int selectedrow = tab.getSelectedRow();
                if (jaccountName.getSelectedIndex() > 0) {
                    accountid = Globals.AccountCode(jaccountName.getSelectedItem().toString());
                }


                try {
                    ps = con.prepareStatement("Update voteheads set VoteheadName='" + jvname.getText().toUpperCase() + "',PayableAsFee='" + in + "',accountappendedid='" + accountid + "' where voteheadid='" + ACCOUNTCODE + "'");
                    ps.execute();
                    if (in.equalsIgnoreCase("1")) {
                        ps = con.prepareStatement("Select * from StudentPayableVoteHeads where voteheadid='" + ACCOUNTCODE + "' and academicyear='" + Globals.academicYear() + "' and termcode='" + Globals.currentTerm() + "'");
                        rs = ps.executeQuery();
                        if (rs.next()) {

                        } else {
                            int prio = 0;
                            ps = con.prepareStatement("Select count(*) from StudentPayableVoteHeads");
                            rs = ps.executeQuery();
                            if (rs.next()) {
                                prio = rs.getInt("Count(*)");
                            }
                            ps = con.prepareStatement("Insert into StudentPayableVoteHeads (voteheadid,academicyear,termcode,priority,amountPerHead) values('" + ACCOUNTCODE + "','" + Globals.academicYear() + "','" + Globals.currentTerm() + "','" + prio + "','" + "0" + "')");
                            ps.execute();
                        }

                    } else {
                        ps = con.prepareStatement("Delete from StudentPayableVoteHeads where voteheadid='" + ACCOUNTCODE + "' and academicyear='" + Globals.academicYear() + "' and termcode='" + Globals.currentTerm() + "'");
                        ps.execute();
                    }


                    JOptionPane.showMessageDialog(this, "VoteHead Details Updated Successfully");
                    row[0] = jvname.getText().toUpperCase();
                    row[1] = ACCOUNTCODE;
                    row[2] = Globals.AccountName(accountid);
                    model.removeRow(selectedrow);
                    model.addRow(row);

                    jvname.setText("");
                    jaccountName.setSelectedIndex(0);
                    include.setSelected(false);
                } catch (Exception sq) {
                    sq.printStackTrace();
                }


            }


        }

    }


    public static void main(String[] args) {
        new VoteHeadAdd();
    }
}
