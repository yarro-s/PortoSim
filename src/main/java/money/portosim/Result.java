package money.portosim;

import java.util.*;
import money.portosim.containers.NumericSeries;
import money.portosim.containers.core.Series;
import money.portosim.containers.numeric.NumFrame;

public class Result {
    private final Series<Portfolio> portfolioHistory;
    private final NumericSeries valueHistory;

    public QuantifiableSeries quant() {
        return valueHistory.quant();
    }

    Result() {
        this.portfolioHistory = new Series<>();
        this.valueHistory = new NumericSeries();
    }

    void update(Date date, NumFrame<String> prices, Portfolio portfolio) {
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
