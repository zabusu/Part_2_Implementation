package hms.GUI;

import hms.GUI.panel.AppointmentsPanel;
import hms.GUI.panel.CliniciansPanel;
import hms.GUI.panel.PatientsPanel;
import hms.GUI.panel.PrescriptionsPanel;
import hms.GUI.panel.ReferralsPanel;
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
        tabs.addTab("Clinicians", new CliniciansPanel(ctx.referenceDataRepository));
        tabs.addTab("Appointments", new AppointmentsPanel(ctx.appointmentService));
        tabs.addTab("Prescriptions", new PrescriptionsPanel(ctx.prescriptionService));
        tabs.addTab("Referrals", new ReferralsPanel(ctx.referralService, ctx.referralManager));

        setLayout(new BorderLayout());
        add(tabs, BorderLayout.CENTER);
        add(footer, BorderLayout.SOUTH);
    }
}
