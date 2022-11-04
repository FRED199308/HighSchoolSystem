/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package highschool;

import java.awt.Dialog;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;

import net.miginfocom.swing.MigLayout;

/**
 * @author FREDDY
 */
public class SalesInfor implements ActionListener {

    JDialog diag;
    private JPanel bottom;
    private JPanel top;
    private JPanel holder;
    private JTable tab;
    private DefaultTableModel model;
    private JScrollPane pane;

    private FredLabel infor, units, search;
    private FredButton generate, refresh, productinfor, dateFilter, productTrailler;
    private PreparedStatement ps;
    private FredCombo bycategory, bysupplier;
    private ResultSet rs;
    private FredTextField jsearch, junit;
    private double total = 0, totalProfit = 0;
    private FredDateChooser start = new FredDateChooser("yyyy/MM/dd");
    private FredDateChooser stop = new FredDateChooser("yyyy/MM/dd");

    private NumberFormat nf = NumberFormat.getInstance();
    int counter = 0;
    private Connection con = DbConnection.connectDb();
    private Object cols[] = {"No.", "ProductName", "product Ref No.", "Units Issued", "Value(Ksh.)"};
    Object row[] = new Object[cols.length];

    public JPanel salesinfor() {
        nf.setMaximumFractionDigits(2);
        nf.setMinimumFractionDigits(2);
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

        bycategory = new FredCombo("Filter By Category");
        bycategory.setBorder(new TitledBorder("Filter By Category"));
        bysupplier = new FredCombo("Filter By Supplier");
        bysupplier.setBorder(new TitledBorder("Filter By Supplier"));
        generate = new FredButton("Export To Pdf.");
        dateFilter = new FredButton("Filter By Date");
        productTrailler = new FredButton("Trail Product Issue");
        refresh = new FredButton("Refresh");
        productinfor = new FredButton("View Product Information");
        start.setBorder(new TitledBorder("Begin From"));
        stop.setBorder(new TitledBorder("End At"));
        holder.add(top, "push,grow,wrap");
        holder.add(bottom, "pushx,growx");
        top.setLayout(new MigLayout());
        top.add(infor);
        top.add(jsearch, "pushx,growx");
        top.add(start, "pushx,growx");
        top.add(stop, "pushx,growx,wrap");
        tab.setModel(model);
        top.add(pane, "gapleft 30,push,grow,span 4 1,wrap");

        bottom.setLayout(new MigLayout());
        bottom.add(bycategory, "Gapleft 30,growx,pushx");
        bottom.add(bysupplier, "gapleft 30,growx,pushx");
        bottom.add(generate, "gapleft 30,growx,pushx");
        bottom.add(refresh, "growx,pushx,gapleft 30");
        bottom.add(productinfor, "growx,pushx,gapleft 30");
        bottom.add(dateFilter, "growx,pushx,gapleft 30");
        bottom.add(productTrailler, "growx,pushx,gapleft 30");
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


            String sql = "Select productname,products.productid,buyingprice from products limit 100 ";
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {

                double value = 0;

                row[0] = model.getRowCount() + 1;
                row[1] = rs.getString("productname");
                row[2] = rs.getString("productid");

                double unitss = 0;
                double unitsSold = 0;
                String prodid = rs.getString("productid");
                double buyingprice = rs.getDouble("buyingprice");


                ps = con.prepareStatement("Select sum(units) as items,sum(BuyingPrice) from sales where productid='" + prodid + "' ");
                ResultSet Rs = ps.executeQuery();
                if (Rs.next()) {
                    unitsSold = Rs.getDouble("items");
                    value = Rs.getDouble("Sum(BuyingPrice)");


                }

                // buyingprice=buyingprice*unitsSold; 

                total += value;

                row[3] = unitsSold;

                total += value;

                row[3] = unitsSold;

                row[4] = value;


                model.addRow(row);

            }
            row[0] = "TOTAL";
            row[1] = "";

            row[2] = "";
            row[3] = "";
            row[4] = total;
            total = 0;
            counter = 0;
            totalProfit = 0;
            model.addRow(row);


        } catch (Exception sq) {
            sq.printStackTrace();
            JOptionPane.showMessageDialog(null, sq.getMessage());
        }


