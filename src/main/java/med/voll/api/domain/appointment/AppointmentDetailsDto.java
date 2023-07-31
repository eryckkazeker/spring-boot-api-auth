package med.voll.api.domain.appointment;

public record AppointmentDetailsDto(
    Long id,
    String medicName,
    String medicField,
    String medicRegister,
    String patientName,
    String date
) {

    public AppointmentDetailsDto(Appointment appointment) {
        this(appointment.getId(),
            appointment.getMedic().getName(),
            appointment.getMedic().getField().toString(),
            appointment.getMedic().getRegister(),
            appointment.getPatient().getName(),
            appointment.getDate().toString());
    }
    
}
