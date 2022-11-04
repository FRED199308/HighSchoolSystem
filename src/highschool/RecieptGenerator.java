/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package highschool;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.Element;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfContentByte;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JDialog;


/**
 * @author FRED
 */
public class RecieptGenerator {

    static PreparedStatement ps;
    static ResultSet rs;
    static Connection con;
    static JDialog dialog = null;
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


    public static void generateReciept(String adm, String reciptno, String date, String amount, String paymode, String DepositeDate) {
        Document doc = new Document();
        try {


            con = DbConnection.connectDb();

            PdfWriter writer = PdfWriter.getInstance(doc, new FileOutputStream("C:\\schooldata\\" + adm + "Reciept" + ".pdf"));
            doc.open();
            //    doc.add(new Paragraph("Printed On: "+date));
            onEndPage(writer, doc);
            Paragraph pr5 = new Paragraph("Ministry Of Education Sicence & Technology", FontFactory.getFont(FontFactory.HELVETICA, 10, java.awt.Font.BOLD, BaseColor.BLACK));

            pr5.setIndentationLeft(80);
            pr5.setSpacingAfter(5);
            doc.add(pr5);
            PdfPTable tab = new PdfPTable(1);

            {//school details header
                DocHead head = new DocHead();
                Image img = head.receiptHeader();

                PdfPCell cell1 = new PdfPCell(img, true);
                cell1.setBorder(PdfPCell.NO_BORDER);

                tab.addCell(cell1);
            }

            tab.setWidthPercentage(100);
            doc.add(tab);

            PdfPTable tabb = new PdfPTable(20);

            {
                Phrase ph = new Phrase("Reciept No".toUpperCase() + ":   " + reciptno + "                                             " + "Ref No.".toUpperCase(), new Font(Font.FontFamily.HELVETICA, 14, Font.NORMAL, BaseColor.BLUE));
                PdfPCell cell = new PdfPCell(ph);
                cell.setColspan(20);
                cell.setMinimumHeight(30);
                tabb.addCell(cell);

                Phrase pha = new Phrase("ADM NUMBER: " + adm + "           NAME: " + Globals.fullName(adm), new Font(Font.FontFamily.HELVETICA, 14, Font.NORMAL, BaseColor.BLACK));
                PdfPCell cella = new PdfPCell(pha);
                cella.setColspan(20);
                cella.setMinimumHeight(30);
                tabb.addCell(cella);

                Phrase ph1 = new Phrase("AMOUNT RECEIVED:     KSH." + amount + "                         DATE:  " + date, new Font(Font.FontFamily.HELVETICA, 14, Font.NORMAL, BaseColor.BLACK));
                PdfPCell cell1 = new PdfPCell(ph1);
                cell1.setColspan(20);
                cell1.setMinimumHeight(30);
                tabb.addCell(cell1);


                Phrase ph2 = new Phrase("VOTEHEAD NAME", new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL, BaseColor.BLACK));
                PdfPCell cell2 = new PdfPCell(ph2);
                cell2.setColspan(10);

                Phrase ph3 = new Phrase("FORM", new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL, BaseColor.BLACK));
                PdfPCell cell3 = new PdfPCell(ph3);
                cell3.setColspan(2);

                Phrase ph4 = new Phrase("TERM", new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL, BaseColor.BLACK));
                PdfPCell cell4 = new PdfPCell(ph4);
                cell4.setColspan(2);

