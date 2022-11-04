package highschool;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Date;



/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 * @author ExamSeverPc
 */
public class PerformanceHistory {

    static PreparedStatement ps;
    static ResultSet rs;
    static Connection con = DbConnection.connectDb();

    public static Object[][] performanceSummary(String examclass, String examname, int year, String adm) {
        Object[][] performance = new Object[4][13];
        int classnumber = 1;
        try {
            performance[0][0] = "Period";
            performance[1][0] = "Mean Grade";
            performance[2][0] = "Mean Score";
            performance[3][0] = "Overall Pos";

            String querry = "Select precision1 from classes where classname='" + examclass + "'";
            ps = con.prepareStatement(querry);
            rs = ps.executeQuery();
            if (rs.next()) {
                classnumber = rs.getInt("precision1");
            }
            int classc = 1;
            String period = "";
            int k = 1;
            while (k < 13) {


                for (int t = 1; t < 4; t++) {

                    period = "F" + classc + " " + "T" + t;

                    performance[0][k] = period;
                    k++;
                    if (k == 4 || k == 7 || k == 10) {
                        classc++;

                    }
                }


            }
            int classcounter;
            classc = 1;
            k = 1;
            int admissionYear = ((year - classnumber) + 1);
            int rowpointer = 0;
            int columncounter = 1;
            int yeardeviation = year;
            int limityear = year - 3;
            while (year >= admissionYear && k < 13) {


                for (int t = 1; t < 4; t++) {
                    String examcode = ExamCodesGenerator.generatecode("Form " + classc, String.valueOf(admissionYear), "Term " + t, examname);


                    String sql = "Select meangrade,meanpoints,classpositionthisterm from examanalysistable where admnumber='" + adm + "' and examcode='" + examcode + "' group by admnumber";
                    ps = con.prepareStatement(sql);
                    rs = ps.executeQuery();
                    if (rs.next()) {

                        performance[1][k] = rs.getString("meangrade");
                        performance[2][k] = rs.getString("meanpoints");
                        performance[3][k] = rs.getString("classpositionthisterm");

                    }

                    k++;
                    if (k == 4 || k == 7 || k == 10) {
                        classc++;

                    }
                }

                admissionYear++;
            }


        } catch (Exception sq) {
            sq.printStackTrace();
        }


        return performance;
    }

    public static void main(String[] args) {

    }
}
