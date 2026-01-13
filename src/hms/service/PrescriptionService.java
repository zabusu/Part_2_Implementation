package hms.service;

import hms.data.PatientRepository;
import hms.data.PrescriptionRepository;
import hms.data.ReferenceDataRepository;
import hms.model.Prescription;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public final class PrescriptionService {
    private final PrescriptionRepository prescriptions;
    private final PatientRepository patients;
    private final ReferenceDataRepository refData;

    public PrescriptionService(
            PrescriptionRepository prescriptions,
            PatientRepository patients,
            ReferenceDataRepository refData
    ) {
        this.prescriptions = prescriptions;
        this.patients = patients;
        this.refData = refData;
    }

    public List<Prescription> listAll() throws IOException {
        return prescriptions.findAll();
    }

    public Prescription create(
            String patientId,
            String clinicianId,
            String appointmentId,
            LocalDate prescriptionDate,
            String medicationName,
            String dosage,
            String frequency,
            int durationDays,
            String quantity,
            String instructions,
            String pharmacyName
    ) throws IOException {
        requireNonBlank(patientId, "patientId");
        requireNonBlank(clinicianId, "clinicianId");
        if (prescriptionDate == null) prescriptionDate = LocalDate.now();
        requireNonBlank(medicationName, "medicationName");
        requireNonBlank(dosage, "dosage");
        requireNonBlank(frequency, "frequency");
        if (durationDays <= 0) throw new ValidationException("durationDays must be > 0");
        requireNonBlank(quantity, "quantity");
        requireNonBlank(instructions, "instructions");
        requireNonBlank(pharmacyName, "pharmacyName");

        if (patients.findById(patientId).isEmpty()) {
            throw new ValidationException("Unknown patient_id: " + patientId);
        }
        if (refData.clinicianById(clinicianId).isEmpty()) {
            throw new ValidationException("Unknown clinician_id: " + clinicianId);
        }

        List<Prescription> all = new ArrayList<>(prescriptions.findAll());
        List<String> ids = all.stream().map(Prescription::prescriptionId).toList();
        String newId = Ids.nextId("RX", 3, ids);

        Prescription created = new Prescription(
                newId,
                patientId,
                clinicianId,
                appointmentId == null ? "" : appointmentId,
                prescriptionDate,
                medicationName,
                dosage,
                frequency,
                durationDays,
                quantity,
                instructions,
                pharmacyName,
                "Issued",
                LocalDate.now(),
                null
        );

        prescriptions.append(created);
        return created;
    }

    private static void requireNonBlank(String s, String field) {
        if (s == null || s.trim().isEmpty()) throw new ValidationException(field + " is required");
    }
}

