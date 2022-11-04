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
import java.io.IOException;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.JDialog;
import javax.swing.JOptionPane;

/**
 * @author FRED
 */
public class ClearanceReportForm {

    static PreparedStatement ps;
    static ResultSet rs;
    static Connection con;
    static JDialog dialog = null;
    static DbConnection db = new DbConnection();

    public static void onEndPage(PdfWriter writer, Document document) {
        Font ffont = new Font(Font.FontFamily.TIMES_ROMAN, 5, Font.ITALIC);
        PdfContentByte cb = writer.getDirectContent();
        String concat = "";
        if (ConfigurationIntialiser.AutoClearance()) {
            concat = "AUTO";
        } else {
            concat = "MANUAL";
        }
        String clearmode = "Clearance Mode: " + concat;
        Phrase footer = new Phrase("Lunar Technologies|System Generated Doc.  " + clearmode, ffont);

        ColumnText.showTextAligned(cb, Element.ALIGN_RIGHT,
                footer,
                (document.right() - document.left()) / 2 + document.leftMargin(),
                document.bottom() - 10, 0);
    }

    public static void generateReport(String adm, String docName) {
        Document doc = new Document();
        try {
            con = DbConnection.connectDb();

            com.itextpdf.text.Image image = com.itextpdf.text.Image.getInstance("C:\\schooldata\\logo.jpg");
            PdfWriter writer = PdfWriter.getInstance(doc, new FileOutputStream("C:\\schooldata\\" + adm + "Clearanceform" + ".pdf"));
            doc.open();


            doc.add(new Paragraph("Printed On: " + new Date(), FontFactory.getFont(FontFactory.TIMES, 9, java.awt.Font.PLAIN, BaseColor.BLACK)));
            PdfPTable tab = new PdfPTable(1);

            {//school details header

                DocHead head = new DocHead();
                Image img = head.im();

                PdfPCell cell1 = new PdfPCell(img, true);
                cell1.setBorder(PdfPCell.NO_BORDER);

                tab.addCell(cell1);
            }
            int year = Globals.academicYear() + 1;
            tab.setWidthPercentage(100);
            doc.add(tab);
            Paragraph pr4 = new Paragraph("NAME:                                   " + Globals.fullName(adm), FontFactory.getFont(FontFactory.HELVETICA, 9, java.awt.Font.PLAIN, BaseColor.BLACK));
            Paragraph pr5 = new Paragraph("ADMISSION NUMBER:         " + adm, FontFactory.getFont(FontFactory.HELVETICA, 9, java.awt.Font.PLAIN, BaseColor.BLACK));
            Paragraph pr6 = new Paragraph("CLEARANCE REPORT ACADEMIC YEAR :" + Globals.academicYear() + "-" + year, FontFactory.getFont(FontFactory.HELVETICA, 9, java.awt.Font.PLAIN, BaseColor.BLACK));
            Chunk glue = new Chunk(new VerticalPositionMark());

            pr5.setSpacingAfter(0);


            doc.add(pr6);
            doc.add(pr4);
            doc.add(pr5);


            PdfPTable tabb = new PdfPTable(3);
            Phrase ph = new Phrase("Clearance Entity".toUpperCase(), new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD));
            PdfPCell cell = new PdfPCell(ph);
            cell.setBorder(PdfPCell.NO_BORDER);
            tabb.addCell(cell);

            Phrase ph1 = new Phrase("Date".toUpperCase(), new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD));

            PdfPCell cell1 = new PdfPCell(ph1);
            cell1.setBorder(PdfPCell.NO_BORDER);
            tabb.addCell(cell1);

