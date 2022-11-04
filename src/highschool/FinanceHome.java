/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package highschool;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
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
import java.util.logging.Level;
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
public class FinanceHome extends JFrame implements ActionListener {

    public static Thread timeOutChecker;
    private static long startTime;
    private static int timeOut;

    public static void main(String[] args) {
        new FinanceHome();
    }

    JScrollPane scrollers = new JScrollPane();

    private JMenuBar nav;
    private JPanel footpanel;
    private FredLabel infor;
    private FredJMenu reg;
    private FredJMenu reports;
    private FredJMenu masters;
    private FredJMenu communication;
    private FredJMenu payments;
    private FredJMenu fees;
    private FredJMenu expensis;
    private FredJMenu help;
    private Toolkit tk = Toolkit.getDefaultToolkit();
    private int height = (int) tk.getScreenSize().getHeight();
    private int width = (int) tk.getScreenSize().getWidth();
    private PreparedStatement ps;
    private Connection con;
    private ResultSet rs;

    private FredJMenuItem studentReg;
    private FredJMenuItem workersReg;
    private FredJMenuItem departmentReg;
    private FredJMenuItem classReg;
    private FredJMenuItem streamReg;
    private FredJMenuItem termReg;
    private FredJMenuItem countyReg, countryreg;
    private FredJMenuItem wardReg;
    private FredJMenuItem provinceReg;
    private FredJMenuItem constituencyReg;
    private FredJMenuItem studentupi;


    private FredJMenuItem classList;

    private FredJMenuItem workersReport;

    private FredJMenuItem studentdetreport;

    @SuppressWarnings("FieldMayBeFinal")
    private FredJMenuItem cashbook;

    private FredJMenuItem userAccount;
    private FredJMenuItem schDetails;
    private FredJMenuItem setup;
    private FredJMenuItem classtransfer;
    private FredJMenuItem userManage;
    private FredJMenuItem termTransfer;
    private FredJMenuItem sync;
    private FredJMenuItem salarystaffsetup;
    private FredJMenuItem systemsetup;

    private FredJMenuItem addvotehead;
    private FredJMenuItem accounttranfer, gokDonation, moneyTransfer, income;

    private FredJMenuItem smsParents;
    private FredJMenuItem smsSpecific;
    private FredJMenuItem smsfeebal;
    private FredJMenuItem smsForeign;
    private FredJMenuItem smsStaff;
    private FredJMenuItem smsLogmenuitem, phoneBook;

    private FredJMenuItem recievefee;
    private FredJMenuItem feestructure;
    private FredLabel infor2 = new FredLabel();
    String title = "";
    private FredJMenuItem feebal;
    private FredJMenuItem recieptreprinter;

    private FredJMenuItem spend;
    private FredJMenuItem viewPayment;
    private JPanel left;
    private JPanel right;
    private JPanel holder;


    private FredJMenuItem expensisre;
    private FredJMenuItem expcategory;
    FredJMenuItem trialbalance = new FredJMenuItem("Trial Balance Report");
    private FredJMenuItem feeFigure = new FredJMenuItem("Collective Fee Figure Per Class");

    HomePanel pane = new HomePanel();
    private FredJMenuItem systemHelp;
    private FredButton home;
    private FredJMenuItem program = new FredJMenuItem("Program ClassList");
    private FredJMenuItem feeregister = new FredJMenuItem("Fee Register");
    private FredButton managepay, managestudent, managefee, manageexpenses, financeclear, manageworker, managesalary, viewinvoices, custompay, manualinvoice;

