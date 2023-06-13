package med.voll.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import med.voll.api.domain.user.AuthenticationData;
import med.voll.api.domain.user.User;
import med.voll.api.infra.security.TokenData;
import med.voll.api.infra.security.TokenService;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private TokenService tokenService;

    @PostMapping
    public ResponseEntity<Object> authenticate(@RequestBody @Valid AuthenticationData authData) {
        var token = new UsernamePasswordAuthenticationToken(authData.login(), authData.password());
        var authentication = authenticationManager.authenticate(token);
        String jwt = tokenService.generateToken((User)authentication.getPrincipal());
        return ResponseEntity.ok(new TokenData(jwt));
    }
    
}
