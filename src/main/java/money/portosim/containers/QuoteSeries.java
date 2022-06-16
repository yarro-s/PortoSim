/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package money.portosim.containers;

import java.util.Date;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.function.Function;
import java.util.stream.Collectors;
import money.portosim.Quantifiable;
import money.portosim.containers.core.AlgebraicMap;
import money.portosim.containers.core.Series;

/**
 *
 * @author yarro
 */
public class QuoteSeries extends Series<Quote> implements AlgebraicMap<Date, Quote> {
      
    public SeriesQuote transpose() {
        final SeriesQuote quoteSeries = new SeriesQuote();
        Set<String> seriesKeys = entrySet().iterator().next().getValue().keySet();
        
        seriesKeys.forEach(k -> {
            final NumericSeries series = new NumericSeries();
            entrySet().forEach(e -> {
                var timestamp = e.getKey();
                var valueAtTimepoint = e.getValue().get(k);
                series.put(timestamp, valueAtTimepoint);
            });
            quoteSeries.put(k, series);
        });
        return quoteSeries;
    }

    private class Quant implements Quantifiable {
        
        @Override
        public Quote seriesMap(Function<NumericSeries, Double> f) {
            return QuoteSeries.this.mapSeries(f);
        }     
    }
    
    public Quantifiable quant() {
        return new Quant();
    }
    
    public QuoteSeries() { 
        super();
    }
    
    public QuoteSeries(Map<Date, Quote> m) {
        super(m);
    }

    @Override
    public QuoteSeries rolling(int n, Function<SortedMap<Date, Quote>, Quote> f) {
        return new QuoteSeries(super.rolling(n, f)); 
    }
    
    public NumericSeries getSeries(String seriesKey) {
        final NumericSeries series = new NumericSeries();
        
        keySet().forEach(k -> {
            var val = get(k).get(seriesKey);
            
            if (val != null) {
                series.put(k, val);
            }         
        });
        
        return series;
    }
    
    public Series<Double> putSeries(String seriesKey, NumericSeries series) {
        series.entrySet().forEach(tv -> {
            put(tv.getKey(), new Quote(Map.of(seriesKey, tv.getValue())));
        });
        
        return getSeries(seriesKey);
    }
    
    public Quote mapSeries(Function<NumericSeries, Double> f) {
        var seriesKeys = firstEntry().getValue().keySet();     
        return new Quote(seriesKeys.stream().collect(Collectors.toMap(Function.identity(),
                sk -> f.apply(getSeries(sk)))));
    }
    
    @Override
    public QuoteSeries add(AlgebraicMap<Date, Quote> arg) {
        return new QuoteSeries(AlgebraicMap.super.add(arg));
    }

    @Override
    public QuoteSeries mult(AlgebraicMap<Date, Quote> arg) {
        return new QuoteSeries(AlgebraicMap.super.mult(arg));
    }   
    
    @Override
    public QuoteSeries div(AlgebraicMap<Date, Quote> arg) {
        return new QuoteSeries(AlgebraicMap.super.div(arg));
    }
    
    @Override
    public QuoteSeries sub(AlgebraicMap<Date, Quote> arg) {
        return new QuoteSeries(AlgebraicMap.super.sub(arg));
    }
          
    @Override
    public Quote add(Quote arg1, Quote arg2) {
        return arg1.add(arg2);
    }

    @Override
    public Quote mult(Quote arg1, Quote arg2) {
        return arg1.mult(arg2);
    }

    @Override
    public Quote sub(Quote arg1, Quote arg2) {
        return arg1.sub(arg2);
    }

    @Override
    public Quote div(Quote arg1, Quote arg2) {
        return arg1.div(arg2);
    }
}