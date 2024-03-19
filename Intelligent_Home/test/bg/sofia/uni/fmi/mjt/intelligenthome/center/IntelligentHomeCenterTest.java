package bg.sofia.uni.fmi.mjt.intelligenthome.center;

import bg.sofia.uni.fmi.mjt.intelligenthome.center.exceptions.DeviceAlreadyRegisteredException;
import bg.sofia.uni.fmi.mjt.intelligenthome.center.exceptions.DeviceNotFoundException;
import bg.sofia.uni.fmi.mjt.intelligenthome.device.AmazonAlexa;
import bg.sofia.uni.fmi.mjt.intelligenthome.device.DeviceType;
import bg.sofia.uni.fmi.mjt.intelligenthome.device.IoTDevice;
import bg.sofia.uni.fmi.mjt.intelligenthome.device.RgbBulb;
import bg.sofia.uni.fmi.mjt.intelligenthome.device.WiFiThermostat;
import bg.sofia.uni.fmi.mjt.intelligenthome.storage.DeviceStorage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class IntelligentHomeCenterTest {
    private final String name = "TestName";
    private final double powerConsumption = 5;
    private final LocalDateTime installationDateTime = LocalDateTime.of(2023, 11, 10, 12, 5);
    private final IoTDevice speaker = new AmazonAlexa(name, powerConsumption, installationDateTime);
    private IntelligentHomeCenter intelligentHomeCenter;

    @Mock
    private final DeviceStorage storageMock = mock(DeviceStorage.class);

    @BeforeEach
    void setUp() {
        intelligentHomeCenter = new IntelligentHomeCenter(storageMock);
    }

    @Test
    void testRegisterAddsADeviceCorrectly() {
        when(storageMock.exists(speaker.getId())).thenReturn(false);

        assertDoesNotThrow(() -> intelligentHomeCenter.register(speaker));
    }

    @Test
    void testRegisterThrowsWhenPassedNull() {
        assertThrows(IllegalArgumentException.class, () -> intelligentHomeCenter.register(null));
    }

    @Test
    void testRegisterThrowsWhenDeviceAlreadyRegistered() {
        when(storageMock.exists(speaker.getId())).thenReturn(true);

        assertThrows(DeviceAlreadyRegisteredException.class, () -> intelligentHomeCenter.register(speaker));
    }

    @Test
    void testUnregisterRemovesADeviceCorrectly() {
        when(storageMock.exists(speaker.getId())).thenReturn(true);

        assertDoesNotThrow(() -> intelligentHomeCenter.unregister(speaker));
    }

    @Test
    void testUnregisterThrowsWhenPassedNull() {
        assertThrows(IllegalArgumentException.class, () -> intelligentHomeCenter.unregister(null));
    }

    @Test
    void testUnregisterThrowsWhenDeviceNotFound() {
        when(storageMock.exists(speaker.getId())).thenReturn(false);

        assertThrows(DeviceNotFoundException.class, () -> intelligentHomeCenter.unregister(speaker));
    }

    @Test
    void testGetDeviceByIdThrowsBehavesCorrectly() {
        when(storageMock.exists(speaker.getId())).thenReturn(true);
        when(storageMock.get(speaker.getId())).thenReturn(speaker);

        assertDoesNotThrow(() -> intelligentHomeCenter.getDeviceById(speaker.getId()));
    }

    @Test
    void testGetDeviceByIdThrowsWhenPassedNull() {
        assertThrows(IllegalArgumentException.class, () -> intelligentHomeCenter.getDeviceById(null));
    }

    @Test
    void testGetDeviceByIdThrowsWhenDeviceNotFound() {
        assertThrows(DeviceNotFoundException.class, () -> intelligentHomeCenter.getDeviceById(speaker.getId()));
    }

    @Test
    void testGetDeviceQuantityPerTypeCalculatesCorrectly() {
        IoTDevice rgbBulb1 = new RgbBulb(name, powerConsumption, installationDateTime);
        IoTDevice rgbBulb2 = new RgbBulb(name, powerConsumption, installationDateTime);

        when(storageMock.listAll()).thenReturn(Set.of(speaker, rgbBulb1, rgbBulb2));

        assertEquals(1, intelligentHomeCenter.getDeviceQuantityPerType(DeviceType.SMART_SPEAKER));
        assertEquals(2, intelligentHomeCenter.getDeviceQuantityPerType(DeviceType.BULB));
        assertEquals(0, intelligentHomeCenter.getDeviceQuantityPerType(DeviceType.THERMOSTAT));
    }

    @Test
    void testGetTopNDevicesByPowerConsumptionReturnsCorrectCountElements() {
        IoTDevice rgbBulb1 = new RgbBulb(name, powerConsumption, installationDateTime);
        IoTDevice wiFiThermostat = new WiFiThermostat(name, powerConsumption, installationDateTime);

        when(storageMock.listAll()).thenReturn(Set.of(speaker, rgbBulb1, wiFiThermostat));

        assertEquals(3, intelligentHomeCenter.getTopNDevicesByPowerConsumption(3).size());
        assertEquals(3, intelligentHomeCenter.getTopNDevicesByPowerConsumption(6).size());
        assertEquals(1, intelligentHomeCenter.getTopNDevicesByPowerConsumption(1).size());
    }

    @Test
    void testGetTopNDevicesByPowerConsumptionSortsElementsCorrectly() {
        IoTDevice rgbBulb1 = new RgbBulb(name, powerConsumption - 2, installationDateTime);
        IoTDevice rgbBulb2 = new RgbBulb(name, powerConsumption - 4, installationDateTime);

        when(storageMock.listAll()).thenReturn(Set.of(rgbBulb2, speaker, rgbBulb1));

        ArrayList<String> res = new ArrayList<>(intelligentHomeCenter.getTopNDevicesByPowerConsumption(3));

        assertEquals(speaker.getId(), res.get(0));
        assertEquals(rgbBulb1.getId(), res.get(1));
        assertEquals(rgbBulb2.getId(), res.get(2));
    }

    @Test
    void testGetTopNDevicesByPowerConsumptionThrowsWhenPassedNegativeNumber() {
        assertThrows(IllegalArgumentException.class, () -> intelligentHomeCenter.getTopNDevicesByPowerConsumption(-1));
    }

    @Test
    void testGetFirstNDevicesByRegistrationReturnsCorrectCountElements() {
        IoTDevice rgbBulb1 = new RgbBulb(name, powerConsumption, installationDateTime);
        IoTDevice wiFiThermostat = new WiFiThermostat(name, powerConsumption, installationDateTime);

        LocalDateTime start = LocalDateTime.of(2023, 11, 15, 12, 5);
        speaker.setRegistration(start);
        rgbBulb1.setRegistration(start.plusHours(1));
        wiFiThermostat.setRegistration(start.plusHours(3));

        when(storageMock.listAll()).thenReturn(Set.of(speaker, rgbBulb1, wiFiThermostat));

        assertEquals(3, intelligentHomeCenter.getFirstNDevicesByRegistration(3).size());
        assertEquals(3, intelligentHomeCenter.getFirstNDevicesByRegistration(6).size());
        assertEquals(1, intelligentHomeCenter.getFirstNDevicesByRegistration(1).size());
    }

    @Test
    void testGetFirstNDevicesByRegistrationSortsElementsCorrectly() {
        IoTDevice rgbBulb1 = new RgbBulb(name, powerConsumption, installationDateTime);
        IoTDevice rgbBulb2 = new RgbBulb(name, powerConsumption, installationDateTime);

        LocalDateTime start = LocalDateTime.of(2023, 11, 15, 12, 5);
        speaker.setRegistration(start);
        rgbBulb1.setRegistration(start.plusHours(1));
        rgbBulb2.setRegistration(start.plusHours(3));

        when(storageMock.listAll()).thenReturn(Set.of(rgbBulb2, speaker, rgbBulb1));

        ArrayList<IoTDevice> res = new ArrayList<>(intelligentHomeCenter.getFirstNDevicesByRegistration(3));

        assertEquals(speaker.getId(), res.get(0).getId());
        assertEquals(rgbBulb1.getId(), res.get(1).getId());
        assertEquals(rgbBulb2.getId(), res.get(2).getId());
    }

    @Test
    void testGetFirstNDevicesByRegistrationThrowsWhenPassedNegativeNumber() {
        assertThrows(IllegalArgumentException.class, () -> intelligentHomeCenter.getFirstNDevicesByRegistration(-1));
    }
}
