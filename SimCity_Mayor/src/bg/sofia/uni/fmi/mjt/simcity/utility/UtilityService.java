package bg.sofia.uni.fmi.mjt.simcity.utility;

import bg.sofia.uni.fmi.mjt.simcity.property.billable.Billable;

import java.util.HashMap;
import java.util.Map;

public class UtilityService implements UtilityServiceAPI {
    private final Map<UtilityType, Double> taxRates;

    public UtilityService(Map<UtilityType, Double> taxRates) {
        this.taxRates = taxRates;
    }

    private double getConsumption(UtilityType type, Billable billable) {
        return switch (type) {
            case UtilityType.ELECTRICITY -> billable.getElectricityConsumption();
            case UtilityType.WATER -> billable.getWaterConsumption();
            case UtilityType.NATURAL_GAS -> billable.getNaturalGasConsumption();
        };
    }

    @Override
    public <T extends Billable> double getUtilityCosts(UtilityType utilityType, T billable) {
        if (utilityType == null || billable == null) {
            throw new IllegalArgumentException("utilityType or billable is null");
        }

        return switch (utilityType) {
            case UtilityType.ELECTRICITY ->
                billable.getElectricityConsumption() * taxRates.get(UtilityType.ELECTRICITY);
            case UtilityType.WATER ->
                billable.getWaterConsumption() * taxRates.get(UtilityType.WATER);
            case UtilityType.NATURAL_GAS ->
                billable.getNaturalGasConsumption() * taxRates.get(UtilityType.NATURAL_GAS);
        };
    }

    @Override
    public <T extends Billable> double getTotalUtilityCosts(T billable) {
        if (billable == null) {
            throw new IllegalArgumentException("billable is null");
        }

        return billable.getElectricityConsumption() * taxRates.get(UtilityType.ELECTRICITY) +
            billable.getWaterConsumption() * taxRates.get(UtilityType.WATER) +
            billable.getNaturalGasConsumption() * taxRates.get(UtilityType.NATURAL_GAS);
    }

    @Override
    public <T extends Billable> Map<UtilityType, Double> computeCostsDifference(T firstBillable, T secondBillable) {
        if (firstBillable == null || secondBillable == null) {
            throw new IllegalArgumentException("firstBillable or secondBillable is null");
        }

        Map<UtilityType, Double> absoluteDifferences = new HashMap<>(UtilityType.values().length);
        for (Map.Entry<UtilityType, Double> pair : taxRates.entrySet()) {
            double firstConsumption = getConsumption(pair.getKey(), firstBillable);
            double secondConsumption = getConsumption(pair.getKey(), secondBillable);

            double taxRate = pair.getValue();
            double costsDifference = Math.abs(firstConsumption * taxRate - secondConsumption * taxRate);

            absoluteDifferences.put(pair.getKey(), costsDifference);
        }

        return Map.copyOf(absoluteDifferences);
    }
}
