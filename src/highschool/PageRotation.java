/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package highschool;

/**
 * @author FRED
 */

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfName;
import com.itextpdf.text.pdf.PdfNumber;
import com.itextpdf.text.pdf.PdfPage;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;


public class PageRotation {
    public static final String DEST = "results/events/rotate_pages.pdf";


    public class Rotate extends PdfPageEventHelper {
        protected PdfNumber rotation = PdfPage.PORTRAIT;

        public void setRotation(PdfNumber rotation) {
            this.rotation = rotation;
        }

        public void onEndPage(PdfWriter writer, Document document) {
            writer.addPageDictEntry(PdfName.ROTATE, rotation);
        }
    }

    public static void main(String[] args) throws IOException, DocumentException {
        File file = new File(DEST);
        file.getParentFile().mkdirs();
        new PageRotation().createPdf(DEST);
    }

    public void createPdf(String filename) throws IOException, DocumentException {
        // step 1
        Document document = new Document();
        // step 2
        PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(filename));
        Rotate rotation = new Rotate();
        writer.setPageEvent(rotation);
        // step 3
        document.open();
        // step 4
        document.add(new Paragraph("Hello World!"));
        document.newPage();
        rotation.setRotation(PdfPage.LANDSCAPE);
        document.add(new Paragraph("Hello World!"));
        document.newPage();
        rotation.setRotation(PdfPage.INVERTEDPORTRAIT);
        document.add(new Paragraph("Hello World!"));
        document.newPage();
        rotation.setRotation(PdfPage.SEASCAPE);
        document.add(new Paragraph("Hello World!"));
        document.newPage();
        rotation.setRotation(PdfPage.PORTRAIT);
        document.add(new Paragraph("Hello World!"));
        // step 5
        document.close();
    }
}