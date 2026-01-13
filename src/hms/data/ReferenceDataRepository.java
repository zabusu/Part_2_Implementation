package hms.data;

import hms.model.Clinician;
import hms.model.Facility;
import hms.model.Staff;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

/**
 * Read-only access to reference datasets (clinicians, facilities, staff).
 * These CSVs typically serve as lookup tables rather than frequently-edited records.
 */
public interface ReferenceDataRepository {
    List<Clinician> clinicians() throws IOException;
    Optional<Clinician> clinicianById(String clinicianId) throws IOException;

    List<Facility> facilities() throws IOException;
    Optional<Facility> facilityById(String facilityId) throws IOException;

    List<Staff> staff() throws IOException;
    Optional<Staff> staffById(String staffId) throws IOException;
}

