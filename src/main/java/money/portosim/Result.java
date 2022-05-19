package money.portosim;

import money.portosim.containers.generic.NumericOrderedMap;
import money.portosim.containers.generic.TaggedOrderedMap;
import money.portosim.containers.PriceMap;

import java.util.*;

public class Result implements QuantifiableResult {
    private final TaggedOrderedMap<Date, Portfolio> portfolioHistory;
    private final NumericOrderedMap<Date> valueHistory;

    @Override
    public List<Double> numSeries() {
        return new ArrayList<>(getValueHistory().ordered().values());
    }

    Result() {
        this.portfolioHistory = new TaggedOrderedMap<>();
        this.valueHistory = new NumericOrderedMap<>();
    }

    void update(Date date, PriceMap prices, Portfolio portfolio) {
        try {
            portfolioHistory.put(date, (Portfolio) portfolio.clone());
        } catch (CloneNotSupportedException e) {
        }
        valueHistory.put(date, portfolio.valueAtPrice(prices).orElse(0.0));
    }

    public TaggedOrderedMap<Date, Portfolio> getPortfolioHistory() {
        return portfolioHistory;
    }

    public NumericOrderedMap<Date> getValueHistory() {
        return valueHistory;
    }
}