            Phrase ph2 = new Phrase("Remarks".toUpperCase(), new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD));
            PdfPCell cell2 = new PdfPCell(ph2);
            Phrase ph3 = new Phrase("Sign".toUpperCase(), new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD));
            PdfPCell cell3 = new PdfPCell(ph3);
            cell3.setBorder(PdfPCell.NO_BORDER);
            cell2.setBorder(PdfPCell.NO_BORDER);
            cell2.setFixedHeight(30);
            tabb.addCell(cell2);//tabb.addCell(cell3);
            tabb.setWidthPercentage(100);
            tabb.setSpacingBefore(5);


            try {
                SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
                if (ConfigurationIntialiser.AutoClearance()) {

                    Date dd = new Date();
                    //  SimpleDateFormat format=new SimpleDateFormat("dd/MM/yyyy");
                    String sql = "Select *   from subjects";
                    ps = con.prepareStatement(sql);
                    rs = ps.executeQuery();
                    while (rs.next()) {


                        String status = "Cleared";
                        String subjectname = rs.getString("subjectname");
                        String subcode = rs.getString("Subjectcode");
                        PdfPCell firstcell = new PdfPCell(new Phrase(subjectname.toUpperCase(), new Font(Font.FontFamily.TIMES_ROMAN, 11, Font.BOLD)));
                        PdfPCell secondcell = new PdfPCell(new Phrase(format.format(dd), new Font(Font.FontFamily.TIMES_ROMAN, 11, Font.BOLD)));
                        PdfPCell fourth = new PdfPCell(new Phrase(".....................", new Font(Font.FontFamily.TIMES_ROMAN, 11, Font.BOLD)));

                        String sql1 = "Select * from books where subjectcode='" + subcode + "' ";
                        ps = con.prepareStatement(sql1);
                        ResultSet RS = ps.executeQuery();
                        while (RS.next()) {
                            String bkserial = RS.getString("Bookserial");
                            String sql2 = "Select status from issuedbooks where status!='" + "Returned" + "'  and admnumber='" + adm + "'  and bookserial='" + bkserial + "'";
                            ps = con.prepareStatement(sql2);
                            ResultSet res = ps.executeQuery();
                            if (res.next()) {
                                status = "Not Cleared";
                                break;
                            }


                        }

                        PdfPCell thirdcell = new PdfPCell(new Phrase(status, new Font(Font.FontFamily.TIMES_ROMAN, 11, Font.BOLD)));
                        firstcell.setBorder(PdfPCell.NO_BORDER);
                        secondcell.setBorder(PdfPCell.NO_BORDER);
                        thirdcell.setBorder(PdfPCell.NO_BORDER);
                        fourth.setBorder(PdfPCell.NO_BORDER);
                        firstcell.setFixedHeight(20);
                        tabb.addCell(firstcell);
                        tabb.addCell(secondcell);
                        tabb.addCell(thirdcell);
                        //  tabb.addCell(fourth);

                    }
                    tabb.setSpacingBefore(20);

                    for (int i = 0; i < 2; ++i) {
                        String name = "";
                        if (i == 0) {
                            name = "Games".toUpperCase();
                        }

                        PdfPCell firstcell = new PdfPCell(new Phrase(name, new Font(Font.FontFamily.TIMES_ROMAN, 11, Font.BOLD)));
                        PdfPCell secondcell = new PdfPCell(new Phrase(format.format(dd), new Font(Font.FontFamily.TIMES_ROMAN, 11, Font.BOLD)));
                        //     PdfPCell thirdcell=new PdfPCell(new Phrase(status,new Font(Font.FontFamily.TIMES_ROMAN, 11, Font.BOLD)));
                        PdfPCell fourth = new PdfPCell(new Phrase(".....................", new Font(Font.FontFamily.TIMES_ROMAN, 11, Font.BOLD)));
                        firstcell.setFixedHeight(25);
                    }


                } else {


                    String sql = "Select *   from subjects";
                    ps = con.prepareStatement(sql);
                    rs = ps.executeQuery();
                    while (rs.next()) {

                        Date dd = new Date();
                        String status = "Not Cleared";
                        String subjectname = rs.getString("subjectname");
                        String subcode = rs.getString("Subjectcode");
                        PdfPCell firstcell = new PdfPCell(new Phrase(subjectname, new Font(Font.FontFamily.TIMES_ROMAN, 11, Font.BOLD)));


                        String sqll = "Select * from studentsubjectclearance where subjectcode='" + subcode + "' and admnumber='" + adm + "'";
                        ps = con.prepareStatement(sqll);
                        ResultSet RS = ps.executeQuery();
                        if (RS.next()) {
                            status = "Cleared";
                            dd = RS.getDate("Date");
                        }

                        PdfPCell thirdcell = new PdfPCell(new Phrase(status, new Font(Font.FontFamily.TIMES_ROMAN, 11, Font.BOLD)));
                        PdfPCell secondcell = new PdfPCell(new Phrase(format.format(dd), new Font(Font.FontFamily.TIMES_ROMAN, 11, Font.BOLD)));
                        firstcell.setBorder(PdfPCell.NO_BORDER);
                        secondcell.setBorder(PdfPCell.NO_BORDER);
                        thirdcell.setBorder(PdfPCell.NO_BORDER);
                        // fourth.setBorder(PdfPCell.NO_BORDER);
                        firstcell.setFixedHeight(25);
                        tabb.addCell(firstcell);
                        tabb.addCell(secondcell);
                        tabb.addCell(thirdcell);
                        // tabb.addCell(fourth);
                    }


                }
                Date dd = new Date();
                PdfPCell firstcell = new PdfPCell(new Phrase("GAMES", new Font(Font.FontFamily.TIMES_ROMAN, 11, Font.BOLD)));
                PdfPCell thirdcell = new PdfPCell(new Phrase("....................", new Font(Font.FontFamily.TIMES_ROMAN, 11, Font.BOLD)));
                PdfPCell secondcell = new PdfPCell(new Phrase(format.format(dd), new Font(Font.FontFamily.TIMES_ROMAN, 11, Font.BOLD)));
                firstcell.setBorder(PdfPCell.NO_BORDER);
                secondcell.setBorder(PdfPCell.NO_BORDER);
                thirdcell.setBorder(PdfPCell.NO_BORDER);
                // fourth.setBorder(PdfPCell.NO_BORDER);
                firstcell.setFixedHeight(25);
                tabb.addCell(firstcell);
                tabb.addCell(secondcell);
                tabb.addCell(thirdcell);
                // tabb.addCell(fourth);
                String status = "....................";


                PdfPCell firstcell1 = new PdfPCell(new Phrase("FINANCE", new Font(Font.FontFamily.TIMES_ROMAN, 11, Font.BOLD)));
                PdfPCell thirdcell1 = new PdfPCell(new Phrase(status, new Font(Font.FontFamily.TIMES_ROMAN, 11, Font.BOLD)));
                PdfPCell secondcell1 = new PdfPCell(new Phrase(format.format(dd), new Font(Font.FontFamily.TIMES_ROMAN, 11, Font.BOLD)));
                firstcell1.setBorder(PdfPCell.NO_BORDER);
                secondcell1.setBorder(PdfPCell.NO_BORDER);
                thirdcell1.setBorder(PdfPCell.NO_BORDER);
                // fourth.setBorder(PdfPCell.NO_BORDER);

                Phrase ph6 = new Phrase("STORE", new Font(Font.FontFamily.TIMES_ROMAN, 11, Font.BOLD, BaseColor.BLACK));
                PdfPCell cell6 = new PdfPCell(ph6);
                cell6.setFixedHeight(25);
                cell6.setBorder(PdfPCell.NO_BORDER);
                Phrase ph6a = new Phrase(format.format(dd), new Font(Font.FontFamily.TIMES_ROMAN, 11, Font.BOLD, BaseColor.BLACK));
                PdfPCell cell6a = new PdfPCell(ph6a);
                Phrase ph6b = new Phrase("....................", new Font(Font.FontFamily.TIMES_ROMAN, 11, Font.BOLD, BaseColor.BLACK));

                PdfPCell cell6b = new PdfPCell(ph6b);
                cell6a.setBorder(PdfPCell.NO_BORDER);
                cell6b.setBorder(PdfPCell.NO_BORDER);

                Phrase ph7 = new Phrase("LIBRARY", new Font(Font.FontFamily.TIMES_ROMAN, 11, Font.BOLD));
                PdfPCell cell7 = new PdfPCell(ph7);
                cell7.setBorder(PdfPCell.NO_BORDER);
                Phrase ph7a = new Phrase(format.format(dd), new Font(Font.FontFamily.TIMES_ROMAN, 11, Font.BOLD));
                PdfPCell cell7a = new PdfPCell(ph7a);
                cell7a.setBorder(PdfPCell.NO_BORDER);
                Phrase ph7b = new Phrase("....................", new Font(Font.FontFamily.TIMES_ROMAN, 11, Font.BOLD));
                PdfPCell cell7b = new PdfPCell(ph7b);
                cell7b.setBorder(PdfPCell.NO_BORDER);
                cell7.setFixedHeight(25);
                tabb.addCell(cell7);
                tabb.addCell(cell7a);
                tabb.addCell(cell7b);
                tabb.addCell(cell6);
                tabb.addCell(cell6a);
                tabb.addCell(cell6b);

                firstcell.setFixedHeight(25);
                firstcell1.setFixedHeight(25);
                tabb.addCell(firstcell1);
                tabb.addCell(secondcell1);
                tabb.addCell(thirdcell1);

                // tabb.addCell(fourth);
                doc.add(tabb);
                Paragraph finance = new Paragraph("ACCOUNT'S SIGN  :......................................STAMP:.......................................DATE:.......................", FontFactory.getFont(FontFactory.TIMES_BOLD, 11, java.awt.Font.PLAIN, BaseColor.BLACK));
                finance.setSpacingAfter(45);
                finance.setSpacingBefore(45);
                Paragraph comment = new Paragraph("PRINCICPAL'S SIGN  :......................................STAMP:.......................................DATE:.....................", FontFactory.getFont(FontFactory.TIMES_BOLD, 11, java.awt.Font.PLAIN, BaseColor.BLACK));
                doc.add(finance);
                doc.add(comment);
                onEndPage(writer, doc);

                onEndPage(writer, doc);
                doc.close();

            } catch (DocumentException | SQLException sq) {
                sq.printStackTrace();
                JOptionPane.showMessageDialog(null, sq.getMessage());
            }


            if (ConfigurationIntialiser.docOpener()) {
                if (dialog == null) {


                    dialog = ReportFrame.getReportFrameInstance(new File("C:\\schooldata\\" + adm + "Clearanceform" + ".pdf"));


                } else {
                    dialog = null;
                    dialog = ReportFrame.getReportFrameInstance(new File("C:\\schooldata\\" + adm + "Clearanceform" + ".pdf"));
                    dialog.setVisible(true);


                }

            } else {

                Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler " + "C:\\schooldata\\" + adm + "Clearanceform" + ".pdf");

            }

        } catch (DocumentException | IOException sq) {
            JOptionPane.showMessageDialog(null, sq.getMessage());
            sq.printStackTrace();
        } finally {
            try {
                con.close();
            } catch (SQLException e) {
            }
        }


    }


    public static void AlumniReport(String adm, String docName) {
        Document doc = new Document();
        try {

            con = DbConnection.connectDb();

            com.itextpdf.text.Image image = com.itextpdf.text.Image.getInstance("C:\\schooldata\\logo.jpg");
            PdfWriter writer = PdfWriter.getInstance(doc, new FileOutputStream("C:\\schooldata\\" + adm + "Clearanceform" + ".pdf"));
            doc.open();

            doc.add(new Paragraph("Printed On: " + new Date(), FontFactory.getFont(FontFactory.TIMES, 9, java.awt.Font.PLAIN, BaseColor.BLACK)));
            PdfPTable tab = new PdfPTable(1);

            {//school details header

                DocHead head = new DocHead();
                Image img = head.im();

                PdfPCell cell1 = new PdfPCell(img, true);
                cell1.setBorder(PdfPCell.NO_BORDER);

                tab.addCell(cell1);
            }
            int year = Globals.academicYear() + 1;
            tab.setWidthPercentage(100);
            doc.add(tab);
            Paragraph pr4 = new Paragraph("NAME:                                   " + Globals.fullName(adm), FontFactory.getFont(FontFactory.HELVETICA, 9, java.awt.Font.PLAIN, BaseColor.BLACK));
            Paragraph pr5 = new Paragraph("ADMISSION NUMBER:         " + adm, FontFactory.getFont(FontFactory.HELVETICA, 9, java.awt.Font.PLAIN, BaseColor.BLACK));
            Paragraph pr6 = new Paragraph("CLEARANCE REPORT. YEAR OF COMPLETION :" + year, FontFactory.getFont(FontFactory.HELVETICA, 9, java.awt.Font.PLAIN, BaseColor.BLACK));
            Chunk glue = new Chunk(new VerticalPositionMark());


            pr4.setSpacingAfter(5);
            pr5.setSpacingAfter(0);


            doc.add(pr6);
            doc.add(pr4);
            doc.add(pr5);


            PdfPTable tabb = new PdfPTable(3);
            Phrase ph = new Phrase("Clearance Entity".toUpperCase(), new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD));
            PdfPCell cell = new PdfPCell(ph);
            cell.setBorder(PdfPCell.NO_BORDER);
            tabb.addCell(cell);

            Phrase ph1 = new Phrase("Date".toUpperCase(), new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD));

            PdfPCell cell1 = new PdfPCell(ph1);
            cell1.setBorder(PdfPCell.NO_BORDER);
            tabb.addCell(cell1);

            Phrase ph2 = new Phrase("Remarks".toUpperCase(), new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD));
            PdfPCell cell2 = new PdfPCell(ph2);
            Phrase ph3 = new Phrase("Sign".toUpperCase(), new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD));
            PdfPCell cell3 = new PdfPCell(ph3);
            cell3.setBorder(PdfPCell.NO_BORDER);
            cell2.setBorder(PdfPCell.NO_BORDER);
            cell2.setFixedHeight(30);
            tabb.addCell(cell2);//tabb.addCell(cell3);
            tabb.setWidthPercentage(100);
            tabb.setSpacingBefore(5);


            try {
                con = DbConnection.connectDb();
                SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
                if (ConfigurationIntialiser.AutoClearance()) {

                    Date dd = new Date();
                    //  SimpleDateFormat format=new SimpleDateFormat("dd/MM/yyyy");

                    String sql = "Select *   from subjects";
                    ps = con.prepareStatement(sql);
                    rs = ps.executeQuery();
                    while (rs.next()) {


                        String status = "Cleared";
                        String subjectname = rs.getString("subjectname");
                        String subcode = rs.getString("Subjectcode");
                        PdfPCell firstcell = new PdfPCell(new Phrase(subjectname.toUpperCase(), new Font(Font.FontFamily.TIMES_ROMAN, 11, Font.BOLD)));
                        PdfPCell secondcell = new PdfPCell(new Phrase(format.format(dd), new Font(Font.FontFamily.TIMES_ROMAN, 11, Font.BOLD)));
                        PdfPCell fourth = new PdfPCell(new Phrase(".....................", new Font(Font.FontFamily.TIMES_ROMAN, 11, Font.BOLD)));

                        String sql1 = "Select * from books where subjectcode='" + subcode + "' ";
                        ps = con.prepareStatement(sql1);
                        ResultSet RS = ps.executeQuery();
                        while (RS.next()) {
                            String bkserial = RS.getString("Bookserial");
                            String sql2 = "Select status from alumnibooksrecord where status!='" + "Returned" + "'  and admnumber='" + adm + "'  and bookserial='" + bkserial + "'";
                            ps = con.prepareStatement(sql2);
                            ResultSet res = ps.executeQuery();
                            if (res.next()) {
                                status = "Not Cleared";
                            } else {
                                status = "Cleared";
                            }

                            break;

                        }

                        PdfPCell thirdcell = new PdfPCell(new Phrase(status, new Font(Font.FontFamily.TIMES_ROMAN, 11, Font.BOLD)));
                        firstcell.setBorder(PdfPCell.NO_BORDER);
                        secondcell.setBorder(PdfPCell.NO_BORDER);
                        thirdcell.setBorder(PdfPCell.NO_BORDER);
                        fourth.setBorder(PdfPCell.NO_BORDER);
                        firstcell.setFixedHeight(25);
                        tabb.addCell(firstcell);
                        tabb.addCell(secondcell);
                        tabb.addCell(thirdcell);
                        //  tabb.addCell(fourth);

                    }
                    tabb.setSpacingBefore(20);

                    for (int i = 0; i < 2; ++i) {
                        String name = "";
                        if (i == 0) {
                            name = "Games".toUpperCase();
                        }

                        PdfPCell firstcell = new PdfPCell(new Phrase(name, new Font(Font.FontFamily.TIMES_ROMAN, 11, Font.BOLD)));
                        PdfPCell secondcell = new PdfPCell(new Phrase(format.format(dd), new Font(Font.FontFamily.TIMES_ROMAN, 11, Font.BOLD)));
                        //     PdfPCell thirdcell=new PdfPCell(new Phrase(status,new Font(Font.FontFamily.TIMES_ROMAN, 11, Font.BOLD)));
                        PdfPCell fourth = new PdfPCell(new Phrase(".....................", new Font(Font.FontFamily.TIMES_ROMAN, 11, Font.BOLD)));
                        firstcell.setFixedHeight(25);
                    }


                } else {


                    String sql = "Select *   from subjects";
                    ps = con.prepareStatement(sql);
                    rs = ps.executeQuery();
                    while (rs.next()) {

                        Date dd = new Date();
                        String status = "Not Cleared";
                        String subjectname = rs.getString("subjectname");
                        String subcode = rs.getString("Subjectcode");
                        PdfPCell firstcell = new PdfPCell(new Phrase(subjectname, new Font(Font.FontFamily.TIMES_ROMAN, 11, Font.BOLD)));


                        String sqll = "Select * from studentsubjectclearance where subjectcode='" + subcode + "' and admnumber='" + adm + "'";
                        ps = con.prepareStatement(sqll);
                        ResultSet RS = ps.executeQuery();
                        if (RS.next()) {
                            status = "Cleared";
                            dd = RS.getDate("Date");
                        }

                        PdfPCell thirdcell = new PdfPCell(new Phrase(status, new Font(Font.FontFamily.TIMES_ROMAN, 11, Font.BOLD)));
                        PdfPCell secondcell = new PdfPCell(new Phrase(format.format(dd), new Font(Font.FontFamily.TIMES_ROMAN, 11, Font.BOLD)));
                        firstcell.setBorder(PdfPCell.NO_BORDER);
                        secondcell.setBorder(PdfPCell.NO_BORDER);
                        thirdcell.setBorder(PdfPCell.NO_BORDER);
                        // fourth.setBorder(PdfPCell.NO_BORDER);
                        firstcell.setFixedHeight(25);
                        tabb.addCell(firstcell);
                        tabb.addCell(secondcell);
                        tabb.addCell(thirdcell);
                        // tabb.addCell(fourth);
                    }


                }
                Date dd = new Date();
                PdfPCell firstcell = new PdfPCell(new Phrase("GAMES", new Font(Font.FontFamily.TIMES_ROMAN, 11, Font.BOLD)));
                PdfPCell thirdcell = new PdfPCell(new Phrase("....................", new Font(Font.FontFamily.TIMES_ROMAN, 11, Font.BOLD)));
                PdfPCell secondcell = new PdfPCell(new Phrase(format.format(dd), new Font(Font.FontFamily.TIMES_ROMAN, 11, Font.BOLD)));
                firstcell.setBorder(PdfPCell.NO_BORDER);
                secondcell.setBorder(PdfPCell.NO_BORDER);
                thirdcell.setBorder(PdfPCell.NO_BORDER);
                // fourth.setBorder(PdfPCell.NO_BORDER);
                firstcell.setFixedHeight(25);
                tabb.addCell(firstcell);
                tabb.addCell(secondcell);
                tabb.addCell(thirdcell);
                // tabb.addCell(fourth);
                String status = "....................";


                PdfPCell firstcell1 = new PdfPCell(new Phrase("FINANCE", new Font(Font.FontFamily.TIMES_ROMAN, 11, Font.BOLD)));
                PdfPCell thirdcell1 = new PdfPCell(new Phrase(status, new Font(Font.FontFamily.TIMES_ROMAN, 11, Font.BOLD)));
                PdfPCell secondcell1 = new PdfPCell(new Phrase(format.format(dd), new Font(Font.FontFamily.TIMES_ROMAN, 11, Font.BOLD)));
                firstcell1.setBorder(PdfPCell.NO_BORDER);
                secondcell1.setBorder(PdfPCell.NO_BORDER);
                thirdcell1.setBorder(PdfPCell.NO_BORDER);
                // fourth.setBorder(PdfPCell.NO_BORDER);

                Phrase ph6 = new Phrase("STORE", new Font(Font.FontFamily.TIMES_ROMAN, 11, Font.BOLD, BaseColor.BLACK));
                PdfPCell cell6 = new PdfPCell(ph6);
                cell6.setFixedHeight(25);
                cell6.setBorder(PdfPCell.NO_BORDER);
                Phrase ph6a = new Phrase(format.format(dd), new Font(Font.FontFamily.TIMES_ROMAN, 11, Font.BOLD, BaseColor.BLACK));
                PdfPCell cell6a = new PdfPCell(ph6a);
                Phrase ph6b = new Phrase("....................", new Font(Font.FontFamily.TIMES_ROMAN, 11, Font.BOLD, BaseColor.BLACK));

                PdfPCell cell6b = new PdfPCell(ph6b);
                cell6a.setBorder(PdfPCell.NO_BORDER);
                cell6b.setBorder(PdfPCell.NO_BORDER);

                Phrase ph7 = new Phrase("LIBRARY", new Font(Font.FontFamily.TIMES_ROMAN, 11, Font.BOLD));
                PdfPCell cell7 = new PdfPCell(ph7);
                cell7.setBorder(PdfPCell.NO_BORDER);
                Phrase ph7a = new Phrase(format.format(dd), new Font(Font.FontFamily.TIMES_ROMAN, 11, Font.BOLD));
                PdfPCell cell7a = new PdfPCell(ph7a);
                cell7a.setBorder(PdfPCell.NO_BORDER);
                Phrase ph7b = new Phrase("....................", new Font(Font.FontFamily.TIMES_ROMAN, 11, Font.BOLD));
                PdfPCell cell7b = new PdfPCell(ph7b);
                cell7b.setBorder(PdfPCell.NO_BORDER);
                cell7.setFixedHeight(25);
                tabb.addCell(cell7);
                tabb.addCell(cell7a);
                tabb.addCell(cell7b);
                tabb.addCell(cell6);
                tabb.addCell(cell6a);
                tabb.addCell(cell6b);

                firstcell.setFixedHeight(25);
                firstcell1.setFixedHeight(25);
                tabb.addCell(firstcell1);
                tabb.addCell(secondcell1);
                tabb.addCell(thirdcell1);
                doc.add(tabb);
                Paragraph finance = new Paragraph("ACCOUNT'S SIGN  :......................................STAMP:.......................................DATE:.......................", FontFactory.getFont(FontFactory.TIMES_BOLD, 11, java.awt.Font.PLAIN, BaseColor.BLACK));
                finance.setSpacingAfter(40);
                finance.setSpacingBefore(40);
                Paragraph comment = new Paragraph("PRINCICPAL'S SIGN  :......................................STAMP:.......................................DATE:.....................", FontFactory.getFont(FontFactory.TIMES_BOLD, 11, java.awt.Font.PLAIN, BaseColor.BLACK));
                doc.add(finance);
                doc.add(comment);
                onEndPage(writer, doc);

                onEndPage(writer, doc);
                doc.close();

            } catch (DocumentException | SQLException sq) {
                sq.printStackTrace();
                JOptionPane.showMessageDialog(null, sq.getMessage());
            }


            if (ConfigurationIntialiser.docOpener()) {
                if (dialog == null) {


                    dialog = ReportFrame.getReportFrameInstance(new File("C:\\schooldata\\" + adm + "Clearanceform" + ".pdf"));


                } else {
                    dialog = null;
                    dialog = ReportFrame.getReportFrameInstance(new File("C:\\schooldata\\" + adm + "Clearanceform" + ".pdf"));
                    dialog.setVisible(true);


                }

            } else {

                Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler " + "C:\\schooldata\\" + adm + "Clearanceform" + ".pdf");

            }

        } catch (DocumentException | IOException sq) {
            JOptionPane.showMessageDialog(null, sq.getMessage());
            sq.printStackTrace();
        } finally {
            try {
                con.close();
            } catch (SQLException e) {
            }
        }


    }

    public static int yearOfCompletion(String adm) {
        try {
            con = DbConnection.connectDb();
            String sql = "Select yearofcompletion from completionclasslists where admnumber='" + adm + "'";
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt("YearofCompletion");
            } else {
                return Globals.academicYear();
            }
        } catch (SQLException sq) {
            sq.printStackTrace();
            return Globals.academicYear();
        } finally {
            try {
                con.close();
            } catch (SQLException e) {
            }
        }
    }


}
