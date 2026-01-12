package hms.model;

import java.time.LocalDate;

public final class Clinician {
    private final String clinicianId;
    private final String firstName;
    private final String lastName;
    private final String title;
    private final String speciality;
    private final String gmcNumber;
    private final String phoneNumber;
    private final String email;
    private final String workplaceId;
    private final String workplaceType;
    private final String employmentStatus;
    private final LocalDate startDate;

    public Clinician(
            String clinicianId,
            String firstName,
            String lastName,
            String title,
            String speciality,
            String gmcNumber,
            String phoneNumber,
            String email,
            String workplaceId,
            String workplaceType,
            String employmentStatus,
            LocalDate startDate
    ) {
        this.clinicianId = clinicianId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.title = title;
        this.speciality = speciality;
        this.gmcNumber = gmcNumber;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.workplaceId = workplaceId;
        this.workplaceType = workplaceType;
        this.employmentStatus = employmentStatus;
        this.startDate = startDate;
    }

    public String clinicianId() { return clinicianId; }
    public String firstName() { return firstName; }
    public String lastName() { return lastName; }
    public String title() { return title; }
    public String speciality() { return speciality; }
    public String gmcNumber() { return gmcNumber; }
    public String phoneNumber() { return phoneNumber; }
    public String email() { return email; }
    public String workplaceId() { return workplaceId; }
    public String workplaceType() { return workplaceType; }
    public String employmentStatus() { return employmentStatus; }
    public LocalDate startDate() { return startDate; }
}

