package hms.model;

import java.time.LocalDate;

public final class Staff {
    private final String staffId;
    private final String firstName;
    private final String lastName;
    private final String role;
    private final String department;
    private final String facilityId;
    private final String phoneNumber;
    private final String email;
    private final String employmentStatus;
    private final LocalDate startDate;
    private final String lineManager;
    private final String accessLevel;

    public Staff(
            String staffId,
            String firstName,
            String lastName,
            String role,
            String department,
            String facilityId,
            String phoneNumber,
            String email,
            String employmentStatus,
            LocalDate startDate,
            String lineManager,
            String accessLevel
    ) {
        this.staffId = staffId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.role = role;
        this.department = department;
        this.facilityId = facilityId;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.employmentStatus = employmentStatus;
        this.startDate = startDate;
        this.lineManager = lineManager;
        this.accessLevel = accessLevel;
    }

    public String staffId() { return staffId; }
    public String firstName() { return firstName; }
    public String lastName() { return lastName; }
    public String role() { return role; }
    public String department() { return department; }
    public String facilityId() { return facilityId; }
    public String phoneNumber() { return phoneNumber; }
    public String email() { return email; }
    public String employmentStatus() { return employmentStatus; }
    public LocalDate startDate() { return startDate; }
    public String lineManager() { return lineManager; }
    public String accessLevel() { return accessLevel; }
}

