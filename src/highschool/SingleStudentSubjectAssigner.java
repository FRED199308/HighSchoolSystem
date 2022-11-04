package highschool;


import java.awt.Color;
import java.awt.Font;
import java.awt.HeadlessException;
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
import java.util.Iterator;
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

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 * @author FREDDY
 */
public class SingleStudentSubjectAssigner extends JFrame implements ActionListener {

    public static void main(String[] args) {
        new SingleStudentSubjectAssigner();
    }

    private int width = 820;
    private int height = 400;
    private JCheckBox smsAll = new JCheckBox("Assign  All");
    private JList members, selectedMembers;

    private JScrollPane scroll, scroll2;
    private String staffcode;
    private FredButton send, cancel, copy, remove;
    private Connection con = DbConnection.connectDb();
    private PreparedStatement ps;
    private ResultSet rs;
    private IdGenerator key = new IdGenerator();
    private FredTextField adm = new FredTextField();
    private DefaultListModel model, model2;


    public SingleStudentSubjectAssigner() {

        setSize(width, height);
        setTitle("Single Student Subject Assigner Window");
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
                CurrentFrame.secFrame().setEnabled(true);
                e.getWindow().dispose();
            }
        });
        setLayout(null);
        setResizable(false);
        setLocationRelativeTo(null);
        members = new JList();
        setResizable(false);
        adm.setBorder(new TitledBorder("admission Number"));
        selectedMembers = new JList();
        scroll2 = new JScrollPane(selectedMembers);
        send = new FredButton("Assign");
        cancel = new FredButton("Close");
        copy = new FredButton("Copy>>");
        remove = new FredButton("<<Remove");
        model = new DefaultListModel();
        model2 = new DefaultListModel();

        members.setModel(model);
        selectedMembers.setModel(model2);

        scroll = new JScrollPane(members);
        members.setVisibleRowCount(6);
        members.setFixedCellHeight(25);
        selectedMembers.setVisibleRowCount(6);
        selectedMembers.setFixedCellHeight(25);
        selectedMembers.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        members.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        scroll.setBounds(50, 30, 300, 150);
        add(scroll);
        scroll2.setBounds(490, 30, 300, 150);
        add(scroll2);
        copy.setBounds(380, 50, 100, 30);
        add(copy);
        remove.setBounds(380, 130, 100, 30);
        add(remove);
        smsAll.setBounds(200, 200, 150, 30);
        add(smsAll);
        adm.setBounds(450, 200, 150, 50);
        add(adm);

        cancel.setBounds(100, 300, 150, 30);
        add(cancel);
        send.setBounds(480, 300, 150, 30);
        add(send);
        scroll2.setBorder(new TitledBorder("Assigned Subjects"));
        scroll.setBorder(new TitledBorder("Subjects"));
        adm.setFont(new Font("serif", Font.BOLD, 20));
        try {
            String sql2 = "Select subjectName from subjects order by subjectcode";
            ps = con.prepareStatement(sql2);
            rs = ps.executeQuery();
            while (rs.next()) {
                model.addElement(rs.getString("subjectName"));

            }

            sql2 = "Select subjectcode from studentsubjectallocation where admnumber='" + Globals.singleAdmissionAllocator + "' and academicyear='" + Globals.singleyearAllocator + "' ";
            ps = con.prepareStatement(sql2);
            rs = ps.executeQuery();
            while (rs.next()) {
                model2.addElement(Globals.subjectName(rs.getString("subjectcode")));


            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        setVisible(true);
        adm.setText(Globals.singleAdmissionAllocator);
        copy.addActionListener(this);
        remove.addActionListener(this);
        send.addActionListener(this);
        cancel.addActionListener(this);
        smsAll.addActionListener(this);

        adm.addKeyListener(new KeyAdapter() {

            public void keyReleased(KeyEvent key) {
                int c = key.getKeyCode();
                if (Globals.admissionVerifier(adm.getText())) {
                    adm.setForeground(Color.black);
                } else {
                    adm.setForeground(Color.RED);
                }
            }
        });
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        Object obj = e.getSource();
        if (obj == cancel) {
            try {
                con.close();
            } catch (SQLException sq) {

            }
            CurrentFrame.secFrame().setEnabled(true);
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

            if (model2.isEmpty()) {
                if (smsAll.isSelected()) {
                    try {
                        if (Globals.admissionVerifier(adm.getText())) {
                            int counter = 0;
                            String sql = "Select * from Subjects";
                            ps = con.prepareStatement(sql);
                            rs = ps.executeQuery();
                            while (rs.next()) {

                                String subjectcode = rs.getString("Subjectcode");

                                sql = "select * from studentsubjectallocation where admnumber='" + adm.getText() + "' and academicyear='" + Globals.singleyearAllocator + "' and subjectcode='" + subjectcode + "'";
                                ps = con.prepareStatement(sql);
                                ResultSet Rs = ps.executeQuery();
                                if (Rs.next()) {

                                } else {
                                    counter++;
                                    sql = "Insert into studentsubjectallocation values ('" + subjectcode + "','" + adm.getText() + "','" + Globals.singleyearAllocator + "')";
                                    ps = con.prepareStatement(sql);
                                    ps.execute();
                                }


                            }

                            JOptionPane.showMessageDialog(this, counter + " subject(s) Allocated To Student admission Number :" + adm.getText());
                        } else {
                            JOptionPane.showMessageDialog(this, "Invalid admission Number");
                        }


                    } catch (Exception sq) {
                        JOptionPane.showMessageDialog(this, sq.getMessage());
                    }

                } else {
                    JOptionPane.showMessageDialog(this, "No Subjects Selected To Assign , Kindly Select From Left List Or Mark All Subjects");
                }
            } else {
                try {

                    if (Globals.admissionVerifier(adm.getText())) {
                        int counter = 0;
                        ps = con.prepareStatement("Delete from studentsubjectallocation  where admnumber='" + adm.getText() + "' and academicyear='" + Globals.singleyearAllocator + "'  ");
                        ps.execute();
                        for (int h = 0; h < model2.getSize(); ++h) {

                            String subjectcode = Globals.subjectCode(model2.getElementAt(h).toString());


                            String sql = "";
                            sql = "select * from studentsubjectallocation where admnumber='" + adm.getText() + "' and academicyear='" + Globals.singleyearAllocator + "' and subjectcode='" + subjectcode + "'";
                            ps = con.prepareStatement(sql);
                            rs = ps.executeQuery();
                            if (rs.next()) {

                            } else {
                                counter++;
                                sql = "Insert into studentsubjectallocation values ('" + subjectcode + "','" + adm.getText() + "','" + Globals.singleyearAllocator + "')";
                                ps = con.prepareStatement(sql);
                                ps.execute();
                            }


                        }
                        JOptionPane.showMessageDialog(this, counter + " subject(s) Allocated To Student admission Number :" + adm.getText());


                    } else {
                        JOptionPane.showMessageDialog(this, "Invalid admission Number");
                    }


                } catch (Exception sq) {
                    JOptionPane.showMessageDialog(this, sq.getMessage());
                }
            }


        }
    }

}
