package hms.data;

import hms.model.Patient;
import hms.util.Csv;
import hms.util.DateTimes;
import hms.util.IO;

import java.io.IOException;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public final class CsvPatientRepository implements PatientRepository {
    private static final String HEADER =
            "patient_id,first_name,last_name,date_of_birth,nhs_number,gender,phone_number,email,address,postcode,emergency_contact_name,emergency_contact_phone,registration_date,gp_surgery_id";

    private final Path csvPath;

    public CsvPatientRepository(Path csvPath) {
        this.csvPath = csvPath;
    }

    @Override
    public List<Patient> findAll() throws IOException {
        CsvTable table = CsvLoader.load(csvPath);
        List<Patient> out = new ArrayList<>();
        for (List<String> row : table.rows()) {
            out.add(map(table, row));
        }
        return out;
    }

    @Override
    public Optional<Patient> findById(String patientId) throws IOException {
        if (patientId == null) return Optional.empty();
        String target = patientId.trim();
        if (target.isEmpty()) return Optional.empty();

        CsvTable table = CsvLoader.load(csvPath);
        for (List<String> row : table.rows()) {
            if (target.equals(table.get(row, "patient_id"))) {
                return Optional.of(map(table, row));
            }
        }
        return Optional.empty();
    }

    @Override
    public void saveAll(List<Patient> patients) throws IOException {
        List<String> lines = new ArrayList<>();
        lines.add(HEADER);
        for (Patient p : patients) {
            lines.add(toLine(p));
        }
        IO.writeAllLines(csvPath, lines);
    }

    private static Patient map(CsvTable t, List<String> r) {
        return new Patient(
                t.get(r, "patient_id"),
                t.get(r, "first_name"),
                t.get(r, "last_name"),
                DateTimes.parseDate(t.get(r, "date_of_birth")),
                t.get(r, "nhs_number"),
                t.get(r, "gender"),
                t.get(r, "phone_number"),
                t.get(r, "email"),
                t.get(r, "address"),
                t.get(r, "postcode"),
                t.get(r, "emergency_contact_name"),
                t.get(r, "emergency_contact_phone"),
                DateTimes.parseDate(t.get(r, "registration_date")),
                t.get(r, "gp_surgery_id")
        );
    }

    private static String toLine(Patient p) {
        List<String> fields = new ArrayList<>(14);
        fields.add(n(p.patientId()));
        fields.add(n(p.firstName()));
        fields.add(n(p.lastName()));
        fields.add(n(d(p.dateOfBirth())));
        fields.add(n(p.nhsNumber()));
        fields.add(n(p.gender()));
        fields.add(n(p.phoneNumber()));
        fields.add(n(p.email()));
        fields.add(n(p.address()));
        fields.add(n(p.postcode()));
        fields.add(n(p.emergencyContactName()));
        fields.add(n(p.emergencyContactPhone()));
        fields.add(n(d(p.registrationDate())));
        fields.add(n(p.gpSurgeryId()));
        return Csv.toLine(fields);
    }

    private static String n(String s) { return s == null ? "" : s; }
    private static String d(LocalDate d) { return d == null ? "" : d.toString(); }
}
