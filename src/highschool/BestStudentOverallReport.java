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
import java.sql.Blob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JDialog;
import javax.swing.JOptionPane;

/**
 * @author FRED
 */
public class BestStudentOverallReport {
    static PreparedStatement ps;
    static ResultSet rs;
    static Connection con;
    static JDialog dialog = null;
    static DbConnection db = new DbConnection();
    static NumberFormat nf = NumberFormat.getNumberInstance();

    public static void onEndPage(PdfWriter writer, Document document) {
        Font ffont = new Font(Font.FontFamily.UNDEFINED, 5, Font.ITALIC);
        PdfContentByte cb = writer.getDirectContent();

        Phrase footer = new Phrase("Lunar Technologies", ffont);

        ColumnText.showTextAligned(cb, Element.ALIGN_RIGHT,
                footer,
                (document.right() - document.left()) / 2 + document.leftMargin(),
                document.bottom() - 10, 0);
    }

    public static void beststudents(String examcode, String examname, String term, String academicyear, String classname, String stream, int number) {
        try {

            nf.setMaximumFractionDigits(2);
            nf.setMinimumFractionDigits(2);


            con = DbConnection.connectDb();
            Document doc = new Document();
            com.itextpdf.text.Image image = com.itextpdf.text.Image.getInstance("C:/schooldata/logo.jpg");
            PdfWriter writer = PdfWriter.getInstance(doc, new FileOutputStream("C:\\schoolData\\BestStudentOverallReport.pdf"));
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
            con = DbConnection.connectDb();
            tab.setWidthPercentage(100);
            doc.add(tab);
            Paragraph pr5 = new Paragraph("Best Student Overall Report", FontFactory.getFont(FontFactory.HELVETICA, 13, java.awt.Font.BOLD, BaseColor.BLACK));
            Chunk glue = new Chunk(new VerticalPositionMark());
            doc.addCreationDate();
            Paragraph pr7 = new Paragraph("CLASS:  " + classname.toUpperCase() + "                 STREAM: " + stream, FontFactory.getFont(FontFactory.TIMES, 13, java.awt.Font.PLAIN, BaseColor.BLACK));
            pr7.add(new Chunk(glue));

            Paragraph pr9 = new Paragraph("EXAM NAME:  " + examname, FontFactory.getFont(FontFactory.TIMES, 13, java.awt.Font.PLAIN, BaseColor.BLACK));
            pr9.add(new Chunk(glue));
            pr9.add("TERM : " + term);
            pr7.add("ACADEMIC YEAR: " + academicyear);
            doc.addCreationDate();
            doc.addTitle("Group Report");

            pr5.setIndentationLeft(150);
            doc.add(pr5);
            doc.add(pr7);
            doc.add(pr9);
            PdfPTable tabb = new PdfPTable(24);
            tabb.setSpacingBefore(10);
            tabb.setWidthPercentage(100);


            tabb.setSpacingBefore(10);
            tabb.setWidthPercentage(100);
            {
                Phrase ph = new Phrase("NO.", new Font(Font.FontFamily.HELVETICA, 7, Font.NORMAL, BaseColor.BLACK));
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
                Phrase ph4 = new Phrase("TOTAL MARKS", new Font(Font.FontFamily.HELVETICA, 7, Font.NORMAL, BaseColor.BLACK));
                PdfPCell cell4 = new PdfPCell(ph4);
                cell4.setRotation(90);
                Phrase ph5 = new Phrase("TOTAL POINT", new Font(Font.FontFamily.HELVETICA, 7, Font.NORMAL, BaseColor.BLACK));
                PdfPCell cell5 = new PdfPCell(ph5);
                cell5.setRotation(90);
                Phrase ph6 = new Phrase("PERCENTAGE", new Font(Font.FontFamily.HELVETICA, 7, Font.NORMAL, BaseColor.BLACK));
                PdfPCell cell6 = new PdfPCell(ph6);
                cell6.setRotation(90);
                Phrase ph6a = new Phrase("AVERAGE MARKS", new Font(Font.FontFamily.HELVETICA, 7, Font.NORMAL, BaseColor.BLACK));
                PdfPCell cell6a = new PdfPCell(ph6a);
                cell6a.setRotation(90);
                Phrase ph7 = new Phrase("AVERAGE POINTS", new Font(Font.FontFamily.HELVETICA, 7, Font.NORMAL, BaseColor.BLACK));
                PdfPCell cell7 = new PdfPCell(ph7);
                cell7.setRotation(90);
                Phrase ph8 = new Phrase("MEAN GRADE", new Font(Font.FontFamily.HELVETICA, 7, Font.NORMAL, BaseColor.BLACK));
                PdfPCell cell8 = new PdfPCell(ph8);
                cell8.setRotation(90);
                Phrase ph9 = new Phrase("CLASS POSITION", new Font(Font.FontFamily.HELVETICA, 7, Font.NORMAL, BaseColor.BLACK));
                PdfPCell cell9 = new PdfPCell(ph9);
                cell9.setRotation(90);
                Phrase ph10 = new Phrase("OVERALL POSITION", new Font(Font.FontFamily.HELVETICA, 7, Font.NORMAL, BaseColor.BLACK));
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
            }
            if (stream.equalsIgnoreCase("OVERALL")) {
                int studentcounter = 1;
                String SQl = "Select classpositionthisterm,admnumber,Streampositionthisterm,Meangrade,TotalMarks,meanmarks,meanpoints,totalpoints,fullname,admnumber from examanalysistable where examcode='" + examcode + "' and classname='" + classname + "' and academicyear='" + academicyear + "' group by admnumber order by classpositionthisterm asc ";
                ps = con.prepareStatement(SQl);
                rs = ps.executeQuery();
                while (rs.next()) {


                    Phrase ph = new Phrase(String.valueOf(studentcounter), new Font(Font.FontFamily.HELVETICA, 7, Font.NORMAL, BaseColor.BLACK));
                    PdfPCell cell = new PdfPCell(ph);

                    cell.setVerticalAlignment(Element.ALIGN_BOTTOM);
                    cell.setHorizontalAlignment(Element.ALIGN_MIDDLE);
                    Phrase ph1 = new Phrase(rs.getString("admnumber"), new Font(Font.FontFamily.HELVETICA, 7, Font.NORMAL, BaseColor.BLACK));
                    PdfPCell cell1 = new PdfPCell(ph1);

                    cell1.setColspan(2);
                    Phrase ph2 = new Phrase(classname + " " + stream, new Font(Font.FontFamily.HELVETICA, 7, Font.NORMAL, BaseColor.BLACK));
                    PdfPCell cell2 = new PdfPCell(ph2);
                    cell2.setColspan(3);

                    Phrase ph3 = new Phrase(rs.getString("fullname"), new Font(Font.FontFamily.HELVETICA, 7, Font.NORMAL, BaseColor.BLACK));

                    PdfPCell cell3 = new PdfPCell(ph3);
                    cell3.setColspan(9);
                    cell3.setVerticalAlignment(Element.ALIGN_BOTTOM);
                    cell3.setHorizontalAlignment(Element.ALIGN_MIDDLE);
                    Phrase ph4 = new Phrase(rs.getString("totalmarks"), new Font(Font.FontFamily.HELVETICA, 7, Font.NORMAL, BaseColor.BLACK));
                    PdfPCell cell4 = new PdfPCell(ph4);

                    Phrase ph5 = new Phrase(rs.getString("Totalpoints"), new Font(Font.FontFamily.HELVETICA, 7, Font.NORMAL, BaseColor.BLACK));
                    PdfPCell cell5 = new PdfPCell(ph5);

                    Phrase ph6 = new Phrase("", new Font(Font.FontFamily.HELVETICA, 7, Font.NORMAL, BaseColor.BLACK));
                    PdfPCell cell6 = new PdfPCell(ph6);

                    Phrase ph6a = new Phrase(rs.getString("meanmarks"), new Font(Font.FontFamily.HELVETICA, 7, Font.NORMAL, BaseColor.BLACK));
                    PdfPCell cell6a = new PdfPCell(ph6a);

                    Phrase ph7 = new Phrase(rs.getString("meanpoints"), new Font(Font.FontFamily.HELVETICA, 7, Font.NORMAL, BaseColor.BLACK));
                    PdfPCell cell7 = new PdfPCell(ph7);

                    Phrase ph8 = new Phrase(rs.getString("meangrade"), new Font(Font.FontFamily.HELVETICA, 7, Font.NORMAL, BaseColor.BLACK));
                    PdfPCell cell8 = new PdfPCell(ph8);

                    Phrase ph9 = new Phrase(rs.getString("classpositionthisterm"), new Font(Font.FontFamily.HELVETICA, 7, Font.NORMAL, BaseColor.BLACK));
                    PdfPCell cell9 = new PdfPCell(ph9);

                    Phrase ph10 = new Phrase(rs.getString("Streampositionthisterm"), new Font(Font.FontFamily.HELVETICA, 7, Font.NORMAL, BaseColor.BLACK));
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

                    studentcounter++;
                    if (studentcounter > number) {
                        break;
                    }

                }


            } else {

                int studentcounter = 1;
                String SQl = "Select classpositionthisterm,admnumber,Streampositionthisterm,Meangrade,TotalMarks,meanmarks,meanpoints,totalpoints,fullname,admnumber from examanalysistable where examcode='" + examcode + "' and classname='" + classname + "' and academicyear='" + academicyear + "' and stream='" + stream + "' group by admnumber order by Streampositionthisterm asc ";
                ps = con.prepareStatement(SQl);
                rs = ps.executeQuery();
                while (rs.next()) {


                    Phrase ph = new Phrase(String.valueOf(studentcounter), new Font(Font.FontFamily.HELVETICA, 7, Font.NORMAL, BaseColor.BLACK));
                    PdfPCell cell = new PdfPCell(ph);

                    cell.setVerticalAlignment(Element.ALIGN_BOTTOM);
                    cell.setHorizontalAlignment(Element.ALIGN_MIDDLE);
                    Phrase ph1 = new Phrase(rs.getString("admnumber"), new Font(Font.FontFamily.HELVETICA, 7, Font.NORMAL, BaseColor.BLACK));
                    PdfPCell cell1 = new PdfPCell(ph1);

                    cell1.setColspan(2);
                    Phrase ph2 = new Phrase(classname + " " + stream, new Font(Font.FontFamily.HELVETICA, 7, Font.NORMAL, BaseColor.BLACK));
                    PdfPCell cell2 = new PdfPCell(ph2);
                    cell2.setColspan(3);

                    Phrase ph3 = new Phrase(rs.getString("fullname"), new Font(Font.FontFamily.HELVETICA, 7, Font.NORMAL, BaseColor.BLACK));

                    PdfPCell cell3 = new PdfPCell(ph3);
                    cell3.setColspan(9);
                    cell3.setVerticalAlignment(Element.ALIGN_BOTTOM);
                    cell3.setHorizontalAlignment(Element.ALIGN_MIDDLE);
                    Phrase ph4 = new Phrase(rs.getString("totalmarks"), new Font(Font.FontFamily.HELVETICA, 7, Font.NORMAL, BaseColor.BLACK));
                    PdfPCell cell4 = new PdfPCell(ph4);

                    Phrase ph5 = new Phrase(rs.getString("Totalpoints"), new Font(Font.FontFamily.HELVETICA, 7, Font.NORMAL, BaseColor.BLACK));
                    PdfPCell cell5 = new PdfPCell(ph5);

                    Phrase ph6 = new Phrase("", new Font(Font.FontFamily.HELVETICA, 7, Font.NORMAL, BaseColor.BLACK));
                    PdfPCell cell6 = new PdfPCell(ph6);

                    Phrase ph6a = new Phrase(rs.getString("meanmarks"), new Font(Font.FontFamily.HELVETICA, 7, Font.NORMAL, BaseColor.BLACK));
                    PdfPCell cell6a = new PdfPCell(ph6a);

                    Phrase ph7 = new Phrase(rs.getString("meanpoints"), new Font(Font.FontFamily.HELVETICA, 7, Font.NORMAL, BaseColor.BLACK));
                    PdfPCell cell7 = new PdfPCell(ph7);

                    Phrase ph8 = new Phrase(rs.getString("meangrade"), new Font(Font.FontFamily.HELVETICA, 7, Font.NORMAL, BaseColor.BLACK));
                    PdfPCell cell8 = new PdfPCell(ph8);

                    Phrase ph9 = new Phrase(rs.getString("classpositionthisterm"), new Font(Font.FontFamily.HELVETICA, 7, Font.NORMAL, BaseColor.BLACK));
                    PdfPCell cell9 = new PdfPCell(ph9);

                    Phrase ph10 = new Phrase(rs.getString("Streampositionthisterm"), new Font(Font.FontFamily.HELVETICA, 7, Font.NORMAL, BaseColor.BLACK));
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

                    studentcounter++;
                    if (studentcounter > number) {
                        break;
                    }

                }


            }


            doc.add(tabb);
            doc.close();

            if (ConfigurationIntialiser.docOpener()) {
                if (dialog == null) {


                    dialog = ReportFrame.getReportFrameInstance(new File("C:\\schoolData\\BestStudentOverallReport.pdf"));


                } else {
                    dialog = null;
                    dialog = ReportFrame.getReportFrameInstance(new File("C:\\schoolData\\BestStudentOverallReport.pdf"));
                    dialog.setVisible(true);


                }

            } else {

                Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler " + "C:\\schoolData\\BestStudentOverallReport.pdf");

            }

        } catch (Exception sq) {
            sq.printStackTrace();
            JOptionPane.showMessageDialog(null, sq.getMessage());
        }

    }

