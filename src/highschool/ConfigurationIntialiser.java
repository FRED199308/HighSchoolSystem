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
 * @author FRED
 */
public class ConfigurationIntialiser {
    static PreparedStatement ps = null;
    static ResultSet rs = null;
    static Connection con = null;
    static String status = null;

    public static String senderId() {
        try {
            con = DbConnection.connectDb();
            String querry = "Select status from systemconfiguration where configurationid='" + "CO026" + "'";
            ps = con.prepareStatement(querry);
            rs = ps.executeQuery();
            if (rs.next()) ;
            {
                status = rs.getString("status");
                return status;
            }

        } catch (Exception sq) {
            JOptionPane.showMessageDialog(null, sq, "Error Occurred", JOptionPane.ERROR_MESSAGE);
            return null;
        } finally {
            try {
                con.close();
                ps.close();
            } catch (SQLException sq) {
                JOptionPane.showMessageDialog(null, sq);
                sq.printStackTrace();
            }

        }

    }

    public static String smsUsername1() {
        try {
            con = DbConnection.connectDb();
            String querry = "Select status from systemconfiguration where configurationid='" + "CO013" + "'";
            ps = con.prepareStatement(querry);
            rs = ps.executeQuery();
            if (rs.next()) ;
            {
                status = rs.getString("status");
                return status;
            }

        } catch (Exception sq) {
            JOptionPane.showMessageDialog(null, sq, "Error Occurred", JOptionPane.ERROR_MESSAGE);
            return null;
        } finally {
            try {
                con.close();
                ps.close();
            } catch (SQLException sq) {
                JOptionPane.showMessageDialog(null, sq);
                sq.printStackTrace();
            }

        }
    }

    public static String smsKey() {
        try {
            con = DbConnection.connectDb();
            String querry = "Select status from systemconfiguration where configurationid='" + "CO014" + "'";
            ps = con.prepareStatement(querry);
            rs = ps.executeQuery();
            if (rs.next()) ;
            {
                status = rs.getString("status");
                return status;
            }

        } catch (Exception sq) {
            JOptionPane.showMessageDialog(null, sq, "Error Occurred", JOptionPane.ERROR_MESSAGE);
            return null;
        }

    }

    public static boolean nimbus() {
        try {
            con = DbConnection.connectDb();
            String querry = "Select status from systemconfiguration where configurationid='" + "CO001" + "'";
            ps = con.prepareStatement(querry);
            rs = ps.executeQuery();
            if (rs.next()) ;
            {
                status = rs.getString("status");
                if (status.equalsIgnoreCase("false")) {
                    return false;
                } else {
                    return true;
                }
            }

        } catch (SQLException sq) {
            JOptionPane.showMessageDialog(null, sq, "Error Occurred", JOptionPane.ERROR_MESSAGE);
            return false;
        } finally {
            try {
                con.close();
            } catch (SQLException sq) {
                sq.printStackTrace();
            }
        }


    }


    public static String schoolInfor() {
        try {
            con = DbConnection.connectDb();
            String querry = "Select name from schooldetails";
            ps = con.prepareStatement(querry);
            rs = ps.executeQuery();
            if (rs.next()) ;
            {

                status = rs.getString("name");
                return status;
            }

        } catch (Exception sq) {

            return null;
        }

    }

    public static boolean automaticFeeCupture() {
        try {
            con = DbConnection.connectDb();
            String querry = "Select status from systemconfiguration where configurationid='" + "CO026" + "'";
            ps = con.prepareStatement(querry);
            rs = ps.executeQuery();
            if (rs.next()) ;
            {
                status = rs.getString("status");
                if (status.equalsIgnoreCase("false")) {
                    return false;
                } else {
                    return true;
                }
            }
        } catch (SQLException sq) {
            JOptionPane.showMessageDialog(null, sq.getMessage(), "Error Occurred", JOptionPane.ERROR_MESSAGE);
            return false;
        } finally {
            try {
                con.close();
            } catch (SQLException sq) {
                sq.printStackTrace();
            }
        }

    }

    public static boolean autoReceiptNumber() {
        try {
            con = DbConnection.connectDb();
            String querry = "Select status from systemconfiguration where configurationid='" + "CO028" + "'";
            ps = con.prepareStatement(querry);
            rs = ps.executeQuery();
            if (rs.next()) ;
            {
                status = rs.getString("status");
                if (status.equalsIgnoreCase("false")) {
                    return false;
                } else {
                    return true;
                }
            }
        } catch (SQLException sq) {
            JOptionPane.showMessageDialog(null, sq.getMessage(), "Error Occurred", JOptionPane.ERROR_MESSAGE);
            return false;
        } finally {
            try {
                con.close();
            } catch (SQLException sq) {
                sq.printStackTrace();
            }
        }

    }


