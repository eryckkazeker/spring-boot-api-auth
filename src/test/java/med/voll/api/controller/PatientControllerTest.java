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
import med.voll.api.domain.patient.Patient;
import med.voll.api.domain.patient.PatientDetailsDto;
import med.voll.api.domain.patient.PatientUpdateDto;
import med.voll.api.domain.persistence.repository.PatientRepository;

class PatientControllerTest {

    private PatientRepository patientRepository = mock(PatientRepository.class);
    private PatientController patientController = new PatientController(patientRepository);

    @Test
    void should_ReturnCreatedStatus_WhenInsertPatient_WhenSuccess() {

        var newPatient = Mocks.createPatientInsertDto();

        var savedPatient = new Patient(1L, newPatient.name(), newPatient.email(), newPatient.telephone(), newPatient.ssn(), null, true);

        given(patientRepository.save(any())).willReturn(savedPatient);

        var response = patientController.insert(newPatient, UriComponentsBuilder.fromPath("localhost"));

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(new PatientDetailsDto(savedPatient), response.getBody());
        assertEquals(MessageFormat.format("localhost/patients/{0}", savedPatient.getId()), response.getHeaders().get("location").get(0));
    }

    @Test
    void should_ReturnPatientList_WhenListPatients_WhenSuccess() {
        
        var patientList = List.of(Mocks.createValidPatient(), Mocks.createValidPatient());
        Page<Patient> patientPage = new PageImpl<Patient>(patientList);
        given(patientRepository.findAllByActiveTrue(any())).willReturn(patientPage);

        var response = patientController.list(Pageable.ofSize(10));

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, response.getBody().toList().size());

    }

    @Test
    void should_ReturnPatientDetails_WhenGetDetails_WhenSuccess() {

        var validPatient = Mocks.createValidPatient();

        given(patientRepository.getReferenceById(validPatient.getId())).willReturn(validPatient);

        var response = patientController.getDetails(validPatient.getId());

        var responsePatient = new PatientDetailsDto(validPatient);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(responsePatient, response.getBody());

    }

    @Test
    void should_ThrowNotFoundException_WhenGetPatientDetails_WhenPatientNotFound() {

        given(patientRepository.getReferenceById(any())).willThrow(new EntityNotFoundException());

        
        Assertions.assertThrows(EntityNotFoundException.class, () -> {
            patientController.getDetails(1L);
        });

    }

    @Test
    void should_UpdatePatientData_WhenUpdate_WhenSuccess() {

        var validPatient = Mocks.createValidPatient();

        var updateDto = new PatientUpdateDto(validPatient.getId(), "Updated Patient Name", "1234123123", null);

        given(patientRepository.getReferenceById(validPatient.getId())).willReturn(validPatient);

        var response = patientController.update(updateDto);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(updateDto.name(), response.getBody().name());
        assertEquals(updateDto.telephone(), response.getBody().telephone());

    }

    @Test
    void should_ThrowNotFoundException_WhenUpdate_WhenPatientNotFound() {

        given(patientRepository.getReferenceById(any())).willThrow(new EntityNotFoundException());

        var updateDto = new PatientUpdateDto(1L, "Updated Patient Name", "1234123123", null);

        
        Assertions.assertThrows(EntityNotFoundException.class, () -> {
            patientController.update(updateDto);
        });

    }

    @Test
    void should_ReturnNoContent_WhenDeletePatient_WhenSuccess() {

        var validPatient = Mocks.createValidPatient();

        given(patientRepository.getReferenceById(validPatient.getId())).willReturn(validPatient);

        var response = patientController.delete(validPatient.getId());

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    void should_ThrowNotFoundException_WhenDeletePatient_WhenPatientNotFound() {

        given(patientRepository.getReferenceById(any())).willThrow(new EntityNotFoundException());

        
        Assertions.assertThrows(EntityNotFoundException.class, () -> {
            patientController.delete(1L);
        });

    }

}
