package med.voll.api;

import java.time.Clock;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;

import lombok.Generated;

@SpringBootApplication
@EnableMethodSecurity(securedEnabled = true)
public class ApiApplication {

    @Generated
    public static void main(String[] args) {
		SpringApplication.run(ApiApplication.class, args);
	}

    @Bean
    public Clock clock() {
        return Clock.systemDefaultZone();
    }

}
