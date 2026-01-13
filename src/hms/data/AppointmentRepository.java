package hms.data;

import hms.model.Appointment;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface AppointmentRepository {
    List<Appointment> findAll() throws IOException;
    Optional<Appointment> findById(String appointmentId) throws IOException;
    void saveAll(List<Appointment> appointments) throws IOException;
}

