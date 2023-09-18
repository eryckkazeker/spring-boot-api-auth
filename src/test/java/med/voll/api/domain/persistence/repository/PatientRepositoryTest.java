package med.voll.api.domain.persistence.repository;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import org.junit.ClassRule;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.junit.jupiter.Testcontainers;

import jakarta.transaction.Transactional;
import med.voll.api.MySqlTestContainer;
import med.voll.api.domain.address.AddressData;
import med.voll.api.domain.patient.Patient;
import med.voll.api.domain.patient.PatientInsertDto;

@SpringBootTest
@Testcontainers
@ActiveProfiles("test")
class PatientRepositoryTest {

    @ClassRule
    public static MySqlTestContainer container = MySqlTestContainer.getInstance();

    @Autowired
    public PatientRepository patientRepository;

    @BeforeEach
    void setup() {
        patientRepository.save(new Patient(new PatientInsertDto("Patient 1", "p1@mail.com", "124124124", "12412412", new AddressData("st1", "134122", "City", "ST", "1"))));
        patientRepository.save(new Patient(new PatientInsertDto("Patient 2", "p2@mail.com", "124124124", "1451512", new AddressData("st1", "134122", "City", "ST", "1"))));
    }

    @AfterEach
    void clean() {
        patientRepository.deleteAll();
    }

    @Test
    void should_ReturnActivePatients() {
        var patientList = patientRepository.findAllByActiveTrue(Pageable.ofSize(10));

        assertEquals(2, patientList.getContent().size());
    }

    @Test
    @Transactional
    void should_NotReturnInactivePatientsWhenFindAll() {
        var patient1 = patientRepository.findAll().get(0);
        patient1.remove();

        var patientList = patientRepository.findAllByActiveTrue(Pageable.ofSize(10));

        assertEquals(1, patientList.getContent().size());
    }

    @Test
    @Transactional
    void should_ThrowExceptionWhenPatienInactive_WhenFindByIdAndActiveTrue() {
        
        var patient1 = patientRepository.findAll().get(0);

        patient1.remove();

        var patientExists = patientRepository.existsByIdAndActiveTrue(patient1.getId());

        assertFalse(patientExists);

    }

}
