/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package money.portosim.containers.numeric;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import money.portosim.containers.core.DataMatrix;
import money.portosim.containers.core.DataRecord;
import money.portosim.containers.core.DataSeries;
import money.portosim.containers.core.Pair;
import money.portosim.containers.core.Frame;
import money.portosim.containers.core.OrderedFrame;

/**
 *
 * @author yarro
 */
public class NumDataMatrix<I, K> extends DataMatrix<I, K, Double> 
        implements NumMatrix<I, K> { 

    public NumDataMatrix(Map<Pair<I, K>, Double> m) {
        super(m);
    }

    public NumDataMatrix() {
        super();
    }
    
    protected class NumFrameView extends FrameView implements NumFrame<K> {
        public NumFrameView(I idx) {
            super(idx);
        }
    }
    
    protected class NumOrderedFrameView extends OrderedFrameView implements NumOrderedFrame<I> {
        public NumOrderedFrameView(K key) {
            super(key);
        }
    }

    @Override
    public OrderedFrame<I, ? extends NumFrame<K>> rows() {
        return new DataSeries<>(indices.stream().collect(Collectors.toMap(Function.identity(), 
                NumFrameView::new)));
    }
        
    @Override
    public Frame<K, ? extends NumOrderedFrame<I>> columns() { 
        return new DataRecord<>(keys.stream().collect(Collectors.toMap(Function.identity(), 
                NumOrderedFrameView::new)));
    }
}
