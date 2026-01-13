package hms.service;

import hms.data.PatientRepository;
import hms.data.ReferralRepository;
import hms.data.ReferenceDataRepository;
import hms.model.Clinician;
import hms.model.Facility;
import hms.model.Patient;
import hms.model.Referral;
import hms.util.IO;

import java.io.IOException;
import java.nio.file.Path;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * Referral management Singleton.
 *
 * Responsibilities (per coursework):
 * - single shared instance to coordinate referral processing
 * - queue referrals to be processed
 * - generate email-style text output (no real email)
 * - write EHR update log entries
 * - maintain audit trail + prevent duplicate processing
 */
public final class ReferralManager {
    private static volatile ReferralManager instance;

    private final ReferralRepository referrals;
    private final PatientRepository patients;
    private final ReferenceDataRepository refData;
    private final Path outDir;
    private final Path auditLog;
    private final Path ehrLog;

    private final Deque<String> referralQueue = new ArrayDeque<>();
    private final Set<String> processedReferralIds = new HashSet<>();

    private static final DateTimeFormatter TS =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").withZone(ZoneId.systemDefault());

    private ReferralManager(
            ReferralRepository referrals,
            PatientRepository patients,
            ReferenceDataRepository refData,
            Path outDir
    ) throws IOException {
        this.referrals = referrals;
        this.patients = patients;
        this.refData = refData;
        this.outDir = outDir;
        this.auditLog = outDir.resolve("referral_audit.log");
        this.ehrLog = outDir.resolve("ehr_updates.log");

        IO.ensureDir(outDir);
        bootstrapProcessedFromAudit();
    }

    /**
     * Initialize the singleton (call once at app startup).
     */
    public static ReferralManager init(
            ReferralRepository referrals,
            PatientRepository patients,
            ReferenceDataRepository refData,
            Path outDir
    ) throws IOException {
        if (instance == null) {
            synchronized (ReferralManager.class) {
                if (instance == null) {
                    instance = new ReferralManager(referrals, patients, refData, outDir);
                }
            }
        }
        return instance;
    }

    /**
     * Get the singleton instance (must call {@link #init} first).
     */
    public static ReferralManager getInstance() {
        ReferralManager inst = instance;
        if (inst == null) {
            throw new IllegalStateException("ReferralManager not initialized. Call ReferralManager.init(...) first.");
        }
        return inst;
    }

    /**
     * Enqueue by referral ID. Duplicate prevention:
     * - If the referral was already processed (audit says SENT), it will NOT be queued again.
     * - If it is already in the queue, it will NOT be added again.
     */
    public synchronized boolean enqueue(String referralId) throws IOException {
        if (referralId == null || referralId.trim().isEmpty()) return false;
        String id = referralId.trim();

        if (processedReferralIds.contains(id)) {
            audit("SKIP_DUPLICATE", id, "Already processed (audit)");
            return false;
        }
        if (referralQueue.contains(id)) {
            audit("SKIP_DUPLICATE", id, "Already queued");
            return false;
        }

        // Ensure referral exists
        if (referrals.findById(id).isEmpty()) {
            audit("SKIP_MISSING", id, "Referral not found in CSV");
            return false;
        }

        referralQueue.addLast(id);
        audit("ENQUEUE", id, "Queued for processing");
        return true;
    }

    /**
     * Process the next referral in the queue (if any).
     * Generates email text file + EHR update log + audit trail entry.
     */
    public synchronized Optional<String> processNext() throws IOException {
        String id = referralQueue.pollFirst();
        if (id == null) return Optional.empty();

        Optional<Referral> maybeReferral = referrals.findById(id);
        if (maybeReferral.isEmpty()) {
            audit("PROCESS_FAIL", id, "Referral missing at processing time");
            return Optional.empty();
        }

        Referral r = maybeReferral.get();
        if (processedReferralIds.contains(id)) {
            audit("SKIP_DUPLICATE", id, "Already processed (late)");
            return Optional.empty();
        }

        String emailText = buildReferralEmailText(r);
        Path emailFile = outDir.resolve("referral_" + id + ".txt");
        IO.writeAllLines(emailFile, List.of(emailText.split("\\R", -1)));
        audit("EMAIL_GENERATED", id, "Wrote " + emailFile.getFileName());

        writeEhrUpdate(r);
        audit("EHR_UPDATED", id, "Appended EHR update log");

        processedReferralIds.add(id);
        audit("SENT", id, "Referral processed successfully");
        return Optional.of(emailFile.toString());
    }

