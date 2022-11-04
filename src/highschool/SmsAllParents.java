/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package highschool;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.*;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

//import java.util.HashMap;

/**
 * @author FRED_ADMIN
 */
public class SmsAllParents extends JFrame implements ActionListener {

    public static void main(String[] r) {
        new SmsAllParents();
    }

    private int width = 750;
    private int height = 500;
    private JTextPane message;

    private FredLabel lab;
    private JButton send, cancel;
    private Connection con;
    private PreparedStatement ps;
    private ResultSet rs;
    private IdGenerator key = new IdGenerator();
    private DbConnection db = new DbConnection();
    private JProgressBar jp;
    private FredCombo options = new FredCombo("All Parents");
    private Font font;
    private FredLabel meslimit = new FredLabel("Character Counter");

    public SmsAllParents() {
        setSize(width, height);
        setTitle("Sms Panel");
        setLayout(null);
        getContentPane().setBackground(Color.cyan);
        setLocationRelativeTo(null);
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
        setResizable(false);
        send = new JButton("send");
        cancel = new JButton("cancel");
        jp = new JProgressBar();
        jp.setMinimum(0);
        jp.setMaximum(100);
        jp.setBackground(Color.red);
        this.setIconImage(FrameProperties.icon());
        ((JComponent) getContentPane()).setBorder(
                BorderFactory.createMatteBorder(5, 5, 5, 5, Color.MAGENTA));

        font = new Font("Serrif", Font.BOLD, (15));

        message = new JTextPane();

        send.addActionListener(this);
        cancel.addActionListener(this);

        message.setToolTipText("Type Message Here");
        lab = new FredLabel("Select Recipients");
        lab.setForeground(Color.red);
        lab.setFont(font);
        lab.setBounds(150, 20, 300, 40);
        add(lab);
        options.setBounds(150, 100, 200, 30);
        add(options);
        meslimit.setBounds(80, 150, 150, 40);
        add(meslimit);
        message.setBounds(80, 200, 550, 200);
        add(message);
        send.setBounds(180, 420, 100, 30);
        add(send);
        cancel.setBounds(400, 420, 100, 30);
        add(cancel);
        message.setBorder(new TitledBorder("Type Message Here"));
        try {
            String sql = "Select * from classes  where  precision1<10 order by precision1 asc";
            con = DbConnection.connectDb();
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                options.addItem(rs.getString("ClassName"));

            }
        } catch (SQLException e) {
        }
        message.addKeyListener(new KeyAdapter() {

            public void keyReleased(KeyEvent key) {
                String messagePart = "";
                int counter = message.getText().length();
                if (counter < 161) {
                    messagePart = "1";
                    meslimit.setText(counter + "(" + messagePart + ")");
                } else if (counter > 160 && counter < 321) {
                    messagePart = "2";
                    meslimit.setText(counter + "(" + messagePart + ")");
                } else if (counter > 321 && counter < 481) {
                    messagePart = "3";
                    meslimit.setText(counter + "(" + messagePart + ")");
                } else if (counter > 321 && counter < 481) {
                    messagePart = "4";
                    meslimit.setText(counter + "(" + messagePart + ")");
                } else if (counter > 481 && counter < 641) {
                    messagePart = "5";
                    meslimit.setText(counter + "(" + messagePart + ")");
                } else if (counter > 641 && counter < 801) {
                    messagePart = "6";
                    meslimit.setText(counter + "(" + messagePart + ")");
                } else {
                    meslimit.setText(String.valueOf(counter));
                }
                if (counter == 0) {
                    meslimit.setText("Character Counter");
                }
            }
        });

        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object obj = e.getSource();
        Socket sock = new Socket();
        InetSocketAddress adr = new InetSocketAddress("www.google.com/", 80);
        ArrayList messagesList = new ArrayList<Map>();
        HashMap messageData = new HashMap<String, String>();

        String MESSAGE = message.getText();


        if (obj == send) {

            if (options.getSelectedIndex() == 0) {


                String combined = null;

                int option = JOptionPane.showConfirmDialog(rootPane, "You are about to send the message to all Parents\n Are You sure you want to send this message", "confirm", JOptionPane.YES_NO_OPTION);


                if (option == JOptionPane.YES_OPTION) {


                    try {

                        String querr = "select parentfullnames,telephone1,admissionnumber  from admission where currentform like '" + "FM" + "%'";
                        con = DbConnection.connectDb();
                        ps = con.prepareStatement(querr);
                        rs = ps.executeQuery();
                        ResultSetMetaData meta = rs.getMetaData();


                        while (rs.next()) {

                            boolean testa = false;

                            String p = rs.getString("telephone1");


                            if (p.equalsIgnoreCase("")) {

                            } else {


                                p = "254" + p.substring(1);
                                messageData = new HashMap<String, String>();
                                messageData.put("message", MESSAGE);


                                messageData.put("phone", p);
                                messagesList.add(messageData);
                            }


                        }
                        if (!messagesList.isEmpty()) {
                            MessageGateway.batchMessageQueuer(messagesList);
                        }


                    } catch (Exception sq) {
                        sq.printStackTrace();
                        JOptionPane.showMessageDialog(this, sq.getMessage());
                    }


                }
                if (option == JOptionPane.NO_OPTION) {
                    JOptionPane.showMessageDialog(rootPane, "operation cancelled");
                }
            } else {
                try {
                    String classcode = Globals.classCode(options.getSelectedItem().toString());

                    String querr = "select parentfullnames,telephone1,admissionnumber  from admission where currentform='" + classcode + "'";
                    con = DbConnection.connectDb();
                    ps = con.prepareStatement(querr);
                    rs = ps.executeQuery();
                    ResultSetMetaData meta = rs.getMetaData();


                    while (rs.next()) {

                        boolean testa = false;

                        String p = rs.getString("telephone1");


                        if (p.equalsIgnoreCase("")) {

                        } else {
                            p = "254" + p.substring(1);
                            messageData = new HashMap<String, String>();
                            messageData.put("message", MESSAGE);

                            messageData.put("phone", p);
                            messagesList.add(messageData);
                        }


                    }
                    if (!messagesList.isEmpty()) {
                        MessageGateway.batchMessageQueuer(messagesList);
                    }


                } catch (HeadlessException | SQLException sq) {
                    JOptionPane.showMessageDialog(this, sq.getMessage());
                }


            }

        } else if (obj == cancel) {
            try {
                con.close();
            } catch (SQLException sq) {

            }
            CurrentFrame.currentWindow();
            dispose();
        }

    }


}
