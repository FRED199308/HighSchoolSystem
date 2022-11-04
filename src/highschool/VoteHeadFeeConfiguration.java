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
import java.sql.SQLException;
import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import static javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE;

import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;

/**
 * @author ExamSeverPc
 */
public class VoteHeadFeeConfiguration extends JFrame implements ActionListener {


    private int width = 800;
    private int height = 550;
    private FredCheckBox bpro = new FredCheckBox("Boarding");
    private FredCheckBox dpro = new FredCheckBox("Day");
    private FredCombo cllass = new FredCombo("Select Class,Leave Unselected To Apply To All");
    private Connection con = DbConnection.connectDb();

    private PreparedStatement ps;
    private ResultSet rs;
    private DefaultTableModel model = new DefaultTableModel() {
        @Override
        public boolean isCellEditable(int row, int col) {
            return false;
        }
    };
    ;
    private JTable tab = new JTable();
    private JScrollPane pane = new JScrollPane(tab);
    private Object cols[] = {"VoteHeadName", "VoteHead Ref No", "AcademicYear", "Term", "Amount", "Priority", "Class", "Program"};
    private Object row[] = new Object[cols.length];
    private FredLabel amount = new FredLabel("VoteHead Amount");
    private FredTextField jamount = new FredTextField();
    private FredButton edit = new FredButton("Update");
    private FredButton cancel = new FredButton("Close");
    private FredLabel priority = new FredLabel("Vote Head Prority");
    private FredTextField jpriority = new FredTextField();
    private FredCombo jacademicYear = new FredCombo("Select Academic Year");
    private FredCombo jterm = new FredCombo("Select Term");
    private FredLabel academicYear = new FredLabel("Academic Year");
    private FredLabel term = new FredLabel("Term");
    private FredLabel votehead = new FredLabel("VoteHead");
    private FredCombo jvotehead = new FredCombo("Select VoteHead");
    private FredButton refresh = new FredButton("Refresh");


    public VoteHeadFeeConfiguration() {

        setSize(width, height);
        setTitle("Vote Head Fee Payable Configuration");
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
        model.setColumnIdentifiers(cols);
        tab.setModel(model);
        try {
            String sql = "Select * from classes where precision1<5";
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                cllass.addItem(rs.getString("Classname"));
            }
            ps = con.prepareStatement("Select StudentPayableVoteHeads.voteheadid,VoteHeadName,classcode,program,priority,AcademicYear,TermCode,AmountPerHead from StudentPayableVoteHeads,voteheads where voteheads.voteheadid=StudentPayableVoteHeads.voteheadid  and academicYear='" + Globals.academicYear() + "' order by academicyear desc");
            rs = ps.executeQuery();
            while (rs.next()) {
                row[0] = rs.getString("VoteHeadName");
                row[1] = rs.getString("VoteHeadId");
                row[2] = rs.getString("AcademicYear");
                row[3] = Globals.termname(rs.getString("TermCode"));
                row[4] = rs.getString("AmountPerHead");
                row[5] = rs.getString("priority");
                row[6] = Globals.className(rs.getString("Classcode"));
                row[7] = rs.getString("program");
                model.addRow(row);
            }
            String sql9 = "Select* from terms";
            ps = con.prepareStatement(sql9);
            rs = ps.executeQuery();
            while (rs.next()) {
                jterm.addItem(rs.getString("TermName"));

            }
            for (int k = 2015; k <= Globals.academicYear() + 1; ++k) {
                jacademicYear.addItem(k);
            }

            ps = con.prepareStatement("Select VoteheadName from voteheads where payableasfee='" + "1" + "'");
            rs = ps.executeQuery();
            while (rs.next()) {
                jvotehead.addItem(rs.getString("VoteheadName"));
            }

        } catch (Exception sq) {
            sq.printStackTrace();
        }


        pane.setBounds(20, 30, 750, 250);
        add(pane);
        votehead.setBounds(30, 300, 150, 30);
        add(votehead);
        jvotehead.setBounds(120, 300, 200, 30);
        add(jvotehead);
        dpro.setBounds(350, 300, 70, 30);
        add(dpro);
        bpro.setBounds(420, 300, 80, 30);
        add(bpro);
        cllass.setBounds(505, 300, 280, 50);
        add(cllass);

        amount.setBounds(50, 350, 150, 30);
        add(amount);
        jamount.setBounds(200, 350, 200, 30);
        add(jamount);
        priority.setBounds(420, 350, 150, 30);
        add(priority);
        jpriority.setBounds(600, 350, 100, 30);
        add(jpriority);
        cancel.setBounds(150, 430, 100, 30);
        add(cancel);

