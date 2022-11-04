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
import java.text.NumberFormat;
import java.util.Date;
import javax.swing.JDialog;
import javax.swing.JOptionPane;

/**
 * @author FRED
 */
public class GradeDistributionReport {

    static PreparedStatement ps;
    static ResultSet rs;
    static Connection con;
    static JDialog dialog = null;
    static DbConnection db = new DbConnection();
    static NumberFormat nf = NumberFormat.getNumberInstance();
    static NumberFormat nf2 = NumberFormat.getNumberInstance();

    public GradeDistributionReport() {
        nf.setMaximumFractionDigits(2);
        nf.setMinimumFractionDigits(2);
    }

    public static void onEndPage(PdfWriter writer, Document document) {
        Font ffont = new Font(Font.FontFamily.UNDEFINED, 5, Font.ITALIC);
        PdfContentByte cb = writer.getDirectContent();

        Phrase footer = new Phrase("Lunar Technologies", ffont);

        ColumnText.showTextAligned(cb, Element.ALIGN_RIGHT,
                footer,
                (document.right() - document.left()) / 2 + document.leftMargin(),
                document.bottom() - 10, 0);
    }

    public static void gradedistributionReport(String examcode, String examname, String term, String classname, String academicyear, String stream, String sort, boolean targets) {
        nf2.setMaximumFractionDigits(0);
        nf2.setMinimumFractionDigits(0);
        try {
            nf.setMaximumFractionDigits(2);
            nf.setMinimumFractionDigits(2);

            con = DbConnection.connectDb();
            Document doc = new Document();
            com.itextpdf.text.Image image = com.itextpdf.text.Image.getInstance("C:/schooldata/logo.jpg");
            PdfWriter writer = PdfWriter.getInstance(doc, new FileOutputStream("C:\\schoolData\\gradedistributionReport.pdf"));
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
            Paragraph pr5 = new Paragraph("Grade Distribution Report", FontFactory.getFont(FontFactory.HELVETICA, 13, java.awt.Font.BOLD, BaseColor.BLACK));
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
            PdfPTable tabb = new PdfPTable(28);
            if (targets) {
                tabb = new PdfPTable(32);
            } else {
                tabb = new PdfPTable(28);
            }
            tabb.setSpacingBefore(10);
            tabb.setWidthPercentage(100);
            int totalwidth = 0;
            if (stream.equalsIgnoreCase("OverAll")) {
                if (sort.equalsIgnoreCase("gender")) {


                    {///table headers
                        Phrase ph1 = new Phrase("STREAM", new Font(Font.FontFamily.HELVETICA, 9, Font.NORMAL, BaseColor.BLACK));
                        PdfPCell cell1 = new PdfPCell(ph1);
                        cell1.setColspan(4);
                        cell1.setVerticalAlignment(Element.ALIGN_BOTTOM);
                        cell1.setHorizontalAlignment(Element.ALIGN_MIDDLE);
                        tabb.addCell(cell1);

                        Phrase ph2 = new Phrase("Exam Code".toUpperCase(), new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL, BaseColor.BLACK));
                        PdfPCell cell2 = new PdfPCell(ph2);
                        cell2.setColspan(6);
                        cell2.setVerticalAlignment(Element.ALIGN_BOTTOM);
                        cell2.setHorizontalAlignment(Element.ALIGN_MIDDLE);
                        tabb.addCell(cell2);

                        String sql2 = "Select * from points_for_each_grade order by points desc";
                        ps = con.prepareStatement(sql2);
                        rs = ps.executeQuery();
                        while (rs.next()) {

                            Phrase ph3 = new Phrase(rs.getString("grade"), new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL, BaseColor.BLACK));
                            PdfPCell cell3 = new PdfPCell(ph3);
                            cell3.setVerticalAlignment(Element.ALIGN_BOTTOM);
                            cell3.setHorizontalAlignment(Element.ALIGN_MIDDLE);

                            tabb.addCell(cell3);
                        }

                        Phrase ph4 = new Phrase("Entries", new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL, BaseColor.BLACK));
                        PdfPCell cell4 = new PdfPCell(ph4);
                        cell4.setColspan(2);
                        cell4.setRotation(90);
                        tabb.addCell(cell4);

                        Phrase ph5 = new Phrase("Mean Score", new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL, BaseColor.BLACK));
                        PdfPCell cell5 = new PdfPCell(ph5);
                        cell5.setColspan(2);
                        cell5.setRotation(90);
                        tabb.addCell(cell5);

                        Phrase ph6 = new Phrase("Mean Grade", new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL, BaseColor.BLACK));
                        PdfPCell cell6 = new PdfPCell(ph6);
                        cell6.setRotation(90);
                        cell6.setColspan(2);

                        tabb.addCell(cell6);

