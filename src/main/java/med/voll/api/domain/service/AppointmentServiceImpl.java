package med.voll.api.domain.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityManager;
import med.voll.api.domain.appointment.Appointment;
import med.voll.api.domain.appointment.AppointmentListDto;
import med.voll.api.domain.appointment.rules.AppointmentRuleHandler;
import med.voll.api.domain.persistence.repository.AppointmentRepository;

@Service
public class AppointmentServiceImpl implements AppointmentService {

    private final AppointmentRepository appointmentRepository;

    private final AppointmentRuleHandler appointmentRuleHandler;

    private final EntityManager entityManager;


    public AppointmentServiceImpl(AppointmentRepository appointmentRepository,
            AppointmentRuleHandler appointmentRuleHandler, EntityManager entityManager) {
        this.appointmentRepository = appointmentRepository;
        this.appointmentRuleHandler = appointmentRuleHandler;
        this.entityManager = entityManager;
    }

    @Override
    public Page<AppointmentListDto> listAppointments(Pageable pageable) {
        return appointmentRepository.findAll(pageable).map(AppointmentListDto::new);
    }

    @Override
    public Appointment getAppointmentDetails(Long id) {
        return appointmentRepository.getReferenceById(id);
    }

    @Override
    public Appointment saveAppointment(Appointment appointment) {

        var insertAppointment = appointmentRuleHandler.handleAppointmentRules(appointment);

        var savedAppointment = appointmentRepository.saveAndFlush(insertAppointment);

        entityManager.refresh(savedAppointment);

        return savedAppointment;
    }
    
}
