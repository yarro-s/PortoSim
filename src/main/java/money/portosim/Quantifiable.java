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
import java.util.stream.IntStream;

/**
 *
 * @author yarro
 */
public interface Quantifiable<K> {

    NavigableMap<K, Double> getNavigableMap();
    
    default <V> V apply(Function<List<Double>, V> f) {
        return f.apply(new ArrayList<>(getNavigableMap().values()));
    }
    
    default <V, K> Function<Function<List<Double>, V>, ? extends Map<K, V>> rolling(int n) {
        Function<Integer, K> keyMapper = i -> (K) (getNavigableMap().navigableKeySet().toArray()[i]);
                
        var valuesList = new ArrayList<>(getNavigableMap().values());
        Map<K, List<Double>> rolled = IntStream.rangeClosed(0, valuesList.size() - n).boxed()
                .collect(Collectors.toMap(j -> keyMapper.apply(j + n - 1), 
                        j -> valuesList.subList(j, j + n)));
        
        return (f) -> rolled.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, 
                e -> f.apply(e.getValue())));
    }
}
