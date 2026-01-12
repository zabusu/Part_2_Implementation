package hms.model;

import java.time.LocalDate;

public final class Referral {
    private final String referralId;
    private final String patientId;
    private final String referringClinicianId;
    private final String referredToClinicianId;
    private final String referringFacilityId;
    private final String referredToFacilityId;
    private final LocalDate referralDate;
    private final String urgencyLevel;
    private final String referralReason;
    private final String clinicalSummary;
    private final String requestedInvestigations;
    private final String status;
    private final String appointmentId; // may be empty
    private final String notes;
    private final LocalDate createdDate;
    private final LocalDate lastUpdated;

    public Referral(
            String referralId,
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
            String status,
            String appointmentId,
            String notes,
            LocalDate createdDate,
            LocalDate lastUpdated
    ) {
        this.referralId = referralId;
        this.patientId = patientId;
        this.referringClinicianId = referringClinicianId;
        this.referredToClinicianId = referredToClinicianId;
        this.referringFacilityId = referringFacilityId;
        this.referredToFacilityId = referredToFacilityId;
        this.referralDate = referralDate;
        this.urgencyLevel = urgencyLevel;
        this.referralReason = referralReason;
        this.clinicalSummary = clinicalSummary;
        this.requestedInvestigations = requestedInvestigations;
        this.status = status;
        this.appointmentId = appointmentId;
        this.notes = notes;
        this.createdDate = createdDate;
        this.lastUpdated = lastUpdated;
    }

    public String referralId() { return referralId; }
    public String patientId() { return patientId; }
    public String referringClinicianId() { return referringClinicianId; }
    public String referredToClinicianId() { return referredToClinicianId; }
    public String referringFacilityId() { return referringFacilityId; }
    public String referredToFacilityId() { return referredToFacilityId; }
    public LocalDate referralDate() { return referralDate; }
    public String urgencyLevel() { return urgencyLevel; }
    public String referralReason() { return referralReason; }
    public String clinicalSummary() { return clinicalSummary; }
    public String requestedInvestigations() { return requestedInvestigations; }
    public String status() { return status; }
    public String appointmentId() { return appointmentId; }
    public String notes() { return notes; }
    public LocalDate createdDate() { return createdDate; }
    public LocalDate lastUpdated() { return lastUpdated; }
}

