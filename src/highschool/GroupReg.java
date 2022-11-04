/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package highschool;

/**
 * @author FRED_ADMIN
 */

import javax.swing.JFrame;
import java.awt.Color;
import javax.swing.JLabel;

import javax.swing.JTextField;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Font;
import java.awt.HeadlessException;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JTextPane;

import static javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE;

import javax.swing.border.TitledBorder;

public class GroupReg extends JFrame implements ActionListener {

    @SuppressWarnings("FieldMayBeFinal")
    private int width = 700;
    @SuppressWarnings("FieldMayBeFinal")
    private int height = 500;
    private JLabel name;
    private JLabel desc;
    private JLabel leader;
    private JLabel date;
    //private JLabel time; 
    private JLabel agenda;
    private JLabel capacity;
    private JLabel groupCode;

    private JTextField jName;
    private JTextField jLeader;
    private FredDateChooser jDate;
    private JTextField jAgenda;
    private JTextField jCapacity;
    private JTextPane jDesc;
    private JTextField jGroupCode;

    private FredButton save;
    private FredButton cancel;
    private Font style;

    private Connection con;
    private PreparedStatement ps;
    private DbConnection one = new DbConnection();
    private IdGenerator k = new IdGenerator();

    @SuppressWarnings({"OverridableMethodCallInConstructor", "LeakingThisInConstructor"})
    public GroupReg() {
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        this.addWindowListener(new WindowAdapter() {

            public void windowClosing(WindowEvent e) {
                CurrentFrame.currentWindow();
                e.getWindow().dispose();
            }
        });
        this.setIconImage(FrameProperties.icon());
        ((JComponent) getContentPane()).setBorder(
                BorderFactory.createMatteBorder(5, 5, 5, 5, Color.blue));
        setTitle("Group Registration");
        setSize(width, height);
        setResizable(false);
        setLocationRelativeTo(null);
        setLayout(null);
        getContentPane().setBackground(Color.cyan);
        name = new JLabel("Group Name");
        agenda = new JLabel("Group Agenda");
        date = new JLabel("Formation Date");
        capacity = new JLabel("Maximum Members");
        desc = new JLabel("Brief Group Description");
        leader = new JLabel("Group Leader");
        groupCode = new JLabel("Group Ref No.");
        style = new Font("Serrif", Font.BOLD, (15));

        jName = new JTextField();
        jLeader = new JTextField();
        jDate = new FredDateChooser();
        jAgenda = new JTextField();
        jDesc = new JTextPane();
        jGroupCode = new JTextField();

        jCapacity = new JTextField();
        jDesc = new JTextPane();
        save = new FredButton("Save");
        cancel = new FredButton("cancel");
        jDate.setDateFormatString("yyyy/MM/dd");
        name.setBounds(60, 30, 100, 30);
        jName.setBounds(300, 30, 280, 30);
        groupCode.setBounds(60, 80, 100, 30);
        add(groupCode);
        jGroupCode.setBounds(300, 80, 280, 30);
        add(jGroupCode);
        agenda.setBounds(60, 130, 100, 30);
        jAgenda.setBounds(300, 130, 280, 30);
        date.setBounds(60, 180, 100, 30);
        jDate.setBounds(300, 180, 280, 30);
        capacity.setBounds(60, 230, 150, 30);
        jCapacity.setBounds(300, 230, 280, 30);
        jDesc.setBounds(80, 300, 300, 130);
        save.setBounds(450, 280, 100, 30);
        cancel.setBounds(450, 350, 100, 30);
        save.addActionListener(this);
        cancel.addActionListener(this);

        String key = "GP" + k.keyGen();
        jGroupCode.setText(key);
        jGroupCode.setEditable(false);
        jDesc.setBorder(new TitledBorder("Group Description"));

        add(name);
        add(jName);
        add(date);
        add(jDate);
        add(agenda);
        add(jAgenda);
        add(capacity);
        add(jCapacity);
        add(jDesc);
        add(save);
        add(cancel);
        setVisible(true);
    }

    @SuppressWarnings("CallToPrintStackTrace")
    public void actionPerformed(ActionEvent e) {
        Object obj = e.getSource();

        if (obj == save) {

            String cap = jCapacity.getText();
            @SuppressWarnings("LocalVariableHidesMemberVariable")
            String name = jName.getText().toUpperCase();
            String date = ((JTextField) jDate.getDateEditor().getUiComponent()).getText();
            String code = jGroupCode.getText().toUpperCase();
            String description = jDesc.getText();
            String agenda = jAgenda.getText().toUpperCase();

            boolean comply = true;
            if (DataValidation.name(jName.getText()) == false && comply == true) {
                getToolkit().beep();
                comply = false;
                JOptionPane.showMessageDialog(this, "Kindly Input A valid Grop Name", "Invalid Group Name", JOptionPane.ERROR_MESSAGE);
            }
            if (DataValidation.number2(jCapacity.getText()) == false && comply == true) {
                getToolkit().beep();
                comply = false;
                JOptionPane.showMessageDialog(this, "Kindly Input A valid Capacity Figure", "Invalid input", JOptionPane.ERROR_MESSAGE);
            } else if (comply == true && jDate.getDate().after(new Date())) {
                getToolkit().beep();
                JOptionPane.showMessageDialog(null, "Cannot Register on Future Date Value ");
                comply = false;

            }

            if (comply == true) {

                try {
                    String querry = "insert into groups values('" + name + "','" + agenda + "','" + date + "','" + cap + "','" + description + "','" + code + "') ";
                    con = one.connectDb();
                    String sql = "Select groupname from groups where groupname='" + jName.getText() + "'";
                    ResultSet rs;
                    ps = con.prepareStatement(sql);
                    rs = ps.executeQuery();
                    if (rs.next()) {
                        JOptionPane.showMessageDialog(this, "A Group Registered With The Name: " + jName.getText() + " Already Exist\n Kindly Input a new Group Name\n Or Delete The Existing One", "Duplicate Prevention", JOptionPane.ERROR_MESSAGE);
                    } else {

                        ps = con.prepareStatement(querry);
                        ps.execute();
                        JOptionPane.showMessageDialog(GroupReg.this, "Group Registration Successful", "Success", JOptionPane.INFORMATION_MESSAGE);
                        String q = "GP" + k.keyGen();
                        jGroupCode.setText(q);
                        jName.setText(null);
                        jAgenda.setText(null);
                        jDesc.setText(null);
                        jDate.setDate(null);
                        jCapacity.setText(null);

                    }

                } catch (HeadlessException | SQLException sq) {

                    JOptionPane.showMessageDialog(GroupReg.this, sq, "failed", JOptionPane.ERROR_MESSAGE);
                }

            }

        }
        if (obj == cancel) {
            CurrentFrame.currentWindow();
            dispose();
        }

    }

    public static void main(String[] args) {
        new GroupReg();
    }
}
