package money.portosim.strategies;

import money.portosim.containers.NumericMap;
import money.portosim.Portfolio;
import money.portosim.AbstractStrategy;

import java.util.Date;
import java.util.Map;
import money.portosim.containers.numeric.NumFrame;
import money.portosim.containers.numeric.NumRecord;

public class FixedAllocation extends AbstractStrategy {
    private final NumericMap<String> targetAllocation;

    public FixedAllocation(Map<String, Double> targetAllocation) {
        this.targetAllocation = new NumericMap<>(targetAllocation);
    }

    @Override
    public Portfolio apply(Date date, NumFrame<String> prices) {
        var pf = getPortfolio();
        if (pf == null) {
            return rebalancePortfolio(prices, 1000.0);
        } 
        return rebalancePortfolio(prices, pf.valueAtPrice(prices).getAsDouble());
    }
    
    private Portfolio rebalancePortfolio(NumFrame<String> prices, double availableValue) {
        var scaledAllocation = new NumRecord<String>(targetAllocation.mult(availableValue));
        var updatedAmounts = scaledAllocation.div(prices);

        return new Portfolio(updatedAmounts);
    }
}


