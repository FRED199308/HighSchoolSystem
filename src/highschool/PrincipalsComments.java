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
import javax.swing.JScrollPane;
import javax.swing.JTable;

import static javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE;

import javax.swing.table.DefaultTableModel;

/**
 * @author FRED
 */
public class PrincipalsComments extends JFrame implements ActionListener {

    private Connection con = DbConnection.connectDb();
    private int width = 800;
    private int height = 550;
    private PreparedStatement ps;
    private ResultSet rs;

    private DefaultTableModel model = new DefaultTableModel();
    private Object cols[] = {"Grade", "Comment"};
    private Object row[] = new Object[cols.length];
    private JTable tab = new JTable();
    private JScrollPane pane = new JScrollPane(tab);
    private FredButton save = new FredButton("Save");
    private FredButton cancel = new FredButton("Close");

    public PrincipalsComments() {
        tab.setModel(model);
        model.setColumnIdentifiers(cols);
        setSize(width, height);
        setTitle("Principal's Report Form Comments");
        ((JComponent) getContentPane()).setBorder(BorderFactory.createMatteBorder(5, 5, 5, 5, Color.MAGENTA));

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

        try {
            String sql = "Select * from principalcomments";
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                row[0] = rs.getString("Grade");
                row[1] = rs.getString("comments");
                model.addRow(row);

            }


        } catch (Exception sq) {
            sq.printStackTrace();
            JOptionPane.showMessageDialog(null, sq.getMessage());
        }
        cancel.setBounds(50, 30, 130, 30);
        add(cancel);
        save.setBounds(450, 30, 130, 30);
        add(save);
        pane.setBounds(30, 100, 750, 300);
        add(pane);
        save.addActionListener(this);
        cancel.addActionListener(this);
        setVisible(true);

    }


    @Override
    public void actionPerformed(ActionEvent e) {
        Object obj = e.getSource();
        if (obj == save) {
            if (tab.getSelectedRowCount() < 1) {
                JOptionPane.showMessageDialog(this, "Kindly Select The Grade To Edit Its Comment and Edit To Save");
            } else {
                int selected = tab.getSelectedRow();
                String grade = model.getValueAt(selected, 0).toString();
                String comment = model.getValueAt(selected, 1).toString();
                System.err.println(comment + "  " + grade);
                try {
                    String sql = "Update principalcomments set comments='" + comment + "' where grade='" + grade + "'";
                    ps = con.prepareStatement(sql);
                    ps.execute();
                    tab.requestFocus();
                    JOptionPane.showMessageDialog(this, "New Comment Affectre");
                } catch (Exception sq) {
                    sq.printStackTrace();
                    JOptionPane.showMessageDialog(null, sq.getMessage());
                }
            }
        } else if (obj == cancel) {
            CurrentFrame.currentWindow();
            dispose();
        }

    }

    public static void main(String[] args) {
        new PrincipalsComments();
    }

}
