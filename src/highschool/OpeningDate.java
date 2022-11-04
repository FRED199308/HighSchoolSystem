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
import javax.swing.JTextField;

import static javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE;

/**
 * @author FRED
 */
public class OpeningDate extends JFrame implements ActionListener {


    private int height = 320;
    private int width = 500;
    private FredLabel term = new FredLabel("Term");
    private FredLabel opDate = new FredLabel("Opening Date");
    private FredLabel clossingDate = new FredLabel("Clossing Date");
    private FredLabel academicYear = new FredLabel("Academic Year");
    private FredDateChooser jOpDate = new FredDateChooser();
    private FredDateChooser jcdate = new FredDateChooser();
    private FredCombo jterm, jAcademicYear;
    private FredCombo jcountry;
    private FredTextField jward;
    private FredButton save = new FredButton("Save");
    private PreparedStatement ps;
    private Connection con;
    ResultSet rs;

    public OpeningDate() {
        setSize(width, height);
        setLocationRelativeTo(null);
        setLayout(null);
        setResizable(false);
        setTitle("Term Dates Configuration Panel");
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
        con = DbConnection.connectDb();
        this.setIconImage(FrameProperties.icon());
        ((JComponent) getContentPane()).setBorder(
                BorderFactory.createMatteBorder(5, 5, 5, 5, Color.MAGENTA));
        jterm = new FredCombo("Select Term");
        jAcademicYear = new FredCombo("Select Academic Year");

        try {
            String sql9 = "Select* from terms";
            ps = con.prepareStatement(sql9);
            rs = ps.executeQuery();
            while (rs.next()) {
                jterm.addItem(rs.getString("TermName"));

            }
            for (int k = 2015; k <= Globals.academicYear() + 1; ++k) {
                jAcademicYear.addItem(k);
            }
        } catch (Exception sq) {
            sq.printStackTrace();
        }

        term.setBounds(30, 30, 150, 30);
        add(term);
        jterm.setBounds(200, 30, 200, 30);
        add(jterm);
        academicYear.setBounds(30, 80, 150, 30);
        add(academicYear);
        jAcademicYear.setBounds(200, 80, 200, 30);
        add(jAcademicYear);
        opDate.setBounds(30, 130, 150, 30);
        add(opDate);
        jOpDate.setBounds(200, 130, 200, 30);
        add(jOpDate);
        clossingDate.setBounds(30, 180, 150, 30);
        add(clossingDate);
        jcdate.setBounds(200, 180, 200, 30);
        add(jcdate);

        save.setBounds(150, 230, 130, 30);
        add(save);
        jcdate.setDateFormatString("yyyy/MM/dd");
        jOpDate.setDateFormatString("yyy/MM/dd");
        save.addActionListener(this);
        jAcademicYear.setSelectedItem(Globals.academicYear());
        jterm.setSelectedItem(Globals.currentTermName());
        setVisible(true);
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        Object obj = e.getSource();
        if (obj == save) {

            String odate = ((JTextField) jOpDate.getDateEditor().getUiComponent()).getText();
            String cdate = ((JTextField) jcdate.getDateEditor().getUiComponent()).getText();

            if (jterm.getSelectedIndex() == 0) {
                JOptionPane.showMessageDialog(this, "Select The Term To Configure Its Opening Date");
            } else {
                if (jAcademicYear.getSelectedIndex() == 0) {
                    JOptionPane.showMessageDialog(this, "Select Academic Year");
                } else {
                    if (odate.isEmpty()) {
                        JOptionPane.showMessageDialog(this, "Select The Term Opening Date");

                    } else {
                        if (cdate.isEmpty()) {
                            JOptionPane.showMessageDialog(this, "Select The Term Clossing Date");
                        } else {

                            if (jcdate.getDate().before(jOpDate.getDate())) {
                                JOptionPane.showMessageDialog(this, "Clossing Date Can Never Come Before The Opening Date Of A Term");
                            } else {

                                try {
                                    String sql1 = "Select termcode  from termdates where termcode='" + Globals.termcode(jterm.getSelectedItem().toString()) + "' and academicyear='" + jAcademicYear.getSelectedItem() + "'";
                                    ps = con.prepareStatement(sql1);
                                    rs = ps.executeQuery();
                                    if (rs.next()) {
                                        int option = JOptionPane.showConfirmDialog(this, "This Term Has Its Dates Already Configured\n Do You Want To Update To The New Term Dates??", "Confirm Term Dates Change", JOptionPane.YES_NO_OPTION);
                                        if (option == JOptionPane.YES_OPTION) {
                                            String sql = "update termdates set openingDate='" + odate + "',clossingDate='" + cdate + "' where termcode= '" + Globals.termcode(jterm.getSelectedItem().toString()) + "' and academicyear='" + jAcademicYear.getSelectedItem() + "'";
                                            ps = con.prepareStatement(sql);
                                            ps.execute();
                                            JOptionPane.showMessageDialog(this, "Term Dates Updated Successfully");
                                        } else {

                                        }
                                    } else {
                                        String sql = "Insert into termdates values('" + Globals.termcode(jterm.getSelectedItem().toString()) + "','" + jAcademicYear.getSelectedItem() + "','" + odate + "','" + cdate + "')";
                                        ps = con.prepareStatement(sql);
                                        ps.execute();
                                        JOptionPane.showMessageDialog(this, "Term Dates Configured Successfully");
                                    }


                                } catch (Exception sq) {
                                    JOptionPane.showMessageDialog(this, sq.getMessage());
                                }
                            }
                        }

                    }
                }
            }

        }
    }

    public static void main(String[] args) {
        new OpeningDate();
    }
}
