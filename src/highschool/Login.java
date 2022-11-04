/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package highschool;

import java.awt.Color;
import java.awt.Font;
import java.awt.HeadlessException;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import static java.lang.Thread.sleep;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JProgressBar;
import javax.swing.JTextField;
import javax.swing.JWindow;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.TitledBorder;

import net.miginfocom.swing.MigLayout;

/**
 * @author FRED
 */
public class Login extends javax.swing.JFrame {

    /**
     * Creates new form Login
     */
    private Connection con;
    private ResultSet rs;
    private PreparedStatement ps;

    @SuppressWarnings("FieldMayBeFinal")
    private FredLabel user;

    private JButton blog;

    @SuppressWarnings("FieldMayBeFinal")
    private FredLabel pass;
    private JPasswordField jppass;

    private JPanel pan;
    private String dLink;
    private String titleconcat = "";
    private Font small = new Font("Serrif", Font.BOLD, (18));
    private IdGenerator key = new IdGenerator();
    private String status, level;
    public static String KEY;
    private FredLabel lab = new FredLabel();
    private JButton dbConfig;
    private JPanel holder = new JPanel();
    private JButton back = new JButton("Back");
    private JButton save = new JButton("Save");
    private FredLabel dbLoc = new FredLabel("Database Location:");
    private JTextField jdbLoc = new JTextField();
    private JPanel holder2 = new JPanel();
    private JCheckBox loc = new JCheckBox("Use LocalHost");
    public String LINK;
    @SuppressWarnings("FieldMayBeFinal")
    private FredLabel dbUsername;
    private FredLabel dbPassword;
    private JTextField jDbUsername;
    private JPasswordField jDbPassword;
    private JCheckBox rem;
    private boolean activation = false;
    public static boolean firstLaunch = true;
    public static String productActivationStatus = "Activated";
    private String look = "Nimbus";
    private boolean trial = true;
    private boolean connectionAvailable = true;
    private String ExpireDate;

