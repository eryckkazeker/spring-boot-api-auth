package med.voll.api.domain.address;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Address {

    private String street;
    private String zip;
    private String number;
    private String city;
    private String state;

    public Address(AddressData data) {
        this.street = data.street();
        this.zip = data.zip();
        this.state = data.state();
        this.city = data.city();
        this.number = data.number();
    }

    public void updateInfo(AddressData data) {
        if (data.street() != null) {
            this.street = data.street();
        }
        if (data.zip() != null) {
            this.zip = data.zip();
        }
        if (data.state() != null) {
            this.state = data.state();
        }
        if (data.city() != null) {
            this.city = data.city();
        }
        if (data.number() != null) {
            this.number = data.number();
        }
    }
}
