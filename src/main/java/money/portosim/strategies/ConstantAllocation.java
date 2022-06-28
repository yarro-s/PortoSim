package money.portosim.strategies;

import money.portosim.Portfolio;
import money.portosim.AbstractStrategy;

import java.util.Date;
import java.util.Map;
import money.portosim.containers.numeric.NumFrame;

public class ConstantAllocation extends AbstractStrategy {
    private final Portfolio portfolio;

    public ConstantAllocation(Map<String, Double> assetAmounts) {
        this.portfolio = new Portfolio(assetAmounts);
    }

    @Override
    public Portfolio apply(Date date, NumFrame<String> prices) {
       return portfolio;
    }

}
