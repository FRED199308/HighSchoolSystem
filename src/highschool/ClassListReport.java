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

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Date;
import javax.imageio.ImageIO;
import javax.swing.JDialog;

import javax.swing.JOptionPane;

/**
 * @author FRED
 */
public class ClassListReport {


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

    public static void subjectClassList(String subjectcode, String stream, String classname) {

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
            Paragraph pr5 = new Paragraph("SUBJECT CLASS LIST", FontFactory.getFont(FontFactory.HELVETICA, 13, java.awt.Font.BOLD, BaseColor.BLACK));
            Chunk glue = new Chunk(new VerticalPositionMark());
            doc.addCreationDate();
            Paragraph pr7 = new Paragraph("CLASS:  " + classname.toUpperCase() + "                 STREAM: " + stream.toUpperCase(), FontFactory.getFont(FontFactory.TIMES, 13, java.awt.Font.PLAIN, BaseColor.BLACK));
            pr7.add(new Chunk(glue));
            Paragraph pr8 = new Paragraph("SUBJECT: " + Globals.subjectName(subjectcode), FontFactory.getFont(FontFactory.TIMES, 13, java.awt.Font.PLAIN, BaseColor.BLACK));

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
                String sql3 = "Select admissionnumber,firstname,middlename,lastname,currentstream from admission where  currentform='" + Globals.classCode(classname) + "'  " + Globals.sortcode + " ";
                ps = con.prepareStatement(sql3);
                rs = ps.executeQuery();

                while (rs.next()) {

                    String adm = rs.getString("AdmissionNumber");


                    String sql5 = "Select * from studentsubjectallocation where subjectcode='" + subjectcode + "' and academicyear='" + Globals.academicYear() + "' and admnumber='" + adm + "'";
                    ps = con.prepareStatement(sql5);
                    ResultSet RX = ps.executeQuery();
                    if (RX.next()) {
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
                    } else {

                    }


                }
            } else {

                int counter = 0;
                String sql3 = "Select admissionnumber,firstname,middlename,lastname from admission where  currentform='" + Globals.classCode(classname) + "' and currentstream='" + Globals.streamcode(stream) + "' " + Globals.sortcode + " ";
                ps = con.prepareStatement(sql3);
                rs = ps.executeQuery();

                while (rs.next()) {

                    String adm = rs.getString("AdmissionNumber");


                    String sql5 = "Select * from studentsubjectallocation where subjectcode='" + subjectcode + "' and academicyear='" + Globals.academicYear() + "' and admnumber='" + adm + "'";
                    ps = con.prepareStatement(sql5);
                    ResultSet RX = ps.executeQuery();
                    if (RX.next()) {
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
                    } else {

                    }


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

    public static void generalClassList(String stream, String classname, String gender) {
        try {

            con = DbConnection.connectDb();
            Document doc = new Document();
            com.itextpdf.text.Image image = com.itextpdf.text.Image.getInstance("C:/schooldata/logo.jpg");
            PdfWriter writer = PdfWriter.getInstance(doc, new FileOutputStream("C:\\schoolData\\generalclasslist.pdf"));
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

            Paragraph pr5 = new Paragraph("GENERAL CLASS LIST", FontFactory.getFont(FontFactory.HELVETICA, 13, java.awt.Font.BOLD, BaseColor.BLACK));
            Chunk glue = new Chunk(new VerticalPositionMark());
            doc.addCreationDate();
            Paragraph pr7 = new Paragraph("CLASS:  " + classname.toUpperCase(), FontFactory.getFont(FontFactory.TIMES, 13, java.awt.Font.PLAIN, BaseColor.BLACK));


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
                Phrase ph1 = new Phrase("ADM Number", new Font(Font.FontFamily.HELVETICA, 9, Font.NORMAL, BaseColor.BLACK));
                PdfPCell cell1 = new PdfPCell(ph1);
                cell1.setRotation(90);
                cell1.setColspan(2);
                Phrase ph2 = new Phrase("Class & Stream", new Font(Font.FontFamily.HELVETICA, 9, Font.NORMAL, BaseColor.BLACK));
                PdfPCell cell2 = new PdfPCell(ph2);
                cell2.setColspan(3);
                cell2.setRotation(90);
                Phrase ph3 = new Phrase("Name", new Font(Font.FontFamily.HELVETICA, 9, Font.NORMAL, BaseColor.BLACK));
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
                if (gender.equalsIgnoreCase("all")) {
                    int counter = 0;
                    String sql3 = "Select admissionnumber,firstname,middlename,lastname,currentstream from admission where  currentform='" + Globals.classCode(classname) + "'  " + Globals.sortcode + " ";
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
                    String sql3 = "Select admissionnumber,firstname,middlename,lastname,currentstream from admission where  currentform='" + Globals.classCode(classname) + "' and gender='" + gender + "'  " + Globals.sortcode + " ";
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

                }


            } else {

                if (gender.equalsIgnoreCase("All")) {

                    int counter = 0;
                    String sql3 = "Select admissionnumber,firstname,middlename,lastname from admission where  currentform='" + Globals.classCode(classname) + "' and currentstream='" + Globals.streamcode(stream) + "' " + Globals.sortcode + " ";
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
                } else {


                    int counter = 0;
                    String sql3 = "Select admissionnumber,firstname,middlename,lastname from admission where  currentform='" + Globals.classCode(classname) + "' and currentstream='" + Globals.streamcode(stream) + "' and gender='" + gender + "' " + Globals.sortcode + " ";
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
                        Phrase ph2 = new Phrase(classname + " " + stream, new Font(Font.FontFamily.HELVETICA, 7, Font.NORMAL, BaseColor.BLACK));
                        PdfPCell cell2 = new PdfPCell(ph2);
                        cell2.setColspan(3);

                        Phrase ph3 = new Phrase(rs.getString("firstName") + "    " + rs.getString("Middlename") + "    " + rs.getString("LastName"), new Font(Font.FontFamily.HELVETICA, 7, Font.NORMAL, BaseColor.BLACK));
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


            }
            tabb.setHeaderRows(1);
            doc.add(tabb);
            doc.close();


            if (ConfigurationIntialiser.docOpener()) {
                if (dialog == null) {


                    dialog = ReportFrame.getReportFrameInstance(new File("C:\\schoolData\\generalclasslist.pdf"));


                } else {
                    dialog = null;
                    dialog = ReportFrame.getReportFrameInstance(new File("C:\\schoolData\\generalclasslist.pdf"));
                    dialog.setVisible(true);


                }

            } else {

                Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler " + "C:\\schoolData\\generalclasslist.pdf");

            }


        } catch (Exception sq) {
            sq.printStackTrace();
            JOptionPane.showMessageDialog(null, sq.getMessage());
        }
    }

    public static void programClassList(String stream, String classname, String program) {
        try {

            con = DbConnection.connectDb();
            Document doc = new Document();
            com.itextpdf.text.Image image = com.itextpdf.text.Image.getInstance("C:/schooldata/logo.jpg");
            PdfWriter writer = PdfWriter.getInstance(doc, new FileOutputStream("C:\\schoolData\\generalclasslist.pdf"));
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

            Paragraph pr5 = new Paragraph("GENERAL CLASS LIST", FontFactory.getFont(FontFactory.HELVETICA, 13, java.awt.Font.BOLD, BaseColor.BLACK));
            Chunk glue = new Chunk(new VerticalPositionMark());
            doc.addCreationDate();
            Paragraph pr7 = new Paragraph("CLASS:  " + classname.toUpperCase(), FontFactory.getFont(FontFactory.TIMES, 13, java.awt.Font.PLAIN, BaseColor.BLACK));


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
                Phrase ph1 = new Phrase("ADM Number", new Font(Font.FontFamily.HELVETICA, 9, Font.NORMAL, BaseColor.BLACK));
                PdfPCell cell1 = new PdfPCell(ph1);
                cell1.setRotation(90);
                cell1.setColspan(2);
                Phrase ph2 = new Phrase("Class & Stream", new Font(Font.FontFamily.HELVETICA, 9, Font.NORMAL, BaseColor.BLACK));
                PdfPCell cell2 = new PdfPCell(ph2);
                cell2.setColspan(3);
                cell2.setRotation(90);
                Phrase ph3 = new Phrase("Name", new Font(Font.FontFamily.HELVETICA, 9, Font.NORMAL, BaseColor.BLACK));
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
                if (program.equalsIgnoreCase("all")) {
                    int counter = 0;
                    String sql3 = "Select admissionnumber,firstname,middlename,lastname,currentstream from admission where  currentform='" + Globals.classCode(classname) + "'  " + Globals.sortcode + " ";
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
                    String sql3 = "Select admissionnumber,firstname,middlename,lastname,currentstream from admission where  currentform='" + Globals.classCode(classname) + "' and Program='" + program + "'  " + Globals.sortcode + " ";
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

                }


            } else {

                if (program.equalsIgnoreCase("All")) {

                    int counter = 0;
                    String sql3 = "Select admissionnumber,firstname,middlename,lastname from admission where  currentform='" + Globals.classCode(classname) + "' and currentstream='" + Globals.streamcode(stream) + "' " + Globals.sortcode + " ";
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
                } else {


                    int counter = 0;
                    String sql3 = "Select admissionnumber,firstname,middlename,lastname from admission where  currentform='" + Globals.classCode(classname) + "' and currentstream='" + Globals.streamcode(stream) + "' and Program='" + program + "' " + Globals.sortcode + " ";
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
                        Phrase ph2 = new Phrase(classname + " " + stream, new Font(Font.FontFamily.HELVETICA, 7, Font.NORMAL, BaseColor.BLACK));
                        PdfPCell cell2 = new PdfPCell(ph2);
                        cell2.setColspan(3);

                        Phrase ph3 = new Phrase(rs.getString("firstName") + "    " + rs.getString("Middlename") + "    " + rs.getString("LastName"), new Font(Font.FontFamily.HELVETICA, 7, Font.NORMAL, BaseColor.BLACK));
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


            }
            tabb.setHeaderRows(1);
            doc.add(tabb);
            doc.close();


            if (ConfigurationIntialiser.docOpener()) {
                if (dialog == null) {


                    dialog = ReportFrame.getReportFrameInstance(new File("C:\\schoolData\\generalclasslist.pdf"));


                } else {
                    dialog = null;
                    dialog = ReportFrame.getReportFrameInstance(new File("C:\\schoolData\\generalclasslist.pdf"));
                    dialog.setVisible(true);


                }

            } else {

                Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler " + "C:\\schoolData\\generalclasslist.pdf");

            }


        } catch (Exception sq) {
            sq.printStackTrace();
            JOptionPane.showMessageDialog(null, sq.getMessage());
        }
    }

    public static void generalClassListPhotoViewer(String stream, String classname, String gender) {
        try {

            con = DbConnection.connectDb();
            Document doc = new Document();
            com.itextpdf.text.Image image = com.itextpdf.text.Image.getInstance("C:/schooldata/logo.jpg");
            PdfWriter writer = PdfWriter.getInstance(doc, new FileOutputStream("C:\\schoolData\\generalclasslist.pdf"));
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

            Paragraph pr5 = new Paragraph("GENERAL CLASS LIST PHOTO PREVIEW", FontFactory.getFont(FontFactory.HELVETICA, 13, java.awt.Font.BOLD, BaseColor.BLACK));
            Chunk glue = new Chunk(new VerticalPositionMark());
            doc.addCreationDate();
            Paragraph pr7 = new Paragraph("CLASS:  " + classname.toUpperCase(), FontFactory.getFont(FontFactory.TIMES, 13, java.awt.Font.PLAIN, BaseColor.BLACK));


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
                Phrase ph1 = new Phrase("ADM Number", new Font(Font.FontFamily.HELVETICA, 9, Font.NORMAL, BaseColor.BLACK));
                PdfPCell cell1 = new PdfPCell(ph1);
                cell1.setRotation(90);
                cell1.setColspan(2);
                Phrase ph2 = new Phrase("Class & Stream", new Font(Font.FontFamily.HELVETICA, 9, Font.NORMAL, BaseColor.BLACK));
                PdfPCell cell2 = new PdfPCell(ph2);
                cell2.setColspan(3);
                cell2.setRotation(90);
                Phrase ph3 = new Phrase("Name", new Font(Font.FontFamily.HELVETICA, 9, Font.NORMAL, BaseColor.BLACK));
                PdfPCell cell3 = new PdfPCell(ph3);
                cell3.setColspan(6);
                cell3.setVerticalAlignment(Element.ALIGN_BOTTOM);
                cell3.setHorizontalAlignment(Element.ALIGN_MIDDLE);


                Phrase ph4 = new Phrase("PHOTO", new Font(Font.FontFamily.HELVETICA, 7, Font.NORMAL, BaseColor.BLACK));
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
                if (gender.equalsIgnoreCase("all")) {
                    int counter = 0;
                    String sql3 = "Select admissionnumber,firstname,middlename,lastname,currentstream from admission where  currentform='" + Globals.classCode(classname) + "'  " + Globals.sortcode + " ";
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


                        File file = new File(Globals.studentImage(adm));
                        Image img2;
                        PdfPCell cell4;
                        if (file.exists()) {
                            BufferedImage inputImage = ImageIO.read(file);
                            BufferedImage outputImage = new BufferedImage(Globals.pictureWidth, Globals.pictureHeight, inputImage.getType());
                            Graphics2D g2d = outputImage.createGraphics();
                            g2d.drawImage(inputImage, 0, 0, Globals.pictureWidth, Globals.pictureHeight, null);
                            g2d.dispose();

                            ImageIO.write(outputImage, "jpg", file);
                            img2 = Image.getInstance(Globals.studentImage(adm));
                            cell4 = new PdfPCell(img2, true);
                        } else {
                            Phrase ph4 = new Phrase("NO IMAGE", new Font(Font.FontFamily.TIMES_ROMAN, 16, Font.NORMAL, BaseColor.RED));
                            cell4 = new PdfPCell(ph4);
                        }


                        cell4.setColspan(8);
                        tabb.addCell(cell);
                        tabb.addCell(cell1);
                        tabb.addCell(cell2);
                        tabb.addCell(cell3);
                        tabb.addCell(cell4);


                    }

                } else {

                    int counter = 0;
                    String sql3 = "Select admissionnumber,firstname,middlename,lastname,currentstream from admission where  currentform='" + Globals.classCode(classname) + "' and gender='" + gender + "'  " + Globals.sortcode + " ";
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


                        File file = new File(Globals.studentImage(adm));
                        Image img2;
                        PdfPCell cell4;
                        if (file.exists()) {
                            BufferedImage inputImage = ImageIO.read(file);
                            BufferedImage outputImage = new BufferedImage(Globals.pictureWidth, Globals.pictureHeight, inputImage.getType());
                            Graphics2D g2d = outputImage.createGraphics();
                            g2d.drawImage(inputImage, 0, 0, Globals.pictureWidth, Globals.pictureHeight, null);
                            g2d.dispose();

                            ImageIO.write(outputImage, "jpg", file);
                            img2 = Image.getInstance(Globals.studentImage(adm));
                            cell4 = new PdfPCell(img2, true);
                        } else {
                            Phrase ph4 = new Phrase("NO IMAGE", new Font(Font.FontFamily.TIMES_ROMAN, 16, Font.NORMAL, BaseColor.RED));
                            cell4 = new PdfPCell(ph4);
                        }


                        cell4.setColspan(8);
                        tabb.addCell(cell);
                        tabb.addCell(cell1);
                        tabb.addCell(cell2);
                        tabb.addCell(cell3);
                        tabb.addCell(cell4);


                    }

                }


            } else {

                if (gender.equalsIgnoreCase("All")) {

                    int counter = 0;
                    String sql3 = "Select admissionnumber,firstname,middlename,lastname from admission where  currentform='" + Globals.classCode(classname) + "' and currentstream='" + Globals.streamcode(stream) + "' " + Globals.sortcode + " ";
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


                        File file = new File(Globals.studentImage(adm));
                        Image img2;
                        PdfPCell cell4;
                        if (file.exists()) {
                            BufferedImage inputImage = ImageIO.read(file);
                            BufferedImage outputImage = new BufferedImage(Globals.pictureWidth, Globals.pictureHeight, inputImage.getType());
                            Graphics2D g2d = outputImage.createGraphics();
                            g2d.drawImage(inputImage, 0, 0, Globals.pictureWidth, Globals.pictureHeight, null);
                            g2d.dispose();

                            ImageIO.write(outputImage, "jpg", file);
                            img2 = Image.getInstance(Globals.studentImage(adm));
                            cell4 = new PdfPCell(img2, true);
                        } else {
                            Phrase ph4 = new Phrase("NO IMAGE", new Font(Font.FontFamily.TIMES_ROMAN, 16, Font.NORMAL, BaseColor.RED));
                            cell4 = new PdfPCell(ph4);
                        }


                        cell4.setColspan(8);
                        tabb.addCell(cell);
                        tabb.addCell(cell1);
                        tabb.addCell(cell2);
                        tabb.addCell(cell3);
                        tabb.addCell(cell4);


                    }
                } else {


                    int counter = 0;
                    String sql3 = "Select admissionnumber,firstname,middlename,lastname from admission where  currentform='" + Globals.classCode(classname) + "' and currentstream='" + Globals.streamcode(stream) + "' and gender='" + gender + "' " + Globals.sortcode + " ";
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
                        Phrase ph2 = new Phrase(classname + " " + stream, new Font(Font.FontFamily.HELVETICA, 7, Font.NORMAL, BaseColor.BLACK));
                        PdfPCell cell2 = new PdfPCell(ph2);
                        cell2.setColspan(3);

                        Phrase ph3 = new Phrase(rs.getString("firstName") + "    " + rs.getString("Middlename") + "    " + rs.getString("LastName"), new Font(Font.FontFamily.HELVETICA, 7, Font.NORMAL, BaseColor.BLACK));
                        PdfPCell cell3 = new PdfPCell(ph3);
                        cell3.setColspan(6);
                        cell3.setVerticalAlignment(Element.ALIGN_BOTTOM);
                        cell3.setHorizontalAlignment(Element.ALIGN_MIDDLE);


                        File file = new File(Globals.studentImage(adm));
                        Image img2;
                        PdfPCell cell4;
                        if (file.exists()) {
                            BufferedImage inputImage = ImageIO.read(file);
                            BufferedImage outputImage = new BufferedImage(Globals.pictureWidth, Globals.pictureHeight, inputImage.getType());
                            Graphics2D g2d = outputImage.createGraphics();
                            g2d.drawImage(inputImage, 0, 0, Globals.pictureWidth, Globals.pictureHeight, null);
                            g2d.dispose();

                            ImageIO.write(outputImage, "jpg", file);
                            img2 = Image.getInstance(Globals.studentImage(adm));
                            cell4 = new PdfPCell(img2, true);
                        } else {
                            Phrase ph4 = new Phrase("NO IMAGE", new Font(Font.FontFamily.TIMES_ROMAN, 16, Font.NORMAL, BaseColor.RED));
                            cell4 = new PdfPCell(ph4);
                        }


                        cell4.setColspan(8);
                        tabb.addCell(cell);
                        tabb.addCell(cell1);
                        tabb.addCell(cell2);
                        tabb.addCell(cell3);
                        tabb.addCell(cell4);


                    }

                }


            }
            tabb.setHeaderRows(1);
            doc.add(tabb);
            doc.close();


            if (ConfigurationIntialiser.docOpener()) {
                if (dialog == null) {


                    dialog = ReportFrame.getReportFrameInstance(new File("C:\\schoolData\\generalclasslist.pdf"));


                } else {
                    dialog = null;
                    dialog = ReportFrame.getReportFrameInstance(new File("C:\\schoolData\\generalclasslist.pdf"));
                    dialog.setVisible(true);


                }

            } else {

                Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler " + "C:\\schoolData\\generalclasslist.pdf");

            }


        } catch (Exception sq) {
            sq.printStackTrace();
            JOptionPane.showMessageDialog(null, sq.getMessage());
        }
    }
}
