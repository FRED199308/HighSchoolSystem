/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package highschool;

import com.google.gson.Gson;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JOptionPane;

import org.apache.commons.io.IOUtils;
import org.json.JSONObject;


public class MessageGateway {
    public static void main(String[] args) {
        String[] ar = {"+254707353225"};
        //  System.err.println(MessageGateway.sendSms("final test", ar));
        System.err.println("Balane" + MessageGateway.smsBalance());
    }


    public static String sendSms(String message, String recipients[]) {
        Gson g = new Gson();
        Map data = new HashMap();
        data.put("senderid", ConfigurationIntialiser.senderId());
        data.put("secretkey", ConfigurationIntialiser.smsKey());
        data.put("userid", ConfigurationIntialiser.smsUsername1());
        data.put("message", message);
        String JSON = g.toJson(message);

        String query_url = "http://api.lunar.cyou/api/sendsms.php";
        String m = "";
        for (int i = 0; i < recipients.length; ++i) {

            if (i == (recipients.length - 1)) {
                m += recipients[i].substring(1);
            } else {
                m += recipients[i].substring(1) + ",";
            }
        }
        data.put("recipient", m);



        try {
            URL url = new URL(query_url);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(5000);
            conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setRequestMethod("POST");
            OutputStream os = conn.getOutputStream();
            os.write(g.toJson(data).getBytes("UTF-8"));
            os.close();
            // read the response
            InputStream in = new BufferedInputStream(conn.getInputStream());
            String result = IOUtils.toString(in, "UTF-8");

            in.close();
            conn.disconnect();
            System.err.println(result);
//String result="[sent";
            if (result.contains("[")) {
//                 JSONArray jsonArray = new JSONArray(result);
//         // jsonArray.put(myResponse);
//               System.out.println(jsonArray);
//               for(int i =0;i<=jsonArray.length();++i)
//               {
//                   System.out.println(jsonArray.getJSONObject(i).get("status"));
//               }
                return "Messages Sent SuccessFully";
            } else {
                JSONObject myResponse = new JSONObject(result);

                if (myResponse.getString("status").equalsIgnoreCase("201")) {
                    return "Error: Invalid Credentials,Please Check The Credentials And Try Again";
                } else if (myResponse.getString("status").equalsIgnoreCase("101")) {
                    return "Error :You Have Insufficient Balance,Please Recharge and Try Again.Balance ksh:" + myResponse.getString("Balance");

                } else if (myResponse.getString("status").equalsIgnoreCase("301")) {
                    return "Error :Invalid senderID!";

                } else if (myResponse.getString("status").equalsIgnoreCase("0")) {
                    return "Messages Sent Successfully";

                } else {
                    return "Error: Unknown,Please Contact Developer";
                }
            }


        } catch (Exception e) {
            System.out.println(e);
            e.printStackTrace();
            return "Error Occured" + e.getMessage();
        }

    }


