package med.voll.api.domain.appointment;

import java.time.LocalDateTime;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;

public record AppointmentInsertDto(
    @NotNull    
    Long medicId, 
    @NotNull
    Long patientId,
    @NotNull
    @Future
    LocalDateTime  date) {

    public LocalDateTime date() {
        return date.withSecond(0).withMinute(0);
    }
    
}