        cllass.setBorder(new TitledBorder("Leave unselected to Aplly To all classes"));
        academicYear.setBounds(50, 420, 150, 30);
        add(academicYear);
        jacademicYear.setBounds(200, 420, 200, 30);
        add(jacademicYear);
        term.setBounds(420, 420, 150, 30);
        add(term);
        jterm.setBounds(600, 420, 100, 30);
        add(jterm);
        cancel.setBounds(150, 470, 100, 30);
        add(cancel);
        edit.setBounds(420, 470, 100, 30);
        add(edit);
        refresh.setBounds(600, 470, 100, 30);
        add(refresh);
        cancel.addActionListener(this);
        edit.addActionListener(this);
        refresh.addActionListener(this);
        jamount.addKeyListener(new KeyAdapter() {
            public void keyTyped(KeyEvent key) {

                char c = key.getKeyChar();
                if (Character.isAlphabetic(c)) {
                    key.consume();

                }

            }
        });
        tab.addMouseListener(new MouseAdapter() {

            public void mouseClicked(MouseEvent ev) {

                jamount.setText(model.getValueAt(tab.getSelectedRow(), 4).toString());
                jpriority.setText(model.getValueAt(tab.getSelectedRow(), 5).toString());

                jacademicYear.setSelectedItem(model.getValueAt(tab.getSelectedRow(), 2).toString());
                jterm.setSelectedItem(model.getValueAt(tab.getSelectedRow(), 3).toString());
                jvotehead.setSelectedItem(model.getValueAt(tab.getSelectedRow(), 0).toString());
            }
        });
        setVisible(true);
        jvotehead.addActionListener(this);

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object obj = e.getSource();
        if (obj == cancel) {
            try {
                con.close();
            } catch (SQLException sq) {

            }
            CurrentFrame.currentWindow();
            dispose();
        } else if (obj == refresh) {

            model.getDataVector().removeAllElements();

            try {


                ps = con.prepareStatement("Select StudentPayableVoteHeads.voteheadid,VoteHeadName,classcode,program,priority,AcademicYear,TermCode,AmountPerHead from StudentPayableVoteHeads,voteheads where voteheads.voteheadid=StudentPayableVoteHeads.voteheadid order by academicyear desc");
                rs = ps.executeQuery();
                while (rs.next()) {
                    row[0] = rs.getString("VoteHeadName");
                    row[1] = rs.getString("VoteHeadId");
                    row[2] = rs.getString("AcademicYear");
                    row[3] = Globals.termname(rs.getString("TermCode"));
                    row[4] = rs.getString("AmountPerHead");
                    row[5] = rs.getString("priority");
                    row[6] = Globals.className(rs.getString("Classcode"));
                    row[7] = rs.getString("program");
                    model.addRow(row);
                }

            } catch (Exception sq) {
                sq.printStackTrace();
            }

        } else if (obj == jvotehead) {
            if (jvotehead.getSelectedIndex() > 0) {
                model.getDataVector().removeAllElements();
                String voteheadid = Globals.voteHeadId(jvotehead.getSelectedItem().toString());

                try {

                    ps = con.prepareStatement("Select StudentPayableVoteHeads.voteheadid,VoteHeadName,priority,classcode,program,AcademicYear,TermCode,AmountPerHead from StudentPayableVoteHeads,voteheads where voteheads.voteheadid=StudentPayableVoteHeads.voteheadid and StudentPayableVoteHeads.voteheadid='" + voteheadid + "' order by academicyear desc");
                    rs = ps.executeQuery();
                    while (rs.next()) {
                        row[0] = rs.getString("VoteHeadName");
                        row[1] = rs.getString("VoteHeadId");
                        row[2] = rs.getString("AcademicYear");
                        row[3] = Globals.termname(rs.getString("TermCode"));
                        row[4] = rs.getString("AmountPerHead");
                        row[5] = rs.getString("priority");
                        row[6] = Globals.className(rs.getString("Classcode"));
                        row[7] = rs.getString("program");
                        model.addRow(row);
                    }


                } catch (Exception sq) {
                    sq.printStackTrace();
                }

            }

        } else if (obj == edit) {
            if (jamount.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Kindly Input A Valid Amount");
            } else {

                if (jvotehead.getSelectedIndex() == 0) {
                    JOptionPane.showMessageDialog(this, "Select VoteHead");
                } else {
                    if (jacademicYear.getSelectedIndex() == 0) {
                        JOptionPane.showMessageDialog(this, "Select Academic Year");
                    } else {
                        if (jterm.getSelectedIndex() == 0) {
                            JOptionPane.showMessageDialog(this, "Select The Term");
                        } else {
                            if (jpriority.getText().isEmpty()) {
                                JOptionPane.showMessageDialog(this, "Kindly Input A Valid Priority Number");
                            } else {
                                if (dpro.isSelected() || bpro.isSelected()) {


                                    String vid = Globals.voteHeadId(jvotehead.getSelectedItem().toString());
                                    String term = Globals.termcode(jterm.getSelectedItem().toString());
                                    String year = jacademicYear.getSelectedItem().toString();
                                    String program = "";
                                    if (dpro.isSelected()) {
                                        program = "Day Program";
                                    }
                                    if (bpro.isSelected()) {

                                        program = "Boarding Program";

                                    }
                                    if (bpro.isSelected() && dpro.isSelected()) {

                                        program = "All";

                                    }
                                    try {


                                        if (program.equalsIgnoreCase("All")) {

                                            //begining of boarding intialisation
                                            if (cllass.getSelectedIndex() > 0) {

                                                String Class = cllass.getSelectedItem().toString();

                                                ps = con.prepareStatement("Select voteheadid from StudentPayableVoteHeads where academicyear='" + year + "' and termcode='" + term + "' and voteheadid='" + vid + "' and classcode='" + Globals.classCode(cllass.getSelectedItem().toString()) + "' and program='" + "Boarding Program" + "'");
                                                rs = ps.executeQuery();
                                                if (rs.next()) {


                                                    String AMOUNT = "";
                                                    if (jamount.getText().isEmpty() || jamount.getText().equalsIgnoreCase("0")) {
                                                        AMOUNT = "0";
                                                    } else {
                                                        AMOUNT = jamount.getText();
                                                    }

                                                    ps = con.prepareStatement("Update StudentPayableVoteHeads set AmountPerHead='" + AMOUNT + "',priority='" + jpriority.getText() + "' where voteheadid='" + vid + "' and academicyear='" + year + "' and termcode='" + term + "' and classcode='" + Globals.classCode(Class) + "' and program='" + "Boarding Program" + "'");
                                                    ps.execute();

                                                    JOptionPane.showMessageDialog(this, "New Amount Affected Succesfully");
                                                    jamount.setText("");
                                                    jacademicYear.setSelectedIndex(0);
                                                    jterm.setSelectedIndex(0);
                                                    jpriority.setText("");
                                                    cllass.setSelectedIndex(0);


                                                } else {

                                                    String AMOUNT = "";
                                                    if (jamount.getText().isEmpty() || jamount.getText().equalsIgnoreCase("0")) {
                                                        AMOUNT = "0";
                                                    } else {
                                                        AMOUNT = jamount.getText();
                                                    }

                                                    ps = con.prepareStatement("Insert into StudentPayableVoteHeads values('" + vid + "','" + AMOUNT + "','" + year + "','" + term + "','" + jpriority.getText() + "','" + Globals.classCode(Class) + "','" + "Boarding Program" + "')");
                                                    ps.execute();
                                                }


                                            } else {


                                                String sql = "Select * from classes where precision1<5";
                                                ps = con.prepareStatement(sql);
                                                ResultSet rx = ps.executeQuery();
                                                while (rx.next()) {


                                                    String Class = rx.getString("Classname");

                                                    ps = con.prepareStatement("Select voteheadid from StudentPayableVoteHeads where academicyear='" + year + "' and termcode='" + term + "' and voteheadid='" + vid + "' and classcode='" + Globals.classCode(Class) + "' and program='" + "Boarding Program" + "'");
                                                    rs = ps.executeQuery();
                                                    if (rs.next()) {


                                                        String AMOUNT = "";
                                                        if (jamount.getText().isEmpty() || jamount.getText().equalsIgnoreCase("0")) {
                                                            AMOUNT = "0";
                                                        } else {
                                                            AMOUNT = jamount.getText();
                                                        }

                                                        ps = con.prepareStatement("Update StudentPayableVoteHeads set AmountPerHead='" + AMOUNT + "',priority='" + jpriority.getText() + "' where voteheadid='" + vid + "' and academicyear='" + year + "' and termcode='" + term + "' and classcode='" + Globals.classCode(Class) + "' and program='" + "Boarding Program" + "'");
                                                        ps.execute();


                                                    } else {

                                                        String AMOUNT = "";
                                                        if (jamount.getText().isEmpty() || jamount.getText().equalsIgnoreCase("0")) {
                                                            AMOUNT = "0";
                                                        } else {
                                                            AMOUNT = jamount.getText();
                                                        }

                                                        ps = con.prepareStatement("Insert into StudentPayableVoteHeads values('" + vid + "','" + AMOUNT + "','" + year + "','" + term + "','" + jpriority.getText() + "','" + Globals.classCode(Class) + "','" + "Boarding Program" + "')");
                                                        ps.execute();

                                                    }


                                                }

                                            }//end of boarding intialisation

                                            // beginning day program intialisation


                                            if (cllass.getSelectedIndex() > 0) {

                                                String Class = cllass.getSelectedItem().toString();

                                                ps = con.prepareStatement("Select voteheadid from StudentPayableVoteHeads where academicyear='" + year + "' and termcode='" + term + "' and voteheadid='" + vid + "' and classcode='" + Globals.classCode(cllass.getSelectedItem().toString()) + "' and program='" + "Day Program" + "'");
                                                rs = ps.executeQuery();
                                                if (rs.next()) {


                                                    String AMOUNT = "";
                                                    if (jamount.getText().isEmpty() || jamount.getText().equalsIgnoreCase("0")) {
                                                        AMOUNT = "0";
                                                    } else {
                                                        AMOUNT = jamount.getText();
                                                    }

                                                    ps = con.prepareStatement("Update StudentPayableVoteHeads set AmountPerHead='" + AMOUNT + "',priority='" + jpriority.getText() + "' where voteheadid='" + vid + "' and academicyear='" + year + "' and termcode='" + term + "' and classcode='" + Globals.classCode(Class) + "' and program='" + "Day Program" + "'");
                                                    ps.execute();


                                                } else {

                                                    String AMOUNT = "";
                                                    if (jamount.getText().isEmpty() || jamount.getText().equalsIgnoreCase("0")) {
                                                        AMOUNT = "0";
                                                    } else {
                                                        AMOUNT = jamount.getText();
                                                    }

                                                    ps = con.prepareStatement("Insert into StudentPayableVoteHeads values('" + vid + "','" + AMOUNT + "','" + year + "','" + term + "','" + jpriority.getText() + "','" + Globals.classCode(Class) + "','" + "Day Program" + "')");
                                                    ps.execute();

                                                }


                                            } else {


                                                String sql = "Select * from classes where precision1<5";
                                                ps = con.prepareStatement(sql);
                                                ResultSet rx = ps.executeQuery();
                                                while (rx.next()) {


                                                    String Class = rx.getString("Classname");

                                                    ps = con.prepareStatement("Select voteheadid from StudentPayableVoteHeads where academicyear='" + year + "' and termcode='" + term + "' and voteheadid='" + vid + "' and classcode='" + Globals.classCode(Class) + "' and program='" + "Day Program" + "'");
                                                    rs = ps.executeQuery();
                                                    if (rs.next()) {


                                                        String AMOUNT = "";
                                                        if (jamount.getText().isEmpty() || jamount.getText().equalsIgnoreCase("0")) {
                                                            AMOUNT = "0";
                                                        } else {
                                                            AMOUNT = jamount.getText();
                                                        }

                                                        ps = con.prepareStatement("Update StudentPayableVoteHeads set AmountPerHead='" + AMOUNT + "',priority='" + jpriority.getText() + "' where voteheadid='" + vid + "' and academicyear='" + year + "' and termcode='" + term + "' and classcode='" + Globals.classCode(Class) + "' and program='" + "Day Program" + "'");
                                                        ps.execute();


                                                    } else {

                                                        String AMOUNT = "";
                                                        if (jamount.getText().isEmpty() || jamount.getText().equalsIgnoreCase("0")) {
                                                            AMOUNT = "0";
                                                        } else {
                                                            AMOUNT = jamount.getText();
                                                        }

                                                        ps = con.prepareStatement("Insert into StudentPayableVoteHeads values('" + vid + "','" + AMOUNT + "','" + year + "','" + term + "','" + jpriority.getText() + "','" + Globals.classCode(Class) + "','" + "Day Program" + "')");
                                                        ps.execute();

                                                    }


                                                }


                                            }
                                            ///end of day program intialisation


                                            jamount.setText("");
                                            jacademicYear.setSelectedIndex(0);
                                            jterm.setSelectedIndex(0);
                                            jpriority.setText("");
                                            cllass.setSelectedIndex(0);
                                            JOptionPane.showMessageDialog(this, "VoteHead Configured Successfully");


                                        } else {

                                            //single program intialisation 


                                            if (cllass.getSelectedIndex() > 0) {

                                                String Class = cllass.getSelectedItem().toString();

                                                ps = con.prepareStatement("Select voteheadid from StudentPayableVoteHeads where academicyear='" + year + "' and termcode='" + term + "' and voteheadid='" + vid + "' and classcode='" + Globals.classCode(cllass.getSelectedItem().toString()) + "' and program='" + program + "'");
                                                rs = ps.executeQuery();
                                                if (rs.next()) {


                                                    String AMOUNT = "";
                                                    if (jamount.getText().isEmpty() || jamount.getText().equalsIgnoreCase("0")) {
                                                        AMOUNT = "0";
                                                    } else {
                                                        AMOUNT = jamount.getText();
                                                    }

                                                    ps = con.prepareStatement("Update StudentPayableVoteHeads set AmountPerHead='" + AMOUNT + "',priority='" + jpriority.getText() + "' where voteheadid='" + vid + "' and academicyear='" + year + "' and termcode='" + term + "' and classcode='" + Globals.classCode(Class) + "' and program='" + program + "'");
                                                    ps.execute();


                                                } else {

                                                    String AMOUNT = "";
                                                    if (jamount.getText().isEmpty() || jamount.getText().equalsIgnoreCase("0")) {
                                                        AMOUNT = "0";
                                                    } else {
                                                        AMOUNT = jamount.getText();
                                                    }

                                                    ps = con.prepareStatement("Insert into StudentPayableVoteHeads values('" + vid + "','" + AMOUNT + "','" + year + "','" + term + "','" + jpriority.getText() + "','" + Globals.classCode(Class) + "','" + program + "')");
                                                    ps.execute();

                                                }


                                            } else {


                                                String sql = "Select * from classes where precision1<5";
                                                ps = con.prepareStatement(sql);
                                                ResultSet rx = ps.executeQuery();
                                                while (rx.next()) {


                                                    String Class = rx.getString("Classname");

                                                    ps = con.prepareStatement("Select voteheadid from StudentPayableVoteHeads where academicyear='" + year + "' and termcode='" + term + "' and voteheadid='" + vid + "' and classcode='" + Globals.classCode(Class) + "' and program='" + program + "'");
                                                    rs = ps.executeQuery();
                                                    if (rs.next()) {


                                                        String AMOUNT = "";
                                                        if (jamount.getText().isEmpty() || jamount.getText().equalsIgnoreCase("0")) {
                                                            AMOUNT = "0";
                                                        } else {
                                                            AMOUNT = jamount.getText();
                                                        }

                                                        ps = con.prepareStatement("Update StudentPayableVoteHeads set AmountPerHead='" + AMOUNT + "',priority='" + jpriority.getText() + "' where voteheadid='" + vid + "' and academicyear='" + year + "' and termcode='" + term + "' and classcode='" + Globals.classCode(Class) + "' and program='" + program + "'");
                                                        ps.execute();


                                                    } else {

                                                        String AMOUNT = "";
                                                        if (jamount.getText().isEmpty() || jamount.getText().equalsIgnoreCase("0")) {
                                                            AMOUNT = "0";
                                                        } else {
                                                            AMOUNT = jamount.getText();
                                                        }

                                                        ps = con.prepareStatement("Insert into StudentPayableVoteHeads values('" + vid + "','" + AMOUNT + "','" + year + "','" + term + "','" + jpriority.getText() + "','" + Globals.classCode(Class) + "','" + program + "')");
                                                        ps.execute();
                                                    }


                                                }


                                            }


                                            jamount.setText("");
                                            jacademicYear.setSelectedIndex(0);
                                            jterm.setSelectedIndex(0);
                                            jpriority.setText("");
                                            cllass.setSelectedIndex(0);
                                            JOptionPane.showMessageDialog(this, "VoteHead Configured Successfully");
                                        }


                                    } catch (Exception sq) {
                                        sq.printStackTrace();
                                    }


                                } else {

                                    JOptionPane.showMessageDialog(this, "ATleast One program must be selected or Both for Votehead Configuration");
                                }


                            }


                        }
                    }
                }


            }
        }
    }

    public static void main(String[] args) {
        new VoteHeadFeeConfiguration();
    }
}
