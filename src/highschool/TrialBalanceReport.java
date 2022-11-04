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
import com.itextpdf.text.FontFactory;
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
import java.text.SimpleDateFormat;
import java.time.Month;
import java.util.Date;
import javax.swing.JDialog;

/**
 * @author ExamSeverPc
 */
public class TrialBalanceReport {

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

    public static void generateReport(String accountName, String month, String accountingYear) {

        try {
            double spentAmount = 0.0, balanceAmount = 0.0, availed = 0.0, storedbal = 0.0;
            con = DbConnection.connectDb();

            SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
            Document doc = new Document();
            com.itextpdf.text.Image image = com.itextpdf.text.Image.getInstance("C:/schooldata/logo.jpg");
            PdfWriter writer = PdfWriter.getInstance(doc, new FileOutputStream("C:\\schoolData\\" + accountName + " trialBalance.pdf"));
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
            double creditBalance = 0.0, debitBalance = 0.0;


            Paragraph pr1 = new Paragraph("TRIAL BALANCE " + accountName, FontFactory.getFont(FontFactory.TIMES, 15, java.awt.Font.BOLD, BaseColor.BLACK));
            pr1.setIndentationLeft(100);
            doc.add(pr1);
            Paragraph pr2 = new Paragraph("Trial Balance for the Month of  " + Month.of(Integer.parseInt(month)) + " Accounting period :" + accountingYear, FontFactory.getFont(FontFactory.TIMES, 13, java.awt.Font.BOLD, BaseColor.BLACK));
            pr2.setIndentationLeft(100);
            doc.add(pr2);

            PdfPTable tabb = new PdfPTable(38);

            {


                Phrase ph1 = new Phrase("VoteHead Details", new Font(Font.FontFamily.HELVETICA, 9, Font.NORMAL, BaseColor.BLACK));
                PdfPCell cell1 = new PdfPCell(ph1);
                cell1.setColspan(8);
                tabb.addCell(cell1);
                Phrase ph2 = new Phrase("L.F", new Font(Font.FontFamily.HELVETICA, 9, Font.NORMAL, BaseColor.BLACK));
                PdfPCell cell2 = new PdfPCell(ph2);
                cell2.setColspan(2);
                tabb.addCell(cell2);
                Phrase ph3 = new Phrase("Budget", new Font(Font.FontFamily.HELVETICA, 9, Font.NORMAL, BaseColor.BLACK));
                PdfPCell cell3 = new PdfPCell(ph3);
                cell3.setColspan(7);
                tabb.addCell(cell3);

                Phrase ph4 = new Phrase("DR  (Kshs.)", new Font(Font.FontFamily.HELVETICA, 9, Font.NORMAL, BaseColor.BLACK));
                PdfPCell cell4 = new PdfPCell(ph4);
                cell4.setColspan(7);
                tabb.addCell(cell4);
                Phrase ph5 = new Phrase("CR  (Kshs.)", new Font(Font.FontFamily.HELVETICA, 9, Font.NORMAL, BaseColor.BLACK));
                PdfPCell cell5 = new PdfPCell(ph5);
                cell5.setColspan(7);
                tabb.addCell(cell5);

                Phrase ph6 = new Phrase("Balance  (Kshs.)", new Font(Font.FontFamily.HELVETICA, 9, Font.NORMAL, BaseColor.BLACK));
                PdfPCell cell6 = new PdfPCell(ph6);
                cell6.setColspan(7);
                tabb.addCell(cell6);
            }
            String accid = Globals.AccountCode(accountName);
            // trying to retrieve previous accounting cycles
            String previousperiod = accountingYear + "-" + month + "-" + 01;
            System.out.println(previousperiod);
            spentAmount = 0.0;
            balanceAmount = 0.0;
            availed = 0.0;

            ps = con.prepareStatement("Select sum(amount) from cashbookanalysis where accountid='" + accid + "' and date<'" + previousperiod + "' and nature='" + "DEP" + "'");
            ResultSet rx = ps.executeQuery();
            if (rx.next()) {
                availed = rx.getDouble("sum(amount)");
                creditBalance += rx.getDouble("sum(amount)");
            }

            ps = con.prepareStatement("Select sum(amount) from cashbookanalysis where accountid='" + accid + "' and date<'" + previousperiod + "'  and nature='" + "WD" + "'");
            rx = ps.executeQuery();
            if (rx.next()) {
                spentAmount = rx.getDouble("sum(amount)");
                debitBalance += rx.getDouble("sum(amount)");
            }
            balanceAmount = availed - spentAmount;


            {
                Phrase ph1 = new Phrase("Opening Bal.", new Font(Font.FontFamily.HELVETICA, 9, Font.NORMAL, BaseColor.BLACK));
                PdfPCell cell1 = new PdfPCell(ph1);
                cell1.setColspan(8);
                tabb.addCell(cell1);
                Phrase ph2 = new Phrase("", new Font(Font.FontFamily.HELVETICA, 9, Font.NORMAL, BaseColor.BLACK));
                PdfPCell cell2 = new PdfPCell(ph2);
                cell2.setColspan(2);
                tabb.addCell(cell2);
                Phrase ph3 = new Phrase("", new Font(Font.FontFamily.HELVETICA, 9, Font.NORMAL, BaseColor.BLACK));
                PdfPCell cell3 = new PdfPCell(ph3);
                cell3.setColspan(7);
                tabb.addCell(cell3);

                Phrase ph4 = new Phrase(String.valueOf(spentAmount), new Font(Font.FontFamily.HELVETICA, 9, Font.NORMAL, BaseColor.BLACK));
                PdfPCell cell4 = new PdfPCell(ph4);
                cell4.setColspan(7);
                tabb.addCell(cell4);
                Phrase ph5 = new Phrase(String.valueOf(availed), new Font(Font.FontFamily.HELVETICA, 9, Font.NORMAL, BaseColor.BLACK));
                PdfPCell cell5 = new PdfPCell(ph5);
                cell5.setColspan(7);
                tabb.addCell(cell5);

                Phrase ph6 = new Phrase(String.valueOf(balanceAmount), new Font(Font.FontFamily.HELVETICA, 9, Font.NORMAL, BaseColor.BLACK));
                PdfPCell cell6 = new PdfPCell(ph6);
                cell6.setColspan(7);
                tabb.addCell(cell6);
            }


            ///end of previous accounting cycle 
            // start of the requested month
            String sql = "Select Distinct voteheadid from cashbookanalysis where accountid='" + accid + "' ";
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                String vid = rs.getString("voteheadid");
                spentAmount = 0.0;
                balanceAmount = 0.0;
                availed = 0.0;

                ps = con.prepareStatement("Select sum(amount) from cashbookanalysis where accountid='" + accid + "' and voteheadid='" + vid + "'  and nature='" + "DEP" + "' and year(date)='" + accountingYear + "' and month(date)='" + month + "'");
                rx = ps.executeQuery();
                if (rx.next()) {
                    availed = rx.getDouble("Sum(amount)");
                    creditBalance += rx.getDouble("Sum(amount)");
                }

                ps = con.prepareStatement("Select sum(amount) from cashbookanalysis where accountid='" + accid + "' and voteheadid='" + vid + "'  and nature='" + "WD" + "' and year(date)='" + accountingYear + "' and month(date)='" + month + "'");
                rx = ps.executeQuery();
                if (rx.next()) {
                    spentAmount = rx.getDouble("Sum(amount)");
                    debitBalance += rx.getDouble("Sum(amount)");
                }
                balanceAmount = availed - spentAmount;


                Phrase ph1 = new Phrase(Globals.VoteHeadName(vid), new Font(Font.FontFamily.HELVETICA, 9, Font.NORMAL, BaseColor.BLACK));
                PdfPCell cell1 = new PdfPCell(ph1);
                cell1.setColspan(8);
                tabb.addCell(cell1);
                Phrase ph2 = new Phrase("", new Font(Font.FontFamily.HELVETICA, 9, Font.NORMAL, BaseColor.BLACK));
                PdfPCell cell2 = new PdfPCell(ph2);
                cell2.setColspan(2);
                tabb.addCell(cell2);
                Phrase ph3 = new Phrase("", new Font(Font.FontFamily.HELVETICA, 9, Font.NORMAL, BaseColor.BLACK));
                PdfPCell cell3 = new PdfPCell(ph3);
                cell3.setColspan(7);
                tabb.addCell(cell3);

                Phrase ph4 = new Phrase(String.valueOf(spentAmount), new Font(Font.FontFamily.HELVETICA, 9, Font.NORMAL, BaseColor.BLACK));
                PdfPCell cell4 = new PdfPCell(ph4);
                cell4.setColspan(7);
                tabb.addCell(cell4);
                Phrase ph5 = new Phrase(String.valueOf(availed), new Font(Font.FontFamily.HELVETICA, 9, Font.NORMAL, BaseColor.BLACK));
                PdfPCell cell5 = new PdfPCell(ph5);
                cell5.setColspan(7);
                tabb.addCell(cell5);

                Phrase ph6 = new Phrase(String.valueOf(balanceAmount), new Font(Font.FontFamily.HELVETICA, 9, Font.NORMAL, BaseColor.BLACK));
                PdfPCell cell6 = new PdfPCell(ph6);
                cell6.setColspan(7);
                tabb.addCell(cell6);

            }

            if (accid.equalsIgnoreCase("3")) {//bursary votehead indicator cells


                Phrase ph1 = new Phrase("BURSARY", new Font(Font.FontFamily.HELVETICA, 9, Font.NORMAL, BaseColor.BLACK));
                PdfPCell cell1 = new PdfPCell(ph1);
                cell1.setColspan(8);
                tabb.addCell(cell1);
                Phrase ph2 = new Phrase("", new Font(Font.FontFamily.HELVETICA, 9, Font.NORMAL, BaseColor.BLACK));
                PdfPCell cell2 = new PdfPCell(ph2);
                cell2.setColspan(2);
                tabb.addCell(cell2);
                Phrase ph3 = new Phrase("", new Font(Font.FontFamily.HELVETICA, 9, Font.NORMAL, BaseColor.BLACK));
                PdfPCell cell3 = new PdfPCell(ph3);
                cell3.setColspan(7);
                tabb.addCell(cell3);

                Phrase ph4 = new Phrase(String.valueOf(Globals.bursaryPayCalculator()), new Font(Font.FontFamily.HELVETICA, 9, Font.NORMAL, BaseColor.BLACK));
                PdfPCell cell4 = new PdfPCell(ph4);
                cell4.setColspan(7);
                tabb.addCell(cell4);
                Phrase ph5 = new Phrase(String.valueOf(Globals.bursaryPayCalculator()), new Font(Font.FontFamily.HELVETICA, 9, Font.NORMAL, BaseColor.BLACK));
                PdfPCell cell5 = new PdfPCell(ph5);
                cell5.setColspan(7);
                tabb.addCell(cell5);

                Phrase ph6 = new Phrase(String.valueOf(""), new Font(Font.FontFamily.HELVETICA, 9, Font.NORMAL, BaseColor.BLACK));
                PdfPCell cell6 = new PdfPCell(ph6);
                cell6.setColspan(7);
                tabb.addCell(cell6);

                creditBalance += Globals.bursaryPayCalculator();
                debitBalance += Globals.bursaryPayCalculator();
            }

            {
//CLOSING BALANCE HEADERS..........
                Phrase ph1 = new Phrase("CLOSING BALANCES", new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL, BaseColor.BLACK));
                PdfPCell cell1 = new PdfPCell(ph1);
                cell1.setColspan(38);
                tabb.addCell(cell1);

            }


