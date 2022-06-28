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
    default public Map<L, Double> add(Double val) {
        return bind(val, Double::sum);
    }
    
    default public Map<L, Double> add(NumFrame<L> nm) {
        return compose(nm, Double::sum);
    }
    
    default public Map<L, Double> mult(Double val) {
        return bind(val, (x, y) -> x * y);
    }
    
    default public Map<L, Double> mult(NumFrame<L> nm) {
        return compose(nm, (x, y) -> x * y);
    }
    
    default public Map<L, Double> div(Double val) {
        return bind(val, (x, y) -> x / y);
    }
    
    default public Map<L, Double> div(NumFrame<L> nm) {
        return compose(nm, (x, y) -> x / y);
    }
    
    default public Map<L, Double> sub(Double val) {
        return bind(val, (x, y) -> x - y);
    }
    
    default public Map<L, Double> sub(NumFrame<L> nm) {
        return compose(nm, (x, y) -> x - y);
    }
    
    default public Double sum() {
        return reduce(Double::sum);
    }
    
}
