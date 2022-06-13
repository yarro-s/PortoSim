/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package money.portosim;

import java.util.ArrayList;
import money.portosim.containers.Quote;
import money.portosim.containers.QuoteSeries;

/**
 *
 * @author yarro
 */
public interface Quantifiable {
    QuoteSeries seriesMap();

    default Quote volatility() {
        return seriesMap().mapSeries(s -> Metrics.volatility(s.values()));       
    }
    
    default Quote average() {
        return seriesMap().mapSeries(s -> Metrics.average(s.values()));       
    }
    
    default Quote cummulativeGrowthRate() {
        return seriesMap().mapSeries(s -> 
                Metrics.cummulativeGrowthRate(new ArrayList<>(s.values())));       
    }
    
    default Quote totalReturn() {
        return seriesMap().mapSeries(s -> 
                Metrics.totalReturn(new ArrayList<>(s.values())));       
    }
}
