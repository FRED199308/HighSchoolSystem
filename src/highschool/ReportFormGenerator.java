/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package highschool;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Date;
import javax.imageio.ImageIO;
import javax.swing.JDialog;
import javax.swing.JOptionPane;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartRenderingInfo;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.entity.StandardEntityCollection;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;

/**
 * @author FRED
 */
public class ReportFormGenerator {

    static PreparedStatement ps;
    static ResultSet rs;
    static Connection con;
    static JDialog dialog = null;
    static DbConnection db = new DbConnection();

    public static void onEndPage(PdfWriter writer, Document document) {
        Font ffont = new Font(Font.FontFamily.TIMES_ROMAN, 9, Font.ITALIC);
        PdfContentByte cb = writer.getDirectContent();

        Phrase footer = new Phrase(" Software developed by LUNAR TECH SOLUTION - 0707353225 ", ffont);

        ColumnText.showTextAligned(cb, Element.ALIGN_LEFT,
                footer,
                (document.right() - document.left()) / 2 + document.leftMargin(),
                document.bottom() - 10, 0);
    }

    public static void reportForms(String examName, String examCode, String academicyear, String classname, String term, String admissionnumber) {
        String ex = "";
        String classcode = Globals.classCode(classname);
        String combinemodes = "", ENDTERMEXAM = "";
        con = DbConnection.connectDb();
        try {


            String sqll = "Select combinemode from examcombinationmodes where examcode='" + examCode + "'";
            ps = con.prepareStatement(sqll);
            ResultSet rr = ps.executeQuery();
            if (rr.next()) {
                combinemodes = rr.getString("combinemode");
            }
            if (combinemodes.equalsIgnoreCase("REVERSAL")) {
                ENDTERMEXAM = "(Previous Term)";
            } else {

            }
        } catch (Exception gx) {
            gx.printStackTrace();
        }
        if (examName.equalsIgnoreCase("TOTAL")) {
            ex = "(AVERAGE)";
        }
        if (admissionnumber == null) {
            try {


                String termOpeningDate = Globals.TermOpeningDate(academicyear, Globals.termcode(term));

                double nextTermFee = 0;
                int year = Integer.parseInt(academicyear);
                int YEAR = Integer.parseInt(academicyear);
                String CL = ""; //holds the name of the class to fetch its balance
                String termcode = "";
                String termname = "";
                String sql10 = "Select * from terms where status='" + "Next" + "'";
                ps = con.prepareStatement(sql10);
                ResultSet rr = ps.executeQuery();
                if (rr.next()) {
                    termcode = rr.getString("Termcode");
                    termname = rr.getString("TermName");
                }
                CL = classname;
                if (termname.equalsIgnoreCase("Term 1")) {
                    int press = Globals.classPrecisionDeterminer(classname);
                    press++;
                    CL = "Form " + press;
                    year += 1;
                } else {

                }

                String currentTerm = term;
                int currentyear = Integer.parseInt(academicyear);
                int previousYear = currentyear;
                int currenttermNumber = Integer.parseInt(term.substring(5));
                int currentClassNumber = Integer.parseInt(classname.substring(5));
                int previousClassNumber = currentClassNumber;


                int previoustermNumber = 1;
                if (currenttermNumber == 1) {
                    previoustermNumber = 3;

                    previousYear = currentyear - 1;
                    previousClassNumber = previousClassNumber - 1;
                } else {
                    previoustermNumber = currenttermNumber - 1;
                    previousYear = currentyear;
                    previousClassNumber = currentClassNumber;
                }

                String previousTerm = "TERM " + previoustermNumber;
                String previuosClass = "Form " + previousClassNumber;
                previousYear = previousYear;

                String previousExamCode = ExamCodesGenerator.generatecode(previuosClass, String.valueOf(previousYear), previousTerm, "END TERM").toUpperCase();

                Document doc = new Document();

                PdfWriter writer = PdfWriter.getInstance(doc, new FileOutputStream("C:\\schoolData\\Reportforms.pdf"));
                doc.open();
                int counter = 0;
                String sqla = "Select classpositionthisterm,vap,Streampositionthistermoutof,classpositionlasttermoutof,Streampositionlasttermoutof,classpositionlastterm,streampositionlastterm,classteachergeneralcomment,principalscomment,classpositionthistermoutof,admnumber,classpositionlastterm,streampositionthisterm,streampositionlastterm,fullname,meangrade,meanpoints,Streampositionthistermoutof,totalmarks,totalpoints from examanalysistable where academicyear='" + academicyear + "' and  classname='" + classname + "' and examcode='" + examCode + "' group by admnumber order by classpositionthisterm";
                ps = con.prepareStatement(sqla);
                rs = ps.executeQuery();
                while (rs.next()) {

                    counter++;

                    String grade = "", vap = "", examscore = "", weightscore = "", oexamscore = "", mexamscore = "", eexamscore = "", meangrade = "", totalmarks, totalpoints, meanpoints, subjectName;
                    int entries = 0;
                    meanpoints = rs.getString("meanpoints");
                    meangrade = rs.getString("meangrade");
                    totalmarks = rs.getString("totalmarks");
                    totalpoints = rs.getString("totalpoints");
                    vap = rs.getString("vap");
                    String adm = rs.getString("admnumber");
                    String ovrposition = rs.getString("classpositionthisterm");
                    String streamposition = rs.getString("Streampositionthisterm");
                    String ovroutof = rs.getString("classpositionthistermoutof");
                    String streamoutof = rs.getString("Streampositionthistermoutof");
                    String Name = rs.getString("fullName");
                    String subpoints = "";
                    String classpositionlastterm = rs.getString("classpositionlastterm");
                    String streampositionlastterm = rs.getString("streampositionlastterm");
                    String classpositionlasttermoutof = rs.getString("classpositionlasttermoutof");
                    String streampositionlasttermoutof = rs.getString("Streampositionlasttermoutof");
                    nextTermFee = Globals.nextTermFee(adm, termcode, String.valueOf(year), Globals.classCode(CL));
                    Object[][] performance = PerformanceHistory.performanceSummary(classname, examName, YEAR, adm);

                    onEndPage(writer, doc);

                    PdfPTable tabb = new PdfPTable(26);

                    {//school details header
                        DocHead head = new DocHead();
                        Image img = head.reportFormHeader();
                        File file = new File(Globals.studentImage(adm));

                        if (file.exists()) {
                            BufferedImage inputImage = ImageIO.read(file);
                            BufferedImage outputImage = new BufferedImage(Globals.pictureWidth, Globals.pictureHeight, inputImage.getType());
                            Graphics2D g2d = outputImage.createGraphics();
                            g2d.drawImage(inputImage, 0, 0, Globals.pictureWidth, Globals.pictureHeight, null);
                            g2d.dispose();

                            ImageIO.write(outputImage, "jpg", file);
                            Image img2 = Image.getInstance(Globals.studentImage(adm));
                            img2.setScaleToFitHeight(true);
                            PdfPCell cell1a = new PdfPCell(img2, true);
                            cell1a.setColspan(5);
                            cell1a.setFixedHeight(60);
                            cell1a.setBorderWidthRight(0);


                            PdfPCell cell1 = new PdfPCell(img, true);
                            cell1.setColspan(21);
                            tabb.addCell(cell1a);

                            tabb.addCell(cell1);
                        } else {


                            PdfPCell cell1 = new PdfPCell(img, true);
                            cell1.setColspan(26);


                            tabb.addCell(cell1);
                        }

                    }

                    String dname = "......................................", kcsepositions = "......", kcse = ".......";
                    {
                        Phrase ph = new Phrase("NAME :" + Name + "    ADM NO.:" + adm + "   CLASS :" + classname + "    RESULTS FOR " + term + "  YEAR: " + academicyear, new Font(Font.FontFamily.HELVETICA, 9, Font.NORMAL, BaseColor.BLACK));
                        PdfPCell cell = new PdfPCell(ph);
                        cell.setColspan(26);
                        Phrase ph1 = new Phrase("KCPE MARKS    :  " + Globals.kcpeMarks(adm) + "  POSITION :" + kcsepositions, new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL, BaseColor.BLACK));
                        PdfPCell cell1 = new PdfPCell(ph1);
                        cell1.setColspan(26);
                        Phrase ph2 = new Phrase("This Report Card Was Generated On   :" + new Date(), new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL, BaseColor.BLACK));
                        PdfPCell cell2 = new PdfPCell(ph2);
                        cell2.setColspan(26);
                        tabb.addCell(cell);
                        tabb.addCell(cell1);
                        tabb.addCell(cell2);

                    }
                    {
                        String mweight = "", eweight = "", oweight = "";

                        String sql2 = "Select weight from examweights where examcode='" + examCode.replaceFirst(examName, "OPENER") + "'";
                        ps = con.prepareStatement(sql2);
                        ResultSet RS = ps.executeQuery();
                        if (RS.next()) {

                            oweight = RS.getString("Weight");
                        }
                        String sql2a = "Select weight from examweights where examcode='" + examCode.replaceFirst(examName, "MID TERM") + "'";
                        ps = con.prepareStatement(sql2a);
                        RS = ps.executeQuery();
                        if (RS.next()) {

                            mweight = RS.getString("Weight");
                        }
                        String sql2b = "Select weight from examweights where examcode='" + examCode.replaceFirst(examName, "END TERM") + "'";
                        ps = con.prepareStatement(sql2b);
                        RS = ps.executeQuery();
                        if (RS.next()) {

                            eweight = RS.getString("Weight");
                        }

                        Phrase ph1 = new Phrase(examName + " " + ex, new Font(Font.FontFamily.HELVETICA, 11, Font.NORMAL, BaseColor.BLACK));
                        PdfPCell cell1 = new PdfPCell(ph1);
                        cell1.setColspan(6);
                        tabb.addCell(cell1);

                        Phrase ph2 = new Phrase("OPENER X/" + oweight, new Font(Font.FontFamily.HELVETICA, 12, Font.NORMAL, BaseColor.BLACK));
                        PdfPCell cell2 = new PdfPCell(ph2);
                        cell2.setRowspan(3);
                        cell2.setColspan(2);
                        cell2.setRotation(90);
                        tabb.addCell(cell2);

                        Phrase ph3 = new Phrase("MID TERM X/" + mweight, new Font(Font.FontFamily.HELVETICA, 12, Font.NORMAL, BaseColor.BLACK));
                        // ph3.add(ENDTERMEXAM);
                        PdfPCell cell3 = new PdfPCell(ph3);
                        cell3.setRowspan(3);
                        cell3.setColspan(2);
                        cell3.setRotation(90);
                        tabb.addCell(cell3);

                        Phrase ph4 = new Phrase("END TERM X/" + eweight + "\n", new Font(Font.FontFamily.HELVETICA, 12, Font.NORMAL, BaseColor.BLACK));
                        Phrase ph4a = new Phrase(ENDTERMEXAM, new Font(Font.FontFamily.TIMES_ROMAN, 8, Font.ITALIC, BaseColor.BLACK));

                        PdfPCell cell4 = new PdfPCell();
                        cell4.addElement(ph4);
                        cell4.addElement(ph4a);
                        cell4.setRotation(90);
                        cell4.setRowspan(3);
                        cell4.setColspan(2);
                        tabb.addCell(cell4);

                        Phrase ph5 = new Phrase("TOTAL %(AVG)", new Font(Font.FontFamily.HELVETICA, 12, Font.NORMAL, BaseColor.BLACK));
                        PdfPCell cell5 = new PdfPCell(ph5);
                        cell5.setRotation(90);
                        cell5.setRowspan(3);
                        cell5.setColspan(2);
                        tabb.addCell(cell5);

                        Phrase ph6 = new Phrase("GRADE", new Font(Font.FontFamily.HELVETICA, 12, Font.NORMAL, BaseColor.BLACK));
                        PdfPCell cell6 = new PdfPCell(ph6);
                        cell6.setRotation(90);
                        cell6.setRowspan(3);
                        tabb.addCell(cell6);

                        Phrase ph6a = new Phrase("POINTS", new Font(Font.FontFamily.HELVETICA, 12, Font.NORMAL, BaseColor.BLACK));
                        PdfPCell cell6a = new PdfPCell(ph6a);
                        cell6a.setRotation(90);
                        cell6a.setRowspan(3);
                        tabb.addCell(cell6a);

                        Phrase ph7 = new Phrase("POSITION PER SUBJECT", new Font(Font.FontFamily.HELVETICA, 7, Font.NORMAL, BaseColor.BLACK));
                        PdfPCell cell7 = new PdfPCell(ph7);
                        cell7.setVerticalAlignment(Element.ALIGN_BOTTOM);
                        cell7.setHorizontalAlignment(Element.ALIGN_MIDDLE);
                        cell7.setRowspan(2);
                        cell7.setColspan(2);
                        tabb.addCell(cell7);

                        Phrase ph8 = new Phrase("COMMENTS", new Font(Font.FontFamily.HELVETICA, 12, Font.NORMAL, BaseColor.BLACK));
                        PdfPCell cell8 = new PdfPCell(ph8);
                        cell8.setRowspan(3);
                        cell8.setColspan(6);
                        cell8.setVerticalAlignment(Element.ALIGN_BOTTOM);
                        cell8.setHorizontalAlignment(Element.ALIGN_MIDDLE);
                        tabb.addCell(cell8);

                        Phrase ph9 = new Phrase("Teacher Intials", new Font(Font.FontFamily.HELVETICA, 9, Font.NORMAL, BaseColor.BLACK));
                        PdfPCell cell9 = new PdfPCell(ph9);
                        cell9.setRowspan(3);
                        cell9.setColspan(2);
                        cell9.setVerticalAlignment(Element.ALIGN_BOTTOM);
                        cell9.setHorizontalAlignment(Element.ALIGN_MIDDLE);
                        tabb.addCell(cell9);

                        Phrase ph1a = new Phrase("SUBJECTS", new Font(Font.FontFamily.HELVETICA, 9, Font.NORMAL, BaseColor.BLACK));
                        PdfPCell cell1a = new PdfPCell(ph1a);
                        cell1a.setColspan(6);
                        cell1a.setVerticalAlignment(Element.ALIGN_BOTTOM);
                        cell1a.setHorizontalAlignment(Element.ALIGN_MIDDLE);
                        tabb.addCell(cell1a);
                        Phrase ph1b = new Phrase("NAME", new Font(Font.FontFamily.HELVETICA, 8, Font.NORMAL, BaseColor.BLACK));
                        PdfPCell cell1b = new PdfPCell(ph1b);
                        cell1b.setColspan(4);
                        cell1b.setVerticalAlignment(Element.ALIGN_BOTTOM);
                        cell1b.setHorizontalAlignment(Element.ALIGN_MIDDLE);
                        tabb.addCell(cell1b);
                        Phrase ph1c = new Phrase("CODE", new Font(Font.FontFamily.HELVETICA, 8, Font.NORMAL, BaseColor.BLACK));
                        PdfPCell cell1c = new PdfPCell(ph1c);
                        cell1c.setColspan(2);
                        cell1c.setVerticalAlignment(Element.ALIGN_BOTTOM);
                        cell1c.setHorizontalAlignment(Element.ALIGN_MIDDLE);
                        tabb.addCell(cell1c);

                        Phrase ph7a = new Phrase("POS", new Font(Font.FontFamily.HELVETICA, 7, Font.NORMAL, BaseColor.BLACK));
                        PdfPCell cell7a = new PdfPCell(ph7a);
                        cell7a.setVerticalAlignment(Element.ALIGN_BOTTOM);
                        cell7a.setHorizontalAlignment(Element.ALIGN_MIDDLE);
                        tabb.addCell(cell7a);

                        Phrase ph7b = new Phrase("OUT OF", new Font(Font.FontFamily.HELVETICA, 7, Font.NORMAL, BaseColor.BLACK));
                        PdfPCell cell7b = new PdfPCell(ph7b);
                        cell7b.setVerticalAlignment(Element.ALIGN_BOTTOM);
                        cell7b.setHorizontalAlignment(Element.ALIGN_MIDDLE);

                        tabb.addCell(cell7b);

                    }

                    int maxmarks = 0;
                    int maxpoint = 0;
                    String subcode = "";
                    String sql = "Select * from subjects group by subjectcode";
                    con = DbConnection.connectDb();
                    ps = con.prepareStatement(sql);
                    ResultSet rsb = ps.executeQuery();

                    while (rsb.next()) {
                        subcode = rsb.getString("Subjectcode");
                        Phrase ph1b = new Phrase(rsb.getString("SubjectName"), new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.BOLD, BaseColor.BLACK));
                        PdfPCell cell1b = new PdfPCell(ph1b);
                        cell1b.setColspan(4);
                        cell1b.setVerticalAlignment(Element.ALIGN_BOTTOM);
                        cell1b.setHorizontalAlignment(Element.ALIGN_MIDDLE);
                        tabb.addCell(cell1b);
                        Phrase ph1c = new Phrase(rsb.getString("SubjectCode"), new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.BOLD, BaseColor.BLACK));
                        PdfPCell cell1c = new PdfPCell(ph1c);
                        cell1c.setColspan(2);
                        cell1c.setVerticalAlignment(Element.ALIGN_BOTTOM);
                        cell1c.setHorizontalAlignment(Element.ALIGN_MIDDLE);
                        tabb.addCell(cell1c);
                        String subpos = "", techcom = "", suboutof = "", techin = "";
                        String sqlc = "Select  exampercentage,convertedscore,subjectexamgrade,subjectName,Sujectexampoints,Teacherinitials,subjectposition,subjectpositionoutof,teacherscomment from examanalysistable where academicyear='" + academicyear + "' and examcode='" + examCode + "'  and subjectcode='" + subcode + "' and admnumber='" + adm + "' and classname='" + classname + "'";
                        ps = con.prepareStatement(sqlc);
                        ResultSet rsc = ps.executeQuery();
                        if (rsc.next()) {
                            maxmarks += 100;
                            maxpoint += 12;
                            examscore = rsc.getString("ExamPercentage");
                            grade = rsc.getString("subjectexamgrade");
                            subpoints = rsc.getString("Sujectexampoints");
                            subpos = rsc.getString("subjectposition");
                            suboutof = rsc.getString("subjectpositionoutof");
                            techcom = rsc.getString("teacherscomment");
                            techin = rsc.getString("Teacherinitials");
                            weightscore = rsc.getString("convertedscore");

                        } else {
                            if (Globals.takingSubject(adm, academicyear, subcode)) {
                                examscore = "Y";
                            } else {
                                examscore = "";
                            }

                            grade = "";
                            subpoints = "";
                            subpos = "";
                            suboutof = "";
                            techcom = "";
                            techin = "";
                        }
                        if (examName.equalsIgnoreCase("Total")) {
                            String examcode2 = examCode.replaceFirst(examName.toUpperCase(), "OPENER");
                            String sqlca = "Select  convertedscore from markstable where academicyear='" + academicyear + "' and examcode='" + examcode2 + "'  and subjectcode='" + subcode + "' and admnumber='" + adm + "' and classcode='" + classcode + "'";
                            ps = con.prepareStatement(sqlca);
                            rsc = ps.executeQuery();
                            if (rsc.next()) {

                                oexamscore = rsc.getString("convertedscore");

                            } else {
                                oexamscore = "";

                            }

                            examcode2 = examCode.replaceFirst(examName.toUpperCase(), "MID TERM");
                            String sqlcb = "Select  convertedscore from markstable where academicyear='" + academicyear + "' and examcode='" + examcode2 + "'  and subjectcode='" + subcode + "' and admnumber='" + adm + "' and classcode='" + classcode + "'";
                            ps = con.prepareStatement(sqlcb);
                            rsc = ps.executeQuery();
                            if (rsc.next()) {

                                mexamscore = rsc.getString("convertedscore");

                            } else {
                                mexamscore = "";

                            }
                            String combineModes = "";
                            String sqll = "Select combinemode from examcombinationmodes where examcode='" + examCode + "'";
                            ps = con.prepareStatement(sqll);
                            rsc = ps.executeQuery();
                            if (rsc.next()) {
                                combineModes = rsc.getString("combinemode");
                            }
                            if (combineModes.equalsIgnoreCase("Reversal")) {
                                examcode2 = previousExamCode;

                                String sqlcc = "Select  convertedscore from markstable where academicyear='" + academicyear + "' and examcode='" + examcode2 + "'  and subjectcode='" + subcode + "' and admnumber='" + adm + "' and classcode='" + classcode + "'";
                                ps = con.prepareStatement(sqlcc);
                                rsc = ps.executeQuery();
                                if (rsc.next()) {

                                    eexamscore = rsc.getString("convertedscore");

                                } else {
                                    eexamscore = "";

                                }
                            } else {
                                examcode2 = examCode.replaceFirst(examName.toUpperCase(), "END TERM");
                                String sqlcc = "Select  convertedscore from markstable where academicyear='" + academicyear + "' and examcode='" + examcode2 + "'  and subjectcode='" + subcode + "' and admnumber='" + adm + "' and classcode='" + classcode + "'";
                                ps = con.prepareStatement(sqlcc);
                                rsc = ps.executeQuery();
                                if (rsc.next()) {

                                    eexamscore = rsc.getString("convertedscore");

                                } else {
                                    eexamscore = "";

                                }

                            }


                        } else {

                            if (examName.equalsIgnoreCase("Opener")) {
                                oexamscore = weightscore;
                            } else if (examName.equalsIgnoreCase("Mid Term")) {
                                mexamscore = weightscore;
                            } else if (examName.equalsIgnoreCase("End Term")) {
                                eexamscore = weightscore;
                            } else {
                                eexamscore = weightscore;
                            }

                        }
                        weightscore = "";

                        Phrase ph2 = new Phrase(oexamscore, new Font(Font.FontFamily.HELVETICA, 8, Font.NORMAL, BaseColor.BLACK));
                        PdfPCell cell2 = new PdfPCell(ph2);
                        cell2.setColspan(2);

                        tabb.addCell(cell2);

                        Phrase ph3 = new Phrase(mexamscore, new Font(Font.FontFamily.HELVETICA, 8, Font.NORMAL, BaseColor.BLACK));
                        PdfPCell cell3 = new PdfPCell(ph3);
                        cell3.setColspan(2);

                        tabb.addCell(cell3);

                        Phrase ph4 = new Phrase(eexamscore, new Font(Font.FontFamily.HELVETICA, 8, Font.NORMAL, BaseColor.BLACK));
                        PdfPCell cell4 = new PdfPCell(ph4);

                        cell4.setColspan(2);
                        tabb.addCell(cell4);

                        Phrase ph5 = new Phrase(examscore, new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL, BaseColor.BLACK));
                        PdfPCell cell5 = new PdfPCell(ph5);
                        cell5.setColspan(2);
                        tabb.addCell(cell5);

