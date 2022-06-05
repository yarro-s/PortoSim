/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package money.portosim.containers;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.NavigableMap;
import java.util.Set;
import java.util.TreeMap;
import money.portosim.QuantifiableSeries;
import money.portosim.containers.core.AlgebraicMap;
import money.portosim.containers.core.Series;

/**
 *
 * @author yarro
 */
public class NumericSeries extends Series<Double> implements AlgebraicMap<Date, Double>, 
        QuantifiableSeries {
    
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

    @Override
    public List<Double> numSeries() {
        return new ArrayList<>(ordered().values());
    }
}
