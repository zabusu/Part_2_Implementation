package hms.model;

import java.time.LocalDate;

public final class Patient {
    private final String patientId;
    private final String firstName;
    private final String lastName;
    private final LocalDate dateOfBirth;
    private final String nhsNumber;
    private final String gender;
    private final String phoneNumber;
    private final String email;
    private final String address;
    private final String postcode;
    private final String emergencyContactName;
    private final String emergencyContactPhone;
    private final LocalDate registrationDate;
    private final String gpSurgeryId;

    public Patient(
            String patientId,
            String firstName,
            String lastName,
            LocalDate dateOfBirth,
            String nhsNumber,
            String gender,
            String phoneNumber,
            String email,
            String address,
            String postcode,
            String emergencyContactName,
            String emergencyContactPhone,
            LocalDate registrationDate,
            String gpSurgeryId
    ) {
        this.patientId = patientId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.dateOfBirth = dateOfBirth;
        this.nhsNumber = nhsNumber;
        this.gender = gender;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.address = address;
        this.postcode = postcode;
        this.emergencyContactName = emergencyContactName;
        this.emergencyContactPhone = emergencyContactPhone;
        this.registrationDate = registrationDate;
        this.gpSurgeryId = gpSurgeryId;
    }

    public String patientId() { return patientId; }
    public String firstName() { return firstName; }
    public String lastName() { return lastName; }
    public LocalDate dateOfBirth() { return dateOfBirth; }
    public String nhsNumber() { return nhsNumber; }
    public String gender() { return gender; }
    public String phoneNumber() { return phoneNumber; }
    public String email() { return email; }
    public String address() { return address; }
    public String postcode() { return postcode; }
    public String emergencyContactName() { return emergencyContactName; }
    public String emergencyContactPhone() { return emergencyContactPhone; }
    public LocalDate registrationDate() { return registrationDate; }
    public String gpSurgeryId() { return gpSurgeryId; }
}

