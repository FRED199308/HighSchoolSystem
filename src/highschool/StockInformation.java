/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package highschool;

import java.awt.Color;
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
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;

import net.miginfocom.swing.MigLayout;

/**
 * @author FREDDY
 */
public class StockInformation implements ActionListener {


    private JPanel bottom;
    private JPanel top;
    private JPanel holder;
    private JTable tab;
    private DefaultTableModel model;
    private JScrollPane pane;

    private FredLabel infor, units, search;
    private FredButton outofStock, expired, generate, refresh, productinfor;
    private PreparedStatement ps;
    private FredCombo bycategory, bysupplier, underoffer;
    private ResultSet rs;
    private FredTextField jsearch, junit;
    private double total = 0;

    int counter = 0;
    private Connection con = DbConnection.connectDb();
    private Object cols[] = {"No.", "ProductName", "product Ref No.", "Units Available", "Value(Ksh.)"};
    Object row[] = new Object[cols.length];

    public JPanel stockInfo() {
        infor = new FredLabel("Search product by ref Number,Name or BarCode Number(showing first 100 products)");
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
        ;
        bycategory = new FredCombo("Filter By Category");
        bycategory.setBorder(new TitledBorder("Filter By Category"));
        bysupplier = new FredCombo("Filter By Supplier");
        bysupplier.setBorder(new TitledBorder("Filter By Supplier"));
        generate = new FredButton("Export To Pdf.");
        outofStock = new FredButton("Out Of Inventory Products");
        refresh = new FredButton("Refresh");

        productinfor = new FredButton("View Product Information");
        expired = new FredButton("Expired Products");
        holder.add(top, "push,grow,wrap");
        holder.add(bottom, "pushx,growx");
        top.setLayout(new MigLayout());
        top.add(infor);
        top.add(jsearch, "pushx,growx,wrap");
        tab.setModel(model);
        top.add(pane, "gapleft 30,push,grow,span 2 1,wrap");

        bottom.setLayout(new MigLayout());
        bottom.add(bycategory, "Gapleft 30,growx,pushx");
        bottom.add(bysupplier, "gapleft 30,growx,pushx");
        bottom.add(outofStock, ",growx,pushx");
        bottom.add(expired, "gapleft 30,growx,pushx,wrap");
        bottom.add(generate, "gapleft 30,growx,pushx");
        bottom.add(refresh, "growx,pushx,gapleft 30");
        bottom.add(productinfor, "growx,pushx,gapleft 30");

        try {
            ps = con.prepareStatement("Select supplierName from suppliers");
            rs = ps.executeQuery();
            while (rs.next()) {
                bysupplier.addItem(rs.getString("supplierName"));
            }
            ps = con.prepareStatement("Select ProductCategoryName from productcategories");
            rs = ps.executeQuery();
            while (rs.next()) {
                bycategory.addItem(rs.getString("ProductCategoryName"));
            }


            String sql = "Select productname,products.productid,buyingprice,barcode from products limit 100 ";
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {


                double value = (rs.getDouble("buyingprice"));
                counter++;
                row[0] = counter;
                row[1] = rs.getString("productname");
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
                total += unitspresent * value;

                row[3] = unitspresent;
                row[4] = unitspresent * value;


                model.addRow(row);

            }
            row[0] = "TOTAL";
            row[1] = "";
            row[2] = "";
            row[3] = "";
            row[4] = total;
            total = 0;
            counter = 0;
            model.addRow(row);


        } catch (Exception sq) {
            sq.printStackTrace();
            JOptionPane.showMessageDialog(null, sq.getMessage());
        }
        bycategory.addActionListener(this);
        bysupplier.addActionListener(this);
        expired.addActionListener(this);
        outofStock.addActionListener(this);

        generate.addActionListener(this);
        refresh.addActionListener(this);
        productinfor.addActionListener(this);

        jsearch.addKeyListener(new KeyAdapter() {

            public void keyTyped(KeyEvent key) {

                if (key.getKeyChar() == KeyEvent.VK_ENTER) {
                    infor.setText("Search Results..");

                    while (model.getRowCount() > 0) {
                        model.removeRow(0);
                    }
                    try {


                        String sql = "Select productname,products.productid,buyingprice,barcode,buyingprice from products where  productname like '" + jsearch.getText() + "%' or productid like '" + jsearch.getText() + "%'  or barcode like '" + jsearch.getText() + "%'  limit 100 ";
                        ps = con.prepareStatement(sql);
                        rs = ps.executeQuery();
                        while (rs.next()) {


                            double value = (rs.getDouble("buyingprice"));
                            counter++;
                            row[0] = counter;
                            row[1] = rs.getString("productname");
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
                            total += unitspresent * value;

                            row[3] = unitspresent;
                            row[4] = unitspresent * value;


                            model.addRow(row);

                        }
                        row[0] = "TOTAL";
                        row[1] = "";
                        row[2] = "";
                        row[3] = "";
                        row[4] = total;
                        total = 0;
                        counter = 0;
                        model.addRow(row);


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
        if (obj == bycategory) {
            if (bycategory.getSelectedIndex() == 0) {

            } else {

                infor.setText("Products Under Category: " + bycategory.getSelectedItem() + " Value");
                model.getDataVector().removeAllElements();
                ;
                new Thread() {

                    public void run() {
                        try {
                            String categoryid = Globals.goodscategoryid(bycategory.getSelectedItem().toString());


                            String sql = "Select productname,products.productid,buyingprice,barcode,buyingprice from products where categoryId='" + categoryid + "'  ";
                            ps = con.prepareStatement(sql);
                            rs = ps.executeQuery();
                            while (rs.next()) {

                                double value = (rs.getDouble("buyingprice"));
                                counter++;
                                row[0] = counter;
                                row[1] = rs.getString("productname");
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
                                total += unitspresent * value;

                                row[3] = unitspresent;
                                row[4] = unitspresent * value;


                                model.addRow(row);

                            }
                            row[0] = "TOTAL";
                            row[1] = "";
                            row[2] = "";
                            row[3] = "";
                            row[4] = total;
                            total = 0;
                            counter = 0;
                            model.addRow(row);


                        } catch (Exception sq) {
                            sq.printStackTrace();
                            JOptionPane.showMessageDialog(null, sq.getMessage());
                        }


                    }
                }.start();


            }

        } else if (obj == bysupplier) {


            if (bysupplier.getSelectedIndex() == 0) {

            } else {
                infor.setText("Products Supplied By: " + bycategory.getSelectedItem() + " Value");
                model.getDataVector().removeAllElements();
                ;


                new Thread() {

                    public void run() {
                        try {
                            String supplierid = Globals.supplieid(bysupplier.getSelectedItem().toString());


                            String sql = "Select productname,products.productid,buyingprice,barcode,buyingprice from products where supplierId='" + supplierid + "'  ";
                            ps = con.prepareStatement(sql);
                            rs = ps.executeQuery();
                            while (rs.next()) {
                                String offerid = rs.getString("Offerid");

                                double value = (rs.getDouble("buyingprice"));
                                counter++;
                                row[0] = counter;
                                row[1] = rs.getString("productname");
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
                                total += unitspresent * value;

                                row[3] = unitspresent;
                                row[4] = unitspresent * value;


                                model.addRow(row);

                            }
                            row[0] = "TOTAL";
                            row[1] = "";
                            row[2] = "";
                            row[3] = "";
                            row[4] = total;
                            total = 0;
                            counter = 0;
                            model.addRow(row);


                        } catch (Exception sq) {
                            sq.printStackTrace();
                            JOptionPane.showMessageDialog(null, sq.getMessage());
                        }


                    }
                }.start();
            }

        } else if (obj == outofStock) {
            if (bycategory.getSelectedIndex() == 0) {
                infor.setText("Products Out Of Inventory");
                model.getDataVector().removeAllElements();
                ;

                new Thread() {

                    public void run() {
                        try {


                            String sql = "Select productname,products.productid,buyingprice,barcode,buyingprice from products  ";
                            ps = con.prepareStatement(sql);
                            rs = ps.executeQuery();
                            while (rs.next()) {
                                String offerid = rs.getString("Offerid");

                                double value = (rs.getDouble("buyingprice"));
                                counter++;
                                row[0] = counter;
                                row[1] = rs.getString("productname");
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
                                total += unitspresent * value;

                                row[3] = unitspresent;
                                row[4] = unitspresent * value;


                                if (unitspresent == 0) {
                                    model.addRow(row);
                                }

                            }
                            row[0] = "TOTAL";
                            row[1] = "";
                            row[2] = "";
                            row[3] = "";
                            row[4] = total;
                            total = 0;
                            counter = 0;
                            model.addRow(row);


                        } catch (Exception sq) {
                            sq.printStackTrace();
                            JOptionPane.showMessageDialog(null, sq.getMessage());
                        }


                    }
                }.start();

            } else {
                infor.setText("Out Of Inventory Products Under Category: " + bycategory.getSelectedItem());
                model.getDataVector().removeAllElements();
                ;
                new Thread() {

                    public void run() {
                        try {
                            String categoryid = Globals.goodscategoryid(bycategory.getSelectedItem().toString());


                            String sql = "Select productname,products.productid,buyingprice,barcode,buyingprice from products where categoryId='" + categoryid + "'  ";
                            ps = con.prepareStatement(sql);
                            rs = ps.executeQuery();
                            while (rs.next()) {
                                String offerid = rs.getString("Offerid");

                                double value = (rs.getDouble("buyingprice"));
                                counter++;
                                row[0] = counter;
                                row[1] = rs.getString("productname");
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
                                total += unitspresent * value;

                                row[3] = unitspresent;
                                row[4] = unitspresent * value;


                                if (unitspresent == 0) {
                                    model.addRow(row);
                                }

                            }
                            row[0] = "TOTAL";
                            row[1] = "";
                            row[2] = "";
                            row[3] = "";
                            row[4] = total;
                            total = 0;
                            counter = 0;
                            model.addRow(row);


                        } catch (Exception sq) {
                            sq.printStackTrace();
                            JOptionPane.showMessageDialog(null, sq.getMessage());
                        }


                    }
                }.start();

            }
        } else if (obj == expired) {


            if (bycategory.getSelectedIndex() == 0) {
                infor.setText("Expired Products Details");
                model.getDataVector().removeAllElements();
                ;

                new Thread() {

                    public void run() {
                        try {


                            String sql = "Select productname,products.productid,buyingprice,barcode,buyingprice from products  ";
                            ps = con.prepareStatement(sql);
                            rs = ps.executeQuery();
                            while (rs.next()) {
                                String offerid = rs.getString("Offerid");

                                double value = (rs.getDouble("buyingprice"));
                                counter++;
                                row[0] = counter;
                                row[1] = rs.getString("productname");
                                row[2] = rs.getString("productid");

                                double unitss = 0;
                                double unitspresent = 0;
                                String prodid = rs.getString("productid");

                                sql = "Select units from inventory where productid='" + prodid + "' and ExpireDate<now()";
                                ps = con.prepareStatement(sql);
                                ResultSet RS = ps.executeQuery();
                                if (RS.next()) {
                                    unitspresent = RS.getDouble("units");
                                    total += unitspresent * value;

                                    row[3] = unitspresent;
                                    row[4] = unitspresent * value;


                                    model.addRow(row);
                                }


                            }
                            row[0] = "TOTAL";
                            row[1] = "";
                            row[2] = "";
                            row[3] = "";
                            row[4] = total;
                            total = 0;
                            counter = 0;
                            model.addRow(row);


                        } catch (Exception sq) {
                            sq.printStackTrace();
                            JOptionPane.showMessageDialog(null, sq.getMessage());
                        }


                    }
                }.start();

            } else {
                infor.setText("Expired Products Under Category: " + bycategory.getSelectedItem() + " Value");
                model.getDataVector().removeAllElements();
                ;
                new Thread() {

                    public void run() {
                        try {
                            String categoryid = Globals.goodscategoryid(bycategory.getSelectedItem().toString());


                            String sql = "Select productname,products.productid,buyingprice,barcode,buyingprice from products where categoryId='" + categoryid + "'  ";
                            ps = con.prepareStatement(sql);
                            rs = ps.executeQuery();
                            while (rs.next()) {
                                String offerid = rs.getString("Offerid");

                                double value = (rs.getDouble("buyingprice"));
                                counter++;
                                row[0] = counter;
                                row[1] = rs.getString("productname");
                                row[2] = rs.getString("productid");

                                double unitss = 0;
                                double unitspresent = 0;
                                String prodid = rs.getString("productid");

                                sql = "Select units from inventory where productid='" + prodid + "' and expiredate<now()";
                                ps = con.prepareStatement(sql);
                                ResultSet RS = ps.executeQuery();
                                if (RS.next()) {
                                    unitspresent = RS.getDouble("units");
                                    total += unitspresent * value;

                                    row[3] = unitspresent;
                                    row[4] = unitspresent * value;

                                    model.addRow(row);
                                }


                            }
                            row[0] = "TOTAL";
                            row[1] = "";
                            row[2] = "";
                            row[3] = "";
                            row[4] = total;
                            total = 0;
                            counter = 0;
                            model.addRow(row);


                        } catch (Exception sq) {
                            sq.printStackTrace();
                            JOptionPane.showMessageDialog(null, sq.getMessage());
                        }


                    }
                }.start();

            }

        } else if (obj == generate) {
            ReportGenerator.generateReport(infor.getText(), "product infor", tab);
        } else if (obj == refresh) {
            model.getDataVector().removeAllElements();
            model.setColumnIdentifiers(cols);

            try {

                infor.setText("Search product by ref Number,Name or BarCode Number(showing first 100 products)");
                String sql = "Select productname,products.productid,buyingprice,barcode,buyingprice from products limit 100 ";
                ps = con.prepareStatement(sql);
                rs = ps.executeQuery();
                while (rs.next()) {
                    String offerid = rs.getString("Offerid");

                    double value = (rs.getDouble("buyingprice"));
                    counter++;
                    row[0] = counter;
                    row[1] = rs.getString("productname");
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
                    total += unitspresent * value;

                    row[3] = unitspresent;
                    row[4] = unitspresent * value;


                    model.addRow(row);

                }
                row[0] = "TOTAL";
                row[1] = "";
                row[2] = "";
                row[3] = "";
                row[4] = total;
                total = 0;
                counter = 0;
                model.addRow(row);

            } catch (Exception sq) {
                sq.printStackTrace();
                JOptionPane.showMessageDialog(null, sq.getMessage());
            }

        } else if (obj == productinfor) {
            if (tab.getSelectedRowCount() < 1) {
                JOptionPane.showMessageDialog(holder, "Select The Product To View Its Information");
            } else {

                String productid = model.getValueAt(tab.getSelectedRow(), 2).toString();
                ReportGenerator.productinfor(productid);


            }
        }


    }


}
