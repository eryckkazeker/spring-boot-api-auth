package med.voll.api.infra.security;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import com.auth0.jwt.JWT;

import med.voll.api.Mocks;
import med.voll.api.domain.user.User;

class TokenServiceImplTest {

    private final TokenServiceImpl tokenService = new TokenServiceImpl();

    private User user = Mocks.createValidUser();

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(tokenService, "secret", "secret");
    }

    @Test
    void should_GenerateToken() {

        var token = tokenService.generateToken(user);

        assertEquals(user.getLogin(), JWT.decode(token).getSubject());

    }

    @Test
    void should_ThrowException_WhenUserNull() {

        var nullProfileUser = new User(1L, "user", "kj12b4i1b34", null, true);
        assertThrows(RuntimeException.class, () -> {
            tokenService.generateToken(nullProfileUser);
        });

    }

    @Test
    void should_GetTokenSubject() {

        var token = tokenService.generateToken(user);

        var subject = tokenService.getSubject(token);

        assertEquals(user.getLogin(), subject);
    }


}
