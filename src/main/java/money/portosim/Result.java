package money.portosim;

import java.util.*;
import money.portosim.containers.NumericSeries;
import money.portosim.containers.core.Series;

public class Result implements Quantifiable<Date> {
    private final Map<Date, Portfolio> portfolioHistory;
    private final NavigableMap<Date, Double> valueHistory;

    @Override
    public NavigableMap<Date, Double> getNavigableMap() {
        return valueHistory;
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
