package med.voll.api.domain.medic;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MedicRepository extends JpaRepository<Medic, Long> {
    Page<Medic> findAllByActiveTrue(Pageable pages);

    boolean existsByIdAndActiveTrue(Long id);
}
