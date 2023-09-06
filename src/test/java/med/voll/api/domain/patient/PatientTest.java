package med.voll.api.domain.patient;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import org.junit.jupiter.api.Test;

import med.voll.api.Mocks;
import med.voll.api.domain.address.AddressData;

class PatientTest {

    @Test
    void should_UpdateInfo() {
        var oldAddress = Mocks.createValidAddress();
        var newAddress = new AddressData("New Street", null, null, null, null);
        var patient = new Patient(1L, "Old Name", "old_mail@mail.com","00000000", "000000", oldAddress, true);

        var updateDto = new PatientUpdateDto(1L, "new name", "11111111", newAddress);

        patient.updateInfo(updateDto);

        assertEquals(updateDto.name(), patient.getName());
        assertEquals(updateDto.telephone(), patient.getTelephone());
        assertEquals(newAddress.street(), patient.getAddress().getStreet());
        
    }

    @Test
    void should_UpdateInfo_WhenNullAddress() {
        var oldAddress = Mocks.createValidAddress();
        var newAddress = new AddressData("New Street", null, null, null, null);
        var patient = new Patient(1L, "Old Name", "old_mail@mail.com","00000000", "000000", oldAddress, true);

        var updateDto = new PatientUpdateDto(1L, "new name", "11111111", null);

        patient.updateInfo(updateDto);

        assertEquals(updateDto.name(), patient.getName());
        assertEquals(updateDto.telephone(), patient.getTelephone());
        assertNotEquals(newAddress.street(), patient.getAddress().getStreet());
        
    }

    @Test
    void should_UpdateInfo_WhenOnlyAddress() {
        var oldAddress = Mocks.createValidAddress();
        var newAddress = new AddressData("New Street", null, null, null, null);
        var patient = new Patient(1L, "Old Name", "old_mail@mail.com","00000000", "000000", oldAddress, true);

        var updateDto = new PatientUpdateDto(1L, null, null, newAddress);

        patient.updateInfo(updateDto);

        assertNotEquals(updateDto.name(), patient.getName());
        assertNotEquals(updateDto.telephone(), patient.getTelephone());
        assertEquals(newAddress.street(), patient.getAddress().getStreet());
        
    }

    @Test
    void should_NotUpdateId() {
        var patient = new Patient(1L, "Old Name", "old_mail@mail.com","00000000", "000000", null, true);

        var updateDto = new PatientUpdateDto(2L, null, null, null);

        patient.updateInfo(updateDto);

        assertNotEquals(updateDto.id(), patient.getId());
    }
}
