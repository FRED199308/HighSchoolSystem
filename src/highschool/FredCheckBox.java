/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package highschool;

import java.awt.Cursor;
import javax.swing.JCheckBox;

/**
 * @author FRED
 */
public class FredCheckBox extends JCheckBox {
    public FredCheckBox(String g) {
        Cursor cur = new Cursor(Cursor.HAND_CURSOR);
        this.setCursor(cur);
        this.setText(g);
    }

}
