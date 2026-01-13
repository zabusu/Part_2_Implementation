package hms.data;

import hms.model.Prescription;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface PrescriptionRepository {
    List<Prescription> findAll() throws IOException;
    Optional<Prescription> findById(String prescriptionId) throws IOException;
    void append(Prescription prescription) throws IOException;
}

