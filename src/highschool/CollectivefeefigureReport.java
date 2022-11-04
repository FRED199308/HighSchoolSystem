/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package highschool;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.VerticalPositionMark;

import java.io.File;
import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Date;
import javax.swing.JDialog;

/**
 * @author FREDDY
 */
public class CollectivefeefigureReport {

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

    public static void feeAmount(String year, String term, String classname, String Program) {

        try {


            con = DbConnection.connectDb();

            Document doc = new Document();
            com.itextpdf.text.Image image = com.itextpdf.text.Image.getInstance("C:/schooldata/logo.jpg");
            PdfWriter writer = PdfWriter.getInstance(doc, new FileOutputStream("C:\\schoolData\\feeAmount.pdf"));
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
            Paragraph pr5 = new Paragraph("Cummulative Fee Figure".toUpperCase(), FontFactory.getFont(FontFactory.HELVETICA, 13, java.awt.Font.BOLD, BaseColor.BLACK));
            Chunk glue = new Chunk(new VerticalPositionMark());
            doc.addCreationDate();
            Paragraph pr7 = new Paragraph("CLASS:  " + classname.toUpperCase() + "                 Term: " + term.toUpperCase(), FontFactory.getFont(FontFactory.TIMES, 13, java.awt.Font.PLAIN, BaseColor.BLACK));
            pr7.add(new Chunk(glue));
            Paragraph pr8 = new Paragraph("YEAR: " + year, FontFactory.getFont(FontFactory.TIMES, 13, java.awt.Font.PLAIN, BaseColor.BLACK));

            pr7.add("Program: ".toUpperCase() + Program);

            pr5.setIndentationLeft(150);
            pr7.setSpacingBefore(30);
            doc.add(pr5);
            doc.add(pr7);
            doc.add(pr8);

            PdfPTable tabb = new PdfPTable(20);
            tabb.setSpacingBefore(10);
            tabb.setWidthPercentage(100);
            {

                Phrase ph = new Phrase("No.", new Font(Font.FontFamily.HELVETICA, 7, Font.NORMAL, BaseColor.BLACK));
                PdfPCell cell = new PdfPCell(ph);

                cell.setVerticalAlignment(Element.ALIGN_BOTTOM);
                cell.setHorizontalAlignment(Element.ALIGN_MIDDLE);
                Phrase ph1 = new Phrase("Votehead Name", new Font(Font.FontFamily.HELVETICA, 7, Font.NORMAL, BaseColor.BLACK));
                PdfPCell cell1 = new PdfPCell(ph1);

                cell1.setColspan(6);
                Phrase ph2 = new Phrase("Amount", new Font(Font.FontFamily.HELVETICA, 7, Font.NORMAL, BaseColor.BLACK));
                PdfPCell cell2 = new PdfPCell(ph2);
                cell2.setColspan(3);

                Phrase ph3 = new Phrase("", new Font(Font.FontFamily.HELVETICA, 7, Font.NORMAL, BaseColor.BLACK));
                PdfPCell cell3 = new PdfPCell(ph3);
                cell3.setColspan(2);
                cell3.setVerticalAlignment(Element.ALIGN_BOTTOM);
                cell3.setHorizontalAlignment(Element.ALIGN_MIDDLE);


                Phrase ph4 = new Phrase("", new Font(Font.FontFamily.HELVETICA, 7, Font.NORMAL, BaseColor.BLACK));
                PdfPCell cell4 = new PdfPCell(ph4);

                cell4.setColspan(8);
                tabb.addCell(cell);
                tabb.addCell(cell1);
                tabb.addCell(cell2);
                tabb.addCell(cell3);
                tabb.addCell(cell4);
            }
            int counter = 1;
            double totalAmount = 0;
            String sql7 = "Select amountperhead,voteheadid from studentpayablevoteheads where termcode='" + Globals.termcode(term) + "' and academicYear='" + year + "'  and amountperhead!='" + "" + "' and amountPerhead!='" + "0" + "' and program='" + Program + "' and classcode='" + Globals.classCode(classname) + "'";
            ps = con.prepareStatement(sql7);
            rs = ps.executeQuery();
            while (rs.next()) {

                double fee = rs.getDouble("amountperhead");
                String voteheadid = rs.getString("voteheadid");
                totalAmount = fee + totalAmount;

                Phrase ph = new Phrase(String.valueOf(counter), new Font(Font.FontFamily.HELVETICA, 7, Font.NORMAL, BaseColor.BLACK));
                PdfPCell cell = new PdfPCell(ph);

                cell.setVerticalAlignment(Element.ALIGN_BOTTOM);
                cell.setHorizontalAlignment(Element.ALIGN_MIDDLE);
                Phrase ph1 = new Phrase(Globals.VoteHeadName(voteheadid), new Font(Font.FontFamily.HELVETICA, 7, Font.NORMAL, BaseColor.BLACK));
                PdfPCell cell1 = new PdfPCell(ph1);

                cell1.setColspan(6);
                Phrase ph2 = new Phrase(String.valueOf(fee), new Font(Font.FontFamily.HELVETICA, 7, Font.NORMAL, BaseColor.BLACK));
                PdfPCell cell2 = new PdfPCell(ph2);
                cell2.setColspan(3);

                Phrase ph3 = new Phrase("", new Font(Font.FontFamily.HELVETICA, 7, Font.NORMAL, BaseColor.BLACK));
                PdfPCell cell3 = new PdfPCell(ph3);
                cell3.setColspan(2);
                cell3.setVerticalAlignment(Element.ALIGN_BOTTOM);
                cell3.setHorizontalAlignment(Element.ALIGN_MIDDLE);


                Phrase ph4 = new Phrase("", new Font(Font.FontFamily.HELVETICA, 7, Font.NORMAL, BaseColor.BLACK));
                PdfPCell cell4 = new PdfPCell(ph4);

                cell4.setColspan(8);
                tabb.addCell(cell);
                tabb.addCell(cell1);
                tabb.addCell(cell2);
                tabb.addCell(cell3);
                tabb.addCell(cell4);
                counter++;

            }

            {


                Phrase ph = new Phrase("", new Font(Font.FontFamily.HELVETICA, 7, Font.NORMAL, BaseColor.BLACK));
                PdfPCell cell = new PdfPCell(ph);

                cell.setVerticalAlignment(Element.ALIGN_BOTTOM);
                cell.setHorizontalAlignment(Element.ALIGN_MIDDLE);
                Phrase ph1 = new Phrase("TOTAL", new Font(Font.FontFamily.HELVETICA, 7, Font.NORMAL, BaseColor.BLACK));
                PdfPCell cell1 = new PdfPCell(ph1);

                cell1.setColspan(6);
                Phrase ph2 = new Phrase(String.valueOf(totalAmount), new Font(Font.FontFamily.HELVETICA, 7, Font.NORMAL, BaseColor.BLACK));
                PdfPCell cell2 = new PdfPCell(ph2);
                cell2.setColspan(3);

                Phrase ph3 = new Phrase("", new Font(Font.FontFamily.HELVETICA, 7, Font.NORMAL, BaseColor.BLACK));
                PdfPCell cell3 = new PdfPCell(ph3);
                cell3.setColspan(2);
                cell3.setVerticalAlignment(Element.ALIGN_BOTTOM);
                cell3.setHorizontalAlignment(Element.ALIGN_MIDDLE);


                Phrase ph4 = new Phrase("", new Font(Font.FontFamily.HELVETICA, 7, Font.NORMAL, BaseColor.BLACK));
                PdfPCell cell4 = new PdfPCell(ph4);

                cell4.setColspan(8);
                tabb.addCell(cell);
                tabb.addCell(cell1);
                tabb.addCell(cell2);
                tabb.addCell(cell3);
                tabb.addCell(cell4);
            }
            tabb.setHeaderRows(1);
            tabb.setWidthPercentage(100);
            doc.add(tabb);
            doc.close();


            if (ConfigurationIntialiser.docOpener()) {
                if (dialog == null) {


                    dialog = ReportFrame.getReportFrameInstance(new File("C:\\schoolData\\feeAmount.pdf"));


                } else {
                    dialog = null;
                    dialog = ReportFrame.getReportFrameInstance(new File("C:\\schoolData\\feeAmount.pdf"));
                    dialog.setVisible(true);


                }

            } else {

                Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler " + "C:\\schoolData\\feeAmount.pdf");

            }


        } catch (Exception sq) {
            sq.printStackTrace();
        }

    }


}
