package med.voll.api.domain.patient;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import med.voll.api.domain.address.Address;

@Table(name = "patients")
@Entity(name = "Patient")
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Patient {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String email;

    private String telephone;

    private String ssn;

    @Embedded
    private Address address;

    private Boolean active;

    public Patient(PatientInsertDto data) {
        this.active = true;
        this.name = data.name();
        this.email = data.email();
        this.telephone = data.telephone();
        this.ssn = data.ssn();
        this.address = new Address(data.address());
    }

    public void updateInfo(PatientUpdateDto data) {
        if (data.name() != null) {
            this.name = data.name();
        }
        if (data.telephone() != null) {
            this.telephone = data.telephone();
        }
        if (data.address() != null) {
            this.address.updateInfo(data.address());
        }

    }

    public void remove() {
        this.active = false;
    }

    public Patient(Long patientId) {
        this.id = patientId;
    }
}
