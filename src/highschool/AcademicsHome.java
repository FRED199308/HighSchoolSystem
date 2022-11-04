/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package highschool;


import com.itextpdf.text.Font;

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
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;

import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JWindow;

import static javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS;

import javax.swing.border.TitledBorder;

import net.miginfocom.swing.MigLayout;

/**
 * @author FRED
 */
public class AcademicsHome extends JFrame implements ActionListener {

    public static Thread timeOutChecker;
    private static long startTime;
    private static int timeOut;
    String title1 = "";

    public static void main(String[] args) {
        new AcademicsHome();
    }

    private Toolkit tk = Toolkit.getDefaultToolkit();
    private int height = ((int) tk.getScreenSize().getHeight());
    private int width = (int) tk.getScreenSize().getWidth();
    private JPanel centerpanel;
    private FredLabel infor2;
    private Connection con;
    private FredJMenuItem regbook, regstaff, regsubject, regclass, regstudent;
    private FredJMenuItem log, change, home;
    private FredJMenuItem assigned, onstore, all, lost, bookStatistics;
    private FredJMenuItem rend, recieveback;
    private FredJMenuItem clear, alumniclearance;
    private FredJMenuItem smsstaff, smslost, smsforeign, smslog;
    private FredJMenuItem useraccount, systemsetup, manageuser, classmigration, assignesubjects;
    AcademicHomePanel pane = new AcademicHomePanel();
    private JPanel panel;
    private JPanel overPanel;

