package med.voll.api.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

import java.text.MessageFormat;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.util.UriComponentsBuilder;

import jakarta.persistence.EntityNotFoundException;
import med.voll.api.Mocks;
import med.voll.api.domain.appointment.AppointmentDetailsDto;
import med.voll.api.domain.appointment.AppointmentListDto;
import med.voll.api.domain.service.AppointmentService;

class AppointmentControllerTest {

    private final AppointmentService appointmentService = mock(AppointmentService.class);
    private final AppointmentController appointmentController = new AppointmentController(appointmentService);

    @Test
    void should_ReturnAppointmentList_WhenListAppointments_WhenSuccess() {
        
        var appointmentList = List.of(Mocks.createAppointmentListDto(), Mocks.createAppointmentListDto());
        Page<AppointmentListDto> appointmentPage = new PageImpl<AppointmentListDto>(appointmentList);
        given(appointmentService.listAppointments(any(Pageable.class))).willReturn(appointmentPage);

        var response = appointmentController.list(Pageable.ofSize(10));

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, response.getBody().toList().size());

    }
    
        @Test
    void should_ReturnAppointmentDetails_WhenGetDetails_WhenSuccess() {

        var validAppointment = Mocks.createValidAppointment();

        given(appointmentService.getAppointmentDetails(validAppointment.getId())).willReturn(validAppointment);

        var response = appointmentController.getDetails(validAppointment.getId());

        var responseAppointment = new AppointmentDetailsDto(validAppointment);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(responseAppointment, response.getBody());

    }

    @Test
    void should_ThrowNotFoundException_WhenGetAppointmentDetails_WhenAppointmentNotFound() {

        given(appointmentService.getAppointmentDetails(any())).willThrow(new EntityNotFoundException());

        
        Assertions.assertThrows(EntityNotFoundException.class, () -> {
            appointmentController.getDetails(1L);
        });

    }

    @Test
    void should_ReturnCreatedStatus_WhenInsertAppointment_WhenSuccess() {

        var newAppointment = Mocks.createAppointmentInsertDto();

        var savedAppointment = Mocks.createValidAppointment();

        given(appointmentService.saveAppointment(any())).willReturn(savedAppointment);

        var response = appointmentController.insert(newAppointment, UriComponentsBuilder.fromPath("localhost"));

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(new AppointmentDetailsDto(savedAppointment), response.getBody());
        assertEquals(MessageFormat.format("localhost/appointments/{0}", savedAppointment.getId()), response.getHeaders().get("location").get(0));
    }

}
