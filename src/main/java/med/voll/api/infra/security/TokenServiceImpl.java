package med.voll.api.infra.security;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;

import med.voll.api.domain.user.User;

@Service
public class TokenServiceImpl implements TokenService {

    @Value("${api.security.token.secret}")
    private String secret;

    @Override
    public String generateToken(User user) {
        var algorithm = Algorithm.HMAC256(secret);
        return JWT.create()
            .withIssuer("API Voll.med")
            .withSubject(user.getLogin())
            .withExpiresAt(Instant.now().plus(10, ChronoUnit.MINUTES))
            .withClaim("role", user.getProfile().toString())
            .sign(algorithm);
    }

    public String getSubject(String tokenJwt) {
        Algorithm algorithm = Algorithm.HMAC256(secret);
        JWTVerifier verifier = JWT.require(algorithm)
            .withIssuer("API Voll.med")
            .build();
            
        var decodedJWT = verifier.verify(tokenJwt);
        return decodedJWT.getSubject();
    }
}
