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
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.BorderFactory;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextPane;
import javax.swing.Popup;
import javax.swing.PopupFactory;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;

import net.miginfocom.swing.MigLayout;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * @author FRED_ADMIN
 */
public class SmsOutBox_1 extends JFrame implements ActionListener {

    private int height = 500;
    private int width = 750;
    private JTable tab, tab2;
    private JProgressBar jp;
    private JScrollPane pane, pane2;
    private FredButton retry, delete, edit, cancel;
    private DefaultTableModel model, model2;
    private Connection con;
    private PreparedStatement ps;
    private ResultSet rs;
    private JTextPane textarea;

    String[] cols = {"NAME", "PHONE NUMBER", "SMS CONTENT", "DATE", "SMSID"};
    DbConnection db = new DbConnection();

    @SuppressWarnings({"OverridableMethodCallInConstructor", "LeakingThisInConstructor"})
    public SmsOutBox_1() {
        setSize(width, height);
        setTitle("SMS Outbox");
        ((JComponent) getContentPane()).setBorder(
                BorderFactory.createMatteBorder(5, 5, 5, 5, Color.MAGENTA));

        this.setIconImage(FrameProperties.icon());

        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        this.addWindowListener(new WindowAdapter() {

            public void windowClosing(WindowEvent e) {
                CurrentFrame.currentWindow();
                e.getWindow().dispose();
            }
        });
        setLayout(new MigLayout());
        setLocationRelativeTo(null);
        getContentPane().setBackground(Color.cyan);
        tab = new JTable();
        model2 = new DefaultTableModel();
        tab2 = new JTable();
        delete = new FredButton("Delete");
        retry = new FredButton("Retry");
        edit = new FredButton("Edit");
        cancel = new FredButton("cancel");
        pane = new JScrollPane(tab);
        jp = new JProgressBar();
        textarea = new JTextPane();
        jp.setMinimum(0);
        model = new DefaultTableModel() {
            public boolean isCellEditable(int row, int column) {
                return true;
            }
        };
        textarea.setBorder(new TitledBorder("SMS Content"));
        tab.setModel(model);
        tab2.setModel(model2);
        model2.setColumnIdentifiers(cols);
        model.setColumnIdentifiers(cols);

        pane2 = new JScrollPane(tab2);
        add(pane, "span,grow,push,wrap");
        add(textarea, "span,grow,push,wrap");
        add(retry, "pushx,growx");

        add(edit, "pushx,growx");

        add(delete, "pushx,growx");

        add(cancel, "pushx,growx");

        add(jp, "pushx,growx");
        jp.setBackground(Color.PINK);
        retry.addActionListener(this);
        delete.addActionListener(this);
        edit.addActionListener(this);
        cancel.addActionListener(this);
        try {
            String phone = null;
            String combined = null;
            String querry1 = "Select * from smsrecord where status='" + "Failed" + "'  ORDER BY  `smsrecord`.`Date` DESC ";
            con = db.connectDb();
            ps = con.prepareStatement(querry1);
            rs = ps.executeQuery();
            while (rs.next()) {
                String date = rs.getString("date");
                String message = rs.getString("SMScontent");
                String membercode = rs.getString("MemberId");
                String id = rs.getString("SmsId");
                String cont = "0" + rs.getString("Phone").substring(4);


                String querr = "select firstname,middlename,lastname,Telephone1 from admission where Telephone1='" + cont + "'";
                DbConnection one = new DbConnection();

                ResultSet rs = null;
                ps = con.prepareStatement(querr);
                rs = ps.executeQuery();
                if (rs.next()) {
                    String fname = rs.getString("firstname");
                    String mname = rs.getString("middlename");
                    String lname = rs.getString("lastname");
                    combined = fname + "   " + mname + "   " + lname;
                    phone = rs.getString("Telephone1");

                } else {
                    combined = "UNKNOWN";

                }

                Object row[] = {combined, cont, message, date, id};
                model.addRow(row);

            }
            tab.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    if (tab.getSelectedRowCount() > 0) {
                        int row = tab.getSelectedRow();
                        String mess = model.getValueAt(row, 2).toString();
                        textarea.setText(mess);
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
                            textarea.setText(mess);
                        }

                    }
                    if (e.getKeyCode() == KeyEvent.VK_DELETE) {
                        if (tab.getSelectedRowCount() > 0) {
                            delete.doClick();
                        }

                    }
                }

            });

        } catch (Exception sq) {
            JOptionPane.showMessageDialog(this, sq, "Error Occurred", JOptionPane.ERROR_MESSAGE);
        }

        setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {
        Object obj = e.getSource();
        ArrayList messagesList = new ArrayList<Map>();
        HashMap messageData = new HashMap<String, String>();
        int[] selected = tab.getSelectedRows();
        if (obj == retry) {

            if (selected.length > 0) {
                Object r[] = new Object[5];
                int messageCounter = 0;
                for (int y = 0; y < selected.length; ++y) {
                    r[0] = model.getValueAt(selected[y], 0);
                    r[1] = model.getValueAt(selected[y], 1);
                    r[2] = model.getValueAt(selected[y], 2);
                    r[3] = model.getValueAt(selected[y], 3);
                    r[4] = model.getValueAt(selected[y], 4);
                    model2.addRow(r);

                }
                String responce = "";
                for (int k = 0; k < selected.length; ++k) {

                    try {


                        String Smscode = model2.getValueAt(k, 4).toString();
                        String phone = model2.getValueAt(k, 1).toString();
                        String mesage = model2.getValueAt(k, 2).toString();
                        messageData.put("message", mesage);
                        messageData.put("phone", phone);
                        messagesList.add(messageData);
                        //   responce=   MessageGateway.silentOneForeignMessageQueue(phone, mesage);
                        String user;
                        if (Globals.CurrentUser.equals("null")) {
                            user = "Superadmin";

                        } else {
                            user = Globals.CurrentUser;
                        }
                        boolean testa = false;
                        SimpleDateFormat dateformat;
                        dateformat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                        Date date = new Date();
                        String da = dateformat.format(date);

                        ps = con.prepareStatement("Delete from smsrecord where smsid='" + Smscode + "'");
                        ps.execute();


                        model.removeRow(k);


                    } catch (Exception sq) {
                        sq.printStackTrace();
                        JOptionPane.showMessageDialog(this, sq, "Error Occurred", JOptionPane.ERROR_MESSAGE);
                    }

                }
                responce = MessageGateway.batchMessageQueuer(messagesList);

                if (!responce.equalsIgnoreCase("sent")) {
                    JOptionPane.showMessageDialog(rootPane, messageCounter + "   messages Failed", "Error In Sending", JOptionPane.ERROR_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(rootPane, "   All Messages Sent", "Success", JOptionPane.INFORMATION_MESSAGE);
                }

            } else {

            }
            JOptionPane.getRootFrame().dispose();
        } else if (obj == cancel) {
            CurrentFrame.currentWindow();
            dispose();
        } else if (obj == delete) {

            if (RightsAnnouncer.DeleteRights()) {
                if (tab.getSelectedRowCount() > 0) {

                    int deleteCounter = 0;
                    Object r[] = new Object[5];
                    for (int y = 0; y < selected.length; ++y) {
                        r[0] = model.getValueAt(selected[y], 0);
                        r[1] = model.getValueAt(selected[y], 1);
                        r[2] = model.getValueAt(selected[y], 2);
                        r[3] = model.getValueAt(selected[y], 3);
                        r[4] = model.getValueAt(selected[y], 4);
                        model2.addRow(r);

                    }
                    jp.setMaximum(selected.length);

                    for (int s = 0; s < model2.getRowCount(); ++s) {
                        jp.setValue(s);

                        String Smscode = model2.getValueAt(s, 4).toString();
                        try {
                            String querry = "Delete from  smsrecord where smsid='" + Smscode + "'";
                            ps = con.prepareStatement(querry);
                            ps.execute();

                            for (int z = 0; z < tab.getRowCount(); ++z) {
                                if (model.getValueAt(z, 4).toString().equals(Smscode)) {
                                    model.removeRow(z);

                                    deleteCounter++;

                                }

                            }

                        } catch (Exception q) {
                            JOptionPane.showMessageDialog(this, q, "Error Occurred", JOptionPane.ERROR_MESSAGE);
                        }

                    }

                    JOptionPane.showMessageDialog(this, deleteCounter + "   Messages Deleted", "Deleted", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this, "Select messages To Delete", "Failed", JOptionPane.ERROR_MESSAGE);
                }
                for (int z = 0; z < model2.getRowCount(); ++z) {
                    model2.removeRow(z);
                }
            } else {
                JOptionPane.showMessageDialog(this, "You Do Not Have Sufficient Rights To perform This Operation\n Consult System Administrator");
            }

        } else if (obj == edit) {
            Popup popup;
            JTextPane r = new JTextPane();
            PopupFactory pop = PopupFactory.getSharedInstance();
            popup = pop.getPopup(null, r, 0, 0);

        }

    }

    public static void main(String[] s) {
        new SmsOutBox_1();

    }

}
