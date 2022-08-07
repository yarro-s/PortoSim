package money.portosim.metrics;

import java.util.List;
import java.util.Map.Entry;

/**
 *
 * @author yarro
 */
public interface QuantContext<K> extends QuantSpanContext {
        
    QuantContext<K> setValuesPerRefPeriod(int valuesPerRefPeriod);

    QuantContext<K> setMeanRiskFreeRate(double meanRiskFreeRate);
    
    List<Entry<K, Double>> entries();
    
    QuantSpan full();

    Quant<K> rolling(int n);
    
    QuantContext<K> range(K spanStart, K spanEnd);
            
    QuantContext<K> range(int spanStart, int spanEnd);
    
    QuantContext<K> from(K spanStart);
    
    QuantContext<K> from(int spanStart);
    
    QuantContext<K> to(K spanEnd);   
        
    QuantContext<K> to(int spanEnd);
}
