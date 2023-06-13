package med.voll.api.domain.address;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record AddressData(
        @NotBlank
        String street,
        @NotBlank
        @Pattern(regexp = "\\d{8}")
        String zip,
        @NotBlank
        String city,
        @NotBlank
        String state,
        @NotBlank
        String number) {
}
