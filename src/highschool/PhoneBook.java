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
import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import static javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE;

import javax.swing.table.DefaultTableModel;

import net.miginfocom.swing.MigLayout;

/**
 * @author FREDDY
 */
public class PhoneBook extends JFrame implements ActionListener {
    private Connection con = DbConnection.connectDb();
    private int width = 1000;
    private int height = 600;
    private PreparedStatement ps;
    private ResultSet rs;
    private FredButton send, edit;
    private FredButton print = new FredButton("Export to pdf");
    private JTable tab = new JTable();
    private JScrollPane pane = new JScrollPane(tab);
    private Object cols[] = {"admission Number", "Student Name", "Parent Name", "Phone Number", "phone 2"};
    private DefaultTableModel model = new DefaultTableModel();
    private FredLabel search = new FredLabel("Search Student By Name,admission Number,Parent Name,or phone Number");
    private FredCombo classs = new FredCombo("Filter By Class");
    private FredCombo nullcontact = new FredCombo("Filter Without Contact");
    private FredTextField jsearch = new FredTextField();
    private FredLabel cont = new FredLabel("Sort Based On Contact");
    FredLabel jc = new FredLabel("Sort By Class");
    private Object row[] = new Object[cols.length];
    private FredButton composer = new FredButton("Compose Message");


