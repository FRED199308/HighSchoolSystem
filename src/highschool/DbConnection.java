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
 * director
 *
 * @author FRED_ADMIN
 */
public class DbConnection {

    static Connection con = null;
    public static String databaseLink;
    public static String password;
    public static String username;


    public static Connection connectDb() {
        try {

            Class.forName("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/kamarandi_database", "root", "root");

            return con;
        } catch (ClassNotFoundException | SQLException sq)
        {
            
            sq.printStackTrace();
            JOptionPane.showMessageDialog(null, sq.getMessage(), "    Error in Connecting The Database.    Link Failure."
                    + "    Server Not Accessible", JOptionPane.ERROR_MESSAGE);

            return null;
        }


    }

}