                        Phrase ph6 = new Phrase(grade, new Font(Font.FontFamily.HELVETICA, 9, Font.NORMAL, BaseColor.BLACK));
                        PdfPCell cell6 = new PdfPCell(ph6);

                        tabb.addCell(cell6);

                        Phrase ph6a = new Phrase(subpoints, new Font(Font.FontFamily.HELVETICA, 9, Font.NORMAL, BaseColor.BLACK));
                        PdfPCell cell6a = new PdfPCell(ph6a);
                        tabb.addCell(cell6a);

                        Phrase ph7a = new Phrase(subpos, new Font(Font.FontFamily.HELVETICA, 9, Font.NORMAL, BaseColor.BLACK));
                        PdfPCell cell7a = new PdfPCell(ph7a);
                        cell7a.setVerticalAlignment(Element.ALIGN_BOTTOM);
                        cell7a.setHorizontalAlignment(Element.ALIGN_MIDDLE);
                        tabb.addCell(cell7a);

                        Phrase ph7b = new Phrase(suboutof, new Font(Font.FontFamily.HELVETICA, 9, Font.NORMAL, BaseColor.BLACK));
                        PdfPCell cell7b = new PdfPCell(ph7b);
                        tabb.addCell(cell7b);

                        Phrase ph8 = new Phrase(techcom, new Font(Font.FontFamily.HELVETICA, 7, Font.NORMAL, BaseColor.BLACK));
                        PdfPCell cell8 = new PdfPCell(ph8);
                        cell8.setColspan(6);