    public PhoneBook() {
        model.setColumnIdentifiers(cols);
        tab.setModel(model);
        edit = new FredButton("Edit Phone");
        nullcontact.addItem("Real Phones");
        nullcontact.addItem("None Phones");
        setLayout(new MigLayout());

        setSize(width, height);
        setTitle("Phone Book Immitation");
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
                dispose();
            }
        });


        try {

            String sql = "Select * from classes where precision1<5 order by precision1 asc";
            con = DbConnection.connectDb();
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                classs.addItem(rs.getString("ClassName"));

            }
            String querry = "Select AdmissionNumber,firstname,middlename,lastname,parentfullnames,telephone1,telephone2 from admission";
            ps = con.prepareStatement(querry);
            rs = ps.executeQuery();
            while (rs.next()) {
                row[0] = rs.getString("AdmissionNumber");
                row[1] = rs.getString("FirstName") + "   " + rs.getString("MIddleName") + "   " + rs.getString("LastName");
                row[2] = rs.getString("parentfullnames");
                row[3] = rs.getString("telephone1");
                row[4] = rs.getString("telephone2");
                model.addRow(row);

            }


        } catch (Exception sq) {
            sq.printStackTrace();
        }
        add(search, "split");
        add(jsearch, "growx,pushx,span 2 1,wrap");
        add(pane, "grow,push,span 2 1,wrap");
        add(jc, "growx,pushx,gapleft 30,split");
        add(classs, "gapleft 30");
        add(cont, "growx,pushx,gapleft 30,split");
        add(nullcontact, "gapleft 30");
        add(composer, "gapleft 30,wrap");
        add(print, "gapleft 30,wrap");
        setLocationRelativeTo(null);
        setVisible(true);
        nullcontact.addActionListener(this);
        classs.addActionListener(this);
        composer.addActionListener(this);

        jsearch.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent key) {

                char c = key.getKeyChar();
                if (c == KeyEvent.VK_ENTER) {

                    while (model.getRowCount() > 0) {
                        model.removeRow(0);
                    }


                    try {

                        String querry = "Select AdmissionNumber,firstname,middlename,lastname,parentfullnames,telephone1,telephone2 from admission where admissionNumber  like '" + jsearch.getText() + "%' or firstname  like '" + jsearch.getText() + "%' or middlename  like '" + jsearch.getText() + "%'  or lastname  like '" + jsearch.getText() + "%' or telephone1  like '" + jsearch.getText() + "%' or telephone2  like '" + jsearch.getText() + "%'";
                        ps = con.prepareStatement(querry);
                        rs = ps.executeQuery();
                        while (rs.next()) {
                            row[0] = rs.getString("AdmissionNumber");
                            row[1] = rs.getString("FirstName") + "   " + rs.getString("MIddleName") + "   " + rs.getString("LastName");
                            row[2] = rs.getString("parentfullnames");
                            row[3] = rs.getString("telephone1");
                            row[4] = rs.getString("telephone2");
                            model.addRow(row);

                        }

                    } catch (Exception sq) {
                        sq.printStackTrace();
                    }


                }


            }
        });

        print.addActionListener(this);
    }

    public void actionPerformed(ActionEvent e) {
        Object obj = e.getSource();

        if (obj == classs) {
            if (classs.getSelectedIndex() > 0) {
                while (model.getRowCount() > 0) {
                    model.removeRow(0);
                }
                try {
                    search.setText(classs.getSelectedItem() + " Contacts");
                    String querry = "Select AdmissionNumber,firstname,middlename,lastname,parentfullnames,telephone1,telephone2 from admission where currentform ='" + Globals.classCode(classs.getSelectedItem().toString()) + "'";
                    ps = con.prepareStatement(querry);
                    rs = ps.executeQuery();
                    while (rs.next()) {
                        row[0] = rs.getString("AdmissionNumber");
                        row[1] = rs.getString("FirstName") + "   " + rs.getString("MIddleName") + "   " + rs.getString("LastName");
                        row[2] = rs.getString("parentfullnames");
                        row[3] = rs.getString("telephone1");
                        row[4] = rs.getString("telephone2");
                        model.addRow(row);

                    }


                } catch (Exception sq) {
                    sq.printStackTrace();
                }


            }
        } else if (obj == composer) {
            if (tab.getSelectedRowCount() < 1) {
                JOptionPane.showMessageDialog(this, "Kindly Select Receipient From The Table above");
            } else {
                String recipient = model.getValueAt(tab.getSelectedRow(), 3).toString();

                Globals.composerRecipient = recipient;
                SingleMessageComposer compose = new SingleMessageComposer();
                CurrentFrame.secFrame().setEnabled(false);


            }
        } else if (obj == print) {
            ReportGenerator.generateReport(search.getText(), "Studentlist", tab);
        } else if (obj == nullcontact) {
            try {
                if (nullcontact.getSelectedIndex() == 1) {
                    while (model.getRowCount() > 0) {
                        model.removeRow(0);
                    }
                    search.setText("Students With Valid Phone 1 Contacts");
                    String querry = "Select AdmissionNumber,firstname,middlename,lastname,parentfullnames,telephone1,telephone2 from admission where telephone1 !='" + null + "'";
                    ps = con.prepareStatement(querry);
                    rs = ps.executeQuery();
                    while (rs.next()) {
                        row[0] = rs.getString("AdmissionNumber");
                        row[1] = rs.getString("FirstName") + "   " + rs.getString("MIddleName") + "   " + rs.getString("LastName");
                        row[2] = rs.getString("parentfullnames");
                        row[3] = rs.getString("telephone1");
                        row[4] = rs.getString("telephone2");
                        model.addRow(row);

                    }

                } else if (nullcontact.getSelectedIndex() == 2) {
                    search.setText("Students With No Valid Phone 1 Contacts");
                    while (model.getRowCount() > 0) {
                        model.removeRow(0);
                    }
                    String querry = "Select AdmissionNumber,firstname,middlename,lastname,parentfullnames,telephone1,telephone2 from admission where telephone1 ='" + null + "'";
                    ps = con.prepareStatement(querry);
                    rs = ps.executeQuery();
                    while (rs.next()) {
                        row[0] = rs.getString("AdmissionNumber");
                        row[1] = rs.getString("FirstName") + "   " + rs.getString("MIddleName") + "   " + rs.getString("LastName");
                        row[2] = rs.getString("parentfullnames");
                        row[3] = rs.getString("telephone1");
                        row[4] = rs.getString("telephone2");
                        model.addRow(row);

                    }

                }


            } catch (Exception sq) {
                sq.printStackTrace();
            }
        }


    }

    public static void main(String[] args) {
        new PhoneBook();
    }

}
