package bg.sofia.uni.fmi.mjt.simcity.plot;

import bg.sofia.uni.fmi.mjt.simcity.exception.BuildableAlreadyExistsException;
import bg.sofia.uni.fmi.mjt.simcity.exception.BuildableNotFoundException;
import bg.sofia.uni.fmi.mjt.simcity.exception.InsufficientPlotAreaException;
import bg.sofia.uni.fmi.mjt.simcity.property.buildable.Buildable;

import java.util.HashMap;
import java.util.Map;

public class Plot<E extends Buildable> implements PlotAPI<E> {
    private final int buildableArea;
    private final Map<String, E> buildings;

    public Plot(int buildableArea) {
        this.buildableArea = buildableArea;
        this.buildings = new HashMap<>();
    }

    private void validateBuildableCanBeConstructedUniqueness(String address) {
        if (buildings.containsKey(address)) {
            throw new BuildableAlreadyExistsException("buildable already exists");
        }
    }

    @Override
    public void construct(String address, E buildable) {
        if (address == null || address.isBlank()) {
            throw new IllegalArgumentException("address is null or blank");
        }

        if (buildable == null) {
            throw new IllegalArgumentException("buildable is null");
        }

        if (buildable.getArea() > getRemainingBuildableArea()) {
            throw new InsufficientPlotAreaException("available area is not sufficient");
        }

        validateBuildableCanBeConstructedUniqueness(address);

        buildings.put(address, buildable);
    }

    @Override
    public void constructAll(Map<String, E> buildables) {
        if (buildables == null || buildables.isEmpty()) {
            throw new IllegalArgumentException("buildables is null or empty");
        }

        int remainingArea = getRemainingBuildableArea();
        for (Map.Entry<String, E> pair : buildables.entrySet()) {
            remainingArea -= pair.getValue().getArea();
            if (remainingArea < 0) {
                throw new InsufficientPlotAreaException("available area is not sufficient");
            }

            validateBuildableCanBeConstructedUniqueness(pair.getKey());
        }

        buildings.putAll(buildables);
    }

    @Override
    public void demolish(String address) {
        if (address == null || address.isBlank()) {
            throw new IllegalArgumentException("address is null or blank");
        }

        Buildable building = buildings.remove(address);

        if (building == null) {
            throw new BuildableNotFoundException("building with such address does not exist");
        }
    }

    @Override
    public void demolishAll() {
        buildings.clear();
    }

    @Override
    public Map<String, E> getAllBuildables() {
        return Map.copyOf(buildings);
    }

    @Override
    public int getRemainingBuildableArea() {
        int occupiedBuildableArea = 0;
        for (Buildable building : buildings.values()) {
            occupiedBuildableArea += building.getArea();
        }

        return buildableArea - occupiedBuildableArea;
    }

    public static void main(String[] args) {

    }
}