    public static void mostImprovedArithmetics(String examcode, String examname, String AcademicYear, String classCode, String term, String examcode2, String examname2, String AcademicYear2, String classCode2, String term2) {

        try {
            nf.setMaximumFractionDigits(3);
            nf.setMinimumFractionDigits(3);

            con = DbConnection.connectDb();
            int entries = 0;
            boolean proceed = false;
            String sql = "Select count(*) from examanalysistable where examcode='" + examcode + "'";
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            if (rs.next()) {

                entries = rs.getInt("Count(*)");
            }

            if (entries > 1) {
                String sql2 = "Select count(*) from examanalysistable where examcode='" + examcode2 + "'";
                ps = con.prepareStatement(sql2);
                rs = ps.executeQuery();
                if (rs.next()) {
                    entries = rs.getInt("Count(*)");
                }

                if (entries > 1) {
                    proceed = true;
                } else {
                    proceed = false;
                    JOptionPane.showMessageDialog(null, "No Analysed Exam That Matches The Second Selected Exam(Upper Exam)\n Please Preview Its Marks On Score Sheet & analyse it If Not Analysed ");
                }

            } else {
                proceed = false;
                JOptionPane.showMessageDialog(null, "No Analysed Exam That Matches The First Selected Exam(Lower Exam)\n Please Preview Its Marks On Score Sheet & analyse it If Not Analysed ");
            }


            if (proceed) {

                String combinedCode = examcode + "-" + examcode2;

                String qrry = "Delete from improvementranking where examcodes='" + combinedCode + "'";
                ps = con.prepareStatement(qrry);
                ps.execute();
                String sq = "Select admnumber,Stream from examanalysistable where examcode='" + examcode + "' group by admnumber";
                ps = con.prepareStatement(sq);
                rs = ps.executeQuery();
                while (rs.next()) {

                    String stream = rs.getString("stream");
                    String adm = rs.getString("admNumber");
                    if (Globals.gradable(adm, examcode2, AcademicYear2)) {

                        if (Globals.gradable(adm, examcode, AcademicYear)) {

                            String sq2 = "Select subjectcode from subjects order by subjectcode";
                            ps = con.prepareStatement(sq2);
                            ResultSet RS = ps.executeQuery();
                            while (RS.next()) {
                                int score = 0, score2 = 0, divscore = 0, divmean = 0;
                                ps = con.prepareStatement("Select exampercentage from examanalysistable where examcode='" + examcode + "' and subjectcode='" + RS.getString("subjectcode") + "' and admnumber='" + adm + "'");
                                ResultSet rr = ps.executeQuery();
                                if (rr.next()) {
                                    score = rr.getInt("exampercentage");
                                }

                                ps = con.prepareStatement("Select exampercentage from examanalysistable where examcode='" + examcode2 + "' and subjectcode='" + RS.getString("subjectcode") + "' and admnumber='" + adm + "'");
                                rr = ps.executeQuery();
                                if (rr.next()) {
                                    score2 = rr.getInt("exampercentage");
                                }
                                divscore = score2 - score;


                                ps = con.prepareStatement("Insert into improvementranking (AdmNumber,examcodes,SubjectDiviation,SubjectCode,Stream) values('" + adm + "','" + combinedCode + "','" + divscore + "','" + RS.getString("subjectcode") + "','" + stream + "')");
                                ps.execute();


                            }


                            ///mean diviation calculations 

                            double mean = 0.0, mean2 = 0.0, divmean = 0.0;

                            ps = con.prepareStatement("Select meanpoints from examanalysistable where examcode='" + examcode + "'  and admnumber='" + adm + "'");
                            ResultSet rr = ps.executeQuery();
                            if (rr.next()) {
                                mean = rr.getDouble("meanpoints");
                            }

                            ps = con.prepareStatement("Select meanpoints from examanalysistable where examcode='" + examcode2 + "' and admnumber='" + adm + "'");
                            rr = ps.executeQuery();
                            if (rr.next()) {
                                mean2 = rr.getDouble("meanpoints");
                            }
                            divmean = mean2 - mean;

                            ps = con.prepareStatement("Update improvementranking set MeanDiviation='" + nf.format(divmean) + "' where examcodes='" + combinedCode + "' and admnumber='" + adm + "'");
                            ps.execute();


                        }
                    }

                }

                int classcapacity = 0, streamCapacity = 0;
//                 ps=con.prepareStatement("select count(*) from improvementranking where examcodes='"+combinedCode+"' group by admnumber");
//                 rs=ps.executeQuery();
//                 if(rs.next())
//                 {
//                  classcapacity=rs.getInt("Count(*)");
//                 }
//                 
                double prevoiusscore = 0.0;
                int counter = 0;
                int tiechecker = 0;
                int i = 0;
                String qr = "Select MeanDiviation,admNumber from improvementranking where examcodes='" + combinedCode + "' group by admnumber order by MeanDiviation desc";
                ps = con.prepareStatement(qr);
                ResultSet RR = ps.executeQuery();
                while (RR.next()) {

                    String adm = RR.getString("AdmNumber");


                    if (prevoiusscore == RR.getDouble("MeanDiviation")) {
                        tiechecker++;

                    } else {
                        counter++;
                        counter += tiechecker;
                        tiechecker = 0;
                    }

                    ps = con.prepareStatement("Update improvementranking set  ClassMeanPosition='" + counter + "' where admnumber='" + adm + "' and examcodes='" + combinedCode + "'");
                    ps.execute();
                    prevoiusscore = RR.getDouble("MeanDiviation");
                }


                String sql3 = "Select * from streams";
                ps = con.prepareStatement(sql3);
                ResultSet rr = ps.executeQuery();
                while (rr.next()) {
                    String streamcode = rr.getString("StreamName");
//                         streamCapacity=0;
//                 ps=con.prepareStatement("select count(*) from improvementranking where examcodes='"+combinedCode+"' and Stream='"+streamcode+"' group by admnumber");
//                 rs=ps.executeQuery();
//                 if(rs.next())
//                 {
//                  streamCapacity=rs.getInt("Count(*)");
//                 }
                    prevoiusscore = 0.0;
                    counter = 0;
                    tiechecker = 0;
                    String qr1 = "Select MeanDiviation,admnumber from improvementranking where examcodes='" + combinedCode + "' and Stream='" + streamcode + "' group by admnumber order by MeanDiviation desc";
                    ps = con.prepareStatement(qr1);
                    RR = ps.executeQuery();
                    while (RR.next()) {
                        String adm = RR.getString("AdmNumber");

                        if (prevoiusscore == RR.getDouble("MeanDiviation")) {
                            tiechecker++;

                        } else {
                            counter++;
                            counter += tiechecker;
                            tiechecker = 0;
                        }

                        ps = con.prepareStatement("Update improvementranking set  StreamMeanPosition='" + counter + "' where admnumber='" + adm + "' and examcodes='" + combinedCode + "'");
                        ps.execute();
                        prevoiusscore = RR.getDouble("MeanDiviation");
                    }

                }


                String qrrr = "Select * from Subjects order by subjectcode desc";
                ps = con.prepareStatement(qrrr);
                ResultSet rx = ps.executeQuery();
                while (rx.next()) {
                    String subcode = rx.getString("SubjectCode");

                    prevoiusscore = 0.0;
                    counter = 0;
                    tiechecker = 0;
                    i = 0;
                    qr = "Select SubjectDiviation,admNumber from improvementranking where examcodes='" + combinedCode + "' and subjectcode='" + subcode + "' group by admnumber order by SubjectDiviation desc";
                    ps = con.prepareStatement(qr);
                    RR = ps.executeQuery();
                    while (RR.next()) {

                        String adm = RR.getString("AdmNumber");


                        if (prevoiusscore == RR.getDouble("SubjectDiviation")) {
                            tiechecker++;

                        } else {
                            counter++;
                            counter += tiechecker;
                            tiechecker = 0;
                        }

                        ps = con.prepareStatement("Update improvementranking set  ClasssubjecPosition='" + counter + "' where admnumber='" + adm + "' and examcodes='" + combinedCode + "' and subjectcode='" + subcode + "'");
                        ps.execute();
                        prevoiusscore = RR.getDouble("SubjectDiviation");
                    }


                }


            }

        } catch (SQLException ex) {


            ex.printStackTrace();
            Logger.getLogger(BestStudentOverallReport.class.getName()).log(Level.SEVERE, null, ex);
        }


    }


