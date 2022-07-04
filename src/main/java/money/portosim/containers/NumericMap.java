/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package money.portosim.containers;

import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import money.portosim.containers.core.AlgebraicMap;

/**
 *
 * @author yarro
 */
public class NumericMap<K> extends AbstractMap<K, Double> implements AlgebraicMap<K, Double> {
    
    private final Map<K, Double> m;
        
    public static <K> NumericMap<K> of(Map<K, Double> m) {
        return new NumericMap<>(m);
    }

    public NumericMap() { 
        m = new HashMap<>();
    }
    
    public NumericMap(Map<K, Double> m) {
        this.m = m;
    }
    
    @Override
    public Set<Entry<K, Double>> entrySet() {
        return m.entrySet();
    }
   
    @Override
    public Double put(K key, Double value) {
        return m.put(key, value); 
    }
    
    @Override
    public Double add(Double arg1, Double arg2) {
        return arg1 + arg2;
    }

    @Override
    public Double mult(Double arg1, Double arg2) {
        return arg1 * arg2;
    }

    @Override
    public Double sub(Double arg1, Double arg2) {
        return arg1 - arg2;
    }

    @Override
    public Double div(Double arg1, Double arg2) {
        return arg1 / arg2;
    }   
    
    @Override
    public NumericMap<K> add(AlgebraicMap<K, Double> arg) {
        return new NumericMap<>(AlgebraicMap.super.add(arg));
    }

    @Override
    public NumericMap<K> mult(AlgebraicMap<K, Double> arg) {
        return new NumericMap<>(AlgebraicMap.super.mult(arg));
    }

    @Override
    public NumericMap<K> div(AlgebraicMap<K, Double> arg) {
        return new NumericMap<>(AlgebraicMap.super.div(arg));
    }
    
    @Override
    public NumericMap<K> sub(AlgebraicMap<K, Double> arg) {
        return new NumericMap<>(AlgebraicMap.super.sub(arg));
    }
}