    public static boolean automaticVoteheadAmountDistribution() {
        try {
            con = DbConnection.connectDb();
            String querry = "Select status from systemconfiguration where configurationid='" + "CO027" + "'";
            ps = con.prepareStatement(querry);
            rs = ps.executeQuery();
            if (rs.next()) ;
            {
                status = rs.getString("status");
                if (status.equalsIgnoreCase("false")) {
                    return false;
                } else {
                    return true;
                }
            }
        } catch (SQLException sq) {
            JOptionPane.showMessageDialog(null, sq.getMessage(), "Error Occurred", JOptionPane.ERROR_MESSAGE);
            return false;
        } finally {
            try {
                con.close();
            } catch (SQLException sq) {
                sq.printStackTrace();
            }
        }

    }

    public static boolean admissionSort() {
        try {
            con = DbConnection.connectDb();
            String querry = "Select status from systemconfiguration where configurationid='" + "CO025" + "'";
            ps = con.prepareStatement(querry);
            rs = ps.executeQuery();
            if (rs.next()) ;
            {
                status = rs.getString("status");
                if (status.equalsIgnoreCase("false")) {
                    return false;
                } else {
                    return true;
                }
            }
        } catch (SQLException sq) {
            JOptionPane.showMessageDialog(null, sq.getMessage(), "Error Occurred", JOptionPane.ERROR_MESSAGE);
            return false;
        } finally {
            try {
                con.close();
            } catch (SQLException sq) {
                sq.printStackTrace();
            }
        }

    }

    public static boolean windows() {
        try {
            con = DbConnection.connectDb();
            String querry = "Select status from systemconfiguration where configurationid='" + "CO002" + "'";
            ps = con.prepareStatement(querry);
            rs = ps.executeQuery();
            if (rs.next()) ;
            {
                status = rs.getString("status");
                if (status.equalsIgnoreCase("false")) {
                    return false;
                } else {
                    return true;
                }
            }

        } catch (SQLException sq) {
            JOptionPane.showMessageDialog(null, sq, "Error Occurred", JOptionPane.ERROR_MESSAGE);
            return false;
        } finally {
            try {
                con.close();
            } catch (SQLException sq) {
                sq.printStackTrace();
            }
        }

    }

    public static boolean windowsClassic() {
        try {
            con = DbConnection.connectDb();
            String querry = "Select status from systemconfiguration where configurationid='" + "CO003" + "'";
            ps = con.prepareStatement(querry);
            rs = ps.executeQuery();
            if (rs.next()) ;
            {
                status = rs.getString("status");
                if (status.equalsIgnoreCase("false")) {
                    return false;
                } else {
                    return true;
                }
            }

        } catch (SQLException sq) {
            JOptionPane.showMessageDialog(null, sq, "Error Occurred", JOptionPane.ERROR_MESSAGE);
            return false;
        } finally {
            try {
                con.close();
            } catch (SQLException sq) {
                sq.printStackTrace();
            }
        }

    }

    public static String userTheme() {
        try {
            con = DbConnection.connectDb();
            String querry = "Select theme from useraccounts where username='" + Globals.CurrentUser + "'";
            ps = con.prepareStatement(querry);
            rs = ps.executeQuery();
            if (rs.next()) ;
            {
                status = rs.getString("theme");

            }
            return status;

        } catch (SQLException sq) {
            JOptionPane.showMessageDialog(null, sq, "Error Occurred", JOptionPane.ERROR_MESSAGE);
            return "";

        } finally {
            try {
                con.close();
            } catch (SQLException sq) {
                sq.printStackTrace();
            }
        }
    }

    public static boolean cde() {
        try {
            con = DbConnection.connectDb();
            String querry = "Select status from systemconfiguration where configurationid='" + "CO004" + "'";
            ps = con.prepareStatement(querry);
            rs = ps.executeQuery();
            if (rs.next()) ;
            {
                status = rs.getString("status");
                if (status.equalsIgnoreCase("false")) {
                    return false;
                } else {
                    return true;
                }
            }


        } catch (SQLException sq) {
            con = DbConnection.connectDb();
            JOptionPane.showMessageDialog(null, sq, "Error Occurred", JOptionPane.ERROR_MESSAGE);
            return false;
        } finally {
            try {
                con.close();
            } catch (SQLException sq) {
                sq.printStackTrace();
            }
        }

    }

