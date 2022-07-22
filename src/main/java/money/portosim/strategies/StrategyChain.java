package money.portosim.strategies;

import java.util.Date;
import java.util.Map;
import money.portosim.AbstractStrategy;
import money.portosim.Portfolio;
import money.portosim.Strategy;

/**
 *
 * @author yarro
 */
public abstract class StrategyChain extends AbstractStrategy {
    private Strategy nextStrategy;
    
    public Portfolio runNextStrategy(Date date, Map<String, Double> prices) {
       return nextStrategy.makePortfolio(date, prices);
    }

    public AbstractStrategy chainTo(Strategy nextStrategy) {
        this.nextStrategy = nextStrategy;
        return this;
    }
}
