package med.voll.api.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import jakarta.validation.Valid;
import med.voll.api.domain.persistence.repository.UserRepository;
import med.voll.api.domain.user.User;
import med.voll.api.domain.user.UserDetailsDto;
import med.voll.api.domain.user.UserInsertDto;
import med.voll.api.domain.user.UserListDto;

@RestController
@RequestMapping("/users")
@Secured("ROLE_ADMIN")
public class UserController {

    private final UserRepository userRepository;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @PostMapping
    @Transactional
    public ResponseEntity<UserDetailsDto> insertUser(@RequestBody @Valid UserInsertDto user, UriComponentsBuilder uriComponentsBuilder) {
        var createdUser = userRepository.save(new User(user));

        var uri = uriComponentsBuilder.path("/users/{id}").buildAndExpand(createdUser.getId()).toUri();

        return ResponseEntity.created(uri).body(new UserDetailsDto(createdUser));
    }

    @GetMapping
    public ResponseEntity<Page<UserListDto>> listUsers(@PageableDefault(size = 10, sort = {"login"}) Pageable pages) {

        var page = userRepository.findAllByActiveTrue(pages).map(UserListDto::new);
        return ResponseEntity.ok(page);

    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDetailsDto> getUserDetails(@PathVariable Long id) {

        var user = userRepository.getReferenceById(id);

        return ResponseEntity.ok(new UserDetailsDto(user));
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<Object> delete(@PathVariable Long id) {
        var user = userRepository.getReferenceById(id);
        user.delete();
        return ResponseEntity.noContent().build();
    }
    
}
