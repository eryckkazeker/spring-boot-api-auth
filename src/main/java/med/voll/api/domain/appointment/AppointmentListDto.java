package med.voll.api.domain.appointment;

import java.time.LocalDateTime;

import med.voll.api.domain.medic.Field;

public record AppointmentListDto(Long id, String patient, String medic, Field field, LocalDateTime date) {
    public AppointmentListDto(Appointment appointment) {
        this(appointment.getId(),
            appointment.getPatient().getName(),
            appointment.getMedic().getName(),
            appointment.getMedic().getField(),
            appointment.getDate());
    }
}
