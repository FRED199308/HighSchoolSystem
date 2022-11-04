/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package highschool;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 * @author FREDDY
 */
public class ProductSalesUpdater {

    private Connection con = DbConnection.connectDb();
    private PreparedStatement ps;
    private ResultSet rs;

    public ProductSalesUpdater() {

        try {
            ps = con.prepareStatement("Select buyingprice,productid from products");
            rs = ps.executeQuery();
            while (rs.next()) {
                String productid = rs.getString("Productid");
                ps = con.prepareStatement("Select units,transactionnumber from sales where productid='" + productid + "'");
                ResultSet RS = ps.executeQuery();
                while (RS.next()) {
                    double bpr = rs.getDouble("buyingprice") * RS.getDouble("units");
                    String transactionNumber = RS.getString("transactionnumber");
                    ps = con.prepareStatement("Update sales set buyingprice='" + bpr + "' where productid='" + productid + "' and transactionNumber='" + transactionNumber + "'");
                    ps.execute();
                }


            }

            System.err.println("Sales Updated");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new ProductSalesUpdater();
    }

}
