/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package highschool;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
 * @author FRED
 */
public class ExamWeightRegistration extends JFrame implements ActionListener {
    private Connection con = DbConnection.connectDb();
    private int width = 750;
    private int height = 550;
    private PreparedStatement ps;
    private ResultSet rs;
    private FredCombo value = new FredCombo("Select Weight");
    private FredCombo jclass = new FredCombo("Select Class");
    private DefaultTableModel model = new DefaultTableModel();
    private Object cols[] = {"Exam Name", "Exam Ref No.", "Class", "Exam Weight"};
    private Object row[] = new Object[cols.length];
    private JTable tab = new JTable();
    private JScrollPane pane = new JScrollPane(tab);
    private FredLabel name = new FredLabel("Class");
    private FredLabel exname = new FredLabel("Exam Name");
    private FredCombo jexame = new FredCombo("Select Exam");
    private FredButton save = new FredButton("Save");


    public ExamWeightRegistration() {
        model.setColumnIdentifiers(cols);
        tab.setModel(model);

        setSize(width, height);
        setTitle("Exam Weight Setting Panel");
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
        value.setBounds(500, 30, 230, 30);
        add(value);
        exname.setBounds(30, 100, 150, 30);
        add(exname);
        jexame.setBounds(130, 100, 200, 30);
        add(jexame);
        save.setBounds(500, 100, 130, 30);
        add(save);
        pane.setBounds(30, 150, 700, 350);
        add(pane);
        try {
            String sqls = "Select examname from exams group by examname";
            ps = con.prepareStatement(sqls);
            rs = ps.executeQuery();
            while (rs.next()) {
                jexame.addItem(rs.getString("ExamName"));
            }
            String sql = "select * from classes  where precision1<5 order by precision1";
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                jclass.addItem(rs.getString("ClassName"));

            }
            String querry = "Select examname,examweights.examcode,weight,classcode from exams,examweights where examweights.examcode=exams.examcode";
            ps = con.prepareStatement(querry);
            rs = ps.executeQuery();
            while (rs.next()) {
                row[0] = rs.getString("ExamName");
                row[1] = rs.getString("Examcode");
                row[2] = Globals.className(rs.getString("Classcode"));
                row[3] = rs.getString("Weight");
                model.addRow(row);
            }
            int k = 5;
            for (; ; ) {
                if (k <= 100) {
                    value.addItem(k);
                    k += 5;
                } else {
                    break;
                }
            }


        } catch (Exception sq) {
            sq.printStackTrace();
            JOptionPane.showMessageDialog(null, sq.getMessage());
        }
        jclass.addActionListener(this);
        jexame.addActionListener(this);
        save.addActionListener(this);

        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object obj = e.getSource();
        if (obj == jclass) {
            String classcode = Globals.classCode(jclass.getSelectedItem().toString());
            while (model.getRowCount() > 0) {
                model.removeRow(0);
            }
            try {
                String querry = "Select examname,examweights.examcode,weight,classcode from exams,examweights where examweights.examcode=exams.examcode and classcode='" + classcode + "' group by examname";
                ps = con.prepareStatement(querry);
                rs = ps.executeQuery();
                while (rs.next()) {
                    row[0] = rs.getString("ExamName");
                    row[1] = rs.getString("Examcode");
                    row[2] = Globals.className(rs.getString("Classcode"));
                    row[3] = rs.getString("Weight");
                    model.addRow(row);
                }
            } catch (Exception sq) {
                sq.printStackTrace();
                JOptionPane.showMessageDialog(null, sq.getMessage());
            }
        } else if (obj == jexame) {
            if (jexame.getSelectedIndex() > 0) {
                if (jclass.getSelectedIndex() > 0) {
                    String classcode = Globals.classCode(jclass.getSelectedItem().toString());
                    while (model.getRowCount() > 0) {
                        model.removeRow(0);
                    }

                    try {
                        String querry = "Select examname,examweights.examcode,weight,classcode from exams,examweights where examweights.examcode=exams.examcode and classcode='" + classcode + "' and examname='" + jexame.getSelectedItem() + "' group by examname";
                        ps = con.prepareStatement(querry);
                        rs = ps.executeQuery();
                        while (rs.next()) {
                            row[0] = rs.getString("ExamName");
                            row[1] = rs.getString("Examcode");
                            row[2] = Globals.className(rs.getString("Classcode"));
                            row[3] = rs.getString("Weight");
                            model.addRow(row);
                        }
                    } catch (Exception sq) {
                        sq.printStackTrace();
                        JOptionPane.showMessageDialog(null, sq.getMessage());
                    }
                }
            }
        } else if (obj == save) {
            if (jclass.getSelectedIndex() == 0) {
                JOptionPane.showMessageDialog(this, "Select Class");
            } else {
                if (jexame.getSelectedIndex() == 0) {
                    JOptionPane.showMessageDialog(this, "Select Exam");
                } else {
                    if (value.getSelectedIndex() == 0) {
                        JOptionPane.showMessageDialog(this, "Select The New Exam Weight For This Exam");
                    } else {
                        for (int k = 0; k < tab.getRowCount(); k++) {
                            String examcode = tab.getValueAt(k, 1).toString();

                            int p = 0;
                            try {
                                String sqq = "Select precision1 from classes where classname='" + jclass.getSelectedItem() + "'";
                                ps = con.prepareStatement(sqq);
                                rs = ps.executeQuery();
                                if (rs.next()) {
                                    p = rs.getInt("precision1");
                                }
                                String cname = "FM" + p + jexame.getSelectedItem();


                                String sql = "Update examweights set weight='" + value.getSelectedItem() + "' where examcode like '" + cname + "%'";
                                ps = con.prepareStatement(sql);
                                ps.execute();

                                tab.setValueAt(value.getSelectedItem(), k, 3);


                            } catch (Exception sq) {
                                sq.printStackTrace();
                                JOptionPane.showMessageDialog(null, sq.getMessage());
                            }


                        }

                        JOptionPane.showMessageDialog(this, "New Weight Updated Successfully");

                    }
                }
            }


        }


    }

    public static void main(String[] args) {
        new ExamWeightRegistration();
    }
}
