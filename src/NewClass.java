/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 * @author FRED
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class NewClass {
    public static void main(String[] args) {
        try {
            Runtime rt = Runtime.getRuntime();
            Process proc = rt.exec("C:\\Users\\FRED\\Desktop\\New Text Document.bat");
            System.err.println(proc.getInputStream());


            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(proc.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
            reader.close();
            int exitVal = proc.waitFor();
            if (exitVal != 0) {
                System.out.println("Abnormal Behaviour! Something bad happened.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