                Phrase ph5 = new Phrase("YEAR", new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL, BaseColor.BLACK));
                PdfPCell cell5 = new PdfPCell(ph5);
                cell5.setColspan(2);

                Phrase ph6 = new Phrase("AMOUNT(KSH)", new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL, BaseColor.BLACK));
                PdfPCell cell6 = new PdfPCell(ph6);
                cell6.setColspan(4);
                tabb.addCell(cell2);
                tabb.addCell(cell3);
                tabb.addCell(cell4);
                tabb.addCell(cell5);
                tabb.addCell(cell6);


            }

            {
                String qrr = "Select distinct voteheadid from voteheads where payableasfee='" + "1" + "' ";
                ps = con.prepareStatement(qrr);
                rs = ps.executeQuery();
                while (rs.next()) {
                    String voteheadid = rs.getString("Voteheadid");
                    double voteheadamount = 0;
                    String sql = "Select sum(amount) from reciepts where RecieptNumber='" + reciptno + "' and voteheadid='" + voteheadid + "' and term='" + Globals.currentTermName() + "' and year(date)='" + Globals.academicYear() + "'";
                    ps = con.prepareStatement(sql);
                    ResultSet rx = ps.executeQuery();
                    if (rx.next()) {
                        voteheadamount = rx.getDouble("Sum(amount)");
                    }
                    String vamount = "";
                    if (voteheadamount == 0) {
                        vamount = "";
                    } else {
                        vamount = String.valueOf(voteheadamount);
                    }
                    Phrase ph2 = new Phrase(Globals.VoteHeadName(voteheadid), new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL, BaseColor.BLACK));
                    PdfPCell cell2 = new PdfPCell(ph2);
                    cell2.setColspan(10);

                    Phrase ph3 = new Phrase("", new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL, BaseColor.BLACK));
                    PdfPCell cell3 = new PdfPCell(ph3);
                    cell3.setColspan(2);

                    Phrase ph4 = new Phrase(Globals.currentTermName(), new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL, BaseColor.BLACK));
                    PdfPCell cell4 = new PdfPCell(ph4);
                    cell4.setColspan(2);

                    Phrase ph5 = new Phrase(String.valueOf(Globals.academicYear()), new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL, BaseColor.BLACK));
                    PdfPCell cell5 = new PdfPCell(ph5);
                    cell5.setColspan(2);

                    Phrase ph6 = new Phrase(vamount, new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL, BaseColor.BLACK));
                    PdfPCell cell6 = new PdfPCell(ph6);
                    cell6.setColspan(4);
                    tabb.addCell(cell2);
                    tabb.addCell(cell3);
                    tabb.addCell(cell4);
                    tabb.addCell(cell5);
                    tabb.addCell(cell6);

                }

                {

                    Phrase ph = new Phrase("MODE OF PAYMENT".toUpperCase() + ":   " + paymode, new Font(Font.FontFamily.HELVETICA, 14, Font.NORMAL, BaseColor.BLACK));
                    PdfPCell cell = new PdfPCell(ph);
                    cell.setColspan(12);
                    tabb.addCell(cell);

                    Phrase ph2 = new Phrase("TOTAL".toUpperCase(), new Font(Font.FontFamily.HELVETICA, 14, Font.NORMAL, BaseColor.BLACK));
                    PdfPCell cell2 = new PdfPCell(ph2);
                    cell2.setColspan(4);
                    tabb.addCell(cell2);

                    Phrase ph3 = new Phrase(String.valueOf(amount), new Font(Font.FontFamily.HELVETICA, 14, Font.NORMAL, BaseColor.BLACK));
                    PdfPCell cell3 = new PdfPCell(ph3);
                    cell3.setColspan(4);
                    tabb.addCell(cell3);

                }

            }
            tabb.setWidthPercentage(100);
            doc.add(tabb);
            Paragraph pr7 = new Paragraph("OUTSTANDING BALANCE KSH. :" + Globals.balanceCalculator(adm), FontFactory.getFont(FontFactory.TIMES, 15, java.awt.Font.PLAIN, BaseColor.BLACK));

            pr7.setSpacingBefore(15);
            doc.add(pr7);
            Paragraph pr8 = new Paragraph("You Were Served By: " + Globals.CurrentUser, FontFactory.getFont(FontFactory.TIMES, 8, java.awt.Font.ITALIC, BaseColor.BLACK));

            pr8.setSpacingBefore(15);
            doc.add(pr8);
            doc.close();


            Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler " + "C:\\schoolData\\" + adm + "Reciept" + ".pdf");


        } catch (DocumentException | IOException | SQLException sq) {

            sq.printStackTrace();
        }
    }

}
