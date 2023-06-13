package med.voll.api.domain.medic;

import med.voll.api.domain.address.Address;

public record MedicDetailsDto(Long id, String name, String email, String register, Field field, Address address) {

    public MedicDetailsDto(Medic medic) {
        this(medic.getId(), medic.getName(), medic.getEmail(), medic.getRegister(), medic.getField(), medic.getAddress());
    }

}
