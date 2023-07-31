package med.voll.api.domain.medic;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import med.voll.api.domain.address.Address;

@Table(name = "medics")
@Entity(name = "Medic")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Medic {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String email;

    private String telephone;

    private String register;

    @Enumerated(EnumType.STRING)
    private Field field;

    @Embedded
    private Address address;

    private Boolean active;

    public Medic(MedicInsertDto data) {
        this.active = true;
        this.name = data.name();
        this.email = data.email();
        this.telephone = data.telephone();
        this.register = data.register();
        this.field = data.field();
        this.address = new Address(data.address());
    }

    public void updateInfo(MedicUpdateDto data) {
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

    public void delete() {
        this.active = false;
    }

    public Medic(Long medicId) {
        this.id = medicId;
    }
}
