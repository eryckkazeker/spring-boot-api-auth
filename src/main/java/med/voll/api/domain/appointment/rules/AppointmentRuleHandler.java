package med.voll.api.domain.appointment.rules;

import java.text.MessageFormat;
import java.time.DayOfWeek;
import java.time.LocalTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import med.voll.api.domain.appointment.Appointment;
import med.voll.api.domain.appointment.AppointmentRepository;
import med.voll.api.domain.medic.MedicRepository;
import med.voll.api.domain.patient.PatientRepository;
import med.voll.api.infra.exception.BadRequestException;

@Component
public class AppointmentRuleHandler {

    @Autowired
    AppointmentRepository appointmentRepository;

    @Autowired
    MedicRepository medicRepository;

    @Autowired
    PatientRepository patientRepository;

    @Value("${appointment.workinghours.begin}")
    private int workingHoursBegin;

    @Value("${appointment.workinghours.end}")
    private int workingHoursEnd;

    public Appointment handleAppointmentRules(Appointment appointment) {

        if (appointment.getDate().getDayOfWeek().equals(DayOfWeek.SUNDAY)) {
            throw new BadRequestException("Cannot schedule appointment on sunday.");
        }
        
        var appointmentBegin = appointment.getDate().toLocalTime();

        if (appointmentBegin.isBefore(LocalTime.of(workingHoursBegin,0)) || appointmentBegin.isAfter(LocalTime.of(workingHoursEnd-1,0))) {
            throw new BadRequestException(MessageFormat.format("Invalid appointment hours. Valid hours are between {0}h and {1}h", workingHoursBegin, workingHoursEnd));
        }

        if (appointmentBegin.plusMinutes(30).isAfter(LocalTime.now())) {
            throw new BadRequestException("Cannot schedule appointment with less than 30 ahead.");
        }

        if (!medicRepository.existsByIdAndActiveTrue(appointment.getMedic().getId())) {
            throw new BadRequestException("The referred medic is not active.");
        }

        if (!patientRepository.existsByIdAndActiveTrue(appointment.getPatient().getId())) {
            throw new BadRequestException("The referred patient is not active.");
        }

        if (appointmentRepository.existsByDateAndMedicId(appointment.getDate(), appointment.getMedic().getId())) {
            throw new BadRequestException(MessageFormat.format("The medic is already booked for {0}", appointment.getDate()));
        }

        return appointment;

    }

}
