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

import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.JTable;

import static javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE;

import javax.swing.table.DefaultTableModel;

/**
 * @author FRED
 */
public class PhotoViewer extends JFrame implements ActionListener {

    public static void main(String[] args) {
        new PhotoViewer();
    }

    private int height = 430;
    private int width = 700;
    private FredButton cancel, print;
    private FredLabel classname, stream, gender;
    private FredCombo jclassname, jstream, jgender;
    private Connection con;
    PreparedStatement ps;
    ResultSet rs;
    private DefaultTableModel model;
    private JTable tab;
    private Object cols[] = {"No", "Name", "Class", "Stream"};
    private JMenuBar bar;
    private Object row[] = new Object[cols.length];

    public PhotoViewer() {
        setSize(width, height);
        setLocationRelativeTo(null);
        setLayout(null);
        bar = new JMenuBar();
        setResizable(false);
        setTitle("Class Photo Generation Window");
        getContentPane().setBackground(Color.CYAN);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        this.addWindowListener(new WindowAdapter() {

            @Override
            public void windowClosing(WindowEvent e) {
                CurrentFrame.currentWindow();
                e.getWindow().dispose();
            }
        });
        con = DbConnection.connectDb();
        cancel = new FredButton("Close");
        tab = new JTable();
        model = new DefaultTableModel();
        model.setColumnIdentifiers(cols);
        tab.setModel(model);
        print = new FredButton("Generate Images");
        classname = new FredLabel("Class");
        stream = new FredLabel("Stream");
        jclassname = new FredCombo("Select Form");
        jstream = new FredCombo("Select Stream");
        gender = new FredLabel("Separate Girls From Boys");
        jgender = new FredCombo("Sort By Gender");
        this.setIconImage(FrameProperties.icon());
        ((JComponent) getContentPane()).setBorder(
                BorderFactory.createMatteBorder(5, 5, 5, 5, Color.MAGENTA));
        bar.setBounds(5, 0, 685, 30);
        add(bar);
        classname.setBounds(30, 70, 150, 30);
        add(classname);
        jclassname.setBounds(300, 70, 300, 30);
        add(jclassname);
        stream.setBounds(30, 170, 150, 30);
        add(stream);
        jstream.setBounds(300, 170, 300, 30);
        add(jstream);
        gender.setBounds(30, 240, 200, 30);
        add(gender);
        jgender.setBounds(300, 240, 300, 30);
        add(jgender);
        cancel.setBounds(100, 330, 130, 30);
        add(cancel);
        print.setBounds(420, 330, 150, 30);
        add(print);
        jgender.addItem("Male");
        jgender.addItem("Female");
        try {
            String sql = "Select * from classes where precision1<5";
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                jclassname.addItem(rs.getString("Classname"));
            }
            String sql2 = "Select Streamname from streams ";
            ps = con.prepareStatement(sql2);
            rs = ps.executeQuery();
            while (rs.next()) {
                jstream.addItem(rs.getString("StreamName"));
            }
        } catch (SQLException sq) {
            JOptionPane.showMessageDialog(this, sq.getMessage());
        }
        print.addActionListener(this);
        cancel.addActionListener(this);
        if (jstream.getItemCount() < 3) {
            jstream.setSelectedIndex(1);
        }
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object obj = e.getSource();
        if (obj == cancel) {
            CurrentFrame.currentWindow();
            dispose();
            ;
        } else if (obj == print) {
            if (jclassname.getSelectedIndex() == 0 && jstream.getSelectedIndex() == 0) {
                JOptionPane.showMessageDialog(this, "Select Either The Class Selection Or The Stream");
            } else {
                String gen = "", st = "";
                if (jgender.getSelectedIndex() == 0) {
                    gen = "ALL";
                } else {
                    gen = jgender.getSelectedItem().toString();
                }
                if (jstream.getSelectedIndex() == 0) {
                    st = "Overall";
                } else {
                    st = jstream.getSelectedItem().toString();
                }

                ClassListReport.generalClassListPhotoViewer(st, jclassname.getSelectedItem().toString(), gen);
            }

        }


    }

}
