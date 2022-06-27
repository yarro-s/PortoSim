/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package money.portosim.containers.core;

import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author yarro
 */
public class DataRecord<K, T> extends AbstractMap<K, T> implements Frame<K, T> {
    
    private final Map<K, T> m;

    public DataRecord(Map<K, T> m) {
        this.m = m;
    }
    
    public DataRecord() {
        this.m = new HashMap<>();
    }
    
    @Override
    public Set<Entry<K, T>> entrySet() {
        return m.entrySet();
    }
       
    @Override
    public T put(K key, T value) {
        return m.put(key, value); 
    }
}
