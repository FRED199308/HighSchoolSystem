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
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextPane;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;

import net.miginfocom.swing.MigLayout;

/**
 * @author FREDDY
 */
public class GroupDetails {
    private FredCombo additionalCol = new FredCombo("Blank Column");
    private Connection con = DbConnection.connectDb();
    private PreparedStatement ps;
    private ResultSet rs;
    private JPanel holder;
    private JPanel top;
    private JPanel bottom;
    private FredLabel groupsearch = new FredLabel();
    private FredButton edit;
    private String groupcode;

    public JPanel groupsJpanel() {

        try {
            holder = new JPanel();
            top = new JPanel();
            holder.setLayout(new MigLayout());


            /// takes user to group operations panel

            JTextPane pp = new JTextPane();
            JLabel Groups = new JLabel("");

            groupsearch.setBorder(new TitledBorder("Search Group"));
            edit = new FredButton("Edit Group Details");
            Groups.setText("Registered Groups");
            FredButton del = new FredButton("Delete Group");
            FredButton groupMemberDetails = new FredButton("View Group Members");
            FredButton back = new FredButton("Back");
            FredButton print = new FredButton("Generate Report");
            FredButton remove = new FredButton("Remove Member From Group");
            remove.setEnabled(false);
            JPanel groupup = new JPanel();
            JPanel groupBottom = new JPanel();
            groupup.setBackground(Color.MAGENTA);
            groupBottom.setBackground(Color.cyan);
            groupBottom.setLayout(new MigLayout());

            holder.add(groupup, "wrap,push,grow");
            holder.add(groupBottom, "wrap,push,grow");
            groupBottom.setLayout(new MigLayout());
            groupup.setLayout(new MigLayout());
            groupBottom.add(del, "pushx,growx");
            groupBottom.add(groupMemberDetails, "pushx,growx");
            groupBottom.add(back, "pushx,growx");
            groupBottom.add(print, "pushx,growx,wrap");
            groupBottom.add(remove, "pushx,growx");
            groupBottom.add(edit, "pushx,growx");
            groupBottom.add(additionalCol, "pushx,growx");
            additionalCol.setVisible(false);
            pp.setBorder(new TitledBorder("Group Description"));
            groupup.add(Groups, "pushx ,growx");
            groupup.add(groupsearch, "pushx ,growx,center,wrap");

            DefaultTableModel groupmodel = new DefaultTableModel() {
                @Override
                public boolean isCellEditable(int row, int col) {
                    return false;
                }
            };
            JTable grouptable = new JTable();
            Object grouprow[] = {"No.", "GroupName", "GroupAgenda", "FormationDate", "Capacity", "Ref No."};
            groupmodel.setColumnIdentifiers(grouprow);
            grouptable.setModel(groupmodel);
            JScrollPane grouppane = new JScrollPane(grouptable);
            String gp = "";

            groupup.add(grouppane, "push,grow,span 2 1,wrap");
            groupup.add(pp, "push ,grow,center,span 2 1,wrap");
            String querry = "select * from groups order by groupname";
            ps = con.prepareStatement(querry);
            rs = ps.executeQuery();
            while (rs.next()) {
                gp = rs.getString("Groupcode");
                int count = 0;
                ps = con.prepareStatement("Select Count(*) from groupmembers where groupcode='" + gp + "'");
                ResultSet gf = ps.executeQuery();
                if (gf.next()) {
                    count = gf.getInt("Count(*)");
                }
                Object row[] = {groupmodel.getRowCount() + 1, rs.getString("GroupName"), rs.getString("GroupAgenda"), rs.getString("FormationDate"), String.valueOf(count) + "/" + rs.getString("capacity"), rs.getString("Groupcode")};
                groupmodel.addRow(row);
            }
            grouptable.setRowHeight(25);


            groupsearch.addKeyListener(new KeyAdapter() {
                public void keyReleased(KeyEvent key) {
                    String text = groupsearch.getText();

                    try {
                        groupmodel.getDataVector().removeAllElements();
                        String querry = "select * from groups where groupname like '" + text + "%' order by groupname";
                        ps = con.prepareStatement(querry);
                        rs = ps.executeQuery();
                        while (rs.next()) {
                            Object row[] = {groupmodel.getRowCount() + 1, rs.getString("GroupName"), rs.getString("GroupAgenda"), rs.getString("FormationDate"), rs.getString("capacity"), rs.getString("Groupcode")};
                            groupmodel.addRow(row);
                        }

                    } catch (Exception sq) {
                        sq.printStackTrace();
                    }

                }
            });
            edit.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    if (RightsAnnouncer.editRights() == false) {

                        JOptionPane.showMessageDialog(null, "You Do Not Have Sufficient Rights To perform This Operation\n Consult Admin");
                    } else {
                        if (grouptable.getSelectedRowCount() < 1) {
                            JOptionPane.showMessageDialog(null, "Kindly Select The Group to be Edited", "Selection Ommitted", JOptionPane.ERROR_MESSAGE);
                        } else {

                            GroupEdit.groupid = groupmodel.getValueAt(grouptable.getSelectedRow(), 5).toString();
                            CurrentFrame.mainFrame().setEnabled(false);
                            new GroupEdit();
                        }
                    }

                }
            });
            del.addActionListener(new ActionListener() {
                @Override

                public void actionPerformed(ActionEvent e) {
                    if (RightsAnnouncer.DeleteRights() == true) {
                        if (grouptable.getSelectedRowCount() > 0) {
                            try {
                                int option = JOptionPane.showConfirmDialog(null, "Are You Sure You want to Delete This Group\nAll the Details concerning"
                                        + "the group will be lost", "Confirm", JOptionPane.YES_NO_OPTION);
                                if (option == JOptionPane.YES_OPTION) {
                                    String querry = "Delete from Groups where groupcode='" + groupmodel.getValueAt(grouptable.getSelectedRow(), 5) + "'";
                                    ps = con.prepareStatement(querry);
                                    ps.execute();
                                    groupmodel.removeRow(grouptable.getSelectedRow());
                                    JOptionPane.showMessageDialog(null, "Group Deleted successfully");

                                } else if (option == JOptionPane.NO_OPTION) {
                                    JOptionPane.showMessageDialog(null, "Operation Cancelled");

                                }
                            } catch (Exception sq) {
                                JOptionPane.showMessageDialog(null, sq, "Error Occurred", JOptionPane.ERROR_MESSAGE);
                            }
                        } else {

                            JOptionPane.showMessageDialog(null, "Kindly Select The Group to Delete");
                        }
                    } else {

                        JOptionPane.showMessageDialog(null, "You Do Not Have Sufficient Rights To perform This Operation\n Consult Admin");
                    }
                }
            });
            grouptable.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    if (grouptable.getSelectedRowCount() > 0) {
                        pp.setText(grouptable.getValueAt(grouptable.getSelectedRow(), 4).toString());
                    }

                }

            });

            groupMemberDetails.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {


                    if (grouptable.getSelectedRowCount() > 0) {
                        groupcode = grouptable.getValueAt(grouptable.getSelectedRow(), 5).toString();
                        additionalCol.setVisible(true);
                        Groups.setText("Registered Members of :   " + grouptable.getValueAt(grouptable.getSelectedRow(), 1).toString());
                        groupsearch.setBorder(new TitledBorder("Search Student By Name"));

                        del.setEnabled(false);
                        remove.setEnabled(true);
                        grouptable.removeMouseListener(null);

                        groupsearch.addKeyListener(new KeyAdapter() {
                            public void keyReleased(KeyEvent key) {
                                String text = groupsearch.getText();

                                try {
                                    groupmodel.getDataVector().removeAllElements();

                                    String querry7 = "select firstname,middlename,lastname,gender,admission.AdmissionNumber from admission,groupmembers where admission.AdmissionNumber=groupmembers.AdmissionNumber and groupcode='" + groupcode + "' and (firstname like '" + text + "%' or middlename like '" + text + "%' or lastname like '" + text + "%' or Telephone1 like '" + text + "%'  or admission.AdmissionNumber='" + text + "%' ) order by firstname";
                                    ps = con.prepareStatement(querry7);
                                    rs = ps.executeQuery();
                                    ResultSetMetaData meta = rs.getMetaData();
                                    Object row[] = new Object[meta.getColumnCount() + 1];
                                    while (rs.next()) {
                                        row[0] = groupmodel.getRowCount() + 1;
                                        row[1] = rs.getString("firstname");
                                        row[2] = rs.getString("middlename");
                                        row[3] = rs.getString("lastname");
                                        row[4] = rs.getString("gender");
                                        row[5] = rs.getString("AdmissionNumber");

                                        groupmodel.addRow(row);
                                    }

                                } catch (Exception sq) {
                                    sq.printStackTrace();
                                    JOptionPane.showMessageDialog(null, sq.getMessage());
                                }

                            }
                        });
                        try {
                            while (grouptable.getRowCount() > 0) {
                                ((DefaultTableModel) grouptable.getModel()).removeRow(0);
                            }
                            Object newcols[] = {"No.", "FirstName", "MiddleName", "LastName", "Gender", "ADM No."};
                            groupmodel.setColumnIdentifiers(newcols);
                            PreparedStatement p1;
                            ResultSet r1;
                            String qerry = "select AdmissionNumber from groupmembers where groupcode='" + groupcode + "'";
                            p1 = con.prepareStatement(qerry);
                            r1 = p1.executeQuery();
                            while (r1.next()) {

                                String memberc = r1.getString("AdmissionNumber");
                                String querry7 = "select firstname,middlename,lastname,gender,AdmissionNumber from admission where AdmissionNumber='" + memberc + "'";
                                ps = con.prepareStatement(querry7);
                                rs = ps.executeQuery();
                                ResultSetMetaData meta = rs.getMetaData();
                                Object row[] = new Object[meta.getColumnCount() + 1];
                                while (rs.next()) {
                                    row[0] = groupmodel.getRowCount() + 1;
                                    row[1] = rs.getString("firstname");
                                    row[2] = rs.getString("middlename");
                                    row[3] = rs.getString("lastname");
                                    row[4] = rs.getString("gender");
                                    row[5] = rs.getString("AdmissionNumber");

                                }
                                groupmodel.addRow(row);
                            }

                        } catch (Exception sq) {
                            sq.printStackTrace();
                            JOptionPane.showMessageDialog(null, sq.getMessage());
                        }
                    } else {

                        JOptionPane.showMessageDialog(null, "Kindly Select Group");
                    }

                }
            });
            back.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    //  deleteGroup.doClick();
                    additionalCol.setVisible(false);
                    del.setEnabled(true);

                }
            });
            print.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    ReportGenerator.generateReport(Groups.getText(), "Group Report", grouptable);

                }
            });

            remove.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {


                    String querry = "Delete from groupmembers where AdmissionNumber='" + grouptable.getValueAt(grouptable.getSelectedRow(), 5) + "' and groupcode='" + groupcode + "'";
                    try {
                        ps = con.prepareStatement(querry);
                        ps.execute();
                        groupmodel.removeRow(grouptable.getSelectedRow());
                        JOptionPane.showMessageDialog(null, "Deletion succeded", "success", JOptionPane.ERROR_MESSAGE);
                    } catch (Exception sq) {

                        JOptionPane.showMessageDialog(null, sq, "Deletion Error", JOptionPane.ERROR_MESSAGE);
                    }

                }
            });
            additionalCol.addItem("First name");
            additionalCol.addItem("Middle name");
            additionalCol.addItem("Last name");
            additionalCol.addItem("admissionNumber");
            additionalCol.addItem("parentName");
            additionalCol.addItem("Gender");
            additionalCol.addItem("Phone Number");
            additionalCol.addItem("UPI");
            additionalCol.addItem("County");
            additionalCol.addItem("KcpeMarks");
            additionalCol.addItem("DateOfBirth");
            additionalCol.addItem("Date of admission ");
            additionalCol.addItem("program");


            additionalCol.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    String memberRef = "";
                    String searchValue = "";
