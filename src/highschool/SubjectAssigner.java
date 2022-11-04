/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package highschool;


import java.awt.Color;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
public class SubjectAssigner extends JFrame implements ActionListener {

    public static void main(String[] args) {
        new SubjectAssigner();
    }

    private int height = 600;
    private int width = 900;
    private Connection con;
    private PreparedStatement ps;
    private ResultSet rs;
    private FredDateChooser jdate;
    private FredButton save, cancel, assign, deassign;
    private JTable tab = new JTable();
    private FredCombo jsubject, jsbjectcode, jteachername, jteachercode, jclassname, jstream;
    private Object cols[] = new Object[]{"Subject Name", "Class Name", "Stream", "Teacher Name", "Clearance Rights"};
    private DefaultTableModel model = new DefaultTableModel();
    private FredLabel subject, subjectcode, teachername, teachercode, classname, stream, date;
    private FredLabel infor = new FredLabel("Current Assigned Subject");
    private JScrollPane pane = new JScrollPane(tab);
    private FredCheckBox clear = new FredCheckBox("Allow Clearance");
    String depcode = "";
    String empcode = "", subcode = "", classcode = "", streamcode = "";
    private Object row[] = new Object[cols.length];

    public SubjectAssigner() {
        setSize(width, height);
        setLocationRelativeTo(null);
        setLayout(null);
        setTitle("Subject Allocation Panel");
        getContentPane().setBackground(Color.CYAN);
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
        model.setColumnIdentifiers(cols);
        tab.setModel(model);
        con = DbConnection.connectDb();
        this.setIconImage(FrameProperties.icon());
        ((JComponent) getContentPane()).setBorder(

                BorderFactory.createMatteBorder(5, 5, 5, 5, Color.MAGENTA));
        save = new FredButton("Save");
        cancel = new FredButton("Close");
        assign = new FredButton("Assign Subject");
        deassign = new FredButton("Revoke");
        subject = new FredLabel("Subject Name");
        subjectcode = new FredLabel("Subject Code");
        teachername = new FredLabel("Teacher Name");
        teachercode = new FredLabel("Teacher Ref No");
        classname = new FredLabel("Class Name");
        stream = new FredLabel("Stream");
        jclassname = new FredCombo("Select Class");
        jsbjectcode = new FredCombo("Select Subject Code");
        jstream = new FredCombo("Select Stream");
        jteachername = new FredCombo("Select Teacher Name");
        jteachercode = new FredCombo("Select Teacher Ref No.");
        jsubject = new FredCombo("Select Subject Name");


        subject.setBounds(30, 30, 150, 30);
        add(subject);
        jsubject.setBounds(250, 30, 200, 30);
        add(jsubject);
        classname.setBounds(500, 30, 150, 30);
        jclassname.setBounds(650, 30, 200, 30);
        add(jclassname);
        add(classname);

        teachername.setBounds(30, 110, 150, 30);
        jteachername.setBounds(250, 110, 200, 30);
        add(teachername);
        add(jteachername);
        jstream.setBounds(630, 90, 150, 30);
        add(jstream);
        assign.setBounds(640, 140, 150, 30);
        add(assign);
        clear.setBounds(500, 140, 130, 30);
        add(clear);
        infor.setBounds(30, 150, 150, 30);
        add(infor);
        pane.setBounds(20, 200, 850, 310);
        add(pane);
        cancel.setBounds(200, 520, 130, 30);
        add(cancel);
        deassign.setBounds(700, 520, 130, 30);
        add(deassign);
        try {
            String sql = "Select * from classes where precision1<5 order by precision1 asc";
            con = DbConnection.connectDb();
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                jclassname.addItem(rs.getString("ClassName"));

            }
            String sql2 = "Select * from streams";

            ps = con.prepareStatement(sql2);
            rs = ps.executeQuery();
            while (rs.next()) {

                jstream.addItem(rs.getString("StreamName"));
            }
            String sql6 = "Select departmentcode from departments where name='" + "Academics" + "'";
            ps = con.prepareStatement(sql6);
            rs = ps.executeQuery();
            while (rs.next()) {
                depcode = rs.getString("Departmentcode");
            }


//          String sql5="Select employeecode from staffs where status='"+"Active"+"'";
//          ps=con.prepareStatement(sql5);
//          rs=ps.executeQuery();
//          while(rs.next())
//          {empcode=rs.getString("Employeecode");
            String sqlin = "Select firstname,middlename,lastname from staffs where status='" + "Active" + "' and departmentcode='" + depcode + "'";
            ps = con.prepareStatement(sqlin);
            ResultSet RS = ps.executeQuery();
            while (RS.next()) {
                jteachername.addItem(RS.getString("firstName") + "    " + RS.getString("Middlename") + "    " + RS.getString("LastName"));
            }


            String sql3 = "Select * from subjects";
            ps = con.prepareStatement(sql3);
            rs = ps.executeQuery();
            while (rs.next()) {
                jsubject.addItem(rs.getString("SubjectName"));
            }
            String sql4 = "Select * from  Subjectrights";
            ps = con.prepareStatement(sql4);
            rs = ps.executeQuery();
            while (rs.next()) {
                row[0] = Globals.subjectName(rs.getString("Subjectcode"));
                row[1] = Globals.className(rs.getString("Classcode"));
                row[2] = Globals.streamName(rs.getString("Streamcode"));
                row[3] = Globals.fullStaffName(rs.getString("teachercode"));
                row[4] = rs.getString("status");
                model.addRow(row);
            }

        } catch (SQLException sq) {
            sq.printStackTrace();
            JOptionPane.showMessageDialog(null, sq.getMessage());
        }


