/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package money.portosim.containers.numeric;

import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import money.portosim.containers.core.DataMatrix;
import money.portosim.containers.core.Pair;
import money.portosim.containers.core.Frame;
import money.portosim.containers.core.OrderedFrame;

/**
 *
 * @author yarro
 */
class NumDataMatrix<I, K> extends DataMatrix<I, K, Double> 
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
        return OrderedFrame.of(indices.stream().collect(Collectors.toMap(Function.identity(), 
                NumFrameView::new)));
    }
        
    @Override
    public Frame<K, ? extends NumOrderedFrame<I>> columns() { 
        return Frame.of(keys.stream().collect(Collectors.toMap(Function.identity(), 
                NumOrderedFrameView::new)));
    }
}
