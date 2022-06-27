/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package money.portosim.containers.numeric;

import java.util.Map;
import money.portosim.containers.core.DataRecord;

/**
 *
 * @author yarro
 */
public class NumRecord<K> extends DataRecord<K, Double> 
        implements NumFrame<K> {
     
    public NumRecord(Map<K, Double> m) {
        super(m);
    }

    public NumRecord() {
        super();
    }
}
