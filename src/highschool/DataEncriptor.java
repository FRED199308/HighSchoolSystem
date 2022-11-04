/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package highschool;

/**
 * @author FRED
 */
public class DataEncriptor {


    private static int adder[] = {1, 2, 2, 3, 3, 4, 4, 5, 5, 6, 6, 7, 7, 8, 8, 9, 9, 0, 0};

    public static String encript(String name) {
        String result = "";
        char ch;
        int ck = 0;
        int length = name.length();
        for (int i = 0; i < length; ++i) {
            if (ck >= adder.length - 1) {
                ck = 0;

            }

            ch = name.charAt(i);
            ch += adder[ck];
            result += ch;
            ck++;

        }
        return result;
    }

    public static String decriptor(String name) {
        String result = "";
        char ch;
        int ck = 0;
        int length = name.length();
        for (int i = 0; i < length; ++i) {
            if (ck >= adder.length - 1) ck = 0;
            ch = name.charAt(i);
            ch -= adder[ck];
            result += ch;
            ck++;

        }
        return result;


    }

    public static void main(String[] args) {
        DataEncriptor one = new DataEncriptor();

        System.out.println(DataEncriptor.encript("admin"));
        ;

    }
}
