package hms.data;

import hms.model.Patient;
import hms.util.DateTimes;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public final class CsvPatientRepository implements PatientRepository {
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
}

