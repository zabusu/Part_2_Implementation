package hms.GUI;

import hms.GUI.panel.PatientsPanel;
import hms.app.AppContext;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTabbedPane;
import javax.swing.SwingConstants;
import java.awt.BorderLayout;

public final class MainFrame extends JFrame {
    public MainFrame(AppContext ctx) {
        super("Healthcare Management System");

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1100, 700);
        setLocationRelativeTo(null);

        JLabel footer = new JLabel("Data folder: " + ctx.config.patientsCsv().getParent(), SwingConstants.LEFT);

        JTabbedPane tabs = new JTabbedPane();
        tabs.addTab("Patients", new PatientsPanel(ctx.patientService));
        tabs.addTab("Clinicians", placeholder("Clinicians (coming next)"));
        tabs.addTab("Appointments", placeholder("Appointments (coming next)"));
        tabs.addTab("Prescriptions", placeholder("Prescriptions (coming next)"));
        tabs.addTab("Referrals", placeholder("Referrals (coming next)"));

        setLayout(new BorderLayout());
        add(tabs, BorderLayout.CENTER);
        add(footer, BorderLayout.SOUTH);
    }

    private static JLabel placeholder(String text) {
        return new JLabel(text, SwingConstants.CENTER);
    }
}
