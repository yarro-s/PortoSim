/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package money.portosim.containers.numeric;

import java.util.AbstractMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import money.portosim.containers.core.OrderedFrame;

/**
 *
 * @author yarro
 */
class NumSeries<I> extends AbstractMap<I, Double> implements NumOrderedFrame<I> {
    
    final OrderedFrame<I, Double> series;
     
    public NumSeries(Map<I, Double> m) {
        series = NumOrderedFrame.of(m);
    }

    public NumSeries() {
        series = NumOrderedFrame.empty();
    }

    @Override
    public Set<Entry<I, Double>> entrySet() {
        return series.entrySet();
    }

    @Override
    public Double put(I index, Double value) {
        return series.put(index, value);
    }

    @Override
    public <V> OrderedFrame<I, V> rolling(int n, Function<List<Double>, V> f) {
        return series.rolling(n, f);
    }

    @Override
    public OrderedFrame<I, Double> from(I startIdx) {
        return series.from(startIdx);
    }

    @Override
    public OrderedFrame<I, Double> to(I endIdx) {
        return series.to(endIdx);
    }

    @Override
    public Entry<I, Double> firstEntry() {
        return series.firstEntry();
    }

    @Override
    public Entry<I, Double> lastEntry() {
        return series.lastEntry();
    }
}
