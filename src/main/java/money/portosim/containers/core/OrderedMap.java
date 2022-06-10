/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package money.portosim.containers.core;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.NavigableMap;
import java.util.NavigableSet;
import java.util.Set;
import java.util.SortedMap;
import java.util.function.Function;

/**
 *
 * @author yarro
 */
public interface OrderedMap<T, V> extends NavigableMap<T, V> {
    public NavigableMap<T, V> ordered();
    
    default public Map<T, V> rolling(int n, Function<SortedMap<T, V>, V> f) {
        var allKeys = (T[]) navigableKeySet().toArray();
        var result = new HashMap<T, V>();
        
        for (int k0 = 0; k0 <= allKeys.length - n; k0++) {
            var kT = allKeys[k0 + n - 1];
            var window = subMap(allKeys[k0], true, kT, true);
            
            result.put(kT, f.apply(window));
        }       
        return result;
    }
            
    @Override
    default public Set<Entry<T, V>> entrySet() {
        return ordered().entrySet();
    }

    @Override
    default public Entry<T, V> lowerEntry(T key) {
        return ordered().lowerEntry(key);
    }

    @Override
    default public T lowerKey(T key) {
        return ordered().lowerKey(key);
    }

    @Override
    default public Entry<T, V> floorEntry(T key) {
        return ordered().floorEntry(key);
    }

    @Override
    default public T floorKey(T key) {
       return ordered().floorKey(key);
    }

    @Override
    default public Entry<T, V> ceilingEntry(T key) {
       return ordered().ceilingEntry(key);
    }

    @Override
    default public T ceilingKey(T key) {
        return ordered().ceilingKey(key);
    }

    @Override
    default public Entry<T, V> higherEntry(T key) {
        return ordered().higherEntry(key);
    }

    @Override
    default public T higherKey(T key) {
        return ordered().higherKey(key);
    }

    @Override
    default public Entry<T, V> firstEntry() {
        return ordered().firstEntry();
    }

    @Override
    default public Entry<T, V> lastEntry() {
        return ordered().lastEntry();
    }

    @Override
    default public Entry<T, V> pollFirstEntry() {
        return ordered().pollFirstEntry();
    }

    @Override
    default public Entry<T, V> pollLastEntry() {
        return ordered().pollLastEntry();
    }

    @Override
    default public NavigableMap<T, V> descendingMap() {
        return ordered().descendingMap();
    }

    @Override
    default public NavigableSet<T> navigableKeySet() {
        return ordered().navigableKeySet();
    }

    @Override
    default public NavigableSet<T> descendingKeySet() {
        return ordered().descendingKeySet();
    }

    @Override
    default public NavigableMap<T, V> subMap(T fromKey, boolean fromInclusive, T toKey, boolean toInclusive) {
        return ordered().subMap(fromKey, fromInclusive, toKey, toInclusive);
    }

    @Override
    default public NavigableMap<T, V> headMap(T toKey, boolean inclusive) {
        return ordered().headMap(toKey, inclusive);
    }

    @Override
    default public NavigableMap<T, V> tailMap(T fromKey, boolean inclusive) {
        return ordered().tailMap(fromKey, inclusive);
    }

    @Override
    default public SortedMap<T, V> subMap(T fromKey, T toKey) {
        return ordered().subMap(fromKey, toKey);
    }

    @Override
    default public SortedMap<T, V> headMap(T toKey) {
        return ordered().headMap(toKey);
    }

    @Override
    default public SortedMap<T, V> tailMap(T fromKey) {
       return ordered().tailMap(fromKey);
    }   

    @Override
    default public Comparator<? super T> comparator() {
        return ordered().comparator();
    }

    @Override
    default public T firstKey() {
        return ordered().firstKey();
    }

    @Override
    default public T lastKey() {
        return ordered().lastKey();
    }
}