        setVisible(true);
        assign.addActionListener(this);
        cancel.addActionListener(this);
        deassign.addActionListener(this);
        if (jstream.getItemCount() < 3) {
            jstream.setSelectedIndex(1);
        }
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        Object obj = e.getSource();
        if (obj == assign) {
            if (jsubject.getSelectedIndex() == 0) {
                JOptionPane.showMessageDialog(this, "Select Subject Name");
            } else {
                if (jclassname.getSelectedIndex() == 0) {
                    JOptionPane.showMessageDialog(this, "Select Class");

                } else {
                    if (jstream.getSelectedIndex() == 0) {
                        JOptionPane.showMessageDialog(this, "Select Stream");
                    } else {


                        try {
                            subcode = Globals.subjectCode(jsubject.getSelectedItem().toString());
                            streamcode = Globals.streamcode(jstream.getSelectedItem().toString());
                            classcode = Globals.classCode(jclassname.getSelectedItem().toString());
                            empcode = Globals.employeeCode(jteachername.getSelectedItem().toString());
                            String sqla = "Select * from subjectrights where classcode='" + classcode + "' and subjectcode='" + subcode + "' and teachercode='" + Globals.employeeCode(jteachername.getSelectedItem().toString()) + "' and streamcode='" + Globals.streamcode(jstream.getSelectedItem().toString()) + "'";
                            ps = con.prepareStatement(sqla);
                            rs = ps.executeQuery();
                            if (rs.next()) {
                                JOptionPane.showMessageDialog(this, Globals.className(rs.getString("classcode")) + " " + jstream.getSelectedItem() + "  " + Globals.subjectName(rs.getString("subjectcode")) + " Had Already Been  Assigned To " + Globals.fullStaffName(rs.getString("teachercode")));
                            } else {
                                String status = "";
                                if (clear.isSelected()) {
                                    status = "True".toUpperCase();
                                } else {
                                    status = "False".toUpperCase();
                                }
                                String sql = "Insert into subjectrights values('" + Globals.subjectCode(jsubject.getSelectedItem().toString()) + "','" + Globals.classCode(jclassname.getSelectedItem().toString()) + "','" + Globals.streamcode(jstream.getSelectedItem().toString()) + "','" + Globals.employeeCode(jteachername.getSelectedItem().toString()) + "','" + status + "')";
                                ps = con.prepareStatement(sql);
                                ps.execute();
                                row[0] = jsubject.getSelectedItem();
                                row[1] = jclassname.getSelectedItem();
                                row[2] = jstream.getSelectedItem();
                                row[3] = jteachername.getSelectedItem();
                                row[4] = status;
                                model.addRow(row);
                                JOptionPane.showMessageDialog(this, "Right Assigned Successfully");
                                jclassname.setSelectedIndex(0);
                                jsubject.setSelectedIndex(0);
                                jstream.setSelectedIndex(0);
                                jteachername.setSelectedIndex(0);


                            }


                        } catch (HeadlessException | SQLException sq) {
                            sq.printStackTrace();
                            JOptionPane.showMessageDialog(null, sq.getMessage());
                        }
                    }
                }
            }
        } else if (obj == deassign) {
            if (tab.getSelectedRowCount() < 1) {
                JOptionPane.showMessageDialog(this, "Select The  Subject Teacher Combination To Revoke The Rights");
            } else {
                try {
                    int selectedrow = tab.getSelectedRow();
                    subcode = Globals.subjectCode(tab.getValueAt(selectedrow, 0).toString());
                    streamcode = Globals.streamcode(tab.getValueAt(selectedrow, 2).toString());
                    classcode = Globals.classCode(tab.getValueAt(selectedrow, 1).toString());
                    empcode = Globals.employeeCode(tab.getValueAt(selectedrow, 3).toString());


                    String sql = "Delete from subjectrights where subjectcode='" + subcode + "' and classcode='" + classcode + "'  and teachercode='" + empcode + "' and streamcode='" + streamcode + "'";
                    ps = con.prepareStatement(sql);
                    ps.execute();
                    model.removeRow(selectedrow);

                    JOptionPane.showMessageDialog(this, "Subject Class Right Revoked Successfully");
                } catch (SQLException sq) {
                    sq.printStackTrace();
                    JOptionPane.showMessageDialog(null, sq.getMessage());
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