    public static void improvedStudentsReport(String examcode, String examname, String AcademicYear, String classCode, String term, String examcode2, String examname2, String AcademicYear2, String classCode2, String term2, String stream, String classname, int limit) {
        try {

            nf.setMaximumFractionDigits(2);
            nf.setMinimumFractionDigits(2);


            con = DbConnection.connectDb();
            Document doc = new Document();
            com.itextpdf.text.Image image = com.itextpdf.text.Image.getInstance("C:/schooldata/logo.jpg");
            PdfWriter writer = PdfWriter.getInstance(doc, new FileOutputStream("C:\\schoolData\\MostImprovedOverallReport.pdf"));
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
            String st = "";
            if (stream.equalsIgnoreCase("Select Stream")) {
                st = "Overall";
            } else {
                st = stream;
            }
            tab.setWidthPercentage(100);
            doc.add(tab);
            Paragraph pr5 = new Paragraph("Most Improved Student Overall Report".toUpperCase(), FontFactory.getFont(FontFactory.HELVETICA, 13, java.awt.Font.BOLD, BaseColor.BLACK));
            Chunk glue = new Chunk(new VerticalPositionMark());
            doc.addCreationDate();
            Paragraph pr7 = new Paragraph("CLASS:  " + classname.toUpperCase() + "                 STREAM: " + st, FontFactory.getFont(FontFactory.TIMES, 13, java.awt.Font.PLAIN, BaseColor.BLACK));
            pr7.add(new Chunk(glue));

            Paragraph pr9 = new Paragraph("EXAM NAME:  " + Globals.className(classCode) + "  " + term + "  Year  " + AcademicYear + "    " + examname + "    To  " + Globals.className(classCode2) + "   " + term2 + " Year " + AcademicYear2 + "  " + examname2, FontFactory.getFont(FontFactory.TIMES, 13, java.awt.Font.PLAIN, BaseColor.BLACK));


            pr7.add("ACADEMIC YEAR: " + AcademicYear2);


            pr5.setIndentationLeft(150);
            doc.add(pr5);
            doc.add(pr7);
            doc.add(pr9);
            PdfPTable tabb = new PdfPTable(20);
            tabb.setSpacingBefore(10);
            tabb.setWidthPercentage(100);


            tabb.setSpacingBefore(10);
            tabb.setWidthPercentage(100);
            tabb.setHeaderRows(1);
            String combinedexcode = examcode + "-" + examcode2;
            {

                Phrase ph = new Phrase(" Serial No.", new Font(Font.FontFamily.HELVETICA, 7, Font.NORMAL, BaseColor.BLACK));
                PdfPCell cell = new PdfPCell(ph);
                cell.setRotation(90);
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

                Phrase ph4 = new Phrase("Diviation", new Font(Font.FontFamily.HELVETICA, 7, Font.NORMAL, BaseColor.BLACK));
                PdfPCell cell4 = new PdfPCell(ph4);
                cell4.setRotation(90);
                cell4.setColspan(3);
                Phrase ph5 = new Phrase("Class Position(OVR)", new Font(Font.FontFamily.HELVETICA, 7, Font.NORMAL, BaseColor.BLACK));
                PdfPCell cell5 = new PdfPCell(ph5);
                cell5.setRotation(90);
                cell5.setColspan(2);
                Phrase ph6 = new Phrase("Stream Position", new Font(Font.FontFamily.HELVETICA, 7, Font.NORMAL, BaseColor.BLACK));
                PdfPCell cell6 = new PdfPCell(ph6);
                cell6.setColspan(3);
                cell6.setRotation(90);
                tabb.addCell(cell);
                tabb.addCell(cell1);
                tabb.addCell(cell2);
                tabb.addCell(cell3);
                tabb.addCell(cell4);
                tabb.addCell(cell5);
                tabb.addCell(cell6);
            }
            if (stream.equalsIgnoreCase("Select Stream")) {
                ps = con.prepareStatement("Select Count(*) from improvementranking where examcodes='" + combinedexcode + "'");
                rs = ps.executeQuery();
                int count = 0;
                if (rs.next()) {
                    count = rs.getInt("Count(*)");
                }
                if (count >= 1) {
                    count = 0;
                    String sql = "Select * from improvementranking where examcodes='" + combinedexcode + "' group by admnumber order by meandiviation desc ";
                    ps = con.prepareStatement(sql);
                    rs = ps.executeQuery();
                    while (rs.next()) {
                        count++;
                        Phrase ph = new Phrase(String.valueOf(count), new Font(Font.FontFamily.HELVETICA, 7, Font.NORMAL, BaseColor.BLACK));
                        PdfPCell cell = new PdfPCell(ph);

                        cell.setVerticalAlignment(Element.ALIGN_BOTTOM);
                        cell.setHorizontalAlignment(Element.ALIGN_MIDDLE);
                        Phrase ph1 = new Phrase(rs.getString("Admnumber"), new Font(Font.FontFamily.HELVETICA, 7, Font.NORMAL, BaseColor.BLACK));
                        PdfPCell cell1 = new PdfPCell(ph1);

                        cell1.setColspan(2);
                        Phrase ph2 = new Phrase(classname + "  " + rs.getString("stream"), new Font(Font.FontFamily.HELVETICA, 7, Font.NORMAL, BaseColor.BLACK));
                        PdfPCell cell2 = new PdfPCell(ph2);
                        cell2.setColspan(3);

                        Phrase ph3 = new Phrase(Globals.fullName(rs.getString("AdmNumber")), new Font(Font.FontFamily.HELVETICA, 7, Font.NORMAL, BaseColor.BLACK));
                        PdfPCell cell3 = new PdfPCell(ph3);
                        cell3.setColspan(6);
                        cell3.setVerticalAlignment(Element.ALIGN_BOTTOM);


                        Phrase ph4 = new Phrase(rs.getString("MeanDiviation"), new Font(Font.FontFamily.HELVETICA, 7, Font.NORMAL, BaseColor.BLACK));
                        PdfPCell cell4 = new PdfPCell(ph4);

                        cell4.setColspan(3);
                        Phrase ph5 = new Phrase(rs.getString("ClassMeanPosition"), new Font(Font.FontFamily.HELVETICA, 7, Font.NORMAL, BaseColor.BLACK));
                        PdfPCell cell5 = new PdfPCell(ph5);

                        cell5.setColspan(2);
                        Phrase ph6 = new Phrase(rs.getString("StreamMeanPosition"), new Font(Font.FontFamily.HELVETICA, 7, Font.NORMAL, BaseColor.BLACK));
                        PdfPCell cell6 = new PdfPCell(ph6);
                        cell6.setColspan(3);

                        tabb.addCell(cell);
                        tabb.addCell(cell1);
                        tabb.addCell(cell2);
                        tabb.addCell(cell3);
                        tabb.addCell(cell4);
                        tabb.addCell(cell5);
                        tabb.addCell(cell6);
                        if (limit != 0 && count == limit) {
                            break;
                        }

                    }

                } else {
                    JOptionPane.showMessageDialog(null, "The System Has Not Detected Any Improvement Report On The Selected Exams\n Kindly Select Analyse First In Order To Generate The Report For The Selected Exams");
                }


            } else {

                ps = con.prepareStatement("Select Count(*) from improvementranking where examcodes='" + combinedexcode + "'");
                rs = ps.executeQuery();
                int count = 0;
                if (rs.next()) {
                    count = rs.getInt("Count(*)");
                }
                if (count >= 1) {
                    count = 0;
                    String sql = "Select * from improvementranking where examcodes='" + combinedexcode + "' and stream='" + stream + "' group by admnumber order by meandiviation desc ";
                    ps = con.prepareStatement(sql);
                    rs = ps.executeQuery();
                    while (rs.next()) {
                        count++;
                        Phrase ph = new Phrase(String.valueOf(count), new Font(Font.FontFamily.HELVETICA, 7, Font.NORMAL, BaseColor.BLACK));
                        PdfPCell cell = new PdfPCell(ph);

                        cell.setVerticalAlignment(Element.ALIGN_BOTTOM);
                        cell.setHorizontalAlignment(Element.ALIGN_MIDDLE);
                        Phrase ph1 = new Phrase(rs.getString("Admnumber"), new Font(Font.FontFamily.HELVETICA, 7, Font.NORMAL, BaseColor.BLACK));
                        PdfPCell cell1 = new PdfPCell(ph1);

                        cell1.setColspan(2);
                        Phrase ph2 = new Phrase(classname + "  " + rs.getString("stream"), new Font(Font.FontFamily.HELVETICA, 7, Font.NORMAL, BaseColor.BLACK));
                        PdfPCell cell2 = new PdfPCell(ph2);
                        cell2.setColspan(3);

                        Phrase ph3 = new Phrase(Globals.fullName(rs.getString("AdmNumber")), new Font(Font.FontFamily.HELVETICA, 7, Font.NORMAL, BaseColor.BLACK));
                        PdfPCell cell3 = new PdfPCell(ph3);
                        cell3.setColspan(6);
                        cell3.setVerticalAlignment(Element.ALIGN_BOTTOM);


                        Phrase ph4 = new Phrase(rs.getString("MeanDiviation"), new Font(Font.FontFamily.HELVETICA, 7, Font.NORMAL, BaseColor.BLACK));
                        PdfPCell cell4 = new PdfPCell(ph4);

                        cell4.setColspan(3);
                        Phrase ph5 = new Phrase(rs.getString("ClassMeanPosition"), new Font(Font.FontFamily.HELVETICA, 7, Font.NORMAL, BaseColor.BLACK));
                        PdfPCell cell5 = new PdfPCell(ph5);

                        cell5.setColspan(2);
                        Phrase ph6 = new Phrase(rs.getString("StreamMeanPosition"), new Font(Font.FontFamily.HELVETICA, 7, Font.NORMAL, BaseColor.BLACK));
                        PdfPCell cell6 = new PdfPCell(ph6);
                        cell6.setColspan(3);

                        tabb.addCell(cell);
                        tabb.addCell(cell1);
                        tabb.addCell(cell2);
                        tabb.addCell(cell3);
                        tabb.addCell(cell4);
                        tabb.addCell(cell5);
                        tabb.addCell(cell6);
                        if (limit != 0 && count == limit) {
                            break;
                        }
                    }

                } else {
                    JOptionPane.showMessageDialog(null, "The System Has Not Detected Any Improvement Report On The Selected Exams\n Kindly Select Analyse First In Order To Generate The Report For The Selected Exams");
                }

            }


            doc.add(tabb);
            doc.close();

            if (ConfigurationIntialiser.docOpener()) {
                if (dialog == null) {


                    dialog = ReportFrame.getReportFrameInstance(new File("C:\\schoolData\\MostImprovedOverallReport.pdf"));


                } else {
                    dialog = null;
                    dialog = ReportFrame.getReportFrameInstance(new File("C:\\schoolData\\MostImprovedOverallReport.pdf"));
                    dialog.setVisible(true);


                }

            } else {

                Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler " + "C:\\schoolData\\MostImprovedOverallReport.pdf");

            }

        } catch (Exception sq) {
            sq.printStackTrace();
            JOptionPane.showMessageDialog(null, sq.getMessage());
        }

    }


