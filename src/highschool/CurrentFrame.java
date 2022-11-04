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


import javax.swing.JDialog;
import javax.swing.JFrame;

/**
 * @author FRED
 */
public class CurrentFrame {
    static JFrame current = new JFrame();
    static JFrame secondFrame = new JFrame();
    static JDialog ReportDialogue = new JDialog();

    public static void jframeon(JFrame frame) {
        current = frame;
        frame.setEnabled(false);
    }

    public static void jframeonIntialiser(JFrame frame) {
        current = frame;
        frame.setEnabled(true);
    }

    public static JFrame mainFrame() {
        return current;
    }

    public static JFrame secFrame() {
        return secondFrame;
    }

    public static JFrame currentWindow() {
        current.setEnabled(true);
        return current;
    }

    public static void setSecondFrame(JFrame frame) {
        secondFrame = frame;

    }

    public static void killSecondFrame() {
        secondFrame.dispose();
    }

    public static JFrame secondFrame() {
        return secondFrame;
    }

    public static void docOpenerIntialiser(JDialog dialog) {
        ReportDialogue = dialog;
    }

    public static JDialog docOpener() {
        return ReportDialogue;
    }
}
