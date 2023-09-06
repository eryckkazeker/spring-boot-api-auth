package med.voll.api.domain.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import med.voll.api.Mocks;
import med.voll.api.domain.appointment.Appointment;
import med.voll.api.domain.appointment.rules.AppointmentRuleHandler;
import med.voll.api.domain.persistence.repository.AppointmentRepository;

class AppointmentServiceImplTest {

    private AppointmentRepository appointmentRepository = mock(AppointmentRepository.class);
    private AppointmentRuleHandler appointmentRuleHandler = mock(AppointmentRuleHandler.class);
    private EntityManager entityManager = mock(EntityManager.class);

    private final AppointmentServiceImpl appointmentService = new AppointmentServiceImpl(appointmentRepository,
                                                                appointmentRuleHandler,
                                                                entityManager);

    @Test
    void should_ReturnAppointmentList_WhenListAppointments_WhenSuccess() {
        
        var appointmentList = List.of(Mocks.createValidAppointment(), Mocks.createValidAppointment());
        Page<Appointment> appointmentPage = new PageImpl<Appointment>(appointmentList);
        given(appointmentRepository.findAll(any(Pageable.class))).willReturn(appointmentPage);

        var response = appointmentService.listAppointments(Pageable.ofSize(10));

        assertEquals(2, response.getContent().size());

    }
    
    @Test
    void should_ReturnAppointmentDetails_WhenGetDetails_WhenSuccess() {

        var validAppointment = Mocks.createValidAppointment();

        given(appointmentRepository.getReferenceById(validAppointment.getId())).willReturn(validAppointment);

        var response = appointmentService.getAppointmentDetails(validAppointment.getId());

        assertEquals(validAppointment, response);

    }

    @Test
    void should_ThrowNotFoundException_WhenGetAppointmentDetails_WhenAppointmentNotFound() {

        given(appointmentRepository.getReferenceById(any())).willThrow(new EntityNotFoundException());

        
        Assertions.assertThrows(EntityNotFoundException.class, () -> {
            appointmentService.getAppointmentDetails(1L);
        });

    }

    @Test
    void should_ReturnCreatedStatus_WhenInsertAppointment_WhenSuccess() {

        var newAppointment = Mocks.createValidAppointment();

        var savedAppointment = Mocks.createValidAppointment();

        given(appointmentRepository.saveAndFlush(any())).willReturn(savedAppointment);
        given(appointmentRuleHandler.handleAppointmentRules(any())).willReturn(savedAppointment);

        var response = appointmentService.saveAppointment(newAppointment);

        assertEquals(savedAppointment, response);
    }
}
