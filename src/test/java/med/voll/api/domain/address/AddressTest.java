package med.voll.api.domain.address;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import org.junit.jupiter.api.Test;

class AddressTest {

    @Test
    void should_UpdateInfos() {
        var oldAddress = new Address("old street", "000000", "00", "old city", "OS");

        AddressData newData = new AddressData("new street", "111111", "new city", "NS", "11");

        oldAddress.updateInfo(newData);

        assertEquals(newData.street(), oldAddress.getStreet());
        assertEquals(newData.zip(), oldAddress.getZip());
        assertEquals(newData.number(), oldAddress.getNumber());
        assertEquals(newData.city(), oldAddress.getCity());
        assertEquals(newData.state(), oldAddress.getState());
    }

    @Test
    void should_NotUpdateNullInfo_WhenSomeNull() {
        var oldAddress = new Address("old street", "000000", "00", "old city", "OS");

        AddressData newData = new AddressData("new street", "111111", null, null, "11");

        oldAddress.updateInfo(newData);

        assertEquals(newData.street(), oldAddress.getStreet());
        assertEquals(newData.zip(), oldAddress.getZip());
        assertEquals(newData.number(), oldAddress.getNumber());
        assertNotEquals(newData.city(), oldAddress.getCity());
        assertNotEquals(newData.state(), oldAddress.getState());
    }

    @Test
    void should_NotUpdateNullInfo_WhenAllNull() {
        var oldAddress = new Address("old street", "000000", "00", "old city", "OS");

        AddressData newData = new AddressData(null, null, null, null, null);

        oldAddress.updateInfo(newData);

        assertNotEquals(newData.street(), oldAddress.getStreet());
        assertNotEquals(newData.zip(), oldAddress.getZip());
        assertNotEquals(newData.number(), oldAddress.getNumber());
        assertNotEquals(newData.city(), oldAddress.getCity());
        assertNotEquals(newData.state(), oldAddress.getState());
    }
}