            {///Real Balances Cell
                String currentMonthFilter = "";
                if (month.equalsIgnoreCase("12")) {
                    month = "1";
                    int year = Integer.parseInt(accountingYear) + 1;
                    accountingYear = String.valueOf(year);
                    currentMonthFilter = accountingYear + "-" + Integer.parseInt(month) + "-" + 01;
                } else {
                    currentMonthFilter = accountingYear + "-" + Integer.parseInt(month + 1) + "-" + 01;
                }


                debitBalance += Globals.accountBalanceCalculatorAtADate(accid, currentMonthFilter);
                Phrase ph1 = new Phrase("Cash Available".toUpperCase(), new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL, BaseColor.BLACK));
                PdfPCell cell1 = new PdfPCell(ph1);
                cell1.setColspan(8);
                tabb.addCell(cell1);
                Phrase ph2 = new Phrase("", new Font(Font.FontFamily.HELVETICA, 9, Font.NORMAL, BaseColor.BLACK));
                PdfPCell cell2 = new PdfPCell(ph2);
                cell2.setColspan(2);
                tabb.addCell(cell2);
                Phrase ph3 = new Phrase("", new Font(Font.FontFamily.HELVETICA, 9, Font.NORMAL, BaseColor.BLACK));
                PdfPCell cell3 = new PdfPCell(ph3);
                cell3.setColspan(7);
                tabb.addCell(cell3);