    public static boolean metal() {
        try {
            con = DbConnection.connectDb();
            String querry = "Select status from systemconfiguration where configurationid='" + "CO005" + "'";
            ps = con.prepareStatement(querry);
            rs = ps.executeQuery();
            if (rs.next()) ;
            {
                status = rs.getString("status");
                if (status.equalsIgnoreCase("false")) {
                    return false;
                } else {
                    return true;
                }
            }

        } catch (SQLException sq) {

            JOptionPane.showMessageDialog(null, sq, "Error Occurred", JOptionPane.ERROR_MESSAGE);
            return false;
        } finally {
            try {
                con.close();
            } catch (SQLException sq) {
                sq.printStackTrace();
            }
        }

    }

    public static boolean mac() {
        try {
            con = DbConnection.connectDb();
            String querry = "Select status from systemconfiguration where configurationid='" + "CO006" + "'";
            ps = con.prepareStatement(querry);
            rs = ps.executeQuery();
            if (rs.next()) ;
            {
                status = rs.getString("status");
                if (status.equalsIgnoreCase("false")) {
                    return false;
                } else {
                    return true;
                }
            }

        } catch (SQLException sq) {
            JOptionPane.showMessageDialog(null, sq, "Error Occurred", JOptionPane.ERROR_MESSAGE);
            return false;
        } finally {
            try {
                con.close();
            } catch (SQLException sq) {
                sq.printStackTrace();
            }
        }
    }

    public static boolean passwordChange() {
        try {
            con = DbConnection.connectDb();
            String querry = "Select status from systemconfiguration where configurationid='" + "CO007" + "'";
            ps = con.prepareStatement(querry);
            rs = ps.executeQuery();
            if (rs.next()) ;
            {
                status = rs.getString("status");
                if (status.equalsIgnoreCase("false")) {
                    return false;
                } else {
                    return true;
                }
            }

        } catch (SQLException sq) {
            JOptionPane.showMessageDialog(null, sq, "Error Occurred", JOptionPane.ERROR_MESSAGE);
            return false;
        } finally {
            try {
                con.close();
            } catch (SQLException sq) {
                sq.printStackTrace();
            }
        }

    }

    public static boolean passwordConstraint() {
        try {
            con = DbConnection.connectDb();
            String querry = "Select status from systemconfiguration where configurationid='" + "CO008" + "'";
            ps = con.prepareStatement(querry);
            rs = ps.executeQuery();
            if (rs.next()) ;
            {
                status = rs.getString("status");
                if (status.equalsIgnoreCase("false")) {
                    return false;
                } else {
                    return true;
                }
            }

        } catch (SQLException sq) {
            JOptionPane.showMessageDialog(null, sq, "Error Occurred", JOptionPane.ERROR_MESSAGE);
            return false;
        } finally {
            try {
                con.close();
            } catch (SQLException sq) {
                sq.printStackTrace();
            }
        }

    }

    public static boolean usernameChange() {
        try {
            con = DbConnection.connectDb();
            String querry = "Select status from systemconfiguration where configurationid='" + "CO009" + "'";
            ps = con.prepareStatement(querry);
            rs = ps.executeQuery();
            if (rs.next()) ;
            {
                status = rs.getString("status");
                if (status.equalsIgnoreCase("false")) {
                    return false;
                } else {
                    return true;
                }
            }

        } catch (SQLException sq) {
            JOptionPane.showMessageDialog(null, sq, "Error Occurred", JOptionPane.ERROR_MESSAGE);
            return false;
        } finally {
            try {
                con.close();
            } catch (SQLException sq) {
                sq.printStackTrace();
            }
        }

    }

    public static boolean multipleAdmins() {
        try {
            con = DbConnection.connectDb();
            String querry = "Select status from systemconfiguration where configurationid='" + "CO010" + "'";
            ps = con.prepareStatement(querry);
            rs = ps.executeQuery();
            if (rs.next()) ;
            {
                status = rs.getString("status");
                if (status.equalsIgnoreCase("false")) {
                    return false;
                } else {
                    return true;
                }
            }

        } catch (SQLException sq) {
            JOptionPane.showMessageDialog(null, sq, "Error Occurred", JOptionPane.ERROR_MESSAGE);
            return false;
        } finally {
            try {
                con.close();
            } catch (SQLException sq) {
                sq.printStackTrace();
            }
        }

    }

