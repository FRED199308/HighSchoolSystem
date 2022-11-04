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
import javax.swing.JMenuItem;

import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import static javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE;

import javax.swing.table.DefaultTableModel;

/**
 * @author FRED
 */
public class GradingSystemReg extends JFrame implements ActionListener {

    private Connection con = DbConnection.connectDb();
    private int width = 800;
    private int height = 550;
    private PreparedStatement ps;
    private ResultSet rs;
    private FredTextField value = new FredTextField();
    private FredButton increament = new FredButton("Increament");
    private FredLabel name, classl, start, off, sub;
    private FredTextField jnameA = new FredTextField();
    private FredCombo jclass = new FredCombo("Select Class");
    private FredCombo jsubject = new FredCombo("Select Subject");
    private FredButton save, update;
    private FredCombo subjectfilter = new FredCombo("Filter By Subject");
    private FredCombo classfilter = new FredCombo("Class Filter");
    private DefaultTableModel model = new DefaultTableModel();
    private Object cols[] = {"Class", "Subject Name", "Subject Code", "Min Value", "Max Value", "Grade"};
    private Object row[] = new Object[cols.length];
    private JTable tab = new JTable();
    private JScrollPane pane = new JScrollPane(tab);

    public GradingSystemReg() {
        tab.setModel(model);
        model.setColumnIdentifiers(cols);
        JPopupMenu pop = new JPopupMenu();
        JMenuItem cop = new JMenuItem("Copy");
        JMenuItem cut = new JMenuItem("Cut");
        JMenuItem paste = new JMenuItem("Paste");
        pop.add(cop);
        pop.add(cut);
        pop.add(paste);
        tab.setComponentPopupMenu(pop);
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
        start = new FredLabel("Start Value");
        off = new FredLabel("To");
        classl = new FredLabel("Class");
        name = new FredLabel("Subject Name");
        save = new FredButton("Save");
        try {

            String sql = "select * from classes  where precision1<5 order by precision1";
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                jclass.addItem(rs.getString("ClassName"));
                classfilter.addItem(rs.getString("ClassName"));
            }
            String sql2 = "Select * from subjects";
            ps = con.prepareStatement(sql2);
            rs = ps.executeQuery();
            while (rs.next()) {
                jsubject.addItem(rs.getString("SubjectName"));
                subjectfilter.addItem(rs.getString("SubjectName"));
            }
            String querry = "Select start_from,end_at,grade,classcode,subjects.subjectcode from Subjectgrading,subjects where subjects.subjectcode=subjectgrading.subjectcode";
            ps = con.prepareStatement(querry);
            rs = ps.executeQuery();
            while (rs.next()) {
                row[0] = Globals.className(rs.getString("Classcode"));
                row[1] = Globals.subjectName(rs.getString("subjects.subjectcode"));
                row[2] = rs.getString("subjects.subjectcode");
                row[3] = rs.getString("start_from");
                row[4] = rs.getString("end_at");
                row[5] = rs.getString("Grade");
                model.addRow(row);
            }


        } catch (Exception sq) {
            sq.printStackTrace();
            JOptionPane.showMessageDialog(null, sq.getMessage());
        }
        classl.setBounds(30, 30, 150, 30);
        add(classl);
        jclass.setBounds(200, 30, 250, 30);
        add(jclass);
        value.setBounds(500, 30, 230, 30);
        add(value);
        name.setBounds(30, 100, 150, 30);
        add(name);
        jsubject.setBounds(200, 100, 250, 30);
        add(jsubject);
        increament.setBounds(600, 100, 100, 30);
        add(increament);
        pane.setBounds(30, 150, 750, 300);
        add(pane);