                        tabb.addCell(cell8);

                        Phrase ph9 = new Phrase(techin, new Font(Font.FontFamily.HELVETICA, 8, Font.NORMAL, BaseColor.BLACK));
                        PdfPCell cell9 = new PdfPCell(ph9);
                        cell9.setColspan(2);

                        tabb.addCell(cell9);

                    }

                    {
                        //hides the fields for totals
                        Phrase ph1 = new Phrase("TOTALS", new Font(Font.FontFamily.HELVETICA, 11, Font.NORMAL, BaseColor.BLACK));
                        PdfPCell cell1 = new PdfPCell(ph1);
                        cell1.setColspan(6);
                        tabb.addCell(cell1);

                        Phrase ph2 = new Phrase("", new Font(Font.FontFamily.HELVETICA, 7, Font.NORMAL, BaseColor.BLACK));
                        PdfPCell cell2 = new PdfPCell(ph2);
                        cell2.setColspan(6);
                        tabb.addCell(cell2);

                        Phrase ph5 = new Phrase(totalmarks, new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL, BaseColor.BLACK));
                        PdfPCell cell5 = new PdfPCell(ph5);

                        cell5.setColspan(2);
                        tabb.addCell(cell5);

                        Phrase ph6 = new Phrase("", new Font(Font.FontFamily.HELVETICA, 9, Font.NORMAL, BaseColor.BLACK));
                        PdfPCell cell6 = new PdfPCell(ph6);

                        tabb.addCell(cell6);

                        Phrase ph6a = new Phrase(totalpoints, new Font(Font.FontFamily.HELVETICA, 9, Font.NORMAL, BaseColor.BLACK));
                        PdfPCell cell6a = new PdfPCell(ph6a);

                        tabb.addCell(cell6a);

                        Phrase ph8 = new Phrase("", new Font(Font.FontFamily.HELVETICA, 7, Font.NORMAL, BaseColor.BLACK));
                        PdfPCell cell8 = new PdfPCell(ph8);
                        cell8.setColspan(10);

                        tabb.addCell(cell8);

                    }

                    {
                        //hides the fields for out of
                        Phrase ph1 = new Phrase("OUT OF", new Font(Font.FontFamily.HELVETICA, 11, Font.NORMAL, BaseColor.BLACK));
                        PdfPCell cell1 = new PdfPCell(ph1);
                        cell1.setColspan(6);
                        tabb.addCell(cell1);

                        Phrase ph2 = new Phrase("", new Font(Font.FontFamily.HELVETICA, 7, Font.NORMAL, BaseColor.BLACK));
                        PdfPCell cell2 = new PdfPCell(ph2);
                        cell2.setColspan(6);
                        tabb.addCell(cell2);

                        Phrase ph5 = new Phrase(String.valueOf(maxmarks), new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL, BaseColor.BLACK));
                        PdfPCell cell5 = new PdfPCell(ph5);

                        cell5.setColspan(2);
                        tabb.addCell(cell5);

                        Phrase ph6 = new Phrase("", new Font(Font.FontFamily.HELVETICA, 7, Font.NORMAL, BaseColor.BLACK));
                        PdfPCell cell6 = new PdfPCell(ph6);

                        tabb.addCell(cell6);

                        Phrase ph6a = new Phrase(String.valueOf(maxpoint), new Font(Font.FontFamily.HELVETICA, 9, Font.NORMAL, BaseColor.BLACK));
                        PdfPCell cell6a = new PdfPCell(ph6a);

                        tabb.addCell(cell6a);

                        Phrase ph8 = new Phrase("", new Font(Font.FontFamily.HELVETICA, 7, Font.NORMAL, BaseColor.BLACK));
                        PdfPCell cell8 = new PdfPCell(ph8);
                        cell8.setColspan(10);

                        tabb.addCell(cell8);

                    }

                    {
                        //hides the fields for student meanscores
                        Phrase ph1 = new Phrase("STUDENT'S MEAN SCORE\n " + meanpoints + " Mean Grade: " + meangrade + ", V.A.P: " + vap, new Font(Font.FontFamily.HELVETICA, 11, Font.NORMAL, BaseColor.BLACK));
                        PdfPCell cell1 = new PdfPCell(ph1);
                        cell1.setColspan(26);
                        tabb.addCell(cell1);

                    }

                    {
                        ///position labels
                        Phrase ph1 = new Phrase("", new Font(Font.FontFamily.HELVETICA, 7, Font.NORMAL, BaseColor.BLACK));
                        PdfPCell cell1 = new PdfPCell(ph1);
                        cell1.setColspan(4);
                        cell1.setRowspan(2);
                        tabb.addCell(cell1);

                        Phrase ph2 = new Phrase("POSITION IN CLASS", new Font(Font.FontFamily.HELVETICA, 11, Font.NORMAL, BaseColor.BLACK));
                        PdfPCell cell2 = new PdfPCell(ph2);
                        cell2.setColspan(6);
                        tabb.addCell(cell2);

                        Phrase ph3 = new Phrase("POSITION IN FORM(OVLR.)", new Font(Font.FontFamily.HELVETICA, 11, Font.NORMAL, BaseColor.BLACK));
                        PdfPCell cell3 = new PdfPCell(ph3);
                        cell3.setColspan(6);
                        tabb.addCell(cell3);

                        Phrase ph4 = new Phrase("", new Font(Font.FontFamily.HELVETICA, 7, Font.NORMAL, BaseColor.BLACK));
                        PdfPCell cell4 = new PdfPCell(ph4);
                        cell4.setColspan(10);
                        cell4.setRowspan(2);
                        tabb.addCell(cell4);

                        Phrase ph2a = new Phrase("POSITION ", new Font(Font.FontFamily.HELVETICA, 9, Font.NORMAL, BaseColor.BLACK));
                        PdfPCell cell2a = new PdfPCell(ph2a);
                        cell2a.setColspan(3);
                        tabb.addCell(cell2a);

                        Phrase ph3a = new Phrase("POSITION OUT OF", new Font(Font.FontFamily.HELVETICA, 9, Font.NORMAL, BaseColor.BLACK));
                        PdfPCell cell3a = new PdfPCell(ph3a);
                        cell3a.setColspan(3);
                        tabb.addCell(cell3a);

                        Phrase ph2c = new Phrase("POSITION ", new Font(Font.FontFamily.HELVETICA, 9, Font.NORMAL, BaseColor.BLACK));
                        PdfPCell cell2c = new PdfPCell(ph2c);
                        cell2c.setColspan(3);
                        tabb.addCell(cell2c);

                        Phrase ph3d = new Phrase("POSITION OUT OF", new Font(Font.FontFamily.HELVETICA, 9, Font.NORMAL, BaseColor.BLACK));
                        PdfPCell cell3d = new PdfPCell(ph3d);
                        cell3d.setColspan(3);
                        tabb.addCell(cell3d);

                    }

                    {
                        ///Real this term Positions
                        Phrase ph4 = new Phrase("THIS TERM", new Font(Font.FontFamily.HELVETICA, 11, Font.NORMAL, BaseColor.BLACK));
                        PdfPCell cell4 = new PdfPCell(ph4);
                        cell4.setColspan(4);
                        tabb.addCell(cell4);

                        Phrase ph2a = new Phrase(streamposition, new Font(Font.FontFamily.HELVETICA, 9, Font.NORMAL, BaseColor.BLACK));
                        PdfPCell cell2a = new PdfPCell(ph2a);
                        cell2a.setColspan(3);
                        tabb.addCell(cell2a);

                        Phrase ph3a = new Phrase(streamoutof, new Font(Font.FontFamily.HELVETICA, 9, Font.NORMAL, BaseColor.BLACK));
                        PdfPCell cell3a = new PdfPCell(ph3a);
                        cell3a.setColspan(3);
                        tabb.addCell(cell3a);

                        Phrase ph2c = new Phrase(ovrposition, new Font(Font.FontFamily.HELVETICA, 9, Font.NORMAL, BaseColor.BLACK));
                        PdfPCell cell2c = new PdfPCell(ph2c);
                        cell2c.setColspan(3);
                        tabb.addCell(cell2c);

                        Phrase ph3d = new Phrase(ovroutof, new Font(Font.FontFamily.HELVETICA, 9, Font.NORMAL, BaseColor.BLACK));
                        PdfPCell cell3d = new PdfPCell(ph3d);
                        cell3d.setColspan(3);
                        tabb.addCell(cell3d);

                        Phrase ph4a = new Phrase("", new Font(Font.FontFamily.HELVETICA, 7, Font.NORMAL, BaseColor.BLACK));
                        PdfPCell cell4a = new PdfPCell(ph4a);
                        cell4a.setColspan(10);
                        cell4a.setRowspan(2);
                        tabb.addCell(cell4a);

                    }

                    {
                        ///Real last term Positions
                        Phrase ph4 = new Phrase("LAST TERM", new Font(Font.FontFamily.HELVETICA, 11, Font.NORMAL, BaseColor.BLACK));
                        PdfPCell cell4 = new PdfPCell(ph4);
                        cell4.setColspan(4);
                        tabb.addCell(cell4);

                        Phrase ph2a = new Phrase(streampositionlastterm, new Font(Font.FontFamily.HELVETICA, 9, Font.NORMAL, BaseColor.BLACK));
                        PdfPCell cell2a = new PdfPCell(ph2a);
                        cell2a.setColspan(3);
                        tabb.addCell(cell2a);

                        Phrase ph3a = new Phrase(streampositionlasttermoutof, new Font(Font.FontFamily.HELVETICA, 9, Font.NORMAL, BaseColor.BLACK));
                        PdfPCell cell3a = new PdfPCell(ph3a);
                        cell3a.setColspan(3);
                        tabb.addCell(cell3a);

                        Phrase ph2c = new Phrase(classpositionlastterm, new Font(Font.FontFamily.HELVETICA, 9, Font.NORMAL, BaseColor.BLACK));
                        PdfPCell cell2c = new PdfPCell(ph2c);
                        cell2c.setColspan(3);
                        tabb.addCell(cell2c);

                        Phrase ph3d = new Phrase(classpositionlasttermoutof, new Font(Font.FontFamily.HELVETICA, 9, Font.NORMAL, BaseColor.BLACK));
                        PdfPCell cell3d = new PdfPCell(ph3d);
                        cell3d.setColspan(3);
                        tabb.addCell(cell3d);

                    }

                    {
                        //peformance History cells .....

                        Phrase ph1 = new Phrase("Student's Performance History Summary ", new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL, BaseColor.BLACK));
                        PdfPCell cell1 = new PdfPCell(ph1);
                        cell1.setBorder(PdfPCell.NO_BORDER);
                        cell1.setVerticalAlignment(Element.ALIGN_CENTER);
                        cell1.setColspan(26);
                        tabb.addCell(cell1);

                        for (int j = 0; j < 4; j++) {

                            for (int k = 0; k < 13; k++) {
                                String content = String.valueOf(performance[j][k]).toUpperCase();
                                if (content.equalsIgnoreCase("null")) {
                                    content = "";
                                }

                                Phrase ph2 = new Phrase(content, new Font(Font.FontFamily.HELVETICA, 9, Font.NORMAL, BaseColor.BLACK));
                                PdfPCell cell2 = new PdfPCell(ph2);
                                cell2.setColspan(2);
                                tabb.addCell(cell2);

                            }
                        }

                    }

