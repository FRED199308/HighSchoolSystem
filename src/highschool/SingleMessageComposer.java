/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package highschool;

import java.awt.Color;
import java.awt.HeadlessException;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JOptionPane;

import static javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE;

import javax.swing.border.TitledBorder;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * @author FRED_ADMIN
 */
public class SingleMessageComposer extends javax.swing.JFrame {

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
            java.util.logging.Logger.getLogger(SMSForeignNumber.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(SMSForeignNumber.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(SMSForeignNumber.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(SMSForeignNumber.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> {
            new SMSForeignNumber().setVisible(true);
        });
    }

    /**
     * Creates new form SMSForeignNumber
     */
    private Connection con;
    private DbConnection db = new DbConnection();
    private PreparedStatement ps;
    private ResultSet rs;


    // Variables declaration - do not modify                     
    private FredButton cancel;
    private FredTextField jPhoneNumber;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextPane message;
    private FredLabel phoneNumber, meslimit;
    private FredButton send;
    // End of variables declaration                   

    public SingleMessageComposer() {
        meslimit = new FredLabel("Character Counter");
        initComponents();
        setSize(650, 400);
        getContentPane().setBackground(Color.CYAN);
        setTitle("Compose Message");
        setLocationRelativeTo(null);
        setResizable(false);
        con = DbConnection.connectDb();

        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        this.addWindowListener(new WindowAdapter() {

            @Override
            public void windowClosing(WindowEvent e) {
                CurrentFrame.secFrame().setEnabled(true);
                try {
                    con.close();
                } catch (SQLException sq) {

                }
                CurrentFrame.currentWindow();
                e.getWindow().dispose();
            }
        });
        send.setOpaque(false);
        this.setIconImage(FrameProperties.icon());
        ((JComponent) getContentPane()).setBorder(
                BorderFactory.createMatteBorder(5, 5, 5, 5, Color.MAGENTA));
        jPhoneNumber.setText(Globals.composerRecipient);


        message.addKeyListener(new KeyAdapter() {

            public void keyReleased(KeyEvent key) {
                String messagePart = "";
                int counter = message.getText().length();
                if (ConfigurationIntialiser.smsOfflineSender()) {
                    if (counter < 161) {
                        messagePart = "1";
                        meslimit.setText(counter + "(" + messagePart + ")");
                    } else if (counter > 160 && counter < 321) {
                        messagePart = "2";
                        meslimit.setText(counter + "(" + messagePart + ")");
                    } else if (counter > 321 && counter < 481) {
                        messagePart = "3";
                        meslimit.setText(counter + "(" + messagePart + ")");
                    } else if (counter > 321 && counter < 481) {
                        messagePart = "4";
                        meslimit.setText(counter + "(" + messagePart + ")");
                    } else if (counter > 641 && counter < 801) {
                        messagePart = "5";
                        meslimit.setText(counter + "(" + messagePart + ")");
                    } else {
                        meslimit.setText(String.valueOf(counter));
                    }
                } else {


                    if (counter < 146) {
                        messagePart = "1";
                        meslimit.setText(counter + "(" + messagePart + ")");
                    } else if (counter > 146 && counter < 306) {
                        messagePart = "2";
                        meslimit.setText(counter + "(" + messagePart + ")");
                    } else if (counter > 306 && counter < 466) {
                        messagePart = "3";
                        meslimit.setText(counter + "(" + messagePart + ")");
                    } else if (counter > 466 && counter < 626) {
                        messagePart = "4";
                        meslimit.setText(counter + "(" + messagePart + ")");
                    } else if (counter > 626 && counter < 786) {
                        messagePart = "5";
                        meslimit.setText(counter + "(" + messagePart + ")");
                    } else {
                        meslimit.setText(String.valueOf(counter));
                    }

                }

            }
        });


        setVisible(true);

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">
    @SuppressWarnings(value = "unchecked")
    private void initComponents() {
        phoneNumber = new FredLabel();
        jPhoneNumber = new FredTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        message = new javax.swing.JTextPane();
        message.setBorder(new TitledBorder("Type Message Here"));
        send = new FredButton("send");
        cancel = new FredButton("Close");

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(null);

        phoneNumber.setText("PHONE NUMBER");
        getContentPane().add(phoneNumber);
        phoneNumber.setBounds(27, 40, 180, 32);

        jPhoneNumber.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jPhoneNumberKeyTyped(evt);
            }
        });
        getContentPane().add(jPhoneNumber);
        jPhoneNumber.setBounds(266, 42, 240, 30);

        jScrollPane1.setViewportView(message);

        getContentPane().add(jScrollPane1);
        jScrollPane1.setBounds(100, 131, 370, 130);
        meslimit.setBounds(450, 30, 150, 40);
        getContentPane().add(meslimit);
        send.setText("Send");
        send.addActionListener(this::sendActionPerformed);
        getContentPane().add(send);
        send.setBounds(87, 279, 150, 31);

        cancel.setText("Cancel");
        cancel.addActionListener(this::cancelActionPerformed);
        getContentPane().add(cancel);
        cancel.setBounds(315, 279, 150, 31);

        pack();
    } // </editor-fold>                        

    private void jPhoneNumberKeyTyped(java.awt.event.KeyEvent evt) {
        char c = evt.getKeyChar();
        if (!Character.isDigit(c)) {
            evt.consume();
            if (c != KeyEvent.VK_BACK_SPACE) {
                getToolkit().beep();
            }
            if (jPhoneNumber.getText().length() > 9) {
                evt.consume();
            }
        }

    }

    private void cancelActionPerformed(java.awt.event.ActionEvent evt) {
        try {
            CurrentFrame.secondFrame.setEnabled(true);
            con.close();
        } catch (SQLException sq) {

        }
        CurrentFrame.currentWindow();
        dispose();        // TODO add your handling code here:
    }

    private void sendActionPerformed(java.awt.event.ActionEvent evt) {
        DataValidation d = new DataValidation();
        boolean comply = false;
        int errorCounter = 2;
        if (DataValidation.phoneValidator(jPhoneNumber.getText()) == false) {
            comply = false;
            JOptionPane.showMessageDialog(this, "kindly Check Your Phone Number Format", "Invalid Phone number", JOptionPane.ERROR_MESSAGE);
        } else {

            comply = true;
            errorCounter--;
        }

        if (message.getText().isEmpty() && comply == true) {
            JOptionPane.showMessageDialog(this, "kindly Type the message", "Empty message", JOptionPane.ERROR_MESSAGE);
            comply = false;
        } else {
            comply = true;
            errorCounter--;
        }
        if (comply == true && errorCounter == 0) {
            String user;

            if (Globals.CurrentUser.equals("null")) {
                user = "Superadmin";

            } else {
                user = Globals.CurrentUser;
            }
            MessageGateway.oneForeignMessageQueue(jPhoneNumber.getText(), message.getText());


        }
    }
}
