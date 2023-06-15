package med.voll.api.domain.user;

public record UserDetailsDto (String login, String profile){

    public UserDetailsDto(User createdUser) {
        this(createdUser.getLogin(), createdUser.getProfile().toString());
    }

}
