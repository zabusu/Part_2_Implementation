package hms.data;

import hms.model.Referral;
import hms.util.Csv;
import hms.util.DateTimes;
import hms.util.IO;

import java.io.IOException;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public final class CsvReferralRepository implements ReferralRepository {
    private static final String HEADER =
            "referral_id,patient_id,referring_clinician_id,referred_to_clinician_id,referring_facility_id,referred_to_facility_id,referral_date,urgency_level,referral_reason,clinical_summary,requested_investigations,status,appointment_id,notes,created_date,last_updated";

    private final Path csvPath;

    public CsvReferralRepository(Path csvPath) {
        this.csvPath = csvPath;
    }

    @Override
    public List<Referral> findAll() throws IOException {
        CsvTable table = CsvLoader.load(csvPath);
        List<Referral> out = new ArrayList<>();
        for (List<String> row : table.rows()) {
            out.add(map(table, row));
        }
        return out;
    }

    @Override
    public Optional<Referral> findById(String referralId) throws IOException {
        if (referralId == null) return Optional.empty();
        String target = referralId.trim();
        if (target.isEmpty()) return Optional.empty();

        CsvTable table = CsvLoader.load(csvPath);
        for (List<String> row : table.rows()) {
            if (target.equals(table.get(row, "referral_id"))) {
                return Optional.of(map(table, row));
            }
        }
        return Optional.empty();
    }

    @Override
    public void saveAll(List<Referral> referrals) throws IOException {
        List<String> lines = new ArrayList<>();
        lines.add(HEADER);
        for (Referral r : referrals) {
            lines.add(toLine(r));
        }
        IO.writeAllLines(csvPath, lines);
    }

    private static Referral map(CsvTable t, List<String> r) {
        return new Referral(
                t.get(r, "referral_id"),
                t.get(r, "patient_id"),
                t.get(r, "referring_clinician_id"),
                t.get(r, "referred_to_clinician_id"),
                t.get(r, "referring_facility_id"),
                t.get(r, "referred_to_facility_id"),
                DateTimes.parseDate(t.get(r, "referral_date")),
                t.get(r, "urgency_level"),
                t.get(r, "referral_reason"),
                t.get(r, "clinical_summary"),
                t.get(r, "requested_investigations"),
                t.get(r, "status"),
                t.get(r, "appointment_id"),
                t.get(r, "notes"),
                DateTimes.parseDate(t.get(r, "created_date")),
                DateTimes.parseDate(t.get(r, "last_updated"))
        );
    }

    private static String toLine(Referral r) {
        List<String> fields = new ArrayList<>(16);
        fields.add(n(r.referralId()));
        fields.add(n(r.patientId()));
        fields.add(n(r.referringClinicianId()));
        fields.add(n(r.referredToClinicianId()));
        fields.add(n(r.referringFacilityId()));
        fields.add(n(r.referredToFacilityId()));
        fields.add(n(d(r.referralDate())));
        fields.add(n(r.urgencyLevel()));
        fields.add(n(r.referralReason()));
        fields.add(n(r.clinicalSummary()));
        fields.add(n(r.requestedInvestigations()));
        fields.add(n(r.status()));
        fields.add(n(r.appointmentId()));
        fields.add(n(r.notes()));
        fields.add(n(d(r.createdDate())));
        fields.add(n(d(r.lastUpdated())));
        return Csv.toLine(fields);
    }

    private static String n(String s) { return s == null ? "" : s; }
    private static String d(LocalDate d) { return d == null ? "" : d.toString(); }
}

