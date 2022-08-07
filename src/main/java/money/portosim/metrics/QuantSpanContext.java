/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package money.portosim.metrics;

import java.util.List;

/**
 *
 * @author yarro
 */
public interface QuantSpanContext {
    
    int getValuesPerRefPeriod();
        
    double getMeanRiskFreeRate();
    
    List<Double> values();
}
