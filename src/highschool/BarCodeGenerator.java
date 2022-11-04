/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package highschool;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import javax.imageio.ImageIO;

import org.krysalis.barcode4j.impl.code39.Code39Bean;
import org.krysalis.barcode4j.output.bitmap.BitmapCanvasProvider;
import org.krysalis.barcode4j.tools.UnitConv;

/**
 * @author FRED
 */
public class BarCodeGenerator {

    public static void barcodeGen(String codeNumber) {


        try {
            //Create the barcode bean
            Code39Bean bean = new Code39Bean();

            final int dpi = 150;

            //Configure the barcode generator
            bean.setModuleWidth(UnitConv.in2mm(1.0f / dpi)); //makes the narrow bar 
            //width exactly one pixel
            bean.setWideFactor(3);
            bean.doQuietZone(false);

            //Open output file


            File outputFile = new File("C:\\POS PRO\\out.jpg");


            OutputStream out = new FileOutputStream(outputFile);


            try {
                //Set up the canvas provider for monochrome JPEG output 
                BitmapCanvasProvider canvas = new BitmapCanvasProvider(
                        out, "image/jpeg", dpi, BufferedImage.TYPE_BYTE_BINARY, false, 0);


                //Generate the barcode
                bean.generateBarcode(canvas, codeNumber);

                //Signal end of generation
                canvas.finish();


                BufferedImage inputImage = ImageIO.read(outputFile);
                BufferedImage outputImage = new BufferedImage(1280, 500, inputImage.getType());
                Graphics2D g2d = outputImage.createGraphics();
                g2d.drawImage(inputImage, 0, 0, 1280, 500, null);
                g2d.dispose();
                ImageIO.write(outputImage, "jpg", outputFile);


            } finally {
                out.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }


    public static void productBarcodeGen(String codeNumber) {


        try {
            //Create the barcode bean
            Code39Bean bean = new Code39Bean();

            final int dpi = 150;

            //Configure the barcode generator
            bean.setModuleWidth(UnitConv.in2mm(1.0f / dpi)); //makes the narrow bar 
            //width exactly one pixel
            bean.setWideFactor(3);
            bean.doQuietZone(false);

            //Open output file


            File outputFile = new File("C:\\POS PRO/" + codeNumber + ".jpg");


            OutputStream out = new FileOutputStream(outputFile);


            try {
                //Set up the canvas provider for monochrome JPEG output 
                BitmapCanvasProvider canvas = new BitmapCanvasProvider(
                        out, "image/jpeg", dpi, BufferedImage.TYPE_BYTE_BINARY, false, 0);


                //Generate the barcode
                bean.generateBarcode(canvas, codeNumber);

                //Signal end of generation
                canvas.finish();


                BufferedImage inputImage = ImageIO.read(outputFile);
                BufferedImage outputImage = new BufferedImage(1280, 500, inputImage.getType());
                Graphics2D g2d = outputImage.createGraphics();
                g2d.drawImage(inputImage, 0, 0, 1280, 500, null);
                g2d.dispose();
                ImageIO.write(outputImage, "jpg", outputFile);


            } finally {
                out.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }


}
