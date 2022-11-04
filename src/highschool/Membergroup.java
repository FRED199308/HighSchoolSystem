/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package highschool;

import java.awt.BorderLayout;
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
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;


import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

/**
 * @author FRED_ADMIN
 */
public class Membergroup extends JFrame implements ActionListener {

    private FredButton cancel, refresh;
    private JTextField jsearch;
    private JLabel search;
    DefaultTableModel mode;
    static PreparedStatement ps;
    ResultSet rs;
    Connection con;

    @SuppressWarnings("OverridableMethodCallInConstructor")
    public Membergroup() {

        setTitle("Hold Ctrl key And Select The Members To Add Or Drag Across The Members List");
        setSize(800, 600);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        this.setIconImage(FrameProperties.icon());
        ((JComponent) getContentPane()).setBorder(
                BorderFactory.createMatteBorder(5, 5, 5, 5, Color.MAGENTA));
        this.addWindowListener(new WindowAdapter() {

            public void windowClosing(WindowEvent e) {
                CurrentFrame.currentWindow();
                e.getWindow().dispose();
            }
        });
        setLocationRelativeTo(null);
        getContentPane().setBackground(Color.cyan);
        setResizable(false);
        setLayout(null);
        JPanel pane;
        JTable tab, tab2;

        FredButton insert;
        FredCombo group;

        ResultSetMetaData meta;
        mode = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }

        };

        DefaultTableModel mode2 = new DefaultTableModel();
        DbConnection db = new DbConnection();
        int rowcount = 0;
        String[] cols = {};
        int h = 1;
        tab2 = new JTable();
        jsearch = new JTextField();
        search = new JLabel("Search Student by AdmissionNumber Or Names");
        refresh = new FredButton("Refresh");
        cancel = new FredButton("Cancel");

        pane = new JPanel();
        insert = new FredButton("ADD SELECTED");
        String[] option = {"Select Group"};
        group = new FredCombo(option[0]);
        group.setToolTipText("select the group to enable the button");
        tab = new JTable();
        JScrollPane one = new JScrollPane(tab);
        mode.setColumnIdentifiers(cols);
        mode2.setColumnIdentifiers(cols);
        tab2.setModel(mode2);
        tab.setModel(mode);
        tab.setRowHeight(27);
        tab.setForeground(Color.MAGENTA);

        try {

            con = db.connectDb();
            String querry3 = "Select * from groups";
            ps = con.prepareStatement(querry3);
            rs = ps.executeQuery();
            while (rs.next()) {
                String groupname = rs.getString("groupname");
                group.addItem(groupname);
            }
            if (group.getSelectedItem().toString().equals("Select Group")) {
                insert.setEnabled(false);
            }
            group.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent y) {
                    if (group.getSelectedItem().toString().equals("Select Group")) {
                        insert.setEnabled(false);
                    } else {
                        insert.setEnabled(true);
                    }
                }

            });

            String querry = "select firstname,middlename,lastname,gender,AdmissionNumber from admission ORDER BY  `admission`.`FirstName` ASC";
            ps = con.prepareStatement(querry);
            rs = ps.executeQuery();
            meta = rs.getMetaData();
            for (int i = 1; i <= meta.getColumnCount(); ++i) {
                mode.addColumn(meta.getColumnName(i).toUpperCase());
                mode2.addColumn(meta.getColumnName(i));
                rowcount++;
            }

            Object[] rows = new Object[rowcount + 1];

            while (rs.next()) {
                for (h = 1; h <= meta.getColumnCount(); ++h) {

                    rows[h - 1] = rs.getString(meta.getColumnName(h));

                }

                mode.addRow(rows);

            }

            insert.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    boolean success = false;
                    int counter = 0;
                    int ro = tab.getSelectedRowCount();
                    int[] selectedRows = tab.getSelectedRows();
                    Object[] rows = new Object[5];
                    if (selectedRows.length > 0) {
                        for (int w = 0; w < selectedRows.length; ++w) {
                            rows[0] = mode.getValueAt(selectedRows[w], 0);
                            rows[1] = mode.getValueAt(selectedRows[w], 1);
                            rows[2] = mode.getValueAt(selectedRows[w], 2);
                            rows[3] = mode.getValueAt(selectedRows[w], 3);
                            rows[4] = mode.getValueAt(selectedRows[w], 4);
                            mode2.addRow(rows);
                        }

                        String selectedGroup = group.getSelectedItem().toString();
                        String groupCode = null;
                        try {
                            PreparedStatement p;

                            String querry4 = "select groupcode from groups where groupname='" + selectedGroup + "'";
                            p = con.prepareStatement(querry4);
                            ResultSet rs = p.executeQuery();
                            while (rs.next()) {
                                groupCode = rs.getString("groupCode");
                            }
                            int k = 0;
                            for (int i = selectedRows.length - 1; i >= 0; --i) {

                                String integrity = "Select AdmissionNumber,groupcode from groupmembers where AdmissionNumber='" + mode2.getValueAt(i, 4) + "' and groupcode='" + groupCode + "'";
                                p = con.prepareStatement(integrity);
                                rs = p.executeQuery();
                                if (rs.next()) {
                                    getToolkit().beep();
                                    JOptionPane.showMessageDialog(null, tab2.getValueAt(i, 0) + "     :is already registered with the group", "Duplicate prevention", JOptionPane.WARNING_MESSAGE);

                                    mode2.removeRow(i);

                                } else {
                                    String querry1 = "Insert into groupmembers values('" + tab2.getValueAt(i, 4) + "','" + groupCode + "')";
                                    p = con.prepareStatement(querry);
                                    p.execute();
                                    success = true;
                                    counter++;
                                    mode2.removeRow(i);

                                }

                            }

                            if (success == true) {
                                JOptionPane.showMessageDialog(null, counter + " Student(s) added successfully To " + selectedGroup, "success", JOptionPane.INFORMATION_MESSAGE);
                            }
                        } catch (Exception d) {

                            getToolkit().beep();
                            JOptionPane.showMessageDialog(null, d, "Consult Developer", JOptionPane.WARNING_MESSAGE);
                        }
                    } else {
                        getToolkit().beep();
                        JOptionPane.showMessageDialog(null, "No member selected", "selection error", JOptionPane.WARNING_MESSAGE);
                    }

                }
            });

        } catch (Exception sq) {

            JOptionPane.showMessageDialog(this, sq, "consult Admin", JOptionPane.WARNING_MESSAGE);
        }
        cancel.addActionListener(this);
        refresh.addActionListener(this);
        pane.setBackground(Color.cyan);
        pane.setLayout(new BorderLayout());
        pane.setLayout(null);
        search.setBounds(30, 10, 200, 30);
        pane.add(search);
        jsearch.setBounds(300, 10, 250, 30);
        pane.add(jsearch);
        one.setBounds(30, 40, 700, 450);
        insert.setBounds(50, 510, 130, 35);
        group.setBounds(230, 510, 100, 35);
        refresh.setBounds(350, 510, 100, 35);
        cancel.setBounds(500, 510, 100, 35);
        pane.add(one);
        pane.add(insert);
        pane.add(group);
        pane.add(cancel);
        pane.add(refresh);
        pane.setBounds(10, 10, 750, 550);
        add(pane);
        jsearch.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
                String typed = jsearch.getText();
                Object addedrow[] = new Object[tab.getColumnCount()];
                String querry = "Select firstname,lastname,gender,middlename,AdmissionNumber from admission where AdmissionNumber like '" + typed + "%' or firstname like '" + typed + "%' or middlename like '" + typed + "%' or lastname like '" + typed + "%' or upi like '" + typed + "%'  ORDER BY  `admission`.`FirstName` ASC";
                try {
                    ps = con.prepareStatement(querry);
                    rs = ps.executeQuery();
                    int counter = 0;
                    while (rs.next()) {
                        if (counter == 0) {
                            while (mode.getRowCount() > 0) {
                                mode.removeRow(0);
                            }
                        }
                        addedrow[0] = rs.getString("firstname");
                        addedrow[1] = rs.getString("middlename");
                        addedrow[2] = rs.getString("lastname");
                        addedrow[3] = rs.getString("gender");
                        addedrow[4] = rs.getString("AdmissionNumber");
                        mode.addRow(addedrow);
                        counter++;
                    }
                } catch (SQLException ex) {
                    Logger.getLogger(Membergroup.class.getName()).log(Level.SEVERE, null, ex);
                }

            }

        });

        setVisible(true);

    }

    public void actionPerformed(ActionEvent d) {

        Object obj = d.getSource();
        if (obj == cancel) {
            CurrentFrame.currentWindow();
            dispose();

        }
        if (obj == refresh) {

        }

    }

    public static void main(String[] args) {
        new Membergroup();
    }

}