        jsearch.addKeyListener(new KeyAdapter() {


            @Override
            public void keyReleased(KeyEvent key) {
                if (key.getKeyChar() == KeyEvent.VK_ENTER) {

                    String ldate = ((JTextField) start.getDateEditor().getUiComponent()).getText();
                    String udate = ((JTextField) stop.getDateEditor().getUiComponent()).getText();

                    if (udate.isEmpty() || ldate.isEmpty()) {
                        infor.setText("Searc Results.....");
                        model.getDataVector().removeAllElements();
                        try {

                            String sql = "Select productname,products.productid,buyingprice from products where productname like '" + jsearch.getText() + "%' or barcode like  '" + jsearch.getText() + "%'";
                            ps = con.prepareStatement(sql);
                            rs = ps.executeQuery();
                            while (rs.next()) {


                                double value = 0;
                                counter++;
                                row[0] = counter;
                                row[1] = rs.getString("productname");
                                row[2] = rs.getString("productid");

                                double unitss = 0;
                                double unitsSold = 0;
                                String prodid = rs.getString("productid");
                                double buyingprice = rs.getDouble("buyingprice");

                                ps = con.prepareStatement("Select sum(units) as items,sum(BuyingPrice) from sales where productid='" + prodid + "' ");
                                ResultSet Rs = ps.executeQuery();
                                if (Rs.next()) {
                                    unitsSold = Rs.getDouble("items");
                                    value = Rs.getDouble("Sum(BuyingPrice)");


                                }


                                total += value;

                                row[3] = unitsSold;

                                row[4] = value;


                                model.addRow(row);

                            }
                            row[0] = "TOTAL";
                            row[1] = "";

                            row[2] = "";
                            row[3] = "";
                            row[4] = total;
                            total = 0;
                            counter = 0;
                            totalProfit = 0;
                            model.addRow(row);


                        } catch (Exception sq) {
                            sq.printStackTrace();
                            JOptionPane.showMessageDialog(null, sq.getMessage());
                        }


                    } else {
                        model.getDataVector().removeAllElements();
                        infor.setText("Search Results...  for products Issued between " + ldate + " And " + udate);


                        try {

                            String sql = "Select productname,products.productid,buyingprice from products where productname like '" + jsearch.getText() + "%' or barcode like  '" + jsearch.getText() + "%'";
                            ps = con.prepareStatement(sql);
                            rs = ps.executeQuery();
                            while (rs.next()) {


                                double value = 0;
                                counter++;
                                row[0] = counter;
                                row[1] = rs.getString("productname");
                                row[2] = rs.getString("productid");
                                double unitss = 0;
                                double unitsSold = 0;
                                String prodid = rs.getString("productid");
                                double buyingprice = rs.getDouble("buyingprice");

                                ps = con.prepareStatement("Select sum(units) as items,sum(BuyingPrice) from sales where productid='" + prodid + "' ");
                                ResultSet Rs = ps.executeQuery();
                                if (Rs.next()) {
                                    unitsSold = Rs.getDouble("items");
                                    value = Rs.getDouble("Sum(BuyingPrice)");


                                }

                                // buyingprice=buyingprice*unitsSold; 

                                total += value;

                                row[3] = unitsSold;

                                row[4] = value;


                                model.addRow(row);

                            }
                            row[0] = "TOTAL";
                            row[1] = "";

                            row[2] = "";
                            row[3] = "";
                            row[4] = total;
                            total = 0;
                            counter = 0;
                            totalProfit = 0;
                            model.addRow(row);

                        } catch (Exception sq) {
                            sq.printStackTrace();
                            JOptionPane.showMessageDialog(null, sq.getMessage());
                        }


                    }


                }
            }

        });
        bycategory.addActionListener(this);
        bysupplier.addActionListener(this);

