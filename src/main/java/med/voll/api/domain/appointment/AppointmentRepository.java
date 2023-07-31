package med.voll.api.domain.appointment;

import java.time.LocalDateTime;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    Appointment findAllByMedicId(Long id);

    Appointment findAllByPatientId(Long id);

    boolean existsByDateAndMedicId(LocalDateTime date, Long medicId);

    Page<Appointment> findAll(Pageable pagination);
    
}
