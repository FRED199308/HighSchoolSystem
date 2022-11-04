/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package highschool;


import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import static javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE;

/**
 * @author ExamSeverPc
 */
public class CashBook extends FredFrame implements ActionListener {


    private int height = 400;
    private int width = 700;
    private Connection con = DbConnection.connectDb();
    private PreparedStatement ps;
    private ResultSet rs;
    private FredDateChooser jdate;
    private FredButton save, cancel;
    private FredLabel from = new FredLabel("From: ");
    private FredLabel to = new FredLabel("To: ");
    private FredDateChooser jfrom = new FredDateChooser();
    private FredDateChooser jto = new FredDateChooser();
    public FredLabel acc = new FredLabel("Account");
    public FredCombo jacc = new FredCombo("Select Account");

    public CashBook() {
        save = new FredButton("Generate");
        cancel = new FredButton("Close");
        setSize(width, height);
        setLocationRelativeTo(null);
        setLayout(null);
        setTitle("Cash Book Generation Panel");

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

        try {
            ps = con.prepareStatement("Select accountname from schoolaccounts");
            rs = ps.executeQuery();
            while (rs.next()) {
                jacc.addItem(rs.getString("AccountName"));
            }
        } catch (Exception sq) {
            sq.printStackTrace();
        }

        acc.setBounds(100, 30, 100, 30);
        add(acc);
        jacc.setBounds(250, 30, 300, 30);
        add(jacc);

        from.setBounds(30, 130, 100, 30);
        add(from);
        jfrom.setBounds(150, 130, 180, 30);
        add(jfrom);
        to.setBounds(400, 130, 100, 30);
        add(to);
        jto.setBounds(500, 130, 150, 30);
        add(jto);


        cancel.setBounds(100, 250, 100, 50);
        add(cancel);
        save.setBounds(400, 250, 130, 50);
        add(save);
        cancel.addActionListener(this);
        save.addActionListener(this);
        jto.setDateFormatString("yyyy/MM/dd");
        jfrom.setDateFormatString("yyyy/MM/dd");
        setVisible(true);


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
        } else if (obj == save) {

            String sdate = ((JTextField) jfrom.getDateEditor().getUiComponent()).getText();
            String fdate = ((JTextField) jto.getDateEditor().getUiComponent()).getText();
            if (jacc.getSelectedIndex() == 0) {
                JOptionPane.showMessageDialog(this, "Kindly Select The Account To Generate The CashBook");
            } else {
                if (sdate.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Select The Generation Start Date");
                } else {
                    if (fdate.isEmpty()) {
                        JOptionPane.showMessageDialog(this, "Select The Upper Date");
                    } else {
                        CashBookGenerator.generateReport(jacc.getSelectedItem().toString(), fdate, sdate);

                    }
                }
            }
        }
    }

    public static void main(String[] args) {
        new CashBook();
    }

}
