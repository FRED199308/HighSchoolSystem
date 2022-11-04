/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package highschool;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.VerticalPositionMark;

import java.awt.Color;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;

import net.miginfocom.swing.MigLayout;

/**
 * @author FREDDY
 */
public class Catalogue extends JFrame implements ActionListener {


    private Connection con = DbConnection.connectDb();
    private PreparedStatement ps;
    private ResultSet rs;
    private Toolkit tk = Toolkit.getDefaultToolkit();
    private int width = (int) tk.getScreenSize().getWidth();
    private int height = (int) tk.getScreenSize().getHeight();
    private FredTextField staffNumber = new FredTextField();
    private FredCheckBox points = new FredCheckBox("Use Gift Points");
    private ButtonGroup bt = new ButtonGroup();
    private JTable tab = new JTable();
    private DefaultTableModel model = new DefaultTableModel() {
        @Override
        public boolean isCellEditable(int row, int col) {
            return false;
        }
    };
    ;
    private Object col[] = {"Product Name", "Product Ref. No.", "No. Of Units"};
    private Object row[] = new Object[col.length];
    private JScrollPane pane = new JScrollPane(tab);
    private FredButton remove = new FredButton("Remove From Catalogue");
    private FredLabel name = new FredLabel("Search Product");
    private FredTextField jname;
    private FredLabel units = new FredLabel("Units");
    private FredTextField junit = new FredTextField();
    private FredButton unitupdater = new FredButton("Update Units");

    private FredButton cancel = new FredButton("Close Session");
    private FredButton save = new FredButton("Issue");

    private FredLabel staff = new FredLabel("staff Name");

    private ArrayList list = new ArrayList();

