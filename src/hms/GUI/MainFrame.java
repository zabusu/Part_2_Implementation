package hms.GUI;

import hms.data.CsvFileConfig;
import hms.data.CsvPatientRepository;
import hms.data.PatientRepository;
import hms.model.Patient;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTabbedPane;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
import javax.swing.JTable;
import javax.swing.SwingWorker;
import java.awt.BorderLayout;
import java.util.List;

public final class MainFrame extends JFrame {
    public MainFrame(CsvFileConfig config) {
        super("Healthcare Management System");

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1100, 700);
        setLocationRelativeTo(null);

        JLabel footer = new JLabel("Data folder: " + config.patientsCsv().getParent(), SwingConstants.LEFT);

        JTabbedPane tabs = new JTabbedPane();
        tabs.addTab("Patients", patientsPanel(config));
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

    private static JScrollPane patientsPanel(CsvFileConfig config) {
        String[] cols = {"patient_id", "first_name", "last_name", "date_of_birth", "nhs_number", "gp_surgery_id"};
        Object[][] empty = new Object[0][cols.length];
        JTable table = new JTable(empty, cols);
        table.setAutoCreateRowSorter(true);

        PatientRepository repo = new CsvPatientRepository(config.patientsCsv());
        new SwingWorker<Object[][], Void>() {
            @Override
            protected Object[][] doInBackground() throws Exception {
                List<Patient> patients = repo.findAll();
                Object[][] data = new Object[patients.size()][cols.length];
                for (int i = 0; i < patients.size(); i++) {
                    Patient p = patients.get(i);
                    data[i][0] = p.patientId();
                    data[i][1] = p.firstName();
                    data[i][2] = p.lastName();
                    data[i][3] = p.dateOfBirth();
                    data[i][4] = p.nhsNumber();
                    data[i][5] = p.gpSurgeryId();
                }
                return data;
            }

            @Override
            protected void done() {
                try {
                    Object[][] data = get();
                    table.setModel(new javax.swing.table.DefaultTableModel(data, cols));
                    table.setAutoCreateRowSorter(true);
                } catch (Exception ignored) {
                    // keep empty table
                }
            }
        }.execute();

        return new JScrollPane(table);
    }
}

