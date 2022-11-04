/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package highschool;


import java.awt.TrayIcon;
import java.io.File;

import static java.lang.Thread.sleep;

import java.security.CodeSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.JOptionPane;

/**
 * @author FRED
 */
public class Globals {

    public static String CurrentUser;
    public static String activationStatus;
    public static String Level;
    public static String empcode;
    public static String depcode;
    public static String depName;
    public static String initials;
    public static String feel;
    public static String composerRecipient = "";
    public static String moduleName;
    public static String look = "";
    public static TrayIcon systemTray;


    public static float dayReminder;
    public static String singleAdmissionAllocator = "";

    public static String singleyearAllocator = "";
    public static String bookSerial = "";
    public static int pictureWidth = 1280;
    public static int pictureHeight = 800;

    public static String sortcode = " b  ";

    public static void SortIntialiser() {
        if (ConfigurationIntialiser.admissionSort()) {
            sortcode = " order by admissionNumber  ";
        } else {
            sortcode = " order by upi  ";
        }


    }

    static int presentsmsCounter = 0;
    static int presentsentsmsCounter = 0;

    public static void MessageinTracker() {

        Connection con;
        PreparedStatement ps = null;
        ResultSet rs;
        con = DbConnection.connectDb();
        try {

            String sql = "Select count(*) from ozekimessagein ";
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            if (rs.next()) {
                presentsmsCounter = rs.getInt("count(*)");
            }

            new Thread() {
                public void run() {
                    for (; ; ) {
                        try {

                            sleep(1000);
                            int presentsmsCounter2 = 0;
                            String sql = "Select count(*) from ozekimessagein ";
                            PreparedStatement pss = con.prepareStatement(sql);
                            ResultSet rss = pss.executeQuery();
                            if (rss.next()) {
                                presentsmsCounter2 = rss.getInt("count(*)");
                            }
                            int messages = 0;
                            messages = presentsmsCounter2 - presentsmsCounter;


                            if (presentsmsCounter2 > presentsmsCounter) {
                                Globals.systemTray.displayMessage("New Message", messages + " New Message(s) Received", TrayIcon.MessageType.INFO);
                            }
                            presentsmsCounter = presentsmsCounter2;
                        } catch (Exception e) {
                        }
                    }
                }

            }.start();
        } catch (SQLException e) {
            e.printStackTrace();

        }

    }


    public static void MessageOutTracker() {

        Connection con;
        PreparedStatement ps = null;
        ResultSet rs;
        con = DbConnection.connectDb();
        try {

            String sql = "Select count(*) from messagelog where statuscode='" + "200" + "' or statuscode='" + "201" + "'";
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            if (rs.next()) {
                presentsentsmsCounter = rs.getInt("count(*)");
            }

            new Thread() {
                public void run() {
                    for (; ; ) {
                        try {

                            sleep(1000);
                            int presentsmsCounter2 = 0;
                            String sql = "Select count(*) from messagelog where statuscode='" + "200" + "' or statuscode='" + "201" + "'";
                            PreparedStatement pss = con.prepareStatement(sql);
                            ResultSet rss = pss.executeQuery();
                            if (rss.next()) {
                                presentsmsCounter2 = rss.getInt("count(*)");
                            }

                            int messages = 0;
                            messages = presentsmsCounter2 - presentsentsmsCounter;


                            if (presentsmsCounter2 > presentsentsmsCounter) {
                                Globals.systemTray.displayMessage("Message Sent", messages + " New Message(s) sent", TrayIcon.MessageType.INFO);
                            }
                            presentsentsmsCounter = presentsmsCounter2;
                        } catch (Exception e) {
                        }
                    }
                }

            }.start();
        } catch (SQLException e) {
            e.printStackTrace();

        }

    }


