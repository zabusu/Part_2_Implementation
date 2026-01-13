package hms.data;

import hms.model.Appointment;
import hms.util.Csv;
import hms.util.DateTimes;
import hms.util.IO;
import hms.util.Numbers;

import java.io.IOException;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public final class CsvAppointmentRepository implements AppointmentRepository {
    private static final String HEADER =
            "appointment_id,patient_id,clinician_id,facility_id,appointment_date,appointment_time,duration_minutes,appointment_type,status,reason_for_visit,notes,created_date,last_modified";

    private final Path csvPath;

    public CsvAppointmentRepository(Path csvPath) {
        this.csvPath = csvPath;
    }

    @Override
    public List<Appointment> findAll() throws IOException {
        CsvTable table = CsvLoader.load(csvPath);
        List<Appointment> out = new ArrayList<>();
        for (List<String> row : table.rows()) {
            out.add(map(table, row));
        }
        return out;
    }

    @Override
    public Optional<Appointment> findById(String appointmentId) throws IOException {
        if (appointmentId == null) return Optional.empty();
        String target = appointmentId.trim();
        if (target.isEmpty()) return Optional.empty();

        CsvTable table = CsvLoader.load(csvPath);
        for (List<String> row : table.rows()) {
            if (target.equals(table.get(row, "appointment_id"))) {
                return Optional.of(map(table, row));
            }
        }
        return Optional.empty();
    }

    @Override
    public void saveAll(List<Appointment> appointments) throws IOException {
        List<String> lines = new ArrayList<>();
        lines.add(HEADER);
        for (Appointment a : appointments) {
            lines.add(toLine(a));
        }
        IO.writeAllLines(csvPath, lines);
    }

    private static Appointment map(CsvTable t, List<String> r) {
        return new Appointment(
                t.get(r, "appointment_id"),
                t.get(r, "patient_id"),
                t.get(r, "clinician_id"),
                t.get(r, "facility_id"),
                DateTimes.parseDate(t.get(r, "appointment_date")),
                DateTimes.parseTime(t.get(r, "appointment_time")),
                Numbers.parseIntOrDefault(t.get(r, "duration_minutes"), 0),
                t.get(r, "appointment_type"),
                t.get(r, "status"),
                t.get(r, "reason_for_visit"),
                t.get(r, "notes"),
                DateTimes.parseDate(t.get(r, "created_date")),
                DateTimes.parseDate(t.get(r, "last_modified"))
        );
    }

    private static String toLine(Appointment a) {
        List<String> fields = new ArrayList<>(13);
        fields.add(n(a.appointmentId()));
        fields.add(n(a.patientId()));
        fields.add(n(a.clinicianId()));
        fields.add(n(a.facilityId()));
        fields.add(n(d(a.appointmentDate())));
        fields.add(n(t(a.appointmentTime())));
        fields.add(Integer.toString(a.durationMinutes()));
        fields.add(n(a.appointmentType()));
        fields.add(n(a.status()));
        fields.add(n(a.reasonForVisit()));
        fields.add(n(a.notes()));
        fields.add(n(d(a.createdDate())));
        fields.add(n(d(a.lastModified())));
        return Csv.toLine(fields);
    }

    private static String n(String s) { return s == null ? "" : s; }
    private static String d(LocalDate d) { return d == null ? "" : d.toString(); }
    private static String t(LocalTime t) { return t == null ? "" : t.toString(); }
}

