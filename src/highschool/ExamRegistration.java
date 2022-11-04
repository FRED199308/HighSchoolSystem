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
public class ExamRegistration extends JFrame implements ActionListener {
    private Connection con = DbConnection.connectDb();
    private int width = 800;
    private int height = 550;
    private PreparedStatement ps;
    private ResultSet rs;
    private FredLabel name, classl;
    private FredTextField jnameA = new FredTextField();
    private FredCombo jclass = new FredCombo("Select Class");
    private FredButton save, update;
    private FredCheckBox transfer = new FredCheckBox("Transferable To Next Academic Periods");
    private DefaultTableModel model = new DefaultTableModel();
    private Object cols[] = {"Name", "Transferable"};
    private Object row[] = new Object[cols.length];
    private JTable tab = new JTable();
    private JScrollPane pane = new JScrollPane(tab);


    public ExamRegistration() {
        classl = new FredLabel("Class Taking Exam");
        name = new FredLabel("Exam Name");
        save = new FredButton("Save");
        update = new FredButton("DELETE");
        model.setColumnIdentifiers(cols);
        tab.setModel(model);

        setSize(width, height);
        setTitle("Exam Registration Panel");
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
        jnameA.setBounds(300, 30, 250, 30);
        add(jnameA);
        classl.setBounds(30, 100, 150, 30);
        add(classl);
        jclass.setBounds(300, 100, 250, 30);
        add(jclass);
        transfer.setBounds(50, 150, 300, 30);
        add(transfer);
        save.setBounds(600, 30, 130, 30);
        add(save);
        update.setBounds(600, 130, 130, 30);
        add(update);
        transfer.setSelected(true);
        pane.setBounds(30, 200, 750, 300);
        add(pane);
        jclass.addItem("All classes");
        try {
            String sql = "select * from classes  where precision1<5 order by precision1";
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                jclass.addItem(rs.getString("ClassName"));
            }
            String sql2 = "Select * from exams group by examname";
            ps = con.prepareStatement(sql2);
            rs = ps.executeQuery();
            while (rs.next()) {
                row[0] = rs.getString("ExamName");
                row[1] = rs.getString("transferable");
                model.addRow(row);
            }


        } catch (Exception e) {
        }

        save.addActionListener(this);
        update.addActionListener(this);
        setVisible(true);
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        Object obj = e.getSource();
        if (obj == save) {
            if (jnameA.getText().isEmpty() || DataValidation.number(jnameA.getText())) {
                JOptionPane.showMessageDialog(this, "Input A Valid Exam Name");
            } else {
                if (jclass.getSelectedIndex() == 0) {
                    JOptionPane.showMessageDialog(this, "Select The classes Affected By The Exam");
                } else {
                    String transfers = "";
                    if (transfer.isSelected()) {
                        transfers = "true";
                    } else {
                        transfers = "false";
                    }

                    if (jclass.getSelectedIndex() == 1) {
                        try {
                            String sql = "select * from classes  where precision1<5 order by precision1";
                            ps = con.prepareStatement(sql);
                            rs = ps.executeQuery();
                            while (rs.next()) {
                                String classname = rs.getString("ClassName");
                                String classcode = rs.getString("Classcode");
                                String examcode = ExamCodesGenerator.generatecode(classname, String.valueOf(Globals.academicYear()), Globals.currentTermName(), jnameA.getText().toUpperCase());

                                ResultSet Rs;
                                String sql2 = "Insert Into exams values('" + jnameA.getText().toUpperCase() + "','" + examcode + "','" + Globals.currentTerm() + "','" + Globals.academicYear() + "','" + transfers + "','" + classcode + "')";
                                ps = con.prepareStatement(sql2);
                                ps.execute();

                                String querry = "Insert into examweights values('" + examcode + "','" + "100" + "')";
                                ps = con.prepareStatement(querry);
                                ps.execute();


                            }
                            JOptionPane.showMessageDialog(this, "Exam Registered Successfully");
                            jnameA.setText("");
                            jclass.setSelectedIndex(0);
                            while (model.getRowCount() > 0) {
                                model.removeRow(0);
                            }
                            String sql2 = "Select * from exams group by examname";
                            ps = con.prepareStatement(sql2);
                            rs = ps.executeQuery();
                            while (rs.next()) {
                                row[0] = rs.getString("ExamName");
                                row[1] = rs.getString("Examcode");
                                model.addRow(row);
                            }
                        } catch (Exception sq) {
                            sq.printStackTrace();
                            JOptionPane.showMessageDialog(null, sq.getMessage());
                        }
                    } else {
                        try {

                            String classcode = Globals.classCode(jclass.getSelectedItem().toString());
                            String examcode = ExamCodesGenerator.generatecode(jclass.getSelectedItem().toString(), String.valueOf(Globals.academicYear()), Globals.currentTermName(), jnameA.getText().toUpperCase());

                            ResultSet Rs;
                            String sql2 = "Insert Into exams values('" + jnameA.getText().toUpperCase() + "','" + examcode + "','" + Globals.currentTerm() + "','" + Globals.academicYear() + "','" + transfers + "','" + classcode + "')";
                            ps = con.prepareStatement(sql2);
                            ps.execute();
                            String querry = "Insert into examweights values('" + examcode + "','" + "100" + "')";
                            ps = con.prepareStatement(querry);
                            ps.execute();
                            JOptionPane.showMessageDialog(this, "Exam Registered Successfully");
                            jnameA.setText("");
                            jclass.setSelectedIndex(0);
                            while (model.getRowCount() > 0) {
                                model.removeRow(0);
                            }
                            String sql23 = "Select * from exams group by examname";
                            ps = con.prepareStatement(sql23);
                            rs = ps.executeQuery();
                            while (rs.next()) {
                                row[0] = rs.getString("ExamName");
                                row[1] = rs.getString("Examcode");
                                model.addRow(row);
                            }
                        } catch (Exception sq) {
                            sq.printStackTrace();
                            JOptionPane.showMessageDialog(null, sq.getMessage());

                        }

                    }

                }
            }
        } else if (obj == update) {
            if (tab.getSelectedRowCount() < 1) {
                JOptionPane.showMessageDialog(this, "Select The Exam That Yo Want Deleted");
            } else {
                int selected = tab.getSelectedRow();
                String exam = model.getValueAt(selected, 0).toString();
                int option = JOptionPane.showConfirmDialog(this, "Are You Sure That You Want To Delete This Exam,\nAll The Data Saved Aganist The Exam Will Be Lost\n Proceed With Deletion", "Confirm Deletion", JOptionPane.YES_OPTION);
                if (option == JOptionPane.NO_OPTION) {
                    JOptionPane.showMessageDialog(this, "Delection Posponed");
                } else if (option == JOptionPane.YES_OPTION) {
                    try {
                        String sql = "Delete from exams where examname='" + exam + "'";
                        ps = con.prepareStatement(sql);

                        ps.execute();
                        model.removeRow(selected);
                        JOptionPane.showMessageDialog(this, "Exam Deleted Successfully");
                    } catch (Exception sq) {
                        sq.printStackTrace();
                        JOptionPane.showMessageDialog(null, sq.getMessage());
                    }
                }
            }
        }

    }

    public static void main(String[] args) {
        new ExamRegistration();
    }

}
