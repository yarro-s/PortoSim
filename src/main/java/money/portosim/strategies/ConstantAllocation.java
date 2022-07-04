package money.portosim.strategies;

import money.portosim.Portfolio;
import money.portosim.AbstractStrategy;

import java.util.Date;
import java.util.Map;

public class ConstantAllocation extends AbstractStrategy {
    private final Portfolio portfolio;

    public ConstantAllocation(Map<String, Double> assetAmounts) {
        this.portfolio = new Portfolio(assetAmounts);
    }

    @Override
    public Portfolio apply(Date date, Map<String, Double> prices) {
       return portfolio;
    }

}
