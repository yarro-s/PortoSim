package money.portosim;

import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.NavigableMap;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 *
 * @author yarro
 */
public interface Quantifiable<K> {

    NavigableMap<K, Double> getNavigableMap();
    
    default <V> V apply(Function<List<Double>, V> f) {
        return f.apply(new ArrayList<>(getNavigableMap().values()));
    }
       
    default <V> Function<Function<List<Double>, V>, Stream<Map.Entry<K, V>>> rolling_depr(int n) {
        var entries = new ArrayList<>(getNavigableMap().entrySet());

        return (f) -> IntStream.rangeClosed(0, entries.size() - n).boxed()
                .map(i -> {
                    var window = entries.subList(i, i + n);
                    var vals = window.stream().map(Map.Entry::getValue).toList();
                    var key = window.get(window.size() - 1).getKey();
                    return Map.entry(key, f.apply(vals));
                });
    }
    
    default Function<Function<List<Double>, Double>, DoubleStream> rollingDouble(int n) {
        return (f) -> this.<Double>rolling_depr(n).apply(f).mapToDouble(Map.Entry::getValue);
    }
    
    default <V> Function<Function<List<Double>, V>, Map<K, V>> rollingMap(int n) {
        return (f) -> this.<V>rolling_depr(n).apply(f).collect(Collectors
                .toMap(Map.Entry::getKey, Map.Entry::getValue));
    }
    
    default double calmarRatio() {
        return apply(vals -> Metrics.calmarRatio(vals, getValuesPerYear()));
    }
    
    default double marRatio() {
        return apply(vals -> Metrics.marRatio(vals, getValuesPerYear()));
    }
    
    default double maxDrawdown() {
        return apply(Metrics::maxDrawdown);
    }
    
    default double sharpeRatio() {
        return apply(vals -> Metrics.sharpeRatio(vals, getRiskFreeRate(), getValuesPerYear()));
    }
    
    default List<Double> excessReturns() {
        return apply(vals -> Metrics.excessReturns(vals, getRiskFreeRate(), getValuesPerYear()));
    }
    
    default List<Double> toReturns() {
        return apply(vals -> Metrics.toReturns(vals, getValuesPerYear()));
    }
     
    default double stdDeviation() {
        return apply(Metrics::stdDeviation);
    }
    
    default double variance() {
        return apply(Metrics::variance);
    }

    default double volatility() {
        return apply(Metrics::volatility);
    }

    default double average() {
        return apply(Metrics::average);
    }
    
    default double cummulativeGrowthRate() {
        return apply(vals -> Metrics.cummulativeGrowthRate(vals, getValuesPerYear()));
    }

    default double totalReturn() {
        return apply(Metrics::totalReturn);
    }

    default int getValuesPerYear() {
        return (int) (365 / getTimeFrame().getDuration().toDays());
    }
    
    ChronoUnit getTimeFrame();
    
    Quantifiable<K> setTimeFrame(ChronoUnit timeFrame);
    
    double getRiskFreeRate();
    
    Quantifiable<K> setRiskFreeRate(double riskFreeRate);
}
