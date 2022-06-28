/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package money.portosim.containers.core;

import java.util.List;
import java.util.Map;
import java.util.function.Function;

/**
 *
 * @author yarro
 */
public interface OrderedFrame<I, T> extends Frame<I, T> {
    
    <V> OrderedFrame<I, V> rolling(int n, Function<List<T>, V> f);
    
    OrderedFrame<I, T> from(I startIdx);
    
    OrderedFrame<I, T> to(I endIdx);
    
    Entry<I, T> firstEntry();
    
    Entry<I, T> lastEntry();
    
    static <I, T> OrderedFrame<I, T> of(Map<I, T> m) {
        return new DataSeries<>(m);
    }
    
    static <I, T> OrderedFrame<I, T> empty() {
        return new DataSeries<>();
    }
}
