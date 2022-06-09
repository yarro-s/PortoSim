package money.portosim;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import money.portosim.containers.Quote;
import money.portosim.containers.QuoteSeries;
import money.portosim.containers.core.Series;

public class Metrics {
    
    public static Quote volatility(QuoteSeries quoteSeries) {
        return quoteSeries.mapSeries(Metrics::volatility);
    }
    
    public static Quote average(QuoteSeries quoteSeries) {    
        return quoteSeries.mapSeries(Metrics::average);
    }
    
    public static Quote cummulativeGrowthRate(QuoteSeries quoteSeries) {
        return quoteSeries.mapSeries(Metrics::cummulativeGrowthRate); 
    }
    
    public static Quote totalReturn(QuoteSeries quoteSeries) {
        return quoteSeries.mapSeries(Metrics::totalReturn);
    }
    
    public static double volatility(Series<Double> series) {
        return volatility(series.values());
    }
    
    public static double average(Series<Double> series) {
        return average(series.values()); 
    }
    
    public static double cummulativeGrowthRate(Series<Double> series) {
        return cummulativeGrowthRate(new ArrayList<>(series.values())); 
    }
        
    public static double totalReturn(Series<Double> series) {
        return totalReturn(new ArrayList<>(series.values()));
    }

    public static double volatility(Collection<Double> values) {
        final double nValues = values.size();
        final double averageValue = average(values);

        var squareDiffs = values.stream().mapToDouble(v ->
                Math.pow(v - averageValue, 2.0));
        return Math.sqrt(squareDiffs.sum() / nValues);
    }

    public static double average(Collection<Double> values) {
        return values.stream().mapToDouble(Double::doubleValue).average().orElseThrow();
    }

    public static double cummulativeGrowthRate(List<Double> values) {
        var totalReturn = totalReturn(values);

        final double nTimePeriods = values.size();
        return Math.pow(totalReturn, 1.0 / nTimePeriods) - 1.0;
    }

    public static double totalReturn(List<Double> values) {
        final double valueAtStart = values.get(0),
                valueAtEnd = values.get(values.size() - 1);

        return valueAtEnd / valueAtStart;
    }

    private Metrics() {
    }
}
