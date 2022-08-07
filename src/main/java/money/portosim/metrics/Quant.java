package money.portosim.metrics;

import java.util.Map;
import java.util.NavigableMap;
import java.util.function.Function;

/**
 *
 * @author yarro
 */
public interface Quant<K> {
    
    default Map<K, Double> calmarRatio() {
        return apply(QuantSpan::calmarRatio);
    }
    
    default Map<K, Double> marRatio() {
        return apply(QuantSpan::marRatio);
    }

    default Map<K, Double> maxDrawdown() {
        return apply(QuantSpan::maxDrawdown);
    }

    default Map<K, Double> sharpeRatio() {
        return apply(QuantSpan::sharpeRatio);
    }
    
    default Map<K, Double> stdDeviation() {
        return apply(QuantSpan::stdDeviation);    
    }
    
    default Map<K, Double> variance() {
        return apply(QuantSpan::stdDeviation);   
    }

    default Map<K, Double> volatility() {
       return apply(QuantSpan::volatility);
    }

    default Map<K, Double> average() {
        return apply(QuantSpan::average);
    }
    
    default Map<K, Double> cummulativeGrowthRate() {
        return apply(QuantSpan::cummulativeGrowthRate);
    }

    default Map<K, Double> totalReturn() {
        return apply(QuantSpan::totalReturn);
    }
    
    Map<K, Double> apply(Function<QuantSpan, Double> f);
    
    static <K extends Comparable> QuantContext<K> of(NavigableMap<K, Double> nm) { 
        return new QuantBuilder<>(nm);
    }
}
