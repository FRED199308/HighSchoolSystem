/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package highschool;

import java.awt.Color;
import java.awt.Toolkit;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;
import java.io.File;
import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JDialog;

import javax.swing.JPanel;

import net.miginfocom.swing.MigLayout;
import org.icepdf.ri.common.SwingController;
import org.icepdf.ri.common.SwingViewBuilder;

/**
 * @author FRED
 */
public class ReportFrame extends JDialog implements WindowFocusListener {
    private static ReportFrame reportFrame;

    public static ReportFrame getReportFrameInstance(File file2) {

        return reportFrame = new ReportFrame(file2);
    }

    @SuppressWarnings("FieldMayBeFinal")
    private Toolkit kit = Toolkit.getDefaultToolkit();
    private int height = (int) kit.getScreenSize().getHeight(), width = (int) kit.getScreenSize().getWidth();
    private JPanel pane2;

    private ReportFrame(File file) {

        setSize(CurrentFrame.mainFrame().getWidth(), CurrentFrame.mainFrame().getHeight());
        setLocationRelativeTo(null);
        setLayout(new MigLayout());
        this.setIconImage(FrameProperties.icon());
        ((JComponent) getContentPane()).setBorder(
                BorderFactory.createMatteBorder(5, 5, 5, 5, Color.MAGENTA));
        getContentPane().setBackground(Color.cyan);
        addWindowFocusListener(this);
        SwingController controller = new SwingController();
        SwingViewBuilder factory = new SwingViewBuilder(controller);

        factory.buildLastPageButton();
        factory.buildCompleteMenuBar();
        pane2 = factory.buildViewerPanel();
        controller.openDocument(file.getAbsolutePath());

        if (file.toString().endsWith("meritList.pdf")) {

        } else {
            controller.setUtilityPaneVisible(true);
        }
        String z = "1.5";
        controller.setZoom(Float.parseFloat(z));
        this.remove(pane2);
        add(pane2, "grow,push");
        this.requestFocus();
        CurrentFrame.docOpenerIntialiser(this);
        this.setFocusable(true);
        setVisible(true);
    }

    @Override
    public void windowGainedFocus(WindowEvent e) {

    }


    @Override
    public void windowLostFocus(WindowEvent e) {

        this.requestFocus();
        this.requestFocusInWindow();

    }


}
