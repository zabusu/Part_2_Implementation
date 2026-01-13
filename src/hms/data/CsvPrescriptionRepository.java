package hms.data;

import hms.model.Prescription;
import hms.util.Csv;
import hms.util.DateTimes;
import hms.util.IO;
import hms.util.Numbers;

import java.io.IOException;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public final class CsvPrescriptionRepository implements PrescriptionRepository {
    private static final String HEADER =
            "prescription_id,patient_id,clinician_id,appointment_id,prescription_date,medication_name,dosage,frequency,duration_days,quantity,instructions,pharmacy_name,status,issue_date,collection_date";

    private final Path csvPath;

    public CsvPrescriptionRepository(Path csvPath) {
        this.csvPath = csvPath;
    }

    @Override
    public List<Prescription> findAll() throws IOException {
        CsvTable table = CsvLoader.load(csvPath);
        List<Prescription> out = new ArrayList<>();
        for (List<String> row : table.rows()) {
            out.add(map(table, row));
        }
        return out;
    }

    @Override
    public Optional<Prescription> findById(String prescriptionId) throws IOException {
        if (prescriptionId == null) return Optional.empty();
        String target = prescriptionId.trim();
        if (target.isEmpty()) return Optional.empty();

        CsvTable table = CsvLoader.load(csvPath);
        for (List<String> row : table.rows()) {
            if (target.equals(table.get(row, "prescription_id"))) {
                return Optional.of(map(table, row));
            }
        }
        return Optional.empty();
    }

    @Override
    public void append(Prescription p) throws IOException {
        // Ensure file exists with header first
        if (IO.readAllLines(csvPath).isEmpty()) {
            IO.writeAllLines(csvPath, List.of(HEADER));
        }
        IO.appendLine(csvPath, toLine(p));
    }

    private static Prescription map(CsvTable t, List<String> r) {
        return new Prescription(
                t.get(r, "prescription_id"),
                t.get(r, "patient_id"),
                t.get(r, "clinician_id"),
                t.get(r, "appointment_id"),
                DateTimes.parseDate(t.get(r, "prescription_date")),
                t.get(r, "medication_name"),
                t.get(r, "dosage"),
                t.get(r, "frequency"),
                Numbers.parseIntOrDefault(t.get(r, "duration_days"), 0),
                t.get(r, "quantity"),
                t.get(r, "instructions"),
                t.get(r, "pharmacy_name"),
                t.get(r, "status"),
                DateTimes.parseDate(t.get(r, "issue_date")),
                DateTimes.parseDate(t.get(r, "collection_date"))
        );
    }

    private static String toLine(Prescription p) {
        List<String> fields = new ArrayList<>(15);
        fields.add(n(p.prescriptionId()));
        fields.add(n(p.patientId()));
        fields.add(n(p.clinicianId()));
        fields.add(n(p.appointmentId()));
        fields.add(n(d(p.prescriptionDate())));
        fields.add(n(p.medicationName()));
        fields.add(n(p.dosage()));
        fields.add(n(p.frequency()));
        fields.add(Integer.toString(p.durationDays()));
        fields.add(n(p.quantity()));
        fields.add(n(p.instructions()));
        fields.add(n(p.pharmacyName()));
        fields.add(n(p.status()));
        fields.add(n(d(p.issueDate())));
        fields.add(n(d(p.collectionDate())));
        return Csv.toLine(fields);
    }

    private static String n(String s) { return s == null ? "" : s; }
    private static String d(LocalDate d) { return d == null ? "" : d.toString(); }
}

