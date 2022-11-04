/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package highschool;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;

import javax.swing.JTextField;

import org.jdesktop.swingx.autocomplete.AutoCompleteDecorator;

/**
 * @author FRED
 */
public class FredTextField extends JTextField implements ActionListener {

    JPopupMenu pop = new JPopupMenu();
    JMenuItem cop = new JMenuItem("Copy");
    JMenuItem cut = new JMenuItem("Cut");
    JMenuItem paste = new JMenuItem("Paste");

    public FredTextField(String text) {

        this.setText(text);


        pop.add(cop);
        pop.add(cut);
        pop.add(paste);
        this.add(pop);
        this.setComponentPopupMenu(pop);
        cop.addActionListener(this);
        cut.addActionListener(this);
        paste.addActionListener(this);


    }

    public FredTextField(boolean autocomplet, ArrayList items) {
        pop.add(cop);
        pop.add(cut);
        pop.add(paste);
        this.add(pop);
        this.setComponentPopupMenu(pop);
        cop.addActionListener(this);
        cut.addActionListener(this);
        paste.addActionListener(this);


        AutoCompleteDecorator.decorate(this, items, false);
        this.setAutoscrolls(true);


    }

    public FredTextField() {
        pop.add(cop);
        pop.add(cut);
        pop.add(paste);
        this.add(pop);
        this.setComponentPopupMenu(pop);
        cop.addActionListener(this);
        cut.addActionListener(this);
        paste.addActionListener(this);

    }

    public FredTextField(boolean autocomplet, String querry) {
        pop.add(cop);
        pop.add(cut);
        pop.add(paste);
        this.add(pop);
        this.setComponentPopupMenu(pop);
        cop.addActionListener(this);
        cut.addActionListener(this);
        paste.addActionListener(this);
        String[] ar = {"Fred", "Ken", "Weks"};
        List j = new ArrayList();
        j.add("Fredrick");


        Connection con;
        PreparedStatement ps = null;
        ResultSet rs;

        con = DbConnection.connectDb();
        try {
            String sql = "Select firstname,middlename,lastname,admissionNumber from admission ";
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            j = Arrays.asList(rs.getArray("FirstName"));
            j.add(rs.getArray("LastName"));
            j.add(rs.getArray("MiddleName"));
            j.add(rs.getArray("AdmissionNumber"));
        } catch (SQLException e) {
            e.printStackTrace();

        } finally {
            try {
                con.close();
                ps.close();
            } catch (SQLException sq) {
                JOptionPane.showMessageDialog(null, sq);
                sq.printStackTrace();
            }

        }

        AutoCompleteDecorator.decorate(this, j, false);

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object obj = e.getSource();
        if (obj == cop) {
            this.copy();
        } else if (obj == cut) {
            this.cut();
        } else if (obj == paste) {
            this.paste();
        }
    }


}
