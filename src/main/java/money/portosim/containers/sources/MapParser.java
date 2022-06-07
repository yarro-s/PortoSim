/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package money.portosim.containers.sources;

import java.util.AbstractMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author yarro
 */
public abstract class MapParser<K, V> extends AbstractMap<K, V> {

    @Override
    public abstract Set<K> keySet();

    @Override
    public abstract V get(Object key);
   
    @Override
    public Set<Entry<K, V>> entrySet() {
        Set<Entry<K, V>> entrySet = new HashSet<>();
        
        keySet().forEach(k -> entrySet.add(
                new AbstractMap.SimpleEntry<K, V>(k, get(k)))); 
        return entrySet;
    }
}
