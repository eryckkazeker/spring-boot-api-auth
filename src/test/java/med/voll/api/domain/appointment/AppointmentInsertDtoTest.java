package med.voll.api.domain.appointment;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

class AppointmentInsertDtoTest {

    @Test
    void test() throws JsonProcessingException {

        var dto = new AppointmentInsertDto(1L, 1L, LocalDateTime.now());

        var content = createAppointmentRequestBody(dto);

        System.out.println(content);

        

    }

    private String createAppointmentRequestBody(AppointmentInsertDto appointmentInsertDto) throws JsonProcessingException {

        return new ObjectMapper()
            .writer()
            .withDefaultPrettyPrinter()
            .writeValueAsString(appointmentInsertDto);

    }
}
