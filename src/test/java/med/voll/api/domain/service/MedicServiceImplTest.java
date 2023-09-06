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

import jakarta.persistence.EntityNotFoundException;
import med.voll.api.Mocks;
import med.voll.api.domain.medic.Medic;
import med.voll.api.domain.medic.MedicUpdateDto;
import med.voll.api.domain.persistence.repository.MedicRepository;

class MedicServiceImplTest {

    MedicRepository medicRepository = mock(MedicRepository.class);

    private final MedicServiceImpl medicService = new MedicServiceImpl(medicRepository);

    @Test
    void should_ReturnMedicList_WhenListMedics_WhenSuccess() {
        
        var medicList = List.of(Mocks.createValidMedic(), Mocks.createValidMedic());
        Page<Medic> medicPage = new PageImpl<Medic>(medicList);
        given(medicRepository.findAllByActiveTrue(any(Pageable.class))).willReturn(medicPage);

        var response = medicService.listMedics(Pageable.ofSize(10));

        assertEquals(2, response.getContent().size());

    }

    @Test
    void should_ReturnMedicDetails_WhenGetDetails_WhenSuccess() {

        var validMedic = Mocks.createValidMedic();

        given(medicRepository.getReferenceById(validMedic.getId())).willReturn(validMedic);

        var response = medicService.getDetails(validMedic.getId());

        assertEquals(validMedic, response);

    }

    @Test
    void should_ThrowNotFoundException_WhenGetMedicDetails_WhenMedicNotFound() {

        given(medicRepository.getReferenceById(any())).willThrow(new EntityNotFoundException());

        
        Assertions.assertThrows(EntityNotFoundException.class, () -> {
            medicService.getDetails(1L);
        });

    }

    @Test
    void should_ThrowNotFoundException_WhenDeleteMedic_WhenMedicNotFound() {

        given(medicRepository.getReferenceById(any())).willThrow(new EntityNotFoundException());

        
        Assertions.assertThrows(EntityNotFoundException.class, () -> {
            medicService.delete(1L);
        });

    }

    @Test
    void should_ThrowNotFoundException_WhenUpdateMedic_WhenMedicNotFound() {

        given(medicRepository.getReferenceById(1L)).willThrow(new EntityNotFoundException());

        
        Assertions.assertThrows(EntityNotFoundException.class, () -> {
            medicService.update(new MedicUpdateDto(1L, null, null, null));
        });

    }

    @Test
    void should_UpdateMedicData_WhenUpdate_WhenSuccess() {

        var validMedic = Mocks.createValidMedic();

        var updateDto = new MedicUpdateDto(validMedic.getId(), "Updated Medic Name", "1234123123", null);

        given(medicRepository.getReferenceById(validMedic.getId())).willReturn(validMedic);

        var response = medicService.update(updateDto);

        assertEquals(updateDto.name(), response.getName());
        assertEquals(updateDto.telephone(), response.getTelephone());

    }


    
    
}
