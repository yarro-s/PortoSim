package money.portosim.metrics;

import java.util.List;
import java.util.function.Function;


/**
 *
 * @author yarro
 */
public interface QuantSpan extends QuantSpanContext {
    
    default double calmarRatio() {
        return Metrics.calmarRatio(values(), getValuesPerRefPeriod());
    }
    
    default double marRatio() {
        return Metrics.marRatio(values(), getValuesPerRefPeriod());
    }

    default double maxDrawdown() {
        return Metrics.maxDrawdown(values());
    }

    default double sharpeRatio() {
        return Metrics.sharpeRatio(values(), getMeanRiskFreeRate(), getValuesPerRefPeriod());
    }
    
    default double stdDeviation() {
        return Metrics.stdDeviation(values());
    }
    
    default double variance() {
        return Metrics.variance(values());
    }

    default double volatility() {
        return Metrics.volatility(values());
    }    

    default double average() {
        return Metrics.average(values());
    }
    
    default double cummulativeGrowthRate() {
        return Metrics.cummulativeGrowthRate(values(), getValuesPerRefPeriod());
    }

    default double totalReturn() {
        return Metrics.totalReturn(values());
    }
    
    default double apply(Function<List<Double>, Double> f)  {
        return f.apply(values());
    }
    
    static QuantSpan of(QuantSpanContext context) { 
        return new QuantCalcSpan(context);
    }
}
