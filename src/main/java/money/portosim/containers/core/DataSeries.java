/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package money.portosim.containers.core;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.NavigableMap;
import java.util.Set;
import java.util.TreeMap;
import java.util.function.Function;

/**
 *
 * @author yarro
 */
class DataSeries<I, T> extends AbstractMap<I, T> implements OrderedFrame<I, T> {

    private final NavigableMap<I, T> tm;
    
    public DataSeries(NavigableMap<I, T> nm) {
        this.tm = nm;
    }
    
    public DataSeries(Map<I, T> m) {
        this.tm = new TreeMap<>(m);
    }
    
    public DataSeries() {
        this.tm = new TreeMap<>();
    }
    
    @Override
    public <V> OrderedFrame<I, V> rolling(int n, Function<List<T>, V> f) {
        var allKeys = (I[]) tm.navigableKeySet().toArray();
        var result = new DataSeries<I, V>();
        
        for (int k0 = 0; k0 <= allKeys.length - n; k0++) {
            var kI = allKeys[k0 + n - 1];
            var window = new ArrayList<>(tm.subMap(allKeys[k0], true, kI, true).values());
            
            result.put(kI, f.apply(window));
        }       
        return result;
    }

    @Override
    public OrderedFrame<I, T> from(I startIdx) {
        return new DataSeries<>(tm.subMap(startIdx, true, tm.lastKey(), true));
    }

    @Override
    public OrderedFrame<I, T> to(I endIdx) {
        return new DataSeries<>(tm.subMap(tm.firstKey(), true, endIdx, true));
    }

    @Override
    public Set<Entry<I, T>> entrySet() {
        return tm.entrySet();
    }
       
    @Override
    public T put(I index, T value) {
        return tm.put(index, value); 
    }

    @Override
    public Entry<I, T> firstEntry() {
        return tm.firstEntry(); 
    }

    @Override
    public Entry<I, T> lastEntry() {
        return tm.lastEntry();
    }
}
