package hms.GUI.panel;

import hms.GUI.SwingDialogs;
import hms.model.Appointment;
import hms.service.AppointmentService;
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
import java.time.LocalTime;
import java.util.List;

public final class AppointmentsPanel extends JPanel {
    private final AppointmentService appointmentService;

    private final String[] cols = {
            "appointment_id", "patient_id", "clinician_id", "facility_id",
            "appointment_date", "appointment_time", "duration_minutes",
            "appointment_type", "status", "reason_for_visit"
    };

    private final DefaultTableModel model = new DefaultTableModel(cols, 0) {
        @Override public boolean isCellEditable(int row, int column) { return false; }
    };

    private final JTable table = new JTable(model);

    public AppointmentsPanel(AppointmentService appointmentService) {
        super(new BorderLayout());
        this.appointmentService = appointmentService;

        JPanel top = new JPanel(new BorderLayout());
        top.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
        top.add(new JLabel("Appointments"), BorderLayout.WEST);

        JPanel actions = new JPanel();
        JButton refreshBtn = new JButton("Refresh");
        JButton createBtn = new JButton("Create");
        JButton rescheduleBtn = new JButton("Reschedule");
        JButton cancelBtn = new JButton("Cancel");
        actions.add(refreshBtn);
        actions.add(createBtn);
        actions.add(rescheduleBtn);
        actions.add(cancelBtn);
        top.add(actions, BorderLayout.EAST);

        table.setAutoCreateRowSorter(true);

        add(top, BorderLayout.NORTH);
        add(new JScrollPane(table), BorderLayout.CENTER);

        refreshBtn.addActionListener(e -> refresh());
        createBtn.addActionListener(e -> createAppointment());
        rescheduleBtn.addActionListener(e -> rescheduleSelected());
        cancelBtn.addActionListener(e -> cancelSelected());

        SwingUtilities.invokeLater(this::refresh);
    }

    private void refresh() {
        try {
            List<Appointment> appts = appointmentService.listAll();
            model.setRowCount(0);
            for (Appointment a : appts) {
                model.addRow(new Object[]{
                        a.appointmentId(),
                        a.patientId(),
                        a.clinicianId(),
                        a.facilityId(),
                        a.appointmentDate(),
                        a.appointmentTime(),
                        a.durationMinutes(),
                        a.appointmentType(),
                        a.status(),
                        a.reasonForVisit()
                });
            }
        } catch (Exception ex) {
            SwingDialogs.showError(this, "Failed to load appointments.", ex);
        }
    }

    private void createAppointment() {
        JPanel form = new JPanel(new GridLayout(0, 2, 6, 6));

        JTextField patientId = new JTextField("P001");
        JTextField clinicianId = new JTextField("C001");
        JTextField facilityId = new JTextField("S001");
        JTextField date = new JTextField(LocalDate.now().plusDays(7).toString());
        JTextField time = new JTextField("09:00");
        JTextField duration = new JTextField("15");
        JTextField type = new JTextField("Routine Consultation");
        JTextField reason = new JTextField("Checkup");
        JTextField notes = new JTextField("");

        form.add(new JLabel("Patient ID")); form.add(patientId);
        form.add(new JLabel("Clinician ID")); form.add(clinicianId);
        form.add(new JLabel("Facility ID")); form.add(facilityId);
        form.add(new JLabel("Date (YYYY-MM-DD)")); form.add(date);
        form.add(new JLabel("Time (HH:MM)")); form.add(time);
        form.add(new JLabel("Duration (minutes)")); form.add(duration);
        form.add(new JLabel("Type")); form.add(type);
        form.add(new JLabel("Reason")); form.add(reason);
        form.add(new JLabel("Notes")); form.add(notes);

        int res = javax.swing.JOptionPane.showConfirmDialog(
                this, form, "Create Appointment", javax.swing.JOptionPane.OK_CANCEL_OPTION
        );
        if (res != javax.swing.JOptionPane.OK_OPTION) return;

        try {
            Appointment created = appointmentService.create(
                    patientId.getText(),
                    clinicianId.getText(),
                    facilityId.getText(),
                    LocalDate.parse(date.getText().trim()),
                    LocalTime.parse(time.getText().trim()),
                    Integer.parseInt(duration.getText().trim()),
                    type.getText(),
                    reason.getText(),
                    notes.getText()
            );
            SwingDialogs.showInfo(this, "Created appointment " + created.appointmentId());
            refresh();
        } catch (ValidationException vex) {
            SwingDialogs.showError(this, "Validation error.", vex);
        } catch (Exception ex) {
            SwingDialogs.showError(this, "Failed to create appointment.", ex);
        }
    }

