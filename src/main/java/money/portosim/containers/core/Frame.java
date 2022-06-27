/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package money.portosim.containers.core;

import java.util.HashSet;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 *
 * @author yarro
 */
public interface Frame<L, T> extends Map<L, T> {
    
    default public <U, V> Map<L, V> compose(Frame<L, U> frame, BiFunction<T, U, V> f) {
        var intersection = new HashSet<>(keySet());
        intersection.retainAll(frame.keySet());

        return intersection.stream().collect(Collectors.toMap(Function.identity(),
                k -> f.apply(get(k), frame.get(k))));      
    }
    
    default public <U, V> Map<L, V> bind(U val, BiFunction<T, U, V> f) {
        return map(e -> f.apply(e, val));
    }
        
    default public T reduce(BinaryOperator<T> f) {
        return values().stream().reduce(f).orElse(null);
    }
    
    default public <V> Map<L, V> map(Function<T, V> f) {
        return entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, 
                e -> f.apply(e.getValue()))); 
    }
    
}
