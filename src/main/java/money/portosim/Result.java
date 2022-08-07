package money.portosim;

import java.util.*;
import money.portosim.containers.NumericSeries;
import money.portosim.containers.core.Series;
import money.portosim.metrics.Quant;
import money.portosim.metrics.QuantContext;
import money.portosim.metrics.QuantSpan;

public class Result implements QuantContext<Date> {
    private final Map<Date, Portfolio> portfolioHistory;
    private final NavigableMap<Date, Double> valueHistory;
    private final QuantContext<Date> quantContext;
    
    Result() {
        portfolioHistory = new Series<>();
        valueHistory = new NumericSeries();
        quantContext = Quant.of(valueHistory);
    }

    void update(Date date, Map<String, Double> prices, Portfolio portfolio) {
        try {
            portfolioHistory.put(date, (Portfolio) portfolio.clone());
        } catch (CloneNotSupportedException e) {
        }
        valueHistory.put(date, portfolio.valueAtPrice(prices).orElse(0.0));
    }

    public Map<Date, Portfolio> getPortfolioHistory() {
        return portfolioHistory;
    }

    public Map<Date, Double> getValueHistory() {
        return valueHistory;
    }

    @Override
    public QuantContext<Date> setValuesPerRefPeriod(int valuesPerRefPeriod) {
        return quantContext.setValuesPerRefPeriod(valuesPerRefPeriod);
    }

    @Override
    public QuantContext<Date> setMeanRiskFreeRate(double meanRiskFreeRate) {
        return quantContext.setMeanRiskFreeRate(meanRiskFreeRate);
    }

    @Override
    public List<Map.Entry<Date, Double>> entries() {
        return quantContext.entries();
    }

    @Override
    public QuantSpan full() {
        return quantContext.full();
    }

    @Override
    public Quant<Date> rolling(int n) {
        return quantContext.rolling(n);
    }

    @Override
    public QuantContext<Date> range(Date spanStart, Date spanEnd) {
        return quantContext.range(spanStart, spanEnd);
    }

    @Override
    public QuantContext<Date> range(int spanStart, int spanEnd) {
        return quantContext.range(spanStart, spanEnd);
    }

    @Override
    public QuantContext<Date> from(Date spanStart) {
        return quantContext.from(spanStart);
    }

    @Override
    public QuantContext<Date> from(int spanStart) {
        return quantContext.from(spanStart);
    }

    @Override
    public QuantContext<Date> to(Date spanEnd) {
        return quantContext.to(spanEnd);
    }

    @Override
    public QuantContext<Date> to(int spanEnd) {
        return quantContext.to(spanEnd);
    }

    @Override
    public int getValuesPerRefPeriod() {
        return quantContext.getValuesPerRefPeriod();
    }

    @Override
    public double getMeanRiskFreeRate() {
        return quantContext.getMeanRiskFreeRate();
    }

    @Override
    public List<Double> values() {
        return quantContext.values();
    }
}
