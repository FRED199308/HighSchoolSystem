/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package highschool;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.JOptionPane;

/**
 * @author FRED_ADMIN
 */
public class DataValidation {

    public static boolean name(String name) {
        String m = "^[a-zA-Z]{2,100}$";
        Pattern pattern = Pattern.compile(m);
        Matcher remach = pattern.matcher(name);
        if (remach.matches()) {

            return true;
        } else {
            return false;
        }
    }

    public static boolean phoneValidator(String phone) {
        if (phone.matches("07[0-9]{8}")) {
            return true;
        } else {
            return false;
        }
    }


    public static boolean IdNumber(String id) {
        if (id.matches("[0-9]{8}")) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean number(String numberr) {
        String n = "^[0-9]{1,7}$";
        Pattern pat = Pattern.compile(n);
        Matcher match = pat.matcher(numberr);
        if (match.matches()) {
            return true;
        } else {
            return false;
        }


    }

    public static boolean ipValidator(String ip) {
        String IP = "^[0-9a-zA-Z]{1,20}:[0-9]{1,5}";
        Pattern pat = Pattern.compile(IP);
        Matcher mach = pat.matcher(ip);
        if (mach.matches()) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean nameWithoutSpace(String name) {
        String m = "^[a-zA-Z]{2,25}$";
        Pattern pattern = Pattern.compile(m);
        Matcher remach = pattern.matcher(name);
        if (remach.matches()) {

            return true;
        } else {
            return false;
        }
    }

    public static boolean number2(String numberr) {
        String n = "^[0-9]{1,6}$";
        Pattern pat = Pattern.compile(n);
        Matcher match = pat.matcher(numberr);
        if (match.matches()) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean password(String pass) {

        String m1 = "((?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%*]).{6,20})";
        Pattern pattern = Pattern.compile(m1);
        Matcher remach = pattern.matcher(pass);
        if (remach.matches()) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean dateInteval(String numberr) {

        String n = "^[0-9]{1,4}/[0-9]{1,2}/[0-9]{1,2}-[0-9]{1,4}/[0-9]{1,2}/[0-9]{1,2}$";
        Pattern pat = Pattern.compile(n);
        Matcher match = pat.matcher(numberr);
        if (match.matches()) {
            return true;
        } else {
            return false;
        }
    }


}
