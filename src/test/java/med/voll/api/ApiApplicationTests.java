package med.voll.api;

import org.junit.ClassRule;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.junit.jupiter.Testcontainers;

@SpringBootTest
@ActiveProfiles("test")
@Testcontainers
class ApiApplicationTests {

    @ClassRule
    public static MySqlTestContainer container = MySqlTestContainer.getInstance();

	@Test
	void contextLoads() {
	}

}
