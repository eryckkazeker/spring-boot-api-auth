package med.voll.api.domain.user;

import jakarta.validation.constraints.NotBlank;

public record UserInsertDto (
    @NotBlank
    String login,
    @NotBlank
    String password,
    UserProfileEnum profile) {
}
