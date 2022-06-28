/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package money.portosim.containers.numeric;

import java.util.Map;
import money.portosim.containers.core.Pair;
import money.portosim.containers.core.Frame;
import money.portosim.containers.core.OrderedFrame;

/**
 *
 * @author yarro
 */
public interface NumMatrix<I, K> extends NumFrame<Pair<I, K>> {
    
    Frame<K, ? extends NumOrderedFrame<I>> columns(); 
    
    OrderedFrame<I, ? extends NumFrame<K>> rows(); 
    
    static <I, K> NumMatrix<I, K> of(Map<Pair<I, K>, Double> m) {
        return new NumDataMatrix<>(m);
    } 
    
    static <I, K> NumMatrix<I, K> empty() {
        return new NumDataMatrix<>();
    } 
}