    public static void improvedStudentsReportPerSubject(String examcode, String examname, String AcademicYear, String classCode, String term, String examcode2, String examname2, String AcademicYear2, String classCode2, String term2, String classname, String subjectCode, int limit) {


        try {

            nf.setMaximumFractionDigits(2);
            nf.setMinimumFractionDigits(2);


            con = DbConnection.connectDb();
            Document doc = new Document();
            com.itextpdf.text.Image image = com.itextpdf.text.Image.getInstance("C:/schooldata/logo.jpg");
            PdfWriter writer = PdfWriter.getInstance(doc, new FileOutputStream("C:\\schoolData\\MostImprovedPerSubjectReport.pdf"));
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
            String st = "";
            tab.setWidthPercentage(100);
            doc.add(tab);
            Paragraph pr5 = new Paragraph("Most Improved Student Per Subject  Report".toUpperCase(), FontFactory.getFont(FontFactory.HELVETICA, 13, java.awt.Font.BOLD, BaseColor.BLACK));
            Chunk glue = new Chunk(new VerticalPositionMark());
            doc.addCreationDate();
            Paragraph pr7 = new Paragraph("CLASS:  " + classname.toUpperCase() + "                 STREAM: " + st, FontFactory.getFont(FontFactory.TIMES, 13, java.awt.Font.PLAIN, BaseColor.BLACK));
            pr7.add(new Chunk(glue));

            Paragraph pr9 = new Paragraph("EXAM NAME:  " + Globals.className(classCode) + "  " + term + "  Year  " + AcademicYear + "    " + examname + "    To  " + Globals.className(classCode2) + "   " + term2 + " Year " + AcademicYear2 + "  " + examname2, FontFactory.getFont(FontFactory.TIMES, 13, java.awt.Font.PLAIN, BaseColor.BLACK));


            pr7.add("ACADEMIC YEAR: " + AcademicYear2);


            pr5.setIndentationLeft(150);
            doc.add(pr5);
            doc.add(pr7);
            doc.add(pr9);
            PdfPTable tabb = new PdfPTable(20);
            tabb.setSpacingBefore(10);
            tabb.setWidthPercentage(100);
            tabb.setHeaderRows(1);

            tabb.setSpacingBefore(10);
            tabb.setWidthPercentage(100);

            String combinedexcode = examcode + "-" + examcode2;
            {

                Phrase ph = new Phrase(" Serial No.", new Font(Font.FontFamily.HELVETICA, 7, Font.NORMAL, BaseColor.BLACK));
                PdfPCell cell = new PdfPCell(ph);
                cell.setRotation(90);
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

                Phrase ph4 = new Phrase("Diviation", new Font(Font.FontFamily.HELVETICA, 7, Font.NORMAL, BaseColor.BLACK));
                PdfPCell cell4 = new PdfPCell(ph4);
                cell4.setRotation(90);
                cell4.setColspan(3);
                Phrase ph5 = new Phrase("Class Position(OVR)", new Font(Font.FontFamily.HELVETICA, 7, Font.NORMAL, BaseColor.BLACK));
                PdfPCell cell5 = new PdfPCell(ph5);
                cell5.setRotation(90);
                cell5.setColspan(2);
                Phrase ph6 = new Phrase("", new Font(Font.FontFamily.HELVETICA, 7, Font.NORMAL, BaseColor.BLACK));
                PdfPCell cell6 = new PdfPCell(ph6);
                cell6.setColspan(3);
                cell6.setRotation(90);
                tabb.addCell(cell);
                tabb.addCell(cell1);
                tabb.addCell(cell2);
                tabb.addCell(cell3);
                tabb.addCell(cell4);
                tabb.addCell(cell5);
                tabb.addCell(cell6);
            }

            if (subjectCode.equalsIgnoreCase("Subject Code")) {
                String sq = "Select * from Subjects";
                ps = con.prepareStatement(sq);
                ResultSet rx = ps.executeQuery();
                while (rx.next()) {
                    Phrase pha = new Phrase(rx.getString("SubjectName"), new Font(Font.FontFamily.HELVETICA, 12, Font.NORMAL, BaseColor.BLACK));
                    PdfPCell cella = new PdfPCell(pha);
                    cella.setColspan(20);
                    tabb.addCell(cella);
                    subjectCode = rx.getString("Subjectcode");
                    ps = con.prepareStatement("Select Count(*) from improvementranking where examcodes='" + combinedexcode + "' and subjectcode='" + subjectCode + "'");
                    rs = ps.executeQuery();
                    int count = 0;
                    if (rs.next()) {
                        count = rs.getInt("Count(*)");
                    }
                    if (count >= 1) {
                        count = 0;
                        String sql = "Select * from improvementranking where examcodes='" + combinedexcode + "' and subjectcode='" + subjectCode + "' group by admnumber order by ClasssubjecPosition asc ";
                        ps = con.prepareStatement(sql);
                        rs = ps.executeQuery();
                        while (rs.next()) {
                            count++;
                            Phrase ph = new Phrase(String.valueOf(count), new Font(Font.FontFamily.HELVETICA, 7, Font.NORMAL, BaseColor.BLACK));
                            PdfPCell cell = new PdfPCell(ph);

                            cell.setVerticalAlignment(Element.ALIGN_BOTTOM);
                            cell.setHorizontalAlignment(Element.ALIGN_MIDDLE);
                            Phrase ph1 = new Phrase(rs.getString("Admnumber"), new Font(Font.FontFamily.HELVETICA, 7, Font.NORMAL, BaseColor.BLACK));
                            PdfPCell cell1 = new PdfPCell(ph1);

                            cell1.setColspan(2);
                            Phrase ph2 = new Phrase(classname + "  " + rs.getString("stream"), new Font(Font.FontFamily.HELVETICA, 7, Font.NORMAL, BaseColor.BLACK));
                            PdfPCell cell2 = new PdfPCell(ph2);
                            cell2.setColspan(3);

                            Phrase ph3 = new Phrase(Globals.fullName(rs.getString("AdmNumber")), new Font(Font.FontFamily.HELVETICA, 7, Font.NORMAL, BaseColor.BLACK));
                            PdfPCell cell3 = new PdfPCell(ph3);
                            cell3.setColspan(6);
                            cell3.setVerticalAlignment(Element.ALIGN_BOTTOM);


                            Phrase ph4 = new Phrase(rs.getString("SubjectDiviation"), new Font(Font.FontFamily.HELVETICA, 7, Font.NORMAL, BaseColor.BLACK));
                            PdfPCell cell4 = new PdfPCell(ph4);

                            cell4.setColspan(3);
                            Phrase ph5 = new Phrase(rs.getString("ClasssubjecPosition"), new Font(Font.FontFamily.HELVETICA, 7, Font.NORMAL, BaseColor.BLACK));
                            PdfPCell cell5 = new PdfPCell(ph5);

                            cell5.setColspan(2);
                            Phrase ph6 = new Phrase("", new Font(Font.FontFamily.HELVETICA, 7, Font.NORMAL, BaseColor.BLACK));
                            PdfPCell cell6 = new PdfPCell(ph6);
                            cell6.setColspan(3);

                            tabb.addCell(cell);
                            tabb.addCell(cell1);
                            tabb.addCell(cell2);
                            tabb.addCell(cell3);
                            tabb.addCell(cell4);
                            tabb.addCell(cell5);
                            tabb.addCell(cell6);
                            if (limit != 0 && count == limit) {
                                break;
                            }

                        }

                    }


                }
            } else {
                ps = con.prepareStatement("Select Count(*) from improvementranking where examcodes='" + combinedexcode + "' and subjectcode='" + subjectCode + "'");
                rs = ps.executeQuery();
                int count = 0;
                if (rs.next()) {
                    count = rs.getInt("Count(*)");
                }
                if (count >= 1) {
                    Phrase pha = new Phrase(Globals.subjectName(subjectCode), new Font(Font.FontFamily.HELVETICA, 12, Font.NORMAL, BaseColor.BLACK));
                    PdfPCell cella = new PdfPCell(pha);
                    cella.setColspan(20);
                    tabb.addCell(cella);
                    count = 0;
                    String sql = "Select * from improvementranking where examcodes='" + combinedexcode + "' and subjectcode='" + subjectCode + "' group by admnumber order by ClasssubjecPosition asc ";
                    ps = con.prepareStatement(sql);
                    rs = ps.executeQuery();
                    while (rs.next()) {
                        count++;
                        Phrase ph = new Phrase(String.valueOf(count), new Font(Font.FontFamily.HELVETICA, 7, Font.NORMAL, BaseColor.BLACK));
                        PdfPCell cell = new PdfPCell(ph);

                        cell.setVerticalAlignment(Element.ALIGN_BOTTOM);
                        cell.setHorizontalAlignment(Element.ALIGN_MIDDLE);
                        Phrase ph1 = new Phrase(rs.getString("Admnumber"), new Font(Font.FontFamily.HELVETICA, 7, Font.NORMAL, BaseColor.BLACK));
                        PdfPCell cell1 = new PdfPCell(ph1);

                        cell1.setColspan(2);
                        Phrase ph2 = new Phrase(classname + "  " + rs.getString("stream"), new Font(Font.FontFamily.HELVETICA, 7, Font.NORMAL, BaseColor.BLACK));
                        PdfPCell cell2 = new PdfPCell(ph2);
                        cell2.setColspan(3);

                        Phrase ph3 = new Phrase(Globals.fullName(rs.getString("AdmNumber")), new Font(Font.FontFamily.HELVETICA, 7, Font.NORMAL, BaseColor.BLACK));
                        PdfPCell cell3 = new PdfPCell(ph3);
                        cell3.setColspan(6);
                        cell3.setVerticalAlignment(Element.ALIGN_BOTTOM);


                        Phrase ph4 = new Phrase(rs.getString("SubjectDiviation"), new Font(Font.FontFamily.HELVETICA, 7, Font.NORMAL, BaseColor.BLACK));
                        PdfPCell cell4 = new PdfPCell(ph4);

                        cell4.setColspan(3);
                        Phrase ph5 = new Phrase(rs.getString("ClasssubjecPosition"), new Font(Font.FontFamily.HELVETICA, 7, Font.NORMAL, BaseColor.BLACK));
                        PdfPCell cell5 = new PdfPCell(ph5);

                        cell5.setColspan(2);
                        Phrase ph6 = new Phrase("", new Font(Font.FontFamily.HELVETICA, 7, Font.NORMAL, BaseColor.BLACK));
                        PdfPCell cell6 = new PdfPCell(ph6);
                        cell6.setColspan(3);

                        tabb.addCell(cell);
                        tabb.addCell(cell1);
                        tabb.addCell(cell2);
                        tabb.addCell(cell3);
                        tabb.addCell(cell4);
                        tabb.addCell(cell5);
                        tabb.addCell(cell6);
                        if (limit != 0 && count == limit) {
                            break;
                        }

                    }

                } else {
                    JOptionPane.showMessageDialog(null, "The System Has Not Detected Any Improvement Report On The Selected Exams/Subject\n Kindly Select Analyse First In Order To Generate The Report For The Selected Exams");
                }

            }


            doc.add(tabb);
            doc.close();


            if (ConfigurationIntialiser.docOpener()) {
                if (dialog == null) {


                    dialog = ReportFrame.getReportFrameInstance(new File("C:\\schoolData\\MostImprovedPerSubjectReport.pdf"));


                } else {
                    dialog = null;
                    dialog = ReportFrame.getReportFrameInstance(new File("C:\\schoolData\\MostImprovedPerSubjectReport.pdf"));
                    dialog.setVisible(true);


                }

            } else {

                Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler " + "C:\\schoolData\\MostImprovedOverallReport.pdf");

            }

        } catch (Exception sq) {
            sq.printStackTrace();

        }

    }


}
