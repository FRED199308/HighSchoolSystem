/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package highschool;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import net.miginfocom.swing.MigLayout;

/**
 * @author FREDDY
 */
public class ProductUpdater implements ActionListener {


    private JPanel bottom;
    private JPanel top;
    private JPanel holder;
    private JTable tab;
    private DefaultTableModel model;
    private JScrollPane pane;

    private FredLabel infor, units, search;
    private FredButton addUnits, viewinfor, remove, generate, edit;
    private PreparedStatement ps;
    private ResultSet rs;
    private FredTextField jsearch, junit;
    private FredButton stale = new FredButton("Declare As Stale");


    private Connection con = DbConnection.connectDb();
    private Object cols[] = {"ProductName", "Barcode", "product Ref No.", "Units Available", "Buying Price(KSH)"};
    Object row[] = new Object[cols.length];


    public JPanel productupdate() {
        search = new FredLabel("Search product by ref Number,Name or BarCode Number(Showing first 100 products)");
        jsearch = new FredTextField();
        holder = new JPanel();
        top = new JPanel();
        bottom = new JPanel();
        model = new DefaultTableModel();
        tab = new JTable();
        pane = new JScrollPane(tab);
        model.setColumnIdentifiers(cols);
        holder.setLayout(new MigLayout());
        junit = new FredTextField();
        units = new FredLabel("Units");
        generate = new FredButton("Export To Pdf.");
        addUnits = new FredButton("Add Units");
        edit = new FredButton("Edit Product Details");
        viewinfor = new FredButton("View Full Product Information");
        holder.add(top, "push,grow,wrap");
        holder.add(bottom, "growx,pushx");
        top.setLayout(new MigLayout());
        top.add(search);
        top.add(jsearch, "pushx,growx,wrap");
        remove = new FredButton("Remove Item From Inventory");
        tab.setModel(model);
        top.add(pane, "gapleft 30,push,grow,span 3 1,wrap");
        top.add(units, "gapleft 50,split,growx,pushx");
        top.add(junit, "growx,pushx,gapleft 50");
        top.add(addUnits);
        top.add(stale);
        bottom.setLayout(new MigLayout());
        bottom.add(viewinfor, "growx,pushx");
        bottom.add(remove, "gapleft 50,growx,pushx");
        bottom.add(generate, "gapleft 50,growx,pushx");
        bottom.add(edit, "growx,pushx,gapleft 30");


        try {

            String sql = "Select productname,products.productid,barcode,buyingprice,buyingprice from products limit 100 ";
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                row[0] = rs.getString("productName");
                row[1] = rs.getString("barcode");
                row[2] = rs.getString("productid");

                double unitss = 0;
                double unitspresent = 0;
                String prodid = rs.getString("productid");

                sql = "Select units from inventory where productid='" + prodid + "'";
                ps = con.prepareStatement(sql);
                ResultSet RS = ps.executeQuery();
                if (RS.next()) {
                    unitspresent = RS.getDouble("units");
                }


                row[3] = unitspresent;
                row[4] = rs.getString("Buyingprice");


                model.addRow(row);

            }
        } catch (Exception sq) {
            sq.printStackTrace();
            JOptionPane.showMessageDialog(null, sq.getMessage());
        }

        addUnits.addActionListener(this);
        remove.addActionListener(this);
        viewinfor.addActionListener(this);
        generate.addActionListener(this);
        edit.addActionListener(this);
        stale.addActionListener(this);
        jsearch.addKeyListener(new KeyAdapter() {


            public void keyReleased(KeyEvent key) {

                if (key.getKeyChar() == KeyEvent.VK_ENTER) {
                    while (model.getRowCount() > 0) {
                        model.removeRow(0);
                    }

                    try {

                        String sql = "Select productname,products.productid,barcode,buyingprice from products where productname like '" + jsearch.getText() + "%' or productid like '" + jsearch.getText() + "%'  or barcode like '" + jsearch.getText() + "%'  ";
                        ps = con.prepareStatement(sql);
                        rs = ps.executeQuery();
                        while (rs.next()) {
                            row[0] = rs.getString("productName");
                            row[1] = rs.getString("barcode");
                            row[2] = rs.getString("productid");

                            double unitss = 0;
                            double unitspresent = 0;
                            String prodid = rs.getString("productid");

                            sql = "Select units from inventory where productid='" + prodid + "'";
                            ps = con.prepareStatement(sql);
                            ResultSet RS = ps.executeQuery();
                            if (RS.next()) {
                                unitspresent = RS.getDouble("units");
                            }


                            row[3] = unitspresent;

                            row[5] = rs.getString("buyingprice");

                            model.addRow(row);

                        }
                    } catch (Exception sq) {
                        sq.printStackTrace();
                        JOptionPane.showMessageDialog(null, sq.getMessage());
                    }

                }
            }
        });


