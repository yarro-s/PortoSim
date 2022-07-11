package money.portosim;

import java.util.Collection;
import java.util.List;
import java.util.stream.IntStream;

public interface Metrics {
    
    static double maxDrawDown(List<Double> values, double riskFreeRate) {
        return 0.0;
    }
    
    static double sharpeRatio(List<Double> values, double riskFreeRate) {
        return (cummulativeGrowthRate(values) - riskFreeRate) / stdDeviation(toReturns(values));
    }
    
    static List<Double> toReturns(List<Double> values) {
        return IntStream.range(1, values.size()).boxed().map(i -> 
                cummulativeGrowthRate(List.of(values.get(i - 1), values.get(i)))
        ).toList();
    }
    
    static double stdDeviation(Collection<Double> values) {
        return Math.sqrt(variance(values));
    }
    
    static double variance(Collection<Double> values) {
        final double nValues = values.size();
        final double averageValue = average(values);

        var squareDiffs = values.stream().mapToDouble(v -> Math.pow(v - averageValue, 2.0));
        return squareDiffs.sum() / (nValues - 1);
    }

    static double volatility(Collection<Double> values) {
        final double nValues = values.size();
        return stdDeviation(values) * Math.sqrt((nValues - 1) / nValues);
    }

    static double average(Collection<Double> values) {
        return values.stream().mapToDouble(Double::doubleValue).average().orElseThrow();
    }

    static double cummulativeGrowthRate(List<Double> values) {
        var totalReturn = totalReturn(values);

        final double nTimePeriods = values.size() - 1;
        return Math.pow(totalReturn, 1.0 / nTimePeriods) - 1.0;
    }

    static double totalReturn(List<Double> values) {
        final double valueAtStart = values.get(0),
                valueAtEnd = values.get(values.size() - 1);

        return valueAtEnd / valueAtStart;
    }
}