    public Login() {

        con = DbConnection.connectDb();

        //   SplashWindow2 win=new SplashWindow2("",this);
        if (firstLaunch) {
            try {
                if (con == null) {
                    try {

                        for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {

                            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
                            Globals.feel = "javax.swing.plaf.nimbus.NimbusLookAndFeel";
                        }
                    } catch (Exception e) {
                    }


                } else {
                    Globals.MessageinTracker();
                    ;
                    Globals.MessageOutTracker();
                    String querry6 = "Select * from systemconfiguration ";
                    int counter = 1;

                    ps = con.prepareStatement(querry6);
                    rs = ps.executeQuery();
                    while (rs.next()) {
                        if (counter < 7) {
                            if (rs.getString("status").equalsIgnoreCase("true")) {
                                look = rs.getString("Configurationname");

                            }
                        } else {
                            break;
                        }
                        counter++;
                    }


                    for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {

                        UIManager.setLookAndFeel(look);

                    }
                }


                File file = new File("C:/schooldata");
                if (file.exists()) {
                } else {
                    file.mkdir();

                }


            } catch (ClassNotFoundException | IllegalAccessException | InstantiationException | SQLException | UnsupportedLookAndFeelException ex) {
                connectionAvailable = false;
                java.util.logging.Logger.getLogger(Login.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
                try {
                    for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {

                        UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");

                    }
                } catch (Exception e) {
                }
            }
        } else {


            try {

                String querry6 = "Select * from systemconfiguration ";
                int counter = 1;

                ps = con.prepareStatement(querry6);
                rs = ps.executeQuery();
                while (rs.next()) {
                    if (counter < 7) {
                        if (rs.getString("status").equalsIgnoreCase("true")) {
                            look = rs.getString("Configurationname");

                        }
                    } else {
                        break;
                    }
                    counter++;
                }


                for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {

                    UIManager.setLookAndFeel(look);

                }


            } catch (ClassNotFoundException | IllegalAccessException | InstantiationException | SQLException | UnsupportedLookAndFeelException ex) {
                connectionAvailable = false;
                java.util.logging.Logger.getLogger(Login.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
            }

        }
        initComponents();
        setSize(800, 500);
        setLocationRelativeTo(null);
        getContentPane().setBackground(Color.WHITE);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);

        this.addWindowListener(new WindowAdapter() {

            @Override
            public void windowClosing(WindowEvent e) {
                int option = JOptionPane.showConfirmDialog(null, "You Are About To Exit The Program\nDo You Really Want To End The Program", "Confirm Exit", JOptionPane.YES_NO_OPTION);
                if (option == JOptionPane.YES_OPTION) {
                    System.exit(0);
                } else if (option == JOptionPane.NO_OPTION) {
                    JOptionPane.showMessageDialog(null, "Program Exit Postponed");
                }

            }


            @Override
            public void windowOpened(WindowEvent e) {
                if (firstLaunch) {

                    try {

                        BufferedReader statusreader = new BufferedReader(new FileReader("mm2.txt"));
                        productActivationStatus = DataEncriptor.decriptor(statusreader.readLine());

                        statusreader.close();

                        if (productActivationStatus.equals("Activated")) {
//                  try{
//                          
//                    BufferedReader br2=new BufferedReader(new FileReader("cvd.txt"));
//                    String cc=br2.readLine();
//                     br2.close();
//                    if(DataEncriptor.decriptor(cc).equals("Checked"))
//                {
//                    
//                }
//                else{
//                      CopyRightViolationDetector nn=new CopyRightViolationDetector();
//                              nn.copydetector();  
//                    }
//                    br2.close();
//                  }  
//                  catch(IOException sq)
//                {
//                     JOptionPane.showMessageDialog(holder, sq.getMessage());
//                }
                        } else {

                            titleconcat = "DEMO VERSION";

                            Date date = new Date();

                            SimpleDateFormat forma = new SimpleDateFormat("yyyy/MM/dd");

                            String kk = forma.format(date);
                            BufferedReader re = new BufferedReader(new FileReader("mm.txt"));
                            //  BufferedReader relife = new BufferedReader(new FileReader("life.txt"));
                            ExpireDate = DataEncriptor.decriptor(re.readLine());
                            // String lifedate=relife.readLine();
                            re.close();

                            Date dd = forma.parse(ExpireDate);
                            long diffrence = dd.getTime() - date.getTime();
                            float daysremaining = TimeUnit.DAYS.convert(diffrence, TimeUnit.MILLISECONDS);
                            Globals.dayReminder = daysremaining;
                            titleconcat = "DEMO VERSION  " + daysremaining + " Days Remaining To Expire";
//                           if(date.after(forma.parse(lifedate)))
//                 {
//                     JOptionPane.showMessageDialog(null, "Incompatible Changes In Host Operating System Detected\n Consult Admin For Assistance System Unable To Completely Run Start up Services\n Database Sever Put On Safe Mode");
//                     System.exit(0);
//                 }
                            if (date.after(dd) && activation == false) {

                                trial = false;

                                String exKey = "";
                                String key1 = "U01mEFk3m6tifgE9akUU8sJVPCc78OFB";
                                String key2 = "yMVBdkgOFkidZqybrVcsPS0sDBzJ3xps";
                                String key3 = "XM4JxaqqVwihX4pr3Y6r2I4vwiKi6OZR";
                                String key4 = "CewDC7CrGeuRq6o1ksLqH67CWXB6m8yt";

                                exKey = JOptionPane.showInputDialog(null, "   Kindly Input The Activation Key, Trial Period Is Over   \n System Locked By Developer's Server ", "Input Key", JOptionPane.NO_OPTION);
                                if (exKey == null) {
                                    JOptionPane.showMessageDialog(null, "Invalid Key System Exiting......\n Developer's Server Issued A Stop Command To The System");
                                    System.exit(0);
                                }
                                if (exKey.equalsIgnoreCase(key1) || exKey.equalsIgnoreCase(key2) || exKey.equalsIgnoreCase(key3) || exKey.equalsIgnoreCase(key4)) {
                                    if (trial) {
                                        JOptionPane.showMessageDialog(null, "Activation Successfull...\n Thank You For Purchasing High School Program From LUNAR TECH SOLUTION\n Life Time Activation Enabled,All Features Unlocked");
                                    }

                                    activation = true;
                                    productActivationStatus = "Activated";
                                    BufferedWriter write = new BufferedWriter(new FileWriter("mm2.txt"));

                                    write.write(DataEncriptor.encript("Activated"));
                                    write.close();
                                    if (trial == false) {
                                        JOptionPane.showMessageDialog(null, "Activation Successfull...\n Welcome Back System Resumes Operations From Previous Save Point\n Thank You For Purchasing High School Program From LUNAR TECH SOLUTION,All Features Unlocked");
                                    }

                                } else {
                                    JOptionPane.showMessageDialog(null, "Invalid Activation Key The  Program is Exiting......\n Developer's Server Issued A Stop Command To The System", "Activation Failed", JOptionPane.ERROR_MESSAGE);
                                    System.exit(0);
                                }


                            } else {


                                trial = true;


                            }


                        }


                    } catch (HeadlessException | IOException | ParseException sq) {

                        try {
                            // BufferedWriter lifespan = new BufferedWriter(new FileWriter("life.txt"));
                            BufferedWriter write = new BufferedWriter(new FileWriter("mm.txt"));
                            BufferedWriter write2 = new BufferedWriter(new FileWriter("mm2.txt"));
                            Calendar cal = Calendar.getInstance();
                            int yearlife = cal.get(Calendar.YEAR) + 1;
                            int day = cal.get(Calendar.DATE);
                            int month = (cal.get(Calendar.MONTH)) + 1;

                            day = (cal.get(Calendar.DATE));
                            month = (cal.get(Calendar.MONTH)) + 4;

                            ExpireDate = (cal.get(Calendar.YEAR) + "/" + month + "/" + day);
                            //  lifespan.write(yearlife+"/"+month+"/"+day);
                            write.write(DataEncriptor.encript(ExpireDate));
                            write.close();
                            write2.write(DataEncriptor.encript("Deactivate"));
                            write2.close();
                            //  lifespan.close();
                        } catch (IOException dd) {
                            dd.printStackTrace();
                            JOptionPane.showMessageDialog(null, dd.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                        }
                        dispose();
                        new Login();
                    }


                }
            }

        });
        FredLabel time = new FredLabel("TIME");
        ImageIcon imageIcon = new ImageIcon(getClass().getResource("/icons/index6.jpg"));
        Image imaage = imageIcon.getImage();
        this.setIconImage(imaage);
        ((JComponent) getContentPane()).setBorder(
                BorderFactory.createMatteBorder(5, 5, 5, 5, Color.MAGENTA));
        try {
            BufferedReader statusreader = new BufferedReader(new FileReader("mm2.txt"));
            productActivationStatus = DataEncriptor.decriptor(statusreader.readLine());
            if (productActivationStatus.equals("Activated")) {
                Globals.activationStatus = "ACTIVATED VERSION";
                Activator.setVisible(false);
            } else {
                Date date = new Date();

                SimpleDateFormat forma = new SimpleDateFormat("yyyy/MM/dd");

                String kk = forma.format(date);
                BufferedReader re = new BufferedReader(new FileReader("mm.txt"));
                //  BufferedReader relife = new BufferedReader(new FileReader("life.txt"));
                ExpireDate = DataEncriptor.decriptor(re.readLine());
                // String lifedate=relife.readLine();
                re.close();

                Date dd = forma.parse(ExpireDate);
                long diffrence = dd.getTime() - date.getTime();
                float daysremaining = TimeUnit.DAYS.convert(diffrence, TimeUnit.MILLISECONDS);
                Globals.dayReminder = daysremaining;
                titleconcat = "DEMO VERSION  " + daysremaining + " Days Remaining To Expire";

                titleconcat = "DEMO VERSION     " + Globals.dayReminder + "  Days Remaining To Expire";
                Globals.activationStatus = ": : DEMO VERSION";
            }

            statusreader.close();

        } catch (Exception e) {
        }

        setTitle(titleconcat + "           High Pro".toUpperCase());
        setResizable(false);
        setVisible(true);
        //username.setText("Janet");
        //password.setText("pass");
        time.setForeground(Color.BLACK);
        time.setFont(new Font("serif", Font.ITALIC, 12));
        time.setBounds(150, 400, 550, 33);
        insidepanel.add(time);
        if (connectionAvailable) {
            Thread clock = new Thread() {
                @Override
                public void run() {

                    for (; ; ) {
                        if (connectionAvailable) {
                            time.setText(Globals.currentdate());
                            try {
                                sleep(1000);
                            } catch (InterruptedException ex) {
                                ex.printStackTrace();
                            }
                        }
                    }

                }

            };
            clock.start();
        }


    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        insidepanel = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        username = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        password = new javax.swing.JPasswordField();
        reset = new javax.swing.JButton();
        login = new javax.swing.JButton();
        exit = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        Activator = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBackground(new java.awt.Color(255, 153, 255));
        setType(java.awt.Window.Type.POPUP);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });
        getContentPane().setLayout(null);

        insidepanel.setBackground(new java.awt.Color(0, 153, 153));
        insidepanel.setLayout(null);

        jLabel1.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel1.setText("Username");
        insidepanel.add(jLabel1);
        jLabel1.setBounds(270, 100, 135, 24);

        username.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        username.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                usernameKeyTyped(evt);
            }
        });
        insidepanel.add(username);
        username.setBounds(430, 100, 230, 30);

        jLabel2.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel2.setText("Password");
        insidepanel.add(jLabel2);
        jLabel2.setBounds(270, 210, 146, 26);

        password.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                passwordKeyTyped(evt);
            }
        });
        insidepanel.add(password);
        password.setBounds(430, 200, 230, 29);

        reset.setText("Reset");
        reset.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        reset.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                resetActionPerformed(evt);
            }
        });
        insidepanel.add(reset);
        reset.setBounds(350, 350, 118, 33);

        login.setText("Login");
        login.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        login.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                loginActionPerformed(evt);
            }
        });
        insidepanel.add(login);
        login.setBounds(530, 350, 118, 33);

        exit.setText("Exit");
        exit.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        exit.setRolloverSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/Windows-Close-Program-icon.png"))); // NOI18N
        exit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exitActionPerformed(evt);
            }
        });
        insidepanel.add(exit);
        exit.setBounds(130, 350, 146, 33);

        jLabel3.setBackground(new java.awt.Color(255, 255, 255));
        jLabel3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/Apps-system-users-icon.png"))); // NOI18N
        insidepanel.add(jLabel3);
        jLabel3.setBounds(80, 110, 180, 160);

        jLabel4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/book_pages_turning.gif"))); // NOI18N
        insidepanel.add(jLabel4);
        jLabel4.setBounds(40, 10, 230, 110);

        Activator.setText("Activate");
        Activator.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ActivatorActionPerformed(evt);
            }
        });
        insidepanel.add(Activator);
        Activator.setBounds(650, 420, 80, 23);

        getContentPane().add(insidepanel);
        insidepanel.setBounds(20, 10, 740, 450);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void loginActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_loginActionPerformed

        if (username.getText().equalsIgnoreCase("")) {
            getToolkit().beep();
            JOptionPane.showMessageDialog(this, "Kindly Input Your UserName");
        } else {
            if (password.getText().equalsIgnoreCase("")) {
                getToolkit().beep();
                JOptionPane.showMessageDialog(this, "Kindly Input Your Passwor");
            } else {
                try {
                    con = DbConnection.connectDb();
                    String empcode = "", departcode = "", name = "";
                    String sql = "Select * from Useraccounts where username='" + username.getText() + "' and password='" + DataEncriptor.encript(password.getText()) + "'";
                    ps = con.prepareStatement(sql);
                    rs = ps.executeQuery();
                    if (rs.next()) {
                        String level = rs.getString("level");
                        Globals.Level = level;


                        status = rs.getString("Status");
                        String pass = DataEncriptor.decriptor(rs.getString("Password"));
                        Globals.CurrentUser = username.getText();
                        empcode = rs.getString("Employeecode");
                        String querry = "Select initials from staffs where employeecode='" + empcode + "' ";
                        ps = con.prepareStatement(querry);
                        rs = ps.executeQuery();
                        if (rs.next()) {
                            Globals.initials = rs.getString("Initials");
                        }


                        Globals.empcode = empcode;
                        String sql4 = "Select departmentcode from staffs where employeecode='" + empcode + "' ";
                        ps = con.prepareStatement(sql4);
                        rs = ps.executeQuery();
                        if (rs.next()) {
                            departcode = rs.getString("Departmentcode");

                        }
                        Globals.depcode = departcode;
                        String sql5 = "Select name from departments where departmentcode='" + departcode + "'";
                        ps = con.prepareStatement(sql5);
                        rs = ps.executeQuery();
                        if (rs.next()) {
                            name = rs.getString("Name");
                        }
                        Globals.depName = name;

                        if (name.equalsIgnoreCase("Finance")) {


                            if (pass.equalsIgnoreCase("pass")) {

                                String querry8 = "Select status from systemconfiguration where configurationid='" + "CO007" + "'";
                                ps = con.prepareStatement(querry8);
                                rs = ps.executeQuery();
                                while (rs.next()) {
                                    if (rs.getString("Status").equalsIgnoreCase("false")) {
                                        dispose();


                                        Globals.moduleName = "Finance";
                                        dispose();
                                        new FinanceHome();
                                        Globals.moduleName = "Finance";
                                        dispose();
                                    } else {

                                        dispose();
                                        new PasswordChange2();
                                    }
                                }

                            } else {


                                Globals.moduleName = "Finance";
                                dispose();
                                new FinanceHome();
                                Globals.moduleName = "Finace";
                                dispose();
                            }

                        } else {


                            if (status.equalsIgnoreCase("Active")) {
                                if (pass.equalsIgnoreCase("pass")) {

                                    String querry8 = "Select status from systemconfiguration where configurationid='" + "CO007" + "'";
                                    ps = con.prepareStatement(querry8);
                                    rs = ps.executeQuery();
                                    while (rs.next()) {
                                        if (rs.getString("Status").equalsIgnoreCase("false")) {
                                            dispose();
                                            JWindow option = new JWindow();

                                            option.setSize(300, 150);
                                            option.setLayout(new MigLayout());
                                            FredCombo com = new FredCombo("Select Department");
                                            com.setBorder(new TitledBorder("Module Selection"));
                                            option.setAlwaysOnTop(true);
                                            com.addItem("EXAM MODULE");
                                            com.addItem("INVENTORY MODULE");
                                            option.add(com, "pushx,growx");
                                            option.setLocationRelativeTo(null);
                                            option.setIconImage(FrameProperties.icon());
                                            option.setVisible(true);
                                            com.addActionListener(new ActionListener() {

                                                @Override
                                                public void actionPerformed(ActionEvent e) {
                                                    if (com.getSelectedIndex() > 0) {
                                                        if (com.getSelectedIndex() == 1) {
                                                            option.dispose();
                                                            Globals.moduleName = "Exam";
                                                            dispose();
                                                            new ExamHome();
                                                            Globals.moduleName = "Exam";
                                                            dispose();
                                                        } else {
                                                            option.dispose();
                                                            new AcademicsHome();
                                                            Globals.moduleName = "Inventory";
                                                            dispose();
                                                        }
                                                    }
                                                }
                                            });
//                      Globals.moduleName="Exam";
//                                                                dispose(); new ExamHome();
//                                                               Globals.moduleName="Exam";
//                                                                dispose();
                                        } else {

                                            dispose();
                                            new PasswordChange2();
                                        }
                                    }

                                } else {

                                    new Thread() {
                                        @Override
                                        public void run() {
                                            JWindow dia = new JWindow();
                                            JProgressBar bar = new JProgressBar();
                                            bar.setIndeterminate(true);

                                            dia.setSize(300, 60);

                                            bar.setBorder(new TitledBorder("Loading User Details"));
                                            dia.setAlwaysOnTop(true);
                                            dia.setLocationRelativeTo(CurrentFrame.mainFrame());
                                            dia.setIconImage(FrameProperties.icon());
                                            dia.add(bar);
                                            dia.setVisible(true);

                                            dispose();

                                            JWindow option = new JWindow();

                                            option.setSize(300, 100);
                                            option.setLayout(new MigLayout());
                                            FredCombo com = new FredCombo("Select Access Module");
                                            com.setBorder(new TitledBorder("Module Selection"));
                                            option.setAlwaysOnTop(true);
                                            com.addItem("EXAM MODULE");
                                            com.addItem("INVENTORY MODULE");
                                            option.add(com, "pushx,growx");
                                            option.setVisible(true);

                                            option.setLocationRelativeTo(null);
                                            option.setIconImage(FrameProperties.icon());
                                            com.addActionListener(new ActionListener() {

                                                @Override
                                                public void actionPerformed(ActionEvent e) {
                                                    if (com.getSelectedIndex() > 0) {
                                                        if (com.getSelectedIndex() == 1) {
                                                            option.dispose();
                                                            new ExamHome();
                                                            Globals.moduleName = "Exam";
                                                            dispose();
                                                        } else {
                                                            option.dispose();
                                                            new AcademicsHome();
                                                            Globals.moduleName = "Inventory";
                                                            dispose();
                                                        }
                                                    }
                                                }
                                            });


                                            revalidate();
                                            repaint();
                                            dia.dispose();
                                        }


                                    }.start();
//                                    Globals.moduleName="Exam";
//                                                                dispose(); new ExamHome();
//                                                               Globals.moduleName="Exam";
//                                                                dispose();   
                                }
                            } else {
                                JOptionPane.showMessageDialog(this, "Account Deactivated,\nKindly Consult The Admin For Account Re-activation");
                            }

                        }


                    } else {
                        getToolkit().beep();
                        JOptionPane.showMessageDialog(this, "Password Username Mismatch");
                    }


                } catch (HeadlessException | SQLException sq) {
                    sq.printStackTrace();
                    JOptionPane.showMessageDialog(holder, sq.getMessage(), "Server Not Accessible", JOptionPane.ERROR_MESSAGE);
                } finally {
                    try {
                        con.close();
                    } catch (SQLException sq) {
                        sq.printStackTrace();
                    }
                }
            }
        }


    }//GEN-LAST:event_loginActionPerformed

    private void exitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_exitActionPerformed
        int option = JOptionPane.showConfirmDialog(null, "You Are About To Exit The Program\nDo You Really Want To End The Program", "Confirm Exit", JOptionPane.YES_NO_OPTION);
        if (option == JOptionPane.YES_OPTION) {
            System.exit(0);
        } else if (option == JOptionPane.NO_OPTION) {
            JOptionPane.showMessageDialog(null, "Program Exit Postponed");
        }

    }//GEN-LAST:event_exitActionPerformed

    private void resetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_resetActionPerformed
        username.setText("");
        password.setText("");
    }//GEN-LAST:event_resetActionPerformed

    private void passwordKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_passwordKeyTyped


        char c = evt.getKeyChar();
        if (c == KeyEvent.VK_ENTER) {
            login.doClick();

        }
    }//GEN-LAST:event_passwordKeyTyped

    private void usernameKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_usernameKeyTyped
        if (username.getText().length() == 1) {
            username.setText(username.getText().toUpperCase());
            ;
        }
    }//GEN-LAST:event_usernameKeyTyped

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing

    }//GEN-LAST:event_formWindowClosing

    private void ActivatorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ActivatorActionPerformed
        // TODO add your handling code here:
        try {
            String exKey = "";
            String key1 = "U01mEFk3m6tifgE9akUU8sJVPCc78OFB";
            String key2 = "yMVBdkgOFkidZqybrVcsPS0sDBzJ3xps";
            String key3 = "XM4JxaqqVwihX4pr3Y6r2I4vwiKi6OZR";
            String key4 = "CewDC7CrGeuRq6o1ksLqH67CWXB6m8yt";

            exKey = JOptionPane.showInputDialog(null, "   Kindly Input The Activation Key, This Is A Life Time Activation", "Input", JOptionPane.INFORMATION_MESSAGE);

            if (exKey.equalsIgnoreCase(key1) || exKey.equalsIgnoreCase(key2) || exKey.equalsIgnoreCase(key3) || exKey.equalsIgnoreCase(key4)) {

                JOptionPane.showMessageDialog(null, "Activation Successfull");
                activation = true;
                productActivationStatus = "Activated";
                BufferedWriter write = new BufferedWriter(new FileWriter("mm2.txt"));

                write.write(DataEncriptor.encript("Activated"));
                write.close();

            } else {
                JOptionPane.showMessageDialog(null, "Invalid Activation Key", "Activation Failed", JOptionPane.ERROR_MESSAGE);

            }
        } catch (Exception e) {
        }
    }//GEN-LAST:event_ActivatorActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Login.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Login.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Login.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Login.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> {
            new Login().setVisible(true);
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton Activator;
    private javax.swing.JButton exit;
    private javax.swing.JPanel insidepanel;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JButton login;
    private javax.swing.JPasswordField password;
    private javax.swing.JButton reset;
    private javax.swing.JTextField username;
    // End of variables declaration//GEN-END:variables
}
