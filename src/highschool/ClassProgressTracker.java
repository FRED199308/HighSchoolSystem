/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package highschool;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JOptionPane;

/**
 * @author ExamSeverPc
 */
public class ClassProgressTracker {


    public static String currentClass(int academicYear, String Class) {
        String currentClass = "";
        int currentYear = Globals.academicYear();
        Connection con;
        PreparedStatement ps = null;
        ResultSet rs;
        con = DbConnection.connectDb();


        try {
            int press = 0;
            String sql = "Select precision1 from classes where classname='" + Class + "'";

            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            if (rs.next()) {
                press = rs.getInt("precision1");
            }
            if (academicYear == currentYear) {
                return Class;
            } else {
                if (academicYear > currentYear) {
                    return Class;
                } else {
                    int classdiffrence = (currentYear - academicYear);
                    press = (press + classdiffrence);
                    return "Form " + press;

                }
            }


        } catch (SQLException e) {
            e.printStackTrace();
            return "";
        } finally {
            try {
                con.close();
                ps.close();
            } catch (SQLException sq) {

                sq.printStackTrace();
            }

        }


    }

}
