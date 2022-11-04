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
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.JDialog;

/**
 * @author ExamSeverPc
 */
public class CashBookGenerator {

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

    public static void generateReport(String accountName, String upperDate, String lowerDate) {

        try {
            Document doc = new Document();
            com.itextpdf.text.Image image = com.itextpdf.text.Image.getInstance("C:/schooldata/logo.jpg");
            PdfWriter writer = PdfWriter.getInstance(doc, new FileOutputStream("C:\\schoolData\\" + accountName + "CashBook.pdf"));
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

            doc.add(tab);

            Paragraph pr5 = new Paragraph("Cash Book :".toUpperCase() + accountName.toUpperCase(), FontFactory.getFont(FontFactory.HELVETICA, 13, java.awt.Font.BOLD, BaseColor.BLACK));

            pr5.setIndentationLeft(150);
            pr5.setSpacingAfter(20);
            doc.add(pr5);

            PdfPTable tabb = new PdfPTable(35);

            Phrase pha = new Phrase("Cash Book Balances As At From " + lowerDate + " To " + upperDate, new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL, BaseColor.BLACK));
            PdfPCell cella = new PdfPCell(pha);
            cella.setColspan(23);

            Phrase phb = new Phrase("Receipt".toUpperCase(), new Font(Font.FontFamily.HELVETICA, 13, Font.NORMAL, BaseColor.BLACK));
            PdfPCell cellb = new PdfPCell(phb);
            cellb.setColspan(6);

            Phrase phc = new Phrase("Payments".toUpperCase(), new Font(Font.FontFamily.HELVETICA, 13, Font.NORMAL, BaseColor.BLACK));
            PdfPCell cellc = new PdfPCell(phc);
            cellc.setColspan(6);
            tabb.addCell(cella);
            tabb.addCell(cellb);
            tabb.addCell(cellc);
            {

                Phrase ph1 = new Phrase("Date", new Font(Font.FontFamily.HELVETICA, 9, Font.NORMAL, BaseColor.BLACK));
                PdfPCell cell1 = new PdfPCell(ph1);
                cell1.setColspan(3);
                tabb.addCell(cell1);
                Phrase ph2 = new Phrase("VoteHead No.", new Font(Font.FontFamily.HELVETICA, 9, Font.NORMAL, BaseColor.BLACK));
                PdfPCell cell2 = new PdfPCell(ph2);
                cell2.setColspan(3);
                tabb.addCell(cell2);
                Phrase ph3 = new Phrase("Particulars", new Font(Font.FontFamily.HELVETICA, 9, Font.NORMAL, BaseColor.BLACK));
                PdfPCell cell3 = new PdfPCell(ph3);
                cell3.setColspan(8);
                tabb.addCell(cell3);

                Phrase ph4 = new Phrase("VoteHead", new Font(Font.FontFamily.HELVETICA, 9, Font.NORMAL, BaseColor.BLACK));
                PdfPCell cell4 = new PdfPCell(ph4);
                cell4.setColspan(6);
                tabb.addCell(cell4);
                Phrase ph5 = new Phrase("Ref No.", new Font(Font.FontFamily.HELVETICA, 9, Font.NORMAL, BaseColor.BLACK));
                PdfPCell cell5 = new PdfPCell(ph5);
                cell5.setColspan(3);
                tabb.addCell(cell5);

                Phrase ph6 = new Phrase("Cash (Kshs)", new Font(Font.FontFamily.HELVETICA, 9, Font.NORMAL, BaseColor.BLACK));
                PdfPCell cell6 = new PdfPCell(ph6);
                cell6.setColspan(3);
                tabb.addCell(cell6);

                Phrase ph7 = new Phrase("Bank (Kshs)", new Font(Font.FontFamily.HELVETICA, 9, Font.NORMAL, BaseColor.BLACK));
                PdfPCell cell7 = new PdfPCell(ph7);
                cell7.setColspan(3);
                tabb.addCell(cell7);

                Phrase ph9 = new Phrase("Cash (Kshs)", new Font(Font.FontFamily.HELVETICA, 9, Font.NORMAL, BaseColor.BLACK));
                PdfPCell cell9 = new PdfPCell(ph9);
                cell9.setColspan(3);
                tabb.addCell(cell9);

                Phrase ph10 = new Phrase("Bank (Kshs)", new Font(Font.FontFamily.HELVETICA, 9, Font.NORMAL, BaseColor.BLACK));
                PdfPCell cell10 = new PdfPCell(ph10);
                cell10.setColspan(3);
                tabb.addCell(cell10);

            }
            SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
            con = DbConnection.connectDb();
            String cashcredit = "", cashdebit = "", bankdebit = "", bankcredit = "";
            ps = con.prepareStatement("Select * from cashbookanalysis where year(Date)='" + Globals.academicYear() + "' and accountid='" + Globals.AccountCode(accountName) + "' and date>='" + lowerDate + "' and date<='" + upperDate + "'order by date asc");
            rs = ps.executeQuery();
            while (rs.next()) {

                String mode = "", Nature = "";
                mode = rs.getString("Mode");
                Nature = rs.getString("Nature");
                if (Nature.equalsIgnoreCase("DEP") && mode.equalsIgnoreCase("Cash")) {
                    cashdebit = rs.getString("Amount");

                } else {
                    cashcredit = "";
                    bankcredit = "";
                    bankdebit = "";

                    if (Nature.equalsIgnoreCase("WD") && mode.equalsIgnoreCase("Cash")) {

                        cashcredit = rs.getString("Amount");

                    } else {
                        bankcredit = "";
                        bankdebit = "";
                        cashdebit = "";
                        if (Nature.equalsIgnoreCase("DEP") && mode.equalsIgnoreCase("Bank")) {

                            bankdebit = rs.getString("Amount");
                            ;

                        } else {
                            cashdebit = "";
                            cashcredit = "";
                            bankcredit = "";

                            if (Nature.equalsIgnoreCase("WD") && mode.equalsIgnoreCase("Bank")) {

                                bankcredit = rs.getString("Amount");
                                ;

                            } else {
                                cashdebit = "";
                                cashcredit = "";
                                bankdebit = "";
                            }

                        }

                    }

                }

                Phrase ph1 = new Phrase(format.format(rs.getDate("Date")), new Font(Font.FontFamily.HELVETICA, 7, Font.NORMAL, BaseColor.BLACK));
                PdfPCell cell1 = new PdfPCell(ph1);
                cell1.setColspan(3);
                tabb.addCell(cell1);
                Phrase ph2 = new Phrase(rs.getString("VoteHeadId"), new Font(Font.FontFamily.HELVETICA, 7, Font.NORMAL, BaseColor.BLACK));
                PdfPCell cell2 = new PdfPCell(ph2);
                cell2.setColspan(3);
                tabb.addCell(cell2);
                Phrase ph3 = new Phrase(rs.getString("ItemName"), new Font(Font.FontFamily.HELVETICA, 7, Font.NORMAL, BaseColor.BLACK));
                PdfPCell cell3 = new PdfPCell(ph3);
                cell3.setColspan(8);
                tabb.addCell(cell3);

                Phrase ph4 = new Phrase(Globals.VoteHeadName(rs.getString("VoteHeadId")), new Font(Font.FontFamily.HELVETICA, 7, Font.NORMAL, BaseColor.BLACK));
                PdfPCell cell4 = new PdfPCell(ph4);
                cell4.setColspan(6);
                tabb.addCell(cell4);
                Phrase ph5 = new Phrase(rs.getString("id"), new Font(Font.FontFamily.HELVETICA, 7, Font.NORMAL, BaseColor.BLACK));
                PdfPCell cell5 = new PdfPCell(ph5);
                cell5.setColspan(3);
                tabb.addCell(cell5);

                Phrase ph6 = new Phrase(cashdebit, new Font(Font.FontFamily.HELVETICA, 7, Font.NORMAL, BaseColor.BLACK));
                PdfPCell cell6 = new PdfPCell(ph6);
                cell6.setColspan(3);
                tabb.addCell(cell6);

                Phrase ph7 = new Phrase(bankdebit, new Font(Font.FontFamily.HELVETICA, 7, Font.NORMAL, BaseColor.BLACK));
                PdfPCell cell7 = new PdfPCell(ph7);
                cell7.setColspan(3);
                tabb.addCell(cell7);

                Phrase ph9 = new Phrase(cashcredit, new Font(Font.FontFamily.HELVETICA, 7, Font.NORMAL, BaseColor.BLACK));
                PdfPCell cell9 = new PdfPCell(ph9);
                cell9.setColspan(3);
                tabb.addCell(cell9);

                Phrase ph10 = new Phrase(bankcredit, new Font(Font.FontFamily.HELVETICA, 7, Font.NORMAL, BaseColor.BLACK));
                PdfPCell cell10 = new PdfPCell(ph10);
                cell10.setColspan(3);
                tabb.addCell(cell10);

            }

            tabb.setWidthPercentage(100);

            doc.add(tabb);

            doc.close();

            if (ConfigurationIntialiser.docOpener()) {
                if (dialog == null) {

                    dialog = ReportFrame.getReportFrameInstance(new File("C:\\schoolData\\" + accountName + "CashBook.pdf"));

                } else {
                    dialog = null;
                    dialog = ReportFrame.getReportFrameInstance(new File("C:\\schoolData\\" + accountName + "Cashbook.pdf"));
                    dialog.setVisible(true);

                }

            } else {

                Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler " + "C:\\schoolData\\" + accountName + "CashBook.pdf");

            }

        } catch (Exception sq) {
            sq.printStackTrace();
        }

    }

}
