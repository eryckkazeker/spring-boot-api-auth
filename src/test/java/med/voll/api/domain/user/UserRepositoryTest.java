package med.voll.api.domain.user;

import static org.junit.Assert.assertEquals;

import org.junit.ClassRule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.junit.jupiter.Testcontainers;

import med.voll.api.MySqlTestContainer;
import med.voll.api.domain.persistence.repository.UserRepository;

@SpringBootTest
@Testcontainers
@ActiveProfiles("test")
class UserRepositoryTest {

    @ClassRule
    public static MySqlTestContainer container = MySqlTestContainer.getInstance();

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setup() {
        userRepository.save(new User(1L,"active_user","act_passw0rd", UserProfileEnum.ROLE_USER, true));
        userRepository.save(new User(2L,"inactive_user","in_pass", UserProfileEnum.ROLE_USER, false));
    }

    @Test
    void should_ReturnUser_WhenFindByLogin_WhenSuccess() {
        var activeUser = userRepository.findByLogin("active_user");
        var inactiveUser = userRepository.findByLogin("inactive_user");

        assertEquals("act_passw0rd", activeUser.getPassword());
        assertEquals("in_pass", inactiveUser.getPassword());
    }
}
