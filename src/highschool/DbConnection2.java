/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package highschool;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import javax.swing.JOptionPane;

/**
 * @author FRED_ADMIN
 */
public class DbConnection2 {

    static Connection con = null;
    public static String databaseLink;
    public static String password;
    public static String username;


    public static Connection connectDb() {
        try {

            Class.forName("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://server/gathara_database", "root", "root");

            return con;
        } catch (ClassNotFoundException | SQLException sq) {

            JOptionPane.showMessageDialog(null, sq.getMessage(), "    Error in Connecting The Database.    Link Failure."
                    + "    Server Not Accessible", JOptionPane.ERROR_MESSAGE);

            return connectDb();
        }


    }

    public static void main(String[] args) {
        DbConnection.connectDb();
    }
}
