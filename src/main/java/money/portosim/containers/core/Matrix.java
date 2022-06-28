/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package money.portosim.containers.core;

import java.util.Map;
import money.portosim.containers.core.Pair;

/**
 *
 * @author yarro
 */
public interface Matrix<I, K, T> extends Frame<Pair<I, K>, T> {
    Frame<K, ? extends OrderedFrame<I, T>> columns(); 
    
    OrderedFrame<I, ? extends Frame<K, T>> rows(); 
    
    static <I, K, T> Matrix<I, K, T> of(Map<Pair<I, K>, T> m) {
        return new DataMatrix<>(m);
    } 
    
    static <I, K, T> Matrix<I, K, T> empty() {
        return new DataMatrix<>();
    } 
}
