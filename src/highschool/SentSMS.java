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
import javax.swing.BorderFactory;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextPane;

import static javax.swing.WindowConstants.DISPOSE_ON_CLOSE;

import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;

import net.miginfocom.swing.MigLayout;

/**
 * @author FRED_ADMIN
 */
public class SentSMS extends JFrame implements ActionListener {

    private int height = 500;
    private int width = 750;
    private JTable tab, tab2;
    private JScrollPane pane, pane2;
    private FredButton delete, cancel;
    private DefaultTableModel model, model2;
    private Connection con;
    private PreparedStatement ps;
    private ResultSet rs;
    private JTextPane textarea;
    String[] cols = {"NAME", "PHONE NUMBER", "SMS CONTENT", "DATE", "SMSID"};
    DbConnection db = new DbConnection();

    @SuppressWarnings({"OverridableMethodCallInConstructor", "LeakingThisInConstructor", "static-access"})
    public SentSMS() {
        setSize(width, height);
        this.setIconImage(FrameProperties.icon());
        ((JComponent) getContentPane()).setBorder(
                BorderFactory.createMatteBorder(5, 5, 5, 5, Color.MAGENTA));
        setTitle("SENT  SMS");

        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
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
        textarea = new JTextPane();
        textarea.setBorder(new TitledBorder("SMS content"));
        cancel = new FredButton("cancel");
        pane = new JScrollPane(tab);
        model = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return true;
            }
        };
        tab.setModel(model);
        tab2.setModel(model2);
        model2.setColumnIdentifiers(cols);
        model.setColumnIdentifiers(cols);
        pane2 = new JScrollPane(tab2);
        add(pane, "push,grow,span");
        add(textarea, "push,grow,span");
        add(delete, "Split,gapleft 100");
        add(cancel, "gapleft 150");
        delete.addActionListener(this);
        cancel.addActionListener(this);
        try {
            String phone = null;
            String combined = null;
            String querry1 = "Select * from smsrecord where status='" + "sent" + "'  ORDER BY  `smsrecord`.`Date` DESC  limit 1000";
            con = db.connectDb();
            ps = con.prepareStatement(querry1);
            rs = ps.executeQuery();

            while (rs.next()) {
                String date = rs.getString("date");
                String message = rs.getString("SMScontent");

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

        } catch (Exception sq) {
            sq.printStackTrace();
            JOptionPane.showMessageDialog(this, sq, "Database Error", JOptionPane.ERROR_MESSAGE);
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

        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object obj = e.getSource();
        int[] selected = tab.getSelectedRows();
        if (obj == delete) {
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

                for (int s = 0; s < selected.length; ++s) {
                    String Smscode = model2.getValueAt(s, 4).toString();
                    try {
                        String querry = "Delete from  smsrecord where smsid='" + Smscode + "'";
                        ps = con.prepareStatement(querry);
                        ps.execute();

                        for (int z = 0; z < tab.getRowCount(); ++z) {
                            if (model.getValueAt(z, 4).toString().equals(Smscode)) {
                                model.removeRow(z);

                                deleteCounter++;
                                break;
                            }
                        }

                    } catch (Exception q) {
                        q.printStackTrace();
                    }

                }

                JOptionPane.showMessageDialog(this, deleteCounter + "   Messages Deleted", "Deleted", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Select messages To Delete", "Failed", JOptionPane.ERROR_MESSAGE);
            }
            for (int z = 0; z < model2.getRowCount(); ++z) {
                model2.removeRow(z);
            }

        } else if (obj == cancel) {
            CurrentFrame.currentWindow();
            dispose();
        }

    }

    public static void main(String[] d) {
        new SentSMS();
    }

}
