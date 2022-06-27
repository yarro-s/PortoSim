/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package money.portosim.containers.core;

import java.util.AbstractMap;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 *
 * @author yarro
 */
public class DataMatrix<I, K, T> extends AbstractMap<Pair<I, K>, T> implements Matrix<I, K, T> {
   
    private final Map<Pair<I, K>, T> m;
    protected final Set<I> indices;   
    protected final Set<K> keys;

    public DataMatrix(Map<Pair<I, K>, T> m) {
        this.m = new HashMap<>(m);
        
        this.indices = this.m.keySet().stream().map(Pair::left).collect(Collectors.toSet());
        this.keys = this.m.keySet().stream().map(Pair::right).collect(Collectors.toSet());
    }
    
    public DataMatrix() {
        this.m = new HashMap<Pair<I, K>, T>();
        this.indices = new HashSet<>();
        this.keys = new HashSet<>();
    }   
    
    protected class FrameView extends AbstractMap<K, T> implements Frame<K, T>  {
        private final I idx;

        public FrameView(I idx) {
            this.idx = idx;
        }
              
        @Override
        public Set<Entry<K, T>> entrySet() {
            return keys.stream().map(k -> new Entry<K, T>() {
                @Override
                public K getKey() {
                    return k;
                }

                @Override
                public T getValue() {
                    return DataMatrix.this.get(Pair.of(idx, k));
                }

                @Override
                public T setValue(T value) {
                    return DataMatrix.this.put(Pair.of(idx, k), value);
                }
            }).collect(Collectors.toSet());
        }

        @Override
        public T put(K key, T value) {
            return DataMatrix.this.put(Pair.of(idx, key), value); 
        }
    }
    
    protected class OrderedFrameView extends AbstractMap<I, T> implements OrderedFrame<I, T>  {
        private final K key;
        private final OrderedFrame<I, T> seriesView;

        public OrderedFrameView(K key) {
            this.key = key;
            var entrySet = indices.stream().map(idx -> new Entry<I, T>() {
                @Override
                public I getKey() {
                    return idx;
                }

                @Override
                public T getValue() {
                    return DataMatrix.this.get(Pair.of(idx, key));
                }

                @Override
                public T setValue(T value) {
                    return DataMatrix.this.put(Pair.of(idx, key), value);
                }
            }).collect(Collectors.toSet());
            this.seriesView = new DataSeries<>(new TreeMap(entrySet.stream()
                    .collect(Collectors.toMap(Entry::getKey, Entry::getValue))));
        }
        
        @Override
        public Set<Entry<I, T>> entrySet() {
            return seriesView.entrySet();
        }

        @Override
        public T put(I idx, T value) {
            return DataMatrix.this.put(Pair.of(idx, key), value); 
        }

        @Override
        public <V> OrderedFrame<I, V> rolling(int n, Function<List<T>, V> f) {
            return seriesView.rolling(n, f);
        }

        @Override
        public OrderedFrame<I, T> from(I startIdx) {
            return seriesView.from(startIdx);
        }

        @Override
        public OrderedFrame<I, T> to(I endIdx) {
            return seriesView.to(endIdx);
        }
        
    }
    
    @Override
    public OrderedFrame<I, ? extends Frame<K, T>> rows() {
        return new DataSeries<>(indices.stream().collect(Collectors.toMap(Function.identity(), 
                FrameView::new)));
    }
    
    @Override
    public Frame<K, ? extends OrderedFrame<I, T>> columns() { 
        return new DataRecord<>(keys.stream().collect(Collectors.toMap(Function.identity(), 
                OrderedFrameView::new)));
    }
    
    @Override
    public Set<Entry<Pair<I, K>, T>> entrySet() {
        return m.entrySet();
    }
    
    @Override
    public T put(Pair<I, K> ik, T val) {
        return m.put(ik, val);
    }
}
