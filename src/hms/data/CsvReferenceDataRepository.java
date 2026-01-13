package hms.data;

import hms.model.Clinician;
import hms.model.Facility;
import hms.model.Staff;
import hms.util.DateTimes;
import hms.util.Numbers;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public final class CsvReferenceDataRepository implements ReferenceDataRepository {
    private final Path cliniciansCsv;
    private final Path facilitiesCsv;
    private final Path staffCsv;

    public CsvReferenceDataRepository(Path cliniciansCsv, Path facilitiesCsv, Path staffCsv) {
        this.cliniciansCsv = cliniciansCsv;
        this.facilitiesCsv = facilitiesCsv;
        this.staffCsv = staffCsv;
    }

    @Override
    public List<Clinician> clinicians() throws IOException {
        CsvTable t = CsvLoader.load(cliniciansCsv);
        List<Clinician> out = new ArrayList<>();
        for (List<String> r : t.rows()) out.add(mapClinician(t, r));
        return out;
    }

    @Override
    public Optional<Clinician> clinicianById(String clinicianId) throws IOException {
        if (clinicianId == null) return Optional.empty();
        String target = clinicianId.trim();
        if (target.isEmpty()) return Optional.empty();

        CsvTable t = CsvLoader.load(cliniciansCsv);
        for (List<String> r : t.rows()) {
            if (target.equals(t.get(r, "clinician_id"))) return Optional.of(mapClinician(t, r));
        }
        return Optional.empty();
    }

    @Override
    public List<Facility> facilities() throws IOException {
        CsvTable t = CsvLoader.load(facilitiesCsv);
        List<Facility> out = new ArrayList<>();
        for (List<String> r : t.rows()) out.add(mapFacility(t, r));
        return out;
    }

    @Override
    public Optional<Facility> facilityById(String facilityId) throws IOException {
        if (facilityId == null) return Optional.empty();
        String target = facilityId.trim();
        if (target.isEmpty()) return Optional.empty();

        CsvTable t = CsvLoader.load(facilitiesCsv);
        for (List<String> r : t.rows()) {
            if (target.equals(t.get(r, "facility_id"))) return Optional.of(mapFacility(t, r));
        }
        return Optional.empty();
    }

    @Override
    public List<Staff> staff() throws IOException {
        CsvTable t = CsvLoader.load(staffCsv);
        List<Staff> out = new ArrayList<>();
        for (List<String> r : t.rows()) out.add(mapStaff(t, r));
        return out;
    }

    @Override
    public Optional<Staff> staffById(String staffId) throws IOException {
        if (staffId == null) return Optional.empty();
        String target = staffId.trim();
        if (target.isEmpty()) return Optional.empty();

        CsvTable t = CsvLoader.load(staffCsv);
        for (List<String> r : t.rows()) {
            if (target.equals(t.get(r, "staff_id"))) return Optional.of(mapStaff(t, r));
        }
        return Optional.empty();
    }

    private static Clinician mapClinician(CsvTable t, List<String> r) {
        return new Clinician(
                t.get(r, "clinician_id"),
                t.get(r, "first_name"),
                t.get(r, "last_name"),
                t.get(r, "title"),
                t.get(r, "speciality"),
                t.get(r, "gmc_number"),
                t.get(r, "phone_number"),
                t.get(r, "email"),
                t.get(r, "workplace_id"),
                t.get(r, "workplace_type"),
                t.get(r, "employment_status"),
                DateTimes.parseDate(t.get(r, "start_date"))
        );
    }

    private static Facility mapFacility(CsvTable t, List<String> r) {
        return new Facility(
                t.get(r, "facility_id"),
                t.get(r, "facility_name"),
                t.get(r, "facility_type"),
                t.get(r, "address"),
                t.get(r, "postcode"),
                t.get(r, "phone_number"),
                t.get(r, "email"),
                t.get(r, "opening_hours"),
                t.get(r, "manager_name"),
                Numbers.parseIntOrDefault(t.get(r, "capacity"), 0),
                t.get(r, "specialities_offered")
        );
    }

    private static Staff mapStaff(CsvTable t, List<String> r) {
        return new Staff(
                t.get(r, "staff_id"),
                t.get(r, "first_name"),
                t.get(r, "last_name"),
                t.get(r, "role"),
                t.get(r, "department"),
                t.get(r, "facility_id"),
                t.get(r, "phone_number"),
                t.get(r, "email"),
                t.get(r, "employment_status"),
                DateTimes.parseDate(t.get(r, "start_date")),
                t.get(r, "line_manager"),
                t.get(r, "access_level")
        );
    }
}

