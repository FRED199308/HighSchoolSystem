/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package highschool;

import java.awt.Color;
import java.awt.Font;
import javax.swing.ImageIcon;
import javax.swing.JLabel;


/**
 * @author ExamSeverPc
 */
public class FredLabel extends JLabel {
    public FredLabel(String text) {
        super();
        this.setText(text);
        this.setForeground(Color.RED);
        this.setFont(new Font("serif", Font.BOLD, 16));
        if (text.contains("Search") || text.contains("search")) {


            this.setIcon(new ImageIcon(getClass().getResource("/icons/search-icon.png")));


        }

    }

    public FredLabel() {

        this.setForeground(Color.red);
        this.setFont(new Font("serif", Font.BOLD, 16));
    }

    public FredLabel(boolean color) {
        this.setForeground(Color.red);
        this.setFont(new Font("serif", Font.BOLD, 16));
    }

}
