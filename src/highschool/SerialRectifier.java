/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package highschool;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.swing.JOptionPane;

/**
 * @author FRED
 */
public class SerialRectifier {

    public static void bookSerialRectifier() {
        PreparedStatement ps;
        ResultSet rs;
        Connection con = DbConnection.connectDb();
        try {

            String sql = "Select * from books";
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                String bookserial = rs.getString("BookSerial");
                if (bookserial.startsWith("F")) {

                } else {


                    String classlevel = Globals.className(rs.getString("classcode"));
                    String subject = Globals.subjectName(rs.getString("SubjectCode"));
                    String newSerialPrefix = classlevel.charAt(0) + "" + classlevel.charAt(5);
                    String newSerialInter = bookserial.substring(3);
                    String newSerial = newSerialPrefix + newSerialInter;
                    ps = con.prepareStatement("Update books set bookserial='" + newSerial + "' where bookserial='" + bookserial + "'");
                    ps.execute();

                }


            }

            JOptionPane.showMessageDialog(null, "Book Serials Repaired");

        } catch (Exception sq) {
            sq.printStackTrace();
        }
    }


}