        generate.addActionListener(this);
        refresh.addActionListener(this);
        productinfor.addActionListener(this);
        dateFilter.addActionListener(this);

        productTrailler.addActionListener(this);

        return holder;


    }


    @Override
    public void actionPerformed(ActionEvent e) {
        Object obj = e.getSource();
        if (obj == bycategory) {
            if (bycategory.getSelectedIndex() == 0) {

            } else {
                infor.setText("Product Issued Under Category :" + bycategory.getSelectedItem());
                model.getDataVector().removeAllElements();
                new Thread() {

                    public void run() {


                        try {

                            String sql = "Select productname,products.productid,buyingprice from products  where categoryid='" + Globals.goodscategoryid(bycategory.getSelectedItem().toString()) + "'  ";
                            ps = con.prepareStatement(sql);
                            rs = ps.executeQuery();
                            while (rs.next()) {


                                double value = 0;
                                counter++;
                                row[0] = counter;
                                row[1] = rs.getString("productname");
                                row[2] = rs.getString("productid");

                                double unitss = 0;
                                double unitsSold = 0;
                                String prodid = rs.getString("productid");
                                double buyingprice = rs.getDouble("buyingprice");
                                double profit = 0;
                                ps = con.prepareStatement("Select sum(units) as items,sum(BuyingPrice) from sales where productid='" + prodid + "' ");
                                ResultSet Rs = ps.executeQuery();
                                if (Rs.next()) {
                                    unitsSold = Rs.getDouble("items");
                                    value = Rs.getDouble("Sum(BuyingPrice)");


                                }

                                // buyingprice=buyingprice*unitsSold; 

                                total += value;


                                row[3] = unitsSold;

                                row[4] = value;


                                model.addRow(row);

                            }
                            row[0] = "TOTAL";
                            row[1] = "";

                            row[2] = "";
                            row[3] = "";
                            row[4] = total;
                            total = 0;
                            counter = 0;
                            totalProfit = 0;
                            model.addRow(row);


                        } catch (Exception sq) {
                            sq.printStackTrace();
                            JOptionPane.showMessageDialog(null, sq.getMessage());
                        }
                    }

                }.start();
            }
        } else if (obj == productTrailler) {

            if (tab.getSelectedRowCount() < 1) {
                JOptionPane.showMessageDialog(holder, "Please Select The Item To Trail");
            } else {
                String productid = model.getValueAt(tab.getSelectedRow(), 2).toString();
                diag = new JDialog();
                diag.setSize(900, 600);
                diag.setTitle(model.getValueAt(tab.getSelectedRow(), 2).toString() + " Issue Trail");
                diag.setLayout(new MigLayout());
                diag.setAlwaysOnTop(true);
                diag.setLocationRelativeTo(null);
                //  CurrentFrame.mainFrame().setEnabled(false);
                DefaultTableModel model2 = new DefaultTableModel();
                JTable tab2 = new JTable();
                Object cols1[] = {"SN", "Ref NO", "Date", "Units", "Recipient"};
                FredLabel infor2 = new FredLabel(model.getValueAt(tab.getSelectedRow(), 1).toString() + " Issue Report (Limit 100 Records)");
                FredButton date = new FredButton("Filter By Date");
                FredButton report = new FredButton("Generate Report");
                JScrollPane pane2 = new JScrollPane(tab2);

                model2.setColumnIdentifiers(cols1);
                Object row1[] = new Object[cols1.length];
                tab2.setModel(model2);
                diag.add(infor2, "pushx,growx");
                diag.add(date, "push,grow");
                diag.add(report, "push,grow,gapleft 50,wrap");
                diag.add(pane2, "push,grow,span 3 1,wrap");
                diag.setVisible(true);
                try {
                    double units = 0, value = 0;
                    String sql = "Select * from sales where productid='" + productid + "' order by date desc limit 100";
                    ps = con.prepareStatement(sql);
                    rs = ps.executeQuery();
                    while (rs.next()) {
                        row1[0] = tab2.getRowCount() + 1;
                        row1[1] = rs.getString("transactionNumber");
                        row1[2] = rs.getString("date");
                        row1[3] = rs.getString("units");
                        row1[4] = Globals.fullStaffName(rs.getString("staffid"));
                        value = +rs.getDouble("BuyingPrice");
                        units = +rs.getDouble("units");
                        model2.addRow(row1);
                    }
                    row1[0] = "Total";
                    row1[1] = "";
                    row1[2] = "";
                    row1[3] = units;

                    model2.addRow(row1);


                } catch (Exception ex) {
                    ex.printStackTrace();
                }

                report.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        diag.setAlwaysOnTop(false);

                        ReportGenerator.generateReport(infor2.getText(), "product trail", tab2);
                    }
                });

                date.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {


                        String interval = "";
                        SimpleDateFormat frm = new SimpleDateFormat("yyyy/MM/dd");
                        interval = JOptionPane.showInputDialog(null, "Kindly Enter The Duration Interval To View Its Equivalent Fee Paid", "2012/12/31-" + frm.format(new Date()));
                        if (DataValidation.dateInteval(interval)) {
                            while (model2.getRowCount() > 0) {
                                model2.removeRow(0);
                            }
                            int k = interval.indexOf("-");
                            String lowerDate = interval.substring(0, k);
                            String upperDate = interval.substring(k + 1);
                            infor2.setText(model.getValueAt(tab.getSelectedRow(), 1).toString() + " Issue Trail Between " + lowerDate + " And " + upperDate);
                            try {

                                double units = 0, value = 0;
                                String sql = "Select * from sales where productid='" + productid + "' and  Date BETWEEN '" + lowerDate + "' and '" + upperDate + "'   order by date desc";
                                ps = con.prepareStatement(sql);
                                rs = ps.executeQuery();
                                while (rs.next()) {
                                    row1[0] = tab2.getRowCount() + 1;
                                    row1[1] = rs.getString("transactionNumber");
                                    row1[2] = rs.getString("date");
                                    row1[3] = rs.getString("units");

                                    units = +rs.getDouble("units");
                                    model2.addRow(row1);
                                }
                                row1[0] = "Total";
                                row1[1] = "";
                                row1[2] = "";
                                row1[3] = units;

                                model2.addRow(row1);


                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }


                        } else {


                        }
                    }
                });


            }


        } else if (obj == generate) {
            ReportGenerator.generateReport(infor.getText(), "Product Issue infor", tab);
        } else if (obj == bysupplier) {
            if (bysupplier.getSelectedIndex() == 0) {

            } else {
                infor.setText("Product Issue Supplied By :" + bysupplier.getSelectedItem());
                model.getDataVector().removeAllElements();
                new Thread() {

                    public void run() {


                        try {

                            String sql = "Select productname,products.productid,buyingprice from products  where supplierid='" + Globals.supplieid(bysupplier.getSelectedItem().toString()) + "' ";
                            ps = con.prepareStatement(sql);
                            rs = ps.executeQuery();
                            while (rs.next()) {


                                double value = 0;
                                counter++;
                                row[0] = counter;
                                row[1] = rs.getString("productname");
                                row[2] = rs.getString("productid");

                                double unitss = 0;
                                double unitsSold = 0;
                                String prodid = rs.getString("productid");
                                double buyingprice = rs.getDouble("buyingprice");


                                ps = con.prepareStatement("Select sum(units) as items,sum(BuyingPrice) from sales where productid='" + prodid + "' ");
                                ResultSet Rs = ps.executeQuery();
                                if (Rs.next()) {
                                    unitsSold = Rs.getDouble("items");
                                    value = Rs.getDouble("Sum(BuyingPrice)");


                                }

                                // buyingprice=buyingprice*unitsSold; 

                                total += value;


                                row[3] = unitsSold;

                                row[4] = value;


                                model.addRow(row);

                            }
                            row[0] = "TOTAL";
                            row[1] = "";

                            row[2] = "";
                            row[3] = "";
                            row[4] = total;
                            total = 0;
                            counter = 0;
                            totalProfit = 0;
                            model.addRow(row);


                        } catch (Exception sq) {
                            sq.printStackTrace();
                            JOptionPane.showMessageDialog(null, sq.getMessage());
                        }
                    }

                }.start();
            }
        } else if (obj == dateFilter) {
            if (bycategory.getSelectedIndex() == 0) {

                String interval = "";
                SimpleDateFormat frm = new SimpleDateFormat("yyyy/MM/dd");
                interval = JOptionPane.showInputDialog(null, "Kindly Enter The Date Interval To Retrieve Its Products Issue", "2017/12/31-" + frm.format(new Date()));
                if (DataValidation.dateInteval(interval)) {
                    Object[] row = new Object[cols.length];
                    while (model.getRowCount() > 0) {
                        model.removeRow(0);
                    }
                    int k = interval.indexOf("-");
                    String lowerDate = interval.substring(0, k);
                    String upperDate = interval.substring(k + 1);
                    infor.setText("Product Issue Information  Between " + lowerDate + " And " + upperDate);

                    new Thread() {
                        public void run() {

                            try {

                                String sql = "Select productname,products.productid,buyingprice from products  ";
                                ps = con.prepareStatement(sql);
                                rs = ps.executeQuery();
                                while (rs.next()) {


                                    double value = 0;
                                    counter++;
                                    row[0] = counter;
                                    row[1] = rs.getString("productname");
                                    row[2] = rs.getString("productid");

                                    double unitss = 0;
                                    double unitsSold = 0;
                                    String prodid = rs.getString("productid");
                                    double buyingprice = rs.getDouble("buyingprice");

                                    ps = con.prepareStatement("Select sum(units) as items,sum(BuyingPrice) from sales where productid='" + prodid + "' ");
                                    ResultSet Rs = ps.executeQuery();
                                    if (Rs.next()) {
                                        unitsSold = Rs.getDouble("items");
                                        value = Rs.getDouble("Sum(BuyingPrice)");


                                    }

                                    // buyingprice=buyingprice*unitsSold; 

                                    total += value;


                                    row[3] = unitsSold;

                                    row[4] = value;


                                    model.addRow(row);

                                }
                                row[0] = "TOTAL";
                                row[1] = "";

                                row[2] = "";
                                row[3] = "";
                                row[4] = total;
                                total = 0;
                                counter = 0;
                                totalProfit = 0;
                                model.addRow(row);


                            } catch (Exception sq) {
                                sq.printStackTrace();
                                JOptionPane.showMessageDialog(null, sq.getMessage());
                            }


                        }


                    }.start();


                } else {
                    JOptionPane.showMessageDialog(holder, "Wrong Date Format");
                }


            } else {


                String interval = "";
                SimpleDateFormat frm = new SimpleDateFormat("yyyy/MM/dd");
                interval = JOptionPane.showInputDialog(null, "Kindly Enter The Date Interval To Retrieve Its Product Issue", "2017/12/31-" + frm.format(new Date()));
                if (DataValidation.dateInteval(interval)) {
                    Object[] row = new Object[cols.length];
                    while (model.getRowCount() > 0) {
                        model.removeRow(0);
                    }
                    int k = interval.indexOf("-");
                    String lowerDate = interval.substring(0, k);
                    String upperDate = interval.substring(k + 1);
                    infor.setText("Product Issue Under Category:" + bycategory.getSelectedItem() + "  Between  Dates " + lowerDate + " And " + upperDate);

                    new Thread() {
                        public void run() {
                            String categoryid = Globals.goodscategoryid(bycategory.getSelectedItem().toString());
                            try {

                                String sql = "Select productname,products.productid,buyingprice from products  where categoryid='" + Globals.goodscategoryid(bycategory.getSelectedItem().toString()) + "' and categoryid='" + categoryid + "' ";
                                ps = con.prepareStatement(sql);
                                rs = ps.executeQuery();
                                while (rs.next()) {


                                    double value = 0;
                                    counter++;
                                    row[0] = counter;
                                    row[1] = rs.getString("productname");
                                    row[2] = rs.getString("productid");

                                    double unitss = 0;
                                    double unitsSold = 0;
                                    String prodid = rs.getString("productid");
                                    double buyingprice = rs.getDouble("buyingprice");

                                    ps = con.prepareStatement("Select sum(units) as items,sum(BuyingPrice) from sales where productid='" + prodid + "' ");
                                    ResultSet Rs = ps.executeQuery();
                                    if (Rs.next()) {
                                        unitsSold = Rs.getDouble("items");
                                        value = Rs.getDouble("Sum(BuyingPrice)");


                                    }

                                    // buyingprice=buyingprice*unitsSold; 

                                    total += value;


                                    row[3] = unitsSold;

                                    row[4] = value;


                                    model.addRow(row);

                                }
                                row[0] = "TOTAL";
                                row[1] = "";

                                row[2] = "";
                                row[3] = "";
                                row[4] = total;
                                total = 0;
                                counter = 0;
                                totalProfit = 0;
                                model.addRow(row);
                            } catch (Exception sq) {
                                sq.printStackTrace();
                                JOptionPane.showMessageDialog(null, sq.getMessage());
                            }


                        }


                    }.start();


                } else {
                    JOptionPane.showMessageDialog(holder, "Wrong Date Format");
                }


            }


        } else if (obj == productinfor) {

            if (tab.getSelectedRowCount() < 1) {
                JOptionPane.showMessageDialog(holder, "Select The Product To View Its Information");
            } else {

                String productid = model.getValueAt(tab.getSelectedRow(), 2).toString();
                ReportGenerator.productinfor(productid);


            }
        } else if (obj == refresh) {
            infor.setText("Search product by ref Number,Name or BarCode Number(showing first 100 products)");
            model.getDataVector().removeAllElements();
            ;
            try {
                String sql = "Select productname,products.productid,buyingprice from products limit 100 ";
                ps = con.prepareStatement(sql);
                rs = ps.executeQuery();
                while (rs.next()) {


                    double value = 0;
                    counter++;
                    row[0] = counter;
                    row[1] = rs.getString("productname");
                    row[2] = rs.getString("productid");
                    double unitss = 0;
                    double unitsSold = 0;
                    String prodid = rs.getString("productid");
                    double buyingprice = rs.getDouble("buyingprice");

                    ps = con.prepareStatement("Select sum(units) as items,sum(BuyingPrice) from sales where productid='" + prodid + "' ");
                    ResultSet Rs = ps.executeQuery();
                    if (Rs.next()) {
                        unitsSold = Rs.getDouble("items");
                        value = Rs.getDouble("Sum(BuyingPrice)");


                    }

                    // buyingprice=buyingprice*unitsSold; 

                    total += value;

                    row[3] = unitsSold;

                    row[4] = value;


                    model.addRow(row);

                }
                row[0] = "TOTAL";
                row[1] = "";

                row[2] = "";
                row[3] = "";
                row[4] = total;
                total = 0;
                counter = 0;
                totalProfit = 0;
                model.addRow(row);

            } catch (Exception sq) {
                sq.printStackTrace();
                JOptionPane.showMessageDialog(null, sq.getMessage());
            }

        }
    }

}
