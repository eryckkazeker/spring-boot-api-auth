package med.voll.api.domain.appointment.rules;

import java.text.MessageFormat;
import java.time.Clock;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import med.voll.api.domain.appointment.Appointment;
import med.voll.api.domain.persistence.repository.AppointmentRepository;
import med.voll.api.domain.persistence.repository.MedicRepository;
import med.voll.api.domain.persistence.repository.PatientRepository;
import med.voll.api.infra.exception.BadRequestException;

@Component
public class AppointmentRuleHandlerImpl implements AppointmentRuleHandler {

    private final AppointmentRepository appointmentRepository;
    private final MedicRepository medicRepository;
    private final PatientRepository patientRepository;
    private final Clock clock;

    @Value("${appointment.workinghours.begin}")
    private int workingHoursBegin;

    @Value("${appointment.workinghours.end}")
    private int workingHoursEnd;

    public AppointmentRuleHandlerImpl(AppointmentRepository appointmentRepository, MedicRepository medicRepository,
            PatientRepository patientRepository, Clock clock) {
        this.appointmentRepository = appointmentRepository;
        this.medicRepository = medicRepository;
        this.patientRepository = patientRepository;
        this.clock = clock;
    }

    @Override
    public Appointment handleAppointmentRules(Appointment appointment) {

        if (appointment.getDate().getDayOfWeek().equals(DayOfWeek.SUNDAY)) {
            throw new BadRequestException("Cannot schedule appointment on sunday.");
        }
        
        var appointmentBegin = appointment.getDate().toLocalTime();

        if (appointmentBegin.isBefore(LocalTime.of(workingHoursBegin,0)) || appointmentBegin.isAfter(LocalTime.of(workingHoursEnd-1,0))) {
            throw new BadRequestException(MessageFormat.format("Invalid appointment hours. Valid hours are between {0}h and {1}h", workingHoursBegin, workingHoursEnd));
        }

        if (appointment.getDate().toLocalDate().isEqual(LocalDate.now(clock)) && LocalDateTime.now(clock).until(appointment.getDate(), ChronoUnit.MINUTES) < 30) {
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
