/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package highschool;


import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.swing.BorderFactory;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.border.TitledBorder;

/**
 * @author FRED_ADMIN
 */
public class GroupEdit extends JFrame implements ActionListener {

    public static String groupid = "";
    private int width = 650;
    private int height = 500;
    private PreparedStatement ps;
    private ResultSet rs;

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

    private DbConnection db = new DbConnection();
    private IdGenerator k = new IdGenerator();

    public GroupEdit() {

        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        setTitle("Group Registration");
        setSize(width, height);
        setResizable(false);
        setLocationRelativeTo(null);
        setLayout(null);
        getContentPane().setBackground(Color.cyan);
        this.addWindowListener(new WindowAdapter() {

            public void windowClosing(WindowEvent e) {
                CurrentFrame.currentWindow();
                e.getWindow().dispose();
            }
        });
        this.setIconImage(FrameProperties.icon());
        ((JComponent) getContentPane()).setBorder(
                BorderFactory.createMatteBorder(5, 5, 5, 5, Color.MAGENTA));
        name = new JLabel("Group Name");
        agenda = new JLabel("Group Agenda");
        date = new JLabel("Formation Date");
        capacity = new JLabel("Maximum Members");
        desc = new JLabel("Brief Group Description");
        leader = new JLabel("Group Leader");
        groupCode = new JLabel("Group Code");
        style = new Font("Serrif", Font.BOLD, (15));
        String[] option = {"Select Group"};

        jName = new JTextField();
        jLeader = new JTextField();
        jDate = new FredDateChooser();
        jAgenda = new JTextField();
        jDesc = new JTextPane();
        jGroupCode = new JTextField();

        jCapacity = new JTextField();
        jDesc = new JTextPane();
        save = new FredButton("Update");
        cancel = new FredButton("cancel");
        jDate.setDateFormatString("yyyy/MM/dd");
        name.setBounds(60, 30, 100, 40);
        jName.setBounds(180, 30, 230, 35);
        groupCode.setBounds(60, 80, 100, 35);
        add(groupCode);
        jGroupCode.setBounds(180, 80, 230, 35);
        add(jGroupCode);
        agenda.setBounds(60, 130, 100, 40);
        jAgenda.setBounds(180, 130, 230, 35);
        date.setBounds(60, 180, 100, 40);
        jDate.setBounds(180, 180, 230, 35);
        capacity.setBounds(60, 230, 100, 40);
        jCapacity.setBounds(180, 230, 230, 35);
        jDesc.setBounds(80, 300, 300, 130);
        save.setBounds(450, 250, 100, 40);
        cancel.setBounds(450, 350, 100, 40);

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

        try {

            con = db.connectDb();

            save.setEnabled(true);
            try {

                String querry2 = "Select* from groups where groupcode='" + groupid + "'";
                ps = con.prepareStatement(querry2);
                rs = ps.executeQuery();
                while (rs.next()) {
                    String name = rs.getString("GroupName");
                    String groupCode = rs.getString("groupCode");
                    String agenda = rs.getString("GroupAgenda");

                    String capacity = Integer.toString(rs.getInt("Capacity"));
                    String desc = rs.getString("Description");
                    // String leader=rs.getString("member")

                    jName.setText(name);
                    jGroupCode.setText(groupCode);
                    jAgenda.setText(agenda);
                    jCapacity.setText(capacity);
                    jDesc.setText(desc);
                    jDate.setDate(rs.getDate("FormationDate"));

                }
            } catch (Exception sq) {
                JOptionPane.showMessageDialog(null, sq.getMessage());
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e, "consult Admin", JOptionPane.ERROR_MESSAGE);

        }

    }

    public void actionPerformed(ActionEvent e) {
        Object obj = e.getSource();
        if (obj == save) {

            String cap = jCapacity.getText();
            String name = jName.getText().toUpperCase();
            String date = ((JTextField) jDate.getDateEditor().getUiComponent()).getText();
            String code = jGroupCode.getText().toUpperCase();
            String description = jDesc.getText();
            String agenda = jAgenda.getText().toUpperCase();

            try {
                //String querry="insert into groups values('"+name+"','"+agenda+"','"+date+"','"+cap+"','"+description+"','"+code+"') ";
                String querry2 = "update groups set GroupName='" + name + "',GroupAgenda='" + agenda + "',FormationDate='" + date + "',"
                        + "Capacity='" + cap + "',Description='" + description + "'"
                        + " where groupcode='" + code + "'";
                con = db.connectDb();
                ps = con.prepareStatement(querry2);
                ps.execute();
                JOptionPane.showMessageDialog(this, "Details Updated successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
                String q = "GP" + k.keyGen();
                jGroupCode.setText(q);
                jName.setText(null);
                jAgenda.setText(null);
                jDesc.setText(null);
                jDate.setDate(null);
                jCapacity.setText(null);

            } catch (Exception sq) {

                JOptionPane.showMessageDialog(this, sq, "failed", JOptionPane.ERROR_MESSAGE);
            }

        } else {
            CurrentFrame.currentWindow();
            dispose();
        }
        ;
    }

    public static void main(String[] obj) {
        new GroupEdit();
    }
}