//                DefaultTableModel searchModel=new DefaultTableModel();
//                searchModel=groupmodel;
                    if (additionalCol.getSelectedIndex() == 1) {
                        searchValue = "FirstName";
                    } else if (additionalCol.getSelectedIndex() == 2) {
                        searchValue = "MiddleName";
                    } else if (additionalCol.getSelectedIndex() == 3) {
                        searchValue = "LastName";
                    } else if (additionalCol.getSelectedIndex() == 4) {
                        searchValue = "admissionNumber";
                    } else if (additionalCol.getSelectedIndex() == 5) {
                        searchValue = "parentfullNames";
                    } else if (additionalCol.getSelectedIndex() == 6) {
                        searchValue = "Gender";
                    } else if (additionalCol.getSelectedIndex() == 7) {
                        searchValue = "Telephone1";
                    } else if (additionalCol.getSelectedIndex() == 8) {
                        searchValue = "UPI";
                    } else if (additionalCol.getSelectedIndex() == 10) {
                        searchValue = "KcpeMarks";
                    } else if (additionalCol.getSelectedIndex() == 11) {
                        searchValue = "DateOfBirth";
                    } else if (additionalCol.getSelectedIndex() == 12) {
                        searchValue = "DateofAdmission";
                    } else if (additionalCol.getSelectedIndex() == 13) {
                        searchValue = "program";
                    }


                    groupmodel.addColumn(additionalCol.getSelectedItem());
                    try {
                        int colNumber = groupmodel.getColumnCount() - 1;
                        int rowcounter = groupmodel.getRowCount() - 1;

                        while (rowcounter >= 0) {
                            memberRef = groupmodel.getValueAt(rowcounter, 5).toString();

                            if (additionalCol.getSelectedIndex() > 0) {
                                String sql = "Select " + searchValue + " from admission where AdmissionNumber='" + memberRef + "'";
                                ps = con.prepareStatement(sql);
                                rs = ps.executeQuery();
                                if (rs.next()) {
                                    groupmodel.setValueAt(rs.getString(searchValue), rowcounter, colNumber);
                                } else {
                                    groupmodel.setValueAt("", rowcounter, colNumber);
                                }

                            } else {
                                groupmodel.setValueAt("", rowcounter, colNumber);
                            }

                            rowcounter--;
                        }


                    } catch (Exception sq) {

                        sq.printStackTrace();
                    }


                }
            });


            return holder;
        } catch (SQLException ex) {
            Logger.getLogger(GroupDetails.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(null, ex.getMessage());
        }

        return holder;

    }

}