//        incase Graph is Needed Uncomment This Code            {
//                        DefaultCategoryDataset dataset=new DefaultCategoryDataset();
//                        String kcpemarks=Globals.kcpeMarks(adm);
//                        int avg=0;
//                        String kcpegrade="";
//                        int kcpepoints=0;
//                        
//                            if(kcpemarks.isEmpty())
//                      {
//                          
//                      }
//                      else{
//                          int TOTALmarks=Integer.parseInt(kcpemarks);
//                        avg=TOTALmarks/5;
//                           String sqll = "Select grade from kcpetable where  '" + avg + "'>=starting_from and '" + avg + "'<=ending_at  group by sort_code";
//                                ps = con.prepareStatement(sqll);
//                                ResultSet RS = ps.executeQuery();
//                                if (RS.next()) {
//
//                                   kcpegrade=(RS.getString("grade"));
//                                    String qq = "Select points from points_for_each_grade where grade='" + RS.getString("grade") + "'";
//                                    ps = con.prepareStatement(qq);
//                                    RS = ps.executeQuery();
//                                    if (RS.next()) {
//                                       kcpepoints=(RS.getInt("points"));
//                                    }
//                                }
//
//                        
//                      } 
//                        
//                     dataset.setValue(kcpepoints, "Grade", "KCPE");
//                      
//                    
//                      
//                      double p=0;
//                            
//                                 for(int k=1;k<13;k++)
//                    {
//                        String content=String.valueOf(performance[2][k]).toUpperCase();
//                        if(content.equalsIgnoreCase("null"))
//                        {
//                            content="";
//                            p=0;
//                        }
//                        else{
//                            p=Double.parseDouble(content);
//                            
//                            
//                        }
//                        dataset.setValue(p, "Points", String.valueOf(performance[0][k]));
//                          
//                        System.err.println(performance[0][k]);
//                        }
//                      
//                        JFreeChart chart=ChartFactory.createBarChart("Grade", "Period", "Points", dataset,PlotOrientation.VERTICAL,false,true,false);
//                        CategoryPlot plot=chart.getCategoryPlot();
//                        plot.setRangeGridlinePaint(Color.black);
//                        final ChartRenderingInfo renderer=new ChartRenderingInfo(new StandardEntityCollection());
//                        final File file=new File("C:\\schoolData\\"+adm+"chat.png");
//                        ChartUtilities.saveChartAsPNG(file, chart, 1000, 200, renderer);
//                         Image img =     img = Image.getInstance("C:\\schoolData\\"+adm+"chat.png");
//                         
//                        PdfPCell cell2 = new PdfPCell(img,true);
//                        cell2.setColspan(26);
//                        cell2.setRowspan(2);
//                        tabb.addCell(cell2);
//                        
//                    }
                    {
                        //fee cells
                        Phrase ph2 = new Phrase("FEES\n ", new Font(Font.FontFamily.HELVETICA, 15, Font.NORMAL, BaseColor.BLACK));
                        PdfPCell cell2 = new PdfPCell(ph2);
                        cell2.setColspan(4);

                        tabb.addCell(cell2);

                        Phrase ph3 = new Phrase("NEXT TERM FEES\n\nKsh." + nextTermFee + "\n", new Font(Font.FontFamily.HELVETICA, 11, Font.NORMAL, BaseColor.BLACK));
                        PdfPCell cell3 = new PdfPCell(ph3);
                        cell3.setColspan(5);

                        tabb.addCell(cell3);

                        Phrase ph4 = new Phrase("OUTSTANDING BALANCE\n\n\nKsh. " + Globals.balanceCalculator(adm) + "\n", new Font(Font.FontFamily.HELVETICA, 11, Font.NORMAL, BaseColor.BLACK));
                        PdfPCell cell4 = new PdfPCell(ph4);

                        cell4.setColspan(5);
                        tabb.addCell(cell4);
                        double payable = nextTermFee + Globals.balanceCalculator(adm);
                        Phrase ph5 = new Phrase("FEES PAYABLE\n\nKsh. " + payable, new Font(Font.FontFamily.HELVETICA, 11, Font.NORMAL, BaseColor.BLACK));
                        PdfPCell cell5 = new PdfPCell(ph5);
                        cell5.setColspan(5);
                        tabb.addCell(cell5);

                        Phrase ph6 = new Phrase("", new Font(Font.FontFamily.HELVETICA, 7, Font.NORMAL, BaseColor.BLACK));
                        PdfPCell cell6 = new PdfPCell(ph6);
                        cell6.setColspan(7);

                        tabb.addCell(cell6);
                    }

                    {//class teacher comment and principals comment

                        Phrase ph2 = new Phrase("CLASS TEACHER'S COMMENTS: " + rs.getString("classteachergeneralcomment"), new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.ITALIC, BaseColor.BLACK));
                        PdfPCell cell2 = new PdfPCell(ph2);
                        cell2.setColspan(26);

                        tabb.addCell(cell2);

                        Phrase ph3 = new Phrase("PRINCIPAL'S COMMENTS: " + rs.getString("principalscomment"), new Font(Font.FontFamily.HELVETICA, 10, Font.ITALIC, BaseColor.BLACK));
                        PdfPCell cell3 = new PdfPCell(ph3);
                        cell3.setColspan(26);


                        tabb.addCell(cell3);


                        DocHead head = new DocHead();
                        Image img = head.principalSign();
                        Phrase ph4 = new Phrase("PRINCIPAL'S SIGNATURE/STAMP: ", new Font(Font.FontFamily.HELVETICA, 10, Font.ITALIC, BaseColor.BLACK));
                        PdfPCell cell4 = new PdfPCell(ph4);
                        cell4.setColspan(13);

                        tabb.addCell(cell4);
                        PdfPCell cell5 = new PdfPCell(img, true);
                        cell5.setColspan(13);
                        cell5.setFixedHeight(18);
                        tabb.addCell(cell5);

                    }
                    {
                        //term dates cells....

                        Phrase ph2 = new Phrase("NEXT TERM BEGINS AT. : " + termOpeningDate, new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.NORMAL, BaseColor.BLACK));
                        PdfPCell cell2 = new PdfPCell(ph2);
                        cell2.setColspan(26);

                        tabb.addCell(cell2);

                        // This Report Card was issued without any erasure or alterations whatsoever               
                    }
                    {
                        //parents signature celll
                        Phrase ph2 = new Phrase("PARENT/GUARDIAN'S SIGNATURE:.......................................", new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.NORMAL, BaseColor.BLACK));
                        PdfPCell cell2 = new PdfPCell(ph2);
                        cell2.setColspan(26);

                        tabb.addCell(cell2);
                    }
                    {
                        //DECLARITION CELL
                        Phrase ph2 = new Phrase("This Report Card was isued without any erasure or alterations whatsoever", new Font(Font.FontFamily.TIMES_ROMAN, 11, Font.ITALIC, BaseColor.BLACK));
                        PdfPCell cell2 = new PdfPCell(ph2);
                        cell2.setColspan(26);

                        tabb.addCell(cell2);
                    }

                    tabb.setSpacingBefore(30);
                    tabb.setWidthPercentage(100);

                    doc.add(tabb);

                    PdfPTable tab = new PdfPTable(6);
                    Phrase ph2 = new Phrase("CLASS & PERIOD", new Font(Font.FontFamily.HELVETICA, 7, Font.NORMAL, BaseColor.BLACK));
                    PdfPCell cell2 = new PdfPCell(ph2);

                    tab.addCell(cell2);

                    Phrase ph3 = new Phrase("MEAN GRADE", new Font(Font.FontFamily.HELVETICA, 7, Font.NORMAL, BaseColor.BLACK));
                    PdfPCell cell3 = new PdfPCell(ph3);

                    tab.addCell(cell3);

                    Phrase ph4 = new Phrase("MEAN POINTS", new Font(Font.FontFamily.HELVETICA, 7, Font.NORMAL, BaseColor.BLACK));
                    PdfPCell cell4 = new PdfPCell(ph4);

                    tab.addCell(cell4);

                    Phrase ph5 = new Phrase("TOTAL POINTS", new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL, BaseColor.BLACK));
                    PdfPCell cell5 = new PdfPCell(ph5);

                    tab.addCell(cell5);

                    Phrase ph6 = new Phrase("MEAN MARKS", new Font(Font.FontFamily.HELVETICA, 9, Font.NORMAL, BaseColor.BLACK));
                    PdfPCell cell6 = new PdfPCell(ph6);

                    tab.addCell(cell6);

                    Phrase ph6a = new Phrase("TOTAL MAKS", new Font(Font.FontFamily.HELVETICA, 9, Font.NORMAL, BaseColor.BLACK));
                    PdfPCell cell6a = new PdfPCell(ph6a);
                    tab.addCell(cell6a);
                    tab.setWidthPercentage(100);
                    // doc.add(tab);

                    doc.newPage();
                }

                if (rs.previous()) {
                    doc.close();
                    if (ConfigurationIntialiser.docOpener()) {
                        if (dialog == null) {

                            dialog = ReportFrame.getReportFrameInstance(new File("C:\\schoolData\\Reportforms.pdf"));

                        } else {
                            dialog = null;
                            dialog = ReportFrame.getReportFrameInstance(new File("C:\\schoolData\\Reportforms.pdf"));
                            dialog.setVisible(true);

                        }

                    } else {

                        Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler " + "C:\\schoolData\\Reportforms.pdf");

                    }
                } else {
                    JOptionPane.showMessageDialog(null, "No Report Forms Were  Found For This Exam,\n Kindly Analyse The Exam And Try To Regenerate Report Forms If Marks Have Been Entered");
                }

            } catch (Exception sq) {
                sq.printStackTrace();
                JOptionPane.showMessageDialog(null, sq.getMessage());
            }
        } else {

            try {
                con = DbConnection.connectDb();
                String termOpeningDate = Globals.TermOpeningDate(academicyear, Globals.termcode(term));
                double nextTermFee = 0;
                int year = Integer.parseInt(academicyear);
                int YEAR = year;
                String termcode = "";
                String termname = "";
                String CL = classname;
                String sql10 = "Select * from terms where status='" + "Next" + "'";
                ps = con.prepareStatement(sql10);
                ResultSet rr = ps.executeQuery();
                if (rr.next()) {
                    termcode = rr.getString("Termcode");
                    termname = rr.getString("TermName");
                }
                if (termname.equalsIgnoreCase("Term 1")) {

                    int press = Globals.classPrecisionDeterminer(classname);
                    press++;
                    CL = "Form " + press;
                    year += 1;

                } else {

                }

                nextTermFee = Globals.nextTermFee(admissionnumber, termcode, String.valueOf(year), Globals.classCode(CL));

                Document doc = new Document();

                PdfWriter writer = PdfWriter.getInstance(doc, new FileOutputStream("C:\\schoolData\\Reportforms.pdf"));
                doc.open();
                int counter = 0;
                String sqla = "Select classpositionthisterm,vap,classpositionlasttermoutof,Streampositionlasttermoutof,streampositionlastterm,classpositionlastterm,classteachergeneralcomment,principalscomment,classpositionthistermoutof,admnumber,classpositionlastterm,streampositionthisterm,streampositionlastterm,fullname,meangrade,meanpoints,Streampositionthistermoutof,totalmarks,totalpoints from examanalysistable where academicyear='" + academicyear + "' and  classname='" + classname + "' and examcode='" + examCode + "' and admnumber='" + admissionnumber + "' group by admnumber order by classpositionthisterm";
                ps = con.prepareStatement(sqla);
                rs = ps.executeQuery();
                while (rs.next()) {

                    Object[][] performance = PerformanceHistory.performanceSummary(classname, examName, YEAR, admissionnumber);
                    counter++;
                    String grade = "", examscore = "", vap = "", weightscore = "", oexamscore = "", mexamscore = "", eexamscore = "", meangrade = "", totalmarks, totalpoints, meanpoints, subjectName;
                    int entries = 0;
                    meanpoints = rs.getString("meanpoints");
                    meangrade = rs.getString("meangrade");
                    totalmarks = rs.getString("totalmarks");
                    totalpoints = rs.getString("totalpoints");
                    String adm = rs.getString("admnumber");
                    String ovrposition = rs.getString("classpositionthisterm");
                    String streamposition = rs.getString("Streampositionthisterm");
                    String ovroutof = rs.getString("classpositionthistermoutof");
                    String streamoutof = rs.getString("Streampositionthistermoutof");
                    vap = rs.getString("vap");
                    String Name = rs.getString("fullName");
                    String subpoints = "";
                    String classpositionlastterm = rs.getString("classpositionlastterm");
                    String streampositionlastterm = rs.getString("streampositionlastterm");
                    String classpositionlasttermoutof = rs.getString("classpositionlasttermoutof");
                    String streampositionlasttermoutof = rs.getString("Streampositionlasttermoutof");
                    onEndPage(writer, doc);

                    PdfPTable tabb = new PdfPTable(26);

                    {//school details header
                        DocHead head = new DocHead();
                        Image img = head.reportFormHeader();

                        File file = new File(Globals.studentImage(adm));

                        if (file.exists()) {
                            BufferedImage inputImage = ImageIO.read(file);
                            BufferedImage outputImage = new BufferedImage(Globals.pictureWidth, Globals.pictureHeight, inputImage.getType());
                            Graphics2D g2d = outputImage.createGraphics();
                            g2d.drawImage(inputImage, 0, 0, Globals.pictureWidth, Globals.pictureHeight, null);
                            g2d.dispose();

                            ImageIO.write(outputImage, "jpg", file);
                            Image img2 = Image.getInstance(Globals.studentImage(adm));
                            img2.setScaleToFitHeight(true);
                            PdfPCell cell1a = new PdfPCell(img2, true);
                            cell1a.setColspan(5);
                            cell1a.setFixedHeight(60);
                            cell1a.setBorderWidthRight(0);


                            PdfPCell cell1 = new PdfPCell(img, true);
                            cell1.setColspan(21);
                            tabb.addCell(cell1a);

                            tabb.addCell(cell1);
                        } else {


                            PdfPCell cell1 = new PdfPCell(img, true);
                            cell1.setColspan(26);


                            tabb.addCell(cell1);
                        }
                    }

                    String dname = "......................................", kcsepositions = "......", kcse = ".......";
                    {
                        Phrase ph = new Phrase("NAME : " + Name + "  ADM NO.: " + adm + "  CLASS :" + classname + "   RESULTS FOR " + term + "  YEAR: " + academicyear, new Font(Font.FontFamily.HELVETICA, 9, Font.NORMAL, BaseColor.BLACK));
                        PdfPCell cell = new PdfPCell(ph);
                        cell.setColspan(26);
                        Phrase ph1 = new Phrase("KCPE MARKS    :  " + Globals.kcpeMarks(adm) + "  POSITION :" + kcsepositions, new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL, BaseColor.BLACK));
                        PdfPCell cell1 = new PdfPCell(ph1);
                        cell1.setColspan(26);
                        Phrase ph2 = new Phrase("This Report Card Was Generated On   :" + new Date(), new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL, BaseColor.BLACK));
                        PdfPCell cell2 = new PdfPCell(ph2);
                        cell2.setColspan(26);
                        tabb.addCell(cell);
                        tabb.addCell(cell1);
                        tabb.addCell(cell2);

                    }
                    {


                        String mweight = "", eweight = "", oweight = "";

                        String sql2 = "Select weight from examweights where examcode='" + examCode.replaceFirst(examName, "OPENER") + "'";
                        ps = con.prepareStatement(sql2);
                        ResultSet RS = ps.executeQuery();
                        if (RS.next()) {

                            oweight = RS.getString("Weight");
                        }
                        String sql2a = "Select weight from examweights where examcode='" + examCode.replaceFirst(examName, "MID TERM") + "'";
                        ps = con.prepareStatement(sql2a);
                        RS = ps.executeQuery();
                        if (RS.next()) {

                            mweight = RS.getString("Weight");
                        }
                        String sql2b = "Select weight from examweights where examcode='" + examCode.replaceFirst(examName, "END TERM") + "'";
                        ps = con.prepareStatement(sql2b);
                        RS = ps.executeQuery();
                        if (RS.next()) {

                            eweight = RS.getString("Weight");
                        }
                        Phrase ph1 = new Phrase(examName + " " + ex, new Font(Font.FontFamily.HELVETICA, 11, Font.NORMAL, BaseColor.BLACK));
                        PdfPCell cell1 = new PdfPCell(ph1);
                        cell1.setColspan(6);
                        tabb.addCell(cell1);

                        Phrase ph2 = new Phrase("OPENER X/" + oweight, new Font(Font.FontFamily.HELVETICA, 12, Font.NORMAL, BaseColor.BLACK));
                        PdfPCell cell2 = new PdfPCell(ph2);
                        cell2.setRowspan(3);
                        cell2.setColspan(2);
                        cell2.setRotation(90);
                        tabb.addCell(cell2);

                        Phrase ph3 = new Phrase("MID TERM X/" + mweight, new Font(Font.FontFamily.HELVETICA, 12, Font.NORMAL, BaseColor.BLACK));
                        PdfPCell cell3 = new PdfPCell(ph3);
                        cell3.setRowspan(3);
                        cell3.setColspan(2);
                        cell3.setRotation(90);
                        tabb.addCell(cell3);

                        Phrase ph4 = new Phrase("END TERM X/" + eweight + "\n", new Font(Font.FontFamily.HELVETICA, 12, Font.NORMAL, BaseColor.BLACK));
                        Phrase ph4a = new Phrase(ENDTERMEXAM, new Font(Font.FontFamily.TIMES_ROMAN, 8, Font.ITALIC, BaseColor.BLACK));
                        PdfPCell cell4 = new PdfPCell();
                        cell4.addElement(ph4);
                        cell4.addElement(ph4a);
                        cell4.setRotation(90);
                        cell4.setRowspan(3);
                        cell4.setColspan(2);
                        tabb.addCell(cell4);

                        Phrase ph5 = new Phrase("TOTAL %(AVG.)", new Font(Font.FontFamily.HELVETICA, 12, Font.NORMAL, BaseColor.BLACK));
                        PdfPCell cell5 = new PdfPCell(ph5);
                        cell5.setRotation(90);
                        cell5.setRowspan(3);
                        cell5.setColspan(2);
                        tabb.addCell(cell5);

                        Phrase ph6 = new Phrase("GRADE", new Font(Font.FontFamily.HELVETICA, 12, Font.NORMAL, BaseColor.BLACK));
                        PdfPCell cell6 = new PdfPCell(ph6);
                        cell6.setRotation(90);
                        cell6.setRowspan(3);
                        tabb.addCell(cell6);

                        Phrase ph6a = new Phrase("POINTS", new Font(Font.FontFamily.HELVETICA, 12, Font.NORMAL, BaseColor.BLACK));
                        PdfPCell cell6a = new PdfPCell(ph6a);
                        cell6a.setRotation(90);
                        cell6a.setRowspan(3);
                        tabb.addCell(cell6a);

                        Phrase ph7 = new Phrase("POSITION PER SUBJECT", new Font(Font.FontFamily.HELVETICA, 7, Font.NORMAL, BaseColor.BLACK));
                        PdfPCell cell7 = new PdfPCell(ph7);
                        cell7.setVerticalAlignment(Element.ALIGN_BOTTOM);
                        cell7.setHorizontalAlignment(Element.ALIGN_MIDDLE);
                        cell7.setRowspan(2);
                        cell7.setColspan(2);
                        tabb.addCell(cell7);

                        Phrase ph8 = new Phrase("COMMENTS", new Font(Font.FontFamily.HELVETICA, 12, Font.NORMAL, BaseColor.BLACK));
                        PdfPCell cell8 = new PdfPCell(ph8);
                        cell8.setRowspan(3);
                        cell8.setColspan(6);
                        cell8.setVerticalAlignment(Element.ALIGN_BOTTOM);
                        cell8.setHorizontalAlignment(Element.ALIGN_MIDDLE);
                        tabb.addCell(cell8);

                        Phrase ph9 = new Phrase("Teacher Intials", new Font(Font.FontFamily.HELVETICA, 9, Font.NORMAL, BaseColor.BLACK));
                        PdfPCell cell9 = new PdfPCell(ph9);
                        cell9.setRowspan(3);
                        cell9.setColspan(2);
                        cell9.setVerticalAlignment(Element.ALIGN_BOTTOM);
                        cell9.setHorizontalAlignment(Element.ALIGN_MIDDLE);
                        tabb.addCell(cell9);

                        Phrase ph1a = new Phrase("SUBJECTS", new Font(Font.FontFamily.HELVETICA, 9, Font.NORMAL, BaseColor.BLACK));
                        PdfPCell cell1a = new PdfPCell(ph1a);
                        cell1a.setColspan(6);
                        cell1a.setVerticalAlignment(Element.ALIGN_BOTTOM);
                        cell1a.setHorizontalAlignment(Element.ALIGN_MIDDLE);
                        tabb.addCell(cell1a);
                        Phrase ph1b = new Phrase("NAME", new Font(Font.FontFamily.HELVETICA, 8, Font.NORMAL, BaseColor.BLACK));
                        PdfPCell cell1b = new PdfPCell(ph1b);
                        cell1b.setColspan(4);
                        cell1b.setVerticalAlignment(Element.ALIGN_BOTTOM);
                        cell1b.setHorizontalAlignment(Element.ALIGN_MIDDLE);
                        tabb.addCell(cell1b);
                        Phrase ph1c = new Phrase("CODE", new Font(Font.FontFamily.HELVETICA, 8, Font.NORMAL, BaseColor.BLACK));
                        PdfPCell cell1c = new PdfPCell(ph1c);
                        cell1c.setColspan(2);
                        cell1c.setVerticalAlignment(Element.ALIGN_BOTTOM);
                        cell1c.setHorizontalAlignment(Element.ALIGN_MIDDLE);
                        tabb.addCell(cell1c);

                        Phrase ph7a = new Phrase("POS", new Font(Font.FontFamily.HELVETICA, 7, Font.NORMAL, BaseColor.BLACK));
                        PdfPCell cell7a = new PdfPCell(ph7a);
                        cell7a.setVerticalAlignment(Element.ALIGN_BOTTOM);
                        cell7a.setHorizontalAlignment(Element.ALIGN_MIDDLE);
                        tabb.addCell(cell7a);

                        Phrase ph7b = new Phrase("OUT OF", new Font(Font.FontFamily.HELVETICA, 7, Font.NORMAL, BaseColor.BLACK));
                        PdfPCell cell7b = new PdfPCell(ph7b);
                        cell7b.setVerticalAlignment(Element.ALIGN_BOTTOM);
                        cell7b.setHorizontalAlignment(Element.ALIGN_MIDDLE);

                        tabb.addCell(cell7b);

                    }

                    int maxmarks = 0;
                    int maxpoint = 0;
                    String subcode = "";
                    String sql = "Select * from subjects group by subjectcode";
                    con = DbConnection.connectDb();
                    ps = con.prepareStatement(sql);
                    ResultSet rsb = ps.executeQuery();

                    while (rsb.next()) {
                        subcode = rsb.getString("Subjectcode");
                        Phrase ph1b = new Phrase(rsb.getString("SubjectName"), new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.BOLD, BaseColor.BLACK));
                        PdfPCell cell1b = new PdfPCell(ph1b);
                        cell1b.setColspan(4);
                        cell1b.setVerticalAlignment(Element.ALIGN_BOTTOM);
                        cell1b.setHorizontalAlignment(Element.ALIGN_MIDDLE);
                        tabb.addCell(cell1b);
                        Phrase ph1c = new Phrase(rsb.getString("SubjectCode"), new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.BOLD, BaseColor.BLACK));
                        PdfPCell cell1c = new PdfPCell(ph1c);
                        cell1c.setColspan(2);
                        cell1c.setVerticalAlignment(Element.ALIGN_BOTTOM);
                        cell1c.setHorizontalAlignment(Element.ALIGN_MIDDLE);
                        tabb.addCell(cell1c);
                        String subpos = "", techcom = "", suboutof = "", techin = "";
                        String sqlc = "Select  exampercentage,subjectexamgrade,convertedscore,subjectName,Sujectexampoints,Teacherinitials,subjectposition,subjectpositionoutof,teacherscomment from examanalysistable where academicyear='" + academicyear + "' and examcode='" + examCode + "'  and subjectcode='" + subcode + "' and admnumber='" + adm + "' and classname='" + classname + "'";
                        ps = con.prepareStatement(sqlc);
                        ResultSet rsc = ps.executeQuery();
                        if (rsc.next()) {
                            maxmarks += 100;
                            maxpoint += 12;
                            examscore = rsc.getString("ExamPercentage");
                            grade = rsc.getString("subjectexamgrade");
                            subpoints = rsc.getString("Sujectexampoints");
                            subpos = rsc.getString("subjectposition");
                            suboutof = rsc.getString("subjectpositionoutof");
                            techcom = rsc.getString("teacherscomment");
                            techin = rsc.getString("Teacherinitials");
                            weightscore = rsc.getString("convertedscore");

                        } else {
                            if (Globals.takingSubject(adm, academicyear, subcode)) {
                                examscore = "Y";
                            } else {
                                examscore = "";
                            }
                            grade = "";
                            subpoints = "";
                            subpos = "";
                            suboutof = "";
                            techcom = "";
                            techin = "";
                        }


                        String currentTerm = term;
                        int currentyear = Integer.parseInt(academicyear);
                        int previousYear = currentyear;
                        int currenttermNumber = Integer.parseInt(term.substring(5));
                        int currentClassNumber = Integer.parseInt(classname.substring(5));
                        int previousClassNumber = currentClassNumber;


                        int previoustermNumber = 1;
                        if (currenttermNumber == 1) {
                            previoustermNumber = 3;

                            previousYear = currentyear - 1;
                            previousClassNumber = previousClassNumber - 1;
                        } else {
                            previoustermNumber = currenttermNumber - 1;
                            previousYear = currentyear;
                            previousClassNumber = currentClassNumber;
                        }

                        String previousTerm = "TERM " + previoustermNumber;
                        String previuosClass = "Form " + previousClassNumber;
                        previousYear = previousYear;

                        String previousExamCode = ExamCodesGenerator.generatecode(previuosClass, String.valueOf(previousYear), previousTerm, "END TERM").toUpperCase();


                        if (examName.equalsIgnoreCase("Total")) {
                            String examcode2 = examCode.replaceFirst(examName, "OPENER");
                            String sqlca = "Select  convertedscore from markstable where academicyear='" + academicyear + "' and examcode='" + examcode2 + "'  and subjectcode='" + subcode + "' and admnumber='" + adm + "' and classcode='" + classcode + "'";
                            ps = con.prepareStatement(sqlca);
                            rsc = ps.executeQuery();
                            if (rsc.next()) {

                                oexamscore = rsc.getString("convertedscore");

                            } else {
                                oexamscore = "";

                            }

                            examcode2 = examCode.replaceFirst(examName, "MID TERM");
                            String sqlcb = "Select  convertedscore from markstable where academicyear='" + academicyear + "' and examcode='" + examcode2 + "'  and subjectcode='" + subcode + "' and admnumber='" + adm + "' and classcode='" + classcode + "'";
                            ps = con.prepareStatement(sqlcb);
                            rsc = ps.executeQuery();
                            if (rsc.next()) {

                                mexamscore = rsc.getString("convertedscore");

                            } else {
                                mexamscore = "";

                            }

                            String combineModes = "";
                            String sqll = "Select combinemode from examcombinationmodes where examcode='" + examCode + "'";
                            ps = con.prepareStatement(sqll);
                            rsc = ps.executeQuery();
                            if (rsc.next()) {
                                combineModes = rsc.getString("combinemode");
                            }
                            if (combineModes.equalsIgnoreCase("Reversal")) {
                                examcode2 = previousExamCode;

                                String sqlcc = "Select  convertedscore from markstable where academicyear='" + academicyear + "' and examcode='" + examcode2 + "'  and subjectcode='" + subcode + "' and admnumber='" + adm + "' and classcode='" + classcode + "'";
                                ps = con.prepareStatement(sqlcc);
                                rsc = ps.executeQuery();
                                if (rsc.next()) {

                                    eexamscore = rsc.getString("convertedscore");

                                } else {
                                    eexamscore = "";

                                }
                            } else {
                                examcode2 = examCode.replaceFirst(examName.toUpperCase(), "END TERM");
                                String sqlcc = "Select  convertedscore from markstable where academicyear='" + academicyear + "' and examcode='" + examcode2 + "'  and subjectcode='" + subcode + "' and admnumber='" + adm + "' and classcode='" + classcode + "'";
                                ps = con.prepareStatement(sqlcc);
                                rsc = ps.executeQuery();
                                if (rsc.next()) {

                                    eexamscore = rsc.getString("convertedscore");

                                } else {
                                    eexamscore = "";

                                }

                            }


                        } else {
                            if (examName.equalsIgnoreCase("Opener")) {
                                oexamscore = weightscore;
                            } else if (examName.equalsIgnoreCase("Mid Term")) {
                                mexamscore = weightscore;
                            } else if (examName.equalsIgnoreCase("End Term")) {
                                eexamscore = weightscore;
                            } else {
                                eexamscore = weightscore;
                            }
                        }
                        weightscore = "";

                        Phrase ph2 = new Phrase(oexamscore, new Font(Font.FontFamily.HELVETICA, 8, Font.NORMAL, BaseColor.BLACK));
                        PdfPCell cell2 = new PdfPCell(ph2);
                        cell2.setColspan(2);

                        tabb.addCell(cell2);

                        Phrase ph3 = new Phrase(mexamscore, new Font(Font.FontFamily.HELVETICA, 8, Font.NORMAL, BaseColor.BLACK));
                        PdfPCell cell3 = new PdfPCell(ph3);
                        cell3.setColspan(2);

                        tabb.addCell(cell3);

                        Phrase ph4 = new Phrase(eexamscore, new Font(Font.FontFamily.HELVETICA, 8, Font.NORMAL, BaseColor.BLACK));
                        PdfPCell cell4 = new PdfPCell(ph4);

                        cell4.setColspan(2);
                        tabb.addCell(cell4);

                        Phrase ph5 = new Phrase(examscore, new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL, BaseColor.BLACK));
                        PdfPCell cell5 = new PdfPCell(ph5);
                        cell5.setColspan(2);
                        tabb.addCell(cell5);

                        Phrase ph6 = new Phrase(grade, new Font(Font.FontFamily.HELVETICA, 9, Font.NORMAL, BaseColor.BLACK));
                        PdfPCell cell6 = new PdfPCell(ph6);

                        tabb.addCell(cell6);

                        Phrase ph6a = new Phrase(subpoints, new Font(Font.FontFamily.HELVETICA, 9, Font.NORMAL, BaseColor.BLACK));
                        PdfPCell cell6a = new PdfPCell(ph6a);
                        tabb.addCell(cell6a);

                        Phrase ph7a = new Phrase(subpos, new Font(Font.FontFamily.HELVETICA, 9, Font.NORMAL, BaseColor.BLACK));
                        PdfPCell cell7a = new PdfPCell(ph7a);
                        cell7a.setVerticalAlignment(Element.ALIGN_BOTTOM);
                        cell7a.setHorizontalAlignment(Element.ALIGN_MIDDLE);
                        tabb.addCell(cell7a);

                        Phrase ph7b = new Phrase(suboutof, new Font(Font.FontFamily.HELVETICA, 9, Font.NORMAL, BaseColor.BLACK));
                        PdfPCell cell7b = new PdfPCell(ph7b);
                        tabb.addCell(cell7b);

                        Phrase ph8 = new Phrase(techcom, new Font(Font.FontFamily.HELVETICA, 7, Font.NORMAL, BaseColor.BLACK));
                        PdfPCell cell8 = new PdfPCell(ph8);
                        cell8.setColspan(6);

                        tabb.addCell(cell8);

                        Phrase ph9 = new Phrase(techin, new Font(Font.FontFamily.HELVETICA, 8, Font.NORMAL, BaseColor.BLACK));
                        PdfPCell cell9 = new PdfPCell(ph9);
                        cell9.setColspan(2);

                        tabb.addCell(cell9);

                    }

                    {
                        //hides the fields for totals
                        Phrase ph1 = new Phrase("TOTALS", new Font(Font.FontFamily.HELVETICA, 11, Font.NORMAL, BaseColor.BLACK));
                        PdfPCell cell1 = new PdfPCell(ph1);
                        cell1.setColspan(6);
                        tabb.addCell(cell1);

                        Phrase ph2 = new Phrase("", new Font(Font.FontFamily.HELVETICA, 7, Font.NORMAL, BaseColor.BLACK));
                        PdfPCell cell2 = new PdfPCell(ph2);
                        cell2.setColspan(6);
                        tabb.addCell(cell2);

                        Phrase ph5 = new Phrase(totalmarks, new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL, BaseColor.BLACK));
                        PdfPCell cell5 = new PdfPCell(ph5);

                        cell5.setColspan(2);
                        tabb.addCell(cell5);

                        Phrase ph6 = new Phrase("", new Font(Font.FontFamily.HELVETICA, 9, Font.NORMAL, BaseColor.BLACK));
                        PdfPCell cell6 = new PdfPCell(ph6);

                        tabb.addCell(cell6);

                        Phrase ph6a = new Phrase(totalpoints, new Font(Font.FontFamily.HELVETICA, 9, Font.NORMAL, BaseColor.BLACK));
                        PdfPCell cell6a = new PdfPCell(ph6a);

                        tabb.addCell(cell6a);

                        Phrase ph8 = new Phrase("", new Font(Font.FontFamily.HELVETICA, 7, Font.NORMAL, BaseColor.BLACK));
                        PdfPCell cell8 = new PdfPCell(ph8);
                        cell8.setColspan(10);

                        tabb.addCell(cell8);

                    }

                    {
                        //hides the fields for out of
                        Phrase ph1 = new Phrase("OUT OF", new Font(Font.FontFamily.HELVETICA, 11, Font.NORMAL, BaseColor.BLACK));
                        PdfPCell cell1 = new PdfPCell(ph1);
                        cell1.setColspan(6);
                        tabb.addCell(cell1);

                        Phrase ph2 = new Phrase("", new Font(Font.FontFamily.HELVETICA, 7, Font.NORMAL, BaseColor.BLACK));
                        PdfPCell cell2 = new PdfPCell(ph2);
                        cell2.setColspan(6);
                        tabb.addCell(cell2);

                        Phrase ph5 = new Phrase(String.valueOf(maxmarks), new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL, BaseColor.BLACK));
                        PdfPCell cell5 = new PdfPCell(ph5);

                        cell5.setColspan(2);
                        tabb.addCell(cell5);

                        Phrase ph6 = new Phrase("", new Font(Font.FontFamily.HELVETICA, 7, Font.NORMAL, BaseColor.BLACK));
                        PdfPCell cell6 = new PdfPCell(ph6);

                        tabb.addCell(cell6);

                        Phrase ph6a = new Phrase(String.valueOf(maxpoint), new Font(Font.FontFamily.HELVETICA, 9, Font.NORMAL, BaseColor.BLACK));
                        PdfPCell cell6a = new PdfPCell(ph6a);

                        tabb.addCell(cell6a);

                        Phrase ph8 = new Phrase("", new Font(Font.FontFamily.HELVETICA, 7, Font.NORMAL, BaseColor.BLACK));
                        PdfPCell cell8 = new PdfPCell(ph8);
                        cell8.setColspan(10);

                        tabb.addCell(cell8);

                    }

                    {
                        //hides the fields for student meanscores
                        Phrase ph1 = new Phrase("STUDENT'S MEAN SCORE\n " + meanpoints + " Mean Grade: " + meangrade + ", V.A.P: " + vap, new Font(Font.FontFamily.HELVETICA, 11, Font.NORMAL, BaseColor.BLACK));
                        PdfPCell cell1 = new PdfPCell(ph1);
                        cell1.setColspan(26);
                        tabb.addCell(cell1);

                    }

                    {
                        ///position labels
                        Phrase ph1 = new Phrase("", new Font(Font.FontFamily.HELVETICA, 7, Font.NORMAL, BaseColor.BLACK));
                        PdfPCell cell1 = new PdfPCell(ph1);
                        cell1.setColspan(4);
                        cell1.setRowspan(2);
                        tabb.addCell(cell1);

                        Phrase ph2 = new Phrase("POSITION IN CLASS", new Font(Font.FontFamily.HELVETICA, 11, Font.NORMAL, BaseColor.BLACK));
                        PdfPCell cell2 = new PdfPCell(ph2);
                        cell2.setColspan(6);
                        tabb.addCell(cell2);

                        Phrase ph3 = new Phrase("POSITION IN FORM(OVLR.)", new Font(Font.FontFamily.HELVETICA, 11, Font.NORMAL, BaseColor.BLACK));
                        PdfPCell cell3 = new PdfPCell(ph3);
                        cell3.setColspan(6);
                        tabb.addCell(cell3);

                        Phrase ph4 = new Phrase("", new Font(Font.FontFamily.HELVETICA, 7, Font.NORMAL, BaseColor.BLACK));
                        PdfPCell cell4 = new PdfPCell(ph4);
                        cell4.setColspan(10);
                        cell4.setRowspan(2);
                        tabb.addCell(cell4);

                        Phrase ph2a = new Phrase("POSITION ", new Font(Font.FontFamily.HELVETICA, 9, Font.NORMAL, BaseColor.BLACK));
                        PdfPCell cell2a = new PdfPCell(ph2a);
                        cell2a.setColspan(3);
                        tabb.addCell(cell2a);

                        Phrase ph3a = new Phrase("POSITION OUT OF", new Font(Font.FontFamily.HELVETICA, 9, Font.NORMAL, BaseColor.BLACK));
                        PdfPCell cell3a = new PdfPCell(ph3a);
                        cell3a.setColspan(3);
                        tabb.addCell(cell3a);

                        Phrase ph2c = new Phrase("POSITION ", new Font(Font.FontFamily.HELVETICA, 9, Font.NORMAL, BaseColor.BLACK));
                        PdfPCell cell2c = new PdfPCell(ph2c);
                        cell2c.setColspan(3);
                        tabb.addCell(cell2c);

                        Phrase ph3d = new Phrase("POSITION OUT OF", new Font(Font.FontFamily.HELVETICA, 9, Font.NORMAL, BaseColor.BLACK));
                        PdfPCell cell3d = new PdfPCell(ph3d);
                        cell3d.setColspan(3);
                        tabb.addCell(cell3d);

                    }

                    {
                        ///Real this term Positions
                        Phrase ph4 = new Phrase("THIS TERM", new Font(Font.FontFamily.HELVETICA, 11, Font.NORMAL, BaseColor.BLACK));
                        PdfPCell cell4 = new PdfPCell(ph4);
                        cell4.setColspan(4);
                        tabb.addCell(cell4);

                        Phrase ph2a = new Phrase(streamposition, new Font(Font.FontFamily.HELVETICA, 9, Font.NORMAL, BaseColor.BLACK));
                        PdfPCell cell2a = new PdfPCell(ph2a);
                        cell2a.setColspan(3);
                        tabb.addCell(cell2a);

                        Phrase ph3a = new Phrase(streamoutof, new Font(Font.FontFamily.HELVETICA, 9, Font.NORMAL, BaseColor.BLACK));
                        PdfPCell cell3a = new PdfPCell(ph3a);
                        cell3a.setColspan(3);
                        tabb.addCell(cell3a);

                        Phrase ph2c = new Phrase(ovrposition, new Font(Font.FontFamily.HELVETICA, 9, Font.NORMAL, BaseColor.BLACK));
                        PdfPCell cell2c = new PdfPCell(ph2c);
                        cell2c.setColspan(3);
                        tabb.addCell(cell2c);

                        Phrase ph3d = new Phrase(ovroutof, new Font(Font.FontFamily.HELVETICA, 9, Font.NORMAL, BaseColor.BLACK));
                        PdfPCell cell3d = new PdfPCell(ph3d);
                        cell3d.setColspan(3);
                        tabb.addCell(cell3d);

                        Phrase ph4a = new Phrase("", new Font(Font.FontFamily.HELVETICA, 7, Font.NORMAL, BaseColor.BLACK));
                        PdfPCell cell4a = new PdfPCell(ph4a);
                        cell4a.setColspan(10);
                        cell4a.setRowspan(2);
                        tabb.addCell(cell4a);

                    }

                    {
                        ///Real last term Positions
                        Phrase ph4 = new Phrase("LAST TERM", new Font(Font.FontFamily.HELVETICA, 11, Font.NORMAL, BaseColor.BLACK));
                        PdfPCell cell4 = new PdfPCell(ph4);
                        cell4.setColspan(4);
                        tabb.addCell(cell4);

                        Phrase ph2a = new Phrase(streampositionlastterm, new Font(Font.FontFamily.HELVETICA, 9, Font.NORMAL, BaseColor.BLACK));
                        PdfPCell cell2a = new PdfPCell(ph2a);
                        cell2a.setColspan(3);
                        tabb.addCell(cell2a);

                        Phrase ph3a = new Phrase(streampositionlasttermoutof, new Font(Font.FontFamily.HELVETICA, 9, Font.NORMAL, BaseColor.BLACK));
                        PdfPCell cell3a = new PdfPCell(ph3a);
                        cell3a.setColspan(3);
                        tabb.addCell(cell3a);

                        Phrase ph2c = new Phrase(classpositionlastterm, new Font(Font.FontFamily.HELVETICA, 9, Font.NORMAL, BaseColor.BLACK));
                        PdfPCell cell2c = new PdfPCell(ph2c);
                        cell2c.setColspan(3);
                        tabb.addCell(cell2c);

                        Phrase ph3d = new Phrase(classpositionlasttermoutof, new Font(Font.FontFamily.HELVETICA, 9, Font.NORMAL, BaseColor.BLACK));
                        PdfPCell cell3d = new PdfPCell(ph3d);
                        cell3d.setColspan(3);
                        tabb.addCell(cell3d);

                    }

                    {
                        //peformance History cells .....

                        Phrase ph1 = new Phrase("Student's  Performance History Summary", new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL, BaseColor.BLACK));
                        PdfPCell cell1 = new PdfPCell(ph1);
                        cell1.setBorder(PdfPCell.NO_BORDER);
                        cell1.setVerticalAlignment(Element.ALIGN_CENTER);
                        cell1.setColspan(26);
                        tabb.addCell(cell1);

                        for (int j = 0; j < 4; j++) {

                            for (int k = 0; k < 13; k++) {
                                String content = String.valueOf(performance[j][k]).toUpperCase();
                                if (content.equalsIgnoreCase("null")) {
                                    content = "";
                                }

                                Phrase ph2 = new Phrase(content, new Font(Font.FontFamily.HELVETICA, 9, Font.NORMAL, BaseColor.BLACK));
                                PdfPCell cell2 = new PdfPCell(ph2);
                                cell2.setColspan(2);
                                tabb.addCell(cell2);

                            }
                        }

                    }

