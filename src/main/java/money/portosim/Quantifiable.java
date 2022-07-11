/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package money.portosim;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;
import java.util.stream.IntStream;

/**
 *
 * @author yarro
 */
public interface Quantifiable {
    
    Collection<Double> values();   
    
    default <V> V apply(Function<List<Double>, V> f) {
        return f.apply(new ArrayList<>(values()));
    }
    
    default <V> Function<Function<List<Double>, V>, List<V>> rolling(int n) {
        var valuesList = new ArrayList<>(values());
        var rolled = IntStream.rangeClosed(0, valuesList.size() - n).boxed()
                .map(j -> valuesList.subList(j, j + n));
        
        return (f) -> rolled.map(f).toList();
    }
    
}
