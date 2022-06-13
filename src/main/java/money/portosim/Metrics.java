package money.portosim;

import java.util.Collection;
import java.util.List;

public class Metrics {

    public static double volatility(Collection<Double> values) {
        final double nValues = values.size();
        final double averageValue = average(values);

        var squareDiffs = values.stream().mapToDouble(v -> Math.pow(v - averageValue, 2.0));
        return Math.sqrt(squareDiffs.sum() / nValues);
    }

    public static double average(Collection<Double> values) {
        return values.stream().mapToDouble(Double::doubleValue).average().orElseThrow();
    }

    public static double cummulativeGrowthRate(List<Double> values) {
        var totalReturn = totalReturn(values);

        final double nTimePeriods = values.size();
        return Math.pow(totalReturn, 1.0 / nTimePeriods) - 1.0;
    }

    public static double totalReturn(List<Double> values) {
        final double valueAtStart = values.get(0),
                valueAtEnd = values.get(values.size() - 1);

        return valueAtEnd / valueAtStart;
    }

    private Metrics() {
    }
}
