package money.portosim.strategies;

import money.portosim.containers.generic.NumericMap;
import money.portosim.containers.PriceMap;
import money.portosim.Portfolio;
import money.portosim.AbstractStrategy;

import java.util.Date;
import java.util.Map;

public class FixedAllocation extends AbstractStrategy {
    private final NumericMap<String> targetAllocation;

    public FixedAllocation(Map<String, Double> targetAllocation) {
        this.targetAllocation = new NumericMap<>(targetAllocation);
    }

    @Override
    public Portfolio apply(Date date, PriceMap prices) {
        var pf = getPortfolio();
        if (pf == null) {
            return rebalancePortfolio(prices, 1000.0);
        } 
        return rebalancePortfolio(prices, pf.valueAtPrice(prices).getAsDouble());
    }
    
    private Portfolio rebalancePortfolio(PriceMap prices, double availableValue) {
        var scaledAllocation = targetAllocation.multiply(availableValue);
        var updatedAmounts = scaledAllocation.divide(prices);

        return new Portfolio(updatedAmounts);
    }
}


