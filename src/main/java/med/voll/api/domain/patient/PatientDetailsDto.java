package med.voll.api.domain.patient;

import med.voll.api.domain.address.Address;

public record PatientDetailsDto (String name, String email, String telephone, String ssn, Address address){

    public PatientDetailsDto(Patient patient) {
        this(patient.getName(), patient.getEmail(), patient.getTelephone(), patient.getSsn(), patient.getAddress());
    }

}
