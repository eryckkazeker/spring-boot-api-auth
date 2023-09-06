package med.voll.api.domain.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import med.voll.api.domain.medic.Medic;
import med.voll.api.domain.medic.MedicListData;
import med.voll.api.domain.medic.MedicUpdateDto;

public interface MedicService {

    Medic saveMedic(Medic medic);
    Page<MedicListData> listMedics(Pageable pageable);
    Medic getDetails(Long id);
    Medic update(MedicUpdateDto updateInfo);
    void delete(Long id);
}
