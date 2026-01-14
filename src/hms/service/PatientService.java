package hms.service;

import hms.data.PatientRepository;
import hms.model.Patient;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public final class PatientService {
    private final PatientRepository patients;

    public PatientService(PatientRepository patients) {
        this.patients = patients;
    }

    public List<Patient> listAll() throws IOException {
        return patients.findAll();
    }

    public Patient register(
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
            String gpSurgeryId
    ) throws IOException {
        requireNonBlank(firstName, "firstName");
        requireNonBlank(lastName, "lastName");
        if (dateOfBirth == null) throw new ValidationException("dateOfBirth is required");
        requireNonBlank(nhsNumber, "nhsNumber");
        requireNonBlank(gender, "gender");
        requireNonBlank(phoneNumber, "phoneNumber");
        requireNonBlank(email, "email");
        requireNonBlank(address, "address");
        requireNonBlank(postcode, "postcode");
        requireNonBlank(emergencyContactName, "emergencyContactName");
        requireNonBlank(emergencyContactPhone, "emergencyContactPhone");
        requireNonBlank(gpSurgeryId, "gpSurgeryId");

        List<Patient> all = new ArrayList<>(patients.findAll());
        String newId = Ids.nextId("P", 3, all.stream().map(Patient::patientId).toList());

        Patient created = new Patient(
                newId,
                firstName,
                lastName,
                dateOfBirth,
                nhsNumber,
                gender,
                phoneNumber,
                email,
                address,
                postcode,
                emergencyContactName,
                emergencyContactPhone,
                LocalDate.now(),
                gpSurgeryId
        );

        all.add(created);
        patients.saveAll(all);
        return created;
    }

    public Patient update(Patient updated) throws IOException {
        requireNonBlank(updated.patientId(), "patientId");
        List<Patient> all = new ArrayList<>(patients.findAll());
        for (int i = 0; i < all.size(); i++) {
            if (updated.patientId().equals(all.get(i).patientId())) {
                all.set(i, updated);
                patients.saveAll(all);
                return updated;
            }
        }
        throw new ValidationException("Unknown patient_id: " + updated.patientId());
    }

    public void delete(String patientId) throws IOException {
        requireNonBlank(patientId, "patientId");
        List<Patient> all = new ArrayList<>(patients.findAll());
        boolean removed = all.removeIf(p -> patientId.equals(p.patientId()));
        if (!removed) throw new ValidationException("Unknown patient_id: " + patientId);
        patients.saveAll(all);
    }

    private static void requireNonBlank(String s, String field) {
        if (s == null || s.trim().isEmpty()) throw new ValidationException(field + " is required");
    }
}
