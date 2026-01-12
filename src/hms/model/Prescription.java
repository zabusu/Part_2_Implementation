package hms.model;

import java.time.LocalDate;

public final class Prescription {
    private final String prescriptionId;
    private final String patientId;
    private final String clinicianId;
    private final String appointmentId; // may be empty
    private final LocalDate prescriptionDate;
    private final String medicationName;
    private final String dosage;
    private final String frequency;
    private final int durationDays;
    private final String quantity;
    private final String instructions;
    private final String pharmacyName;
    private final String status;
    private final LocalDate issueDate;
    private final LocalDate collectionDate; // may be null

    public Prescription(
            String prescriptionId,
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
            String pharmacyName,
            String status,
            LocalDate issueDate,
            LocalDate collectionDate
    ) {
        this.prescriptionId = prescriptionId;
        this.patientId = patientId;
        this.clinicianId = clinicianId;
        this.appointmentId = appointmentId;
        this.prescriptionDate = prescriptionDate;
        this.medicationName = medicationName;
        this.dosage = dosage;
        this.frequency = frequency;
        this.durationDays = durationDays;
        this.quantity = quantity;
        this.instructions = instructions;
        this.pharmacyName = pharmacyName;
        this.status = status;
        this.issueDate = issueDate;
        this.collectionDate = collectionDate;
    }

    public String prescriptionId() { return prescriptionId; }
    public String patientId() { return patientId; }
    public String clinicianId() { return clinicianId; }
    public String appointmentId() { return appointmentId; }
    public LocalDate prescriptionDate() { return prescriptionDate; }
    public String medicationName() { return medicationName; }
    public String dosage() { return dosage; }
    public String frequency() { return frequency; }
    public int durationDays() { return durationDays; }
    public String quantity() { return quantity; }
    public String instructions() { return instructions; }
    public String pharmacyName() { return pharmacyName; }
    public String status() { return status; }
    public LocalDate issueDate() { return issueDate; }
    public LocalDate collectionDate() { return collectionDate; }
}

