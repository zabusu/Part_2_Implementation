package hms.app;

import hms.data.*;
import hms.service.*;

import java.io.IOException;
import java.nio.file.Path;


 //Simple dependency container  that wires the 3 tiers together
 
public final class AppContext {
    public final CsvFileConfig config;

    // Data tier
    public final PatientRepository patientRepository;
    public final AppointmentRepository appointmentRepository;
    public final PrescriptionRepository prescriptionRepository;
    public final ReferralRepository referralRepository;
    public final ReferenceDataRepository referenceDataRepository;

    // Business tier
    public final PatientService patientService;
    public final AppointmentService appointmentService;
    public final PrescriptionService prescriptionService;
    public final ReferralService referralService;
    public final ReferralManager referralManager;

    private AppContext(
            CsvFileConfig config,
            PatientRepository patientRepository,
            AppointmentRepository appointmentRepository,
            PrescriptionRepository prescriptionRepository,
            ReferralRepository referralRepository,
            ReferenceDataRepository referenceDataRepository,
            PatientService patientService,
            AppointmentService appointmentService,
            PrescriptionService prescriptionService,
            ReferralService referralService,
            ReferralManager referralManager
    ) {
        this.config = config;
        this.patientRepository = patientRepository;
        this.appointmentRepository = appointmentRepository;
        this.prescriptionRepository = prescriptionRepository;
        this.referralRepository = referralRepository;
        this.referenceDataRepository = referenceDataRepository;
        this.patientService = patientService;
        this.appointmentService = appointmentService;
        this.prescriptionService = prescriptionService;
        this.referralService = referralService;
        this.referralManager = referralManager;
    }

    public static AppContext create(Path projectRoot) throws IOException {
        CsvFileConfig config = CsvFileConfig.fromProjectRoot(projectRoot);

        PatientRepository patients = new CsvPatientRepository(config.patientsCsv());
        ReferenceDataRepository ref = new CsvReferenceDataRepository(
                config.cliniciansCsv(),
                config.facilitiesCsv(),
                config.staffCsv()
        );
        AppointmentRepository appts = new CsvAppointmentRepository(config.appointmentsCsv());
        PrescriptionRepository rxs = new CsvPrescriptionRepository(config.prescriptionsCsv());
        ReferralRepository refs = new CsvReferralRepository(config.referralsCsv());

        PatientService patientService = new PatientService(patients);
        AppointmentService appointmentService = new AppointmentService(appts, patients, ref);
        PrescriptionService prescriptionService = new PrescriptionService(rxs, patients, ref);
        ReferralService referralService = new ReferralService(refs, patients, ref);

        ReferralManager referralManager = ReferralManager.init(refs, patients, ref, config.referralOutDir());

        return new AppContext(
                config,
                patients,
                appts,
                rxs,
                refs,
                ref,
                patientService,
                appointmentService,
                prescriptionService,
                referralService,
                referralManager
        );
    }
}

