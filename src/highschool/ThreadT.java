/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package highschool;

import static java.lang.Thread.sleep;

/**
 * @author FRED
 */
public class ThreadT {

    private static Thread thread;
    private static int i = 0;

    public ThreadT() {
        thread = new Thread() {
            @Override
            public void run() {
                try {
                    while (i < 100) {
                        System.err.println("value i=" + i);
                        sleep(100);
                        i++;

                    }
                } catch (Exception e) {
                }
            }
        };
    }

    public static void main(String[] args) {
        ThreadT th = new ThreadT();
        th.startthread();
        System.err.println(i);
    }

    public void startthread() {
        thread.start();
    }

}
