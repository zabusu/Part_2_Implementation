package hms.service;

import hms.data.PatientRepository;
import hms.data.ReferralRepository;
import hms.data.ReferenceDataRepository;
import hms.model.Referral;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public final class ReferralService {
    private final ReferralRepository referrals;
    private final PatientRepository patients;
    private final ReferenceDataRepository refData;

    public ReferralService(
            ReferralRepository referrals,
            PatientRepository patients,
            ReferenceDataRepository refData
    ) {
        this.referrals = referrals;
        this.patients = patients;
        this.refData = refData;
    }

    public List<Referral> listAll() throws IOException {
        return referrals.findAll();
    }

    public Referral create(
            String patientId,
            String referringClinicianId,
            String referredToClinicianId,
            String referringFacilityId,
            String referredToFacilityId,
            LocalDate referralDate,
            String urgencyLevel,
            String referralReason,
            String clinicalSummary,
            String requestedInvestigations,
            String appointmentId,
            String notes
    ) throws IOException {
        requireNonBlank(patientId, "patientId");
        requireNonBlank(referringClinicianId, "referringClinicianId");
        requireNonBlank(referredToClinicianId, "referredToClinicianId");
        requireNonBlank(referringFacilityId, "referringFacilityId");
        requireNonBlank(referredToFacilityId, "referredToFacilityId");
        if (referralDate == null) referralDate = LocalDate.now();
        requireNonBlank(urgencyLevel, "urgencyLevel");
        requireNonBlank(referralReason, "referralReason");
        requireNonBlank(clinicalSummary, "clinicalSummary");
        requireNonBlank(requestedInvestigations, "requestedInvestigations");

        if (patients.findById(patientId).isEmpty()) {
            throw new ValidationException("Unknown patient_id: " + patientId);
        }
        if (refData.clinicianById(referringClinicianId).isEmpty()) {
            throw new ValidationException("Unknown referring_clinician_id: " + referringClinicianId);
        }
        if (refData.clinicianById(referredToClinicianId).isEmpty()) {
            throw new ValidationException("Unknown referred_to_clinician_id: " + referredToClinicianId);
        }
        if (refData.facilityById(referringFacilityId).isEmpty()) {
            throw new ValidationException("Unknown referring_facility_id: " + referringFacilityId);
        }
        if (refData.facilityById(referredToFacilityId).isEmpty()) {
            throw new ValidationException("Unknown referred_to_facility_id: " + referredToFacilityId);
        }

        List<Referral> all = new ArrayList<>(referrals.findAll());
        List<String> ids = all.stream().map(Referral::referralId).toList();
        String newId = Ids.nextId("R", 3, ids);

        Referral created = new Referral(
                newId,
                patientId,
                referringClinicianId,
                referredToClinicianId,
                referringFacilityId,
                referredToFacilityId,
                referralDate,
                urgencyLevel,
                referralReason,
                clinicalSummary,
                requestedInvestigations,
                "New",
                appointmentId == null ? "" : appointmentId,
                notes == null ? "" : notes,
                LocalDate.now(),
                LocalDate.now()
        );

        all.add(created);
        referrals.saveAll(all);
        return created;
    }

    private static void requireNonBlank(String s, String field) {
        if (s == null || s.trim().isEmpty()) throw new ValidationException(field + " is required");
    }
}

