package hms.GUI.panel;

import hms.GUI.SwingDialogs;
import hms.data.ReferenceDataRepository;
import hms.model.Clinician;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;
import java.awt.BorderLayout;
import java.util.List;

public final class CliniciansPanel extends JPanel {
    private final ReferenceDataRepository refData;

    private final String[] cols = {
            "clinician_id", "first_name", "last_name", "title", "speciality", "gmc_number", "workplace_id", "workplace_type"
    };
    private final DefaultTableModel model = new DefaultTableModel(cols, 0) {
        @Override public boolean isCellEditable(int row, int column) { return false; }
    };
    private final JTable table = new JTable(model);

    public CliniciansPanel(ReferenceDataRepository refData) {
        super(new BorderLayout());
        this.refData = refData;

        JPanel top = new JPanel(new BorderLayout());
        top.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
        top.add(new JLabel("Clinicians"), BorderLayout.WEST);

        JPanel actions = new JPanel();
        JButton refreshBtn = new JButton("Refresh");
        actions.add(refreshBtn);
        top.add(actions, BorderLayout.EAST);

        table.setAutoCreateRowSorter(true);

        add(top, BorderLayout.NORTH);
        add(new JScrollPane(table), BorderLayout.CENTER);

        refreshBtn.addActionListener(e -> refresh());
        SwingUtilities.invokeLater(this::refresh);
    }

    private void refresh() {
        try {
            List<Clinician> clinicians = refData.clinicians();
            model.setRowCount(0);
            for (Clinician c : clinicians) {
                model.addRow(new Object[]{
                        c.clinicianId(),
                        c.firstName(),
                        c.lastName(),
                        c.title(),
                        c.speciality(),
                        c.gmcNumber(),
                        c.workplaceId(),
                        c.workplaceType()
                });
            }
        } catch (Exception ex) {
            SwingDialogs.showError(this, "Failed to load clinicians.", ex);
        }
    }
}

