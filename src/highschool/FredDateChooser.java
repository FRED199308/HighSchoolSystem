/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package highschool;


import com.toedter.calendar.JDateChooser;
import com.toedter.calendar.JTextFieldDateEditor;

/**
 * @author FREDDY
 */
public class FredDateChooser extends JDateChooser {

    public FredDateChooser() {
        JTextFieldDateEditor ed = (JTextFieldDateEditor) this.getComponents()[1];
        ed.setEditable(false);
        this.setToolTipText("Kindly Pick Date Typing Deactivated");

    }

    public FredDateChooser(String format) {
        this.setDateFormatString(format);
        JTextFieldDateEditor ed = (JTextFieldDateEditor) this.getComponents()[1];
        ed.setEditable(false);
        this.setToolTipText("Kindly Pick Date Typing Deactivated");
    }


}
