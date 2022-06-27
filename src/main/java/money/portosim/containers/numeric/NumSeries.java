/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package money.portosim.containers.numeric;

import java.util.Map;
import money.portosim.containers.core.DataSeries;

/**
 *
 * @author yarro
 */
public class NumSeries<I> extends DataSeries<I, Double> 
        implements NumFrame<I> {
     
    public NumSeries(Map<I, Double> m) {
        super(m);
    }

    public NumSeries() {
        super();
    }
}
