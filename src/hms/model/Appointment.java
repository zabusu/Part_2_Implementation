package hms.model;

import java.time.LocalDate;
import java.time.LocalTime;

public final class Appointment {
    private final String appointmentId;
    private final String patientId;
    private final String clinicianId;
    private final String facilityId;
    private final LocalDate appointmentDate;
    private final LocalTime appointmentTime;
    private final int durationMinutes;
    private final String appointmentType;
    private final String status;
    private final String reasonForVisit;
    private final String notes;
    private final LocalDate createdDate;
    private final LocalDate lastModified;

    public Appointment(
            String appointmentId,
            String patientId,
            String clinicianId,
            String facilityId,
            LocalDate appointmentDate,
            LocalTime appointmentTime,
            int durationMinutes,
            String appointmentType,
            String status,
            String reasonForVisit,
            String notes,
            LocalDate createdDate,
            LocalDate lastModified
    ) {
        this.appointmentId = appointmentId;
        this.patientId = patientId;
        this.clinicianId = clinicianId;
        this.facilityId = facilityId;
        this.appointmentDate = appointmentDate;
        this.appointmentTime = appointmentTime;
        this.durationMinutes = durationMinutes;
        this.appointmentType = appointmentType;
        this.status = status;
        this.reasonForVisit = reasonForVisit;
        this.notes = notes;
        this.createdDate = createdDate;
        this.lastModified = lastModified;
    }

    public String appointmentId() { return appointmentId; }
    public String patientId() { return patientId; }
    public String clinicianId() { return clinicianId; }
    public String facilityId() { return facilityId; }
    public LocalDate appointmentDate() { return appointmentDate; }
    public LocalTime appointmentTime() { return appointmentTime; }
    public int durationMinutes() { return durationMinutes; }
    public String appointmentType() { return appointmentType; }
    public String status() { return status; }
    public String reasonForVisit() { return reasonForVisit; }
    public String notes() { return notes; }
    public LocalDate createdDate() { return createdDate; }
    public LocalDate lastModified() { return lastModified; }
}

