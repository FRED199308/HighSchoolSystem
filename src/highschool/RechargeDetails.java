/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package highschool;

import java.awt.Color;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.BorderFactory;
import javax.swing.JComponent;

import static javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE;

/**
 * @author FRED
 */
public class RechargeDetails extends javax.swing.JFrame {

    /**
     * Creates new form RechargeDetails
     */
    public RechargeDetails() {
        initComponents();
        String instructions = "M-Pesa Pay Bill\n" +
                "1.Using your MPesa-enabled phone, select \"Pay Bill\" from the M-Pesa-\n" +
                " menu\n" +
                "2.Enter LUNAR TECH SOLUTION Business Number 4036505\n" +
                "3.Enter your LUNAR TECH SOLUTION Account Number.Your account number-\n" +
                " is " + ConfigurationIntialiser.smsUsername1() + ".sms \n" +
                "4.Enter the Amount of credits you want to buy\n" +
                "5.Confirm that all the details are correct and press Ok\n" +
                "6.Your Account will automatically be credited on your system.\n" +
                "For help call 0707353225";
        instructionHolder.setText(instructions);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        this.setIconImage(FrameProperties.icon());
        ((JComponent) getContentPane()).setBorder(
                BorderFactory.createMatteBorder(5, 5, 5, 5, Color.MAGENTA));
        this.addWindowListener(new WindowAdapter() {

            public void windowClosing(WindowEvent e) {
                CurrentFrame.currentWindow();
                e.getWindow().dispose();
            }
        });
        setTitle("Account Recharge Procedure");
        getContentPane().setBackground(Color.cyan);
        setResizable(false);
        setLayout(null);

        setLocationRelativeTo(null);
        setVisible(true);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        instructionHolder = new javax.swing.JTextArea();
        jButton1 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        instructionHolder.setColumns(20);
        instructionHolder.setFont(new java.awt.Font("Monospaced", 3, 18)); // NOI18N
        instructionHolder.setForeground(new java.awt.Color(255, 102, 102));
        instructionHolder.setRows(5);
        instructionHolder.setText("M-Pesa Pay Bill\n1.Using your MPesa-enabled phone, select \"Pay Bill\" from the M-Pesa-\n menu\n2.Enter LUNAR TECH SOLUTION Business Number 4036505\n3.Enter your LUNAR TECH SOLUTION Account Number.Your account number-\n is KAGMWEA.SMS\n4.Enter the Amount of credits you want to buy\n5.Confirm that all the details are correct and press Ok\n6.Your Account will automatically be credited on your system.\nFor help call 0707353225\n");
        jScrollPane1.setViewportView(instructionHolder);

        jButton1.setText("Close");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(layout.createSequentialGroup()
                                                .addGap(75, 75, 75)
                                                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 804, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGroup(layout.createSequentialGroup()
                                                .addGap(139, 139, 139)
                                                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 157, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addContainerGap(97, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 269, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 27, Short.MAX_VALUE)
                                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed


        CurrentFrame.currentWindow();
        dispose();
    }//GEN-LAST:event_jButton1ActionPerformed

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
            java.util.logging.Logger.getLogger(RechargeDetails.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(RechargeDetails.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(RechargeDetails.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(RechargeDetails.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new RechargeDetails().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextArea instructionHolder;
    private javax.swing.JButton jButton1;
    private javax.swing.JScrollPane jScrollPane1;
    // End of variables declaration//GEN-END:variables
}