                        if (targets) {
                            Phrase ph7 = new Phrase("Target Points", new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL, BaseColor.BLACK));
                            PdfPCell cell7 = new PdfPCell(ph7);
                            cell7.setRotation(90);
                            cell7.setColspan(2);
                            tabb.addCell(cell7);
                            Phrase ph8 = new Phrase("Target Grade", new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL, BaseColor.BLACK));
                            PdfPCell cell8 = new PdfPCell(ph8);
                            cell8.setRotation(90);
                            cell8.setColspan(2);
                            tabb.addCell(cell8);
                        }

                    }

                    for (int i = 1; i <= 2; i++) {
                        String genderValue = "", sex;
                        if (i == 1) {
                            genderValue = "Male";
                            sex = "Boys".toUpperCase();
                        } else {
                            genderValue = "female";
                            sex = "Girls".toUpperCase();
                        }

                        {
                            //real distribution from db
                            Phrase ph1a = new Phrase(sex, new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.NORMAL, BaseColor.BLACK));
                            PdfPCell cell1a = new PdfPCell(ph1a);
                            if (targets) {
                                cell1a.setColspan(32);
                            } else {
                                cell1a.setColspan(28);
                            }
                            tabb.addCell(cell1a);


                            Phrase ph1 = new Phrase(stream + "\n ", new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL, BaseColor.BLACK));
                            PdfPCell cell1 = new PdfPCell(ph1);
                            cell1.setColspan(4);
                            tabb.addCell(cell1);

                            Phrase ph2 = new Phrase(examcode, new Font(Font.FontFamily.HELVETICA, 8, Font.NORMAL, BaseColor.BLACK));
                            PdfPCell cell2 = new PdfPCell(ph2);
                            cell2.setRowspan(3);
                            cell2.setColspan(6);

                            tabb.addCell(cell2);
                            int COUNTER = 0;
                            int gradepoint = 0;
                            double totalpoints = 0.0;
                            String sql2 = "Select * from points_for_each_grade order by points desc";
                            ps = con.prepareStatement(sql2);
                            rs = ps.executeQuery();
                            while (rs.next()) {
                                gradepoint = rs.getInt("Points");
                                String grade = rs.getString("grade");
                                int count = 0;
                                String sql1 = "Select admnumber,meanpoints from examanalysistable,admission where examcode='" + examcode + "' and gender='" + genderValue + "' and admnumber=admissionNumber and classname='" + classname + "' and academicyear='" + academicyear + "' and meangrade='" + grade + "' group by admnumber";
                                ps = con.prepareStatement(sql1);
                                ResultSet rsx = ps.executeQuery();
                                while (rsx.next()) {
                                    String adm = rsx.getString("AdmNumber");
                                    if (Globals.gradable(adm, examcode, academicyear)) {
                                        totalpoints += rsx.getDouble("meanpoints");
                                        count++;
                                        COUNTER++;
                                    }

                                }

                                Phrase ph3 = new Phrase(String.valueOf(count), new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL, BaseColor.BLACK));
                                PdfPCell cell3 = new PdfPCell(ph3);

                                tabb.addCell(cell3);
                            }

                            String meangrade = "";
                            Phrase ph4 = new Phrase(String.valueOf(COUNTER), new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL, BaseColor.BLACK));
                            PdfPCell cell4 = new PdfPCell(ph4);
                            cell4.setColspan(2);
                            tabb.addCell(cell4);

                            Phrase ph5 = new Phrase(String.valueOf(nf.format(totalpoints / COUNTER)), new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL, BaseColor.BLACK));
                            PdfPCell cell5 = new PdfPCell(ph5);
                            cell5.setColspan(2);
                            tabb.addCell(cell5);

                            String sqll = " Select grade from points_for_each_grade where points='" + nf2.format(totalpoints / COUNTER) + "'";
                            ps = con.prepareStatement(sqll);
                            rs = ps.executeQuery();
                            if (rs.next()) {
                                meangrade = rs.getString("grade");
                            }

                            Phrase ph6 = new Phrase(meangrade, new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL, BaseColor.BLACK));
                            PdfPCell cell6 = new PdfPCell(ph6);
                            cell6.setColspan(2);

                            tabb.addCell(cell6);
                            if (targets) {
                                Phrase ph7 = new Phrase(String.valueOf(Globals.totalTargetPoints(Globals.classCode(classname), academicyear)), new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL, BaseColor.BLACK));
                                PdfPCell cell7 = new PdfPCell(ph7);

                                cell7.setColspan(2);
                                tabb.addCell(cell7);
                                Phrase ph8 = new Phrase(String.valueOf(Globals.totalTargetPoints(Globals.classCode(classname), academicyear)), new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL, BaseColor.BLACK));
                                PdfPCell cell8 = new PdfPCell(ph8);

                                cell8.setColspan(2);
                                tabb.addCell(cell8);
                            }
                        }

                    }


                } else {

                    {///table headers
                        Phrase ph1 = new Phrase("STREAM", new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL, BaseColor.BLACK));
                        PdfPCell cell1 = new PdfPCell(ph1);
                        cell1.setColspan(4);
                        cell1.setVerticalAlignment(Element.ALIGN_BOTTOM);
                        cell1.setHorizontalAlignment(Element.ALIGN_MIDDLE);
                        tabb.addCell(cell1);

                        Phrase ph2 = new Phrase("Exam Code".toUpperCase(), new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL, BaseColor.BLACK));
                        PdfPCell cell2 = new PdfPCell(ph2);
                        cell2.setColspan(6);
                        cell2.setVerticalAlignment(Element.ALIGN_BOTTOM);
                        cell2.setHorizontalAlignment(Element.ALIGN_MIDDLE);
                        tabb.addCell(cell2);

                        String sql2 = "Select * from points_for_each_grade order by points desc";
                        ps = con.prepareStatement(sql2);
                        rs = ps.executeQuery();
                        while (rs.next()) {

                            Phrase ph3 = new Phrase(rs.getString("grade"), new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL, BaseColor.BLACK));
                            PdfPCell cell3 = new PdfPCell(ph3);
                            cell3.setVerticalAlignment(Element.ALIGN_BOTTOM);
                            cell3.setHorizontalAlignment(Element.ALIGN_MIDDLE);

                            tabb.addCell(cell3);
                        }

                        Phrase ph4 = new Phrase("Entries", new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL, BaseColor.BLACK));
                        PdfPCell cell4 = new PdfPCell(ph4);
                        cell4.setColspan(2);
                        cell4.setRotation(90);
                        tabb.addCell(cell4);

                        Phrase ph5 = new Phrase("Mean Score", new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL, BaseColor.BLACK));
                        PdfPCell cell5 = new PdfPCell(ph5);
                        cell5.setColspan(2);
                        cell5.setRotation(90);
                        tabb.addCell(cell5);

                        Phrase ph6 = new Phrase("Mean Grade", new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL, BaseColor.BLACK));
                        PdfPCell cell6 = new PdfPCell(ph6);
                        cell6.setRotation(90);
                        cell6.setColspan(2);

                        tabb.addCell(cell6);
                        if (targets) {
                            Phrase ph7 = new Phrase("Target Points", new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL, BaseColor.BLACK));
                            PdfPCell cell7 = new PdfPCell(ph7);
                            cell7.setRotation(90);
                            cell7.setColspan(2);
                            tabb.addCell(cell7);
                            Phrase ph8 = new Phrase("Target Grade", new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL, BaseColor.BLACK));
                            PdfPCell cell8 = new PdfPCell(ph8);
                            cell8.setRotation(90);
                            cell8.setColspan(2);
                            tabb.addCell(cell8);
                        }

                    }

                    {
                        //real distribution from db

                        Phrase ph1 = new Phrase(stream + "\n ", new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL, BaseColor.BLACK));
                        PdfPCell cell1 = new PdfPCell(ph1);
                        cell1.setColspan(4);
                        tabb.addCell(cell1);

                        Phrase ph2 = new Phrase(examcode, new Font(Font.FontFamily.HELVETICA, 8, Font.NORMAL, BaseColor.BLACK));
                        PdfPCell cell2 = new PdfPCell(ph2);
                        cell2.setRowspan(3);
                        cell2.setColspan(6);

                        tabb.addCell(cell2);
                        int COUNTER = 0, gradepoint = 0;
                        double totalpoints = 0.000;
                        String sql2 = "Select * from points_for_each_grade order by points desc";
                        ps = con.prepareStatement(sql2);
                        rs = ps.executeQuery();
                        while (rs.next()) {
                            gradepoint = rs.getInt("Points");
                            String grade = rs.getString("grade");
                            int count = 0;
                            String sql1 = "Select admnumber,meanpoints from examanalysistable where examcode='" + examcode + "' and classname='" + classname + "' and academicyear='" + academicyear + "' and meangrade='" + grade + "' group by admnumber";
                            ps = con.prepareStatement(sql1);
                            ResultSet rsx = ps.executeQuery();
                            while (rsx.next()) {
                                String adm = rsx.getString("AdmNumber");
                                if (Globals.gradable(adm, examcode, academicyear)) {
                                    totalpoints += rsx.getDouble("meanpoints");
                                    count++;
                                    COUNTER++;
                                }
                            }

                            Phrase ph3 = new Phrase(String.valueOf(count), new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL, BaseColor.BLACK));
                            PdfPCell cell3 = new PdfPCell(ph3);

                            tabb.addCell(cell3);
                        }

                        String meangrade = "";
                        Phrase ph4 = new Phrase(String.valueOf(COUNTER), new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL, BaseColor.BLACK));
                        PdfPCell cell4 = new PdfPCell(ph4);
                        cell4.setColspan(2);
                        tabb.addCell(cell4);

                        Phrase ph5 = new Phrase(String.valueOf(nf.format(totalpoints / COUNTER)), new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL, BaseColor.BLACK));
                        PdfPCell cell5 = new PdfPCell(ph5);
                        cell5.setColspan(2);
                        tabb.addCell(cell5);

                        String sqll = " Select grade from points_for_each_grade where points='" + nf2.format(totalpoints / COUNTER) + "'";
                        ps = con.prepareStatement(sqll);
                        rs = ps.executeQuery();
                        if (rs.next()) {
                            meangrade = rs.getString("grade");
                        }

                        Phrase ph6 = new Phrase(meangrade, new Font(Font.FontFamily.HELVETICA, 8, Font.NORMAL, BaseColor.BLACK));
                        PdfPCell cell6 = new PdfPCell(ph6);
                        cell6.setColspan(2);

                        tabb.addCell(cell6);
                        if (targets) {
                            Phrase ph7 = new Phrase(String.valueOf(Globals.totalTargetPoints(Globals.classCode(classname), academicyear)), new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL, BaseColor.BLACK));
                            PdfPCell cell7 = new PdfPCell(ph7);

                            cell7.setColspan(2);
                            tabb.addCell(cell7);
                            Phrase ph8 = new Phrase(String.valueOf(Globals.totalTargetPoints(Globals.classCode(classname), academicyear)), new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL, BaseColor.BLACK));
                            PdfPCell cell8 = new PdfPCell(ph8);

                            cell8.setColspan(2);
                            tabb.addCell(cell8);
                        }
                    }
                }

            } else {


                {///table headers
                    Phrase ph1 = new Phrase("STREAM", new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL, BaseColor.BLACK));
                    PdfPCell cell1 = new PdfPCell(ph1);
                    cell1.setColspan(4);
                    cell1.setVerticalAlignment(Element.ALIGN_BOTTOM);
                    cell1.setHorizontalAlignment(Element.ALIGN_MIDDLE);
                    tabb.addCell(cell1);

                    Phrase ph2 = new Phrase("Exam Code".toUpperCase(), new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL, BaseColor.BLACK));
                    PdfPCell cell2 = new PdfPCell(ph2);
                    cell2.setColspan(6);
                    cell2.setVerticalAlignment(Element.ALIGN_BOTTOM);
                    cell2.setHorizontalAlignment(Element.ALIGN_MIDDLE);
                    tabb.addCell(cell2);

                    String sql2 = "Select * from points_for_each_grade order by points desc";
                    ps = con.prepareStatement(sql2);
                    rs = ps.executeQuery();
                    while (rs.next()) {

                        Phrase ph3 = new Phrase(rs.getString("grade"), new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL, BaseColor.BLACK));
                        PdfPCell cell3 = new PdfPCell(ph3);
                        cell3.setVerticalAlignment(Element.ALIGN_BOTTOM);
                        cell3.setHorizontalAlignment(Element.ALIGN_MIDDLE);

                        tabb.addCell(cell3);
                    }

                    Phrase ph4 = new Phrase("Entries", new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL, BaseColor.BLACK));
                    PdfPCell cell4 = new PdfPCell(ph4);
                    cell4.setColspan(2);
                    cell4.setRotation(90);
                    tabb.addCell(cell4);

                    Phrase ph5 = new Phrase("Mean Score", new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL, BaseColor.BLACK));
                    PdfPCell cell5 = new PdfPCell(ph5);
                    cell5.setColspan(2);
                    cell5.setRotation(90);
                    tabb.addCell(cell5);

                    Phrase ph6 = new Phrase("Mean Grade", new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL, BaseColor.BLACK));
                    PdfPCell cell6 = new PdfPCell(ph6);
                    cell6.setRotation(90);
                    cell6.setColspan(2);

                    tabb.addCell(cell6);
                    if (targets) {
                        Phrase ph7 = new Phrase("Target Points", new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL, BaseColor.BLACK));
                        PdfPCell cell7 = new PdfPCell(ph7);

                        cell7.setColspan(2);
                        tabb.addCell(cell7);
                        Phrase ph8 = new Phrase("Target Grade", new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL, BaseColor.BLACK));
                        PdfPCell cell8 = new PdfPCell(ph8);

                        cell8.setColspan(2);
                        tab.addCell(cell8);
                    }

                }

                if (sort.equalsIgnoreCase("gender")) {


                    for (int i = 1; i <= 2; ++i) {

                        String genderValue = "", sex;
                        if (i == 1) {
                            genderValue = "Male";
                            sex = "Boys".toUpperCase();
                        } else {
                            genderValue = "Female";
                            sex = "Girls".toUpperCase();
                        }


                        {
                            //real distribution from db


                            Phrase ph1a = new Phrase(sex, new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL, BaseColor.BLACK));
                            PdfPCell cell1a = new PdfPCell(ph1a);
                            if (targets) {
                                cell1a.setColspan(32);
                            } else {
                                cell1a.setColspan(28);
                            }
                            tabb.addCell(cell1a);


                            Phrase ph1 = new Phrase(stream + "\n ", new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL, BaseColor.BLACK));
                            PdfPCell cell1 = new PdfPCell(ph1);
                            cell1.setColspan(4);
                            tabb.addCell(cell1);

                            Phrase ph2 = new Phrase(examcode, new Font(Font.FontFamily.HELVETICA, 8, Font.NORMAL, BaseColor.BLACK));
                            PdfPCell cell2 = new PdfPCell(ph2);
                            cell2.setRowspan(3);
                            cell2.setColspan(6);

                            tabb.addCell(cell2);
                            int COUNTER = 0, gradepoint = 0;
                            double totalpoints = 0.0;
                            String sql2 = "Select * from points_for_each_grade order by points desc";
                            ps = con.prepareStatement(sql2);
                            rs = ps.executeQuery();
                            while (rs.next()) {
                                String grade = rs.getString("grade");
                                gradepoint = rs.getInt("Points");
                                int count = 0;
                                String sql1 = "Select admnumber,meanpoints from examanalysistable,admission where examcode='" + examcode + "'  and admissionNumber=admnumber and gender='" + genderValue + "' and classname='" + classname + "' and academicyear='" + academicyear + "' and meangrade='" + grade + "' and stream='" + stream + "' group by admnumber";
                                ps = con.prepareStatement(sql1);
                                ResultSet rsx = ps.executeQuery();
                                while (rsx.next()) {
                                    String adm = rsx.getString("AdmNumber");
                                    if (Globals.gradable(adm, examcode, academicyear)) {
                                        totalpoints += rsx.getDouble("meanpoints");
                                        count++;
                                        COUNTER++;
                                    }
                                }

                                Phrase ph3 = new Phrase(String.valueOf(count), new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL, BaseColor.BLACK));
                                PdfPCell cell3 = new PdfPCell(ph3);

                                tabb.addCell(cell3);
                            }

                            String meangrade = "";
                            Phrase ph4 = new Phrase(String.valueOf(COUNTER), new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL, BaseColor.BLACK));
                            PdfPCell cell4 = new PdfPCell(ph4);
                            cell4.setColspan(2);
                            tabb.addCell(cell4);

                            Phrase ph5 = new Phrase(String.valueOf(nf.format(totalpoints / COUNTER)), new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL, BaseColor.BLACK));
                            PdfPCell cell5 = new PdfPCell(ph5);
                            cell5.setColspan(2);
                            tabb.addCell(cell5);
                            String sqll = " Select grade from points_for_each_grade where points='" + nf2.format(totalpoints / COUNTER) + "'";
                            ps = con.prepareStatement(sqll);
                            rs = ps.executeQuery();
                            if (rs.next()) {
                                meangrade = rs.getString("grade");
                            }


                            Phrase ph6 = new Phrase(meangrade, new Font(Font.FontFamily.HELVETICA, 8, Font.NORMAL, BaseColor.BLACK));
                            PdfPCell cell6 = new PdfPCell(ph6);
                            cell6.setColspan(2);

                            tabb.addCell(cell6);

                            if (targets) {
                                Phrase ph7 = new Phrase(String.valueOf(Globals.totalTargetPoints(Globals.classCode(classname), academicyear)), new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL, BaseColor.BLACK));
                                PdfPCell cell7 = new PdfPCell(ph7);

                                cell7.setColspan(2);
                                tabb.addCell(cell7);
                                Phrase ph8 = new Phrase(String.valueOf(Globals.totalTargetPoints(Globals.classCode(classname), academicyear)), new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL, BaseColor.BLACK));
                                PdfPCell cell8 = new PdfPCell(ph8);

                                cell8.setColspan(2);
                                tabb.addCell(cell8);
                            }
                        }


                    }


                } else {


                    {
                        //real distribution from db

                        Phrase ph1 = new Phrase(stream + "\n ", new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL, BaseColor.BLACK));
                        PdfPCell cell1 = new PdfPCell(ph1);
                        cell1.setColspan(4);
                        tabb.addCell(cell1);

                        Phrase ph2 = new Phrase(examcode, new Font(Font.FontFamily.HELVETICA, 8, Font.NORMAL, BaseColor.BLACK));
                        PdfPCell cell2 = new PdfPCell(ph2);
                        cell2.setRowspan(3);
                        cell2.setColspan(6);

                        tabb.addCell(cell2);
                        int COUNTER = 0, gradepoint = 0;
                        double totalpoints = 0.0;
                        String sql2 = "Select * from points_for_each_grade order by points desc";
                        ps = con.prepareStatement(sql2);
                        rs = ps.executeQuery();
                        while (rs.next()) {
                            String grade = rs.getString("grade");
                            gradepoint = rs.getInt("Points");
                            int count = 0;
                            String sql1 = "Select admnumber,meanpoints from examanalysistable where examcode='" + examcode + "' and classname='" + classname + "' and academicyear='" + academicyear + "' and meangrade='" + grade + "' and stream='" + stream + "' group by admnumber";
                            ps = con.prepareStatement(sql1);
                            ResultSet rsx = ps.executeQuery();
                            while (rsx.next()) {
                                String adm = rsx.getString("AdmNumber");
                                if (Globals.gradable(adm, examcode, academicyear)) {
                                    totalpoints += rsx.getDouble("meanpoints");
                                    count++;
                                    COUNTER++;
                                }
                            }

                            Phrase ph3 = new Phrase(String.valueOf(count), new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL, BaseColor.BLACK));
                            PdfPCell cell3 = new PdfPCell(ph3);

                            tabb.addCell(cell3);
                        }

                        String meangrade = "";
                        Phrase ph4 = new Phrase(String.valueOf(COUNTER), new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL, BaseColor.BLACK));
                        PdfPCell cell4 = new PdfPCell(ph4);
                        cell4.setColspan(2);
                        tabb.addCell(cell4);

                        Phrase ph5 = new Phrase(String.valueOf(nf.format(totalpoints / COUNTER)), new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL, BaseColor.BLACK));
                        PdfPCell cell5 = new PdfPCell(ph5);
                        cell5.setColspan(2);
                        tabb.addCell(cell5);
                        String sqll = " Select grade from points_for_each_grade where points='" + nf2.format(totalpoints / COUNTER) + "'";
                        ps = con.prepareStatement(sqll);
                        rs = ps.executeQuery();
                        if (rs.next()) {
                            meangrade = rs.getString("grade");
                        }


                        Phrase ph6 = new Phrase(meangrade, new Font(Font.FontFamily.HELVETICA, 8, Font.NORMAL, BaseColor.BLACK));
                        PdfPCell cell6 = new PdfPCell(ph6);
                        cell6.setColspan(2);

                        tabb.addCell(cell6);
                        if (targets) {
                            Phrase ph7 = new Phrase(String.valueOf(Globals.totalTargetPoints(Globals.classCode(classname), academicyear)), new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL, BaseColor.BLACK));
                            PdfPCell cell7 = new PdfPCell(ph7);

                            cell7.setColspan(2);
                            tabb.addCell(cell7);
                            Phrase ph8 = new Phrase(String.valueOf(Globals.totalTargetPoints(Globals.classCode(classname), academicyear)), new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL, BaseColor.BLACK));
                            PdfPCell cell8 = new PdfPCell(ph8);

                            cell8.setColspan(2);
                            tabb.addCell(cell8);
                        }
                    }

                }


            }

            doc.add(tabb);

            doc.close();

            if (ConfigurationIntialiser.docOpener()) {
                if (dialog == null) {

                    dialog = ReportFrame.getReportFrameInstance(new File("C:\\schoolData\\gradedistributionReport.pdf"));

                } else {
                    dialog = null;
                    dialog = ReportFrame.getReportFrameInstance(new File("C:\\schoolData\\gradedistributionReport.pdf"));
                    dialog.setVisible(true);

                }

            } else {

                Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler " + "C:\\schoolData\\gradedistributionReport.pdf");

            }

        } catch (Exception sq) {
            sq.printStackTrace();
            JOptionPane.showMessageDialog(null, sq.getMessage());
        }
    }

    public static void distributionPerSubject(String examcode, String examname, String term, String classname, String academicyear, String stream, String subjectcode, String gender, boolean targets) {
        nf2.setMaximumFractionDigits(0);
        nf2.setMinimumFractionDigits(0);
        String sex = "BOYS";
        try {
            nf.setMaximumFractionDigits(2);
            nf.setMinimumFractionDigits(2);

            con = DbConnection.connectDb();
            Document doc = new Document();
            com.itextpdf.text.Image image = com.itextpdf.text.Image.getInstance("C:/schooldata/logo.jpg");
            PdfWriter writer = PdfWriter.getInstance(doc, new FileOutputStream("C:\\schoolData\\gradedistributionReport.pdf"));
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
            Paragraph pr5 = new Paragraph("Grade Distribution Report", FontFactory.getFont(FontFactory.HELVETICA, 13, java.awt.Font.BOLD, BaseColor.BLACK));
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
            PdfPTable tabb = new PdfPTable(28);
            if (targets) {
                tabb = new PdfPTable(32);
            } else {
                tabb = new PdfPTable(28);
            }
            tabb.setSpacingBefore(10);
            tabb.setWidthPercentage(100);
            int totalwidth = 0;
            if (stream.equalsIgnoreCase("OverAll")) {
                {///table headers
                    Phrase ph1 = new Phrase("STREAM", new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL, BaseColor.BLACK));
                    PdfPCell cell1 = new PdfPCell(ph1);
                    cell1.setColspan(4);
                    cell1.setVerticalAlignment(Element.ALIGN_BOTTOM);
                    cell1.setHorizontalAlignment(Element.ALIGN_MIDDLE);
                    tabb.addCell(cell1);

                    Phrase ph2 = new Phrase("Subject".toUpperCase(), new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL, BaseColor.BLACK));
                    PdfPCell cell2 = new PdfPCell(ph2);
                    cell2.setColspan(6);
                    cell2.setVerticalAlignment(Element.ALIGN_BOTTOM);
                    cell2.setHorizontalAlignment(Element.ALIGN_MIDDLE);
                    tabb.addCell(cell2);

                    String sql2 = "Select * from points_for_each_grade order by points desc";
                    ps = con.prepareStatement(sql2);
                    rs = ps.executeQuery();
                    while (rs.next()) {

                        Phrase ph3 = new Phrase(rs.getString("grade"), new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL, BaseColor.BLACK));
                        PdfPCell cell3 = new PdfPCell(ph3);
                        cell3.setVerticalAlignment(Element.ALIGN_BOTTOM);
                        cell3.setHorizontalAlignment(Element.ALIGN_MIDDLE);

                        tabb.addCell(cell3);
                    }

                    Phrase ph4 = new Phrase("Entries", new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL, BaseColor.BLACK));
                    PdfPCell cell4 = new PdfPCell(ph4);
                    cell4.setColspan(2);
                    cell4.setRotation(90);
                    tabb.addCell(cell4);

                    Phrase ph5 = new Phrase("Mean Score", new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL, BaseColor.BLACK));
                    PdfPCell cell5 = new PdfPCell(ph5);
                    cell5.setColspan(2);
                    cell5.setRotation(90);
                    tabb.addCell(cell5);

                    Phrase ph6 = new Phrase("Mean Grade", new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL, BaseColor.BLACK));
                    PdfPCell cell6 = new PdfPCell(ph6);
                    cell6.setRotation(90);
                    cell6.setColspan(2);

                    tabb.addCell(cell6);

                    if (targets) {
                        Phrase ph7 = new Phrase("Target Points", new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL, BaseColor.BLACK));
                        PdfPCell cell7 = new PdfPCell(ph7);
                        cell7.setRotation(90);
                        cell7.setColspan(2);
                        tabb.addCell(cell7);
                        Phrase ph8 = new Phrase("Target Grade", new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL, BaseColor.BLACK));
                        PdfPCell cell8 = new PdfPCell(ph8);
                        cell8.setRotation(90);
                        cell8.setColspan(2);
                        tabb.addCell(cell8);
                    }

                }

                if (subjectcode.equalsIgnoreCase("all")) {


                    if (gender.equalsIgnoreCase("All")) {

                        String SQL = "Select * From subjects";
                        ps = con.prepareStatement(SQL);
                        ResultSet rx = ps.executeQuery();
                        while (rx.next()) {
                            String subcode = rx.getString("subjectcode");
                            String subname = rx.getString("SubjectName");
                            {
                                //real distribution from db

                                Phrase ph1 = new Phrase(stream, new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL, BaseColor.BLACK));
                                PdfPCell cell1 = new PdfPCell(ph1);
                                cell1.setColspan(4);
                                tabb.addCell(cell1);

                                Phrase ph2 = new Phrase(subname, new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL, BaseColor.BLACK));
                                PdfPCell cell2 = new PdfPCell(ph2);
                                cell2.setColspan(6);

                                tabb.addCell(cell2);
                                int COUNTER = 0, gradepoint = 0;
                                double totalpoints = 0.0;

                                String sql2 = "Select * from points_for_each_grade order by points desc";
                                ps = con.prepareStatement(sql2);
                                rs = ps.executeQuery();
                                while (rs.next()) {
                                    String grade = rs.getString("grade");
                                    gradepoint = rs.getInt("Points");
                                    int count = 0;
                                    String sql1 = "Select admnumber,Sujectexampoints from examanalysistable where examcode='" + examcode + "' and classname='" + classname + "' and academicyear='" + academicyear + "' and subjectexamgrade='" + grade + "' and subjectcode='" + subcode + "' group by admnumber";
                                    ps = con.prepareStatement(sql1);
                                    ResultSet rsx = ps.executeQuery();
                                    while (rsx.next()) {
                                        totalpoints += gradepoint;
                                        count++;
                                        COUNTER++;
                                    }

                                    Phrase ph3 = new Phrase(String.valueOf(count), new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL, BaseColor.BLACK));
                                    PdfPCell cell3 = new PdfPCell(ph3);

                                    tabb.addCell(cell3);
                                }

                                String meangrade = "";
                                Phrase ph4 = new Phrase(String.valueOf(COUNTER), new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL, BaseColor.BLACK));
                                PdfPCell cell4 = new PdfPCell(ph4);
                                cell4.setColspan(2);
                                tabb.addCell(cell4);

                                Phrase ph5 = new Phrase(String.valueOf(nf.format(totalpoints / COUNTER)), new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL, BaseColor.BLACK));
                                PdfPCell cell5 = new PdfPCell(ph5);
                                cell5.setColspan(2);
                                tabb.addCell(cell5);
                                String sqll = " Select grade from points_for_each_grade where points='" + nf2.format(totalpoints / COUNTER) + "'";
                                ps = con.prepareStatement(sqll);
                                rs = ps.executeQuery();
                                if (rs.next()) {
                                    meangrade = rs.getString("grade");
                                }

                                Phrase ph6 = new Phrase(meangrade, new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL, BaseColor.BLACK));
                                PdfPCell cell6 = new PdfPCell(ph6);
                                cell6.setColspan(2);

                                tabb.addCell(cell6);

                                if (targets) {
                                    Phrase ph7 = new Phrase(String.valueOf(Globals.targetPoints(examcode, subcode)), new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL, BaseColor.BLACK));
                                    PdfPCell cell7 = new PdfPCell(ph7);

                                    cell7.setColspan(2);
                                    tabb.addCell(cell7);
                                    Phrase ph8 = new Phrase(Globals.targetGrade(examcode, subcode), new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL, BaseColor.BLACK));
                                    PdfPCell cell8 = new PdfPCell(ph8);

                                    cell8.setColspan(2);
                                    tabb.addCell(cell8);
                                }
                            }

                        }

                    } else {

                        for (int i = 0; i < 2; i++) {
                            if (i == 0) {
                                gender = "Male";
                                sex = "BOYS";
                            } else {
                                gender = "Female";
                                sex = "GIRLS";
                            }

                            Phrase ph1a = new Phrase(sex, new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL, BaseColor.BLACK));
                            PdfPCell cell1a = new PdfPCell(ph1a);
                            if (targets) {
                                cell1a.setColspan(32);
                            } else {
                                cell1a.setColspan(28);
                            }
                            tabb.addCell(cell1a);

                            String SQL = "Select * From subjects";
                            ps = con.prepareStatement(SQL);
                            ResultSet rx = ps.executeQuery();
                            while (rx.next()) {
                                String subcode = rx.getString("subjectcode");
                                String subname = rx.getString("SubjectName");
                                {
                                    //real distribution from db

                                    Phrase ph1 = new Phrase(stream, new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL, BaseColor.BLACK));
                                    PdfPCell cell1 = new PdfPCell(ph1);
                                    cell1.setColspan(4);
                                    tabb.addCell(cell1);

                                    Phrase ph2 = new Phrase(subname, new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL, BaseColor.BLACK));
                                    PdfPCell cell2 = new PdfPCell(ph2);
                                    cell2.setColspan(6);

                                    tabb.addCell(cell2);
                                    int COUNTER = 0, gradepoint = 0;
                                    double totalpoints = 0.0;

                                    String sql2 = "Select * from points_for_each_grade order by points desc";
                                    ps = con.prepareStatement(sql2);
                                    rs = ps.executeQuery();
                                    while (rs.next()) {
                                        String grade = rs.getString("grade");
                                        gradepoint = rs.getInt("Points");
                                        int count = 0;
                                        String sql1 = "Select admnumber,Sujectexampoints from examanalysistable,admission where admissionnumber=admnumber and gender='" + gender + "' and examcode='" + examcode + "' and classname='" + classname + "' and academicyear='" + academicyear + "' and subjectexamgrade='" + grade + "' and subjectcode='" + subcode + "' group by admnumber";
                                        ps = con.prepareStatement(sql1);
                                        ResultSet rsx = ps.executeQuery();
                                        while (rsx.next()) {
                                            totalpoints += gradepoint;
                                            count++;
                                            COUNTER++;
                                        }

                                        Phrase ph3 = new Phrase(String.valueOf(count), new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL, BaseColor.BLACK));
                                        PdfPCell cell3 = new PdfPCell(ph3);

                                        tabb.addCell(cell3);
                                    }

                                    String meangrade = "";
                                    Phrase ph4 = new Phrase(String.valueOf(COUNTER), new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL, BaseColor.BLACK));
                                    PdfPCell cell4 = new PdfPCell(ph4);
                                    cell4.setColspan(2);
                                    tabb.addCell(cell4);

                                    Phrase ph5 = new Phrase(String.valueOf(nf.format(totalpoints / COUNTER)), new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL, BaseColor.BLACK));
                                    PdfPCell cell5 = new PdfPCell(ph5);
                                    cell5.setColspan(2);
                                    tabb.addCell(cell5);
                                    String sqll = " Select grade from points_for_each_grade where points='" + nf2.format(totalpoints / COUNTER) + "'";
                                    ps = con.prepareStatement(sqll);
                                    rs = ps.executeQuery();
                                    if (rs.next()) {
                                        meangrade = rs.getString("grade");
                                    }

                                    Phrase ph6 = new Phrase(meangrade, new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL, BaseColor.BLACK));
                                    PdfPCell cell6 = new PdfPCell(ph6);
                                    cell6.setColspan(2);

                                    tabb.addCell(cell6);

                                    if (targets) {
                                        Phrase ph7 = new Phrase(String.valueOf(Globals.targetPoints(examcode, subcode)), new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL, BaseColor.BLACK));
                                        PdfPCell cell7 = new PdfPCell(ph7);

                                        cell7.setColspan(2);
                                        tabb.addCell(cell7);
                                        Phrase ph8 = new Phrase(Globals.targetGrade(examcode, subcode), new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL, BaseColor.BLACK));
                                        PdfPCell cell8 = new PdfPCell(ph8);

                                        cell8.setColspan(2);
                                        tabb.addCell(cell8);
                                    }
                                }

                            }


                        }


                    }

                } else {

                    {
                        //real distribution from db
                        if (gender.equalsIgnoreCase("All")) {

                            Phrase ph1 = new Phrase(stream, new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL, BaseColor.BLACK));
                            PdfPCell cell1 = new PdfPCell(ph1);
                            cell1.setColspan(4);
                            tabb.addCell(cell1);

                            Phrase ph2 = new Phrase(Globals.subjectName(subjectcode), new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL, BaseColor.BLACK));
                            PdfPCell cell2 = new PdfPCell(ph2);
                            cell2.setColspan(6);

                            tabb.addCell(cell2);
                            int COUNTER = 0, gradepoint = 0;
                            double totalpoints = 0.0;

                            String sql2 = "Select * from points_for_each_grade order by points desc";
                            ps = con.prepareStatement(sql2);
                            rs = ps.executeQuery();
                            while (rs.next()) {
                                String grade = rs.getString("grade");
                                gradepoint = rs.getInt("Points");
                                int count = 0;
                                String sql1 = "Select admnumber,Sujectexampoints from examanalysistable where examcode='" + examcode + "' and classname='" + classname + "' and academicyear='" + academicyear + "' and subjectexamgrade='" + grade + "' and subjectcode='" + subjectcode + "' group by admnumber";
                                ps = con.prepareStatement(sql1);
                                ResultSet rsx = ps.executeQuery();
                                while (rsx.next()) {
                                    totalpoints += gradepoint;
                                    count++;
                                    COUNTER++;
                                }

                                Phrase ph3 = new Phrase(String.valueOf(count), new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL, BaseColor.BLACK));
                                PdfPCell cell3 = new PdfPCell(ph3);

                                tabb.addCell(cell3);
                            }

                            String meangrade = "";
                            Phrase ph4 = new Phrase(String.valueOf(COUNTER), new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL, BaseColor.BLACK));
                            PdfPCell cell4 = new PdfPCell(ph4);
                            cell4.setColspan(2);
                            tabb.addCell(cell4);

                            Phrase ph5 = new Phrase(String.valueOf(nf.format(totalpoints / COUNTER)), new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL, BaseColor.BLACK));
                            PdfPCell cell5 = new PdfPCell(ph5);
                            cell5.setColspan(2);
                            tabb.addCell(cell5);
                            String sqll = " Select grade from points_for_each_grade where points='" + nf.format(totalpoints / COUNTER) + "'";
                            ps = con.prepareStatement(sqll);
                            rs = ps.executeQuery();
                            if (rs.next()) {
                                meangrade = rs.getString("grade");
                            }

                            Phrase ph6 = new Phrase(meangrade, new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL, BaseColor.BLACK));
                            PdfPCell cell6 = new PdfPCell(ph6);
                            cell6.setColspan(2);

                            tabb.addCell(cell6);
                            if (targets) {
                                Phrase ph7 = new Phrase(String.valueOf(Globals.targetPoints(examcode, subjectcode)), new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL, BaseColor.BLACK));
                                PdfPCell cell7 = new PdfPCell(ph7);

                                cell7.setColspan(2);
                                tabb.addCell(cell7);
                                Phrase ph8 = new Phrase(Globals.targetGrade(examcode, subjectcode), new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL, BaseColor.BLACK));
                                PdfPCell cell8 = new PdfPCell(ph8);

                                cell8.setColspan(2);
                                tabb.addCell(cell8);
                            }

                        } else {

                            for (int i = 0; i < 2; ++i) {
                                if (i == 0) {
                                    gender = "Male";
                                    sex = "BOYS";
                                } else {
                                    gender = "Female";
                                    sex = "GIRLS";
                                }
                                Phrase ph1a = new Phrase(sex, new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL, BaseColor.BLACK));
                                PdfPCell cell1a = new PdfPCell(ph1a);
                                if (targets) {
                                    cell1a.setColspan(32);
                                } else {
                                    cell1a.setColspan(28);
                                }
                                tabb.addCell(cell1a);

                                Phrase ph1 = new Phrase(stream, new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL, BaseColor.BLACK));
                                PdfPCell cell1 = new PdfPCell(ph1);
                                cell1.setColspan(4);
                                tabb.addCell(cell1);

                                Phrase ph2 = new Phrase(Globals.subjectName(subjectcode), new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL, BaseColor.BLACK));
                                PdfPCell cell2 = new PdfPCell(ph2);
                                cell2.setColspan(6);

                                tabb.addCell(cell2);
                                int COUNTER = 0, gradepoint = 0;
                                double totalpoints = 0.0;

                                String sql2 = "Select * from points_for_each_grade order by points desc";
                                ps = con.prepareStatement(sql2);
                                rs = ps.executeQuery();
                                while (rs.next()) {
                                    String grade = rs.getString("grade");
                                    gradepoint = rs.getInt("Points");
                                    int count = 0;
                                    String sql1 = "Select admnumber,Sujectexampoints from examanalysistable,admission where admissionNumber=admnumber and gender='" + gender + "' and examcode='" + examcode + "' and classname='" + classname + "' and academicyear='" + academicyear + "' and subjectexamgrade='" + grade + "' and subjectcode='" + subjectcode + "' group by admnumber";
                                    ps = con.prepareStatement(sql1);
                                    ResultSet rsx = ps.executeQuery();
                                    while (rsx.next()) {
                                        totalpoints += gradepoint;
                                        count++;
                                        COUNTER++;
                                    }

                                    Phrase ph3 = new Phrase(String.valueOf(count), new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL, BaseColor.BLACK));
                                    PdfPCell cell3 = new PdfPCell(ph3);

                                    tabb.addCell(cell3);
                                }

                                String meangrade = "";
                                Phrase ph4 = new Phrase(String.valueOf(COUNTER), new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL, BaseColor.BLACK));
                                PdfPCell cell4 = new PdfPCell(ph4);
                                cell4.setColspan(2);
                                tabb.addCell(cell4);

                                Phrase ph5 = new Phrase(String.valueOf(nf.format(totalpoints / COUNTER)), new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL, BaseColor.BLACK));
                                PdfPCell cell5 = new PdfPCell(ph5);
                                cell5.setColspan(2);
                                tabb.addCell(cell5);
                                String sqll = " Select grade from points_for_each_grade where points='" + nf2.format(totalpoints / COUNTER) + "'";
                                ps = con.prepareStatement(sqll);
                                rs = ps.executeQuery();
                                if (rs.next()) {
                                    meangrade = rs.getString("grade");
                                }

                                Phrase ph6 = new Phrase(meangrade, new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL, BaseColor.BLACK));
                                PdfPCell cell6 = new PdfPCell(ph6);
                                cell6.setColspan(2);

                                tabb.addCell(cell6);

                                if (targets) {
                                    Phrase ph7 = new Phrase(String.valueOf(Globals.targetPoints(examcode, subjectcode)), new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL, BaseColor.BLACK));
                                    PdfPCell cell7 = new PdfPCell(ph7);

                                    cell7.setColspan(2);
                                    tabb.addCell(cell7);
                                    Phrase ph8 = new Phrase(Globals.targetGrade(examcode, subjectcode), new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL, BaseColor.BLACK));
                                    PdfPCell cell8 = new PdfPCell(ph8);

                                    cell8.setColspan(2);
                                    tabb.addCell(cell8);
                                }


                            }


                        }


                    }

                }

            } else {


                {///table headers
                    Phrase ph1 = new Phrase("STREAM", new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL, BaseColor.BLACK));
                    PdfPCell cell1 = new PdfPCell(ph1);
                    cell1.setColspan(4);
                    cell1.setVerticalAlignment(Element.ALIGN_BOTTOM);
                    cell1.setHorizontalAlignment(Element.ALIGN_MIDDLE);
                    tabb.addCell(cell1);

                    Phrase ph2 = new Phrase("Subject".toUpperCase(), new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL, BaseColor.BLACK));
                    PdfPCell cell2 = new PdfPCell(ph2);
                    cell2.setColspan(6);
                    cell2.setVerticalAlignment(Element.ALIGN_BOTTOM);
                    cell2.setHorizontalAlignment(Element.ALIGN_MIDDLE);
                    tabb.addCell(cell2);

                    String sql2 = "Select * from points_for_each_grade order by points desc";
                    ps = con.prepareStatement(sql2);
                    rs = ps.executeQuery();
                    while (rs.next()) {

                        Phrase ph3 = new Phrase(rs.getString("grade"), new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL, BaseColor.BLACK));
                        PdfPCell cell3 = new PdfPCell(ph3);
                        cell3.setVerticalAlignment(Element.ALIGN_BOTTOM);
                        cell3.setHorizontalAlignment(Element.ALIGN_MIDDLE);

                        tabb.addCell(cell3);
                    }

                    Phrase ph4 = new Phrase("Entries", new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL, BaseColor.BLACK));
                    PdfPCell cell4 = new PdfPCell(ph4);
                    cell4.setColspan(2);
                    cell4.setRotation(90);
                    tabb.addCell(cell4);

                    Phrase ph5 = new Phrase("Mean Score", new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL, BaseColor.BLACK));
                    PdfPCell cell5 = new PdfPCell(ph5);
                    cell5.setColspan(2);
                    cell5.setRotation(90);
                    tabb.addCell(cell5);

                    Phrase ph6 = new Phrase("Mean Grade", new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL, BaseColor.BLACK));
                    PdfPCell cell6 = new PdfPCell(ph6);
                    cell6.setRotation(90);
                    cell6.setColspan(2);

                    tabb.addCell(cell6);
                    if (targets) {
                        Phrase ph7 = new Phrase("Target Points", new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL, BaseColor.BLACK));
                        PdfPCell cell7 = new PdfPCell(ph7);
                        cell7.setRotation(90);
                        cell7.setColspan(2);
                        tabb.addCell(cell7);
                        Phrase ph8 = new Phrase("Target Grade", new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL, BaseColor.BLACK));
                        PdfPCell cell8 = new PdfPCell(ph8);
                        cell8.setRotation(90);
                        cell8.setColspan(2);
                        tabb.addCell(cell8);
                    }

                }

                if (subjectcode.equalsIgnoreCase("All")) {

                    if (gender.equalsIgnoreCase("All")) {
                        String SQL = "Select * From subjects";
                        ps = con.prepareStatement(SQL);
                        ResultSet rx = ps.executeQuery();
                        while (rx.next()) {
                            String subcode = rx.getString("subjectcode");
                            String subname = rx.getString("SubjectName");

                            {
                                //real distribution from db

                                Phrase ph1 = new Phrase(stream, new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL, BaseColor.BLACK));
                                PdfPCell cell1 = new PdfPCell(ph1);
                                cell1.setColspan(4);
                                tabb.addCell(cell1);

                                Phrase ph2 = new Phrase(subname, new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL, BaseColor.BLACK));
                                PdfPCell cell2 = new PdfPCell(ph2);
                                cell2.setColspan(6);

                                tabb.addCell(cell2);
                                int COUNTER = 0, gradepoint = 0;
                                double totalpoints = 0.0;
                                String sql2 = "Select * from points_for_each_grade order by points desc";
                                ps = con.prepareStatement(sql2);
                                rs = ps.executeQuery();
                                while (rs.next()) {
                                    String grade = rs.getString("grade");
                                    gradepoint = rs.getInt("Points");
                                    int count = 0;
                                    String sql1 = "Select admnumber,Sujectexampoints from examanalysistable where examcode='" + examcode + "' and classname='" + classname + "' and academicyear='" + academicyear + "' and subjectexamgrade='" + grade + "' and stream='" + stream + "' and subjectcode='" + subcode + "' group by admnumber";
                                    ps = con.prepareStatement(sql1);
                                    ResultSet rsx = ps.executeQuery();
                                    while (rsx.next()) {
                                        totalpoints += gradepoint;
                                        count++;
                                        COUNTER++;
                                    }

                                    Phrase ph3 = new Phrase(String.valueOf(count), new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL, BaseColor.BLACK));
                                    PdfPCell cell3 = new PdfPCell(ph3);

                                    tabb.addCell(cell3);
                                }

                                String meangrade = "";
                                Phrase ph4 = new Phrase(String.valueOf(COUNTER), new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL, BaseColor.BLACK));
                                PdfPCell cell4 = new PdfPCell(ph4);
                                cell4.setColspan(2);
                                tabb.addCell(cell4);

                                Phrase ph5 = new Phrase(String.valueOf(nf.format(totalpoints / COUNTER)), new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL, BaseColor.BLACK));
                                PdfPCell cell5 = new PdfPCell(ph5);
                                cell5.setColspan(2);
                                tabb.addCell(cell5);
                                String sqll = " Select grade from points_for_each_grade where points='" + nf2.format(totalpoints / COUNTER) + "'";
                                ps = con.prepareStatement(sqll);
                                rs = ps.executeQuery();
                                if (rs.next()) {
                                    meangrade = rs.getString("grade");
                                }

                                Phrase ph6 = new Phrase(meangrade, new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL, BaseColor.BLACK));
                                PdfPCell cell6 = new PdfPCell(ph6);
                                cell6.setColspan(2);

                                tabb.addCell(cell6);
                                if (targets) {
                                    Phrase ph7 = new Phrase(String.valueOf(Globals.targetPoints(examcode, subcode)), new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL, BaseColor.BLACK));
                                    PdfPCell cell7 = new PdfPCell(ph7);

                                    cell7.setColspan(2);
                                    tabb.addCell(cell7);
                                    Phrase ph8 = new Phrase(Globals.targetGrade(examcode, subcode), new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL, BaseColor.BLACK));
                                    PdfPCell cell8 = new PdfPCell(ph8);

                                    cell8.setColspan(2);
                                    tabb.addCell(cell8);
                                }
                            }

                        }

                    } else {

                        for (int i = 0; i < 2; ++i) {
                            if (i == 0) {
                                gender = "Male";
                                sex = "BOYS";
                            } else {

                                gender = "Female";
                                sex = "GIRLS";
                            }


                            String SQL = "Select * From subjects";
                            ps = con.prepareStatement(SQL);
                            ResultSet rx = ps.executeQuery();
                            while (rx.next()) {
                                String subcode = rx.getString("subjectcode");
                                String subname = rx.getString("SubjectName");

                                {
                                    //real distribution from db

                                    Phrase ph1a = new Phrase(sex, new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL, BaseColor.BLACK));
                                    PdfPCell cell1a = new PdfPCell(ph1a);
                                    if (targets) {
                                        cell1a.setColspan(32);
                                    } else {
                                        cell1a.setColspan(28);
                                    }
                                    tabb.addCell(cell1a);

                                    Phrase ph1 = new Phrase(stream, new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL, BaseColor.BLACK));
                                    PdfPCell cell1 = new PdfPCell(ph1);
                                    cell1.setColspan(4);
                                    tabb.addCell(cell1);

                                    Phrase ph2 = new Phrase(subname, new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL, BaseColor.BLACK));
                                    PdfPCell cell2 = new PdfPCell(ph2);
                                    cell2.setColspan(6);

                                    tabb.addCell(cell2);
                                    int COUNTER = 0, gradepoint = 0;
                                    double totalpoints = 0.0;
                                    String sql2 = "Select * from points_for_each_grade order by points desc";
                                    ps = con.prepareStatement(sql2);
                                    rs = ps.executeQuery();
                                    while (rs.next()) {
                                        String grade = rs.getString("grade");
                                        gradepoint = rs.getInt("Points");
                                        int count = 0;
                                        String sql1 = "Select admnumber,Sujectexampoints from examanalysistable,admission where admissionnumber=admNumber and gender='" + gender + "' and examcode='" + examcode + "' and classname='" + classname + "' and academicyear='" + academicyear + "' and subjectexamgrade='" + grade + "' and stream='" + stream + "' and subjectcode='" + subcode + "' group by admnumber";
                                        ps = con.prepareStatement(sql1);
                                        ResultSet rsx = ps.executeQuery();
                                        while (rsx.next()) {
                                            totalpoints += gradepoint;
                                            count++;
                                            COUNTER++;
                                        }

                                        Phrase ph3 = new Phrase(String.valueOf(count), new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL, BaseColor.BLACK));
                                        PdfPCell cell3 = new PdfPCell(ph3);

                                        tabb.addCell(cell3);
                                    }

                                    String meangrade = "";
                                    Phrase ph4 = new Phrase(String.valueOf(COUNTER), new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL, BaseColor.BLACK));
                                    PdfPCell cell4 = new PdfPCell(ph4);
                                    cell4.setColspan(2);
                                    tabb.addCell(cell4);

                                    Phrase ph5 = new Phrase(String.valueOf(nf.format(totalpoints / COUNTER)), new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL, BaseColor.BLACK));
                                    PdfPCell cell5 = new PdfPCell(ph5);
                                    cell5.setColspan(2);
                                    tabb.addCell(cell5);
                                    String sqll = " Select grade from points_for_each_grade where points='" + nf2.format(totalpoints / COUNTER) + "'";
                                    ps = con.prepareStatement(sqll);
                                    rs = ps.executeQuery();
                                    if (rs.next()) {
                                        meangrade = rs.getString("grade");
                                    }

                                    Phrase ph6 = new Phrase(meangrade, new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL, BaseColor.BLACK));
                                    PdfPCell cell6 = new PdfPCell(ph6);
                                    cell6.setColspan(2);

                                    tabb.addCell(cell6);
                                    if (targets) {
                                        Phrase ph7 = new Phrase(String.valueOf(Globals.targetPoints(examcode, subjectcode)), new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL, BaseColor.BLACK));
                                        PdfPCell cell7 = new PdfPCell(ph7);

                                        cell7.setColspan(2);
                                        tabb.addCell(cell7);
                                        Phrase ph8 = new Phrase(Globals.targetGrade(examcode, subjectcode), new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL, BaseColor.BLACK));
                                        PdfPCell cell8 = new PdfPCell(ph8);

                                        cell8.setColspan(2);
                                        tabb.addCell(cell8);
                                    }
                                }

                            }


                        }

                    }


                } else {


                    if (gender.equalsIgnoreCase("All")) {


                        {
                            //real distribution from db

                            Phrase ph1 = new Phrase(stream, new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL, BaseColor.BLACK));
                            PdfPCell cell1 = new PdfPCell(ph1);
                            cell1.setColspan(4);
                            tabb.addCell(cell1);

                            Phrase ph2 = new Phrase(Globals.subjectName(subjectcode), new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL, BaseColor.BLACK));
                            PdfPCell cell2 = new PdfPCell(ph2);
                            cell2.setColspan(6);

                            tabb.addCell(cell2);
                            int COUNTER = 0, gradepoint = 0;
                            double totalpoints = 0.0;
                            String sql2 = "Select * from points_for_each_grade order by points desc";
                            ps = con.prepareStatement(sql2);
                            rs = ps.executeQuery();
                            while (rs.next()) {
                                String grade = rs.getString("grade");
                                gradepoint = rs.getInt("Points");
                                int count = 0;
                                String sql1 = "Select admnumber,Sujectexampoints from examanalysistable where examcode='" + examcode + "' and classname='" + classname + "' and academicyear='" + academicyear + "' and subjectexamgrade='" + grade + "' and stream='" + stream + "' and subjectcode='" + subjectcode + "' group by admnumber";
                                ps = con.prepareStatement(sql1);
                                ResultSet rsx = ps.executeQuery();
                                while (rsx.next()) {
                                    totalpoints += gradepoint;
                                    count++;
                                    COUNTER++;
                                }

                                Phrase ph3 = new Phrase(String.valueOf(count), new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL, BaseColor.BLACK));
                                PdfPCell cell3 = new PdfPCell(ph3);

                                tabb.addCell(cell3);
                            }

                            String meangrade = "";
                            Phrase ph4 = new Phrase(String.valueOf(COUNTER), new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL, BaseColor.BLACK));
                            PdfPCell cell4 = new PdfPCell(ph4);
                            cell4.setColspan(2);
                            tabb.addCell(cell4);

                            Phrase ph5 = new Phrase(String.valueOf(nf.format(totalpoints / COUNTER)), new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL, BaseColor.BLACK));
                            PdfPCell cell5 = new PdfPCell(ph5);
                            cell5.setColspan(2);
                            tabb.addCell(cell5);
                            String sqll = " Select grade from points_for_each_grade where points='" + nf2.format(totalpoints / COUNTER) + "'";
                            ps = con.prepareStatement(sqll);
                            rs = ps.executeQuery();
                            if (rs.next()) {
                                meangrade = rs.getString("grade");
                            }

                            Phrase ph6 = new Phrase(meangrade, new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL, BaseColor.BLACK));
                            PdfPCell cell6 = new PdfPCell(ph6);
                            cell6.setColspan(2);

                            tabb.addCell(cell6);
                            if (targets) {
                                Phrase ph7 = new Phrase(String.valueOf(Globals.targetPoints(examcode, subjectcode)), new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL, BaseColor.BLACK));
                                PdfPCell cell7 = new PdfPCell(ph7);

                                cell7.setColspan(2);
                                tabb.addCell(cell7);
                                Phrase ph8 = new Phrase(Globals.targetGrade(examcode, subjectcode), new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL, BaseColor.BLACK));
                                PdfPCell cell8 = new PdfPCell(ph8);

                                cell8.setColspan(2);
                                tab.addCell(cell8);
                            }
                        }


                    } else {

                        for (int i = 0; i < 2; ++i) {
                            if (i == 0) {
                                gender = "Male";
                                sex = "BOYS";
                            } else {
                                gender = "Female";
                                sex = "GIRLS";
                            }

                            {
                                //real distribution from db
                                Phrase ph1a = new Phrase(sex, new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL, BaseColor.BLACK));
                                PdfPCell cell1a = new PdfPCell(ph1a);
                                if (targets) {
                                    cell1a.setColspan(32);
                                } else {
                                    cell1a.setColspan(28);
                                }
                                tabb.addCell(cell1a);

                                Phrase ph1 = new Phrase(stream, new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL, BaseColor.BLACK));
                                PdfPCell cell1 = new PdfPCell(ph1);
                                cell1.setColspan(4);
                                tabb.addCell(cell1);

                                Phrase ph2 = new Phrase(Globals.subjectName(subjectcode), new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL, BaseColor.BLACK));
                                PdfPCell cell2 = new PdfPCell(ph2);
                                cell2.setColspan(6);

                                tabb.addCell(cell2);
                                int COUNTER = 0, gradepoint = 0;
                                double totalpoints = 0.0;
                                String sql2 = "Select * from points_for_each_grade order by points desc";
                                ps = con.prepareStatement(sql2);
                                rs = ps.executeQuery();
                                while (rs.next()) {
                                    String grade = rs.getString("grade");
                                    gradepoint = rs.getInt("Points");
                                    int count = 0;
                                    String sql1 = "Select admnumber,Sujectexampoints from examanalysistable,admission where admissionNumber=admNumber and gender='" + gender + "' and examcode='" + examcode + "' and classname='" + classname + "' and academicyear='" + academicyear + "' and subjectexamgrade='" + grade + "' and stream='" + stream + "' and subjectcode='" + subjectcode + "' group by admnumber";
                                    ps = con.prepareStatement(sql1);
                                    ResultSet rsx = ps.executeQuery();
                                    while (rsx.next()) {
                                        totalpoints += gradepoint;
                                        count++;
                                        COUNTER++;
                                    }

                                    Phrase ph3 = new Phrase(String.valueOf(count), new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL, BaseColor.BLACK));
                                    PdfPCell cell3 = new PdfPCell(ph3);

                                    tabb.addCell(cell3);
                                }

                                String meangrade = "";
                                Phrase ph4 = new Phrase(String.valueOf(COUNTER), new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL, BaseColor.BLACK));
                                PdfPCell cell4 = new PdfPCell(ph4);
                                cell4.setColspan(2);
                                tabb.addCell(cell4);

                                Phrase ph5 = new Phrase(String.valueOf(nf.format(totalpoints / COUNTER)), new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL, BaseColor.BLACK));
                                PdfPCell cell5 = new PdfPCell(ph5);
                                cell5.setColspan(2);
                                tabb.addCell(cell5);
                                String sqll = " Select grade from points_for_each_grade where points='" + nf2.format(totalpoints / COUNTER) + "'";
                                ps = con.prepareStatement(sqll);
                                rs = ps.executeQuery();
                                if (rs.next()) {
                                    meangrade = rs.getString("grade");
                                }

                                Phrase ph6 = new Phrase(meangrade, new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL, BaseColor.BLACK));
                                PdfPCell cell6 = new PdfPCell(ph6);
                                cell6.setColspan(2);

                                tabb.addCell(cell6);
                                if (targets) {
                                    Phrase ph7 = new Phrase(String.valueOf(Globals.targetPoints(examcode, subjectcode)), new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL, BaseColor.BLACK));
                                    PdfPCell cell7 = new PdfPCell(ph7);

                                    cell7.setColspan(2);
                                    tabb.addCell(cell7);
                                    Phrase ph8 = new Phrase(Globals.targetGrade(examcode, subjectcode), new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL, BaseColor.BLACK));
                                    PdfPCell cell8 = new PdfPCell(ph8);

                                    cell8.setColspan(2);
                                    tabb.addCell(cell8);
                                }
                            }

                        }

                    }


                }

            }

            doc.add(tabb);

            doc.close();

            if (ConfigurationIntialiser.docOpener()) {
                if (dialog == null) {

                    dialog = ReportFrame.getReportFrameInstance(new File("C:\\schoolData\\gradedistributionReport.pdf"));

                } else {
                    dialog = null;
                    dialog = ReportFrame.getReportFrameInstance(new File("C:\\schoolData\\gradedistributionReport.pdf"));
                    dialog.setVisible(true);

                }

            } else {

                Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler " + "C:\\schoolData\\gradedistributionReport.pdf");

            }

        } catch (Exception sq) {
            sq.printStackTrace();
            JOptionPane.showMessageDialog(null, sq.getMessage());
        }
    }

}
