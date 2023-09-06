package med.voll.api.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doThrow;
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
import med.voll.api.domain.medic.Field;
import med.voll.api.domain.medic.Medic;
import med.voll.api.domain.medic.MedicDetailsDto;
import med.voll.api.domain.medic.MedicListData;
import med.voll.api.domain.medic.MedicUpdateDto;
import med.voll.api.domain.service.MedicService;

class MedicControllerTest {

    private MedicService medicService = mock(MedicService.class);
    private MedicController medicController = new MedicController(medicService);

    @Test
    void should_ReturnCreatedStatus_WhenInsertMedicWhenSuccess() {

        var newMedic = Mocks.createMedicInsertDto();

        var savedMedic = new Medic(1L, "New Medic","new_medic@email.com","141235","124154",Field.CARDIOLOGY,Mocks.createValidAddress(),true);

        given(medicService.saveMedic(any())).willReturn(savedMedic);

        var response = medicController.insert(newMedic, UriComponentsBuilder.fromPath("localhost"));

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(new MedicDetailsDto(savedMedic), response.getBody());
        assertEquals(MessageFormat.format("localhost/medics/{0}", savedMedic.getId()), response.getHeaders().get("location").get(0));
    }

    @Test
    void should_ReturnMedicList_WhenListMedics_WhenSuccess() {
        
        var medicList = List.of(Mocks.createValidMedicListData(), Mocks.createValidMedicListData());
        Page<MedicListData> medicPage = new PageImpl<MedicListData>(medicList);
        given(medicService.listMedics(any())).willReturn(medicPage);

        var response = medicController.list(Pageable.ofSize(10));

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, response.getBody().toList().size());

    }

    @Test
    void should_ReturnMedicDetails_WhenGetDetails_WhenSuccess() {

        var validMedic = Mocks.createValidMedic();

        given(medicService.getDetails(validMedic.getId())).willReturn(validMedic);

        var response = medicController.getDetails(validMedic.getId());

        var responsePatient = new MedicDetailsDto(validMedic);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(responsePatient, response.getBody());

    }

    @Test
    void should_ThrowNotFoundException_WhenGetMedicDetails_WhenMedicNotFound() {

        given(medicService.getDetails(any())).willThrow(new EntityNotFoundException());

        
        Assertions.assertThrows(EntityNotFoundException.class, () -> {
            medicController.getDetails(1L);
        });

    }

    @Test
    void should_UpdateMedicData_WhenUpdate_WhenSuccess() {

        var updateDto = new MedicUpdateDto(1L, "Updated Medic","2653245", null);

        var updatedMedic = new Medic(updateDto.id(), updateDto.name(),"","","",Field.CARDIOLOGY,null,true);

        given(medicService.update(updateDto)).willReturn(updatedMedic);

        var response = medicController.update(updateDto);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(updateDto.name(), response.getBody().name());

    }

    @Test
    void should_ThrowNotFoundException_WhenUpdate_WhenMedicNotFound() {

        given(medicService.update(any())).willThrow(new EntityNotFoundException());

        var updateDto = new MedicUpdateDto(1L, "Updated Medic","2653245", null);
        
        Assertions.assertThrows(EntityNotFoundException.class, () -> {
            medicController.update(updateDto);
        });

    }

    @Test
    void should_ReturnNoContent_WhenDeleteMedic_WhenSuccess() {

        var validMedic = Mocks.createValidMedic();

        var response = medicController.delete(validMedic.getId());

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    void should_ThrowNotFoundException_WhenDeleteMedic_WhenMedicNotFound() {


        doThrow(EntityNotFoundException.class).when(medicService).delete(any());

        
        Assertions.assertThrows(EntityNotFoundException.class, () -> {
            medicController.delete(1L);
        });

    }

}
