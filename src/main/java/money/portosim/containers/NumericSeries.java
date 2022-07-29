package money.portosim.containers;

import java.util.Date;
import java.util.Map;
import java.util.NavigableMap;
import money.portosim.containers.core.AlgebraicMap;
import money.portosim.containers.core.Series;
import money.portosim.Quantifiable;
import money.portosim.TimedQuant;

/**
 *
 * @author yarro
 */
public class NumericSeries extends Series<Double> 
        implements AlgebraicMap<Date, Double> {
    
    public Quantifiable<Date> quant() {
        return new TimedQuant() {
            
            @Override
            public NavigableMap<Date, Double> getNavigableMap() {
                return NumericSeries.this;
            }
        };
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
