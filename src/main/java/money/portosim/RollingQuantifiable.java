/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package money.portosim;

import java.util.List;
import java.util.function.Function;
import java.util.stream.IntStream;

/**
 *
 * @author yarro
 */
public interface RollingQuantifiable {
    
    List<Double> asList();
    
    default <V> List<V> roll(int n, Function<List<Double>, V> f) {
        var values = asList();
        
        return IntStream.rangeClosed(0, values.size() - n).boxed().map(j -> {
            var jT = j + n;
            var window = values.subList(j, jT); 
            return f.apply(window);
        }).toList();
    } 
}
