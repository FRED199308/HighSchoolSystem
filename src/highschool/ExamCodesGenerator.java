/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package highschool;

import javax.swing.JOptionPane;

/**
 * @author FRED
 */
public class ExamCodesGenerator {
    public static String generatecode(String classl, String year, String term, String exam) {
        if (classl == null) {
            JOptionPane.showMessageDialog(null, "Select Class First");
            return "";
        } else {
            if (term == null) {
                JOptionPane.showMessageDialog(null, "Select Term First");
                return "";
            } else {
                if (year == null) {
                    JOptionPane.showMessageDialog(null, "Select Academic Year First");
                    return "";
                } else {
                    String classpart = "FM" + classl.charAt(classl.length() - 1);
                    String termpart = "TM" + term.charAt(term.length() - 1);

                    return classpart + exam + termpart + year;
                }
            }
        }


    }

}
