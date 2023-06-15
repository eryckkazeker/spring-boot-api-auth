package med.voll.api.domain.user;

public record UserListDto(Long id, String login) {
    public UserListDto(User u) {
        this(u.getId(), u.getLogin());
    }
}