    public AcademicsHome() {
        Globals.SortIntialiser();

        this.setExtendedState(MAXIMIZED_BOTH);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        setLayout(new BorderLayout());
        CurrentFrame.jframeonIntialiser(this);

        JMenuBar nav = new JMenuBar();
        JPanel footpanel = new JPanel();
        FredLabel infor = new FredLabel("THIS SOFTWARE IS DESIGNED,DISTRIBUTED & MAINTAINED BY LUNAR TECH. SOLUTION CONTACT CHIEF DEVELOPER @ O707353225");
        infor2 = new FredLabel();
        infor.setFont(new java.awt.Font("serif", java.awt.Font.BOLD, 11));
        infor2.setFont(new java.awt.Font("serif", java.awt.Font.BOLD, 11));
        centerpanel = new JPanel();
        footpanel.setBackground(Color.WHITE);
        footpanel.setLayout(new MigLayout());
        nav.setLayout(new GridLayout(1, 5));
        add(nav, BorderLayout.NORTH);
        JScrollPane scrolls = new JScrollPane(centerpanel);
        scrolls.setVerticalScrollBarPolicy(VERTICAL_SCROLLBAR_ALWAYS);
        // scrolls.setHorizontalScrollBarPolicy(HORIZONTAL_SCROLLBAR_ALWAYS);
        add(scrolls, BorderLayout.CENTER);
        add(footpanel, BorderLayout.SOUTH);
        centerpanel.setLayout(new MigLayout());
        FredJMenu reg = new FredJMenu("Registration");
        FredJMenu viewbooks = new FredJMenu("View Books");
        FredJMenu clearance = new FredJMenu("Clearance Record");
        FredJMenu bookop = new FredJMenu("Book Allocation");
        FredJMenu masters = new FredJMenu("Masters");
        FredJMenu comm = new FredJMenu("Communication");
        FredJMenu homeMenu = new FredJMenu("Home");
        nav.add(homeMenu);
        nav.add(reg);
        nav.add(viewbooks);
        nav.add(bookop);
        nav.add(clearance);
        nav.add(comm);
        nav.add(masters);//nav.add(home);
        regbook = new FredJMenuItem("Register Book");
        regsubject = new FredJMenuItem("Register Subject");
        regstudent = new FredJMenuItem("Register Student");
        regclass = new FredJMenuItem("Register Class");
        regstaff = new FredJMenuItem("Register Teacher");
        log = new FredJMenuItem("Log Out");
        change = new FredJMenuItem("Change Password");
        assigned = new FredJMenuItem("View Issued Books");
        onstore = new FredJMenuItem("View Books On Store");
        all = new FredJMenuItem("View All Books");
        lost = new FredJMenuItem("View Lost Books");
        bookStatistics = new FredJMenuItem("View Book Statistics");
        rend = new FredJMenuItem("Issue Book");
        recieveback = new FredJMenuItem("Receive Back");
        clear = new FredJMenuItem("View Clearance Record");
        alumniclearance = new FredJMenuItem("Alumni Clearance Record");
        useraccount = new FredJMenuItem("Create User Account");
        manageuser = new FredJMenuItem("Manage Users");
        manageuser.setIcon(new ImageIcon(getClass().getResource("/icons/User-Group-icon.png")));
        assignesubjects = new FredJMenuItem("Assign Subject Rights");
        systemsetup = new FredJMenuItem("System Set Up");
        classmigration = new FredJMenuItem("Class Migration");
        smslost = new FredJMenuItem("Lost Book SMS Notifications");
        smslog = new FredJMenuItem("SMS Log");
        smsstaff = new FredJMenuItem("SMS All Staff");
        home = new FredJMenuItem("Return Home");
        smsforeign = new FredJMenuItem("SMS Non Contact");
        smsforeign.addActionListener(this);
        manageuser.addActionListener(this);
        alumniclearance.addActionListener(this);
        home.addActionListener(this);
        smslog.addActionListener(this);
        regbook.addActionListener(this);
        change.addActionListener(this);
        classmigration.addActionListener(this);
        clear.addActionListener(this);
        smslost.addActionListener(this);
        smsstaff.addActionListener(this);
        regstaff.addActionListener(this);
        onstore.addActionListener(this);
        lost.addActionListener(this);
        all.addActionListener(this);
        systemsetup.addActionListener(this);
        regstudent.addActionListener(this);
        regstudent.addActionListener(this);
        regclass.addActionListener(this);
        regsubject.addActionListener(this);
        log.addActionListener(this);
        assignesubjects.addActionListener(this);
        rend.addActionListener(this);
        recieveback.addActionListener(this);
        useraccount.addActionListener(this);
        assigned.addActionListener(this);
        bookStatistics.addActionListener(this);
        reg.add(regbook);
        reg.add(regsubject);
        reg.add(regstudent);
        reg.add(regstaff);
        reg.add(regclass);
        viewbooks.add(assigned);
        viewbooks.add(onstore);
        viewbooks.add(lost);
        viewbooks.add(all);
        viewbooks.add(bookStatistics);
        bookop.add(rend);
        bookop.add(recieveback);
        clearance.add(clear);
        clearance.add(alumniclearance);
        homeMenu.add(home);
        masters.add(useraccount);
        masters.add(manageuser);
        masters.add(assignesubjects);
        masters.add(classmigration);
        masters.add(systemsetup);
        comm.add(smslost);
        comm.add(smsstaff);
        comm.add(smsforeign);
        comm.add(smslog);
        ImageIcon icon = new ImageIcon(getClass().getResource("/icons/Settings-icon.png"));
        systemsetup.setIcon(icon);
        add(footpanel, BorderLayout.SOUTH);
        footpanel.add(infor, "gapleft 100,grow,push");
        footpanel.add(infor2, "gapleft 100");
        if (Globals.Level.equalsIgnoreCase("Normal")) {
            masters.setEnabled(false);
            comm.setEnabled(false);
            regstaff.setEnabled(false);
            regclass.setEnabled(false);
        }
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        getContentPane().setBackground(Color.CYAN);
        this.addWindowListener(new WindowAdapter() {

            @Override
            public void windowClosing(WindowEvent e) {
                int option = JOptionPane.showConfirmDialog(null, "You Are About To Exit The Program\nDo You Really Want To End The Program", "Confirm Exit", JOptionPane.YES_NO_OPTION);
                if (option == JOptionPane.YES_OPTION) {
                    try {
                        con.close();
                    } catch (SQLException sq) {

                    }
                    System.exit(0);
                } else if (option == JOptionPane.NO_OPTION) {
                    JOptionPane.showMessageDialog(null, "Program Exit Postponed");
                }
            }
        });
        try {

            con = DbConnection.connectDb();
            String sql = "Select Name from schoolDetails";
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                setTitle(rs.getString("Name") + " Book Issuance Software -   " + Globals.CurrentUser + "   " + Globals.activationStatus);
                title1 = rs.getString("Name") + " Book Issuance Software -   " + Globals.CurrentUser + "   " + Globals.activationStatus;
            }
            String querry5 = "Select * from useraccounts  where username='" + Globals.CurrentUser + "'";
            ps = con.prepareStatement(querry5);
            rs = ps.executeQuery();
            while (rs.next()) {
                timeOut = rs.getInt("TimeOut") * 1000;
            }

        } catch (SQLException sq) {
            JOptionPane.showMessageDialog(null, sq.getMessage());
            sq.printStackTrace();
        } finally {
            try {
                con.close();
            } catch (SQLException sq) {
                JOptionPane.showMessageDialog(null, sq.getMessage());
                sq.printStackTrace();
            }
        }
        this.setIconImage(FrameProperties.icon());
        ((JComponent) getContentPane()).setBorder(
                BorderFactory.createMatteBorder(5, 5, 5, 5, Color.MAGENTA));
        CurrentFrame.jframeonIntialiser(this);

