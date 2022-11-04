/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package highschool;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfName;
import com.itextpdf.text.pdf.PdfNumber;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfPage;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.VerticalPositionMark;

import java.awt.HeadlessException;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JTable;

/**
 * @author FRED
 */
public class ReportGenerator {

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


    public static String completionYear(String adm) {
        Connection con;
        PreparedStatement ps = null;
        ResultSet rs;
        con = DbConnection.connectDb();
        try {

            String sql = "Select yearofcompletion from completionclasslists where admnumber='" + adm + "'";
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getString("yearofcompletion");
            } else {
                return "";
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return "";
        } finally {
            try {
                con.close();
                ps.close();
            } catch (SQLException sq) {
                JOptionPane.showMessageDialog(null, sq);
                sq.printStackTrace();
            }

        }

    }

    public static void productinfor(String productid) {
        try {


            String docName = "ProductInfor";
            con = DbConnection.connectDb();

            Document doc = new Document();

            PdfWriter writer = PdfWriter.getInstance(doc, new FileOutputStream("C:\\schoolData\\" + docName + ".pdf"));
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

            Paragraph pr5 = new Paragraph("product Information".toUpperCase(), FontFactory.getFont(FontFactory.HELVETICA, 13, java.awt.Font.BOLD, BaseColor.BLACK));

            doc.add(pr5);
            String description = "";
            PdfPTable tabb = new PdfPTable(2);
            tabb.setSpacingBefore(10);
            tabb.setWidthPercentage(100);

            Phrase ph = new Phrase("Product Attribute".toUpperCase(), new Font(Font.FontFamily.HELVETICA, 11, Font.NORMAL, BaseColor.BLUE));
            PdfPCell cell = new PdfPCell(ph);
            Phrase pha = new Phrase("Information".toUpperCase(), new Font(Font.FontFamily.HELVETICA, 11, Font.NORMAL, BaseColor.BLUE));
            PdfPCell cella = new PdfPCell(pha);
            tabb.addCell(cell);
            tabb.addCell(cella);

            String sql = "select * from products,inventory where products.productid='" + productid + "' and products.productid=inventory.productid";
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            if (rs.next()) {

                Phrase ph1 = new Phrase("Product Name".toUpperCase(), new Font(Font.FontFamily.HELVETICA, 9, Font.NORMAL, BaseColor.BLACK));
                PdfPCell cell1 = new PdfPCell(ph1);
                Phrase ph1a = new Phrase(rs.getString("productName"), new Font(Font.FontFamily.HELVETICA, 9, Font.NORMAL, BaseColor.BLACK));
                PdfPCell cell1a = new PdfPCell(ph1a);
                tabb.addCell(cell1);
                tabb.addCell(cell1a);

                Phrase ph2 = new Phrase(" Product Ref. Number".toUpperCase(), new Font(Font.FontFamily.HELVETICA, 9, Font.NORMAL, BaseColor.BLACK));
                PdfPCell cell2 = new PdfPCell(ph2);
                Phrase ph2a = new Phrase(rs.getString("productid").toUpperCase(), new Font(Font.FontFamily.HELVETICA, 9, Font.NORMAL, BaseColor.BLACK));
                PdfPCell cell2a = new PdfPCell(ph2a);
                tabb.addCell(cell2);
                tabb.addCell(cell2a);


                Phrase ph3 = new Phrase("product Barcode Number".toUpperCase(), new Font(Font.FontFamily.HELVETICA, 9, Font.NORMAL, BaseColor.BLACK));
                PdfPCell cell3 = new PdfPCell(ph3);
                Phrase ph3a = new Phrase(rs.getString("barcode").toUpperCase(), new Font(Font.FontFamily.HELVETICA, 9, Font.NORMAL, BaseColor.BLACK));
                PdfPCell cell3a = new PdfPCell(ph3a);
                tabb.addCell(cell3);
                tabb.addCell(cell3a);


                Phrase ph4 = new Phrase("Product Supplier".toUpperCase(), new Font(Font.FontFamily.HELVETICA, 9, Font.NORMAL, BaseColor.BLACK));
                PdfPCell cell4 = new PdfPCell(ph4);
                Phrase ph4a = new Phrase(Globals.supplierName(rs.getString("supplierid")).toUpperCase(), new Font(Font.FontFamily.HELVETICA, 9, Font.NORMAL, BaseColor.BLACK));
                PdfPCell cell4a = new PdfPCell(ph4a);
                tabb.addCell(cell4);
                tabb.addCell(cell4a);

                Phrase ph5 = new Phrase("Product Category".toUpperCase(), new Font(Font.FontFamily.HELVETICA, 9, Font.NORMAL, BaseColor.BLACK));
                PdfPCell cell5 = new PdfPCell(ph5);
                Phrase ph5a = new Phrase(Globals.goodscategoryName(rs.getString("categoryId")).toUpperCase(), new Font(Font.FontFamily.HELVETICA, 9, Font.NORMAL, BaseColor.BLACK));
                PdfPCell cell5a = new PdfPCell(ph5a);
                tabb.addCell(cell5);
                tabb.addCell(cell5a);


                Phrase ph8 = new Phrase("Buying Price".toUpperCase(), new Font(Font.FontFamily.HELVETICA, 9, Font.NORMAL, BaseColor.BLACK));
                PdfPCell cell8 = new PdfPCell(ph8);
                Phrase ph8a = new Phrase("Ksh. " + rs.getString("Buyingprice"), new Font(Font.FontFamily.HELVETICA, 9, Font.NORMAL, BaseColor.BLACK));
                PdfPCell cell8a = new PdfPCell(ph8a);
                tabb.addCell(cell8);
                tabb.addCell(cell8a);


                Phrase ph10 = new Phrase("Units Present".toUpperCase(), new Font(Font.FontFamily.HELVETICA, 9, Font.NORMAL, BaseColor.BLACK));
                PdfPCell cell10 = new PdfPCell(ph10);
                Phrase ph10a = new Phrase(rs.getString("Units"), new Font(Font.FontFamily.HELVETICA, 9, Font.NORMAL, BaseColor.BLACK));
                PdfPCell cell10a = new PdfPCell(ph10a);
                tabb.addCell(cell10);
                tabb.addCell(cell10a);

                Phrase ph11 = new Phrase("Expire Date".toUpperCase(), new Font(Font.FontFamily.HELVETICA, 9, Font.NORMAL, BaseColor.BLACK));
                PdfPCell cell11 = new PdfPCell(ph11);
                Phrase ph11a = new Phrase(rs.getString("ExpireDate"), new Font(Font.FontFamily.HELVETICA, 9, Font.NORMAL, BaseColor.BLACK));
                PdfPCell cell11a = new PdfPCell(ph11a);
                tabb.addCell(cell11);
                tabb.addCell(cell11a);


                description = rs.getString("Description");

            }
            doc.add(tabb);


            Paragraph pr6 = new Paragraph("product Description: ".toUpperCase() + description, FontFactory.getFont(FontFactory.HELVETICA, 13, java.awt.Font.BOLD, BaseColor.BLACK));
            doc.add(pr6);
            doc.close();

            if (ConfigurationIntialiser.docOpener()) {
                if (dialog == null) {

                    dialog = ReportFrame.getReportFrameInstance(new File("C:\\POSData\\" + docName + ".pdf"));

                } else {
                    dialog = null;
                    dialog = ReportFrame.getReportFrameInstance(new File("C:\\schoolData\\" + docName + ".pdf"));
                    dialog.setVisible(true);

                }

            } else {

                Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler " + "C:\\schoolData\\" + docName + ".pdf");

            }

        } catch (Exception sq) {
            sq.printStackTrace();
            JOptionPane.showMessageDialog(null, sq.getMessage());
        }


    }

    public static void partialMissingMarks(String examcode, String examname, String stream, String term, String academicyear, String subjectcode, String classname, String subjectname, String paper) {
        try {

            con = DbConnection.connectDb();

            Document doc = new Document();

            PdfWriter writer = PdfWriter.getInstance(doc, new FileOutputStream("C:\\schoolData\\missingmarks.pdf"));
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
            Paragraph pr5 = new Paragraph("MISSING MARKS".toUpperCase(), FontFactory.getFont(FontFactory.TIMES, 13, java.awt.Font.BOLD, BaseColor.BLACK));
            Chunk glue = new Chunk(new VerticalPositionMark());
            doc.addCreationDate();
            Paragraph pr7 = new Paragraph("CLASS:  " + classname.toUpperCase() + "             STREAM: " + stream.toUpperCase(), FontFactory.getFont(FontFactory.TIMES, 13, java.awt.Font.PLAIN, BaseColor.BLACK));
            pr7.add(new Chunk(glue));

            Paragraph pr8 = new Paragraph("EXAM NAME:  " + examname.toUpperCase(), FontFactory.getFont(FontFactory.TIMES, 13, java.awt.Font.PLAIN, BaseColor.BLACK));
            pr8.add(new Chunk(glue));
            pr7.add("TERM       " + term + "     ACADEMIC YEAR: " + academicyear);
            pr8.add("SUBJECT       " + subjectname + " PAPER " + paper + "     SUBJECT CODE: " + subjectcode);

            pr5.setIndentationLeft(150);
            pr7.setSpacingBefore(20);
            pr8.setSpacingBefore(10);
            doc.add(pr5);
            doc.add(pr7);
            doc.add(pr8);
            PdfPTable tabb = new PdfPTable(20);
            {
                tabb.setSpacingBefore(10);
                tabb.setWidthPercentage(100);
                Phrase ph = new Phrase("No.", new Font(Font.FontFamily.HELVETICA, 7, Font.NORMAL, BaseColor.BLACK));
                PdfPCell cell = new PdfPCell(ph);
                cell.setVerticalAlignment(Element.ALIGN_BOTTOM);
                cell.setHorizontalAlignment(Element.ALIGN_MIDDLE);
                tabb.addCell(cell);

                Phrase ph1 = new Phrase("ADM Number", new Font(Font.FontFamily.HELVETICA, 7, Font.NORMAL, BaseColor.BLACK));
                PdfPCell cell1 = new PdfPCell(ph1);
                cell1.setRotation(90);
                cell1.setColspan(2);
                tabb.addCell(cell1);
                Phrase ph2 = new Phrase("NAME", new Font(Font.FontFamily.HELVETICA, 7, Font.NORMAL, BaseColor.BLACK));
                PdfPCell cell2 = new PdfPCell(ph2);
                cell2.setVerticalAlignment(Element.ALIGN_BOTTOM);
                cell2.setHorizontalAlignment(Element.ALIGN_MIDDLE);
                cell2.setColspan(10);
                tabb.addCell(cell2);
                for (int k = 0; k < 7; ++k) {
                    Phrase ph3 = new Phrase("", new Font(Font.FontFamily.HELVETICA, 7, Font.NORMAL, BaseColor.BLACK));
                    PdfPCell cell3 = new PdfPCell(ph3);
                    tabb.addCell(cell3);
                }

            }

            int counter = 1;
            String sql5 = "Select admissionnumber from studentsubjectallocation,admission where subjectcode='" + subjectcode + "' and academicyear='" + academicyear + "' and admnumber=admissionnumber and currentform='" + Globals.classCode(classname) + "' and currentstream='" + Globals.streamcode(stream) + "'";
            ps = con.prepareStatement(sql5);
            rs = ps.executeQuery();
            while (rs.next()) {
                String adm = rs.getString("admissionNumber");
                String sq = "Select * from partialSubjectmark where subjectcode='" + subjectcode + "' and academicyear='" + academicyear + "' and examcode='" + examcode + "' and admnumber='" + adm + "' and paper='" + paper + "'";
                ps = con.prepareStatement(sq);
                ResultSet RS = ps.executeQuery();
                if (RS.next()) {

                } else {

                    Phrase ph = new Phrase(String.valueOf(counter), new Font(Font.FontFamily.HELVETICA, 7, Font.NORMAL, BaseColor.BLACK));
                    PdfPCell cell = new PdfPCell(ph);
                    cell.setVerticalAlignment(Element.ALIGN_BOTTOM);
                    cell.setHorizontalAlignment(Element.ALIGN_MIDDLE);
                    tabb.addCell(cell);

                    Phrase ph1 = new Phrase(adm, new Font(Font.FontFamily.HELVETICA, 7, Font.NORMAL, BaseColor.BLACK));
                    PdfPCell cell1 = new PdfPCell(ph1);

                    cell1.setColspan(2);
                    tabb.addCell(cell1);
                    Phrase ph2 = new Phrase(Globals.fullName(adm), new Font(Font.FontFamily.HELVETICA, 7, Font.NORMAL, BaseColor.BLACK));
                    PdfPCell cell2 = new PdfPCell(ph2);
                    cell2.setColspan(10);
                    tabb.addCell(cell2);

                    for (int k = 0; k < 7; ++k) {
                        Phrase ph3 = new Phrase("", new Font(Font.FontFamily.HELVETICA, 7, Font.NORMAL, BaseColor.BLACK));
                        PdfPCell cell3 = new PdfPCell(ph3);
                        tabb.addCell(cell3);
                    }
                    counter++;
                }

            }
            tabb.setHeaderRows(1);
            doc.add(tabb);

            doc.close();
            if (ConfigurationIntialiser.docOpener()) {
                if (dialog == null) {

                    dialog = ReportFrame.getReportFrameInstance(new File("C:\\schoolData\\missingmarks.pdf"));

                } else {
                    dialog = null;
                    dialog = ReportFrame.getReportFrameInstance(new File("C:\\schoolData\\missingmarks.pdf"));
                    dialog.setVisible(true);

                }

            } else {

                Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler " + "C:\\schoolData\\missingmarks.pdf");

            }

        } catch (Exception sq) {
            sq.printStackTrace();
            JOptionPane.showMessageDialog(null, sq.getMessage());
        }

    }


