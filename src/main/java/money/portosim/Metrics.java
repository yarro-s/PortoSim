package money.portosim;

import java.util.Collection;
import java.util.List;
import java.util.function.BiFunction;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;

public interface Metrics {
    
    static double calmarRatio(List<Double> values, int valuesPerRefPeriod) {
        return (Math.pow(average(toReturns(values, valuesPerRefPeriod)), valuesPerRefPeriod) - 1) 
                / maxDrawdown(values);
    }
    
    static double marRatio(List<Double> values, int valuesPerRefPeriod) {
        return cummulativeGrowthRate(values, valuesPerRefPeriod) / maxDrawdown(values);
    }
    
    static double maxDrawdown(List<Double> values) {
        BiFunction<List<Double>, Double, DoubleStream> listMinusVal = (l, x) -> 
                l.stream().mapToDouble(v -> (v - x) / x);
        
        var n = values.size();      
        var drawdowns = IntStream.range(0, n).boxed().flatMapToDouble(i -> {
            var currentValue = values.get(i);
            var remainingValues = values.subList(i + 1, n);
            return listMinusVal.apply(remainingValues, currentValue);
        }).filter(v -> v < 0);
        
        var maxDrawdown = drawdowns.min().orElse(0.0);
        
        return maxDrawdown;
    }
    
    static double sharpeRatio(List<Double> values, double meanRiskFreeRate, int valuesPerRefPeriod) {
        var excReturns = excessReturns(values, meanRiskFreeRate, valuesPerRefPeriod);
        return Math.sqrt(valuesPerRefPeriod) * average(excReturns) / stdDeviation(excReturns);
    }
    
    static List<Double> excessReturns(List<Double> values, double baseRate, int valuesPerRefPeriod) {
        return toReturns(values, valuesPerRefPeriod).stream().map(v -> v - baseRate).toList();
    }
   
    static List<Double> toReturns(List<Double> values, int valuesPerRefPeriod) {
        return IntStream.range(0, values.size() - valuesPerRefPeriod + 1).boxed().map(i -> 
            values.get(i + valuesPerRefPeriod - 1) / values.get(i) - 1).toList();
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

    static double cummulativeGrowthRate(List<Double> values, double valuesPerRefPeriod) {
        var totalReturn = totalReturn(values);

        final double nValues = values.size() - 1;
        final double nPeriods = nValues / valuesPerRefPeriod;
        return Math.pow(totalReturn, 1 / nPeriods) - 1.0;
    }

    static double totalReturn(List<Double> values) {
        final double valueAtStart = values.get(0),
                valueAtEnd = values.get(values.size() - 1);

        return valueAtEnd / valueAtStart;
    }
}
