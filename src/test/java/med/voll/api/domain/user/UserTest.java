package med.voll.api.domain.user;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.jupiter.api.Test;

class UserTest {

    @Test
    void testOverridenMethods() {
        var user = new User(1L, "username","124gi1g4", UserProfileEnum.ROLE_ADMIN, true);

        assertTrue(user.isAccountNonExpired());
        assertTrue(user.isAccountNonLocked());
        assertTrue(user.isActive());
        assertTrue(user.isCredentialsNonExpired());
        assertTrue(user.isEnabled());

        assertEquals(UserProfileEnum.ROLE_ADMIN.toString(), user.getAuthorities().iterator().next().toString());

        assertEquals("username", user.getLogin());
    }

}