//        incase Graph is Needed Uncomment This Code            {
//                        DefaultCategoryDataset dataset=new DefaultCategoryDataset();
//                        String kcpemarks=Globals.kcpeMarks(adm);
//                        int avg=0;
//                        String kcpegrade="";
//                        int kcpepoints=0;
//                        
//                            if(kcpemarks.isEmpty())
//                      {
//                          
//                      }
//                      else{
//                          int TOTALmarks=Integer.parseInt(kcpemarks);
//                        avg=TOTALmarks/5;
//                           String sqll = "Select grade from kcpetable where  '" + avg + "'>=starting_from and '" + avg + "'<=ending_at  group by sort_code";
//                                ps = con.prepareStatement(sqll);
//                                ResultSet RS = ps.executeQuery();
//                                if (RS.next()) {
//
//                                   kcpegrade=(RS.getString("grade"));
//                                    String qq = "Select points from points_for_each_grade where grade='" + RS.getString("grade") + "'";
//                                    ps = con.prepareStatement(qq);
//                                    RS = ps.executeQuery();
//                                    if (RS.next()) {
//                                       kcpepoints=(RS.getInt("points"));
//                                    }
//                                }
//
//                        
//                      } 
//                        
//                     dataset.setValue(kcpepoints, "Grade", "KCPE");
//                      
//                    
//                      
//                      double p=0;
//                            
//                                 for(int k=1;k<13;k++)
//                    {
//                        String content=String.valueOf(performance[2][k]).toUpperCase();
//                        if(content.equalsIgnoreCase("null"))
//                        {
//                            content="";
//                            p=0;
//                        }
//                        else{
//                            p=Double.parseDouble(content);
//                            
//                            
//                        }
//                        dataset.setValue(p, "Points", String.valueOf(performance[0][k]));
//                          
//                        System.err.println(performance[0][k]);
//                        }
//                      
//                        JFreeChart chart=ChartFactory.createBarChart("Grade", "Period", "Points", dataset,PlotOrientation.VERTICAL,false,true,false);
//                        CategoryPlot plot=chart.getCategoryPlot();
//                        plot.setRangeGridlinePaint(Color.black);
//                        final ChartRenderingInfo renderer=new ChartRenderingInfo(new StandardEntityCollection());
//                        final File file=new File("C:\\schoolData\\"+adm+"chat.png");
//                        ChartUtilities.saveChartAsPNG(file, chart, 1000, 200, renderer);
//                         Image img =     img = Image.getInstance("C:\\schoolData\\"+adm+"chat.png");
//                         
//                        PdfPCell cell2 = new PdfPCell(img,true);
//                        cell2.setColspan(26);
//                        cell2.setRowspan(2);
//                        tabb.addCell(cell2);
//                        
//                    }
                    {
                        //fee cells
                        Phrase ph2 = new Phrase("FEES\n", new Font(Font.FontFamily.HELVETICA, 15, Font.NORMAL, BaseColor.BLACK));
                        PdfPCell cell2 = new PdfPCell(ph2);
                        cell2.setColspan(4);

                        tabb.addCell(cell2);

                        Phrase ph3 = new Phrase("NEXT TERM FEES\n\nKsh. " + nextTermFee + "\n", new Font(Font.FontFamily.HELVETICA, 11, Font.NORMAL, BaseColor.BLACK));
                        PdfPCell cell3 = new PdfPCell(ph3);
                        cell3.setColspan(5);

                        tabb.addCell(cell3);

                        Phrase ph4 = new Phrase("OUTSTANDING BALANCE\n\n\nKsh. " + Globals.balanceCalculator(adm), new Font(Font.FontFamily.HELVETICA, 11, Font.NORMAL, BaseColor.BLACK));
                        PdfPCell cell4 = new PdfPCell(ph4);

                        cell4.setColspan(5);
                        tabb.addCell(cell4);
                        double payable = nextTermFee + Globals.balanceCalculator(adm);
                        Phrase ph5 = new Phrase("FEES PAYABLE\n\n\nKsh. " + payable, new Font(Font.FontFamily.HELVETICA, 11, Font.NORMAL, BaseColor.BLACK));
                        PdfPCell cell5 = new PdfPCell(ph5);
                        cell5.setColspan(5);
                        tabb.addCell(cell5);

                        Phrase ph6 = new Phrase("", new Font(Font.FontFamily.HELVETICA, 7, Font.NORMAL, BaseColor.BLACK));
                        PdfPCell cell6 = new PdfPCell(ph6);
                        cell6.setColspan(7);

                        tabb.addCell(cell6);
                    }

                    {//class teacher comment and principals comment

                        Phrase ph2 = new Phrase("CLASS TEACHER'S COMMENTS: " + rs.getString("classteachergeneralcomment"), new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.ITALIC, BaseColor.BLACK));
                        PdfPCell cell2 = new PdfPCell(ph2);
                        cell2.setColspan(26);

                        tabb.addCell(cell2);

                        Phrase ph3 = new Phrase("PRINCIPALS COMMENT'S: " + rs.getString("principalscomment"), new Font(Font.FontFamily.HELVETICA, 10, Font.ITALIC, BaseColor.BLACK));
                        PdfPCell cell3 = new PdfPCell(ph3);
                        cell3.setColspan(26);

                        tabb.addCell(cell3);
                        DocHead head = new DocHead();
                        Image img = head.principalSign();
                        Phrase ph4 = new Phrase("PRINCIPAL'S SIGNATURE/STAMP: ", new Font(Font.FontFamily.HELVETICA, 10, Font.ITALIC, BaseColor.BLACK));
                        PdfPCell cell4 = new PdfPCell(ph4);
                        cell4.setColspan(13);

                        tabb.addCell(cell4);
                        PdfPCell cell5 = new PdfPCell(img, true);
                        cell5.setColspan(13);
                        cell5.setFixedHeight(18);

                        tabb.addCell(cell5);

                    }
                    {
                        //term dates cells....

                        Phrase ph2 = new Phrase("NEXT TERM BEGINS AT.. : " + termOpeningDate, new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.NORMAL, BaseColor.BLACK));
                        PdfPCell cell2 = new PdfPCell(ph2);
                        cell2.setColspan(26);

                        tabb.addCell(cell2);

                        // This Report Card was issued without any erasure or alterations whatsoever               
                    }
                    {
                        //parents signature celll
                        Phrase ph2 = new Phrase("PARENT/GUARDIAN'S SIGNATURE:.......................................", new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.NORMAL, BaseColor.BLACK));
                        PdfPCell cell2 = new PdfPCell(ph2);
                        cell2.setColspan(26);

                        tabb.addCell(cell2);
                    }
                    {
                        //DECLARITION CELL
                        Phrase ph2 = new Phrase("This Report Card was isued without any erasure or alterations whatsoever", new Font(Font.FontFamily.TIMES_ROMAN, 11, Font.ITALIC, BaseColor.BLACK));
                        PdfPCell cell2 = new PdfPCell(ph2);
                        cell2.setColspan(26);

                        tabb.addCell(cell2);
                    }

                    tabb.setSpacingBefore(30);
                    tabb.setWidthPercentage(100);

                    doc.add(tabb);

                }

                doc.close();
                if (rs.previous()) {
                    if (ConfigurationIntialiser.docOpener()) {
                        if (dialog == null) {

                            dialog = ReportFrame.getReportFrameInstance(new File("C:\\schoolData\\Reportforms.pdf"));

                        } else {
                            dialog = null;
                            dialog = ReportFrame.getReportFrameInstance(new File("C:\\schoolData\\Reportforms.pdf"));
                            dialog.setVisible(true);

                        }

                    } else {

                        Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler " + "C:\\schoolData\\Reportforms.pdf");

                    }
                } else {
                    JOptionPane.showMessageDialog(null, "No Report Forms Were  Found For This Exam,\n Kindly Analyse The Exam And Try To Regenerate Report Forms If Marks Have Been Entered");
                }

            } catch (Exception sq) {
                JOptionPane.showMessageDialog(null, sq.getMessage());
            }

        }

    }

}
