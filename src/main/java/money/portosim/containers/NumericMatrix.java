/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package money.portosim.containers;

import java.util.AbstractMap;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.function.Function;
import money.portosim.containers.core.AlgebraicMap;
import money.portosim.containers.core.Series;
import money.portosim.QuantifiableMatrix;

/**
 *
 * @author yarro
 */
public class NumericMatrix extends AbstractMap<String, NumericSeries> 
        implements AlgebraicMap<String, NumericSeries> {

    private Map<String, NumericSeries> m;

    @Override
    public Set<Map.Entry<String, NumericSeries>> entrySet() {
        return m.entrySet();
    }

    private class Quant implements QuantifiableMatrix {
        
        @Override
        public Map<String, Double> map(Function<NumericSeries, Double> f) {
            return NumericMatrix.this.map(f);
        }     
    }
    
    public QuantifiableMatrix quant() {
        return new Quant();
    }
    
    public NumericMatrix() { 
        m = new HashMap<>();
    }
    
    public NumericMatrix(Map<String, NumericSeries> m) {
        this.m = new HashMap<>(m);
    }

    public NumericMatrix rolling(int n, Function<SortedMap<Date, Double>, Double> f) {
        return new NumericMatrix(map(s -> s.rolling(n, f)));
    }
    
    public NumericSeries get(String key) {
        return m.get(key);
    }
    
    @Override
    public NumericSeries put(String key, NumericSeries series) {
        return m.put(key, series);
    }
    
    public Series<Quote> transpose() {
        final Series<Quote> quoteSeries = new Series<>();
        Set<Date> timepoints = entrySet().iterator().next().getValue().keySet();
        
        timepoints.forEach(t -> {
            final Quote quote = new Quote();
            entrySet().forEach(e -> {
                var seriesKey = e.getKey();
                var valueAtTimepoint = e.getValue().get(t);
                quote.put(seriesKey, valueAtTimepoint);
            });
            quoteSeries.put(t, quote);
        });
        return quoteSeries;
    }
    
    @Override
    public NumericMatrix add(AlgebraicMap<String, NumericSeries> arg) {
        var res0 = AlgebraicMap.super.add(arg);
        var res = new NumericMatrix(res0);
        return res;
    }

    @Override
    public NumericMatrix mult(AlgebraicMap<String, NumericSeries> arg) {
        return new NumericMatrix(AlgebraicMap.super.mult(arg));
    }   
    
    @Override
    public NumericMatrix div(AlgebraicMap<String, NumericSeries> arg) {
        return new NumericMatrix(AlgebraicMap.super.div(arg));
    }
    
    @Override
    public NumericMatrix sub(AlgebraicMap<String, NumericSeries> arg) {
        return new NumericMatrix(AlgebraicMap.super.sub(arg));
    }     
    
    @Override
    public NumericSeries add(NumericSeries arg1, NumericSeries arg2) {
        return arg1.add(arg2);
    }

    @Override
    public NumericSeries mult(NumericSeries arg1, NumericSeries arg2) {
        return arg1.mult(arg2);
    }

    @Override
    public NumericSeries sub(NumericSeries arg1, NumericSeries arg2) {
        return arg1.sub(arg2);
    }

    @Override
    public NumericSeries div(NumericSeries arg1, NumericSeries arg2) {
        return arg1.div(arg2);
    }
}