    public Catalogue() {
        this.setSize(width, height - 10);
        this.setExtendedState(MAXIMIZED_BOTH);
        this.addKeyListener(new KeyAdapter() {
            public void KeyReleased(KeyEvent key) {
                if (key.getKeyCode() == KeyEvent.VK_F1) {
                    save.doClick();
                }
            }
        });
        setLocationRelativeTo(null);
        setLayout(new MigLayout());
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        setTitle("System Catalogue");
        this.setIconImage(FrameProperties.icon());
        ((JComponent) getContentPane()).setBorder(
                BorderFactory.createMatteBorder(5, 5, 5, 5, Color.MAGENTA));

        this.addWindowListener(new WindowAdapter() {

            @Override
            public void windowClosing(WindowEvent e) {
                try {
                    con.close();
                } catch (SQLException sq) {
                    sq.printStackTrace();
                }
                CurrentFrame.currentWindow();
                e.getWindow().dispose();
            }
        });

        tab.setForeground(Color.BLUE);

        tab.getTableHeader().setFont(new Font("serif", Font.BOLD, 25));

        try {

            String sql = "Select productname,products.productid,barcode from products  ";
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {


                int unitss = 0;
                int unitspresent = 0;
                String prodid = rs.getString("productid");

                sql = "Select units from inventory where productid='" + prodid + "'";
                ps = con.prepareStatement(sql);
                ResultSet RS = ps.executeQuery();
                if (RS.next()) {
                    unitspresent = RS.getInt("units");

                }


                list.add(rs.getString("productName"));
                list.add(rs.getString("productid"));
                list.add(rs.getString("barcode"));

            }
        } catch (Exception sq) {
            sq.printStackTrace();
            JOptionPane.showMessageDialog(this, sq.getMessage());
        }


        tab.addKeyListener(new KeyAdapter() {
            public void KeyReleased(KeyEvent key) {
                if (key.getKeyCode() == KeyEvent.VK_F1) {
                    save.doClick();
                }
            }
        });


        staffNumber.setBorder(new TitledBorder("Input Staff Number"));
        ;
        model.setColumnIdentifiers(col);
        jname = new FredTextField(true, list);
        jname.setBorder(new TitledBorder("Scan Or Searh Product Here"));
        tab.setModel(model);
        add(pane, "span 2 1,grow,push,wrap");

        add(staff, "growx,pushx");
        ;
        add(staffNumber, "gapleft 30,growx,pushx,width 50:600:800,height 70:70:70,wrap");
        add(units, "gapleft 30");
        add(junit, "height 70:70:70,gapleft 30,width 20:100:150,split");
        ;
        add(unitupdater, "height 70:70:70,gapleft 30,width 20:100:150,wrap");
        add(name, "Gapleft 30,growx,pushx");
        add(jname, "gapleft 30,width 50:600:800,height 70:70:70,wrap");

        add(cancel, "gapleft 100,gaptop 30");
        add(save, "gapbottom 50");


        unitupdater.setFont(new Font("serif", Font.BOLD, 20));

        remove.setFont(new Font("serif", Font.BOLD, 20));
        cancel.setFont(new Font("serif", Font.BOLD, 20));
        save.setFont(new Font("serif", Font.BOLD, 20));


        junit.setFont(new Font("serif", Font.BOLD, 30));
        junit.setForeground(Color.BLUE);
        units.setFont(new Font("serif", Font.BOLD, 30));

        jname.setFont(new Font("serif", Font.BOLD, 35));
        name.setFont(new Font("serif", Font.BOLD, 30));
        jname.setForeground(Color.MAGENTA);

        cancel.addActionListener(this);
        unitupdater.addActionListener(this);
        remove.addActionListener(this);
        save.addActionListener(this);

        tab.setRowHeight(30);
        //  tab.setFont(new Font("serif", Font.PLAIN, 25));
        tab.setFont(new Font("Serif", Font.BOLD, 40));

        // bt.add(credit);bt.add(points);


        setVisible(true);
        staffNumber.addKeyListener(new KeyAdapter() {

            public void keyReleased(KeyEvent key) {

                staff.setText(Globals.fullStaffName(staffNumber.getText()));


            }


        });
        junit.addKeyListener(new KeyAdapter() {


            public void keyReleased(KeyEvent key) {
                if (key.getKeyCode() == KeyEvent.VK_F1) {
                    save.doClick();
                }


            }

            public void keyTyped(KeyEvent key) {
                char c = key.getKeyChar();

                if (Character.isAlphabetic(c)) {

                    key.consume();
                }
                if (c == KeyEvent.VK_ENTER) {
                    unitupdater.doClick();
                }
            }
        });


        jname.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent key) {
                if (key.getKeyCode() == KeyEvent.VK_F1) {
                    save.doClick();
                }
                if (key.getKeyCode() == KeyEvent.VK_ENTER) {
                    try {
                        String productIdentifier = jname.getText();
                        String sql = "Select productname,products.productid,siunit  from products  where  products.barcode='" + productIdentifier + "' or productname='" + productIdentifier + "' or products.productid='" + productIdentifier + "'";
                        ps = con.prepareStatement(sql);
                        rs = ps.executeQuery();
                        while (rs.next()) {
                            row[0] = rs.getString("productName");
                            row[1] = rs.getString("productid");
                            String siunit = rs.getString("siunit");

                            double unitss = 0;
                            double unitspresent = 0;
                            String prodid = rs.getString("productid");

                            sql = "Select units from inventory where productid='" + prodid + "'";
                            ps = con.prepareStatement(sql);
                            ResultSet RS = ps.executeQuery();
                            if (RS.next()) {
                                unitspresent = RS.getDouble("units");

                            }

                            if (junit.getText().isEmpty()) {
                                unitss = 1;
                            } else {

                                unitss = Double.parseDouble(junit.getText());
                            }

                            row[2] = unitss + " " + siunit;

                            if (unitspresent >= unitss) {
                                model.addRow(row);
                            } else {
                                JOptionPane.showMessageDialog(null, "Product Out Of Inventory, \n The System Was Unable to Sell " + unitss + "\n Kindly Update Inventory");
                            }


                        }
                    } catch (Exception sq) {
                        sq.printStackTrace();
                        JOptionPane.showMessageDialog(null, sq.getMessage());
                    }
                    {

                    }
                    jname.setText("");

                }

            }
        });


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
        } else if (obj == unitupdater) {
            if (tab.getSelectedRowCount() < 1) {
                JOptionPane.showMessageDialog(this, "Select The Product You Want To Update its Unit On The Table Obove");
            } else {

                if (junit.getText().isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Input Valid Units Value");
                } else {
                    String prodid = model.getValueAt(tab.getSelectedRow(), 1).toString();
                    double unitss = Double.valueOf(junit.getText().toString());

                    tab.setValueAt(unitss, tab.getSelectedRow(), 2);


                    junit.setText("");

                }

            }
        } else if (obj == remove) {
            if (tab.getSelectedRowCount() < 1) {
                JOptionPane.showMessageDialog(this, "Select The product To Remove");
            } else {
                model.removeRow(tab.getSelectedRow());

            }
        } else if (obj == save) {
            if (tab.getRowCount() < 1) {
                JOptionPane.showMessageDialog(this, "Cannot Issue , No Item On The Cart");
            } else {
                System.out.println(Globals.fullStaffName(staffNumber.getText()));
                if (Globals.fullStaffName(staffNumber.getText()).isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Invalid Staff Number");
                } else {


                    String transactionrefNumber = Globals.nextReceiptNumber(0);
                    for (int i = 0; i < tab.getRowCount(); ++i) {
                        String productid = model.getValueAt(i, 1).toString();
                        String unit = model.getValueAt(i, 2).toString();
                        double unitsBought = Double.parseDouble(unit.substring(0, unit.indexOf(" ")));


                        try {

                            String sql = "Insert Into sales values('" + productid + "','" + staffNumber.getText() + "','" + unitsBought + "','" + transactionrefNumber + "','" + Globals.productBuyingPrice(productid) * unitsBought + "',now())";
                            ps = con.prepareStatement(sql);
                            ps.execute();
                            productnunitupdater(productid, unitsBought);

                        } catch (Exception sq) {
                            sq.printStackTrace();
                            JOptionPane.showMessageDialog(this, sq.getMessage());
                        }

                    }


                    String sql = "insert into saletransactionrecord values('" + transactionrefNumber + "',now(),'" + staffNumber.getText() + "')";
                    try {
                        ps = con.prepareStatement(sql);
                        ps.execute();

                    } catch (Exception sq) {
                        sq.printStackTrace();
                        JOptionPane.showMessageDialog(this, sq.getMessage());
                    }


                    while (model.getRowCount() > 0) {
                        model.removeRow(0);
                    }

                    junit.setText("");
                    ;
                    jname.setText("");
                    staffNumber.setText("");
                    staff.setText("");
                    JOptionPane.showMessageDialog(this, "Items Were Issued Successfuly");

                }


            }

        }


    }


    public void productnunitupdater(String productid, double unitsbought) {
        try {
            double currentUnits = 0;

            String sql = "select units from inventory where productid='" + productid + "'";
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            if (rs.next()) {
                currentUnits = rs.getDouble("Units");

            }
            double unitremaining = currentUnits - unitsbought;

            sql = "Update inventory set units='" + unitremaining + "' where productid='" + productid + "'";
            ps = con.prepareStatement(sql);
            ps.execute();


        } catch (Exception sq) {
            sq.printStackTrace();
            JOptionPane.showMessageDialog(this, sq.getMessage());
        }
    }


    public static void main(String[] args) {
        new Catalogue();
    }

}
