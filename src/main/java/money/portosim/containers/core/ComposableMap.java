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
public interface ComposableMap<K, V> extends Map<K, V> {
    
    default <R> Map<K, R> map(Function<V, R> mapper) {
        return keySet().stream().collect(Collectors.toMap(Function.identity(),
                k -> mapper.apply(get(k))));
    }
    
    default V reduce(BinaryOperator<V> combiner) {    
        return values().stream().reduce(combiner).orElse(null);
    }
    
    default <T, R> Map<K, R> combine(Map<K, T> m, BiFunction<V, T, R> combiner) {
        var intersection = new HashSet<>(keySet());
        intersection.retainAll(m.keySet());

        return intersection.stream().collect(Collectors.toMap(Function.identity(),
                k -> combiner.apply(get(k), m.get(k))));
    }
    
}
