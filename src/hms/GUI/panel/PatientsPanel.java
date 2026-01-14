package hms.GUI.panel;

import hms.GUI.SwingDialogs;
import hms.model.Patient;
import hms.service.PatientService;
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

public final class PatientsPanel extends JPanel {
    private final PatientService patientService;

    private final String[] cols = {
            "patient_id", "first_name", "last_name", "date_of_birth", "nhs_number", "gender", "phone_number", "email", "gp_surgery_id"
    };
    private final DefaultTableModel model = new DefaultTableModel(cols, 0) {
        @Override public boolean isCellEditable(int row, int column) { return false; }
    };
    private final JTable table = new JTable(model);

    public PatientsPanel(PatientService patientService) {
        super(new BorderLayout());
        this.patientService = patientService;

        JPanel top = new JPanel(new BorderLayout());
        top.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
        top.add(new JLabel("Patients"), BorderLayout.WEST);

        JPanel actions = new JPanel();
        JButton refreshBtn = new JButton("Refresh");
        JButton addBtn = new JButton("Add");
        JButton deleteBtn = new JButton("Delete");
        actions.add(refreshBtn);
        actions.add(addBtn);
        actions.add(deleteBtn);
        top.add(actions, BorderLayout.EAST);

        table.setAutoCreateRowSorter(true);

        add(top, BorderLayout.NORTH);
        add(new JScrollPane(table), BorderLayout.CENTER);

        refreshBtn.addActionListener(e -> refresh());
        addBtn.addActionListener(e -> addPatient());
        deleteBtn.addActionListener(e -> deleteSelected());

        SwingUtilities.invokeLater(this::refresh);
    }

    public void refresh() {
        try {
            List<Patient> patients = patientService.listAll();
            model.setRowCount(0);
            for (Patient p : patients) {
                model.addRow(new Object[]{
                        p.patientId(),
                        p.firstName(),
                        p.lastName(),
                        p.dateOfBirth(),
                        p.nhsNumber(),
                        p.gender(),
                        p.phoneNumber(),
                        p.email(),
                        p.gpSurgeryId()
                });
            }
        } catch (Exception ex) {
            SwingDialogs.showError(this, "Failed to load patients.", ex);
        }
    }

    private void addPatient() {
        JPanel form = new JPanel(new GridLayout(0, 2, 6, 6));

        JTextField firstName = new JTextField();
        JTextField lastName = new JTextField();
        JTextField dob = new JTextField("1980-01-01");
        JTextField nhs = new JTextField();
        JTextField gender = new JTextField("M/F");
        JTextField phone = new JTextField();
        JTextField email = new JTextField();
        JTextField address = new JTextField();
        JTextField postcode = new JTextField();
        JTextField ecName = new JTextField();
        JTextField ecPhone = new JTextField();
        JTextField gpSurgery = new JTextField("S001");

        form.add(new JLabel("First name")); form.add(firstName);
        form.add(new JLabel("Last name")); form.add(lastName);
        form.add(new JLabel("Date of birth (YYYY-MM-DD)")); form.add(dob);
        form.add(new JLabel("NHS number")); form.add(nhs);
        form.add(new JLabel("Gender")); form.add(gender);
        form.add(new JLabel("Phone")); form.add(phone);
        form.add(new JLabel("Email")); form.add(email);
        form.add(new JLabel("Address")); form.add(address);
        form.add(new JLabel("Postcode")); form.add(postcode);
        form.add(new JLabel("Emergency contact name")); form.add(ecName);
        form.add(new JLabel("Emergency contact phone")); form.add(ecPhone);
        form.add(new JLabel("GP surgery id")); form.add(gpSurgery);

        int res = javax.swing.JOptionPane.showConfirmDialog(
                this, form, "Add Patient", javax.swing.JOptionPane.OK_CANCEL_OPTION
        );
        if (res != javax.swing.JOptionPane.OK_OPTION) return;

        try {
            LocalDate parsedDob = LocalDate.parse(dob.getText().trim());
            patientService.register(
                    firstName.getText(),
                    lastName.getText(),
                    parsedDob,
                    nhs.getText(),
                    gender.getText(),
                    phone.getText(),
                    email.getText(),
                    address.getText(),
                    postcode.getText(),
                    ecName.getText(),
                    ecPhone.getText(),
                    gpSurgery.getText()
            );
            SwingDialogs.showInfo(this, "Patient added.");
            refresh();
        } catch (ValidationException vex) {
            SwingDialogs.showError(this, "Validation error.", vex);
        } catch (Exception ex) {
            SwingDialogs.showError(this, "Failed to add patient.", ex);
        }
    }

    private void deleteSelected() {
        int row = table.getSelectedRow();
        if (row < 0) {
            SwingDialogs.showInfo(this, "Select a patient row first.");
            return;
        }
        int modelRow = table.convertRowIndexToModel(row);
        String patientId = String.valueOf(model.getValueAt(modelRow, 0));
        if (!SwingDialogs.confirm(this, "Delete patient " + patientId + "?")) return;

        try {
            patientService.delete(patientId);
            SwingDialogs.showInfo(this, "Patient deleted.");
            refresh();
        } catch (Exception ex) {
            SwingDialogs.showError(this, "Failed to delete patient.", ex);
        }
    }
}

