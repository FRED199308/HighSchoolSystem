/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package highschool;

import com.toedter.calendar.JCalendar;

import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileOutputStream;
import java.io.IOException;

import static java.lang.Thread.sleep;

import java.sql.Blob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import javax.swing.ImageIcon;

import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JWindow;
import javax.swing.border.TitledBorder;

import net.miginfocom.swing.MigLayout;

/**
 * @author FRED
 */
public class HomePanel implements ActionListener {
    public static FredJMenuItem logout;
    private JPanel holder, right, left;
    FredJMenuItem changepass = new FredJMenuItem("Change Password");
    FredLabel photoholder = new FredLabel();
    FredLabel animation = new FredLabel();
    private PreparedStatement ps;
    private ResultSet rs;
    private Connection con = null;
    private ImageIcon iimage;
    FredLabel bal = new FredLabel("Status");

    FredLabel status = new FredLabel("Balance");
    JPanel gatewayInfor = new JPanel();
    JPanel panel = new JPanel();

    FredJMenuItem mod = new FredJMenuItem("Switch Module");

    public JPanel rightPanel() {
        con = DbConnection.connectDb();
        JCalendar calendar = new JCalendar();
        JMenuBar bar = new JMenuBar();
        FredJMenu menu = new FredJMenu("My Account");
        menu.setFont(new Font("serif", Font.TRUETYPE_FONT, 18));
        logout = new FredJMenuItem("Log Out");
        bar.add(menu);
        menu.add(changepass);
        menu.add(logout);

        menu.add(mod);
        holder = new JPanel();
        panel.setLayout(new MigLayout());
        holder.setLayout(new MigLayout());
        right = new JPanel();
        right.setLayout(new MigLayout());
        left = new JPanel();
        left.setLayout(new MigLayout());
        holder.add(left, "Grow,Push");
        holder.add(right, "growy,pushy");
        panel.add(photoholder, "wrap");

        left.add(calendar, "gapleft 10,gaptop 0,width 100:420:500,height 250:280:300");
        JPanel animationpane = new JPanel();
        animationpane.add(animation);
        // left.setBackground(Color.WHITE);
        // right.setBackground(Color.WHITE);
        right.add(bar, "gaptop 0,gapleft 150,wrap");
        right.add(panel, "width 300:300:300,height 300:300:300,wrap");
        gatewayInfor.setLayout(new MigLayout());
        gatewayInfor.add(status, "wrap");
        gatewayInfor.add(bal);
        //holder.add(pane,"width 200:200:200,height 200:200:200,gapleft 300");
        FredLabel time = new FredLabel("TIME");
        time.setForeground(Color.red);

        time.setFont(new Font("", Font.ROMAN_BASELINE, 18));
        left.add(time, "gapleft 20,gaptop 20,wrap");

        left.add(gatewayInfor, "grow,push");
        right.add(animationpane, "grow,push,width 300:300:300,height 250:250:280");
        Thread clock = new Thread() {
            @Override
            public void run() {
                for (; ; ) {

                    time.setText(new Date().toString());
                    try {
                        sleep(1000);
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }
                }

            }

        };
        clock.start();
        time.setFont(new Font("serif", Font.BOLD, 22));
        try {
            String sql = "Select logo from schooldetails";
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            if (rs.next()) {
                Blob imm = rs.getBlob("logo");
                String imagename = "logo";
                FileOutputStream fos = new FileOutputStream("C:/schooldata/logo.jpg");
                int length = (int) imm.length();
                byte[] bf = imm.getBytes(1, length);
                fos.write(bf, 0, length);

            }
        } catch (IOException | SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                con.close();
            } catch (SQLException sq) {
                sq.printStackTrace();
            }
        }

        photoholder.setIcon(resizeImage("C:\\schoolData/logo.jpg"));
        iimage = new ImageIcon(getClass().getResource("/icons/giphy (6).gif"));
        Image image = iimage.getImage();
        ImageIcon icon = new ImageIcon(image);
        animation.setIcon(icon);
        logout.addActionListener(this);
        changepass.addActionListener(this);
        mod.addActionListener(this);

        if (!ConfigurationIntialiser.smsOfflineSender()) {
            new Thread() {

                public void run() {


                    for (; ; ) {
                        try {

                            if (MessageGateway.isComputerOffline()) {
                                status.setText("Computer Status: Online");
                                status.setForeground(Color.MAGENTA);
                            } else {
                                status.setText("Computer Status: Offline");
                                status.setForeground(Color.RED);
                            }
                            sleep(1000);
                        } catch (Exception e) {
                        }
                    }
                }
            }.start();
            new Thread() {

                public void run() {


                    for (; ; ) {
                        try {

                            String bala = MessageGateway.gatewayBalance();

                            bal.setText("Balance:" + bala);
                            sleep(10000);

                        } catch (Exception e) {
                        }
                    }
                }
            }.start();
        }
        return holder;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object obj = e.getSource();
        if (obj == logout) {
            try {
                con.close();
            } catch (SQLException sq) {

            }
            Login.firstLaunch = false;
            CurrentFrame.killSecondFrame();

            CurrentFrame.currentWindow();
            CurrentFrame.mainFrame().dispose();
            new Login();
            FinanceHome.timeOutChecker.stop();
        } else if (obj == mod) {

            new Thread() {
                @Override
                public void run() {

                    JWindow window = new JWindow();
                    JProgressBar bar = new JProgressBar();
                    bar.setIndeterminate(true);

                    window.setSize(300, 60);

                    bar.setBorder(new TitledBorder("Switching Module......."));

                    window.setLocationRelativeTo(CurrentFrame.mainFrame());
                    window.setIconImage(FrameProperties.icon());
                    window.add(bar);
                    window.setVisible(true);

                    if (Globals.moduleName.equalsIgnoreCase("Finance")) {
                        FinanceHome.timeOutChecker.stop();
                        CurrentFrame.ReportDialogue.dispose();
                        CurrentFrame.secondFrame.dispose();
                        Globals.moduleName = "Inventory";
                        CurrentFrame.mainFrame().dispose();
                        new POSHome();

                        window.dispose();
                    }

                    window.dispose();
                }

            }.start();


        } else if (obj == changepass) {
            CurrentFrame.setSecondFrame(new PasswordChange());
            CurrentFrame.mainFrame().setEnabled(false);

        }
    }

    public ImageIcon resizeImage(String path) {

        iimage = new ImageIcon(path);
        Image image = iimage.getImage();


        Image newimage = image.getScaledInstance(280, 220, Image.SCALE_SMOOTH);
        ImageIcon ima = new ImageIcon(newimage);

        return ima;
    }

    public ImageIcon resizeImage2(String path) {

        iimage = new ImageIcon(path);
        Image image = iimage.getImage();


        Image newimage = image.getScaledInstance(400, 330, Image.SCALE_SMOOTH);
        ImageIcon ima = new ImageIcon(newimage);

        return ima;
    }

}
