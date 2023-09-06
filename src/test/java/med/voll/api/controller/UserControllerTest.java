package med.voll.api.controller;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

import java.text.MessageFormat;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.util.UriComponentsBuilder;

import jakarta.persistence.EntityNotFoundException;
import med.voll.api.Mocks;
import med.voll.api.domain.persistence.repository.UserRepository;
import med.voll.api.domain.user.User;
import med.voll.api.domain.user.UserDetailsDto;
import med.voll.api.domain.user.UserInsertDto;
import med.voll.api.domain.user.UserProfileEnum;

class UserControllerTest {

    private UserRepository userRepository = mock(UserRepository.class);
    private UserController userController = new UserController(userRepository);

    @Test
    void should_ReturnCreatedStatus_WhenInsertUser_WhenSuccess() {
        var newUser = new UserInsertDto("newUser", "2i1gi12gv4", UserProfileEnum.ROLE_USER);

        var savedUser = new User(1L, newUser.login(), newUser.password(), newUser.profile(), true);

        given(userRepository.save(any())).willReturn(savedUser);

        var response = userController.insertUser(newUser, UriComponentsBuilder.fromPath("localhost"));

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(new UserDetailsDto(savedUser), response.getBody());
        assertEquals(MessageFormat.format("localhost/users/{0}", savedUser.getId()), response.getHeaders().get("location").get(0));
    }

    @Test
    void should_ReturnUserList_WhenListUsers_WhenSuccess() {
        
        var userList = List.of(Mocks.createValidUser(), Mocks.createValidUser());
        Page<User> userPage = new PageImpl<User>(userList);
        given(userRepository.findAllByActiveTrue(any())).willReturn(userPage);

        var response = userController.listUsers(Pageable.ofSize(10));

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, response.getBody().toList().size());

    }

    @Test
    void should_ReturnUserDetails_WhenGetUserDetails_WhenSuccess() {

        var validUser = Mocks.createValidUser();

        given(userRepository.getReferenceById(validUser.getId())).willReturn(validUser);

        var response = userController.getUserDetails(validUser.getId());

        var responseUser = new UserDetailsDto(validUser);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(responseUser, response.getBody());

    }

    @Test
    void should_ThrowNotFoundException_WhenGetUserDetails_WhenUserNotFound() {

        given(userRepository.getReferenceById(any())).willThrow(new EntityNotFoundException());

        
        Assertions.assertThrows(EntityNotFoundException.class, () -> {
            userController.getUserDetails(1L);
        });

    }

    @Test
    void should_ReturnNoContent_WhenDeleteUser_WhenSuccess() {

        var validUser = Mocks.createValidUser();

        given(userRepository.getReferenceById(validUser.getId())).willReturn(validUser);

        var response = userController.delete(validUser.getId());

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    void should_ThrowNotFoundException_WhenDeleteUser_WhenUserNotFound() {

        given(userRepository.getReferenceById(any())).willThrow(new EntityNotFoundException());

        
        Assertions.assertThrows(EntityNotFoundException.class, () -> {
            userController.delete(1L);
        });

    }

}
