package med.voll.api.domain.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import med.voll.api.domain.medic.Medic;
import med.voll.api.domain.medic.MedicListData;
import med.voll.api.domain.medic.MedicUpdateDto;
import med.voll.api.domain.persistence.repository.MedicRepository;

@Service
public class MedicServiceImpl implements MedicService {

    private final MedicRepository repository;

    public MedicServiceImpl(MedicRepository repository) {
        this.repository = repository;
    }

    @Override
    public Medic saveMedic(Medic medic) {
        return repository.save(medic);
    }

    @Override
    public Page<MedicListData> listMedics(Pageable pageable) {
        return repository.findAllByActiveTrue(pageable).map(MedicListData::new);
    }

    @Override
    public Medic getDetails(Long id) {
        return repository.getReferenceById(id);
    }

    @Override
    public Medic update(MedicUpdateDto updateInfo) {
        var medic = repository.getReferenceById(updateInfo.id());
        medic.updateInfo(updateInfo);
        return medic;
    }

    @Override
    public void delete(Long id) {
        var medico = repository.getReferenceById(id);
        medico.delete();
    }
    
}