    private void rescheduleSelected() {
        int row = table.getSelectedRow();
        if (row < 0) {
            SwingDialogs.showInfo(this, "Select an appointment first.");
            return;
        }
        int modelRow = table.convertRowIndexToModel(row);
        String appointmentId = String.valueOf(model.getValueAt(modelRow, 0));
        String currentDate = String.valueOf(model.getValueAt(modelRow, 4));
        String currentTime = String.valueOf(model.getValueAt(modelRow, 5));
        String currentDur = String.valueOf(model.getValueAt(modelRow, 6));

        JPanel form = new JPanel(new GridLayout(0, 2, 6, 6));
        JTextField newDate = new JTextField(currentDate);
        JTextField newTime = new JTextField(currentTime);
        JTextField newDuration = new JTextField(currentDur);
        JTextField newNotes = new JTextField("");

        form.add(new JLabel("New date (YYYY-MM-DD)")); form.add(newDate);
        form.add(new JLabel("New time (HH:MM)")); form.add(newTime);
        form.add(new JLabel("New duration (minutes)")); form.add(newDuration);
        form.add(new JLabel("Notes (optional)")); form.add(newNotes);

        int res = javax.swing.JOptionPane.showConfirmDialog(
                this, form, "Reschedule " + appointmentId, javax.swing.JOptionPane.OK_CANCEL_OPTION
        );
        if (res != javax.swing.JOptionPane.OK_OPTION) return;

        try {
            appointmentService.reschedule(
                    appointmentId,
                    LocalDate.parse(newDate.getText().trim()),
                    LocalTime.parse(newTime.getText().trim()),
                    Integer.parseInt(newDuration.getText().trim()),
                    newNotes.getText()
            );
            SwingDialogs.showInfo(this, "Appointment updated.");
            refresh();
        } catch (ValidationException vex) {
            SwingDialogs.showError(this, "Validation error.", vex);
        } catch (Exception ex) {
            SwingDialogs.showError(this, "Failed to reschedule appointment.", ex);
        }
    }

    private void cancelSelected() {
        int row = table.getSelectedRow();
        if (row < 0) {
            SwingDialogs.showInfo(this, "Select an appointment first.");
            return;
        }
        int modelRow = table.convertRowIndexToModel(row);
        String appointmentId = String.valueOf(model.getValueAt(modelRow, 0));

        JTextField notes = new JTextField("");
        JPanel form = new JPanel(new GridLayout(0, 2, 6, 6));
        form.add(new JLabel("Cancel notes (optional)"));
        form.add(notes);

        int res = javax.swing.JOptionPane.showConfirmDialog(
                this, form, "Cancel " + appointmentId, javax.swing.JOptionPane.OK_CANCEL_OPTION
        );
        if (res != javax.swing.JOptionPane.OK_OPTION) return;

        if (!SwingDialogs.confirm(this, "Confirm cancel appointment " + appointmentId + "?")) return;

        try {
            appointmentService.cancel(appointmentId, notes.getText());
            SwingDialogs.showInfo(this, "Appointment cancelled.");
            refresh();
        } catch (Exception ex) {
            SwingDialogs.showError(this, "Failed to cancel appointment.", ex);
        }
    }
}