    public static String batchMessageQueuer(ArrayList<Map> messageList) {
        String responce = "";
        Connection con;
        PreparedStatement ps = null;
        ResultSet rs;
        con = DbConnection.connectDb();


        try {
            if (ConfigurationIntialiser.smsOfflineSender()) {
                for (int i = 0; i < messageList.size(); i++) {
                    if (((Map) messageList.get(i)).get("phone").toString().equals("")) {

                    } else {
                        String message = ((Map) messageList.get(i)).get("message").toString();
                        String phone = ((Map) messageList.get(i)).get("phone").toString();
                        String pf = ((Map) messageList.get(i)).get("phone").toString();

                        phone = pf;
                        PreparedStatement ps1;

                        String sqlInsert =
                                "INSERT INTO " +
                                        "ozekimessageout (receiver,msg,status) " +
                                        "VALUES " +
                                        "('" + pf + "',?,'send')";
                        ps1 = con.prepareStatement(sqlInsert);
                        ps1.setString(1, message);
                        ps1.execute();

                    }

                }
                JOptionPane.showMessageDialog(null, "Message(s) Queued Successfully\n System gathering necessary resources....,\n check sending progress from the tray icon messages or SMS logs");
                return "sent";

            } else {


                SimpleDateFormat dateformat;
                dateformat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                Date date = new Date();
                String da = dateformat.format(date);


                try {


                    responce = MessageGateway.sendSms(messageList);
                    JOptionPane.showMessageDialog(null, responce);
                    adminMessageConfirmation("Messages Sent Successfull");
                    if (responce.startsWith("Error")) {
                        for (int i = 0; i < messageList.size(); ++i) {
                            String querry4 = "insert into smsrecord  (SMScontent,Date,MemberId,SMSID,SenderID,Status,Phone) values(?,'" + da + "','" + "" + "','" + IdGenerator.keyGen() + "','" + Globals.CurrentUser + "','" + "Failed" + "','" + ((Map) messageList.get(i)).get("phone").toString() + "')";

                            PreparedStatement Fine;

                            Fine = con.prepareStatement(querry4);
                            Fine.setString(1, ((Map) messageList.get(i)).get("message").toString());
                            Fine.execute();

                        }

                        return "Error";
                    } else {
                        for (int i = 0; i < messageList.size(); ++i) {
                            String querry4 = "insert into smsrecord (SMScontent,Date,MemberId,SMSID,SenderID,Status,Phone)  values(?,'" + da + "','" + "" + "','" + IdGenerator.keyGen() + "','" + Globals.CurrentUser + "','" + "Sent" + "','" + ((Map) messageList.get(i)).get("phone").toString() + "')";

                            PreparedStatement Fine;
                            Fine = con.prepareStatement(querry4);
                            Fine.setString(1, ((Map) messageList.get(i)).get("message").toString());
                            Fine.execute();


                        }
                    }


                    return "sent";


                } catch (Exception ex) {

                    JOptionPane.showMessageDialog(null, "System Unable To Queue Message check Internet Possibly\n Kindly fix and Retry" + ex.getMessage());
                    ex.printStackTrace();
                    for (int i = 0; i < messageList.size() - 1; ++i) {
                        IdGenerator k = new IdGenerator();
                        String id = "SMS" + IdGenerator.keyGen();
                        String querry5 = "insert into smsrecord values('" + ((Map) messageList.get(i)).get("message").toString() + "',Now(),'" + "" + "','" + id + "','" + Globals.CurrentUser + "','" + "Failed" + "','" + ((Map) messageList.get(i)).get("phone").toString() + "')";
                        ps = con.prepareStatement(querry5);
                        ps.execute();

                    }
                    return "error";
                }


            }


        } catch (Exception sq) {

            sq.printStackTrace();
            return "error";
        }


    }


    public static String smsBalance() {
        String query_url = "http://api.lunar.cyou/api/smsbalance.php";
        String json = "{ \"data\":{\"senderid\":\"" + "22136" + "\",\"secretkey\":\"" + ConfigurationIntialiser.smsKey() + "\",\"userid\":\"" + ConfigurationIntialiser.smsUsername1() + "\"";

        String m = "";

        m = m + "}}";
        json = json + m;
        // System.err.println(json);
        try {
            URL url = new URL(query_url);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(5000);
            conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setRequestMethod("POST");
            OutputStream os = conn.getOutputStream();
            os.write(json.getBytes("UTF-8"));
            os.close();
            // read the response
            InputStream in = new BufferedInputStream(conn.getInputStream());
            String result = IOUtils.toString(in, "UTF-8");

            JSONObject myResponse = new JSONObject(result);


            in.close();
            conn.disconnect();
            if (myResponse.getString("ResponseCode").equalsIgnoreCase("0")) {
                return " Ksh " + myResponse.getString("actual_Balance") + ",Or Sms: " + myResponse.getString("sms_Balance") + " Item(s)";

            } else {
                return "Error";
            }
        } catch (Exception e) {
            System.out.println(e);
            return "Error Occured" + e.getMessage();
        }
    }


