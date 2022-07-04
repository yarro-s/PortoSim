package money.portosim.strategies;

import money.portosim.containers.NumericMap;
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
    public Portfolio apply(Date date, Map<String, Double> prices) {
        var pf = getPortfolio();
        var quote = NumericMap.of(prices);
        if (pf == null) {
            return rebalancePortfolio(quote, 1000.0);
        } 
        return rebalancePortfolio(quote, pf.valueAtPrice(quote).getAsDouble());
    }
    
    private Portfolio rebalancePortfolio(NumericMap<String> prices, double availableValue) {
        var scaledAllocation = new NumericMap<String>(targetAllocation.mult(availableValue));
        var updatedAmounts = scaledAllocation.div(prices);

        return new Portfolio(updatedAmounts);
    }
}


