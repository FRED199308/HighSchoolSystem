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
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.ListSelectionModel;

import static javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE;

import javax.swing.border.TitledBorder;

/**
 * @author FRED
 */
public class SmsPta extends JFrame implements ActionListener {

    public static void main(String[] args) {
        new SmsPta();
    }

    private int width = 820;
    private int height = 580;
    private JCheckBox smsAll = new JCheckBox("SMS All PTA Members");
    private JList members, selectedMembers;
    private JTextPane message;
    private JScrollPane scroll, scroll2;
    private String staffcode;
    private FredButton send, cancel, copy, remove;
    private Connection con = DbConnection.connectDb();
    private PreparedStatement ps;
    private ResultSet rs;
    private IdGenerator key = new IdGenerator();

    private DefaultListModel model, model2;


    public SmsPta() {

        setSize(width, height);
        setTitle("P.T.A. SMS");
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
        members = new JList();
        setResizable(false);

        selectedMembers = new JList();
        scroll2 = new JScrollPane(selectedMembers);
        send = new FredButton("Send");
        cancel = new FredButton("Close");
        copy = new FredButton("Copy>>");
        remove = new FredButton("<<Remove");
        model = new DefaultListModel();
        model2 = new DefaultListModel();

        members.setModel(model);
        selectedMembers.setModel(model2);
        message = new JTextPane();
        scroll = new JScrollPane(members);
        members.setVisibleRowCount(6);
        members.setFixedCellHeight(25);
        selectedMembers.setVisibleRowCount(6);
        selectedMembers.setFixedCellHeight(25);
        selectedMembers.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        members.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        members.setBounds(50, 30, 300, 150);
        add(members);
        selectedMembers.setBounds(490, 30, 300, 150);
        add(selectedMembers);
        copy.setBounds(380, 50, 100, 30);
        add(copy);
        remove.setBounds(380, 130, 100, 30);
        add(remove);
        smsAll.setBounds(200, 200, 250, 30);
        add(smsAll);
        message.setBounds(150, 260, 400, 150);
        add(message);
        cancel.setBounds(100, 450, 150, 30);
        add(cancel);
        send.setBounds(480, 450, 150, 30);
        add(send);
        message.setBorder(new TitledBorder("Type Messsage Here"));
        try {
            String sql2 = "Select fullname from Pta_Table order by fullname";
            ps = con.prepareStatement(sql2);
            rs = ps.executeQuery();
            while (rs.next()) {
                model.addElement(rs.getString("fullname"));
            }
        } catch (SQLException e) {
        }
        setVisible(true);
        copy.addActionListener(this);
        remove.addActionListener(this);
        send.addActionListener(this);
        cancel.addActionListener(this);
        smsAll.addActionListener(this);


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
        } else if (obj == smsAll) {
            if (smsAll.isSelected()) {
                model2.removeAllElements();
                selectedMembers.setEnabled(false);
            } else {
                selectedMembers.setEnabled(true);
            }

        }
        if (obj == copy) {

            for (Iterator it = members.getSelectedValuesList().iterator(); it.hasNext(); ) {
                String sel = (String) it.next();
                if (!model.contains(sel)) {

                } else {
                    boolean checker = false;
                    if (model2.isEmpty()) {

                    } else {

                        for (int k = 0; k < model2.getSize(); k++) {
                            if (model2.getElementAt(k).toString().equalsIgnoreCase(sel)) {
                                checker = true;
                            }
                        }
                    }
                    if (checker == false) {
                        model2.addElement(sel);
                    }


                }
            }

        } else if (obj == remove) {

            int index = selectedMembers.getSelectedIndex();

            if (index >= 0) {
                model2.remove(index);

            }
        } else if (obj == send) {
            String phonenumber = "", gender = "", name = "";
            ArrayList messagesList = new ArrayList<Map>();
            HashMap messageData = new HashMap<String, String>();
            if (message.getText().equalsIgnoreCase("")) {
                JOptionPane.showMessageDialog(this, "System Does not allow Blank Messages");
            } else {
                if (model2.isEmpty()) {
                    if (smsAll.isSelected()) {
                        try {
                            int counter = 0;
                            String sql = "Select fullname from pta_table";
                            ps = con.prepareStatement(sql);
                            rs = ps.executeQuery();
                            while (rs.next()) {

                                name = rs.getString("fullname");
                                phonenumber = rs.getString("telephone1");


                                String phone;
                                String p = phonenumber;
                                if (p.equalsIgnoreCase("")) {

                                } else {
                                    String pf = p.replaceFirst("0", "+254");
                                    phone = pf;
                                    String prefix;
                                    messageData.put("message", message.getText());
                                    messageData.put("phone", phone);
                                    messagesList.add(messageData);

                                    counter++;


                                }


                            }
                            if (!messagesList.isEmpty())
                                MessageGateway.batchMessageQueuer(messagesList);
                            if (counter > 0) {
                                JOptionPane.showMessageDialog(this, counter + " Messages Sent To Sms System Server\n System Gathering Resources To Send Message(s)\n Kindly Ensure A Model Is Plugged In And Has PIN DISABLED");
                            }


                        } catch (HeadlessException | SQLException sq) {
                            JOptionPane.showMessageDialog(this, sq.getMessage());
                        }

                    } else {
                        JOptionPane.showMessageDialog(this, "No Target Recipients , Kindly Select From Left List Or Mark All PTA members");
                    }
                } else {
                    try {
                        int counter = 0;
                        for (int h = 0; h < model2.getSize(); ++h) {
                            String sql = "Select * from pta_table where fullname='" + model2.getElementAt(h).toString() + "'";
                            ps = con.prepareStatement(sql);
                            rs = ps.executeQuery();
                            if (rs.next()) {
                                name = rs.getString("fullname");
                                phonenumber = rs.getString("telephone1");


                            }

                            String phone;
                            String p = phonenumber;
                            if (p.equalsIgnoreCase("")) {

                            } else {
                                String pf = p.replaceFirst("0", "+254");
                                phone = pf;
                                String prefix;
                                PreparedStatement ps1;
                                messageData.put("message", message.getText());
                                messageData.put("phone", phone);
                                messagesList.add(messageData);
                                counter++;


                            }


                            if (!messagesList.isEmpty())
                                MessageGateway.batchMessageQueuer(messagesList);


                            if (counter > 0) {
                                JOptionPane.showMessageDialog(this, counter + " Messages Sent To Sms System Server\n System Gathering Resources To Send Message(s)\n Kindly Ensure A Model Is Plugged In And Has PIN DISABLED");
                            }

                        }


                    } catch (HeadlessException | SQLException sq) {
                        JOptionPane.showMessageDialog(this, sq.getMessage());
                    }
                }
            }

        }
    }

}
