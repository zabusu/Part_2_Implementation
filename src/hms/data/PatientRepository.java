package hms.data;

import hms.model.Patient;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface PatientRepository {
    List<Patient> findAll() throws IOException;
    Optional<Patient> findById(String patientId) throws IOException;
}

