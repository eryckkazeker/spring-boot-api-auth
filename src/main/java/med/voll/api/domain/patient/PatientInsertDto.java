package med.voll.api.domain.patient;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import med.voll.api.domain.address.AddressData;

public record PatientInsertDto(
        @NotBlank
        String name,
        @NotBlank
        @Email
        String email,

        @NotBlank
        String telephone,
        @NotBlank
        String ssn,

        @NotNull @Valid AddressData address) {
}
