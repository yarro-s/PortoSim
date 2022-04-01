package money.portosim.strategies;

import money.portosim.containers.generic.NumericMap;
import money.portosim.containers.PriceMap;
import money.portosim.Portfolio;
import money.portosim.Strategy;

import java.util.Date;
import java.util.Map;
import java.util.Optional;

public class FixedAllocation extends Strategy {
    private Optional<Portfolio> portfolio;
    private final NumericMap<String> targetAllocation;

    public FixedAllocation(Map<String, Double> targetAllocation) {
        this.targetAllocation = new NumericMap<>(targetAllocation);
        portfolio = Optional.empty();
    }

    @Override
    public Portfolio apply(Date date, PriceMap prices) {
        portfolio.ifPresentOrElse(currentPortfolio -> rebalancePortfolio(currentPortfolio, prices),
                () -> updatePortfolio(prices, 1000.0));

        return portfolio.get();
    }

    private void updatePortfolio(PriceMap prices, double availableValue) {
        var scaledAllocation = targetAllocation.multiply(availableValue);
        var updatedAmounts = scaledAllocation.divide(prices);

        portfolio = Optional.of(new Portfolio(updatedAmounts));
    }

    private void rebalancePortfolio(Portfolio currentPortfolio, PriceMap updatedPrices) {
        var pfValue = currentPortfolio.valueAtPrice(updatedPrices);

        pfValue.ifPresent(updatedValue -> {
            updatePortfolio(updatedPrices, updatedValue);
        });
    }
}
