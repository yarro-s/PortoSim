/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package money.portosim;

import java.util.ArrayList;
import java.util.function.Function;
import money.portosim.containers.NumericMap;
import money.portosim.containers.NumericSeries;

/**
 *
 * @author yarro
 */
public interface Quantifiable {
    
    NumericMap<String> seriesMap(Function<NumericSeries, Double> f);

    default NumericMap<String> volatility() {
        return seriesMap(s -> Metrics.volatility(s.values()));    
    }
    
    default NumericMap<String> average() {
        return seriesMap(s -> Metrics.average(s.values()));       
    }
    
    default NumericMap<String> cummulativeGrowthRate() {
        return seriesMap(s -> Metrics.cummulativeGrowthRate(new ArrayList<>(s.values())));       
    }
    
    default NumericMap<String> totalReturn() {
        return seriesMap(s -> Metrics.totalReturn(new ArrayList<>(s.values())));       
    }
}
