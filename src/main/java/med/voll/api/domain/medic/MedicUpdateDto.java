package med.voll.api.domain.medic;

import jakarta.validation.constraints.NotNull;
import med.voll.api.domain.address.AddressData;

public record MedicUpdateDto(
        @NotNull
        Long id,
        String name,
        String telephone,
        AddressData address) {
}