    public static boolean isComputerOffline() {
        try {
            Process process = java.lang.Runtime.getRuntime().exec("ping www.google.com");
            int x = process.waitFor();
            if (x == 0) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static String gatewayBalance() {
//      String USERNAME =ConfigurationIntialiser.smsUsername1();
//		String API_KEY = ConfigurationIntialiser.smsKey();
//
//		/* Initialize SDK */
//		AfricasTalking.initialize(USERNAME, API_KEY);
//
//		/* Get an instance of the ApplicationService */
//		ApplicationService application = AfricasTalking.getService(AfricasTalking.SERVICE_APPLICATION);
//
//		/* And send the request (synchronously) */


        try {

            //ApplicationResponse response = application.fetchApplicationData();
            return MessageGateway.smsBalance();

        } catch (Exception ex) {

            return "Unknown";
        }
    }


    public static void oneForeignMessageQueue(String phone, String message) {
        Connection con;
        PreparedStatement ps = null;
        ResultSet rs;
        con = DbConnection.connectDb();
        String pf = phone.replaceFirst("0", "+254");

        phone = pf;
        try {
            if (ConfigurationIntialiser.smsOfflineSender()) {

                PreparedStatement ps1;

                String sqlInsert =
                        "INSERT INTO " +
                                "ozekimessageout (receiver,msg,status) " +
                                "VALUES " +
                                "('" + pf + "',?,'send')";
                ps1 = con.prepareStatement(sqlInsert);
                ps1.setString(1, message);
                ps1.execute();
                JOptionPane.showMessageDialog(null, "Message(s) Queued Successfully\n System gathering necessary resources....,\n check sending progress from the tray icon messages or SMS logs");

            } else {

                SimpleDateFormat dateformat;
                dateformat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                Date date = new Date();
                String da = dateformat.format(date);


                String[] recipients = new String[]{
                        phone
                };

                try {


                    String responce = MessageGateway.sendSms(message, recipients);
                    JOptionPane.showMessageDialog(null, responce);
                    if (responce.startsWith("Error")) {
                        for (int i = 0; i < recipients.length; ++i) {
                            String querry4 = "insert into smsrecord  (SMScontent,Date,MemberId,SMSID,SenderID,Status,Phone) values(?,'" + da + "','" + "" + "','" + IdGenerator.keyGen() + "','" + Globals.CurrentUser + "','" + "Failed" + "','" + recipients[i] + "')";

                            PreparedStatement Fine;

                            Fine = con.prepareStatement(querry4);
                            Fine.setString(1, message);
                            Fine.execute();

                        }
                    } else {
                        for (int i = 0; i < recipients.length; ++i) {
                            String querry4 = "insert into smsrecord (SMScontent,Date,MemberId,SMSID,SenderID,Status,Phone)  values(?,'" + da + "','" + "" + "','" + IdGenerator.keyGen() + "','" + Globals.CurrentUser + "','" + "Sent" + "','" + recipients[i] + "')";

                            PreparedStatement Fine;
                            Fine = con.prepareStatement(querry4);
                            Fine.setString(1, message);
                            Fine.execute();

                        }
                    }


                } catch (Exception ex) {
                    IdGenerator k = new IdGenerator();
                    String id = "SMS" + IdGenerator.keyGen();
                    String querry5 = "insert into smsrecord values('" + message + "','" + da + "','" + "" + "','" + id + "','" + Globals.CurrentUser + "','" + "Failed" + "','" + phone + "')";
                    ps = con.prepareStatement(querry5);
                    ps.execute();
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(null, ex.getMessage());
                }
            }


        } catch (Exception sq) {
            sq.printStackTrace();
        }

    }

    public static String sendSms(ArrayList<Map> MessageLists) {
        Gson g = new Gson();
        Map data = new HashMap();
        data.put("senderid", ConfigurationIntialiser.senderId());
        data.put("secretkey", ConfigurationIntialiser.smsKey());
        data.put("userid", ConfigurationIntialiser.smsUsername1());
        data.put("messageParameters", MessageLists);


        String query_url = "http://api.lunar.cyou/api/Multsendsms.php";


        System.err.println(g.toJson(data));

        try {
            URL url = new URL(query_url);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(5000);
            conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setRequestMethod("POST");
            OutputStream os = conn.getOutputStream();
            os.write(g.toJson(data).getBytes("UTF-8"));
            os.close();
            // read the response
            InputStream in = new BufferedInputStream(conn.getInputStream());
            String result = IOUtils.toString(in, "UTF-8");

            in.close();
            conn.disconnect();
            System.err.println(result);

            if (result.contains("[")) {
//                 JSONArray jsonArray = new JSONArray(result);
//         // jsonArray.put(myResponse);
//               System.out.println(jsonArray);
//               for(int i =0;i<=jsonArray.length();++i)
//               {
//                   System.out.println(jsonArray.getJSONObject(i).get("status"));
//               }
                return MessageLists.size() + " Message(s) Sent SuccessFully";
            } else {
                JSONObject myResponse = new JSONObject(result);

                if (myResponse.getString("status").equalsIgnoreCase("201")) {
                    return "Error: Invalid Credentials,Please Check The Credentials And Try Again";
                } else if (myResponse.getString("status").equalsIgnoreCase("101")) {
                    return "Error :You Have Insufficient Balance,Please Recharge and Try Again.Balance ksh:" + myResponse.getString("Balance");

                } else if (myResponse.getString("status").equalsIgnoreCase("301")) {
                    return "Error :Invalid senderID!";

                } else if (myResponse.getString("status").equalsIgnoreCase("0")) {
                    return "Messages Sent Successfully";

                } else {
                    return "Error: Unknown,Please Contact Developer";
                }
            }


        } catch (Exception e) {

            e.printStackTrace();
            return "Error Occured" + e.toString();
        }

    }

    public static String silent1OneForeignMessageQueue(String phone, String message) {
        Connection con;
        PreparedStatement ps = null;
        ResultSet rs;
        con = DbConnection.connectDb();
        String pf = phone.replaceFirst("0", "+254");
        pf = "254" + phone.substring(1);

        phone = pf;
        try {
            if (ConfigurationIntialiser.smsOfflineSender()) {

                PreparedStatement ps1;

                String sqlInsert =
                        "INSERT INTO " +
                                "ozekimessageout (receiver,msg,status) " +
                                "VALUES " +
                                "('" + pf + "',?,'send')";
                ps1 = con.prepareStatement(sqlInsert);
                ps1.setString(1, message);
                ps1.execute();
                // JOptionPane.showMessageDialog(null, "Message(s) Queued Successfully\n System gathering necessary resources....,\n check sending progress from the tray icon messages or SMS logs");
                return "sent";
            } else {

                SimpleDateFormat dateformat;
                dateformat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                Date date = new Date();
                String da = dateformat.format(date);


                String[] recipients = new String[]{
                        phone
                };

                try {


                    String responce = MessageGateway.sendSms(message, recipients);

                    if (responce.startsWith("Error")) {
                        for (int i = 0; i < recipients.length; ++i) {
                            String querry4 = "insert into smsrecord  (SMScontent,Date,MemberId,SMSID,SenderID,Status,Phone) values(?,'" + da + "','" + "" + "','" + IdGenerator.keyGen() + "','" + Globals.CurrentUser + "','" + "Failed" + "','" + recipients[i] + "')";

                            PreparedStatement Fine;

                            Fine = con.prepareStatement(querry4);
                            Fine.setString(1, message);
                            Fine.execute();

                        }
                        return "Not Sent";
                    } else {
                        for (int i = 0; i < recipients.length; ++i) {
                            String querry4 = "insert into smsrecord (SMScontent,Date,MemberId,SMSID,SenderID,Status,Phone)  values(?,'" + da + "','" + "" + "','" + IdGenerator.keyGen() + "','" + Globals.CurrentUser + "','" + "Sent" + "','" + recipients[i] + "')";

                            PreparedStatement Fine;
                            Fine = con.prepareStatement(querry4);
                            Fine.setString(1, message);
                            Fine.execute();

                        }
                        return "Sent";
                    }


                } catch (Exception ex) {
                    IdGenerator k = new IdGenerator();
                    String id = "SMS" + IdGenerator.keyGen();
                    String querry5 = "insert into smsrecord values('" + message + "','" + da + "','" + "" + "','" + id + "','" + Globals.CurrentUser + "','" + "Failed" + "','" + phone + "')";
                    ps = con.prepareStatement(querry5);
                    ps.execute();
                    ex.printStackTrace();

                    return " errror";
                }
            }


        } catch (Exception sq) {
            sq.printStackTrace();
            return sq.toString();
        } finally {
            try {
                con.close();

            } catch (SQLException sq) {
                JOptionPane.showMessageDialog(null, sq);
                sq.printStackTrace();
            }

        }
    }

    public static void adminMessageConfirmation(String message) {

        Connection con;
        PreparedStatement ps = null;
        ResultSet rs;
        con = DbConnection.connectDb();


        String pf = "", pi = "";
        try {

            String sq = "Select PhoneNumber from staffs,useraccounts where level='" + "Admin" + "' and staffs.employeecode=useraccounts.EmployeeCode";
            ps = con.prepareStatement(sq);
            rs = ps.executeQuery();
            while (rs.next()) {

                pi = rs.getString("PhoneNumber").replaceFirst("0", "+254");
                pf = pf.concat(pi + ":");


            }

            String[] phones = pf.split(":");


            if (ConfigurationIntialiser.smsOfflineSender()) {
                for (int i = 0; i < phones.length; i++) {
                    if (phones[i].equals("")) {

                    } else {
                        String phone = "";
                        pf = phones[i];

                        phone = pf;
                        PreparedStatement ps1;

                        String sqlInsert =
                                "INSERT INTO " +
                                        "ozekimessageout (receiver,msg,status) " +
                                        "VALUES " +
                                        "('" + pf + "',?,'send')";
                        ps1 = con.prepareStatement(sqlInsert);
                        ps1.setString(1, message);
                        ps1.execute();

                    }

                }

                { //Developer notification...............


                    PreparedStatement ps1;

                    String sqlInsert =
                            "INSERT INTO " +
                                    "ozekimessageout (receiver,msg,status) " +
                                    "VALUES " +
                                    "('" + "+254707353225" + "','" + "Developer Alert System In Use At " + ConfigurationIntialiser.schoolInfor() + "','send')";
                    ps1 = con.prepareStatement(sqlInsert);
                    ps1.execute();
                }
            } else {


                SimpleDateFormat dateformat;
                dateformat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                Date date = new Date();
                String da = dateformat.format(date);

                String[] recipients = phones;
                String responce = MessageGateway.sendSms(message, recipients);
                String developercontact[] = {"+254707353225"};
                MessageGateway.sendSms("Developer Alert System In Use At " + ConfigurationIntialiser.schoolInfor(), developercontact);

                try {


                    try {


                    } catch (Exception e) {
                    }


                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, "System Unable To Queue Message check Internet Possibly\n Kindly fix and Retry" + ex.getMessage());
                    ex.printStackTrace();
                    for (int i = 0; i < phones.length - 1; ++i) {
                        IdGenerator k = new IdGenerator();
                        String id = "SMS" + IdGenerator.keyGen();
                        String querry5 = "insert into smsrecord values('" + message + "',Now(),'" + "" + "','" + id + "','" + Globals.CurrentUser + "','" + "Failed" + "','" + phones[i] + "')";
                        ps = con.prepareStatement(querry5);
                        ps.execute();

                    }
                }


            }


        } catch (Exception sq) {
            sq.printStackTrace();
        }


    }

}
