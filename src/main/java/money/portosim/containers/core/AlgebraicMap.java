/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package money.portosim.containers.core;

import java.util.Map;

/**
 *
 * @author yarro
 */
public interface AlgebraicMap<K, V> extends ComposableMap<K, V> {
    
    V add(V arg1, V arg2);
    
    V mult(V arg1, V arg2);
    
    V sub(V arg1, V arg2);
    
    V div(V arg1, V arg2);
    
    default Map<K, V> add(V val) {
        return map(v -> add(v, val));
    }
    
    default Map<K, V> mult(V val) {
        return map(v -> mult(v, val));
    }
    
    default Map<K, V> sub(V val) {
        return map(v -> sub(v, val));
    }
    
    default Map<K, V> div(V val) {
        return map(v -> div(v, val));
    }
    
    default V sum() {
        return reduce(this::add);
    }
    
    default Map<K, V> sub(AlgebraicMap<K, V> arg) {
        return combine(arg, this::sub);       
    }

    default Map<K, V> div(AlgebraicMap<K, V> arg) {
        return combine(arg, this::div);
        
    }
    
    default Map<K, V> add(AlgebraicMap<K, V> arg) {
        return combine(arg, this::add);
    }
    
    default Map<K, V> mult(AlgebraicMap<K, V> arg) {
        return combine(arg, this::mult);
    }
}
