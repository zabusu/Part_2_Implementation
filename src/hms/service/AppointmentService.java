package hms.service;

import hms.data.AppointmentRepository;
import hms.data.PatientRepository;
import hms.data.ReferenceDataRepository;
import hms.model.Appointment;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public final class AppointmentService {
    private final AppointmentRepository appointments;
    private final PatientRepository patients;
    private final ReferenceDataRepository refData;

    public AppointmentService(
            AppointmentRepository appointments,
            PatientRepository patients,
            ReferenceDataRepository refData
    ) {
        this.appointments = appointments;
        this.patients = patients;
        this.refData = refData;
    }

    public List<Appointment> listAll() throws IOException {
        return appointments.findAll();
    }

    public Appointment create(
            String patientId,
            String clinicianId,
            String facilityId,
            LocalDate date,
            LocalTime time,
            int durationMinutes,
            String appointmentType,
            String reasonForVisit,
            String notes
    ) throws IOException {
        requireNonBlank(patientId, "patientId");
        requireNonBlank(clinicianId, "clinicianId");
        requireNonBlank(facilityId, "facilityId");
        if (date == null) throw new ValidationException("date is required");
        if (time == null) throw new ValidationException("time is required");
        if (durationMinutes <= 0) throw new ValidationException("durationMinutes must be > 0");
        requireNonBlank(appointmentType, "appointmentType");
        requireNonBlank(reasonForVisit, "reasonForVisit");

        if (patients.findById(patientId).isEmpty()) {
            throw new ValidationException("Unknown patient_id: " + patientId);
        }
        if (refData.clinicianById(clinicianId).isEmpty()) {
            throw new ValidationException("Unknown clinician_id: " + clinicianId);
        }
        if (refData.facilityById(facilityId).isEmpty()) {
            throw new ValidationException("Unknown facility_id: " + facilityId);
        }

        List<Appointment> all = new ArrayList<>(appointments.findAll());
        List<String> ids = all.stream().map(Appointment::appointmentId).toList();
        String newId = Ids.nextId("A", 3, ids);

        Appointment created = new Appointment(
                newId,
                patientId,
                clinicianId,
                facilityId,
                date,
                time,
                durationMinutes,
                appointmentType,
                "Scheduled",
                reasonForVisit,
                notes == null ? "" : notes,
                LocalDate.now(),
                LocalDate.now()
        );

        all.add(created);
        appointments.saveAll(all);
        return created;
    }

    public Appointment reschedule(
            String appointmentId,
            LocalDate newDate,
            LocalTime newTime,
            Integer newDurationMinutes,
            String newNotes
    ) throws IOException {
        requireNonBlank(appointmentId, "appointmentId");
        if (newDate == null) throw new ValidationException("newDate is required");
        if (newTime == null) throw new ValidationException("newTime is required");

        List<Appointment> all = new ArrayList<>(appointments.findAll());
        for (int i = 0; i < all.size(); i++) {
            Appointment a = all.get(i);
            if (!appointmentId.equals(a.appointmentId())) continue;

            String durationStr = newDurationMinutes == null ? null : Integer.toString(newDurationMinutes);
            int duration = newDurationMinutes == null ? a.durationMinutes() : Integer.parseInt(durationStr);
            if (duration <= 0) throw new ValidationException("durationMinutes must be > 0");

            Appointment updated = new Appointment(
                    a.appointmentId(),
                    a.patientId(),
                    a.clinicianId(),
                    a.facilityId(),
                    newDate,
                    newTime,
                    duration,
                    a.appointmentType(),
                    a.status(),
                    a.reasonForVisit(),
                    newNotes == null ? a.notes() : newNotes,
                    a.createdDate(),
                    LocalDate.now()
            );
            all.set(i, updated);
            appointments.saveAll(all);
            return updated;
        }
        throw new ValidationException("Unknown appointment_id: " + appointmentId);
    }

    public Appointment cancel(String appointmentId, String cancelNotes) throws IOException {
        requireNonBlank(appointmentId, "appointmentId");
        List<Appointment> all = new ArrayList<>(appointments.findAll());
        for (int i = 0; i < all.size(); i++) {
            Appointment a = all.get(i);
            if (!appointmentId.equals(a.appointmentId())) continue;

            String combinedNotes = a.notes();
            if (cancelNotes != null && !cancelNotes.isBlank()) {
                combinedNotes = (combinedNotes == null || combinedNotes.isBlank())
                        ? ("Cancelled: " + cancelNotes)
                        : (combinedNotes + " | Cancelled: " + cancelNotes);
            }

            Appointment updated = new Appointment(
                    a.appointmentId(),
                    a.patientId(),
                    a.clinicianId(),
                    a.facilityId(),
                    a.appointmentDate(),
                    a.appointmentTime(),
                    a.durationMinutes(),
                    a.appointmentType(),
                    "Cancelled",
                    a.reasonForVisit(),
                    combinedNotes,
                    a.createdDate(),
                    LocalDate.now()
            );
            all.set(i, updated);
            appointments.saveAll(all);
            return updated;
        }
        throw new ValidationException("Unknown appointment_id: " + appointmentId);
    }

    public Optional<Appointment> findById(String appointmentId) throws IOException {
        return appointments.findById(appointmentId);
    }

    private static void requireNonBlank(String s, String field) {
        if (s == null || s.trim().isEmpty()) throw new ValidationException(field + " is required");
    }
}

