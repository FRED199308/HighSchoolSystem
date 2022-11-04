/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package highschool;

import java.awt.Color;
import java.awt.Cursor;
import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JComponent;

import org.jdesktop.swingx.autocomplete.AutoCompleteDecorator;


/**
 * @author FRED
 */
public class FredCombo extends JComboBox {

    public FredCombo(String t) {
        if (t.equalsIgnoreCase("Select Exam Code") || t.equalsIgnoreCase("Exam Code")) {
            this.setEnabled(false);
            ((JComponent) this).setBorder(
                    BorderFactory.createMatteBorder(2, 2, 2, 2, Color.BLUE));
            this.setToolTipText("Exam Code Is Selected Automatically Depending On Upper Fields Selection");

        } else {
            this.setToolTipText(t);
        }
        if (t.equalsIgnoreCase("Select Exam") || t.equalsIgnoreCase("Exam Name")) {
            this.setToolTipText("Select This as The Last Item.Reselect After Changing Upper Fields");
        }

        Cursor cur = new Cursor(Cursor.HAND_CURSOR);
        this.setCursor(cur);
        addItem(t);

        // setAutoscrolls(true);

        AutoCompleteDecorator.decorate(this);


    }

    public FredCombo() {
        Cursor cur = new Cursor(Cursor.HAND_CURSOR);
        this.setCursor(cur);


        // setAutoscrolls(true);

        AutoCompleteDecorator.decorate(this);
    }

    public FredCombo(String ar[]) {
        for (int i = 0; i < ar.length; ++i) {
            this.addItem(ar[i]);
        }
        AutoCompleteDecorator.decorate(this);
    }

}
