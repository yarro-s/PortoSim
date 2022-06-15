/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package money.portosim.containers;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.function.Function;
import money.portosim.QuantifiableSeries;
import money.portosim.containers.core.AlgebraicMap;
import money.portosim.containers.core.Series;

/**
 *
 * @author yarro
 */
public class NumericSeries extends Series<Double> implements AlgebraicMap<Date, Double> {

    @Override
    public NumericSeries rolling(int n, Function<SortedMap<Date, Double>, Double> f) {
        return new NumericSeries(super.rolling(n, f)); 
    }
     
    private class Quant implements QuantifiableSeries {

        @Override
        public List<Double> asList() {
            return new ArrayList<>(NumericSeries.this.values());
        }
        
    }
    
    public QuantifiableSeries quant() {
        return new Quant();
    }
    
    public NumericSeries() { 
        super();
    }
    
    public NumericSeries(Map<Date, Double> m) {
        super(m);
    }

    @Override
    public Double add(Double arg1, Double arg2) {
        return arg1 + arg2;
    }

    @Override
    public Double mult(Double arg1, Double arg2) {
        return arg1 * arg2;
    }

    @Override
    public Double sub(Double arg1, Double arg2) {
        return arg1 - arg2;
    }

    @Override
    public Double div(Double arg1, Double arg2) {
        return arg1 / arg2;
    }
    
    @Override
    public NumericSeries add(AlgebraicMap<Date, Double> arg) {
        return new NumericSeries(AlgebraicMap.super.add(arg));
    }

    @Override
    public NumericSeries mult(AlgebraicMap<Date, Double> arg) {
        return new NumericSeries(AlgebraicMap.super.mult(arg));
    }   
    
    @Override
    public NumericSeries div(AlgebraicMap<Date, Double> arg) {
        return new NumericSeries(AlgebraicMap.super.div(arg));
    }
    
    @Override
    public NumericSeries sub(AlgebraicMap<Date, Double> arg) {
        return new NumericSeries(AlgebraicMap.super.sub(arg));
    }
}
