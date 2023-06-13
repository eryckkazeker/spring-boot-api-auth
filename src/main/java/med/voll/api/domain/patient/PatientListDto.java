package med.voll.api.domain.patient;

public record PatientListDto(Long id, String name, String email, String ssn) {

    public PatientListDto(Patient patient) {
        this(patient.getId(), patient.getName(), patient.getEmail(), patient.getSsn());
    }

}