    public static String reminderTime() {
        try {
            con = DbConnection.connectDb();
            String querry = "Select status from systemconfiguration where configurationid='" + "CO011" + "'";
            ps = con.prepareStatement(querry);
            rs = ps.executeQuery();
            if (rs.next()) ;
            {
                status = rs.getString("status");
                status = rs.getString("status");
                return status;
            }

        } catch (SQLException sq) {
            JOptionPane.showMessageDialog(null, sq, "Error Occurred", JOptionPane.ERROR_MESSAGE);
            return null;
        } finally {
            try {
                con.close();
            } catch (SQLException sq) {
                sq.printStackTrace();
            }
        }
    }

    public static boolean AutoClearance() {
        try {
            con = DbConnection.connectDb();
            String querry = "Select status from systemconfiguration where configurationid='" + "CO012" + "'";
            ps = con.prepareStatement(querry);
            rs = ps.executeQuery();
            if (rs.next()) ;
            {
                status = rs.getString("status");

            }
            if (status.equalsIgnoreCase("true"))
                return true;
            else
                return false;

        } catch (SQLException sq) {
            JOptionPane.showMessageDialog(null, sq, "Error Occurred", JOptionPane.ERROR_MESSAGE);
            return false;
        } finally {
            try {
                con.close();
            } catch (SQLException sq) {
                sq.printStackTrace();
            }
        }

    }

    public static String imageFolder() {
        try {
            con = DbConnection.connectDb();
            String querry = "Select status from systemconfiguration where configurationid='" + "CO023" + "'";
            ps = con.prepareStatement(querry);
            rs = ps.executeQuery();
            if (rs.next()) ;
            {
                status = rs.getString("status");
                return status;
            }

        } catch (SQLException sq) {
            JOptionPane.showMessageDialog(null, sq, "Error Occurred", JOptionPane.ERROR_MESSAGE);
            return "";
        } finally {
            try {
                con.close();
            } catch (SQLException sq) {
                sq.printStackTrace();
            }
        }

    }

    public static boolean lastName() {
        try {
            con = DbConnection.connectDb();
            String querry = "Select status from systemconfiguration where configurationid='" + "CO015" + "'";
            ps = con.prepareStatement(querry);
            rs = ps.executeQuery();
            if (rs.next()) ;
            {
                status = rs.getString("status");
                if (status.equalsIgnoreCase("false")) {
                    return false;
                } else {
                    return true;
                }
            }

        } catch (SQLException sq) {
            JOptionPane.showMessageDialog(null, sq, "Error Occurred", JOptionPane.ERROR_MESSAGE);
            return false;
        } finally {
            try {
                con.close();
            } catch (SQLException sq) {
                sq.printStackTrace();
            }
        }

    }

    public static boolean parentDetails() {
        try {
            con = DbConnection.connectDb();
            String querry = "Select status from systemconfiguration where configurationid='" + "CO016" + "'";
            ps = con.prepareStatement(querry);
            rs = ps.executeQuery();
            if (rs.next()) ;
            {
                status = rs.getString("status");
                if (status.equalsIgnoreCase("false")) {
                    return false;
                } else {
                    return true;
                }
            }
        } catch (SQLException sq) {
            JOptionPane.showMessageDialog(null, sq, "Error Occurred", JOptionPane.ERROR_MESSAGE);
            return false;
        } finally {
            try {
                con.close();
            } catch (SQLException sq) {
                sq.printStackTrace();
            }
        }
    }

    public static boolean residentialDetails() {
        try {
            con = DbConnection.connectDb();
            String querry = "Select status from systemconfiguration where configurationid='" + "CO017" + "'";
            ps = con.prepareStatement(querry);
            rs = ps.executeQuery();
            if (rs.next()) ;
            {
                status = rs.getString("status");
                if (status.equalsIgnoreCase("false")) {
                    return false;
                } else {
                    return true;
                }
            }

        } catch (SQLException sq) {
            JOptionPane.showMessageDialog(null, sq, "Error Occurred", JOptionPane.ERROR_MESSAGE);
            return false;
        } finally {
            try {
                con.close();
            } catch (SQLException sq) {
                sq.printStackTrace();
            }
        }

    }

