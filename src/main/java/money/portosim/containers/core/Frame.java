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
    
    default <U, V> Frame<L, V> compose(Frame<L, U> frame, BiFunction<T, U, V> f) {
        var intersection = new HashSet<>(keySet());
        intersection.retainAll(frame.keySet());

        return Frame.of(intersection.stream().collect(Collectors.toMap(Function.identity(),
                k -> f.apply(get(k), frame.get(k)))));      
    }
    
    default <U, V> Frame<L, V> bind(U val, BiFunction<T, U, V> f) {
        return Frame.of(map(e -> f.apply(e, val)));
    }
        
    default T reduce(BinaryOperator<T> f) {
        return values().stream().reduce(f).orElse(null);
    }
    
    default <V> Frame<L, V> map(Function<T, V> f) {
        return Frame.of(entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, 
                e -> f.apply(e.getValue())))); 
    }
    
    static <L, T> Frame<L, T> of(Map<L, T> m) {
        return new DataRecord<>(m);
    }
    
    static <L, T> Frame<L, T> empty() {
        return new DataRecord<>();
    }
}
