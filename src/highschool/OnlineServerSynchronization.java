/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package highschool;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author FRED
 */
public class OnlineServerSynchronization {
    PreparedStatement ps;
    ResultSet rs;
    Connection con;

    public static void feesSynchronization() {


    }

    public static void upwardSynchronization(String tableName) {
        try {
            PreparedStatement ps;
            ResultSet rs;
            Connection con = DbConnection.connectDb();


            PreparedStatement ps1;
            ResultSet rs1;
            Connection con1 = DbConnection2.connectDb();
            String sql = "SELECT * from " + tableName;
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            ResultSetMetaData rsmd = rs.getMetaData();
            int numberOfColumns = rsmd.getColumnCount();

            String columnVariables = "";
            for (int i = 1; i <= numberOfColumns; i++) {
                if (i == numberOfColumns) {
                    columnVariables = columnVariables + "?";
                } else {
                    columnVariables = columnVariables + "?,";
                }

            }

            sql = "Delete from " + tableName;
            ps1 = con1.prepareStatement(sql);
            ps1.execute();
            sql = "Select * from " + tableName;
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                System.err.println("transmitting...................");
                sql = "insert into " + tableName + " values(" + columnVariables + ")";
                System.err.println("Number Of Columns:" + sql);
                ps1 = con1.prepareStatement(sql);
                for (int i = 1; i <= numberOfColumns; i++) {
                    ps1.setString(i, rs.getString(i));
                }
                ps1.execute();
            }


        } catch (SQLException ex) {
            ex.printStackTrace();

        }
    }


    public static void downwardSynchronization(String tableName) {
        try {
            PreparedStatement ps;
            ResultSet rs;
            Connection con = DbConnection.connectDb();


            PreparedStatement ps1;
            ResultSet rs1;
            Connection con1 = DbConnection2.connectDb();
            String sql = "SELECT * from " + tableName;
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            ResultSetMetaData rsmd = rs.getMetaData();
            int numberOfColumns = rsmd.getColumnCount();

            String columnVariables = "";
            for (int i = 1; i <= numberOfColumns; i++) {
                if (i == numberOfColumns) {
                    columnVariables = columnVariables + "?";
                } else {
                    columnVariables = columnVariables + "?,";
                }

            }

            sql = "Delete from " + tableName;
            ps1 = con1.prepareStatement(sql);
            ps1.execute();
            sql = "Select * from " + tableName;
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                System.err.println("transmitting...................");
                sql = "insert into " + tableName + " values(" + columnVariables + ")";
                System.err.println("Number Of Columns:" + sql);
                ps1 = con1.prepareStatement(sql);
                for (int i = 1; i <= numberOfColumns; i++) {
                    ps1.setString(i, rs.getString(i));
                }
                ps1.execute();
            }


        } catch (SQLException ex) {
            ex.printStackTrace();

        }
    }

    public static void main(String[] args) {
        upwardSynchronization("admission");
    }
}