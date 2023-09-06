package med.voll.api.domain.persistence.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;

import med.voll.api.domain.user.User;

public interface UserRepository extends JpaRepository<User, Long> {

    UserDetails findByLogin(String login);

    Page<User> findAllByActiveTrue(Pageable pages);
}
