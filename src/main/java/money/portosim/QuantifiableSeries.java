package money.portosim;

import java.util.List;

public interface QuantifiableSeries {
    
    List<Double> asList();

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
