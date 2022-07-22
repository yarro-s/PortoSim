/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package money.portosim;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.NavigableMap;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 *
 * @author yarro
 */
public interface Quantifiable<K> {

    NavigableMap<K, Double> getNavigableMap();
    
    default <V> V apply(Function<List<Double>, V> f) {
        return f.apply(new ArrayList<>(getNavigableMap().values()));
    }
       
    default <V> Function<Function<List<Double>, V>, Stream<Map.Entry<K, V>>> rolling(int n) {
        var entries = new ArrayList<>(getNavigableMap().entrySet());

        return (f) -> IntStream.rangeClosed(0, entries.size() - n).boxed()
                .map(i -> {
                    var window = entries.subList(i, i + n);
                    var vals = window.stream().map(Map.Entry::getValue).toList();
                    var key = window.get(window.size() - 1).getKey();
                    return Map.entry(key, f.apply(vals));
                });
    }
    
    default Function<Function<List<Double>, Double>, DoubleStream> rollingDouble(int n) {
        return (f) -> this.<Double>rolling(n).apply(f).mapToDouble(Map.Entry::getValue);
    }
    
    default <V> Function<Function<List<Double>, V>, Map<K, V>> rollingMap(int n) {
        return (f) -> this.<V>rolling(n).apply(f).collect(Collectors
                .toMap(Map.Entry::getKey, Map.Entry::getValue));
    }
}
