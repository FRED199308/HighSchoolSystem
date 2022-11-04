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
import java.io.IOException;

import static java.lang.Thread.sleep;

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
import static javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE;

import javax.swing.border.TitledBorder;

import net.miginfocom.swing.MigLayout;

/**
 * @author FRED
 */
public class ExamHome extends JFrame implements ActionListener {

    public static Thread timeOutChecker;
    private static long startTime;
    private static int timeOut;


    public static void main(String[] args) {
        new ExamHome();
    }

    private Connection con;
    private PreparedStatement ps;
    private ResultSet rs;
    private Toolkit tk = Toolkit.getDefaultToolkit();
    private int width = (int) tk.getScreenSize().getWidth();
    private int height = (int) tk.getScreenSize().getHeight();
    private JMenuBar nav;

    private JPanel footpanel;
    private JPanel centerpanel;
    private FredLabel infor, infor2;

    private FredJMenu reg, examination, masters, comm, reports, myaccount, Attendance;
    private FredJMenuItem attend, attendanceReport, compreg;
    private FredJMenuItem examreg, gradesysreg, meangradereg, examweighreg, streamreg, classreg, regstudent, regteacher;
    private FredJMenuItem entermarks, subjectPaperCombination, missingmarks, kcpemarks, examTargets, subcomment, teachercomment, principalcomment, allocatesubjects, deletemarks, kcseExam, assignIndex;
    private FredJMenuItem merit, reportform, bestpersub, mostimproved, bestoverall, gradedistribution, subjectVap, teacherdet, studentdetails, mostimprovedpersub, generalclasslist, subjectclasslist, photoViewer;
    private FredJMenuItem smsresults, smsparents, recharge, smsstaff, smslog, phonebook, onlineSms, onlineOutbox, smsForeign;
    private FredJMenuItem applygradesys, opentt, sync, examanalysis, combine, classtranfer, mangeuser, settings, account, timetable, schoolreg, assignesubjects, termmigration, opening, fee, databaseSize, kcpeanalysis;
    private static FredJMenuItem logout, changepass, returnhome;
    private JPanel left;
    private JPanel right;
    private JPanel holder;
    private JPanel overPanel;
    ExamHomePanel pane = new ExamHomePanel();
    private JPanel panel;
    String title = "";

