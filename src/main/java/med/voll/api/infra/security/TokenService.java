package med.voll.api.infra.security;

import org.springframework.stereotype.Service;

import med.voll.api.domain.user.User;

@Service
public interface TokenService {

    String generateToken(User user);
    String getSubject(String tokenJwt);
    
}