    @SuppressWarnings("LeakingThisInConstructor")
    public FinanceHome() {
        Globals.SortIntialiser();
        this.setExtendedState(MAXIMIZED_BOTH);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        setLayout(new BorderLayout());
        reg = new FredJMenu("Registration");
        manageexpenses = new FredButton("Manage Expenses");
        managefee = new FredButton("Fee Collection Report");
        managestudent = new FredButton("Manage Students");
        manageworker = new FredButton("Manage Staff");
        viewinvoices = new FredButton("View Invoices");
        managepay = new FredButton("Manage Custom Payments");
        custompay = new FredButton("Create Custom Pay");
        manualinvoice = new FredButton("Bill Students");
        financeclear = new FredButton("Alumni Clearance");
        sync = new FredJMenuItem("Synchronize With Online Server");
        feestructure = new FredJMenuItem("Fee Structures");
        phoneBook = new FredJMenuItem("Phone Book Immitation");
        scrollers.setVerticalScrollBarPolicy(VERTICAL_SCROLLBAR_ALWAYS);
        nav = new JMenuBar();
        footpanel = new JPanel();
        infor = new FredLabel("THIS SOFTWARE IS DESIGNED, DISTRIBUTED & MAINTAINED BY LUNAR TECH. SOLUTION CONTACT CHIEF DEVELOPER @ O707353225");
        infor.setFont(new Font("serif", Font.BOLD, 11));
        footpanel.setBackground(Color.WHITE);
        footpanel.setLayout(new MigLayout());
        nav.setLayout(new GridLayout(1, 5));
        add(nav, BorderLayout.NORTH);

        add(footpanel, BorderLayout.SOUTH);
        footpanel.add(infor, "gapleft 50,grow,push");
        footpanel.add(infor2);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        getContentPane().setBackground(Color.CYAN);
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
        holder = new JPanel();
        left = new JPanel();
        right = new JPanel();
        right.setLayout(new MigLayout());

        JScrollPane scrolls = new JScrollPane(holder);
        scrolls.setVerticalScrollBarPolicy(VERTICAL_SCROLLBAR_ALWAYS);
        // scrolls.setHorizontalScrollBarPolicy(HORIZONTAL_SCROLLBAR_ALWAYS);
        add(scrolls, BorderLayout.CENTER);
        holder.setLayout(new MigLayout());
        holder.add(left, "growy,pushy");
        holder.add(right, "grow,push");
        left.setBackground(Color.lightGray);
        reg = new FredJMenu("Registration");
        nav.add(reg);
        fees = new FredJMenu("Fee Balances");
        nav.add(fees);
        payments = new FredJMenu("Payments");
        communication = new FredJMenu("Communication");
        masters = new FredJMenu("Masters");
        reports = new FredJMenu("Reports");
        expensis = new FredJMenu("Expenses");
        help = new FredJMenu("System Help");

        gokDonation = new FredJMenuItem("Receive From G.O.K");
        moneyTransfer = new FredJMenuItem("Inter Account Fund Tranfer");
        nav.add(payments);
        //  nav.add(expensis);
        nav.add(communication);
        nav.add(reports);
        nav.add(masters);
        nav.add(help);
        right.add(pane.rightPanel(), "grow,push");
        try {

            con = DbConnection.connectDb();
            String sql = "Select Name from schoolDetails";
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            if (rs.next()) {
                setTitle(rs.getString("Name") + " Finance Management Software -   " + Globals.CurrentUser + "  " + Globals.activationStatus);
                title = rs.getString("Name") + " Finance Management Software -   " + Globals.CurrentUser + "  " + Globals.activationStatus;
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
        countryreg = new FredJMenuItem("Country Registration");
        streamReg = new FredJMenuItem("Stream Registration");
        studentReg = new FredJMenuItem("Student Registration");
        classReg = new FredJMenuItem("Class Registration");
        termReg = new FredJMenuItem("Term Registration");
        workersReg = new FredJMenuItem("Staff Registration");
        departmentReg = new FredJMenuItem("Department Registration");
        provinceReg = new FredJMenuItem("Province Registration");
        countyReg = new FredJMenuItem("County Registration");
        constituencyReg = new FredJMenuItem("Constituency Registration");
        wardReg = new FredJMenuItem("Ward Registration");
        studentupi = new FredJMenuItem("Assign Student UPI");

        reg.add(studentReg);
        reg.add(studentupi);
        ;
        reg.add(classReg);
        ;
        reg.add(workersReg);
        reg.add(departmentReg);
        ;
        reg.add(countryreg);
        reg.add(provinceReg);
        reg.add(countyReg);
        reg.add(constituencyReg);
        reg.add(wardReg);

        cashbook = new FredJMenuItem("Cash Book Report");
        feebal = new FredJMenuItem("View Fee Balance");
        recievefee = new FredJMenuItem("Receive School Fee");
        recieptreprinter = new FredJMenuItem("Receipt Reprinter");
        fees.add(recievefee);
        fees.add(feebal);
        fees.add(recieptreprinter);
        // fees.add(feestructure);

        expensisre = new FredJMenuItem("Votehead Payment Transactio Report");
        expensis.add(expensisre);
        spend = new FredJMenuItem("Record Expenditure");
        expcategory = new FredJMenuItem("Create Expenditure Category");
        //expensis.add(expcategory);

        smsParents = new FredJMenuItem("SMS All Parents");
        smsSpecific = new FredJMenuItem("SMS Specific Parents");
        smsfeebal = new FredJMenuItem("SMS Fee Balances");
        smsForeign = new FredJMenuItem("SMS Foreign Number");
        smsStaff = new FredJMenuItem("SMS Staff");
        smsLogmenuitem = new FredJMenuItem("SMS Log");
        communication.add(phoneBook);
        communication.add(smsParents);
        communication.add(smsParents);
        communication.add(smsfeebal);
        communication.add(smsForeign);
        communication.add(smsStaff);
        communication.add(smsLogmenuitem);
        classList = new FredJMenuItem("Class Lists");

        workersReport = new FredJMenuItem("Staff Reports");

        studentdetreport = new FredJMenuItem("View Student Details");

        reports.add(classList);
        reports.add(program);
        reports.add(feeFigure);
        reports.add(studentdetreport);

        reports.add(workersReport);
        reports.add(feeregister);
        reports.add(trialbalance);


        reports.add(cashbook);
        userAccount = new FredJMenuItem("Create A User Account");
        userManage = new FredJMenuItem("Manage Users");
        setup = new FredJMenuItem("Set Fee Votehead Amounts");
        schDetails = new FredJMenuItem("Register School Details");
        classtransfer = new FredJMenuItem("Class Migration");
        termTransfer = new FredJMenuItem("Term Transfer");
        addvotehead = new FredJMenuItem("Add VoteHead");
        income = new FredJMenuItem("Receive External Income");

        salarystaffsetup = new FredJMenuItem("Review Job Group Salary");
        home = new FredButton("Resume Home");
        systemsetup = new FredJMenuItem("System SetUp");
        accounttranfer = new FredJMenuItem("Account Fund Transfer");
        masters.add(userAccount);
        masters.add(userManage);
        masters.add(setup);
        masters.add(schDetails);
        //   masters.add(salarystaffsetup);
        masters.add(termTransfer);
        masters.add(classtransfer);
        masters.add(sync);
        masters.add(systemsetup);
        masters.add(addvotehead);
        masters.add(accounttranfer);
        masters.add(gokDonation);
        masters.add(income);


        systemHelp = new FredJMenuItem("(Remote Assistance)Open Team Viewer");
        recieptreprinter.addActionListener(this);
        smsLogmenuitem.addActionListener(this);
        help.add(systemHelp);
        userManage.addActionListener(this);
        smsStaff.addActionListener(this);
        cashbook.addActionListener(this);

        payments.add(spend);
        payments.add(expensisre);
        smsForeign.addActionListener(this);

        expensisre.addActionListener(this);
        smsParents.addActionListener(this);

        smsfeebal.addActionListener(this);
        systemHelp.addActionListener(this);

        systemsetup.addActionListener(this);
        workersReport.addActionListener(this);
        studentdetreport.addActionListener(this);

        manageexpenses.addActionListener(this);
        userAccount.addActionListener(this);

        home.addActionListener(this);
        viewinvoices.addActionListener(this);
        program.addActionListener(this);
        feeregister.addActionListener(this);
        feeFigure.addActionListener(this);
        manageworker.addActionListener(this);
        salarystaffsetup.addActionListener(this);
        financeclear.addActionListener(this);
        managestudent.addActionListener(this);
        classList.addActionListener(this);
        managepay.addActionListener(this);
        studentReg.addActionListener(this);
        termTransfer.addActionListener(this);
        recievefee.addActionListener(this);
        countyReg.addActionListener(this);
        wardReg.addActionListener(this);
        provinceReg.addActionListener(this);
        constituencyReg.addActionListener(this);
        countryreg.addActionListener(this);
        classtransfer.addActionListener(this);
        classReg.addActionListener(this);
        setup.addActionListener(this);
        custompay.addActionListener(this);
        manualinvoice.addActionListener(this);
        workersReg.addActionListener(this);
        departmentReg.addActionListener(this);
        schDetails.addActionListener(this);
        expcategory.addActionListener(this);
        spend.addActionListener(this);
        feebal.addActionListener(this);
        managefee.addActionListener(this);
        feestructure.addActionListener(this);

        sync.addActionListener(this);
        addvotehead.addActionListener(this);
        gokDonation.addActionListener(this);

        phoneBook.addActionListener(this);
        ;
        accounttranfer.addActionListener(this);
        trialbalance.addActionListener(this);
        income.addActionListener(this);
        left.setLayout(new MigLayout());
        left.add(home, "gapleft 20,gapright 20,shrink,grow,push,wrap");
        left.add(managefee, "gapleft 20,gapright 20,gaptop 20,shrink,grow,push,wrap");
        left.add(managestudent, "gapleft 20,gapright 20,gaptop 20,shrink,grow,push,wrap");
        // left.add(manageexpenses, "gapleft 20,gapright 20,gaptop 20,shrink,grow,push,wrap");
        // left.add(managepay, "gapleft 20,gapright 20,gaptop 20,shrink,grow,push,wrap");
        left.add(manualinvoice, "gapleft 20,gapright 20,gaptop 20,shrink,grow,push,wrap");
        left.add(manageworker, "gapleft 20,gapright 20,gaptop 20,shrink,grow,push,wrap");
        //left.add(custompay, "gapleft 20,gapright 20,gaptop 20,shrink,grow,push,wrap");
        left.add(viewinvoices, "gapleft 20,gapright 20,gaptop 20,shrink,grow,push,wrap");
        left.add(financeclear, "gapleft 20,gapright 20,gaptop 20,shrink,grow,push,wrap");


        timeOutChecker = new Thread() {
            @Override
            public void run() {
                startTime = System.currentTimeMillis();
                for (; ; ) {
                    long currenttime = System.currentTimeMillis();
                    if (currenttime - startTime > timeOut) {
                        HomePanel.logout.doClick();
                        JOptionPane.showMessageDialog(null, "No activity within " + timeOut + "seconds; please log in again.", "Message", JOptionPane.INFORMATION_MESSAGE);
                    }
                    long t = (timeOut - (currenttime - startTime)) / 1000;
                    infor2.setText("SYSTEM TIME OUT REMAINING : ".toUpperCase() + String.valueOf(t) + " (Sec)");

                    CurrentFrame.mainFrame().setTitle(title + "                        " + "SYSTEM TIME OUT REMAINING : ".toUpperCase() + String.valueOf(t) + " (Sec)");

                    try {
                        sleep(1000);
                        CurrentFrame.secondFrame().addMouseMotionListener(new MouseMotionListener() {
                            @Override
                            public void mouseDragged(MouseEvent e) {
                                startTime = System.currentTimeMillis();

                            }

                            @Override
                            public void mouseMoved(MouseEvent e) {
                                startTime = System.currentTimeMillis();

                            }
                        });

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

                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }

                }
            }
        };

        timeOutChecker.start();
        if (Globals.Level.equalsIgnoreCase("Normal")) {
            masters.setEnabled(false);
        }
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object obj = e.getSource();
        if (obj == accounttranfer) {

            CurrentFrame.setSecondFrame(new InterAccountFundTransfer());
            CurrentFrame.jframeon(this);
        }

        if (obj == studentReg) {
            CurrentFrame.setSecondFrame(new studentReg());
            CurrentFrame.jframeon(this);
        }
        if (obj == sync) {
            CurrentFrame.setSecondFrame(new Synchronization());
            CurrentFrame.jframeon(this);
        }
        if (obj == program) {
            CurrentFrame.setSecondFrame(new ProgramClassList());
            CurrentFrame.jframeon(this);
        }
        if (obj == feeFigure) {
            CurrentFrame.setSecondFrame(new CollectiveFeeFigure());
            CurrentFrame.jframeon(this);
        }

        if (obj == phoneBook) {
            CurrentFrame.setSecondFrame(new PhoneBook());
            CurrentFrame.jframeon(this);
        }


        if (obj == workersReport) {
            manageworker.doClick();
        }

        if (obj == studentdetreport) {
            managestudent.doClick();
        }

        if (obj == systemsetup) {
            CurrentFrame.setSecondFrame(new Configurations());
            CurrentFrame.jframeon(this);
        } else if (obj == smsForeign) {
            if (RightsAnnouncer.communication()) {
                getToolkit().beep();
                CurrentFrame.setSecondFrame(new SMSForeignNumber());
                CurrentFrame.jframeon(this);
            } else {
                JOptionPane.showMessageDialog(this, "You Do Not Have The Necessary Rights\n To Perform This Action,Consult Administrator", "Access Denied", JOptionPane.INFORMATION_MESSAGE);
            }

        } else if (obj == userAccount) {
            CurrentFrame.setSecondFrame(new UserAccount());
            CurrentFrame.jframeon(this);
        } else if (obj == userManage) {
            CurrentFrame.setSecondFrame(new UserRights());
            CurrentFrame.jframeon(this);
        } else if (obj == termTransfer) {
            CurrentFrame.setSecondFrame(new TermMigration());
            CurrentFrame.jframeon(this);
        } else if (obj == feeregister) {
            CurrentFrame.setSecondFrame(new FeeRegister());
            CurrentFrame.jframeon(this);
        } else if (obj == addvotehead) {
            CurrentFrame.setSecondFrame(new VoteHeadAdd());
            CurrentFrame.jframeon(this);
        } else if (obj == income) {
            CurrentFrame.setSecondFrame(new IncomeReceiver());
            CurrentFrame.jframeon(this);
        } else if (obj == trialbalance) {
            CurrentFrame.setSecondFrame(new TrialBalance());
            CurrentFrame.jframeon(this);
        } else if (obj == smsParents) {

            if (RightsAnnouncer.communication()) {
                CurrentFrame.setSecondFrame(new SmsAllParents());
                CurrentFrame.jframeon(this);
            } else {
                JOptionPane.showMessageDialog(this, "You Do Not Have The Necessary Rights\n To Perform This Action,Consult Administrator", "Access Denied", JOptionPane.INFORMATION_MESSAGE);
            }

        } else if (obj == smsStaff) {

            if (RightsAnnouncer.communication()) {
                CurrentFrame.setSecondFrame(new SmsStaff());
                CurrentFrame.jframeon(this);
            } else {
                JOptionPane.showMessageDialog(this, "You Do Not Have The Necessary Rights\n To Perform This Action,Consult Administrator", "Access Denied", JOptionPane.INFORMATION_MESSAGE);
            }

        } else if (obj == provinceReg) {
            CurrentFrame.setSecondFrame(new ProvinceReg());
            CurrentFrame.jframeon(this);
        } else if (obj == gokDonation) {
            CurrentFrame.setSecondFrame(new RecieveFromGOK());
            CurrentFrame.jframeon(this);
        } else if (obj == countyReg) {
            CurrentFrame.setSecondFrame(new CountyReg());
            CurrentFrame.jframeon(this);
        } else if (obj == wardReg) {
            CurrentFrame.setSecondFrame(new WardReg());
            CurrentFrame.jframeon(this);
        } else if (obj == constituencyReg) {
            CurrentFrame.setSecondFrame(new ConstituencyReg());
            CurrentFrame.jframeon(this);
        } else if (obj == recievefee) {
            if (RightsAnnouncer.recievePaymentRights()) {
                CurrentFrame.setSecondFrame(new RecievePayment());
                CurrentFrame.jframeon(this);
            } else {
                JOptionPane.showMessageDialog(this, "You Do Not Have The Necessary Rights\n To Perform This Action,Consult Administrator", "Access Denied", JOptionPane.INFORMATION_MESSAGE);
            }

        } else if (obj == smsfeebal) {
            if (RightsAnnouncer.communication()) {
                CurrentFrame.setSecondFrame(new SmsFeeBalances());
                CurrentFrame.jframeon(this);
            } else {
                JOptionPane.showMessageDialog(this, "You Do Not Have The Necessary Rights\n To Perform This Action,Consult Administrator", "Access Denied", JOptionPane.INFORMATION_MESSAGE);
            }

        } else if (obj == countryreg) {
            CurrentFrame.setSecondFrame(new CountryReg());
            CurrentFrame.jframeon(this);
        } else if (obj == classtransfer) {
            CurrentFrame.setSecondFrame(new ClassMigration());
            CurrentFrame.jframeon(this);
        } else if (obj == classReg) {
            CurrentFrame.setSecondFrame(new ClassReg());
            CurrentFrame.jframeon(this);
        } else if (obj == setup) {
            CurrentFrame.setSecondFrame(new VoteHeadFeeConfiguration());
            CurrentFrame.jframeon(this);
        } else if (obj == manualinvoice) {
            CurrentFrame.setSecondFrame(new ManualFeeInvoice());
            CurrentFrame.jframeon(this);
        } else if (obj == systemHelp) {
            try {
                Runtime run = Runtime.getRuntime();
                Process p = run.exec("C:\\Program Files (x86)\\TeamViewer\\TeamViewer.exe");
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(CurrentFrame.mainFrame(), "Team Viewer Application Not Found On Host\n Kindly Install One For This Service");
            }
        } else if (obj == custompay) {
            CurrentFrame.setSecondFrame(new ContributionReg());
            CurrentFrame.jframeon(this);
        } else if (obj == workersReg) {
            CurrentFrame.setSecondFrame(new StaffRegistration());
            CurrentFrame.jframeon(this);
        } else if (obj == departmentReg) {
            CurrentFrame.setSecondFrame(new DepartmentReg());
            CurrentFrame.jframeon(this);
        } else if (obj == schDetails) {
            CurrentFrame.setSecondFrame(new SchoolDetailsReg());
            CurrentFrame.jframeon(this);
        } else if (obj == classList) {
            CurrentFrame.setSecondFrame(new ClassListGenerator());
            CurrentFrame.jframeon(this);
        } else if (obj == expcategory) {
            CurrentFrame.setSecondFrame(new Expensis());
            CurrentFrame.jframeon(this);
        } else if (obj == spend) {
            if (RightsAnnouncer.expenseRecord()) {
                CurrentFrame.setSecondFrame(new Spend());
                CurrentFrame.jframeon(this);
            } else {
                JOptionPane.showMessageDialog(this, "You Do Not Have The Necessary Rights\n To Perform This Action,Consult Administrator", "Access Denied", JOptionPane.INFORMATION_MESSAGE);
            }

        } else if (obj == feebal) {
            new Thread() {
                @Override
                public void run() {
                    JWindow dia = new JWindow();
                    JProgressBar bar = new JProgressBar();
                    bar.setIndeterminate(true);

                    dia.setSize(300, 60);
                    bar.setBorder(new TitledBorder("Fetching School Balances"));

                    dia.setAlwaysOnTop(true);
                    dia.setLocationRelativeTo(CurrentFrame.mainFrame());
                    dia.setIconImage(FrameProperties.icon());
                    dia.add(bar);
                    dia.setVisible(true);
                    right.removeAll();
                    revalidate();
                    repaint();
                    FeeDetails feebal = new FeeDetails();
                    scrollers = new JScrollPane(feebal.feeholder());


                    right.add(scrollers, "grow,push");
                    revalidate();
                    repaint();
                    dia.dispose();
                }

            }.start();

        } else if (obj == managefee) {

            new Thread() {
                @Override
                public void run() {
                    JWindow dia = new JWindow();
                    JProgressBar bar = new JProgressBar();
                    bar.setIndeterminate(true);

                    dia.setSize(300, 60);


                    bar.setBorder(new TitledBorder("Fetching Fee Details"));
                    dia.setAlwaysOnTop(true);
                    dia.setLocationRelativeTo(CurrentFrame.mainFrame());
                    dia.setIconImage(FrameProperties.icon());
                    dia.add(bar);
                    dia.setVisible(true);
                    right.removeAll();
                    revalidate();
                    repaint();
                    ManageFee mm = new ManageFee();

                    right.add(mm.feeManage(), "grow,push");
                    revalidate();
                    repaint();
                    dia.dispose();
                }

            }.start();

        } else if (obj == smsLogmenuitem) {

            new Thread() {
                @Override
                public void run() {
                    JWindow dia = new JWindow();
                    JProgressBar bar = new JProgressBar();
                    bar.setIndeterminate(true);

                    dia.setSize(300, 60);

                    bar.setBorder(new TitledBorder("Fetching SMS Record..."));
                    dia.setAlwaysOnTop(true);
                    dia.setLocationRelativeTo(CurrentFrame.mainFrame());
                    dia.setIconImage(FrameProperties.icon());
                    dia.add(bar);
                    dia.setVisible(true);
                    right.removeAll();
                    revalidate();
                    repaint();
                    SMSLog mm = new SMSLog();

                    right.add(mm.smsLogPanel(), "grow,push");
                    revalidate();
                    repaint();
                    dia.dispose();
                }

            }.start();

        } else if (obj == expensisre) {
            new Thread() {
                @Override
                public void run() {
                    JWindow dia = new JWindow();
                    JProgressBar bar = new JProgressBar();
                    bar.setIndeterminate(true);

                    dia.setSize(300, 60);
                    bar.setBorder(new TitledBorder("Fetching Expenditure Reports"));

                    dia.setAlwaysOnTop(true);
                    dia.setLocationRelativeTo(CurrentFrame.mainFrame());
                    dia.setIconImage(FrameProperties.icon());
                    dia.add(bar);
                    dia.setVisible(true);
                    right.removeAll();
                    revalidate();
                    repaint();
                    ExpenseManager panel = new ExpenseManager();

                    right.add(panel.expensePanel(), "grow,push");
                    revalidate();
                    repaint();
                    dia.dispose();
                }

            }.start();

        } else if (obj == cashbook) {
            CurrentFrame.setSecondFrame(new CashBook());
            CurrentFrame.jframeon(this);
        } else if (obj == manageworker) {
            right.removeAll();
            revalidate();
            repaint();
            StaffManagement manage = new StaffManagement();

            right.add(manage.staffManagerPanel(), "grow,push");
            revalidate();
            repaint();

        } else if (obj == recieptreprinter) {
            right.removeAll();
            revalidate();
            repaint();
            RecieptReprinter reprint = new RecieptReprinter();

            right.add(reprint.reprintRecieptPanel(), "grow,push");
            revalidate();
            repaint();
        } else if (obj == financeclear) {

            new Thread() {
                @Override
                public void run() {
                    JWindow dia = new JWindow();
                    JProgressBar bar = new JProgressBar();
                    bar.setIndeterminate(true);

                    dia.setSize(300, 60);

                    bar.setBorder(new TitledBorder("Fetching Alumni Records"));
                    dia.setAlwaysOnTop(true);
                    dia.setLocationRelativeTo(CurrentFrame.mainFrame());
                    dia.setIconImage(FrameProperties.icon());
                    dia.add(bar);
                    dia.setVisible(true);
                    right.removeAll();
                    revalidate();
                    repaint();
                    FinanceClearance ff = new FinanceClearance();

                    right.add(ff.feeClear(), "grow,push");
                    revalidate();
                    repaint();
                    dia.dispose();
                }

            }.start();

        } else if (obj == managestudent) {

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
                    right.removeAll();
                    revalidate();
                    repaint();
                    StudentView view = new StudentView();

                    right.add(view.studentManger(), "grow,push");
                    revalidate();
                    repaint();
                    dia.dispose();
                }

            }.start();

        } else if (obj == managepay) {

            new Thread() {
                @Override
                public void run() {
                    JWindow dia = new JWindow();
                    JProgressBar bar = new JProgressBar();
                    bar.setIndeterminate(true);

                    dia.setSize(300, 60);

                    bar.setBorder(new TitledBorder("Fetching Custom Pay Records"));
                    dia.setAlwaysOnTop(true);
                    dia.setLocationRelativeTo(CurrentFrame.mainFrame());
                    dia.setIconImage(FrameProperties.icon());
                    dia.add(bar);
                    dia.setVisible(true);
                    right.removeAll();
                    revalidate();
                    repaint();
                    ManageCustomPayments manage = new ManageCustomPayments();

                    right.add(manage.customPaymentHolder(), "grow,push");
                    revalidate();
                    repaint();
                    dia.dispose();
                }

            }.start();

        } else if (obj == home) {
            right.removeAll();
            revalidate();
            repaint();

            right.add(pane.rightPanel(), "grow,push");
            revalidate();
            repaint();

        } else if (obj == viewinvoices) {
            right.removeAll();
            revalidate();
            repaint();

            InvoicesRecord records = new InvoicesRecord();

            right.add(records.invoiceManagerPanel(), "grow,push");
            revalidate();
            repaint();

        }

    }


}
