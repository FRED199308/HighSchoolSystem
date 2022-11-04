/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package highschool;

import java.awt.Image;
import javax.swing.ImageIcon;

/**
 * @author FRED
 */
public class FrameProperties {
    static ImageIcon imageIcon;

    public static Image icon() {
        DocHead head = new DocHead();
        imageIcon = head.framesIcon();


        Image imaage = imageIcon.getImage();
        return imaage;
    }


}