/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package highschool;


import java.awt.Color;
import java.awt.HeadlessException;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;

import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;

import static javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE;

import javax.swing.border.TitledBorder;

/**
 * @author FRED_ADMIN
 */
public class Expensis extends JFrame implements ActionListener {

    public static void main(String[] args) {
        new Expensis();
    }

    @SuppressWarnings("FieldMayBeFinal")
    private int heigth = 500;
    private int width = 600;
    private ImageIcon imageIcon;
    private FredLabel name;
    private FredLabel date;
    private FredLabel amount;
    private FredLabel desc;
    private JButton save;
    private JButton cancel;
    private JTextField jName;
    private JTextField jAmount;
    private FredDateChooser jDate;
    private JTextPane pane;
    private PreparedStatement ps;
    private ResultSet rs;
    private DbConnection connect = new DbConnection();
    private IdGenerator generate = new IdGenerator();
    private DataValidation validate = new DataValidation();
    private Connection con;

    @SuppressWarnings({"OverridableMethodCallInConstructor", "LeakingThisInConstructor"})
    public Expensis() {
        setTitle("Expensis Creation Panel");
        setSize(width, heigth);
        setLayout(null);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        getContentPane().setBackground(Color.cyan);
        ((JComponent) getContentPane()).setBorder(
                BorderFactory.createMatteBorder(5, 5, 5, 5, Color.MAGENTA));


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
        name = new FredLabel("Expense Name");
        date = new FredLabel("Date");
        amount = new FredLabel("Cielling Amount");
        desc = new FredLabel("Description");
        jAmount = new JTextField();
        jDate = new FredDateChooser();
        jName = new JTextField();
        pane = new JTextPane();
        pane.setBorder(new TitledBorder("Brief Description"));
        save = new JButton("Save");
        cancel = new JButton("Cancel");
        jDate.setDateFormatString("yyyy/MM/dd");
        name.setBounds(30, 30, 100, 30);
        add(name);
        jName.setBounds(300, 30, 250, 30);
        add(jName);
        date.setBounds(30, 110, 100, 30);
        add(date);
        jDate.setBounds(300, 110, 250, 30);
        add(jDate);
        amount.setBounds(30, 190, 100, 30);
        add(amount);
        jAmount.setBounds(300, 190, 250, 30);
        add(jAmount);
        desc.setBounds(30, 300, 120, 30);
        add(desc);
        pane.setBounds(300, 250, 250, 130);
        add(pane);
        save.setBounds(100, 400, 130, 30);
        add(save);
        cancel.setBounds(350, 400, 130, 30);
        add(cancel);

        save.addActionListener(this);
        cancel.addActionListener(this);

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
            String key = "EX" + IdGenerator.keyGen();
            boolean comply = true;
            if (DataValidation.name(jName.getText()) == false && comply == true) {
                JOptionPane.showMessageDialog(this, "kindly check the Expence Name");
                comply = false;
            } else if (((JTextField) jDate.getDateEditor().getUiComponent()).getText().isEmpty() && comply == true) {
                JOptionPane.showMessageDialog(this, "Kindl pick A valid Date form the Date Picker");
                comply = false;
            } else if (jDate.getDate().after(new Date()) && comply == true) {
                getToolkit().beep();
                JOptionPane.showMessageDialog(null, "Creation Date Value cannot be Future");
                comply = false;

            }

            if (comply == true) {
                if (jAmount.getText().isEmpty()) {
                    int l = JOptionPane.showConfirmDialog(this, "This Contribution would not be limited to Any Amount\n Do you want to proceed and create it", "Check", JOptionPane.YES_NO_OPTION);
                    if (l == JOptionPane.YES_OPTION) {
                        String Name = jName.getText().toUpperCase();
                        String Date = ((JTextField) jDate.getDateEditor().getUiComponent()).getText();
                        String Amount = "0";
                        String Desc = pane.getText();
                        try {
                            String querry = "Insert into expensis values('" + Name + "','" + Date + "','" + Amount + "','" + Desc + "','" + key + "')";
                            ps = con.prepareStatement(querry);
                            ps.execute();
                            JOptionPane.showMessageDialog(this, "Expense Successfully Created", "Success", JOptionPane.INFORMATION_MESSAGE);

                        } catch (HeadlessException | SQLException sq) {
                            JOptionPane.showMessageDialog(this, sq.getMessage());
                        }
                    } else if (l == JOptionPane.NO_OPTION) {
                        JOptionPane.showMessageDialog(this, "Operation Cancelled");
                    }
                } else {

                    String Name = jName.getText().toUpperCase();
                    String Date = ((JTextField) jDate.getDateEditor().getUiComponent()).getText();
                    String Amount = jAmount.getText();
                    String Desc = pane.getText();
                    try {
                        String querry = "Insert into expensis values('" + Name + "','" + Date + "','" + Amount + "','" + Desc + "','" + key + "')";
                        ps = con.prepareStatement(querry);
                        ps.execute();
                        JOptionPane.showMessageDialog(this, "Expense Successfully Created", "Success", JOptionPane.INFORMATION_MESSAGE);
                        jName.setText(null);
                        jAmount.setText(null);
                        pane.setText(null);
                        jDate.setDate(null);

                    } catch (HeadlessException | SQLException sq) {
                        JOptionPane.showMessageDialog(this, sq.getMessage());
                    }

                }
            }

        }
    }

}