                Phrase ph4 = new Phrase(String.valueOf(Globals.accountBalanceCalculatorAtADate(accid, currentMonthFilter)), new Font(Font.FontFamily.HELVETICA, 9, Font.NORMAL, BaseColor.BLACK));
                PdfPCell cell4 = new PdfPCell(ph4);
                cell4.setColspan(7);
                tabb.addCell(cell4);
                Phrase ph5 = new Phrase("", new Font(Font.FontFamily.HELVETICA, 9, Font.NORMAL, BaseColor.BLACK));
                PdfPCell cell5 = new PdfPCell(ph5);
                cell5.setColspan(7);
                tabb.addCell(cell5);

                Phrase ph6 = new Phrase("", new Font(Font.FontFamily.HELVETICA, 9, Font.NORMAL, BaseColor.BLACK));
                PdfPCell cell6 = new PdfPCell(ph6);
                cell6.setColspan(7);
                tabb.addCell(cell6);
            }

            {//Balancing figure cells


                Phrase ph1 = new Phrase("Balancing Figure".toUpperCase(), new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL, BaseColor.BLACK));
                PdfPCell cell1 = new PdfPCell(ph1);
                cell1.setColspan(8);
                tabb.addCell(cell1);
                Phrase ph2 = new Phrase("", new Font(Font.FontFamily.HELVETICA, 9, Font.NORMAL, BaseColor.BLACK));
                PdfPCell cell2 = new PdfPCell(ph2);
                cell2.setColspan(2);
                tabb.addCell(cell2);
                Phrase ph3 = new Phrase("", new Font(Font.FontFamily.HELVETICA, 9, Font.NORMAL, BaseColor.BLACK));
                PdfPCell cell3 = new PdfPCell(ph3);
                cell3.setColspan(7);
                tabb.addCell(cell3);

                Phrase ph4 = new Phrase(String.valueOf(debitBalance), new Font(Font.FontFamily.HELVETICA, 9, Font.NORMAL, BaseColor.BLACK));
                PdfPCell cell4 = new PdfPCell(ph4);
                cell4.setColspan(7);
                tabb.addCell(cell4);
                Phrase ph5 = new Phrase(String.valueOf(creditBalance), new Font(Font.FontFamily.HELVETICA, 9, Font.NORMAL, BaseColor.BLACK));
                PdfPCell cell5 = new PdfPCell(ph5);
                cell5.setColspan(7);
                tabb.addCell(cell5);

                Phrase ph6 = new Phrase("", new Font(Font.FontFamily.HELVETICA, 9, Font.NORMAL, BaseColor.BLACK));
                PdfPCell cell6 = new PdfPCell(ph6);
                cell6.setColspan(7);
                tabb.addCell(cell6);

            }
            tabb.setSpacingBefore(20);
            tabb.setWidthPercentage(100);
            doc.add(tabb);
            doc.close();


            if (ConfigurationIntialiser.docOpener()) {
                if (dialog == null) {


                    dialog = ReportFrame.getReportFrameInstance(new File("C:\\schoolData\\" + accountName + " trialBalance.pdf"));


                } else {
                    dialog = null;
                    dialog = ReportFrame.getReportFrameInstance(new File("C:\\schoolData\\" + accountName + " TrialBalance.pdf"));
                    dialog.setVisible(true);


                }

            } else {

                Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler " + "C:\\schoolData\\" + accountName + " Trialbalance.pdf");

            }

        } catch (Exception sq) {
            sq.printStackTrace();
        }


    }

}
