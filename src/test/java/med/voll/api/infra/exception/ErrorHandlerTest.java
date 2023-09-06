package med.voll.api.infra.exception;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

import java.io.OutputStream;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;

import org.junit.ClassRule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.MediaType;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.testcontainers.junit.jupiter.Testcontainers;

import com.auth0.jwt.exceptions.TokenExpiredException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletResponse;
import med.voll.api.Mocks;
import med.voll.api.MySqlTestContainer;
import med.voll.api.domain.appointment.AppointmentInsertDto;
import med.voll.api.domain.persistence.repository.UserRepository;
import med.voll.api.domain.service.AppointmentService;
import med.voll.api.domain.service.MedicService;
import med.voll.api.infra.security.TokenService;

@ActiveProfiles("test")
@Testcontainers
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ErrorHandlerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MedicService medicService;

    @MockBean
    private AppointmentService appointmentService;

    @MockBean
    private TokenService tokenService;

    @MockBean
    private UserRepository userRepository;

    @ClassRule
    public static MySqlTestContainer container = MySqlTestContainer.getInstance();

    private HttpServletResponse servletResponse = mock(HttpServletResponse.class);

    @BeforeEach
    void setup() {
        given(tokenService.generateToken(any())).willReturn("token");
        given(userRepository.findByLogin(any())).willReturn(new User("user", "user", List.of(new SimpleGrantedAuthority("ROLE_USER"))));
    }

    @Test
    void should_ReturnNotFound_WhenEntityNotFoundException() throws Exception {
        given(medicService.getDetails(any())).willThrow(EntityNotFoundException.class);

        mockMvc
            .perform(MockMvcRequestBuilders
                .get("/patients/{id}", 1L)
                .header("Authorization", "Bearer token"))
            .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    void should_ReturnUnauthorized_WhenTokenExpired() throws Exception {
        given(tokenService.getSubject(any()))
            .willThrow(
                new TokenExpiredException("Expired Token",
                                            LocalDateTime.now().toInstant(ZoneOffset.UTC)));

        given(servletResponse.getWriter()).willReturn(new PrintWriter(OutputStream.nullOutputStream()));
         
        mockMvc
            .perform(MockMvcRequestBuilders
                .get("/patients")
                .header("Authorization", "Bearer token"))
            .andExpect(MockMvcResultMatchers.status().isUnauthorized());
    }

    @Test
    void should_ReturnBadRequest_WhenRequestBodyEmpty() throws Exception {

         mockMvc
            .perform(MockMvcRequestBuilders
                .post("/patients")
                .header("Authorization", "Bearer token"))
            .andExpect(MockMvcResultMatchers.status().isBadRequest());

    }

    @Test
    void should_ReturnBadRequest_WhenBadRequestExceptionThrown() throws Exception {
        given(appointmentService.saveAppointment(any()))
            .willThrow(new BadRequestException("Bad request"));

        AppointmentInsertDto createAppointmentInsertDto = Mocks.createAppointmentInsertDto();

        var requestBody = createRequestBody(createAppointmentInsertDto);

        mockMvc
            .perform(MockMvcRequestBuilders
                .post("/appointments")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody)
                .header("Authorization", "Bearer token"))
            .andExpect(MockMvcResultMatchers.status().isBadRequest());

    }

    @Test
    void should_ReturnBadRequest_WhenValidationError() throws Exception {
        
        AppointmentInsertDto createAppointmentInsertDto = Mocks.createAppointmentInsertDto();

        var requestBody = createRequestBody(createAppointmentInsertDto);

        mockMvc
            .perform(MockMvcRequestBuilders
                .post("/patients")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody)
                .header("Authorization", "Bearer token"))
            .andExpect(MockMvcResultMatchers.status().isBadRequest());

    }

    @Test
    void should_ReturnBadRequest_WhenDataIntegrityViolation() throws Exception {

        given(appointmentService.saveAppointment(any())).willThrow(new DataIntegrityViolationException("Error"));

        AppointmentInsertDto createAppointmentInsertDto = Mocks.createAppointmentInsertDto();

        var requestBody = createRequestBody(createAppointmentInsertDto);

        mockMvc
            .perform(MockMvcRequestBuilders
                .post("/appointments")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody)
                .header("Authorization", "Bearer token"))
            .andExpect(MockMvcResultMatchers.status().isBadRequest());

    }

    @Test
    void should_ReturnUnauthorized_WhenTokenNull() throws Exception {
        mockMvc
            .perform(MockMvcRequestBuilders
                .get("/patients/{id}", 1L))
            .andExpect(MockMvcResultMatchers.status().isUnauthorized());
    }

    private String createRequestBody(Object requestBodyObject) throws JsonProcessingException {
        
        return new ObjectMapper()
            .writer()
            .withDefaultPrettyPrinter()
            .writeValueAsString(requestBodyObject);

    }
}