    public static void kcpeAnalysis(String classcode) {
        Connection con;
        PreparedStatement ps = null;
        ResultSet rs;
        con = DbConnection.connectDb();


        try {
            int previous = 0;
            int pos = 0;
            int tiecheck = 0;
            String sql = "Select kcpemarks,admissionnumber from admission where currentform='" + classcode + "' order by kcpemarks desc";
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {

                int kcpemarks = rs.getInt("Kcpemarks");
                String adm = rs.getString("AdmissionNumber");
                ps = con.prepareStatement("Delete from kcperanking where admissionnumber='" + adm + "'");
                ps.execute();

                if (kcpemarks == previous) {
                    tiecheck++;

                } else {
                    pos++;
                    pos = tiecheck + pos;
                    tiecheck = 0;
                }

                ps = con.prepareStatement("Insert into kcperanking values ('" + kcpemarks + "','" + pos + "','" + adm + "')");
                ps.execute();

                previous = rs.getInt("Kcpemarks");
            }


        } catch (Exception e) {
            e.printStackTrace();
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

    public static String voteHeadId(String Voteheadname) {
        Connection con;
        PreparedStatement ps = null;
        ResultSet rs;
        con = DbConnection.connectDb();
        try {
            String sql = "Select voteheadid from voteheads where voteheadname ='" + Voteheadname + "' ";

            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            if (rs.next()) {

                return rs.getString("voteheadid");
            } else {

                return "";
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return "";
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


    public static String Grade(String score) {
        Connection con;
        PreparedStatement ps = null;
        ResultSet rs;
        con = DbConnection.connectDb();
        try {


            if (score == null || score.isEmpty()) {
                return "";
            } else {
                String sqll = "Select grade from kcpetable where  '" + score + "'>=starting_from and '" + score + "'<=ending_at  group by sort_code";
                ps = con.prepareStatement(sqll);
                ResultSet RS = ps.executeQuery();
                if (RS.next()) {

                    return (RS.getString("grade"));

                } else {
                    return "";
                }
            }


        } catch (SQLException e) {
            e.printStackTrace();
            return "";
        } finally {
            try {
                con.close();
                // ps.close();
            } catch (SQLException sq) {
                JOptionPane.showMessageDialog(null, sq);
                sq.printStackTrace();
            }

        }

    }

    public static double targetPoints(String ExamCode, String subjectCode) {
        Connection con;
        PreparedStatement ps = null;
        ResultSet rs;
        con = DbConnection.connectDb();
        try {
            String sql = "Select points from examtargets where classcode ='" + ExamCode + "' and subjectcode='" + subjectCode + "' ";

            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            if (rs.next()) {

                return rs.getDouble("points");
            } else {

                return 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
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

    public static double totalTargetPoints(String classcode, String academicYear) {
        Connection con;
        PreparedStatement ps = null;
        ResultSet rs;
        con = DbConnection.connectDb();
        try {
            String sql = "Select points from examtargets where classcode ='" + classcode + "' and academicYear='" + academicYear + "' ";

            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            if (rs.next()) {

                return rs.getDouble("points");
            } else {

                return 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
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

    public static String targetGrade(String ExamCode, String subjectCode) {
        Connection con;
        PreparedStatement ps = null;
        ResultSet rs;
        con = DbConnection.connectDb();
        try {
            String sql = "Select grade from examtargets where examcode ='" + ExamCode + "' and subjectcode='" + subjectCode + "' ";

            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            if (rs.next()) {

                return rs.getString("grade");
            } else {

                return "";
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return "";
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


    public static double nextTermFee(String admNumber, String termCode, String year, String classcode) {
        Connection con;
        PreparedStatement ps = null;
        ResultSet rs;
        con = DbConnection.connectDb();
        try {
            String program = Globals.studentProgram(admNumber);
            double nextTermFee = 0;
            String ss = "Select sum(AmountPerHead) from studentpayablevoteheads where   termcode='" + termCode + "' and academicYear='" + year + "' and classcode='" + classcode + "' and program='" + program + "'";
            ps = con.prepareStatement(ss);
            rs = ps.executeQuery();
            if (rs.next()) {
                nextTermFee = rs.getDouble("sum(AmountPerHead)");
            }


            return nextTermFee;
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
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


    public static double OpeningBalance(String academicYear, String voteheadid, String adm) {
        Connection con;
        PreparedStatement ps = null;
        ResultSet rs;
        con = DbConnection.connectDb();
        String year = academicYear;
        if (voteheadid == null) {
            try {
                double expected = 0, paid = 0;
                String sql = "Select sum(amount) from reciepts where academicYear<'" + year + "' and admnumber='" + adm + "'";

                ps = con.prepareStatement(sql);
                rs = ps.executeQuery();
                if (rs.next()) {

                    paid = rs.getDouble("sum(amount)");
                }
                sql = "Select sum(Amount) from payablevoteheadperstudent where  academicYear<'" + year + "' and admnumber='" + adm + "'";

                ps = con.prepareStatement(sql);
                rs = ps.executeQuery();
                if (rs.next()) {

                    expected = rs.getDouble("sum(Amount)");
                }

                return expected - paid;

            } catch (SQLException e) {
                e.printStackTrace();
                return 0;
            } finally {
                try {
                    con.close();
                    ps.close();
                } catch (SQLException sq) {
                    JOptionPane.showMessageDialog(null, sq);
                    sq.printStackTrace();
                }

            }
        } else {
            try {
                double expected = 0, paid = 0;
                String sql = "Select sum(amount) from reciepts where voteheadname ='" + voteheadid + "'  and academicYear<'" + year + "' and admnumber='" + adm + "'";

                ps = con.prepareStatement(sql);
                rs = ps.executeQuery();
                if (rs.next()) {

                    paid = rs.getDouble("sum(amount)");
                }
                sql = "Select sum(Amount) from payablevoteheadperstudent where voteheadname ='" + voteheadid + "'  and academicYear<'" + year + "'  and admnumber='" + adm + "'";

                ps = con.prepareStatement(sql);
                rs = ps.executeQuery();
                if (rs.next()) {

                    expected = rs.getDouble("sum(Amount)");
                }

                return expected - paid;

            } catch (SQLException e) {
                e.printStackTrace();
                return 0;
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


    }


    public static String admissionNumberForTheName(String combinedName) {
        Connection con;
        PreparedStatement ps = null;
        ResultSet rs;
        con = DbConnection.connectDb();
        try {
            int namematch = 0;
            String admno = "";
            String querry41 = "Select firstname,middlename,lastname,admissionNumber from admission ";
            ps = con.prepareStatement(querry41);
            ResultSet Rs = ps.executeQuery();
            while (Rs.next()) {
                if ((Rs.getString("FirstName") + "       " + Rs.getString("Middlename") + "     " + Rs.getString("Lastname")).equalsIgnoreCase(combinedName)) {
                    admno = Rs.getString("admissionNumber");
                    namematch++;
                    if (namematch > 1) {
                        break;
                    }
                }
            }

            if (namematch > 0) {
                return admno;
            } else {
                return "";
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return "";
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

    public static String receiptConflictChecker(String receiptNumber) {
        Connection con;
        PreparedStatement ps = null;
        ResultSet rs;
        con = DbConnection.connectDb();
        try {

            String sql = "Select admnumber from reciepts where recieptnumber='" + receiptNumber + "'";
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getString("admnumber");
            } else {
                return "";
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return "";
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

    public static int classPrecisionDeterminer(String classIdentifier) {
        Connection con;
        PreparedStatement ps = null;
        ResultSet rs;
        con = DbConnection.connectDb();

        try {

            String sql = "Select precision1 from classes where classcode='" + classIdentifier + "' or classname='" + classIdentifier + "'";

            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            if (rs.next()) {

                return rs.getInt("precision1");
            } else {
                return 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
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

    public static int termPrecisionDeterminer(String termIdentifier) {
        Connection con;
        PreparedStatement ps = null;
        ResultSet rs;
        con = DbConnection.connectDb();

        try {

            String sql = "Select Precisions from terms where termcode='" + termIdentifier + "' or termname='" + termIdentifier + "'";

            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            if (rs.next()) {

                return rs.getInt("Precisions");
            } else {
                return 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
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

    public static String completionYear(String adm) {


        Connection con;
        PreparedStatement ps = null;
        ResultSet rs;
        con = DbConnection.connectDb();
        try {

            String sql = "Select yearofcompletion from completionclasslists where admnumber='" + adm + "'";
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            if (rs.next()) {
                return String.valueOf(rs.getInt("yearofcompletion"));
            } else {
                return "";
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return "";
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


    public static String AccountName(String accountcode) {
        Connection con;
        PreparedStatement ps = null;
        ResultSet rs;
        con = DbConnection.connectDb();
        try {
            String sql = "Select AccountName from schoolAccounts where AccountId='" + accountcode + "'";
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getString("AccountName");
            } else {
                return "";
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return "";
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

    public static String AccountCode(String accountName) {
        Connection con;
        PreparedStatement ps = null;
        ResultSet rs;
        con = DbConnection.connectDb();
        try {
            String sql = "Select Accountid from schoolAccounts where AccountName='" + accountName + "'";
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getString("Accountid");
            } else {
                return "";
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return "";
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

    public static String nameAmbiguityTest(String combinedName) {
        Connection con;
        PreparedStatement ps = null;
        ResultSet rs;
        con = DbConnection.connectDb();
        try {
            int namematch = 0;
            String admno = "";
            String querry41 = "Select firstname,middlename,lastname,admissionNumber from admission ";
            ps = con.prepareStatement(querry41);
            ResultSet Rs = ps.executeQuery();
            while (Rs.next()) {
                if ((Rs.getString("FirstName") + "       " + Rs.getString("Middlename") + "     " + Rs.getString("Lastname")).equalsIgnoreCase(combinedName)) {
                    admno = Rs.getString("admissionNumber");
                    namematch++;
                    if (namematch > 1) {
                        break;
                    }
                }
            }

            if (namematch > 1) {
                return "1";
            } else {
                return "2";
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return "";
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

    public static double balanceCalculator(String admNumber) {
        Connection con;
        PreparedStatement ps = null;
        ResultSet rs;
        con = DbConnection.connectDb();
        try {
            double paid = 0, payable = 0;
            String sql = "Select sum(amount) from PayableVoteHeadPerStudent where admnumber ='" + admNumber + "' ";

            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            if (rs.next()) {

                payable = rs.getInt("sum(amount)");
            }

            ps = con.prepareStatement("Select sum(amount) from reciepts where admnumber ='" + admNumber + "' ");


            rs = ps.executeQuery();
            if (rs.next()) {

                paid = rs.getDouble("sum(amount)");
            }

            double bal = (payable - paid);

            return bal;
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
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

    public static double voteHeadBalanceCalculator(String voteheadid, String accountid) {
        Connection con;
        PreparedStatement ps = null;
        ResultSet rs;
        con = DbConnection.connectDb();
        double bal = 0.0, sumused = 0.0, sumincome = 0.0;
        try {

            ps = con.prepareStatement("Select sum(amount) from cashbookanalysis where accountid='" + accountid + "' and voteheadid='" + voteheadid + "' and Nature='" + "DEP" + "' ");
            ResultSet Rs = ps.executeQuery();
            if (Rs.next()) {
                sumincome = Rs.getDouble("Sum(amount)");
            }
            ps = con.prepareStatement("Select sum(amount) from cashbookanalysis where accountid='" + accountid + "' and voteheadid='" + voteheadid + "' and Nature='" + "WD" + "'");
            Rs = ps.executeQuery();
            if (Rs.next()) {
                sumused = Rs.getDouble("Sum(amount)");
            }
            bal = sumincome - sumused;
            return bal;

        } catch (SQLException e) {
            e.printStackTrace();
            return 0.0;
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


    public static double bursaryPayCalculator() {


        String pymode = "Bursary";
        Connection con;
        PreparedStatement ps = null;
        ResultSet rs;
        con = DbConnection.connectDb();
        double bal = 0.0, sumused = 0.0, sumincome = 0.0;
        try {

            ps = con.prepareStatement("Select sum(amount) from reciepts where paymentmode='" + pymode + "' and academicyear='" + Globals.academicYear() + "'");
            ResultSet Rs = ps.executeQuery();
            if (Rs.next()) {
                sumincome = Rs.getDouble("Sum(amount)");
            }

            return sumincome;

        } catch (SQLException e) {
            e.printStackTrace();
            return 0.0;
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

    public static double accountBalanceCashAtBankCalculator(String accountid) {
        Connection con;
        PreparedStatement ps = null;
        ResultSet rs;
        con = DbConnection.connectDb();
        double bal = 0.0, sumused = 0.0, sumincome = 0.0;
        try {

            ps = con.prepareStatement("Select sum(amount) from cashbookanalysis where accountid='" + accountid + "'  and Nature='" + "DEP" + "' and year(Date)='" + Globals.academicYear() + "' and mode='" + "Bank" + "' ");
            ResultSet Rs = ps.executeQuery();
            if (Rs.next()) {
                sumincome = Rs.getDouble("Sum(amount)");
            }

            ps = con.prepareStatement("Select sum(amount) from cashbookanalysis where accountid='" + accountid + "' and Nature='" + "WD" + "'  and year(Date)='" + Globals.academicYear() + "' and Mode='" + "Bank" + "' ");
            Rs = ps.executeQuery();
            if (Rs.next()) {
                sumused = Rs.getDouble("Sum(amount)");
            }
            bal = sumincome - sumused;
            return bal;

        } catch (SQLException e) {
            e.printStackTrace();
            return 0.0;
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

    public static double accountBalanceCashInHandCalculatoor(String accountid) {
        Connection con;
        PreparedStatement ps = null;
        ResultSet rs;
        con = DbConnection.connectDb();
        double bal = 0.0, sumused = 0.0, sumincome = 0.0;
        try {

            ps = con.prepareStatement("Select sum(amount) from cashbookanalysis where accountid='" + accountid + "'  and Nature='" + "DEP" + "' and year(Date)='" + Globals.academicYear() + "' and mode='" + "Cash" + "' ");
            ResultSet Rs = ps.executeQuery();
            if (Rs.next()) {
                sumincome = Rs.getDouble("Sum(amount)");
            }
            System.err.println(sumincome);
            ps = con.prepareStatement("Select sum(amount) from cashbookanalysis where accountid='" + accountid + "' and Nature='" + "WD" + "'  and year(Date)='" + Globals.academicYear() + "' and mode='" + "Cash" + "'");
            Rs = ps.executeQuery();
            if (Rs.next()) {
                sumused = Rs.getDouble("Sum(amount)");
            }
            bal = sumincome - sumused;
            return bal;

        } catch (SQLException e) {
            e.printStackTrace();
            return 0.0;
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

    public static double accountBalanceCalculator(String accountid) {
        Connection con;
        PreparedStatement ps = null;
        ResultSet rs;
        con = DbConnection.connectDb();
        double bal = 0.0, sumused = 0.0, sumincome = 0.0;
        try {

            ps = con.prepareStatement("Select sum(amount) from cashbookanalysis where accountid='" + accountid + "'  and Nature='" + "DEP" + "' and year(Date)='" + Globals.academicYear() + "' ");
            ResultSet Rs = ps.executeQuery();
            if (Rs.next()) {
                sumincome = Rs.getDouble("Sum(amount)");
            }
            System.err.println(sumincome);
            ps = con.prepareStatement("Select sum(amount) from cashbookanalysis where accountid='" + accountid + "' and Nature='" + "WD" + "'  and year(Date)='" + Globals.academicYear() + "' ");
            Rs = ps.executeQuery();
            if (Rs.next()) {
                sumused = Rs.getDouble("Sum(amount)");
            }
            bal = sumincome - sumused;
            return bal;

        } catch (SQLException e) {
            e.printStackTrace();
            return 0.0;
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

    public static double accountBalanceCalculatorAtADate(String accountid, String date) {
        Connection con;
        PreparedStatement ps = null;
        ResultSet rs;
        con = DbConnection.connectDb();
        double bal = 0.0, sumused = 0.0, sumincome = 0.0;
        try {

            ps = con.prepareStatement("Select sum(amount) from cashbookanalysis where accountid='" + accountid + "'  and Nature='" + "DEP" + "' and Date<'" + date + "' ");
            ResultSet Rs = ps.executeQuery();
            if (Rs.next()) {
                sumincome = Rs.getDouble("Sum(amount)");
            }
            System.err.println(sumincome);
            ps = con.prepareStatement("Select sum(amount) from cashbookanalysis where accountid='" + accountid + "' and Nature='" + "WD" + "'  and Date<'" + date + "' ");
            Rs = ps.executeQuery();
            if (Rs.next()) {
                sumused = Rs.getDouble("Sum(amount)");
            }
            bal = sumincome - sumused;
            return bal;

        } catch (SQLException e) {
            e.printStackTrace();
            return 0.0;
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

    public static String VoteHeadName(String voteheadid) {
        Connection con;
        PreparedStatement ps = null;
        ResultSet rs;
        con = DbConnection.connectDb();

        try {

            String sql = "Select voteheadname from voteheads where voteheadid='" + voteheadid + "' ";

            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getString("voteheadname");
            } else {
                return "";
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return "";
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

    public static void cashBookUpdater(String particulars, String date, String transactionid, String transactionNature, int amount, String transactionMode, String lf, String accountid, String voteheadid) {
        Connection con;
        PreparedStatement ps = null;
        ResultSet rs;
        con = DbConnection.connectDb();

        try {

            if (transactionMode.equalsIgnoreCase("Bank") && transactionNature.equalsIgnoreCase("DEP")) {
                int cashbookvalue = 0, finalcachbookvalue = 0;
                String querry0 = "Select max(date),bankbal from cashbookanalysis where  mode='" + "Bank" + "' and accountid='" + accountid + "'";
                ps = con.prepareStatement(querry0);
                ResultSet rs1 = ps.executeQuery();
                while (rs1.next()) {
                    cashbookvalue = rs1.getInt("bankbal");
                }

                finalcachbookvalue = (cashbookvalue + amount);
                String querry = "Insert into Cashbookanalysis values('" + particulars + "',Now(),'" + transactionid + "','" + "DEP" + "','" + amount + "','" + "0" + "','" + finalcachbookvalue + "','" + "Bank" + "','" + lf + "','" + accountid + "','" + voteheadid + "')";
                ps = con.prepareStatement(querry);
                ps.execute();

            } else if (transactionMode.equalsIgnoreCase("Cash") && transactionNature.equalsIgnoreCase("DEP")) {

                int cashbookvalue = 0, finalcachbookvalue = 0;
                String querry0 = "Select max(date),cashbal from cashbookanalysis where  mode='" + "cash" + "' and accountid='" + accountid + "'";
                ps = con.prepareStatement(querry0);
                ResultSet rs1 = ps.executeQuery();
                while (rs1.next()) {
                    cashbookvalue = rs1.getInt("cashbal");
                }
                finalcachbookvalue = (cashbookvalue + amount);
                String querry = "Insert into Cashbookanalysis values('" + particulars + "',Now(),'" + transactionid + "','" + "DEP" + "','" + amount + "','" + "0" + "','" + finalcachbookvalue + "','" + "Cash" + "','" + lf + "','" + accountid + "','" + voteheadid + "')";
                ps = con.prepareStatement(querry);
                ps.execute();


            } else if (transactionMode.equalsIgnoreCase("Bank") && transactionNature.equalsIgnoreCase("WD")) {


            } else if (transactionMode.equalsIgnoreCase("Cash") && transactionNature.equalsIgnoreCase("WD")) {


            }
        } catch (SQLException e) {
            e.printStackTrace();

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


    public static String subjectCode(String subjectname) {
        Connection con;
        PreparedStatement ps = null;
        ResultSet rs;
        con = DbConnection.connectDb();
        try {
            String sql = "Select subjectcode from subjects where subjectname='" + subjectname + "'";

            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getString("subjectcode");
            } else {
                return "";
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return "";
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


    public static boolean gradable(String adm, String examcode, String academicYear) {
        Connection con;
        String analysisMode = "";
        PreparedStatement ps = null;
        ResultSet rs;
        con = DbConnection.connectDb();
        try {


            int entries = 0;
            String sql = "Select count(*) from examanalysistable where examcode='" + examcode + "' and admnumber='" + adm + "'";

            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            if (rs.next()) {
                entries = rs.getInt("count(*)");
            }

            int subtaking = Globals.subjectAllocationCounter(adm, academicYear);
            String sql2 = "Select AnalysisMode from Examanalysismodes  where examcode='" + examcode + "'";
            ps = con.prepareStatement(sql2);
            rs = ps.executeQuery();
            if (rs.next()) {
                analysisMode = rs.getString("AnalysisMode");
            } else {
                analysisMode = "All";
            }

            if (analysisMode.equalsIgnoreCase("By Seven")) {
                if (entries < subtaking) {
                    return false;
                } else {
                    return true;
                }
            } else {

                if (subtaking == entries || entries > subtaking) {
                    return true;
                } else {
                    return false;
                }

            }


        } catch (SQLException e) {
            e.printStackTrace();
            return false;
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


    public static boolean gradableeditted(String adm, String examcode, String academicYear) {
        Connection con;
        String analysisMode = "";
        PreparedStatement ps = null;
        ResultSet rs;
        con = DbConnection.connectDb();
        try {


            int entries = 0;
            String sql = "Select count(*) from examanalysistable where examcode='" + examcode + "' and admnumber='" + adm + "'";

            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            if (rs.next()) {
                entries = rs.getInt("count(*)");
            }

            int subtaking = Globals.subjectAllocationCounter(adm, academicYear);
            String sql2 = "Select AnalysisMode from Examanalysismodes  where examcode='" + examcode + "'";
            ps = con.prepareStatement(sql2);
            rs = ps.executeQuery();
            if (rs.next()) {
                analysisMode = rs.getString("AnalysisMode");
            } else {
                analysisMode = "All";
            }

            if (analysisMode.equalsIgnoreCase("By Seven")) {
                int langcount = 0, sciencecount = 0, humancount = 0, mathcount = 0, techcount = 0;
                int count = 0;
                ps = con.prepareStatement("Select distinct category from subjects");
                rs = ps.executeQuery();
                while (rs.next()) {
                    String category = rs.getString("Category");

                    int subcount = 0;
//                    ps=con.prepareStatement("Select subjectcode from subjects where category='"+category+"'");
//                    ResultSet rx=ps.executeQuery();
//                    while(rx.next())
//                    {
//                        String subjectcode=rx.getString("SubjectCode");
//                        
//                                             ps=con.prepareStatement("Select DISTINCT examanalysistable.SUBJECTCODE from examanalysistable,subjects where examcode='"+examcode+"'  and subjectcode='"+category+"' and admNumber='"+adm+"' and  subjects.subjectcode =examanalysistable.subjectcode");
//                      ResultSet rr=ps.executeQuery();
//                      while(rr.next())
//                      {
//                       
//                        subcount++;
//                          System.err.println(rr.getString("examanalysistable.SUBJECTCODE"));
//                      } 
//                        System.err.println("adm:"+adm);
//                        System.err.println("The subuject Count "+subcount); 
//                    }

                    if (category.equalsIgnoreCase("Languages")) {
                        ps = con.prepareStatement("Select distinct examanalysistable.SubjectCode from examanalysistable,subjects where examcode='" + examcode + "'  and category='" + category + "' and admNumber='" + adm + "' and subjects.subjectcode =examanalysistable.subjectcode ");
                        ResultSet rr = ps.executeQuery();
                        while (rr.next()) {

                            langcount++;

                        }
                        if (langcount > 2) {
                            langcount = 2;
                        }
                        count = count + langcount;

                    } else if (category.equalsIgnoreCase("Mathematics")) {
                        ps = con.prepareStatement("Select distinct examanalysistable.SubjectCode from examanalysistable,subjects where examcode='" + examcode + "'  and category='" + category + "' and admNumber='" + adm + "' and subjects.subjectcode =examanalysistable.subjectcode ");
                        ResultSet rr = ps.executeQuery();
                        while (rr.next()) {

                            mathcount++;

                        }
                        if (mathcount > 1) {
                            mathcount = 1;
                        }
                        count = count + mathcount;

                    } else if (category.equalsIgnoreCase("Sciences")) {
                        ps = con.prepareStatement("Select distinct examanalysistable.SubjectCode from examanalysistable,subjects where examcode='" + examcode + "'  and category='" + category + "' and admNumber='" + adm + "' and subjects.subjectcode =examanalysistable.subjectcode ");
                        ResultSet rr = ps.executeQuery();
                        while (rr.next()) {

                            sciencecount++;

                        }
                        if (sciencecount > 2) {
                            sciencecount = 2;
                        }
                        count = count + sciencecount;

                    } else if (category.equalsIgnoreCase("Humanities") || category.equalsIgnoreCase("Technical")) {
                        ps = con.prepareStatement("Select distinct examanalysistable.SubjectCode from examanalysistable,subjects where examcode='" + examcode + "'  and category='" + category + "' and admNumber='" + adm + "' and subjects.subjectcode =examanalysistable.subjectcode ");
                        ResultSet rr = ps.executeQuery();
                        while (rr.next()) {

                            humancount = humancount + 1;

                        }


                    }


                }

                if (humancount > 2) {
                    humancount = 2;
                }
                count = count + humancount;
                entries = count;


                if (entries < 7) {
                    return false;
                } else {
                    return true;
                }
            } else {
                System.err.println("Entries" + entries);
                System.err.println("Allocation" + subtaking);
                if (subtaking == entries || entries > subtaking) {
                    return true;
                } else {
                    return false;
                }

            }


        } catch (SQLException e) {
            e.printStackTrace();
            return false;
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


    public static int subjectAllocationCounter(String adm, String academicYear) {
        Connection con;
        PreparedStatement ps = null;
        ResultSet rs;
        con = DbConnection.connectDb();

        try {

            String sql = "Select count(*) from studentsubjectallocation where admnumber='" + adm + "'  and academicyear='" + academicYear + "'";

            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            if (rs.next()) {

                return rs.getInt("Count(*)");
            } else {
                return 11;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return 11;
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

    public static int nextAdmissionNumber() {
        Connection con;
        PreparedStatement ps = null;
        ResultSet rs;
        con = DbConnection.connectDb();
        int max = 0, next = 0;
        try {

            String sql = "Select max(AdmissionNumber) from admission ";

            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            if (rs.next()) {

                max = rs.getInt("max(admissionNumber)");
            }

            next = max + 1;
            return next;
        } catch (SQLException e) {
            e.printStackTrace();
            return 1;
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

    public static int nextReceiptNumber() {
        Connection con;
        PreparedStatement ps = null;
        ResultSet rs;
        con = DbConnection.connectDb();
        int max = 0, next = 0;
        try {

            String sql = "Select max(recieptNumber) from reciepts ";

            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            if (rs.next()) {

                max = rs.getInt("max(recieptNumber)");
            }

            next = max + 1;
            return next;
        } catch (SQLException e) {
            e.printStackTrace();
            return 1;
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

    public static boolean takingSubject(String adm, String academicYear, String subcode) {
        Connection con;
        PreparedStatement ps = null;
        ResultSet rs;
        con = DbConnection.connectDb();

        try {

            String sql = "Select * from studentsubjectallocation where admnumber='" + adm + "' and subjectcode='" + subcode + "' and academicyear='" + academicYear + "'";

            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            if (rs.next()) {

                return true;
            } else {
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
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

    public static void dataBaseSizeQuerryRunner() {
        Connection con;
        PreparedStatement ps = null;
        ResultSet rs;
        con = DbConnection.connectDb();
        try {
            String sql = "SELECT table_schema '" + "Db_Name" + "', ROUND(SUM(data_length + index_length)/1024/1024,1) '" + "DB Size in MB" + "' FROM   information_schema.tables where table_schema='" + "Gitooini_Database" + "'";

            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            if (rs.next()) {
                JOptionPane.showMessageDialog(CurrentFrame.mainFrame(), "Database Name :  " + rs.getString("Db_Name") + "\n " + "Current Database Size :  " + rs.getString("DB Size in MB") + " MB", "LUNAR TECHNOLOGIES Running Database Size", JOptionPane.INFORMATION_MESSAGE);
                rs.getString("DB Size in MB");
            } else {
                JOptionPane.showMessageDialog(null, "Querry Never Returned Anaything");
            }
        } catch (SQLException e) {
            e.printStackTrace();

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

    public static String subjectName(String subjectcode) {
        Connection con;
        PreparedStatement ps = null;
        ResultSet rs;
        con = DbConnection.connectDb();
        try {
            String sql = "Select subjectname from subjects where subjectcode='" + subjectcode + "'";

            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getString("subjectNAme");
            } else {
                return "";
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return "";
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


    public static String currentTerm() {
        Connection con;
        PreparedStatement ps = null;
        ResultSet rs;
        con = DbConnection.connectDb();
        try {

            String sql = "Select termcode from terms where status='" + "Current" + "'";
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getString("termcode");
            } else {
                return "";
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return "";
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


    public static String grade(String mean) {
        Connection con;
        PreparedStatement ps = null;
        ResultSet rs;
        con = DbConnection.connectDb();
        try {

            String sqll = " Select grade from points_for_each_grade where points='" + mean + "'";
            ps = con.prepareStatement(sqll);
            rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getString("grade");
            } else {
                return "";
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return "";
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


    public static int point(String grade) {
        Connection con;
        PreparedStatement ps = null;
        ResultSet rs;
        con = DbConnection.connectDb();
        try {

            String sqll = " Select points from points_for_each_grade where grade='" + grade + "'";
            ps = con.prepareStatement(sqll);
            rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt("points");
            } else {
                return 0;
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
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


    public static String termname(String termcode) {
        Connection con;
        PreparedStatement ps = null;
        ResultSet rs;
        con = DbConnection.connectDb();
        try {
            String sql = "Select termname from terms where termcode='" + termcode + "'";

            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getString("termname");
            } else {
                return "";
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return "";
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

    public static String termcode(String termname) {
        Connection con;
        PreparedStatement ps = null;
        ResultSet rs;
        con = DbConnection.connectDb();
        try {
            String sql = "Select termcode from terms where termname='" + termname + "'";

            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getString("termcode");
            } else {
                return "";
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return "";
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

    public static String currentTermName() {
        Connection con;
        PreparedStatement ps = null;
        ResultSet rs;
        con = DbConnection.connectDb();
        try {
            String sql = "Select termname from terms where status='" + "Current" + "'";
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getString("termname");
            } else {
                return "";
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return "";
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

    public static String TermOpeningDate(String year, String termcode) {
        Connection con;
        PreparedStatement ps = null;
        ResultSet rs;
        con = DbConnection.connectDb();
        try {
            int yr = Integer.parseInt(year);
            int term = 3;
            String sq = "Select Precisions from terms where termcode='" + termcode + "'";
            ps = con.prepareStatement(sq);
            rs = ps.executeQuery();
            if (rs.next()) {
                term = rs.getInt("Precisions");

            }
            if (term == 3) {
                term = 1;
                yr = (yr + 1);
            } else {
                term = (term + 1);
            }
            String sql2 = "Select termcode from terms where precisions='" + term + "'";
            ps = con.prepareStatement(sql2);
            rs = ps.executeQuery();
            if (rs.next()) {
                termcode = rs.getString("Termcode");
            }
            String sql = "Select openingDate from termdates where academicYear='" + yr + "' and termcode='" + termcode + "'";
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getString("openingDate");
            } else {
                return "";
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return "";
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

    public static String classCode(String className) {
        Connection con;
        PreparedStatement ps = null;
        ResultSet rs;
        con = DbConnection.connectDb();
        try {
            String sql = "Select classcode from classes where classname='" + className + "'";
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getString("classcode");
            } else {
                return "";
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return "";
        } finally {
            try {
                con.close();
                ps.close();
            } catch (SQLException sq) {
                JOptionPane.showMessageDialog(null, sq.getMessage());
                sq.printStackTrace();
            }

        }

    }

    public static String pyCode(String payname) {
        Connection con;
        PreparedStatement ps = null;
        ResultSet rs;
        con = DbConnection.connectDb();
        try {
            String sql = "Select pycode from expectedpayments where name ='" + payname + "' and termcode='" + Globals.currentTerm() + "' and date='" + Globals.academicYear() + "'";

            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getString("pycode");
            } else {

                return "";
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return "";
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

    public static String paymentName(String paycode, int academic) {
        Connection con;
        PreparedStatement ps = null;
        ResultSet rs;
        con = DbConnection.connectDb();

        try {

            String sql = "Select name from expectedpayments where pycode='" + paycode + "' and termcode='" + Globals.currentTerm() + "' and date='" + academic + "'";

            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getString("name");
            } else {
                return "";
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return "";
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

    public static String currentdate() {
        SimpleDateFormat dateformat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date();
        String d = Globals.currentTermName() + "   Academic Year : " + Globals.academicYear() + " Current Date: " + date.toString() + " Powered By Lunar Tech Solutions";
        return d;

    }

    public static String paymentNameonly(String paycode) {
        Connection con;
        PreparedStatement ps = null;
        ResultSet rs;
        con = DbConnection.connectDb();
        try {

            String sql = "Select name from expectedpayments where pycode='" + paycode + "'";
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getString("name");
            } else {
                return "";
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return "";
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

    public static int examWeightChecker(String examcode) {
        Connection con;
        PreparedStatement ps = null;
        ResultSet rs;
        con = DbConnection.connectDb();
        try {

            String sql = "Select weight  from examweights where examcode='" + examcode + "'";
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt("weight");
            } else {
                return 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
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

    public static int academicYear() {
        Connection con;
        PreparedStatement ps = null;
        ResultSet rs;
        con = DbConnection.connectDb();
        try {
            String sql = "Select academicyear from terms where status='" + "Current" + "'";

            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt("academicyear");
            } else {
                return -1;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
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

    public static String streamName(String streamcode) {
        Connection con;
        PreparedStatement ps = null;
        ResultSet rs;
        con = DbConnection.connectDb();
        try {
            String sql = "Select StreamName from Streams where streamcode='" + streamcode + "'";
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getString("streamname");
            } else {
                return "";
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return "";
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

    public static String streamcode(String streamname) {
        Connection con;
        PreparedStatement ps = null;
        ResultSet rs;
        con = DbConnection.connectDb();
        try {
            String sql = "Select streamcode from Streams where streamname='" + streamname + "'";
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getString("streamcode");
            } else {
                return "";
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return "";
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

    public static String fullName(String adm) {
        Connection con;
        PreparedStatement ps = null;
        ResultSet rs;
        con = DbConnection.connectDb();
        try {
            String sql = "Select firstname,middlename,lastname from admission where admissionnumber='" + adm + "'";
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getString("firstName") + "   " + rs.getString("Middlename") + "   " + rs.getString("LastName");
            } else {
                return "";
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return "";
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

    public static String studentProgram(String adm) {
        Connection con;
        PreparedStatement ps = null;
        ResultSet rs;
        con = DbConnection.connectDb();
        try {
            String sql = "Select program from admission where admissionnumber='" + adm + "'";
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getString("program");
            } else {
                return "";
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return "";
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

    public static String studentInfor() {
        Connection con;
        PreparedStatement ps = null;
        ResultSet rs;
        con = DbConnection.connectDb();
        try {
            String sql = "Select firstname,middlename,lastname,admissionNumber from admission ";
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getString("firstName") + "    " + rs.getString("Middlename") + "    " + rs.getString("LastName");
            } else {
                return "";
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return "";
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

    public static boolean admissionVerifier(String adm) {
        Connection con;
        PreparedStatement ps = null;
        ResultSet rs;
        con = DbConnection.connectDb();
        try {
            String sql = "Select firstname,middlename,lastname from admission where admissionnumber='" + adm + "'";
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            if (rs.next()) {
                return true;
            } else {
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
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

    public static String studentImage(String adm) {
        Connection con;
        PreparedStatement ps = null;
        ResultSet rs;
        con = DbConnection.connectDb();

        String image = "";

        image = ConfigurationIntialiser.imageFolder() + "/" + adm + ".jpg";

        return image;


    }


    public static String kcpeMarks(String adm) {
        Connection con;
        PreparedStatement ps = null;
        ResultSet rs;
        con = DbConnection.connectDb();
        try {
            String sql = "Select kcpemarks from admission where admissionnumber='" + adm + "'";
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getString("kcpemarks");
            } else {
                return "";
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return "";
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


    public static String className(String classcode) {
        Connection con;
        PreparedStatement ps = null;
        ResultSet rs;
        con = DbConnection.connectDb();

        try {

            String sql = "Select classname from classes where classcode='" + classcode + "'";

            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            if (rs.next()) {

                return rs.getString("classname");
            } else {
                return "";
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return "";
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

    public static String nextReceiptNumber(int increament) {
        Connection con;
        PreparedStatement ps = null;
        ResultSet rs;
        con = DbConnection.connectDb();
        int max = 0, next = 0;
        try {

            String sql = "Select count(*) from saletransactionrecord";

            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            if (rs.next()) {

                max = rs.getInt("count(*)");
            }
            increament = increament + 1;
            next = max + increament;
            sql = "Select * from saletransactionrecord where transactionNumber='" + "HBT" + next + "'";
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            if (rs.next()) {
                return nextReceiptNumber(increament);
            } else {
                if (String.valueOf(next).length() == 1) {
                    return String.valueOf(next);
                } else if (String.valueOf(next).length() == 2) {
                    return String.valueOf(next);
                } else if (String.valueOf(next).length() == 3) {
                    return String.valueOf(next);
                } else {
                    return String.valueOf(next);
                }

            }


        } catch (SQLException e) {
            e.printStackTrace();
            return "KM";
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

    public static double productBuyingPrice(String productid) {
        PreparedStatement ps;
        ResultSet rs;
        Connection con;
        con = DbConnection.connectDb();
        try {
            String sql = "Select buyingprice from products where productid='" + productid + "' ";
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getDouble("buyingprice");
            } else {
                return 0;
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public static String fullStaffName(String employeecode) {
        Connection con;
        PreparedStatement ps = null;
        ResultSet rs;
        con = DbConnection.connectDb();
        try {
            String sql = "Select firstname,middlename,lastname from staffs where employeecode='" + employeecode + "'";
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getString("firstName") + "    " + rs.getString("Middlename") + "    " + rs.getString("LastName");
            } else {
                return "";
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return "";
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

    public static String employeeCode(String staffname) {
        String Staffcode = "";
        PreparedStatement ps;
        ResultSet rs;
        Connection con;
        con = DbConnection.connectDb();
        try {
            String sql = "Select firstname,middlename,lastname,employeecode from staffs";
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                if ((rs.getString("firstName") + "    " + rs.getString("Middlename") + "    " + rs.getString("LastName")).equalsIgnoreCase(staffname)) {
                    Staffcode = rs.getString("employeecode");
                    break;
                }
            }
            return Staffcode;
        } catch (SQLException e) {
            e.printStackTrace();
            return "";
        }
    }


    public static void Backupdbtosql() {
        try {

            /*NOTE: Getting path to the Jar file being executed*/
            /*NOTE: YourImplementingClass-> replace with the class executing the code*/
            CodeSource codeSource = Globals.class.getProtectionDomain().getCodeSource();
            File jarFile = new File(codeSource.getLocation().toURI().getPath());
            String jarDir = jarFile.getParentFile().getPath();


            /*NOTE: Creating Database Constraints*/
            String dbName = "kamarandi_Database";
            String dbUser = "root";
            String dbPass = "root";

            /*NOTE: Creating Path Constraints for folder saving*/
            /*NOTE: Here the backup folder is created for saving inside it*/
            String folderPath = jarDir + "\\backup";

            /*NOTE: Creating Folder if it does not exist*/
            File f1 = new File(folderPath);
            f1.mkdir();

            /*NOTE: Creating Path Constraints for backup saving*/
            /*NOTE: Here the backup is saved in a folder called backup with the name backup.sql*/
            String savePath = "\"" + jarDir + "\\backup\\" + "backup.sql\"";

            /*NOTE: Used to create a cmd command*/
            String executeCmd = "mysqldump -u" + dbUser + " -p" + dbPass + " --database " + dbName + " -r " + savePath;

            /*NOTE: Executing the command here*/
            Process runtimeProcess = Runtime.getRuntime().exec(executeCmd);
            int processComplete = runtimeProcess.waitFor();

            /*NOTE: processComplete=0 if correctly executed, will contain other values if not*/
            if (processComplete == 0) {
                System.out.println("Backup Complete");
            } else {
                System.out.println("Backup Failure");
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error at Backuprestore" + ex.getMessage());
        }
    }


    public static void Restoredbfromsql(String s) {
        try {
            /*NOTE: String s is the mysql file name including the .sql in its name*/
            /*NOTE: Getting path to the Jar file being executed*/
            /*NOTE: YourImplementingClass-> replace with the class executing the code*/
            CodeSource codeSource = Globals.class.getProtectionDomain().getCodeSource();
            File jarFile = new File(codeSource.getLocation().toURI().getPath());
            String jarDir = jarFile.getParentFile().getPath();

            /*NOTE: Creating Database Constraints*/
            String dbName = "YourDBName";
            String dbUser = "YourUserName";
            String dbPass = "YourUserPassword";

            /*NOTE: Creating Path Constraints for restoring*/
            String restorePath = jarDir + "\\backup" + "\\" + s;

            /*NOTE: Used to create a cmd command*/
            /*NOTE: Do not create a single large string, this will cause buffer locking, use string array*/
            String[] executeCmd = new String[]{"mysql", dbName, "-u" + dbUser, "-p" + dbPass, "-e", " source " + restorePath};

            /*NOTE: processComplete=0 if correctly executed, will contain other values if not*/
            Process runtimeProcess = Runtime.getRuntime().exec(executeCmd);
            int processComplete = runtimeProcess.waitFor();

            /*NOTE: processComplete=0 if correctly executed, will contain other values if not*/
            if (processComplete == 0) {
                JOptionPane.showMessageDialog(null, "Successfully restored from SQL : " + s);
            } else {
                JOptionPane.showMessageDialog(null, "Error at restoring");
            }


        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Error at Restoredbfromsql" + ex.getMessage());
        }

    }


    public static String goodscategoryid(String categoryName) {
        PreparedStatement ps;
        ResultSet rs;
        Connection con;
        con = DbConnection.connectDb();
        try {
            String sql = "Select CategoryID from productcategories where ProductCategoryName='" + categoryName + "'";
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getString("CategoryID");
            } else {
                return "";
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return "";
        }
    }

    public static String goodscategoryName(String categoryid) {
        PreparedStatement ps;
        ResultSet rs;
        Connection con;
        con = DbConnection.connectDb();
        try {
            String sql = "Select ProductCategoryName from productcategories where Categoryid='" + categoryid + "'";
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getString("ProductCategoryName");
            } else {
                return "";
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return "";
        }
    }

    public static String supplierName(String supplierid) {
        PreparedStatement ps;
        ResultSet rs;
        Connection con;
        con = DbConnection.connectDb();
        try {
            String sql = "Select supplierName from suppliers where supplierName='" + supplierid + "'";
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getString("suppliername");
            } else {
                return "";
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return "";
        }
    }

    public static String supplieid(String supplierName) {
        PreparedStatement ps;
        ResultSet rs;
        Connection con;
        con = DbConnection.connectDb();
        try {
            String sql = "Select supplierid from suppliers where supplierName='" + supplierName + "'";
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getString("supplierid");
            } else {
                return "";
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return "";
        }
    }

}
