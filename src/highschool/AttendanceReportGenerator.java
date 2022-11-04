/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package highschool;


import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.File;
import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.NumberFormat;
import java.util.Date;
import javax.swing.JDialog;
import javax.swing.JOptionPane;

/**
 * @author EXAMSERVERPC
 */
public class AttendanceReportGenerator {

    static PreparedStatement ps;
    static ResultSet rs;
    static Connection con;
    static JDialog dialog = null;
    static NumberFormat nf = NumberFormat.getNumberInstance();
    static DbConnection db = new DbConnection();

    public static void onEndPage(PdfWriter writer, Document document) {
        Font ffont = new Font(Font.FontFamily.UNDEFINED, 5, Font.ITALIC);
        PdfContentByte cb = writer.getDirectContent();

        Phrase footer = new Phrase("Lunar Technologies", ffont);

        ColumnText.showTextAligned(cb, Element.ALIGN_RIGHT,
                footer,
                (document.right() - document.left()) / 2 + document.leftMargin(),
                document.bottom() - 10, 0);
    }


    public static void attendanceReport(String classcode, String streamcode, String termcode, String upperdate, String lowerdate) {
        try {
            Document doc = new Document();
            com.itextpdf.text.Image image = com.itextpdf.text.Image.getInstance("C:/schooldata/logo.jpg");
            PdfWriter writer = PdfWriter.getInstance(doc, new FileOutputStream("C:\\schoolData\\" + "Class Register" + ".pdf"));
            doc.open();
            onEndPage(writer, doc);
            doc.add(new Paragraph(new Date().toString()));
            PdfPTable tab = new PdfPTable(1);

            {//school details header
                DocHead head = new DocHead();
                Image img = head.im();

                PdfPCell cell1 = new PdfPCell(img, true);
                cell1.setBorder(PdfPCell.NO_BORDER);

                tab.addCell(cell1);
            }

            tab.setWidthPercentage(100);
            doc.add(tab);


            doc.close();

            if (ConfigurationIntialiser.docOpener()) {
                if (dialog == null) {

                    dialog = ReportFrame.getReportFrameInstance(new File("C:\\schoolData\\" + "Class Register" + ".pdf"));

                } else {
                    dialog = null;
                    dialog = ReportFrame.getReportFrameInstance(new File("C:\\schoolData\\" + "Class Register" + ".pdf"));
                    dialog.setVisible(true);

                }

            } else {

                Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler " + "C:\\schoolData\\" + "Class Register" + ".pdf");

            }


        } catch (Exception sq) {
            sq.printStackTrace();
            JOptionPane.showMessageDialog(null, sq.getMessage());
        }


    }

}
