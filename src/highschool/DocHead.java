/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package highschool;

import com.itextpdf.text.Image;

import javax.swing.ImageIcon;

/**
 * @author FRED
 */
public class DocHead {

    public Image receiptHeader() {
        Image img = null;
        try {
            img = Image.getInstance("C:\\High Sch. Pro/ReceiptHeader.png");
            return img;
        } catch (Exception e) {
        }
        return img;
    }

    public ImageIcon trayIcon() {
        ImageIcon img = null;
        try {
            img = new ImageIcon(getClass().getResource("/icons/icon.png"));
            return img;
        } catch (Exception e) {
        }
        return img;
    }

    public Image im() {
        Image img = null;
        try {
            img = Image.getInstance("C:\\High Sch. Pro/OVERDOCHEADER.png");
            return img;
        } catch (Exception e) {
        }
        return img;
    }

    public Image reportFormHeader() {
        Image img = null;
        try {

            img = Image.getInstance("C:\\High Sch. Pro/ReportLogo.png");
            return img;
        } catch (Exception e) {
        }
        return img;
    }

    public ImageIcon framesIcon() {
        ImageIcon img = null;
        try {
            img = new ImageIcon(getClass().getResource("/icons/index6.jpg"));
            return img;
        } catch (Exception e) {
        }
        return img;
    }

    public Image principalSign() {
        Image img = null;
        try {
            img = Image.getInstance("C:\\High Sch. Pro/sign.png");
            return img;
        } catch (Exception e) {
        }
        return img;
    }

}