        return holder;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object obj = e.getSource();
        if (obj == addUnits) {
            if (tab.getSelectedColumnCount() < 1) {
                JOptionPane.showMessageDialog(holder, "select The Product To Update Its Inventory");
            } else {
                if (junit.getText().isEmpty()) {
                    JOptionPane.showMessageDialog(holder, "Input A Valid Units Value");
                } else {
                    double currentunit = Double.parseDouble(model.getValueAt(tab.getSelectedRow(), 3).toString());
                    double newunits = currentunit + Double.parseDouble(junit.getText());
                    String prductid = model.getValueAt(tab.getSelectedRow(), 2).toString();

                    String sql = "Update inventory set units='" + newunits + "' where productid='" + prductid + "'";

                    try {
                        ps = con.prepareStatement(sql);
                        ps.execute();
                        model.setValueAt(newunits, tab.getSelectedRow(), 3);
                        junit.setText("");
                        JOptionPane.showMessageDialog(holder, "Update SuccessFull");

                    } catch (Exception sq) {
                        sq.printStackTrace();
                        JOptionPane.showMessageDialog(null, sq.getMessage());
                    }


                }


            }
        } else if (obj == stale) {

            if (tab.getSelectedColumnCount() < 1) {
                JOptionPane.showMessageDialog(holder, "select The Product To Declare as stale");
            } else {
                if (junit.getText().isEmpty()) {
                    JOptionPane.showMessageDialog(holder, "Input A Valid Units Value");
                } else {
                    String reason = JOptionPane.showInputDialog(holder, "Input A Valid Reason Why The Product is To Be Declared As Stale", "Description", JOptionPane.PLAIN_MESSAGE);
                    if (reason.isEmpty()) {
                        JOptionPane.showMessageDialog(null, "Input A valid Reason ");
                    } else {
                        double staleUnits = Double.parseDouble(junit.getText());
                        double currentunit = Double.parseDouble(model.getValueAt(tab.getSelectedRow(), 3).toString());
                        double newunits = currentunit - Double.parseDouble(junit.getText());
                        double costperunit = Double.parseDouble(model.getValueAt(tab.getSelectedRow(), 5).toString());
                        double valueperUnit = Double.parseDouble(model.getValueAt(tab.getSelectedRow(), 4).toString());
                        String prductid = model.getValueAt(tab.getSelectedRow(), 2).toString();

                        String sql = "Update inventory set units='" + newunits + "' where productid='" + prductid + "'";


                        try {
                            ps = con.prepareStatement(sql);
                            ps.execute();
                            model.setValueAt(newunits, tab.getSelectedRow(), 3);
                            junit.setText("");


                            sql = "Insert into staleproducts values('" + prductid + "','" + staleUnits + "',now(),'" + valueperUnit * staleUnits + "','" + costperunit * staleUnits + "','" + Globals.CurrentUser + "','" + "" + "') ";
                            ps = con.prepareStatement(sql);
                            ps.execute();


                            JOptionPane.showMessageDialog(holder, "Product Declared As stale");

                        } catch (Exception sq) {
                            sq.printStackTrace();
                            JOptionPane.showMessageDialog(null, sq.getMessage());
                        }


                    }


                }


            }

        } else if (obj == remove) {
            if (tab.getSelectedRow() < 1) {
                JOptionPane.showMessageDialog(holder, "Select The Item To Remove From Inventory");
            } else {
                String productid = model.getValueAt(tab.getSelectedRow(), 2).toString();

                int option = JOptionPane.showConfirmDialog(holder, "Are You Sure You Want To Remove This Item From Inventory Completely??\n All Information Regarding The Product Will Be Lost", "Confirm Deletion", JOptionPane.YES_NO_OPTION);
                if (option == JOptionPane.YES_OPTION) {
                    try {
                        String sql = "Delete from products where productid='" + productid + "'";
                        ps = con.prepareStatement(sql);
                        ps.execute();
                        model.removeRow(tab.getSelectedRow());
                        JOptionPane.showMessageDialog(holder, "Product Deleted From The Business Database");
                    } catch (Exception sq) {
                        sq.printStackTrace();
                        JOptionPane.showMessageDialog(null, sq.getMessage());
                    }

                } else {

                    JOptionPane.showMessageDialog(holder, "Deleting Process Aborted");
                }
            }
        } else if (obj == generate) {
            ReportGenerator.generateReport("Product Details", "productdetail", tab);
        } else if (obj == viewinfor) {
            if (tab.getSelectedRowCount() < 1) {
                JOptionPane.showMessageDialog(holder, "Select The Product To View Its Information");
            } else {

                String productid = model.getValueAt(tab.getSelectedRow(), 2).toString();
                //   ReportGenerator.productinfor(productid);


            }
        } else if (obj == edit) {

            if (tab.getSelectedRowCount() < 1) {
                JOptionPane.showMessageDialog(holder, "Select The Product To Edit Its Information");
            } else {

                //   Globals.edittingproductid=model.getValueAt(tab.getSelectedRow(),2).toString();
                //  CurrentFrame.setSecondFrame(new EditProduct());
                CurrentFrame.mainFrame().setEnabled(false);


            }

        }


    }


}
