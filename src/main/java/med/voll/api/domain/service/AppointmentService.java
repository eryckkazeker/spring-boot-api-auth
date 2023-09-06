package med.voll.api.domain.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import med.voll.api.domain.appointment.Appointment;
import med.voll.api.domain.appointment.AppointmentListDto;

public interface AppointmentService {
    
    Page<AppointmentListDto> listAppointments(Pageable pageable);
    Appointment getAppointmentDetails(Long id);
    Appointment saveAppointment(Appointment appointment);

}
