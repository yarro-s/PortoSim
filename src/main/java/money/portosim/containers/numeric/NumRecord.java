/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package money.portosim.containers.numeric;

import java.util.AbstractMap;
import java.util.Map;
import java.util.Set;
import money.portosim.containers.core.Frame;

/**
 *
 * @author yarro
 */
class NumRecord<K> extends AbstractMap<K, Double> implements NumFrame<K> {
    
    final Frame<K, Double> frame;
     
    public NumRecord(Map<K, Double> m) {
        frame = Frame.of(m);
    }

    public NumRecord() {
        frame = Frame.empty();
    }

    @Override
    public Set<Entry<K, Double>> entrySet() {
        return frame.entrySet();
    }

    @Override
    public Double put(K key, Double value) {
        return frame.put(key, value);
    }
}
