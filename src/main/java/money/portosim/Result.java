package money.portosim;

import java.util.*;
import money.portosim.containers.NumericSeries;
import money.portosim.containers.Quote;
import money.portosim.containers.core.Series;

public class Result implements QuantifiableResult {
    private final Series<Portfolio> portfolioHistory;
    private final NumericSeries valueHistory;

    @Override
    public List<Double> numSeries() {
        return new ArrayList<>(getValueHistory().ordered().values());
    }

    Result() {
        this.portfolioHistory = new Series<>();
        this.valueHistory = new NumericSeries();
    }

    void update(Date date, Quote prices, Portfolio portfolio) {
        try {
            portfolioHistory.put(date, (Portfolio) portfolio.clone());
        } catch (CloneNotSupportedException e) {
        }
        valueHistory.put(date, portfolio.valueAtPrice(prices).orElse(0.0));
    }

    public Series<Portfolio> getPortfolioHistory() {
        return portfolioHistory;
    }

    public NumericSeries getValueHistory() {
        return valueHistory;
    }
}
