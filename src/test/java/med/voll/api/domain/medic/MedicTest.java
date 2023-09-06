package med.voll.api.domain.medic;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import org.junit.jupiter.api.Test;

import med.voll.api.Mocks;
import med.voll.api.domain.address.AddressData;

class MedicTest {

    @Test
    void should_UpdateInfo() {
        var medic = Mocks.createValidMedic();

        var newAddress = new AddressData("new street", null, null, null, null);
        var updateInfo = new MedicUpdateDto(1L, "new medic", "11111111", newAddress);

        medic.updateInfo(updateInfo);

        assertEquals(updateInfo.name(), medic.getName());
        assertEquals(updateInfo.telephone(), medic.getTelephone());
        assertEquals(newAddress.street(), medic.getAddress().getStreet());
    }

    @Test
    void should_UpdateInfo_WhenAddressNull() {
        var medic = Mocks.createValidMedic();

        var newAddress = new AddressData("new street", null, null, null, null);
        var updateInfo = new MedicUpdateDto(1L, "new medic", "11111111", null);

        medic.updateInfo(updateInfo);

        assertEquals(updateInfo.name(), medic.getName());
        assertEquals(updateInfo.telephone(), medic.getTelephone());
        assertNotEquals(newAddress.street(), medic.getAddress().getStreet());
    }

    @Test
    void should_UpdateInfo_WhenOnlyAddress() {
        var medic = Mocks.createValidMedic();

        var newAddress = new AddressData("new street", null, null, null, null);
        var updateInfo = new MedicUpdateDto(1L, null, null, newAddress);

        medic.updateInfo(updateInfo);

        assertNotEquals(updateInfo.name(), medic.getName());
        assertNotEquals(updateInfo.telephone(), medic.getTelephone());
        assertEquals(newAddress.street(), medic.getAddress().getStreet());
    }

    @Test
    void should_NotUpdateId() {
        var medic = Mocks.createValidMedic();

        var updateDto = new MedicUpdateDto(2L, null, null, null);

        medic.updateInfo(updateDto);

        assertNotEquals(updateDto.id(), medic.getId());
    }

}
