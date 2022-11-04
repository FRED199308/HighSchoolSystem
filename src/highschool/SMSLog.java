package highschool;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


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
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextPane;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;

import net.miginfocom.swing.MigLayout;

/**
 * @author FRED
 */
public class SMSLog implements ActionListener {

    private JPanel bottom;
    private JPanel top;
    private JPanel holder;
    private JTable tab;
    private DefaultTableModel model, model2;
    private JScrollPane pane;
    private JTextPane desc = new JTextPane();

    private JLabel infor, filter;
    private FredButton inbox, delete, sentitems, outbox, retry;

    private PreparedStatement ps;
    private ResultSet rs;
    Object cols[] = {"Name", "Phone", "message", "Time Recieved", "Message Ref No."};
    private Connection con = DbConnection.connectDb();
    Object row[] = new Object[cols.length];
    String box = "inbox";
    Thread refresh;
    int sentcounter = 0, sentcounter2 = 0;

    boolean viewingSent = false;

    public SMSLog() {

    }

    public void sentItemRefresher() {
        if (refresh == null) {
            refresh = new Thread() {
                public void run() {
                    for (; ; ) {

                        if (viewingSent) {

                            try {
                                sleep(5000);
                                if (viewingSent) {
                                    String sqla = "Select count(*) from ozekimessageout ";
                                    ps = con.prepareStatement(sqla);
                                    rs = ps.executeQuery();
                                    if (rs.next()) {
                                        sentcounter = rs.getInt("Count(*)");
                                    }
                                    if (sentcounter == sentcounter2) {

                                    } else {


                                        sentcounter2 = 0;
                                        Object newcols[] = {"Name", "Phone", "message", "Time sent", "Message Ref No.", "Status", "Time Recieved"};
                                        Object newrow[] = new Object[newcols.length];
                                        while (model.getRowCount() > 0) {
                                            model.removeRow(0);
                                        }
                                        model.setColumnIdentifiers(newcols);
                                        infor.setText("Sent Items");
                                        String phone;

                                        String sql = "Select * from ozekimessageout order by senttime desc";
                                        ps = con.prepareStatement(sql);
                                        rs = ps.executeQuery();
                                        while (rs.next()) {
                                            sentcounter2++;
                                            String name;
                                            phone = "0" + rs.getString("receiver").substring(4, rs.getString("receiver").length()).trim();
                                            String sql2 = "Select firstname,middlename,lastname from admission where telephone1='" + phone + "'";
                                            ps = con.prepareStatement(sql2);
                                            ResultSet RS = ps.executeQuery();
                                            if (RS.next()) {
                                                name = RS.getString("firstName") + "    " + RS.getString("Middlename") + "    " + RS.getString("LastName");
                                            } else {

                                                String sql3 = "Select firstname,middlename,lastname from staffs where phonenumber='" + phone + "'";
                                                ps = con.prepareStatement(sql3);
                                                RS = ps.executeQuery();
                                                if (RS.next()) {
                                                    name = RS.getString("firstName") + "    " + RS.getString("Middlename") + "    " + RS.getString("LastName");
                                                } else {
                                                    name = "Unknown";
                                                }
                                            }

                                            newrow[0] = name;
                                            newrow[1] = phone;
                                            newrow[2] = rs.getString("msg");
                                            newrow[3] = rs.getString("senttime");
                                            newrow[4] = rs.getString("Id");
                                            if (rs.getString("status").equalsIgnoreCase("Transmitted")) {
                                                newrow[5] = "Sent";
                                            } else if (rs.getString("status").equalsIgnoreCase("received")) {
                                                newrow[5] = "Received";
                                            } else if (rs.getString("status").equalsIgnoreCase("sent")) {
                                                newrow[5] = "Sending.......";
                                            }
                                            newrow[6] = rs.getString("receivedtime");
                                            model.addRow(newrow);
                                        }
                                    }
                                }
                            } catch (InterruptedException | SQLException sq) {
                                sq.printStackTrace();
                                JOptionPane.showMessageDialog(null, sq.getMessage());
                            }
                        }
                    }
                }

            };
            refresh.start();
        }


    }

