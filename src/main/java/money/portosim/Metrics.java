package money.portosim;

import java.util.Collection;
import java.util.List;
import java.util.function.BiFunction;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;

public interface Metrics {
    
    static double calmarRatio(List<Double> values, int valuesPerRefPeriod) {
        return (Math.pow(average(toReturns(values)), valuesPerRefPeriod) - 1) / maxDrawdown(values);
    }
    
    static double marRatio(List<Double> values) {
        return cummulativeGrowthRate(values) / maxDrawdown(values);
    }
    
    static double maxDrawdown(List<Double> values) {
        BiFunction<List<Double>, Double, DoubleStream> listMinusVal = (l, x) -> 
                l.stream().mapToDouble(v -> (v - x) / x);
        
        var n = values.size();      
        return IntStream.range(0, n).boxed().flatMapToDouble(i -> {
            var currentValue = values.get(i);
            var remainingValues = values.subList(i + 1, n);
            return listMinusVal.apply(remainingValues, currentValue);
        }).filter(v -> v < 0).min().orElse(0.0);
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

    static double cummulativeGrowthRate(List<Double> values, int valuesPerRefPeriod) {
        var totalReturn = totalReturn(values);

        final double nTimePeriods = values.size() - 1;
        return Math.pow(totalReturn, valuesPerRefPeriod / nTimePeriods) - 1.0;
    }
    
    static double cummulativeGrowthRate(List<Double> values) {
        return cummulativeGrowthRate(values, 1);
    }

    static double totalReturn(List<Double> values) {
        final double valueAtStart = values.get(0),
                valueAtEnd = values.get(values.size() - 1);

        return valueAtEnd / valueAtStart;
    }
}