    public static void partialSubjectResults(String examcode, String examname, String stream, String term, String academicyear, String subjectcode, String classname, String subjectname, String sort, String paper) {
        try {
            NumberFormat nf2 = NumberFormat.getInstance();
            nf2.setMaximumFractionDigits(0);
            nf2.setMinimumFractionDigits(0);

            nf.setMaximumFractionDigits(0);
            nf.setMinimumFractionDigits(0);
            con = DbConnection.connectDb();

            Document doc = new Document();

            PdfWriter writer = PdfWriter.getInstance(doc, new FileOutputStream("C:\\schoolData\\subjectResults.pdf"));
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
            Paragraph pr5 = new Paragraph("Partial Exam Subject Results".toUpperCase(), FontFactory.getFont(FontFactory.HELVETICA, 13, java.awt.Font.BOLD, BaseColor.BLACK));
            Chunk glue = new Chunk(new VerticalPositionMark());
            doc.addCreationDate();
            Paragraph pr7 = new Paragraph("CLASS:  " + classname.toUpperCase() + "             STREAM: " + stream.toUpperCase(), FontFactory.getFont(FontFactory.TIMES, 13, java.awt.Font.PLAIN, BaseColor.BLACK));
            pr7.add(new Chunk(glue));

            Paragraph pr8 = new Paragraph("EXAM NAME:  " + examname.toUpperCase(), FontFactory.getFont(FontFactory.TIMES, 13, java.awt.Font.PLAIN, BaseColor.BLACK));
            pr8.add(new Chunk(glue));
            pr7.add("TERM       " + term + "     ACADEMIC YEAR: " + academicyear);
            pr8.add("SUBJECT       " + subjectname + " PAPER " + paper + "     SUBJECT CODE: " + subjectcode);

            pr5.setIndentationLeft(150);
            pr7.setSpacingBefore(20);
            pr8.setSpacingBefore(10);
            doc.add(pr5);
            doc.add(pr7);
            doc.add(pr8);
            double value = 0, totalPossible = 0;
            String sql6 = "Select value,totalpossible from subjectcombinationrules where subjectcode='" + subjectcode + "' and paper='" + paper + "'";
            ps = con.prepareStatement(sql6);
            rs = ps.executeQuery();
            if (rs.next()) {
                value = rs.getDouble("value");
                totalPossible = rs.getDouble("Totalpossible");
            }

            PdfPTable tabb = new PdfPTable(21);
            {
                tabb.setSpacingBefore(10);
                tabb.setWidthPercentage(100);
                Phrase ph = new Phrase("No.", new Font(Font.FontFamily.HELVETICA, 7, Font.NORMAL, BaseColor.BLACK));
                PdfPCell cell = new PdfPCell(ph);

                cell.setVerticalAlignment(Element.ALIGN_BOTTOM);
                cell.setHorizontalAlignment(Element.ALIGN_MIDDLE);
                Phrase ph1 = new Phrase("ADM Number", new Font(Font.FontFamily.HELVETICA, 7, Font.NORMAL, BaseColor.BLACK));
                PdfPCell cell1 = new PdfPCell(ph1);
                cell1.setRotation(90);
                cell1.setColspan(2);
                Phrase ph2 = new Phrase("Class & Stream", new Font(Font.FontFamily.HELVETICA, 7, Font.NORMAL, BaseColor.BLACK));
                PdfPCell cell2 = new PdfPCell(ph2);
                cell2.setColspan(3);
                cell2.setRotation(90);
                Phrase ph3 = new Phrase("Name", new Font(Font.FontFamily.HELVETICA, 7, Font.NORMAL, BaseColor.BLACK));

                PdfPCell cell3 = new PdfPCell(ph3);
                cell3.setColspan(9);
                cell3.setVerticalAlignment(Element.ALIGN_BOTTOM);
                cell3.setHorizontalAlignment(Element.ALIGN_MIDDLE);
                Phrase ph4 = new Phrase("RAW MARKS X/", new Font(Font.FontFamily.HELVETICA, 7, Font.NORMAL, BaseColor.BLACK));
                PdfPCell cell4 = new PdfPCell(ph4);
                cell4.setRotation(90);
                Phrase ph5 = new Phrase("PROCESSED MARKS X/" + value, new Font(Font.FontFamily.HELVETICA, 7, Font.NORMAL, BaseColor.BLACK));
                PdfPCell cell5 = new PdfPCell(ph5);
                cell5.setRotation(90);
//                Phrase ph6 = new Phrase("PERCENTAGE", new Font(Font.FontFamily.HELVETICA, 7, Font.NORMAL, BaseColor.BLACK));
//                PdfPCell cell6 = new PdfPCell(ph6);
//                cell6.setRotation(90);
//                Phrase ph6a = new Phrase("POINTS", new Font(Font.FontFamily.HELVETICA, 7, Font.NORMAL, BaseColor.BLACK));
//                PdfPCell cell6a = new PdfPCell(ph6a);
//                cell6a.setRotation(90);
//                Phrase ph7 = new Phrase("GRADE", new Font(Font.FontFamily.HELVETICA, 7, Font.NORMAL, BaseColor.BLACK));
//                PdfPCell cell7 = new PdfPCell(ph7);
//                cell7.setRotation(90);
                Phrase ph8 = new Phrase("POSITION", new Font(Font.FontFamily.HELVETICA, 7, Font.NORMAL, BaseColor.BLACK));
                PdfPCell cell8 = new PdfPCell(ph8);
                cell8.setRotation(90);
                Phrase ph9 = new Phrase("POSITION OUT OF", new Font(Font.FontFamily.HELVETICA, 7, Font.NORMAL, BaseColor.BLACK));
                PdfPCell cell9 = new PdfPCell(ph9);
                cell9.setRotation(90);
                Phrase ph10 = new Phrase("TEACHER INTIALS", new Font(Font.FontFamily.HELVETICA, 7, Font.NORMAL, BaseColor.BLACK));
                PdfPCell cell10 = new PdfPCell(ph10);
                cell10.setRotation(90);
                cell10.setColspan(2);
                tabb.addCell(cell);
                tabb.addCell(cell1);
                tabb.addCell(cell2);
                tabb.addCell(cell3);
                tabb.addCell(cell4);
                tabb.addCell(cell5);
//                tabb.addCell(cell6);
//                tabb.addCell(cell6a);
//                tabb.addCell(cell7);
                tabb.addCell(cell8);
                tabb.addCell(cell9);
                tabb.addCell(cell10);
                int headcount = 0;
                int previousscore = 0;
                int totalentries = 0;
                String sql2 = "Select count(*) from partialsubjectMark where examcode='" + examcode + "'  and academicyear='" + academicyear + "' and subjectcode='" + subjectcode + "' and paper='" + paper + "' order by score";
                ps = con.prepareStatement(sql2);
                rs = ps.executeQuery();
                if (rs.next()) {
                    totalentries = rs.getInt("count(*)");
                }
                int tiechck = 0;

                String sql3 = "Select score,admnumber from partialsubjectmark where examcode='" + examcode + "' and academicyear='" + academicyear + "' and subjectcode='" + subjectcode + "' and paper='" + paper + "' order by score desc";
                ps = con.prepareStatement(sql3);
                rs = ps.executeQuery();
                while (rs.next()) {
                    String adm = rs.getString("admNumber");

                    int examresult = rs.getInt("score");
                    if (examresult == previousscore) {
                        tiechck++;
                    } else {
                        headcount++;
                        headcount += tiechck;
                        tiechck = 0;
                    }
                    String sql4 = "Update partialsubjectMark  set position_per_subject='" + headcount + "', position_per_subject_out_of='" + totalentries + "' where examcode='" + examcode + "' and academicyear='" + academicyear + "' and subjectcode='" + subjectcode + "' and admnumber='" + adm + "' and paper='" + paper + "' ";
                    ps = con.prepareStatement(sql4);
                    ps.execute();

                    previousscore = rs.getInt("score");
                }

            }

            int counter = 1;
            String sql = "Select * from partialsubjectMark where examcode='" + examcode + "' and academicyear='" + academicyear + "' and streamcode='" + Globals.streamcode(stream) + "' and subjectcode='" + subjectcode + "' and paper='" + paper + "' order by " + sort + " desc";
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                double pscore = rs.getDouble("Score") * value / totalPossible;
                Phrase ph = new Phrase(String.valueOf(counter), new Font(Font.FontFamily.HELVETICA, 9, Font.NORMAL, BaseColor.BLACK));
                PdfPCell cell = new PdfPCell(ph);

                Phrase ph1 = new Phrase(rs.getString("admNumber"), new Font(Font.FontFamily.HELVETICA, 9, Font.NORMAL, BaseColor.BLACK));
                PdfPCell cell1 = new PdfPCell(ph1);

                cell1.setColspan(2);
                Phrase ph2 = new Phrase(classname + " " + stream, new Font(Font.FontFamily.HELVETICA, 7, Font.NORMAL, BaseColor.BLACK));
                PdfPCell cell2 = new PdfPCell(ph2);
                cell2.setColspan(3);

                Phrase ph3 = new Phrase(Globals.fullName(rs.getString("admnumber")), new Font(Font.FontFamily.HELVETICA, 9, Font.NORMAL, BaseColor.BLACK));

                PdfPCell cell3 = new PdfPCell(ph3);
                cell3.setColspan(9);

                Phrase ph4 = new Phrase(rs.getString("Score"), new Font(Font.FontFamily.HELVETICA, 9, Font.NORMAL, BaseColor.BLACK));
                PdfPCell cell4 = new PdfPCell(ph4);

                Phrase ph5 = new Phrase(String.valueOf(nf.format(pscore)), new Font(Font.FontFamily.HELVETICA, 9, Font.NORMAL, BaseColor.BLACK));
                PdfPCell cell5 = new PdfPCell(ph5);

//                Phrase ph6 = new Phrase(rs.getString("Exampercentage"), new Font(Font.FontFamily.HELVETICA, 9, Font.NORMAL, BaseColor.BLACK));
//                PdfPCell cell6 = new PdfPCell(ph6);
//
//                Phrase ph6a = new Phrase(rs.getString("exampoints"), new Font(Font.FontFamily.HELVETICA, 9, Font.NORMAL, BaseColor.BLACK));
//                PdfPCell cell6a = new PdfPCell(ph6a);
//
//                Phrase ph7 = new Phrase(rs.getString("ExamGrade"), new Font(Font.FontFamily.HELVETICA, 9, Font.NORMAL, BaseColor.BLACK));
//                PdfPCell cell7 = new PdfPCell(ph7);

                Phrase ph8 = new Phrase(rs.getString("position_per_subject"), new Font(Font.FontFamily.HELVETICA, 9, Font.NORMAL, BaseColor.BLACK));
                PdfPCell cell8 = new PdfPCell(ph8);

                Phrase ph9 = new Phrase(rs.getString("position_per_subject_out_of"), new Font(Font.FontFamily.HELVETICA, 9, Font.NORMAL, BaseColor.BLACK));
                PdfPCell cell9 = new PdfPCell(ph9);

                Phrase ph10 = new Phrase(rs.getString("teacherInitials"), new Font(Font.FontFamily.HELVETICA, 9, Font.NORMAL, BaseColor.BLACK));
                PdfPCell cell10 = new PdfPCell(ph10);

                cell10.setColspan(2);
                tabb.addCell(cell);
                tabb.addCell(cell1);
                tabb.addCell(cell2);
                tabb.addCell(cell3);
                tabb.addCell(cell4);
                tabb.addCell(cell5);
//                tabb.addCell(cell6);
//                tabb.addCell(cell6a);
//                tabb.addCell(cell7);
                tabb.addCell(cell8);
                tabb.addCell(cell9);
                tabb.addCell(cell10);
                counter++;
            }
            double meanpoint = 0, meanscore = 0, meanconverted = 0, meanpercent = 0;
            String meangrade = "";
            String sql5 = "Select avg(score) from partialsubjectMark where examcode='" + examcode + "' and classcode='" + Globals.classCode(classname) + "' and academicyear='" + academicyear + "' and subjectcode='" + subjectcode + "' and streamcode='" + Globals.streamcode(stream) + "'  and paper='" + paper + "' order by " + sort + " desc";
            ps = con.prepareStatement(sql5);
            rs = ps.executeQuery();
            if (rs.next()) {
                Phrase ph = new Phrase("MEAN SCORES", new Font(Font.FontFamily.HELVETICA, 9, Font.NORMAL, BaseColor.BLACK));
                PdfPCell cell = new PdfPCell(ph);
                cell.setColspan(15);
                Phrase ph4 = new Phrase(nf.format(rs.getDouble("avg(score)")), new Font(Font.FontFamily.HELVETICA, 9, Font.NORMAL, BaseColor.BLACK));
                PdfPCell cell4 = new PdfPCell(ph4);

//                Phrase ph5 = new Phrase(nf.format(rs.getDouble("avg(convertedscore)")), new Font(Font.FontFamily.HELVETICA, 9, Font.NORMAL, BaseColor.BLACK));
//                PdfPCell cell5 = new PdfPCell(ph5);
//
//                Phrase ph6 = new Phrase(nf.format(rs.getDouble("avg(exampercentage)")), new Font(Font.FontFamily.HELVETICA, 9, Font.NORMAL, BaseColor.BLACK));
//                PdfPCell cell6 = new PdfPCell(ph6);
//
//                Phrase ph6a = new Phrase(nf.format(rs.getDouble("avg(exampoints)")), new Font(Font.FontFamily.HELVETICA, 9, Font.NORMAL, BaseColor.BLACK));
//                meanpoint = rs.getDouble("avg(exampoints)");
//                PdfPCell cell6a = new PdfPCell(ph6a);
//                Phrase ph5a = null;
//
//                meanscore = rs.getDouble("avg(exampercentage)");
                Phrase ph5a = new Phrase("", new Font(Font.FontFamily.HELVETICA, 9, Font.NORMAL, BaseColor.BLACK));
                PdfPCell cell5a = new PdfPCell(ph5a);
                Phrase ph6aa = new Phrase("", new Font(Font.FontFamily.HELVETICA, 8, Font.NORMAL, BaseColor.BLACK));

                PdfPCell cell6aa = new PdfPCell(ph6aa);
                cell6aa.setColspan(4);

                tabb.addCell(cell);
                tabb.addCell(cell4);
//                tabb.addCell(cell5);
//                tabb.addCell(cell6);
//                tabb.addCell(cell6a);
                tabb.addCell(cell5a);
                tabb.addCell(cell6aa);
            }

            tabb.setHeaderRows(1);
            doc.add(tabb);

            doc.close();

            if (ConfigurationIntialiser.docOpener()) {
                if (dialog == null) {

                    dialog = ReportFrame.getReportFrameInstance(new File("C:\\schoolData\\subjectResults.pdf"));

                } else {
                    dialog = null;
                    dialog = ReportFrame.getReportFrameInstance(new File("C:\\schoolData\\subjectResults.pdf"));
                    dialog.setVisible(true);

                }

            } else {

                Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler " + "C:\\schoolData\\subjectResults.pdf");

            }
        } catch (Exception sq) {
            sq.printStackTrace();
            JOptionPane.showMessageDialog(null, sq.getMessage());
        }
    }


    public static void feeRegister(String Class, String academicYear, String term, String votehead) {

        try {

            Document doc = new Document(PageSize.A4.rotate());
            com.itextpdf.text.Image image = com.itextpdf.text.Image.getInstance("C:/schooldata/logo.jpg");
            PdfWriter writer = PdfWriter.getInstance(doc, new FileOutputStream("C:\\schoolData\\feeRegister.pdf"));
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
            double totalPaid = 0;
            double totalBalance = 0;
            String v = "", vid;
            if (votehead == null) {
                v = "ALL";
                vid = null;
            } else {
                v = votehead.toUpperCase();
                vid = Globals.voteHeadId(votehead);
            }
            tab.setWidthPercentage(100);
            doc.add(tab);
            Paragraph pr5 = new Paragraph("Fee Register".toUpperCase(), FontFactory.getFont(FontFactory.HELVETICA, 13, java.awt.Font.BOLD, BaseColor.BLACK));
            Chunk glue = new Chunk(new VerticalPositionMark());
            doc.addCreationDate();
            Paragraph pr7 = new Paragraph("CLASS:  " + Class, FontFactory.getFont(FontFactory.TIMES, 13, java.awt.Font.PLAIN, BaseColor.BLACK));
            pr7.add(new Chunk(glue));


            pr5.setIndentationLeft(100);
            pr5.add(new Chunk(glue));


            pr7.add("ACADEMIC YEAR: " + academicYear);
            pr5.add("VOTE HEAD: " + v.toUpperCase());

            doc.add(pr5);
            doc.add(pr7);

            con = DbConnection.connectDb();


            PdfPTable tabb = new PdfPTable(30);
            tabb.setHeaderRows(1);

            tabb.setSpacingBefore(10);
            tabb.setWidthPercentage(100);


            {
                Phrase ph = new Phrase("No.", new Font(Font.FontFamily.HELVETICA, 7, Font.NORMAL, BaseColor.BLACK));
                PdfPCell cell = new PdfPCell(ph);


                Phrase ph1 = new Phrase("ADM No.", new Font(Font.FontFamily.HELVETICA, 7, Font.NORMAL, BaseColor.BLACK));
                PdfPCell cell1 = new PdfPCell(ph1);


                Phrase ph2 = new Phrase("Name", new Font(Font.FontFamily.HELVETICA, 7, Font.NORMAL, BaseColor.BLACK));
                PdfPCell cell2 = new PdfPCell(ph2);
                cell2.setColspan(4);
                Phrase ph2a = new Phrase("OP Bal", new Font(Font.FontFamily.HELVETICA, 7, Font.NORMAL, BaseColor.BLACK));
                PdfPCell cell2a = new PdfPCell(ph2a);
                cell2a.setColspan(2);
                Phrase ph3 = new Phrase("Term 1", new Font(Font.FontFamily.HELVETICA, 7, Font.NORMAL, BaseColor.BLACK));

                PdfPCell cell3 = new PdfPCell(ph3);
                cell3.setColspan(6);

                Phrase ph4 = new Phrase("Term 2", new Font(Font.FontFamily.HELVETICA, 7, Font.NORMAL, BaseColor.BLACK));
                PdfPCell cell4 = new PdfPCell(ph4);
                cell4.setColspan(6);
                Phrase ph5 = new Phrase("Term 3", new Font(Font.FontFamily.HELVETICA, 7, Font.NORMAL, BaseColor.BLACK));
                PdfPCell cell5 = new PdfPCell(ph5);
                cell5.setColspan(6);
                Phrase ph6 = new Phrase("Total Paid", new Font(Font.FontFamily.HELVETICA, 7, Font.NORMAL, BaseColor.BLACK));
                PdfPCell cell6 = new PdfPCell(ph6);
                cell6.setColspan(2);
                Phrase ph7 = new Phrase("Balance", new Font(Font.FontFamily.HELVETICA, 7, Font.NORMAL, BaseColor.BLACK));
                PdfPCell cell7 = new PdfPCell(ph7);
                cell7.setColspan(2);

                tabb.addCell(cell);
                tabb.addCell(cell1);
                tabb.addCell(cell2);
                tabb.addCell(cell2a);
                tabb.addCell(cell3);
                tabb.addCell(cell4);
                tabb.addCell(cell5);
                tabb.addCell(cell6);
                tabb.addCell(cell7);

            }


            tabb.setWidthPercentage(100);

            if (votehead == null) {
                int count = 1;
                String sql3 = "Select admissionnumber,firstname,middlename,lastname from admission where  currentform='" + Globals.classCode(ClassProgressTracker.currentClass(Integer.parseInt(academicYear), Class)) + "'  order by admissionNumber";
                ps = con.prepareStatement(sql3);
                rs = ps.executeQuery();
                while (rs.next()) {

                    String adm = rs.getString("AdmissionNumber");
                    double opbal = Globals.OpeningBalance(academicYear, vid, adm);

                    Phrase ph = new Phrase(String.valueOf(count), new Font(Font.FontFamily.HELVETICA, 7, Font.NORMAL, BaseColor.BLACK));
                    PdfPCell cell = new PdfPCell(ph);

                    Phrase ph1 = new Phrase(rs.getString("AdmissionNumber"), new Font(Font.FontFamily.HELVETICA, 7, Font.NORMAL, BaseColor.BLACK));
                    PdfPCell cell1 = new PdfPCell(ph1);


                    Phrase ph2 = new Phrase(rs.getString("firstName") + "  " + rs.getString("Middlename") + " " + rs.getString("LastName"), new Font(Font.FontFamily.HELVETICA, 6, Font.NORMAL, BaseColor.BLACK));
                    PdfPCell cell2 = new PdfPCell(ph2);
                    cell2.setColspan(4);
                    Phrase ph2a = new Phrase(String.valueOf(opbal), new Font(Font.FontFamily.HELVETICA, 7, Font.NORMAL, BaseColor.BLACK));
                    PdfPCell cell2a = new PdfPCell(ph2a);
                    cell2a.setColspan(2);
                    double termtotalcounter = 0;
                    double bal = 0;
                    String paymentDetails = "";
                    String sql = "Select recieptnumber,amount from reciepts where term='" + "Term 1" + "' and academicyear='" + academicYear + "' and admnumber='" + adm + "' order by date";
                    ps = con.prepareStatement(sql);
                    ResultSet rx = ps.executeQuery();
                    while (rx.next()) {
                        termtotalcounter += rx.getDouble("Amount");
                        paymentDetails = paymentDetails + "R No. " + rx.getString("recieptnumber") + " Ksh " + rx.getDouble("Amount") + ",";

                    }

                    Phrase ph3 = new Phrase(paymentDetails, new Font(Font.FontFamily.HELVETICA, 6, Font.NORMAL, BaseColor.BLACK));

                    PdfPCell cell3 = new PdfPCell(ph3);
                    cell3.setColspan(6);
                    paymentDetails = "";
                    sql = "Select recieptnumber,amount from reciepts where term='" + "Term 2" + "' and academicyear='" + academicYear + "' and admnumber='" + adm + "' order by date";
                    ps = con.prepareStatement(sql);
                    rx = ps.executeQuery();
                    while (rx.next()) {
                        termtotalcounter += rx.getDouble("Amount");
                        paymentDetails = paymentDetails + "R No. " + rx.getString("recieptnumber") + " Ksh " + rx.getDouble("Amount") + ",";

                    }
                    Phrase ph4 = new Phrase(paymentDetails, new Font(Font.FontFamily.HELVETICA, 6, Font.NORMAL, BaseColor.BLACK));
                    PdfPCell cell4 = new PdfPCell(ph4);
                    cell4.setColspan(6);

                    paymentDetails = "";
                    sql = "Select recieptnumber,amount from reciepts where term='" + "Term 3" + "' and academicyear='" + academicYear + "' and admnumber='" + adm + "' order by date";
                    ps = con.prepareStatement(sql);
                    rx = ps.executeQuery();
                    while (rx.next()) {
                        termtotalcounter += rx.getDouble("Amount");
                        paymentDetails = paymentDetails + ",R No. " + rx.getString("recieptnumber") + " Ksh " + rx.getDouble("Amount") + ",";

                    }

                    Phrase ph5 = new Phrase(paymentDetails, new Font(Font.FontFamily.HELVETICA, 6, Font.NORMAL, BaseColor.BLACK));
                    PdfPCell cell5 = new PdfPCell(ph5);
                    cell5.setColspan(6);
                    paymentDetails = "";


                    Phrase ph6 = new Phrase(String.valueOf(termtotalcounter), new Font(Font.FontFamily.HELVETICA, 6, Font.NORMAL, BaseColor.BLACK));
                    PdfPCell cell6 = new PdfPCell(ph6);
                    cell6.setColspan(2);


                    sql = "Select sum(amount) from payablevoteheadperstudent where academicyear='" + academicYear + "' and admnumber='" + adm + "' ";
                    ps = con.prepareStatement(sql);
                    rx = ps.executeQuery();
                    if (rx.next()) {
                        bal = rx.getDouble("sum(amount)");


                    }

                    totalPaid += termtotalcounter;
                    bal = (bal - termtotalcounter) + opbal;
                    totalBalance += bal;

                    Phrase ph7 = new Phrase(String.valueOf(bal), new Font(Font.FontFamily.HELVETICA, 7, Font.NORMAL, BaseColor.BLACK));
                    PdfPCell cell7 = new PdfPCell(ph7);
                    cell7.setColspan(2);


                    tabb.addCell(cell);
                    tabb.addCell(cell1);
                    tabb.addCell(cell2);
                    tabb.addCell(cell2a);
                    tabb.addCell(cell3);
                    tabb.addCell(cell4);
                    tabb.addCell(cell5);
                    tabb.addCell(cell6);

                    tabb.addCell(cell7);
                    termtotalcounter = 0;
                    bal = 0;
                    count++;
                }

                Phrase ph8 = new Phrase("TOTAL", new Font(Font.FontFamily.HELVETICA, 7, Font.NORMAL, BaseColor.BLACK));
                PdfPCell cell8 = new PdfPCell(ph8);
                cell8.setColspan(26);

                Phrase ph9 = new Phrase(String.valueOf(totalPaid), new Font(Font.FontFamily.HELVETICA, 7, Font.NORMAL, BaseColor.BLACK));
                PdfPCell cell9 = new PdfPCell(ph9);
                cell9.setColspan(2);

                Phrase ph10 = new Phrase(String.valueOf(totalBalance), new Font(Font.FontFamily.HELVETICA, 7, Font.NORMAL, BaseColor.BLACK));
                PdfPCell cell10 = new PdfPCell(ph10);
                cell10.setColspan(2);
                tabb.addCell(cell8);
                tabb.addCell(cell9);
                tabb.addCell(cell10);

            } else {

                vid = Globals.voteHeadId(votehead);
                int count = 1;
                String sql3 = "Select admissionnumber,firstname,middlename,lastname from admission where  currentform='" + Globals.classCode(ClassProgressTracker.currentClass(Integer.parseInt(academicYear), Class)) + "'  order by admissionNumber";
                ps = con.prepareStatement(sql3);
                rs = ps.executeQuery();
                while (rs.next()) {

                    String adm = rs.getString("AdmissionNumber");
                    double opbal = Globals.OpeningBalance(academicYear, vid, adm);
                    Phrase ph = new Phrase(String.valueOf(count), new Font(Font.FontFamily.HELVETICA, 7, Font.NORMAL, BaseColor.BLACK));
                    PdfPCell cell = new PdfPCell(ph);

                    Phrase ph1 = new Phrase(rs.getString("AdmissionNumber"), new Font(Font.FontFamily.HELVETICA, 7, Font.NORMAL, BaseColor.BLACK));
                    PdfPCell cell1 = new PdfPCell(ph1);


                    Phrase ph2 = new Phrase(rs.getString("firstName") + "  " + rs.getString("Middlename") + " " + rs.getString("LastName"), new Font(Font.FontFamily.HELVETICA, 6, Font.NORMAL, BaseColor.BLACK));
                    PdfPCell cell2 = new PdfPCell(ph2);
                    cell2.setColspan(4);
                    Phrase ph2a = new Phrase(String.valueOf(opbal), new Font(Font.FontFamily.HELVETICA, 7, Font.NORMAL, BaseColor.BLACK));
                    PdfPCell cell2a = new PdfPCell(ph2a);
                    cell2a.setColspan(2);
                    double termtotalcounter = 0;
                    double bal = 0;
                    String paymentDetails = "";
                    String sql = "Select recieptnumber,amount from reciepts where term='" + "Term 1" + "' and academicyear='" + academicYear + "' and admnumber='" + adm + "' and voteheadid='" + vid + "' order by date";
                    ps = con.prepareStatement(sql);
                    ResultSet rx = ps.executeQuery();
                    while (rx.next()) {
                        termtotalcounter += rx.getDouble("Amount");
                        paymentDetails = paymentDetails + "R No. " + rx.getString("recieptnumber") + " Ksh " + rx.getDouble("Amount") + ",";

                    }

                    Phrase ph3 = new Phrase(paymentDetails, new Font(Font.FontFamily.HELVETICA, 6, Font.NORMAL, BaseColor.BLACK));

                    PdfPCell cell3 = new PdfPCell(ph3);
                    cell3.setColspan(6);
                    paymentDetails = "";
                    sql = "Select recieptnumber,amount from reciepts where term='" + "Term 2" + "' and academicyear='" + academicYear + "' and admnumber='" + adm + "' and voteheadid='" + vid + "'  order by date";
                    ps = con.prepareStatement(sql);
                    rx = ps.executeQuery();
                    while (rx.next()) {
                        termtotalcounter += rx.getDouble("Amount");
                        paymentDetails = paymentDetails + "R No. " + rx.getString("recieptnumber") + " Ksh " + rx.getDouble("Amount") + ",";

                    }
                    Phrase ph4 = new Phrase(paymentDetails, new Font(Font.FontFamily.HELVETICA, 6, Font.NORMAL, BaseColor.BLACK));
                    PdfPCell cell4 = new PdfPCell(ph4);
                    cell4.setColspan(6);

                    paymentDetails = "";
                    sql = "Select recieptnumber,amount from reciepts where term='" + "Term 3" + "' and academicyear='" + academicYear + "' and admnumber='" + adm + "'  and voteheadid='" + vid + "' order by date";
                    ps = con.prepareStatement(sql);
                    rx = ps.executeQuery();
                    while (rx.next()) {
                        termtotalcounter += rx.getDouble("Amount");
                        paymentDetails = paymentDetails + ",R No. " + rx.getString("recieptnumber") + " Ksh " + rx.getDouble("Amount") + ",";

                    }

                    Phrase ph5 = new Phrase(paymentDetails, new Font(Font.FontFamily.HELVETICA, 6, Font.NORMAL, BaseColor.BLACK));
                    PdfPCell cell5 = new PdfPCell(ph5);
                    cell5.setColspan(6);
                    paymentDetails = "";


                    Phrase ph6 = new Phrase(String.valueOf(termtotalcounter), new Font(Font.FontFamily.HELVETICA, 6, Font.NORMAL, BaseColor.BLACK));
                    PdfPCell cell6 = new PdfPCell(ph6);
                    cell6.setColspan(2);


                    sql = "Select sum(amount) from payablevoteheadperstudent where academicyear='" + academicYear + "' and admnumber='" + adm + "'  and voteheadid='" + vid + "'  ";
                    ps = con.prepareStatement(sql);
                    rx = ps.executeQuery();
                    if (rx.next()) {
                        bal += rx.getDouble("sum(amount)");


                    }
                    totalPaid += termtotalcounter;

                    bal = (bal - termtotalcounter) + opbal;
                    totalBalance += bal;
                    Phrase ph7 = new Phrase(String.valueOf(bal), new Font(Font.FontFamily.HELVETICA, 7, Font.NORMAL, BaseColor.BLACK));
                    PdfPCell cell7 = new PdfPCell(ph7);
                    cell7.setColspan(2);


                    tabb.addCell(cell);
                    tabb.addCell(cell1);
                    tabb.addCell(cell2);
                    tabb.addCell(cell2a);
                    tabb.addCell(cell3);
                    tabb.addCell(cell4);
                    tabb.addCell(cell5);
                    tabb.addCell(cell6);

                    tabb.addCell(cell7);
                    termtotalcounter = 0;
                    bal = 0;
                    count++;
                }

                Phrase ph8 = new Phrase("TOTAL", new Font(Font.FontFamily.HELVETICA, 7, Font.NORMAL, BaseColor.BLACK));
                PdfPCell cell8 = new PdfPCell(ph8);
                cell8.setColspan(26);

                Phrase ph9 = new Phrase(String.valueOf(totalPaid), new Font(Font.FontFamily.HELVETICA, 7, Font.NORMAL, BaseColor.BLACK));
                PdfPCell cell9 = new PdfPCell(ph9);
                cell9.setColspan(2);

                Phrase ph10 = new Phrase(String.valueOf(totalBalance), new Font(Font.FontFamily.HELVETICA, 7, Font.NORMAL, BaseColor.BLACK));
                PdfPCell cell10 = new PdfPCell(ph10);
                cell10.setColspan(2);
                tabb.addCell(cell8);
                tabb.addCell(cell9);
                tabb.addCell(cell10);


            }


            doc.add(tabb);

            doc.close();


            if (ConfigurationIntialiser.docOpener()) {
                if (dialog == null) {

                    dialog = ReportFrame.getReportFrameInstance(new File("C:\\schoolData\\FeeRegister.pdf"));

                } else {
                    dialog = null;
                    dialog = ReportFrame.getReportFrameInstance(new File("C:\\schoolData\\FeeRegister.pdf"));
                    dialog.setVisible(true);

                }

            } else {

                Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler " + "C:\\schoolData\\feeRegister.pdf");

            }


        } catch (Exception e) {
        }


    }

    public static void programClassList(String program, String stream, String classname) {

        try {

            con = DbConnection.connectDb();

            Document doc = new Document();
            com.itextpdf.text.Image image = com.itextpdf.text.Image.getInstance("C:/schooldata/logo.jpg");
            PdfWriter writer = PdfWriter.getInstance(doc, new FileOutputStream("C:\\schoolData\\SubjectClasslist.pdf"));
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
            Paragraph pr5 = new Paragraph("PROGRAM CLASS LIST", FontFactory.getFont(FontFactory.HELVETICA, 13, java.awt.Font.BOLD, BaseColor.BLACK));
            Chunk glue = new Chunk(new VerticalPositionMark());
            doc.addCreationDate();
            Paragraph pr7 = new Paragraph("CLASS:  " + classname.toUpperCase() + "                 STREAM: " + stream.toUpperCase(), FontFactory.getFont(FontFactory.TIMES, 13, java.awt.Font.PLAIN, BaseColor.BLACK));
            pr7.add(new Chunk(glue));
            Paragraph pr8 = new Paragraph("PROGRAM: " + program, FontFactory.getFont(FontFactory.TIMES, 13, java.awt.Font.PLAIN, BaseColor.BLACK));

            pr7.add("ACADEMIC YEAR: " + Globals.academicYear());

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
                Phrase ph1 = new Phrase("ADM Number", new Font(Font.FontFamily.HELVETICA, 7, Font.NORMAL, BaseColor.BLACK));
                PdfPCell cell1 = new PdfPCell(ph1);
                cell1.setRotation(90);
                cell1.setColspan(2);
                Phrase ph2 = new Phrase("Class & Stream", new Font(Font.FontFamily.HELVETICA, 7, Font.NORMAL, BaseColor.BLACK));
                PdfPCell cell2 = new PdfPCell(ph2);
                cell2.setColspan(3);
                cell2.setRotation(90);
                Phrase ph3 = new Phrase("Name", new Font(Font.FontFamily.HELVETICA, 7, Font.NORMAL, BaseColor.BLACK));
                PdfPCell cell3 = new PdfPCell(ph3);
                cell3.setColspan(6);
                cell3.setVerticalAlignment(Element.ALIGN_BOTTOM);
                cell3.setHorizontalAlignment(Element.ALIGN_MIDDLE);


                Phrase ph4 = new Phrase("", new Font(Font.FontFamily.HELVETICA, 7, Font.NORMAL, BaseColor.BLACK));
                PdfPCell cell4 = new PdfPCell(ph4);
                cell4.setRotation(90);
                cell4.setColspan(8);
                tabb.addCell(cell);
                tabb.addCell(cell1);
                tabb.addCell(cell2);
                tabb.addCell(cell3);
                tabb.addCell(cell4);
            }
            if (stream.equalsIgnoreCase("Overall")) {
                int counter = 0;
                String sql3 = "Select admissionnumber,firstname,middlename,lastname,currentstream from admission where  currentform='" + Globals.classCode(classname) + "' and program='" + program + "' order by admissionNumber";
                ps = con.prepareStatement(sql3);
                rs = ps.executeQuery();

                while (rs.next()) {

                    String adm = rs.getString("AdmissionNumber");


                    counter++;
                    Phrase ph = new Phrase(String.valueOf(counter), new Font(Font.FontFamily.HELVETICA, 7, Font.NORMAL, BaseColor.BLACK));
                    PdfPCell cell = new PdfPCell(ph);

                    cell.setVerticalAlignment(Element.ALIGN_BOTTOM);
                    cell.setHorizontalAlignment(Element.ALIGN_MIDDLE);
                    Phrase ph1 = new Phrase(adm, new Font(Font.FontFamily.HELVETICA, 7, Font.NORMAL, BaseColor.BLACK));
                    PdfPCell cell1 = new PdfPCell(ph1);

                    cell1.setColspan(2);
                    Phrase ph2 = new Phrase(classname + " " + Globals.streamName(rs.getString("CurrentStream")), new Font(Font.FontFamily.HELVETICA, 9, Font.NORMAL, BaseColor.BLACK));
                    PdfPCell cell2 = new PdfPCell(ph2);
                    cell2.setColspan(3);

                    Phrase ph3 = new Phrase(rs.getString("firstName") + "    " + rs.getString("Middlename") + "    " + rs.getString("LastName"), new Font(Font.FontFamily.HELVETICA, 9, Font.NORMAL, BaseColor.BLACK));
                    PdfPCell cell3 = new PdfPCell(ph3);
                    cell3.setColspan(6);
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
            } else {

                int counter = 0;
                String sql3 = "Select admissionnumber,firstname,middlename,lastname from admission where  currentform='" + Globals.classCode(classname) + "' and currentstream='" + Globals.streamcode(stream) + "' and program='" + program + "' order by admissionNumber";
                ps = con.prepareStatement(sql3);
                rs = ps.executeQuery();

                while (rs.next()) {

                    String adm = rs.getString("AdmissionNumber");


                    counter++;
                    Phrase ph = new Phrase(String.valueOf(counter), new Font(Font.FontFamily.HELVETICA, 7, Font.NORMAL, BaseColor.BLACK));
                    PdfPCell cell = new PdfPCell(ph);

                    cell.setVerticalAlignment(Element.ALIGN_BOTTOM);
                    cell.setHorizontalAlignment(Element.ALIGN_MIDDLE);
                    Phrase ph1 = new Phrase(adm, new Font(Font.FontFamily.HELVETICA, 7, Font.NORMAL, BaseColor.BLACK));
                    PdfPCell cell1 = new PdfPCell(ph1);

                    cell1.setColspan(2);
                    Phrase ph2 = new Phrase(classname + " " + stream, new Font(Font.FontFamily.HELVETICA, 9, Font.NORMAL, BaseColor.BLACK));
                    PdfPCell cell2 = new PdfPCell(ph2);
                    cell2.setColspan(3);

                    Phrase ph3 = new Phrase(rs.getString("firstName") + "    " + rs.getString("Middlename") + "    " + rs.getString("LastName"), new Font(Font.FontFamily.HELVETICA, 9, Font.NORMAL, BaseColor.BLACK));
                    PdfPCell cell3 = new PdfPCell(ph3);
                    cell3.setColspan(6);
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

            }

            tabb.setHeaderRows(1);
            doc.add(tabb);
            doc.close();


            if (ConfigurationIntialiser.docOpener()) {
                if (dialog == null) {


                    dialog = ReportFrame.getReportFrameInstance(new File("C:\\schoolData\\SubjectClasslist.pdf"));


                } else {
                    dialog = null;
                    dialog = ReportFrame.getReportFrameInstance(new File("C:\\schoolData\\SubjectClasslist.pdf"));
                    dialog.setVisible(true);


                }

            } else {

                Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler " + "C:\\schoolData\\SubjectClasslist.pdf");

            }


        } catch (Exception sq) {

            sq.printStackTrace();
            JOptionPane.showMessageDialog(null, sq.getMessage());
        }


    }


    public static void subjectVap(String examcode, String examName, String term, String academicYear, String classname, String stream, String subjectcode) {

        try {

            con = DbConnection.connectDb();

            Document doc = new Document();
            com.itextpdf.text.Image image = com.itextpdf.text.Image.getInstance("C:/schooldata/logo.jpg");
            PdfWriter writer = PdfWriter.getInstance(doc, new FileOutputStream("C:\\schoolData\\SubjectClasslist.pdf"));
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
            Paragraph pr5 = new Paragraph("Subject Value Addition Progress Report".toUpperCase(), FontFactory.getFont(FontFactory.HELVETICA, 13, java.awt.Font.BOLD, BaseColor.BLACK));
            Chunk glue = new Chunk(new VerticalPositionMark());
            doc.addCreationDate();
            Paragraph pr7 = new Paragraph("CLASS:  " + classname.toUpperCase() + "                 STREAM: " + stream.toUpperCase(), FontFactory.getFont(FontFactory.TIMES, 13, java.awt.Font.PLAIN, BaseColor.BLACK));
            pr7.add(new Chunk(glue));
            Paragraph pr8 = new Paragraph("Exam Name: " + examName, FontFactory.getFont(FontFactory.TIMES, 13, java.awt.Font.PLAIN, BaseColor.BLACK));

            pr7.add("ACADEMIC YEAR: " + Globals.academicYear());

            pr5.setIndentationLeft(150);
            pr7.setSpacingBefore(30);
            doc.add(pr5);
            doc.add(pr7);
            doc.add(pr8);


            PdfPTable tabb = new PdfPTable(26);
            tabb.setSpacingBefore(10);
            tabb.setWidthPercentage(100);
            {

                Phrase ph = new Phrase("No.", new Font(Font.FontFamily.HELVETICA, 7, Font.NORMAL, BaseColor.BLACK));
                PdfPCell cell = new PdfPCell(ph);

                cell.setVerticalAlignment(Element.ALIGN_BOTTOM);
                cell.setHorizontalAlignment(Element.ALIGN_MIDDLE);
                Phrase ph1 = new Phrase("ADM Number", new Font(Font.FontFamily.HELVETICA, 7, Font.NORMAL, BaseColor.BLACK));
                PdfPCell cell1 = new PdfPCell(ph1);
                cell1.setRotation(90);
                cell1.setColspan(2);
                Phrase ph2 = new Phrase("Class & Stream", new Font(Font.FontFamily.HELVETICA, 7, Font.NORMAL, BaseColor.BLACK));
                PdfPCell cell2 = new PdfPCell(ph2);
                cell2.setColspan(3);
                cell2.setRotation(90);
                Phrase ph3 = new Phrase("Name", new Font(Font.FontFamily.HELVETICA, 7, Font.NORMAL, BaseColor.BLACK));
                PdfPCell cell3 = new PdfPCell(ph3);
                cell3.setColspan(8);
                cell3.setVerticalAlignment(Element.ALIGN_BOTTOM);
                cell3.setHorizontalAlignment(Element.ALIGN_MIDDLE);


                Phrase ph4 = new Phrase("Exam Score", new Font(Font.FontFamily.HELVETICA, 7, Font.NORMAL, BaseColor.BLACK));
                PdfPCell cell4 = new PdfPCell(ph4);
                cell4.setRotation(90);
                cell4.setColspan(2);
                Phrase ph5 = new Phrase("Kcpe Score", new Font(Font.FontFamily.HELVETICA, 7, Font.NORMAL, BaseColor.BLACK));
                PdfPCell cell5 = new PdfPCell(ph5);
                cell5.setRotation(90);
                cell5.setColspan(2);

                Phrase ph6 = new Phrase("Vap", new Font(Font.FontFamily.HELVETICA, 7, Font.NORMAL, BaseColor.BLACK));
                PdfPCell cell6 = new PdfPCell(ph6);
                cell6.setRotation(90);
                cell6.setColspan(8);

                tabb.addCell(cell);
                tabb.addCell(cell1);
                tabb.addCell(cell2);
                tabb.addCell(cell3);
                tabb.addCell(cell4);
                tabb.addCell(cell5);
                tabb.addCell(cell6);
            }
            if (stream.equalsIgnoreCase("Overall")) {
                int counter = 0;
                String sql3 = "Select admissionnumber,firstname,middlename,lastname,currentstream,exampercentage,subjectcode from admission,marksTable where  markstable.classcode='" + Globals.classCode(classname) + "' and examcode='" + examcode + "' and subjectcode='" + subjectcode + "' and admissionNumber=admnumber order by admissionNumber";
                ps = con.prepareStatement(sql3);
                rs = ps.executeQuery();

                while (rs.next()) {

                    String adm = rs.getString("AdmissionNumber");


                    counter++;
                    Phrase ph = new Phrase(String.valueOf(counter), new Font(Font.FontFamily.HELVETICA, 7, Font.NORMAL, BaseColor.BLACK));
                    PdfPCell cell = new PdfPCell(ph);

                    cell.setVerticalAlignment(Element.ALIGN_BOTTOM);
                    cell.setHorizontalAlignment(Element.ALIGN_MIDDLE);
                    Phrase ph1 = new Phrase(adm, new Font(Font.FontFamily.HELVETICA, 7, Font.NORMAL, BaseColor.BLACK));
                    PdfPCell cell1 = new PdfPCell(ph1);

                    cell1.setColspan(2);
                    Phrase ph2 = new Phrase(classname + " " + Globals.streamName(rs.getString("CurrentStream")), new Font(Font.FontFamily.HELVETICA, 9, Font.NORMAL, BaseColor.BLACK));
                    PdfPCell cell2 = new PdfPCell(ph2);
                    cell2.setColspan(3);

                    Phrase ph3 = new Phrase(rs.getString("firstName") + "    " + rs.getString("Middlename") + "    " + rs.getString("LastName"), new Font(Font.FontFamily.HELVETICA, 9, Font.NORMAL, BaseColor.BLACK));
                    PdfPCell cell3 = new PdfPCell(ph3);
                    cell3.setColspan(8);
                    cell3.setVerticalAlignment(Element.ALIGN_BOTTOM);
                    cell3.setHorizontalAlignment(Element.ALIGN_MIDDLE);


                    double kcpescore = 0;
                    String diviation = "";
                    String sub = "";
                    double score = rs.getDouble("Exampercentage");
                    String category = "";
                    ps = con.prepareStatement("Select Category from subjects where subjectcode='" + subjectcode + "'");
                    ResultSet rr = ps.executeQuery();
                    if (rr.next()) {
                        category = rr.getString("Category");
                    }


                    if (category.equalsIgnoreCase("Sciences") || category.equalsIgnoreCase("Technical")) {

                        ps = con.prepareStatement("Select science from kcpemarkstable where admnumber='" + adm + "'");
                        rr = ps.executeQuery();
                        if (rr.next()) {
                            kcpescore = rr.getDouble("Science");
                        } else {
                            kcpescore = 0;
                        }
                    } else if (category.equalsIgnoreCase("Languages")) {
                        if (Globals.subjectName(subjectcode).equalsIgnoreCase("Kiswahili")) {
                            ps = con.prepareStatement("Select kiswahili from kcpemarkstable where admnumber='" + adm + "'");
                            rr = ps.executeQuery();
                            if (rr.next()) {
                                kcpescore = rr.getDouble("kiswahili");
                            } else {
                                kcpescore = 0;
                            }

                        } else {

                            ps = con.prepareStatement("Select English from kcpemarkstable where admnumber='" + adm + "'");
                            rr = ps.executeQuery();
                            if (rr.next()) {
                                kcpescore = rr.getDouble("English");
                            } else {
                                kcpescore = 0;
                            }
                        }

                    } else if (category.equalsIgnoreCase("Mathematics")) {

                        ps = con.prepareStatement("Select Mathematics from kcpemarkstable where admnumber='" + adm + "'");
                        rr = ps.executeQuery();
                        if (rr.next()) {
                            kcpescore = rr.getDouble("Mathematics");
                        } else {
                            kcpescore = 0;
                        }
                    } else if (category.equalsIgnoreCase("Humanities")) {

                        ps = con.prepareStatement("Select SocialStudies from kcpemarkstable where admnumber='" + adm + "'");
                        rr = ps.executeQuery();
                        if (rr.next()) {
                            kcpescore = rr.getDouble("SocialStudies");
                        } else {
                            kcpescore = 0;
                        }
                    }
                    if (kcpescore == 0) {
                        diviation = "";
                    } else {
                        diviation = String.valueOf(score - kcpescore);
                    }
                    Phrase ph4 = new Phrase(String.valueOf(score), new Font(Font.FontFamily.HELVETICA, 7, Font.NORMAL, BaseColor.BLACK));
                    PdfPCell cell4 = new PdfPCell(ph4);

                    cell4.setColspan(2);
                    Phrase ph5 = new Phrase(String.valueOf(kcpescore), new Font(Font.FontFamily.HELVETICA, 7, Font.NORMAL, BaseColor.BLACK));
                    PdfPCell cell5 = new PdfPCell(ph5);

                    cell5.setColspan(2);


                    Phrase ph6 = new Phrase(diviation, new Font(Font.FontFamily.HELVETICA, 7, Font.NORMAL, BaseColor.BLACK));
                    PdfPCell cell6 = new PdfPCell(ph6);
                    cell6.setColspan(8);
                    tabb.addCell(cell);
                    tabb.addCell(cell1);
                    tabb.addCell(cell2);
                    tabb.addCell(cell3);
                    tabb.addCell(cell4);
                    tabb.addCell(cell5);
                    tabb.addCell(cell6);


                }
            } else {


                int counter = 0;
                String sql3 = "Select admissionnumber,firstname,middlename,lastname,currentstream,exampercentage,subjectcode from admission,marksTable where  markstable.classcode='" + Globals.classCode(classname) + "' and examcode='" + examcode + "' and subjectcode='" + subjectcode + "' and streamcode='" + Globals.streamcode(stream) + "' and admissionNumber=admnumber order by admissionNumber";
                ps = con.prepareStatement(sql3);
                rs = ps.executeQuery();

                while (rs.next()) {

                    String adm = rs.getString("AdmissionNumber");


                    counter++;
                    Phrase ph = new Phrase(String.valueOf(counter), new Font(Font.FontFamily.HELVETICA, 7, Font.NORMAL, BaseColor.BLACK));
                    PdfPCell cell = new PdfPCell(ph);

                    cell.setVerticalAlignment(Element.ALIGN_BOTTOM);
                    cell.setHorizontalAlignment(Element.ALIGN_MIDDLE);
                    Phrase ph1 = new Phrase(adm, new Font(Font.FontFamily.HELVETICA, 7, Font.NORMAL, BaseColor.BLACK));
                    PdfPCell cell1 = new PdfPCell(ph1);

                    cell1.setColspan(2);
                    Phrase ph2 = new Phrase(classname + " " + Globals.streamName(rs.getString("CurrentStream")), new Font(Font.FontFamily.HELVETICA, 9, Font.NORMAL, BaseColor.BLACK));
                    PdfPCell cell2 = new PdfPCell(ph2);
                    cell2.setColspan(3);

                    Phrase ph3 = new Phrase(rs.getString("firstName") + "    " + rs.getString("Middlename") + "    " + rs.getString("LastName"), new Font(Font.FontFamily.HELVETICA, 9, Font.NORMAL, BaseColor.BLACK));
                    PdfPCell cell3 = new PdfPCell(ph3);
                    cell3.setColspan(8);
                    cell3.setVerticalAlignment(Element.ALIGN_BOTTOM);
                    cell3.setHorizontalAlignment(Element.ALIGN_MIDDLE);


                    double kcpescore = 0;
                    String diviation = "";
                    String sub = "";
                    double score = rs.getDouble("Exampercentage");
                    String category = "";
                    ps = con.prepareStatement("Select Category from subjects where subjectcode='" + subjectcode + "'");
                    ResultSet rr = ps.executeQuery();
                    if (rr.next()) {
                        category = rr.getString("Category");
                    }


                    if (category.equalsIgnoreCase("Sciences") || category.equalsIgnoreCase("Technical")) {

                        ps = con.prepareStatement("Select science from kcpemarkstable where admnumber='" + adm + "'");
                        rr = ps.executeQuery();
                        if (rr.next()) {
                            kcpescore = rr.getDouble("Science");
                        } else {
                            kcpescore = 0;
                        }
                    } else if (category.equalsIgnoreCase("Languages")) {
                        if (Globals.subjectName(subjectcode).equalsIgnoreCase("Kiswahili")) {
                            ps = con.prepareStatement("Select kiswahili from kcpemarkstable where admnumber='" + adm + "'");
                            rr = ps.executeQuery();
                            if (rr.next()) {
                                kcpescore = rr.getDouble("kiswahili");
                            } else {
                                kcpescore = 0;
                            }

                        } else {

                            ps = con.prepareStatement("Select English from kcpemarkstable where admnumber='" + adm + "'");
                            rr = ps.executeQuery();
                            if (rr.next()) {
                                kcpescore = rr.getDouble("English");
                            } else {
                                kcpescore = 0;
                            }
                        }

                    } else if (category.equalsIgnoreCase("Mathematics")) {

                        ps = con.prepareStatement("Select Mathematics from kcpemarkstable where admnumber='" + adm + "'");
                        rr = ps.executeQuery();
                        if (rr.next()) {
                            kcpescore = rr.getDouble("Mathematics");
                        } else {
                            kcpescore = 0;
                        }
                    } else if (category.equalsIgnoreCase("Humanities")) {

                        ps = con.prepareStatement("Select SocialStudies from kcpemarkstable where admnumber='" + adm + "'");
                        rr = ps.executeQuery();
                        if (rr.next()) {
                            kcpescore = rr.getDouble("SocialStudies");
                        } else {
                            kcpescore = 0;
                        }
                    }
                    if (kcpescore == 0) {
                        diviation = "";
                    } else {
                        diviation = String.valueOf(score - kcpescore);
                    }
                    Phrase ph4 = new Phrase(String.valueOf(score), new Font(Font.FontFamily.HELVETICA, 7, Font.NORMAL, BaseColor.BLACK));
                    PdfPCell cell4 = new PdfPCell(ph4);

                    cell4.setColspan(2);
                    Phrase ph5 = new Phrase(String.valueOf(kcpescore), new Font(Font.FontFamily.HELVETICA, 7, Font.NORMAL, BaseColor.BLACK));
                    PdfPCell cell5 = new PdfPCell(ph5);

                    cell5.setColspan(2);


                    Phrase ph6 = new Phrase(diviation, new Font(Font.FontFamily.HELVETICA, 7, Font.NORMAL, BaseColor.BLACK));
                    PdfPCell cell6 = new PdfPCell(ph6);
                    cell6.setColspan(8);
                    tabb.addCell(cell);
                    tabb.addCell(cell1);
                    tabb.addCell(cell2);
                    tabb.addCell(cell3);
                    tabb.addCell(cell4);
                    tabb.addCell(cell5);
                    tabb.addCell(cell6);


                }

            }

            tabb.setHeaderRows(1);
            doc.add(tabb);
            doc.close();


            if (ConfigurationIntialiser.docOpener()) {
                if (dialog == null) {


                    dialog = ReportFrame.getReportFrameInstance(new File("C:\\schoolData\\SubjectClasslist.pdf"));


                } else {
                    dialog = null;
                    dialog = ReportFrame.getReportFrameInstance(new File("C:\\schoolData\\SubjectClasslist.pdf"));
                    dialog.setVisible(true);


                }

            } else {

                Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler " + "C:\\schoolData\\SubjectClasslist.pdf");

            }


        } catch (Exception sq) {

            sq.printStackTrace();
            JOptionPane.showMessageDialog(null, sq.getMessage());
        }


    }

    public static void FeeStatementGenerator(String adm) {
        Document doc = new Document();
        try {
            con = DbConnection.connectDb();

            PdfWriter writer = PdfWriter.getInstance(doc, new FileOutputStream("C:\\schooldata\\" + adm + "Fee Statement" + ".pdf"));
            doc.open();
            // doc.add(new Paragraph("Printed On: "+date));
            onEndPage(writer, doc);

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
            String classadmitted = "", termadmitted = "", currentclass = "";
            ps = con.prepareStatement("Select classcode,TermAdmitted,currentform from admission where admissionNumber='" + adm + "'");
            rs = ps.executeQuery();
            if (rs.next()) {
                classadmitted = rs.getString("classcode");
                currentclass = rs.getString("currentform");
                termadmitted = rs.getString("TermAdmitted");
            }


            int yearofadmission = (Globals.academicYear() - Globals.classPrecisionDeterminer(currentclass)) + 1;
            int termpayerbleamount = 0;
            int yeardiff = (Globals.academicYear() - yearofadmission);


            Paragraph pr = new Paragraph(" Student Fee Statement".toUpperCase(), FontFactory.getFont(FontFactory.TIMES_BOLD, 18, java.awt.Font.BOLD, BaseColor.BLACK));
            pr.setIndentationLeft(150);

            doc.add(pr);

            Paragraph pr1 = new Paragraph("ADMISSION NUMBER:                     ".toUpperCase() + adm, FontFactory.getFont(FontFactory.TIMES, 10, java.awt.Font.BOLD, BaseColor.BLACK));

            doc.add(pr1);
            Paragraph pr4 = new Paragraph("STUDENT NAME:                              ".toUpperCase() + Globals.fullName(adm), FontFactory.getFont(FontFactory.TIMES, 10, java.awt.Font.BOLD, BaseColor.BLACK));
            doc.add(pr4);

            Paragraph pr2 = new Paragraph("YEAR OF ADMISSION:                    ".toUpperCase() + yearofadmission, FontFactory.getFont(FontFactory.TIMES, 10, java.awt.Font.BOLD, BaseColor.BLACK));
            doc.add(pr2);

            Paragraph pr3 = new Paragraph("CURRENT CLASS:                             ".toUpperCase() + Globals.className(currentclass).toString(), FontFactory.getFont(FontFactory.TIMES, 10, java.awt.Font.BOLD, BaseColor.BLACK));
            doc.add(pr3);

            for (int i = 0; i <= yeardiff; ++i) {
//                         if(i>3)
//                         {
//                             break;
//                         }
//                      
                for (int j = 1; j <= 3; j++) {
                    if (i == yeardiff && j > Globals.termPrecisionDeterminer(Globals.currentTerm())) {
                        break;
                    }

                    int cll = (i + 1);

                    if (cll > 4) {
                        cll = 4;
                    }

                    Paragraph pr8 = new Paragraph("Term " + j + "  FORM " + cll + "  Year " + (yearofadmission + i), FontFactory.getFont(FontFactory.COURIER_BOLD, 13, java.awt.Font.ITALIC, BaseColor.BLACK));
                    pr8.setSpacingAfter(10);
                    doc.add(pr8);


                    PdfPTable tabb = new PdfPTable(20);

                    {


                        Phrase ph2 = new Phrase("Date", new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL, BaseColor.BLACK));
                        PdfPCell cell2 = new PdfPCell(ph2);
                        cell2.setColspan(3);

                        Phrase ph3 = new Phrase("Description", new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL, BaseColor.BLACK));
                        PdfPCell cell3 = new PdfPCell(ph3);
                        cell3.setColspan(6);

                        Phrase ph4 = new Phrase("Expected(Debit)", new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL, BaseColor.BLACK));
                        PdfPCell cell4 = new PdfPCell(ph4);
                        cell4.setColspan(4);

                        Phrase ph5 = new Phrase("Paid(Credit)", new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL, BaseColor.BLACK));
                        PdfPCell cell5 = new PdfPCell(ph5);
                        cell5.setColspan(3);

                        Phrase ph6 = new Phrase("Balance", new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL, BaseColor.BLACK));
                        PdfPCell cell6 = new PdfPCell(ph6);
                        cell6.setColspan(4);
                        tabb.addCell(cell2);
                        tabb.addCell(cell3);
                        tabb.addCell(cell4);
                        tabb.addCell(cell5);
                        tabb.addCell(cell6);


                    }
                    String date = "";
                    int yy = (yearofadmission + i);
                    String feeinvoice = "";
                    String term = "Term " + j;
                    String sql = "Select sum(amount),date from payablevoteheadperstudent where admnumber='" + adm + "'  and term='" + term + "' and academicyear='" + yy + "'";
                    ps = con.prepareStatement(sql);
                    rs = ps.executeQuery();
                    if (rs.next()) {
                        termpayerbleamount = (termpayerbleamount + rs.getInt("sum(Amount)"));
                        feeinvoice = rs.getString("sum(Amount)");
                        date = rs.getString("Date");
                    }

                    if (feeinvoice == null) {
                        feeinvoice = "";
                    }

                    {


                        Phrase ph2 = new Phrase(date, new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL, BaseColor.BLACK));
                        PdfPCell cell2 = new PdfPCell(ph2);
                        cell2.setColspan(3);

                        Phrase ph3 = new Phrase("Fee Invoice", new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL, BaseColor.BLACK));
                        PdfPCell cell3 = new PdfPCell(ph3);
                        cell3.setColspan(6);

                        Phrase ph4 = new Phrase(String.valueOf(feeinvoice), new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL, BaseColor.BLACK));
                        PdfPCell cell4 = new PdfPCell(ph4);
                        cell4.setColspan(4);

                        Phrase ph5 = new Phrase("", new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL, BaseColor.BLACK));
                        PdfPCell cell5 = new PdfPCell(ph5);
                        cell5.setColspan(3);

                        Phrase ph6 = new Phrase(String.valueOf(termpayerbleamount), new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL, BaseColor.BLACK));
                        PdfPCell cell6 = new PdfPCell(ph6);
                        cell6.setColspan(4);
                        tabb.addCell(cell2);
                        tabb.addCell(cell3);
                        tabb.addCell(cell4);
                        tabb.addCell(cell5);
                        tabb.addCell(cell6);


                    }

                    String qll = "Select Distinct recieptnumber from reciepts  where admnumber='" + adm + "'  and term='" + "Term " + j + "' and academicyear='" + (yearofadmission + i) + "'";
                    ps = con.prepareStatement(qll);
                    rs = ps.executeQuery();
                    while (rs.next()) {
                        String receiptno = rs.getString("RecieptNumber");
                        ps = con.prepareStatement("Select Sum(amount),date from reciepts  where admnumber='" + adm + "'  and term='" + "Term " + j + "' and academicyear='" + (yearofadmission + i) + "' and RecieptNumber='" + receiptno + "'");
                        ResultSet rx = ps.executeQuery();
                        while (rx.next()) {


                            Phrase ph2 = new Phrase(rx.getString("Date"), new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL, BaseColor.BLACK));
                            PdfPCell cell2 = new PdfPCell(ph2);
                            cell2.setColspan(3);

                            Phrase ph3 = new Phrase("Receipt No. " + receiptno, new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL, BaseColor.BLACK));
                            PdfPCell cell3 = new PdfPCell(ph3);
                            cell3.setColspan(6);

                            Phrase ph4 = new Phrase("", new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL, BaseColor.BLACK));
                            PdfPCell cell4 = new PdfPCell(ph4);
                            cell4.setColspan(4);

                            Phrase ph5 = new Phrase(rx.getString("sum(amount)"), new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL, BaseColor.BLACK));
                            PdfPCell cell5 = new PdfPCell(ph5);
                            cell5.setColspan(3);
                            int bal = (termpayerbleamount - rx.getInt("Sum(Amount)"));
                            termpayerbleamount = bal;
                            Phrase ph6 = new Phrase(String.valueOf(bal), new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL, BaseColor.BLACK));
                            PdfPCell cell6 = new PdfPCell(ph6);
                            cell6.setColspan(4);
                            tabb.addCell(cell2);
                            tabb.addCell(cell3);
                            tabb.addCell(cell4);
                            tabb.addCell(cell5);
                            tabb.addCell(cell6);


                        }


                    }


                    tabb.setWidthPercentage(100);
                    doc.add(tabb);


                }
            }


            SimpleDateFormat fm = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

            Paragraph pr9 = new Paragraph("Balance As Per " + Globals.currentTermName() + "  " + Globals.academicYear() + "     " + fm.format(new Date()) + "    Ksh." + Globals.balanceCalculator(adm) + ".00", FontFactory.getFont(FontFactory.TIMES, 10, java.awt.Font.ITALIC, BaseColor.BLACK));
            pr9.setSpacingAfter(30);
            doc.add(pr9);

            doc.close();
            if (ConfigurationIntialiser.docOpener()) {
                if (dialog == null) {


                    dialog = ReportFrame.getReportFrameInstance(new File("C:\\schooldata\\" + adm + "Fee Statement" + ".pdf"));


                } else {
                    dialog = null;
                    dialog = ReportFrame.getReportFrameInstance(new File("C:\\schooldata\\" + adm + "Fee Statement" + ".pdf"));
                    dialog.setVisible(true);


                }

            } else {

                Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler " + "C:\\schooldata\\" + adm + "Fee Statement" + ".pdf");

            }

        } catch (Exception sq) {
            sq.printStackTrace();

        }


    }

    public static void totalFeeStatementGenerator(String adm) {
        Document doc = new Document();
        try {
            con = DbConnection.connectDb();

            PdfWriter writer = PdfWriter.getInstance(doc, new FileOutputStream("C:\\schooldata\\" + adm + "Fee Statement" + ".pdf"));
            doc.open();
            // doc.add(new Paragraph("Printed On: "+date));
            onEndPage(writer, doc);

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
            String classadmitted = "", termadmitted = "", currentclass = "";
            ps = con.prepareStatement("Select classcode,TermAdmitted,currentform from admission where admissionNumber='" + adm + "'");
            rs = ps.executeQuery();
            if (rs.next()) {
                classadmitted = rs.getString("classcode");
                currentclass = rs.getString("currentform");
                termadmitted = rs.getString("TermAdmitted");
            }


            int yearofadmission = (Globals.academicYear() - Globals.classPrecisionDeterminer(currentclass)) + 1;
            double termpayerbleamount = 0;
            int yeardiff = (Globals.academicYear() - yearofadmission);


            Paragraph pr = new Paragraph("Comprehensive Student Fee Statement".toUpperCase(), FontFactory.getFont(FontFactory.TIMES_BOLD, 18, java.awt.Font.BOLD, BaseColor.BLACK));
            pr.setIndentationLeft(150);

            doc.add(pr);

            Paragraph pr1 = new Paragraph("ADMISSION NUMBER:                     ".toUpperCase() + adm, FontFactory.getFont(FontFactory.TIMES, 10, java.awt.Font.BOLD, BaseColor.BLACK));

            doc.add(pr1);
            Paragraph pr4 = new Paragraph("STUDENT NAME:                              ".toUpperCase() + Globals.fullName(adm), FontFactory.getFont(FontFactory.TIMES, 10, java.awt.Font.BOLD, BaseColor.BLACK));
            doc.add(pr4);

            Paragraph pr2 = new Paragraph("YEAR OF ADMISSION:                    ".toUpperCase() + yearofadmission, FontFactory.getFont(FontFactory.TIMES, 10, java.awt.Font.BOLD, BaseColor.BLACK));
            doc.add(pr2);

            Paragraph pr3 = new Paragraph("CURRENT CLASS:                             ".toUpperCase() + Globals.className(currentclass).toString(), FontFactory.getFont(FontFactory.TIMES, 10, java.awt.Font.BOLD, BaseColor.BLACK));
            doc.add(pr3);

            for (int i = 0; i <= yeardiff; ++i) {
//                         if(i>3)
//                         {
//                             break;
//                         }
//                      
                for (int j = 1; j <= 3; j++) {
                    if (i == yeardiff && j > Globals.termPrecisionDeterminer(Globals.currentTerm())) {
                        break;
                    }

                    int cll = (i + 1);

                    if (cll > 4) {
                        cll = 4;
                    }

                    Paragraph pr8 = new Paragraph("Term " + j + "  FORM " + cll + "  Year " + (yearofadmission + i), FontFactory.getFont(FontFactory.COURIER_BOLD, 13, java.awt.Font.ITALIC, BaseColor.BLACK));
                    pr8.setSpacingAfter(10);
                    doc.add(pr8);


                    PdfPTable tabb = new PdfPTable(20);

                    {


                        Phrase ph2 = new Phrase("Date", new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL, BaseColor.BLACK));
                        PdfPCell cell2 = new PdfPCell(ph2);
                        cell2.setColspan(3);

                        Phrase ph3 = new Phrase("Description", new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL, BaseColor.BLACK));
                        PdfPCell cell3 = new PdfPCell(ph3);
                        cell3.setColspan(6);

                        Phrase ph4 = new Phrase("Expected(Debit)", new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL, BaseColor.BLACK));
                        PdfPCell cell4 = new PdfPCell(ph4);
                        cell4.setColspan(4);

                        Phrase ph5 = new Phrase("Paid(Credit)", new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL, BaseColor.BLACK));
                        PdfPCell cell5 = new PdfPCell(ph5);
                        cell5.setColspan(3);

                        Phrase ph6 = new Phrase("Balance", new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL, BaseColor.BLACK));
                        PdfPCell cell6 = new PdfPCell(ph6);
                        cell6.setColspan(4);
                        tabb.addCell(cell2);
                        tabb.addCell(cell3);
                        tabb.addCell(cell4);
                        tabb.addCell(cell5);
                        tabb.addCell(cell6);


                    }
                    String date = "";
                    int yy = (yearofadmission + i);
                    String feeinvoice = "";
                    String term = "Term " + j;
                    String Desc = "STD AMNT";
                    String sql = "Select amount,date,voteheadid,description from payablevoteheadperstudent where admnumber='" + adm + "'  and term='" + term + "' and academicyear='" + yy + "'";
                    ps = con.prepareStatement(sql);
                    rs = ps.executeQuery();
                    while (rs.next()) {
                        termpayerbleamount = (termpayerbleamount + rs.getDouble("Amount"));
                        feeinvoice = rs.getString("Amount");
                        date = rs.getString("Date");
                        Desc = rs.getString("Description");


                        if (feeinvoice == null) {
                            feeinvoice = "";
                        }
                        if (Desc.equalsIgnoreCase("INV")) {
                            Desc = "STD AMNT";
                        } else {
                            Desc = Desc;
                        }

                        {


                            Phrase ph2 = new Phrase(date, new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL, BaseColor.BLACK));
                            PdfPCell cell2 = new PdfPCell(ph2);
                            cell2.setColspan(3);

                            Phrase ph3 = new Phrase(Globals.VoteHeadName(rs.getString("Voteheadid")) + "   (" + Desc + ")".toUpperCase(), new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL, BaseColor.BLACK));
                            PdfPCell cell3 = new PdfPCell(ph3);
                            cell3.setColspan(6);

                            Phrase ph4 = new Phrase(String.valueOf(feeinvoice), new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL, BaseColor.BLACK));
                            PdfPCell cell4 = new PdfPCell(ph4);
                            cell4.setColspan(4);

                            Phrase ph5 = new Phrase("", new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL, BaseColor.BLACK));
                            PdfPCell cell5 = new PdfPCell(ph5);
                            cell5.setColspan(3);

                            Phrase ph6 = new Phrase(String.valueOf(termpayerbleamount), new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL, BaseColor.BLACK));
                            PdfPCell cell6 = new PdfPCell(ph6);
                            cell6.setColspan(4);
                            tabb.addCell(cell2);
                            tabb.addCell(cell3);
                            tabb.addCell(cell4);
                            tabb.addCell(cell5);
                            tabb.addCell(cell6);

                        }


                    }

                    String qll = "Select Distinct recieptnumber from reciepts  where admnumber='" + adm + "'  and term='" + "Term " + j + "' and academicyear='" + (yearofadmission + i) + "'";
                    ps = con.prepareStatement(qll);
                    rs = ps.executeQuery();
                    while (rs.next()) {
                        String receiptno = rs.getString("RecieptNumber");
                        ps = con.prepareStatement("Select Sum(amount),date from reciepts  where admnumber='" + adm + "'  and term='" + "Term " + j + "' and academicyear='" + (yearofadmission + i) + "' and RecieptNumber='" + receiptno + "'");
                        ResultSet rx = ps.executeQuery();
                        while (rx.next()) {


                            Phrase ph2 = new Phrase(rx.getString("Date"), new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL, BaseColor.BLACK));
                            PdfPCell cell2 = new PdfPCell(ph2);
                            cell2.setColspan(3);

                            Phrase ph3 = new Phrase("Receipt No. " + receiptno, new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL, BaseColor.BLACK));
                            PdfPCell cell3 = new PdfPCell(ph3);
                            cell3.setColspan(6);

                            Phrase ph4 = new Phrase("", new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL, BaseColor.BLACK));
                            PdfPCell cell4 = new PdfPCell(ph4);
                            cell4.setColspan(4);

                            Phrase ph5 = new Phrase(rx.getString("sum(amount)"), new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL, BaseColor.BLACK));
                            PdfPCell cell5 = new PdfPCell(ph5);
                            cell5.setColspan(3);
                            double bal = (termpayerbleamount - rx.getDouble("Sum(Amount)"));
                            termpayerbleamount = bal;
                            Phrase ph6 = new Phrase(String.valueOf(bal), new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL, BaseColor.BLACK));
                            PdfPCell cell6 = new PdfPCell(ph6);
                            cell6.setColspan(4);
                            tabb.addCell(cell2);
                            tabb.addCell(cell3);
                            tabb.addCell(cell4);
                            tabb.addCell(cell5);
                            tabb.addCell(cell6);


                        }


                    }


                    tabb.setWidthPercentage(100);
                    doc.add(tabb);


                }
            }


            SimpleDateFormat fm = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

            Paragraph pr9 = new Paragraph("Balance As Per " + Globals.currentTermName() + "  " + Globals.academicYear() + "     " + fm.format(new Date()) + "    Ksh." + Globals.balanceCalculator(adm) + ".00", FontFactory.getFont(FontFactory.TIMES, 10, java.awt.Font.ITALIC, BaseColor.BLACK));
            pr9.setSpacingAfter(30);
            doc.add(pr9);

            doc.close();
            if (ConfigurationIntialiser.docOpener()) {
                if (dialog == null) {


                    dialog = ReportFrame.getReportFrameInstance(new File("C:\\schooldata\\" + adm + "Fee Statement" + ".pdf"));


                } else {
                    dialog = null;
                    dialog = ReportFrame.getReportFrameInstance(new File("C:\\schooldata\\" + adm + "Fee Statement" + ".pdf"));
                    dialog.setVisible(true);


                }

            } else {

                Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler " + "C:\\schooldata\\" + adm + "Fee Statement" + ".pdf");

            }

        } catch (Exception sq) {
            sq.printStackTrace();

        }


    }

    public static void paymentVocherGenerator(String vocherNumber, String payeeDetails, String adress, String Date, String invoiceNumber, String description, String amount, String paymentMode, String VoteHeadId, String recipientDetails) {
        Document doc = new Document();
        try {


            con = DbConnection.connectDb();
            com.itextpdf.text.Image image = com.itextpdf.text.Image.getInstance("C:\\schooldata\\logo.jpg");
            PdfWriter writer = PdfWriter.getInstance(doc, new FileOutputStream("C:\\schooldata\\" + "PaymentVochure" + vocherNumber + ".pdf"));
            doc.open();
            // doc.add(new Paragraph("Printed On: "+date));
            onEndPage(writer, doc);

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

            Paragraph pra = new Paragraph();
            Chunk chunk1 = new Chunk();
            Chunk chunk = new Chunk("PAYMENT VOCHURE").setUnderline(1, 0);
            Chunk chunk2 = new Chunk();
            pra.add(chunk1);
            pra.add(chunk);
            pra.add(chunk2);
            doc.add(pra);

            Paragraph pr7 = new Paragraph("Payee's Name: ".toUpperCase(), FontFactory.getFont(FontFactory.TIMES, 13, java.awt.Font.PLAIN, BaseColor.BLACK));
            Chunk chunk3 = new Chunk(payeeDetails).setUnderline(1, 0);
            pr7.add(chunk3);
            doc.add(pr7);

            Paragraph pr8 = new Paragraph("Address:  ".toUpperCase(), FontFactory.getFont(FontFactory.TIMES, 13, java.awt.Font.PLAIN, BaseColor.BLACK));
            Chunk chunk4 = new Chunk(adress).setUnderline(1, 0);
            pr8.add(chunk4);
            pr8.setSpacingAfter(20);
            doc.add(pr8);

            PdfPTable tabb = new PdfPTable(24);

            {
                Phrase ph1 = new Phrase("INVOICES/STATEMENTS", new Font(Font.FontFamily.HELVETICA, 7, Font.NORMAL, BaseColor.BLACK));
                PdfPCell cell1 = new PdfPCell(ph1);
                cell1.setColspan(8);
                tabb.addCell(cell1);
                Phrase ph2 = new Phrase("PARTICULARS", new Font(Font.FontFamily.HELVETICA, 7, Font.NORMAL, BaseColor.BLACK));
                PdfPCell cell2 = new PdfPCell(ph2);
                cell2.setRowspan(2);
                cell2.setColspan(10);
                tabb.addCell(cell2);
                Phrase ph3 = new Phrase("AMOUNTS", new Font(Font.FontFamily.HELVETICA, 7, Font.NORMAL, BaseColor.BLACK));
                PdfPCell cell3 = new PdfPCell(ph3);
                cell3.setColspan(6);
                tabb.addCell(cell3);

                Phrase ph4 = new Phrase("Date", new Font(Font.FontFamily.HELVETICA, 9, Font.NORMAL, BaseColor.BLACK));
                PdfPCell cell4 = new PdfPCell(ph4);
                cell4.setColspan(4);
                tabb.addCell(cell4);
                Phrase ph5 = new Phrase("Invoice No.", new Font(Font.FontFamily.HELVETICA, 9, Font.NORMAL, BaseColor.BLACK));
                PdfPCell cell5 = new PdfPCell(ph5);
                cell5.setColspan(4);
                tabb.addCell(cell5);
                Phrase ph6 = new Phrase("KSH.", new Font(Font.FontFamily.HELVETICA, 9, Font.NORMAL, BaseColor.BLACK));
                PdfPCell cell6 = new PdfPCell(ph6);
                cell6.setColspan(3);
                tabb.addCell(cell6);


                Phrase ph7 = new Phrase("Cts", new Font(Font.FontFamily.HELVETICA, 9, Font.NORMAL, BaseColor.BLACK));
                PdfPCell cell7 = new PdfPCell(ph7);
                cell7.setColspan(3);
                tabb.addCell(cell7);

            }

            {


                Phrase ph4 = new Phrase(Date, new Font(Font.FontFamily.HELVETICA, 9, Font.NORMAL, BaseColor.BLACK));
                PdfPCell cell4 = new PdfPCell(ph4);
                cell4.setColspan(4);
                cell4.setMinimumHeight(100);
                tabb.addCell(cell4);
                Phrase ph5 = new Phrase(invoiceNumber, new Font(Font.FontFamily.HELVETICA, 9, Font.NORMAL, BaseColor.BLACK));
                PdfPCell cell5 = new PdfPCell(ph5);
                cell5.setColspan(4);
                tabb.addCell(cell5);

                Phrase ph6a = new Phrase(description, new Font(Font.FontFamily.HELVETICA, 9, Font.NORMAL, BaseColor.BLACK));
                PdfPCell cell6a = new PdfPCell(ph6a);
                cell6a.setColspan(10);
                tabb.addCell(cell6a);


                Phrase ph6 = new Phrase(amount, new Font(Font.FontFamily.HELVETICA, 9, Font.NORMAL, BaseColor.BLACK));
                PdfPCell cell6 = new PdfPCell(ph6);
                cell6.setColspan(3);
                tabb.addCell(cell6);


                Phrase ph7 = new Phrase("00", new Font(Font.FontFamily.HELVETICA, 9, Font.NORMAL, BaseColor.BLACK));
                PdfPCell cell7 = new PdfPCell(ph7);
                cell7.setColspan(3);
                tabb.addCell(cell7);
            }
            {
                Phrase ph1 = new Phrase("TOTAL", new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL, BaseColor.BLACK));
                PdfPCell cell1 = new PdfPCell(ph1);
                cell1.setColspan(18);
                tabb.addCell(cell1);
                Phrase ph2 = new Phrase(amount, new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL, BaseColor.BLACK));
                PdfPCell cell2 = new PdfPCell(ph2);

                cell2.setColspan(3);
                tabb.addCell(cell2);
                Phrase ph3 = new Phrase("00", new Font(Font.FontFamily.HELVETICA, 7, Font.NORMAL, BaseColor.BLACK));
                PdfPCell cell3 = new PdfPCell(ph3);
                cell3.setColspan(3);
                tabb.addCell(cell3);

            }


            tabb.setWidthPercentage(100);
            doc.add(tabb);
            Paragraph pr1 = new Paragraph("CASH/CHEQUE...." + paymentMode, FontFactory.getFont(FontFactory.TIMES, 13, java.awt.Font.PLAIN, BaseColor.BLACK));
            pr1.setSpacingAfter(20);
            doc.add(pr1);
            Paragraph pr2 = new Paragraph("Payment Authorised By:", FontFactory.getFont(FontFactory.TIMES, 9, java.awt.Font.PLAIN, BaseColor.BLACK));
            doc.add(pr2);

            Paragraph pr3 = new Paragraph("Principal:.............................", FontFactory.getFont(FontFactory.TIMES, 9, java.awt.Font.PLAIN, BaseColor.BLACK));
            Chunk chunk3a = new Chunk("Clerk/Secreatary.............................");
            pr3.add(chunk3a);
            doc.add(pr3);
            Paragraph pr4 = new Paragraph("Sign........................", FontFactory.getFont(FontFactory.TIMES, 9, java.awt.Font.PLAIN, BaseColor.BLACK));
            Chunk chunk4a = new Chunk("Sign....................................");
            pr4.add(chunk4a);
            doc.add(pr4);
            Paragraph pr5 = new Paragraph("Date.......................", FontFactory.getFont(FontFactory.TIMES, 9, java.awt.Font.PLAIN, BaseColor.BLACK));
            Chunk chunk5a = new Chunk("Date.............................");
            pr5.add(chunk5a);
            doc.add(pr5);


            PdfPTable tab2 = new PdfPTable(25);

            {
                Phrase ph1 = new Phrase("VoteHead Details", new Font(Font.FontFamily.HELVETICA, 9, Font.NORMAL, BaseColor.BLACK));
                PdfPCell cell1 = new PdfPCell(ph1);
                cell1.setColspan(15);
                tab2.addCell(cell1);
                Phrase ph2 = new Phrase("KSH.", new Font(Font.FontFamily.HELVETICA, 9, Font.NORMAL, BaseColor.BLACK));
                PdfPCell cell2 = new PdfPCell(ph2);

                cell2.setColspan(5);
                tab2.addCell(cell2);
                Phrase ph3 = new Phrase("Cts.", new Font(Font.FontFamily.HELVETICA, 9, Font.NORMAL, BaseColor.BLACK));
                PdfPCell cell3 = new PdfPCell(ph3);
                cell3.setColspan(5);
                tab2.addCell(cell3);

            }

            {
                Phrase ph1 = new Phrase(Globals.VoteHeadName(VoteHeadId), new Font(Font.FontFamily.HELVETICA, 9, Font.NORMAL, BaseColor.BLACK));
                PdfPCell cell1 = new PdfPCell(ph1);
                cell1.setColspan(15);
                cell1.setMinimumHeight(100);
                tab2.addCell(cell1);
                Phrase ph2 = new Phrase(amount, new Font(Font.FontFamily.HELVETICA, 9, Font.NORMAL, BaseColor.BLACK));
                PdfPCell cell2 = new PdfPCell(ph2);

                cell2.setColspan(5);
                tab2.addCell(cell2);
                Phrase ph3 = new Phrase("00", new Font(Font.FontFamily.HELVETICA, 9, Font.NORMAL, BaseColor.BLACK));
                PdfPCell cell3 = new PdfPCell(ph3);
                cell3.setColspan(5);
                tab2.addCell(cell3);
            }
            {
                Phrase ph1 = new Phrase("TOTAL", new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL, BaseColor.BLACK));
                PdfPCell cell1 = new PdfPCell(ph1);
                cell1.setColspan(15);
                tab2.addCell(cell1);
                Phrase ph2 = new Phrase(amount, new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL, BaseColor.BLACK));
                PdfPCell cell2 = new PdfPCell(ph2);

                cell2.setColspan(5);
                tab2.addCell(cell2);
                Phrase ph3 = new Phrase("00", new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL, BaseColor.BLACK));
                PdfPCell cell3 = new PdfPCell(ph3);
                cell3.setColspan(5);
                tab2.addCell(cell3);
            }
            tab2.setSpacingBefore(25);
            tab2.setWidthPercentage(100);
            doc.add(tab2);
            Paragraph pr10 = new Paragraph("Reciept For Cash Payment- Recieved The Sum of Ksh. ............................................................................."
                    + "......................................................................................................................................................"
                    + "....................................................................................................................................."
                    + "........................................................................................................................", FontFactory.getFont(FontFactory.TIMES, 9, java.awt.Font.PLAIN, BaseColor.BLACK));

            doc.add(pr10);

            Paragraph pr11 = new Paragraph("Signature of Recipient/Thumb Print ................................", FontFactory.getFont(FontFactory.TIMES, 9, java.awt.Font.PLAIN, BaseColor.BLACK));
            Chunk chunk11 = new Chunk("ID NUMBER: " + recipientDetails);
            pr11.add(chunk11);
            doc.add(pr11);

            doc.close();
            if (ConfigurationIntialiser.docOpener()) {
                if (dialog == null) {


                    dialog = ReportFrame.getReportFrameInstance(new File("C:\\schooldata\\" + "PaymentVochure" + vocherNumber + ".pdf"));


                } else {
                    dialog = null;
                    dialog = ReportFrame.getReportFrameInstance(new File("C:\\schooldata\\" + "PaymentVochure" + vocherNumber + ".pdf"));
                    dialog.setVisible(true);


                }

            } else {

                Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler " + "C:\\schooldata\\" + "PaymentVochure" + vocherNumber + ".pdf");

            }

        } catch (Exception sq) {
            sq.printStackTrace();
        }


    }

    public static void dayAttendance(String classcode, String streamcode, String termcode, String date, String week) {
        try {
            con = DbConnection.connectDb();

            Document doc = new Document();
            com.itextpdf.text.Image image = com.itextpdf.text.Image.getInstance("C:/schooldata/logo.jpg");
            PdfWriter writer = PdfWriter.getInstance(doc, new FileOutputStream("C:\\schoolData\\" + "Day Attendance" + ".pdf"));
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
            Paragraph pr5 = new Paragraph("Day Attendance Preview".toUpperCase(), FontFactory.getFont(FontFactory.HELVETICA, 13, java.awt.Font.BOLD, BaseColor.BLACK));
            Chunk glue = new Chunk(new VerticalPositionMark());
            doc.addCreationDate();
            Paragraph pr7 = new Paragraph("CLASS:  " + Globals.className(classcode) + "                 STREAM: " + Globals.streamName(streamcode).toUpperCase(), FontFactory.getFont(FontFactory.TIMES, 13, java.awt.Font.PLAIN, BaseColor.BLACK));
            pr7.add(new Chunk(glue));
            Paragraph pr8 = new Paragraph("KEY:  X=Present                             0=Absent ", FontFactory.getFont(FontFactory.TIMES, 13, java.awt.Font.PLAIN, BaseColor.BLACK));
            Paragraph pr9 = new Paragraph("Date:  " + date, FontFactory.getFont(FontFactory.TIMES, 13, java.awt.Font.PLAIN, BaseColor.BLACK));
            pr9.add(new Chunk(glue));

            pr9.add("TERM : " + Globals.termname(termcode));
            pr7.add("WEEK: " + week);

            pr7.setSpacingBefore(30);

            doc.add(pr5);
            doc.add(pr7);
            doc.add(pr9);
            doc.add(pr8);
            String className = Globals.className(classcode);
            String streamname = Globals.streamName(streamcode);
            PdfPTable tabb = new PdfPTable(20);
            tabb.setSpacingBefore(10);
            tabb.setWidthPercentage(100);
            {

                Phrase ph = new Phrase("No.", new Font(Font.FontFamily.HELVETICA, 7, Font.NORMAL, BaseColor.BLACK));
                PdfPCell cell = new PdfPCell(ph);

                cell.setVerticalAlignment(Element.ALIGN_BOTTOM);
                cell.setHorizontalAlignment(Element.ALIGN_MIDDLE);
                Phrase ph1 = new Phrase("ADM Number", new Font(Font.FontFamily.HELVETICA, 7, Font.NORMAL, BaseColor.BLACK));
                PdfPCell cell1 = new PdfPCell(ph1);
                cell1.setRotation(90);
                cell1.setColspan(2);
                Phrase ph2 = new Phrase("Class & Stream", new Font(Font.FontFamily.HELVETICA, 7, Font.NORMAL, BaseColor.BLACK));
                PdfPCell cell2 = new PdfPCell(ph2);
                cell2.setColspan(3);
                cell2.setRotation(90);
                Phrase ph3 = new Phrase("Name", new Font(Font.FontFamily.HELVETICA, 7, Font.NORMAL, BaseColor.BLACK));
                PdfPCell cell3 = new PdfPCell(ph3);
                cell3.setColspan(6);
                cell3.setVerticalAlignment(Element.ALIGN_BOTTOM);
                cell3.setHorizontalAlignment(Element.ALIGN_MIDDLE);

                Phrase ph4 = new Phrase("Status", new Font(Font.FontFamily.HELVETICA, 7, Font.NORMAL, BaseColor.BLACK));
                PdfPCell cell4 = new PdfPCell(ph4);
                cell4.setRotation(90);
                cell4.setColspan(2);
                Phrase ph5 = new Phrase("", new Font(Font.FontFamily.HELVETICA, 7, Font.NORMAL, BaseColor.BLACK));
                PdfPCell cell5 = new PdfPCell(ph5);
                cell5.setRotation(90);
                cell5.setColspan(6);

                tabb.addCell(cell);
                tabb.addCell(cell1);
                tabb.addCell(cell2);
                tabb.addCell(cell3);
                tabb.addCell(cell4);
                tabb.addCell(cell5);
            }
            String sql = "Select * from admission where currentform='" + classcode + "' order by admissionnumber";
            int studentcounter = 0;
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                studentcounter++;
                Phrase ph = new Phrase(String.valueOf(studentcounter), new Font(Font.FontFamily.HELVETICA, 7, Font.NORMAL, BaseColor.BLACK));
                PdfPCell cell = new PdfPCell(ph);

                cell.setVerticalAlignment(Element.ALIGN_BOTTOM);
                cell.setHorizontalAlignment(Element.ALIGN_MIDDLE);
                Phrase ph1 = new Phrase(rs.getString("AdmissionNumber"), new Font(Font.FontFamily.HELVETICA, 7, Font.NORMAL, BaseColor.BLACK));
                PdfPCell cell1 = new PdfPCell(ph1);

                cell1.setColspan(2);
                Phrase ph2 = new Phrase(className + "  " + streamname, new Font(Font.FontFamily.HELVETICA, 7, Font.NORMAL, BaseColor.BLACK));
                PdfPCell cell2 = new PdfPCell(ph2);
                cell2.setColspan(3);

                Phrase ph3 = new Phrase(rs.getString("firstName") + "    " + rs.getString("Middlename") + "    " + rs.getString("LastName"), new Font(Font.FontFamily.HELVETICA, 7, Font.NORMAL, BaseColor.BLACK));
                PdfPCell cell3 = new PdfPCell(ph3);
                cell3.setColspan(6);
                cell3.setVerticalAlignment(Element.ALIGN_BOTTOM);
                cell3.setHorizontalAlignment(Element.ALIGN_MIDDLE);
                String adm = rs.getString("AdmissionNumber");
                String state = "";
                String sql2 = "Select status from attendance where admnumber='" + adm + "' and date='" + date + "'";
                ps = con.prepareStatement(sql2);
                ResultSet RS = ps.executeQuery();
                if (RS.next()) {
                    if (RS.getString("Status").equalsIgnoreCase("P")) {
                        state = "X";
                    } else {
                        state = "0";
                    }
                } else {
                    state = "";

                }

                Phrase ph4 = new Phrase(state, new Font(Font.FontFamily.HELVETICA, 7, Font.NORMAL, BaseColor.BLACK));
                PdfPCell cell4 = new PdfPCell(ph4);

                cell4.setColspan(2);
                Phrase ph5 = new Phrase("", new Font(Font.FontFamily.HELVETICA, 7, Font.NORMAL, BaseColor.BLACK));
                PdfPCell cell5 = new PdfPCell(ph5);
                cell5.setRotation(90);
                cell5.setColspan(6);
                tabb.addCell(cell);
                tabb.addCell(cell1);
                tabb.addCell(cell2);
                tabb.addCell(cell3);
                tabb.addCell(cell4);
                tabb.addCell(cell5);

            }

            tabb.setWidthPercentage(100);
            // tabb.setLockedWidth(true);
            doc.add(tabb);

            doc.close();

            if (ConfigurationIntialiser.docOpener()) {
                if (dialog == null) {

                    dialog = ReportFrame.getReportFrameInstance(new File("C:\\schoolData\\" + "Day Attendance" + ".pdf"));

                } else {
                    dialog = null;
                    dialog = ReportFrame.getReportFrameInstance(new File("C:\\schoolData\\" + "Day Attendance" + ".pdf"));
                    dialog.setVisible(true);

                }

            } else {

                Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler " + "C:\\schoolData\\" + "Day Attendance" + ".pdf");

            }

        } catch (Exception sq) {
            sq.printStackTrace();
            JOptionPane.showMessageDialog(null, sq.getMessage());
        }

    }

    public static void indexNumberAssignmentReport(String academicYear) {
        try {
            con = DbConnection.connectDb();

            Document doc = new Document();
            com.itextpdf.text.Image image = com.itextpdf.text.Image.getInstance("C:/schooldata/logo.jpg");
            PdfWriter writer = PdfWriter.getInstance(doc, new FileOutputStream("C:\\schoolData\\" + "Index Numbers Report" + ".pdf"));
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
            Paragraph pr5 = new Paragraph("K.c.s.e index Numbers assignment Report".toUpperCase(), FontFactory.getFont(FontFactory.HELVETICA, 13, java.awt.Font.BOLD, BaseColor.BLACK));
            Chunk glue = new Chunk(new VerticalPositionMark());
            doc.addCreationDate();
            Paragraph pr7 = new Paragraph("CLASS OF  " + academicYear, FontFactory.getFont(FontFactory.TIMES, 13, java.awt.Font.PLAIN, BaseColor.BLACK));


            pr7.setSpacingBefore(15);

            doc.add(pr5);
            doc.add(pr7);


            PdfPTable tabb = new PdfPTable(20);
            tabb.setSpacingBefore(10);
            tabb.setWidthPercentage(100);
            {

                Phrase ph = new Phrase("No.", new Font(Font.FontFamily.HELVETICA, 7, Font.NORMAL, BaseColor.BLACK));
                PdfPCell cell = new PdfPCell(ph);

                cell.setVerticalAlignment(Element.ALIGN_BOTTOM);
                cell.setHorizontalAlignment(Element.ALIGN_MIDDLE);
                Phrase ph1 = new Phrase("ADM Number", new Font(Font.FontFamily.HELVETICA, 7, Font.NORMAL, BaseColor.BLACK));
                PdfPCell cell1 = new PdfPCell(ph1);
                cell1.setRotation(90);
                cell1.setColspan(2);
                Phrase ph2 = new Phrase("K.C.S.E Index No.", new Font(Font.FontFamily.HELVETICA, 7, Font.NORMAL, BaseColor.BLACK));
                PdfPCell cell2 = new PdfPCell(ph2);
                cell2.setColspan(3);
                cell2.setRotation(90);
                Phrase ph3 = new Phrase("Name", new Font(Font.FontFamily.HELVETICA, 7, Font.NORMAL, BaseColor.BLACK));
                PdfPCell cell3 = new PdfPCell(ph3);
                cell3.setColspan(6);
                cell3.setVerticalAlignment(Element.ALIGN_BOTTOM);
                cell3.setHorizontalAlignment(Element.ALIGN_MIDDLE);

                Phrase ph4 = new Phrase("", new Font(Font.FontFamily.HELVETICA, 7, Font.NORMAL, BaseColor.BLACK));
                PdfPCell cell4 = new PdfPCell(ph4);
                cell4.setRotation(90);
                cell4.setColspan(2);
                Phrase ph5 = new Phrase("", new Font(Font.FontFamily.HELVETICA, 7, Font.NORMAL, BaseColor.BLACK));
                PdfPCell cell5 = new PdfPCell(ph5);
                cell5.setRotation(90);
                cell5.setColspan(6);

                tabb.addCell(cell);
                tabb.addCell(cell1);
                tabb.addCell(cell2);
                tabb.addCell(cell3);
                tabb.addCell(cell4);
                tabb.addCell(cell5);
            }


            int counte = 0;
            String sql = "Select * from kcseindexnumbers where academicyear='" + academicYear + "' order by kcseindexNumber";
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery(sql);
            while (rs.next()) {
                counte++;


                Phrase ph = new Phrase(String.valueOf(counte), new Font(Font.FontFamily.HELVETICA, 7, Font.NORMAL, BaseColor.BLACK));
                PdfPCell cell = new PdfPCell(ph);

                cell.setVerticalAlignment(Element.ALIGN_BOTTOM);
                cell.setHorizontalAlignment(Element.ALIGN_MIDDLE);
                Phrase ph1 = new Phrase(rs.getString("AdmNumber"), new Font(Font.FontFamily.HELVETICA, 7, Font.NORMAL, BaseColor.BLACK));
                PdfPCell cell1 = new PdfPCell(ph1);

                cell1.setColspan(2);
                Phrase ph2 = new Phrase(rs.getString("kcseIndexNumber"), new Font(Font.FontFamily.HELVETICA, 7, Font.NORMAL, BaseColor.BLACK));
                PdfPCell cell2 = new PdfPCell(ph2);
                cell2.setColspan(3);

                Phrase ph3 = new Phrase(Globals.fullName(rs.getString("admNumber")), new Font(Font.FontFamily.HELVETICA, 7, Font.NORMAL, BaseColor.BLACK));
                PdfPCell cell3 = new PdfPCell(ph3);
                cell3.setColspan(6);
                cell3.setVerticalAlignment(Element.ALIGN_BOTTOM);
                cell3.setHorizontalAlignment(Element.ALIGN_MIDDLE);

                Phrase ph4 = new Phrase("", new Font(Font.FontFamily.HELVETICA, 7, Font.NORMAL, BaseColor.BLACK));
                PdfPCell cell4 = new PdfPCell(ph4);
                cell4.setRotation(90);
                cell4.setColspan(2);
                Phrase ph5 = new Phrase("", new Font(Font.FontFamily.HELVETICA, 7, Font.NORMAL, BaseColor.BLACK));
                PdfPCell cell5 = new PdfPCell(ph5);
                cell5.setRotation(90);
                cell5.setColspan(6);

                tabb.addCell(cell);
                tabb.addCell(cell1);
                tabb.addCell(cell2);
                tabb.addCell(cell3);
                tabb.addCell(cell4);
                tabb.addCell(cell5);


            }
            tabb.setWidthPercentage(100);
            tabb.setHeaderRows(1);
            doc.add(tabb);
            doc.close();

            if (ConfigurationIntialiser.docOpener()) {
                if (dialog == null) {

                    dialog = ReportFrame.getReportFrameInstance(new File("C:\\schoolData\\" + "Index Numbers Report" + ".pdf"));

                } else {
                    dialog = null;
                    dialog = ReportFrame.getReportFrameInstance(new File("C:\\schoolData\\" + "Index Numbers Report" + ".pdf"));
                    dialog.setVisible(true);

                }

            } else {

                Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler " + "C:\\schoolData\\" + "Index Numbers Report" + ".pdf");

            }

        } catch (Exception e) {
        }
    }


    public static void generateReport(String title, String docName, JTable table1) {
        try {
            String location;


            Document doc = new Document();
            com.itextpdf.text.Image image = com.itextpdf.text.Image.getInstance("C:/schooldata/logo.jpg");
            PdfWriter writer = PdfWriter.getInstance(doc, new FileOutputStream("C:\\schoolData\\" + docName + ".pdf"));
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
            Paragraph pr5 = new Paragraph(title.toUpperCase(), FontFactory.getFont(FontFactory.HELVETICA, 13, java.awt.Font.BOLD, BaseColor.BLACK));

            pr5.setIndentationLeft(150);
            doc.add(pr5);

            PdfPTable tabb = new PdfPTable(table1.getColumnCount());
            tabb.setSpacingBefore(10);
            tabb.setWidthPercentage(100);
            int totalwidth = 0;
            int[] widthLength = new int[table1.getColumnCount()];
            for (int f = 0; f < table1.getColumnCount(); ++f) {

                totalwidth += table1.getColumnModel().getColumn(f).getWidth();

            }

            for (int f = 0; f < table1.getColumnCount(); ++f) {
                int colsize = table1.getColumnModel().getColumn(f).getWidth();
                int itexttablecolsize = (colsize * 550) / totalwidth;

                widthLength[f] = itexttablecolsize;
                Phrase ph = new Phrase(table1.getColumnName(f), new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.NORMAL));


                PdfPCell cell = new PdfPCell(ph);

                cell.setBorderColor(BaseColor.DARK_GRAY);
                cell.setBackgroundColor(BaseColor.YELLOW);
                cell.setFixedHeight(20);

                tabb.addCell(cell);

            }


            for (int y = 0; y < table1.getRowCount(); ++y) {

                for (int w = 0; w < table1.getColumnCount(); w++) {

                    String value = "";
                    if (table1.getValueAt(y, w) == null) {
                        value = "";
                    } else {
                        value = table1.getValueAt(y, w).toString();
                    }
                    Phrase ph = new Phrase(value, new Font(Font.FontFamily.HELVETICA, 7, Font.NORMAL));
                    PdfPCell cell = new PdfPCell(ph);
                    cell.setBorderColor(BaseColor.BLACK);
                    tabb.addCell(cell);

                    // cell.setBorder(0);
                }

            }

            tabb.setWidths(widthLength);
            // tabb.setLockedWidth(true);
            doc.add(tabb);


            doc.close();

            if (ConfigurationIntialiser.docOpener()) {
                if (dialog == null) {


                    dialog = ReportFrame.getReportFrameInstance(new File("C:\\schoolData\\" + docName + ".pdf"));


                } else {
                    dialog = null;
                    dialog = ReportFrame.getReportFrameInstance(new File("C:\\schoolData\\" + docName + ".pdf"));
                    dialog.setVisible(true);


                }

            } else {

                Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler " + "C:\\schoolData\\" + docName + ".pdf");

            }

        } catch (DocumentException | IOException sq) {
            JOptionPane.showMessageDialog(null, sq.getMessage());
        }


    }

    public static void kcseMissingMarks(String year) {
        try {
            Document doc = new Document(PageSize.A4.rotate());
            com.itextpdf.text.Image image = com.itextpdf.text.Image.getInstance("C:/schooldata/logo.jpg");
            PdfWriter writer = PdfWriter.getInstance(doc, new FileOutputStream("C:\\schoolData\\kcsemissingResults" + year + ".pdf"));
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
            Paragraph pr5 = new Paragraph("K.C.S.E Missing  Results Preview".toUpperCase(), FontFactory.getFont(FontFactory.HELVETICA, 13, java.awt.Font.BOLD, BaseColor.BLACK));
            Chunk glue = new Chunk(new VerticalPositionMark());
            doc.addCreationDate();
            Paragraph pr7 = new Paragraph("                  CLASS OF :  " + year, FontFactory.getFont(FontFactory.TIMES, 13, java.awt.Font.PLAIN, BaseColor.BLACK));

            pr7.add(new Chunk("                                                                     KEY: Y= MISSING "));
            pr7.setSpacingBefore(10);

            doc.add(pr5);
            doc.add(pr7);

            con = DbConnection.connectDb();
            int columns = 0;
            String sql = "Select  Count(*) from subjects";
            ps = con.prepareCall(sql);
            rs = ps.executeQuery();
            if (rs.next()) {
                columns = rs.getInt("count(*)");

            }
            columns = 2 * (columns) + 13;
            PdfPTable tabb = new PdfPTable(columns);
            System.err.println(columns);
            tabb.setSpacingBefore(10);
            tabb.setWidthPercentage(100);

            {
                Phrase ph = new Phrase("No.", new Font(Font.FontFamily.HELVETICA, 8, Font.NORMAL, BaseColor.BLACK));
                PdfPCell cell = new PdfPCell(ph);

                cell.setVerticalAlignment(Element.ALIGN_BOTTOM);
                cell.setHorizontalAlignment(Element.ALIGN_MIDDLE);
                Phrase ph1 = new Phrase("ADM NUMBER", new Font(Font.FontFamily.HELVETICA, 8, Font.NORMAL, BaseColor.BLACK));
                PdfPCell cell1 = new PdfPCell(ph1);
                cell1.setRotation(90);
                cell1.setColspan(2);
                Phrase ph2 = new Phrase("KCSE INDEX NO.", new Font(Font.FontFamily.HELVETICA, 8, Font.NORMAL, BaseColor.BLACK));
                PdfPCell cell2 = new PdfPCell(ph2);
                cell2.setColspan(3);
                cell2.setRotation(90);
                Phrase ph3 = new Phrase("NAME", new Font(Font.FontFamily.HELVETICA, 8, Font.NORMAL, BaseColor.BLACK));

                PdfPCell cell3 = new PdfPCell(ph3);
                cell3.setColspan(6);
                cell3.setVerticalAlignment(Element.ALIGN_BOTTOM);
                cell3.setHorizontalAlignment(Element.ALIGN_MIDDLE);

                tabb.addCell(cell);
                tabb.addCell(cell1);
                tabb.addCell(cell2);
                tabb.addCell(cell3);
                String sql2 = "Select  subjectname from subjects order by subjectcode";
                ps = con.prepareCall(sql2);
                rs = ps.executeQuery();
                while (rs.next()) {
                    Phrase ph4 = new Phrase(rs.getString("SubjectName"), new Font(Font.FontFamily.HELVETICA, 8, Font.NORMAL, BaseColor.BLACK));
                    PdfPCell cell4 = new PdfPCell(ph4);
                    cell4.setRotation(90);
                    cell4.setColspan(2);
                    tabb.addCell(cell4);

                }
                Phrase ph4 = new Phrase("Total Entries", new Font(Font.FontFamily.HELVETICA, 7, Font.NORMAL, BaseColor.BLACK));
                PdfPCell cell4 = new PdfPCell(ph4);
                cell4.setRotation(90);
                tabb.addCell(cell4);

            }
            int counter = 0;
            String sqla = "Select * from kcseindexNumbers where academicyear='" + year + "'";
            ps = con.prepareStatement(sqla);
            ResultSet rx = ps.executeQuery();
            while (rx.next()) {

                counter++;
                String adm = rx.getString("Admnumber");
                Phrase ph = new Phrase(String.valueOf(counter), new Font(Font.FontFamily.HELVETICA, 8, Font.NORMAL, BaseColor.BLACK));
                PdfPCell cell = new PdfPCell(ph);

                cell.setVerticalAlignment(Element.ALIGN_BOTTOM);
                cell.setHorizontalAlignment(Element.ALIGN_MIDDLE);
                Phrase ph1 = new Phrase(adm, new Font(Font.FontFamily.HELVETICA, 8, Font.NORMAL, BaseColor.BLACK));
                PdfPCell cell1 = new PdfPCell(ph1);

                cell1.setColspan(2);
                Phrase ph2 = new Phrase(rx.getString("Kcseindexnumber"), new Font(Font.FontFamily.HELVETICA, 8, Font.NORMAL, BaseColor.BLACK));
                PdfPCell cell2 = new PdfPCell(ph2);
                cell2.setColspan(3);

                Phrase ph3 = new Phrase(Globals.fullName(adm), new Font(Font.FontFamily.HELVETICA, 8, Font.NORMAL, BaseColor.BLACK));

                PdfPCell cell3 = new PdfPCell(ph3);
                cell3.setColspan(6);
                cell3.setVerticalAlignment(Element.ALIGN_BOTTOM);
                cell3.setHorizontalAlignment(Element.ALIGN_MIDDLE);

                tabb.addCell(cell);
                tabb.addCell(cell1);
                tabb.addCell(cell2);
                tabb.addCell(cell3);
                int subcounter = 0;
                String sqlc = "Select * from subjects";
                ps = con.prepareStatement(sqlc);
                rs = ps.executeQuery();
                while (rs.next()) {
                    String subcode = rs.getString("Subjectcode");
                    String score = "";
                    String sq1 = "select points,grade from kcsemarks where kcseyear='" + year + "' and subjectcode='" + subcode + "' and admnumber='" + adm + "' order by subjectcode ";
                    ps = con.prepareStatement(sq1);
                    ResultSet rr = ps.executeQuery();
                    if (rr.next()) {
                        subcounter++;
                        score = "";
                    } else {

                        score = "Y";
                    }
                    Phrase ph4 = new Phrase(score, new Font(Font.FontFamily.HELVETICA, 8, Font.NORMAL, BaseColor.BLACK));
                    PdfPCell cell4 = new PdfPCell(ph4);
                    cell4.setColspan(2);
                    tabb.addCell(cell4);

                }

                Phrase ph4 = new Phrase(String.valueOf(subcounter), new Font(Font.FontFamily.HELVETICA, 7, Font.NORMAL, BaseColor.BLACK));
                PdfPCell cell4 = new PdfPCell(ph4);

                tabb.addCell(cell4);
            }
            tabb.setHeaderRows(1);
            doc.add(tabb);
            doc.close();

            if (ConfigurationIntialiser.docOpener()) {
                if (dialog == null) {

                    dialog = ReportFrame.getReportFrameInstance(new File("C:\\schoolData\\kcsemissingResults" + year + ".pdf"));

                } else {
                    dialog = null;
                    dialog = ReportFrame.getReportFrameInstance(new File("C:\\schoolData\\kcsemissingResults" + year + ".pdf"));
                    dialog.setVisible(true);

                }

            } else {

                Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler " + "C:\\schoolData\\kcsemissingResults" + year + ".pdf");

            }

        } catch (Exception sq) {
            sq.printStackTrace();
            JOptionPane.showMessageDialog(null, sq.getMessage());
        }

    }

    public static void kcseMarks(String year, String admissionNumber, boolean analyse) {

        NumberFormat nf2 = NumberFormat.getInstance();
        nf2.setMaximumFractionDigits(0);
        nf2.setMinimumFractionDigits(0);
        try {
            Document doc = new Document(PageSize.A4.rotate());
            com.itextpdf.text.Image image = com.itextpdf.text.Image.getInstance("C:/schooldata/logo.jpg");
            PdfWriter writer = PdfWriter.getInstance(doc, new FileOutputStream("C:\\schoolData\\kcse" + year + ".pdf"));
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
            Paragraph pr5 = new Paragraph("K.C.S.E Results".toUpperCase(), FontFactory.getFont(FontFactory.HELVETICA, 13, java.awt.Font.BOLD, BaseColor.BLACK));
            Chunk glue = new Chunk(new VerticalPositionMark());
            doc.addCreationDate();
            Paragraph pr7 = new Paragraph("                  CLASS OF :  " + year, FontFactory.getFont(FontFactory.TIMES, 13, java.awt.Font.PLAIN, BaseColor.BLACK));

            pr7.setSpacingBefore(10);

            doc.add(pr5);
            doc.add(pr7);

            con = DbConnection.connectDb();
            int columns = 0;
            String sql = "Select  Count(*) from subjects";
            ps = con.prepareCall(sql);
            rs = ps.executeQuery();
            if (rs.next()) {
                columns = rs.getInt("count(*)");

            }
            columns = 2 * (columns) + 17;
            PdfPTable tabb = new PdfPTable(columns);

            tabb.setSpacingBefore(10);
            tabb.setWidthPercentage(100);

            {
                Phrase ph = new Phrase("No.", new Font(Font.FontFamily.HELVETICA, 8, Font.NORMAL, BaseColor.BLACK));
                PdfPCell cell = new PdfPCell(ph);

                cell.setVerticalAlignment(Element.ALIGN_BOTTOM);
                cell.setHorizontalAlignment(Element.ALIGN_MIDDLE);
                Phrase ph1 = new Phrase("ADM NUMBER", new Font(Font.FontFamily.HELVETICA, 8, Font.NORMAL, BaseColor.BLACK));
                PdfPCell cell1 = new PdfPCell(ph1);
                cell1.setRotation(90);
                cell1.setColspan(2);
                Phrase ph2 = new Phrase("KCSE INDEX NO.", new Font(Font.FontFamily.HELVETICA, 8, Font.NORMAL, BaseColor.BLACK));
                PdfPCell cell2 = new PdfPCell(ph2);
                cell2.setColspan(3);
                cell2.setRotation(90);
                Phrase ph3 = new Phrase("NAME", new Font(Font.FontFamily.HELVETICA, 8, Font.NORMAL, BaseColor.BLACK));

                PdfPCell cell3 = new PdfPCell(ph3);
                cell3.setColspan(6);
                cell3.setVerticalAlignment(Element.ALIGN_BOTTOM);
                cell3.setHorizontalAlignment(Element.ALIGN_MIDDLE);

                tabb.addCell(cell);
                tabb.addCell(cell1);
                tabb.addCell(cell2);
                tabb.addCell(cell3);
                String sql2 = "Select  subjectname from subjects order by subjectcode";
                ps = con.prepareCall(sql2);
                rs = ps.executeQuery();
                while (rs.next()) {
                    Phrase ph4 = new Phrase(rs.getString("SubjectName"), new Font(Font.FontFamily.HELVETICA, 8, Font.NORMAL, BaseColor.BLACK));
                    PdfPCell cell4 = new PdfPCell(ph4);
                    cell4.setRotation(90);
                    cell4.setColspan(2);
                    tabb.addCell(cell4);

                }
                Phrase ph4 = new Phrase("Total Points", new Font(Font.FontFamily.HELVETICA, 7, Font.NORMAL, BaseColor.BLACK));
                PdfPCell cell4 = new PdfPCell(ph4);
                cell4.setRotation(90);
                tabb.addCell(cell4);

                Phrase ph5 = new Phrase("AVG(POINTS)", new Font(Font.FontFamily.HELVETICA, 7, Font.NORMAL, BaseColor.BLACK));
                PdfPCell cell5 = new PdfPCell(ph5);
                cell5.setRotation(90);
                Phrase ph6 = new Phrase("MEAN GRADE", new Font(Font.FontFamily.HELVETICA, 7, Font.NORMAL, BaseColor.BLACK));
                PdfPCell cell6 = new PdfPCell(ph6);
                cell6.setRotation(90);
                Phrase ph6a = new Phrase("POSITION", new Font(Font.FontFamily.HELVETICA, 7, Font.NORMAL, BaseColor.BLACK));
                PdfPCell cell6a = new PdfPCell(ph6a);
                cell6a.setRotation(90);
                Phrase ph7 = new Phrase("POSITION OUT OF", new Font(Font.FontFamily.HELVETICA, 7, Font.NORMAL, BaseColor.BLACK));
                PdfPCell cell7 = new PdfPCell(ph7);
                cell7.setRotation(90);
                tabb.addCell(cell5);
                tabb.addCell(cell6);
                tabb.addCell(cell6a);
                tabb.addCell(cell7);
            }
            if (analyse) {

                String sqlstart = "select admnumber from kcsemarks where kcseyear='" + year + "'  group by admnumber";
                ps = con.prepareStatement(sqlstart);
                rs = ps.executeQuery();
                while (rs.next()) {

                    String adm = rs.getString("admnumber");
                    int subtp1 = 0, subtp2 = 0, subtp3 = 0, subtp4 = 0;


                    double totalpoint = 0;
                    double totalmarks = 0;
                    int totalsubject = 0;

                    String codes[] = new String[5];
                    int codepointer = 0;
                    int sub = 1;
                    String sqll = "select points,kcsemarks.subjectcode from kcsemarks,subjects where  kcseyear='" + year + "' and admnumber='" + adm + "' and subjects.subjectcode=kcsemarks.subjectcode and category='" + "languages" + "' ";
                    ps = con.prepareStatement(sqll);
                    ResultSet rx = ps.executeQuery();
                    while (rx.next()) {
                        subtp1 += rx.getInt("points");
                        codes[codepointer] = rx.getString("SubjectCode");

                        codepointer++;
                        sub++;

                        if (sub > 2) {
                            break;
                        }

                    }
                    int subcounter = 1;
                    String sqly = "select points,kcsemarks.subjectcode from kcsemarks,subjects where  kcseyear='" + year + "' and admnumber='" + adm + "' and subjects.subjectcode=kcsemarks.subjectcode and category='" + "sciences" + "' order by points desc";
                    ps = con.prepareStatement(sqly);
                    rx = ps.executeQuery();
                    while (rx.next()) {
                        if (subcounter > 2) {
                            break;
                        }

                        codes[codepointer] = rx.getString("SubjectCode");
                        codepointer++;
                        subtp2 += rx.getInt("points");

                        subcounter++;
                    }

                    String sqlx = "select points,kcsemarks.subjectcode from kcsemarks,subjects where kcseyear='" + year + "' and admnumber='" + adm + "' and subjects.subjectcode=kcsemarks.subjectcode and category='" + "Mathematics" + "' order by points desc";
                    ps = con.prepareStatement(sqlx);
                    rx = ps.executeQuery();
                    while (rx.next()) {
                        codes[codepointer] = rx.getString("SubjectCode");
                        codepointer++;

                        subtp4 += rx.getInt("points");

                        break;
                    }
                    sub = 1;
                    String sqlz = "select points,kcsemarks.subjectcode from kcsemarks,subjects where  kcseyear='" + year + "' and admnumber='" + adm + "' and subjects.subjectcode=kcsemarks.subjectcode and kcsemarks.subjectcode!='" + codes[0] + "' and kcsemarks.subjectcode!='" + codes[1] + "' and kcsemarks.subjectcode!='" + codes[2] + "' and kcsemarks.subjectcode!='" + codes[3] + "' and kcsemarks.subjectcode!='" + codes[4] + "' order by points desc";
                    ps = con.prepareStatement(sqlz);
                    rx = ps.executeQuery();
                    while (rx.next()) {

                        subtp3 += rx.getInt("points");


                        sub++;
                        if (sub > 2) {
                            break;
                        }
                    }

//                String sqlxx = "select points from kcsemarks,subjects where kcseyear='" + year + "' and admnumber='" + adm + "' and subjects.subjectcode=kcsemarks.subjectcode and category='" + "mathematics" + "' order by points desc";
//                ps = con.prepareStatement(sqlxx);
//                rx = ps.executeQuery();
//                while (rx.next()) {
//
//                    totalpoint += rx.getInt("points");
//
//                    break;
//                }
//                
                    totalpoint += subtp1 + subtp2 + subtp3 + subtp4;

                    totalsubject = 7;

                    String grade = "";

                    int point = Integer.valueOf(nf2.format((totalpoint / totalsubject)));

                    String sqla = "Select grade from points_for_each_grade where points='" + point + "' ";
                    ps = con.prepareStatement(sqla);
                    rx = ps.executeQuery();
                    if (rx.next()) {
                        grade = rx.getString("grade");
                    }

                    double avgp = (totalpoint / totalsubject);
                    nf.setMaximumFractionDigits(2);

                    String sql3 = "Update kcsemarks set totalpoints='" + totalpoint + "',meanpoints='" + nf.format(avgp) + "',meangrade='" + grade + "'  where  kcseyear='" + year + "' and admnumber='" + adm + "' ";
                    ps = con.prepareStatement(sql3);
                    ps.execute();
                }
                int count = 0;
                int previousscore = 0;
                int tiechecker = 0;
                String sqlxx = "select totalpoints,admnumber from kcsemarks where kcseyear='" + year + "' group by admnumber order by totalpoints desc";
                ps = con.prepareStatement(sqlxx);
                ResultSet rx = ps.executeQuery();
                while (rx.next()) {

                    if (previousscore == rx.getInt("Totalpoints")) {
                        tiechecker++;

                    } else {

                        count++;
                        count = tiechecker + count;
                        tiechecker = 0;
                    }


                    String adm = rx.getString("AdmNumber");

                    String sql3 = "Update kcsemarks set position='" + count + "'  where  kcseyear='" + year + "' and admnumber='" + adm + "' ";
                    ps = con.prepareStatement(sql3);
                    ps.execute();
                    previousscore = rx.getInt("Totalpoints");

                }
                String sql3 = "Update kcsemarks set positionoutof='" + count + "'  where  kcseyear='" + year + "' ";
                ps = con.prepareStatement(sql3);
                ps.execute();


            }

            int count = 0;

            int counter = 0;
            ResultSet rx;

            if (admissionNumber.equalsIgnoreCase("All")) {

                String sqlxxa = "select * from kcsemarks where kcseyear='" + year + "' group by KCSEIndexNumber order by totalpoints desc";
                ps = con.prepareStatement(sqlxxa);
                rx = ps.executeQuery();
                while (rx.next()) {

                    counter++;
                    String adm = rx.getString("Admnumber");
                    Phrase ph = new Phrase(String.valueOf(counter), new Font(Font.FontFamily.HELVETICA, 8, Font.NORMAL, BaseColor.BLACK));
                    PdfPCell cell = new PdfPCell(ph);

                    cell.setVerticalAlignment(Element.ALIGN_BOTTOM);
                    cell.setHorizontalAlignment(Element.ALIGN_MIDDLE);
                    Phrase ph1 = new Phrase(adm, new Font(Font.FontFamily.HELVETICA, 8, Font.NORMAL, BaseColor.BLACK));
                    PdfPCell cell1 = new PdfPCell(ph1);

                    cell1.setColspan(2);
                    Phrase ph2 = new Phrase(rx.getString("Kcseindexnumber"), new Font(Font.FontFamily.HELVETICA, 8, Font.NORMAL, BaseColor.BLACK));
                    PdfPCell cell2 = new PdfPCell(ph2);
                    cell2.setColspan(3);

                    Phrase ph3 = new Phrase(Globals.fullName(adm), new Font(Font.FontFamily.HELVETICA, 8, Font.NORMAL, BaseColor.BLACK));

                    PdfPCell cell3 = new PdfPCell(ph3);
                    cell3.setColspan(6);
                    cell3.setVerticalAlignment(Element.ALIGN_BOTTOM);
                    cell3.setHorizontalAlignment(Element.ALIGN_MIDDLE);

                    tabb.addCell(cell);
                    tabb.addCell(cell1);
                    tabb.addCell(cell2);
                    tabb.addCell(cell3);

                    String sqlc = "Select * from subjects order by subjectcode";
                    ps = con.prepareStatement(sqlc);
                    rs = ps.executeQuery();
                    while (rs.next()) {
                        String subcode = rs.getString("Subjectcode");
                        String score = "", gr = "";
                        String sq = "select points,grade from kcsemarks where kcseyear='" + year + "' and subjectcode='" + subcode + "' and admnumber='" + adm + "'  ";
                        ps = con.prepareStatement(sq);
                        ResultSet rr = ps.executeQuery();
                        if (rr.next()) {
                            score = rr.getString("points");
                            gr = rr.getString("grade");
                        } else {
                            gr = "";
                            score = "";
                        }
                        Phrase ph4 = new Phrase(score, new Font(Font.FontFamily.HELVETICA, 8, Font.NORMAL, BaseColor.BLACK));
                        PdfPCell cell4 = new PdfPCell(ph4);
                        Phrase ph4a = new Phrase(gr, new Font(Font.FontFamily.HELVETICA, 8, Font.NORMAL, BaseColor.BLACK));
                        PdfPCell cell4a = new PdfPCell(ph4a);
                        tabb.addCell(cell4);
                        tabb.addCell(cell4a);

                    }

                    Phrase ph4 = new Phrase(rx.getString("Totalpoints"), new Font(Font.FontFamily.HELVETICA, 7, Font.NORMAL, BaseColor.BLACK));
                    PdfPCell cell4 = new PdfPCell(ph4);

                    tabb.addCell(cell4);

                    Phrase ph5 = new Phrase(rx.getString("meanpoints"), new Font(Font.FontFamily.HELVETICA, 7, Font.NORMAL, BaseColor.BLACK));
                    PdfPCell cell5 = new PdfPCell(ph5);

                    Phrase ph6 = new Phrase(rx.getString("meangrade"), new Font(Font.FontFamily.HELVETICA, 7, Font.NORMAL, BaseColor.BLACK));
                    PdfPCell cell6 = new PdfPCell(ph6);

                    Phrase ph6a = new Phrase(rx.getString("position"), new Font(Font.FontFamily.HELVETICA, 7, Font.NORMAL, BaseColor.BLACK));
                    PdfPCell cell6a = new PdfPCell(ph6a);

                    Phrase ph7 = new Phrase(rx.getString("positionoutof"), new Font(Font.FontFamily.HELVETICA, 7, Font.NORMAL, BaseColor.BLACK));
                    PdfPCell cell7 = new PdfPCell(ph7);

                    tabb.addCell(cell5);
                    tabb.addCell(cell6);
                    tabb.addCell(cell6a);
                    tabb.addCell(cell7);

                }

                tabb.setHeaderRows(1);
                doc.add(tabb);

                PdfPTable tab2 = new PdfPTable(22);
                {/// meangrade table headers
                    Phrase ph1a = new Phrase("Overall Mean Grade Distribution".toUpperCase(), new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL, BaseColor.BLACK));
                    PdfPCell cell1a = new PdfPCell(ph1a);
                    cell1a.setColspan(22);
                    cell1a.setVerticalAlignment(Element.ALIGN_BOTTOM);
                    cell1a.setHorizontalAlignment(Element.ALIGN_MIDDLE);
                    tab2.addCell(cell1a);

                    Phrase ph1 = new Phrase("GRADES", new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL, BaseColor.BLACK));
                    PdfPCell cell1 = new PdfPCell(ph1);
                    cell1.setColspan(4);
                    cell1.setVerticalAlignment(Element.ALIGN_BOTTOM);
                    cell1.setHorizontalAlignment(Element.ALIGN_MIDDLE);
                    tab2.addCell(cell1);

                    String sql2 = "Select * from points_for_each_grade order by points desc";
                    ps = con.prepareStatement(sql2);
                    rs = ps.executeQuery();
                    while (rs.next()) {

                        Phrase ph3 = new Phrase(rs.getString("grade"), new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL, BaseColor.BLACK));
                        PdfPCell cell3 = new PdfPCell(ph3);
                        cell3.setVerticalAlignment(Element.ALIGN_BOTTOM);
                        cell3.setHorizontalAlignment(Element.ALIGN_MIDDLE);

                        tab2.addCell(cell3);
                    }

                    Phrase ph4 = new Phrase("Entries", new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL, BaseColor.BLACK));
                    PdfPCell cell4 = new PdfPCell(ph4);
                    cell4.setColspan(2);
                    cell4.setRotation(90);
                    tab2.addCell(cell4);

                    Phrase ph5 = new Phrase("Mean Score", new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL, BaseColor.BLACK));
                    PdfPCell cell5 = new PdfPCell(ph5);
                    cell5.setColspan(2);
                    cell5.setRotation(90);
                    tab2.addCell(cell5);

                    Phrase ph6 = new Phrase("Mean Grade", new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL, BaseColor.BLACK));
                    PdfPCell cell6 = new PdfPCell(ph6);
                    cell6.setRotation(90);
                    cell6.setColspan(2);

                    tab2.addCell(cell6);

                }

                {
                    // Real Distribution from the database overall

                    Phrase ph2 = new Phrase("GRADE FREQUENCIES".toUpperCase(), new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL, BaseColor.BLACK));
                    PdfPCell cell2 = new PdfPCell(ph2);
                    cell2.setColspan(4);
                    cell2.setVerticalAlignment(Element.ALIGN_BOTTOM);
                    cell2.setHorizontalAlignment(Element.ALIGN_MIDDLE);
                    tab2.addCell(cell2);
                    int entryCounter = 0, studentCounter = 0;
                    double schoolTotal = 0.0;
                    String sqlc = "Select * from points_for_each_grade order by points desc";
                    ps = con.prepareStatement(sqlc);
                    rs = ps.executeQuery();
                    while (rs.next()) {
                        int gradecounter = 0;

                        String score = "", grade = rs.getString("Grade");
                        String sq = "select meangrade,meanpoints from kcsemarks where kcseyear='" + year + "'  and meangrade='" + grade + "' group by admNumber";
                        ps = con.prepareStatement(sq);
                        ResultSet rr = ps.executeQuery();
                        while (rr.next()) {
                            gradecounter++;
                            schoolTotal = Globals.point(rr.getString("meangrade")) + schoolTotal;
                            studentCounter++;
                        }
                        Phrase ph4 = new Phrase(String.valueOf(gradecounter), new Font(Font.FontFamily.HELVETICA, 8, Font.NORMAL, BaseColor.BLACK));
                        PdfPCell cell4 = new PdfPCell(ph4);

                        tab2.addCell(cell4);

                    }

                    Phrase ph4 = new Phrase(String.valueOf(studentCounter), new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL, BaseColor.BLACK));
                    PdfPCell cell4 = new PdfPCell(ph4);
                    cell4.setColspan(2);

                    tab2.addCell(cell4);

                    Phrase ph5 = new Phrase(String.valueOf(nf.format(schoolTotal / studentCounter)), new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL, BaseColor.BLACK));
                    PdfPCell cell5 = new PdfPCell(ph5);
                    cell5.setColspan(2);

                    tab2.addCell(cell5);
                    nf.setMaximumFractionDigits(0);
                    Phrase ph6 = new Phrase(Globals.grade(String.valueOf(nf.format(schoolTotal / studentCounter))), new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL, BaseColor.BLACK));
                    PdfPCell cell6 = new PdfPCell(ph6);

                    cell6.setColspan(2);

                    tab2.addCell(cell6);

                }

                {
                    // Real Distribution from the database gender wise

                    Phrase ph1a = new Phrase("Mean Grade Distribution By Gender".toUpperCase(), new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL, BaseColor.BLACK));
                    PdfPCell cell1a = new PdfPCell(ph1a);
                    cell1a.setColspan(22);
                    cell1a.setVerticalAlignment(Element.ALIGN_BOTTOM);
                    cell1a.setHorizontalAlignment(Element.ALIGN_MIDDLE);
                    tab2.addCell(cell1a);

                    for (int i = 0; i < 2; i++) {
                        String gender, sex;
                        if (i == 0) {
                            gender = "Male";
                            sex = "BOYS";
                        } else {
                            gender = "Female";
                            sex = "GIRLS";
                        }

                        Phrase ph2 = new Phrase("GRADE FREQUENCIES  " + sex.toUpperCase(), new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL, BaseColor.BLACK));
                        PdfPCell cell2 = new PdfPCell(ph2);
                        cell2.setColspan(4);
                        cell2.setVerticalAlignment(Element.ALIGN_BOTTOM);
                        cell2.setHorizontalAlignment(Element.ALIGN_MIDDLE);
                        tab2.addCell(cell2);
                        int entryCounter = 0, studentCounter = 0;
                        double schoolTotal = 0.0;
                        String sqlc = "Select * from points_for_each_grade order by points desc";
                        ps = con.prepareStatement(sqlc);
                        rs = ps.executeQuery();
                        while (rs.next()) {
                            int gradecounter = 0;

                            String score = "", grade = rs.getString("Grade");
                            String sq = "select meangrade,meanpoints from kcsemarks,admission where admnumber=admissionNumber and gender='" + gender + "' and kcseyear='" + year + "'  and meangrade='" + grade + "' group by admNumber";
                            ps = con.prepareStatement(sq);
                            ResultSet rr = ps.executeQuery();
                            while (rr.next()) {
                                gradecounter++;
                                schoolTotal = Globals.point(rr.getString("meangrade")) + schoolTotal;
                                studentCounter++;
                            }
                            Phrase ph4 = new Phrase(String.valueOf(gradecounter), new Font(Font.FontFamily.HELVETICA, 8, Font.NORMAL, BaseColor.BLACK));
                            PdfPCell cell4 = new PdfPCell(ph4);

                            tab2.addCell(cell4);

                        }

                        Phrase ph4 = new Phrase(String.valueOf(studentCounter), new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL, BaseColor.BLACK));
                        PdfPCell cell4 = new PdfPCell(ph4);
                        cell4.setColspan(2);

                        tab2.addCell(cell4);
                        nf.setMaximumFractionDigits(2);
                        Phrase ph5 = new Phrase(String.valueOf(nf.format(schoolTotal / studentCounter)), new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL, BaseColor.BLACK));
                        PdfPCell cell5 = new PdfPCell(ph5);
                        cell5.setColspan(2);

                        tab2.addCell(cell5);
                        nf.setMaximumFractionDigits(0);
                        Phrase ph6 = new Phrase(Globals.grade(String.valueOf(nf.format(schoolTotal / studentCounter))), new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL, BaseColor.BLACK));
                        PdfPCell cell6 = new PdfPCell(ph6);

                        cell6.setColspan(2);

                        tab2.addCell(cell6);

                    }

                }

                tab2.setSpacingBefore(20);
                tab2.setWidthPercentage(100);
                doc.add(tab2);

                PdfPTable tab3 = new PdfPTable(24);
                {
                    //subject grade distribution table headers

                    Phrase ph2 = new Phrase("Subject".toUpperCase(), new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL, BaseColor.BLACK));
                    PdfPCell cell2 = new PdfPCell(ph2);
                    cell2.setColspan(6);
                    cell2.setVerticalAlignment(Element.ALIGN_BOTTOM);
                    cell2.setHorizontalAlignment(Element.ALIGN_MIDDLE);
                    tab3.addCell(cell2);

                    String sql2 = "Select * from points_for_each_grade order by points desc";
                    ps = con.prepareStatement(sql2);
                    rs = ps.executeQuery();
                    while (rs.next()) {

                        Phrase ph3 = new Phrase(rs.getString("grade"), new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL, BaseColor.BLACK));
                        PdfPCell cell3 = new PdfPCell(ph3);
                        cell3.setVerticalAlignment(Element.ALIGN_BOTTOM);
                        cell3.setHorizontalAlignment(Element.ALIGN_MIDDLE);

                        tab3.addCell(cell3);
                    }

                    Phrase ph4 = new Phrase("Entries", new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL, BaseColor.BLACK));
                    PdfPCell cell4 = new PdfPCell(ph4);
                    cell4.setColspan(2);
                    cell4.setRotation(90);
                    tab3.addCell(cell4);

                    Phrase ph5 = new Phrase("Mean Score", new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL, BaseColor.BLACK));
                    PdfPCell cell5 = new PdfPCell(ph5);
                    cell5.setColspan(2);
                    cell5.setRotation(90);
                    tab3.addCell(cell5);

                    Phrase ph6 = new Phrase("Mean Grade", new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL, BaseColor.BLACK));
                    PdfPCell cell6 = new PdfPCell(ph6);
                    cell6.setRotation(90);
                    cell6.setColspan(2);

                    tab3.addCell(cell6);

                }

                {///real subject distribution from database overall

                    Phrase ph1 = new Phrase("Overall Subject Distribution".toUpperCase(), new Font(Font.FontFamily.HELVETICA, 14, Font.NORMAL, BaseColor.BLACK));
                    PdfPCell cell1 = new PdfPCell(ph1);
                    cell1.setColspan(24);
                    tab3.addCell(cell1);
                    String SQL = "Select * From subjects";
                    ps = con.prepareStatement(sql);
                    rx = ps.executeQuery();
                    while (rx.next()) {
                        String subcode = rx.getString("subjectcode");
                        String subname = rx.getString("SubjectName");
                        {
                            //real distribution from db

                            Phrase ph2 = new Phrase(subname, new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL, BaseColor.BLACK));
                            PdfPCell cell2 = new PdfPCell(ph2);
                            cell2.setColspan(6);

                            tab3.addCell(cell2);
                            int COUNTER = 0, gradepoint = 0;
                            double totalpoints = 0.0;

                            String sql2 = "Select * from points_for_each_grade order by points desc";
                            ps = con.prepareStatement(sql2);
                            rs = ps.executeQuery();
                            while (rs.next()) {
                                String grade = rs.getString("grade");
                                gradepoint = rs.getInt("Points");
                                count = 0;
                                String sql1 = "Select admnumber from kcsemarks where kcseyear='" + year + "' and grade='" + grade + "'  and  subjectcode='" + subcode + "' group by admnumber";
                                ps = con.prepareStatement(sql1);
                                ResultSet rsx = ps.executeQuery();
                                while (rsx.next()) {
                                    totalpoints += gradepoint;
                                    count++;
                                    COUNTER++;
                                }

                                Phrase ph3 = new Phrase(String.valueOf(count), new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL, BaseColor.BLACK));
                                PdfPCell cell3 = new PdfPCell(ph3);

                                tab3.addCell(cell3);
                            }

                            String meangrade = "";
                            Phrase ph4 = new Phrase(String.valueOf(COUNTER), new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL, BaseColor.BLACK));
                            PdfPCell cell4 = new PdfPCell(ph4);
                            cell4.setColspan(2);
                            tab3.addCell(cell4);
                            nf.setMaximumFractionDigits(2);
                            nf.setMinimumFractionDigits(2);
                            Phrase ph5 = new Phrase(String.valueOf(nf.format((totalpoints / COUNTER))), new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL, BaseColor.BLACK));
                            PdfPCell cell5 = new PdfPCell(ph5);
                            cell5.setColspan(2);
                            tab3.addCell(cell5);
                            String sqll = " Select grade from points_for_each_grade where points='" + nf2.format(totalpoints / COUNTER) + "'";
                            ps = con.prepareStatement(sqll);
                            rs = ps.executeQuery();
                            if (rs.next()) {
                                meangrade = rs.getString("grade");
                            }

                            Phrase ph6 = new Phrase(meangrade, new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL, BaseColor.BLACK));
                            PdfPCell cell6 = new PdfPCell(ph6);
                            cell6.setColspan(2);

                            tab3.addCell(cell6);
                        }

                    }

                }

                {
                    //Real Subject Grade Distribution from the Database gender wise

                    Phrase ph1 = new Phrase("Gender Wise Distribution".toUpperCase(), new Font(Font.FontFamily.HELVETICA, 14, Font.NORMAL, BaseColor.BLACK));
                    PdfPCell cell1 = new PdfPCell(ph1);
                    cell1.setColspan(24);
                    tab3.addCell(cell1);

                    for (int i = 0; i < 2; i++) {
                        String gender, sex;
                        if (i == 0) {
                            gender = "Male";
                            sex = "BOYS";
                        } else {
                            gender = "Female";
                            sex = "GIRLS";
                        }

                        Phrase ph1a = new Phrase(sex, new Font(Font.FontFamily.HELVETICA, 13, Font.NORMAL, BaseColor.BLACK));
                        PdfPCell cell1a = new PdfPCell(ph1a);
                        cell1a.setColspan(24);
                        tab3.addCell(cell1a);

                        String SQL = "Select * From subjects";
                        ps = con.prepareStatement(sql);
                        rx = ps.executeQuery();
                        while (rx.next()) {
                            String subcode = rx.getString("subjectcode");
                            String subname = rx.getString("SubjectName");
                            {
                                //real distribution from db

                                Phrase ph2 = new Phrase(subname, new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL, BaseColor.BLACK));
                                PdfPCell cell2 = new PdfPCell(ph2);
                                cell2.setColspan(6);

                                tab3.addCell(cell2);
                                int COUNTER = 0, gradepoint = 0;
                                double totalpoints = 0.0;

                                String sql2 = "Select * from points_for_each_grade order by points desc";
                                ps = con.prepareStatement(sql2);
                                rs = ps.executeQuery();
                                while (rs.next()) {
                                    String grade = rs.getString("grade");
                                    gradepoint = rs.getInt("Points");
                                    count = 0;
                                    String sql1 = "Select admnumber from kcsemarks,admission where kcseyear='" + year + "' and grade='" + grade + "'  and  subjectcode='" + subcode + "' and gender='" + gender + "' and admissionnumber=admnumber group by admnumber";
                                    ps = con.prepareStatement(sql1);
                                    ResultSet rsx = ps.executeQuery();
                                    while (rsx.next()) {
                                        totalpoints += gradepoint;
                                        count++;
                                        COUNTER++;
                                    }

                                    Phrase ph3 = new Phrase(String.valueOf(count), new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL, BaseColor.BLACK));
                                    PdfPCell cell3 = new PdfPCell(ph3);

                                    tab3.addCell(cell3);
                                }

                                String meangrade = "";
                                Phrase ph4 = new Phrase(String.valueOf(COUNTER), new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL, BaseColor.BLACK));
                                PdfPCell cell4 = new PdfPCell(ph4);
                                cell4.setColspan(2);
                                tab3.addCell(cell4);

                                Phrase ph5 = new Phrase(String.valueOf(nf.format(totalpoints / COUNTER)), new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL, BaseColor.BLACK));
                                PdfPCell cell5 = new PdfPCell(ph5);
                                cell5.setColspan(2);
                                tab3.addCell(cell5);
                                String sqll = " Select grade from points_for_each_grade where points='" + nf2.format(totalpoints / COUNTER) + "'";
                                ps = con.prepareStatement(sqll);
                                rs = ps.executeQuery();
                                if (rs.next()) {
                                    meangrade = rs.getString("grade");
                                }

                                Phrase ph6 = new Phrase(meangrade, new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL, BaseColor.BLACK));
                                PdfPCell cell6 = new PdfPCell(ph6);
                                cell6.setColspan(2);

                                tab3.addCell(cell6);
                            }

                        }

                    }

                }

                tab3.setHeaderRows(1);
                tab3.setSpacingBefore(20);
                tab3.setWidthPercentage(100);

                doc.add(tab3);
                doc.close();

            } else {

                String sqlxxa = "select * from kcsemarks where kcseyear='" + year + "'  and admnumber='" + admissionNumber + "' group by admnumber order by totalpoints desc";
                ps = con.prepareStatement(sqlxxa);
                rx = ps.executeQuery();
                while (rx.next()) {

                    counter++;
                    String adm = rx.getString("Admnumber");
                    Phrase ph = new Phrase(String.valueOf(counter), new Font(Font.FontFamily.HELVETICA, 8, Font.NORMAL, BaseColor.BLACK));
                    PdfPCell cell = new PdfPCell(ph);

                    cell.setVerticalAlignment(Element.ALIGN_BOTTOM);
                    cell.setHorizontalAlignment(Element.ALIGN_MIDDLE);
                    Phrase ph1 = new Phrase(adm, new Font(Font.FontFamily.HELVETICA, 8, Font.NORMAL, BaseColor.BLACK));
                    PdfPCell cell1 = new PdfPCell(ph1);

                    cell1.setColspan(2);
                    Phrase ph2 = new Phrase(rx.getString("Kcseindexnumber"), new Font(Font.FontFamily.HELVETICA, 8, Font.NORMAL, BaseColor.BLACK));
                    PdfPCell cell2 = new PdfPCell(ph2);
                    cell2.setColspan(3);

                    Phrase ph3 = new Phrase(Globals.fullName(adm), new Font(Font.FontFamily.HELVETICA, 8, Font.NORMAL, BaseColor.BLACK));

                    PdfPCell cell3 = new PdfPCell(ph3);
                    cell3.setColspan(6);
                    cell3.setVerticalAlignment(Element.ALIGN_BOTTOM);
                    cell3.setHorizontalAlignment(Element.ALIGN_MIDDLE);

                    tabb.addCell(cell);
                    tabb.addCell(cell1);
                    tabb.addCell(cell2);
                    tabb.addCell(cell3);

                    String sqlc = "Select * from subjects";
                    ps = con.prepareStatement(sqlc);
                    rs = ps.executeQuery();
                    while (rs.next()) {
                        String subcode = rs.getString("Subjectcode");
                        String score = "", gr = "";
                        String sq = "select points,grade from kcsemarks where kcseyear='" + year + "' and subjectcode='" + subcode + "' and admNumber='" + adm + "' order by subjectcode ";
                        ps = con.prepareStatement(sq);
                        ResultSet rr = ps.executeQuery();
                        if (rr.next()) {
                            score = rr.getString("points");
                            gr = rr.getString("grade");
                        } else {
                            gr = "";
                            score = "";
                        }
                        Phrase ph4 = new Phrase(score, new Font(Font.FontFamily.HELVETICA, 8, Font.NORMAL, BaseColor.BLACK));
                        PdfPCell cell4 = new PdfPCell(ph4);
                        Phrase ph4a = new Phrase(gr, new Font(Font.FontFamily.HELVETICA, 8, Font.NORMAL, BaseColor.BLACK));
                        PdfPCell cell4a = new PdfPCell(ph4a);
                        tabb.addCell(cell4);
                        tabb.addCell(cell4a);

                    }

                    Phrase ph4 = new Phrase(rx.getString("Totalpoints"), new Font(Font.FontFamily.HELVETICA, 7, Font.NORMAL, BaseColor.BLACK));
                    PdfPCell cell4 = new PdfPCell(ph4);

                    tabb.addCell(cell4);

                    Phrase ph5 = new Phrase(rx.getString("meanpoints"), new Font(Font.FontFamily.HELVETICA, 7, Font.NORMAL, BaseColor.BLACK));
                    PdfPCell cell5 = new PdfPCell(ph5);

                    Phrase ph6 = new Phrase(rx.getString("meangrade"), new Font(Font.FontFamily.HELVETICA, 7, Font.NORMAL, BaseColor.BLACK));
                    PdfPCell cell6 = new PdfPCell(ph6);

                    Phrase ph6a = new Phrase(rx.getString("position"), new Font(Font.FontFamily.HELVETICA, 7, Font.NORMAL, BaseColor.BLACK));
                    PdfPCell cell6a = new PdfPCell(ph6a);

                    Phrase ph7 = new Phrase(rx.getString("positionoutof"), new Font(Font.FontFamily.HELVETICA, 7, Font.NORMAL, BaseColor.BLACK));
                    PdfPCell cell7 = new PdfPCell(ph7);

                    tabb.addCell(cell5);
                    tabb.addCell(cell6);
                    tabb.addCell(cell6a);
                    tabb.addCell(cell7);

                }
                Phrase ph = new Phrase("Mean Scores", new Font(Font.FontFamily.HELVETICA, 8, Font.NORMAL, BaseColor.BLACK));
                PdfPCell cell = new PdfPCell(ph);
                cell.setColspan(3);
                String sq = "Select * from subjects order by subjectcode";
                ps = con.prepareStatement(sql);
                rs = ps.executeQuery();
                while (rs.next()) {

                }
                tabb.setHeaderRows(1);
                doc.add(tabb);
                doc.close();
            }

            if (ConfigurationIntialiser.docOpener()) {
                if (dialog == null) {

                    dialog = ReportFrame.getReportFrameInstance(new File("C:\\schoolData\\kcse" + year + ".pdf"));

                } else {
                    dialog = null;
                    dialog = ReportFrame.getReportFrameInstance(new File("C:\\schoolData\\kcse" + year + ".pdf"));
                    dialog.setVisible(true);

                }

            } else {

                Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler " + "C:\\schoolData\\kcse" + year + ".pdf");

            }

        } catch (Exception sq) {
            sq.printStackTrace();
        }
    }


    public static void kcpeMarks(String year, String admissionNumber, boolean analyse) {

        NumberFormat nf2 = NumberFormat.getInstance();
        nf2.setMaximumFractionDigits(0);
        nf2.setMinimumFractionDigits(0);
        try {
            Document doc = new Document(PageSize.A4.rotate());
            com.itextpdf.text.Image image = com.itextpdf.text.Image.getInstance("C:/schooldata/logo.jpg");
            PdfWriter writer = PdfWriter.getInstance(doc, new FileOutputStream("C:\\schoolData\\kcse" + year + ".pdf"));
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
            Paragraph pr5 = new Paragraph("K.C.P.E Results".toUpperCase(), FontFactory.getFont(FontFactory.HELVETICA, 13, java.awt.Font.BOLD, BaseColor.BLACK));
            Chunk glue = new Chunk(new VerticalPositionMark());
            doc.addCreationDate();
            Paragraph pr7 = new Paragraph("                  CLASS OF :  " + year, FontFactory.getFont(FontFactory.TIMES, 13, java.awt.Font.PLAIN, BaseColor.BLACK));

            pr7.setSpacingBefore(10);

            doc.add(pr5);
            doc.add(pr7);

            con = DbConnection.connectDb();
            int columns = 0;
            String sql = "Select  Count(*) from primarysubjects";
            ps = con.prepareCall(sql);
            rs = ps.executeQuery();
            if (rs.next()) {
                columns = rs.getInt("count(*)");

            }
            columns = 2 * (columns) + 14;
            PdfPTable tabb = new PdfPTable(columns);

            tabb.setSpacingBefore(10);
            tabb.setWidthPercentage(100);

            {
                Phrase ph = new Phrase("No.", new Font(Font.FontFamily.HELVETICA, 8, Font.NORMAL, BaseColor.BLACK));
                PdfPCell cell = new PdfPCell(ph);

                cell.setVerticalAlignment(Element.ALIGN_BOTTOM);
                cell.setHorizontalAlignment(Element.ALIGN_MIDDLE);
                Phrase ph1 = new Phrase("ADM NUMBER", new Font(Font.FontFamily.HELVETICA, 8, Font.NORMAL, BaseColor.BLACK));
                PdfPCell cell1 = new PdfPCell(ph1);
                cell1.setRotation(90);
                cell1.setColspan(2);
                Phrase ph2 = new Phrase("KCPE INDEX NO.", new Font(Font.FontFamily.HELVETICA, 8, Font.NORMAL, BaseColor.BLACK));
                PdfPCell cell2 = new PdfPCell(ph2);
                cell2.setColspan(3);
                cell2.setRotation(90);
                Phrase ph3 = new Phrase("NAME", new Font(Font.FontFamily.HELVETICA, 8, Font.NORMAL, BaseColor.BLACK));

                PdfPCell cell3 = new PdfPCell(ph3);
                cell3.setColspan(6);
                cell3.setVerticalAlignment(Element.ALIGN_BOTTOM);
                cell3.setHorizontalAlignment(Element.ALIGN_MIDDLE);

                tabb.addCell(cell);
                tabb.addCell(cell1);
                tabb.addCell(cell2);
                tabb.addCell(cell3);
                String sql2 = "Select  subjectname from primarysubjects order by subjectcode";
                ps = con.prepareCall(sql2);
                rs = ps.executeQuery();
                while (rs.next()) {
                    Phrase ph4 = new Phrase(rs.getString("SubjectName"), new Font(Font.FontFamily.HELVETICA, 8, Font.NORMAL, BaseColor.BLACK));
                    PdfPCell cell4 = new PdfPCell(ph4);
                    cell4.setRotation(90);

                    Phrase ph4a = new Phrase("Grade", new Font(Font.FontFamily.HELVETICA, 8, Font.NORMAL, BaseColor.BLACK));
                    PdfPCell cell4a = new PdfPCell(ph4a);
                    cell4a.setRotation(90);


                    tabb.addCell(cell4);
                    tabb.addCell(cell4a);

                }
                Phrase ph4 = new Phrase("Total Marks", new Font(Font.FontFamily.HELVETICA, 7, Font.NORMAL, BaseColor.BLACK));
                PdfPCell cell4 = new PdfPCell(ph4);
                cell4.setColspan(2);
                cell4.setRotation(90);
                tabb.addCell(cell4);


            }
            System.err.println("huhuh");
            int count = 0;

            int counter = 0;
            ResultSet rx;

            if (admissionNumber.equalsIgnoreCase("All")) {

                String sqlxxa = "select * from kcpemarkstable where yearofkcpe='" + year + "'  order by admnumber desc";
                ps = con.prepareStatement(sqlxxa);
                rx = ps.executeQuery();
                while (rx.next()) {

                    counter++;
                    String adm = rx.getString("admnumber");
                    Phrase ph = new Phrase(String.valueOf(counter), new Font(Font.FontFamily.HELVETICA, 8, Font.NORMAL, BaseColor.BLACK));
                    PdfPCell cell = new PdfPCell(ph);

                    cell.setVerticalAlignment(Element.ALIGN_BOTTOM);
                    cell.setHorizontalAlignment(Element.ALIGN_MIDDLE);
                    Phrase ph1 = new Phrase(adm, new Font(Font.FontFamily.HELVETICA, 8, Font.NORMAL, BaseColor.BLACK));
                    PdfPCell cell1 = new PdfPCell(ph1);

                    cell1.setColspan(2);
                    Phrase ph2 = new Phrase(rx.getString("Kcpeindexnumber"), new Font(Font.FontFamily.HELVETICA, 8, Font.NORMAL, BaseColor.BLACK));
                    PdfPCell cell2 = new PdfPCell(ph2);
                    cell2.setColspan(3);

                    Phrase ph3 = new Phrase(Globals.fullName(adm), new Font(Font.FontFamily.HELVETICA, 8, Font.NORMAL, BaseColor.BLACK));

                    PdfPCell cell3 = new PdfPCell(ph3);
                    cell3.setColspan(6);
                    cell3.setVerticalAlignment(Element.ALIGN_BOTTOM);
                    cell3.setHorizontalAlignment(Element.ALIGN_MIDDLE);

                    tabb.addCell(cell);
                    tabb.addCell(cell1);
                    tabb.addCell(cell2);
                    tabb.addCell(cell3);


                    Phrase ph4e = new Phrase(rx.getString("English"), new Font(Font.FontFamily.HELVETICA, 8, Font.NORMAL, BaseColor.BLACK));
                    PdfPCell cell4e = new PdfPCell(ph4e);
                    Phrase ph4eg = new Phrase(Globals.Grade(rx.getString("English")), new Font(Font.FontFamily.HELVETICA, 8, Font.NORMAL, BaseColor.BLACK));
                    PdfPCell cell4eg = new PdfPCell(ph4eg);
                    tabb.addCell(cell4e);
                    tabb.addCell(cell4eg);

                    Phrase ph4k = new Phrase(rx.getString("Kiswahili"), new Font(Font.FontFamily.HELVETICA, 8, Font.NORMAL, BaseColor.BLACK));
                    PdfPCell cell4k = new PdfPCell(ph4k);
                    Phrase ph4kg = new Phrase(Globals.Grade(rx.getString("kiswahili")), new Font(Font.FontFamily.HELVETICA, 8, Font.NORMAL, BaseColor.BLACK));
                    PdfPCell cell4kg = new PdfPCell(ph4kg);
                    tabb.addCell(cell4k);
                    tabb.addCell(cell4kg);

                    Phrase ph4m = new Phrase(rx.getString("Mathematics"), new Font(Font.FontFamily.HELVETICA, 8, Font.NORMAL, BaseColor.BLACK));
                    PdfPCell cell4m = new PdfPCell(ph4m);
                    Phrase ph4mg = new Phrase(Globals.Grade(rx.getString("Mathematics")), new Font(Font.FontFamily.HELVETICA, 8, Font.NORMAL, BaseColor.BLACK));
                    PdfPCell cell4mg = new PdfPCell(ph4mg);
                    tabb.addCell(cell4m);
                    tabb.addCell(cell4mg);


                    Phrase ph4ss = new Phrase(rx.getString("SocialStudies"), new Font(Font.FontFamily.HELVETICA, 8, Font.NORMAL, BaseColor.BLACK));
                    PdfPCell cell4ss = new PdfPCell(ph4ss);
                    Phrase ph4ssg = new Phrase(Globals.Grade(rx.getString("SocialStudies")), new Font(Font.FontFamily.HELVETICA, 8, Font.NORMAL, BaseColor.BLACK));
                    PdfPCell cell4ssg = new PdfPCell(ph4ssg);
                    tabb.addCell(cell4ss);
                    tabb.addCell(cell4ssg);


                    Phrase ph4s = new Phrase(rx.getString("Science"), new Font(Font.FontFamily.HELVETICA, 8, Font.NORMAL, BaseColor.BLACK));
                    PdfPCell cell4s = new PdfPCell(ph4s);
                    Phrase ph4sg = new Phrase(Globals.Grade(rx.getString("Science")), new Font(Font.FontFamily.HELVETICA, 8, Font.NORMAL, BaseColor.BLACK));
                    PdfPCell cell4sg = new PdfPCell(ph4sg);
                    tabb.addCell(cell4s);
                    tabb.addCell(cell4sg);


                    Phrase ph4 = new Phrase(Globals.kcpeMarks(adm), new Font(Font.FontFamily.HELVETICA, 7, Font.NORMAL, BaseColor.BLACK));
                    PdfPCell cell4 = new PdfPCell(ph4);
                    cell4.setColspan(2);
                    tabb.addCell(cell4);


                }

                tabb.setHeaderRows(1);
                doc.add(tabb);
                System.err.println("hahahaha");
                doc.close();

            } else {

                String sqlxxa = "select * from kcsemarks where kcseyear='" + year + "'  and admnumber='" + admissionNumber + "' group by admnumber order by totalpoints desc";
                ps = con.prepareStatement(sqlxxa);
                rx = ps.executeQuery();
                while (rx.next()) {

                    counter++;
                    String adm = rx.getString("Admnumber");
                    Phrase ph = new Phrase(String.valueOf(counter), new Font(Font.FontFamily.HELVETICA, 8, Font.NORMAL, BaseColor.BLACK));
                    PdfPCell cell = new PdfPCell(ph);

                    cell.setVerticalAlignment(Element.ALIGN_BOTTOM);
                    cell.setHorizontalAlignment(Element.ALIGN_MIDDLE);
                    Phrase ph1 = new Phrase(adm, new Font(Font.FontFamily.HELVETICA, 8, Font.NORMAL, BaseColor.BLACK));
                    PdfPCell cell1 = new PdfPCell(ph1);

                    cell1.setColspan(2);
                    Phrase ph2 = new Phrase(rx.getString("Kcseindexnumber"), new Font(Font.FontFamily.HELVETICA, 8, Font.NORMAL, BaseColor.BLACK));
                    PdfPCell cell2 = new PdfPCell(ph2);
                    cell2.setColspan(3);

                    Phrase ph3 = new Phrase(Globals.fullName(adm), new Font(Font.FontFamily.HELVETICA, 8, Font.NORMAL, BaseColor.BLACK));

                    PdfPCell cell3 = new PdfPCell(ph3);
                    cell3.setColspan(6);
                    cell3.setVerticalAlignment(Element.ALIGN_BOTTOM);
                    cell3.setHorizontalAlignment(Element.ALIGN_MIDDLE);

                    tabb.addCell(cell);
                    tabb.addCell(cell1);
                    tabb.addCell(cell2);
                    tabb.addCell(cell3);

                    String sqlc = "Select * from subjects";
                    ps = con.prepareStatement(sqlc);
                    rs = ps.executeQuery();
                    while (rs.next()) {
                        String subcode = rs.getString("Subjectcode");
                        String score = "", gr = "";
                        String sq = "select points,grade from kcsemarks where kcseyear='" + year + "' and subjectcode='" + subcode + "' and admNumber='" + adm + "' order by subjectcode ";
                        ps = con.prepareStatement(sq);
                        ResultSet rr = ps.executeQuery();
                        if (rr.next()) {
                            score = rr.getString("points");
                            gr = rr.getString("grade");
                        } else {
                            gr = "";
                            score = "";
                        }
                        Phrase ph4 = new Phrase(score, new Font(Font.FontFamily.HELVETICA, 8, Font.NORMAL, BaseColor.BLACK));
                        PdfPCell cell4 = new PdfPCell(ph4);
                        Phrase ph4a = new Phrase(gr, new Font(Font.FontFamily.HELVETICA, 8, Font.NORMAL, BaseColor.BLACK));
                        PdfPCell cell4a = new PdfPCell(ph4a);
                        tabb.addCell(cell4);
                        tabb.addCell(cell4a);

                    }

                    Phrase ph4 = new Phrase(rx.getString("Totalpoints"), new Font(Font.FontFamily.HELVETICA, 7, Font.NORMAL, BaseColor.BLACK));
                    PdfPCell cell4 = new PdfPCell(ph4);

                    tabb.addCell(cell4);

                    Phrase ph5 = new Phrase(rx.getString("meanpoints"), new Font(Font.FontFamily.HELVETICA, 7, Font.NORMAL, BaseColor.BLACK));
                    PdfPCell cell5 = new PdfPCell(ph5);

                    Phrase ph6 = new Phrase(rx.getString("meangrade"), new Font(Font.FontFamily.HELVETICA, 7, Font.NORMAL, BaseColor.BLACK));
                    PdfPCell cell6 = new PdfPCell(ph6);

                    Phrase ph6a = new Phrase(rx.getString("position"), new Font(Font.FontFamily.HELVETICA, 7, Font.NORMAL, BaseColor.BLACK));
                    PdfPCell cell6a = new PdfPCell(ph6a);

                    Phrase ph7 = new Phrase(rx.getString("positionoutof"), new Font(Font.FontFamily.HELVETICA, 7, Font.NORMAL, BaseColor.BLACK));
                    PdfPCell cell7 = new PdfPCell(ph7);

                    tabb.addCell(cell5);
                    tabb.addCell(cell6);
                    tabb.addCell(cell6a);
                    tabb.addCell(cell7);

                }
                Phrase ph = new Phrase("Mean Scores", new Font(Font.FontFamily.HELVETICA, 8, Font.NORMAL, BaseColor.BLACK));
                PdfPCell cell = new PdfPCell(ph);
                cell.setColspan(3);
                String sq = "Select * from subjects order by subjectcode";
                ps = con.prepareStatement(sql);
                rs = ps.executeQuery();
                while (rs.next()) {

                }
                tabb.setHeaderRows(1);
                doc.add(tabb);
                doc.close();
            }

            if (ConfigurationIntialiser.docOpener()) {
                if (dialog == null) {

                    dialog = ReportFrame.getReportFrameInstance(new File("C:\\schoolData\\kcse" + year + ".pdf"));

                } else {
                    dialog = null;
                    dialog = ReportFrame.getReportFrameInstance(new File("C:\\schoolData\\kcse" + year + ".pdf"));
                    dialog.setVisible(true);

                }

            } else {

                Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler " + "C:\\schoolData\\kcse" + year + ".pdf");

            }

        } catch (Exception sq) {
            sq.printStackTrace();
        }
    }

    public static void emptyScoreSheet(String classname, String academicyear, String Stream, String examcode, String examname, String term) {
        try {

            Document doc = new Document();
            com.itextpdf.text.Image image = com.itextpdf.text.Image.getInstance("C:/schooldata/logo.jpg");
            PdfWriter writer = PdfWriter.getInstance(doc, new FileOutputStream("C:\\schoolData\\emptyscoresheet.pdf"));
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
            Paragraph pr5 = new Paragraph("Empty Score Sheet Preview", FontFactory.getFont(FontFactory.HELVETICA, 13, java.awt.Font.BOLD, BaseColor.BLACK));
            Chunk glue = new Chunk(new VerticalPositionMark());
            doc.addCreationDate();
            Paragraph pr7 = new Paragraph("CLASS:  " + classname.toUpperCase() + "                 STREAM: " + Stream.toUpperCase(), FontFactory.getFont(FontFactory.TIMES, 13, java.awt.Font.PLAIN, BaseColor.BLACK));
            pr7.add(new Chunk(glue));
            Paragraph pr8 = new Paragraph("KEY:  _=: Marks Entry Eligible.      X=: Student Not Taking The Subject In The Specified Year", FontFactory.getFont(FontFactory.TIMES, 13, java.awt.Font.ROMAN_BASELINE, BaseColor.BLACK));
            Paragraph pr9 = new Paragraph("EXAM NAME:  " + examname, FontFactory.getFont(FontFactory.TIMES, 13, java.awt.Font.PLAIN, BaseColor.BLACK));
            pr9.add(new Chunk(glue));

            pr9.add("TERM : " + term);
            pr7.add("ACADEMIC YEAR: " + academicyear);

            doc.add(pr5);
            doc.add(pr7);
            doc.add(pr9);
            doc.add(pr8);
            try {
                con = DbConnection.connectDb();
                int columns = 0;
                String sql = "Select  Count(*) from subjects";
                ps = con.prepareCall(sql);
                rs = ps.executeQuery();
                if (rs.next()) {
                    columns = rs.getInt("count(*)");

                }
                columns = columns + 13;
                PdfPTable tabb = new PdfPTable(columns);

                tabb.setSpacingBefore(10);
                tabb.setWidthPercentage(100);

                {
                    Phrase ph = new Phrase("No.", new Font(Font.FontFamily.HELVETICA, 7, Font.NORMAL, BaseColor.BLACK));
                    PdfPCell cell = new PdfPCell(ph);

                    cell.setVerticalAlignment(Element.ALIGN_BOTTOM);
                    cell.setHorizontalAlignment(Element.ALIGN_MIDDLE);
                    Phrase ph1 = new Phrase("ADM Number", new Font(Font.FontFamily.HELVETICA, 7, Font.NORMAL, BaseColor.BLACK));
                    PdfPCell cell1 = new PdfPCell(ph1);
                    cell1.setRotation(90);
                    cell1.setColspan(2);
                    Phrase ph2 = new Phrase("Class & Stream", new Font(Font.FontFamily.HELVETICA, 7, Font.NORMAL, BaseColor.BLACK));
                    PdfPCell cell2 = new PdfPCell(ph2);
                    cell2.setColspan(3);
                    cell2.setRotation(90);
                    Phrase ph3 = new Phrase("Name", new Font(Font.FontFamily.HELVETICA, 7, Font.NORMAL, BaseColor.BLACK));

                    PdfPCell cell3 = new PdfPCell(ph3);
                    cell3.setColspan(6);
                    cell3.setVerticalAlignment(Element.ALIGN_BOTTOM);
                    cell3.setHorizontalAlignment(Element.ALIGN_MIDDLE);

                    tabb.addCell(cell);
                    tabb.addCell(cell1);
                    tabb.addCell(cell2);
                    tabb.addCell(cell3);
                    String sql2 = "Select  subjectname from subjects order by subjectcode";
                    ps = con.prepareCall(sql2);
                    rs = ps.executeQuery();
                    while (rs.next()) {
                        Phrase ph4 = new Phrase(rs.getString("SubjectName") + " Out Of ___", new Font(Font.FontFamily.HELVETICA, 7, Font.NORMAL, BaseColor.BLACK));
                        PdfPCell cell4 = new PdfPCell(ph4);
                        cell4.setRotation(90);
                        tabb.addCell(cell4);

                    }
                    Phrase ph4 = new Phrase("Total Subjects Allocated", new Font(Font.FontFamily.HELVETICA, 7, Font.NORMAL, BaseColor.BLACK));
                    PdfPCell cell4 = new PdfPCell(ph4);
                    cell4.setRotation(90);
                    tabb.addCell(cell4);

                }

                int counter = 0;

                String streamcode = Globals.streamcode(Stream);
                String classcode = Globals.classCode(classname);

                String sql3 = "Select admissionnumber,firstname,middlename,lastname from admission where  currentform='" + classcode + "' and currentstream='" + streamcode + "' order by admissionNumber";
                ps = con.prepareStatement(sql3);
                rs = ps.executeQuery();

                while (rs.next()) {

                    String adm = rs.getString("AdmissionNumber");
                    counter++;
                    Phrase ph = new Phrase(" \n" + String.valueOf(counter), new Font(Font.FontFamily.HELVETICA, 7, Font.NORMAL));
                    PdfPCell cell = new PdfPCell(ph);

                    Phrase ph1 = new Phrase(adm, new Font(Font.FontFamily.HELVETICA, 7, Font.NORMAL));
                    PdfPCell cell1 = new PdfPCell(ph1);
                    cell1.setColspan(2);

                    Phrase ph2 = new Phrase(classname + "  " + Stream, new Font(Font.FontFamily.HELVETICA, 7, Font.NORMAL));
                    PdfPCell cell2 = new PdfPCell(ph2);
                    cell2.setColspan(3);
                    Phrase ph3 = new Phrase(rs.getString("firstName") + "    " + rs.getString("Middlename") + "    " + rs.getString("LastName"), new Font(Font.FontFamily.HELVETICA, 7, Font.NORMAL));

                    PdfPCell cell3 = new PdfPCell(ph3);
                    cell3.setColspan(6);

                    tabb.addCell(cell);
                    tabb.addCell(cell1);
                    tabb.addCell(cell2);
                    tabb.addCell(cell3);

                    int subcounter = 0;
                    String sql4 = "Select subjectcode from subjects order by subjectcode";
                    ps = con.prepareStatement(sql4);
                    ResultSet RS = ps.executeQuery();
                    while (RS.next()) {
                        String allocated = "X";
                        String subcode = RS.getString("Subjectcode");
                        String sql5 = "Select * from studentsubjectallocation where subjectcode='" + subcode + "' and academicyear='" + academicyear + "' and admnumber='" + adm + "'";
                        ps = con.prepareStatement(sql5);
                        ResultSet RX = ps.executeQuery();
                        if (RX.next()) {
                            subcounter++;

                            allocated = "\n_";

                        } else {
                            allocated = "X";
                        }
                        Phrase ph4 = new Phrase(allocated, new Font(Font.FontFamily.HELVETICA, 7, Font.NORMAL));
                        PdfPCell cell4 = new PdfPCell(ph4);

                        tabb.addCell(cell4);

                    }

                    Phrase ph4 = new Phrase(String.valueOf(subcounter), new Font(Font.FontFamily.HELVETICA, 7, Font.NORMAL));
                    PdfPCell cell4 = new PdfPCell(ph4);

                    tabb.addCell(cell4);

                }
                tabb.setHeaderRows(1);
                doc.add(tabb);
            } catch (Exception sq) {
                sq.printStackTrace();
                JOptionPane.showMessageDialog(null, sq.getMessage());
            }

            doc.close();

            if (ConfigurationIntialiser.docOpener()) {
                if (dialog == null) {

                    dialog = ReportFrame.getReportFrameInstance(new File("C:\\schoolData\\emptyscoresheet.pdf"));

                } else {
                    dialog = null;
                    dialog = ReportFrame.getReportFrameInstance(new File("C:\\schoolData\\emptyscoresheet.pdf"));
                    dialog.setVisible(true);

                }

            } else {

                Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler " + "C:\\schoolData\\emptyscoresheet.pdf");

            }

        } catch (Exception sq) {
            sq.printStackTrace();
            JOptionPane.showMessageDialog(null, sq.getMessage());

        }
    }

    public static void missingResultsOverall(String classname, String academicyear, String Stream, String examcode, String examname, String term) {
        try {

            Document doc = new Document();
            com.itextpdf.text.Image image = com.itextpdf.text.Image.getInstance("C:/schooldata/logo.jpg");
            PdfWriter writer = PdfWriter.getInstance(doc, new FileOutputStream("C:\\schoolData\\missingmarksoverall.pdf"));
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
            Paragraph pr5 = new Paragraph("Entered Marks Preview(Score Sheet)", FontFactory.getFont(FontFactory.HELVETICA, 13, java.awt.Font.BOLD, BaseColor.BLACK));
            Chunk glue = new Chunk(new VerticalPositionMark());
            doc.addCreationDate();
            Paragraph pr7 = new Paragraph("CLASS:  " + classname.toUpperCase() + "                 STREAM: " + Stream.toUpperCase(), FontFactory.getFont(FontFactory.TIMES, 13, java.awt.Font.PLAIN, BaseColor.BLACK));
            pr7.add(new Chunk(glue));
            Paragraph pr8 = new Paragraph("KEY:  Y=Score Missing.  Blank Cell: Student Not Taking The Subject In The Specified Year", FontFactory.getFont(FontFactory.TIMES, 13, java.awt.Font.ROMAN_BASELINE, BaseColor.BLACK));
            Paragraph pr9 = new Paragraph("EXAM NAME:  " + examname, FontFactory.getFont(FontFactory.TIMES, 13, java.awt.Font.PLAIN, BaseColor.BLACK));
            pr9.add(new Chunk(glue));

            pr9.add("TERM : " + term);
            pr7.add("ACADEMIC YEAR: " + academicyear);

            pr7.setSpacingBefore(30);

            doc.add(pr5);
            doc.add(pr7);
            doc.add(pr9);
            doc.add(pr8);
            try {
                con = DbConnection.connectDb();
                int columns = 0;
                String sql = "Select  Count(*) from subjects";
                ps = con.prepareCall(sql);
                rs = ps.executeQuery();
                if (rs.next()) {
                    columns = rs.getInt("count(*)");

                }
                columns = columns + 13;
                PdfPTable tabb = new PdfPTable(columns);

                tabb.setSpacingBefore(10);
                tabb.setWidthPercentage(100);

                {
                    Phrase ph = new Phrase("No.", new Font(Font.FontFamily.HELVETICA, 7, Font.NORMAL, BaseColor.BLACK));
                    PdfPCell cell = new PdfPCell(ph);

                    cell.setVerticalAlignment(Element.ALIGN_BOTTOM);
                    cell.setHorizontalAlignment(Element.ALIGN_MIDDLE);
                    Phrase ph1 = new Phrase("ADM Number", new Font(Font.FontFamily.HELVETICA, 7, Font.NORMAL, BaseColor.BLACK));
                    PdfPCell cell1 = new PdfPCell(ph1);
                    cell1.setRotation(90);
                    cell1.setColspan(2);
                    Phrase ph2 = new Phrase("Class & Stream", new Font(Font.FontFamily.HELVETICA, 7, Font.NORMAL, BaseColor.BLACK));
                    PdfPCell cell2 = new PdfPCell(ph2);
                    cell2.setColspan(3);
                    cell2.setRotation(90);
                    Phrase ph3 = new Phrase("Name", new Font(Font.FontFamily.HELVETICA, 7, Font.NORMAL, BaseColor.BLACK));

                    PdfPCell cell3 = new PdfPCell(ph3);
                    cell3.setColspan(6);
                    cell3.setVerticalAlignment(Element.ALIGN_BOTTOM);
                    cell3.setHorizontalAlignment(Element.ALIGN_MIDDLE);

                    tabb.addCell(cell);
                    tabb.addCell(cell1);
                    tabb.addCell(cell2);
                    tabb.addCell(cell3);
                    String sql2 = "Select  subjectname from subjects order by subjectcode";
                    ps = con.prepareCall(sql2);
                    rs = ps.executeQuery();
                    while (rs.next()) {
                        Phrase ph4 = new Phrase(rs.getString("SubjectName"), new Font(Font.FontFamily.HELVETICA, 7, Font.NORMAL, BaseColor.BLACK));
                        PdfPCell cell4 = new PdfPCell(ph4);
                        cell4.setRotation(90);
                        tabb.addCell(cell4);

                    }
                    Phrase ph4 = new Phrase("Total Subjects Allocated", new Font(Font.FontFamily.HELVETICA, 7, Font.NORMAL, BaseColor.BLACK));
                    PdfPCell cell4 = new PdfPCell(ph4);
                    cell4.setRotation(90);
                    tabb.addCell(cell4);

                }

                int counter = 0;

                String streamcode = Globals.streamcode(Stream);
                String classcode = Globals.classCode(classname);

                String sql3 = "Select admissionnumber,firstname,middlename,lastname from admission where  currentform='" + Globals.classCode(ClassProgressTracker.currentClass(Integer.parseInt(academicyear), classname)) + "' and currentstream='" + streamcode + "' order by admissionNumber";
                ps = con.prepareStatement(sql3);
                rs = ps.executeQuery();

                while (rs.next()) {

                    String adm = rs.getString("AdmissionNumber");
                    counter++;
                    Phrase ph = new Phrase(String.valueOf(counter), new Font(Font.FontFamily.HELVETICA, 7, Font.NORMAL));
                    PdfPCell cell = new PdfPCell(ph);

                    Phrase ph1 = new Phrase(adm, new Font(Font.FontFamily.HELVETICA, 7, Font.NORMAL));
                    PdfPCell cell1 = new PdfPCell(ph1);
                    cell1.setColspan(2);

                    Phrase ph2 = new Phrase(classname + "  " + Stream, new Font(Font.FontFamily.HELVETICA, 7, Font.NORMAL));
                    PdfPCell cell2 = new PdfPCell(ph2);
                    cell2.setColspan(3);
                    Phrase ph3 = new Phrase(rs.getString("firstName") + "    " + rs.getString("Middlename") + "    " + rs.getString("LastName"), new Font(Font.FontFamily.HELVETICA, 7, Font.NORMAL));

                    PdfPCell cell3 = new PdfPCell(ph3);
                    cell3.setColspan(6);

                    tabb.addCell(cell);
                    tabb.addCell(cell1);
                    tabb.addCell(cell2);
                    tabb.addCell(cell3);

                    int subcounter = 0;
                    String sql4 = "Select subjectcode from subjects order by subjectcode";
                    ps = con.prepareStatement(sql4);
                    ResultSet RS = ps.executeQuery();
                    while (RS.next()) {
                        String allocated = "Y";
                        String subcode = RS.getString("Subjectcode");
                        String sql5 = "Select * from studentsubjectallocation where subjectcode='" + subcode + "' and academicyear='" + academicyear + "' and admnumber='" + adm + "'";
                        ps = con.prepareStatement(sql5);
                        ResultSet RX = ps.executeQuery();
                        if (RX.next()) {
                            subcounter++;
                            String sql8 = "Select examscore from markstable where subjectcode='" + subcode + "' and academicyear='" + academicyear + "' and admnumber='" + adm + "' and examcode='" + examcode + "'";
                            ps = con.prepareStatement(sql8);
                            ResultSet rs = ps.executeQuery();
                            if (rs.next()) {
                                allocated = rs.getString("ExamScore");
                            } else {
                                allocated = "Y";
                            }

                        } else {
                            allocated = "";
                        }
                        Phrase ph4 = new Phrase(allocated, new Font(Font.FontFamily.HELVETICA, 7, Font.NORMAL));
                        PdfPCell cell4 = new PdfPCell(ph4);

                        tabb.addCell(cell4);

                    }

                    Phrase ph4 = new Phrase(String.valueOf(subcounter), new Font(Font.FontFamily.HELVETICA, 7, Font.NORMAL));
                    PdfPCell cell4 = new PdfPCell(ph4);

                    tabb.addCell(cell4);

                }
                tabb.setHeaderRows(1);
                doc.add(tabb);
            } catch (Exception sq) {
                sq.printStackTrace();
                JOptionPane.showMessageDialog(null, sq.getMessage());
            }

            doc.close();

            if (ConfigurationIntialiser.docOpener()) {
                if (dialog == null) {

                    dialog = ReportFrame.getReportFrameInstance(new File("C:\\schoolData\\missingmarksoverall.pdf"));

                } else {
                    dialog = null;
                    dialog = ReportFrame.getReportFrameInstance(new File("C:\\schoolData\\missingmarksoverall.pdf"));
                    dialog.setVisible(true);

                }

            } else {

                Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler " + "C:\\schoolData\\missingmarksoverall.pdf");

            }

        } catch (Exception sq) {
            sq.printStackTrace();
            JOptionPane.showMessageDialog(null, sq.getMessage());

        }
    }

    public static void subjectAllocationReport(String classname, String academicyear, String Stream) {
        try {

            Document doc = new Document();
            com.itextpdf.text.Image image = com.itextpdf.text.Image.getInstance("C:/schooldata/logo.jpg");
            PdfWriter writer = PdfWriter.getInstance(doc, new FileOutputStream("C:\\schoolData\\subjectallocationReport.pdf"));
            doc.open();
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
            Paragraph pr5 = new Paragraph("Subject Allocation Report".toUpperCase(), FontFactory.getFont(FontFactory.HELVETICA, 13, java.awt.Font.BOLD, BaseColor.BLACK));
            Chunk glue = new Chunk(new VerticalPositionMark());

            doc.addCreationDate();
            Paragraph pr7 = new Paragraph("CLASS:  " + classname.toUpperCase() + "                 STREAM: " + Stream.toUpperCase(), FontFactory.getFont(FontFactory.TIMES, 13, java.awt.Font.PLAIN, BaseColor.BLACK));

            pr7.add(new Chunk(glue));

            Chunk chunk = new Chunk("ACADEMIC YEAR: " + academicyear).setUnderline(1, 0);

            pr7.add(chunk);

            pr5.setIndentationLeft(150);
            pr7.setSpacingBefore(30);
            doc.add(pr5);
            doc.add(pr7);

            try {
                con = DbConnection.connectDb();
                int columns = 0;
                String sql = "Select  Count(*) from subjects";
                ps = con.prepareCall(sql);
                rs = ps.executeQuery();
                if (rs.next()) {
                    columns = rs.getInt("count(*)");

                }
                columns = columns + 13;
                PdfPTable tabb = new PdfPTable(columns);

                tabb.setSpacingBefore(10);
                tabb.setWidthPercentage(100);

                {
                    Phrase ph = new Phrase("No.", new Font(Font.FontFamily.HELVETICA, 7, Font.NORMAL, BaseColor.BLACK));
                    PdfPCell cell = new PdfPCell(ph);

                    cell.setVerticalAlignment(Element.ALIGN_BOTTOM);
                    cell.setHorizontalAlignment(Element.ALIGN_MIDDLE);
                    Phrase ph1 = new Phrase("ADM Number", new Font(Font.FontFamily.HELVETICA, 7, Font.NORMAL, BaseColor.BLACK));
                    PdfPCell cell1 = new PdfPCell(ph1);
                    cell1.setRotation(90);
                    cell1.setColspan(2);
                    Phrase ph2 = new Phrase("Class & Stream", new Font(Font.FontFamily.HELVETICA, 7, Font.NORMAL, BaseColor.BLACK));
                    PdfPCell cell2 = new PdfPCell(ph2);
                    cell2.setColspan(3);
                    cell2.setRotation(90);
                    Phrase ph3 = new Phrase("Name", new Font(Font.FontFamily.HELVETICA, 7, Font.NORMAL, BaseColor.BLACK));

                    PdfPCell cell3 = new PdfPCell(ph3);
                    cell3.setColspan(6);
                    cell3.setVerticalAlignment(Element.ALIGN_BOTTOM);
                    cell3.setHorizontalAlignment(Element.ALIGN_MIDDLE);

                    tabb.addCell(cell);
                    tabb.addCell(cell1);
                    tabb.addCell(cell2);
                    tabb.addCell(cell3);
                    String sql2 = "Select  subjectname from subjects order by subjectcode";
                    ps = con.prepareCall(sql2);
                    rs = ps.executeQuery();
                    while (rs.next()) {
                        Phrase ph4 = new Phrase(rs.getString("SubjectName"), new Font(Font.FontFamily.HELVETICA, 7, Font.NORMAL, BaseColor.BLACK));
                        PdfPCell cell4 = new PdfPCell(ph4);
                        cell4.setRotation(90);
                        tabb.addCell(cell4);

                    }
                    Phrase ph4 = new Phrase("Total Subjects", new Font(Font.FontFamily.HELVETICA, 7, Font.NORMAL, BaseColor.BLACK));
                    PdfPCell cell4 = new PdfPCell(ph4);
                    cell4.setRotation(90);
                    tabb.addCell(cell4);

                }

                int counter = 0;

                String streamcode = Globals.streamcode(Stream);
                String classcode = Globals.classCode(classname);

                String sql3 = "Select admissionnumber,firstname,middlename,lastname from admission where  currentform='" + classcode + "' and currentstream='" + streamcode + "' order by admissionNumber";
                ps = con.prepareStatement(sql3);
                rs = ps.executeQuery();

                while (rs.next()) {

                    String adm = rs.getString("AdmissionNumber");
                    counter++;
                    Phrase ph = new Phrase(String.valueOf(counter), new Font(Font.FontFamily.HELVETICA, 7, Font.NORMAL));
                    PdfPCell cell = new PdfPCell(ph);

                    Phrase ph1 = new Phrase(adm, new Font(Font.FontFamily.HELVETICA, 7, Font.NORMAL));
                    PdfPCell cell1 = new PdfPCell(ph1);
                    cell1.setColspan(2);

                    Phrase ph2 = new Phrase(classname + "  " + Stream, new Font(Font.FontFamily.HELVETICA, 7, Font.NORMAL));
                    PdfPCell cell2 = new PdfPCell(ph2);
                    cell2.setColspan(3);
                    Phrase ph3 = new Phrase(rs.getString("firstName") + "    " + rs.getString("Middlename") + "    " + rs.getString("LastName"), new Font(Font.FontFamily.HELVETICA, 7, Font.NORMAL));

                    PdfPCell cell3 = new PdfPCell(ph3);
                    cell3.setColspan(6);

                    tabb.addCell(cell);
                    tabb.addCell(cell1);
                    tabb.addCell(cell2);
                    tabb.addCell(cell3);

                    int subcounter = 0;
                    String sql4 = "Select subjectcode from subjects order by subjectcode";
                    ps = con.prepareStatement(sql4);
                    ResultSet RS = ps.executeQuery();
                    while (RS.next()) {
                        String allocated = "X";
                        String subcode = RS.getString("Subjectcode");
                        String sql5 = "Select * from studentsubjectallocation where subjectcode='" + subcode + "' and academicyear='" + academicyear + "' and admnumber='" + adm + "'";
                        ps = con.prepareStatement(sql5);
                        ResultSet RX = ps.executeQuery();
                        if (RX.next()) {
                            subcounter++;
                            allocated = "X";
                        } else {
                            allocated = "";
                        }
                        Phrase ph4 = new Phrase(allocated, new Font(Font.FontFamily.HELVETICA, 7, Font.NORMAL));
                        PdfPCell cell4 = new PdfPCell(ph4);

                        tabb.addCell(cell4);

                    }

                    Phrase ph4 = new Phrase(String.valueOf(subcounter), new Font(Font.FontFamily.HELVETICA, 7, Font.NORMAL));
                    PdfPCell cell4 = new PdfPCell(ph4);

                    tabb.addCell(cell4);

                }
                tabb.setHeaderRows(1);
                doc.add(tabb);
                doc.close();

                if (ConfigurationIntialiser.docOpener()) {
                    if (dialog == null) {

                        dialog = ReportFrame.getReportFrameInstance(new File("C:\\schoolData\\subjectallocationReport.pdf"));

                    } else {
                        dialog = null;
                        dialog = ReportFrame.getReportFrameInstance(new File("C:\\schoolData\\subjectallocationReport.pdf"));
                        dialog.setVisible(true);

                    }

                } else {

                    Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler " + "C:\\schoolData\\subjectallocationReport.pdf");

                }

            } catch (Exception sq) {
                sq.printStackTrace();
                JOptionPane.showMessageDialog(null, sq.getMessage());
            }

        } catch (Exception sq) {
            sq.printStackTrace();
            JOptionPane.showMessageDialog(null, sq.getMessage());

        }
    }

    public static void subjectResults(String examcode, String examname, String stream, String term, String academicyear, String subjectcode, String classname, String subjectname, String sort) {
        try {
            NumberFormat nf2 = NumberFormat.getInstance();
            nf2.setMaximumFractionDigits(0);
            nf2.setMinimumFractionDigits(0);

            nf.setMaximumFractionDigits(2);
            nf.setMinimumFractionDigits(0);
            con = DbConnection.connectDb();

            Document doc = new Document();
            com.itextpdf.text.Image image = com.itextpdf.text.Image.getInstance("C:/schooldata/logo.jpg");
            PdfWriter writer = PdfWriter.getInstance(doc, new FileOutputStream("C:\\schoolData\\subjectResults.pdf"));
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
            Paragraph pr5 = new Paragraph("Exam Subject Results".toUpperCase(), FontFactory.getFont(FontFactory.HELVETICA, 13, java.awt.Font.BOLD, BaseColor.BLACK));
            Chunk glue = new Chunk(new VerticalPositionMark());
            doc.addCreationDate();
            Paragraph pr7 = new Paragraph("CLASS:  " + classname.toUpperCase() + "             STREAM: " + stream.toUpperCase(), FontFactory.getFont(FontFactory.TIMES, 13, java.awt.Font.PLAIN, BaseColor.BLACK));
            pr7.add(new Chunk(glue));

            Paragraph pr8 = new Paragraph("EXAM NAME:  " + examname.toUpperCase(), FontFactory.getFont(FontFactory.TIMES, 13, java.awt.Font.PLAIN, BaseColor.BLACK));
            pr8.add(new Chunk(glue));
            pr7.add("TERM       " + term + "     ACADEMIC YEAR: " + academicyear);
            pr8.add("SUBJECT       " + subjectname + "     SUBJECT CODE: " + subjectcode);

            pr5.setIndentationLeft(150);
            pr7.setSpacingBefore(20);
            pr8.setSpacingBefore(10);
            doc.add(pr5);
            doc.add(pr7);
            doc.add(pr8);
            String weight = "";
            String sql6 = "Select weight from examweights where examcode='" + examcode + "'";
            ps = con.prepareStatement(sql6);
            rs = ps.executeQuery();
            if (rs.next()) {
                weight = rs.getString("weight");

            }

            PdfPTable tabb = new PdfPTable(24);
            {
                tabb.setSpacingBefore(10);
                tabb.setWidthPercentage(100);
                Phrase ph = new Phrase("No.", new Font(Font.FontFamily.HELVETICA, 7, Font.NORMAL, BaseColor.BLACK));
                PdfPCell cell = new PdfPCell(ph);

                cell.setVerticalAlignment(Element.ALIGN_BOTTOM);
                cell.setHorizontalAlignment(Element.ALIGN_MIDDLE);
                Phrase ph1 = new Phrase("ADM Number", new Font(Font.FontFamily.HELVETICA, 7, Font.NORMAL, BaseColor.BLACK));
                PdfPCell cell1 = new PdfPCell(ph1);
                cell1.setRotation(90);
                cell1.setColspan(2);
                Phrase ph2 = new Phrase("Class & Stream", new Font(Font.FontFamily.HELVETICA, 7, Font.NORMAL, BaseColor.BLACK));
                PdfPCell cell2 = new PdfPCell(ph2);
                cell2.setColspan(3);
                cell2.setRotation(90);
                Phrase ph3 = new Phrase("Name", new Font(Font.FontFamily.HELVETICA, 7, Font.NORMAL, BaseColor.BLACK));

                PdfPCell cell3 = new PdfPCell(ph3);
                cell3.setColspan(9);
                cell3.setVerticalAlignment(Element.ALIGN_BOTTOM);
                cell3.setHorizontalAlignment(Element.ALIGN_MIDDLE);
                Phrase ph4 = new Phrase("RAW MARKS X/", new Font(Font.FontFamily.HELVETICA, 7, Font.NORMAL, BaseColor.BLACK));
                PdfPCell cell4 = new PdfPCell(ph4);
                cell4.setRotation(90);
                Phrase ph5 = new Phrase("PROCESSED MARKS X/" + weight, new Font(Font.FontFamily.HELVETICA, 7, Font.NORMAL, BaseColor.BLACK));
                PdfPCell cell5 = new PdfPCell(ph5);
                cell5.setRotation(90);
                Phrase ph6 = new Phrase("PERCENTAGE", new Font(Font.FontFamily.HELVETICA, 7, Font.NORMAL, BaseColor.BLACK));
                PdfPCell cell6 = new PdfPCell(ph6);
                cell6.setRotation(90);
                Phrase ph6a = new Phrase("POINTS", new Font(Font.FontFamily.HELVETICA, 7, Font.NORMAL, BaseColor.BLACK));
                PdfPCell cell6a = new PdfPCell(ph6a);
                cell6a.setRotation(90);
                Phrase ph7 = new Phrase("GRADE", new Font(Font.FontFamily.HELVETICA, 7, Font.NORMAL, BaseColor.BLACK));
                PdfPCell cell7 = new PdfPCell(ph7);
                cell7.setRotation(90);
                Phrase ph8 = new Phrase("POSITION", new Font(Font.FontFamily.HELVETICA, 7, Font.NORMAL, BaseColor.BLACK));
                PdfPCell cell8 = new PdfPCell(ph8);
                cell8.setRotation(90);
                Phrase ph9 = new Phrase("POSITION OUT OF", new Font(Font.FontFamily.HELVETICA, 7, Font.NORMAL, BaseColor.BLACK));
                PdfPCell cell9 = new PdfPCell(ph9);
                cell9.setRotation(90);
                Phrase ph10 = new Phrase("TEACHER INTIALS", new Font(Font.FontFamily.HELVETICA, 7, Font.NORMAL, BaseColor.BLACK));
                PdfPCell cell10 = new PdfPCell(ph10);
                cell10.setRotation(90);
                cell10.setColspan(2);
                tabb.addCell(cell);
                tabb.addCell(cell1);
                tabb.addCell(cell2);
                tabb.addCell(cell3);
                tabb.addCell(cell4);
                tabb.addCell(cell5);
                tabb.addCell(cell6);
                tabb.addCell(cell6a);
                tabb.addCell(cell7);
                tabb.addCell(cell8);
                tabb.addCell(cell9);
                tabb.addCell(cell10);
                int headcount = 0;
                int previousscore = 0;
                int totalentries = 0;
                String sql2 = "Select count(*) from markstable where examcode='" + examcode + "' and classcode='" + Globals.classCode(classname) + "' and academicyear='" + academicyear + "' and subjectcode='" + subjectcode + "' order by examscore";
                ps = con.prepareStatement(sql2);
                rs = ps.executeQuery();
                if (rs.next()) {
                    totalentries = rs.getInt("count(*)");
                }
                int tiechck = 0;

                String sql3 = "Select examscore,admnumber from markstable where examcode='" + examcode + "' and academicyear='" + academicyear + "' and subjectcode='" + subjectcode + "' order by examscore desc";
                ps = con.prepareStatement(sql3);
                rs = ps.executeQuery();
                while (rs.next()) {
                    String adm = rs.getString("admNumber");

                    int examresult = rs.getInt("Examscore");
                    if (examresult == previousscore) {
                        tiechck++;
                    } else {
                        headcount++;
                        headcount += tiechck;
                        tiechck = 0;
                    }
                    String sql4 = "Update markstable  set position_per_subject='" + headcount + "', position_per_subject_out_of='" + totalentries + "' where examcode='" + examcode + "' and academicyear='" + academicyear + "' and subjectcode='" + subjectcode + "' and admnumber='" + adm + "' ";
                    ps = con.prepareStatement(sql4);
                    ps.execute();

                    previousscore = rs.getInt("Examscore");
                }

            }

            int counter = 1;
            String sql = "Select * from markstable where examcode='" + examcode + "' and classcode='" + Globals.classCode(classname) + "' and academicyear='" + academicyear + "' and streamcode='" + Globals.streamcode(stream) + "' and subjectcode='" + subjectcode + "' order by " + sort + " desc";
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                Phrase ph = new Phrase(String.valueOf(counter), new Font(Font.FontFamily.HELVETICA, 9, Font.NORMAL, BaseColor.BLACK));
                PdfPCell cell = new PdfPCell(ph);

                Phrase ph1 = new Phrase(rs.getString("admNumber"), new Font(Font.FontFamily.HELVETICA, 9, Font.NORMAL, BaseColor.BLACK));
                PdfPCell cell1 = new PdfPCell(ph1);

                cell1.setColspan(2);
                Phrase ph2 = new Phrase(classname + " " + stream, new Font(Font.FontFamily.HELVETICA, 7, Font.NORMAL, BaseColor.BLACK));
                PdfPCell cell2 = new PdfPCell(ph2);
                cell2.setColspan(3);

                Phrase ph3 = new Phrase(Globals.fullName(rs.getString("admnumber")), new Font(Font.FontFamily.HELVETICA, 9, Font.NORMAL, BaseColor.BLACK));

                PdfPCell cell3 = new PdfPCell(ph3);
                cell3.setColspan(9);

                Phrase ph4 = new Phrase(rs.getString("examScore"), new Font(Font.FontFamily.HELVETICA, 9, Font.NORMAL, BaseColor.BLACK));
                PdfPCell cell4 = new PdfPCell(ph4);

                Phrase ph5 = new Phrase(rs.getString("Convertedscore"), new Font(Font.FontFamily.HELVETICA, 9, Font.NORMAL, BaseColor.BLACK));
                PdfPCell cell5 = new PdfPCell(ph5);

                Phrase ph6 = new Phrase(rs.getString("Exampercentage"), new Font(Font.FontFamily.HELVETICA, 9, Font.NORMAL, BaseColor.BLACK));
                PdfPCell cell6 = new PdfPCell(ph6);

                Phrase ph6a = new Phrase(rs.getString("exampoints"), new Font(Font.FontFamily.HELVETICA, 9, Font.NORMAL, BaseColor.BLACK));
                PdfPCell cell6a = new PdfPCell(ph6a);

                Phrase ph7 = new Phrase(rs.getString("ExamGrade"), new Font(Font.FontFamily.HELVETICA, 9, Font.NORMAL, BaseColor.BLACK));
                PdfPCell cell7 = new PdfPCell(ph7);

                Phrase ph8 = new Phrase(rs.getString("position_per_subject"), new Font(Font.FontFamily.HELVETICA, 9, Font.NORMAL, BaseColor.BLACK));
                PdfPCell cell8 = new PdfPCell(ph8);

                Phrase ph9 = new Phrase(rs.getString("position_per_subject_out_of"), new Font(Font.FontFamily.HELVETICA, 9, Font.NORMAL, BaseColor.BLACK));
                PdfPCell cell9 = new PdfPCell(ph9);

                Phrase ph10 = new Phrase(rs.getString("class_teacher_initials"), new Font(Font.FontFamily.HELVETICA, 9, Font.NORMAL, BaseColor.BLACK));
                PdfPCell cell10 = new PdfPCell(ph10);

                cell10.setColspan(2);
                tabb.addCell(cell);
                tabb.addCell(cell1);
                tabb.addCell(cell2);
                tabb.addCell(cell3);
                tabb.addCell(cell4);
                tabb.addCell(cell5);
                tabb.addCell(cell6);
                tabb.addCell(cell6a);
                tabb.addCell(cell7);
                tabb.addCell(cell8);
                tabb.addCell(cell9);
                tabb.addCell(cell10);
                counter++;
            }
            double meanpoint = 0, meanscore = 0, meanconverted = 0, meanpercent = 0;
            String meangrade = "";
            String sql5 = "Select avg(exampoints),avg(exampercentage),avg(examscore),avg(convertedscore) from markstable where examcode='" + examcode + "' and classcode='" + Globals.classCode(classname) + "' and academicyear='" + academicyear + "' and subjectcode='" + subjectcode + "' and streamcode='" + Globals.streamcode(stream) + "' order by " + sort + " desc";
            ps = con.prepareStatement(sql5);
            rs = ps.executeQuery();
            if (rs.next()) {
                Phrase ph = new Phrase("MEAN SCORES", new Font(Font.FontFamily.HELVETICA, 9, Font.NORMAL, BaseColor.BLACK));
                PdfPCell cell = new PdfPCell(ph);
                cell.setColspan(15);
                Phrase ph4 = new Phrase(nf.format(rs.getDouble("avg(examscore)")), new Font(Font.FontFamily.HELVETICA, 9, Font.NORMAL, BaseColor.BLACK));
                PdfPCell cell4 = new PdfPCell(ph4);

                Phrase ph5 = new Phrase(nf.format(rs.getDouble("avg(convertedscore)")), new Font(Font.FontFamily.HELVETICA, 9, Font.NORMAL, BaseColor.BLACK));
                PdfPCell cell5 = new PdfPCell(ph5);

                Phrase ph6 = new Phrase(nf.format(rs.getDouble("avg(exampercentage)")), new Font(Font.FontFamily.HELVETICA, 9, Font.NORMAL, BaseColor.BLACK));
                PdfPCell cell6 = new PdfPCell(ph6);

                Phrase ph6a = new Phrase(nf.format(rs.getDouble("avg(exampoints)")), new Font(Font.FontFamily.HELVETICA, 9, Font.NORMAL, BaseColor.BLACK));
                meanpoint = rs.getDouble("avg(exampoints)");
                PdfPCell cell6a = new PdfPCell(ph6a);
                Phrase ph5a = null;

                meanscore = rs.getDouble("avg(exampercentage)");
                System.err.println(nf2.format(meanpoint));
                String sqll = "select grade from points_for_each_grade where points='" + nf2.format(meanpoint) + "' ";
                ps = con.prepareStatement(sqll);
                rs = ps.executeQuery();
                if (rs.next()) {

                    ph5a = new Phrase(rs.getString("grade"), new Font(Font.FontFamily.HELVETICA, 9, Font.NORMAL, BaseColor.BLACK));

                    meangrade = rs.getString("grade");
                }
                PdfPCell cell5a = new PdfPCell(ph5a);
                Phrase ph6aa = new Phrase("AVERAGE POINTS " + nf.format(meanpoint) + " GRADE:" + meangrade, new Font(Font.FontFamily.HELVETICA, 8, Font.NORMAL, BaseColor.BLACK));

                PdfPCell cell6aa = new PdfPCell(ph6aa);
                cell6aa.setColspan(4);

                tabb.addCell(cell);
                tabb.addCell(cell4);
                tabb.addCell(cell5);
                tabb.addCell(cell6);
                tabb.addCell(cell6a);
                tabb.addCell(cell5a);
                tabb.addCell(cell6aa);
            }
            Phrase ph = new Phrase("GRADE DISTRIBUTION", new Font(Font.FontFamily.HELVETICA, 9, Font.NORMAL, BaseColor.BLACK));
            PdfPCell cell = new PdfPCell(ph);
            cell.setColspan(24);
            tabb.addCell(cell);
            String sqla = "Select grade from points_for_each_grade order by points desc";
            ps = con.prepareStatement(sqla);
            rs = ps.executeQuery();
            while (rs.next()) {
                Phrase ph1 = new Phrase(rs.getString("grade"), new Font(Font.FontFamily.HELVETICA, 9, Font.NORMAL, BaseColor.BLACK));
                PdfPCell cell1 = new PdfPCell(ph1);
                cell1.setColspan(2);
                tabb.addCell(cell1);
            }
            String sqlb = "Select grade from points_for_each_grade  order by points desc";
            ps = con.prepareStatement(sqlb);
            rs = ps.executeQuery();
            while (rs.next()) {
                String grade = rs.getString("grade");
                String sqlc = "Select count(*) from markstable where examcode='" + examcode + "' and examgrade='" + grade + "' and classcode='" + Globals.classCode(classname) + "' and academicyear='" + academicyear + "' and subjectcode='" + subjectcode + "'  and streamcode='" + Globals.streamcode(stream) + "'";
                ps = con.prepareStatement(sqlc);
                ResultSet RS = ps.executeQuery();
                if (RS.next()) {
                    Phrase ph1 = new Phrase(RS.getString("count(*)"), new Font(Font.FontFamily.HELVETICA, 9, Font.NORMAL, BaseColor.BLACK));
                    PdfPCell cell1 = new PdfPCell(ph1);
                    cell1.setColspan(2);
                    tabb.addCell(cell1);
                }

            }
            tabb.setHeaderRows(1);
            doc.add(tabb);

            doc.close();

            if (ConfigurationIntialiser.docOpener()) {
                if (dialog == null) {

                    dialog = ReportFrame.getReportFrameInstance(new File("C:\\schoolData\\subjectResults.pdf"));

                } else {
                    dialog = null;
                    dialog = ReportFrame.getReportFrameInstance(new File("C:\\schoolData\\subjectResults.pdf"));
                    dialog.setVisible(true);

                }

            } else {

                Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler " + "C:\\schoolData\\subjectResults.pdf");

            }
        } catch (Exception sq) {
            sq.printStackTrace();
            JOptionPane.showMessageDialog(null, sq.getMessage());
        }
    }

    public static void missingMarks(String examcode, String examname, String stream, String term, String academicyear, String subjectcode, String classname, String subjectname) {
        try {

            con = DbConnection.connectDb();

            Document doc = new Document();
            com.itextpdf.text.Image image = com.itextpdf.text.Image.getInstance("C:/schooldata/logo.jpg");
            PdfWriter writer = PdfWriter.getInstance(doc, new FileOutputStream("C:\\schoolData\\missingmarks.pdf"));
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
            Paragraph pr5 = new Paragraph("MISSING MARKS".toUpperCase(), FontFactory.getFont(FontFactory.TIMES, 13, java.awt.Font.BOLD, BaseColor.BLACK));
            Chunk glue = new Chunk(new VerticalPositionMark());
            doc.addCreationDate();
            Paragraph pr7 = new Paragraph("CLASS:  " + classname.toUpperCase() + "             STREAM: " + stream.toUpperCase(), FontFactory.getFont(FontFactory.TIMES, 13, java.awt.Font.PLAIN, BaseColor.BLACK));
            pr7.add(new Chunk(glue));

            Paragraph pr8 = new Paragraph("EXAM NAME:  " + examname.toUpperCase(), FontFactory.getFont(FontFactory.TIMES, 13, java.awt.Font.PLAIN, BaseColor.BLACK));
            pr8.add(new Chunk(glue));
            pr7.add("TERM       " + term + "     ACADEMIC YEAR: " + academicyear);
            pr8.add("SUBJECT       " + subjectname + "     SUBJECT CODE: " + subjectcode);

            pr5.setIndentationLeft(150);
            pr7.setSpacingBefore(20);
            pr8.setSpacingBefore(10);
            doc.add(pr5);
            doc.add(pr7);
            doc.add(pr8);
            PdfPTable tabb = new PdfPTable(20);
            {
                tabb.setSpacingBefore(10);
                tabb.setWidthPercentage(100);
                Phrase ph = new Phrase("No.", new Font(Font.FontFamily.HELVETICA, 7, Font.NORMAL, BaseColor.BLACK));
                PdfPCell cell = new PdfPCell(ph);
                cell.setVerticalAlignment(Element.ALIGN_BOTTOM);
                cell.setHorizontalAlignment(Element.ALIGN_MIDDLE);
                tabb.addCell(cell);

                Phrase ph1 = new Phrase("ADM Number", new Font(Font.FontFamily.HELVETICA, 7, Font.NORMAL, BaseColor.BLACK));
                PdfPCell cell1 = new PdfPCell(ph1);
                cell1.setRotation(90);
                cell1.setColspan(2);
                tabb.addCell(cell1);
                Phrase ph2 = new Phrase("NAME", new Font(Font.FontFamily.HELVETICA, 7, Font.NORMAL, BaseColor.BLACK));
                PdfPCell cell2 = new PdfPCell(ph2);
                cell2.setVerticalAlignment(Element.ALIGN_BOTTOM);
                cell2.setHorizontalAlignment(Element.ALIGN_MIDDLE);
                cell2.setColspan(10);
                tabb.addCell(cell2);
                for (int k = 0; k < 7; ++k) {
                    Phrase ph3 = new Phrase("", new Font(Font.FontFamily.HELVETICA, 7, Font.NORMAL, BaseColor.BLACK));
                    PdfPCell cell3 = new PdfPCell(ph3);
                    tabb.addCell(cell3);
                }

            }

            int counter = 1;
            String sql5 = "Select admissionnumber from studentsubjectallocation,admission where subjectcode='" + subjectcode + "' and academicyear='" + academicyear + "' and admnumber=admissionnumber and currentform='" + Globals.classCode(classname) + "' and currentstream='" + Globals.streamcode(stream) + "'";
            ps = con.prepareStatement(sql5);
            rs = ps.executeQuery();
            while (rs.next()) {
                String adm = rs.getString("admissionNumber");
                String sq = "Select * from markstable where subjectcode='" + subjectcode + "' and academicyear='" + academicyear + "' and examcode='" + examcode + "' and admnumber='" + adm + "'";
                ps = con.prepareStatement(sq);
                ResultSet RS = ps.executeQuery();
                if (RS.next()) {

                } else {

                    Phrase ph = new Phrase(String.valueOf(counter), new Font(Font.FontFamily.HELVETICA, 7, Font.NORMAL, BaseColor.BLACK));
                    PdfPCell cell = new PdfPCell(ph);
                    cell.setVerticalAlignment(Element.ALIGN_BOTTOM);
                    cell.setHorizontalAlignment(Element.ALIGN_MIDDLE);
                    tabb.addCell(cell);

                    Phrase ph1 = new Phrase(adm, new Font(Font.FontFamily.HELVETICA, 7, Font.NORMAL, BaseColor.BLACK));
                    PdfPCell cell1 = new PdfPCell(ph1);

                    cell1.setColspan(2);
                    tabb.addCell(cell1);
                    Phrase ph2 = new Phrase(Globals.fullName(adm), new Font(Font.FontFamily.HELVETICA, 7, Font.NORMAL, BaseColor.BLACK));
                    PdfPCell cell2 = new PdfPCell(ph2);
                    cell2.setColspan(10);
                    tabb.addCell(cell2);

                    for (int k = 0; k < 7; ++k) {
                        Phrase ph3 = new Phrase("", new Font(Font.FontFamily.HELVETICA, 7, Font.NORMAL, BaseColor.BLACK));
                        PdfPCell cell3 = new PdfPCell(ph3);
                        tabb.addCell(cell3);
                    }
                    counter++;
                }

            }
            tabb.setHeaderRows(1);
            doc.add(tabb);

            doc.close();
            if (ConfigurationIntialiser.docOpener()) {
                if (dialog == null) {

                    dialog = ReportFrame.getReportFrameInstance(new File("C:\\schoolData\\missingmarks.pdf"));

                } else {
                    dialog = null;
                    dialog = ReportFrame.getReportFrameInstance(new File("C:\\schoolData\\missingmarks.pdf"));
                    dialog.setVisible(true);

                }

            } else {

                Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler " + "C:\\schoolData\\missingmarks.pdf");

            }

        } catch (Exception sq) {
            sq.printStackTrace();
            JOptionPane.showMessageDialog(null, sq.getMessage());
        }

    }

    public static void kcperesults(String classname) {
        try {


            con = DbConnection.connectDb();

            Document doc = new Document();

            PdfWriter writer = PdfWriter.getInstance(doc, new FileOutputStream("C:\\schoolData\\kcperesults.pdf"));
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
            Paragraph pr5 = new Paragraph("KCPE RESULTS RANKING", FontFactory.getFont(FontFactory.HELVETICA, 13, java.awt.Font.BOLD, BaseColor.BLACK));
            Chunk glue = new Chunk(new VerticalPositionMark());
            doc.addCreationDate();
            Paragraph pr7 = new Paragraph("CLASS:  " + classname.toUpperCase(), FontFactory.getFont(FontFactory.TIMES, 13, java.awt.Font.PLAIN, BaseColor.BLACK));
            pr7.add(new Chunk(glue));


            pr5.setIndentationLeft(150);
            pr7.setSpacingBefore(30);
            doc.add(pr5);
            doc.add(pr7);


            PdfPTable tabb = new PdfPTable(20);
            tabb.setSpacingBefore(10);
            tabb.setWidthPercentage(100);
            {

                Phrase ph = new Phrase("No.", new Font(Font.FontFamily.HELVETICA, 7, Font.NORMAL, BaseColor.BLACK));
                PdfPCell cell = new PdfPCell(ph);

                cell.setVerticalAlignment(Element.ALIGN_BOTTOM);
                cell.setHorizontalAlignment(Element.ALIGN_MIDDLE);
                Phrase ph1 = new Phrase("ADM Number", new Font(Font.FontFamily.HELVETICA, 7, Font.NORMAL, BaseColor.BLACK));
                PdfPCell cell1 = new PdfPCell(ph1);
                cell1.setRotation(90);
                cell1.setColspan(2);
                Phrase ph2 = new Phrase("Class & Stream", new Font(Font.FontFamily.HELVETICA, 7, Font.NORMAL, BaseColor.BLACK));
                PdfPCell cell2 = new PdfPCell(ph2);
                cell2.setColspan(3);
                cell2.setRotation(90);
                Phrase ph3 = new Phrase("Name", new Font(Font.FontFamily.HELVETICA, 7, Font.NORMAL, BaseColor.BLACK));
                PdfPCell cell3 = new PdfPCell(ph3);
                cell3.setColspan(6);
                cell3.setVerticalAlignment(Element.ALIGN_BOTTOM);
                cell3.setHorizontalAlignment(Element.ALIGN_MIDDLE);


                Phrase ph4 = new Phrase("KCPE Marks", new Font(Font.FontFamily.HELVETICA, 7, Font.NORMAL, BaseColor.BLACK));
                PdfPCell cell4 = new PdfPCell(ph4);

                cell4.setColspan(3);

                Phrase ph5 = new Phrase("position", new Font(Font.FontFamily.HELVETICA, 7, Font.NORMAL, BaseColor.BLACK));
                PdfPCell cell5 = new PdfPCell(ph5);

                cell5.setColspan(5);
                tabb.addCell(cell);
                tabb.addCell(cell1);
                tabb.addCell(cell2);
                tabb.addCell(cell3);
                tabb.addCell(cell4);
                tabb.addCell(cell5);
            }


            int counter = 0;
            String sql3 = "Select admission.admissionnumber,firstname,middlename,lastname,currentstream,position,kcperanking.kcpemarks from admission,kcperanking where  currentform='" + Globals.classCode(classname) + "'  and kcperanking.admissionnumber=admission.admissionnumber order by position ";
            ps = con.prepareStatement(sql3);
            rs = ps.executeQuery();

            while (rs.next()) {

                String adm = rs.getString("AdmissionNumber");


                counter++;
                Phrase ph = new Phrase(String.valueOf(counter), new Font(Font.FontFamily.HELVETICA, 7, Font.NORMAL, BaseColor.BLACK));
                PdfPCell cell = new PdfPCell(ph);

                cell.setVerticalAlignment(Element.ALIGN_BOTTOM);
                cell.setHorizontalAlignment(Element.ALIGN_MIDDLE);
                Phrase ph1 = new Phrase(adm, new Font(Font.FontFamily.HELVETICA, 7, Font.NORMAL, BaseColor.BLACK));
                PdfPCell cell1 = new PdfPCell(ph1);

                cell1.setColspan(2);
                Phrase ph2 = new Phrase(classname + " " + Globals.streamName(rs.getString("CurrentStream")), new Font(Font.FontFamily.HELVETICA, 9, Font.NORMAL, BaseColor.BLACK));
                PdfPCell cell2 = new PdfPCell(ph2);
                cell2.setColspan(3);

                Phrase ph3 = new Phrase(rs.getString("firstName") + "    " + rs.getString("Middlename") + "    " + rs.getString("LastName"), new Font(Font.FontFamily.HELVETICA, 9, Font.NORMAL, BaseColor.BLACK));
                PdfPCell cell3 = new PdfPCell(ph3);
                cell3.setColspan(6);
                cell3.setVerticalAlignment(Element.ALIGN_BOTTOM);
                cell3.setHorizontalAlignment(Element.ALIGN_MIDDLE);


                Phrase ph4 = new Phrase(rs.getString("kcpemarks"), new Font(Font.FontFamily.HELVETICA, 7, Font.NORMAL, BaseColor.BLACK));
                PdfPCell cell4 = new PdfPCell(ph4);

                cell4.setColspan(3);

                Phrase ph5 = new Phrase(rs.getString("Position"), new Font(Font.FontFamily.HELVETICA, 7, Font.NORMAL, BaseColor.BLACK));
                PdfPCell cell5 = new PdfPCell(ph5);

                cell5.setColspan(5);
                tabb.addCell(cell);
                tabb.addCell(cell1);
                tabb.addCell(cell2);
                tabb.addCell(cell3);
                tabb.addCell(cell4);
                tabb.addCell(cell5);


            }
            tabb.setWidthPercentage(100);
            doc.add(tabb);

            doc.close();

            if (ConfigurationIntialiser.docOpener()) {
                if (dialog == null) {

                    dialog = ReportFrame.getReportFrameInstance(new File("C:\\schoolData\\kcperesults.pdf"));

                } else {
                    dialog = null;
                    dialog = ReportFrame.getReportFrameInstance(new File("C:\\schoolData\\kcperesults.pdf"));
                    dialog.setVisible(true);

                }

            } else {

                Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler " + "C:\\schoolData\\kcperesults.pdf");

            }


        } catch (Exception sq) {
            sq.printStackTrace();
        }
    }

    public static void MeritListReport(String examname, String examcode, String academicyear, String classname, String term, String stream) {
        double[] means = null;
        int subjectentryconter = 0;

        try {


            String ranker = "Average Marks";
            if (classname.equalsIgnoreCase("Form 1")) {
                ranker = "Average Marks";

            } else {
                ranker = "Average Points";

            }
            NumberFormat nf2 = NumberFormat.getInstance();
            nf2.setMaximumFractionDigits(0);
            nf2.setMinimumFractionDigits(0);
            nf.setMaximumFractionDigits(2);
            nf.setMinimumFractionDigits(2);
            Document doc = new Document(PageSize.A4.rotate());

            com.itextpdf.text.Image image = com.itextpdf.text.Image.getInstance("C:/schooldata/logo.jpg");
            PdfWriter writer = PdfWriter.getInstance(doc, new FileOutputStream("C:\\schoolData\\meritList.pdf"));
            /*  Rotate rotation = new Rotate();
             writer.setPageEvent(rotation);
             rotation.setRotation(PdfPage.LANDSCAPE);*/
            doc.open();
            onEndPage(writer, doc);
            doc.add(new Paragraph(new Date().toString()));
            PdfPTable tab = new PdfPTable(1);

            {//school details header
                DocHead head = new DocHead();
                Image img = head.im();

                PdfPCell cell1 = new PdfPCell(img, true);
                cell1.setBorder(PdfPCell.NO_BORDER);

                //  tab.addCell(cell1);
            }
            con = DbConnection.connectDb();
            tab.setWidthPercentage(100);
            //doc.add(tab);

            String sq = "Select name from schooldetails";
            ps = con.prepareStatement(sq);
            rs = ps.executeQuery();
            String name = "";
            if (rs.next()) {
                name = rs.getString("Name");
            }
            Paragraph pr5a = new Paragraph(name.toUpperCase(), FontFactory.getFont(FontFactory.HELVETICA, 20, java.awt.Font.BOLD, BaseColor.RED));
            Paragraph pr5 = new Paragraph("Exam Results Merit List".toUpperCase(), FontFactory.getFont(FontFactory.HELVETICA, 13, java.awt.Font.BOLD, BaseColor.BLACK));
            Chunk glue = new Chunk(new VerticalPositionMark());

            Paragraph pr7 = new Paragraph("CLASS:  " + classname.toUpperCase() + "             STREAM: " + stream.toUpperCase(), FontFactory.getFont(FontFactory.TIMES, 13, java.awt.Font.PLAIN, BaseColor.BLACK));
            pr7.add(new Chunk(glue));

            Paragraph pr8 = new Paragraph("EXAM NAME:  " + examname.toUpperCase(), FontFactory.getFont(FontFactory.TIMES, 13, java.awt.Font.PLAIN, BaseColor.BLACK));
            pr8.add(new Chunk(glue));
            pr7.add("TERM       " + term + "     ACADEMIC YEAR: " + academicyear);
            Paragraph pr = new Paragraph("");

            pr5.setIndentationLeft(200);
            pr5a.setIndentationLeft(150);
//            pr7.setSpacingBefore(20);
//            pr8.setSpacingBefore(10);
            doc.add(pr5a);
            doc.add(pr5);
            doc.add(pr7);
            doc.add(pr8);
            int columns = 0;
            String sql = "Select  Count(*) from subjects";
            ps = con.prepareCall(sql);
            rs = ps.executeQuery();
            if (rs.next()) {
                columns = rs.getInt("count(*)");

            }
            columns = (columns * 4) + 52;
            PdfPTable tabb = new PdfPTable(columns);

            {


                tabb.setSpacingBefore(10);
                tabb.setWidthPercentage(100);
                Phrase ph = new Phrase("SERIAL NO.", new Font(Font.FontFamily.HELVETICA, 9, Font.NORMAL, BaseColor.BLUE));
                PdfPCell cell = new PdfPCell(ph);
                cell.setColspan(2);
                cell.setRotation(90);
                cell.setVerticalAlignment(Element.ALIGN_BOTTOM);
                cell.setHorizontalAlignment(Element.ALIGN_MIDDLE);
                Phrase ph1 = new Phrase("ADM Number", new Font(Font.FontFamily.HELVETICA, 9, Font.NORMAL, BaseColor.BLUE));
                PdfPCell cell1 = new PdfPCell(ph1);
                cell1.setRotation(90);
                cell1.setColspan(4);
                Phrase ph2 = new Phrase(" Stream", new Font(Font.FontFamily.HELVETICA, 9, Font.NORMAL, BaseColor.BLUE));
                PdfPCell cell2 = new PdfPCell(ph2);
                cell2.setColspan(4);
                cell2.setRotation(90);
                Phrase ph3 = new Phrase("Name", new Font(Font.FontFamily.HELVETICA, 12, Font.NORMAL, BaseColor.BLUE));

                PdfPCell cell3 = new PdfPCell(ph3);
                cell3.setColspan(16);
                cell3.setVerticalAlignment(Element.ALIGN_BOTTOM);
                cell3.setHorizontalAlignment(Element.ALIGN_MIDDLE);
                Phrase ph4 = new Phrase("KCPE MARKS", new Font(Font.FontFamily.HELVETICA, 9, Font.NORMAL, BaseColor.BLUE));
                PdfPCell cell4 = new PdfPCell(ph4);
                cell4.setRotation(90);
                cell4.setColspan(4);
                Phrase ph5 = new Phrase("POSITION\nTHIS TERM", new Font(Font.FontFamily.HELVETICA, 9, Font.NORMAL, BaseColor.BLUE));
                PdfPCell cell5 = new PdfPCell(ph5);
                cell5.setColspan(4);
                cell5.setRotation(90);

                Phrase ph6 = new Phrase("POSITION\nLAST TERM", new Font(Font.FontFamily.HELVETICA, 9, Font.NORMAL, BaseColor.BLUE));
                PdfPCell cell6 = new PdfPCell(ph6);
                cell6.setColspan(4);
                cell6.setRotation(90);

                Phrase ph6a = new Phrase("Total Subject Entries", new Font(Font.FontFamily.HELVETICA, 9, Font.NORMAL, BaseColor.BLUE));
                PdfPCell cell6a = new PdfPCell(ph6a);
                cell6a.setRotation(90);
                cell6a.setColspan(2);
                Phrase ph7 = new Phrase("Total Marks", new Font(Font.FontFamily.HELVETICA, 9, Font.NORMAL, BaseColor.BLUE));
                PdfPCell cell7 = new PdfPCell(ph7);
                cell7.setRotation(90);
                cell7.setColspan(2);
                Phrase ph8 = new Phrase("Total Points", new Font(Font.FontFamily.HELVETICA, 9, Font.NORMAL, BaseColor.BLUE));
                PdfPCell cell8 = new PdfPCell(ph8);
                cell8.setRotation(90);
                cell8.setColspan(2);
                Phrase ph9 = new Phrase(ranker, new Font(Font.FontFamily.HELVETICA, 9, Font.NORMAL, BaseColor.BLUE));
                PdfPCell cell9 = new PdfPCell(ph9);
                cell9.setRotation(90);
                cell9.setColspan(4);
                Phrase ph10 = new Phrase("Meangrade", new Font(Font.FontFamily.HELVETICA, 9, Font.NORMAL, BaseColor.BLUE));
                PdfPCell cell10 = new PdfPCell(ph10);
                Phrase ph11 = new Phrase("V.A.P", new Font(Font.FontFamily.HELVETICA, 9, Font.NORMAL, BaseColor.BLUE));
                PdfPCell cell11 = new PdfPCell(ph11);
                cell11.setRotation(90);
                cell11.setColspan(4);
                cell11.setRowspan(2);
                cell10.setRotation(90);
                cell10.setColspan(2);
                cell.setRowspan(2);
                cell1.setRowspan(2);
                cell2.setRowspan(2);
                cell3.setRowspan(2);
                cell4.setRowspan(2);
                cell6a.setRowspan(2);
                cell7.setRowspan(2);
                cell8.setRowspan(2);
                cell9.setRowspan(2);
                cell10.setRowspan(2);
                // tabb.addCell(cell);
                tabb.addCell(cell1);
                tabb.addCell(cell2);
                tabb.addCell(cell3);
                tabb.addCell(cell4);
                tabb.addCell(cell5);
                tabb.addCell(cell6);

                String sql2 = "Select  subjectname from subjects order by subjectcode";
                ps = con.prepareCall(sql2);
                rs = ps.executeQuery();
                while (rs.next()) {
                    Phrase ph44 = new Phrase(rs.getString("SubjectName"), new Font(Font.FontFamily.HELVETICA, 9, Font.NORMAL, BaseColor.BLUE));
                    PdfPCell cell44 = new PdfPCell(ph44);
                    cell44.setRotation(90);
                    cell44.setRowspan(2);
                    cell44.setColspan(4);
                    tabb.addCell(cell44);
                    ;

                }
                Phrase phcls = new Phrase("CLS");
                PdfPCell cls = new PdfPCell(phcls);
                cls.setColspan(2);
                Phrase phovr = new Phrase("OVRL");
                PdfPCell ovr = new PdfPCell(phovr);
                ovr.setColspan(2);

                Phrase phcls1 = new Phrase("CLS");
                PdfPCell cls1 = new PdfPCell(phcls1);
                cls1.setColspan(2);
                Phrase phovr1 = new Phrase("OVRL");
                PdfPCell ovr1 = new PdfPCell(phovr1);
                ovr1.setColspan(2);
                tabb.addCell(cell6a);
                tabb.addCell(cell7);
                tabb.addCell(cell8);
                tabb.addCell(cell9);
                tabb.addCell(cell10);
                tabb.addCell(cell11);
                tabb.addCell(cls);
                tabb.addCell(ovr);
                tabb.addCell(cls1);
                tabb.addCell(ovr1);

            }
            String rankerIndex = "";


            if (ranker.equalsIgnoreCase("Average Marks")) {
                rankerIndex = "meanmarks";
            } else {
                rankerIndex = "meanpoints";


            }

            if (stream.equalsIgnoreCase("combined")) {
                int pg = doc.getPageNumber();
                int pg2 = pg;
                int studentcounter = 0;
                String sqlstart = "Select admnumber from examanalysistable where academicyear='" + academicyear + "' and   classname='" + classname + "' and examcode='" + examcode + "' group by admnumber order by classpositionthisterm";
                ps = con.prepareStatement(sqlstart);
                rs = ps.executeQuery();
                while (rs.next()) {
                    studentcounter++;
                }
                means = new double[studentcounter];

                int counter = 0, gradableCounter = 0;
                String sqla = "Select classpositionthisterm,vap,meanmarks,STREAM,admnumber,classpositionlastterm,streampositionthisterm,streampositionlastterm,fullname,meangrade,meanpoints,totalmarks,totalpoints from examanalysistable where academicyear='" + academicyear + "' and   classname='" + classname + "' and examcode='" + examcode + "' group by admnumber order by classpositionthisterm";
                ps = con.prepareStatement(sqla);
                rs = ps.executeQuery();
                while (rs.next()) {
                    String adm = rs.getString("admnumber");
                    if (Globals.gradable(adm, examcode, academicyear)) {
                        means[counter] = rs.getFloat("meanpoints");
                        gradableCounter++;
                    }

                    counter++;
                    String grade = "", examscore = "", meangrade = "", totalmarks, totalpoints, meanpoints;
                    int entries = 0;
                    meangrade = rs.getString("meangrade");
                    totalmarks = rs.getString("totalmarks");
                    totalpoints = rs.getString("totalpoints");

                    Phrase ph = new Phrase(" \n" + String.valueOf(counter), new Font(Font.FontFamily.HELVETICA, 9, Font.NORMAL, BaseColor.BLACK));
                    PdfPCell cell = new PdfPCell(ph);
                    cell.setColspan(2);

                    Phrase ph1 = new Phrase(adm, new Font(Font.FontFamily.HELVETICA, 8, Font.NORMAL, BaseColor.BLACK));
                    PdfPCell cell1 = new PdfPCell(ph1);

                    cell1.setColspan(4);
                    Phrase ph2 = new Phrase(rs.getString("stream"), new Font(Font.FontFamily.HELVETICA, 7, Font.NORMAL, BaseColor.BLACK));
                    PdfPCell cell2 = new PdfPCell(ph2);
                    cell2.setColspan(4);

                    Phrase ph3 = new Phrase(rs.getString("fullName"), new Font(Font.FontFamily.HELVETICA, 7, Font.NORMAL, BaseColor.BLACK));

                    PdfPCell cell3 = new PdfPCell(ph3);
                    cell3.setColspan(16);
                    Phrase ph4 = new Phrase(Globals.kcpeMarks(adm), new Font(Font.FontFamily.HELVETICA, 9, Font.NORMAL, BaseColor.BLACK));
                    PdfPCell cell4 = new PdfPCell(ph4);

                    cell4.setColspan(4);

                    Phrase phcls = new Phrase(rs.getString("Streampositionthisterm"), new Font(Font.FontFamily.HELVETICA, 9, Font.NORMAL, BaseColor.BLACK));
                    PdfPCell cls = new PdfPCell(phcls);
                    cls.setColspan(2);
                    Phrase phovr = new Phrase(rs.getString("classpositionthisterm"), new Font(Font.FontFamily.HELVETICA, 9, Font.NORMAL, BaseColor.BLACK));
                    PdfPCell ovr = new PdfPCell(phovr);
                    ovr.setColspan(2);

                    Phrase phcls1 = new Phrase(rs.getString("Streampositionlastterm"), new Font(Font.FontFamily.HELVETICA, 9, Font.NORMAL, BaseColor.BLACK));
                    PdfPCell cls1 = new PdfPCell(phcls1);
                    cls1.setColspan(2);
                    Phrase phovr1 = new Phrase(rs.getString("classpositionlastterm"), new Font(Font.FontFamily.HELVETICA, 9, Font.NORMAL, BaseColor.BLACK));
                    PdfPCell ovr1 = new PdfPCell(phovr1);
                    ovr1.setColspan(2);

                    // tabb.addCell(cell);
                    tabb.addCell(cell1);
                    tabb.addCell(cell2);
                    tabb.addCell(cell3);
                    tabb.addCell(cell4);
                    tabb.addCell(cls);
                    tabb.addCell(ovr);
                    tabb.addCell(cls1);
                    tabb.addCell(ovr1);

                    int subcounter = 0;
                    String sqlb = "SElect Distinct subjectcode from subjects order by subjectcode";
                    ps = con.prepareCall(sqlb);
                    ResultSet rsb = ps.executeQuery();

                    while (rsb.next()) {
                        String subcode = rsb.getString("Subjectcode");

                        String sqlc = "Select  exampercentage,subjectexamgrade from examanalysistable where academicyear='" + academicyear + "' and examcode='" + examcode + "'  and subjectcode='" + subcode + "' and admnumber='" + adm + "' and classname='" + classname + "'";
                        ps = con.prepareStatement(sqlc);
                        ResultSet rsc = ps.executeQuery();
                        if (rsc.next()) {
                            examscore = rsc.getString("ExamPercentage");
                            grade = rsc.getString("subjectexamgrade");
                            subcounter++;

                        } else {
                            if (Globals.takingSubject(adm, academicyear, subcode)) {
                                examscore = "Y";
                            } else {
                                examscore = "";
                            }
                            grade = "";
                        }
                        Phrase ph44 = new Phrase(examscore, new Font(Font.FontFamily.HELVETICA, 9, Font.NORMAL, BaseColor.BLACK));
                        PdfPCell cell44 = new PdfPCell(ph44);
                        cell44.setColspan(2);
                        Phrase ph44g = new Phrase(grade, new Font(Font.FontFamily.HELVETICA, 9, Font.NORMAL, BaseColor.BLACK));
                        PdfPCell cell44g = new PdfPCell(ph44g);
                        cell44g.setColspan(2);
                        tabb.addCell(cell44);
                        tabb.addCell(cell44g);
                    }

                    Phrase ph6a = new Phrase(String.valueOf(subcounter), new Font(Font.FontFamily.HELVETICA, 9, Font.NORMAL, BaseColor.BLACK));
                    PdfPCell cell6a = new PdfPCell(ph6a);

                    cell6a.setColspan(2);
                    tabb.addCell(cell6a);
                    Phrase ph7 = new Phrase(rs.getString("totalmarks"), new Font(Font.FontFamily.HELVETICA, 7, Font.NORMAL, BaseColor.RED));
                    PdfPCell cell7 = new PdfPCell(ph7);

                    cell7.setColspan(2);
                    tabb.addCell(cell7);
                    Phrase ph8 = new Phrase(rs.getString("totalpoints"), new Font(Font.FontFamily.HELVETICA, 7, Font.NORMAL, BaseColor.RED));
                    PdfPCell cell8 = new PdfPCell(ph8);
                    cell8.setColspan(2);
                    tabb.addCell(cell8);

                    Phrase ph9 = new Phrase(rs.getString(rankerIndex), new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL, BaseColor.RED));
                    PdfPCell cell9 = new PdfPCell(ph9);
                    cell9.setColspan(4);
                    tabb.addCell(cell9);
                    Phrase ph10 = new Phrase(rs.getString("meangrade"), new Font(Font.FontFamily.HELVETICA, 8, Font.NORMAL, BaseColor.RED));
                    PdfPCell cell10 = new PdfPCell(ph10);
                    cell10.setColspan(2);
                    tabb.addCell(cell10);


                    Phrase ph11 = new Phrase(rs.getString("vap"), new Font(Font.FontFamily.HELVETICA, 9, Font.NORMAL, BaseColor.BLUE));
                    PdfPCell cell11 = new PdfPCell(ph11);

                    cell11.setColspan(4);

                    tabb.addCell(cell11);

                }

                int i = 0;
                String[] gradestore = new String[columns / 4];
                double meanpoint = 0, meanscore = 0, meanconverted = 0, meanpercent = 0;
                String meangrade = "";
                {
                    Phrase ph = new Phrase("SUBJECT MEAN SCORES", new Font(Font.FontFamily.HELVETICA, 11, Font.NORMAL, BaseColor.BLACK));
                    PdfPCell cell = new PdfPCell(ph);
                    cell.setColspan(36);
                    tabb.addCell(cell);
                    String sqlz = "Select distinct subjectcode from subjects order by subjectcode";
                    ps = con.prepareStatement(sqlz);
                    ResultSet rx = ps.executeQuery();
                    while (rx.next()) {
                        String subcode = rx.getString("subjectcode");

                        String sql5 = "Select avg(Sujectexampoints) from examanalysistable where examcode='" + examcode + "' and classname='" + classname + "' and subjectcode='" + subcode + "'  and academicyear='" + academicyear + "'   order by classpositionthisterm";
                        ps = con.prepareStatement(sql5);
                        ResultSet r = ps.executeQuery();
                        if (r.next()) {

                            meanscore = r.getDouble("avg(Sujectexampoints)");

                            if (meanscore == 0.0) {

                                Phrase ph6 = new Phrase("", new Font(Font.FontFamily.HELVETICA, 7, Font.NORMAL, BaseColor.BLACK));
                                PdfPCell cell6 = new PdfPCell(ph6);
                                cell6.setColspan(4);
                                tabb.addCell(cell6);
                                gradestore[i] = "";
                            } else {
                                subjectentryconter++;
                                Phrase ph6 = new Phrase(nf.format(meanscore), new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.BOLD, BaseColor.RED));
                                PdfPCell cell6 = new PdfPCell(ph6);
                                cell6.setColspan(4);
                                tabb.addCell(cell6);
                                String sqll = "Select grade from points_for_each_grade where points='" + nf2.format(meanscore) + "' ";
                                ps = con.prepareStatement(sqll);
                                ResultSet vv = ps.executeQuery();
                                if (vv.next()) {

                                    meangrade = vv.getString("grade");

                                    gradestore[i] = meangrade;

                                }

                            }
                        }

                        i++;
                    }

                    Phrase ph6a = new Phrase("", new Font(Font.FontFamily.HELVETICA, 4, Font.NORMAL, BaseColor.BLACK));
                    PdfPCell cell6a = new PdfPCell(ph6a);

                    cell6a.setColspan(2);
                    tabb.addCell(cell6a);
                    Phrase ph7 = new Phrase("", new Font(Font.FontFamily.HELVETICA, 4, Font.NORMAL, BaseColor.BLACK));
                    PdfPCell cell7 = new PdfPCell(ph7);

                    cell7.setColspan(2);
                    tabb.addCell(cell7);
                    Phrase ph8 = new Phrase("", new Font(Font.FontFamily.HELVETICA, 4, Font.NORMAL, BaseColor.BLACK));
                    PdfPCell cell8 = new PdfPCell(ph8);
                    cell8.setColspan(2);
                    tabb.addCell(cell8);
                    Phrase ph9 = new Phrase("", new Font(Font.FontFamily.HELVETICA, 4, Font.NORMAL, BaseColor.BLACK));
                    PdfPCell cell9 = new PdfPCell(ph9);
                    cell9.setColspan(4);
                    tabb.addCell(cell9);
                    Phrase ph10 = new Phrase("", new Font(Font.FontFamily.HELVETICA, 4, Font.NORMAL, BaseColor.BLACK));
                    PdfPCell cell10 = new PdfPCell(ph10);
                    cell10.setColspan(6);
                    tabb.addCell(cell10);
                }
                {
                    Phrase ph = new Phrase("SUBJECT MEAN GRADES", new Font(Font.FontFamily.TIMES_ROMAN, 11, Font.BOLD, BaseColor.BLACK));
                    PdfPCell cell = new PdfPCell(ph);
                    cell.setColspan(36);
                    tabb.addCell(cell);
                    for (int k = 0; k < gradestore.length; ++k) {
                        Phrase ph6 = new Phrase(gradestore[k], new Font(Font.FontFamily.HELVETICA, 11, Font.BOLD, BaseColor.RED));
                        PdfPCell cell6 = new PdfPCell(ph6);
                        cell6.setColspan(4);
                        tabb.addCell(cell6);
                    }

                    Phrase ph6a = new Phrase("", new Font(Font.FontFamily.HELVETICA, 4, Font.NORMAL, BaseColor.BLACK));
                    PdfPCell cell6a = new PdfPCell(ph6a);

                    cell6a.setColspan(2);
                    tabb.addCell(cell6a);
                    Phrase ph7 = new Phrase("", new Font(Font.FontFamily.HELVETICA, 4, Font.NORMAL, BaseColor.BLACK));
                    PdfPCell cell7 = new PdfPCell(ph7);

                    cell7.setColspan(2);
                    tabb.addCell(cell7);
                    Phrase ph8 = new Phrase("", new Font(Font.FontFamily.HELVETICA, 4, Font.NORMAL, BaseColor.BLACK));
                    PdfPCell cell8 = new PdfPCell(ph8);
                    cell8.setColspan(2);
                    tabb.addCell(cell8);
                    Phrase ph9 = new Phrase("", new Font(Font.FontFamily.HELVETICA, 4, Font.NORMAL, BaseColor.BLACK));
                    PdfPCell cell9 = new PdfPCell(ph9);
                    cell9.setColspan(4);
                    tabb.addCell(cell9);
                    Phrase ph10 = new Phrase("", new Font(Font.FontFamily.HELVETICA, 4, Font.NORMAL, BaseColor.BLACK));
                    PdfPCell cell10 = new PdfPCell(ph10);
                    cell10.setColspan(6);
                    tabb.addCell(cell10);
                }
                double classmean = 0;

                for (int h = 0; h < means.length; ++h) {
                    classmean += means[h];
                }
                classmean = (classmean / gradableCounter);
                String sqll = "Select grade from points_for_each_grade where points='" + nf2.format(classmean) + "' ";
                ps = con.prepareStatement(sqll);
                ResultSet vv = ps.executeQuery();
                if (vv.next()) {
                    pr = new Paragraph("Overall Mean Score:   ".toUpperCase() + nf.format(classmean) + "  Mean Grade: ".toUpperCase() + vv.getString("grade"), FontFactory.getFont(FontFactory.HELVETICA, 13, java.awt.Font.BOLD, BaseColor.BLACK));

                }
                tabb.setHeaderRows(2);

                doc.add(tabb);

                pr.setSpacingBefore(20);
                doc.add(pr);
                doc.close();

            } else {

                int studentcounter = 0;
                String sqlstart = "Select admnumber from examanalysistable where academicyear='" + academicyear + "' and   classname='" + classname + "' and examcode='" + examcode + "' group by admnumber order by classpositionthisterm";
                ps = con.prepareStatement(sqlstart);
                rs = ps.executeQuery();
                while (rs.next()) {
                    studentcounter++;
                }
                means = new double[studentcounter];
                int counter = 0, gradableCounter = 0;
                String sqla = "Select classpositionthisterm,vap,meanmarks,admnumber,classpositionlastterm,streampositionthisterm,streampositionlastterm,fullname,meangrade,meanpoints,totalmarks,totalpoints from examanalysistable where academicyear='" + academicyear + "' and  classname='" + classname + "' and examcode='" + examcode + "' and stream='" + stream + "' group by admnumber order by classpositionthisterm";
                ps = con.prepareStatement(sqla);
                rs = ps.executeQuery();
                while (rs.next()) {
                    String adm = rs.getString("admnumber");
                    if (Globals.gradable(adm, examcode, academicyear)) {

                        means[counter] = rs.getFloat("meanpoints");
                        gradableCounter++;
                    }
                    counter++;
                    String grade = "", examscore = "", meangrade = "", totalmarks, totalpoints, meanpoints;
                    int entries = 0;
                    meangrade = rs.getString("meangrade");
                    totalmarks = rs.getString("totalmarks");
                    totalpoints = rs.getString("totalpoints");

                    Phrase ph = new Phrase(String.valueOf(counter), new Font(Font.FontFamily.HELVETICA, 9, Font.NORMAL, BaseColor.BLACK));
                    PdfPCell cell = new PdfPCell(ph);
                    cell.setColspan(2);

                    Phrase ph1 = new Phrase(adm, new Font(Font.FontFamily.HELVETICA, 9, Font.NORMAL, BaseColor.BLACK));
                    PdfPCell cell1 = new PdfPCell(ph1);

                    cell1.setColspan(4);
                    Phrase ph2 = new Phrase(stream, new Font(Font.FontFamily.HELVETICA, 9, Font.NORMAL, BaseColor.BLACK));
                    PdfPCell cell2 = new PdfPCell(ph2);
                    cell2.setColspan(4);

                    Phrase ph3 = new Phrase(rs.getString("fullName"), new Font(Font.FontFamily.HELVETICA, 7, Font.NORMAL, BaseColor.BLACK));

                    PdfPCell cell3 = new PdfPCell(ph3);
                    cell3.setColspan(16);
                    Phrase ph4 = new Phrase(Globals.kcpeMarks(adm), new Font(Font.FontFamily.HELVETICA, 9, Font.NORMAL, BaseColor.BLACK));
                    PdfPCell cell4 = new PdfPCell(ph4);

                    cell4.setColspan(4);

                    Phrase phcls = new Phrase(rs.getString("Streampositionthisterm"), new Font(Font.FontFamily.HELVETICA, 9, Font.NORMAL, BaseColor.BLACK));
                    PdfPCell cls = new PdfPCell(phcls);
                    cls.setColspan(2);
                    Phrase phovr = new Phrase(rs.getString("classpositionthisterm"), new Font(Font.FontFamily.HELVETICA, 9, Font.NORMAL, BaseColor.BLACK));
                    PdfPCell ovr = new PdfPCell(phovr);
                    ovr.setColspan(2);

                    Phrase phcls1 = new Phrase(rs.getString("Streampositionlastterm"), new Font(Font.FontFamily.HELVETICA, 9, Font.NORMAL, BaseColor.BLACK));
                    PdfPCell cls1 = new PdfPCell(phcls1);
                    cls1.setColspan(2);
                    Phrase phovr1 = new Phrase(rs.getString("classpositionlastterm"), new Font(Font.FontFamily.HELVETICA, 9, Font.NORMAL, BaseColor.BLACK));
                    PdfPCell ovr1 = new PdfPCell(phovr1);
                    ovr1.setColspan(2);

                    // tabb.addCell(cell);
                    tabb.addCell(cell1);
                    tabb.addCell(cell2);
                    tabb.addCell(cell3);
                    tabb.addCell(cell4);
                    tabb.addCell(cls);
                    tabb.addCell(ovr);
                    tabb.addCell(cls1);
                    tabb.addCell(ovr1);

                    int subcounter = 0;
                    String sqlb = "SElect Distinct subjectcode from subjects order by subjectcode";
                    ps = con.prepareCall(sqlb);
                    ResultSet rsb = ps.executeQuery();

                    while (rsb.next()) {
                        String subcode = rsb.getString("Subjectcode");

                        String sqlc = "Select  exampercentage,subjectexamgrade from examanalysistable where academicyear='" + academicyear + "' and examcode='" + examcode + "'  and subjectcode='" + subcode + "' and admnumber='" + adm + "' and classname='" + classname + "'";
                        ps = con.prepareStatement(sqlc);
                        ResultSet rsc = ps.executeQuery();
                        if (rsc.next()) {
                            examscore = rsc.getString("ExamPercentage");
                            grade = rsc.getString("subjectexamgrade");
                            subcounter++;

                        } else {
                            if (Globals.takingSubject(adm, academicyear, subcode)) {
                                examscore = "Y";
                            } else {
                                examscore = "";
                            }
                            grade = "";
                        }
                        Phrase ph44 = new Phrase(examscore, new Font(Font.FontFamily.HELVETICA, 9, Font.NORMAL, BaseColor.BLACK));
                        PdfPCell cell44 = new PdfPCell(ph44);
                        cell44.setColspan(2);
                        Phrase ph44g = new Phrase(grade, new Font(Font.FontFamily.HELVETICA, 9, Font.NORMAL, BaseColor.BLACK));
                        PdfPCell cell44g = new PdfPCell(ph44g);
                        cell44g.setColspan(2);
                        tabb.addCell(cell44);
                        tabb.addCell(cell44g);
                    }

                    Phrase ph6a = new Phrase(String.valueOf(subcounter), new Font(Font.FontFamily.HELVETICA, 9, Font.NORMAL, BaseColor.RED));
                    PdfPCell cell6a = new PdfPCell(ph6a);

                    cell6a.setColspan(2);
                    tabb.addCell(cell6a);
                    Phrase ph7 = new Phrase(rs.getString("totalmarks"), new Font(Font.FontFamily.HELVETICA, 7, Font.NORMAL, BaseColor.RED));
                    PdfPCell cell7 = new PdfPCell(ph7);

                    cell7.setColspan(2);
                    tabb.addCell(cell7);
                    Phrase ph8 = new Phrase(rs.getString("totalpoints"), new Font(Font.FontFamily.HELVETICA, 7, Font.NORMAL, BaseColor.RED));
                    PdfPCell cell8 = new PdfPCell(ph8);
                    cell8.setColspan(2);
                    tabb.addCell(cell8);
                    Phrase ph9 = new Phrase(rs.getString(rankerIndex), new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL, BaseColor.RED));
                    PdfPCell cell9 = new PdfPCell(ph9);
                    cell9.setColspan(4);
                    tabb.addCell(cell9);
                    Phrase ph10 = new Phrase(rs.getString("meangrade"), new Font(Font.FontFamily.HELVETICA, 9, Font.NORMAL, BaseColor.RED));
                    PdfPCell cell10 = new PdfPCell(ph10);
                    cell10.setColspan(2);
                    tabb.addCell(cell10);
                    Phrase ph11 = new Phrase(rs.getString("vap"), new Font(Font.FontFamily.HELVETICA, 9, Font.NORMAL, BaseColor.BLUE));
                    PdfPCell cell11 = new PdfPCell(ph11);

                    cell11.setColspan(4);

                    tabb.addCell(cell11);

                }
                int i = 0;
                String[] gradestore = new String[columns / 4];
                double meanpoint = 0, meanscore = 0, meanconverted = 0, meanpercent = 0;
                String meangrade = "";
                {
                    Phrase ph = new Phrase("SUBJECT MEAN SCORES", new Font(Font.FontFamily.HELVETICA, 11, Font.NORMAL, BaseColor.BLACK));
                    PdfPCell cell = new PdfPCell(ph);
                    cell.setColspan(36);
                    tabb.addCell(cell);
                    String sqlz = "Select distinct subjectcode from subjects order by subjectcode";
                    ps = con.prepareStatement(sqlz);
                    ResultSet rx = ps.executeQuery();
                    while (rx.next()) {
                        String subcode = rx.getString("subjectcode");

                        String sql5 = "Select avg(Sujectexampoints) from examanalysistable where examcode='" + examcode + "' and classname='" + classname + "' and stream='" + stream + "' and subjectcode='" + subcode + "'  and academicyear='" + academicyear + "'   order by classpositionthisterm";
                        ps = con.prepareStatement(sql5);
                        ResultSet r = ps.executeQuery();
                        if (r.next()) {

                            meanscore = r.getDouble("avg(Sujectexampoints)");

                            if (meanscore == 0.0) {

                                Phrase ph6 = new Phrase("", new Font(Font.FontFamily.HELVETICA, 7, Font.NORMAL, BaseColor.RED));
                                PdfPCell cell6 = new PdfPCell(ph6);
                                cell6.setColspan(4);
                                tabb.addCell(cell6);
                                gradestore[i] = "";
                            } else {
                                subjectentryconter++;
                                Phrase ph6 = new Phrase(nf.format(meanscore), new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.BOLD, BaseColor.RED));
                                PdfPCell cell6 = new PdfPCell(ph6);
                                cell6.setColspan(4);
                                tabb.addCell(cell6);
                                String sqll = "Select grade from points_for_each_grade  where points='" + nf2.format(meanscore) + "' ";
                                ps = con.prepareStatement(sqll);
                                ResultSet vv = ps.executeQuery();
                                if (vv.next()) {

                                    meangrade = vv.getString("grade");

                                    gradestore[i] = meangrade;

                                }

                            }
                        }

                        i++;
                    }

                    Phrase ph6a = new Phrase("", new Font(Font.FontFamily.HELVETICA, 4, Font.NORMAL, BaseColor.BLACK));
                    PdfPCell cell6a = new PdfPCell(ph6a);

                    cell6a.setColspan(2);
                    tabb.addCell(cell6a);
                    Phrase ph7 = new Phrase("", new Font(Font.FontFamily.HELVETICA, 4, Font.NORMAL, BaseColor.BLACK));
                    PdfPCell cell7 = new PdfPCell(ph7);

                    cell7.setColspan(2);
                    tabb.addCell(cell7);
                    Phrase ph8 = new Phrase("", new Font(Font.FontFamily.HELVETICA, 4, Font.NORMAL, BaseColor.BLACK));
                    PdfPCell cell8 = new PdfPCell(ph8);
                    cell8.setColspan(2);
                    tabb.addCell(cell8);
                    Phrase ph9 = new Phrase("", new Font(Font.FontFamily.HELVETICA, 4, Font.NORMAL, BaseColor.BLACK));
                    PdfPCell cell9 = new PdfPCell(ph9);
                    cell9.setColspan(4);
                    tabb.addCell(cell9);
                    Phrase ph10 = new Phrase("", new Font(Font.FontFamily.HELVETICA, 4, Font.NORMAL, BaseColor.BLACK));
                    PdfPCell cell10 = new PdfPCell(ph10);
                    cell10.setColspan(6);
                    tabb.addCell(cell10);
                }
                {
                    Phrase ph = new Phrase("SUBJECT MEAN GRADES", new Font(Font.FontFamily.TIMES_ROMAN, 11, Font.BOLD, BaseColor.BLACK));
                    PdfPCell cell = new PdfPCell(ph);
                    cell.setColspan(36);
                    tabb.addCell(cell);
                    for (int k = 0; k < gradestore.length; ++k) {
                        Phrase ph6 = new Phrase(gradestore[k], new Font(Font.FontFamily.TIMES_ROMAN, 11, Font.BOLD, BaseColor.RED));
                        PdfPCell cell6 = new PdfPCell(ph6);
                        cell6.setColspan(4);
                        tabb.addCell(cell6);
                    }

                    Phrase ph6a = new Phrase("", new Font(Font.FontFamily.HELVETICA, 4, Font.NORMAL, BaseColor.BLACK));
                    PdfPCell cell6a = new PdfPCell(ph6a);

                    cell6a.setColspan(2);
                    tabb.addCell(cell6a);
                    Phrase ph7 = new Phrase("", new Font(Font.FontFamily.HELVETICA, 4, Font.NORMAL, BaseColor.BLACK));
                    PdfPCell cell7 = new PdfPCell(ph7);

                    cell7.setColspan(2);
                    tabb.addCell(cell7);
                    Phrase ph8 = new Phrase("", new Font(Font.FontFamily.HELVETICA, 4, Font.NORMAL, BaseColor.BLACK));
                    PdfPCell cell8 = new PdfPCell(ph8);
                    cell8.setColspan(2);
                    tabb.addCell(cell8);
                    Phrase ph9 = new Phrase("", new Font(Font.FontFamily.HELVETICA, 4, Font.NORMAL, BaseColor.BLACK));
                    PdfPCell cell9 = new PdfPCell(ph9);
                    cell9.setColspan(4);
                    tabb.addCell(cell9);
                    Phrase ph10 = new Phrase("", new Font(Font.FontFamily.HELVETICA, 4, Font.NORMAL, BaseColor.BLACK));
                    PdfPCell cell10 = new PdfPCell(ph10);
                    cell10.setColspan(6);
                    tabb.addCell(cell10);
                }

                double classmean = 0;

                for (int h = 0; h < means.length; ++h) {
                    classmean += means[h];
                }
                classmean = (classmean / gradableCounter);
                String sqll = "Select grade from points_for_each_grade where points='" + nf2.format(classmean) + "' ";
                ps = con.prepareStatement(sqll);
                ResultSet vv = ps.executeQuery();
                if (vv.next()) {
                    pr = new Paragraph("Overall Stream Mean Score:   ".toUpperCase() + nf.format(classmean) + "  Mean Grade: ".toUpperCase() + vv.getString("grade"), FontFactory.getFont(FontFactory.HELVETICA, 13, java.awt.Font.BOLD, BaseColor.BLACK));

                }
                tabb.setHeaderRows(2);
                doc.add(tabb);
                pr.setSpacingBefore(20);
                doc.add(pr);
                doc.close();
            }

            if (rs.previous()) {

                if (ConfigurationIntialiser.docOpener()) {
                    if (dialog == null) {

                        dialog = ReportFrame.getReportFrameInstance(new File("C:\\schoolData\\meritList.pdf"));

                    } else {
                        dialog = null;
                        dialog = ReportFrame.getReportFrameInstance(new File("C:\\schoolData\\meritList.pdf"));
                        dialog.setVisible(true);

                    }

                } else {

                    Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler " + "C:\\schoolData\\MeritList.pdf");

                }

            } else {
                JOptionPane.showMessageDialog(null, "No Records Found");
            }

        } catch (DocumentException | HeadlessException | IOException | SQLException sq) {
            sq.printStackTrace();
            JOptionPane.showMessageDialog(null, sq.getMessage());
        }

    }

    public static class Rotate extends PdfPageEventHelper {

        protected PdfNumber rotation = PdfPage.LANDSCAPE;

        public void setRotation(PdfNumber rotation) {
            this.rotation = rotation;
        }

        @Override
        public void onEndPage(PdfWriter writer, Document document) {
            writer.addPageDictEntry(PdfName.ROTATE, rotation);
        }
    }


    public static void bookStatistics(String bookSub, String booktype, String bookCodition, String studentClass, String studentSubject) {
        try {


            Document doc = new Document(PageSize.A4.rotate());
            com.itextpdf.text.Image image = com.itextpdf.text.Image.getInstance("C:/schooldata/logo.jpg");
            PdfWriter writer = PdfWriter.getInstance(doc, new FileOutputStream("C:\\schoolData\\Book Statistics.pdf"));
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
            Paragraph pr5 = new Paragraph("Book Statistics Extract".toUpperCase(), FontFactory.getFont(FontFactory.HELVETICA, 13, java.awt.Font.BOLD, BaseColor.BLACK));
            Chunk glue = new Chunk(new VerticalPositionMark());
            doc.addCreationDate();

            pr5.setIndentationLeft(150);
            doc.add(pr5);


            doc.close();

            if (ConfigurationIntialiser.docOpener()) {
                if (dialog == null) {

                    dialog = ReportFrame.getReportFrameInstance(new File("C:\\schoolData\\Book Statistics.pdf"));

                } else {
                    dialog = null;
                    dialog = ReportFrame.getReportFrameInstance(new File("C:\\schoolData\\Book Statistics.pdf"));
                    dialog.setVisible(true);

                }

            } else {

                Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler " + "C:\\schoolData\\Book Statistics.pdf");

            }


        } catch (Exception sq) {
            sq.printStackTrace();
        }
    }

}