    public JPanel smsLogPanel() {
        holder = new JPanel();
        top = new JPanel();
        bottom = new JPanel();
        sentitems = new FredButton("Sent Items");
        delete = new FredButton("Delete");
        outbox = new FredButton("Outbox");
        inbox = new FredButton("Inbox");
        retry = new FredButton("Resend");

        infor = new JLabel("Inbox(Recieved Messages)");
        top.setLayout(new MigLayout());
        bottom.setLayout(new MigLayout());
        holder.setLayout(new MigLayout());
        holder.add(bottom, "growy,pushy");
        holder.add(top, "grow,push");
        tab = new JTable();
        model = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int col) {
                return false;
            }
        };
        model2 = new DefaultTableModel();
        model.setColumnIdentifiers(cols);
        model2.setColumnIdentifiers(cols);
        tab.setModel(model);
        pane = new JScrollPane(tab);
        top.add(infor, "gapleft 50,wrap");
        top.add(pane, "grow,push,wrap");
        top.add(desc, "growx,pushx");
        desc.setBorder(new TitledBorder("Message Content"));
        bottom.add(inbox, "gapleft 20,gaptop 30,wrap");
        bottom.add(sentitems, "gapleft 20,gaptop 30,wrap");
        bottom.add(outbox, "gapleft 20,gaptop 30,wrap");
        bottom.add(delete, "gapleft 20,gaptop 30,wrap");
        bottom.add(retry, "gapleft 20,gaptop 30,wrap");
        try {
            String phone;
            String sql = "Select * from ozekimessagein order by receivedtime desc";
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                String name;
                int length = rs.getString("sender").length();
                phone = "0" + rs.getString("sender").substring(4, length).trim();
                String sql2 = "Select firstname,middlename,lastname from admission where telephone1='" + phone + "'";
                ps = con.prepareStatement(sql2);
                ResultSet RS = ps.executeQuery();
                if (RS.next()) {
                    name = RS.getString("firstName") + "    " + RS.getString("Middlename") + "    " + RS.getString("LastName");
                } else {

                    String sql3 = "Select firstname,middlename,lastname from staffs where phonenumber='" + phone + "'";
                    ps = con.prepareStatement(sql3);
                    RS = ps.executeQuery();
                    if (RS.next()) {
                        name = RS.getString("firstName") + "    " + RS.getString("Middlename") + "    " + RS.getString("LastName");
                    } else {
                        name = "Unknown";
                    }
                }

                row[0] = name;
                row[1] = phone;
                row[2] = rs.getString("msg");
                row[3] = rs.getString("receivedtime");
                row[4] = rs.getString("Id");
                model.addRow(row);
            }
        } catch (SQLException sq) {
            sq.printStackTrace();
            JOptionPane.showMessageDialog(null, sq.getMessage());
        }
        sentitems.addActionListener(this);
        inbox.addActionListener(this);
        delete.addActionListener(this);

        outbox.addActionListener(this);
        retry.addActionListener(this);
        tab.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (tab.getSelectedRowCount() > 0) {
                    int row = tab.getSelectedRow();
                    String mess = model.getValueAt(row, 2).toString();
                    desc.setText(mess);
                }

            }


        });
        tab.addKeyListener(new KeyAdapter() {


            @Override
            public void keyPressed(KeyEvent e) {
                char c = e.getKeyChar();
                if (e.getKeyCode() == KeyEvent.VK_DOWN || e.getKeyCode() == KeyEvent.VK_UP) {
                    if (tab.getSelectedRowCount() > 0) {
                        int row = tab.getSelectedRow();
                        String mess = model.getValueAt(row, 2).toString();
                        desc.setText(mess);
                    }

                }
                if (e.getKeyCode() == KeyEvent.VK_DELETE) {
                    if (tab.getSelectedRowCount() > 0) {
                        delete.doClick();
                    }

                }
            }


        });


        return holder;


    }

    @Override
    public void actionPerformed(ActionEvent e) {
        con = DbConnection.connectDb();
        Object obj = e.getSource();
        if (obj == sentitems) {
            box = "outbox";
            sentcounter = 0;
            sentcounter2 = 0;
            Object newcols[] = {"Name", "Phone", "message", "Time sent", "Message Ref No.", "Status", "Time Recieved"};
            Object newrow[] = new Object[newcols.length];
            while (model.getRowCount() > 0) {
                model.removeRow(0);
            }
            model.setColumnIdentifiers(newcols);
            infor.setText("Sent Items");
            try {
                String phone;
                String sql = "Select * from ozekimessageout order by senttime desc";
                ps = con.prepareStatement(sql);
                rs = ps.executeQuery();
                while (rs.next()) {
                    sentcounter++;
                    sentcounter2++;
                    String name;
                    phone = "0" + rs.getString("receiver").substring(4, rs.getString("receiver").length()).trim();
                    String sql2 = "Select firstname,middlename,lastname from admission where telephone1='" + phone + "'";
                    ps = con.prepareStatement(sql2);
                    ResultSet RS = ps.executeQuery();
                    if (RS.next()) {
                        name = RS.getString("firstName") + "    " + RS.getString("Middlename") + "    " + RS.getString("LastName");
                    } else {

                        String sql3 = "Select firstname,middlename,lastname from staffs where phonenumber='" + phone + "'";
                        ps = con.prepareStatement(sql3);
                        RS = ps.executeQuery();
                        if (RS.next()) {
                            name = RS.getString("firstName") + "    " + RS.getString("Middlename") + "    " + RS.getString("LastName");
                        } else {
                            name = "Unknown";
                        }
                    }

                    newrow[0] = name;
                    newrow[1] = phone;
                    newrow[2] = rs.getString("msg");
                    newrow[3] = rs.getString("senttime");
                    newrow[4] = rs.getString("Id");
                    if (rs.getString("status").equalsIgnoreCase("Transmitted")) {
                        newrow[5] = "Sent";
                    } else if (rs.getString("status").equalsIgnoreCase("received")) {
                        newrow[5] = "Received";
                    } else if (rs.getString("status").equalsIgnoreCase("sent")) {
                        newrow[5] = "Sending.......";
                    }
                    newrow[6] = rs.getString("receivedtime");
                    model.addRow(newrow);
                }
            } catch (SQLException sq) {
                sq.printStackTrace();
                JOptionPane.showMessageDialog(null, sq.getMessage());
            }
            viewingSent = true;
            sentItemRefresher();
        } else if (obj == retry) {
            if (tab.getSelectedRowCount() < 1) {
                JOptionPane.showMessageDialog(holder, "Select The Message To Resend");
            } else {
                int selected = tab.getSelectedRow();
                String messageid = model.getValueAt(selected, 4).toString();
                try {
                    String sql = "Update ozekimessageout set status='" + "sent" + "' where id='" + messageid + "'";
                    ps = con.prepareStatement(sql);
                    ps.execute();
                    tab.setValueAt("Sending.......", selected, 5);
                    JOptionPane.showMessageDialog(holder, "Message is Being Sent");
                } catch (Exception sq) {
                    sq.printStackTrace();
                    JOptionPane.showMessageDialog(null, sq.getMessage());
                }
            }
        } else if (obj == inbox) {
            viewingSent = false;
            box = "inbox";
            while (model.getRowCount() > 0) {
                model.removeRow(0);
            }
            model.setColumnIdentifiers(cols);
            infor.setText("Inbox(Recieved Items)");
            try {
                String phone;
                String sql = "Select * from ozekimessagein order by receivedtime desc";
                ps = con.prepareStatement(sql);
                rs = ps.executeQuery();
                while (rs.next()) {
                    String name;

                    int length = rs.getString("sender").length();
                    phone = "0" + rs.getString("sender").substring(4, length).trim();
                    String sql2 = "Select firstname,middlename,lastname from admission where telephone1='" + phone + "'";
                    ps = con.prepareStatement(sql2);
                    ResultSet RS = ps.executeQuery();
                    if (RS.next()) {
                        name = RS.getString("firstName") + "    " + RS.getString("Middlename") + "    " + RS.getString("LastName");
                    } else {

                        String sql3 = "Select firstname,middlename,lastname from staffs where phonenumber='" + phone + "'";
                        ps = con.prepareStatement(sql3);
                        RS = ps.executeQuery();
                        if (RS.next()) {
                            name = RS.getString("firstName") + "    " + RS.getString("Middlename") + "    " + RS.getString("LastName");
                        } else {
                            name = "Unknown";
                        }
                    }

                    row[0] = name;
                    row[1] = phone;
                    row[2] = rs.getString("msg");
                    row[3] = rs.getString("receivedtime");
                    row[4] = rs.getString("Id");
                    model.addRow(row);
                }
            } catch (SQLException sq) {
                sq.printStackTrace();
                JOptionPane.showMessageDialog(null, sq.getMessage());
            }

        } else if (obj == outbox) {
            viewingSent = false;
            box = "Outbox";
            Object newcols[] = {"Name", "Phone", "message", "Time sent", "Message Ref No.", "Status"};
            Object newrow[] = new Object[newcols.length];
            while (model.getRowCount() > 0) {
                model.removeRow(0);
            }
            model.setColumnIdentifiers(newcols);
            infor.setText("Failed Items");
            try {
                String phone;
                String sql = "Select * from ozekimessageout where status='" + "send" + "' order by senttime desc";
                ps = con.prepareStatement(sql);
                rs = ps.executeQuery();
                while (rs.next()) {
                    String name;
                    phone = "0" + rs.getString("receiver").substring(4, rs.getString("receiver").length()).trim();
                    String sql2 = "Select firstname,middlename,lastname from admission where telephone1='" + phone + "'";
                    ps = con.prepareStatement(sql2);
                    ResultSet RS = ps.executeQuery();
                    if (RS.next()) {
                        name = RS.getString("firstName") + "    " + RS.getString("Middlename") + "    " + RS.getString("LastName");
                    } else {

                        String sql3 = "Select firstname,middlename,lastname from staffs where phonenumber='" + phone + "'";
                        ps = con.prepareStatement(sql3);
                        RS = ps.executeQuery();
                        if (RS.next()) {
                            name = RS.getString("firstName") + "    " + RS.getString("Middlename") + "    " + RS.getString("LastName");
                        } else {
                            name = "Unknown";
                        }
                    }

                    newrow[0] = name;
                    newrow[1] = phone;
                    newrow[2] = rs.getString("msg");
                    newrow[3] = rs.getString("senttime");
                    newrow[4] = rs.getString("Id");
                    newrow[5] = "Failed";

                    model.addRow(newrow);
                }
            } catch (SQLException sq) {
                JOptionPane.showMessageDialog(null, sq.getMessage());
                sq.printStackTrace();
            }

        } else if (obj == delete) {
            int[] selected = tab.getSelectedRows();

            if (RightsAnnouncer.DeleteRights()) {
                if (tab.getSelectedRowCount() > 0) {


                    int deleteCounter = 0;
                    Object r[] = new Object[7];
                    for (int y = 0; y < selected.length; ++y) {
                        r[0] = model.getValueAt(selected[y], 0);
                        r[1] = model.getValueAt(selected[y], 1);
                        r[2] = model.getValueAt(selected[y], 2);
                        r[3] = model.getValueAt(selected[y], 3);
                        r[4] = model.getValueAt(selected[y], 4);
                        model2.addRow(r);

                    }


                    for (int s = 0; s < model2.getRowCount(); ++s) {


                        String Smscode = model2.getValueAt(s, 4).toString();
                        try {
                            if (box.equalsIgnoreCase("inbox")) {
                                String querry = "Delete from  ozekimessagein where id='" + Smscode + "'";
                                ps = con.prepareStatement(querry);
                                ps.execute();
                            } else {
                                String querry = "Delete from  ozekimessageout where id='" + Smscode + "'";
                                ps = con.prepareStatement(querry);
                                ps.execute();
                            }


                            for (int z = 0; z < tab.getRowCount(); ++z) {
                                if (model.getValueAt(z, 4).toString().equals(Smscode)) {
                                    model.removeRow(z);

                                    deleteCounter++;

                                }

                            }

                        } catch (SQLException q) {
                            JOptionPane.showMessageDialog(holder, q, "Error Occurred", JOptionPane.ERROR_MESSAGE);
                        }

                    }

                    JOptionPane.showMessageDialog(holder, deleteCounter + "   Messages Deleted", "Deleted", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(holder, "Select messages To Delete", "Failed", JOptionPane.ERROR_MESSAGE);
                }
                for (int z = 0; z < model2.getRowCount(); ++z) {
                    model2.removeRow(z);
                }
            } else {
                JOptionPane.showMessageDialog(holder, "You Do Not Have Sufficient Rights To perform This Operation\n Consult System Administrator");
            }

        }


    }

}
