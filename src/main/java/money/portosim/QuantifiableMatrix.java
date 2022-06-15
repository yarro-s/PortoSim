/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package money.portosim;

import java.util.ArrayList;
import java.util.Map;
import java.util.function.Function;
import money.portosim.containers.NumericSeries;

/**
 *
 * @author yarro
 */
public interface QuantifiableMatrix {
    
    Map<String, Double> map(Function<NumericSeries, Double> mapper);

    default Map<String, Double> volatility() {
        return map(s -> Metrics.volatility(s.values()));    
    }
    
    default Map<String, Double> average() {
        return map(s -> Metrics.average(s.values()));       
    }
    
    default Map<String, Double> cummulativeGrowthRate() {
        return map(s -> Metrics.cummulativeGrowthRate(new ArrayList<>(s.values())));       
    }
    
    default Map<String, Double> totalReturn() {
        return map(s -> Metrics.totalReturn(new ArrayList<>(s.values())));       
    }
}