    public synchronized int queueSize() {
        return referralQueue.size();
    }

    public synchronized void clearQueue() throws IOException {
        referralQueue.clear();
        audit("QUEUE_CLEARED", "-", "Queue cleared");
    }

    private void writeEhrUpdate(Referral r) throws IOException {
        String line = TS.format(Instant.now())
                + " | referral_id=" + r.referralId()
                + " | patient_id=" + r.patientId()
                + " | from=" + r.referringFacilityId()
                + " | to=" + r.referredToFacilityId()
                + " | status=" + r.status();
        IO.appendLine(ehrLog, line);
    }

    private void audit(String action, String referralId, String details) throws IOException {
        String line = TS.format(Instant.now())
                + " | action=" + action
                + " | referral_id=" + referralId
                + " | details=" + (details == null ? "" : details);
        IO.appendLine(auditLog, line);
    }

    private void bootstrapProcessedFromAudit() throws IOException {
        List<String> lines = IO.readAllLines(auditLog);
        for (String line : lines) {
            if (line == null) continue;
            if (!line.contains("action=SENT")) continue;
            String id = extractField(line, "referral_id");
            if (id != null && !id.isBlank()) processedReferralIds.add(id.trim());
        }
    }

    private static String extractField(String line, String key) {
        // very simple parser: "... | key=value | ..."
        String needle = key + "=";
        int idx = line.indexOf(needle);
        if (idx < 0) return null;
        int start = idx + needle.length();
        int end = line.indexOf(" |", start);
        if (end < 0) end = line.length();
        return line.substring(start, end).trim();
    }

    private String buildReferralEmailText(Referral r) throws IOException {
        Optional<Patient> p = patients.findById(r.patientId());
        Optional<Clinician> fromClin = refData.clinicianById(r.referringClinicianId());
        Optional<Clinician> toClin = refData.clinicianById(r.referredToClinicianId());
        Optional<Facility> fromFac = refData.facilityById(r.referringFacilityId());
        Optional<Facility> toFac = refData.facilityById(r.referredToFacilityId());

        String patientName = p.map(x -> x.firstName() + " " + x.lastName()).orElse(r.patientId());

        String subject = "Referral " + r.referralId() + " (" + r.urgencyLevel() + ") - " + patientName;

        LocalDate refDate = r.referralDate() == null ? LocalDate.now() : r.referralDate();

        StringBuilder sb = new StringBuilder();
        sb.append("TO: ").append(toFac.map(Facility::email).orElse("[hospital email unknown]")).append("\n");
        sb.append("FROM: ").append(fromFac.map(Facility::email).orElse("[gp surgery email unknown]")).append("\n");
        sb.append("SUBJECT: ").append(subject).append("\n");
        sb.append("\n");
        sb.append("Referral ID: ").append(r.referralId()).append("\n");
        sb.append("Date: ").append(refDate).append("\n");
        sb.append("Urgency: ").append(r.urgencyLevel()).append("\n");
        sb.append("\n");
        sb.append("Patient: ").append(patientName).append(" (").append(r.patientId()).append(")\n");
        sb.append("Referring clinician: ").append(fromClin.map(x -> x.firstName() + " " + x.lastName()).orElse(r.referringClinicianId())).append("\n");
        sb.append("Referred to clinician: ").append(toClin.map(x -> x.firstName() + " " + x.lastName()).orElse(r.referredToClinicianId())).append("\n");
        sb.append("From facility: ").append(fromFac.map(Facility::facilityName).orElse(r.referringFacilityId())).append("\n");
        sb.append("To facility: ").append(toFac.map(Facility::facilityName).orElse(r.referredToFacilityId())).append("\n");
        sb.append("\n");
        sb.append("Referral reason:\n").append(r.referralReason()).append("\n");
        sb.append("\n");
        sb.append("Clinical summary:\n").append(r.clinicalSummary()).append("\n");
        sb.append("\n");
        sb.append("Requested investigations:\n").append(r.requestedInvestigations()).append("\n");
        sb.append("\n");
        if (r.appointmentId() != null && !r.appointmentId().isBlank()) {
            sb.append("Linked appointment: ").append(r.appointmentId()).append("\n");
        }
        if (r.notes() != null && !r.notes().isBlank()) {
            sb.append("Notes: ").append(r.notes()).append("\n");
        }
        sb.append("\n");
        sb.append("----\n");
        sb.append("This message was generated by the Healthcare Management System for coursework demonstration purposes.\n");
        return sb.toString();
    }
}

