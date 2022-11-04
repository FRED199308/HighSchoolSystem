/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package highschool;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import static java.lang.Thread.sleep;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JFrame;

import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JWindow;

import static javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE;

import javax.swing.border.TitledBorder;

import net.miginfocom.swing.MigLayout;

/**
 * @author EXAMSERVERPC
 */
public class POSHome extends JFrame implements ActionListener {
    private Connection con;
    private PreparedStatement ps;
    private ResultSet rs;
    private Toolkit tk = Toolkit.getDefaultToolkit();
    private int width = (int) tk.getScreenSize().getWidth();
    private int height = (int) tk.getScreenSize().getHeight();
    private JMenuBar nav;

    public static Thread timeOutChecker;
    private static long startTime;
    private static int timeOut;

    private JPanel footpanel;
    private JPanel centerpanel;
    private FredLabel infor, infor2;

    private JPanel left;
    private JPanel right;
    private JPanel holder;
    private JPanel overPanel;
    private static FredJMenuItem logout, changepass, returnhome;
    private FredJMenu reg, masters, comm, reports, myaccount, Inventory;
    private FredJMenuItem customerReg, SupplierReg, goodReg, staffReg;
    private FredJMenuItem addstock, update;
    private FredJMenuItem catalogue;
    private JMenuItem saletransaction;

    private FredJMenuItem smslog, smsforeign, smssuplier, smscustomer, smsstaff;
    private FredJMenuItem customersReport, supplierReport, salesReport, stockReport, staffReport;
    private FredJMenuItem useraccount, manageuser, systemsetting, databaseSize;


    String title;
    POSHomePanel pane = new POSHomePanel();

    @SuppressWarnings("LeakingThisInConstructor")
    public POSHome() {

        this.setSize(width, height - 10);
        this.setExtendedState(MAXIMIZED_BOTH);

        setLocationRelativeTo(null);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        setLayout(new BorderLayout());
        CurrentFrame.jframeonIntialiser(this);
        con = DbConnection.connectDb();
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
        });

        this.setIconImage(FrameProperties.icon());
        ((JComponent) getContentPane()).setBorder(
                BorderFactory.createMatteBorder(5, 5, 5, 5, Color.MAGENTA));
        CurrentFrame.jframeonIntialiser(this);
        try {

            con = DbConnection.connectDb();
            String sql = "Select Name from schoolDetails";
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            if (rs.next()) {
                setTitle(rs.getString("Name") + " Store Management Software -   " + Globals.CurrentUser + "  " + Globals.activationStatus);
                title = rs.getString("Name") + " Store Management Software -   " + Globals.CurrentUser + "  " + Globals.activationStatus;
            }
            String querry5 = "Select * from useraccounts  where username='" + Globals.CurrentUser + "'";
            ps = con.prepareStatement(querry5);
            rs = ps.executeQuery();
            while (rs.next()) {
                timeOut = rs.getInt("TimeOut") * 1000;
            }

        } catch (SQLException sq) {
            sq.printStackTrace();
        } finally {
            try {
                con.close();
            } catch (SQLException sq) {
                sq.printStackTrace();
            }
        }
        this.setTitle(title);

        nav = new JMenuBar();
        comm = new FredJMenu("Communication");
        masters = new FredJMenu("Admin");

        reg = new FredJMenu("Registration");
        reports = new FredJMenu("Reports");
        myaccount = new FredJMenu("My Account");
        Inventory = new FredJMenu("Inventory");


        customerReg = new FredJMenuItem("Support Staff Registration");
        SupplierReg = new FredJMenuItem("Supplier Registration");

        goodReg = new FredJMenuItem("Goods Category Registration");

        staffReg = new FredJMenuItem("Staff Registration");


        //working
        reg.add(customerReg);
        reg.add(SupplierReg);

        reg.add(goodReg);

        reg.add(staffReg);
        //..   
        addstock = new FredJMenuItem("Add New Inventory Item");
        catalogue = new FredJMenuItem("Start Catalogue(Issue)(Cart)");
        update = new FredJMenuItem("Update Existing Inventory");
        Inventory.add(catalogue);
        Inventory.add(addstock);
        Inventory.add(update);


        //work

        saletransaction = new JMenuItem("Sales Transaction Log");
//not work


