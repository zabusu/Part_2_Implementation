package hms.GUI.panel;

import hms.GUI.SwingDialogs;
import hms.model.Referral;
import hms.service.ReferralManager;
import hms.service.ReferralService;
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

public final class ReferralsPanel extends JPanel {
    private final ReferralService referralService;
    private final ReferralManager referralManager;

    private final String[] cols = {
            "referral_id", "patient_id", "urgency_level", "status",
            "referring_facility_id", "referred_to_facility_id",
            "referring_clinician_id", "referred_to_clinician_id",
            "referral_reason", "last_updated"
    };

    private final DefaultTableModel model = new DefaultTableModel(cols, 0) {
        @Override public boolean isCellEditable(int row, int column) { return false; }
    };

    private final JTable table = new JTable(model);

    public ReferralsPanel(ReferralService referralService, ReferralManager referralManager) {
        super(new BorderLayout());
        this.referralService = referralService;
        this.referralManager = referralManager;

        JPanel top = new JPanel(new BorderLayout());
        top.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
        top.add(new JLabel("Referrals"), BorderLayout.WEST);

        JPanel actions = new JPanel();
        JButton refreshBtn = new JButton("Refresh");
        JButton createBtn = new JButton("Create");
        JButton sendBtn = new JButton("Send Selected");
        actions.add(refreshBtn);
        actions.add(createBtn);
        actions.add(sendBtn);
        top.add(actions, BorderLayout.EAST);

        table.setAutoCreateRowSorter(true);

        add(top, BorderLayout.NORTH);
        add(new JScrollPane(table), BorderLayout.CENTER);

        refreshBtn.addActionListener(e -> refresh());
        createBtn.addActionListener(e -> createReferral());
        sendBtn.addActionListener(e -> sendSelected());

        SwingUtilities.invokeLater(this::refresh);
    }

    private void refresh() {
        try {
            List<Referral> list = referralService.listAll();
            model.setRowCount(0);
            for (Referral r : list) {
                model.addRow(new Object[]{
                        r.referralId(),
                        r.patientId(),
                        r.urgencyLevel(),
                        r.status(),
                        r.referringFacilityId(),
                        r.referredToFacilityId(),
                        r.referringClinicianId(),
                        r.referredToClinicianId(),
                        r.referralReason(),
                        r.lastUpdated()
                });
            }
        } catch (Exception ex) {
            SwingDialogs.showError(this, "Failed to load referrals.", ex);
        }
    }

    private void createReferral() {
        JPanel form = new JPanel(new GridLayout(0, 2, 6, 6));

        JTextField patientId = new JTextField("P001");
        JTextField referringClinicianId = new JTextField("C001");
        JTextField referredToClinicianId = new JTextField("C005");
        JTextField referringFacilityId = new JTextField("S001");
        JTextField referredToFacilityId = new JTextField("H001");
        JTextField referralDate = new JTextField(LocalDate.now().toString());
        JTextField urgency = new JTextField("Routine");
        JTextField reason = new JTextField("Specialist assessment");
        JTextField summary = new JTextField("Clinical summary here...");
        JTextField investigations = new JTextField("ECG|Blood tests");
        JTextField appointmentId = new JTextField("");
        JTextField notes = new JTextField("");

        form.add(new JLabel("Patient ID")); form.add(patientId);
        form.add(new JLabel("Referring clinician ID")); form.add(referringClinicianId);
        form.add(new JLabel("Referred-to clinician ID")); form.add(referredToClinicianId);
        form.add(new JLabel("Referring facility ID")); form.add(referringFacilityId);
        form.add(new JLabel("Referred-to facility ID")); form.add(referredToFacilityId);
        form.add(new JLabel("Referral date (YYYY-MM-DD)")); form.add(referralDate);
        form.add(new JLabel("Urgency level")); form.add(urgency);
        form.add(new JLabel("Referral reason")); form.add(reason);
        form.add(new JLabel("Clinical summary")); form.add(summary);
        form.add(new JLabel("Requested investigations")); form.add(investigations);
        form.add(new JLabel("Appointment ID (optional)")); form.add(appointmentId);
        form.add(new JLabel("Notes (optional)")); form.add(notes);

        int res = javax.swing.JOptionPane.showConfirmDialog(
                this, form, "Create Referral", javax.swing.JOptionPane.OK_CANCEL_OPTION
        );
        if (res != javax.swing.JOptionPane.OK_OPTION) return;

        try {
            Referral created = referralService.create(
                    patientId.getText(),
                    referringClinicianId.getText(),
                    referredToClinicianId.getText(),
                    referringFacilityId.getText(),
                    referredToFacilityId.getText(),
                    LocalDate.parse(referralDate.getText().trim()),
                    urgency.getText(),
                    reason.getText(),
                    summary.getText(),
                    investigations.getText(),
                    appointmentId.getText(),
                    notes.getText()
            );
            SwingDialogs.showInfo(this, "Created referral " + created.referralId());
            refresh();
        } catch (ValidationException vex) {
            SwingDialogs.showError(this, "Validation error.", vex);
        } catch (Exception ex) {
            SwingDialogs.showError(this, "Failed to create referral.", ex);
        }
    }

    private void sendSelected() {
        int row = table.getSelectedRow();
        if (row < 0) {
            SwingDialogs.showInfo(this, "Select a referral first.");
            return;
        }
        int modelRow = table.convertRowIndexToModel(row);
        String referralId = String.valueOf(model.getValueAt(modelRow, 0));

        try {
            referralManager.clearQueue();
            boolean queued = referralManager.enqueue(referralId);
            if (!queued) {
                SwingDialogs.showInfo(this, "Referral was not queued (already processed or invalid): " + referralId);
                return;
            }
            String file = referralManager.processNext().orElse("none");
            SwingDialogs.showInfo(this, "Referral processed. Output file:\n" + file);
        } catch (Exception ex) {
            SwingDialogs.showError(this, "Failed to process referral.", ex);
        }
    }
}
