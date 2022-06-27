/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package money.portosim.containers.numeric;

import money.portosim.containers.core.Pair;
import money.portosim.containers.core.Frame;
import money.portosim.containers.core.OrderedFrame;

/**
 *
 * @author yarro
 */
public interface NumMatrix<I, K> extends NumFrame<Pair<I, K>> {
    public Frame<K, ? extends NumOrderedFrame<I>> columns(); 
    
    public OrderedFrame<I, ? extends NumFrame<K>> rows(); 
}
