package money.portosim.metrics;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.NavigableMap;
import java.util.function.Function;

/**
 *
 * @author yarro
 */
class QuantBuilder<K extends Comparable> implements Quant<K>, QuantContext<K> {
    
    private int valuesPerRefPeriod = 1;
    private double meanRiskFreeRate = 0.0;
    private NavigableMap<K, Double> sm;
    
    QuantWindow<K> quantGeneral;
    
    QuantBuilder(NavigableMap<K, Double> sm) {
        this.sm = sm;
    }
    
    private QuantBuilder(QuantContext qc, NavigableMap<K, Double> sm) {
        this(sm);
        valuesPerRefPeriod = qc.getValuesPerRefPeriod();
        meanRiskFreeRate = qc.getMeanRiskFreeRate();
    }
    
    @Override
    public QuantSpan full() {
        return new QuantCalcSpan(this);
    }

    @Override
    public Quant<K> rolling(int n) {
        return new QuantWindow<>(this, n);
    }
    
    @Override
    public QuantContext<K> from(K spanStart) {
        return new QuantBuilder(this, sm.tailMap(spanStart, true));
    }
    
    @Override
    public QuantContext<K> to(K spanEnd) {
        return new QuantBuilder(this, sm.headMap(spanEnd, true));
    }
    
    @Override
    public QuantContext<K> range(K spanStart, K spanEnd) {
        return new QuantBuilder(this, sm.subMap(spanStart, true, spanEnd, true));
    } 
    
    @Override
    public QuantContext<K> from(int spanStart) {  
        var spanKStart = new ArrayList<>(sm.keySet()).get(spanStart);
        return from(spanKStart);
    }
    
    @Override
    public QuantContext<K> to(int spanEnd) {
        var spanKEnd = new ArrayList<>(sm.keySet()).get(spanEnd);
        return to(spanKEnd);
    }
    
    @Override
    public QuantContext<K> range(int spanStart, int spanEnd) {
        var keys = new ArrayList<>(sm.keySet());
        return range(keys.get(spanStart), keys.get(spanEnd));
    } 

    @Override
    public QuantContext<K> setValuesPerRefPeriod(int valuesPerRefPeriod) {
        this.valuesPerRefPeriod = valuesPerRefPeriod;
        return this;
    }
    
    @Override
    public int getValuesPerRefPeriod() {
        return valuesPerRefPeriod;
    }

    @Override
    public double getMeanRiskFreeRate() {
        return meanRiskFreeRate;
    }

    @Override
    public QuantContext<K> setMeanRiskFreeRate(double meanRiskFreeRate) {
        this.meanRiskFreeRate = meanRiskFreeRate;
        return this;
    }

    @Override
    public List<Double> values() {
        return entries().stream().map(Entry::getValue).toList();
    }

    @Override
    public List<Entry<K, Double>> entries() {
        return sm.entrySet().stream().toList();
    }

    @Override
    public Map<K, Double> apply(Function<QuantSpan, Double> f) {
        if (quantGeneral == null)
            quantGeneral = new QuantWindow<>(this);
        
        return quantGeneral.apply(f);
    }
}
