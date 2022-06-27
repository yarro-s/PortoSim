/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package money.portosim.containers.core;

import money.portosim.containers.core.Pair;

/**
 *
 * @author yarro
 */
public interface Matrix<I, K, T> extends Frame<Pair<I, K>, T> {
    public Frame<K, ? extends OrderedFrame<I, T>> columns(); 
    
    public OrderedFrame<I, ? extends Frame<K, T>> rows(); 
}
