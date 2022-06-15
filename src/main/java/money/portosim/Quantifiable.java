/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package money.portosim;

import java.util.ArrayList;
import java.util.function.Function;
import money.portosim.containers.NumericSeries;
import money.portosim.containers.Quote;

/**
 *
 * @author yarro
 */
public interface Quantifiable {
    
    Quote seriesMap(Function<NumericSeries, Double> f);

    default Quote volatility() {
        return seriesMap(s -> Metrics.volatility(s.values()));    
    }
    
    default Quote average() {
        return seriesMap(s -> Metrics.average(s.values()));       
    }
    
    default Quote cummulativeGrowthRate() {
        return seriesMap(s -> Metrics.cummulativeGrowthRate(new ArrayList<>(s.values())));       
    }
    
    default Quote totalReturn() {
        return seriesMap(s -> Metrics.totalReturn(new ArrayList<>(s.values())));       
    }
}