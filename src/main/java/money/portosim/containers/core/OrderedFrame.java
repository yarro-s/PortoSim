/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package money.portosim.containers.core;

import java.util.List;
import java.util.SortedMap;
import java.util.function.Function;

/**
 *
 * @author yarro
 */
public interface OrderedFrame<I, T> extends Frame<I, T> {
    
    public <V> OrderedFrame<I, V> rolling(int n, Function<List<T>, V> f);
    
    public OrderedFrame<I, T> from(I startIdx);
    
    public OrderedFrame<I, T> to(I endIdx);
    
    public Entry<I, T> firstEntry();
    
    public Entry<I, T> lastEntry();
}