    public static boolean dateOfBirth() {
        try {
            con = DbConnection.connectDb();
            String querry = "Select status from systemconfiguration where configurationid='" + "CO018" + "'";
            ps = con.prepareStatement(querry);
            rs = ps.executeQuery();
            if (rs.next()) ;
            {
                status = rs.getString("status");
                if (status.equalsIgnoreCase("false")) {
                    return false;
                } else {
                    return true;
                }
            }

        } catch (SQLException sq) {
            JOptionPane.showMessageDialog(null, sq, "Error Occurred", JOptionPane.ERROR_MESSAGE);
            return false;
        } finally {
            try {
                con.close();
            } catch (SQLException sq) {
                sq.printStackTrace();
            }
        }

    }

    public static boolean phone() {
        try {
            con = DbConnection.connectDb();
            String querry = "Select status from systemconfiguration where configurationid='" + "CO019" + "'";
            ps = con.prepareStatement(querry);
            rs = ps.executeQuery();
            if (rs.next()) ;
            {
                status = rs.getString("status");
                if (status.equalsIgnoreCase("false")) {
                    return false;
                } else {
                    return true;
                }
            }
        } catch (SQLException sq) {
            JOptionPane.showMessageDialog(null, sq, "Error Occurred", JOptionPane.ERROR_MESSAGE);
            return false;
        } finally {
            try {
                con.close();
            } catch (SQLException sq) {
                sq.printStackTrace();
            }
        }

    }

    public static boolean image() {
        try {
            con = DbConnection.connectDb();
            String querry = "Select status from systemconfiguration where configurationid='" + "CO020" + "'";
            ps = con.prepareStatement(querry);
            rs = ps.executeQuery();
            if (rs.next()) ;
            {
                status = rs.getString("status");
                if (status.equalsIgnoreCase("false")) {
                    return false;
                } else {
                    return true;
                }
            }

        } catch (SQLException sq) {
            JOptionPane.showMessageDialog(null, sq, "Error Occurred", JOptionPane.ERROR_MESSAGE);
            return false;
        } finally {
            try {
                con.close();
            } catch (SQLException sq) {
                sq.printStackTrace();
            }
        }

    }

    public static boolean docOpener() {
        try {
            con = DbConnection.connectDb();
            String querry = "Select status from systemconfiguration where configurationid='" + "CO021" + "'";
            ps = con.prepareStatement(querry);
            rs = ps.executeQuery();
            if (rs.next()) ;
            {
                status = rs.getString("status");
                if (status.equalsIgnoreCase("false")) {
                    return false;
                } else {
                    return true;
                }
            }
        } catch (SQLException sq) {
            JOptionPane.showMessageDialog(null, sq, "Error Occurred", JOptionPane.ERROR_MESSAGE);
            return false;
        } finally {
            try {
                con.close();
            } catch (SQLException sq) {
                sq.printStackTrace();
            }
        }

    }

    public static boolean smsOfflineSender() {
        try {
            con = DbConnection.connectDb();
            String querry = "Select status from systemconfiguration where configurationid='" + "CO022" + "'";
            ps = con.prepareStatement(querry);
            rs = ps.executeQuery();
            if (rs.next()) ;
            {
                status = rs.getString("status");
                if (status.equalsIgnoreCase("false")) {
                    return false;
                } else {
                    return true;
                }
            }
        } catch (SQLException sq) {
            JOptionPane.showMessageDialog(null, sq, "Error Occurred", JOptionPane.ERROR_MESSAGE);
            return false;
        } finally {
            try {
                con.close();
            } catch (SQLException sq) {
                sq.printStackTrace();
            }
        }

    }

    public static boolean migrationReadiness() {
        try {
            con = DbConnection.connectDb();
            String querry = "Select status from systemconfiguration where configurationid='" + "CO024" + "'";
            ps = con.prepareStatement(querry);
            rs = ps.executeQuery();
            if (rs.next()) ;
            {
                status = rs.getString("status");
                if (status.equalsIgnoreCase("false")) {
                    return false;
                } else {
                    return true;
                }
            }
        } catch (SQLException sq) {
            JOptionPane.showMessageDialog(null, sq.getMessage(), "Error Occurred", JOptionPane.ERROR_MESSAGE);
            return false;
        } finally {
            try {
                con.close();
            } catch (SQLException sq) {
                sq.printStackTrace();
            }
        }

    }

}