        setVisible(true);
        increament.addActionListener(this);
        jclass.addActionListener(this);
        jsubject.addActionListener(this);
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
                String classcode = Globals.classCode(jclass.getSelectedItem().toString());
                try {
                    String querry = "Select start_from,end_at,grade,classcode,subjects.subjectcode from Subjectgrading,subjects where subjects.subjectcode=subjectgrading.subjectcode and classcode='" + classcode + "'";
                    ps = con.prepareStatement(querry);
                    rs = ps.executeQuery();
                    while (rs.next()) {
                        row[0] = Globals.className(rs.getString("Classcode"));
                        row[1] = Globals.subjectName(rs.getString("subjects.subjectcode"));
                        row[2] = rs.getString("subjects.subjectcode");
                        row[3] = rs.getString("start_from");
                        row[4] = rs.getString("end_at");
                        row[5] = rs.getString("Grade");
                        model.addRow(row);
                    }
                } catch (Exception sq) {
                    sq.printStackTrace();
                    JOptionPane.showMessageDialog(null, sq.getMessage());
                }


            }
        } else if (obj == jsubject) {
            if (jsubject.getSelectedIndex() == 0) {

            } else {
                while (model.getRowCount() > 0) {
                    model.removeRow(0);
                }
                String subcode = Globals.subjectCode(jsubject.getSelectedItem().toString());
                if (jclass.getSelectedIndex() == 0) {
                    try {
                        String querry = "Select start_from,end_at,grade,classcode,subjects.subjectcode from Subjectgrading,subjects where subjects.subjectcode=subjectgrading.subjectcode and subjectcode='" + subcode + "' order by sortcode asc";
                        ps = con.prepareStatement(querry);
                        rs = ps.executeQuery();
                        while (rs.next()) {
                            row[0] = Globals.className(rs.getString("Classcode"));
                            row[1] = Globals.subjectName(rs.getString("subjects.subjectcode"));
                            row[2] = rs.getString("subjects.subjectcode");
                            row[3] = rs.getString("start_from");
                            row[4] = rs.getString("end_at");
                            row[5] = rs.getString("Grade");
                            model.addRow(row);
                        }

                    } catch (Exception sq) {
                        sq.printStackTrace();
                        JOptionPane.showMessageDialog(null, sq.getMessage());
                    }
                } else {
                    String classcode = Globals.classCode(jclass.getSelectedItem().toString());

                    try {

                        String querry = "Select start_from,end_at,grade,classcode,subjects.subjectcode from Subjectgrading,subjects where subjects.subjectcode=subjectgrading.subjectcode and subjectgrading.subjectcode='" + subcode + "' and classcode='" + classcode + "' order by sortcode";
                        ps = con.prepareStatement(querry);
                        rs = ps.executeQuery();
                        while (rs.next()) {
                            row[0] = Globals.className(rs.getString("Classcode"));
                            row[1] = Globals.subjectName(rs.getString("subjects.subjectcode"));
                            row[2] = rs.getString("subjects.subjectcode");
                            row[3] = rs.getString("start_from");
                            row[4] = rs.getString("end_at");
                            row[5] = rs.getString("Grade");
                            model.addRow(row);
                        }


                    } catch (Exception sq) {
                        sq.printStackTrace();
                        JOptionPane.showMessageDialog(null, sq.getMessage());
                    }
                }
            }
        } else if (obj == increament) {
            if (jclass.getSelectedIndex() == 0) {
                JOptionPane.showMessageDialog(this, "Select Class");
            } else {
                if (jsubject.getSelectedIndex() == 0) {
                    JOptionPane.showMessageDialog(this, "Select Subject");
                } else {

                    if (value.getText().isEmpty() || value.getText().length() > 2) {
                        JOptionPane.showMessageDialog(this, "Invalid Increament/Decreament Value");
                    } else {

                        try {
                            String subcode = Globals.subjectCode(jsubject.getSelectedItem().toString());
                            String classcode = Globals.classCode(jclass.getSelectedItem().toString());
                            int increametvalue = Integer.parseInt(value.getText());
                            String sql = "Select start_from,end_at,grade,classcode,subjectcode,sortcode from Subjectgrading where  subjectgrading.subjectcode='" + subcode + "' and classcode='" + classcode + "'  order by sortcode";
                            ps = con.prepareStatement(sql);
                            rs = ps.executeQuery();
                            while (rs.next()) {
                                String sortcode = rs.getString("Sortcode");
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
                                String sql2 = "Update Subjectgrading set start_from='" + min + "',end_at='" + max + "' where classcode='" + classcode + "' and subjectcode='" + subcode + "' and sortcode='" + sortcode + "'";
                                ps = con.prepareStatement(sql2);
                                ps.execute();

                            }
                            while (model.getRowCount() > 0) {
                                model.removeRow(0);
                            }
                            String querry = "Select start_from,end_at,grade,classcode,subjects.subjectcode from Subjectgrading,subjects where subjects.subjectcode=subjectgrading.subjectcode and subjectgrading.subjectcode='" + subcode + "' and classcode='" + classcode + "'  order by sortcode";
                            ps = con.prepareStatement(querry);
                            rs = ps.executeQuery();
                            while (rs.next()) {
                                row[0] = Globals.className(rs.getString("Classcode"));
                                row[1] = Globals.subjectName(rs.getString("subjects.subjectcode"));
                                row[2] = rs.getString("subjects.subjectcode");
                                row[3] = rs.getString("start_from");
                                row[4] = rs.getString("end_at");
                                row[5] = rs.getString("Grade");
                                model.addRow(row);
                            }

                        } catch (Exception sq) {
                            sq.printStackTrace();
                            JOptionPane.showMessageDialog(null, sq.getMessage());
                        }
                    }

                }
            }
        }

    }

    public static void main(String[] args) {
        new GradingSystemReg();
    }


}
