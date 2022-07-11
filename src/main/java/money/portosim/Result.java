package money.portosim;

import java.util.*;
import money.portosim.containers.NumericSeries;
import money.portosim.containers.core.Series;

public class Result {
    private final Map<Date, Portfolio> portfolioHistory;
    private final Map<Date, Double> valueHistory;

    public Quantifiable quant() {
        return new NumericSeries(valueHistory).quant();
    }

    Result() {
        this.portfolioHistory = new Series<>();
        this.valueHistory = new NumericSeries();
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
}
