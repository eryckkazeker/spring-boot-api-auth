package med.voll.api.domain.medic;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import med.voll.api.domain.address.AddressData;

public record MedicInsertDto(
        @NotNull(message = "{name.null}")
        @NotEmpty(message = "{name.empty}")
        String name,
        @NotBlank
        @Email(message = "invalid e-mail")
        String email,

        @NotBlank
        String telephone,
        @NotBlank
        @Pattern(regexp = "\\d{4,6}")
        String register,
        @NotNull
        Field field,

        @NotNull @Valid AddressData address) {
}