    @SuppressWarnings("LeakingThisInConstructor")
    public ExamHome() {
        Globals.SortIntialiser();
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
                setTitle(rs.getString("Name") + " Exam Results Processing Software  -   " + Globals.CurrentUser + "   " + Globals.activationStatus);
                title = rs.getString("Name") + " Exam Results Processing Software  -   " + Globals.CurrentUser + "   " + Globals.activationStatus;
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

        nav = new JMenuBar();
        footpanel = new JPanel();
        infor = new FredLabel("THIS SOFTWARE IS DESIGNED,DISTRIBUTED & MAINTAINED BY LUNAR TECH. SOLUTION CONTACT CHIEF DEVELOPER @ O707353225 OR 0702031163");
        infor2 = new FredLabel();
        infor.setFont(new java.awt.Font("serif", java.awt.Font.BOLD, 11));
        infor2.setFont(new java.awt.Font("serif", java.awt.Font.ITALIC, 11));
        centerpanel = new JPanel();
        JScrollPane scrolls = new JScrollPane(centerpanel);
        scrolls.setVerticalScrollBarPolicy(VERTICAL_SCROLLBAR_ALWAYS);

        add(scrolls, BorderLayout.CENTER);
        footpanel.setBackground(Color.WHITE);
        footpanel.setLayout(new MigLayout());
        nav.setLayout(new GridLayout(1, 5));
        add(nav, BorderLayout.NORTH);
        add(scrolls, BorderLayout.CENTER);
        add(footpanel, BorderLayout.SOUTH);
        centerpanel.setLayout(new MigLayout());
        add(footpanel, BorderLayout.SOUTH);
        footpanel.add(infor, "gapleft 100,grow,push");
        footpanel.add(infor2, "gapleft 50");
        comm = new FredJMenu("Communication");
        masters = new FredJMenu("Masters");
        examination = new FredJMenu("Examinations");
        reg = new FredJMenu("Registration");
        reports = new FredJMenu("Reports");
        myaccount = new FredJMenu("My Account");
        Attendance = new FredJMenu("School Register");
        compreg = new FredJMenuItem("Comprehensive Student Register");
        kcpeanalysis = new FredJMenuItem("K.C.P.E Analysis");

        nav.add(reg);
        nav.add(examination);
        nav.add(reports);
        nav.add(comm);
        nav.add(Attendance);
        nav.add(masters);
        nav.add(myaccount);
        attend = new FredJMenuItem("Take Attendance");
        attendanceReport = new FredJMenuItem("View Attendance Report");
        Attendance.add(attend);
        Attendance.add(attendanceReport);
        Attendance.add(compreg);
//        comm.setEnabled(false);
//       Attendance.setEnabled(false);
        examreg = new FredJMenuItem("Register Exam");
        gradesysreg = new FredJMenuItem("Register Subject Grading System");
        meangradereg = new FredJMenuItem("Register Mean Grade System");
        examweighreg = new FredJMenuItem("Register Exam Weight");
        streamreg = new FredJMenuItem("Register  Stream");
        classreg = new FredJMenuItem("Register classes");
        regstudent = new FredJMenuItem("Register Student");
        regteacher = new FredJMenuItem("Register Teacher");
        reg.add(regstudent);
        reg.add(regteacher);
        reg.add(examreg);
        reg.add(gradesysreg);
        reg.add(meangradereg);
        reg.add(examweighreg);
        reg.add(streamreg);
        reg.add(classreg);

        entermarks = new FredJMenuItem("Enter Internal Examination Marks");
        missingmarks = new FredJMenuItem("View Entered Marks");
        subcomment = new FredJMenuItem("Subject Comments");
        teachercomment = new FredJMenuItem("Teacher's Comments");
        principalcomment = new FredJMenuItem("Principal's Comments");
        allocatesubjects = new FredJMenuItem("Allocate Subjects");
        deletemarks = new FredJMenuItem("Delete Marks");
        assignIndex = new FredJMenuItem("Assign K.C.S.E Index Numbers");
        kcseExam = new FredJMenuItem("Enter K.C.S.E Marks");
        examTargets = new FredJMenuItem("Set Exam Targets");
        kcpemarks = new FredJMenuItem("Enter K.C.P.E Marks");
        subjectPaperCombination = new FredJMenuItem("Combine Subject Papers Marks");
        examination.add(entermarks);
        examination.add(missingmarks);
        examination.add(subcomment);
        examination.add(teachercomment);
        examination.add(principalcomment);
        examination.add(kcpemarks);
        examination.add(examTargets);
        examination.add(assignIndex);
        examination.add(kcseExam);
        examination.add(subjectPaperCombination);
        examination.add(deletemarks);
        examination.add(allocatesubjects);

        merit = new FredJMenuItem("Merit List");
        reportform = new FredJMenuItem("Report Forms");
        bestpersub = new FredJMenuItem("Best Student Per Subject");
        mostimprovedpersub = new FredJMenuItem("Most Improved Per Subect");
        mostimproved = new FredJMenuItem("Most Improved Overall");
        bestoverall = new FredJMenuItem("Best Student Overall");
        gradedistribution = new FredJMenuItem("Grade Distribution");
        studentdetails = new FredJMenuItem("View Student Details");
        generalclasslist = new FredJMenuItem("General Class List");
        subjectclasslist = new FredJMenuItem("Subject Class List");
        teacherdet = new FredJMenuItem("Teachers Details");
        subjectVap = new FredJMenuItem("View Subject V.A.Ps");
        photoViewer = new FredJMenuItem("Preview Student Images");
        reports.add(merit);
        reports.add(reportform);
        reports.add(bestoverall);
        reports.add(bestpersub);
        reports.add(mostimproved);
        reports.add(mostimprovedpersub);
        reports.add(gradedistribution);
        reports.add(subjectVap);
        reports.add(generalclasslist);
        reports.add(subjectclasslist);
        reports.add(photoViewer);
        reports.add(studentdetails);
        reports.add(teacherdet);

        smsparents = new FredJMenuItem("SMS Parents");
        smsresults = new FredJMenuItem("SMS Exam Results");
        smsstaff = new FredJMenuItem("SMS Staff");
        smslog = new FredJMenuItem("SMS Log");
        phonebook = new FredJMenuItem("Phone Book Immitation");
        onlineSms = new FredJMenuItem("Online Sent SMS");
        onlineOutbox = new FredJMenuItem("Online Failed SMS");
        smsForeign = new FredJMenuItem("SMS Unknown Contact");
        recharge = new FredJMenuItem("SMS Recharge Procedure");
        comm.add(phonebook);
        comm.add(smsparents);
        comm.add(smsresults);
        comm.add(smsstaff);
        comm.add(smsForeign);
        comm.add(smslog);
        comm.add(onlineSms);
        comm.add(onlineOutbox);
        comm.add(recharge);
        recharge.addActionListener(this);

        applygradesys = new FredJMenuItem("Apply Grading System/Weight To Entered Marks");
        examanalysis = new FredJMenuItem("Perform Exam Analysis");
        combine = new FredJMenuItem("Combine Exam Marks");
        classtranfer = new FredJMenuItem("Class Migration");
        mangeuser = new FredJMenuItem("Manage Users");
        settings = new FredJMenuItem("System SetUp");
        schoolreg = new FredJMenuItem("Register School Details");
        account = new FredJMenuItem("Create User Account");
        termmigration = new FredJMenuItem("Term Migration");
        assignesubjects = new FredJMenuItem("Assign Subject Rights");
        opening = new FredJMenuItem("Configure Term Dates ");
        timetable = new FredJMenuItem("Open Time Table Program");
        fee = new FredJMenuItem("Configure Next Term Fee");
        databaseSize = new FredJMenuItem("Querry Database Size");

        sync = new FredJMenuItem("Syncronize with Marks Pro");

        masters.add(account);
        masters.add(mangeuser);
        masters.add(applygradesys);
        masters.add(examanalysis);
        masters.add(kcpeanalysis);
        masters.add(combine);
        masters.add(termmigration);
        masters.add(classtranfer);
        masters.add(settings);
        masters.add(assignesubjects);
        masters.add(opening);
        masters.add(timetable);
        masters.add(schoolreg);

        masters.add(databaseSize);
        // masters.add(sync);
        logout = new FredJMenuItem("Log Out");
        changepass = new FredJMenuItem("Change Password");
        returnhome = new FredJMenuItem("Return Home");
        myaccount.add(logout);
        myaccount.add(changepass);
        myaccount.add(returnhome);

        examreg.addActionListener(this);
        examweighreg.addActionListener(this);
        meangradereg.addActionListener(this);
        gradesysreg.addActionListener(this);
        entermarks.addActionListener(this);
        classreg.addActionListener(this);
        streamreg.addActionListener(this);
        missingmarks.addActionListener(this);
        allocatesubjects.addActionListener(this);
        examanalysis.addActionListener(this);
        merit.addActionListener(this);
        smsresults.addActionListener(this);
        subcomment.addActionListener(this);
        principalcomment.addActionListener(this);
        studentdetails.addActionListener(this);
        settings.addActionListener(this);
        account.addActionListener(this);
        smsparents.addActionListener(this);
        smsstaff.addActionListener(this);
        teachercomment.addActionListener(this);
        smslog.addActionListener(this);
        schoolreg.addActionListener(this);
        mangeuser.addActionListener(this);
        classtranfer.addActionListener(this);
        assignesubjects.addActionListener(this);
        logout.addActionListener(this);
        changepass.addActionListener(this);
        returnhome.addActionListener(this);
        termmigration.addActionListener(this);
        gradedistribution.addActionListener(this);
        bestoverall.addActionListener(this);
        bestpersub.addActionListener(this);
        deletemarks.addActionListener(this);
        combine.addActionListener(this);
        reportform.addActionListener(this);
        subjectclasslist.addActionListener(this);
        generalclasslist.addActionListener(this);
        regteacher.addActionListener(this);
        regstudent.addActionListener(this);
        applygradesys.addActionListener(this);
        opening.addActionListener(this);
        timetable.addActionListener(this);
        fee.addActionListener(this);
        kcseExam.addActionListener(this);
        kcpemarks.addActionListener(this);
        attend.addActionListener(this);
        attendanceReport.addActionListener(this);
        mostimproved.addActionListener(this);
        mostimprovedpersub.addActionListener(this);
        teacherdet.addActionListener(this);
        assignIndex.addActionListener(this);
        databaseSize.addActionListener(this);
        kcpeanalysis.addActionListener(this);
        compreg.addActionListener(this);
        onlineSms.addActionListener(this);
        smsForeign.addActionListener(this);
        phonebook.addActionListener(this);
        examTargets.addActionListener(this);
        sync.addActionListener(this);
        subjectVap.addActionListener(this);
        subjectPaperCombination.addActionListener(this);
        onlineOutbox.addActionListener(this);
        photoViewer.addActionListener(this);
        ImageIcon icon = new ImageIcon(getClass().getResource("/icons/Settings-icon.png"));
        settings.setIcon(icon);
        add(footpanel, BorderLayout.SOUTH);
        footpanel.add(infor, "gapleft 100");
        if (Globals.Level.equalsIgnoreCase("Normal")) {
            masters.setEnabled(false);
            comm.setEnabled(false);

        }
        CurrentFrame.jframeonIntialiser(this);

        timeOutChecker = new Thread() {
            @Override
            public void run() {
                startTime = System.currentTimeMillis();
                for (; ; ) {
                    long currenttime = System.currentTimeMillis();
                    if (currenttime - startTime > timeOut) {
                        ExamHomePanel.logout.doClick();

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
        if (obj == returnhome) {
            centerpanel.removeAll();
            centerpanel.add(overPanel, "grow,push");
            centerpanel.revalidate();
            centerpanel.repaint();
        }
        if (obj == timetable) {
            try {
                Runtime run = Runtime.getRuntime();
                Process p = run.exec("C:\\Program Files (x86)\\TimeTable/roz.exe");

            } catch (IOException ex) {
                JOptionPane.showMessageDialog(CurrentFrame.mainFrame(), "Time Table Application Not Found On Host\n Kindly Install One For This Service");
            }
        }

        if (obj == examanalysis) {
            CurrentFrame.setSecondFrame(new ExamAnalysis());
            CurrentFrame.jframeon(this);
        }
        if (obj == attend) {
            CurrentFrame.setSecondFrame(new AttendanceCapture());
            CurrentFrame.jframeon(this);
        }

        if (obj == onlineSms) {
            CurrentFrame.setSecondFrame(new SentSMS());
            CurrentFrame.jframeon(this);
        }
        if (obj == onlineOutbox) {
            CurrentFrame.setSecondFrame(new SmsOutBox());
            CurrentFrame.jframeon(this);
        }
        if (obj == recharge) {
            CurrentFrame.setSecondFrame(new RechargeDetails());
            CurrentFrame.jframeon(this);
        }
        if (obj == smsForeign) {
            CurrentFrame.setSecondFrame(new SMSForeignNumber());
            CurrentFrame.jframeon(this);
        }
        if (obj == subjectPaperCombination) {
            CurrentFrame.setSecondFrame(new PartialMarksCombination());
            CurrentFrame.jframeon(this);
        }
        if (obj == kcpemarks) {
            CurrentFrame.setSecondFrame(new KCPEMarksEntry());
            CurrentFrame.jframeon(this);
        }
        if (obj == attendanceReport) {
            CurrentFrame.setSecondFrame(new Attendance());
            CurrentFrame.jframeon(this);
        }
        if (obj == photoViewer) {
            CurrentFrame.setSecondFrame(new PhotoViewer());
            CurrentFrame.jframeon(this);
        }
        if (obj == subjectVap) {
            CurrentFrame.setSecondFrame(new SubjectVap());
            CurrentFrame.jframeon(this);
        }
        if (obj == examTargets) {
            CurrentFrame.setSecondFrame(new ExamTargets());
            CurrentFrame.jframeon(this);
        } else if (obj == sync) {

            int option = JOptionPane.showConfirmDialog(this, "Are You Sure That You Really Want To Synchronise With Marks Pro DataBase?", "Confirm Sync", JOptionPane.YES_NO_OPTION);
            if (option == JOptionPane.NO_OPTION) {
                JOptionPane.showMessageDialog(this, "Operation Terminated");
            } else {

                new Thread() {
                    @Override
                    public void run() {
                        MarksProDatabaseSync syncer = new MarksProDatabaseSync();
                        syncer.dataSync();
                    }

                }.start();


            }

        }
        if (obj == databaseSize) {


            Globals.dataBaseSizeQuerryRunner();
        }
        if (obj == mostimproved) {
            CurrentFrame.setSecondFrame(new BestImproved());
            CurrentFrame.jframeon(this);
        }
        if (obj == mostimprovedpersub) {
            CurrentFrame.setSecondFrame(new BestImprovedPerSubject());
            CurrentFrame.jframeon(this);
        }
        if (obj == assignIndex) {
            CurrentFrame.setSecondFrame(new IndexNumbersAssigner());
            CurrentFrame.jframeon(this);
        }
        if (obj == kcpeanalysis) {
            CurrentFrame.setSecondFrame(new KCPEAnalysis());
            CurrentFrame.jframeon(this);
        }
        if (obj == phonebook) {
            CurrentFrame.setSecondFrame(new PhoneBook());
            CurrentFrame.jframeon(this);
        }
        if (obj == kcseExam) {
            CurrentFrame.setSecondFrame(new KCSEMarksEntry());
            CurrentFrame.jframeon(this);
        }
        if (obj == fee) {
            CurrentFrame.setSecondFrame(new FeeReg());
            CurrentFrame.jframeon(this);
        }
        if (obj == applygradesys) {
            CurrentFrame.setSecondFrame(new GradingSystemReview());
            CurrentFrame.jframeon(this);
        }
        if (obj == opening) {
            CurrentFrame.setSecondFrame(new OpeningDate());
            CurrentFrame.jframeon(this);
        }
        if (obj == regstudent) {
            CurrentFrame.setSecondFrame(new studentReg());
            CurrentFrame.jframeon(this);
        }

        if (obj == regteacher) {
            CurrentFrame.setSecondFrame(new TeacherRegistration());
            CurrentFrame.jframeon(this);
        }
        if (obj == subjectclasslist) {
            CurrentFrame.setSecondFrame(new SubjectClassList());
            CurrentFrame.jframeon(this);
        }
        if (obj == generalclasslist) {
            CurrentFrame.setSecondFrame(new ProgramClassList());
            CurrentFrame.jframeon(this);
        }
        if (obj == reportform) {
            CurrentFrame.setSecondFrame(new ReportForms());
            CurrentFrame.jframeon(this);
        }

        if (obj == combine) {
            CurrentFrame.setSecondFrame(new CombineExams());
            CurrentFrame.jframeon(this);
        }
        if (obj == deletemarks) {
            CurrentFrame.setSecondFrame(new MarksDeletion());
            CurrentFrame.jframeon(this);
        }
        if (obj == bestoverall) {
            CurrentFrame.setSecondFrame(new BestStudentOverall());
            CurrentFrame.jframeon(this);
        }
        if (obj == bestpersub) {
            CurrentFrame.setSecondFrame(new BestPerSubject());
            CurrentFrame.jframeon(this);
        }
        if (obj == schoolreg) {
            CurrentFrame.setSecondFrame(new SchoolDetailsReg());
            CurrentFrame.jframeon(this);
        }
        if (obj == gradedistribution) {
            CurrentFrame.setSecondFrame(new GradeDistributionAnalysis());
            CurrentFrame.jframeon(this);
        }
        if (obj == termmigration) {
            CurrentFrame.setSecondFrame(new TermMigration());
            CurrentFrame.jframeon(this);
        }
        if (obj == logout) {
            try {
                Login.firstLaunch = false;
                CurrentFrame.killSecondFrame();

                CurrentFrame.currentWindow();
                CurrentFrame.mainFrame().dispose();
                CurrentFrame.docOpener().dispose();
                ExamHome.timeOutChecker.stop();
                new Login().setVisible(true);
                con.close();
            } catch (SQLException sq) {
                CurrentFrame.currentWindow();
                CurrentFrame.mainFrame().dispose();
                CurrentFrame.docOpener().dispose();
            }

        } else if (obj == mangeuser) {
            CurrentFrame.setSecondFrame(new UserRights());
            CurrentFrame.jframeon(this);

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
        } else if (obj == teacherdet) {

            new Thread() {
                @Override
                public void run() {
                    JWindow dia = new JWindow();
                    JProgressBar bar = new JProgressBar();
                    bar.setIndeterminate(true);

                    dia.setSize(300, 60);

                    bar.setBorder(new TitledBorder("Fetching Teachers Details"));
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
        } else if (obj == classtranfer) {
            CurrentFrame.setSecondFrame(new ClassMigration());
            CurrentFrame.jframeon(this);

        } else if (obj == assignesubjects) {
            CurrentFrame.setSecondFrame(new SubjectAssigner());
            CurrentFrame.jframeon(this);

        }
        if (obj == changepass) {
            CurrentFrame.setSecondFrame(new PasswordChange());
            CurrentFrame.jframeon(this);
        }
        if (obj == settings) {
            CurrentFrame.setSecondFrame(new Configurations());
            CurrentFrame.jframeon(this);
        }
        if (obj == teachercomment) {
            CurrentFrame.setSecondFrame(new ClassTeachersComments());
            CurrentFrame.jframeon(this);
        }
        if (obj == smsparents) {
            CurrentFrame.setSecondFrame(new SmsAllParents());
            CurrentFrame.jframeon(this);
        }
        if (obj == smsstaff) {
            CurrentFrame.setSecondFrame(new SmsStaff());
            CurrentFrame.jframeon(this);
        }
        if (obj == account) {
            CurrentFrame.setSecondFrame(new UserAccount());
            CurrentFrame.jframeon(this);
        } else if (obj == studentdetails) {
            new Thread() {
                @Override
                public void run() {
                    JWindow dia = new JWindow();
                    JProgressBar bar = new JProgressBar();
                    bar.setIndeterminate(true);

                    dia.setSize(300, 60);

                    bar.setBorder(new TitledBorder("Fetching School Register"));
                    dia.setAlwaysOnTop(true);
                    dia.setLocationRelativeTo(CurrentFrame.mainFrame());
                    dia.setIconImage(FrameProperties.icon());
                    dia.add(bar);
                    dia.setVisible(true);
                    centerpanel.removeAll();
                    revalidate();
                    repaint();
                    StudentView ff = new StudentView();

                    centerpanel.add(ff.studentManger(), "grow,push");
                    revalidate();
                    repaint();
                    dia.dispose();
                }

            }.start();
        }

        if (obj == compreg) {
            studentdetails.doClick();
        } else if (obj == principalcomment) {
            CurrentFrame.setSecondFrame(new PrincipalsComments());
            CurrentFrame.jframeon(this);
        } else if (obj == subcomment) {
            CurrentFrame.setSecondFrame(new SubjectComment());
            CurrentFrame.jframeon(this);
        }
        if (obj == examreg) {
            CurrentFrame.setSecondFrame(new ExamRegistration());
            CurrentFrame.jframeon(this);
        }
        if (obj == merit) {
            CurrentFrame.setSecondFrame(new MeritList());
            CurrentFrame.jframeon(this);
        }
        if (obj == smsresults) {
            CurrentFrame.setSecondFrame(new SmsParentExamResults());
            CurrentFrame.jframeon(this);
        }
        if (obj == missingmarks) {
            CurrentFrame.setSecondFrame(new MissingMarks());
            CurrentFrame.jframeon(this);
        }
        if (obj == examweighreg) {
            CurrentFrame.setSecondFrame(new ExamWeightRegistration());
            CurrentFrame.jframeon(this);
        }
        if (obj == allocatesubjects) {
            CurrentFrame.setSecondFrame(new StudentSubjectAllocation());
            CurrentFrame.jframeon(this);
        }
        if (obj == meangradereg) {

            new Thread() {
                @Override
                public void run() {
                    JWindow dia = new JWindow();
                    JProgressBar bar = new JProgressBar();
                    bar.setIndeterminate(true);

                    dia.setSize(300, 60);

                    bar.setBorder(new TitledBorder("Retrieving Current Mean Grading System"));
                    dia.setAlwaysOnTop(true);
                    dia.setLocationRelativeTo(CurrentFrame.mainFrame());
                    dia.setIconImage(FrameProperties.icon());
                    dia.add(bar);
                    dia.setVisible(true);
                    CurrentFrame.setSecondFrame(new MeanGradeConfiguration());
                    CurrentFrame.jframeon(CurrentFrame.mainFrame());
                    dia.dispose();
                }

            }.start();

        }
        if (obj == gradesysreg) {

            new Thread() {
                @Override
                public void run() {
                    JWindow dia = new JWindow();
                    JProgressBar bar = new JProgressBar();
                    bar.setIndeterminate(true);

                    dia.setSize(300, 60);

                    bar.setBorder(new TitledBorder("Retrieving Current Subject Grading System"));
                    dia.setAlwaysOnTop(true);
                    dia.setLocationRelativeTo(CurrentFrame.mainFrame());
                    dia.setIconImage(FrameProperties.icon());
                    dia.add(bar);
                    dia.setVisible(true);
                    CurrentFrame.setSecondFrame(new GradingSystemReg());
                    CurrentFrame.jframeon(CurrentFrame.mainFrame());
                    dia.dispose();
                }

            }.start();

        }
        if (obj == classreg) {
            CurrentFrame.setSecondFrame(new ClassReg());
            CurrentFrame.jframeon(this);
        }
        if (obj == streamreg) {

        }
        if (obj == entermarks) {
            CurrentFrame.setSecondFrame(new MarksEntry());
            CurrentFrame.jframeon(this);
        }
    }

}
