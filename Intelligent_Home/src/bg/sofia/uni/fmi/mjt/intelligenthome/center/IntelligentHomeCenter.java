package bg.sofia.uni.fmi.mjt.intelligenthome.center;

import bg.sofia.uni.fmi.mjt.intelligenthome.device.DeviceType;
import bg.sofia.uni.fmi.mjt.intelligenthome.device.IoTDevice;
import bg.sofia.uni.fmi.mjt.intelligenthome.center.comparator.KWhComparator;
import bg.sofia.uni.fmi.mjt.intelligenthome.center.comparator.RegistrationComparator;
import bg.sofia.uni.fmi.mjt.intelligenthome.center.exceptions.DeviceAlreadyRegisteredException;
import bg.sofia.uni.fmi.mjt.intelligenthome.center.exceptions.DeviceNotFoundException;
import bg.sofia.uni.fmi.mjt.intelligenthome.storage.DeviceStorage;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class IntelligentHomeCenter {
    DeviceStorage storage;

    public IntelligentHomeCenter(DeviceStorage storage) {
        this.storage = storage;
    }

    /**
     * Adds a @device to the IntelligentHomeCenter.
     *
     * @throws IllegalArgumentException         in case @device is null.
     * @throws DeviceAlreadyRegisteredException in case the @device is already
     *                                          registered.
     */
    public void register(IoTDevice device) throws DeviceAlreadyRegisteredException {
        if (device == null) {
            throw new IllegalArgumentException("device cannot be null");
        }

        if (storage.exists(device.getId())) {
            throw new DeviceAlreadyRegisteredException("device already exists");
        }

        storage.store(device.getId(), device);
        device.setRegistration(LocalDateTime.now());
    }

    /**
     * Removes the @device from the IntelligentHomeCenter.
     *
     * @throws IllegalArgumentException in case null is passed.
     * @throws DeviceNotFoundException  in case the @device is not found.
     */
    public void unregister(IoTDevice device) throws DeviceNotFoundException {
        if (device == null) {
            throw new IllegalArgumentException("device cannot be null");
        }

        if (!storage.exists(device.getId())) {
            throw new DeviceNotFoundException("device does not exist in storage");
        }

        storage.delete(device.getId());
    }

    /**
     * Returns a IoTDevice with an ID @id if found.
     *
     * @throws IllegalArgumentException in case @id is null or empty.
     * @throws DeviceNotFoundException  in case device with ID @id is not found.
     */
    public IoTDevice getDeviceById(String id) throws DeviceNotFoundException {
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("device cannot be null");
        }

        if (!storage.exists(id)) {
            throw new DeviceNotFoundException("device not found");
        }

        return storage.get(id);
    }

    /**
     * Returns the total number of devices with type @type registered in
     * SmartCityHub.
     *
     * @throws IllegalArgumentException in case @type is null.
     */
    public int getDeviceQuantityPerType(DeviceType type) {
        int quantity = 0;

        for (IoTDevice value:storage.listAll()) {
            if (value.getType().getShortName().equals(type.getShortName())) {
                quantity++;
            }
        }

        return quantity;
    }

    public Collection<String> getTopNDevicesByPowerConsumption(int n) {
        if (n < 0) {
            throw new IllegalArgumentException();
        }

        List<IoTDevice> list = new LinkedList<>();
        for (IoTDevice value : storage.listAll()) {
            list.add(value);
        }

        KWhComparator compareKWh = new KWhComparator();
        Collections.sort(list, compareKWh);

        if (n >= list.size()) {
            n = list.size();
        }

        List<String> arrList = new ArrayList<>(n);
        for (int i = 0; i < n; i++) {
            arrList.add(list.get(i).getId());
        }

        return arrList;
    }

    public Collection<IoTDevice> getFirstNDevicesByRegistration(int n) {
        if (n < 0) {
            throw new IllegalArgumentException();
        }

        List<IoTDevice> list = new LinkedList<>();
        for (IoTDevice value : storage.listAll()) {
            list.add(value);
        }

        RegistrationComparator compareReg = new RegistrationComparator();
        Collections.sort(list, compareReg);

        if (n >= list.size()) {
            n = list.size();
        }

        List<IoTDevice> arrList = new ArrayList<>(n);
        for (int i = 0; i < n; i++) {
            arrList.add(list.get(i));
        }

        return arrList;
    }
}