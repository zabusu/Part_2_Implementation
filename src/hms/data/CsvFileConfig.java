package hms.data;

import java.nio.file.Path;

public final class CsvFileConfig {
    private final Path patientsCsv;
    private final Path cliniciansCsv;
    private final Path facilitiesCsv;
    private final Path appointmentsCsv;
    private final Path prescriptionsCsv;
    private final Path referralsCsv;
    private final Path staffCsv;

    private final Path referralOutDir;

    public CsvFileConfig(
            Path patientsCsv,
            Path cliniciansCsv,
            Path facilitiesCsv,
            Path appointmentsCsv,
            Path prescriptionsCsv,
            Path referralsCsv,
            Path staffCsv,
            Path referralOutDir
    ) {
        this.patientsCsv = patientsCsv;
        this.cliniciansCsv = cliniciansCsv;
        this.facilitiesCsv = facilitiesCsv;
        this.appointmentsCsv = appointmentsCsv;
        this.prescriptionsCsv = prescriptionsCsv;
        this.referralsCsv = referralsCsv;
        this.staffCsv = staffCsv;
        this.referralOutDir = referralOutDir;
    }

    public static CsvFileConfig fromProjectRoot(Path projectRoot) {
        Path dataDir = projectRoot.resolve("data");
        return new CsvFileConfig(
                dataDir.resolve("patients.csv"),
                dataDir.resolve("clinicians.csv"),
                dataDir.resolve("facilities.csv"),
                dataDir.resolve("appointments.csv"),
                dataDir.resolve("prescriptions.csv"),
                dataDir.resolve("referrals.csv"),
                dataDir.resolve("staff.csv"),
                projectRoot.resolve("referral_out")
        );
    }

    public Path patientsCsv() { return patientsCsv; }
    public Path cliniciansCsv() { return cliniciansCsv; }
    public Path facilitiesCsv() { return facilitiesCsv; }
    public Path appointmentsCsv() { return appointmentsCsv; }
    public Path prescriptionsCsv() { return prescriptionsCsv; }
    public Path referralsCsv() { return referralsCsv; }
    public Path staffCsv() { return staffCsv; }

    public Path referralOutDir() { return referralOutDir; }
}

