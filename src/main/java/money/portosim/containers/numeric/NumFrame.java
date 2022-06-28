/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package money.portosim.containers.numeric;

import java.util.Map;
import money.portosim.containers.core.Frame;

/**
 *
 * @author yarro
 */
public interface NumFrame<L> extends Frame<L, Double> {
    default NumFrame<L> add(Double val) {
        return NumFrame.of(bind(val, Double::sum));
    }
    
    default NumFrame<L> add(NumFrame<L> nm) {
        return NumFrame.of(compose(nm, Double::sum));
    }
    
    default NumFrame<L> mult(Double val) {
        return NumFrame.of(bind(val, (x, y) -> x * y));
    }
    
    default NumFrame<L> mult(NumFrame<L> nm) {
        return NumFrame.of(compose(nm, (x, y) -> x * y));
    }
    
    default NumFrame<L> div(Double val) {
        return NumFrame.of(bind(val, (x, y) -> x / y));
    }
    
    default NumFrame<L> div(NumFrame<L> nm) {
        return NumFrame.of(compose(nm, (x, y) -> x / y));
    }
    
    default NumFrame<L> sub(Double val) {
        return NumFrame.of(bind(val, (x, y) -> x - y));
    }
    
    default NumFrame<L> sub(NumFrame<L> nm) {
        return NumFrame.of(compose(nm, (x, y) -> x - y));
    }
    
    default Double sum() {
        return reduce(Double::sum);
    }
    
    static <L> NumFrame<L> of(Map<L, Double> m) {
        return new NumRecord<>(m);
    }
    
    static <L> NumFrame<L> empty() {
        return new NumRecord<>();
    }
}