        timeOutChecker = new Thread() {
            @Override
            public void run() {
                startTime = System.currentTimeMillis();
                for (; ; ) {
                    long currenttime = System.currentTimeMillis();
                    if (currenttime - startTime > timeOut) {
                        AcademicHomePanel.logout.doClick();
                        break;
                        // JOptionPane.showMessageDialog(null, "No activity within " + timeOut + "seconds; please log in again.", "Message", JOptionPane.INFORMATION_MESSAGE);
                    }
                    long t = (timeOut - (currenttime - startTime)) / 1000;
                    infor2.setText("SYSTEM TIME OUT REMAINING : ".toUpperCase() + String.valueOf(t) + " (Sec)");
                    ;
                    CurrentFrame.mainFrame().setTitle(title1 + "                        " + "SYSTEM TIME OUT REMAINING : ".toUpperCase() + String.valueOf(t) + " (Sec)");
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

                    } catch (InterruptedException ex) {
                        ex.printStackTrace();

                    }

                }
            }
        };

        timeOutChecker.start();
        overPanel = pane.rightPanel();
        centerpanel.add(overPanel, "grow,push");


        setVisible(true);
        this.setAlwaysOnTop(true);
        this.setAlwaysOnTop(false);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object obj = e.getSource();
        if (obj == regbook) {
            CurrentFrame.jframeon(this);
            CurrentFrame.setSecondFrame(new BookRegistration());
            ;
        } else if (obj == home) {
            centerpanel.removeAll();
            centerpanel.add(overPanel, "grow,push");
            centerpanel.revalidate();
            centerpanel.repaint();
        } else if (obj == regsubject) {
            CurrentFrame.setSecondFrame(new SubjectRegistration());
            CurrentFrame.jframeon(this);

        } else if (obj == bookStatistics) {
            CurrentFrame.setSecondFrame(new BookStatistics());
            CurrentFrame.jframeon(this);

        } else if (obj == regstudent) {
            CurrentFrame.setSecondFrame(new studentReg());
            CurrentFrame.jframeon(this);

        } else if (obj == manageuser) {
            CurrentFrame.setSecondFrame(new UserRights());
            CurrentFrame.jframeon(this);

        } else if (obj == regstaff) {
            CurrentFrame.setSecondFrame(new TeacherRegistration());
            CurrentFrame.jframeon(this);

        } else if (obj == regclass) {
            CurrentFrame.setSecondFrame(new ClassReg());
            CurrentFrame.jframeon(this);

        } else if (obj == log) {
            try {
                con.close();
            } catch (SQLException sq) {

            }
            dispose();
            new Login();
        } else if (obj == smsstaff) {
            CurrentFrame.setSecondFrame(new SmsStaff());
            CurrentFrame.jframeon(this);

        } else if (obj == smslost) {
            CurrentFrame.setSecondFrame(new LostBooksNotifier());
            CurrentFrame.jframeon(this);
        } else if (obj == classmigration) {
            CurrentFrame.setSecondFrame(new ClassMigration());
            CurrentFrame.jframeon(this);

        } else if (obj == change) {
            CurrentFrame.setSecondFrame(new PasswordChange());
            CurrentFrame.jframeon(this);

        } else if (obj == systemsetup) {
            CurrentFrame.setSecondFrame(new Configurations());
            CurrentFrame.jframeon(this);

        } else if (obj == assignesubjects) {
            CurrentFrame.setSecondFrame(new SubjectAssigner());
            CurrentFrame.jframeon(this);

        } else if (obj == smsforeign) {
            CurrentFrame.setSecondFrame(new SMSForeignNumber());
            CurrentFrame.jframeon(this);

        } else if (obj == rend) {

            new Thread() {
                @Override
                public void run() {
                    JWindow dia = new JWindow();
                    JProgressBar bar = new JProgressBar();
                    bar.setIndeterminate(true);

                    dia.setSize(300, 60);

                    bar.setBorder(new TitledBorder("Fetching Books Information"));
                    dia.setAlwaysOnTop(true);
                    dia.setLocationRelativeTo(CurrentFrame.mainFrame());
                    dia.setIconImage(FrameProperties.icon());
                    dia.add(bar);
                    dia.setVisible(true);
                    centerpanel.removeAll();
                    revalidate();
                    repaint();
                    BookAssigner ff = new BookAssigner();

                    centerpanel.add(ff.bookassignpanel(), "grow,push");
                    revalidate();
                    repaint();
                    dia.dispose();
                }


            }.start();

        }
        if (obj == recieveback) {
            new Thread() {
                @Override
                public void run() {
                    JWindow dia = new JWindow();
                    JProgressBar bar = new JProgressBar();
                    bar.setIndeterminate(true);

                    dia.setSize(300, 60);

                    bar.setBorder(new TitledBorder("Retrieving Book Issuance Information"));
                    dia.setAlwaysOnTop(true);
                    dia.setLocationRelativeTo(CurrentFrame.mainFrame());
                    dia.setIconImage(FrameProperties.icon());
                    dia.add(bar);
                    dia.setVisible(true);
                    centerpanel.removeAll();
                    revalidate();
                    repaint();
                    RecieveBack ff = new RecieveBack();

                    centerpanel.add(ff.bookrecieverPanel(), "grow,push");
                    revalidate();
                    repaint();
                    dia.dispose();
                }


            }.start();

        } else if (obj == assigned) {
            new Thread() {
                @Override
                public void run() {
                    JDialog dia = new JDialog();
                    JProgressBar bar = new JProgressBar();
                    bar.setIndeterminate(true);

                    dia.setSize(300, 60);

                    dia.setTitle("Retrieving Issued Books");
                    dia.setAlwaysOnTop(true);
                    dia.setLocationRelativeTo(CurrentFrame.mainFrame());
                    dia.setIconImage(FrameProperties.icon());
                    dia.add(bar);
                    dia.setVisible(true);
                    centerpanel.removeAll();
                    revalidate();
                    repaint();
                    AssignedBooks ff = new AssignedBooks();

                    centerpanel.add(ff.assignedBooksPanel(), "grow,push");
                    revalidate();
                    repaint();
                    dia.dispose();
                }


            }.start();
        } else if (obj == onstore) {
            new Thread() {
                @Override
                public void run() {
                    JDialog dia = new JDialog();
                    JProgressBar bar = new JProgressBar();
                    bar.setIndeterminate(true);

                    dia.setSize(300, 60);

                    dia.setTitle("Retrieving Books in Store");
                    dia.setAlwaysOnTop(true);
                    dia.setLocationRelativeTo(CurrentFrame.mainFrame());
                    dia.setIconImage(FrameProperties.icon());
                    dia.add(bar);
                    dia.setVisible(true);
                    centerpanel.removeAll();
                    revalidate();
                    repaint();
                    BooksOnStore ff = new BooksOnStore();

                    centerpanel.add(ff.storeBooksPanel(), "grow,push");
                    revalidate();
                    repaint();
                    dia.dispose();
                }


            }.start();
        } else if (obj == lost) {

            new Thread() {
                @Override
                public void run() {
                    JDialog dia = new JDialog();
                    JProgressBar bar = new JProgressBar();
                    bar.setIndeterminate(true);

                    dia.setSize(300, 60);

                    dia.setTitle("Retrieving Lost Books Information");
                    dia.setAlwaysOnTop(true);
                    dia.setLocationRelativeTo(CurrentFrame.mainFrame());
                    dia.setIconImage(FrameProperties.icon());
                    dia.add(bar);
                    dia.setVisible(true);
                    centerpanel.removeAll();
                    revalidate();
                    repaint();
                    LostBooks ff = new LostBooks();

                    centerpanel.add(ff.LostBooksPanel(), "grow,push");
                    revalidate();
                    repaint();
                    dia.dispose();
                }


            }.start();

        } else if (obj == all) {
            new Thread() {
                @Override
                public void run() {
                    JDialog dia = new JDialog();
                    JProgressBar bar = new JProgressBar();
                    bar.setIndeterminate(true);

                    dia.setSize(300, 60);

                    dia.setTitle("Retrieving Books Information");
                    dia.setAlwaysOnTop(true);
                    dia.setLocationRelativeTo(CurrentFrame.mainFrame());
                    dia.setIconImage(FrameProperties.icon());
                    dia.add(bar);
                    dia.setVisible(true);
                    centerpanel.removeAll();
                    revalidate();
                    repaint();
                    AllBooks ff = new AllBooks();

                    centerpanel.add(ff.AllBooksPanel(), "grow,push");
                    revalidate();
                    repaint();
                    dia.dispose();
                }


            }.start();
        } else if (obj == clear) {
            new Thread() {
                @Override
                public void run() {
                    JWindow dia = new JWindow();
                    JProgressBar bar = new JProgressBar();
                    bar.setIndeterminate(true);

                    dia.setSize(300, 60);

                    bar.setBorder(new TitledBorder("Fetching Student Information"));
                    dia.setAlwaysOnTop(true);
                    dia.setLocationRelativeTo(CurrentFrame.mainFrame());
                    dia.setIconImage(FrameProperties.icon());
                    dia.add(bar);
                    dia.setVisible(true);
                    centerpanel.removeAll();
                    revalidate();
                    repaint();
                    StudentOperations ff = new StudentOperations();

                    centerpanel.add(ff.studentManger(), "grow,push");
                    revalidate();
                    repaint();
                    dia.dispose();
                }


            }.start();

        } else if (obj == alumniclearance) {
            new Thread() {
                @Override
                public void run() {
                    JWindow dia = new JWindow();
                    JProgressBar bar = new JProgressBar();
                    bar.setIndeterminate(true);

                    dia.setSize(300, 60);

                    bar.setBorder(new TitledBorder("Fetching Alumi Information"));
                    dia.setAlwaysOnTop(true);
                    dia.setLocationRelativeTo(CurrentFrame.mainFrame());
                    dia.setIconImage(FrameProperties.icon());
                    dia.add(bar);
                    dia.setVisible(true);
                    centerpanel.removeAll();
                    revalidate();
                    repaint();
                    AlumniClearance ff = new AlumniClearance();

                    centerpanel.add(ff.studentManger(), "grow,push");
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
        } else if (obj == useraccount) {
            CurrentFrame.setSecondFrame(new UserAccount());
            CurrentFrame.jframeon(this);


        }
    }

}
