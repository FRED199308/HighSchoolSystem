/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package highschool;

import java.awt.Color;
import java.awt.Image;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JFrame;

/**
 * @author FRED
 */
public class FredFrame extends JFrame {
    public FredFrame() {

        ((JComponent) getContentPane()).setBorder(
                BorderFactory.createMatteBorder(5, 5, 5, 5, Color.magenta));
        ImageIcon imageIcon;
        imageIcon = new ImageIcon(getClass().getResource("/icons/index6.jpg"));


        Image imaage = imageIcon.getImage();
        this.setIconImage(imaage);
    }

}