package med.voll.api.controller;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;

import med.voll.api.Mocks;
import med.voll.api.domain.user.AuthenticationData;
import med.voll.api.domain.user.User;
import med.voll.api.infra.security.TokenService;

class AuthenticationControllerTest {

    private AuthenticationManager authenticationManager = mock(AuthenticationManager.class);
    private TokenService tokenService = mock(TokenService.class);

    private final AuthenticationController authenticationController = new AuthenticationController(authenticationManager, tokenService);

    @Test
    void should_ReturnToken_WhenSuccess() {

        var authenticationMock = mock(Authentication.class);

        given(authenticationManager.authenticate(any())).willReturn(authenticationMock);

        User validUser = Mocks.createValidUser();

        given(authenticationMock.getPrincipal()).willReturn(validUser);

        String token = "accessToken";

        given(tokenService.generateToken(validUser)).willReturn(token);

        var response = authenticationController.authenticate(new AuthenticationData("login", "password"));

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(token, response.getBody().token());


    }

}
