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
import java.util.Date;
import javax.swing.JDialog;
import javax.swing.JOptionPane;

/**
 * @author FRED
 */
public class BestStudentReport {
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

    public static void subjectResults(String examcode, String examname, String term, String academicyear, String subjectcode, String classname, String subjectname, String number) {
        try {


            Document doc = new Document();
            com.itextpdf.text.Image image = com.itextpdf.text.Image.getInstance("C:/schooldata/logo.jpg");
            PdfWriter writer = PdfWriter.getInstance(doc, new FileOutputStream("C:\\schoolData\\BestStudentperSubject.pdf"));
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
            Paragraph pr5 = new Paragraph("Best STudent per subject report".toUpperCase(), FontFactory.getFont(FontFactory.HELVETICA, 13, java.awt.Font.BOLD, BaseColor.BLACK));
            Chunk glue = new Chunk(new VerticalPositionMark());
            doc.addCreationDate();
            Paragraph pr7 = new Paragraph("CLASS:  " + classname.toUpperCase(), FontFactory.getFont(FontFactory.TIMES, 13, java.awt.Font.PLAIN, BaseColor.BLACK));
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

            if (subjectcode.equalsIgnoreCase("All")) {


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

                    String SQL = "Select * from Subjects";
                    ps = con.prepareStatement(SQL);
                    ResultSet rx = ps.executeQuery();
                    while (rx.next()) {

                        subjectcode = rx.getString("subjectcode");
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


                }


                String SQL = "Select * from Subjects";
                ps = con.prepareStatement(SQL);
                ResultSet rx = ps.executeQuery();
                while (rx.next()) {
                    Phrase ph1SubjectName = new Phrase(rx.getString("SubjectName"), new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL, BaseColor.BLACK));
                    PdfPCell cell1SubjectName = new PdfPCell(ph1SubjectName);
                    cell1SubjectName.setVerticalAlignment(Element.ALIGN_BOTTOM);

                    cell1SubjectName.setColspan(24);
                    tabb.addCell(cell1SubjectName);

                    subjectcode = rx.getString("subjectcode");

                    int counter = 1;
                    String sql = "Select * from markstable where examcode='" + examcode + "' and classcode='" + Globals.classCode(classname) + "' and academicyear='" + academicyear + "'  and subjectcode='" + subjectcode + "' order by position_per_subject asc";
                    ps = con.prepareStatement(sql);
                    rs = ps.executeQuery();
                    while (rs.next()) {
                        Phrase ph = new Phrase(String.valueOf(counter), new Font(Font.FontFamily.HELVETICA, 7, Font.NORMAL, BaseColor.BLACK));
                        PdfPCell cell = new PdfPCell(ph);

                        Phrase ph1 = new Phrase(rs.getString("admNumber"), new Font(Font.FontFamily.HELVETICA, 7, Font.NORMAL, BaseColor.BLACK));
                        PdfPCell cell1 = new PdfPCell(ph1);

                        cell1.setColspan(2);
                        Phrase ph2 = new Phrase(classname + " " + Globals.streamName(rs.getString("StreamCode")), new Font(Font.FontFamily.HELVETICA, 7, Font.NORMAL, BaseColor.BLACK));
                        PdfPCell cell2 = new PdfPCell(ph2);
                        cell2.setColspan(3);

                        Phrase ph3 = new Phrase(Globals.fullName(rs.getString("admnumber")), new Font(Font.FontFamily.HELVETICA, 7, Font.NORMAL, BaseColor.BLACK));

                        PdfPCell cell3 = new PdfPCell(ph3);
                        cell3.setColspan(9);

                        Phrase ph4 = new Phrase(rs.getString("examScore"), new Font(Font.FontFamily.HELVETICA, 7, Font.NORMAL, BaseColor.BLACK));
                        PdfPCell cell4 = new PdfPCell(ph4);

                        Phrase ph5 = new Phrase(rs.getString("Convertedscore"), new Font(Font.FontFamily.HELVETICA, 7, Font.NORMAL, BaseColor.BLACK));
                        PdfPCell cell5 = new PdfPCell(ph5);

                        Phrase ph6 = new Phrase(rs.getString("Exampercentage"), new Font(Font.FontFamily.HELVETICA, 7, Font.NORMAL, BaseColor.BLACK));
                        PdfPCell cell6 = new PdfPCell(ph6);

                        Phrase ph6a = new Phrase(rs.getString("exampoints"), new Font(Font.FontFamily.HELVETICA, 7, Font.NORMAL, BaseColor.BLACK));
                        PdfPCell cell6a = new PdfPCell(ph6a);

                        Phrase ph7 = new Phrase(rs.getString("ExamGrade"), new Font(Font.FontFamily.HELVETICA, 7, Font.NORMAL, BaseColor.BLACK));
                        PdfPCell cell7 = new PdfPCell(ph7);

                        Phrase ph8 = new Phrase(rs.getString("position_per_subject"), new Font(Font.FontFamily.HELVETICA, 7, Font.NORMAL, BaseColor.BLACK));
                        PdfPCell cell8 = new PdfPCell(ph8);

                        Phrase ph9 = new Phrase(rs.getString("position_per_subject_out_of"), new Font(Font.FontFamily.HELVETICA, 7, Font.NORMAL, BaseColor.BLACK));
                        PdfPCell cell9 = new PdfPCell(ph9);

                        Phrase ph10 = new Phrase(rs.getString("class_teacher_initials"), new Font(Font.FontFamily.HELVETICA, 7, Font.NORMAL, BaseColor.BLACK));
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
                        if (counter > Integer.parseInt(number)) {
                            break;
                        }
                    }


                }


            } else {


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
                String sql = "Select * from markstable where examcode='" + examcode + "' and classcode='" + Globals.classCode(classname) + "' and academicyear='" + academicyear + "'  and subjectcode='" + subjectcode + "' order by position_per_subject asc";
                ps = con.prepareStatement(sql);
                rs = ps.executeQuery();
                while (rs.next()) {
                    Phrase ph = new Phrase(String.valueOf(counter), new Font(Font.FontFamily.HELVETICA, 7, Font.NORMAL, BaseColor.BLACK));
                    PdfPCell cell = new PdfPCell(ph);

                    Phrase ph1 = new Phrase(rs.getString("admNumber"), new Font(Font.FontFamily.HELVETICA, 7, Font.NORMAL, BaseColor.BLACK));
                    PdfPCell cell1 = new PdfPCell(ph1);

                    cell1.setColspan(2);
                    Phrase ph2 = new Phrase(classname + " " + Globals.streamName(rs.getString("StreamCode")), new Font(Font.FontFamily.HELVETICA, 7, Font.NORMAL, BaseColor.BLACK));
                    PdfPCell cell2 = new PdfPCell(ph2);
                    cell2.setColspan(3);

                    Phrase ph3 = new Phrase(Globals.fullName(rs.getString("admnumber")), new Font(Font.FontFamily.HELVETICA, 7, Font.NORMAL, BaseColor.BLACK));

                    PdfPCell cell3 = new PdfPCell(ph3);
                    cell3.setColspan(9);

                    Phrase ph4 = new Phrase(rs.getString("examScore"), new Font(Font.FontFamily.HELVETICA, 7, Font.NORMAL, BaseColor.BLACK));
                    PdfPCell cell4 = new PdfPCell(ph4);

                    Phrase ph5 = new Phrase(rs.getString("Convertedscore"), new Font(Font.FontFamily.HELVETICA, 7, Font.NORMAL, BaseColor.BLACK));
                    PdfPCell cell5 = new PdfPCell(ph5);

                    Phrase ph6 = new Phrase(rs.getString("Exampercentage"), new Font(Font.FontFamily.HELVETICA, 7, Font.NORMAL, BaseColor.BLACK));
                    PdfPCell cell6 = new PdfPCell(ph6);

                    Phrase ph6a = new Phrase(rs.getString("exampoints"), new Font(Font.FontFamily.HELVETICA, 7, Font.NORMAL, BaseColor.BLACK));
                    PdfPCell cell6a = new PdfPCell(ph6a);

                    Phrase ph7 = new Phrase(rs.getString("ExamGrade"), new Font(Font.FontFamily.HELVETICA, 7, Font.NORMAL, BaseColor.BLACK));
                    PdfPCell cell7 = new PdfPCell(ph7);

                    Phrase ph8 = new Phrase(rs.getString("position_per_subject"), new Font(Font.FontFamily.HELVETICA, 7, Font.NORMAL, BaseColor.BLACK));
                    PdfPCell cell8 = new PdfPCell(ph8);

                    Phrase ph9 = new Phrase(rs.getString("position_per_subject_out_of"), new Font(Font.FontFamily.HELVETICA, 7, Font.NORMAL, BaseColor.BLACK));
                    PdfPCell cell9 = new PdfPCell(ph9);

                    Phrase ph10 = new Phrase(rs.getString("class_teacher_initials"), new Font(Font.FontFamily.HELVETICA, 7, Font.NORMAL, BaseColor.BLACK));
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
                    if (counter > Integer.parseInt(number)) {
                        break;
                    }
                }

            }

            doc.add(tabb);


            doc.close();


            if (ConfigurationIntialiser.docOpener()) {
                if (dialog == null) {


                    dialog = ReportFrame.getReportFrameInstance(new File("C:\\schoolData\\BestStudentperSubject.pdf"));


                } else {
                    dialog = null;
                    dialog = ReportFrame.getReportFrameInstance(new File("C:\\schoolData\\BestStudentperSubject.pdf"));
                    dialog.setVisible(true);


                }

            } else {

                Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler " + "C:\\schoolData\\BestStudentperSubject.pdf");

            }
        } catch (Exception sq) {
            sq.printStackTrace();
            JOptionPane.showMessageDialog(null, sq.getMessage());
        }
    }


}
