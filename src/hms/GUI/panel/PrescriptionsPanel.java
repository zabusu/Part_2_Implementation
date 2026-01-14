package hms.GUI.panel;

import hms.GUI.SwingDialogs;
import hms.model.Prescription;
import hms.service.PrescriptionService;
import hms.service.ValidationException;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.time.LocalDate;
import java.util.List;

public final class PrescriptionsPanel extends JPanel {
    private final PrescriptionService prescriptionService;

    private final String[] cols = {
            "prescription_id", "patient_id", "clinician_id", "appointment_id",
            "medication_name", "dosage", "frequency", "duration_days", "pharmacy_name", "status", "issue_date"
    };

    private final DefaultTableModel model = new DefaultTableModel(cols, 0) {
        @Override public boolean isCellEditable(int row, int column) { return false; }
    };

    private final JTable table = new JTable(model);

    public PrescriptionsPanel(PrescriptionService prescriptionService) {
        super(new BorderLayout());
        this.prescriptionService = prescriptionService;

        JPanel top = new JPanel(new BorderLayout());
        top.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
        top.add(new JLabel("Prescriptions"), BorderLayout.WEST);

        JPanel actions = new JPanel();
        JButton refreshBtn = new JButton("Refresh");
        JButton createBtn = new JButton("Create");
        actions.add(refreshBtn);
        actions.add(createBtn);
        top.add(actions, BorderLayout.EAST);

        table.setAutoCreateRowSorter(true);

        add(top, BorderLayout.NORTH);
        add(new JScrollPane(table), BorderLayout.CENTER);

        refreshBtn.addActionListener(e -> refresh());
        createBtn.addActionListener(e -> createPrescription());

        SwingUtilities.invokeLater(this::refresh);
    }

    private void refresh() {
        try {
            List<Prescription> list = prescriptionService.listAll();
            model.setRowCount(0);
            for (Prescription p : list) {
                model.addRow(new Object[]{
                        p.prescriptionId(),
                        p.patientId(),
                        p.clinicianId(),
                        p.appointmentId(),
                        p.medicationName(),
                        p.dosage(),
                        p.frequency(),
                        p.durationDays(),
                        p.pharmacyName(),
                        p.status(),
                        p.issueDate()
                });
            }
        } catch (Exception ex) {
            SwingDialogs.showError(this, "Failed to load prescriptions.", ex);
        }
    }

    private void createPrescription() {
        JPanel form = new JPanel(new GridLayout(0, 2, 6, 6));

        JTextField patientId = new JTextField("P001");
        JTextField clinicianId = new JTextField("C001");
        JTextField appointmentId = new JTextField("");
        JTextField prescriptionDate = new JTextField(LocalDate.now().toString());
        JTextField medication = new JTextField("Paracetamol");
        JTextField dosage = new JTextField("500mg");
        JTextField frequency = new JTextField("Once daily");
        JTextField durationDays = new JTextField("7");
        JTextField quantity = new JTextField("14 tablets");
        JTextField instructions = new JTextField("Take with water");
        JTextField pharmacy = new JTextField("Boots Pharmacy Birmingham");

        form.add(new JLabel("Patient ID")); form.add(patientId);
        form.add(new JLabel("Clinician ID")); form.add(clinicianId);
        form.add(new JLabel("Appointment ID (optional)")); form.add(appointmentId);
        form.add(new JLabel("Prescription date (YYYY-MM-DD)")); form.add(prescriptionDate);
        form.add(new JLabel("Medication name")); form.add(medication);
        form.add(new JLabel("Dosage")); form.add(dosage);
        form.add(new JLabel("Frequency")); form.add(frequency);
        form.add(new JLabel("Duration days")); form.add(durationDays);
        form.add(new JLabel("Quantity")); form.add(quantity);
        form.add(new JLabel("Instructions")); form.add(instructions);
        form.add(new JLabel("Pharmacy name")); form.add(pharmacy);

        int res = javax.swing.JOptionPane.showConfirmDialog(
                this, form, "Create Prescription", javax.swing.JOptionPane.OK_CANCEL_OPTION
        );
        if (res != javax.swing.JOptionPane.OK_OPTION) return;

        try {
            Prescription created = prescriptionService.create(
                    patientId.getText(),
                    clinicianId.getText(),
                    appointmentId.getText(),
                    LocalDate.parse(prescriptionDate.getText().trim()),
                    medication.getText(),
                    dosage.getText(),
                    frequency.getText(),
                    Integer.parseInt(durationDays.getText().trim()),
                    quantity.getText(),
                    instructions.getText(),
                    pharmacy.getText()
            );
            SwingDialogs.showInfo(this, "Created prescription " + created.prescriptionId());
            refresh();
        } catch (ValidationException vex) {
            SwingDialogs.showError(this, "Validation error.", vex);
        } catch (Exception ex) {
            SwingDialogs.showError(this, "Failed to create prescription.", ex);
        }
    }
}
