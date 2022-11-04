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
 * @author FRED
 */
public class MeanGradeConfiguration extends JFrame implements ActionListener {

    private Connection con = DbConnection.connectDb();
    private int width = 750;
    private int height = 550;
    private PreparedStatement ps;
    private ResultSet rs;
    private FredCombo jclass = new FredCombo("Select Class");
    private DefaultTableModel model = new DefaultTableModel();
    private Object cols[] = {"Class", "Ref No.", "Mean Grade", "Min Value", "Max Value"};
    private Object row[] = new Object[cols.length];
    private JTable tab = new JTable();
    private JScrollPane pane = new JScrollPane(tab);
    private FredLabel name = new FredLabel("Class");
    private FredButton increment = new FredButton("Increament");
    private FredTextField value = new FredTextField("Use -ve Value To lower Grading System");

    public MeanGradeConfiguration() {
        model.setColumnIdentifiers(cols);
        tab.setModel(model);

        setSize(width, height);
        setTitle("Grading System Setting Panel");
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
        name.setBounds(30, 30, 150, 30);
        add(name);
        jclass.setBounds(130, 30, 200, 30);
        add(jclass);
        value.setBounds(360, 30, 230, 30);
        add(value);
        increment.setBounds(600, 30, 100, 30);
        add(increment);
        pane.setBounds(30, 100, 700, 400);
        add(pane);
        try {


            String sql = "select * from classes  where precision1<5 order by precision1";
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                jclass.addItem(rs.getString("ClassName"));

            }

            String sql2 = "Select * from meangrade_table";
            ps = con.prepareStatement(sql2);
            rs = ps.executeQuery();
            while (rs.next()) {
                row[0] = Globals.className(rs.getString("Classcode"));
                row[1] = rs.getString("Sort_code");
                row[2] = rs.getString("grade");
                row[3] = rs.getString("Start_from");
                row[4] = rs.getString("end_at");
                model.addRow(row);
            }


        } catch (Exception sq) {
            sq.printStackTrace();
            JOptionPane.showMessageDialog(null, sq.getMessage());
        }
        value.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();

                if (Character.isAlphabetic(c) || value.getText().length() > 2) {
                    getToolkit().beep();
                    e.consume();
                }

            }

        });
        jclass.addActionListener(this);
        increment.addActionListener(this);
        setVisible(true);
    }

    public static void main(String[] args) {
        new MeanGradeConfiguration();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object obj = e.getSource();
        if (obj == jclass) {
            if (jclass.getSelectedIndex() == 0) {

            } else {
                while (model.getRowCount() > 0) {
                    model.removeRow(0);
                }
                try {
                    String sql2 = "Select * from meangrade_table where classcode='" + Globals.classCode(jclass.getSelectedItem().toString()) + "'  order by Sort_code asc";
                    ps = con.prepareStatement(sql2);
                    rs = ps.executeQuery();
                    while (rs.next()) {
                        row[0] = Globals.className(rs.getString("Classcode"));
                        row[1] = rs.getString("Sort_code");
                        row[2] = rs.getString("grade");
                        row[3] = rs.getString("Start_from");
                        row[4] = rs.getString("end_at");
                        model.addRow(row);
                    }
                } catch (Exception sq) {
                    sq.printStackTrace();
                    JOptionPane.showMessageDialog(null, sq.getMessage());
                }
            }
        } else if (obj == increment) {
            if (jclass.getSelectedIndex() == 0) {
                JOptionPane.showMessageDialog(this, "Select The Class To Decreament Or Increament Its Grading System");
            } else {
                if (value.getText().isEmpty() || value.getText().length() > 2) {
                    JOptionPane.showMessageDialog(this, "Invalid Increament/Decreament Value");
                } else {
                    try {
                        String classcode = Globals.classCode(jclass.getSelectedItem().toString());
                        int increametvalue = Integer.parseInt(value.getText());
                        String sql = "Select * from meangrade_table where classcode='" + classcode + "'";
                        ps = con.prepareStatement(sql);
                        rs = ps.executeQuery();
                        while (rs.next()) {
                            String sortcode = rs.getString("Sort_code");
                            int min = rs.getInt("Start_from");
                            int max = rs.getInt("End_at");
                            min += increametvalue;
                            max += increametvalue;
                            if (max > 100) {
                                max = 100;
                            }
                            if (min < 0) {
                                min = 0;
                            }
                            if (sortcode.equalsIgnoreCase("1")) {
                                max = 100;
                            }
                            if (sortcode.equalsIgnoreCase("12")) {
                                min = 0;
                            }
                            String sql2 = "Update meangrade_table set start_from='" + min + "',end_at='" + max + "' where classcode='" + classcode + "' and sort_code='" + sortcode + "'";
                            ps = con.prepareStatement(sql2);
                            ps.execute();

                        }
                        while (model.getRowCount() > 0) {
                            model.removeRow(0);
                        }
                        String sql2 = "Select * from meangrade_table where classcode='" + Globals.classCode(jclass.getSelectedItem().toString()) + "'";
                        ps = con.prepareStatement(sql2);
                        rs = ps.executeQuery();
                        while (rs.next()) {
                            row[0] = Globals.className(rs.getString("Classcode"));
                            row[1] = rs.getString("Sort_code");
                            row[2] = rs.getString("grade");
                            row[3] = rs.getString("Start_from");
                            row[4] = rs.getString("end_at");
                            model.addRow(row);
                        }

                    } catch (SQLException sq) {
                        sq.printStackTrace();
                        JOptionPane.showMessageDialog(null, sq.getMessage());

                    }
                }
            }

        }
    }


}
