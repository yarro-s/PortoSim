/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package money.portosim.containers.numeric;

import java.util.Map;
import money.portosim.containers.core.OrderedFrame;

/**
 *
 * @author yarro
 */
public interface NumOrderedFrame<L> extends OrderedFrame<L, Double>, NumFrame<L> {
    
    static <L> NumOrderedFrame<L> of(Map<L, Double> m) {
        return new NumSeries<>(m);
    } 
    
    static <L> NumOrderedFrame<L> empty() {
        return new NumSeries<>();
    } 
}