//
        smscustomer = new FredJMenuItem("SMS Customers");
        smsforeign = new FredJMenuItem("SMS Unknown Contact");
        smslog = new FredJMenuItem("SMS Log");
        smsstaff = new FredJMenuItem("SMS Staff");
        smssuplier = new FredJMenuItem("SMS Supplier");
        ///////
        comm.add(smsstaff);
        comm.add(smsforeign);
        comm.add(smslog);

        customersReport = new FredJMenuItem("Staff Report");

        supplierReport = new FredJMenuItem("Supplier Report");

        salesReport = new FredJMenuItem("Inventory Issue Report");
        stockReport = new FredJMenuItem("Present Inventory Value Report");
        staffReport = new FredJMenuItem("Staff Report");

        reports.add(customersReport);
        reports.add(staffReport);
        reports.add(salesReport);
        reports.add(stockReport);


        logout = new FredJMenuItem("Log Out");
        changepass = new FredJMenuItem("Change Password");
        returnhome = new FredJMenuItem("Return Home");
        myaccount.add(logout);
        myaccount.add(changepass);
        myaccount.add(returnhome);


        useraccount = new FredJMenuItem("Create User Account");
        manageuser = new FredJMenuItem("Manage Users");
        systemsetting = new FredJMenuItem("System Set Up");

        databaseSize = new FredJMenuItem("Querry Database Size");
        masters.add(useraccount);
        masters.add(manageuser);
        masters.add(systemsetting);
        masters.add(databaseSize);

        nav.add(reg);
        nav.add(Inventory);


        nav.add(reports);
        nav.add(comm);

        nav.add(masters);
        nav.add(myaccount);
        // nav.add(biometrics);


        customerReg.addActionListener(this);
        goodReg.addActionListener(this);
        SupplierReg.addActionListener(this);
        staffReg.addActionListener(this);


        smslog.addActionListener(this);

        addstock.addActionListener(this);
        catalogue.addActionListener(this);
        update.addActionListener(this);


        stockReport.addActionListener(this);


        saletransaction.addActionListener(this);

        smsforeign.addActionListener(this);
        smsstaff.addActionListener(this);
        useraccount.addActionListener(this);
        manageuser.addActionListener(this);
        systemsetting.addActionListener(this);
        smscustomer.addActionListener(this);
        databaseSize.addActionListener(this);
        logout.addActionListener(this);
        returnhome.addActionListener(this);
        changepass.addActionListener(this);
        salesReport.addActionListener(this);
        staffReport.addActionListener(this);
        customersReport.addActionListener(this);

        footpanel = new JPanel();
        infor = new FredLabel("THIS SOFTWARE IS DESIGNED,DISTRIBUTED & MAINTAINED BY LUNAR TECH. SOLUTION CONTACT CHIEF DEVELOPER @ O707353225 Or 0719561240");
        infor2 = new FredLabel();
        infor.setFont(new java.awt.Font("serif", java.awt.Font.BOLD, 11));
        infor2.setFont(new java.awt.Font("serif", java.awt.Font.ITALIC, 11));
        centerpanel = new JPanel();
        footpanel.setBackground(Color.WHITE);
        footpanel.setLayout(new MigLayout());
        nav.setLayout(new GridLayout(1, 5));
        add(nav, BorderLayout.NORTH);
        add(centerpanel, BorderLayout.CENTER);
        add(footpanel, BorderLayout.SOUTH);
        centerpanel.setLayout(new MigLayout());
        add(footpanel, BorderLayout.SOUTH);
        footpanel.add(infor, "gapleft 100,grow,push");
        footpanel.add(infor2, "gapleft 50");
        CurrentFrame.jframeonIntialiser(this);

        timeOutChecker = new Thread() {
            @Override
            public void run() {
                startTime = System.currentTimeMillis();
                for (; ; ) {
                    long currenttime = System.currentTimeMillis();
                    if (currenttime - startTime > timeOut) {
                        POSHomePanel.logout.doClick();

                        break;

                    }
                    long t = (timeOut - (currenttime - startTime)) / 1000;
                    infor2.setText("SYSTEM TIME OUT REMAINING : ".toUpperCase() + String.valueOf(t) + " (Sec)");

                    CurrentFrame.mainFrame().setTitle(title + "                        " + "SYSTEM TIME OUT REMAINING : ".toUpperCase() + String.valueOf(t) + " (Sec)");

                    try {
                        sleep(1000);
                        CurrentFrame.mainFrame().addMouseMotionListener(new MouseMotionListener() {
                            @Override
                            public void mouseDragged(MouseEvent e) {
                                startTime = System.currentTimeMillis();

                            }

                            @Override
                            public void mouseMoved(MouseEvent e) {
                                startTime = System.currentTimeMillis();

                            }
                        });

                        CurrentFrame.secFrame().addMouseMotionListener(new MouseMotionListener() {
                            @Override
                            public void mouseDragged(MouseEvent e) {
                                startTime = System.currentTimeMillis();
                            }

                            @Override
                            public void mouseMoved(MouseEvent e) {
                                startTime = System.currentTimeMillis();

                            }
                        });

                        if (CurrentFrame.docOpener() == null) {

                        } else {
                            CurrentFrame.docOpener().addMouseMotionListener(new MouseMotionListener() {
                                @Override
                                public void mouseDragged(MouseEvent e) {
                                    startTime = System.currentTimeMillis();
                                }

                                @Override
                                public void mouseMoved(MouseEvent e) {
                                    startTime = System.currentTimeMillis();

                                }
                            });
                        }

                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }

                }
            }
        };

        if (!RightsAnnouncer.communication()) {
            comm.setEnabled(false);
        }


        timeOutChecker.start();
        overPanel = pane.rightPanel();
        centerpanel.add(overPanel, "grow,push");


        setVisible(true);
    }

    public static void main(String[] args) {


    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object obj = e.getSource();
        if (obj == customerReg) {

            CurrentFrame.setSecondFrame(new StaffRegistration());
            CurrentFrame.jframeon(this);
        } else if (obj == goodReg) {
            CurrentFrame.setSecondFrame(new GoodCategoryRegistration());
            CurrentFrame.jframeon(this);
        } else if (obj == stockReport) {
            new Thread() {
                @Override
                public void run() {
                    JWindow dia = new JWindow();
                    JProgressBar bar = new JProgressBar();
                    bar.setIndeterminate(true);

                    dia.setSize(300, 60);

                    bar.setBorder(new TitledBorder("Retrieving Inventory  Information"));
                    dia.setAlwaysOnTop(true);
                    dia.setLocationRelativeTo(CurrentFrame.mainFrame());
                    dia.setIconImage(FrameProperties.icon());
                    dia.add(bar);
                    dia.setVisible(true);
                    centerpanel.removeAll();
                    revalidate();
                    repaint();
                    StockInformation mm = new StockInformation();

                    centerpanel.add(mm.stockInfo(), "grow,push");

                    revalidate();
                    repaint();
                    dia.dispose();
                }

            }.start();
        }

        if (obj == salesReport) {

            new Thread() {
                @Override
                public void run() {
                    JWindow dia = new JWindow();
                    JProgressBar bar = new JProgressBar();
                    bar.setIndeterminate(true);

                    dia.setSize(300, 60);

                    bar.setBorder(new TitledBorder("Retrieving Inventory Use Information"));
                    dia.setAlwaysOnTop(true);
                    dia.setLocationRelativeTo(CurrentFrame.mainFrame());
                    dia.setIconImage(FrameProperties.icon());
                    dia.add(bar);
                    dia.setVisible(true);
                    centerpanel.removeAll();
                    revalidate();
                    repaint();
                    SalesInfor mm = new SalesInfor();

                    centerpanel.add(mm.salesinfor(), "grow,push");

                    revalidate();
                    repaint();
                    dia.dispose();
                }

            }.start();
        }
        if (obj == databaseSize) {


            Globals.dataBaseSizeQuerryRunner();
        } else if (obj == smscustomer) {
            CurrentFrame.setSecondFrame(new SMSCustomer());
            CurrentFrame.jframeon(this);
        }


        if (obj == logout) {
            try {
                Login.firstLaunch = false;
                CurrentFrame.killSecondFrame();

                CurrentFrame.currentWindow();
                CurrentFrame.mainFrame().dispose();
                CurrentFrame.docOpener().dispose();
                POSHome.timeOutChecker.stop();
                new Login().setVisible(true);
                con.close();
            } catch (SQLException sq) {
                CurrentFrame.currentWindow();
                CurrentFrame.mainFrame().dispose();
                CurrentFrame.docOpener().dispose();
            }

        }

        if (obj == changepass) {
            CurrentFrame.setSecondFrame(new PasswordChange());
            CurrentFrame.jframeon(this);
        } else if (obj == systemsetting) {
            CurrentFrame.setSecondFrame(new Configurations());
            CurrentFrame.jframeon(this);
        }
        if (obj == returnhome) {
            centerpanel.removeAll();
            centerpanel.add(overPanel, "grow,push");
            centerpanel.revalidate();
            centerpanel.repaint();
        } else if (obj == useraccount) {
            CurrentFrame.setSecondFrame(new UserAccount());
            CurrentFrame.jframeon(this);
        } else if (obj == smsstaff) {
            CurrentFrame.setSecondFrame(new SmsStaff());
            CurrentFrame.jframeon(this);
        } else if (obj == smsforeign) {
            CurrentFrame.setSecondFrame(new SMSForeignNumber());
            CurrentFrame.jframeon(this);
        } else if (obj == manageuser) {
            CurrentFrame.setSecondFrame(new UserRights());
            CurrentFrame.jframeon(this);
        } else if (obj == SupplierReg) {
            CurrentFrame.setSecondFrame(new SupplierRegstration());
            CurrentFrame.jframeon(this);
        } else if (obj == staffReg) {
            CurrentFrame.setSecondFrame(new StaffRegistration());
            CurrentFrame.jframeon(this);
        } else if (obj == catalogue) {
            CurrentFrame.setSecondFrame(new Catalogue());
            CurrentFrame.jframeon(this);
        } else if (obj == addstock) {
            CurrentFrame.setSecondFrame(new ProductRegistration());
            CurrentFrame.jframeon(this);
        } else if (obj == customersReport) {
            new Thread() {
                @Override
                public void run() {
                    JWindow dia = new JWindow();
                    JProgressBar bar = new JProgressBar();
                    bar.setIndeterminate(true);

                    dia.setSize(300, 60);

                    bar.setBorder(new TitledBorder("Retrieving Support Staff Information"));
                    dia.setAlwaysOnTop(true);
                    dia.setLocationRelativeTo(CurrentFrame.mainFrame());
                    dia.setIconImage(FrameProperties.icon());
                    dia.add(bar);
                    dia.setVisible(true);
                    centerpanel.removeAll();
                    revalidate();
                    repaint();
                    // Customer mm = new Customer();

                    // centerpanel.add(mm.CustomerManagerPanel(), "grow,push");

                    revalidate();
                    repaint();
                    dia.dispose();
                }

            }.start();
        } else if (obj == staffReport) {
            new Thread() {
                @Override
                public void run() {
                    JWindow dia = new JWindow();
                    JProgressBar bar = new JProgressBar();
                    bar.setIndeterminate(true);

                    dia.setSize(300, 60);

                    bar.setBorder(new TitledBorder("Retrieving Staff Information"));
                    dia.setAlwaysOnTop(true);
                    dia.setLocationRelativeTo(CurrentFrame.mainFrame());
                    dia.setIconImage(FrameProperties.icon());
                    dia.add(bar);
                    dia.setVisible(true);
                    centerpanel.removeAll();
                    revalidate();
                    repaint();
                    StaffManagement mm = new StaffManagement();

                    centerpanel.add(mm.staffManagerPanel(), "grow,push");

                    revalidate();
                    repaint();
                    dia.dispose();
                }

            }.start();
        } else if (obj == saletransaction) {
            new Thread() {
                @Override
                public void run() {
                    JWindow dia = new JWindow();
                    JProgressBar bar = new JProgressBar();
                    bar.setIndeterminate(true);

                    dia.setSize(300, 60);

                    bar.setBorder(new TitledBorder("Retrieving Sales Transaction  Log"));
                    dia.setAlwaysOnTop(true);
                    dia.setLocationRelativeTo(CurrentFrame.mainFrame());
                    dia.setIconImage(FrameProperties.icon());
                    dia.add(bar);
                    dia.setVisible(true);
                    centerpanel.removeAll();
                    revalidate();
                    repaint();
                    // SaleTransactionLog mm = new SaleTransactionLog();

                    //   centerpanel.add(mm.salesTrail(), "grow,push");

                    revalidate();
                    repaint();
                    dia.dispose();
                }

            }.start();
        } else if (obj == update) {
            new Thread() {
                @Override
                public void run() {
                    JWindow dia = new JWindow();
                    JProgressBar bar = new JProgressBar();
                    bar.setIndeterminate(true);

                    dia.setSize(300, 60);

                    bar.setBorder(new TitledBorder("Fetching Inventory Record"));
                    dia.setAlwaysOnTop(true);
                    dia.setLocationRelativeTo(CurrentFrame.mainFrame());
                    dia.setIconImage(FrameProperties.icon());
                    dia.add(bar);
                    dia.setVisible(true);
                    centerpanel.removeAll();
                    revalidate();
                    repaint();
                    ProductUpdater mm = new ProductUpdater();

                    centerpanel.add(mm.productupdate(), "grow,push");

                    revalidate();
                    repaint();
                    dia.dispose();
                }

            }.start();
        } else if (obj == smslog) {


            new Thread() {
                @Override
                public void run() {
                    JWindow dia = new JWindow();
                    JProgressBar bar = new JProgressBar();
                    bar.setIndeterminate(true);

                    dia.setSize(300, 60);

                    bar.setBorder(new TitledBorder("Fetching SMS Log"));
                    dia.setAlwaysOnTop(true);
                    dia.setLocationRelativeTo(CurrentFrame.mainFrame());
                    dia.setIconImage(FrameProperties.icon());
                    dia.add(bar);
                    dia.setVisible(true);
                    centerpanel.removeAll();
                    revalidate();
                    repaint();
                    SMSLog mm = new SMSLog();

                    centerpanel.add(mm.smsLogPanel(), "grow,push");

                    revalidate();
                    repaint();
                    dia.dispose();
                }

            }.start();

        }


    }

}
