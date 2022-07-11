package money.portosim;

import java.util.List;

public interface Quantifiable {
    
    List<Double> asList();
    
    default double sharpeRatio(List<Double> values, double riskFreeRate) {
        return Metrics.sharpeRatio(values, riskFreeRate);
    }
    
    default List<Double> toReturns() {
        return Metrics.toReturns(asList());   
    }
    
    default double stdDeviation() {
        return Metrics.stdDeviation(asList());      
    }
    
    default double variance() {
        return Metrics.variance(asList());      
    }

    default double volatility() {
        return Metrics.volatility(asList());      
    }
    
    default double average() {
        return Metrics.average(asList());             
    }
    
    default double cummulativeGrowthRate() {
        return Metrics.cummulativeGrowthRate(asList());     
    }
    
    default double totalReturn() {
        return Metrics.totalReturn(asList());      
    }
}
